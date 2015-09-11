'WeatherServiceApp(Retained)' is designed to fetch weather data from openweathermap.org's weather API.
The app's source code is contained in six different packages:

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
				

3. Activities: This package contains the code that is the does the user facing operations i.e creating and instantiating
				views, button etc. It contains the following classes:

				i) LifecycleLoggingActivity: This class is a helper class whose main function is to log the various lifecycle
											 callback hook methods of the activity that displays the weather data.
											 
				ii) WeatherActivity: This class extends the LifecycleLoggingctivity class and does the major work to establish 
									 the User facing components.
				
				v) WeatherDataArrayAdapter: Extends the ArrayAdapter class and is responsible for mapping the fields of WeatherData
											object to the views that display them to the user
											

4. Services: 	This package contains classes which have a blueprint for creating bound services:
				
				i) WeatherBoundServiceAsync: Service class that handles the download of the weatherData asynchronously.
				
				ii) WeatherBoundServiceSync: Service class that handles the download of the weatherData synchronously.
				
5. Utilities:	This package contains various utility classes:

				 i) Utils: Helper class that contains different helper method to send a GET Request, show a Toast etc.
				 
				 ii) RetainedFragmentManager: This class encapsulates a RetainedFragment - which retains its state during
				 							  runtime reconfiguration changes - as well as the fragment manager that manages 
				 							  the setting up of retained fragment as well as its management
				 							  
				 iii) WeatherDataArrayAdapter: This class extends the array adapter class and is primarily used to map the list
				 							   of WeatherData object returned by the Open Weather Map API into the corresponding 
				 							   TextViews that display those fields.
				 							    
6. Operations:	This package contains a single class called 'WeatherOps' which does the bulk of the processing behind the scenes inorder
				to fetch the data as well as display it. This is done in order to encapsulate various different operations into a single
				class and then store that class into a RetainedFragment whose state is maintained during runtime configuration changes.