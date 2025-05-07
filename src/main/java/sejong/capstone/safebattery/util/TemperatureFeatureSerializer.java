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
                feature.a(),
                feature.b(),
                feature.c(),
                feature.d(),
            }, 0, 4);
    }
}
