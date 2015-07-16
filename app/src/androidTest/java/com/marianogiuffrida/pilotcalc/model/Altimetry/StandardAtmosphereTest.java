package com.marianogiuffrida.pilotcalc.model.Altimetry;

import android.test.AndroidTestCase;

import com.marianogiuffrida.pilotcalc.data.SqlLiteDataStore;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

    @Rule
    ExpectedException exception = ExpectedException.none();

    //region calculateStandardTemperature

    @Test
    public void ShouldThrowIfCalculatingTempWithNullAltitude() {
        standardAtmosphere.calculateStandardTemperature(null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("altitude");
    }

    @Test
    public void ShouldThrowIfCalculatingTempWithInvalidMeasure() {
        standardAtmosphere.calculateStandardTemperature(new Measurement(1.0, Units.Pressure.HectoPascal));
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("HECTOPASCAL is not a valid unit for an altitude");
    }

    @Test
    public void ShouldThrowIfRequestingInvalidConversion() {
        standardAtmosphere.calculateStandardTemperature(new Measurement(1.0, Units.Length.Foot), Units.Length.Foot);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("FOOT is not a valid unit for a temperature");
    }

    @Test
    public void testCalculateTemperatureAtAltitudeAboveTropopauseGivenInFeet() {
        Measurement result = standardAtmosphere.calculateStandardTemperature(
                new Measurement(BigDecimal.valueOf(40000), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), standardAtmosphere.StandardTropopauseTemperature.getMagnitude().doubleValue());
    }

    @Test
    public void testCalculateTemperatureAtAltitudeAboveTropopauseGivenInKilometre() {
        Measurement result = standardAtmosphere.calculateStandardTemperature(
                new Measurement(BigDecimal.valueOf(12), Units.Length.Kilometre));

        assertEquals(result.getMagnitude().doubleValue(), standardAtmosphere.StandardTropopauseTemperature.getMagnitude().doubleValue());
    }

    @Test
    public void testCalculateTemperatureAtAltitudeBelowTropopauseGivenInFeet() {
        Measurement result = standardAtmosphere.calculateStandardTemperature(
                new Measurement(BigDecimal.valueOf(3000), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), 9.0564);
    }

    @Test
    public void testCalculateTemperatureAtAltitudeBelowTropopauseGivenInMetre() {
        Measurement result = standardAtmosphere.calculateStandardTemperature(
                new Measurement(BigDecimal.valueOf(300), Units.Length.Metre));

        assertEquals(result.getMagnitude().doubleValue(), 13.05, 0.01);
    }

    @Test
    public void testCalculateTemperatureAtSeaLevelGivenInFeet() {
        Measurement result = standardAtmosphere.calculateStandardTemperature(
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), standardAtmosphere.StandardTemperatureInCelsius.getMagnitude().doubleValue());
    }
    //endregion

    //region calculatePressureAltitude(pressure, measurement)

    @Test
    public void ShouldThrowIfPressureIsNull() {
        standardAtmosphere.calculatePressureAltitude(null, new Measurement(BigDecimal.valueOf(0), Units.Length.Foot));
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("altitude");
    }

    @Test
    public void ShouldThrowIfElevationIsNull() {
        standardAtmosphere.calculatePressureAltitude(new Measurement(0, Units.Pressure.InchMercury), (Measurement)null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("altitude");
    }

    @Test
    public void testCalculateStandardPressureAtZero() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.92), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), 0d);
    }

    @Test
    public void testCalculateStandardPressure() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.32), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(500), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), 1100d);
    }

    //endregion

    //region calculatePressureAltitude(pressure, measurement, unit)

    @Test
    public void ShouldThrowIfPressureIsNull_withConversion() {
        standardAtmosphere.calculatePressureAltitude(null,
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot),
                Units.Length.Foot);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("altitude");
    }

    @Test
    public void ShouldThrowIfElevationIsNull_withConversion() {
        standardAtmosphere.calculatePressureAltitude(new Measurement(0, Units.Pressure.InchMercury)
                , null
                , Units.Length.Foot);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("altitude");
    }

    @Test
    public void ShouldThrowIfUnitIsNull_withConversion() {
        standardAtmosphere.calculatePressureAltitude(new Measurement(0, Units.Pressure.InchMercury)
                , new Measurement(0, Units.Length.Foot)
                , null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("\" is not a valid unit for a inchmercury\"");
    }

    @Test
    public void testCalculateStandardPressureAtZero_withConversion() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.92), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot),
                Units.Length.Metre);

        assertEquals(result.getMagnitude().doubleValue(), 0d);
    }

    @Test
    public void testCalculateStandardPressure_withConversion() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.32), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(500), Units.Length.Foot),
                Units.Length.Metre);

        assertEquals(result.getMagnitude().doubleValue(), 335.28d, 0.01);
    }

    @Test
    public void testCalculateStandardPressure_withConversions() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(993), Units.Pressure.HectoPascal),
                new Measurement(BigDecimal.valueOf(500), Units.Length.Foot),
                Units.Length.Metre);

        assertEquals(result.getMagnitude().doubleValue(), 335.28d, 3);
    }

    //endregion

    //region calculatePressureAltitude(pressure)

    @Test
    public void ShouldThrowIfPressureIsNull_noElevation() {
        standardAtmosphere.calculatePressureAltitude(null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("altitude");
    }

    @Test
    public void testCalculateStandardPressureAtZero_noElevation() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.92), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot));

        assertEquals(result.getMagnitude().doubleValue(), 0d);
    }

    @Test
    public void testCalculateStandardPressure_noElevation() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.32), Units.Pressure.InchMercury));

        assertEquals(result.getMagnitude().doubleValue(), 600d);
    }

    //endregion

    //region calculatePressureAltitude(pressure, unit)

    @Test
    public void ShouldThrowIfPressureIsNull_noElevationWithConversion() {
        standardAtmosphere.calculatePressureAltitude(null, Units.Length.Foot);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("altitude");
    }

    @Test
    public void ShouldThrowIfUnitIsNull_noElevationWithConversion() {
        standardAtmosphere.calculatePressureAltitude(new Measurement(0, Units.Pressure.InchMercury)
                , (String)null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("\" is not a valid unit for a inchmercury\"");
    }

    @Test
    public void testCalculateStandardPressureAtZero_noElevationWithConversion() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.92), Units.Pressure.InchMercury),
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot),
                Units.Length.Metre);

        assertEquals(result.getMagnitude().doubleValue(), 0.0);
    }

    @Test
    public void testCalculateStandardPressure_noElevationWithConversion() {
        Measurement result = standardAtmosphere.calculatePressureAltitude(
                new Measurement(BigDecimal.valueOf(29.32), Units.Pressure.InchMercury),
                Units.Length.Metre);

        assertEquals(result.getMagnitude().doubleValue(), 182.88);
    }

    //endregion

}