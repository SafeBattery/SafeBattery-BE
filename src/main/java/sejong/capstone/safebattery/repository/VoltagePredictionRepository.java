package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.VoltagePrediction;

public interface VoltagePredictionRepository extends JpaRepository<VoltagePrediction, Long> {
}
