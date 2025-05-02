package sejong.capstone.safebattery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Prediction;
import sejong.capstone.safebattery.enums.State;
import sejong.capstone.safebattery.repository.ClientRepository;
import sejong.capstone.safebattery.repository.PemfcRepository;
import sejong.capstone.safebattery.repository.PredictionRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PredictionRepositoryTest {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    PemfcRepository pemfcRepository;
    @Autowired
    PredictionRepository predictionRepository;

    @Test
    public void create() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client, State.NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Prediction prediction = new Prediction(pemfc, 1.047, 1.0);

        //when
        Pemfc savedPemfc = pemfcRepository.save(pemfc);
        Prediction savedPrediction = predictionRepository.save(prediction);

        //then
        assertThat(savedPemfc.getClient()).isEqualTo(client);
        assertThat(savedPrediction.getTsec()).isEqualTo(1.047);
    }

    @Test
    public void read() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client, State.NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Prediction prediction = new Prediction(pemfc, 1.047, 1.0);
        Prediction savedPrediction = predictionRepository.save(prediction);

        //when
        Optional<Prediction> findPrediction = predictionRepository.findById(savedPrediction.getId());
        Optional<Prediction> noSuchPrediction = predictionRepository.findById(-1L);

        //then
        assertThat(findPrediction.orElseThrow().getTsec()).isEqualTo(1.047);
        assertThatThrownBy(noSuchPrediction::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void readAllByPemfc() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc1 = new Pemfc(client, State.NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc2 = new Pemfc(client, State.NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc savedPemfc1 = pemfcRepository.save(pemfc1);
        Pemfc savedPemfc2 = pemfcRepository.save(pemfc2);
        Prediction prediction1 = new Prediction(pemfc1, 1.047, 1.0);
        Prediction prediction2 = new Prediction(pemfc1, 2.047, 3.0);
        Prediction prediction3 = new Prediction(pemfc2, 3.047, 2.0);

        //when
        clientRepository.save(client);
        pemfcRepository.save(pemfc1);
        pemfcRepository.save(pemfc2);
        predictionRepository.save(prediction1);
        predictionRepository.save(prediction2);
        predictionRepository.save(prediction3);

        //then
        assertThat(predictionRepository.findAllByPemfc(savedPemfc1).size()).isEqualTo(2);
        assertThat(predictionRepository.findAllByPemfc(savedPemfc2).size()).isEqualTo(1);
    }
}
