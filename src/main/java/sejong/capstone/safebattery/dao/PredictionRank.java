package sejong.capstone.safebattery.dao;

import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.enums.PredictionState;

public record PredictionRank(Pemfc pemfc, long totalCount, long errorCount, double errorRate) {
    public static PredictionRank fromRawPredictionRank(RawPredictionRank rawPredictionRank, Pemfc pemfc) {
        return new PredictionRank(
            pemfc,
            rawPredictionRank.totalCount(),
            rawPredictionRank.errorCount(),
            rawPredictionRank.errorRate()
        );
    }
}
