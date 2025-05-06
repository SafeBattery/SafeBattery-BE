package sejong.capstone.safebattery.dto;

import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.PredictionState;

public record PredictionRank(Pemfc pemfc, PredictionState state, long count) {}
