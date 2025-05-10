package sejong.capstone.safebattery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.safebattery.dto.PredictionRankDto;
import sejong.capstone.safebattery.service.PredictionRankService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class PredictionRankController {

    private final PredictionRankService rankService;

    @GetMapping("/voltage")
    public List<PredictionRankDto> getVoltagePredictionRank() {
        List<PredictionRankDto> list = rankService.getVoltagePredictionRanks()
            .stream().map(PredictionRankDto::fromEntity).toList();
        return list;
    }

    @GetMapping("/power")
    public List<PredictionRankDto> getPowerPredictionRank() {
        List<PredictionRankDto> list = rankService.getPowerPredictionRanks()
            .stream().map(PredictionRankDto::fromEntity).toList();
        return list;
    }

    @GetMapping("/temperature")
    public List<PredictionRankDto> getTemperaturePredictionRank() {
        List<PredictionRankDto> list = rankService.getTemperatureRanks().stream()
            .map(PredictionRankDto::fromEntity).toList();
        return list;
    }
}
