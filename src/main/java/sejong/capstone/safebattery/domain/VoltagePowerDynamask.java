package sejong.capstone.safebattery.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import sejong.capstone.safebattery.util.MaskConverter;

import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
public class VoltagePowerDynamask extends BaseDynamask {
}
