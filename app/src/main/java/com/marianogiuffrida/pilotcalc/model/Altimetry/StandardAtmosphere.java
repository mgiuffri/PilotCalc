package com.marianogiuffrida.pilotcalc.model.Altimetry;

import com.marianogiuffrida.helpers.ArgumentCheck;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Conversions.ConversionCalculator;
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
    public static final Measurement StandardPressureInHg = new Measurement(BigDecimal.valueOf(29.92), Units.Pressure.InchMercury);

    //Standard tropopause altitude in ft.
    public static final Measurement TropopauseAltitudeInFeet = new Measurement(BigDecimal.valueOf(36089.24), Units.Length.Foot);
    public static final Measurement StandardTropopauseTemperature = new Measurement(BigDecimal.valueOf(-56.5), Units.Temperature.Celsius);

    private final UnitConversionRepository unitConversions;
    private final ConversionCalculator conversionCalculator;

    public StandardAtmosphere(UnitConversionRepository uc) {
        unitConversions = uc;
        conversionCalculator = new ConversionCalculator(uc);
    }

    public Measurement calculateStandardTemperature(Measurement altitude) {
        ArgumentCheck.IsNotNull(altitude, "altitude");
        Units.Validator.check(altitude, Units.Length.class);

        if (!Units.Validator.isUnitSupported(altitude.getUnitName(), Units.Length.class))
            throw new IllegalArgumentException(String.format("%1 is not a valid unit for an altitude", altitude.getUnitName()));

        BigDecimal altitudeInFeet = altitude.getMagnitude();

        if (!altitude.getUnitName().equals(TropopauseAltitudeInFeet.getUnitName())) {
            altitudeInFeet = (conversionCalculator.convert(altitude, TropopauseAltitudeInFeet.getUnitName())).getMagnitude();
        }

        if (altitudeInFeet.compareTo(TropopauseAltitudeInFeet.getMagnitude()) > 0) {
            return StandardTropopauseTemperature;
        }

        BigDecimal result = StandardTemperatureInCelsius.getMagnitude()
                .subtract(altitudeInFeet.multiply(StandardTemperatureLapseRate.getMagnitude()));

        return new Measurement(result.stripTrailingZeros(), Units.Temperature.Celsius);
    }

    public Measurement calculateStandardTemperature(Measurement altitude, String resultUnit) {
        Units.Validator.check(resultUnit, Units.Temperature.class);
        Measurement result = calculateStandardTemperature(altitude);
        return conversionCalculator.convert(result, resultUnit);
    }

    public Measurement calculatePressureAltitude(Measurement pressureReading){
        ArgumentCheck.IsNotNull(pressureReading, "pressureReading");
        Units.Validator.check(pressureReading, Units.Pressure.class);
        return calculatePressureAltitude(pressureReading, new Measurement(0, Units.Length.Foot));
    }

    public Measurement calculatePressureAltitude(Measurement pressureReading, Measurement elevation){
        ArgumentCheck.IsNotNull(pressureReading, "pressureReading");
        ArgumentCheck.IsNotNull(elevation, "elevation");
        Units.Validator.check(pressureReading, Units.Pressure.class);
        Units.Validator.check(elevation, Units.Length.class);

        BigDecimal pressure = pressureReading.getMagnitude();
        BigDecimal height = elevation.getMagnitude();

        if (!pressureReading.getUnitName().equals(StandardPressureInHg.getUnitName())) {
            pressure = (conversionCalculator.convert(pressureReading, StandardPressureInHg.getUnitName())).getMagnitude();
        }
        if (!elevation.getUnitName().equals(Units.Length.Foot)) {
            height = (conversionCalculator.convert(elevation, Units.Length.Foot)).getMagnitude();
        }

        BigDecimal result = StandardPressureInHg.getMagnitude()
                .subtract(pressure)
                .multiply(BigDecimal.valueOf(1000))
                .add(height);

        return new Measurement(result.stripTrailingZeros(), Units.Length.Foot);
    }

    public Measurement calculatePressureAltitude(Measurement pressureReading, String resultUnit) {
        Units.Validator.check(resultUnit, Units.Length.class);
        Measurement result = calculatePressureAltitude(pressureReading);
        return conversionCalculator.convert(result, resultUnit);
    }

    public Measurement calculatePressureAltitude(Measurement pressureReading, Measurement elevation, String resultUnit) {
        Units.Validator.check(resultUnit, Units.Length.class);
        Measurement result = calculatePressureAltitude(pressureReading, elevation);
        return conversionCalculator.convert(result, resultUnit);
    }

}
