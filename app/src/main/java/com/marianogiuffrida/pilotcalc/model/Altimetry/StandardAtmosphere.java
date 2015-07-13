package com.marianogiuffrida.pilotcalc.model.Altimetry;

import com.marianogiuffrida.helpers.ArgumentCheck;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Conversions.ConversionCalculator;
import com.marianogiuffrida.pilotcalc.model.Conversions.UnitValidator;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

import java.math.BigDecimal;

/**
 * Created by Mariano on 7/02/2015.
 */
public final class StandardAtmosphere {

    // Standard temperature at Sea Level in Celsius
    public static final Measurement StandardTemperatureInCelsius = new Measurement(BigDecimal.valueOf(15), Units.Temperature.Celsius);
    public static final Measurement StandardTemperatureInKelvin = new Measurement(BigDecimal.valueOf(288.15), Units.Temperature.Kelvin);

    public static final Measurement StandardTemperatureLapseRate = new Measurement(BigDecimal.valueOf(0.0019812), "CELSIUS_PER_FOOT");

    //Standard temperature in Pa.
    public static final Measurement StandardPressureInhPa = new Measurement(BigDecimal.valueOf(1013.25), Units.Pressure.HectoPascal);
    public static final Measurement StandardPressureInHg = new Measurement(BigDecimal.valueOf(29.92), Units.Pressure.HectoPascal);

    //Standard tropopause altitude in ft.
    public static final Measurement TropopauseAltitudeInFeet = new Measurement(BigDecimal.valueOf(36089.24), Units.Length.Foot);
    public static final Measurement StandardTropopauseTemperature = new Measurement(BigDecimal.valueOf(-56.5), Units.Temperature.Celsius);

    private final UnitConversionRepository unitConversions;
    private final ConversionCalculator conversionCalculator;

    public StandardAtmosphere(UnitConversionRepository uc) {
        unitConversions = uc;
        conversionCalculator = new ConversionCalculator(uc);
    }

    public Measurement calculateStandardTemperatureAtAltitude(Measurement altitude) {
        ArgumentCheck.IsNotNull(altitude, "altitude");
        if(!UnitValidator.isUnitSupported(altitude.getUnitName(), Units.Length.class))
            throw new IllegalArgumentException(String.format("%1 is not a valid unit for an altitude", altitude.getUnitName()));

        BigDecimal altitudeInFeet = altitude.getMagnitude();

        if (!altitude.getUnitName().equals(TropopauseAltitudeInFeet.getUnitName())) {
            altitudeInFeet = (conversionCalculator.convert(altitude,TropopauseAltitudeInFeet.getUnitName())).getMagnitude();
        }

        if (altitudeInFeet.compareTo(TropopauseAltitudeInFeet.getMagnitude()) > 0) {
            return StandardTropopauseTemperature;
        }

        BigDecimal result = StandardTemperatureInCelsius.getMagnitude()
                .subtract(altitudeInFeet.multiply(StandardTemperatureLapseRate.getMagnitude()));

        return new Measurement(result.stripTrailingZeros(), Units.Temperature.Celsius);
    }

    public Measurement calculateStandardTemperatureAtAltitude(Measurement altitude, String resultUnit) {
        if(!UnitValidator.isUnitSupported(resultUnit, Units.Temperature.class))
            throw new IllegalArgumentException(String.format("%1 is not a valid unit for a temperature", resultUnit));

        Measurement result = calculateStandardTemperatureAtAltitude(altitude);
        return conversionCalculator.convert(result, resultUnit);
    }
}
