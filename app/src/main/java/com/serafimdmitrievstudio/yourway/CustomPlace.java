package com.serafimdmitrievstudio.yourway;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Serafim on 07.09.2017.
 */

class CustomPlace{
    String title;
    LatLng latLng;

    CustomPlace(){};

    CustomPlace(CustomPlace place) {
        this.title = place.title;
        this.latLng = place.latLng;
    }

    boolean equals(CustomPlace place) {
        return title.equals(place.title) && latLng.equals(place.latLng);
    }

    CustomPlaceSerialization serialize() {
        CustomPlaceSerialization place = new CustomPlaceSerialization();

        place.Latitude = latLng.latitude;
        place.Longitude = latLng.longitude;
        place.Name = title;

        return place;
    }
}
