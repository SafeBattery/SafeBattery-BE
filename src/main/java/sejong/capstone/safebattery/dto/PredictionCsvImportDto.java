package sejong.capstone.safebattery.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.*;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.PowerPrediction;
import sejong.capstone.safebattery.domain.TemperaturePrediction;
import sejong.capstone.safebattery.domain.VoltagePrediction;
import sejong.capstone.safebattery.enums.PredictionState;

@Getter
@Setter
public class PredictionCsvImportDto {
    @CsvBindByName(column = "tsec")
    private double tsec;
    @CsvBindByName(column = "predicted_value")
    private double predictedValue;
    @CsvBindByName(column = "pemfc_id")
    private Long pemfcId;
    @CsvBindByName(column = "state")
    private PredictionState state;

    public VoltagePrediction toVoltagePredictionEntity(Pemfc pemfc) {
        return VoltagePrediction.builder()
                .tsec(tsec)
                .predictedValue(predictedValue)
                .pemfc(pemfc)
                .state(state)
                .build();
    }

    public PowerPrediction toPowerPredictionEntity(Pemfc pemfc) {
        return PowerPrediction.builder()
                .tsec(tsec)
                .predictedValue(predictedValue)
                .pemfc(pemfc)
                .state(state)
                .build();
    }

    public TemperaturePrediction toTemperaturePredictionEntity(Pemfc pemfc) {
        return TemperaturePrediction.builder()
                .tsec(tsec)
                .predictedValue(predictedValue)
                .pemfc(pemfc)
                .state(state)
                .build();
    }
}
