package sejong.capstone.safebattery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.dto.ClientUpdateDto;
import sejong.capstone.safebattery.dto.PemfcUpdateDto;
import sejong.capstone.safebattery.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client addNewClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> searchClientById(Long id) {
        return clientRepository.findById(id);
    }

    public List<Client> searchAllClients() {
        return clientRepository.findAll();
    }

    public void updateClientById(Long id, ClientUpdateDto updateParams) {
        Client client = clientRepository.findById(id).orElseThrow();
        client.setLoginId(updateParams.getLoginId());
        client.setPassword(updateParams.getPassword());
        client.setName(updateParams.getName());
    }

    public void deleteClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow();
        clientRepository.delete(client);
    }
}
