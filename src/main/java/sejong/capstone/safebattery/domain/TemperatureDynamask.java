package sejong.capstone.safebattery.domain;

import jakarta.persistence.*;
import lombok.*;
import sejong.capstone.safebattery.util.MaskConverter;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TemperatureDynamask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double tsec;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pemfc pemfc;

    @Lob
    @Convert(converter = MaskConverter.class)
    private List<List<Double>> value;

    public TemperatureDynamask() {}

    public TemperatureDynamask(double tsec, Pemfc pemfc, List<List<Double>> masks) {
        this.tsec = tsec;
        this.pemfc = pemfc;
        this.value = masks;
    }
}
