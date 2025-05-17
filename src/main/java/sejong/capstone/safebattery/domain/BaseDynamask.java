package sejong.capstone.safebattery.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import sejong.capstone.safebattery.util.MaskConverter;

import java.util.List;

@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BaseDynamask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double tsec;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pemfc pemfc;

    @Lob
    @Convert(converter = MaskConverter.class)
    private List<List<Double>> value;
}
