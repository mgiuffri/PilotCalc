package com.marianogiuffrida.pilotcalc.model;

/**
 * Created by Mariano on 11/01/2015.
 */
public class UnitConversionDescriptor {
    private final String type;
    private final String sourceUnit;
    private final String destinationUnit;
    private final double conversionFactor;
    private final double offset;
    private final double valueOffset;

    public UnitConversionDescriptor(String type,
                                    String sourceUnit,
                                    String destinationUnit,
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

    public String getSourceUnit() {
        return sourceUnit;
    }

    public String getDestinationUnit() {
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
                type, sourceUnit, destinationUnit, offset, conversionFactor, valueOffset);
    }
}
