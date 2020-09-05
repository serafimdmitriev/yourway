package com.serafimdmitrievstudio.yourway;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Serafim on 08.09.2017.
 */

class CustomPlaceSerialization implements Serializable{
    double Latitude;
    double Longitude;
    String Name;

    CustomPlace deserialize() {
        CustomPlace place = new CustomPlace();

        place.latLng = new LatLng(Latitude, Longitude);
        place.title = Name;

        return place;
    }
}
