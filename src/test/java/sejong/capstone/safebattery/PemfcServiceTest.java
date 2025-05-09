package sejong.capstone.safebattery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.dto.PemfcUpdateDto;
import sejong.capstone.safebattery.repository.ClientRepository;
import sejong.capstone.safebattery.service.PemfcService;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sejong.capstone.safebattery.enums.PredictionState.*;

@SpringBootTest
@Transactional
public class PemfcServiceTest {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    PemfcService pemfcService;

    @Test
    public void 모든Pemfc찾기() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc1 = new Pemfc(client, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc2 = new Pemfc(client, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc3 = new Pemfc(client, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));

        //when
        clientRepository.save(client);
        pemfcService.addNewPemfc(pemfc1);
        pemfcService.addNewPemfc(pemfc2);
        pemfcService.addNewPemfc(pemfc3);

        //then
        assertThat(pemfcService.searchAllPemfcs().size()).isEqualTo(3);
    }

    @Test
    public void 클라이언트의모든Pemfc찾기() {
        //given
        Client client1 = new Client("Gildong Hong");
        Client client2 = new Client("Baksa Hong");
        Pemfc pemfc1 = new Pemfc(client1, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc2 = new Pemfc(client1, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        Pemfc pemfc3 = new Pemfc(client2, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));

        //when
        clientRepository.save(client1);
        clientRepository.save(client2);
        pemfcService.addNewPemfc(pemfc1);
        pemfcService.addNewPemfc(pemfc2);
        pemfcService.addNewPemfc(pemfc3);

        //then
        assertThat(pemfcService.searchPemfcsByClient(client1).size()).isEqualTo(2);
        assertThat(pemfcService.searchPemfcsByClient(client2).size()).isEqualTo(1);
    }

    @Test
    public void 클라이언트수정() {
        //given
        Client client1 = new Client("Gildong Hong");
        Client client2 = new Client("Baksa Hong");
        Pemfc pemfc = new Pemfc(client1, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        pemfcService.addNewPemfc(pemfc);
        PemfcUpdateDto updateParams = new PemfcUpdateDto(client2, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));

        //when
        pemfcService.updatePemfcPowerVoltageStateById(pemfc.getId(), WARNING);
        pemfcService.updatePemfcTemperatureStateById(pemfc.getId(), ERROR);

        //then
        assertThat(pemfcService.searchPemfcById(pemfc.getId()).orElseThrow()
                .getPowerVoltageState()).isEqualTo(WARNING);
        assertThat(pemfcService.searchPemfcById(pemfc.getId()).orElseThrow()
                .getTemperatureState()).isEqualTo(ERROR);
    }

    @Test
    public void 클라이언트삭제() {
        //given
        Client client = new Client("Gildong Hong");
        Pemfc pemfc = new Pemfc(client, NORMAL, NORMAL, 34,127, "testPemfc-001", LocalDate.of(2025, 1, 1));
        pemfcService.addNewPemfc(pemfc);

        //when
        pemfcService.deletePemfcById(pemfc.getId());
        Optional<Pemfc> findClient = pemfcService.searchPemfcById(pemfc.getId());

        //then
        assertThatThrownBy(findClient::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }
}
