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

    public void testCalculateAirVector(){
        WindTriangleVector windVector = new WindTriangleVector(angleConverter.ToRadians(118), 55);
        WindTriangleVector groundVector = new WindTriangleVector(angleConverter.ToRadians(175), 144 );
        WindTriangleVector result = calculator.calculateAirVector(windVector, groundVector);
        assertNotNull(result);
        assertEquals(result.speed, 180 , 1);
        assertEquals(angleConverter.ToDegrees(result.directionInRadians), 160 , 1);
    }

    public void testCalculateAirVector2(){
        WindTriangleVector windVector = new WindTriangleVector(angleConverter.ToRadians(120), 45);
        WindTriangleVector groundVector = new WindTriangleVector(angleConverter.ToRadians(56), 166 );
        WindTriangleVector result = calculator.calculateAirVector(windVector, groundVector);
        assertNotNull(result);
        assertEquals(result.speed, 190 , 1);
        assertEquals(angleConverter.ToDegrees(result.directionInRadians), 68 , 1);
    }
    public void testCalculateHeadingAndGroundSpeed(){
        WindTriangleVector windVector = new WindTriangleVector(angleConverter.ToRadians(118), 55);
        WindTriangleVector result = calculator.calculateHeadingAndGroundSpeed(windVector,
                angleConverter.ToRadians(175), 180);

        assertNotNull(result);
        assertEquals(angleConverter.ToDegrees(result.directionInRadians), 160 , 1);
        assertEquals(result.speed, 144 , 1);
    }

    public void testCalculateHeadingAndGroundSpeed2(){
        WindTriangleVector windVector = new WindTriangleVector(angleConverter.ToRadians(100), 40);
        WindTriangleVector result = calculator.calculateHeadingAndGroundSpeed(windVector,
                angleConverter.ToRadians(186), 180);

        assertNotNull(result);
        assertEquals(angleConverter.ToDegrees(result.directionInRadians), 173 , 1);
        assertEquals(result.speed, 172 , 1);
    }
}