package sejong.capstone.safebattery.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 prediction - 하나의 pemfc
    //@JoinColumn(name = "pemfc_id") //생략 가능
    private Pemfc pemfc;

    private double tsec;
    private double predictedValue;

    public Prediction() {}

    public Prediction(Pemfc pemfc, double tsec, double predictedValue) {
        this.pemfc = pemfc;
        this.tsec = tsec;
        this.predictedValue = predictedValue;
    }
}
