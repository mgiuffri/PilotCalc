package com.marianogiuffrida.pilotcalc.model.WindTriangle;

import com.marianogiuffrida.pilotcalc.model.Conversions.AngleConverter;

/**
 * Created by Mariano on 05/08/2015.
 */
public class CompassDirection {

    private final double degrees;

    public CompassDirection(double degrees){
        this.degrees = degrees;
    }

    public static CompassDirection fromRadians(double radians){
        return new CompassDirection(AngleConverter.toDegrees(radians));
    }

    public CompassDirection(int d, int m, int s){
        this.degrees = AngleConverter.toDegrees(d, m, s);
    }

    public double getDegrees() {
        return degrees;
    }

    public double asRadians() {
        return AngleConverter.toRadians(degrees);
    }
}
