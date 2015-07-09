package com.marianogiuffrida.pilotcalc.model.Conversions;

import com.marianogiuffrida.pilotcalc.model.comm.Unit;

/**
 * Created by Mariano on 11/01/2015.
 */
public class UnitConversionDescriptor {
    private final String type;
    private final Unit sourceUnit;
    private final Unit destinationUnit;
    private final double conversionFactor;
    private final double offset;
    private final double valueOffset;

    public UnitConversionDescriptor(String type,
                                    Unit sourceUnit,
                                    Unit destinationUnit,
                                    double conversionFactor,
                                    double offset,
                                    double valueOffset) {
        this.type = type;
        this.sourceUnit = sourceUnit;
        this.destinationUnit = destinationUnit;
        this.conversionFactor = conversionFactor;
        this.offset = offset;
        this.valueOffset = valueOffset;
    }

    public String getType() {
        return type;
    }

    public Unit getSourceUnit() {
        return sourceUnit;
    }

    public Unit getDestinationUnit() {
        return destinationUnit;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double getOffset() {
        return offset;
    }

    public double getValueOffset() {
        return valueOffset;
    }

    public String toString() {
        return String.format("ConversionDescriptor: %s: %s -> %s => %.2f + (%.2f* (value + (%.2f))",
                type, sourceUnit.Name, destinationUnit.Name, offset, conversionFactor, valueOffset);
    }
}
