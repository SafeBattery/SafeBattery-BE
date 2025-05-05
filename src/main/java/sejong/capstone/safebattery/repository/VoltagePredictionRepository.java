package sejong.capstone.safebattery.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.VoltagePrediction;

public interface VoltagePredictionRepository extends JpaRepository<VoltagePrediction, Long> {

    List<VoltagePrediction> findAllByTsecBetween(LocalDateTime startTime, LocalDateTime endTime);
}
