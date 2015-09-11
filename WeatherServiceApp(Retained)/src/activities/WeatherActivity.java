package activities;

import java.util.ArrayList;
import java.util.List;

import com.vishesh.weatherserviceappretained.R;

import aidl.WeatherData;
import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import operations.WeatherOps;
import utils.RetainedFragmentManager;
import utils.Utils;
import utils.WeatherDataArrayAdapter;


public class WeatherActivity extends LifecycleLoggingActivity {
	
	
	//String used for posting messages to the logcat.
	private static final String LOG = WeatherActivity.class.getName();
		
		
	//Fragment Tag that will be used to store 
	//RetainedFragment in the RetainedFragmentManager. 
	private static final String FRAGMENT_TAG = 
					"com.example.weatherserviceapp.mainactivity.fragmentTag"; 
		
		
	//Key for storing mWeatherOps object during activity reconfiguration.
	private static final String OPS_STATE = 
					"com.example.weatherserviceapp.mainactivity.opstate";
		
		
	//RetainedFragmentManager that will manage the RetainedFragment.
	private final RetainedFragmentManager mFragmentManager = 
			new RetainedFragmentManager(getFragmentManager(), FRAGMENT_TAG);
		
		
	//WeatherOps object that does the processing work behind the scenes.
	private WeatherOps mWeatherOps;
    
    
    //Object that maps the WeatherData object's fields to the views that display those fields
    private WeatherDataArrayAdapter mAdapter;
      
    private ListView mListView; 
    
    private EditText mEditText;
   
    
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_weather);
    	
    	mEditText = (EditText) findViewById(R.id.editText);
    	mListView = (ListView) findViewById(R.id.listView1);
		mAdapter = new WeatherDataArrayAdapter(this);
		mListView.setAdapter(mAdapter);
    
		handleConfigurationChange();
    }
    
    
    /**
	 * This method handles the runtime configuration changes
	 */
	private void handleConfigurationChange(){
		
		
		//If it is the first time that it is being called then create a WeatherOps object
		if(mFragmentManager.isItFirstTime()){
			
			Log.d(LOG, "Creating a new WeatherOps Object");
			
			mWeatherOps = new WeatherOps(this);
			mFragmentManager.put(OPS_STATE, mWeatherOps);	
			
			mWeatherOps.bindService();
		}
		else{
			
			Log.d(LOG, "Retrieving the WeatherOps object back");
			
			mWeatherOps = mFragmentManager.get(OPS_STATE);
			
			//If due to some inexplicable reason, WetherOps is still
			//null, create it from the scratch.
			if(mWeatherOps==null){
				
				mWeatherOps = new WeatherOps(this);
				mFragmentManager.put(OPS_STATE, mWeatherOps);
				
				mWeatherOps.bindService();
			}
			
			else{
				mWeatherOps.onConfigurationChange(this);
			}
		}
	}
     
    
    /**
     * Gets called when the user presses either of the two buttons:
     * GetWeatherSync or GetWeatherAsync
     */
    public void runService(View view) {
    	
    	String location = getLocation();
    	hideKeyboard();

    	switch (view.getId()) {
        case R.id.button_sync:
        	mWeatherOps.getWeatherSync(location);
        	break;
        case R.id.button_async:
            mWeatherOps.getWeatherAsync(location);
            break;
        }
    }
        
    
    /**
     * Hook method called when the WeatherActivity becomes visible 
     */
    @Override
    public void onStart () {
    	super.onStart();
    	
    	//Bind the Activity to the Services.
    	mWeatherOps.bindService();
    }
    
    
    /**
     * Hook method called when the WeatherActivity becomes completely hidden
     */
    @Override
    public void onStop () {
    	super.onStop();
    	
    	if(isChangingConfigurations()){
    		Log.d(LOG, "No need to unbind the service as a runtime "
    				+ "reconfiguration change is happening");
    		
    	}
    	else{
    		
    		//Unbind the Activity from the Services.
        	mWeatherOps.unBindService();
    		
    	}
    }
   
    
    /**
	 * Displays the results obtained via the Services in the Activity.
	 */
	public void displayResults(List<WeatherData> results){
		
		if(results==null || results.size()==0){
			Utils.showToast(this, "No Weather found!");
		}
		else{
			mAdapter.clear();
			mAdapter.addAll(results);
			mAdapter.notifyDataSetChanged();
		}
	}
	
	
	/**
	 * Gets the location entered by the user in the EditText box. 
	 */
	private String getLocation(){
		return mEditText.getText().toString();
	}
	
	
	/**
	 * Hides the keyboard
	 */
	private void hideKeyboard(){
		
		View view = this.getCurrentFocus();
		
		if(view!=null){
			
			InputMethodManager inputManager = (InputMethodManager)
							 this.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow
							(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
    
}
