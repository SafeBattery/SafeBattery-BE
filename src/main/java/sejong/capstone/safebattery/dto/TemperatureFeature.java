package sejong.capstone.safebattery.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.util.TemperatureFeatureSerializer;


// todo: 필드 이름 적절하게 바꾸기
@JsonSerialize(using= TemperatureFeatureSerializer.class)
public record TemperatureFeature(double a, double b, double c, double d) {
    public static TemperatureFeature fromEntity(Record r) {
        return new TemperatureFeature(0.0, 0.0, 0.0,0.0);
    }
}
