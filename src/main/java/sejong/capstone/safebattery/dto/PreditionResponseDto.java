package sejong.capstone.safebattery.dto;

import sejong.capstone.safebattery.domain.BasePrediction;
import sejong.capstone.safebattery.enums.PredictionState;

public record PreditionResponseDto(long id, PemfcResponseDto pemfc, double tsec, double predictedValue,
                                   PredictionState state) {

    public static PreditionResponseDto fromEntity(BasePrediction prediction) {
        return new PreditionResponseDto(prediction.getId(),
            PemfcResponseDto.fromEntity(prediction.getPemfc()),
            prediction.getTsec(), prediction.getPredictedValue(), prediction.getState());
    }
}
