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
        UnitConversionDescriptor d = new UnitConversionDescriptor();
        d.setConversionFactor(2);
        d.setOffset(-21);
        d.setValueOffset(1);
        double result = UnitConversionHelper.convertValue(10, d);
        assertEquals(result, 1, 0.001);
    }

    public void testqueryAllUnitConversion() {
        UnitConversionDatabase myDb = new UnitConversionDatabase(this.getContext());
        List<UnitConversionDescriptor> all = myDb.getAllUnitConversionDescriptors();
        assertEquals(all.size(), 126.0, 0.0);
    }

    public void testConversionBothSenses() {
        UnitConversionDatabase myDb = new UnitConversionDatabase(this.getContext());
        List<UnitConversionDescriptor> all = myDb.getAllUnitConversionDescriptors();
        double[] values = new double[]{10.0, 0, 1.2, -23.1};

        for (UnitConversionDescriptor d : all){
            for (double i : values)
                doubleConvert(myDb, i, d);
        }
    }

    public void testDB() {
        UnitConversionDatabase myDb = new UnitConversionDatabase(this.getContext());
        List<String> from = myDb.getFromUnits();
        assert (from.size() > 0);
    }

    private void doubleConvert(UnitConversionDatabase myDb, double initialValue, UnitConversionDescriptor d) {
        double convertedValue = UnitConversionHelper.convertValue(initialValue, d);
        UnitConversionDescriptor reverseDesc = myDb.getUnitConversionDescriptor(d.getDestionationUnit(), d.getSourceUnit());
        assertNotNull(String.format("%s -> %s", d.getDestionationUnit(), d.getSourceUnit()),
                reverseDesc);
        assertEquals(
                String.format("%s -> %s: %f", d.getSourceUnit(), d.getDestionationUnit(), convertedValue),
                initialValue,
                UnitConversionHelper.convertValue(convertedValue, reverseDesc),
                0.001);
    }

}