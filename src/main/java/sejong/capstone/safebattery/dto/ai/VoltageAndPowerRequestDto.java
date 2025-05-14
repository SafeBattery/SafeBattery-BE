package sejong.capstone.safebattery.dto.ai;

import java.util.List;

public record VoltageAndPowerRequestDto(List<VoltageAndPowerFeature> input, double[] threshold) {

}
