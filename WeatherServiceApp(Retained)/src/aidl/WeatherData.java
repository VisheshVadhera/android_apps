package aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a Plain Old Java Object (POJO) used for data
 * transport within the WeatherService app.  This POJO implements the
 * Parcelable interface to enable IPC between the WeatherActivity and
 * the WeatherServiceSync and WeatherServiceAsync. It represents the
 * response Json obtained from the Open Weather Map API, e.g., a call
 * to http://api.openweathermap.org/data/2.5/weather?q=Nashville,TN
 * might return the following Json data:
 * 
 * { "coord":{ "lon":-86.78, "lat":36.17 }, "sys":{ "message":0.0138,
 * "country":"United States of America", "sunrise":1431427373,
 * "sunset":1431477841 }, "weather":[ { "id":802, "main":"Clouds",
 * "description":"scattered clouds", "icon":"03d" } ],
 * "base":"stations", "main":{ "temp":289.847, "temp_min":289.847,
 * "temp_max":289.847, "pressure":1010.71, "sea_level":1035.76,
 * "grnd_level":1010.71, "humidity":76 }, "wind":{ "speed":2.42,
 * "deg":310.002 }, "clouds":{ "all":36 }, "dt":1431435983,
 * "id":4644585, "name":"Nashville", "cod":200 }
 *
 * The meaning of these Json fields is documented at 
 * http://openweathermap.org/weather-data#current.
 *
 */
public class WeatherData implements Parcelable {
    
	
	/**
     * These data members are the local variables that will store the
     * WeatherData's state
     */
    private String mName;
    private String mDescription;
    private double mTemp;
    private long mHumidity;
    
    
    /**
     * Constructor
     * 
     * @param name
     * @param speed
     * @param deg
     * @param temp
     * @param humidity
     * @param sunrise
     * @param sunset
     */
    public WeatherData(String name,
                       String description,
                       double temp,
                       long humidity) {
        
    	setmName(name);
        setmDescription(description);
        setmTemp(temp);
        setmHumidity(humidity);
        
    }

    
    /**
     * Provides a printable representation of this object.
     */
    @Override
    public String toString() {
        return "WeatherData [name=" + getmName() 
            + ", description=" + getmDescription()
            + ", temp=" + getmTemp()
            + ", humidity=" + getmHumidity()  + "]";
    }

    
    /*
     * BELOW THIS is related to Parcelable Interface.
     */

    /**
     * A bitmask indicating the set of special object types marshaled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    
    /**
     * Write this instance out to byte contiguous memory.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getmName());
        dest.writeDouble(getmTemp());
        dest.writeLong(getmHumidity());
        dest.writeString(getmDescription());
    }

    
    /**
     * Private constructor provided for the CREATOR interface, which
     * is used to de-marshal an WeatherData from the Parcel of data.
     * <p>
     * The order of reading in variables HAS TO MATCH the order in
     * writeToParcel(Parcel, int)
     *
     * @param in
     */
    private WeatherData(Parcel in) {
        setmName(in.readString());
        setmTemp(in.readDouble());
        setmHumidity(in.readLong());
        setmDescription(in.readString());
    }

    
    public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmDescription() {
		return mDescription;
	}

	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public double getmTemp() {
		return mTemp;
	}

	public void setmTemp(double mTemp) {
		this.mTemp = mTemp;
	}

	public long getmHumidity() {
		return mHumidity;
	}

	public void setmHumidity(long mHumidity) {
		this.mHumidity = mHumidity;
	}

	
	/**
     * public Parcelable.Creator for WeatherData, which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of your Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<WeatherData> CREATOR =
        new Parcelable.Creator<WeatherData>() {
            public WeatherData createFromParcel(Parcel in) {
                return new WeatherData(in);
            }

            public WeatherData[] newArray(int size) {
                return new WeatherData[size];
            }
        };
}
