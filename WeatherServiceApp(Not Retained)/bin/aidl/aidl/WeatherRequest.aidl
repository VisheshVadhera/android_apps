package aidl;

import aidl.WeatherResults;


/**
 * Interface defining the method that receives callbacks from the
 * WeatherActivity. This method should be implemented by the
 * WeatherServiceAsync.
 */
interface WeatherRequest{

	/**
     * This one-way (non-blocking) method allows WeatherServiceAsync
     * to download the List of WeatherData results.
     */
	oneway void getCurrentWeather(String weather, WeatherResults results);

}