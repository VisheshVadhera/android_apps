package com.example.vishesh.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.vishesh.locationserviceapp.R;
import com.example.vishesh.services.LocationService;
import com.example.vishesh.utils.InternetPromptDialog;
import com.example.vishesh.utils.LocationBroker;

/**-------------------------------------------------------------------------------------------------
 *  Author:     Vishesh Vadhera
 *  Written:    23/9/2015
 *
 *
 * This is the MainActivity class which contains the most of the user facing operations.
 * The class has two major operations:
 *
 * 1. Start a LocationService when the user presses the 'Send Location' Button.
 *
 * 2. Receive the intent broadcasted by the LocationService when it detects that there is no
 *    internet connectivity available. On Receiving the intent, activity displays the dialog
 *    asking the user if he wants to change the network connection settings.
 *
 *--------------------------------------------------------------------------------------------------
 */
public class MainActivity extends LifecycleLoggingActivity {


    private static final String LOG = MainActivity.class.getSimpleName();


    // Broadcast Receiver that receives intent to alert the user to turn on network access.
    private InternetBroadcastReceiver mBroadcastReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBroadcastReceiver = new InternetBroadcastReceiver();
    }


    /**
     * This method registers the Broadcast Receiver when
     * the activity becomes visible to the user.
     */
    @Override
    public void onResume(){
        super.onResume();

        mBroadcastReceiver.registerReceiver(this);
    }


    /**
     * Gets called back back when the user presses the 'Send Location' button.
     *
     * @param v View which is being clicked (Button).
     */
    public void sendLocation(View v){

        Log.d(LOG, "Starting the LocationService");
        startService(LocationService.makeIntent(getApplicationContext()));

    }


    /**
     * This method unregisters the Broadcast Receiver when
     * the activity becomes partially visible.
     */
    @Override
    public void onPause(){

        super.onPause();
        mBroadcastReceiver.unRegisterReceiver(this);
    }


    /**
     * Inner class that defines the blueprint for the Broadcast Receiver
     * that will receive the broadcasted intent from the LocationService.
     */
    private class InternetBroadcastReceiver extends BroadcastReceiver{

        public InternetBroadcastReceiver(){

        }

        /*
         * Callback hook method that gets called when the Receiver receives
         * the intent from the LocationService. This method displays the
         * alert dialog to the user.
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(LOG, "Intent signalling 'No Internet Connection' received in Activity");
            showInternetPromptDialog();
        }


        /*
         * Registers the Broadcast Receiver to receive intents with
         * 'INTERNET_PROMPT_INTENT_ACTION' action.
         */
        private void registerReceiver(Context context){

            IntentFilter serviceIntentFilter = new IntentFilter(LocationBroker.INTERNET_PROMPT_INTENT_ACTION);
            serviceIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

            context.registerReceiver(this, serviceIntentFilter);
        }


        /*
         * Unregisters the Broadcast Receiver.
         */
        private void unRegisterReceiver(Context context){
            context.unregisterReceiver(this);
        }
    }


    /**
     * Displays the alert dialog.
     */
    private void showInternetPromptDialog(){

        Log.d(LOG, "Showing Alert Dialog to the user");
        InternetPromptDialog internetPromptDialog = new InternetPromptDialog();
        internetPromptDialog.show(getFragmentManager(), "Dialog");
    }

}

