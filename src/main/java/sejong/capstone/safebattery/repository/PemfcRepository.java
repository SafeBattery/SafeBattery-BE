package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.Pemfc;

public interface PemfcRepository extends JpaRepository<Pemfc, Long> {
    //save
    //findById
    //findAll
}
