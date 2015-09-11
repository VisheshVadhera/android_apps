package weatherserviceapp;

import java.util.ArrayList;
import java.util.List;

import com.vishesh.weatherserviceappnotretained.R;

import aidl.WeatherCall;
import aidl.WeatherData;
import aidl.WeatherRequest;
import aidl.WeatherResults;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;


public class WeatherActivity extends LifecycleLoggingActivity {
    
	
    // Used for debugging purpose.
    private final String LOG = this.getClass().getSimpleName(); 
    
    
    //Handler associated with the UI Thread that posts runnable to the UI Thread
    private final Handler displayHandler = new Handler();
    
    
    private EditText mEditText;
    
    
    //Object that maps the WeatherData object's fields to the views that display those fields
    private WeatherDataArrayAdapter mAdapter;
    
    
    private ListView mListView;
    
    
    /**
     * The AIDL Interface that's used to make twoway calls to the
     * WeatherBoundServiceSync Service. If it's null then there's no
     * connection to the Service.
     */
    private WeatherCall mWeatherCall;
    
    
    /**
     * The AIDL Interface that we will use to make oneway calls to the
     * WeatherBoundServiceAsync Service. If it's null then there's no 
     * connection to the Service.
     */
    private WeatherRequest mWeatherRequest;
     
    
    /** 
     * This ServiceConnection establishes an IPC channel between activity 
     * and service and is used to receive results after binding to
     * the WeatherServiceSync Service using bindService().
     */
    private ServiceConnection mServiceConnectionSync = new ServiceConnection() {
            
    	
    		/**
             * Cast the returned IBinder object to the WeatherCall
             * AIDL Interface
             */
            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
            	
                mWeatherCall = WeatherCall.Stub.asInterface(service);
            }

            
            /**
             * Called if the remote service crashes and is no longer
             * available.  The ServiceConnection will remain bound,
             * but the service will not respond to any requests
             */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mWeatherCall = null;
            }
    	 
        };
     
        
     
    //This ServiceConnection establishes an IPC channel between activity 
    //and service and is used to receive results after binding
    //to the WeatherServiceAsync Service using bindService() 
    private ServiceConnection mServiceConnectionAsync = new ServiceConnection() {
    	
    	
            /**
             * Cast the returned IBinder object to the WeatherRequest
             * AIDL Interface
             */
            @Override
		public void onServiceConnected(ComponentName name,
                                               IBinder service) {
                mWeatherRequest = WeatherRequest.Stub.asInterface(service);
            }

            /**
             * Called if the remote service crashes and is no longer
             * available.  The ServiceConnection will remain bound,
             * but the service will not respond to any requests.
             */
            @Override
		public void onServiceDisconnected(ComponentName name) {
                mWeatherRequest = null;
            }
        };
    
        
    //Object that resides in the Activity and will be passed to
    //the WeatherServiceAsync to send results back to the activity
    private WeatherResults.Stub mWeatherResults = new WeatherResults.Stub() {
					
			@Override
			public void sendResults(final List<WeatherData> results) 
					throws RemoteException {

				Log.d(LOG, "Displaying the weatherResults on UI Thread"
						+ " via WeatherResults object's sendResults method");
				
				displayHandler.post(new Runnable(){

					@Override
					public void run() {
							displayResults(results);
					}
				});	
			
		}

     };
        
        
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.activity_weather);
    	mEditText = (EditText) findViewById(R.id.editText);
    	mListView = (ListView) findViewById(R.id.listView1);
		mAdapter = new WeatherDataArrayAdapter(this);
		mListView.setAdapter(mAdapter);
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
        	getWeatherSync(location);
        	break;
        case R.id.button_async:
            getWeatherAsync(location);
            break;
        }
    }
    
    
    /**
     * Callback method that gets called when the user presses the
     * 'Get Weather Via Sync SErvice' button
     * @param location location whose weather has to fetched
     */
    private void getWeatherSync(String location){
    	
    	
    	if (mWeatherCall != null) {
    		
    		
    		//Since its a synchronous call, in order to not block the Main UI Thread,
			//a new AsyncTask is spawned that obtains the WeatherData in a background
			//thread and displays the results to the UI Thread in onPostExecute method.
			new AsyncTask<String, Void, List<WeatherData>>(){

				@Override
				protected List<WeatherData> doInBackground(String... params) {
					
					List<WeatherData> results = new ArrayList<WeatherData>();
					
					try {
						
						Log.d(LOG, "Fetching WeatherData via Sync service in a background thread");
						results =  mWeatherCall.getCurrentWeather(params[0]);
					} 
					catch (RemoteException e) {
						e.printStackTrace();
						return null;
					}
					return results;
				}
				
				@Override 
				protected void onPostExecute(List<WeatherData> weatherData){
						
						Log.d(LOG, "Displaying the WeatherData returned via Sync service in the UI Thread");
						
						displayResults(weatherData);
				}
			}.execute(location);
			
		}
    	else{
    		Utils.showToast(this, "WeatherCall Object is null");
			Log.d(LOG, "WeatherCall Object is null");
    	}
    }

    
    /**
     * Callback method that gets called when the user presses the
     * 'Get Weather Via Async SErvice' button
     * @param location location whose weather has to fetched
     */
    private void getWeatherAsync(String location){
    	
    	if (mWeatherRequest != null) {
        	try {
				
				Log.d(LOG, "Fetching WeatherData via WeatherAsync service");
				
				mWeatherRequest.getCurrentWeather(location, mWeatherResults);
			} 
			catch (RemoteException e) {
				
				e.printStackTrace();
			}
		}
		else{
			Utils.showToast(this, "WeatherRequest is null");
			Log.d(LOG, "WeatherRequest Object is null");
		}
		
    }
    
    
    /**
     * Hook method called when the WeatherActivity becomes visible to
     * bind the Activity to the Services.
     */
    @Override
    public void onStart () {
    	super.onStart();
    	
    	// Bind this activity to the WeatherBoundServices if
    	// they aren't already bound 
    	if(mWeatherCall == null){
    		Log.d(LOG, "Binding to WeatherSync service");
            bindService(WeatherBoundServiceSync.makeIntent(this), 
                        mServiceConnectionSync, 
                        BIND_AUTO_CREATE);
    	}
    		
    	if (mWeatherRequest == null){
    		Log.d(LOG, "Binding to WeatherSync service");
            bindService(WeatherBoundServiceAsync.makeIntent(this), 
                        mServiceConnectionAsync, 
                        BIND_AUTO_CREATE);
    	}
    }
    
    
    /**
     * Hook method called when the WeatherActivity becomes completely
     * hidden to unbind the Activity from the Services.
     */
    @Override
    public void onStop () {
    	super.onStop();
    	
    	// Unbind the Sync/Async Services if they are bound.
    	if (mWeatherCall != null){
    		Log.d(LOG, "Unbinding the Sync Service");
            unbindService(mServiceConnectionSync);
    	}
    	if (mWeatherRequest != null) 
    	{
    		Log.d(LOG, "Unbinding the Async Service");
            unbindService(mServiceConnectionAsync);
    	}
    }
   
    
    /**
	 * Displays the results obtained via the Services in the Activity.
	 * @param results
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
	 * @return
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
