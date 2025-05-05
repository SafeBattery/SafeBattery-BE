package sejong.capstone.safebattery.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;

@Getter
@Setter
public class RecordCsvExportDto {
    @CsvBindByName(column = "tsec")
    private double tsec;
    @CsvBindByName(column = "U_totV")
    private double U_totV;
    @CsvBindByName(column = "iA")
    private double iA;
    @CsvBindByName(column = "PW")
    private double PW;
    @CsvBindByName(column = "m_Air")
    private double m_Air;
    @CsvBindByName(column = "m_H2")
    private double m_H2;
    @CsvBindByName(column = "RH_Air")
    private double RH_Air;
    @CsvBindByName(column = "RH_H2")
    private double RH_H2;
    @CsvBindByName(column = "P_Air_supply")
    private double P_Air_supply;
    @CsvBindByName(column = "P_H2_supply")
    private double P_H2_supply;
    @CsvBindByName(column = "P_Air_inlet")
    private double P_Air_inlet;
    @CsvBindByName(column = "P_H2_inlet")
    private double P_H2_inlet;
    @CsvBindByName(column = "T_1")
    private double T_1;
    @CsvBindByName(column = "T_2")
    private double T_2;
    @CsvBindByName(column = "T_3")
    private double T_3;
    @CsvBindByName(column = "T_4")
    private double T_4;
    @CsvBindByName(column = "T_Air_inlet")
    private double T_Air_inlet;
    @CsvBindByName(column = "T_H2_inlet")
    private double T_H2_inlet;
    @CsvBindByName(column = "T_Stack_inlet")
    private double T_Stack_inlet;
    @CsvBindByName(column = "T_Heater")
    private double T_Heater;
    @CsvBindByName(column = "m_Air_write")
    private double m_Air_write;
    @CsvBindByName(column = "m_H2_write")
    private double m_H2_write;
    @CsvBindByName(column = "Heater_power")
    private int Heater_power;
    @CsvBindByName(column = "i_write")
    private double i_write;
    @CsvBindByName(column = "lat")
    private double lat;
    @CsvBindByName(column = "lng")
    private double lng;

    public RecordCsvExportDto(Record r) {
        this.tsec = r.getTsec();
        this.U_totV = r.getU_totV();
        this.iA = r.getIA();
        this.PW = r.getPW();
        this.m_Air = r.getM_Air();
        this.m_H2 = r.getM_H2();
        this.RH_Air = r.getRH_Air();
        this.RH_H2 = r.getRH_H2();
        this.P_Air_supply = r.getP_Air_supply();
        this.P_H2_supply = r.getP_H2_supply();
        this.P_Air_inlet = r.getP_Air_inlet();
        this.P_H2_inlet = r.getP_H2_inlet();
        this.T_1 = r.getT_1();
        this.T_2 = r.getT_2();
        this.T_3 = r.getT_3();
        this.T_4 = r.getT_4();
        this.T_Air_inlet = r.getT_Air_inlet();
        this.T_H2_inlet = r.getT_H2_inlet();
        this.T_Stack_inlet = r.getT_Stack_inlet();
        this.T_Heater = r.getT_Heater();
        this.m_Air_write = r.getM_Air_write();
        this.m_H2_write = r.getM_H2_write();
        this.Heater_power = r.getHeater_power();
        this.i_write = r.getI_write();
        this.lat = r.getLat();
        this.lng = r.getLng();
    }
}