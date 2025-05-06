package sejong.capstone.safebattery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sejong.capstone.safebattery.domain.PredictionState;
import sejong.capstone.safebattery.domain.VoltagePrediction;
import sejong.capstone.safebattery.dto.PredictionRank;

public interface VoltagePredictionRepository extends JpaRepository<VoltagePrediction, Long> {
    @Query(value = """
            SELECT new sejong.capstone.safebattery.dto.PredictionRank(
                v.pemfc,
                v.state,
                COUNT(v.id)
            )
            FROM VoltagePrediction v
            WHERE v.state = :state
            GROUP BY v.pemfc
            ORDER BY COUNT(v.id) DESC
            """)
    List<PredictionRank> countIdGroupByPemfcWhereStateDesc(@Param("state") PredictionState state);
}
