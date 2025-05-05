package sejong.capstone.safebattery.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.util.VoltageAndPowerFeatureSerializer;

@JsonSerialize(using= VoltageAndPowerFeatureSerializer.class)
public record VoltageAndPowerFeature(
    double iA,
    double P_H2_supply,
    double P_H2_inlet,
    double m_Air_write,
    double m_H2_write,
    double P_air_supply,
    double P_air_inlet,
    double T_Stack_inlet,
    double iA_diff) {

    public static VoltageAndPowerFeature fromEntity(Record r, double iA_diff) {
        return new VoltageAndPowerFeature(
            r.getIA(),
            r.getP_H2_supply(),
            r.getP_H2_inlet(),
            r.getM_Air_write(),
            r.getM_H2_write(),
            r.getP_Air_supply(),
            r.getP_Air_inlet(),
            r.getT_Stack_inlet(),
            iA_diff
        );
    }
}
