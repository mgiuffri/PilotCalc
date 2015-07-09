package com.marianogiuffrida.pilotcalc.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.marianogiuffrida.pilotcalc.model.Conversions.ConversionCalculator;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Mariano on 9/07/2015.
 */
public class SqlLiteDataStore extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "UnitConversions.db";
    private static final int DATABASE_VERSION = 1;

    public SqlLiteDataStore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLiteDatabase getReadOnlyData(){
        return getReadableDatabase();
    }
}
