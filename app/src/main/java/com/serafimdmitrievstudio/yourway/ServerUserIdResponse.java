package com.serafimdmitrievstudio.yourway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Serafim on 18.02.2018.
 */

class ServerUserIdResponse {
    @Expose
    @SerializedName("userId")
    private int userId;
    @SerializedName("error")
    private String error;

    public int getUserId() {
        return userId;
    }

    public String getError() {
        return error;
    }
}
