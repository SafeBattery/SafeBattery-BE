package sejong.capstone.safebattery.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    public Pemfc() {}

    public Pemfc(Client client) {
        this.client = client;
    }
}
