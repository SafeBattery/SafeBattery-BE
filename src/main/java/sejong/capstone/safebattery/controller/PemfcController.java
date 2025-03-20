package sejong.capstone.safebattery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.dto.PemfcResponseDto;
import sejong.capstone.safebattery.service.PemfcService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pemfc")
@RequiredArgsConstructor
public class PemfcController {
    private final PemfcService service;

    @PostMapping("/create")
    public PemfcResponseDto createNewRow(@Valid @ModelAttribute Pemfc row,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //결측치 발생 시 수행 로직 추가...
        }

        return new PemfcResponseDto(service.addNewRow(row));
    }

    @GetMapping("/read/{id}")
    public PemfcResponseDto searchRowById(@PathVariable("id") Long id) {
        return new PemfcResponseDto(service.searchRowById(id).orElseThrow());
    }

    @GetMapping("/read/all")
    public List<PemfcResponseDto> searchAllRows() {
        return service.searchAllRows().stream()
                .map(PemfcResponseDto::new).toList();
    }
}
