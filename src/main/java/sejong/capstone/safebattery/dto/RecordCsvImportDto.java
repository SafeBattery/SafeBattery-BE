package sejong.capstone.safebattery.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.*;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.domain.Record;

@Getter
@Setter
public class RecordCsvImportDto {
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

    public Record convert(Pemfc pemfc) {
        return new Record(
                pemfc, tsec, U_totV, iA, PW, m_Air, m_H2, RH_Air, RH_H2,
                P_Air_supply, P_H2_supply, P_Air_inlet, P_H2_inlet,
                T_1, T_2, T_3, T_4, T_Air_inlet, T_H2_inlet, T_Stack_inlet, T_Heater,
                m_Air_write, m_H2_write, Heater_power, i_write, lat, lng
        );
    }
}