package com.marianogiuffrida.pilotcalc.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.LinkedList;
import java.util.List;

public class UnitConversionDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "UnitConversions.db";
    private static final int DATABASE_VERSION = 1;

    public interface TABLES {
        String Conversions = "UnitConversions";
    }

    public interface UnitConversionColumns {
        String FromUnit = "FROM_UNIT";
        String ToUnit = "TO_UNIT";
        String ConversionFactor = "FACTOR";
        String Offset = "OFFSET";
        String ValueOffset = "VALUE_OFFSET";
    }

    public UnitConversionDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<UnitConversionDescriptor> getAllUnitConversionDescriptors() {
        return getUnitConversionDescriptors(null, null);
    }

    public UnitConversionDescriptor getUnitConversionDescriptor(String sourceUnit, String destinationUnit){
        List<UnitConversionDescriptor> resultList =  getUnitConversionDescriptors( String.format(
                        UnitConversionColumns.FromUnit + " = ?" +
                                "and " + UnitConversionColumns.ToUnit + " = ?"),
                new String[]{sourceUnit, destinationUnit});
        if(resultList != null && resultList.size() > 0)
            return resultList.get(0);
        else
            return null;
    }

    public List<UnitConversionDescriptor> getUnitConversionDescriptors(String selection, String[] selectionArgs) {
        List<UnitConversionDescriptor> conversions = null;
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLES.Conversions);
        Cursor c = qb.query(db, null, selection, selectionArgs, null, null, null);
        if( c != null && c.moveToFirst() ){
            conversions = new LinkedList<>();
            do{
                UnitConversionDescriptor uc = new UnitConversionDescriptor();
                uc.setSourceUnit(c.getString(0));
                uc.setDestionationUnit(c.getString(1));
                uc.setConversionFactor(Double.parseDouble(c.getString(2)));
                uc.setOffset(Double.parseDouble(c.getString(3)));
                uc.setValueOffset(Double.parseDouble(c.getString(4)));
                conversions.add(uc);
            }while (c.moveToNext());
        }else{
            Log.d("This", "null cursor");
        }
             return conversions;
    }

    public List<String> getFromUnits() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLES.Conversions);
        qb.setDistinct(true);
        Cursor c = qb.query(db, new String[]{UnitConversionColumns.FromUnit}, null, null, null, null, null);
        c.moveToFirst();
        List<String> fromUnits = new LinkedList<>();
        do{
            fromUnits.add(c.getString(0));
        }while (c.moveToNext());
        return fromUnits;
    }

    public List<String> getDestinationUnits(String sourceUnit) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLES.Conversions);
        qb.setDistinct(true);
        Cursor c = qb.query(db,
                new String[]{UnitConversionColumns.ToUnit},
                String.format(UnitConversionColumns.FromUnit + " = ?"),
                new String[]{sourceUnit}, null, null, null);
        c.moveToFirst();
        List<String> destinationUnits = new LinkedList<>();
        do {
            destinationUnits.add(c.getString(0));
        } while (c.moveToNext());

        return destinationUnits;
    }
}
