package operations;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import activities.WeatherActivity;
import aidl.WeatherCall;
import aidl.WeatherData;
import aidl.WeatherRequest;
import aidl.WeatherResults;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import services.WeatherBoundServiceAsync;
import services.WeatherBoundServiceSync;
import utils.Utils;

public class WeatherOps {
	
	
	// Used for debugging purpose.
    private static final String LOG = WeatherOps.class.getName(); 
    
    
    //Handler associated with the UI Thread that posts runnable to the UI Thread
    private final Handler displayHandler = new Handler();
    
    
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
     * the WeatherBoundServiceSync Service using bindService().
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
      
     
    /**
     * This ServiceConnection establishes an IPC channel between activity
     * and service and is used to receive results after binding
     * to the WeatherServiceAsync Service using bindService()  
     */
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

      
    /**
     * Object that resides in the Activity and will be passed to
     * the WeatherServiceAsync to send results back to the activity
     */
    private WeatherResults.Stub mWeatherResults = new WeatherResults.Stub() {
    					
    			@Override
    			public void sendResults(final List<WeatherData> results) 
    					throws RemoteException {

    				Log.d(LOG, "Displaying the weatherResults on UI Thread"
    						+ " via WeatherResults object's sendResults method");
    				
    				displayHandler.post(new Runnable(){

    					@Override
    					public void run() {
    							mResults = results;
    							mActivity.get().displayResults(results);
    					}
    				});	
    			}
    };

    private WeakReference<WeatherActivity> mActivity;
    
	private List<WeatherData> mResults;
    
	
    public WeatherOps(WeatherActivity activity){
    	
    	mActivity = new WeakReference<WeatherActivity>(activity); 
    }
    
    
    /**
     * Reinitializes the mActivity in case of activity reconfiguration changes.
     * @param activity
     */
    public void onConfigurationChange(WeatherActivity activity){
    		
    	Log.d(LOG, "onConfigurationChange is being called");
    		
    	mActivity = new WeakReference<WeatherActivity>(activity);
    		
    	updateDisplay();
    }
    
    
    /**
     * Bind the activity to the services
     */
    public void bindService(){
    	
    	if(mWeatherCall == null){
    		Log.d(LOG, "Binding to WeatherSync service");
            
    		mActivity.get().getApplicationContext()
            .bindService(WeatherBoundServiceSync.makeIntent
            			(mActivity.get().getApplicationContext()), 
                        mServiceConnectionSync, 
                        Context.BIND_AUTO_CREATE);
    	}
    		
    	if (mWeatherRequest == null){
    		Log.d(LOG, "Binding to WeatherSync service");
            
    		mActivity.get().getApplicationContext()
            .bindService(WeatherBoundServiceAsync.makeIntent
            			(mActivity.get().getApplicationContext()), 
                        mServiceConnectionAsync, 
                        Context.BIND_AUTO_CREATE);
    	}
    }
    
    
    /**
     * Callback method that gets called from the activity when 
     * the user presses the 'Get Weather Via Sync Service' button
     * @param location location whose weather has to fetched
     */
    public void getWeatherSync(String location){
        	
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
    						
    					mActivity.get().displayResults(weatherData);
    			}
    		}.execute(location);
    			
    	}
        else{
        	Utils.showToast(mActivity.get(), "WeatherCall Object is null");
    		Log.d(LOG, "WeatherCall Object is null");
        }
    }

        
    /**
     * Callback method that gets called from the activity when  
     * the user presses the 'Get Weather Via Async Service' button 
     * @param location location whose weather has to fetched
     */
    public void getWeatherAsync(String location){
        	
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
    		Utils.showToast(mActivity.get(), "WeatherRequest is null");
    		Log.d(LOG, "WeatherRequest Object is null");
    	}
    		
    }
     
    
    /**
     * Callback method to unbind the service
     */
    public void unBindService(){
    	
    	// Unbind the Sync/Async Services if they are bound.
    	
    	if (mWeatherCall != null){
    		Log.d(LOG, "Unbinding the Sync Service");
            
    		mActivity.get().getApplicationContext()
            	.unbindService(mServiceConnectionSync);
    	}
    	
    	if (mWeatherRequest != null) 
    	{
    		Log.d(LOG, "Unbinding the Async Service");
            
    		mActivity.get().getApplicationContext()
            	.unbindService(mServiceConnectionAsync);
    	}
    }
    
    
    private void updateDisplay(){
 		mActivity.get().displayResults(mResults);
 	}
}
