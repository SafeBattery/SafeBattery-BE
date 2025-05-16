package sejong.capstone.safebattery.dto.ai;

import java.util.List;

public record VoltageAndPowerRequestDto(String model_type, List<VoltageAndPowerFeature> input, double[] threshold) {

}
