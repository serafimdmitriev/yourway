package com.serafimdmitrievstudio.yourway;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.PolyUtil;
import com.ibm.icu.text.Transliterator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class RouteResponse {
    @Expose

    @SerializedName("routes")
    List<Route> routes;
    private List<StreetName> navigationPoints;
    private List<LatLng> polylinePoints;
    private HashMap<Integer, String> streetNamesOnPolyline;

    private class StreetName {
        LatLng fromPoint;
        String streetName;

        StreetName(LatLng fromPoint, String streetName) {
            this.fromPoint = fromPoint;
            this.streetName = streetName;
        }
    }

    private void addRoutePart(LatLng point, String streetName) {
        navigationPoints.add(new StreetName(point, streetName));
    }

    List<LatLng> getPolylinePoints(){
        return polylinePoints;
    }

    HashMap<Integer, String> getStreetNames(){
        return streetNamesOnPolyline;
    }

    void initialize() {
        navigationPoints = new ArrayList<>();
        polylinePoints = PolyUtil.decode(this.routes.get(0).overview_polyline.points);

        streetNamesOnPolyline = new HashMap<>();

        int stepsSize = this.routes.get(0).legs.get(0).steps.size();
        for (int i = 0; i < stepsSize; i++) {

            String instructions = this.routes.get(0).legs.get(0).steps.get(i).html_instructions;
            String streetName = "";
            int position = 0;
            for (int a = 0; a < instructions.length() - 2; a++) {
                if (instructions.charAt(a) == '<'
                        && instructions.charAt(a + 1) == 'b'
                        && instructions.charAt(a + 2) == '>') {

                    switch (position) {
                        case 0: {
                            position++;
                        } break;
                        case 1: {
                            position++;
                            a = a + 3;
                            for (int b = a; instructions.charAt(b) != '<'; b++) {
                                streetName += instructions.charAt(b);
                            }
                        } break;
                    }
                }
            }


            addRoutePart(new LatLng(
                    Double.parseDouble(this.routes.get(0).legs.get(0).steps.get(i).start_location.lat),
                    Double.parseDouble(this.routes.get(0).legs.get(0).steps.get(i).start_location.lng)),
                    streetName);
        }

        /*

        addRoutePart(new LatLng(
                Double.parseDouble(this.routes.get(0).legs.get(0).steps.get(stepsSize - 1).end_location.lat),
                Double.parseDouble(this.routes.get(0).legs.get(0).steps.get(stepsSize - 1).end_location.lng)),
                "");
                \*/


        for (int i = 0; i < polylinePoints.size(); i++) {
            streetNamesOnPolyline.put(i, "");
        }

        for (int a = 0; a < navigationPoints.size(); a++) {
            streetNamesOnPolyline.put(
                    findTheClosestPolylinePoint(navigationPoints.get(a).fromPoint),
                    navigationPoints.get(a).streetName);
        }

        String currentName = "";
        for (int i= 0; i < streetNamesOnPolyline.size(); i++) {
            if (!streetNamesOnPolyline.get(i).equals("")) {
                currentName = streetNamesOnPolyline.get(i);
            }
            streetNamesOnPolyline.put(i, currentName);
        }
    }

    private Integer findTheClosestPolylinePoint(LatLng toPoint) {
        double newDistance;
        double theSmallestDistance = Double.MAX_VALUE;
        Integer result = null;

        for (int i = 0; i < polylinePoints.size(); i++) {
            android.location.Location locationA = new android.location.Location("point A");

            locationA.setLatitude(polylinePoints.get(i).latitude);
            locationA.setLongitude(polylinePoints.get(i).longitude);

            android.location.Location locationB = new android.location.Location("point B");

            locationB.setLatitude(toPoint.latitude);
            locationB.setLongitude(toPoint.longitude);

            if ((newDistance = locationA.distanceTo(locationB)) < theSmallestDistance) {

                theSmallestDistance = newDistance;
                result = i;
            }
        }

        return result;
    }



    /*
    public String getPoints(){
        return this.routes.get(0).overview_polyline.points;
    }

    class StreetName {
        double start_location_latitude;
        double start_location_longitude;
        double end_location_latitude;
        double end_location_longitude;
        String streetName;

        @Override
        public String toString() {
            return Double.toString(start_location_latitude)
                    + "\n" + Double.toString(start_location_longitude)
                    + "\n"  + Double.toString(end_location_latitude)
                    + "\n"  + Double.toString(end_location_longitude)
                    + "\n";
        }
    }

    ArrayList<StreetName> getStreetNames(){
        ArrayList<StreetName> streetNames = new ArrayList<>();

        try {
            for (int i = 0; i < this.routes.get(0).legs.get(0).steps.size(); i++) {

                String instructions = this.routes.get(0).legs.get(0).steps.get(i).html_instructions;
                String streetName = "";

                int position = 0;
                for (int a = 0; a < instructions.length() - 2; a++) {
                    if (instructions.charAt(a) == '<'
                            && instructions.charAt(a + 1) == 'b'
                            && instructions.charAt(a + 2) == '>') {

                        switch (position) {
                            case 0: {
                                position++;
                            } break;
                            case 1: {
                                position++;
                                a = a + 3;
                                for (int b = a; instructions.charAt(b) != '<'; b++) {
                                    streetName += instructions.charAt(b);
                                }
                            } break;
                        }

                    }
                }

                if (!streetName.equals("")) {
                    StreetName newStreetName = new StreetName();
                    newStreetName.start_location_latitude =
                            Double.parseDouble(this.routes.get(0).legs.get(0).steps.get(i).start_location.lat);
                    newStreetName.start_location_longitude =
                            Double.parseDouble(this.routes.get(0).legs.get(0).steps.get(i).start_location.lng);
                    newStreetName.end_location_latitude =
                            Double.parseDouble(this.routes.get(0).legs.get(0).steps.get(i).end_location.lat);
                    newStreetName.end_location_longitude =
                            Double.parseDouble(this.routes.get(0).legs.get(0).steps.get(i).end_location.lng);
                    newStreetName.streetName = streetName;
                    streetNames.add(newStreetName);
                }
            }
        } catch (Exception e) {
            Log.write(e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Log.write(el.toString());
            }
        }

        return streetNames;
    }
    */

    String getStartAddress() {
        return this.routes.get(0).legs.get(0).start_address;
    }

    String getEndAddress() {
        return this.routes.get(0).legs.get(0).end_address;
    }

    private class Route {
        @SerializedName("overview_polyline")
        OverviewPolyline overview_polyline;

        @SerializedName("legs")
        List<Leg> legs;

    }

    private class OverviewPolyline {
        @SerializedName("points")
        String points;
    }

    private class Leg {
        @SerializedName("steps")
        List<Step> steps;
        @SerializedName("start_address")
        String start_address;
        @SerializedName("end_address")
        String end_address;
    }

    private class Step {
        @SerializedName("html_instructions")
        String html_instructions;
        @SerializedName("start_location")
        RouteResponse.Location start_location;
        @SerializedName("end_location")
        RouteResponse.Location end_location;
    }

    private class Location {
        @SerializedName("lat")
        String lat;
        @SerializedName("lng")
        String lng;
    }

}
