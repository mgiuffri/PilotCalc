package com.marianogiuffrida.pilotcalc.model;

import java.math.BigDecimal;

/**
 * Created by Mariano on 7/02/2015.
 * This class represents the value of a physical quantity as defined by
 * a Magnitude and a Unit of Measurement e.g. 15 meters => 15 is magnitude and meters is the unit
 */
public class Measurement {

    private final BigDecimal magnitude;
    private final Unit unit;

    public Measurement(BigDecimal value, String unit){
        magnitude = value;
        this.unit = new Unit(unit, unit);
    }

    public Measurement(BigDecimal value, Unit unit){
        magnitude = value;
        this.unit = unit;
    }

    public Unit getUnit(){
        return unit;
    }

    public String getUnitName() {
        return unit.Name;
    }

    public BigDecimal getMagnitude() {
        return magnitude;
    }

    @Override
    public String toString() {
        return String.format("Measurement{%s %s}", magnitude, unit.Symbol);
    }
}
