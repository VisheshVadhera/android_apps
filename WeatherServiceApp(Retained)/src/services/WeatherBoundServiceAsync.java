package services;

import java.util.ArrayList;
import java.util.List;
import aidl.WeatherData;
import aidl.WeatherRequest;
import aidl.WeatherResults;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import utils.Utils;

/**
 *  This class handles Weathers using asynchronous AIDL
 *  interactions  
 **/
public class WeatherBoundServiceAsync extends Service{
	
	
	private static final String LOG = WeatherBoundServiceAsync.class.getName();

	
	//Object that resides in the WeatherBoundServiceAsync and fetches the Weather asynchronously.
    private WeatherRequest.Stub mWeatherRequestImpl = new WeatherRequest.Stub() {
            

    	@Override
		public void getCurrentWeather(String Weather, 
									  WeatherResults results) 
									  throws RemoteException {
			
			Log.d(LOG, "Fetching the WeatherData using WeatherRequest Object's"
					+ "getCurrentWeather method");
			
			
			//Obtain the WeatherData Results by calling Utils's getWeatherResults method.
			final List<WeatherData> weatherResults = Utils.getWeatherResults(Weather);
			
			if(weatherResults==null){
				
				Log.d(LOG, "Downloaded list is found to be null");
				results.sendResults(new ArrayList<WeatherData>());
			}
			else{
				
				Log.d(LOG, "Sending back the results by calling sendResults method"
						+ "on WeatherResults object");
				
				//Pass the results back to the activity via the WeatherResults callback object.
				results.sendResults(weatherResults);
			}
			
		}
		
	};
	
	
    /**
     * Called when a component calls bindService() with the proper
     * intent.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherRequestImpl;
    }

    
    /**
     * Make an Intent that will start this service when passed to
     * bindService().
     *
     * @param context The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        
        return new Intent(context, WeatherBoundServiceAsync.class);		
    }
}
