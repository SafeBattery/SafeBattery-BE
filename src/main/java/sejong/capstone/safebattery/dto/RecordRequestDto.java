package sejong.capstone.safebattery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordRequestDto {

    private double tsec;

    @JsonProperty("U_totV")
    private double uTotV;

    @JsonProperty("iA")
    private double iA;
    @JsonProperty("PW")
    private double pw;

    @JsonProperty("m_Air")
    private double mAir;

    @JsonProperty("m_H2")
    private double mH2;

    @JsonProperty("RH_Air")
    private double rhAir;

    @JsonProperty("RH_H2")
    private double rhH2;

    @JsonProperty("P_Air_supply")
    private double pAirSupply;

    @JsonProperty("P_H2_supply")
    private double pH2Supply;

    @JsonProperty("P_Air_inlet")
    private double pAirInlet;

    @JsonProperty("P_H2_inlet")
    private double pH2Inlet;

    @JsonProperty("T_1")
    private double t1;

    @JsonProperty("T_2")
    private double t2;

    @JsonProperty("T_3")
    private double t3;

    @JsonProperty("T_4")
    private double t4;

    @JsonProperty("T_Air_inlet")
    private double tAirInlet;

    @JsonProperty("T_H2_inlet")
    private double tH2Inlet;

    @JsonProperty("T_Stack_inlet")
    private double tStackInlet;

    @JsonProperty("T_Heater")
    private double tHeater;

    @JsonProperty("m_Air_write")
    private double mAirWrite;

    @JsonProperty("m_H2_write")
    private double mH2Write;

    @JsonProperty("Heater_power")
    private int heaterPower;

    @JsonProperty("i_write")
    private double iWrite;

    private double lat;
    private double lng;

    @JsonProperty("record_number")
    private int recordNumber;

    public Record toEntity(Pemfc pemfc) {
        return new Record(
                pemfc, null, null, null, tsec, uTotV, iA, pw, mAir, mH2,
                rhAir, rhH2, pAirSupply, pH2Supply, pAirInlet, pH2Inlet,
                t1, t2, t3, t4, tAirInlet, tH2Inlet, tStackInlet, tHeater,
                mAirWrite, mH2Write, heaterPower, iWrite,
                lat, lng, recordNumber
        );
    }
}

