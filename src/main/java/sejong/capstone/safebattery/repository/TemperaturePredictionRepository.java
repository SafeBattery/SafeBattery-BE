package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.TemperaturePrediction;

public interface TemperaturePredictionRepository extends JpaRepository<TemperaturePrediction, Long> {

}
