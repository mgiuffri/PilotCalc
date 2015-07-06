package com.marianogiuffrida.pilotcalc.model.WindTriangle;

import com.marianogiuffrida.helpers.ArgumentCheck;

/**
* Created by Mariano on 6/07/2015.
*/
public class WindTriangleVector {
    double speed;
    double directionInRadians;

    public WindTriangleVector(double direction, double speed){
        this.directionInRadians = direction;
        this.speed = speed;
    }

    public double getDirectionInRadians() {
        return directionInRadians;
    }

    public double getSpeed() {
        return speed;
    }
}
