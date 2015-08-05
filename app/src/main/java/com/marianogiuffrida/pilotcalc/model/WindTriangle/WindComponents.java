package com.marianogiuffrida.pilotcalc.model.WindTriangle;

import com.marianogiuffrida.helpers.ArgumentCheck;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

/**
 * Created by Mariano on 05/08/2015.
 */
public class WindComponents {

    private final Measurement headWind;
    private final Measurement crossWind;

    public WindComponents(Measurement headWind, Measurement crosswind){
        ArgumentCheck.IsNotNull(headWind, "headwind");
        ArgumentCheck.IsNotNull(crosswind, "crosswind");
        Units.Validator.check(headWind, Units.Speed.class);
        Units.Validator.check(crosswind, Units.Speed.class);

        this.headWind = headWind;
        this.crossWind = crosswind;
    }

    public Measurement getHeadWind() {
        return headWind;
    }

    public Measurement getCrossWind() {
        return crossWind;
    }
}
