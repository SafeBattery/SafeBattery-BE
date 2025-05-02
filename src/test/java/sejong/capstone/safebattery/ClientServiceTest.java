package sejong.capstone.safebattery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.dto.ClientUpdateDto;
import sejong.capstone.safebattery.dto.PemfcUpdateDto;
import sejong.capstone.safebattery.service.ClientService;
import sejong.capstone.safebattery.service.PemfcService;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ClientServiceTest {
    @Autowired
    ClientService clientService;

    @Test
    public void 모든클라이언트찾기() {
        //given
        Client client1 = new Client("Gildong Hong");
        Client client2 = new Client("Gildong Kim");
        Client client3 = new Client("Baksa Hong");

        //when
        clientService.addNewClient(client1);
        clientService.addNewClient(client2);
        clientService.addNewClient(client3);

        //then
        assertThat(clientService.searchAllClients().size()).isEqualTo(3);
    }

    @Test
    public void 클라이언트수정() {
        //given
        Client client = new Client("Gildong Hong");
        clientService.addNewClient(client);
        ClientUpdateDto updateParams = new ClientUpdateDto("Baksa Hong");

        //when
        clientService.updateClientById(client.getId(), updateParams);

        //then
        assertThat(clientService.searchClientById(client.getId()).orElseThrow().getName())
                .isEqualTo("Baksa Hong");
    }

    @Test
    public void 클라이언트삭제() {
        //given
        Client client = new Client("Gildong Hong");
        clientService.addNewClient(client);

        //when
        clientService.deleteClientById(client.getId());
        Optional<Client> findClient = clientService.searchClientById(client.getId());

        //then
        assertThatThrownBy(findClient::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }
}
