package sejong.capstone.safebattery;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.repository.ClientRepository;
import sejong.capstone.safebattery.repository.PemfcRepository;
import sejong.capstone.safebattery.repository.RecordRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sejong.capstone.safebattery.enums.PredictionState.*;

@SpringBootTest
@Transactional
public class RecordRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(RecordRepositoryTest.class);
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    PemfcRepository pemfcRepository;

    @Test
    public void create() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Record record = new Record(pemfc, NORMAL, NORMAL, NORMAL, 1.047, 0.956, 0.112, 0.107,
                7.749, 1.052, 94.016, 100.098,
        0.96, 0.556, 1.059, 0.677,
                14.691, -64762.981, 34.011, 14.584,
                29.724, 18.663, 42.509,
        64.992, 0.024, 0.002, 0, 0, 36.4624299, 127.276368, 0);

        //when
        Pemfc savedPemfc = pemfcRepository.save(pemfc);
        Record savedRecord = recordRepository.save(record);

        //then
        assertThat(savedPemfc.getClient()).isEqualTo(client);
        assertThat(savedRecord.getTsec()).isEqualTo(1.047);
    }

    @Test
    public void read() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Record record = new Record(pemfc, NORMAL, NORMAL, NORMAL, 1.047, 0.956, 0.112, 0.107,
                7.749, 1.052, 94.016, 100.098,
                0.96, 0.556, 1.059, 0.677,
                14.691, -64762.981, 34.011, 14.584,
                29.724, 18.663, 42.509,
                64.992, 0.024, 0.002, 0, 0, 36.4624299, 127.276368,0);
        Record savedRecord = recordRepository.save(record);

        //when
        Optional<Record> findRecord = recordRepository.findById(savedRecord.getId());
        Optional<Record> noSuchRecord = recordRepository.findById(-1L);

        //then
        assertThat(findRecord.orElseThrow().getTsec()).isEqualTo(1.047);
        assertThatThrownBy(noSuchRecord::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void readAllByPemfc() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc1 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc2 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc savedPemfc1 = pemfcRepository.save(pemfc1);
        Pemfc savedPemfc2 = pemfcRepository.save(pemfc2);
        Record record1 = new Record(pemfc1, NORMAL, NORMAL, NORMAL, 1.047, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0, 36.4624299, 127.276368,0);
        Record record2 = new Record(pemfc1, NORMAL, NORMAL, NORMAL, 2.047, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0, 36.4624184, 127.27638,0);
        Record record3 = new Record(pemfc2, NORMAL, NORMAL, NORMAL, 3.047, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0, 36.462407, 127.276392,0);

        //when
        clientRepository.save(client);
        pemfcRepository.save(pemfc1);
        pemfcRepository.save(pemfc2);
        recordRepository.save(record1);
        recordRepository.save(record2);
        recordRepository.save(record3);

        //then
        assertThat(recordRepository.findAllByPemfc(savedPemfc1).size()).isEqualTo(2);
        assertThat(recordRepository.findAllByPemfc(savedPemfc2).size()).isEqualTo(1);
    }

    @Test
    public void read600RowsByPemfc() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        pemfcRepository.save(pemfc);

        //when
        clientRepository.save(client);
        pemfcRepository.save(pemfc);
        for(int i = 0; i < 700; i++) {
            Record newRecord = new Record(pemfc, NORMAL, NORMAL, NORMAL, (double) i, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0, 36.4624299, 127.276368, 0);
            recordRepository.save(newRecord);
        }

        //then
        //System.out.println(recordRepository.findTop600ByPemfcOrderByTsecDesc(pemfc).toString());
        assertThat(recordRepository.findTop3000ByPemfcOrderByTsecDesc(pemfc).size()).isEqualTo(600);
    }

    @Test
    public void countAllByPemfc() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc1 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc2 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc savedPemfc1 = pemfcRepository.save(pemfc1);
        Pemfc savedPemfc2 = pemfcRepository.save(pemfc2);
        Record record1 = new Record(pemfc1, NORMAL, NORMAL, NORMAL, 1.047, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0, 36.4624299, 127.276368, 0);
        Record record2 = new Record(pemfc1, NORMAL, NORMAL, NORMAL, 2.047, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0, 36.4624184, 127.27638, 0);
        Record record3 = new Record(pemfc2, NORMAL, NORMAL, NORMAL, 3.047, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0, 36.462407, 127.276392, 0);

        //when
        clientRepository.save(client);
        pemfcRepository.save(pemfc1);
        pemfcRepository.save(pemfc2);
        recordRepository.save(record1);
        recordRepository.save(record2);
        recordRepository.save(record3);

        //then
        assertThat(recordRepository.countByPemfc(savedPemfc1)).isEqualTo(2);
        assertThat(recordRepository.countByPemfc(savedPemfc2)).isEqualTo(1);
    }
}
