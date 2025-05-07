package sejong.capstone.safebattery.dto;

import sejong.capstone.safebattery.dao.PredictionRank;

public record PowerPredictionRankDto(PemfcResponseDto pemfc, long count) {

    public static PowerPredictionRankDto fromEntity(PredictionRank rank) {
        return new PowerPredictionRankDto(PemfcResponseDto.fromEntity(rank.pemfc()), rank.count());
    }
}
