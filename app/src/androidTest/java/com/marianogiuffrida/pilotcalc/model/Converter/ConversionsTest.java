package com.marianogiuffrida.pilotcalc.model.Converter;

import android.test.AndroidTestCase;

import com.marianogiuffrida.pilotcalc.data.SqlLiteDataStore;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Conversions.ConversionCalculator;
import com.marianogiuffrida.pilotcalc.model.Common.Unit;
import com.marianogiuffrida.pilotcalc.model.Conversions.UnitConversionDescriptor;

import java.util.List;

public class ConversionsTest extends AndroidTestCase {

    private UnitConversionRepository unitConversionRepo;
    private ConversionCalculator converter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        unitConversionRepo = new UnitConversionRepository(new SqlLiteDataStore(this.getContext()));
        converter = new ConversionCalculator(unitConversionRepo);
    }

    public void testConvertValue() throws Exception {
        UnitConversionDescriptor d = new UnitConversionDescriptor("test", null, null, 2, -21, 1);
        double result = converter.convert(10, d);
        assertEquals(result, 1, 0.001);
    }

    public void testQueryAllUnitConversion() {
        List<UnitConversionDescriptor> all = unitConversionRepo.getSupportedUnitConversions();
        assertEquals(all.size(), 126.0, 0.0);
    }

    public void testConversionBothSenses() {
        List<UnitConversionDescriptor> all = unitConversionRepo.getSupportedUnitConversions();
        double[] values = new double[]{10.0, 0, 1.2, -23.1};

        for (UnitConversionDescriptor d : all){
            for (double i : values)
                doubleConvert(i, d);
        }
    }

    public void testDB() {
        List<Unit> from = unitConversionRepo.getSupportedUnits();
        assert (from.size() > 0);
    }

    private void doubleConvert(double initialValue, UnitConversionDescriptor d) {
        double convertedValue = converter.convert(initialValue, d);
        UnitConversionDescriptor reverseDesc = unitConversionRepo.getUnitConversionDescriptorBySourceDestination(d.getDestinationUnit().Name, d.getSourceUnit().Name);
        assertNotNull(String.format("%s -> %s", d.getDestinationUnit(), d.getSourceUnit()),
                reverseDesc);
        assertEquals(
                String.format("%s -> %s: %f", d.getSourceUnit(), d.getDestinationUnit(), convertedValue),
                initialValue,
                converter.convert(convertedValue, reverseDesc),
                0.001);
    }
}