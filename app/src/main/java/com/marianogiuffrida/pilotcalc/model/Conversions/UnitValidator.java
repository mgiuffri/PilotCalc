package com.marianogiuffrida.pilotcalc.model.Conversions;

import java.lang.reflect.Field;

/**
 * Created by Mariano on 13/07/2015.
 */
public final class UnitValidator {

    public static Boolean isUnitSupported(String unit, Class<?> unitType){

        for (Field field : unitType.getDeclaredFields()) {
            String supportedUnit;
            try {
                supportedUnit = (String) field.get(null);
            } catch (IllegalAccessException e) {
                return false;
            }
            if(supportedUnit.equals(unit)) return true;
        }
        return false;
    }

}
