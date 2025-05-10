package sejong.capstone.safebattery.dto;

import sejong.capstone.safebattery.dao.PredictionRank;

public record PredictionRankDto(PemfcResponseDto pemfc, long totalCount, long errorCount,
                                double errorRate) {

    public static PredictionRankDto fromEntity(PredictionRank rank) {
        return new PredictionRankDto(PemfcResponseDto.fromEntity(rank.pemfc()),
            rank.totalCount(), rank.errorCount(), rank.errorRate());
    }
}
