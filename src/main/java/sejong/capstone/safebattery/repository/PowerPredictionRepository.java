package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sejong.capstone.safebattery.domain.PowerPrediction;
import sejong.capstone.safebattery.domain.PredictionState;
import sejong.capstone.safebattery.dto.PredictionRank;

import java.util.List;

public interface PowerPredictionRepository extends JpaRepository<PowerPrediction, Long> {
    @Query(value = """
            SELECT new sejong.capstone.safebattery.dto.PredictionRank(
                p.pemfc,
                p.state,
                COUNT(p.id)
            )
            FROM PowerPrediction p
            WHERE p.state = :state
            GROUP BY p.pemfc
            ORDER BY COUNT(p.id) DESC
            """)
    List<PredictionRank> countIdGroupByPemfcWhereStateDesc(@Param("state") PredictionState state);
}
