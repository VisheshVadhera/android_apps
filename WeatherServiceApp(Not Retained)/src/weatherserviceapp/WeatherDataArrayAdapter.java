package weatherserviceapp;

import aidl.WeatherData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.vishesh.weatherserviceappnotretained.R;


public class WeatherDataArrayAdapter extends ArrayAdapter<WeatherData> {
	

	public WeatherDataArrayAdapter(Context context){
		super(context, R.layout.weather_data_row);
	}
	

	public WeatherDataArrayAdapter(Context context, List<WeatherData> weatherDataObjects){
		super(context, R.layout.weather_data_row, weatherDataObjects);
	}
	
	
	/**
	 * @param position 		The position in the List of WeatherData which has to be converted into a View.
	 * 		  convertView 	The View whose subViews have to be given specific values.	
	 * 		  parent 		Parent View of the convertView.
	 * 
	 * return The view associated with item at position.
	 */
	public View getView(int position, View convertView, ViewGroup parent){
		
		WeatherData data = getItem(position);
		
		if(convertView==null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.weather_data_row, parent, false);
		}
		
		TextView nameTV = (TextView) convertView.findViewById(R.id.tv_name);
		TextView descripTV = (TextView) convertView.findViewById(R.id.tv_description);
		TextView tempTV = (TextView) convertView.findViewById(R.id.tv_temp);
		TextView humidityTV = (TextView) convertView.findViewById(R.id.tv_humidity);
		
		
		
		nameTV.setText("Name: " + data.getmName());
		descripTV.setText("Status: " + data.getmDescription());
		tempTV.setText("Temp(C): " + tempInC(data.getmTemp()));
		humidityTV.setText("Humidity(%): " + data.getmHumidity());
		
		return convertView;
	}
	
	
	private double tempInC(double tempInK){
		return tempInK - 273.15;
	}

}
