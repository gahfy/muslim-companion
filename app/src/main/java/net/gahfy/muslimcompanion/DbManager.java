package net.gahfy.muslimcompanion;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DbManager extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "muslimcompanion.db";
    private static final int DATABASE_VERSION = 1;

    public DbManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}