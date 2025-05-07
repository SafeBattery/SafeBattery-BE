package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.PowerPrediction;

public interface PowerPredictionRepository extends JpaRepository<PowerPrediction, Long> {
}
