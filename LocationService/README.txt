The 'LocationService' app is designed to send location updates to the server every 30 seconds. The app detects when the internet is down and for that period stores the location in 
database and also alert the user to switch on the internet. In a situation whenthe user decides to kill the app, it should keep on sending the location from the background thread
The project's source code is divided into the following packages:

1. activities: Contains the following classes:

               1.1 LifecycleLoggingActivity:     This class's primary task is to keep track of the MainActivity's various lifecycle callback hook methods by posting 
                                                 messages to the logcat.
               
               1.2 MainActivity:                 Contains the code dealing with bulk of user facing operations. Also contains a nested class called InternetBroadcastReceiver 
                                                 which creates a BroadcastReceiver necessary to receieve broadcasted intents from LocationService, signalling 'No Internet Connection',
                                                 and then prompting the user to switch on the connecion to internet.
   
   
2. database: Contains the following classes:

                2.1 LocationContract:            This class acts as a binding contract between a client and a database and helps the client in getting access to the database.
                
                2.2 LocationDatabaseHelper:      This class defines the SQLiteDatabase that implements the underlying storage system used to store location updates when 
                                                 the internet connectivity isn't available.
   
   
3. services: Contains the class 'LocationService' that creates the service, and does the bulk of the processing work behind the scenes.


4. utils:    Contains the following utiltity classes:

                4.1 InternetPromptDialog:        This class creates the Alert Dialog which is shown to the user when it has been confirmed by the service that an 
                                                 active internet connection is not available.
                                          
                4.2 LocationBroker:              This class acts as a broker between the LocationService & the Server or the LocationService & the SQLiteDatabase, 
                                                 depending on the internet connectivity.
                
                4.3 LocationData:                This is a Plain Ol' Java Object (POJO) class which wraps metadata related to the location i.e. a latitude, a longitude 
                                                 and a timestamp, into a LocationData Object.
                
                4.4 LocationServiceProxy:        This interface acts as a proxy between the app and the server and is used to create a REST based adapter that sends 
                                                 HTTP Requests to the server.
