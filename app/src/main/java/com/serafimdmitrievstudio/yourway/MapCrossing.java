package com.serafimdmitrievstudio.yourway;

/**
 * Created by Serafim on 24.08.2017.
 */

public class MapCrossing extends MapSegment {
    MapCrossing(MapPoint point1, MapPoint point2, MapItemQuality quality, String streetName) {
        super(point1, point2, MapItemType.Crossing, quality, streetName);
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
