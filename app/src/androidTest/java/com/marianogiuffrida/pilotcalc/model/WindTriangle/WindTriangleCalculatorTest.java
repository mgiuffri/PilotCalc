package com.marianogiuffrida.pilotcalc.model.WindTriangle;

import android.test.AndroidTestCase;

import com.marianogiuffrida.pilotcalc.data.SqlLiteDataStore;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

public class WindTriangleCalculatorTest extends AndroidTestCase {

    private WindTriangleCalculator calculator;
    private UnitConversionRepository unitConversions;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        unitConversions = new UnitConversionRepository(new SqlLiteDataStore(this.getContext()));
        calculator = new WindTriangleCalculator(unitConversions);
    }

    public void testCalculateWindVector(){
        WindTriangleVector groundVector = new WindTriangleVector(new CompassDirection(175), new Measurement(144, Units.Speed.Knot));
        WindTriangleVector airVector = new WindTriangleVector(new CompassDirection(160), new Measurement(180, Units.Speed.Knot));
        WindTriangleVector result = calculator.calculateWindVector(groundVector, airVector);
        assertNotNull(result);
        assertEquals(result.getDirection().getDegrees(), 118 , 1);
        assertEquals(result.getSpeed().getMagnitude().doubleValue(), 55 , 1);
    }

    public void testCalculateGroundVector(){
        WindTriangleVector windVector = new WindTriangleVector(new CompassDirection(118), new Measurement(55, Units.Speed.Knot));
        WindTriangleVector airVector = new WindTriangleVector(new CompassDirection(160), new Measurement(180, Units.Speed.Knot));
        WindTriangleVector result = calculator.calculateGroundVector(windVector, airVector);
        assertNotNull(result);
        assertEquals(result.getDirection().getDegrees(), 175 , 1);
        assertEquals(result.getSpeed().getMagnitude().doubleValue(), 144 , 1);
    }

    public void testCalculateAirVector(){
        WindTriangleVector windVector = new WindTriangleVector(new CompassDirection(118), new Measurement(55, Units.Speed.Knot));
        WindTriangleVector groundVector = new WindTriangleVector(new CompassDirection(175), new Measurement(144, Units.Speed.Knot));
        WindTriangleVector result = calculator.calculateAirVector(windVector, groundVector);
        assertNotNull(result);
        assertEquals(result.getDirection().getDegrees(), 160 , 1);
        assertEquals(result.getSpeed().getMagnitude().doubleValue(), 180 , 1);
    }

    public void testCalculateAirVector2(){
        WindTriangleVector windVector = new WindTriangleVector(new CompassDirection(120), new Measurement(45, Units.Speed.Knot));
        WindTriangleVector groundVector = new WindTriangleVector(new CompassDirection(56), new Measurement(166, Units.Speed.Knot));
        WindTriangleVector result = calculator.calculateAirVector(windVector, groundVector);
        assertNotNull(result);
        assertEquals(result.getDirection().getDegrees(), 68 , 1);
        assertEquals(result.getSpeed().getMagnitude().doubleValue(), 190 , 1);
    }

    public void testCalculateHeadingAndGroundSpeed(){
        WindTriangleVector windVector = new WindTriangleVector(new CompassDirection(118), new Measurement(55, Units.Speed.Knot));
        WindTriangleVector result = calculator.calculateHeadingAndGroundSpeed(windVector,
                new CompassDirection(175), new Measurement(180, Units.Speed.Knot));

        assertNotNull(result);
        assertEquals(result.getDirection().getDegrees(), 160 , 1);
        assertEquals(result.getSpeed().getMagnitude().doubleValue(), 144 , 1);
    }

    public void testCalculateHeadingAndGroundSpeed2(){
        WindTriangleVector windVector = new WindTriangleVector(new CompassDirection(100), new Measurement(40, Units.Speed.Knot));
        WindTriangleVector result = calculator.calculateHeadingAndGroundSpeed(windVector,
                new CompassDirection(186), new Measurement(180, Units.Speed.Knot));

        assertNotNull(result);
        assertEquals(result.getDirection().getDegrees(), 173 , 1);
        assertEquals(result.getSpeed().getMagnitude().doubleValue(), 172 , 1);
    }

    public void testCalculateWindComponents(){
        WindTriangleVector windVector = new WindTriangleVector(new CompassDirection(60), new Measurement(20, Units.Speed.Knot));
        WindComponents result = calculator.calculateWindComponents(new CompassDirection(30), windVector);

        assertNotNull(result);
        assertEquals(result.getHeadWind().getMagnitude().doubleValue(), 17.32 , 1);
        assertEquals(result.getCrossWind().getMagnitude().doubleValue(), 10 , 1);
    }
}