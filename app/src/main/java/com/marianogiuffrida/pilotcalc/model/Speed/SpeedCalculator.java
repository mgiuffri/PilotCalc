package com.marianogiuffrida.pilotcalc.model.Speed;

import com.marianogiuffrida.helpers.ArgumentCheck;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Altimetry.StandardAtmosphere;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Conversions.ConversionCalculator;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

/**
 * Created by Mariano on 03/08/2015.
 */
public class SpeedCalculator {
    private final ConversionCalculator conversionCalculator;
    private final StandardAtmosphere standardAtmosphere;

    public SpeedCalculator(UnitConversionRepository uc) {
        conversionCalculator = new ConversionCalculator(uc);
        standardAtmosphere = new StandardAtmosphere(uc);
    }

    public Measurement calculateSpeedSound(Measurement outsideTemperature) {
        ArgumentCheck.IsNotNull(outsideTemperature, "outsideTemperature");
        Units.Validator.check(outsideTemperature, Units.Temperature.class);

        double OAT = conversionCalculator.convert(outsideTemperature, Units.Temperature.Kelvin).getMagnitude().doubleValue();
        double result = 38.967854 * Math.sqrt(OAT);
        return new Measurement(result, Units.Speed.Knot);
    }

    public double calculateMachNumber(Measurement trueAirspeed, Measurement outsideTemperature) {
        ArgumentCheck.IsNotNull(trueAirspeed, "trueAirspeed");
        Units.Validator.check(trueAirspeed, Units.Speed.class);

        double TAS = conversionCalculator.convert(trueAirspeed, Units.Speed.Knot).getMagnitude().doubleValue();
        double speedSound = calculateSpeedSound(outsideTemperature).getMagnitude().doubleValue();
        double result = TAS / speedSound;
        return result;
    }

    public double calculateMachNumberWithPressureAltitude(Measurement calibratedAirspeed, Measurement pressureAltitude) {

        double DP = standardAtmosphere.calculateDifferentialPressure(calibratedAirspeed).getMagnitude().doubleValue();
        double P = standardAtmosphere.calculatePressure(pressureAltitude).getMagnitude().doubleValue();

        double machNumber = Math.pow((5.0D * (Math.pow(DP / P + 1, 2.0D / 7.0D) - 1)), 0.5D);
        return machNumber;
    }

    public Measurement calculateTrueAirspeed(Measurement calibratedAirspeed,
                                             Measurement pressureAltitude,
                                             Measurement indicatedAirTemperature,
                                             double recoveryCoefficient,
                                             String resultUnit) {

        Units.Validator.check(resultUnit, Units.Speed.class);
        Measurement result = calculateTrueAirspeed(
                calibratedAirspeed,
                pressureAltitude,
                indicatedAirTemperature,
                recoveryCoefficient);
        return conversionCalculator.convert(result, resultUnit);
    }

    public Measurement calculateTrueAirspeed(Measurement calibratedAirspeed,
                                             Measurement pressureAltitude,
                                             Measurement indicatedAirTemperature,
                                             double recoveryCoefficient) {

        double M = calculateMachNumberWithPressureAltitude(calibratedAirspeed, pressureAltitude);
        Measurement OAT = standardAtmosphere.calculateOutsideAirTemperature(indicatedAirTemperature, M, recoveryCoefficient);
        double speedSound = calculateSpeedSound(OAT).getMagnitude().doubleValue();
        double result = speedSound * M;
        return new Measurement(result, Units.Speed.Knot);
    }

    public Measurement calculateTrueAirTemperature(Measurement calibratedAirspeed,
                                                   Measurement pressureAltitude,
                                                   Measurement indicatedAirTemperature,
                                                   double recoveryCoefficient,
                                                   String resultUnit) {

        Units.Validator.check(resultUnit, Units.Temperature.class);
        Measurement result = calculateTrueAirTemperature(
                calibratedAirspeed,
                pressureAltitude,
                indicatedAirTemperature,
                recoveryCoefficient);
        return conversionCalculator.convert(result, resultUnit);
    }

    public Measurement calculateTrueAirTemperature(Measurement calibratedAirspeed,
                                             Measurement pressureAltitude,
                                             Measurement indicatedAirTemperature,
                                             double recoveryCoefficient) {

        double M = calculateMachNumberWithPressureAltitude(calibratedAirspeed, pressureAltitude);
        return standardAtmosphere.calculateOutsideAirTemperature(indicatedAirTemperature, M, recoveryCoefficient);
    }

}
