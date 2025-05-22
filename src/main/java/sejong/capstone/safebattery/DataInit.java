package sejong.capstone.safebattery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.TemperatureDynamask;
import sejong.capstone.safebattery.domain.VoltagePowerDynamask;
import sejong.capstone.safebattery.repository.*;
import sejong.capstone.safebattery.service.PredictionService;
import sejong.capstone.safebattery.service.RecordService;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.*;

import static sejong.capstone.safebattery.enums.PredictionState.NORMAL;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInit {
    private final RecordRepository recordRepository;
    private final PemfcRepository pemfcRepository;
    private final ClientRepository clientRepository;
    private final VoltagePredictionRepository voltagePredictionRepository;
    private final PowerPredictionRepository powerPredictionRepository;
    private final TemperaturePredictionRepository temperaturePredictionRepository;
    private final VoltagePowerDynamaskRepository voltagePowerDynamaskRepository;
    private final TemperatureDynamaskRepository temperatureDynamaskRepository;
    private final RecordService recordService;
    private final PredictionService predictionService;

    @EventListener(ApplicationReadyEvent.class)
    public void dataInit() {
        //data.sql에 있는 sql들을 모두 실행한 후 실행되는 초기화 코드입니다.
        // output stream 저장
        PrintStream originalOut = System.out;
        try {
            // SQL 로그 끄기
            System.setOut(new PrintStream(OutputStream.nullOutputStream())); // 출력 끔

            Client client = new Client("Gildong Hong");
            Pemfc pemfc1 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34, 127, "Pemfc-001", LocalDate.of(2025, 1, 1));
            Pemfc pemfc2 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34, 127, "Pemfc-002", LocalDate.of(2025, 2, 2));
            Pemfc pemfc3 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34, 127, "Pemfc-003", LocalDate.of(2025, 3, 3));
            clientRepository.save(client);
            pemfcRepository.save(pemfc1);
            pemfcRepository.save(pemfc2);
            pemfcRepository.save(pemfc3);

            recordService.add3000RowsFromCsv(pemfc1.getId(), 20900);
            recordService.add3000RowsFromCsv(pemfc2.getId(), 26420);
            recordService.add3000RowsFromCsv(pemfc3.getId(), 22080);

            predictionService.addVoltagePredictionRowsFromCsv("pemfc_1_voltage.csv", pemfc1.getId());
            predictionService.addPowerPredictionRowsFromCsv("pemfc_1_power.csv", pemfc1.getId());
            predictionService.addTemperaturePredictionRowsFromCsv("pemfc_1_temp.csv", pemfc1.getId());
            predictionService.addVoltagePredictionRowsFromCsv("pemfc_2_voltage.csv", pemfc2.getId());
            predictionService.addPowerPredictionRowsFromCsv("pemfc_2_power.csv", pemfc2.getId());
            predictionService.addTemperaturePredictionRowsFromCsv("pemfc_2_temp.csv", pemfc2.getId());
            predictionService.addVoltagePredictionRowsFromCsv("pemfc_3_voltage.csv", pemfc3.getId());
            predictionService.addPowerPredictionRowsFromCsv("pemfc_3_power.csv", pemfc3.getId());
            predictionService.addTemperaturePredictionRowsFromCsv("pemfc_3_temp.csv", pemfc3.getId());
//            for (Pemfc pemfc : List.of(pemfc1, pemfc2, pemfc3)) {
//                List<List<Double>> vpMaskData = new ArrayList<>();
//                for (int i = 0; i < 600; i++) {
//                    List<Double> row = new ArrayList<>();
//                    for (int j = 0; j < 9; j++) {
//                        row.add(Math.round(Math.random() * 10) / 10.0);
//                    }
//                    vpMaskData.add(row);
//                }
//                VoltagePowerDynamask vpDynamask = VoltagePowerDynamask.builder()
//                        .tsec(0.0)
//                        .pemfc(pemfc)
//                        .value(vpMaskData).build();
//                voltagePowerDynamaskRepository.save(vpDynamask);
//
//                List<List<Double>> tempMaskData = new ArrayList<>();
//                for (int i = 0; i < 600; i++) {
//                    List<Double> row = new ArrayList<>();
//                    for (int j = 0; j < 4; j++) {
//                        row.add(Math.round(Math.random() * 10) / 10.0);
//                    }
//                    tempMaskData.add(row);
//                }
//                TemperatureDynamask tempDynamask = TemperatureDynamask.builder()
//                        .tsec(0.0)
//                        .pemfc(pemfc)
//                        .value(tempMaskData).build();
//                temperatureDynamaskRepository.save(tempDynamask);
//            }

            // 로그 기능 복원
            System.setOut(originalOut);
            log.info("Data initialization : completed.");
        } catch (Exception e) {
            // 로그 기능 복원 후 에러 스택 출력
            System.setOut(originalOut);
            log.error("Data init error", e);
        }
    }
}
