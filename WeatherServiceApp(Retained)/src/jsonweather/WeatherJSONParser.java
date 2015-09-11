package jsonweather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;
import android.util.JsonToken;
import jsonweather.JsonWeather;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of JsonWeather objects that contain this data.
 */
public class WeatherJSONParser {
    
	
	/**
     * Parse the inputStream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonStream(InputStream inputStream)
        throws IOException {
    	
    	//JsonReader that reads the input stream.
        JsonReader weatherReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
    	try{
    		
    		if(weatherReader.peek()==JsonToken.BEGIN_ARRAY){
    			//Get the list of JsonWeather objects from the Json Stream.
                return parseJsonWeatherArray(weatherReader);
    		}
    		
    		else{
    			
    			List<JsonWeather> listWeather = new ArrayList<JsonWeather>();
    			
    			listWeather.add(parseJsonWeather(weatherReader));
    			
    			return listWeather;
    		}
    	}
    	finally{
    		weatherReader.close();
    	}
    }

    
    /**
     * Parse a Json stream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonWeatherArray(JsonReader reader)
        throws IOException {
    	
    	List<JsonWeather> weatherData = new ArrayList<JsonWeather>();
    	
        reader.beginArray();
        
        while(reader.hasNext()){
        	weatherData.add(parseJsonWeather(reader));
        }
        reader.endArray();
        return weatherData;
    }

    
    /**
     * Parse a Json stream and return a JsonWeather object.
     */
    public JsonWeather parseJsonWeather(JsonReader reader) 
        throws IOException {

        JsonWeather jsonWeather = new JsonWeather();
              	
        reader.beginObject();
        while(reader.hasNext()){
        	
        	String nextName = reader.nextName();
        	
            if(nextName.equals(JsonWeather.coord_JSON)){
            	jsonWeather.setCoord(parseCoord(reader));
            }
            
           	else if(nextName.equals(JsonWeather.weather_JSON)){
           		jsonWeather.setWeather(parseWeathers(reader));
           	}
            
           	else if(nextName.equals(JsonWeather.base_JSON)){
           		jsonWeather.setBase(reader.nextString());
           	}
            
           	else if(nextName.equals(JsonWeather.main_JSON)){
           		jsonWeather.setMain(parseMain(reader));
           	}
    		
           	else if(nextName.equals(JsonWeather.wind_JSON)){
           		jsonWeather.setWind(parseWind(reader));
           	}
            
           	else if(nextName.equals(JsonWeather.clouds_JSON)){
           		jsonWeather.setClouds(parseClouds(reader));
           	}
            
           	else if(nextName.equals(JsonWeather.dt_JSON)){
           		jsonWeather.setDt(reader.nextLong());
           	}
            
            else if(nextName.equals(JsonWeather.sys_JSON)){
           		jsonWeather.setSys(parseSys(reader));
           	}
            	
           	else if(nextName.equals(JsonWeather.id_JSON)){
           		jsonWeather.setId(reader.nextLong());
           	}
            	
           	else if(nextName.equals(JsonWeather.name_JSON)){
           		jsonWeather.setName(reader.nextString());
           	}
            	
           	else if(nextName.equals(JsonWeather.cod_JSON)){
           		jsonWeather.setCod(reader.nextLong());
           	}	
           	else{
           		reader.skipValue();
           	}
        }
        
        reader.endObject();
        return jsonWeather;
    }
    
    /**
     * Parse a Json stream and return a Cloud object.
     */
    private Clouds parseClouds(JsonReader reader) throws IOException {
		Clouds clouds = new Clouds();
		
		reader.beginObject();
		while(reader.hasNext()){
			String nextName  = reader.nextName();
			
			if(nextName.equals(JsonWeather.clouds_JSON)){
				clouds.setAll(reader.nextInt());
			}
			else{
           		reader.skipValue();
           	}
		}
		
		reader.endObject();
		return clouds;
	}


	/**
     * Parse a Json stream and return a List of Weather objects.
     */
    public List<Weather> parseWeathers(JsonReader reader) throws IOException {
    	
    	
    	List<Weather> listWeather = new ArrayList<Weather>();
    	
    	reader.beginArray();
    	while(reader.hasNext()){
    		listWeather.add(parseWeather(reader));
    	}
    	
    	reader.endArray();
    	return listWeather;
    }

    
    /**
     * Parse a Json stream and return a Weather object.
     */
    public Weather parseWeather(JsonReader reader) throws IOException {
        
    	Weather weather = new Weather();
    	reader.beginObject();
    	
    	while(reader.hasNext()){
    		
    		String nextName = reader.nextName();
    	
    		if(nextName.equals(Weather.id_JSON)){
    			weather.setId(reader.nextLong());
    		}
    		else if(nextName.equals(Weather.main_JSON)){
    			weather.setMain(reader.nextString());
    		}
    		else if(nextName.equals(Weather.description_JSON)){
    			weather.setDescription(reader.nextString());
    		}
    		else if(nextName.equals(Weather.icon_JSON)){
    			weather.setIcon(reader.nextString());
    		}
    		else{
    			reader.skipValue();
    		}
    	}
    	reader.endObject();
    	return weather;
    }
    
    
    /**
     * Parse a Json stream and return a Main Object.
     */
    public Main parseMain(JsonReader reader) 
        throws IOException {
        
    	Main main = new Main();
    	
    	reader.beginObject();
    	
    	while(reader.hasNext()){
    		
    		String nextName = reader.nextName();
    		
    		if(nextName.equals(Main.temp_JSON)){
    			main.setTemp(reader.nextDouble());
    		}
    		else if(nextName.equals(Main.tempMin_JSON)){
    			main.setTempMin(reader.nextDouble());
    		}
    		else if(nextName.equals(Main.tempMax_JSON)){
    			main.setTempMax(reader.nextDouble());
    		}
    		else if(nextName.equals(Main.pressure_JSON)){
    			main.setPressure(reader.nextDouble());
    		}
    		else if(nextName.equals(Main.seaLevel_JSON)){
    			reader.skipValue();
    		}
    		else if(nextName.equals(Main.grndLevel_JSON)){
    			reader.skipValue();
    		}
    		else if(nextName.equals(Main.humidity_JSON)){
    			main.setHumidity(reader.nextLong());
    		}
    		else{
           		reader.skipValue();
           	}
    	}
    	
    	reader.endObject();
    	return main; 
    }

    
    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader) throws IOException {
        
    	Wind wind = new Wind();
    	
    	reader.beginObject();
    	while(reader.hasNext()){
    		String nextName = reader.nextName();
    		
    		if(nextName.equals(Wind.deg_JSON)){
    			wind.setDeg(reader.nextDouble());
    		}
    		else if(reader.equals(Wind.speed_JSON)){
    			wind.setSpeed(reader.nextDouble());
    		}
    		else{
           		reader.skipValue();
           	}
    	}
    	
    	reader.endObject();
    	return wind;
    }

    
    /**
     * Parse a Json stream and return a Sys Object.
     * @throws IOException 
     */
    public Sys parseSys(JsonReader reader) throws IOException{
    	
    	Sys sys = new Sys();
    	
    	reader.beginObject();
    	while(reader.hasNext()){
    		String nextName = reader.nextName();
    		if(nextName.equals(Sys.message_JSON)){
    			sys.setMessage(reader.nextDouble());
    		}
    		else if(nextName.equals(Sys.country_JSON)){
    			sys.setCountry(reader.nextString());
    		}
    		else if(nextName.equals(Sys.sunrise_JSON)){
    			sys.setSunrise(reader.nextLong());
    		}
    		else if(nextName.equals(Sys.sunset_JSON)){
    			sys.setSunset(reader.nextLong());
    		}
    		else{
    			reader.skipValue();
    		}
    			
    	}
    	reader.endObject();
    	return sys;
    }
    
    
    /**
     * Parse a Json stream and return a Coord object.
     */
    public Coord parseCoord(JsonReader reader) throws IOException{
    	
    	Coord coord = new Coord();
    	
    	reader.beginObject();
    	
    	while(reader.hasNext()){
    		String nextName = reader.nextName();
    		
    		if(nextName.equals(Coord.lat_JSON)){
    			coord.setLatitude(reader.nextDouble());
    		}
    		else if(nextName.equals(Coord.lon_JSON)){
    			coord.setLongitude(reader.nextDouble());
    		}
    		else{
           		reader.skipValue();
           	}
    	}
    	
    	reader.endObject();
    	return coord;
    }
}
