package services;

import java.util.ArrayList;
import java.util.List;

import aidl.WeatherCall;
import aidl.WeatherData;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import utils.Utils;


/**
 * This class handles Weathers using synchronous AIDL
 * interactions.
 */
public class WeatherBoundServiceSync extends Service {
    
	
	private static final String LOG = WeatherBoundServiceSync.class.getName();
	
	
	//Callback object that resides in the WeatherBoundServiceSync and fetches the Weather synchronously.
    private WeatherCall.Stub mWeatherCallImpl = new WeatherCall.Stub() {
            

			@Override
			public List<WeatherData> getCurrentWeather(String Weather) throws RemoteException {
			
				Log.d(LOG, "Fetching the weatherData via WeatherCall object's"
						+ "getCurrentWeather method");
				
				//Obtain the WeatherData Results by calling Utils's getWeatherResults method.
				final List<WeatherData> weatherResults = Utils.getWeatherResults(Weather);
				
				if(weatherResults==null){
					
					Log.d(LOG, "Downloaded list is found to be null");
					return new ArrayList<WeatherData>();
				}
				else{
					
					Log.d(LOG, "Sending back the results through"
							+ " WeatherCall Object");
					
					//Pass the results back to the activity via the WeatherResults callback object.
					return weatherResults;
				}
			}
	};
	
	
    /**
     * Called when a component calls bindService() with the proper
     * intent.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherCallImpl;
    }
	
    
    /**
     * Make an Intent that will start this service when passed to
     * bindService().
     *
     * @param context		The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
                          WeatherBoundServiceSync.class);		
    }
}
