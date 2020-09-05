package com.serafimdmitrievstudio.yourway;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Serafim on 07.05.2017.
 */

public class MapSegment extends MapItem {
    private MapPoint point1;
    private MapPoint point2;

    private String streetName = "";

    Polyline polyline;

    MapSegment(MapPoint point1, MapPoint point2, short type, MapItemQuality quality, String streetName) {
        super();

        this.point1 = point1;
        this.point1.addSegment(this);
        this.point2 = point2;
        this.point2.addSegment(this);
        this.quality = quality;
        this.type = new MapItemType(type);
        this.streetName = streetName;

        polyline = null;
    }

    void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    String getStreetName() {
        return streetName;
    }

    void setQuality(MapItemQuality quality) {
        this.quality = quality;
    }

    boolean hasPoint(MapPoint point) {
        if (point1.equals(point) || point2.equals(point)) {
            return true;
        } else {
            return false;
        }
    }

    boolean hasCommonPoint(MapSegment segment) {
        if (segment.hasPoint(point1) || segment.hasPoint(point2)) {
            return true;
        } else {
            return false;
        }
    }

    MapItemQuality getQuality() {
        return quality;
    }

    MapPoint getPoint1() {
        return point1;
    }

    MapPoint getPoint2() {
        return point2;
    }

    boolean equals(MapPoint point1, MapPoint point2){
        if (this.point1.equals(point1) && this.point2.equals(point2) ||
                this.point1.equals(point2) && this.point2.equals(point1)) {
            return true;
        }
        return false;
    }

    double getLength(){
        return point1.distanceTo(point2.getLatLng());
    }

    MapPoint getSecondPoint(MapPoint firstPoint) {
        if (firstPoint == point1) return point2;
        if (firstPoint == point2) return point1;

        return null;
    }

    double getAngle (MapPoint firstPoint) {
        MapPoint target = null;
        if (firstPoint == point1) target = point2;
        if (firstPoint == point2) target = point1;

        double angle = (float) Math.toDegrees(Math.atan2(target.getLatLng().latitude - firstPoint.getLatLng().latitude,
                                                         target.getLatLng().longitude - firstPoint.getLatLng().longitude));

        if (angle < 0){
            angle += 360;
        }

        return angle;
    }


    /*(

    public void setDirectionToActionString(MapPoint firstPoint) {
        String Direction = "Unknown";
        double Angle = getAngle(firstPoint);
        if (Angle >= 22.5 && Angle <= 67.5) Direction = "North-East";
        if (Angle >= 67.5 && Angle <= 112.5) Direction = "North";
        if (Angle >= 112.5 && Angle <= 157.5) Direction = "North-West";
        if (Angle >= 157.5 && Angle <= 202.5) Direction = "West";
        if (Angle >= 202.5 && Angle <= 247.5) Direction = "South-West";
        if (Angle >= 247.5 && Angle <= 292.5) Direction = "South";
        if (Angle >= 292.5 && Angle <= 337.5) Direction = "South-East";
        if (Angle >= 337.5 && Angle <= 360 || Angle >=0 && Angle <= 22.5) Direction = "East";

        actionString = "Go to "+ Direction;
    }
    */

    void redraw(GoogleMap googleMap) {
        remove();
        draw(googleMap);
    }


    void draw(GoogleMap googleMap) {
        Log.write("void draw(GoogleMap googleMap) {");
        if (polyline == null) {
            Log.write("polyline == null");

            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.width(13);
            if (quality != null) {
                switch (quality.getGeneralState()) {
                    case MapItemQuality.Tolerantly: {
                        lineOptions.color(Color.argb(130, 255, 0, 0));
                    } break;
                    case MapItemQuality.Normal: {
                        lineOptions.color(Color.argb(145, 255, 255, 0));
                    } break;
                    case MapItemQuality.Excellent: {
                        lineOptions.color(Color.argb(165, 204, 255, 0));
                    } break;
                    default: {
                        lineOptions.color(Color.LTGRAY);
                    } break;
                }
            } else {
                lineOptions.color(Color.LTGRAY);
            }
            lineOptions.add(getPoint1().getLatLng());
            lineOptions.add(getPoint2().getLatLng());
            polyline = googleMap.addPolyline(lineOptions);

        }
    }

    void remove() {
        if (polyline != null) {
            polyline.remove();
            polyline = null;
        }
    };

    boolean isVisible() {
        if (polyline == null) {
            return false;
        } else {
            return true;
        }
    }
}
