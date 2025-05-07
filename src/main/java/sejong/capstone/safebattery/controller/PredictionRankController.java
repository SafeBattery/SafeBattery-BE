package sejong.capstone.safebattery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.safebattery.dto.PowerPredictionRankDto;
import sejong.capstone.safebattery.dto.TemperaturePredictionRankDto;
import sejong.capstone.safebattery.dto.VoltagePredictionRankDto;
import sejong.capstone.safebattery.service.PredictionRankService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class PredictionRankController {

    private final PredictionRankService rankService;

    @GetMapping("/voltage")
    public List<VoltagePredictionRankDto> getVoltagePredictionRank() {
        List<VoltagePredictionRankDto> list = rankService.getVoltagePredictionRanks()
            .stream().map(VoltagePredictionRankDto::fromEntity).toList();
        return list;
    }

    @GetMapping("/power")
    public List<PowerPredictionRankDto> getPowerPredictionRank() {
        List<PowerPredictionRankDto> list = rankService.getPowerPredictionRanks()
            .stream().map(PowerPredictionRankDto::fromEntity).toList();
        return list;
    }

    @GetMapping("/temperature")
    public List<TemperaturePredictionRankDto> getTemperaturePredictionRank() {
        List<TemperaturePredictionRankDto> list = rankService.getTemperatureRanks().stream()
            .map(TemperaturePredictionRankDto::fromEntity).toList();
        return list;
    }
}
