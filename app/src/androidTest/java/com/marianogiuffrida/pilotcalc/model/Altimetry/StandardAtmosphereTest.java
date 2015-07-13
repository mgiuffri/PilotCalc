package com.marianogiuffrida.pilotcalc.model.Altimetry;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.marianogiuffrida.pilotcalc.data.SqlLiteDataStore;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

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

    public void testAltitudeAboveTropo(){
        Measurement result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
                new Measurement(BigDecimal.valueOf(40000), Units.Length.Foot));

        assertEquals(result.getMagnitude(),standardAtmosphere.StandardTropopauseTemperature.getMagnitude());
    }

    public void testAltitudeAboveTropoWithConversion(){
        Measurement result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
                new Measurement(BigDecimal.valueOf(12), Units.Length.Kilometre));

        assertEquals(result.getMagnitude(),standardAtmosphere.StandardTropopauseTemperature.getMagnitude());
    }

    public void testAltitude(){
        Measurement result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
                new Measurement(BigDecimal.valueOf(3000), Units.Length.Foot));

        assertEquals(result.getMagnitude(), new BigDecimal("9.0564"));
    }

    public void testAltitudeWithConv(){
        Measurement result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
                new Measurement(BigDecimal.valueOf(300), Units.Length.Metre));

        assertEquals(result.getMagnitude().doubleValue(), BigDecimal.valueOf(13.05).doubleValue(), 0.01);
    }

    public void testAltitudeAtSeaLevel(){
        Measurement result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
                new Measurement(BigDecimal.valueOf(0), Units.Length.Foot));

        assertEquals(result.getMagnitude(), standardAtmosphere.StandardTemperatureInCelsius.getMagnitude());
    }

}