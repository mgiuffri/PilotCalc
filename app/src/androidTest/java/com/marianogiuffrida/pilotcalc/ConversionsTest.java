package com.marianogiuffrida.pilotcalc;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.marianogiuffrida.pilotcalc.database.UnitConversionDatabase;
import com.marianogiuffrida.pilotcalc.model.UnitConversionDescriptor;
import com.marianogiuffrida.pilotcalc.model.UnitConversionHelper;

import java.util.List;

public class ConversionsTest extends ApplicationTestCase<Application> {

    public ConversionsTest() {
        super(Application.class);
    }

    public void testConvertValue() throws Exception {
        UnitConversionDescriptor d = new UnitConversionDescriptor("test", "from", "to", 2, -21, 1);
        double result = UnitConversionHelper.convertValue(10, d);
        assertEquals(result, 1, 0.001);
    }

    public void testqueryAllUnitConversion() {
        UnitConversionDatabase myDb = new UnitConversionDatabase(this.getContext());
        List<UnitConversionDescriptor> all = myDb.getSupportedUnitConversions();
        assertEquals(all.size(), 126.0, 0.0);
    }

    public void testConversionBothSenses() {
        UnitConversionDatabase myDb = new UnitConversionDatabase(this.getContext());
        List<UnitConversionDescriptor> all = myDb.getSupportedUnitConversions();
        double[] values = new double[]{10.0, 0, 1.2, -23.1};

        for (UnitConversionDescriptor d : all){
            for (double i : values)
                doubleConvert(myDb, i, d);
        }
    }

    public void testDB() {
        UnitConversionDatabase myDb = new UnitConversionDatabase(this.getContext());
        List<String> from = myDb.getSupportedUnits();
        assert (from.size() > 0);
    }

    private void doubleConvert(UnitConversionDatabase myDb, double initialValue, UnitConversionDescriptor d) {
        double convertedValue = UnitConversionHelper.convertValue(initialValue, d);
        UnitConversionDescriptor reverseDesc = myDb.getUnitConversionDescriptorBySourceDestination(d.getDestinationUnit(), d.getSourceUnit());
        assertNotNull(String.format("%s -> %s", d.getDestinationUnit(), d.getSourceUnit()),
                reverseDesc);
        assertEquals(
                String.format("%s -> %s: %f", d.getSourceUnit(), d.getDestinationUnit(), convertedValue),
                initialValue,
                UnitConversionHelper.convertValue(convertedValue, reverseDesc),
                0.001);
    }

}