package com.marianogiuffrida.pilotcalc.model;

/**
 * Created by Mariano on 11/01/2015.
 */
public class UnitConversionDescriptor {
    private String sourceUnit;
    private String destionationUnit;
    private double conversionFactor;
    private double offset;
    private double valueOffset;

    public String getSourceUnit() {
        return sourceUnit;
    }

    public void setSourceUnit(String sourceUnit) {
        this.sourceUnit = sourceUnit;
    }

    public String getDestionationUnit() {
        return destionationUnit;
    }

    public void setDestionationUnit(String destionationUnit) {
        this.destionationUnit = destionationUnit;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getValueOffset() {
        return valueOffset;
    }

    public void setValueOffset(double valueOffset) {
        this.valueOffset = valueOffset;
    }

    public String toString(){
        return String.format("ConversionDescriptor: %s -> %s => %.2f + (%.2f* (value + (%.2f))",
                sourceUnit, destionationUnit, offset, conversionFactor, valueOffset);
    }
}
