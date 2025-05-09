package sejong.capstone.safebattery.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sejong.capstone.safebattery.Constants;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.enums.PredictionState;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.TemperatureFeature;
import sejong.capstone.safebattery.dto.TemperaturePredictionRequestDto;
import sejong.capstone.safebattery.dto.TemperaturePredictionResponseDto;
import sejong.capstone.safebattery.dto.VoltageAndPowerFeature;
import sejong.capstone.safebattery.dto.VoltageAndPowerRequestDto;
import sejong.capstone.safebattery.dto.VoltageAndPowerResponseDto;
import sejong.capstone.safebattery.repository.PowerPredictionRepository;
import sejong.capstone.safebattery.repository.TemperaturePredictionRepository;
import sejong.capstone.safebattery.repository.VoltagePredictionRepository;

import static sejong.capstone.safebattery.enums.PredictionState.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

    private final VoltagePredictionRepository voltagePredictionRepository;
    private final PowerPredictionRepository powerPredictionRepository;
    private final TemperaturePredictionRepository temperaturePredictionRepository;
    private final WebClient AIServerWebClient;
    private final String VoltageAndPowerPredictionUrl = "/predict_and_explain/UtotV_and_PW";
    private final String TemperaturePredictionUrl = "/predict_and_explain/T3";

    /**
     * 600개의 record를 ai서버로 전송하여 prediction을 획득. 획득한 prediction은 데이터베이스에 저장한다.
     */

    public void createPredictions(List<Record> records) {
        // 1. 정보 추출
        List<VoltageAndPowerFeature> voltageAndPowerFeatures
                = this.extractVoltageAndPowerFeaturesFromRecords(records);
        List<TemperatureFeature> temperatureFeatures
                = this.extractTemperatureFeaturesFromRecords(records);

        // 2. 요청
        // todo:
        //  1. threshold값을 어떤 기준으로 정할지 논의가 필요함.
        //  2. 요청이 실패하는 경우에 대한 예외처리 필요함.
        VoltageAndPowerRequestDto requestDto1 = new VoltageAndPowerRequestDto(
            voltageAndPowerFeatures, new double[]{0.0, 1.0});
        VoltageAndPowerResponseDto voltageAndPowerResponseDto = this.requestVoltageAndPowerPredictionToAIServer(
            requestDto1);

        TemperaturePredictionRequestDto requestDto2 = new TemperaturePredictionRequestDto(
            temperatureFeatures, new double[]{0.0});
        TemperaturePredictionResponseDto temperaturePredictionResponseDto = this.requestTemperaturePredictionToAIServer(
            requestDto2);

        // 3. 결과 저장
        // todo: 예측값을 보고 PredictionState를 정하는 로직이 필요함.
        Record record = records.get(0);
        this.savePredictions(voltageAndPowerResponseDto, temperaturePredictionResponseDto, record);
    }

    private List<VoltageAndPowerFeature> extractVoltageAndPowerFeaturesFromRecords(
        List<Record> records) {
        return IntStream.range(0, records.size()).mapToObj(i -> {
            Record current = records.get(i);
            double iA_diff = (i > 0) ? current.getIA() - records.get(i - 1).getIA() : 0;
            return VoltageAndPowerFeature.fromEntity(current, iA_diff);
        }).collect(Collectors.toList());
    }

    private List<TemperatureFeature> extractTemperatureFeaturesFromRecords(List<Record> records) {
        return records.stream().map(TemperatureFeature::fromEntity).toList();
    }

    /**
     * 전압(U_totV)과 전력(PW)에 대한 예측값 요청
     */
    private VoltageAndPowerResponseDto requestVoltageAndPowerPredictionToAIServer(
        VoltageAndPowerRequestDto requestDto) {
        return AIServerWebClient.post().uri(VoltageAndPowerPredictionUrl).bodyValue(requestDto)
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    // 성공 응답 처리
                    return response.bodyToMono(VoltageAndPowerResponseDto.class);
                } else {
                    // 에러 응답의 본문을 String으로 읽어서 로그로 남기고 예외 던지기
                    return response.bodyToMono(String.class).flatMap(body -> {
                        log.error("AI 서버 에러 (status: {}): {}", response.statusCode(), body);
                        return Mono.error(
                            new RuntimeException("AI 서버 요청 실패: status=" + response.statusCode()));
                    });
                }
            }).block();
    }

    /**
     * 온도(T3)에 대한 예측값 요청
     */
    private TemperaturePredictionResponseDto requestTemperaturePredictionToAIServer(
        TemperaturePredictionRequestDto requestDto) {
        return AIServerWebClient.post().uri(TemperaturePredictionUrl).bodyValue(requestDto)
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    // 성공 응답 처리
                    return response.bodyToMono(TemperaturePredictionResponseDto.class);
                } else {
                    // 에러 응답의 본문을 String으로 읽어서 로그로 남기고 예외 던지기
                    return response.bodyToMono(String.class).flatMap(body -> {
                        log.error("AI 서버 에러 (status: {}): {}", response.statusCode(), body);
                        return Mono.error(
                            new RuntimeException("AI 서버 요청 실패: status=" + response.statusCode()));
                    });
                }
            }).block();
    }

    private void savePredictions(VoltageAndPowerResponseDto voltageAndPowerResponseDto,
        TemperaturePredictionResponseDto temperaturePredictionResponseDto, Record record) {
        PredictionState voltagePredictionState = this.classifyVoltagePredictionByValue(
            voltageAndPowerResponseDto.getVoltagePrediction(), record);
        PredictionState powerPredictionState = this.classifyPowerPredictionByValue(
            voltageAndPowerResponseDto.getPowerPrediction());
        PredictionState temperaturePredictionState = this.classifyTemperaturePredictionByValue(
            temperaturePredictionResponseDto.getTemperaturePrediction());

        voltagePredictionRepository.save(
            voltageAndPowerResponseDto.toVoltagePrediction(record.getPemfc(), record.getTsec(),
                voltagePredictionState));
        powerPredictionRepository.save(
            voltageAndPowerResponseDto.toPowerPrediction(record.getPemfc(), record.getTsec(),
                powerPredictionState));
        temperaturePredictionRepository.save(
            temperaturePredictionResponseDto.toEntity(record.getPemfc(), record.getTsec(),
                temperaturePredictionState));
    }

    private PredictionState classifyVoltagePredictionByValue(double voltagePrediction, Record record) {
        // todo: 예측 전압값을 보고 고장 상태를 정하는 로직이 필요함.
        if (isNormalVoltage(voltagePrediction)) {
            return NORMAL;
        }
        else {
            Pemfc pemfc = record.getPemfc();
            if (pemfc.getState() == NORMAL) {
                //  todo: pemfc의 상태를 바꿔 주는 로직 필요
                return WARNING;
            }
            else{
                //  todo: pemfc의 상태를 바꿔 주는 로직 필요
                return ERROR;}
        }
    }

    private PredictionState classifyPowerPredictionByValue(double powerPrediction) {
        // todo: 예측 전력값을 보고 고장 상태를 정하는 로직이 필요함.
        return NORMAL;
    }

    private PredictionState classifyTemperaturePredictionByValue(double temperaturePrediction) {
        // todo: 예측 온도값을 보고 고장 상태를 정하는 로직이 필요함.
        return NORMAL;
    }

    private boolean isNormalVoltage(double voltagePrediction) {
        return voltagePrediction > Constants.PREDICTION_VOLTAGE_LOWER_BOUND
                && voltagePrediction < Constants.PREDICTION_VOLTAGE_UPPER_BOUND;
    }
}
