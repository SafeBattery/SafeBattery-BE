package sejong.capstone.safebattery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Prediction;
import sejong.capstone.safebattery.repository.PredictionRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PredictionService {
    private final PredictionRepository predictionRepository;

    public Prediction addNewPrediction(Prediction prediction) {
        return predictionRepository.save(prediction);
    }

    public Optional<Prediction> searchPredictionById(Long id) {
        return predictionRepository.findById(id);
    }

    public List<Prediction> searchAllPredictions() {
        return predictionRepository.findAll();
    }

    public List<Prediction> searchPredictionsByPemfc(Pemfc pemfc) {
        return predictionRepository.findAllByPemfc(pemfc);
    }
}
