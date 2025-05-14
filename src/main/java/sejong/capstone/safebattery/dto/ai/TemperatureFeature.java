package sejong.capstone.safebattery.dto.ai;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.util.TemperatureFeatureSerializer;


@JsonSerialize(using = TemperatureFeatureSerializer.class)
public record TemperatureFeature(double P_H2_inlet, double P_Air_inlet, double T_Heater,
                                 double T_Stack_inlet) {

    public static TemperatureFeature fromEntity(Record r) {
        return new TemperatureFeature(
            r.getP_H2_inlet(),
            r.getP_Air_inlet(),
            r.getT_Heater(),
            r.getT_Stack_inlet());
    }
}
