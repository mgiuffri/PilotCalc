package com.marianogiuffrida.pilotcalc.model;

/**
 * Created by Mariano on 6/07/2015.
 */
public final class AngleConverter {

    public final double ToRadians(int d, int m, int s){
        return ToRadians(d + m/60 + s/3600);
    }

    public final double ToRadians(double degree){
        return 0.0174532925 * degree;
    }

    public final double ToDegrees(double radians){
        return 57.2957795 * radians;
    }



}
