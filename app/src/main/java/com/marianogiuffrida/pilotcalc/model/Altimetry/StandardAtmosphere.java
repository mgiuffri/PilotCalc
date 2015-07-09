package com.marianogiuffrida.pilotcalc.model.Altimetry;

import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;

import java.math.BigDecimal;

/**
 * Created by Mariano on 7/02/2015.
 */
public final class StandardAtmosphere {

    // Standard temperature at Sea Level in Celsius
    public static final Measurement StandardTemperatureInCelsius = new Measurement(BigDecimal.valueOf(15), "CELSIUS");
    public static final Measurement StandardTemperatureInKelvin = new Measurement(BigDecimal.valueOf(288.15), "KELVIN");

    public static final BigDecimal StandardTemperatureLapseRate = BigDecimal.valueOf(0.0019812); //* C per ft

    //Standard temperature in Pa.
    public static final Measurement StandardPressureInhPa =  new Measurement( BigDecimal.valueOf(1013.25), "HECTOPASCAL");
    public static final Measurement StandardPressureInHg =  new Measurement( BigDecimal.valueOf(29.92), "INCHOFMERCURY");

    //Standard tropopause altitude in ft.
    public static final Measurement TropopauseAltitudeInFeet =new Measurement( BigDecimal.valueOf(36089.24), "FOOT");
    public static final Measurement StandardTropopauseTemperature = new Measurement(BigDecimal.valueOf(-56.5), "CELSIUS");

    private final UnitConversionRepository unitConversions;

    public StandardAtmosphere(UnitConversionRepository uc){
        unitConversions = uc;
    }

    public Measurement calculateStandardTemperatureAtAltitude(Measurement altitude){
//        ArgumentCheck.IsNotNull(altitude, "altitude");
//        BigDecimal altitudeInFeet = altitude.getMagnitude();
//        if(!altitude.getUnitName().equals(TropopauseAltitudeUnit)){
//            altitudeInFeet = BigDecimal.valueOf(unitConversions.getConverter().convertMeasurement(altitude, TropopauseAltitudeUnit));
//        }
//
//        if (altitudeInFeet.compareTo(TropopauseAltitudeInFeet) > 0) return StandardTropopauseTemperature;
//
//        return StandardTemperatureInCelsius.subtract(altitudeInFeet.multiply(StandardTemperatureLapseRate));
        return null;
    }


}
