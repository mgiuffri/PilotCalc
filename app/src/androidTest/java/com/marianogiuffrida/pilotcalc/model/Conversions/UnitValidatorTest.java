package com.marianogiuffrida.pilotcalc.model.Conversions;

import junit.framework.TestCase;

/**
 * Created by Mariano on 13/07/2015.
 */
public class UnitValidatorTest extends TestCase {


    public void testOnce(){
        assertTrue(UnitValidator.isUnitSupported(Units.Length.Foot, Units.Length.class));
    }

    public void testFails(){
        assertFalse(UnitValidator.isUnitSupported(Units.Length.Foot, Units.Weight.class));
    }
}