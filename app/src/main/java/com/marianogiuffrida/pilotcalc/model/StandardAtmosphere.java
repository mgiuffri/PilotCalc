package com.marianogiuffrida.pilotcalc.model;

import com.marianogiuffrida.helpers.ArgumentCheck;

import java.math.BigDecimal;

/**
 * Created by Mariano on 7/02/2015.
 */
public class StandardAtmosphere {

    // Standard temperature at Sea Level in Celsius
    public static final BigDecimal StandardTemperature = BigDecimal.valueOf(15); //*C
    public static final String StandardTemperatureUnit = "CELSIUS"; //*C

    public static final BigDecimal StandardTemperatureLapseRate = BigDecimal.valueOf(0.0019812); //* C per ft

    //Standard temperature in Pa.
    public static final BigDecimal StandardPressure = BigDecimal.valueOf(1013.25);//hPa
    public static final String StandardPressureUnit = "HECTOPASCAL";//hPa

    //Standard tropopose altitude in ft.
    public static final BigDecimal TropopauseAltitude = BigDecimal.valueOf(36089.24); //ft
    public static final String TropopauseAltitudeUnit = "FOOT"; //ft
    public static final BigDecimal StandardTropopauseTemperature = BigDecimal.valueOf(-56.5);
    private final UnitConversions unitConversions;

    public StandardAtmosphere(UnitConversions uc){
        unitConversions = uc;
    }

    public BigDecimal calculateStandardTemperatureAtAltitude(Measurement altitude){
        ArgumentCheck.IsNotNull(altitude, "altitude");
        BigDecimal altitudeInFeet = altitude.getMagnitude();
        if(!altitude.getUnit().equals(TropopauseAltitudeUnit)){
            altitudeInFeet = BigDecimal.valueOf(unitConversions.getConverter().convertMeasurement(altitude, TropopauseAltitudeUnit));
        }

        if (altitudeInFeet.compareTo(TropopauseAltitude) > 0) return StandardTropopauseTemperature;

        return StandardTemperature.subtract(altitudeInFeet.multiply(StandardTemperatureLapseRate));
    }
}
