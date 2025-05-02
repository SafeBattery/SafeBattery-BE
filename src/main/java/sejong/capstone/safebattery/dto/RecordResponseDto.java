package sejong.capstone.safebattery.dto;

import lombok.Getter;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;

@Getter
public class RecordResponseDto {
    private Long id; // PK

    private double tsec;
    private double U_totV;
    private double iA;
    private double PW;
    private double m_Air;
    private double m_H2;
    private double RH_Air;
    private double RH_H2;
    private double P_Air_supply;
    private double P_H2_supply;
    private double P_Air_inlet;
    private double P_H2_inlet;
    private double T_1;
    private double T_2;
    private double T_3;
    private double T_4;
    private double T_Air_inlet;
    private double T_H2_inlet;
    private double T_Stack_inlet;
    private double T_Heater;
    private double m_Air_write;
    private double m_H2_write;
    private int Heater_power;
    private double i_write;
    private double lat;
    private double lng;


    public RecordResponseDto() {}

    public RecordResponseDto(Record record) {
        this.id = record.getId();
        this.tsec = record.getTsec();
        this.U_totV = record.getU_totV();
        this.iA = record.getIA();
        this.PW = record.getPW();
        this.m_Air = record.getM_Air();
        this.m_H2 = record.getM_H2();
        this.RH_Air = record.getRH_Air();
        this.RH_H2 = record.getRH_H2();
        this.P_Air_supply = record.getP_Air_supply();
        this.P_H2_supply = record.getP_H2_supply();
        this.P_Air_inlet = record.getP_Air_inlet();
        this.P_H2_inlet = record.getP_H2_inlet();
        this.T_1 = record.getT_1();
        this.T_2 = record.getT_2();
        this.T_3 = record.getT_3();
        this.T_4 = record.getT_4();
        this.T_Air_inlet = record.getT_Air_inlet();
        this.T_H2_inlet = record.getT_H2_inlet();
        this.T_Stack_inlet = record.getT_Stack_inlet();
        this.T_Heater = record.getT_Heater();
        this.m_Air_write = record.getM_Air_write();
        this.m_H2_write = record.getM_H2_write();
        this.Heater_power = record.getHeater_power();
        this.i_write = record.getI_write();
        this.lat = record.getLat();
        this.lng = record.getLng();
    }
}

