package com.marianogiuffrida.pilotcalc.model.WindTriangle;

import com.marianogiuffrida.helpers.ArgumentCheck;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Conversions.ConversionCalculator;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

/**
 * Created by Mariano on 6/07/2015.
 */
public class WindTriangleCalculator {

    private final ConversionCalculator conversionCalculator;

    public WindTriangleCalculator(UnitConversionRepository uc) {
        conversionCalculator = new ConversionCalculator(uc);
    }

    public WindTriangleVector calculateAirVector(WindTriangleVector windVector, WindTriangleVector groundVector, String resultUnit) {
        Units.Validator.check(resultUnit, Units.Speed.class);
        WindTriangleVector result = calculateAirVector(windVector, groundVector);
        Measurement convertedSpeed = conversionCalculator.convert(result.getSpeed(), resultUnit);
        return new WindTriangleVector(result.getDirection(), convertedSpeed);
    }

    public WindTriangleVector calculateAirVector(WindTriangleVector windVector, WindTriangleVector groundVector) {

        ArgumentCheck.IsNotNull(windVector, "windVector");
        ArgumentCheck.IsNotNull(groundVector, "groundVector");
        Units.Validator.check(windVector.getSpeed(), Units.Speed.class);
        Units.Validator.check(groundVector.getSpeed(), Units.Speed.class);

        double windSpeed = conversionCalculator.convert(windVector.getSpeed(), Units.Speed.Knot).getMagnitude().doubleValue();
        double windDirection = windVector.getDirection().asRadians();

        double groundSpeed = conversionCalculator.convert(groundVector.getSpeed(), Units.Speed.Knot).getMagnitude().doubleValue();
        double trueCourse = groundVector.getDirection().asRadians();

        double windToTrackAngle = trueCourse - (Math.PI + windDirection) % (2 * Math.PI);
        double trueAirspeed = Math.sqrt(windSpeed * windSpeed
                + groundSpeed * groundSpeed
                - 2 * windSpeed * groundSpeed * Math.cos(windToTrackAngle));

        double windCorrectionAngle = Math.asin((windSpeed / trueAirspeed) * Math.sin(windDirection - trueCourse));
        double trueHeading = (trueCourse + windCorrectionAngle) % (2 * Math.PI);

        return new WindTriangleVector(
                CompassDirection.fromRadians(trueHeading),
                new Measurement(trueAirspeed, Units.Speed.Knot));
    }

    public WindTriangleVector calculateGroundVector(WindTriangleVector windVector, WindTriangleVector airVector, String resultUnit) {
        Units.Validator.check(resultUnit, Units.Speed.class);
        WindTriangleVector result = calculateGroundVector(windVector, airVector);
        Measurement convertedSpeed = conversionCalculator.convert(result.getSpeed(), resultUnit);
        return new WindTriangleVector(result.getDirection(), convertedSpeed);
    }

    public WindTriangleVector calculateGroundVector(WindTriangleVector windVector, WindTriangleVector airVector) {
        ArgumentCheck.IsNotNull(windVector, "windVector");
        ArgumentCheck.IsNotNull(airVector, "airVector");
        Units.Validator.check(windVector.getSpeed(), Units.Speed.class);
        Units.Validator.check(airVector.getSpeed(), Units.Speed.class);

        double windSpeed = conversionCalculator.convert(windVector.getSpeed(), Units.Speed.Knot).getMagnitude().doubleValue();
        double windDirection = windVector.getDirection().asRadians();

        double trueAirSpeed = conversionCalculator.convert(airVector.getSpeed(), Units.Speed.Knot).getMagnitude().doubleValue();
        double trueHeading = airVector.getDirection().asRadians();

        double windToHeadingAngle = trueHeading - windDirection;
        double groundSpeed = Math.sqrt(windSpeed * windSpeed
                + trueAirSpeed * trueAirSpeed
                - 2 * windSpeed * trueAirSpeed * Math.cos(windToHeadingAngle));

        double windCorrectionAngle = Math.atan2(windSpeed * Math.sin(windToHeadingAngle),
                trueAirSpeed - windSpeed * Math.cos(windToHeadingAngle));
        double course = (trueHeading + windCorrectionAngle) % (2 * Math.PI);

        return new WindTriangleVector(
                CompassDirection.fromRadians(course),
                new Measurement(groundSpeed, Units.Speed.Knot));
    }

    public WindTriangleVector calculateWindVector(WindTriangleVector groundVector, WindTriangleVector airVector, String resultUnit) {
        Units.Validator.check(resultUnit, Units.Speed.class);
        WindTriangleVector result = calculateGroundVector(groundVector, airVector);
        Measurement convertedSpeed = conversionCalculator.convert(result.getSpeed(), resultUnit);
        return new WindTriangleVector(result.getDirection(), convertedSpeed);
    }

    public WindTriangleVector calculateWindVector(WindTriangleVector groundVector, WindTriangleVector airVector) {
        ArgumentCheck.IsNotNull(groundVector, "groundVector");
        ArgumentCheck.IsNotNull(airVector, "airVector");
        Units.Validator.check(groundVector.getSpeed(), Units.Speed.class);
        Units.Validator.check(airVector.getSpeed(), Units.Speed.class);

        double groundSpeed = conversionCalculator.convert(groundVector.getSpeed(), Units.Speed.Knot).getMagnitude().doubleValue();
        double trueCourse = groundVector.getDirection().asRadians();

        double trueAirSpeed = conversionCalculator.convert(airVector.getSpeed(), Units.Speed.Knot).getMagnitude().doubleValue();
        double trueHeading = airVector.getDirection().asRadians();

        double windSpeed = Math.sqrt(groundSpeed * groundSpeed
                + trueAirSpeed * trueAirSpeed
                - 2 * groundSpeed * trueAirSpeed * Math.cos(trueHeading - trueCourse));

        double windDirection = trueCourse +
                Math.atan2(trueAirSpeed * Math.sin(trueHeading - trueCourse),
                        trueAirSpeed * Math.cos(trueHeading - trueCourse) - groundSpeed);

        return new WindTriangleVector(
                CompassDirection.fromRadians(windDirection % (2 * Math.PI)),
                new Measurement(windSpeed, Units.Speed.Knot));
    }

    public WindTriangleVector calculateHeadingAndGroundSpeed(WindTriangleVector windVector,
                                                             CompassDirection trueCourse,
                                                             Measurement trueAirspeed,
                                                             String resultUnit) {
        Units.Validator.check(resultUnit, Units.Speed.class);
        WindTriangleVector result = calculateHeadingAndGroundSpeed(windVector, trueCourse, trueAirspeed);
        Measurement convertedSpeed = conversionCalculator.convert(result.getSpeed(), resultUnit);
        return new WindTriangleVector(result.getDirection(), convertedSpeed);
    }

    public WindTriangleVector calculateHeadingAndGroundSpeed(WindTriangleVector windVector,
                                                             CompassDirection trueCourse,
                                                             Measurement trueAirspeed) {
        ArgumentCheck.IsNotNull(windVector, "windVector");
        ArgumentCheck.IsNotNull(trueCourse, "trueCourse");
        ArgumentCheck.IsNotNull(trueAirspeed, "trueAirspeed");
        Units.Validator.check(windVector.getSpeed(), Units.Speed.class);
        Units.Validator.check(trueAirspeed, Units.Speed.class);

        double windSpeed = conversionCalculator.convert(windVector.getSpeed(), Units.Speed.Knot).getMagnitude().doubleValue();
        double TAS = conversionCalculator.convert(trueAirspeed, Units.Speed.Knot).getMagnitude().doubleValue();
        double windDirection = windVector.getDirection().asRadians();
        double TC = trueCourse.asRadians();
        double SWC = (windSpeed / TAS) * Math.sin(windDirection - TC);

        double trueHeading = TC + Math.asin(SWC);

        double groundSpeed = TAS * Math.sqrt(1 - Math.pow(SWC, 2))
                - windSpeed * Math.cos(windDirection - TC);

        return new WindTriangleVector(
                CompassDirection.fromRadians(trueHeading % (2 * Math.PI)),
                new Measurement(groundSpeed, Units.Speed.Knot));
    }

    public WindComponents calculateWindComponents(CompassDirection runwayDirection,
                                                  WindTriangleVector windVector,
                                                  String resultUnit) {
        Units.Validator.check(resultUnit, Units.Speed.class);
        WindComponents result = calculateWindComponents(runwayDirection, windVector);
        Measurement convertedHeadwind = conversionCalculator.convert(result.getHeadWind(), resultUnit);
        Measurement convertedCrosswind = conversionCalculator.convert(result.getCrossWind(), resultUnit);
        return new WindComponents(convertedHeadwind, convertedCrosswind);
    }

    public WindComponents calculateWindComponents(CompassDirection runwayDirection, WindTriangleVector windVector) {
        ArgumentCheck.IsNotNull(windVector, "windVector");
        Units.Validator.check(windVector.getSpeed(), Units.Speed.class);

        double windSpeed = windVector.getSpeed().getMagnitude().doubleValue();
        double windDirection = windVector.getDirection().asRadians();

        double diff = windDirection - runwayDirection.asRadians();
        return new WindComponents(
                new Measurement(windSpeed * Math.cos(diff), windVector.getSpeed().getUnit()),
                new Measurement(windSpeed * Math.sin(diff), windVector.getSpeed().getUnit()));
    }
}
