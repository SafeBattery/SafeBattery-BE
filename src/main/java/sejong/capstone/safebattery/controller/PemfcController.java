package sejong.capstone.safebattery.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sejong.capstone.safebattery.Constants;
import sejong.capstone.safebattery.domain.*;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.*;
import sejong.capstone.safebattery.service.*;

import java.util.List;

import static sejong.capstone.safebattery.util.StatePolicy.*;

@Slf4j
@RestController
@RequestMapping("/api/pemfc")
@RequiredArgsConstructor
public class PemfcController {

    private final ClientService clientService;
    private final PemfcService pemfcService;
    private final RecordService recordService;
    private final PredictionService predictionService;
    private final DynamaskService dynamaskService;

    @GetMapping("{pemfcId}")
    public PemfcResponseDto getPemfcById(@PathVariable("pemfcId") Long pemfcId) {
        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        return new PemfcResponseDto(pemfc);
    }

    @PostMapping("/")
    public ResponseEntity<String> addNewPemfc(@Valid @RequestBody PemfcRequestDto form,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //결측치 발생 시 수행 로직 추가...
            log.info("Invalid pemfc: {}", bindingResult.getAllErrors());
        }
        Client client = clientService.searchClientById(form.getClientId()).orElseThrow();
        pemfcService.addNewPemfc(
            new Pemfc(client, form.getPowerState(), form.getVoltageState(), form.getTemperatureState(),
                    form.getLat(), form.getLng(), form.getModelName(), form.getManufacturedDate()));

        return ResponseEntity.ok("pemfc가 성공적으로 추가되었습니다.");
    }

    @DeleteMapping("/{pemfcId}/delete")
    public ResponseEntity<String> DeletePemfcOfClient(@PathVariable("pemfcId") Long pemfcId) {

        pemfcService.deletePemfcById(pemfcId);

        return ResponseEntity.ok("pemfc가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/{pemfcId}/record/all")
    public List<RecordResponseDto> getRecordsOfPemfc(@PathVariable("pemfcId") Long pemfcId) {
        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        return recordService.searchRecordsByPemfc(pemfc).stream().map(RecordResponseDto::new)
            .toList();
    }

    @GetMapping("/{pemfcId}/record/recent600")
    public List<RecordResponseDto> getRecent600RecordsOfPemfc(
        @PathVariable("pemfcId") Long pemfcId) {
        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        return recordService.search600RecordsByPemfc(pemfc).stream().map(RecordResponseDto::new)
            .toList();
    }

    @GetMapping("/{pemfcId}/csv")
    public void getRecordCsvOfPemfc(@PathVariable("pemfcId") Long pemfcId,
        HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
            "attachment; filename=\"csv_of_pemfc_" + pemfcId + ".csv\"");
        recordService.makeCsvByRecordsOfPemfc(pemfcId, response.getWriter());
    }

    /**
     * 현재 pemfc의 고장 상태를 예측, 실제 예측 정보를 가져와서 고장 유형에 따라 전류, 전압, 온도?로 나누어 저장하기
     */
    @PostMapping("/{pemfcId}/record")
    public ResponseEntity<String> addNewRecordAndGetPrediction(@PathVariable Long pemfcId,
        @Valid @RequestBody RecordRequestDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // 결측치 발생 시 수행 로직 추가...
            log.info("Invalid record: {}", bindingResult.getAllErrors());
        }

        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        //pemfc 위치 최신화
        Record record = dto.toEntity(pemfc);
        pemfcService.updatePemfcLocation(pemfcId, record.getLat(), record.getLng());

        record.setPemfc(pemfc);
        // 현재 record 값을 토대로 현재 state를 도출
        record.setPowerState(getCurrentPowerState(
            record.getPW()));
        record.setVoltageState(getCurrentVoltageState(
                record.getU_totV()));
        record.setTemperatureState(getCurrentTemperatureState(
            record.getT_3()));
        recordService.addNewRecord(record);
        log.info("added record : {}", record);

        if (recordService.countRecordsByPemfc(pemfc) > 3000) {
            List<Record> aiServerRequestData = recordService.search3000RecordsByPemfc(pemfc);
            //  todo : 여기서 해당 pemfc의 state값이 업데이트되어야 함. dynamask도 여기서 저장됨
            log.info("aiServerRequestData send");
            predictionService.createPredictionsAndChangeState(aiServerRequestData);
            log.info("prediction created");
            return ResponseEntity.ok("record와 prediction이 추가되었습니다.");
        } else {
            return ResponseEntity.ok("record가 추가되었습니다.");
        }
    }

    @GetMapping("/{pemfcId}/predictions/voltage")
    public ResponseEntity<List<PredictionResponseDto>> getVoltagePredictions(@PathVariable Long pemfcId) {
        return ResponseEntity.ok(predictionService.getVoltagePredictions(pemfcId).stream().map(
            PredictionResponseDto::fromEntity).toList());
    }

    @GetMapping("/{pemfcId}/predictions/power")
    public ResponseEntity<List<PredictionResponseDto>> getPowerPredictions(@PathVariable Long pemfcId) {
        return ResponseEntity.ok(predictionService.getPowerPredictions(pemfcId).stream().map(
            PredictionResponseDto::fromEntity).toList());
    }

    @GetMapping("/{pemfcId}/predictions/temperature")
    public ResponseEntity<List<PredictionResponseDto>> getTemperaturePredictions(@PathVariable Long pemfcId) {
        return ResponseEntity.ok(predictionService.getTemperaturePredictions(pemfcId).stream().map(
            PredictionResponseDto::fromEntity).toList());
    }

    @GetMapping("/{pemfcId}/predictions/voltage/recent100")
    public ResponseEntity<List<PredictionResponseDto>> getRecent100VoltagePredictions(@PathVariable Long pemfcId) {
        return ResponseEntity.ok(predictionService.getRecent100VoltagePredictions(pemfcId).stream().map(
                PredictionResponseDto::fromEntity).toList());
    }

    @GetMapping("/{pemfcId}/predictions/power/recent100")
    public ResponseEntity<List<PredictionResponseDto>> getRecent100PowerPredictions(@PathVariable Long pemfcId) {
        return ResponseEntity.ok(predictionService.getRecent100PowerPredictions(pemfcId).stream().map(
                PredictionResponseDto::fromEntity).toList());
    }

    @GetMapping("/{pemfcId}/predictions/temperature/recent20")
    public ResponseEntity<List<PredictionResponseDto>> getRecent20TemperaturePredictions(@PathVariable Long pemfcId) {
        return ResponseEntity.ok(predictionService.getRecent20TemperaturePredictions(pemfcId).stream().map(
                PredictionResponseDto::fromEntity).toList());
    }

    @GetMapping("/{pemfcId}/dynamask/voltagepower/recent")
    public ResponseEntity<DynamaskDto> getRecentVoltagePowerDynamask(@PathVariable Long pemfcId) {
        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        VoltagePowerDynamask dynamask = dynamaskService.searchRecentVoltagePowerDynamask(pemfc).orElseThrow();
        return ResponseEntity.ok(DynamaskDto.fromEntity(dynamask));
    }

    @GetMapping("/{pemfcId}/dynamask/temperature/recent")
    public ResponseEntity<DynamaskDto> getRecentTemperatureDynamask(@PathVariable Long pemfcId) {
        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        TemperatureDynamask dynamask = dynamaskService.searchRecentTemperatureDynamask(pemfc).orElseThrow();
        return ResponseEntity.ok(DynamaskDto.fromEntity(dynamask));
    }
}
