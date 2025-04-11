package sejong.capstone.safebattery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Prediction;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.RecordResponseDto;
import sejong.capstone.safebattery.service.PemfcService;
import sejong.capstone.safebattery.service.PredictionService;
import sejong.capstone.safebattery.service.RecordService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pemfc")
@RequiredArgsConstructor
public class PemfcController {
    private final WebClient webClient;
    private final PemfcService pemfcService;
    private final RecordService recordService;
    private final PredictionService predictionService;

    @PostMapping("/{pemfcId}/record")
    public ResponseEntity<String> addNewRecordAndGetPredict
            (@PathVariable Long pemfcId,
             @Valid @ModelAttribute Record record,
             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            //결측치 발생 시 수행 로직 추가...
            log.info("Invalid record: {}", bindingResult.getAllErrors());
        }

        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        record.setPemfc(pemfc);

        recordService.addNewRecord(record);
        if (recordService.countRecordsByPemfc(pemfc) > 600) {
            List<Record> records = recordService.search600RecordsByPemfc(pemfc);
            List<RecordResponseDto> aiServerRequestData = records.stream()
                    .map(RecordResponseDto::new).toList();
            webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/pemfc/{pemfcId}/predict")
                            .build(pemfc.getId())) // pemfcId 경로에 삽입
                    .bodyValue(aiServerRequestData)
                    .retrieve()
                    .bodyToMono(Prediction.class) // 응답 타입
                    .publishOn(Schedulers.boundedElastic()) // 블로킹-safe한 스레드풀로 이동
                    .doOnNext(response -> { // 블로킹 가능성 있는 코드
                        log.info("response : {}", response);
                        predictionService.addNewPrediction(response);
                    })
                    .block(); // 실제 요청 실행
                    //.subscribe(); // 실제 요청 실행
            return ResponseEntity.ok("record와 prediction이 추가되었습니다.");
        } else {
            return ResponseEntity.ok("record가 추가되었습니다.");
        }
    }
}
