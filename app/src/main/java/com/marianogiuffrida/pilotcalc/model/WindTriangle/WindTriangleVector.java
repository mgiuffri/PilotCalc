package com.marianogiuffrida.pilotcalc.model.WindTriangle;

import com.marianogiuffrida.helpers.ArgumentCheck;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

/**
* Created by Mariano on 6/07/2015.
*/
public class WindTriangleVector {
    private Measurement speed;
    private CompassDirection direction;

    public WindTriangleVector(CompassDirection direction, Measurement speed){
        ArgumentCheck.IsNotNull(speed, "speed");
        Units.Validator.check(speed, Units.Speed.class);

        this.direction = direction;
        this.speed = speed;
    }

    public CompassDirection getDirection() {
        return direction;
    }

    public Measurement getSpeed() {
        return speed;
    }
}
