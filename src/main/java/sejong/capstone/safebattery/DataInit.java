package sejong.capstone.safebattery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.repository.*;
import sejong.capstone.safebattery.service.RecordService;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import static sejong.capstone.safebattery.enums.PredictionState.NORMAL;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInit {
    private final RecordRepository recordRepository;
    private final PemfcRepository pemfcRepository;
    private final ClientRepository clientRepository;
    private final PredictionRepository predictionRepository;
    private final VoltagePredictionRepository voltagePredictionRepository;
    private final PowerPredictionRepository powerPredictionRepository;
    private final TemperaturePredictionRepository temperaturePredictionRepository;
    private final RecordService recordService;

    @EventListener(ApplicationReadyEvent.class)
    public void dataInit() {
        // output stream 저장
        PrintStream originalOut = System.out;
        try {
            // SQL 로그 끄기
            System.setOut(new PrintStream(OutputStream.nullOutputStream())); // 출력 끔

            voltagePredictionRepository.deleteAll();
            powerPredictionRepository.deleteAll();
            temperaturePredictionRepository.deleteAll();
            recordRepository.deleteAll();
            pemfcRepository.deleteAll();
            clientRepository.deleteAll();
            predictionRepository.deleteAll();

            Client client = new Client("Gildong Hong");
            Pemfc pemfc = new Pemfc(client, NORMAL, 34, 127, "testPemfc-001", LocalDate.of(2025, 1, 1));
            Pemfc pemfc1 = new Pemfc(client, NORMAL, 34, 127, "testPemfc-001", LocalDate.of(2025, 2, 2));
            Pemfc pemfc2 = new Pemfc(client, NORMAL, 34, 127, "testPemfc-001", LocalDate.of(2025, 3, 3));
            clientRepository.save(client);
            pemfcRepository.save(pemfc);
            pemfcRepository.save(pemfc1);
            pemfcRepository.save(pemfc2);

            recordService.add600RowsFromCsv(pemfc.getId());
            recordService.add600RowsFromCsv(pemfc1.getId());
            recordService.add600RowsFromCsv(pemfc2.getId());
            // 로그 기능 복원
            System.setOut(originalOut);
            log.info("Data initialization : completed.");
        } catch(Exception e) {
            // 로그 기능 복원 후 에러 스택 출력
            System.setOut(originalOut);
            e.printStackTrace();
        }
    }
}
