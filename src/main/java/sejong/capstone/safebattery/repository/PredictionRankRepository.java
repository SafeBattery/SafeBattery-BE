package sejong.capstone.safebattery.repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import sejong.capstone.safebattery.dao.RawPredictionRank;
import sejong.capstone.safebattery.domain.BasePrediction;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.enums.PredictionState;
import sejong.capstone.safebattery.dao.PredictionRank;
import sejong.capstone.safebattery.util.TableNameResolver;

@RequiredArgsConstructor
public class PredictionRankRepository<T extends BasePrediction> {

    private final JdbcTemplate jdbcTemplate;
    private final PemfcRepository pemfcRepository;
    private final Class<T> domainClassType;
    private final String sql = """
            SELECT
              t.pemfc_id   AS pemfcId,
              t.state      AS state,
              COUNT(t.id)  AS cnt
            FROM (
              SELECT *
                FROM %s
               WHERE state = ?
               ORDER BY tsec DESC
               LIMIT ?
            ) t
            GROUP BY t.pemfc_id, t.state
            ORDER BY cnt DESC
        """;

    public List<PredictionRank> getPredictionRanks() {
        // todo: recordSize에 대해서 논의 필요
        //  PredictionState.ERROR로 변경하기
        List<RawPredictionRank> rawPredictionRanks = this.getRawPredictionRanks(
            PredictionState.NORMAL, 10);
        return this.mapFromRawPredictionRankToPredictionRank(rawPredictionRanks);
    }

    protected List<RawPredictionRank> getRawPredictionRanks(PredictionState state,
        long recordSize) {
        String formattedSql = this.generateSql();

        List<RawPredictionRank> raws = jdbcTemplate.query(formattedSql,
            new Object[]{state.name(), recordSize},
            (rs, rowNum) -> new RawPredictionRank(rs.getLong("pemfcId"), rs.getString("state"),
                rs.getLong("cnt")));
        if (raws.isEmpty()) {
            return Collections.emptyList();
        }

        return raws;
    }

    private String generateSql() {
        String tableName = TableNameResolver.resolve(domainClassType);
        return String.format(sql, tableName);
    }

    private List<PredictionRank> mapFromRawPredictionRankToPredictionRank(
        List<RawPredictionRank> rawPredictionRanks) {

        List<Long> pemfcIds = rawPredictionRanks.stream().map(RawPredictionRank::pemfcId).distinct()
            .toList();

        List<Pemfc> pemfcs = pemfcRepository.findAllById(pemfcIds);
        Map<Long, Pemfc> pemfcMap = pemfcs.stream()
            .collect(Collectors.toMap(Pemfc::getId, Function.identity()));

        return rawPredictionRanks.stream().map(r -> new PredictionRank(pemfcMap.get(r.pemfcId()),
            PredictionState.valueOf(r.predictionState()), r.count())).toList();
    }
}
