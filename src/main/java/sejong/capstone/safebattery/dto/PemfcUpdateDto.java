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
    private PredictionState state;
    private double lat;
    private double lng;
    private String modelName;
    LocalDate manufacturedDate;

    public PemfcUpdateDto() {}

    public PemfcUpdateDto(Client client, PredictionState state, double lat, double lng,
                          String modelName, LocalDate manufacturedDate) {
        this.client = client;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
        this.modelName = modelName;
        this.manufacturedDate = manufacturedDate;
    }
}
