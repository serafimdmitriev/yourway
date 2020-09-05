package com.serafimdmitrievstudio.yourway;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Serafim on 18.02.2018.
 */

class ServerGetMapResponse {
    @Expose
    @SerializedName("id")
    private int id;
    @SerializedName("type")
    private short type;
    @SerializedName("latitude1")
    private double latitude1;
    @SerializedName("longitude1")
    private double longitude1;
    @SerializedName("latitude2")
    private double latitude2;
    @SerializedName("longitude2")
    private double longitude2;
    @SerializedName("wheelchairAccessible")
    private boolean wheelchairAccessible;
    @SerializedName("electricWheelchairAccessible")
    private boolean electricWheelchairAccessible;
    @SerializedName("grade")
    private short grade;
    @SerializedName("generalState")
    private short generalState;
    @SerializedName("streetName")
    private String streetName;

    int getId() {
        return id;
    }
    short getType() {
        return type;
    }
    LatLng getLatLng1() {
        return new LatLng(latitude1, longitude1);
    }
    LatLng getLatLng2() {
        try {
            return new LatLng(latitude2, longitude2);
        } catch (Exception e) {
            Log.write("Nihuya sebe " + e.getMessage());
            return null;
        }
    }
    MapItemQuality getMapItemQuality() {
        return new MapItemQuality(wheelchairAccessible, electricWheelchairAccessible, grade, generalState);
    }
    String getStreetName() {
        if (streetName == null) return "";

        try {
            return streetName;
        } catch (Exception e) {
            return "";
        }

    }

}
