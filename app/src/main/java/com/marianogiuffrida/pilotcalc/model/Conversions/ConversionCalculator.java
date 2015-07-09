package com.marianogiuffrida.pilotcalc.model.Conversions;

import com.marianogiuffrida.helpers.ArgumentCheck;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;

/**
 * Created by Mariano on 9/07/2015.
 */
public class ConversionCalculator {

    private UnitConversionRepository unitConversions;

    public ConversionCalculator(UnitConversionRepository unitConversions) {
        this.unitConversions = unitConversions;
    }

    public double convertValue(double value, UnitConversionDescriptor conversionDescriptor) {
        ArgumentCheck.IsNotNull(conversionDescriptor, "conversionDescriptor");
        return (conversionDescriptor.getOffset()) +
                (value + conversionDescriptor.getValueOffset()) * conversionDescriptor.getConversionFactor();
    }

    public double convertMeasurement(Measurement value, String toUnit) throws IllegalArgumentException {
        ArgumentCheck.IsNotNull(value, "value");
        ArgumentCheck.IsNotNullorEmpty(toUnit, "toUnit");
        UnitConversionDescriptor d = unitConversions.getUnitConversionDescriptorBySourceDestination(value.getUnitName(), toUnit);

        if (d == null) throw new UnsupportedOperationException("no conversion from "
                + value.getUnitName()
                + " to "
                + toUnit);

        return convertValue(value.getMagnitude().doubleValue(), d);
    }
}
