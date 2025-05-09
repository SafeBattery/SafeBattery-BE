package sejong.capstone.safebattery.dao;

import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.enums.PredictionState;

public record PredictionRank(Pemfc pemfc, PredictionState state, long count) {

}
