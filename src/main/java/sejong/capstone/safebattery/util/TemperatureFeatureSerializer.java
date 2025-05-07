package sejong.capstone.safebattery.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import sejong.capstone.safebattery.dto.TemperatureFeature;

public class TemperatureFeatureSerializer extends JsonSerializer<TemperatureFeature> {

    @Override
    public void serialize(TemperatureFeature feature, JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeArray(
            new double[]{
                feature.P_H2_inlet(),
                feature.P_Air_inlet(),
                feature.T_Heater(),
                feature.T_Stack_inlet(),
            }, 0, 4);
    }
}
