package com.marianogiuffrida.pilotcalc.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.marianogiuffrida.helpers.ArgumentCheck;
import com.marianogiuffrida.pilotcalc.model.IUnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Measurement;
import com.marianogiuffrida.pilotcalc.model.Unit;
import com.marianogiuffrida.pilotcalc.model.UnitConversionDescriptor;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.LinkedList;
import java.util.List;

public class UnitConversions extends SQLiteAssetHelper implements IUnitConversionRepository {

    public interface TABLES {
        String Conversions = "UnitConversions";
    }

    public interface UnitConversionColumns {
        String ConversionType = "CONVERSION_TYPE";
        String FromUnit = "FROM_UNIT";
        String FromUnitAbbr = "FROM_UNIT_SYMBOL";
        String ToUnit = "TO_UNIT";
        String ToUnitAbbr = "TO_UNIT_SYMBOL";
        String ConversionFactor = "FACTOR";
        String Offset = "OFFSET";
        String ValueOffset = "VALUE_OFFSET";
    }

    public class Converter {

        public double convertValue(double value, UnitConversionDescriptor conversionDescriptor){
            ArgumentCheck.IsNotNull(conversionDescriptor, "conversionDescriptor");
            return (conversionDescriptor.getOffset()) +
                    (value + conversionDescriptor.getValueOffset()) * conversionDescriptor.getConversionFactor();
        }

        public double convertMeasurement(Measurement value, String toUnit) throws IllegalArgumentException{
            ArgumentCheck.IsNotNull(value, "value");
            ArgumentCheck.IsNotNullorEmpty(toUnit, "toUnit");
            UnitConversionDescriptor d = getUnitConversionDescriptorBySourceDestination(value.getUnit(), toUnit);

            if (d == null) throw new UnsupportedOperationException("no conversion from "
                    + value.getUnit()
                    + " to "
                    + toUnit);

            return convertValue(value.getMagnitude().doubleValue(), d);
        }
    }

    private static final String DATABASE_NAME = "UnitConversions.db";
    private static final int DATABASE_VERSION = 1;

    private final UnitConversionDescriptor identityConversion = new UnitConversionDescriptor("",null,null,1,0,0);
    private final Converter converter;

    public Converter getConverter() {
        return converter;
    }

    public UnitConversions(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.converter = new Converter();
    }

    @Override
    public List<UnitConversionDescriptor> getSupportedUnitConversions() {
        return queryUnitConversionDescriptors(null, null);
    }

    @Override
    public UnitConversionDescriptor getUnitConversionDescriptorBySourceDestination(String sourceUnit, String destinationUnit){
        if(sourceUnit==null || sourceUnit.length()==0 || destinationUnit==null || destinationUnit.length()==0) return null;
        if (sourceUnit.equals(destinationUnit)) return identityConversion;
        List<UnitConversionDescriptor> resultList =  queryUnitConversionDescriptors(String.format(
                        UnitConversionColumns.FromUnit + " = ? " +
                                "and " + UnitConversionColumns.ToUnit + " = ?"),
                new String[]{sourceUnit, destinationUnit});
        if(resultList != null && resultList.size() > 0)
            return resultList.get(0);
        else
            return null;
    }

    @Override
    public List<Unit> getSupportedUnits() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLES.Conversions);
        qb.setDistinct(true);
        Cursor c = qb.query(db, new String[]{UnitConversionColumns.FromUnit, UnitConversionColumns.FromUnitAbbr }, null, null, null, null, null);
        c.moveToFirst();
        List<Unit> fromUnits = new LinkedList<>();
        do{
            fromUnits.add(new Unit(c.getString(0), c.getString(1)));
        }while (c.moveToNext());
        return fromUnits;
    }

    @Override
    public List<Unit> getSupportedUnitsByConversionType(String conversionType) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLES.Conversions);
        qb.setDistinct(true);
        Cursor c = qb.query(db,
                new String[]{UnitConversionColumns.FromUnit, UnitConversionColumns.FromUnitAbbr},
                String.format(UnitConversionColumns.ConversionType + " = ?"),
                new String[]{conversionType}, null, null, UnitConversionColumns.FromUnitAbbr + " COLLATE NOCASE");
        c.moveToFirst();
        List<Unit> fromUnits = new LinkedList<>();
        do {
            fromUnits.add(new Unit(c.getString(0), c.getString(1)));
        } while (c.moveToNext());

        return fromUnits;
    }

    @Override
    public List<Unit> getDestinationUnitsBySourceUnit(String sourceUnit) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLES.Conversions);
        qb.setDistinct(true);
        Cursor c = qb.query(db,
                new String[]{UnitConversionColumns.ToUnit, UnitConversionColumns.ToUnitAbbr},
                String.format(UnitConversionColumns.FromUnit + " = ?"),
                new String[]{sourceUnit}, null, null, UnitConversionColumns.ToUnitAbbr + " COLLATE NOCASE");
        c.moveToFirst();
        List<Unit> destinationUnits = new LinkedList<>();
        do {
            destinationUnits.add(new Unit(c.getString(0), c.getString(1)));
        } while (c.moveToNext());

        return destinationUnits;
    }

    private List<UnitConversionDescriptor> queryUnitConversionDescriptors(String selection, String[] selectionArgs) {
        List<UnitConversionDescriptor> conversions = null;
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLES.Conversions);
        Cursor c = qb.query(db, null, selection, selectionArgs, null, null, UnitConversionColumns.FromUnitAbbr
                + "," + UnitConversionColumns.ToUnitAbbr + " COLLATE NOCASE");
        if( c != null && c.moveToFirst() ){
            conversions = new LinkedList<>();
            do{
                UnitConversionDescriptor uc = new UnitConversionDescriptor(
                        c.getString(c.getColumnIndexOrThrow(UnitConversionColumns.ConversionType)),
                        new Unit(c.getString(c.getColumnIndexOrThrow(UnitConversionColumns.FromUnit)),
                                c.getString(c.getColumnIndexOrThrow(UnitConversionColumns.FromUnitAbbr))),
                        new Unit(c.getString(c.getColumnIndexOrThrow(UnitConversionColumns.ToUnit)),
                                c.getString(c.getColumnIndexOrThrow(UnitConversionColumns.ToUnitAbbr))),
                        c.getDouble(c.getColumnIndexOrThrow(UnitConversionColumns.ConversionFactor)),
                        c.getDouble(c.getColumnIndexOrThrow(UnitConversionColumns.Offset)),
                        c.getDouble(c.getColumnIndexOrThrow(UnitConversionColumns.ValueOffset)));
                conversions.add(uc);
            }while (c.moveToNext());
        }else{
            Log.d("This", "null cursor");
        }
        return conversions;
    }
}
