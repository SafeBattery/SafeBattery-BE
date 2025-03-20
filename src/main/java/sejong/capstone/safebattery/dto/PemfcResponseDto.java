package sejong.capstone.safebattery.dto;

import lombok.Getter;
import sejong.capstone.safebattery.domain.Pemfc;

@Getter
public class PemfcResponseDto {
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

    public PemfcResponseDto() {}

    public PemfcResponseDto(Pemfc row) {
        this.id = row.getId();
        this.tsec = row.getTsec();
        this.U_totV = row.getU_totV();
        this.iA = row.getIA();
        this.PW = row.getPW();
        this.m_Air = row.getM_Air();
        this.m_H2 = row.getM_H2();
        this.RH_Air = row.getRH_Air();
        this.RH_H2 = row.getRH_H2();
        this.P_Air_supply = row.getP_Air_supply();
        this.P_H2_supply = row.getP_H2_supply();
        this.P_Air_inlet = row.getP_Air_inlet();
        this.P_H2_inlet = row.getP_H2_inlet();
        this.T_1 = row.getT_1();
        this.T_2 = row.getT_2();
        this.T_3 = row.getT_3();
        this.T_4 = row.getT_4();
        this.T_Air_inlet = row.getT_Air_inlet();
        this.T_H2_inlet = row.getT_H2_inlet();
        this.T_Stack_inlet = row.getT_Stack_inlet();
        this.T_Heater = row.getT_Heater();
        this.m_Air_write = row.getM_Air_write();
        this.m_H2_write = row.getM_H2_write();
        this.Heater_power = row.getHeater_power();
        this.i_write = row.getI_write();
    }
}

