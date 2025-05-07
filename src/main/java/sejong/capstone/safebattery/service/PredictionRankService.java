package sejong.capstone.safebattery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.capstone.safebattery.dao.PredictionRank;

import java.util.List;
import sejong.capstone.safebattery.domain.PowerPrediction;
import sejong.capstone.safebattery.domain.VoltagePrediction;
import sejong.capstone.safebattery.repository.PredictionRankRepository;

@Service
@RequiredArgsConstructor
public class PredictionRankService {

    private final PredictionRankRepository<VoltagePrediction> voltagePredictionRankRepository;
    private final PredictionRankRepository<PowerPrediction> powerPredictionRankRepository;

    public List<PredictionRank> getVoltagePredictionRanks() {
        // todo: 테이블 이름을 동적으로 갖고 오도록 수정해야함.
        return voltagePredictionRankRepository.getPredictionRanks();
    }

    public List<PredictionRank> getPowerPredictionRanks() {
        // todo: 테이블 이름을 동적으로 갖고 오도록 수정해야함.
        return powerPredictionRankRepository.getPredictionRanks();
    }
}
