package com.marianogiuffrida.pilotcalc.model.WindTriangle;

import com.marianogiuffrida.helpers.ArgumentCheck;

/**
 * Created by Mariano on 6/07/2015.
 */
public class WindTriangleCalculator {

    public WindTriangleVector calculateAirVector(WindTriangleVector windVector, WindTriangleVector groundVector){
        ArgumentCheck.IsNotNull(windVector, "windVector");
        ArgumentCheck.IsNotNull(groundVector, "groundVector");

        double windSpeed = windVector.speed;
        double windDirection = windVector.directionInRadians;
        double groundSpeed = groundVector.speed;
        double trueCourse = groundVector.directionInRadians;

        double windToTrackAngle = trueCourse - (Math.PI + windDirection) % (2 * Math.PI);

        double trueAirspeed = Math.sqrt(windSpeed * windSpeed
                + groundSpeed * groundSpeed
                - 2 * windSpeed * groundSpeed * Math.cos(windToTrackAngle));

        double windCorrectionAngle = Math.asin((windSpeed / trueAirspeed) * Math.sin(windDirection - trueCourse));

        double trueHeading = (trueCourse + windCorrectionAngle) % (2 * Math.PI);

        return new WindTriangleVector(trueHeading , trueAirspeed);
    }

    public WindTriangleVector calculateGroundVector(WindTriangleVector windVector, WindTriangleVector airVector){
        ArgumentCheck.IsNotNull(windVector, "windVector");
        ArgumentCheck.IsNotNull(airVector, "airVector");

        double windSpeed = windVector.speed;
        double windDirection = windVector.directionInRadians;
        double trueAirSpeed = airVector.speed;
        double trueHeading = airVector.directionInRadians;

        double windToHeadingAngle = trueHeading - windDirection;

        double groundSpeed = Math.sqrt(windSpeed * windSpeed
                + trueAirSpeed * trueAirSpeed
                - 2 * windSpeed * trueAirSpeed * Math.cos(windToHeadingAngle));

        double windCorrectionAngle = Math.atan2(windSpeed * Math.sin(windToHeadingAngle),
                trueAirSpeed - windSpeed * Math.cos(windToHeadingAngle));

        double course = (trueHeading + windCorrectionAngle) % (2 * Math.PI);

        return new WindTriangleVector(course, groundSpeed);
    }

    public WindTriangleVector calculateWindVector(WindTriangleVector groundVector, WindTriangleVector airVector){
        ArgumentCheck.IsNotNull(groundVector, "windVector");
        ArgumentCheck.IsNotNull(airVector, "airVector");

        double groundSpeed = groundVector.speed;
        double trueCourse = groundVector.directionInRadians;
        double trueAirSpeed = airVector.speed;
        double trueHeading = airVector.directionInRadians;

        double windSpeed = Math.sqrt(groundSpeed * groundSpeed
                + trueAirSpeed * trueAirSpeed
                - 2 * groundSpeed * trueAirSpeed * Math.cos(trueHeading - trueCourse));

        double windDirection = trueCourse +
                Math.atan2( trueAirSpeed * Math.sin(trueHeading - trueCourse),
                        trueAirSpeed * Math.cos(trueHeading - trueCourse) - groundSpeed);


        return new WindTriangleVector(windDirection % (2 * Math.PI), windSpeed);
    }

    public WindTriangleVector calculateHeadingAndGroundSpeed(WindTriangleVector windVector,
                                                             double trueCourse,
                                                             double trueAirspeed){

        double windSpeed = windVector.speed;
        double windDirection = windVector.directionInRadians;

        double SWC = (windSpeed / trueAirspeed) * Math.sin(windDirection - trueCourse);

        double trueHeading = trueCourse + Math.asin(SWC);

        double groundSpeed = trueAirspeed * Math.sqrt(1 - Math.pow(SWC,2))
                - windSpeed * Math.cos(windDirection - trueCourse);

        return new WindTriangleVector(trueHeading % (2 * Math.PI), groundSpeed);
    }
}
