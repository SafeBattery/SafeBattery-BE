package sejong.capstone.safebattery.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ClientUpdateDto {
    private String name;

    public ClientUpdateDto() {}

    public ClientUpdateDto(String name) {
        this.name = name;
    }
}
