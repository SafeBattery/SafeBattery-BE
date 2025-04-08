package sejong.capstone.safebattery;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.service.RecordService;

@Service
public class PemfcCsvReader {

    private static final String CSV_FILE_PATH = "src/main/resources/full_test_data.csv";

    private final RecordService recordService;
    // 현재까지 읽은 행의 인덱스(0부터 시작)
    private int currentLine = 0;

    public PemfcCsvReader(RecordService recordService) {
        this.recordService = recordService;
    }

    @Scheduled(fixedRate = 3000)  // 3000밀리초, 즉 3초마다 실행
    public void readCsvFile() {
        System.out.println("CSV 파일을 읽는 작업 시작...");
        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH))) {
            // CsvToBeanBuilder를 사용하여 CSV 데이터를 객체 리스트로 파싱
            CsvToBean<Record> csvToBean = new CsvToBeanBuilder<Record>(reader)
                .withType(Record.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

            List<Record> csvDataList = csvToBean.parse();

            // 아직 읽지 않은 행이 있다면 한 행만 출력
            if (currentLine < csvDataList.size()) {
                Record record = csvDataList.get(currentLine);
                // Record를 저장하는 서비스 코드를 호출
                recordService.addNewRecord(record);

                currentLine++;  // 다음 행으로 인덱스 증가
            } else {
                System.out.println("더 이상 읽을 데이터가 없습니다.");
            }
        } catch (IOException e) {
            System.err.println("CSV 파일 읽기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}