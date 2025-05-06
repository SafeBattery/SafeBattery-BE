package sejong.capstone.safebattery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.capstone.safebattery.domain.PredictionState;
import sejong.capstone.safebattery.dto.PredictionRank;
import sejong.capstone.safebattery.dto.VoltagePredictionRankDto;
import sejong.capstone.safebattery.repository.VoltagePredictionRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {
    private final VoltagePredictionRepository voltagePredictionRepository;

    public List<PredictionRank> getVoltagePredictionRankOfRecent100Predictions() {
        // todo: 최근 100건만 group by로 가져오도록 쿼리를 수정해야함.
        return voltagePredictionRepository.countIdGroupByPemfcWhereStateDesc(PredictionState.ERROR);
    }

    public List<PredictionRank> getPowerPredictionRankOfRecent100Predictions() {
        // todo: 최근 100건만 group by로 가져오도록 쿼리를 수정해야함.
        return voltagePredictionRepository.countIdGroupByPemfcWhereStateDesc(PredictionState.ERROR);
    }
}
