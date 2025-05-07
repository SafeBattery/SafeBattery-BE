package sejong.capstone.safebattery.dto;

import sejong.capstone.safebattery.dao.PredictionRank;

public record TemperaturePredictionRankDto(PemfcResponseDto pemfc, long count) {

    public static TemperaturePredictionRankDto fromEntity(PredictionRank predictionRank) {
        return new TemperaturePredictionRankDto(PemfcResponseDto.fromEntity(predictionRank.pemfc()),
            predictionRank.count());
    }
}
