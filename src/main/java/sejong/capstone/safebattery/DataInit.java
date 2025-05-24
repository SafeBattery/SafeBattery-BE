package sejong.capstone.safebattery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.repository.*;
import sejong.capstone.safebattery.service.PredictionService;
import sejong.capstone.safebattery.service.RecordService;

import java.time.LocalDate;

import static sejong.capstone.safebattery.enums.PredictionState.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInit {
//    private final RecordRepository recordRepository;
    private final PemfcRepository pemfcRepository;
    private final ClientRepository clientRepository;
//    private final VoltagePredictionRepository voltagePredictionRepository;
//    private final PowerPredictionRepository powerPredictionRepository;
//    private final TemperaturePredictionRepository temperaturePredictionRepository;
//    private final VoltagePowerDynamaskRepository voltagePowerDynamaskRepository;
//    private final TemperatureDynamaskRepository temperatureDynamaskRepository;
    private final RecordService recordService;
    private final PredictionService predictionService;

    @EventListener(ApplicationReadyEvent.class)
    public void dataInit() {
        try {

            Client client = new Client("Gildong Hong");
            Pemfc pemfc1 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 36.451354, 127.285213, "Pemfc-001", LocalDate.of(2025, 1, 1));
            Pemfc pemfc2 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 36.4556036, 127.283117, "Pemfc-002", LocalDate.of(2025, 2, 2));
            Pemfc pemfc3 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 36.4317705, 127.286513, "Pemfc-003", LocalDate.of(2025, 3, 3));
            clientRepository.save(client);
            pemfcRepository.save(pemfc1);
            pemfcRepository.save(pemfc2);
            pemfcRepository.save(pemfc3);

            // Prediction 초기화 데이터를 얻기 위한 코드
//            recordService.add3000RowsFromCsv(pemfc1.getId(), 20900);
//            recordService.add3000RowsFromCsv(pemfc2.getId(), 26420);
//            recordService.add3000RowsFromCsv(pemfc3.getId(), 22080);

            recordService.add3000RowsFromCsv(pemfc1.getId(), 21000);
            recordService.add3000RowsFromCsv(pemfc2.getId(), 26520);
            recordService.add3000RowsFromCsv(pemfc3.getId(), 22180);

            predictionService.addVoltagePredictionRowsFromCsv("prediction_pemfc1_voltage.csv", pemfc1.getId());
            predictionService.addPowerPredictionRowsFromCsv("prediction_pemfc1_power.csv", pemfc1.getId());
            predictionService.addTemperaturePredictionRowsFromCsv("prediction_pemfc1_temp.csv", pemfc1.getId());
            predictionService.addVoltagePredictionRowsFromCsv("prediction_pemfc2_voltage.csv", pemfc2.getId());
            predictionService.addPowerPredictionRowsFromCsv("prediction_pemfc2_power.csv", pemfc2.getId());
            predictionService.addTemperaturePredictionRowsFromCsv("prediction_pemfc2_temp.csv", pemfc2.getId());
            predictionService.addVoltagePredictionRowsFromCsv("prediction_pemfc3_voltage.csv", pemfc3.getId());
            predictionService.addPowerPredictionRowsFromCsv("prediction_pemfc3_power.csv", pemfc3.getId());
            predictionService.addTemperaturePredictionRowsFromCsv("prediction_pemfc3_temp.csv", pemfc3.getId());

            log.info("Data initialization : completed.");
        } catch (Exception e) {
            log.error("Data init error", e);
        }
    }
}
