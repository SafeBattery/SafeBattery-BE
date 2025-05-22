package sejong.capstone.safebattery.dto.ai;

import java.util.List;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.enums.PredictionState;
import sejong.capstone.safebattery.domain.TemperaturePrediction;

public record TemperaturePredictionResponseDto(List<List<Double>> mask, List<Double> prediction) {

    public TemperaturePrediction toEntity(Pemfc pemfc, double tsec,
        PredictionState predictionState) {
        return TemperaturePrediction.builder()
            .pemfc(pemfc)
            .tsec(tsec)
            .predictedValue(prediction.get(0))
            .state(predictionState)
            .build();
    }

    public double getTemperaturePrediction() {
        return prediction.get(0);
    }
}
