package com.marianogiuffrida.pilotcalc.model.Conversions;

import com.marianogiuffrida.pilotcalc.model.Common.Unit;
import com.marianogiuffrida.pilotcalc.model.Conversions.UnitConversionDescriptor;

import java.util.List;

/**
 * Created by Mariano on 31/01/2015.
 */
public interface IUnitConversionRepository {
    List<UnitConversionDescriptor> getSupportedUnitConversions();

    UnitConversionDescriptor getUnitConversionDescriptorBySourceDestination(String sourceUnit, String destinationUnit);

    List<Unit> getSupportedUnits();

    List<Unit> getSupportedUnitsByConversionType(String conversionType);

    List<Unit> getDestinationUnitsBySourceUnit(String sourceUnit);
}
