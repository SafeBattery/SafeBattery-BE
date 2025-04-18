package sejong.capstone.safebattery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.RecordResponseDto;
import sejong.capstone.safebattery.service.RecordService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService service;

    @PostMapping("/create")
    public RecordResponseDto createNewRecord(@Valid @ModelAttribute Record record,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //결측치 발생 시 수행 로직 추가...
        }

        return new RecordResponseDto(service.addNewRecord(record));
    }

    @GetMapping("/read/{id}")
    public RecordResponseDto searchRecordById(@PathVariable("id") Long id) {
        return new RecordResponseDto(service.searchRecordById(id).orElseThrow());
    }

    @GetMapping("/read/all")
    public List<RecordResponseDto> searchAllRecords() {
        return service.searchAllRecords().stream()
                .map(RecordResponseDto::new).toList();
    }
}
