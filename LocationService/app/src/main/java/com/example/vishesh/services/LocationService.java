package com.example.vishesh.services;

import android.os.Process;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.vishesh.utils.LocationBroker;
import com.example.vishesh.utils.LocationData;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


/**-------------------------------------------------------------------------------------------------
 *  Author:     Vishesh Vadhera
 *  Written:    24/9/2015
 *
 *
 *  This is the LocationService class that creates the service, that does
 *  the bulk of the processing work behind the scenes. The service is created
 *  in a process, different from that of the activity so as to make it independent
 *  of MainActivity. This class connects with the 'Google Play Services API' through
 *  GoogleApi Client by implementing the ConnectionCallback interface. It listens to
 *  changes in the user's location by implementing the LocationListener interface.
 *
 *  The service is designed to run indefinitely, even after the activity has been killed,
 *  though in case the device power becomes low, the service is automatically shut down
 *  in order to save the system's resources.
 *--------------------------------------------------------------------------------------------------
 */
public class LocationService extends Service
        implements GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener{


    private static final String LOG = LocationService.class.getSimpleName();

    // Looper associated with the background thread.
    private Looper mLooper;


    // Handler associated with the background thread.
    private Handler mServiceHandler;


    // Time Interval (in milliseconds) after which the location updates should be received.
    private static final int TIME_INTERVAL = 30 * 1000;


    // Client that connects to the Google Play Services API.
    private GoogleApiClient mGoogleApiClient;


    // Object that manages the the frequency, accuracy and source of the location updates.
    private LocationRequest mLocationRequest;


    // TimeStamp at which the location update is received.
    private String mLastUpdateTime;


    // Broker object that handles the received location updates,
    // whether to send it to server or to store it in database.
    private LocationBroker mLocationBroker;


    //Broadcast Receiver that is designed to receive intents broascasted when power goes low.
    private LowPowerBroadcastReceiver mLowPowBroadRec = new LowPowerBroadcastReceiver();


    public LocationService(){

    }


    /*
     * Factory method that creates the intent necessary to start the LocationService.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context, LocationService.class);
    }


    @Override
    public void onCreate(){

        Log.d(LOG, "LocationService's onCreate is being called");

        mLowPowBroadRec.registerReceiver(this);
        mLocationBroker = new LocationBroker(getApplicationContext());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addApi(LocationServices.API)
                .build();
        mLocationRequest = new LocationRequest()
                .setInterval(TIME_INTERVAL)
                .setFastestInterval(TIME_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        HandlerThread workerThread = new HandlerThread("WorkerThread",
                                        Process.THREAD_PRIORITY_BACKGROUND);

        workerThread.start();

        mLooper = workerThread.getLooper();
        mServiceHandler = new Handler(mLooper);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(LOG, "LocationService's onStartCommand is being called");
        mGoogleApiClient.connect();

        return START_STICKY;

    }


    /*
     * Callback hook method that gets called when the service has
     * connected successfully to the 'Google Play Services API'.
     * This method registers the GoogleApiClient to receive location
     * updates periodically by calling requestLocationUpdates on it.
     */
    @Override
    public void onConnected(Bundle bundle) {

        Log.d(LOG, "LocationService's onConnected method is being called, starting location updates");

        //Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        //if(loc==null){
        LocationServices.FusedLocationApi
             .requestLocationUpdates(mGoogleApiClient,
                     mLocationRequest,
                     this);
    }


    /**
     * Callback hook method that gets called when a new location update is received.
     * This method gets the current timestamp and passes it alongwith location, to the
     * LocationBroker object to handle it appropriately.
     *
     * @param location Current location.
     */
    @Override
    public void onLocationChanged(Location location) {

        Log.d(LOG, "LocationService's onLocationChanged is being called");
        Log.d(LOG, "Latitude is: " + location.getLatitude() + " and Longitude is: " + location.getLongitude());

        final Location currentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        mServiceHandler.post(new Runnable(){

            @Override
            public void run() {
                mLocationBroker.handleLocation
                        (new LocationData(currentLocation.getLatitude(),
                                          currentLocation.getLongitude(),
                                          mLastUpdateTime));
            }
        });
    }


    /**
     * Inner class that defines the blueprint of a Broadcast Receiver
     * designed to receive the intent broadcasted when device power goes low.
     */
    private class LowPowerBroadcastReceiver extends BroadcastReceiver{


        /*
         * Stop the service after receiving the intent.
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(LOG, "Receiving Intent signalling low poer, hence shutting down the LocationService");
            stopSelf();
        }


        /*
         * Register the Broadcast Receiver.
         */
        private void registerReceiver(Context context){

            IntentFilter lowPowerIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
            lowPowerIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

            context.registerReceiver(this, lowPowerIntentFilter);
        }


        /*
         * Unregister the Broadcast Receiver.
         */
        private void unRegisterReceiver(Context context){
            context.unregisterReceiver(this);
        }
    }


    /**
     * Callback method that gets called when the service is about to be destroyed.
     * It unregisters the Low Power Broadcast Reciever.
     */
    @Override
    public void onDestroy(){

        super.onDestroy();

        Log.d(LOG, "LocationService's onDestroy is being called");

        Log.d(LOG, "Unregistering the LowPow Broadcast Receiver");
        mLowPowBroadRec.unRegisterReceiver(this);

        Log.d(LOG, "Stopping Location Updates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

        mLocationBroker.close();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
