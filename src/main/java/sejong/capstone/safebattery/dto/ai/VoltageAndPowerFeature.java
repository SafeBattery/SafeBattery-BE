package sejong.capstone.safebattery.dto.ai;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.util.ModelFeatureSerializer;

import java.util.List;

@JsonSerialize(using = ModelFeatureSerializer.class)
public record VoltageAndPowerFeature(
        double iA,
        double iA_diff,
        double P_H2_supply,
        double P_H2_inlet,
        double P_air_supply,
        double P_air_inlet,
        double m_Air_write,
        double m_H2_write,
        double T_Stack_inlet
) implements ModelFeature {

    public static VoltageAndPowerFeature fromEntity(Record r, double iA_diff) {
        return new VoltageAndPowerFeature(
                r.getIA(),
                iA_diff,
                r.getP_H2_supply(),
                r.getP_H2_inlet(),
                r.getP_Air_supply(),
                r.getP_Air_inlet(),
                r.getM_Air_write(),
                r.getM_H2_write(),
                r.getT_Stack_inlet()
        );
    }

    @Override
    public List<Double> toList() {
        return List.of(
                iA,
                iA_diff,
                P_H2_supply,
                P_H2_inlet,
                P_air_supply,
                P_air_inlet,
                m_Air_write,
                m_H2_write,
                T_Stack_inlet);
    }
}
