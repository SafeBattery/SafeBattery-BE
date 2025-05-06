package sejong.capstone.safebattery.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sejong.capstone.safebattery.domain.PredictionState;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.VoltageAndPowerFeature;
import sejong.capstone.safebattery.dto.VoltageAndPowerRequestDto;
import sejong.capstone.safebattery.dto.VoltageAndPowerResponseDto;
import sejong.capstone.safebattery.repository.PowerPredictionRepository;
import sejong.capstone.safebattery.repository.VoltagePredictionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

    private final VoltagePredictionRepository voltagePredictionRepository;
    private final PowerPredictionRepository powerPredictionRepository;
    private final WebClient AIServerWebClient;

    /**
     * 600개의 record를 ai서버로 전송하여 prediction을 획득. 획득한 prediction은 데이터베이스에 저장한다.
     */

    public void createPredictions(List<Record> records) {
        // 1. 정보 추출
        List<VoltageAndPowerFeature> voltageAndPowerFeatures = this.extractFeaturesFromRecords(
            records);
        // todo: 온도 예측에 필요한 정보만 추출하여 dto로 만들기

        // 2. 요청
        // todo:
        //  1. threshold값을 어떤 기준으로 정할지 논의가 필요함.
        //  2. 온도(T_3)에 대한 예측값을 요청하는 로직이 필요함.
        VoltageAndPowerRequestDto requestDto = new VoltageAndPowerRequestDto(
            voltageAndPowerFeatures, new double[]{0.0, 1.0});
        VoltageAndPowerResponseDto voltageAndPowerResponseDto = this.requestVoltageAndPowerPredictionToAIServer(
            requestDto);

        // 3. 결과 저장
        // todo: 예측값을 보고 PredictionState를 정하는 로직이 필요함.
        Record record = records.get(0);
        this.savePredictions(voltageAndPowerResponseDto, record);
    }

    private List<VoltageAndPowerFeature> extractFeaturesFromRecords(List<Record> records) {
        return IntStream.range(0, records.size()).mapToObj(i -> {
            Record current = records.get(i);
            double iA_diff = (i > 0) ? current.getIA() - records.get(i - 1).getIA() : 0;
            return VoltageAndPowerFeature.fromEntity(current, iA_diff);
        }).collect(Collectors.toList());
    }

    /**
     * 전압(U_totV)과 전력(PW)에 대한 예측값 요청
     */
    private VoltageAndPowerResponseDto requestVoltageAndPowerPredictionToAIServer(
        VoltageAndPowerRequestDto requestDto) {
        return AIServerWebClient.post().uri("/predict_and_explain").bodyValue(requestDto)
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    // 성공 응답 처리
                    return response.bodyToMono(VoltageAndPowerResponseDto.class);
                } else {
                    // 에러 응답의 본문을 String으로 읽어서 로그로 남기고 예외 던지기
                    return response.bodyToMono(String.class)
                        .flatMap(body -> {
                            log.error("AI 서버 에러 (status: {}): {}", response.statusCode(), body);
                            return Mono.error(new RuntimeException(
                                "AI 서버 요청 실패: status=" + response.statusCode()));
                        });
                }
            }).block();
    }

    private PredictionState classifyVoltagePredictionByValue(double voltagePrediction) {
        // todo: 예측 전압값을 보고 고장 상태를 정하는 로직이 필요함.
        return PredictionState.NORMAL;
    }

    private PredictionState classifyPowerPredictionByValue(double powerPrediction) {
        // todo: 예측 전력값을 보고 고장 상태를 정하는 로직이 필요함.
        return PredictionState.NORMAL;
    }

    private void savePredictions(VoltageAndPowerResponseDto voltageAndPowerResponseDto,
        Record record) {
        PredictionState voltagePredictionState = this.classifyVoltagePredictionByValue(
            voltageAndPowerResponseDto.getVoltagePrediction());
        PredictionState powerPredictionState = this.classifyPowerPredictionByValue(
            voltageAndPowerResponseDto.getPowerPrediction());

        voltagePredictionRepository.save(
            voltageAndPowerResponseDto.toVoltagePrediction(record.getPemfc(), record.getTsec(),
                voltagePredictionState));
        powerPredictionRepository.save(
            voltageAndPowerResponseDto.toPowerPrediction(record.getPemfc(), record.getTsec(),
                powerPredictionState));
    }
}
