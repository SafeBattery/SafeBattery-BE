package sejong.capstone.safebattery.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sejong.capstone.safebattery.domain.Client;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PemfcUpdateDto {
    private Client client;

    public PemfcUpdateDto() {}

    public PemfcUpdateDto(Client client) {
        this.client = client;
    }
}
