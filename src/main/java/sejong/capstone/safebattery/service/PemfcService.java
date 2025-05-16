package sejong.capstone.safebattery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.dto.PemfcUpdateDto;
import sejong.capstone.safebattery.enums.PredictionState;
import sejong.capstone.safebattery.repository.PemfcRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PemfcService {
    private final PemfcRepository pemfcRepository;

    public Pemfc addNewPemfc(Pemfc pemfc) {
        return pemfcRepository.save(pemfc);
    }

    public Optional<Pemfc> searchPemfcById(Long id) {
        return pemfcRepository.findById(id);
    }

    public List<Pemfc> searchAllPemfcs() {
        return pemfcRepository.findAll();
    }

    public List<Pemfc> searchPemfcsByClient(Client client) {
        return pemfcRepository.findAllByClient(client);
    }

    public void updatePemfcPowerStateById(Long id, PredictionState state) {
        Pemfc pemfc = pemfcRepository.findById(id).orElseThrow();
        pemfc.setPowerState(state);
    }

    public void updatePemfcVoltageStateById(Long id, PredictionState state) {
        Pemfc pemfc = pemfcRepository.findById(id).orElseThrow();
        pemfc.setVoltageState(state);
    }

    public void updatePemfcTemperatureStateById(Long id, PredictionState state) {
        Pemfc pemfc = pemfcRepository.findById(id).orElseThrow();
        pemfc.setTemperatureState(state);
    }

    public void deletePemfcById(Long id) {
        Pemfc pemfc = pemfcRepository.findById(id).orElseThrow();
        pemfcRepository.delete(pemfc);
    }

    public void updatePemfcLocation(Long id, double lat, double lng) {
        Pemfc pemfc = pemfcRepository.findById(id).orElseThrow();
        pemfc.setLat(lat);
        pemfc.setLng(lng);
    }
}
