package sejong.capstone.safebattery.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sejong.capstone.safebattery.util.PredictionStateConverter;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BasePrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 prediction - 하나의 pemfc
    //@JoinColumn(name = "pemfc_id") //생략 가능
    protected Pemfc pemfc;

    protected double tsec;
    protected double predictedValue;
    @Column(nullable = false)
    @Convert(converter = PredictionStateConverter.class)
    protected PredictionState state;
}
