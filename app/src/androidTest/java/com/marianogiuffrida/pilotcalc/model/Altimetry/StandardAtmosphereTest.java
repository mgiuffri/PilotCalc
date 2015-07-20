package com.marianogiuffrida.pilotcalc.model.Altimetry;

import android.test.AndroidTestCase;

import com.marianogiuffrida.pilotcalc.data.SqlLiteDataStore;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

import java.math.BigDecimal;

public class StandardAtmosphereTest extends AndroidTestCase{

    private UnitConversionRepository unitConversions;
    private StandardAtmosphere standardAtmosphere;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        unitConversions = new UnitConversionRepository(new SqlLiteDataStore(this.getContext()));
        standardAtmosphere = new StandardAtmosphere(unitConversions);
    }

    //region calculateStandardTemperature

    public void testShouldThrowIfCalculatingTempWithNullAltitude() {
        try{
            standardAtmosphere.calculateStandardTemperature(null);
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }

    
    public void testShouldThrowIfCalculatingTempWithInvalidMeasure() {
        try{
            standardAtmosphere.calculateStandardTemperature(new Measurement(1.0, Units.Pressure.HectoPascal));
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }

    public void testShouldThrowIfRequestingInvalidConversion() {
        try{
            standardAtmosphere.calculateStandardTemperature(new Measurement(1.0, Units.Length.Foot), Units.Length.Foot);
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }
    
    public void testCalculateTemperatureAtAltitudeAboveTropopauseGivenInFeet() {
        Measurement result = standardAtmosphere.calculateStandardTemperature(
                new Measurement(BigDecimal.valueOf(40000), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), standardAtmosphere.StandardTropopauseTemperature.getMagnitude().doubleValue());
    }

    
    public void testCalculateTemperatureAtAltitudeAboveTropopauseGivenInKilometre() {
        Measurement result = standardAtmosphere.calculateStandardTemperature(
                new Measurement(BigDecimal.valueOf(12), Units.Length.Kilometre));

        assertEquals(result.getMagnitude().doubleValue(), standardAtmosphere.StandardTropopauseTemperature.getMagnitude().doubleValue());
    }

    
    public void testCalculateTemperatureAtAltitudeBelowTropopauseGivenInFeet() {
        Measurement result = standardAtmosphere.calculateStandardTemperature(
                new Measurement(BigDecimal.valueOf(3000), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), 9.0564);
    }

    
    public void testCalculateTemperatureAtAltitudeBelowTropopauseGivenInMetre() {
        Measurement result = standardAtmosphere.calculateStandardTemperature(
                new Measurement(BigDecimal.valueOf(300), Units.Length.Metre));

        assertEquals(result.getMagnitude().doubleValue(), 13.05, 0.01);
    }

    
    public void testCalculateTemperatureAtSeaLevelGivenInFeet() {
        Measurement result = standardAtmosphere.calculateStandardTemperature(
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), standardAtmosphere.StandardTemperatureInCelsius.getMagnitude().doubleValue());
    }
    //endregion

    //region calculatePressureAltitude(pressure, measurement)

    
    public void testShouldThrowIfPressureIsNull() {
        try{
            standardAtmosphere.calculatePressureAltitude(null, new Measurement(BigDecimal.valueOf(0), Units.Length.Foot));
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }

    
    public void testShouldThrowIfElevationIsNull() {
        try{
            standardAtmosphere.calculatePressureAltitude(new Measurement(0, Units.Pressure.InchMercury), (Measurement) null);
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }

    
    public void testCalculateStandardPressureAtZero() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.92), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), 0d);
    }

    
    public void testCalculateStandardPressure() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.32), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(500), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), 1100d);
    }

    //endregion

    //region calculatePressureAltitude(pressure, measurement, unit)

    
    public void testShouldThrowIfPressureIsNull_withConversion() {
        try{
            standardAtmosphere.calculatePressureAltitude(null,
                    new Measurement(BigDecimal.valueOf(0), Units.Length.Foot),
                    Units.Length.Foot);
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }

    
    public void testShouldThrowIfElevationIsNull_withConversion() {
        try{
            standardAtmosphere.calculatePressureAltitude(new Measurement(0, Units.Pressure.InchMercury)
                    , null
                    , Units.Length.Foot);
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }

    
    public void testShouldThrowIfUnitIsNull_withConversion() {
        try{
            standardAtmosphere.calculatePressureAltitude(new Measurement(0, Units.Pressure.InchMercury)
                    , new Measurement(0, Units.Length.Foot)
                    , null);
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }

    
    public void testCalculateStandardPressureAtZero_withConversion() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.92), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot),
                Units.Length.Metre);

        assertEquals(result.getMagnitude().doubleValue(), 0d);
    }

    
    public void testCalculateStandardPressure_withConversion() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.32), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(500), Units.Length.Foot),
                Units.Length.Metre);

        assertEquals(result.getMagnitude().doubleValue(), 335.28d, 0.01);
    }

    
    public void testCalculateStandardPressure_withConversions() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(993), Units.Pressure.HectoPascal),
                new Measurement(BigDecimal.valueOf(500), Units.Length.Foot),
                Units.Length.Metre);

        assertEquals(result.getMagnitude().doubleValue(), 335.28d, 3);
    }

    //endregion

    //region calculatePressureAltitude(pressure)

    
    public void testShouldThrowIfPressureIsNull_noElevation() {
        try{
            standardAtmosphere.calculatePressureAltitude(null);
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }

    
    public void testCalculateStandardPressureAtZero_noElevation() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.92), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), 0d);
    }

    
    public void testCalculateStandardPressure_noElevation() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.32), Units.Pressure.InchMercury));

        assertEquals(result.getMagnitude().doubleValue(), 600d);
    }

    //endregion

    //region calculatePressureAltitude(pressure, unit)

    
    public void testShouldThrowIfPressureIsNull_noElevationWithConversion() {
        try {
            standardAtmosphere.calculatePressureAltitude(null, Units.Length.Foot);
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }

    
    public void testShouldThrowIfUnitIsNull_noElevationWithConversion() {
        try{
            standardAtmosphere.calculatePressureAltitude(new Measurement(0, Units.Pressure.InchMercury)
                    , (String) null);
            fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){

        }
    }

    
    public void testCalculateStandardPressureAtZero_noElevationWithConversion() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.92), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot),
                Units.Length.Metre);

        assertEquals(result.getMagnitude().doubleValue(), 0.0);
    }

    
    public void testCalculateStandardPressure_noElevationWithConversion() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.32), Units.Pressure.InchMercury),
                Units.Length.Metre);

        assertEquals(result.getMagnitude().doubleValue(), 182.88);
    }

    //endregion

}