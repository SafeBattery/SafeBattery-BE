package sejong.capstone.safebattery.dto;

import lombok.Getter;
import sejong.capstone.safebattery.enums.PredictionState;
import java.time.LocalDate;

@Getter
public class PemfcRequestDto {
    private Long clientId;
    private PredictionState powerVoltageState;
    private PredictionState temperatureState;
    private double lat;
    private double lng;
    private String modelName;
    private LocalDate manufacturedDate;
}
