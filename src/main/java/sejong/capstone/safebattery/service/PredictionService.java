package sejong.capstone.safebattery.service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sejong.capstone.safebattery.domain.*;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.ai.*;
import sejong.capstone.safebattery.enums.PredictionState;
import sejong.capstone.safebattery.exception.AiServerException;
import sejong.capstone.safebattery.repository.PowerPredictionRepository;
import sejong.capstone.safebattery.repository.TemperaturePredictionRepository;
import sejong.capstone.safebattery.repository.VoltagePredictionRepository;

import static sejong.capstone.safebattery.enums.PredictionState.*;
import static sejong.capstone.safebattery.util.StatePolicy.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

    private final VoltagePredictionRepository voltagePredictionRepository;
    private final PowerPredictionRepository powerPredictionRepository;
    private final TemperaturePredictionRepository temperaturePredictionRepository;
    private final WebClient AIServerWebClient;
    private final PemfcService pemfcService;
    private final DynamaskService dynamaskService;
    private final String PREDICTION_URL = "/predict";
    private final String VOLTAGE_AND_POWER_MODEL_TYPE = "PWU";
    private final String TEMPERATURE_MODEL_TYPE = "T3";

    /**
     * 600개의 record를 ai서버로 전송하여 prediction을 획득. 획득한 prediction은 데이터베이스에 저장한다.
     */

    public void createPredictionsAndChangeState(List<Record> records) {
        // 1. 정보 추출
        List<VoltageAndPowerFeature> voltageAndPowerFeatures
            = this.extractVoltageAndPowerFeaturesFromRecords(records);
        List<TemperatureFeature> temperatureFeatures
            = this.extractTemperatureFeaturesFromRecords(records);

        // 2. 요청
        PredictionRequestDto<VoltageAndPowerFeature> requestDto1 = new PredictionRequestDto<>(
                VOLTAGE_AND_POWER_MODEL_TYPE,
                voltageAndPowerFeatures);
        PredictionRequestDto<TemperatureFeature> requestDto2 = new PredictionRequestDto<>(
                TEMPERATURE_MODEL_TYPE,
                temperatureFeatures);

        Record record = records.get(0);

        //voltage, power : 응답 받기
        VoltageAndPowerResponseDto voltageAndPowerResponseDto =
            this.requestVoltageAndPowerPredictionToAIServer(requestDto1);
        // prediction 저장. 여기서 Pemfc의 State 수정도 같이 이루어짐
        this.saveVoltageAndPowerPredictionsAndChangeState(
                voltageAndPowerResponseDto, record);
        // dynamask가 있으면 db에 저장
        addVoltagePowerDynamaskIfPresent(voltageAndPowerResponseDto, record);

        if (record.getRecordNumber() % 5 == 0) {
            //temp : 응답 받기
            TemperaturePredictionResponseDto temperaturePredictionResponseDto =
                    this.requestTemperaturePredictionToAIServer(requestDto2);
            // prediction 저장. 여기서 Pemfc의 State 수정도 같이 이루어짐
            this.saveTemperaturePredictionsAndChangeState(
                    temperaturePredictionResponseDto, record);
            // dynamask가 있으면 db에 저장
            addTemperatureDynamaskIfPresent(temperaturePredictionResponseDto, record);
        }
    }

    private List<VoltageAndPowerFeature> extractVoltageAndPowerFeaturesFromRecords(
        List<Record> records) {
        return IntStream.range(0, 600).mapToObj(i -> {
            Record current = records.get(i);
            double iA_diff = (i > 0) ? current.getIA() - records.get(i - 1).getIA() : 0;
            return VoltageAndPowerFeature.fromEntity(current, iA_diff);
        }).collect(Collectors.toList());
    }

    private List<TemperatureFeature> extractTemperatureFeaturesFromRecords(List<Record> records) {
        return IntStream.range(0, 600)
                .map(i -> 5 * i)  // 0, 5, 10, ..., 2995
                .filter(i -> i < records.size())  // index 초과 방지
                .mapToObj(i -> TemperatureFeature.fromEntity(records.get(i)))
                .toList();
    }


    /**
     * 전압(U_totV)과 전력(PW)에 대한 예측값 요청
     */
    private VoltageAndPowerResponseDto requestVoltageAndPowerPredictionToAIServer(
        PredictionRequestDto<? extends ModelFeature> requestDto) {
        try {
            return AIServerWebClient.post().uri(PREDICTION_URL).bodyValue(requestDto)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        // 성공 응답 처리
                        return response.bodyToMono(VoltageAndPowerResponseDto.class);
                    } else {
                        // 에러 응답의 본문을 String으로 읽어서 로그로 남기고 예외 던지기
                        return response.bodyToMono(String.class).flatMap(body -> {
                            log.error("AI 서버 에러 (status: {}): {}", response.statusCode(), body);
                            return Mono.error(
                                new RuntimeException(
                                    "AI 서버 요청 실패: status=" + response.statusCode()));
                        });
                    }
                }).block();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AiServerException("AI 서버에 전압/전력 예측값 요청 중 예외 발생", e);
        }
    }

    /**
     * 온도(T3)에 대한 예측값 요청
     */
    private TemperaturePredictionResponseDto requestTemperaturePredictionToAIServer(
        PredictionRequestDto<? extends ModelFeature>  requestDto) {
        try {
            return AIServerWebClient.post().uri(PREDICTION_URL).bodyValue(requestDto)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        // 성공 응답 처리
                        return response.bodyToMono(TemperaturePredictionResponseDto.class);
                    } else {
                        // 에러 응답의 본문을 String으로 읽어서 로그로 남기고 예외 던지기
                        return response.bodyToMono(String.class).flatMap(body -> {
                            log.error("AI 서버 에러 (status: {}): {}", response.statusCode(), body);
                            return Mono.error(
                                new RuntimeException(
                                    "AI 서버 요청 실패: status=" + response.statusCode()));
                        });
                    }
                }).block();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AiServerException("AI 서버에 온도 예측값 요청 중 예외 발생", e);
        }
    }

    private void saveVoltageAndPowerPredictionsAndChangeState(
        VoltageAndPowerResponseDto voltageAndPowerResponseDto,
        Record record) {
        PredictionState voltagePredictionState = this.classifyVoltagePredictionByValueAndChangeState(
            voltageAndPowerResponseDto.getVoltagePrediction(), record);
        PredictionState powerPredictionState = this.classifyPowerPredictionByValueAndChangeState(
            voltageAndPowerResponseDto.getPowerPrediction(), record);

        voltagePredictionRepository.save(
            voltageAndPowerResponseDto.toVoltagePrediction(record.getPemfc(), record.getTsec(),
                voltagePredictionState));
        powerPredictionRepository.save(
            voltageAndPowerResponseDto.toPowerPrediction(record.getPemfc(), record.getTsec(),
                powerPredictionState));
    }

    private void saveTemperaturePredictionsAndChangeState(
            TemperaturePredictionResponseDto temperaturePredictionResponseDto,
            Record record) {
        PredictionState temperaturePredictionState = this.classifyTemperaturePredictionByValueAndChangeState(
                temperaturePredictionResponseDto.getTemperaturePrediction(), record);
        temperaturePredictionRepository.save(
                temperaturePredictionResponseDto.toEntity(record.getPemfc(), record.getTsec(),
                        temperaturePredictionState));
    }

    private PredictionState classifyVoltagePredictionByValueAndChangeState(
        double voltagePrediction, Record record) {
        Pemfc pemfc = record.getPemfc();
        if (isNormal(record.getVoltageState())) { // record == NORMAL
            if (isNormalVoltage(voltagePrediction)) { // prediction == NORMAL
                if (!isNormal(pemfc.getVoltageState())) // pemfc != NORMAL
                    solveNNProblem(pemfc, voltagePredictionRepository::findTop100ByPemfcOrderByTsecDesc, VoltagePrediction::getState, state -> pemfcService.updatePemfcVoltageStateById(pemfc.getId(), state));
                return NORMAL;
            } else { // prediction == ERROR
                if (!isWarning(pemfc.getVoltageState())) // pemfc != WARNING
                    pemfcService.updatePemfcVoltageStateById(pemfc.getId(), WARNING);
                return ERROR;
            }
        } else { // record == ERROR
            if (isNormalVoltage(voltagePrediction)) { // prediction == NORMAL
                if (!isError(pemfc.getVoltageState())) // pemfc != ERROR
                    pemfcService.updatePemfcVoltageStateById(pemfc.getId(), ERROR);
                return NORMAL;
            } else { // prediction == ERROR
                if (!isError(pemfc.getVoltageState())) // pemfc != ERROR
                    pemfcService.updatePemfcVoltageStateById(pemfc.getId(), ERROR);
                return ERROR;
            }
        }
    }

    private PredictionState classifyPowerPredictionByValueAndChangeState(
            double powerPrediction, Record record) {
        Pemfc pemfc = record.getPemfc();
        if (isNormal(record.getPowerState())) { // record == NORMAL
            if (isNormalPower(powerPrediction)) { // prediction == NORMAL
                if (!isNormal(pemfc.getPowerState())) // pemfc != NORMAL
                    solveNNProblem(pemfc, powerPredictionRepository::findTop100ByPemfcOrderByTsecDesc, PowerPrediction::getState, state -> pemfcService.updatePemfcPowerStateById(pemfc.getId(), state));
                return NORMAL;
            } else { // prediction == ERROR
                if (!isWarning(pemfc.getPowerState())) // pemfc != WARNING
                    pemfcService.updatePemfcPowerStateById(pemfc.getId(), WARNING);
                return ERROR;
            }
        } else { // record == ERROR
            if (isNormalPower(powerPrediction)) { // prediction == NORMAL
                if (!isError(pemfc.getPowerState())) // pemfc != ERROR
                    pemfcService.updatePemfcPowerStateById(pemfc.getId(), ERROR);
                return NORMAL;
            } else { // prediction == ERROR
                if (!isError(pemfc.getPowerState())) // pemfc != ERROR
                    pemfcService.updatePemfcPowerStateById(pemfc.getId(), ERROR);
                return ERROR;
            }
        }
    }

    private PredictionState classifyTemperaturePredictionByValueAndChangeState(
        double temperaturePrediction, Record record) {
        Pemfc pemfc = record.getPemfc();
        if (isNormal(record.getTemperatureState())) { // record == NORMAL
            if (isNormalTemperature(temperaturePrediction)) { // prediction == NORMAL
                if (!isNormal(pemfc.getTemperatureState())) // pemfc != NORMAL
                    solveNNProblem(pemfc, temperaturePredictionRepository::findTop500ByPemfcOrderByTsecDesc, TemperaturePrediction::getState, state -> pemfcService.updatePemfcTemperatureStateById(pemfc.getId(), state));
                return NORMAL;
            } else { // prediction == ERROR
                if (!isWarning(pemfc.getTemperatureState())) // pemfc != WARNING
                    pemfcService.updatePemfcTemperatureStateById(pemfc.getId(), WARNING);
                return ERROR;
            }
        } else { // record == ERROR
            if (isNormalTemperature(temperaturePrediction)) { // prediction == NORMAL
                if (!isError(pemfc.getTemperatureState())) // pemfc != ERROR
                    pemfcService.updatePemfcTemperatureStateById(pemfc.getId(), ERROR);
                return NORMAL;
            } else { // prediction == ERROR
                if (!isError(pemfc.getTemperatureState())) // pemfc != ERROR
                    pemfcService.updatePemfcTemperatureStateById(pemfc.getId(), ERROR);
                return ERROR;
            }
        }
    }

    private <T> void solveNNProblem(
            Pemfc pemfc,
            Function<Pemfc, List<T>> predictionListGetter,
            Function<T, PredictionState> stateGetter,
            Consumer<PredictionState> stateUpdater) {
        List<T> predictions = predictionListGetter.apply(pemfc);
        PredictionState result = predictions.stream()
                .anyMatch(pred -> stateGetter.apply(pred) != NORMAL) ? WARNING : NORMAL;
        stateUpdater.accept(result);
    }

    public List<VoltagePrediction> getVoltagePredictions(long pemfcId) {
        return voltagePredictionRepository.findAllByPemfcId(pemfcId);
    }

    public List<PowerPrediction> getPowerPredictions(long pemfcId) {
        return powerPredictionRepository.findAllByPemfcId(pemfcId);
    }

    public List<TemperaturePrediction> getTemperaturePredictions(long pemfcId) {
        return temperaturePredictionRepository.findAllByPemfcId(pemfcId);
    }

    private void addVoltagePowerDynamaskIfPresent(VoltageAndPowerResponseDto response, Record record) {
        if (response.mask() != null) {
            Pemfc pemfc = record.getPemfc();
            VoltagePowerDynamask dynamask = VoltagePowerDynamask.builder()
                    .tsec(record.getTsec())
                    .pemfc(pemfc)
                    .value(response.mask()).build();
            dynamaskService.addNewVoltagePowerDynamask(dynamask);
        }
    }

    private void addTemperatureDynamaskIfPresent(TemperaturePredictionResponseDto response, Record record) {
        if (response.mask() != null) {
            Pemfc pemfc = record.getPemfc();
            TemperatureDynamask dynamask = TemperatureDynamask.builder()
                    .tsec(record.getTsec())
                    .pemfc(pemfc)
                    .value(response.mask()).build();
            dynamaskService.addNewTemperatureDynamask(dynamask);
        }
    }
}

//private void solveVoltageNNProblem(Pemfc pemfc) {
//    List<VoltagePrediction> predictions =
//            voltagePredictionRepository.findTop100ByPemfcOrderByTsecDesc(pemfc);
//    PredictionState result = predictions.stream()
//            .anyMatch(pred -> pred.getState() != NORMAL) ?
//            WARNING : NORMAL;
//    pemfcService.updatePemfcVoltageStateById(pemfc.getId(), result);
//}
//
//private void solvePowerNNProblem(Pemfc pemfc) {
//    List<PowerPrediction> predictions =
//            powerPredictionRepository.findTop100ByPemfcOrderByTsecDesc(pemfc);
//    PredictionState result = predictions.stream()
//            .anyMatch(pred -> pred.getState() != NORMAL) ?
//            WARNING : NORMAL;
//    pemfcService.updatePemfcPowerStateById(pemfc.getId(), result);
//}
//
//private void solveTemperatureNNProblem(Pemfc pemfc) {
//    List<TemperaturePrediction> predictions =
//            temperaturePredictionRepository.findTop100ByPemfcOrderByTsecDesc(pemfc);
//    PredictionState result = predictions.stream()
//            .anyMatch(pred -> pred.getState() != NORMAL) ?
//            WARNING : NORMAL;
//    pemfcService.updatePemfcTemperatureStateById(pemfc.getId(), result);
//}
