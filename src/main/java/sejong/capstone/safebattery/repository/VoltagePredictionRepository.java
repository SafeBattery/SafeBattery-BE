package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.VoltagePrediction;

import java.util.List;

public interface VoltagePredictionRepository extends JpaRepository<VoltagePrediction, Long> {
    //특정 pemfc의 가장 최근 tsec을 가진 100개의 VoltagePrediction들을 반환
    List<VoltagePrediction> findTop100ByPemfcOrderByTsecDesc(Pemfc pemfc);
}
