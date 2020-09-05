package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Serafim on 10.05.2017.
 */

public class Map {
    private static ArrayList<MapSegment> Segments;
    private static ArrayList<MapPoint> Points;

    private static void checkSegments() {
        if (Segments == null) {
            Segments = new ArrayList<>();
        }
    }

    private static void checkPoints() {
        if (Points == null) {
            Points = new ArrayList<>();
        }
    }

    static void draw(GoogleMap googleMap, boolean justPoint) {
        for (MapSegment segment : Segments) {
            segment.draw(googleMap);
        }
        drawPoints(googleMap, justPoint);
    }
    static void remove() {
        for (MapSegment segment : Segments) {
            segment.remove();
        }
        removePoints();
    }

    static void drawPoints(GoogleMap googleMap, boolean justPoint) {
        if (googleMap.getCameraPosition().zoom >= minZoomForDrawingPoints) {
            for (MapPoint point : Points) {
                if (justPoint) {
                    point.draw(googleMap);
                } else {
                    if (point.getType().getType() != MapItemType.Point) {
                        point.draw(googleMap);
                    }
                }
            }
        }
    }


    private static int minZoomForDrawingPoints = 17;
    static void removePoints() {
        for (MapPoint point : Points) {
            point.remove();
        }
    }

    static void redrawPoints(GoogleMap googleMap, boolean justPoint) {
        if (googleMap.getCameraPosition().zoom >= minZoomForDrawingPoints) {
             drawPoints(googleMap, justPoint);
        } else {
            removePoints();
        }
    }


    /*
    private static boolean drawState = false;
    static void inverseDrawState() {
        drawState = !drawState;
    }
    static boolean getDrawState() {return drawState;}
    static void drawWithState(GoogleMap googleMap, boolean justPoint) {
        draw(googleMap, justPoint);

        for (MapSegment segment : Segments) {
            segment.drawState(googleMap);
        }
    }
    static void removeState() {
        for (MapSegment segment : Segments) {
            segment.removeState();
        }
    }

    static void drawConsideringTheState() {
        if (drawState) {
            drawWithState(GoogleMapHandler.getGoogleMap(), false);
        } else {
            draw(GoogleMapHandler.getGoogleMap(), false);
        }
    }
    */

    static ArrayList<MapPoint> findTheClosestPoints(double Radius, LatLng toLocation){
        ArrayList<MapPoint> result = new ArrayList<>();

        for (MapPoint point : Points) {
            if (point.distanceTo(toLocation) < Radius) {
                result.add(point);
            }
        }

        return result;
    }

    static void load() {
        checkPoints();
        checkSegments();

        DatabaseHandler.loadDatabase(Points, Segments);

        if (Points.size() == 0) {
            Log.write("Points are empty");
        }
        if (Segments.size() == 0) {
            Log.write("Segments are empty");
        }

        /*

        MapPoint point1 = new MapPoint(new LatLng(55.9997529, 37.2002729));
        Points.add(point1);
        Log.write("point1 created");
        MapPoint point2 = new MapPoint(new LatLng(56.00134, 37.2085734));
        Points.add(point2);
        Log.write("point2 created");
        MapRoad road1 = new MapRoad(point1, point2,
                new MapItemQuality(MapItemQuality.Accessible(MapItemQuality.Independent),
                        MapItemQuality.Accessible(MapItemQuality.Independent)),
                new MapSegmentGrade(MapSegmentGrade.NoGrade));
        Segments.add(road1);

        Log.write("Segment created");
        */
    }

    static void reload() {
        checkPoints();
        checkSegments();

        remove();

        Segments.clear();
        Points.clear();

        load();
    }

    static void addSegment(MapSegment mapSegment) {
        checkSegments();

        Segments.add(mapSegment);
    }

    static void addPoint(MapPoint mapPoint) {
        checkPoints();

        Points.add(mapPoint);
    }

    static ArrayList<MapSegment> getSegments() {
        checkSegments();

        return Segments;
    }

    public static ArrayList<MapPoint> getPoints() {
        checkPoints();

        return Points;
    }


    /*
    static boolean unload() {
        try {
            DatabaseHandler.clear();

            Log.write("Segments " + Integer.toString(Segments.size()));
            Log.write("Points " + Integer.toString(Points.size()));

                for (MapSegment segment : Segments) {
                    if (segment == null) Log.write("Segment is null, WTF?");
                    DatabaseHandler.addNewSegment(segment);
                }

                for (MapPoint point : Points) {
                    if (point == null) Log.write("Point is null, WTF?");
                    if (point.getType().getType() == MapItemType.Lift || point.getType().getType() == MapItemType.Ramp) {
                        DatabaseHandler.addNewPoint(point);
                    }
                }

            TipLayout.openForTime("Success!");

        } catch (Exception e) {
            ErrorLayout.show("Oh, something went wrong =( \n" +
                    " Please, try again a little bit later.");
            return false;
        }

        return true;
    }
    */

    private static short Mode = MapItemType.Road;
    private static final int RoadDrawing = MapItemType.Road;
    private static final int GroundPassageDrawing = MapItemType.Crossing;
    private static final int OverheadPassageDrawing = MapItemType.OverheadPassage;
    private static final int UndergroundPassageDrawing = MapItemType.UndergroundPassage;

    static void enableRoadDrawingMode() {
        Cache.clear();
        Mode = RoadDrawing;
    }

    static boolean isRoadDrawingModeEnabled() {
        if (Mode == RoadDrawing) {
            return true;
        } else {
            return false;
        }
    }

    static void enableGroundPassageDrawingMode() {
        Cache.clear();
        Mode = GroundPassageDrawing;
    }

    static boolean isGroundPassageDrawingModeEnabled() {
        if (Mode == GroundPassageDrawing) {
            return true;
        } else {
            return false;
        }
    }

    static void enableUndergroundPassageDrawingMode() {
        Cache.clear();
        Mode = UndergroundPassageDrawing;
    }

    static boolean isUndergroundPassageDrawingModeEnabled() {
        if (Mode == UndergroundPassageDrawing) {
            return true;
        } else {
            return false;
        }
    }

    static void enableOverheadPassageDrawingMode() {
        Cache.clear();
        Mode = OverheadPassageDrawing;
    }

    static boolean isOverheadPassageDrawingModeEnabled() {
        if (Mode == OverheadPassageDrawing) {
            return true;
        } else {
            return false;
        }
    }

    static class Cache{
        static ArrayList<MapAction> Actions = new ArrayList<>();
        static ArrayList<MapAction> PinnedActions = new ArrayList<>();

        static void createPinnedActions() {
            for (MapAction action : Actions) {
                PinnedActions.add(action);
            }
        }

        static void removePinnedActions() {
            PinnedActions.clear();
        }

        static void clear() {
            Actions.clear();
        }

        static void applyChanges(boolean unload) {
            Log.write("001");

            JSONArray ActionsArray = new JSONArray();

            /*
            try {
                JSONObject UserIdObject = new JSONObject();
                UserIdObject.put("userId", SharedPreferencesHandler.mSettings.getInt(SharedPreferencesHandler.A_P_USERID, 0));
                ActionsArray.put(UserIdObject);
            } catch (Exception e){
                Log.write(e.getMessage());
            };
            */

            if (Actions.size() > 0) {
                Log.write("002");
                for (int i = 0; i < Actions.size(); i++) {

                    if (Actions.get(i).SegmentToDelete != null) {
                        Log.write("002");
                        Actions.get(i).SegmentToAdd2 = new MapSegment(Actions.get(i).SegmentToDelete.getPoint1(),
                                Actions.get(i).PointToAdd,
                                Actions.get(i).SegmentToDelete.getType().getType(),
                                Actions.get(i).SegmentToDelete.getQuality(),
                                Actions.get(i).SegmentToDelete.getStreetName()
                                );
                        Log.write("003");
                        Actions.get(i).SegmentToAdd3 = new MapSegment(Actions.get(i).SegmentToDelete.getPoint2(),
                                Actions.get(i).PointToAdd,
                                Actions.get(i).SegmentToDelete.getType().getType(),
                                Actions.get(i).SegmentToDelete.getQuality(),
                                Actions.get(i).SegmentToDelete.getStreetName()
                        );

                        Log.write("004");
                        for (int a = i + 1; a < Actions.size(); a++) {
                            Log.write("005");
                            if (Actions.get(a).SegmentToDelete == Actions.get(i).SegmentToDelete) {
                                Log.write("006");
                                try {
                                    if (Actions.get(a).SegmentToDelete.hasCommonPoint(Actions.get(i).SegmentToAdd2)) {
                                        Log.write("007");
                                        Actions.get(a).SegmentToDelete = Actions.get(i).SegmentToAdd2;
                                    } else {
                                        Log.write("008");
                                        if (Actions.get(a).SegmentToDelete.hasCommonPoint(Actions.get(i).SegmentToAdd3)) {
                                            Log.write("009");
                                            Actions.get(a).SegmentToDelete = Actions.get(i).SegmentToAdd3;
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.write(e.getMessage());
                                }
                            }
                        }
                    }

                    ActionsArray.put(Actions.get(i).getJSON());

                    /*

                    Log.write("010");
                    if (!Points.contains(Actions.get(i).PointToAdd)) {
                        Log.write("011" + Actions.get(i).PointToAdd.toString());
                        Points.add(Actions.get(i).PointToAdd);
                    }
                    if (Actions.get(i).SegmentToDelete != null) {
                        Segments.remove(Actions.get(i).SegmentToDelete);
                        Segments.add(Actions.get(i).SegmentToAdd2);
                        Segments.add(Actions.get(i).SegmentToAdd3);
                    }
                    Log.write("013");
                    if (Actions.get(i).SegmentToAdd != null)
                        Segments.add(Actions.get(i).SegmentToAdd);
                        */
                }


                if (unload) {
                    try {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://accesspassed.com:8080/") //Базовая часть адреса
                                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                                .build();

                        ServerApi serverApi = retrofit.create(ServerApi.class);

                        Call<ServerSimpleResponse> call = serverApi.postActions("postActions",
                                Integer.toString(SharedPreferencesHandler.mSettings.getInt(SharedPreferencesHandler.A_P_USERID, 0)),
                                ActionsArray);

                        call.enqueue(new Callback<ServerSimpleResponse>() {
                            @Override
                            public void onResponse(Call<ServerSimpleResponse> call, Response<ServerSimpleResponse> response) {
                                if (response.body().getResponse().equals("SUCCESSFULLY_ADDED_TO_MAP_FROM_ACTIONS")) {
                                    TipLayout.openForTime("Woohoo!");
                                    Log.write(response.body().getResponse());
                                    reload();
                                }
                                if (response.body().getResponse().equals("SUCCESSFULLY_ADDED_TO_ACTIONS")) {
                                    TipLayout.openForTime(StringHandler.getString(R.string.success_object_adding));
                                    Log.write(response.body().getResponse());
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerSimpleResponse> call, Throwable t) {
                                ErrorLayout.show(StringHandler.getString(R.string.failure_object_adding));
                                Log.write(call.toString());
                                Log.write(t.toString());
                            }
                        });
                    } catch (Exception e) {
                        ErrorLayout.show(StringHandler.getString(R.string.failure_object_adding));
                    }
                }

                //Log.write("014");
                //if (unload) Map.unload();

                Log.write(ActionsArray.toString());
            }
        }

        static void setQualityForSegmentsAndPoints(MapItemQuality quality) {
            for (MapAction action : Actions) {
                if (action.SegmentToAdd != null) action.SegmentToAdd.setQuality(quality);
                if (action.PointToAdd != null) action.PointToAdd.setQuality(quality);
            }
        }

        static void setStreetName(String streetName) {
            for (MapAction action : Actions) {
                if (action.SegmentToAdd != null) action.SegmentToAdd.setStreetName(streetName);
            }
        }


        static boolean applyLastPoint(MapPoint point, MapSegment segment, boolean justPoint, boolean draw) {
            Log.write("0");
            Actions.add(new MapAction());

            Log.write("1");
            Actions.get(Actions.size()-1).PointToAdd = point;
            Log.write("1.1");
            if (draw) Actions.get(Actions.size()-1).drawPoint();

            Log.write("2");
            if (segment != null) {
                Actions.get(Actions.size()-1).SegmentToDelete = segment;
            }

            Log.write("3");
            if (!justPoint) {
                if (Actions.size() % 2 == 0) {
                    Log.write("4");
                    if (!Actions.get(Actions.size() - 1).PointToAdd.equals(Actions.get(Actions.size() - 2).PointToAdd)) {

                        Log.write("5");
                        MapSegment NewSegment  = new MapSegment(
                                Actions.get(Actions.size() - 1).PointToAdd,
                                Actions.get(Actions.size() - 2).PointToAdd,
                                Mode,
                                null,
                                "");

                        Log.write("6");
                        boolean exist = false;
                        Log.write("7");
                        for (MapAction action: Actions) {
                            if (action.SegmentToAdd != null) {
                                if (action.SegmentToAdd.equals(NewSegment.getPoint1(), NewSegment.getPoint2())) {
                                    exist = true;
                                    break;
                                }
                            }
                        }

                        Log.write("8");
                        if (!exist) {
                            Log.write("9");
                            Actions.get(Actions.size() - 1).SegmentToAdd = NewSegment;
                        } else {
                            Log.write("10");
                            removeLastPoint();
                            ErrorLayout.show(StringHandler.getString(R.string.segment_is_already_existing));
                            return false;
                        }

                        Log.write("11");
                        Actions.get(Actions.size() - 1).drawSegment();

                    } else {
                        Log.write("12");
                        removeLastPoint();
                        Log.write("13");
                        ErrorLayout.show(StringHandler.getString(R.string.segment_should_have_different_start_and_finish));
                        return false;
                    }
                }
            }

            return true;
        }

        static void setLastPointAs(short Type) {
            if (Actions.get(Actions.size() - 1).PointToAdd != null) {
                Actions.get(Actions.size() - 1).LastTypeOfPoint = Actions.get(Actions.size() - 1).PointToAdd.getType().getType();
                Actions.get(Actions.size() - 1).PointToAdd.setType(Type, GoogleMapHandler.getGoogleMap());
            }
        }

        static boolean removeLastPoint() {
            if (Actions.size() > 0 && !PinnedActions.contains(Actions.get(Actions.size() - 1))) {
                if (Actions.get(Actions.size() - 1).SegmentToAdd != null) {
                    Actions.get(Actions.size() - 1).removeSegment();
                }

                if (Actions.get(Actions.size() - 1).LastTypeOfPoint != null) {
                    Actions.get(Actions.size() - 1).PointToAdd.setType(Actions.get(Actions.size() - 1).LastTypeOfPoint, GoogleMapHandler.getGoogleMap());
                }

                boolean exist = false;
                for (int i = 0; i < Actions.size() - 1; i++) {
                    if (Actions.get(i).PointToAdd.equals(Actions.get(Actions.size() - 1).PointToAdd)) {
                        exist = true;
                        break;
                    }
                }
                for (int i = 0; i < Points.size() - 1; i++) {
                    if (Points.get(i).equals(Actions.get(Actions.size() - 1).PointToAdd)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) Actions.get(Actions.size() - 1).removePoint();

                Actions.remove(Actions.size() - 1);

                return true;

            } else {
                return false;
            }
        }

        static boolean analyzeBounds() {
            ArrayList<MapPoint> points = getPoints();
            HashMap<MapPoint, Boolean> pointsBounds = new HashMap<>();
            ArrayList<MapSegment> segments = getSegments();

            for (MapPoint point: points) {
                for (MapSegment segment : segments) {
                    if (segment.hasPoint(point)) {
                        pointsBounds.put(point, true);
                        break;
                    } else {
                        pointsBounds.put(point, false);
                    }
                }
            }

            if (pointsBounds.size() == 0) return false;
            for (MapPoint point : points) {
                if (!pointsBounds.get(point)) return false;
            }

            return true;
        }



        private static Polyline polylineFromLastPointToScreenCenter;

        static void redrawPolylineToScreenCenter() {
            removePolylineToScreenCenter();

            if (Actions.size() % 2 == 1) {
                PolylineOptions lineOptions = new PolylineOptions();
                lineOptions.width(10).color(Color.GRAY);
                lineOptions.add(Actions.get(Actions.size() - 1).PointToAdd.getLatLng());
                lineOptions.add(GoogleMapHandler.getGoogleMap().getCameraPosition().target);

                polylineFromLastPointToScreenCenter = GoogleMapHandler.getGoogleMap().addPolyline(lineOptions);
            }
        }

        static void removePolylineToScreenCenter() {
            if (polylineFromLastPointToScreenCenter != null) {
                polylineFromLastPointToScreenCenter.remove();
            }
        }

        static void animateCameraToPoints() {
            LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

            for (int i = 0; i < Actions.size(); i++) {

                latLngBuilder.include(Actions.get(i).PointToAdd.getLatLng());
            }

            CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(), 160);
            GoogleMapHandler.getGoogleMap().animateCamera(track);
        }

        static int getSizeOfPointsArray() {
            return Actions.size();
        }

        static ArrayList<MapPoint> getPoints() {
            ArrayList<MapPoint> points = new ArrayList<>();
            for (MapAction action : Actions) {
                if (action.PointToAdd != null)
                points.add(action.PointToAdd);
            }
            return points;
        }

        static ArrayList<MapSegment> getSegments() {
            ArrayList<MapSegment> segments = new ArrayList<>();
            for (MapAction action : Actions) {
                if (action.SegmentToAdd != null)
                segments.add(action.SegmentToAdd);
            }
            return segments;
        }

        static void redrawCache() {
            for (MapAction action : Actions) {
                if (action.PointToAdd != null)
                action.PointToAdd.redraw(GoogleMapHandler.getGoogleMap());
                if (action.SegmentToAdd != null)
                action.SegmentToAdd.redraw(GoogleMapHandler.getGoogleMap());
            }
        }


        /*

        static class NewPoint {
            private MapPoint nonstaticPoint;
            private MapSegment nonstaticSegmentToCrop;

            NewPoint(MapPoint mp,MapSegment ms) {
                nonstaticPoint = mp;
                nonstaticSegmentToCrop = ms;
            }

            MapPoint getMapPoint() {
                return nonstaticPoint;
            }

            void cropSegmentIfNeeded() {
                if (nonstaticSegmentToCrop != null) {
                    Segments.add(new MapRoad(nonstaticSegmentToCrop.getPoint1(), nonstaticPoint, nonstaticSegmentToCrop.getQuality(), nonstaticSegmentToCrop.getGrade()));
                    Segments.add(new MapRoad(nonstaticSegmentToCrop.getPoint2(), nonstaticPoint, nonstaticSegmentToCrop.getQuality(), nonstaticSegmentToCrop.getGrade()));
                    Segments.remove(nonstaticSegmentToCrop);
                    Points.add(nonstaticPoint);
                }
            }

            private static MapPoint Point;
            private static MapSegment SegmentToCrop;

            public static NewPoint getPoint() {
                return new NewPoint(Point, SegmentToCrop);
            }
        }

        static ArrayList<NewPoint> NewPoints = new ArrayList<>();
        static ArrayList<MapSegment> NewSegments = new ArrayList<>();
        private static Polyline Line;

        static int getSizeOfNewPoints() {
            return NewPoints.size();
        }

        static class CacheL2 {

            static ArrayList<NewPoint> PointsForSegments = new ArrayList<>();
            private static Polyline LineL2;



            static void applyPointForSegments(MapPoint point, MapSegment segment) {
                NewPoint.Point = point;
                NewPoint.SegmentToCrop = segment;

                PointsForSegments.add(NewPoint.getPoint());

                    if (PointsForSegments.size() % 2 == 0) {
                        NewSegments.add(new MapUndergroundPassage(PointsForSegments.get(PointsForSegments.size()-2).getMapPoint(),
                                PointsForSegments.get(PointsForSegments.size()-1).getMapPoint(),
                                new MapItemQuality(MapItemQuality.Independent, MapItemQuality.Independent),
                                new MapSegmentGrade(MapSegmentGrade.NoGrade)));
                    }

                    NewPoint.Point = null;
                    NewPoint.SegmentToCrop = null;
            }

            static void drawLastPointAndSegmentIfNeeded(GoogleMap googleMap) {
                if (PointsForSegments.size() > 0) {
                    if (!PointsForSegments.get(PointsForSegments.size() - 1).getMapPoint().isVisible()) {
                        PointsForSegments.get(PointsForSegments.size() - 1).getMapPoint().draw(googleMap);
                    }
                }
                if (NewSegments.size() > 0 && PointsForSegments.size() > 0 && PointsForSegments.size() % 2 == 0) {
                    NewSegments.get(NewSegments.size()-1).draw(googleMap);
                }
            }

            static boolean removeLastPoint() {
                if (PointsForSegments.size() > 0) {
                    if (PointsForSegments.size() % 2 == 0) {
                        NewSegments.get(NewSegments.size() - 1).remove();
                        NewSegments.remove(NewSegments.size() - 1);
                    }

                    boolean exist = false;
                    for (NewPoint nPoint : NewPoints) {
                        if (nPoint.getMapPoint().equals(PointsForSegments.get(PointsForSegments.size() - 1).getMapPoint())) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        PointsForSegments.get(PointsForSegments.size() - 1).getMapPoint().remove();
                    }

                    PointsForSegments.remove(PointsForSegments.size() - 1);

                    return true;
                } else {
                    return false;
                }
            }

            static void redrawPolylineIfNeeded(GoogleMap googleMap) {
                if (LineL2 != null) {
                    LineL2.remove();
                }

                if (PointsForSegments.size() % 2 == 1) {
                    PolylineOptions lineOptions = new PolylineOptions();
                    lineOptions.width(10).color(Color.GRAY);
                    lineOptions.add(PointsForSegments.get(PointsForSegments.size() - 1).getMapPoint().getLatLng());
                    lineOptions.add(googleMap.getCameraPosition().target);

                    LineL2 = googleMap.addPolyline(lineOptions);
                }
            }
        }

        static ArrayList<MapPoint> getNewPoints() {
            ArrayList<MapPoint> NewMapPoints = new ArrayList<>();

            for (NewPoint point : NewPoints) {
                NewMapPoints.add(point.getMapPoint());
            }

            return NewMapPoints;
        }

        static ArrayList<MapSegment> getNewSegments() {
            return NewSegments;
        }

        public static void cropLastSegmentIfNeeded() {
            if (NewPoint.SegmentToCrop != null) {
                Segments.add(new MapRoad(NewPoint.SegmentToCrop.getPoint1(), NewPoint.Point, NewPoint.SegmentToCrop.getQuality(), NewPoint.SegmentToCrop.getGrade()));
                Segments.add(new MapRoad(NewPoint.SegmentToCrop.getPoint2(), NewPoint.Point, NewPoint.SegmentToCrop.getQuality(), NewPoint.SegmentToCrop.getGrade()));
                Segments.remove(NewPoint.SegmentToCrop);
                Points.add(NewPoint.Point);
            }
        }

        public static void cropAllSegmentsIfNeeded() {
            for (NewPoint point : NewPoints) {
                point.cropSegmentIfNeeded();
            }
        }

        public static void applyLastPoint() {
            NewPoints.add(NewPoint.getPoint());

            NewPoint.Point = null;
            NewPoint.SegmentToCrop = null;
        }

        public static void applyLastPoint(MapPoint point, MapSegment segment) {
            NewPoint.Point = point;
            NewPoint.SegmentToCrop = segment;

            NewPoints.add(NewPoint.getPoint());

            NewPoint.Point = null;
            NewPoint.SegmentToCrop = null;
        }

        public static void drawLastPoint(GoogleMap googleMap) {
            if (NewPoints.size() > 0) {
                NewPoints.get(NewPoints.size() - 1).getMapPoint().draw(googleMap);
            }
        }

        public static boolean removeLastPoint() {
            if (NewPoints.size() > 0) {
                NewPoints.get(NewPoints.size() - 1).getMapPoint().remove();
                NewPoints.remove(NewPoints.size() - 1);
                return true;
            } else {
                return false;
            }
        }

        public static void addPolyline(GoogleMap googleMap) {
            if (NewPoints.size() > 0) {
                PolylineOptions lineOptions = new PolylineOptions();
                if (Map.isRoadDrawingModeEnabled()) {
                    lineOptions.width(10).color(Color.GRAY);
                }
                if (Map.isGroundPassageDrawingModeEnabled()) {
                    lineOptions.width(10).color(Color.BLUE);
                }
                lineOptions.add(NewPoints.get(NewPoints.size() - 1).getMapPoint().getLatLng());
                lineOptions.add(googleMap.getCameraPosition().target);

                Line = googleMap.addPolyline(lineOptions);
            }
        }

        public static void removePolyline() {
            if (Line != null) {
                Line.remove();
            }
        }

        public static ArrayList<LatLng> getPoints() {
            ArrayList<LatLng> positions = new ArrayList<>();

            for (int i = 0; i < NewPoints.size(); i++) {
                positions.add(NewPoints.get(i).getMapPoint().getLatLng());
            }

            return positions;
        }

        */
    }
}
