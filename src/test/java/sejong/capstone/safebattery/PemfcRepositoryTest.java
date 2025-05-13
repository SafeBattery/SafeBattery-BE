package sejong.capstone.safebattery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.enums.PredictionState;
import sejong.capstone.safebattery.repository.ClientRepository;
import sejong.capstone.safebattery.repository.PemfcRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sejong.capstone.safebattery.enums.PredictionState.*;

@SpringBootTest
@Transactional
public class PemfcRepositoryTest {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    PemfcRepository pemfcRepository;

    @Test
    public void create() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));

        //when
        clientRepository.save(client);
        Pemfc savedPemfc = pemfcRepository.save(pemfc);

        //then
        assertThat(savedPemfc.getClient()).isEqualTo(client);
    }

    @Test
    public void read() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc savedPemfc = pemfcRepository.save(pemfc);

        //when
        Optional<Pemfc> findClient = pemfcRepository.findById(savedPemfc.getId());
        Optional<Pemfc> noSuchClient = pemfcRepository.findById(-1L);

        //then
        assertThat(findClient.orElseThrow().getClient()).isEqualTo(client);
        assertThatThrownBy(noSuchClient::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void readAll() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc1 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc2 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc3 = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));

        //when
        clientRepository.save(client);
        Pemfc savedPemfc1 = pemfcRepository.save(pemfc1);
        Pemfc savedPemfc2 = pemfcRepository.save(pemfc2);
        Pemfc savedPemfc3 = pemfcRepository.save(pemfc3);

        //then
        assertThat(pemfcRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    public void readAllByClient() {
        //given
        Client client1 = new Client("Gildong Hong");
        Client client2 = new Client("Baksa Hong");
        Pemfc pemfc1 = new Pemfc(client1, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc2 = new Pemfc(client1, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc3 = new Pemfc(client2, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));

        //when
        clientRepository.save(client1);
        clientRepository.save(client2);
        Pemfc savedPemfc1 = pemfcRepository.save(pemfc1);
        Pemfc savedPemfc2 = pemfcRepository.save(pemfc2);
        Pemfc savedPemfc3 = pemfcRepository.save(pemfc3);

        //then
        assertThat(pemfcRepository.findAllByClient(client1).size()).isEqualTo(2);
        assertThat(pemfcRepository.findAllByClient(client2).size()).isEqualTo(1);
    }

    @Test
    public void delete() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client, NORMAL, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        pemfcRepository.save(pemfc);

        //when
        pemfcRepository.delete(pemfc);
        Optional<Pemfc> findClient = pemfcRepository.findById(pemfc.getId());

        //then
        assertThatThrownBy(findClient::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }
}
