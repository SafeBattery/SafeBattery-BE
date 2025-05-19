package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.TemperaturePrediction;

import java.util.List;

public interface TemperaturePredictionRepository extends
    JpaRepository<TemperaturePrediction, Long> {

    //특정 pemfc의 가장 최근 tsec을 가진 500개의 PowerPrediction들을 반환
    List<TemperaturePrediction> findTop500ByPemfcOrderByTsecDesc(Pemfc pemfc);

    List<TemperaturePrediction> findAllByPemfcId(long pemfcId);
}
