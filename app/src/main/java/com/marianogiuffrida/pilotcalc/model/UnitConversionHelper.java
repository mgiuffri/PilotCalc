package com.marianogiuffrida.pilotcalc.model;

public class UnitConversionHelper {
    public static double convertValue(double value, UnitConversionDescriptor conversionDescriptor){
        if (conversionDescriptor == null) throw new IllegalArgumentException("null descriptor");
        return (conversionDescriptor.getOffset()) +
                (value + conversionDescriptor.getValueOffset()) * conversionDescriptor.getConversionFactor();
    }
}
