'WeatherServiceApp(Not Retained)' is designed to fetch weather data from openweathermap.org's weather API.
The app's source code is contained in three different packages:

1. AIDL: Contains four files with .aidl extensions in which interfaces for 3 different java objects are defined. Also contains 
		 a WeatherData.java file which contains a blueprint for a Plain Old Java Object that contstructs a WeatherData POJO from
		 a JSON Object which gets returned by the openWeatherMap API.
		 
		 
2. JsonWeather: Contains classes to construct a JsonWeather JAVA object out of the JSON object returned by the API. The JSON object 
				returned by the API has the a number of key value pairs, plus the following JSON objects contained in it and hence 
				the different classes defined in this package correspond to different nested JSON Objects:
				
				i) Coord
				ii) Clouds
				iii) Main 
				iv) Sys
				v) Weather
				vi) Wind
				
				The package has another class called 'WeatherJsonParser' which contains the code needed to make a JSON Parser.
				

3. WeatherServiceApp: This package contains the code that is thw work horse of the project. It contains the following classes:

				i) LifecycleLoggingActivity: This class is a helper class whose main function is to log the various lifecycle
											 callback hook methods of the activity that displays the weather data.
											 
				ii) WeatherActivity: This class extends the LifecycleLoggingctivity class and does the major work to establish 
									 the User facing components
									 
				iii) WeatherBoundServiceAsync: Service class that handles the download of the weatherData asynchronously.
				
				iv) WeatherBoundServiceSync: Service class that handles the download of the weatherData synchronously.
				
				v) WeatherDataArrayAdapter: Extends the ArrayAdapter class and is responsible for mapping the fields of WeatherData
											object to the views that display them to the user
											
				vi) Utils: Helper class that contains different helper method to send a GET Request, show a Toast etc.
