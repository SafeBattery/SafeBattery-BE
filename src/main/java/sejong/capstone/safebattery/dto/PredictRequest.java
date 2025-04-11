package sejong.capstone.safebattery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PredictRequest {
    private List<List<Double>> input;
    private double target;
}
