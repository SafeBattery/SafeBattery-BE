package sejong.capstone.safebattery.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;
import sejong.capstone.safebattery.domain.PredictionState;

@Converter
public class PredictionStateConverter implements AttributeConverter<PredictionState, String> {

    @Override
    public String convertToDatabaseColumn(PredictionState predictionState) {
        if (predictionState == null) {
            return null;
        }
        return predictionState.name();
    }

    @Override
    public PredictionState convertToEntityAttribute(String s) {
        return Stream.of(PredictionState.values()).filter(
            state -> state.name().equals(s)
        ).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
