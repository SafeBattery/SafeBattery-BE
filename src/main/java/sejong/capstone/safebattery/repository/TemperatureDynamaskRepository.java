package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.TemperatureDynamask;

import java.util.Optional;

public interface TemperatureDynamaskRepository extends JpaRepository<TemperatureDynamask, Long> {
    //해당 Pemfc의 최신 dynamask 객체를 반환
    Optional<TemperatureDynamask> findTopByPemfcOrderByTsecDesc(Pemfc pemfc);
}
