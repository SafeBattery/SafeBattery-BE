package sejong.capstone.safebattery.controller;

import java.io.IOException;
import java.util.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.PemfcRequestDto;
import sejong.capstone.safebattery.dto.PemfcResponseDto;
import sejong.capstone.safebattery.dto.PredictRequest;
import sejong.capstone.safebattery.dto.RecordResponseDto;
import sejong.capstone.safebattery.service.ClientService;
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
    private final ClientService clientService;
    private final PemfcService pemfcService;
    private final RecordService recordService;
    private final PredictionService predictionService;

    @GetMapping("{pemfcId}")
    public PemfcResponseDto getPemfcById(
            @PathVariable("pemfcId") Long pemfcId) {
        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        return new PemfcResponseDto(pemfc);
    }

    @PostMapping("/")
    public ResponseEntity<String> addNewPemfc(@Valid @ModelAttribute PemfcRequestDto form,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //결측치 발생 시 수행 로직 추가...
            log.info("Invalid pemfc: {}", bindingResult.getAllErrors());
        }
        Client client = clientService.searchClientById(form.getClientId()).orElseThrow();
        pemfcService.addNewPemfc(new Pemfc(client, form.getState(), form.getLat(), form.getLng(),
                form.getModelName(), form.getManufacturedDate()));

        return ResponseEntity.ok("pemfc가 성공적으로 추가되었습니다.");
    }

    @DeleteMapping("/{pemfcId}/delete")
    public ResponseEntity<String> DeletePemfcOfClient(
            @PathVariable("pemfcId") Long pemfcId) {

        pemfcService.deletePemfcById(pemfcId);

        return ResponseEntity.ok("pemfc가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/{pemfcId}/record/all")
    public List<RecordResponseDto> getRecordsOfPemfc(
            @PathVariable("pemfcId") Long pemfcId) {
        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        return recordService.searchRecordsByPemfc(pemfc).stream()
                .map(RecordResponseDto::new).toList();
    }

    @GetMapping("/{pemfcId}/record/recent600")
    public List<RecordResponseDto> getRecent600RecordsOfPemfc(
            @PathVariable("pemfcId") Long pemfcId) {
        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        return recordService.search600RecordsByPemfc(pemfc).stream()
                .map(RecordResponseDto::new).toList();
    }

    @GetMapping("/{pemfcId}/csv")
    public void getRecordCsvOfPemfc(
            @PathVariable("pemfcId") Long pemfcId,
            HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"csv_of_pemfc_" + pemfcId + ".csv\"");
        recordService.makeCsvByRecordsOfPemfc(pemfcId, response.getWriter());
    }

    @PostMapping("/{pemfcId}/prediction")
    public ResponseEntity<String> addNewRecordAndGetPrediction
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

            List<List<Double>> inputList = new ArrayList<>();
            //임시 코드
            for (int i = 0; i < 600; i++) {
                List<Double> list = new ArrayList<>();
                for (int j = 0; j < 9; j++) {
                    list.add(Math.random()); // 예시로 랜덤 실수값
                }
                inputList.add(list);
            }

            PredictRequest requestData = new PredictRequest(inputList, 0.4);
            webClient.post()
                                .uri("/predict") //
                    .bodyValue(requestData)
                    .retrieve()
                    .bodyToMono(Object.class) // 응답 타입
                    .publishOn(Schedulers.boundedElastic()) // 블로킹-safe한 스레드풀로 이동
                    .doOnNext(response -> { // 블로킹 가능성 있는 코드
                        log.info("response : {}", response);
                    })
                    .block(); // 실제 요청 실행
            //임시 코드 종료
                    //.subscribe(); // 실제 요청 실행
//                    .uri(uriBuilder -> uriBuilder
//                            .path("/api/pemfc/{pemfcId}/predict")
//                            .build(pemfc.getId())) // pemfcId 경로에 삽입
//                    .bodyValue(aiServerRequestData)
//                    .retrieve()
//                    .bodyToMono(Prediction.class) // 응답 타입
//                    .publishOn(Schedulers.boundedElastic()) // 블로킹-safe한 스레드풀로 이동
//                    .doOnNext(response -> { // 블로킹 가능성 있는 코드
//                        log.info("response : {}", response);
//                        predictionService.addNewPrediction(response);
//                    })
//                    .block(); // 실제 요청 실행
//                    //.subscribe(); // 실제 요청 실행
            return ResponseEntity.ok("record와 prediction이 추가되었습니다.");
        } else {
            return ResponseEntity.ok("record가 추가되었습니다.");
        }
    }
}
