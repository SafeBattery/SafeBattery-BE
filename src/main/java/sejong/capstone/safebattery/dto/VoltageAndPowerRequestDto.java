package sejong.capstone.safebattery.dto;

import java.util.List;

public record VoltageAndPowerRequestDto(List<VoltageAndPowerFeature> dtos, double[] threshold) {

}
