package sejong.capstone.safebattery.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.enums.PredictionState;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PemfcUpdateDto {
    private Client client;
    private PredictionState powerState;
    private PredictionState voltageState;
    private PredictionState temperatureState;
    private double lat;
    private double lng;
    private String modelName;
    LocalDate manufacturedDate;

    public PemfcUpdateDto() {}

    public PemfcUpdateDto(Client client, PredictionState powerState,
                          PredictionState voltageState,
                          PredictionState temperatureState, double lat, double lng,
                          String modelName, LocalDate manufacturedDate) {
        this.client = client;
        this.powerState = powerState;
        this.voltageState = voltageState;
        this.temperatureState = temperatureState;
        this.lat = lat;
        this.lng = lng;
        this.modelName = modelName;
        this.manufacturedDate = manufacturedDate;
    }
}
