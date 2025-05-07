package sejong.capstone.safebattery.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.capstone.safebattery.dao.PredictionRank;

import java.util.List;
import sejong.capstone.safebattery.domain.PowerPrediction;
import sejong.capstone.safebattery.domain.TemperaturePrediction;
import sejong.capstone.safebattery.domain.VoltagePrediction;
import sejong.capstone.safebattery.repository.PredictionRankRepository;

@Service
@RequiredArgsConstructor
public class PredictionRankService {

    private final PredictionRankRepository<VoltagePrediction> voltagePredictionRankRepository;
    private final PredictionRankRepository<PowerPrediction> powerPredictionRankRepository;
    private final PredictionRankRepository<TemperaturePrediction> temperaturePredictionRankRepository;

    public List<PredictionRank> getVoltagePredictionRanks() {
        return voltagePredictionRankRepository.getPredictionRanks();
    }

    public List<PredictionRank> getPowerPredictionRanks() {
        return powerPredictionRankRepository.getPredictionRanks();
    }

    public List<PredictionRank> getTemperatureRanks() {
        return temperaturePredictionRankRepository.getPredictionRanks();
    }
}
