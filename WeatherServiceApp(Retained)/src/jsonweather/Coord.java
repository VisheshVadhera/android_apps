package jsonweather;


/**
 * This "Plain Ol' Java Object" (POJO) class represents data related
 * to latitude and longitude downloaded in Json from the
 * Weather Service.
 */
public class Coord {
	
	
	/**
     * Various tags corresponding to data downloaded in Json from the
     * Weather Service.
     */
	public static final String lon_JSON = "lon";
	public static final String lat_JSON = "lat";
	
	 /**
     * Various fields corresponding to data downloaded in Json from
     * the Weather Service.
     */
	private double longitude;
	private double latitude;

	
	public double getLatitude() {
		return latitude;
	}

	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	
	public double getLongitude() {
		return longitude;
	}

	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
