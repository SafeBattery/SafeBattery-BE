package sejong.capstone.safebattery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.repository.RecordRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;

    public Record addNewRecord(Record record) {
        return recordRepository.save(record);
    }

    public Optional<Record> searchRecordById(Long id) {
        return recordRepository.findById(id);
    }

    public List<Record> searchAllRecords() {
        return recordRepository.findAll();
    }

    public List<Record> searchRecordsByPemfc(Pemfc pemfc) {
        return recordRepository.findAllByPemfc(pemfc);
    }

    public List<Record> search600RecordsByPemfc(Pemfc pemfc) {
        return recordRepository.findTop600ByPemfcOrderByTsecDesc(pemfc);
    }
}
