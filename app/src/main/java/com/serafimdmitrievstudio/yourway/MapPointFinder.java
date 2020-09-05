package com.serafimdmitrievstudio.yourway;

import android.app.Activity;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Serafim on 30.08.2017.
 */

public class MapPointFinder {
    private static double Radius;
    private static LatLng CenterOfSearching;

    private static MapPoint LastNewPoint;
    private static MapPoint NewPoint;
    private static MapSegment SegmentToCrop;

    private static Circle CaptureArea;
    private static int colorOfCaptureArea;
    private static double RadiusOfCaptureArea;
    private static double defaultRadiusOfCaptureArea = 7.0;

    public static void initialize(Activity activity) {
        colorOfCaptureArea = activity.getResources().getColor(R.color.colorSearching);
        RadiusOfCaptureArea = defaultRadiusOfCaptureArea;

        clear();
    }

    public static void findThePoint(ArrayList<MapPoint> points) {
        double NewDistance;

        for (MapPoint point : points) {
            if ((NewDistance = point.distanceTo(CenterOfSearching)) < Radius) {
                Log.write("22");

                Radius = NewDistance;

                NewPoint = point;
                SegmentToCrop = null;
            }
        }
    }

    public static void findThePointOnSegment(ArrayList<MapSegment> segments, Short type) {
        double NewDistance;

        for (MapSegment segment : segments) {

            if (type == null || segment.type.getType() == type) {

                double SegmentLengthX = segment.getPoint2().getLatLng().latitude - segment.getPoint1().getLatLng().latitude;
                double SegmentLengthY = segment.getPoint2().getLatLng().longitude - segment.getPoint1().getLatLng().longitude;

                double ValueX = CenterOfSearching.latitude - segment.getPoint1().getLatLng().latitude;
                double ValueY = CenterOfSearching.longitude - segment.getPoint1().getLatLng().longitude;

                double c1 = ValueX * SegmentLengthX + ValueY * SegmentLengthY;
                double c2 = SegmentLengthX * SegmentLengthX + SegmentLengthY * SegmentLengthY;

                double Ratio = c1 / c2;
                double TheClosestPointX = segment.getPoint1().getLatLng().latitude + Ratio * SegmentLengthX;
                double TheClosestPointY = segment.getPoint1().getLatLng().longitude + Ratio * SegmentLengthY;

                if ((TheClosestPointX > segment.getPoint1().getLatLng().latitude &&
                        TheClosestPointX < segment.getPoint2().getLatLng().latitude ||
                        TheClosestPointX > segment.getPoint2().getLatLng().latitude &&
                                TheClosestPointX < segment.getPoint1().getLatLng().latitude)
                        &&
                        (TheClosestPointY > segment.getPoint1().getLatLng().longitude &&
                                TheClosestPointY < segment.getPoint2().getLatLng().longitude ||
                                TheClosestPointY > segment.getPoint2().getLatLng().longitude &&
                                        TheClosestPointY < segment.getPoint1().getLatLng().longitude)) {

                    MapPoint TheClosestPoint = new MapPoint(new LatLng(TheClosestPointX, TheClosestPointY));

                    if ((NewDistance = TheClosestPoint.distanceTo(CenterOfSearching)) < Radius) {
                        Log.write("21");

                        Radius = NewDistance;

                        NewPoint = TheClosestPoint;
                        SegmentToCrop = segment;
                    }

                }
            }
        }
    }

    private static void createNewPoint() {
        Log.write("23");

        NewPoint = new MapPoint(CenterOfSearching);
        SegmentToCrop = null;
    }

    static void clear() {
        NewPoint = null;
        LastNewPoint = null;
    }

    private static void applyDefaultSettings() {
        Radius = defaultRadiusOfCaptureArea;

        CenterOfSearching = GoogleMapHandler.getGoogleMap().getCameraPosition().target;

        LastNewPoint = NewPoint;
        NewPoint = null;
        SegmentToCrop = null;
    }

    static MapPoint searchInWholeDatabase(double radius, LatLng toLocation, Short typeOfSegmentsToSearch) {
        LastNewPoint = NewPoint;
        NewPoint = null;
        SegmentToCrop = null;

        CenterOfSearching = toLocation;
        Radius = radius;

        findThePoint(Map.getPoints());
        if (NewPoint == null) findThePointOnSegment(Map.getSegments(), typeOfSegmentsToSearch);

        return NewPoint;
    }

    static void searchInWholeDatabase(Short typeOfSegmentsToSearch) {
        applyDefaultSettings();

        findThePoint(Map.getPoints());
        if (NewPoint == null) findThePointOnSegment(Map.getSegments(), typeOfSegmentsToSearch);
        if (NewPoint != null) {
            if (!(NewPoint == LastNewPoint)) {
                drawCaptureArea();
            }
        } else {
            removeCaptureArea();
            createNewPoint();
        }
    }

     static void searchInPreActionsCache(Short typeOfSegmentsToSearch) {
         applyDefaultSettings();

         findThePoint(Map.Cache.getPoints());
         if (NewPoint == null) findThePointOnSegment(Map.Cache.getSegments(), typeOfSegmentsToSearch);

         if (NewPoint == null) {
             removeCaptureArea();
         }
         if (NewPoint != null && !(NewPoint == LastNewPoint)) {
             drawCaptureArea();
         }

         if (NewPoint == null && Map.Cache.getSizeOfPointsArray() % 2 == 1) createNewPoint();
    }

    public static void drawCaptureArea() {
        removeCaptureArea();

        if (NewPoint != null) {
            Log.write("10");
            CircleOptions circleOptions = new CircleOptions()
                    .center(NewPoint.getLatLng())
                    .radius(RadiusOfCaptureArea)
                    .fillColor(colorOfCaptureArea)
                    .strokeWidth(0);

            Log.write("11");
            CaptureArea = GoogleMapHandler.getGoogleMap().addCircle(circleOptions);
        }
    }

    public static void removeCaptureArea() {
        Log.write("12");
        if (CaptureArea != null)
        CaptureArea.remove();
    }

    static boolean addPointToCache(boolean justPoint, boolean draw) {
        if (NewPoint != null) {
            Map.Cache.applyLastPoint(NewPoint, SegmentToCrop, justPoint, draw);
            return true;
        } else {
            return false;
        }
    };

    static void animateCameraToPoint() {
        if (NewPoint != null)
        GoogleMapHandler.animateCamera(NewPoint.getLatLng(), false);
    };
}
