package com.marianogiuffrida.pilotcalc.model.Altimetry;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.marianogiuffrida.pilotcalc.data.SqlLiteDataStore;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

public class StandardAtmosphereTest extends ApplicationTestCase<Application> {

    private UnitConversionRepository unitConversions;
    private StandardAtmosphere standardAtmosphere;

    public StandardAtmosphereTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        unitConversions = new UnitConversionRepository( new SqlLiteDataStore(this.getContext()));
        standardAtmosphere = new StandardAtmosphere(unitConversions);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void ShouldThrowIfCalculatingTempWithNullAltitude(){
            standardAtmosphere.calculateStandardTemperatureAtAltitude(null);
            exception.expect(IllegalArgumentException.class);
            exception.expectMessage("altitude");
    }

    @Test
    public void ShouldThrowIfCalculatingTempWithInvalidMeasure(){
        standardAtmosphere.calculateStandardTemperatureAtAltitude(new Measurement(1.0, Units.Pressure.HectoPascal));
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("HECTOPASCAL is not a valid unit for an altitude");
    }

    @Test
    public void ShouldThrowIfRequestingInvalidConversion(){
        standardAtmosphere.calculateStandardTemperatureAtAltitude(new Measurement(1.0, Units.Length.Foot), Units.Length.Foot);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("FOOT is not a valid unit for a temperature");
    }

    @Test
    public void calculateTemperatureAtAltitudeAboveTropopauseGivenInFeet(){
        Measurement result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
                new Measurement(BigDecimal.valueOf(40000), Units.Length.Foot));

        assertEquals(result.getMagnitude(),standardAtmosphere.StandardTropopauseTemperature.getMagnitude());
    }

    @Test
    public void calculateTemperatureAtAltitudeAboveTropopauseGivenInKilometre(){
        Measurement result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
                new Measurement(BigDecimal.valueOf(12), Units.Length.Kilometre));

        assertEquals(result.getMagnitude(),standardAtmosphere.StandardTropopauseTemperature.getMagnitude());
    }

    @Test
    public void calculateTemperatureAtAltitudeBelowTropopauseGivenInFeet(){
        Measurement result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
                new Measurement(BigDecimal.valueOf(3000), Units.Length.Foot));

        assertEquals(result.getMagnitude(), new BigDecimal("9.0564"));
    }

    @Test
    public void calculateTemperatureAtAltitudeBelowTropopauseGivenInMetre(){
        Measurement result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
                new Measurement(BigDecimal.valueOf(300), Units.Length.Metre));

        assertEquals(result.getMagnitude().doubleValue(), BigDecimal.valueOf(13.05).doubleValue(), 0.01);
    }

    @Test
    public void calculateTemperatureAtSeaLevelGivenInFeet(){
        Measurement result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot));

        assertEquals(result.getMagnitude(), standardAtmosphere.StandardTemperatureInCelsius.getMagnitude());
    }
}