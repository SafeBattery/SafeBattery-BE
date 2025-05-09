package sejong.capstone.safebattery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.RecordResponseDto;
import sejong.capstone.safebattery.service.RecordService;
import sejong.capstone.safebattery.util.StatePolicy;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService service;

    @PostMapping("/")
    public RecordResponseDto createNewRecord(@Valid @ModelAttribute Record record,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //  todo : 결측치 발생 시 수행 로직 추가
        }

        return new RecordResponseDto(service.addNewRecord(record));
    }

    @GetMapping("/{recordId}")
    public RecordResponseDto searchRecordById(@PathVariable("recordId") Long id) {
        return new RecordResponseDto(service.searchRecordById(id).orElseThrow());
    }

    @GetMapping("/all")
    public List<RecordResponseDto> searchAllRecords() {
        return service.searchAllRecords().stream()
                .map(RecordResponseDto::new).toList();
    }
}
