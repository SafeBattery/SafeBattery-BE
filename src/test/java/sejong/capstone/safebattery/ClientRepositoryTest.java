package sejong.capstone.safebattery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.repository.ClientRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ClientRepositoryTest {
    @Autowired ClientRepository clientRepository;

    @Test
    public void create() {
        //given
        Client client = new Client("Gildong Hong");

        //when
        Client savedClient = clientRepository.save(client);

        //then
        assertThat(savedClient.getName()).isEqualTo("Gildong Hong");
    }

    @Test
    public void read() {
        //given
        Client client = new Client("Gildong Hong");
        Client savedClient = clientRepository.save(client);

        //when
        Optional<Client> findClient = clientRepository.findById(savedClient.getId());
        Optional<Client> noSuchClient = clientRepository.findById(-1L);

        //then
        assertThat(findClient.orElseThrow().getName()).isEqualTo("Gildong Hong");
        assertThatThrownBy(noSuchClient::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void readAll() {
        //given
        Client client1 = new Client("Gildong Hong");
        Client client2 = new Client("Gildong Hong");
        Client client3 = new Client("Gildong Hong");

        //when
        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);

        //then
        assertThat(clientRepository.findAll().size()).isEqualTo(3);
    }

    //update는 serviceTest에서만 가능. 생략

    @Test
    public void delete() {
        //given
        Client client = new Client("Gildong Hong");
        Client savedClient = clientRepository.save(client);

        //when
        clientRepository.delete(client);
        Optional<Client> findClient = clientRepository.findById(savedClient.getId());

        //then
        assertThatThrownBy(findClient::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }
}
