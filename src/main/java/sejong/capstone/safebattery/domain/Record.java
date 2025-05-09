package sejong.capstone.safebattery.domain;

import jakarta.persistence.*;
import lombok.*;
import sejong.capstone.safebattery.enums.PredictionState;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY) // 여러 row - 하나의 pemfc
    //@JoinColumn(name = "pemfc_id") //생략 가능
    private Pemfc pemfc;

    private PredictionState powerVoltageState;
    private PredictionState temperatureState;

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

    public Record() {
    }

    public Record(Pemfc pemfc, PredictionState powerVoltageState, PredictionState temperatureState,
                  double tsec, double u_totV, double iA, double PW, double m_Air, double m_H2,
                  double RH_Air, double RH_H2, double p_Air_supply, double p_H2_supply, double p_Air_inlet,
                  double p_H2_inlet, double t_1, double t_2, double t_3, double t_4, double t_Air_inlet,
                  double t_H2_inlet, double t_Stack_inlet, double t_Heater, double m_Air_write,
                  double m_H2_write, int heater_power, double i_write,
                  double lat, double lng) {
        this.pemfc = pemfc;
        this.powerVoltageState = powerVoltageState;
        this.temperatureState = temperatureState;
        this.tsec = tsec;
        this.U_totV = u_totV;
        this.iA = iA;
        this.PW = PW;
        this.m_Air = m_Air;
        this.m_H2 = m_H2;
        this.RH_Air = RH_Air;
        this.RH_H2 = RH_H2;
        this.P_Air_supply = p_Air_supply;
        this.P_H2_supply = p_H2_supply;
        this.P_Air_inlet = p_Air_inlet;
        this.P_H2_inlet = p_H2_inlet;
        this.T_1 = t_1;
        this.T_2 = t_2;
        this.T_3 = t_3;
        this.T_4 = t_4;
        this.T_Air_inlet = t_Air_inlet;
        this.T_H2_inlet = t_H2_inlet;
        this.T_Stack_inlet = t_Stack_inlet;
        this.T_Heater = t_Heater;
        this.m_Air_write = m_Air_write;
        this.m_H2_write = m_H2_write;
        this.Heater_power = heater_power;
        this.i_write = i_write;
        this.lat = lat;
        this.lng = lng;
    }
}