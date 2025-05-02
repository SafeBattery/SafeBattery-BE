package sejong.capstone.safebattery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.repository.ClientRepository;
import sejong.capstone.safebattery.repository.PemfcRepository;
import sejong.capstone.safebattery.repository.PredictionRepository;
import sejong.capstone.safebattery.repository.RecordRepository;

@Slf4j
@RequiredArgsConstructor
public class DataInit {
    private final RecordRepository recordRepository;
    private final PemfcRepository pemfcRepository;
    private final ClientRepository clientRepository;
    private final PredictionRepository predictionRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void dataInit() {
        recordRepository.deleteAll();
        pemfcRepository.deleteAll();
        clientRepository.deleteAll();
        predictionRepository.deleteAll();

        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client);
        clientRepository.save(client);
        pemfcRepository.save(pemfc);
        for (int i = 0; i < 600; i++) {
            Record record = new Record(pemfc, i, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0, 36.4624299, 127.276368);
            recordRepository.save(record);
        }
    }
}
