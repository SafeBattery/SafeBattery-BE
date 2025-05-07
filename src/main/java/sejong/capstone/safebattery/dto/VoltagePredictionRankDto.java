package sejong.capstone.safebattery.dto;

import sejong.capstone.safebattery.dao.PredictionRank;

public record VoltagePredictionRankDto(PemfcResponseDto pemfc, long count) {
    public static VoltagePredictionRankDto fromEntity(PredictionRank rank) {
        return new VoltagePredictionRankDto(PemfcResponseDto.fromEntity(rank.pemfc()), rank.count());
    }
}
