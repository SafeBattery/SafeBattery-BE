package sejong.capstone.safebattery.dao;

import sejong.capstone.safebattery.enums.PredictionState;

public record RawPredictionRank(Long pemfcId, Long totalCount, Long errorCount, Double errorRate) {

}
