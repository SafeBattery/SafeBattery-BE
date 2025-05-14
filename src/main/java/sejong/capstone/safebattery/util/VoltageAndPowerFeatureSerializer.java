package sejong.capstone.safebattery.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import sejong.capstone.safebattery.dto.ai.VoltageAndPowerFeature;

public class VoltageAndPowerFeatureSerializer extends JsonSerializer<VoltageAndPowerFeature> {
    //        double iA,
//        double P_H2_supply,
//        double P_H2_inlet,
//        double m_Air_write,
//        double m_H2_write,
//        double P_air_supply,
//        double P_air_inlet,
//        double T_Stack_inlet,
//        double iA_diff) {
    @Override
    public void serialize(VoltageAndPowerFeature feature, JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeArray(
            new double[]{
                feature.iA(),
                feature.P_H2_supply(),
                feature.P_H2_inlet(),
                feature.m_Air_write(),
                feature.m_H2_write(),
                feature.P_air_supply(),
                feature.P_air_inlet(),
                feature.T_Stack_inlet(),
                feature.iA_diff()
            }, 0, 9);
    }
}
