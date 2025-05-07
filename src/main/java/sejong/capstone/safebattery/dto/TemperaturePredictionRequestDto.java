package sejong.capstone.safebattery.dto;

import java.util.List;

public record TemperaturePredictionRequestDto(List<TemperatureFeature> input, double[] threshold) {

}
