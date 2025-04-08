package sejong.capstone.safebattery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.safebattery.service.CsvService;

@Slf4j
@RestController
@RequestMapping("/api/pemfc")
@RequiredArgsConstructor
public class PemfcController {
    private final CsvService csvService;

    @GetMapping("/{pemfcId}/start")
    public ResponseEntity<String> startCsvToRecordScheduler
            (@PathVariable Long pemfcId) {
        csvService.startScheduler(pemfcId);
        return ResponseEntity.ok("스케줄러가 시작되었습니다.");
    }
}
