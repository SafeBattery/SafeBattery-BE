package sejong.capstone.safebattery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.enums.State;
import sejong.capstone.safebattery.repository.ClientRepository;
import sejong.capstone.safebattery.repository.PemfcRepository;
import sejong.capstone.safebattery.repository.PredictionRepository;
import sejong.capstone.safebattery.repository.RecordRepository;
import sejong.capstone.safebattery.service.RecordService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public class DataInit {
    private final RecordRepository recordRepository;
    private final PemfcRepository pemfcRepository;
    private final ClientRepository clientRepository;
    private final PredictionRepository predictionRepository;
    private final RecordService recordService;

    @EventListener(ApplicationReadyEvent.class)
    public void dataInit() throws IOException {
        // SQL 로그 끄기
//        PrintStream originalOut = System.out; // 저장
//        System.setOut(new PrintStream(OutputStream.nullOutputStream())); // 출력 끔

        recordRepository.deleteAll();
        pemfcRepository.deleteAll();
        clientRepository.deleteAll();
        predictionRepository.deleteAll();

        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client, State.NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        clientRepository.save(client);
        pemfcRepository.save(pemfc);

        recordService.add600RowsFromCsv(pemfc.getId());

//        for (int i = 0; i < 600; i++) {
//            Record record = new Record(pemfc, i, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0, 36.4624299 + 0.1 * i, 127.276368);
//            recordRepository.save(record);
//        }
        // 로그 기능 복원
//        System.setOut(originalOut);
        log.info("Data initialization : completed.");
    }
}
