package sejong.capstone.safebattery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.repository.PemfcRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PemfcService {
    private final PemfcRepository pemfcRepository;

    public Pemfc addNewRow(Pemfc pemfc) {
        return pemfcRepository.save(pemfc);
    }

    public Optional<Pemfc> searchRowById(Long id) {
        return pemfcRepository.findById(id);
    }

    public List<Pemfc> searchAllRows() {
        return pemfcRepository.findAll();
    }
}
