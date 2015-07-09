package com.marianogiuffrida.pilotcalc.model.Altimetry;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Altimetry.StandardAtmosphere;

public class StandardAtmosphereTest extends ApplicationTestCase<Application> {

    private UnitConversionRepository unitConversions;
    private StandardAtmosphere standardAtmosphere;

    public StandardAtmosphereTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
       // unitConversions = new UnitConversionRepository(this.getContext());
        standardAtmosphere = new StandardAtmosphere(unitConversions);
    }

//    public void testAltitudeAboveTropo(){
//        BigDecimal result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
//                new Measurement(BigDecimal.valueOf(40000), "FOOT"));
//
//        assertEquals(result.doubleValue(), standardAtmosphere.StandardTropopauseTemperature.doubleValue(), 0.001);
//    }
//
//    public void testAltitudeAboveTropoWithConversion(){
//        BigDecimal result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
//                new Measurement(BigDecimal.valueOf(12), "KILOMETRE"));
//
//        assertEquals(result.doubleValue(), standardAtmosphere.StandardTropopauseTemperature.doubleValue(), 0.001);
//    }
//
//    public void testAltitude(){
//        BigDecimal result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
//                new Measurement(BigDecimal.valueOf(3000), "FOOT"));
//
//        assertEquals(result.doubleValue(), BigDecimal.valueOf(9.05640).doubleValue(), 0.001);
//    }
//
//    public void testAltitudeWithConv(){
//        BigDecimal result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
//                new Measurement(BigDecimal.valueOf(300), "METRE"));
//
//        assertEquals(result.doubleValue(), BigDecimal.valueOf(13.05).doubleValue(), 0.001);
//    }
//
//    public void testAltitudeAtSeaLevel(){
//        BigDecimal result = standardAtmosphere.calculateStandardTemperatureAtAltitude(
//                new Measurement(BigDecimal.valueOf(0), "FOOT"));
//
//        assertEquals(result.doubleValue(), standardAtmosphere.StandardTemperature.doubleValue(), 0.001);
//    }

}