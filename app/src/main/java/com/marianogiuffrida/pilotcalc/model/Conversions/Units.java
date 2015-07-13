package com.marianogiuffrida.pilotcalc.model.Conversions;

public interface Units {

    interface Weight {
        String Name = "WEIGHT";

        String Gram = "GRAM";
        String Kilogram = "KILOGRAM";
        String Pound = "POUND";
    }

    interface Length {
        String Name = "LENGTH";

        String Centimetre = "CM";
        String Foot = "FOOT";
        String Inch = "INCH";
        String Kilometre = "KILOMETRE";
        String Metre = "METRE";
        String Millimetre = "MILLIMETRE";
        String NauticalMile = "NAUTICALMILE";
        String StatuteMile = "STATUTEMILE";
        String Yard = "YARD";
    }

    interface Speed {
        String Name = "SPEED";

        String Foot_Per_Minute = "FOOT_PER_MINUTE";
        String Foot_Per_Second = "FOOT_PER_SECOND";
        String Kilometre_Per_Hour = "KILOMETRE_PER_HOUR";
        String Knot = "KNOT";
        String Metre_Per_Second = "METRE_PER_SECOND";
        String Mile_Per_Hour = "MILE_PER_HOUR";
    }

    interface Volume {
        String Name = "VOLUME";

        String ImperialGallon = "IMPERIAL_GALLON";
        String Litre = "LITRE";
        String UsGallon = "USGALLON";
    }

    interface Pressure {
        String Name = "PRESSURE";

        String Bar = "BAR";
        String HectoPascal = "HECTOPASCAL";
        String InchMercury = "INCHOFMERCURY";
    }

    interface Temperature {
        String Name = "TEMPERATURE";

        String Celsius ="CELSIUS";
        String Fahrenheit="FAHRENHEIT";
        String Kelvin ="KELVIN";
    }
}
