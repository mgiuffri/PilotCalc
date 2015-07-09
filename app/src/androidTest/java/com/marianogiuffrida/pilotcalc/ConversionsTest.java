package com.marianogiuffrida.pilotcalc;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Common.Unit;
import com.marianogiuffrida.pilotcalc.model.Conversions.UnitConversionDescriptor;

import java.util.List;

public class ConversionsTest extends ApplicationTestCase<Application> {

    private UnitConversionRepository unitConversions;

    public ConversionsTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        unitConversions = new UnitConversionRepository(this.getContext());
    }

    public void testConvertValue() throws Exception {
        UnitConversionDescriptor d = new UnitConversionDescriptor("test", null, null, 2, -21, 1);
        double result = unitConversions.getConverter().convertValue(10, d);
        assertEquals(result, 1, 0.001);
    }

    public void testqueryAllUnitConversion() {
        ;
        List<UnitConversionDescriptor> all = unitConversions.getSupportedUnitConversions();
        assertEquals(all.size(), 126.0, 0.0);
    }

    public void testConversionBothSenses() {
        List<UnitConversionDescriptor> all = unitConversions.getSupportedUnitConversions();
        double[] values = new double[]{10.0, 0, 1.2, -23.1};

        for (UnitConversionDescriptor d : all){
            for (double i : values)
                doubleConvert(i, d);
        }
    }

    public void testDB() {
        List<Unit> from = unitConversions.getSupportedUnits();
        assert (from.size() > 0);
    }

    private void doubleConvert(double initialValue, UnitConversionDescriptor d) {
        double convertedValue = unitConversions.getConverter().convertValue(initialValue, d);
        UnitConversionDescriptor reverseDesc = unitConversions.getUnitConversionDescriptorBySourceDestination(d.getDestinationUnit().Name, d.getSourceUnit().Name);
        assertNotNull(String.format("%s -> %s", d.getDestinationUnit(), d.getSourceUnit()),
                reverseDesc);
        assertEquals(
                String.format("%s -> %s: %f", d.getSourceUnit(), d.getDestinationUnit(), convertedValue),
                initialValue,
                unitConversions.getConverter().convertValue(convertedValue, reverseDesc),
                0.001);
    }

}