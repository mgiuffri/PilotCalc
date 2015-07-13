package com.marianogiuffrida.pilotcalc.model.Conversions;

import com.marianogiuffrida.helpers.ArgumentCheck;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;

import java.math.BigDecimal;

/**
 * Created by Mariano on 9/07/2015.
 */
public class ConversionCalculator {

    private UnitConversionRepository unitConversions;

    public ConversionCalculator(UnitConversionRepository unitConversions) {
        this.unitConversions = unitConversions;
    }

    public double convert(double value, UnitConversionDescriptor conversionDescriptor) {
        ArgumentCheck.IsNotNull(conversionDescriptor, "conversionDescriptor");
        return (conversionDescriptor.getOffset()) +
                (value + conversionDescriptor.getValueOffset()) * conversionDescriptor.getConversionFactor();
    }

    public BigDecimal convert(BigDecimal input, UnitConversionDescriptor conversionDescriptor){
        return BigDecimal.valueOf(convert(input.doubleValue(), conversionDescriptor));
    }

    public Measurement convert(Measurement input, String toUnit) throws IllegalArgumentException {
        ArgumentCheck.IsNotNull(input, "input");
        ArgumentCheck.IsNotNullorEmpty(toUnit, "toUnit");
        UnitConversionDescriptor d = unitConversions.getUnitConversionDescriptorBySourceDestination(input.getUnitName(), toUnit);

        if (d == null) throw new UnsupportedOperationException("no conversion from "
                + input.getUnitName()
                + " to "
                + toUnit);

        return new Measurement(convert(input.getMagnitude(), d), toUnit);
    }
}
