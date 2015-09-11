package activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LifecycleLoggingActivity extends Activity {
	
	// Used for debugging purpose
	private static final String LOG_TAG = 
			LifecycleLoggingActivity.class.getName();
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState==null)
			Log.d(LOG_TAG, "onCreate being called for the first time");
		else
			Log.d(LOG_TAG, "onCreate being called again");
	}

	@Override
	public void onStart(){
		super.onStart();
		
		Log.d(LOG_TAG, "onStart is being called");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		Log.d(LOG_TAG, "onResume is being called");
	}
	
	@Override
	public void onPause(){
		super.onPause();
		
		Log.d(LOG_TAG, "onPause is being called");
	}
	
	@Override
	public void onStop(){
		super.onStop();
		
		Log.d(LOG_TAG, "onStop is being called");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		
		Log.d(LOG_TAG, "onDestroy is being called");
	}
}
