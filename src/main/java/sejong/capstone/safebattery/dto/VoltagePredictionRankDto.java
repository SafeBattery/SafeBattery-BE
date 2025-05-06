package sejong.capstone.safebattery.dto;

import sejong.capstone.safebattery.domain.Pemfc;

public record VoltagePredictionRankDto(Pemfc pemfc, long count) {
    public static VoltagePredictionRankDto fromEntity(PredictionRank rank) {
        return new VoltagePredictionRankDto(rank.pemfc(), rank.count());
    }
}
