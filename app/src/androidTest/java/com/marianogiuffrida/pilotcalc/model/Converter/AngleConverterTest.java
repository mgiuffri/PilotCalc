package com.marianogiuffrida.pilotcalc.model.Converter;

import com.marianogiuffrida.pilotcalc.model.Conversions.AngleConverter;

import junit.framework.TestCase;

public class AngleConverterTest extends TestCase {

    public void testConvertToRadians(){
        double result = AngleConverter.toRadians(180);
        assertEquals(result, 3.14159265, 0.001);
    }

    public void testConvertToRadiansFromDegreeAndMinutes(){
        double result = AngleConverter.toRadians(180, 0, 0);
        assertEquals(result, 3.14159265, 0.001);
    }

    public void testConvertToDegree(){
        double result = AngleConverter.toDegrees(3.14159265);
        assertEquals(result, 180, 0.001);
    }

    public void testConvertToDegrees(){
        double result = AngleConverter.toDegrees(180,0,0);
        assertEquals(result, 180, 0);
    }

    public void testConvertZeroDegreesToRadians(){
        double result = AngleConverter.toRadians(0);
        assertEquals(result, 0, 0);
    }

    public void testConvertZeroRadiansToDegrees(){
        double result = AngleConverter.toDegrees(0);
        assertEquals(result, 0, 0);
    }


}