package com.marianogiuffrida.pilotcalc.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.marianogiuffrida.pilotcalc.model.Conversions.IUnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Conversions.UnitConversionDescriptor;
import com.marianogiuffrida.pilotcalc.model.comm.Unit;

import java.util.LinkedList;
import java.util.List;

public class UnitConversionRepository implements IUnitConversionRepository {

    private final UnitConversionDescriptor identityConversion = new UnitConversionDescriptor("",null,null,1,0,0);
    private final SQLiteDatabase readableDb;

    public UnitConversionRepository(SqlLiteDataStore dataStore){
        readableDb = dataStore.getReadOnlyData();
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
                        DataStoreSchema.UnitConversions.FromUnitColumn + " = ? " +
                                "and " + DataStoreSchema.UnitConversions.ToUnitColumn + " = ?"),
                new String[]{sourceUnit, destinationUnit});
        if(resultList != null && resultList.size() > 0)
            return resultList.get(0);
        else
            return null;
    }

    @Override
    public List<Unit> getSupportedUnits() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DataStoreSchema.Conversions);
        qb.setDistinct(true);
        Cursor c = qb.query(readableDb, new String[]{DataStoreSchema.UnitConversions.FromUnitColumn, DataStoreSchema.UnitConversions.FromUnitAbbrXolumn}, null, null, null, null, null);
        c.moveToFirst();
        List<Unit> fromUnits = new LinkedList<>();
        do{
            fromUnits.add(new Unit(c.getString(0), c.getString(1)));
        }while (c.moveToNext());
        return fromUnits;
    }

    @Override
    public List<Unit> getSupportedUnitsByConversionType(String conversionType) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DataStoreSchema.Conversions);
        qb.setDistinct(true);
        Cursor c = qb.query(readableDb,
                new String[]{DataStoreSchema.UnitConversions.FromUnitColumn, DataStoreSchema.UnitConversions.FromUnitAbbrXolumn},
                String.format(DataStoreSchema.UnitConversions.ConversionTypeColumn + " = ?"),
                new String[]{conversionType}, null, null, DataStoreSchema.UnitConversions.FromUnitAbbrXolumn + " COLLATE NOCASE");
        c.moveToFirst();
        List<Unit> fromUnits = new LinkedList<>();
        do {
            fromUnits.add(new Unit(c.getString(0), c.getString(1)));
        } while (c.moveToNext());

        return fromUnits;
    }

    @Override
    public List<Unit> getDestinationUnitsBySourceUnit(String sourceUnit) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DataStoreSchema.Conversions);
        qb.setDistinct(true);
        Cursor c = qb.query(readableDb,
                new String[]{DataStoreSchema.UnitConversions.ToUnitColumn, DataStoreSchema.UnitConversions.ToUnitAbbrColumn},
                String.format(DataStoreSchema.UnitConversions.FromUnitColumn + " = ?"),
                new String[]{sourceUnit}, null, null, DataStoreSchema.UnitConversions.ToUnitAbbrColumn + " COLLATE NOCASE");
        c.moveToFirst();
        List<Unit> destinationUnits = new LinkedList<>();
        do {
            destinationUnits.add(new Unit(c.getString(0), c.getString(1)));
        } while (c.moveToNext());

        return destinationUnits;
    }

    private List<UnitConversionDescriptor> queryUnitConversionDescriptors(String selection, String[] selectionArgs) {
        List<UnitConversionDescriptor> conversions = null;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DataStoreSchema.Conversions);
        Cursor c = qb.query(readableDb, null, selection, selectionArgs, null, null, DataStoreSchema.UnitConversions.FromUnitAbbrXolumn
                + "," + DataStoreSchema.UnitConversions.ToUnitAbbrColumn + " COLLATE NOCASE");
        if( c != null && c.moveToFirst() ){
            conversions = new LinkedList<>();
            do{
                UnitConversionDescriptor uc = new UnitConversionDescriptor(
                        c.getString(c.getColumnIndexOrThrow(DataStoreSchema.UnitConversions.ConversionTypeColumn)),
                        new Unit(c.getString(c.getColumnIndexOrThrow(DataStoreSchema.UnitConversions.FromUnitColumn)),
                                c.getString(c.getColumnIndexOrThrow(DataStoreSchema.UnitConversions.FromUnitAbbrXolumn))),
                        new Unit(c.getString(c.getColumnIndexOrThrow(DataStoreSchema.UnitConversions.ToUnitColumn)),
                                c.getString(c.getColumnIndexOrThrow(DataStoreSchema.UnitConversions.ToUnitAbbrColumn))),
                        c.getDouble(c.getColumnIndexOrThrow(DataStoreSchema.UnitConversions.ConversionFactorColumn)),
                        c.getDouble(c.getColumnIndexOrThrow(DataStoreSchema.UnitConversions.OffsetColumn)),
                        c.getDouble(c.getColumnIndexOrThrow(DataStoreSchema.UnitConversions.ValueOffsetColumn)));
                conversions.add(uc);
            }while (c.moveToNext());
        }else{
            Log.d("This", "null cursor");
        }
        return conversions;
    }
}
