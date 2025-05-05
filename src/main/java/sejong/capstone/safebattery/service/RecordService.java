package sejong.capstone.safebattery.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;
import sejong.capstone.safebattery.dto.RecordCsvDto;
import sejong.capstone.safebattery.repository.PemfcRepository;
import sejong.capstone.safebattery.repository.RecordRepository;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final PemfcRepository pemfcRepository;

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


    public long countRecordsByPemfc(Pemfc pemfc) {
        return recordRepository.countByPemfc(pemfc);
    }

    public void add600RowsFromCsv(Long pemfcId) throws IOException {
        Pemfc pemfc = pemfcRepository.findById(pemfcId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pemfc ID"));

        // ✅ classpath 기준 리소스 접근
        InputStream inputStream = new ClassPathResource("full_test_data.csv").getInputStream();
        Reader reader = new InputStreamReader(inputStream);

        CsvToBean<RecordCsvDto> csvToBean = new CsvToBeanBuilder<RecordCsvDto>(reader)
                .withType(RecordCsvDto.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withSeparator(',')
                .build();

        List<Record> records = csvToBean.stream()
                .limit(600)
                .map(dto -> dto.convert(pemfc))
                .toList();

        recordRepository.saveAll(records);
    }
}
