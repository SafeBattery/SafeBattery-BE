package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Prediction;

import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    //save
    //findById
    //findAll
    List<Prediction> findAllByPemfc(Pemfc pemfc);
}
