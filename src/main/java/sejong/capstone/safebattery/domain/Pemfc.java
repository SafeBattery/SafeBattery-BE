package sejong.capstone.safebattery.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sejong.capstone.safebattery.enums.PredictionState;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Pemfc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 pemfc - 하나의 클라이언트
    //@JoinColumn(name = "client_id") //생략 가능
    private Client client;

    private PredictionState state;
    private double lat;
    private double lng;
    private String modelName;
    LocalDate manufacturedDate;

    public Pemfc() {}

    public Pemfc(Client client, PredictionState state, double lat, double lng,
                 String modelName, LocalDate manufacturedDate) {
        this.client = client;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
        this.modelName = modelName;
        this.manufacturedDate = manufacturedDate;
    }
}
