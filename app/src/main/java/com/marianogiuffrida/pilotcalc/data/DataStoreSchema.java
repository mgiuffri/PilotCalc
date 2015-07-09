package com.marianogiuffrida.pilotcalc.data;

/**
 * Created by Mariano on 9/07/2015.
 */
public interface DataStoreSchema {
    String Conversions = "UnitConversions";

    interface UnitConversions {
        String ConversionTypeColumn = "CONVERSION_TYPE";
        String FromUnitColumn = "FROM_UNIT";
        String FromUnitAbbrXolumn = "FROM_UNIT_SYMBOL";
        String ToUnitColumn = "TO_UNIT";
        String ToUnitAbbrColumn = "TO_UNIT_SYMBOL";
        String ConversionFactorColumn = "FACTOR";
        String OffsetColumn = "OFFSET";
        String ValueOffsetColumn = "VALUE_OFFSET";
    }
}
