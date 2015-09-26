package com.example.vishesh.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**-------------------------------------------------------------------------------------------------
 *
 *  Author:     Vishesh Vadhera
 *  Written:    23/9/2015
 *
 *  This class is primarily used to log the events into the logcat when activity's
 *  various lifecycle callback hook methods are called.
 * -------------------------------------------------------------------------------------------------
 */
public class LifecycleLoggingActivity extends Activity {

    private static final String LOG = LifecycleLoggingActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Log.d(LOG, "onCreate is being called");
    }

    @Override
    public void onStart(){
        super.onStart();

        Log.d(LOG, "onStart is being called");
    }

    @Override
    public void onResume(){
        super.onResume();

        Log.d(LOG, "onResume is being called");
    }

    @Override
    public void onPause(){
        super.onPause();

        Log.d(LOG, "onPause is being called");
    }

    @Override
    public void onRestart(){
        super.onRestart();

        Log.d(LOG, "onRestart is being called");
    }

    @Override
    public void onStop(){
        super.onStop();

        Log.d(LOG, "onStop is being called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Log.d(LOG, "onDestroy is being called");
    }
}
