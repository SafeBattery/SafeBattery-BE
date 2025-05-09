package sejong.capstone.safebattery.dto;

import lombok.Getter;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.enums.PredictionState;

import java.time.LocalDate;

@Getter
public class PemfcResponseDto {
    private Long id;
    private Long clientId;
    private PredictionState powerVoltageState;
    private PredictionState temperatureState;
    private double lat;
    private double lng;
    private String modelName;
    LocalDate manufacturedDate;

    public PemfcResponseDto() {}

    public PemfcResponseDto(Pemfc pemfc) {
        this.id = pemfc.getId();
        this.clientId = pemfc.getClient().getId();
        this.powerVoltageState = pemfc.getPowerVoltageState();
        this.temperatureState = pemfc.getTemperatureState();
        this.lat = pemfc.getLat();
        this.lng = pemfc.getLng();
        this.modelName = pemfc.getModelName();
        this.manufacturedDate = pemfc.getManufacturedDate();
    }
    public static PemfcResponseDto fromEntity(Pemfc pemfc) {
        return new PemfcResponseDto(pemfc);
    }
}
