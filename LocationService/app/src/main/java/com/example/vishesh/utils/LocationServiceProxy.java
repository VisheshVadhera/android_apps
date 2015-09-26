package com.example.vishesh.utils;

import retrofit.http.Body;
import retrofit.http.POST;

/**
 * -------------------------------------------------------------------------------------------------
 *
 *  Author:     Vishesh Vadhera
 *  Written:    24/9/2015
 *
 *
 * This interface acts as a proxy between the app and the server and is used
 * to create a REST based adapter that sends HTTP Requests to the server.
 * -------------------------------------------------------------------------------------------------
 */
public interface LocationServiceProxy {

    public static final String LOCATION_SVC_PATH = "/location/user";

    @POST(LOCATION_SVC_PATH)
    public void sendLocation(@Body LocationData locationData);

}
