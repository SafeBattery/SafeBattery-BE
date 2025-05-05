package sejong.capstone.safebattery.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class VoltagePrediction extends BasePrediction {

}
