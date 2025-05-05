package sejong.capstone.safebattery.dto;

import java.util.List;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.PowerPrediction;
import sejong.capstone.safebattery.domain.PredictionState;
import sejong.capstone.safebattery.domain.VoltagePrediction;

public record VoltageAndPowerResponseDto(List<List<Double>> masks, List<Double> prediction) {

    public VoltagePrediction toVoltagePrediction(Pemfc pemfc, double tsec, PredictionState predictionState) {
        return VoltagePrediction.builder()
            .pemfc(pemfc)
            .tsec(tsec)
            .predictedValue(this.prediction.get(0))
            .state(predictionState)
            .build();
    }

    public PowerPrediction toPowerPrediction(Pemfc pemfc, double tsec, PredictionState predictionState) {
        return PowerPrediction.builder()
            .pemfc(pemfc)
            .tsec(tsec)
            .predictedValue(this.prediction.get(0))
            .state(predictionState)
            .build();
    }

    public double getVoltagePrediction() {
        return prediction.get(0);
    }

    public double getPowerPrediction() {
        return prediction.get(1);
    }
}
