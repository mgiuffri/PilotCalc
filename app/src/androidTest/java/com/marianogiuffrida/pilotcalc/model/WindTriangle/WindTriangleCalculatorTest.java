package com.marianogiuffrida.pilotcalc.model.WindTriangle;

import com.marianogiuffrida.pilotcalc.model.AngleConverter;

import junit.framework.TestCase;

public class WindTriangleCalculatorTest extends TestCase {

    private WindTriangleCalculator calculator;
    private AngleConverter angleConverter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        calculator = new WindTriangleCalculator();
        angleConverter = new AngleConverter();
    }

    public void testCalculateWindVector(){
        WindTriangleVector groundVector = new WindTriangleVector(angleConverter.ToRadians(175), 144 );
        WindTriangleVector airVector = new WindTriangleVector(angleConverter.ToRadians(160), 180);
        WindTriangleVector result = calculator.calculateWindVector(groundVector, airVector);
        assertNotNull(result);
        assertEquals(angleConverter.ToDegrees(result.directionInRadians), 118 , 1);
        assertEquals(result.speed, 55 , 1);
    }

    public void testCalculateGroundVector(){
        WindTriangleVector windVector = new WindTriangleVector(angleConverter.ToRadians(118), 55);
        WindTriangleVector airVector = new WindTriangleVector(angleConverter.ToRadians(160), 180);
        WindTriangleVector result = calculator.calculateGroundVector(windVector, airVector);
        assertNotNull(result);
        assertEquals(angleConverter.ToDegrees(result.directionInRadians), 175 , 1);
        assertEquals(result.speed, 144 , 1);
    }
}