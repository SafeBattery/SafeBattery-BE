package sejong.capstone.safebattery.dto;

import sejong.capstone.safebattery.domain.BasePrediction;
import sejong.capstone.safebattery.enums.PredictionState;

public record PredictionDto(long id, PemfcResponseDto pemfc, double tsec, double predictedValue,
                            PredictionState state) {

    public static PredictionDto fromEntity(BasePrediction prediction) {
        return new PredictionDto(prediction.getId(),
            PemfcResponseDto.fromEntity(prediction.getPemfc()),
            prediction.getTsec(), prediction.getPredictedValue(), prediction.getState());
    }
}
