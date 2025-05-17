package sejong.capstone.safebattery.dto;

import sejong.capstone.safebattery.domain.BaseDynamask;

import java.util.List;

public record DynamaskDto(long id, double tsec, PemfcResponseDto pemfc, List<List<Double>> value) {
    public static DynamaskDto fromEntity(BaseDynamask dynamask) {
        return new DynamaskDto(dynamask.getId(), dynamask.getTsec(), PemfcResponseDto.fromEntity(dynamask.getPemfc()), dynamask.getValue());
    }
}
