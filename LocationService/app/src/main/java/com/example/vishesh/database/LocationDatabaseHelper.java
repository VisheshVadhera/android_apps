package com.example.vishesh.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**-------------------------------------------------------------------------------------------------
 *
 *  Author:     Vishesh Vadhera
 *  Written:    24/9/2015
 *
 *  This class defines the SQLiteDatabase that implements the underlying
 *  storage system used to store location updates when the internet connectivity isn't available.
 *--------------------------------------------------------------------------------------------------
 */
public class LocationDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "com.example.vishesh.locationdb";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_LOCATION =
                "CREATE TABLE "
                + LocationContract.TABLE_NAME
                + "("
                + LocationContract.LocationEntry._ID + " INTEGER PRIMARY KEY, "
                + LocationContract.LocationEntry.COLUMN_LATITUDE + " REAL NOT NULL, "
                + LocationContract.LocationEntry.COLUMN_LONGITUDE + " REAL NOT NULL, "
                + LocationContract.LocationEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL "
                + ");";

    public LocationDatabaseHelper(Context context){
        super(context,
            context.getCacheDir()
            + File.separator
            + DATABASE_NAME,
            null,
            DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_LOCATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + LocationContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
