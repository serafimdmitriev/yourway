package com.serafimdmitrievstudio.yourway;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Serafim on 07.05.2017.
 */

public class MapPoint extends MapItem {
    private LatLng point;
    private ArrayList<MapSegment> SegmentsWhichUseThisPoint;

    Marker marker;

    MapPoint(LatLng point) {
        super();

        this.point = point;
        this.type = new MapItemType(MapItemType.Point);
    }

    MapPoint(LatLng point, short type) {
        super();

        this.type = new MapItemType(MapItemType.Point);

        this.point = point;
        this.type = new MapItemType(type);
    }

    void addSegment(MapSegment segment) {
        if (SegmentsWhichUseThisPoint == null) {
            SegmentsWhichUseThisPoint = new ArrayList<>();
        }
        SegmentsWhichUseThisPoint.add(segment);
    }

/*


    MapSegment findTheRouteSegment(MapPoint destinationPoint) {
        Integer NumberOfSegment = null;
        double distance = Double.MAX_VALUE;
        double currentDistance;

        for (int i = 0; i < SegmentsWhichUseThisPoint.size(); i++) {
            currentDistance = findDistanceUsingCoords(SegmentsWhichUseThisPoint.get(i).getSecondPoint(this).getLatLng(),
                    destinationPoint.getLatLng());

            if (currentDistance < distance) {
                distance = currentDistance;
                        NumberOfSegment = i;
            }
        }

        return SegmentsWhichUseThisPoint.get(NumberOfSegment);
    }

    static double findDistanceUsingCoords(LatLng point1, LatLng point2) {
        double distanceX = point2.longitude - point1.longitude;
        double distanceY = point2.latitude - point1.latitude;

        return Math.sqrt(distanceX*distanceX + distanceY*distanceY);
    }

    static double findDistance(LatLng point1, LatLng point2) {
        Location locationA = new Location("point A");

        locationA.setLatitude(point1.latitude);
        locationA.setLongitude(point1.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(point1.latitude);
        locationB.setLongitude(point2.longitude);

        return locationA.distanceTo(locationB);
    }
    */

    double distanceTo(LatLng toPoint) {
        Location locationA = new Location("point A");

        locationA.setLatitude(point.latitude);
        locationA.setLongitude(point.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(toPoint.latitude);
        locationB.setLongitude(toPoint.longitude);

        return locationA.distanceTo(locationB);
    }

    /*

    static ArrayList<MapPoint> findTheClosestPoints(LatLng initialPoint, double radius) {
        ArrayList<MapPoint> result = new ArrayList<>();

        for (int i = 0; i < Map.getPoints().size(); i++) {
            if (findDistance(initialPoint, Map.getPoints().get(i).getLatLng()) < radius) {
                result.add(Map.getPoints().get(i));
            }
        }

        return result;
    }
    */

    LatLng getLatLng() {
        return point;
    }

    boolean equals(MapPoint point) {
        if (this.point.latitude == point.getLatLng().latitude &&
                this.point.longitude == point.getLatLng().longitude) {
            return true;
        }
        return false;
    }

    String getDefaultTitle() {
        return "POINT";
    }
    int getDefaultIcon() {
        Log.write("Try to get Drawable");
        //int icon = context.getResources().getDrawable(R.drawable.icon_next);
        Log.write("Finish getting Drawable");
        return R.drawable.icon_next;
    }
    String getDefaultDescription1() {
        return "";
    }
    String getDefaultDescription2() {
        return "";
    }
    String getDefaultActionString() {
        return "";
    }


    void redraw(GoogleMap googleMap) {
        remove();
        draw(googleMap);
    }

    void draw(GoogleMap googleMap) {

        if (marker == null) {

            MarkerOptions markerOptions;

            switch (type.getType()) {
                case MapItemType.Lift: {
                    markerOptions = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_lift_marker))
                            .anchor((float) 0.5, (float) 0.5)
                            .visible(false)
                            .position(new LatLng(0, 0));
                }
                break;
                case MapItemType.Ramp: {
                    markerOptions = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_ramp_marker))
                            .anchor((float) 0.5, (float) 0.5)
                            .visible(false)
                            .position(new LatLng(0, 0));
                }
                break;
                default: {
                    markerOptions = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_point_marker))
                            .anchor((float) 0.5, (float) 0.5)
                            .visible(false)
                            .position(new LatLng(0, 0));
                }
                break;
            }


            markerOptions.visible(true);
            markerOptions.position(point);

            marker = googleMap.addMarker(markerOptions);
        }
    }

    boolean isVisible() {
        if (marker == null) {
            return false;
        } else {
            return true;
        }
    }

    void remove() {
        if (marker != null) {
            marker.remove();
            marker = null;
        }
    }

    MapItemType getType() {
        return type;
    }
}
