package com.example.vishesh.utils;

/**-------------------------------------------------------------------------------------------------
 *
 *  Author:     Vishesh vadhera
 *  Written:    24/9/2015
 *
 *
 *  This class represents a Plain Ol' Java Object (POJO) which contains information about
 *  a Location object, i.e. a latitude, a longitude and a timestamp.
 * -------------------------------------------------------------------------------------------------
 */
public class LocationData {

    private double mLatitude;
    private double mLongitude;
    private String mTimestamp;

    public LocationData(double latitude, double longitude, String timestamp){
        mLatitude = latitude;
        mLongitude = longitude;
        mTimestamp = timestamp;
    }

    public void setLatitude(double latitude){
        mLatitude = latitude;
    }

    public void setLongitude(double longitude){
        mLongitude = longitude;
    }

    public void setTimestamp(String timestamp){
        mTimestamp = timestamp;
    }

    public double getLatitude(){
        return mLatitude;
    }

    public double getLongitude(){
        return mLongitude;
    }

    public String getTimestamp(){
        return mTimestamp;
    }
}
