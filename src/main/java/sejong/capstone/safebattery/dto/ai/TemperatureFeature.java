package sejong.capstone.safebattery.dto.ai;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.util.ModelFeatureSerializer;

import java.util.List;


@JsonSerialize(using = ModelFeatureSerializer.class)
public record TemperatureFeature(double P_H2_inlet, double P_Air_inlet, double T_Heater,
                                 double T_Stack_inlet) implements ModelFeature {

    public static TemperatureFeature fromEntity(Record r) {
        return new TemperatureFeature(
            r.getP_H2_inlet(),
            r.getP_Air_inlet(),
            r.getT_Heater(),
            r.getT_Stack_inlet());
    }

    @Override
    public List<Double> toList() {
        return List.of(P_H2_inlet, P_Air_inlet, T_Heater, T_Stack_inlet);
    }
}
