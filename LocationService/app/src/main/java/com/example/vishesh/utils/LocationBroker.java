package com.example.vishesh.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import retrofit.Retrofit;


import com.example.vishesh.database.LocationContract;
import com.example.vishesh.database.LocationDatabaseHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**-------------------------------------------------------------------------------------------------
 *
 *  Author:     Vishesh Vadhera
 *  Written:    24/9/2015
 *
 *  This is the LocationBroker class that acts as a broker between the LocationService & the Server
 *  or the LocationService & the SQLiteDatabase, depending on the internet connectivity. The calls
 *  to the HTTP Server are made via calls to the Retrofit's REST Adapter.
 * -------------------------------------------------------------------------------------------------
 */
public class LocationBroker {


    // Address of the server where the location has to be sent.
    public static final String LOCATION_SERVER = "http://ABCD.EFGH.XYZ";


    // Used for logging messages to the logcat.
    private static final String LOG = LocationBroker.class.getSimpleName();


    // Helper Object that is used to get access to the underlying SQLite Database.
    private LocationDatabaseHelper mLocationDatabaseHelper;


    // String that defines the action of the intent broadcasted
    // to prompt the user to switch on network connectivity.
    public static final String INTERNET_PROMPT_INTENT_ACTION =
                "com.example.vishesh.locationservice.utils.locationbroker.internetpromptaction";


    // List Of Columns which have to be extracted from the database.
    private String mColumnsToReturn[] = {LocationContract.LocationEntry.COLUMN_LONGITUDE,
                                         LocationContract.LocationEntry.COLUMN_LATITUDE,
                                         LocationContract.LocationEntry.COLUMN_TIMESTAMP};


    // Object that is used to make HTTP Requests to the server.
    private LocationServiceProxy mLocationProxy;


    private Context mContext;


    /*
     * Constructor that initializes the DataBaseHelper Object
     * and Retrofit based REST Adapter.
     */
    public LocationBroker(Context context){

        mLocationDatabaseHelper = new LocationDatabaseHelper(context);

        mContext = context;

        Retrofit adapter = new Retrofit.Builder().baseUrl(LOCATION_SERVER).build();
        mLocationProxy = adapter.create(LocationServiceProxy.class);
    }


    /*
     * Callback method that gets called, by the service to handle
     * the LocationData object, whether to store it or send it.
     */
    public void handleLocation(LocationData locationData){

        boolean hasNetAccess = hasInternetAccess();
        Log.d(LOG, "Net access is available? " + hasNetAccess);

        if(hasNetAccess){

            handleLocationOnConnectivity(locationData);
        }
        else{
            Log.d(LOG, "Internet Connection is not available, hence calling storeLocationInDatabase method");
            storeLocationInDatabase(locationData);
        }
    }


    /*
     * Callback method that gets called from handleLocation when it has
     * confirmed the availability of internet.This method decides how to
     * deal with the LocationData object.
     * It queries the database to check if it has any data stored in it.
     * If the obtained Cursor is not empty, that means that the internet
     * connectivity, was previously unavailable and has been restored, hence
     * it fetches all the items which were stored during the period of
     * internet unavailability, and sends them to the server, and removes
     * them from the database. And afterwards it sends the current location to
     * the server.
     */
    private void handleLocationOnConnectivity(LocationData location){

        SQLiteDatabase db = mLocationDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                                    + LocationContract.TABLE_NAME, null);

        if(cursor.getCount()!=0){

            Log.d(LOG, "Database has " + cursor.getCount() + " no of locations");
            cursor.moveToFirst();

            while(cursor.isAfterLast()==false){
                LocationData previousLocationData =
                        new LocationData
                                (cursor.getDouble(cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_LATITUDE)),
                                 cursor.getDouble(cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_LONGITUDE)),
                                 cursor.getString(cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_TIMESTAMP)));

                Log.d(LOG, "Latitude is: " + previousLocationData.getLatitude() +
                            ". Longitude is: " + previousLocationData.getLongitude() +
                            ". Timestamp is: " + previousLocationData.getTimestamp());

                //Uncomment this line to send location to server. mLocationProxy.sendLocation(previousLocationData);
                cursor.moveToNext();
            }
            db.delete(LocationContract.TABLE_NAME, null, null);
        }
        cursor.close();

        Log.d(LOG, "Latitude is: " + location.getLatitude() +
                ". Longitude is: " + location.getLongitude() +
                ". Timestamp is: " + location.getTimestamp());

        //Uncomment this line to send location data to server via retrofit. mLocationProxy.sendLocation(location);
    }


    /*
     * Callback method that gets called from handleLocation method,
     * when it has confirmed that the internet is not available. This
     * method then takes the current location and stores it into database.
     * Afterwards it broadcasts an intent to the activity signalling the
     * unavailability of the internet.
     */
    private void storeLocationInDatabase(LocationData locationData){

        Log.d(LOG, "Storing locationData in database");
        SQLiteDatabase db = mLocationDatabaseHelper.getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put(LocationContract.LocationEntry.COLUMN_LATITUDE, locationData.getLatitude());
        cvs.put(LocationContract.LocationEntry.COLUMN_LONGITUDE, locationData.getLongitude());
        cvs.put(LocationContract.LocationEntry.COLUMN_TIMESTAMP, locationData.getTimestamp());

        db.insert(LocationContract.TABLE_NAME, null, cvs);

        sendBroadcast();
    }

    /*
     * Broadcasts an intent to the activity.
     */
    private void sendBroadcast(){

        Log.d(LOG, "Broadcasting an intent signalling that no internet is available");
        mContext.sendBroadcast(new Intent(INTERNET_PROMPT_INTENT_ACTION)
                                    .addCategory(Intent.CATEGORY_DEFAULT));
    }


    /*
     * Checks to see if an active internet connection is available,
     * first by confirming the availability of the network connectivity,
     * and then sending a message to a URL.
     */
    private boolean hasInternetAccess(){
        if(isNetworkAvailable()){
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(LOG, "Error checking internet connection", e);
            }
        } else {
            Log.d(LOG, "No network available!");
        }
        return false;
    }


    /*
     * Checks the availability of a network connection.
     */
    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null;
    }


    /*
     * Closes the database
     */
    public void close(){
        mLocationDatabaseHelper.close();
    }
}
