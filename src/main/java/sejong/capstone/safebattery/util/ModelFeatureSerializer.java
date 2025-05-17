package sejong.capstone.safebattery.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

import sejong.capstone.safebattery.dto.ai.ModelFeature;

public class ModelFeatureSerializer extends JsonSerializer<ModelFeature> {
    @Override
    public void serialize(ModelFeature feature, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<Double> features = feature.toList();

        double[] array = new double[features.size()];
        for (int i = 0; i < features.size(); i++) {
            array[i] = features.get(i);
        }
        jsonGenerator.writeArray(array, 0, features.size());
    }
}
