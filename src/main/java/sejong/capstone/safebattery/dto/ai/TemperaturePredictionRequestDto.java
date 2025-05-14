package sejong.capstone.safebattery.dto.ai;

import java.util.List;

public record TemperaturePredictionRequestDto(List<TemperatureFeature> input, double[] threshold) {

}
