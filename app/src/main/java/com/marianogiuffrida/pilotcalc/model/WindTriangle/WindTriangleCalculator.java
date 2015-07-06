package com.marianogiuffrida.pilotcalc.model.WindTriangle;

import com.marianogiuffrida.helpers.ArgumentCheck;

/**
 * Created by Mariano on 6/07/2015.
 */
public class WindTriangleCalculator {

    public WindTriangleVector calculateGroundVector(WindTriangleVector windVector, WindTriangleVector airVector){
        ArgumentCheck.IsNotNull(windVector, "windVector");
        ArgumentCheck.IsNotNull(airVector, "airVector");

        double windSpeed = windVector.speed;
        double windDirection = windVector.directionInRadians;
        double trueAirSpeed = airVector.speed;
        double trueHeading = airVector.directionInRadians;

        double groundSpeed = Math.sqrt(windSpeed * windSpeed
                + trueAirSpeed * trueAirSpeed
                - 2 * windSpeed * trueAirSpeed * Math.cos(trueHeading - windDirection));

        double windCorrectionAngle = Math.atan2(windSpeed * Math.sin(trueHeading - windDirection),
                                        trueAirSpeed - windSpeed * Math.cos(trueHeading - windDirection));

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

        double windSpeed = Math.sqrt(
                    Math.pow( trueAirSpeed - groundSpeed, 2)
                    + 4 * trueAirSpeed * groundSpeed * Math.pow(Math.sin((trueHeading - trueCourse)/2), 2));

        double windDirection = trueCourse +
                Math.atan2( trueAirSpeed * Math.sin(trueHeading - trueCourse),
                        trueAirSpeed * Math.cos(trueHeading - trueCourse) - groundSpeed);


        return new WindTriangleVector(windDirection % (2 * Math.PI), windSpeed);
    }
}
