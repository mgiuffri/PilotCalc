package com.marianogiuffrida.pilotcalc.model.Conversions;

/**
 * Created by Mariano on 6/07/2015.
 */
public final class AngleConverter {

    private AngleConverter(){}

    public static final double toRadians(int d, int m, int s){
        return toRadians(toDegrees(d, m, s));
    }

    public static final double toDegrees(int d, int m, int s){
        return d + m / 60 + s / 3600;
    }

    public static final double toRadians(double degree){
        return 0.0174532925 * degree;
    }

    public static final double toDegrees(double radians){
        return 57.2957795 * radians;
    }
}
