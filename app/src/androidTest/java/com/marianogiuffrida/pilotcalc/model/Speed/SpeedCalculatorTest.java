package com.marianogiuffrida.pilotcalc.model.Speed;

import android.test.AndroidTestCase;

import com.marianogiuffrida.pilotcalc.data.SqlLiteDataStore;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

/**
 * Created by Mariano on 03/08/2015.
 */
public class SpeedCalculatorTest extends AndroidTestCase {

    private UnitConversionRepository unitConversions;
    private SpeedCalculator speedCalculator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        unitConversions = new UnitConversionRepository(new SqlLiteDataStore(this.getContext()));
        speedCalculator = new SpeedCalculator(unitConversions);
    }

    public void testCalculateSpeedSound() {
        Measurement result = speedCalculator.calculateSpeedSound(
                new Measurement(15.0D, Units.Temperature.Celsius));

        assertEquals(result.getMagnitude().doubleValue(), 661.47, 0.01);

    }

    public void testCalculateMachNumberWithPressureAltitude(){
        double result = speedCalculator.calculateMachNumberWithPressureAltitude(
                new Measurement(280, Units.Speed.Knot),
                new Measurement(14500, Units.Length.Foot));

        assertEquals(result, 0.55, 0.01);
    }


    public void testCalculateMachNumberWithPressureAltitude2(){
        double result = speedCalculator.calculateMachNumberWithPressureAltitude(
                new Measurement(250, Units.Speed.Knot),
                new Measurement(10000, Units.Length.Foot));

        assertEquals(result, 0.4523, 0.01);
    }

    public void testCalculateMachNumber() {
        double result = speedCalculator.calculateMachNumber(
                new Measurement(343, Units.Speed.Knot),
                new Measurement(-15.0, Units.Temperature.Celsius));

        assertEquals(result, 0.55, 0.01);
    }

    public void testCalculateTrueAirspeed() {
        Measurement result = speedCalculator.calculateTrueAirspeed(
                new Measurement(250, Units.Speed.Knot),
                new Measurement(10000, Units.Length.Foot),
                new Measurement(2.0, Units.Temperature.Celsius),
                0.8D);

        assertEquals(result.getMagnitude().doubleValue(), 287.7, 0.1);
    }

    public void testCalculateTrueAirTemperature() {
        Measurement result = speedCalculator.calculateTrueAirTemperature(
                new Measurement(276, Units.Speed.Knot),
                new Measurement(10000, Units.Length.Foot),
                new Measurement(0, Units.Temperature.Celsius),
                1.0);

        assertEquals(result.getMagnitude().doubleValue(), -13.0, 0.1);
    }
}