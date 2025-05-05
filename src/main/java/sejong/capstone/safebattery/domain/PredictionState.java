package sejong.capstone.safebattery.domain;

import lombok.Getter;

@Getter
public enum PredictionState {
    // 정상 상태
    NORMAL("normal"),
    // 주의 상태
    WARNING("warning"),
    // 이상 상태
    ERROR("error");

    private final String name;

    PredictionState(String name) {
        this.name = name;
    }

}
