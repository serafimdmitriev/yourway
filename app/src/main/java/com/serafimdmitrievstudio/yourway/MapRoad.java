package com.serafimdmitrievstudio.yourway;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Serafim on 07.05.2017.
 */

public class MapRoad extends MapSegment {

    MapRoad(MapPoint point1, MapPoint point2, MapItemQuality quality, String streetName) {
        super(point1, point2, MapItemType.Road, quality, streetName);
    }
}
