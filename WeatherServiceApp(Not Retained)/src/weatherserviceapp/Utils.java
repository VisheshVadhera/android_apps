package weatherserviceapp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import aidl.WeatherData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import jsonweather.JsonWeather;
import jsonweather.WeatherJSONParser;

/**
 * Helper class which contains helper methods
 * 
 * @author Vishesh
 */
public class Utils {

	
	//Used to log messages to the logcat
	private static final String LOG = Utils.class.getName(); 
	
	
	//Base URL which connects to the Open Weather Map API.
	private static final String baseURL = "http://api.openweathermap.org/data/2.5/weather?q=";
	
	
	/**
	 * Gets the list of weatherData objects from the Open Weather Map API.
	 * @param location
	 * @return
	 */
	public static List<WeatherData> getWeatherResults(String location){
		
		
		final List<WeatherData> weatherResults = new ArrayList<WeatherData>();
		List<JsonWeather> mJsonWeather = null;
		URL weatherURL = null;
		
		try {
			
			//URL to which the GET Request would be sent
			weatherURL = new URL(baseURL + URLEncoder.encode(location, "UTF-8"));
			
			
			//Establish a HTTP Connection to the weatherURL
			HttpURLConnection weatherURLConnection = (HttpURLConnection) weatherURL.openConnection();
			
			
			try(InputStream inputStream = new BufferedInputStream(weatherURLConnection.getInputStream())){
			
				
				//Create a JSON Parse object that parses the incoming inputstream 
				final WeatherJSONParser jsonParser = new WeatherJSONParser();
			
				
				//Create a JsonWeather JAVA Object from the inputstream
				mJsonWeather = jsonParser.parseJsonStream(inputStream);
			}
			finally{
				
				weatherURLConnection.disconnect();
			}
					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		Log.d(LOG, "Is the JsonWeather null? " + mJsonWeather.equals(null));
		Log.d(LOG, "Size of mJsonWeather is " + mJsonWeather.size());
		
		
		if(mJsonWeather!=null && mJsonWeather.size()>0){
			
			JsonWeather jsonWeather = mJsonWeather.get(0);
			
			//Create a WeatherData object from the JsonWeatherObject
			weatherResults.add(new WeatherData(jsonWeather.getName(),
											   jsonWeather.getWeather().get(0).getDescription(),
											   jsonWeather.getMain().getTemp(),
											   jsonWeather.getMain().getHumidity()));
			
			return weatherResults;
		}
		else{
			return null;
		}	
	}
	
	
	/**
	 * Display a toast to the user
	 * @param context The Activity context in which the toast is displayed
	 * @param message The message to be displayed
	 */
	public static void showToast(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
}
