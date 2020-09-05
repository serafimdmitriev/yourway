package com.serafimdmitrievstudio.yourway;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Serafim on 28.08.2017.
 */

public class MapUndergroundPassage extends MapSegment {
    MapUndergroundPassage(MapPoint point1, MapPoint point2, MapItemQuality quality) {
        super(point1, point2, MapItemType.UndergroundPassage, quality, "");
    }

    /*
    void draw(GoogleMap googleMap) {
        PolylineOptions lineOptions = new PolylineOptions();
            if (getQuality().isAccessibleForWheelchairIndependently()
                    && getQuality().isAccessibleForElectricWheelchairIndependently()
                    && getGrade().getGrade() == MapSegmentGrade.NoGrade) {
                lineOptions.width(10).color(Color.GREEN);
            } else {
                lineOptions.width(10).color(Color.YELLOW);
            }
            lineOptions.add(getPoint1().getLatLng());
            lineOptions.add(getPoint2().getLatLng());

            polyline = googleMap.addPolyline(lineOptions);

    }
    */
}
