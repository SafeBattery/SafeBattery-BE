package sejong.capstone.safebattery.dto;

import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.repository.PowerPredictionRepository;

public record PowerPredictionRankDto(Pemfc pemfc, long count) {
    public static PowerPredictionRankDto fromEntity(PredictionRank rank) {
        return new PowerPredictionRankDto(rank.pemfc(), rank.count());
    }
}
