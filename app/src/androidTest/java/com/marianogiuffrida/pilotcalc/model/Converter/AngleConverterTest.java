package com.marianogiuffrida.pilotcalc.model.Converter;

import com.marianogiuffrida.pilotcalc.model.Conversions.AngleConverter;

import junit.framework.TestCase;

public class AngleConverterTest extends TestCase {

    private AngleConverter converter;

    public AngleConverterTest() {
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        converter = new AngleConverter();
    }

    public void testConvertToRadians(){
        double result = converter.ToRadians(180);
        assertEquals(result, 3.14159265, 0.001);
    }

    public void testConvertToRadiansFromDegreeAndMinutes(){
        double result = converter.ToRadians(180, 0, 0);
        assertEquals(result, 3.14159265, 0.001);
    }

    public void testConvertToDegree(){
        double result = converter.ToDegrees(3.14159265);
        assertEquals(result, 180, 0.001);
    }

    public void testConvertZeroDegreesToRadians(){
        double result = converter.ToRadians(0);
        assertEquals(result, 0, 0);
    }

    public void testConvertZeroRadiansToDegrees(){
        double result = converter.ToDegrees(0);
        assertEquals(result, 0, 0);
    }


}