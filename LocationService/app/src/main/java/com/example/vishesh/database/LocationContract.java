package com.example.vishesh.database;

import android.provider.BaseColumns;

/**-------------------------------------------------------------------------------------------------
 *
 *  Author:     Vishesh Vadhera
 *  Written:    24/9/2015
 *
 *
 *  LocationContract class acts as a binding contract between a client and a database and helps the
 *  client in getting access to the database
 *--------------------------------------------------------------------------------------------------
 */
public class LocationContract {

    // Name of the Table.
    public static final String TABLE_NAME = "location_table";


    public LocationContract(){

    }

    /**
     * Nested class that defines the columns which should be present in the database.
     */
    public static class LocationEntry implements BaseColumns{

        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }

}
