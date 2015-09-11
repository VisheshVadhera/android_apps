package jsonweather;


/**
 * This "Plain Ol' Java Object" (POJO) class represents data related
 * to all downloaded in Json from the
 * Weather Service.
 */
public class Clouds {

	/**
	 * Tag
	 */
	public static final String all_JSON = "all";
	
	private int all;

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}
	
	
	
}
