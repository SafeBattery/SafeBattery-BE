package sejong.capstone.safebattery.dto;

import lombok.Getter;
import sejong.capstone.safebattery.domain.Pemfc;

@Getter
public class PemfcResponseDto {
    private Long id;
    private Long clientId;

    public PemfcResponseDto() {}

    public PemfcResponseDto(Pemfc pemfc) {
        this.id = pemfc.getId();
        this.clientId = pemfc.getClient().getId();
    }
}
