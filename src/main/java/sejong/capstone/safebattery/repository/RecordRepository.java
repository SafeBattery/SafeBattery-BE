package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    //save
    //findById
    //findAll
    List<Record> findAllByPemfc(Pemfc pemfc);
    List<Record> findTop600ByPemfcOrderByTsecDesc(Pemfc pemfc);
    long countByPemfc(Pemfc pemfc);
}
