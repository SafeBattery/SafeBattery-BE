package sejong.capstone.safebattery.dto.ai;

import java.util.List;

public record PredictionRequestDto<T>(String type, List<T> input) {
}
