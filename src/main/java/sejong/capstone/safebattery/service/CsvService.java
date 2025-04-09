package sejong.capstone.safebattery.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Prediction;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.RecordResponseDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvService {

    private final RecordService recordService;
    private final PemfcService pemfcService;
    private final WebClient webClient;
    private final PredictionService predictionService;
    private final ScheduledExecutorService schedulerService = Executors.newSingleThreadScheduledExecutor();


    private static final String CSV_FILE_PATH = "src/main/resources/full_test_data.csv";
    // 현재까지 읽은 행의 인덱스(0부터 시작)
    private int currentLine = 0;
    private boolean started = false;
    List<Record> csvDataList;

    @PostConstruct
    public void init() {
        log.info("CSV 파일을 읽는 작업 시작...");
        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH))) {
            // CsvToBeanBuilder를 사용하여 CSV 데이터를 객체 리스트로 파싱
            CsvToBean<Record> csvToBean = new CsvToBeanBuilder<Record>(reader)
                    .withType(Record.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            csvDataList = csvToBean.parse();
        } catch (IOException e) {
            log.error("CSV 파일 읽기 중 오류 발생: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public void startScheduler(Long pemfcId) {
        if (!started) {
            init(); // 처음 시작 시만 파일 읽기
            Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
            schedulerService.scheduleAtFixedRate(
                    () -> processNextLine(pemfc), 0, 3, TimeUnit.SECONDS);
            started = true;
            log.info("스케줄러 시작됨");
        }
    }

    private void processNextLine(Pemfc pemfc) {
        if (csvDataList != null && currentLine < csvDataList.size()) {
            Record record = csvDataList.get(currentLine);
            record.setPemfc(pemfc);

            recordService.addNewRecord(record);
            log.info("[{}] 레코드 처리 완료", currentLine);

            //600line 이상이면 해당 pemfc의 가장 최근 600개 record를 가져와서 저장
            //flask 서버 api 호출(POST) - 600개 record를 request body로 전송
            //flask 서버는 모델 호출 결과를 다시 WAS에 전송
            if (currentLine > 600) {
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
                        .doOnNext(response -> { // 블로킹 가능성 있는 코드)
                            log.info("response : {}", response);
                            predictionService.addNewPrediction(response);
                        })
                        .subscribe(); // 실제 요청 실행
            }
            currentLine++;
        } else {
            log.info("모든 데이터를 처리했습니다.");
        }
    }
}