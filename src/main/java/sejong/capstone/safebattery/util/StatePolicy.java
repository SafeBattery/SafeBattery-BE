package sejong.capstone.safebattery.util;

import org.springframework.stereotype.Component;
import sejong.capstone.safebattery.Constants;
import sejong.capstone.safebattery.enums.PredictionState;

import static sejong.capstone.safebattery.enums.PredictionState.*;

@Component
public class StatePolicy {
    public static boolean isNormalVoltage(double voltage) {
        return voltage > Constants.VOLTAGE_LOWER_BOUND
                && voltage < Constants.VOLTAGE_UPPER_BOUND;
    }

    public static boolean isNormalPower(double power) {
        return power > Constants.POWER_LOWER_BOUND
                && power < Constants.POWER_UPPER_BOUND;
    }

    public static boolean isNormalTemperature(double temperature) {
        return temperature > Constants.TEMPERATURE_LOWER_BOUND
                && temperature < Constants.TEMPERATURE_UPPER_BOUND;
    }

    public static PredictionState getCurrentPowerVoltageState(double power, double voltage) {
        if (isNormalPower(power) && isNormalVoltage(voltage)) {
            return NORMAL;
        } else return ERROR;
    }

    public static PredictionState getCurrentTemperatureState(double temperature) {
        if (isNormalTemperature(temperature)) {
            return NORMAL;
        } else return ERROR;
    }
}
