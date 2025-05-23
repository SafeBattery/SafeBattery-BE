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
        WITH limited_preds AS (
                     SELECT
                         p.*,
                     ROW_NUMBER() OVER (
                 PARTITION BY p.pemfc_id
                 ORDER BY p.tsec DESC
                     ) AS rn
                 FROM
                 %s p
            )
        SELECT
            l.pemfc_id  AS pemfcId,
            COUNT(*)    AS totalCount,
            SUM(CASE WHEN state = 'ERROR' THEN 1 ELSE 0 END) AS errorCount,
            AVG(CASE WHEN state = 'ERROR' THEN 1.0 ELSE 0 END)   AS errorRate
        FROM
            limited_preds l
        WHERE
            rn <= ?
        GROUP BY
            pemfc_id
        ORDER BY
            errorRate DESC;
        """;

    public List<PredictionRank> getPredictionRanks(int recordSize) {
        // todo: recordSize에 대해서 논의 필요
        //  PredictionState.ERROR로 변경하기
        List<RawPredictionRank> rawPredictionRanks = this.getRawPredictionRanks(recordSize);
        return this.mapFromRawPredictionRankToPredictionRank(rawPredictionRanks);
    }

    protected List<RawPredictionRank> getRawPredictionRanks(int recordSize) {
        String formattedSql = this.generateSql();

        List<RawPredictionRank> raws = jdbcTemplate.query(formattedSql,
            new Object[]{recordSize},
            (rs, rowNum) -> new RawPredictionRank(
                rs.getLong("pemfcId"),
                rs.getLong("totalCount"),
                rs.getLong("errorCount"),
                rs.getDouble("errorRate")));
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

        return rawPredictionRanks.stream().map(r -> PredictionRank.fromRawPredictionRank(
            r, pemfcMap.get(r.pemfcId()))).toList();
    }
}
