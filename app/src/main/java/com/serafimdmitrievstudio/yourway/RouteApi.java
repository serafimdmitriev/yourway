package com.serafimdmitrievstudio.yourway;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Serafim on 14.05.2017.
 */

public interface RouteApi {

    @GET("/maps/api/directions/json")
    Call<RouteResponse> getRoute(
            @Query(value = "origin") String position,
            @Query(value = "destination") String destination,
            @Query(value = "mode") String mode,
            @Query(value = "key") String key,
            @Query(value = "language") String language//,
            //Callback<RouteResponse> cb
    );
    //, encodeValue = false
}