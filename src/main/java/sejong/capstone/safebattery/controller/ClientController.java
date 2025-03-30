package sejong.capstone.safebattery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.PemfcResponseDto;
import sejong.capstone.safebattery.dto.RecordResponseDto;
import sejong.capstone.safebattery.service.ClientService;
import sejong.capstone.safebattery.service.PemfcService;
import sejong.capstone.safebattery.service.RecordService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final PemfcService pemfcService;
    private final RecordService recordService;

    @GetMapping("/{clientId}")
    public List<PemfcResponseDto> getPemfcListOfClient(@PathVariable("clientId") Long clientId) {
        Client client = clientService.searchClientById(clientId).orElseThrow();
        return pemfcService.searchPemfcsByClient(client).stream().map(
                PemfcResponseDto::new).toList();
    }

    @GetMapping("/{clientId}/{pemfcId}")
    public PemfcResponseDto getPemfcOfClientByPemfcId(
            @PathVariable("clientId") Long clientId,
            @PathVariable("pemfcId") Long pemfcId) {
        Client client = clientService.searchClientById(clientId).orElseThrow();
        Pemfc pemfc = pemfcService.searchPemfcsByClient(client).stream()
                .filter(p -> p.getId().equals(pemfcId))
                .findFirst().orElseThrow();
        return new PemfcResponseDto(pemfc);
    }

    @PostMapping("/{clientId}")
    public String addNewPemfcOfClient(@PathVariable("clientId") Long clientId) {
        Client client = clientService.searchClientById(clientId).orElseThrow();
        pemfcService.addNewPemfc(new Pemfc(client));

        return "pemfc add success";
    }

    @DeleteMapping("/{clientId}/{pemfcId}")
    public String DeletePemfcOfClient(
            @PathVariable("clientId") Long clientId,
            @PathVariable("pemfcId") Long pemfcId) {

        pemfcService.deletePemfcById(pemfcId);

        return "pemfc delete success";
    }

    @GetMapping("/{clientId}/{pemfcId}/history")
    public List<RecordResponseDto> getRecordsOfPemfc(
            @PathVariable("clientId") Long clientId,
            @PathVariable("pemfcId") Long pemfcId) {
        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        return recordService.searchRecordsByPemfc(pemfc).stream()
                .map(RecordResponseDto::new).toList();
    }

    @GetMapping("/{clientId}/{pemfcId}/prediction")
    public String getPredictionOfPemfc(
            @PathVariable("clientId") Long clientId,
            @PathVariable("pemfcId") Long pemfcId) {
        Pemfc pemfc = pemfcService.searchPemfcById(pemfcId).orElseThrow();
        List<RecordResponseDto> records = recordService.searchRecordsByPemfc(pemfc)
                .stream().map(RecordResponseDto::new).toList();

        //prediction logic...

        return "normal";
    }
}
