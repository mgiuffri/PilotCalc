package com.marianogiuffrida.pilotcalc.database;

public class UnitConversionHelper {
    public static double convertValue(double value, UnitConversionDescriptor conversionDescriptor){
        return (conversionDescriptor.getOffset()) +
                (value + conversionDescriptor.getValueOffset()) * conversionDescriptor.getConversionFactor();
    }
}
