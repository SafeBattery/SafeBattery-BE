package sejong.capstone.safebattery.dto.ai;

import java.util.List;

public record TemperaturePredictionRequestDto(String type, List<TemperatureFeature> input, double[] threshold) {

}
