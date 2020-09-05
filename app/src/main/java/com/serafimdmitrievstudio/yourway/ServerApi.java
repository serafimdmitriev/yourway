package com.serafimdmitrievstudio.yourway;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Serafim on 18.02.2018.
 */

interface ServerApi {
    String ServerString = "YourWay/YourWayServer";

    @GET(ServerString)
    Call<ServerUserIdResponse> getUserId(@Query("reason") String reason);

    @GET(ServerString)
    Call<List<ServerGetMapResponse>> getMap(@Query("reason") String reason);

    @FormUrlEncoded
    @POST(ServerString)
    Call<ServerSimpleResponse> postActions(@Field("reason") String reason,
                                           @Field("userId") String userId,
                                           @Field("actions") JSONArray actions);

    @FormUrlEncoded
    @POST(ServerString)
    Call<ServerSimpleResponse> postMessage(@Field("reason") String reason,
                                           @Field("userId") int userId,
                                           @Field("message") String message);
}
