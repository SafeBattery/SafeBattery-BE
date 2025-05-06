package sejong.capstone.safebattery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.safebattery.dto.PowerPredictionRankDto;
import sejong.capstone.safebattery.dto.VoltagePredictionRankDto;
import sejong.capstone.safebattery.service.RankService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class RankController {
    private final RankService rankService;

    @GetMapping("/voltage")
    public List<VoltagePredictionRankDto> getVoltagePredictionRank() {
        return rankService.getVoltagePredictionRankOfRecent100Predictions().stream().map(VoltagePredictionRankDto::fromEntity).toList();
    }
    @GetMapping("/power")
    public List<PowerPredictionRankDto> getPowerPredictionRank() {
        return rankService.getPowerPredictionRankOfRecent100Predictions().stream().map(PowerPredictionRankDto::fromEntity).toList();
    }
}
