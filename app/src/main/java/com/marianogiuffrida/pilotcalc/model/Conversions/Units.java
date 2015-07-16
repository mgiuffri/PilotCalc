package com.marianogiuffrida.pilotcalc.model.Conversions;

import com.marianogiuffrida.helpers.StringUtils;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;

import java.lang.reflect.Field;

public interface Units {
    interface Weight extends Units {
        String Name = "WEIGHT";

        String Gram = "GRAM";
        String Kilogram = "KILOGRAM";
        String Pound = "POUND";
    }

    interface Length extends Units  {
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

    interface Speed extends Units  {
        String Name = "SPEED";

        String Foot_Per_Minute = "FOOT_PER_MINUTE";
        String Foot_Per_Second = "FOOT_PER_SECOND";
        String Kilometre_Per_Hour = "KILOMETRE_PER_HOUR";
        String Knot = "KNOT";
        String Metre_Per_Second = "METRE_PER_SECOND";
        String Mile_Per_Hour = "MILE_PER_HOUR";
    }

    interface Volume extends Units  {
        String Name = "VOLUME";

        String ImperialGallon = "IMPERIAL_GALLON";
        String Litre = "LITRE";
        String UsGallon = "USGALLON";
    }

    interface Pressure extends Units  {
        String Name = "PRESSURE";

        String Bar = "BAR";
        String HectoPascal = "HECTOPASCAL";
        String InchMercury = "INCHOFMERCURY";
    }

    interface Temperature extends Units  {
        String Name = "TEMPERATURE";

        String Celsius ="CELSIUS";
        String Fahrenheit="FAHRENHEIT";
        String Kelvin ="KELVIN";
    }

    /**
     * Created by Mariano on 13/07/2015.
     */
    final class Validator {
        public static void check(Measurement m, Class<? extends Units> unitType){
            check(m.getUnitName(), unitType);
        }

        public static void check(String unit, Class<? extends Units> unitType){
            if (!isUnitSupported(unit, unitType))
                try {
                    Object name = unitType.getField("Name").get(null);
                    String unitName = unit != null ? unit : "<unspecified>";
                    throw new IllegalArgumentException( unitName + " is not a valid unit for a " + name);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
        }

        public static Boolean isUnitSupported(String unit, Class<?> unitType){
            if(StringUtils.isNullOrWhiteSpace(unit)) return false;
            for (Field field : unitType.getDeclaredFields()) {
                String supportedUnit;
                try {
                    supportedUnit = (String) field.get(null);
                } catch (IllegalAccessException e) {
                    return false;
                }
                if(supportedUnit.equals(unit)) return true;
            }
            return false;
        }

    }
}
