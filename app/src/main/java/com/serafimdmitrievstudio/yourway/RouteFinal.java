package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.os.Debug;
import android.util.SparseBooleanArray;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.ibm.icu.text.Transliterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


class RouteFinal {

    //<ArrayList<LatLng>> googleRoutes;
    //ArrayList<Route> mapRoutes;
    //boolean googleRouteIsFirst = true;


    private ArrayList<RouteItem> routeItems;
    private int belongingOfThisRoute;

    private String startAddress = "";
    private String endAddress = "";

    RouteFinal() {
        //googleRoutes = new ArrayList<>();
        //mapRoutes = new ArrayList<>();

        /*
        beginRoutePointsIndexes = new ArrayList<>();
        endRoutePointsIndexes = new ArrayList<>();
        beginGooglePointsIndexes = new ArrayList<>();
        endGooglePointsIndexes = new ArrayList<>();
        */

        routeItems = new ArrayList<>();
    }

    public void build(String startAddress, String endAddress,
                      HashMap<Integer, String> streetNames,
                      List<LatLng> mPoints, int belongingOfThisRoute) {

        this.startAddress = startAddress;
        this.endAddress = endAddress;

        this.belongingOfThisRoute = belongingOfThisRoute;

        if (mPoints.size() == 0) {
            Log.write("EMPTY mPoints!");
        }
        double radiusOfSearching = 30.0;

        /*
        if (mPoints.size() > 3) {
            MapPointFinder.searchInWholeDatabase(radiusOfSearching, LocationHandler.departure.getMarker().getPosition(), MapItemType.Road);
        }
        MapPointFinder.searchInWholeDatabase(radiusOfSearching, LocationHandler.destination.getMarker().getPosition(), MapItemType.Road);
        MapPointFinder.addPointToCache();
        */

        HashMap<LatLng, ArrayList<MapPoint>> MapPointsNearGooglePoints = new HashMap<>();

        for (int i = 0; i < mPoints.size(); i++) {
            MapPointsNearGooglePoints.put(mPoints.get(i), Map.findTheClosestPoints(radiusOfSearching, mPoints.get(i)));
        }


        /*
        for (int a = 0; a < mPoints.size(); a++) {
            Log.write("0 Point number " + Integer.toString(a) + " LatLng: " + Double.toString(mPoints.get(a).longitude) + " " + Double.toString(mPoints.get(a).latitude));
            for (int b = 0; b < MapPointsNearGooglePoints.get(mPoints.get(a)).size(); b++) {
                Log.write(Double.toString(MapPointsNearGooglePoints.get(mPoints.get(a)).get(b).getLatLng().longitude) + " " +
                        Double.toString(MapPointsNearGooglePoints.get(mPoints.get(a)).get(b).getLatLng().latitude));
            }
        }
        */

        if (MapPointsNearGooglePoints.get(mPoints.get(0)).size() == 0) {
            MapPoint Departure = MapPointFinder.searchInWholeDatabase(radiusOfSearching, mPoints.get(0), MapItemType.Road);
            MapPointFinder.addPointToCache(true, false);
            if (Departure != null) {
                MapPointsNearGooglePoints.get(mPoints.get(0)).add(Departure);
            }
        }
        if (MapPointsNearGooglePoints.get(mPoints.get(mPoints.size() - 1)).size() == 0) {
            MapPoint Destination = MapPointFinder.searchInWholeDatabase(radiusOfSearching, mPoints.get(mPoints.size() - 1), MapItemType.Road);
            MapPointFinder.addPointToCache(true, false);
            if (Destination != null) {
                MapPointsNearGooglePoints.get(mPoints.get(mPoints.size() - 1)).add(Destination);
            }
        }
        Map.Cache.applyChanges(false);

        boolean routeFound;
        LatLng LastPoint = null;

        Log.write("100");
        for (int p1 = 0; p1 < mPoints.size() - 2; p1++) {
                routeFound = false;

            for (int p2 = mPoints.size() - 1; p2 > p1; p2--) {
                    Route route = new Route();
                    if (route.build(belongingOfThisRoute, MapPointsNearGooglePoints.get(mPoints.get(p1)), MapPointsNearGooglePoints.get(mPoints.get(p2)))) {

                        routeFound = true;

                        if (routeItems.size() != 0) {
                            if (routeItems.get(routeItems.size()-1).isSegment()) {
                                Log.write("4 Size of routeItems:" + Integer.toString(routeItems.size()));
                                routeItems.get(routeItems.size() - 1).getPoints().set(1, route.getFirstPoint());
                            }
                        }

                        /*Log.write("300");
                        RouteItem routeItem = new RouteItem();
                        routeItem.addPoint(mPoints.get(p1));
                        routeItem.addPoint(route.getFirstPoint());
                        routeItems.add(routeItem);
                        */

                        ArrayList<RouteItem> routeItemsFromBuiltRoute = route.getRouteItems();
                        for (int i = 0; i < routeItemsFromBuiltRoute.size(); i++) {
                            routeItems.add(routeItemsFromBuiltRoute.get(i));
                        }

                        Log.write("500");
                        LastPoint = route.getLastPoint();

                        Log.write("6 Size of routeItems:" + Integer.toString(routeItems.size()));
                        Log.write("600");
                        p1 = p2 - 1;

                        break;
                    }
                }

                if (!routeFound) {
                    Log.write("700");
                    RouteItem routeItem = new RouteItem(RouteItem.RoutePart);
                    if (LastPoint == null) {
                        routeItem.addPoint(mPoints.get(p1));
                    } else {
                        routeItem.addPoint(LastPoint);
                        LastPoint = null;
                    }
                    routeItem.addPoint(mPoints.get(p1 + 1));
                    routeItem.setAdditionalText(streetNames.get(p1));
                    routeItems.add(routeItem);
                }
            }
    }

    public ArrayList<RouteItem> getItems() {
        return routeItems;
    }

    public void draw(GoogleMap mMap) {

        Log.write("800");
        for (int i = 1; i < routeItems.size()-1; i++) {
            routeItems.get(i).draw(mMap);
        }


        routeItems.get(0).draw(mMap);
        routeItems.get(routeItems.size()-1).draw(mMap);

    }

     LatLngBounds getLatLngBounds(){
        Log.write("900");

        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        int counter = 0;

        for (int a = 0; a < routeItems.size(); a++) {
            for (int b = 0; b < routeItems.get(a).getPoints().size(); b++) {
                latLngBuilder.include(routeItems.get(a).getPoints().get(b));
                counter++;
            }
        }

        return counter < 2 ? null : latLngBuilder.build();
    }

    void fill(Activity activity, CustomScrollView RouteListView, GoogleMap mMap) {
        for (int i = 0; i < routeItems.size(); i++) {
            routeItems.get(i).generateSplash(activity, mMap);
            RouteListView.addItem(routeItems.get(i).getSplash());
        }
    }

    private double getAngle(LatLng firstPoint, LatLng target) {
        double Angle = (float) Math.toDegrees(Math.atan2(target.latitude - firstPoint.latitude,
                target.longitude - firstPoint.longitude));

        if (Angle < 0){
            Angle += 360;
        }

        return Angle;
    }

    private String getDirectionString (LatLng firstPoint, LatLng target) {

        double Angle = getAngle(firstPoint, target);

        String Direction = "";

        if (Angle >= 22.5 && Angle <= 67.5) Direction = StringHandler.getString(R.string.go_to_north_east);
        if (Angle >= 67.5 && Angle <= 112.5) Direction = StringHandler.getString(R.string.go_to_north);
        if (Angle >= 112.5 && Angle <= 157.5) Direction = StringHandler.getString(R.string.go_to_north_west);
        if (Angle >= 157.5 && Angle <= 202.5) Direction = StringHandler.getString(R.string.go_to_west);
        if (Angle >= 202.5 && Angle <= 247.5) Direction = StringHandler.getString(R.string.go_to_south_west);
        if (Angle >= 247.5 && Angle <= 292.5) Direction = StringHandler.getString(R.string.go_to_south);
        if (Angle >= 292.5 && Angle <= 337.5) Direction = StringHandler.getString(R.string.go_to_south_east);
        if (Angle >= 337.5 && Angle <= 360 || Angle >=0 && Angle <= 22.5) Direction = StringHandler.getString(R.string.go_to_east);

        return Direction;
    }

    private void setTitleAsDirection (RouteItem routeItem, double Angle) {
        String Direction = "";
        String AdditionalText = "";

        if (Angle < 0){
            Angle += 360;
        }
        if (Angle >= 22.5 && Angle <= 50) {
            Direction = StringHandler.getString(R.string.keep_left);
        }
        if (Angle >= 50 && Angle <= 130) {
            Direction = StringHandler.getString(R.string.turn_left);
        }
        if (Angle >= 130 && Angle <= 170) {
            Direction = StringHandler.getString(R.string.turn_left_abruptly);
        }
        if (Angle >= 170 && Angle <= 190) {
            Direction = StringHandler.getString(R.string.turn_back);
        }
        if (Angle >= 190 && Angle <= 230) {
            Direction = StringHandler.getString(R.string.turn_right_abruptly);
        }
        if (Angle >= 230 && Angle <= 310) {
            Direction = StringHandler.getString(R.string.turn_right);
        }
        if (Angle >= 310 && Angle <= 337.5) {
            Direction = StringHandler.getString(R.string.keep_right);
        }
        if (Angle >= 337.5 && Angle <= 360 || Angle >=0 && Angle <= 22.5) {
            Direction = StringHandler.getString(R.string.go_ahead);
        }

        routeItem.setTitle(Direction);
        routeItem.setIconId(R.drawable.icon_point);
    }

    private double getAngleOfTurning(LatLng firstPoint, LatLng secondPoint, LatLng thirdPoint) {

        return getAngle(secondPoint, thirdPoint) - getAngle(firstPoint, secondPoint);
    }

    private RouteItem getNewRouteItem(int index) {
        Log.write("10001");
        RouteItem routeItem = new RouteItem(RouteItem.RoutePart);
        routeItem.addPoint(routeItems.get(index).getPoints().get(0));
        routeItem.addPoint(routeItems.get(index).getPoints().get(1));
        routeItem.setTitle(StringHandler.getString(R.string.go_ahead));
        routeItem.setType(routeItems.get(index).getType());
        routeItem.setQuality(belongingOfThisRoute, routeItems.get(index).getQuality());
        routeItem.setTextAsLength();
        return routeItem;
    }

    private RouteItem getAloneGradeItem(int a) {
        Log.write("10002");
        RouteItem routeItem = null;
        if (routeItems.get(a).getQuality() !=  null) {
            switch (routeItems.get(a).getQuality().getGrade()) {
                case MapItemQuality.GradeFromFirstPointToSecond: {
                    routeItem = new RouteItem(RouteItem.RoutePart);
                    routeItem.addPoint(routeItems.get(a).getPoints().get(0));
                    routeItem.setFirstText(StringHandler.getString(R.string.beginning_of_upward_gradient));
                }
                break;
                case MapItemQuality.GradeFromSecondPointToFirst: {
                    routeItem = new RouteItem(RouteItem.RoutePart);
                    routeItem.addPoint(routeItems.get(a).getPoints().get(0));
                    routeItem.setFirstText(StringHandler.getString(R.string.beginning_of_downward_gradient));
                }
                break;
            }
        }
        return routeItem;
    }

    private RouteItem getAloneWheelchairQualityItem(int a) {
        Log.write("10003");
        RouteItem routeItem = null;
        if (routeItems.get(a).getQuality() !=  null) {
            if (!routeItems.get(a).getQuality().isAccessibleForWheelchair() && belongingOfThisRoute == RouteBelonging.Wheelchair) {
                routeItem = new RouteItem(RouteItem.RoutePart);
                routeItem.addPoint(routeItems.get(a).getPoints().get(0));
                routeItem.setFirstText(StringHandler.getString(R.string.if_you_are_wheelchair_user_probably_you_need_a_helper));
            }
        }
        return routeItem;
    }

    private RouteItem getAloneElectricWheelchairQualityItem(int a) {
        Log.write("100041");
        RouteItem routeItem = null;
        if (routeItems.get(a).getQuality() !=  null) {
            if (!routeItems.get(a).getQuality().isAccessibleForElectricWheelchair() && belongingOfThisRoute == RouteBelonging.ElectricWheelchair) {
                routeItem = new RouteItem(RouteItem.RoutePart);
                routeItem.addPoint(routeItems.get(a).getPoints().get(0));
                routeItem.setFirstText(StringHandler.getString(R.string.if_you_are_electric_wheelchair_user_probably_you_need_a_helper));
            }
        }
        return routeItem;
    }

    private String transliterateUsingLocale(String streetName) {
        Log.write("A_2");
        String result = "";
        Log.write("A_3");
        String systemLanguage = Locale.getDefault().getISO3Language();
        Log.write("A_4");
        try {
            switch(systemLanguage) {
                case "abk":
                case "ady":
                case "ale":
                case "alt":
                case "ava":
                case "bak":
                case "bel":
                case "bua":
                case "bul":
                case "che":
                case "chm":
                case "chu":
                case "chv":
                case "cnr":
                case "crh":
                case "dar":
                case "inh":
                case "kaa":
                case "kaz":
                case "kbd":
                case "kir":
                case "kom":
                case "krc":
                case "kum":
                case "lez":
                case "mkd":
                case "mdf":
                case "nog":
                case "oss":
                case "rus":
                case "sah":
                case "srp":
                case "tat":
                case "tgk":
                case "tyv":
                case "udm":
                case "ukr":
                case "uzb":
                case "xal": {
                    Log.write("A_5");
                    Transliterator toCyrillicTransliterator = Transliterator.getInstance("Any-Cyrillic;[:Diacritic:]remove");
                    Log.write("A_5_1");
                    result = toCyrillicTransliterator.transliterate(streetName);
                }
                break;
                default: {
                    Log.write("A_6");
                    Transliterator toLatinTransliterator = Transliterator.getInstance("Any-Latin/UNGEGN;[:Diacritic:]remove");
                    result = toLatinTransliterator.transliterate(streetName);
                }
                break;
            }
        } catch (Throwable e) {
            Log.write("A_7");
            Log.write(e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Log.write(el.toString());
            }
        }

        return result;
    }

    void normalize() {
        try {
            Log.write("A");
            for (RouteItem item : routeItems) {
                Log.write("A_1");
                String text = item.getAdditionalText();
                Log.write("A_1_1");
                text = transliterateUsingLocale(text);
                Log.write("A_1_2");
                item.setAdditionalText(text);
            }

            Log.write("B");
            ArrayList<RouteItem> finalRouteItems;
            finalRouteItems = new ArrayList<>();

            Log.write("C");
            RouteItem Departure = new RouteItem(RouteItem.RoutePart);
            Departure.addPoint(routeItems.get(0).getPoints().get(0));
            Departure.setTitle(StringHandler.getString(R.string.departure));
            Departure.setIconId(R.drawable.icon_departure);
            Departure.setMarkerIconId(R.drawable.icon_departure_marker);
            Departure.setFirstText(StringHandler.getString(R.string.have_a_good_journey));
            Departure.setAdditionalText(startAddress);
            finalRouteItems.add(Departure);

            Log.write("D");
            int firstIndexOfSegment = 0;
            for (int i = 0; i < routeItems.size(); i++) {
                if (routeItems.get(i).isSegment()) {
                    firstIndexOfSegment = i;
                    break;
                }
            }

            Log.write("E");

            RouteItem FirstGradeItem = getAloneGradeItem(firstIndexOfSegment);
            if (FirstGradeItem != null) {
                finalRouteItems.add(FirstGradeItem);
            }
            RouteItem FirstWheelchairQualityItem = getAloneWheelchairQualityItem(firstIndexOfSegment);
            if (FirstWheelchairQualityItem != null) {
                finalRouteItems.add(FirstWheelchairQualityItem);
            }
            RouteItem FirstWheelchairElectricQualityItem = getAloneElectricWheelchairQualityItem(firstIndexOfSegment);
            if (FirstWheelchairElectricQualityItem != null) {
                finalRouteItems.add(FirstWheelchairElectricQualityItem);
            }

            Log.write("F");
            RouteItem FirstSegment = new RouteItem(RouteItem.RoutePart);
            FirstSegment.addPoint(routeItems.get(firstIndexOfSegment).getPoints().get(0));
            FirstSegment.addPoint(routeItems.get(firstIndexOfSegment).getPoints().get(1));
            FirstSegment.setType(routeItems.get(firstIndexOfSegment).getType());
            FirstSegment.setQuality(belongingOfThisRoute, routeItems.get(firstIndexOfSegment).getQuality());
            FirstSegment.setTitle(getDirectionString(FirstSegment.getPoints().get(0), FirstSegment.getPoints().get(1)));
            FirstSegment.setAdditionalText(routeItems.get(firstIndexOfSegment).getAdditionalText());
            //FirstSegment.setIconId(R.drawable.icon_road);
            FirstSegment.setTextAsLength();
            finalRouteItems.add(FirstSegment);

            Log.write("G");
            for (int a = firstIndexOfSegment + 1; a < routeItems.size(); a++) {
                if (routeItems.get(a).isSegment()) {
                    int b = finalRouteItems.size() - 1;
                    for (; b >= 0; b--) {
                        if (finalRouteItems.get(b).isSegment()) break;
                    }

                    Log.write("H");
                    boolean demarcatorAdded = false;
                    if (finalRouteItems.get(b).getQuality() == null) {
                        RouteItem routeItem = getAloneGradeItem(a);
                        if (routeItem != null) {
                            finalRouteItems.add(routeItem);
                            demarcatorAdded = true;
                        }
                    }
                    Log.write("I");
                    if (finalRouteItems.get(b).getQuality() != null && routeItems.get(a).getQuality() != null) {
                        if ((finalRouteItems.get(b).getQuality().getGrade() == MapItemQuality.NoGrade ||
                                finalRouteItems.get(b).getQuality().getGrade() == MapItemQuality.GradeFromFirstPointToSecond)
                                && routeItems.get(a).getQuality().getGrade() == MapItemQuality.GradeFromSecondPointToFirst) {
                            RouteItem routeItem = getAloneGradeItem(a);
                            if (routeItem != null) {
                                finalRouteItems.add(routeItem);
                                demarcatorAdded = true;
                            }
                        }
                        if ((finalRouteItems.get(b).getQuality().getGrade() == MapItemQuality.NoGrade ||
                                finalRouteItems.get(b).getQuality().getGrade() == MapItemQuality.GradeFromSecondPointToFirst)
                                && routeItems.get(a).getQuality().getGrade() == MapItemQuality.GradeFromFirstPointToSecond) {
                            RouteItem routeItem = getAloneGradeItem(a);
                            if (routeItem != null) {
                                finalRouteItems.add(routeItem);
                                demarcatorAdded = true;
                            }
                        }
                    }


                    Log.write("J");
                    if (finalRouteItems.get(b).getQuality() == null) {
                        RouteItem routeItem = getAloneWheelchairQualityItem(a);
                        if (routeItem != null) {
                            finalRouteItems.add(routeItem);
                            demarcatorAdded = true;
                        }
                        routeItem = getAloneElectricWheelchairQualityItem(a);
                        if (routeItem != null) {
                            finalRouteItems.add(routeItem);
                            demarcatorAdded = true;
                        }

                    } else {
                        if (routeItems.get(a).getQuality() != null) {
                            if (!(finalRouteItems.get(b).getQuality().isAccessibleForWheelchair() == routeItems.get(a).getQuality().isAccessibleForWheelchair())) {
                                RouteItem routeItem = getAloneWheelchairQualityItem(a);
                                if (routeItem != null) {
                                    finalRouteItems.add(routeItem);
                                    demarcatorAdded = true;
                                }
                            }
                            if (!(finalRouteItems.get(b).getQuality().isAccessibleForElectricWheelchair() == routeItems.get(a).getQuality().isAccessibleForElectricWheelchair())) {
                                RouteItem routeItem = getAloneElectricWheelchairQualityItem(a);
                                if (routeItem != null) {
                                    finalRouteItems.add(routeItem);
                                    demarcatorAdded = true;
                                }
                            }
                        }
                    }

                    Log.write("L");
                    if (finalRouteItems.get(b).getQuality() != null && routeItems.get(a).getQuality() == null) {
                        RouteItem routeItem = new RouteItem(RouteItem.RoutePart);
                        routeItem.addPoint(routeItems.get(a).getPoints().get(0));
                        routeItem.setFirstText(StringHandler.getString(R.string.its_no_information_about_next_part_of_route));
                        demarcatorAdded = true;
                        finalRouteItems.add(routeItem);
                    }

                    Log.write("M");
                    if (routeItems.get(a).getQuality() != null) {
                        if (finalRouteItems.get(b).getQuality() == null) {
                            if (routeItems.get(a).getQuality().getGeneralState() == MapItemQuality.Tolerantly) {
                                RouteItem routeItem = new RouteItem(RouteItem.RoutePart);
                                routeItem.addPoint(routeItems.get(a).getPoints().get(0));
                                routeItem.setFirstText(StringHandler.getString(R.string.the_beginning_of_route_part_that_users_marked_as_not_so_good));
                                demarcatorAdded = true;
                                finalRouteItems.add(routeItem);
                            }
                        } else {
                            if (routeItems.get(a).getQuality().getGeneralState() <
                                    finalRouteItems.get(b).getQuality().getGeneralState() &&
                                    routeItems.get(a).getQuality().getGeneralState() == MapItemQuality.Tolerantly) {
                                RouteItem routeItem = new RouteItem(RouteItem.RoutePart);
                                routeItem.addPoint(routeItems.get(a).getPoints().get(0));
                                routeItem.setFirstText(StringHandler.getString(R.string.the_beginning_of_route_part_that_users_marked_as_not_so_good));
                                demarcatorAdded = true;
                                finalRouteItems.add(routeItem);
                            }
                        }
                    }


                    Log.write("N");
                    double Angle = getAngleOfTurning(finalRouteItems.get(b).getPoints().get(finalRouteItems.get(b).getPoints().size() - 2),
                            finalRouteItems.get(b).getPoints().get(finalRouteItems.get(b).getPoints().size() - 1),
                            routeItems.get(a).getPoints().get(1));

                    Log.write("O");
                    if (!(b != finalRouteItems.size() - 1 && !demarcatorAdded)) {
                        if (!(Angle >= -22.5 && Angle <= 22.5)) {
                            RouteItem routeItem = new RouteItem(RouteItem.RoutePart);
                            routeItem.addPoint(finalRouteItems.get(b).getPoints().get(finalRouteItems.get(b).getPoints().size() - 1));

                            setTitleAsDirection(routeItem, Angle);

                            if (a < routeItems.size() - 1) {
                                routeItem.setAdditionalText(routeItems.get(a + 1).getAdditionalText());
                                //routeItem.setYourWayItem(routeItems.get(a + 1).isYourWayItem());
                            }

                            finalRouteItems.add(routeItem);
                            finalRouteItems.add(getNewRouteItem(a));
                        } else {
                            if (!demarcatorAdded) {
                                if (routeItems.get(a).getType() != null) {
                                    if (finalRouteItems.get(b).getType() != null) {

                                        if (routeItems.get(a).getType().getType() ==
                                            finalRouteItems.get(b).getType().getType()) {

                                            finalRouteItems.get(b).addPoint(routeItems.get(a).getPoints().get(1));
                                            finalRouteItems.get(b).setTextAsLength();
                                        } else {
                                            finalRouteItems.add(getNewRouteItem(a));
                                        }
                                    } else {
                                        finalRouteItems.add(getNewRouteItem(a));
                                    }
                                } else {
                                    if (finalRouteItems.get(b).getType() == null) {
                                        finalRouteItems.get(b).addPoint(routeItems.get(a).getPoints().get(1));
                                        finalRouteItems.get(b).setTextAsLength();
                                    } else {
                                        finalRouteItems.add(getNewRouteItem(a));
                                    }
                                }
                            } else {
                                finalRouteItems.add(getNewRouteItem(a));
                            }
                        }
                    } else {
                        finalRouteItems.add(getNewRouteItem(a));
                    }
                } else {
                    RouteItem routeItem = new RouteItem(RouteItem.RoutePart);
                    routeItem.addPoint(routeItems.get(a).getPoints().get(0));
                    routeItem.setType(routeItems.get(a).getType());
                    finalRouteItems.add(routeItem);
                }
            }



            Log.write("P");
            RouteItem routeItem = new RouteItem(RouteItem.RoutePart);
            routeItem.addPoint(finalRouteItems.get(finalRouteItems.size() - 1).getPoints().get(finalRouteItems.get(finalRouteItems.size() - 1).getPoints().size() - 1));
            routeItem.setTitle(StringHandler.getString(R.string.destination));
            routeItem.setIconId(R.drawable.icon_destination);
            routeItem.setMarkerIconId(R.drawable.icon_destination_marker);
            routeItem.setFirstText(StringHandler.getString(R.string.see_you));
            routeItem.setAdditionalText(endAddress);
            finalRouteItems.add(routeItem);

            Log.write("Q");
            for (int i = 1; i < finalRouteItems.size() - 1; i++) {
                if (!finalRouteItems.get(i).isSegment()) {
                    if (finalRouteItems.get(i - 1).getQualityColorId() == finalRouteItems.get(i + 1).getQualityColorId()) {
                        finalRouteItems.get(i).setQualityColorId(finalRouteItems.get(i - 1).getQualityColorId());
                    }
                }
            }

            Log.write("R");
            finalRouteItems.get(0).setQualityColorId(finalRouteItems.get(1).getQualityColorId());
            finalRouteItems.get(finalRouteItems.size() - 1).setQualityColorId(finalRouteItems.get(finalRouteItems.size() - 2).getQualityColorId());


            Log.write("S");
            SparseBooleanArray changed = new SparseBooleanArray();
            for (int i = 0 ; i < finalRouteItems.size() ; i++) {
                changed.put(i, false);
            }

            Log.write("T");
            for (int i = 1; i < finalRouteItems.size() - 1; i++) {
                Log.write("U");
                if (checkTheBeginningOfNongroundPassage(finalRouteItems.get(i-1)
                          ,finalRouteItems.get(i), finalRouteItems.get(i+1))) {

                    if (finalRouteItems.get(i+1).getType().getType() == MapItemType.OverheadPassage) {
                        finalRouteItems.get(i).setAdditionalText(StringHandler.getString(R.string.take_it_up));
                        changed.put(i, true);
                    }
                    if (finalRouteItems.get(i+1).getType().getType() == MapItemType.UndergroundPassage){
                        finalRouteItems.get(i).setAdditionalText(StringHandler.getString(R.string.take_it_down));
                        changed.put(i, true);
                    }
                }

                Log.write("V");
                if (checkTheEndOfNongroundPassage(finalRouteItems.get(i-1)
                        ,finalRouteItems.get(i), finalRouteItems.get(i+1))) {

                    if (finalRouteItems.get(i-1).getType().getType() == MapItemType.OverheadPassage) {
                        finalRouteItems.get(i).setAdditionalText(StringHandler.getString(R.string.take_it_down));
                        changed.put(i, true);
                    }
                    if (finalRouteItems.get(i-1).getType().getType() == MapItemType.UndergroundPassage) {
                        finalRouteItems.get(i).setAdditionalText(StringHandler.getString(R.string.take_it_up));
                        changed.put(i, true);
                    }
                }

                Log.write("W");
                if (finalRouteItems.get(i + 1).getType() != null) {
                    if (finalRouteItems.get(i + 1).getType().getType() == MapItemType.Crossing &&
                            !finalRouteItems.get(i + 1).getAdditionalText().equals("")) {
                        finalRouteItems.get(i + 1).setAdditionalText(StringHandler.getString(R.string.the) + " " + finalRouteItems.get(i).getAdditionalText());
                        finalRouteItems.get(i).setAdditionalText("");
                        changed.put(i, true);
                        changed.put(i+1, true);
                    }
                }

                Log.write("X");
                if (!finalRouteItems.get(i).getAdditionalText().equals("") &&
                        !changed.get(i)) {
                    finalRouteItems.get(i).setAdditionalText(StringHandler.getString(R.string.towards) + " " + finalRouteItems.get(i).getAdditionalText());
                }

                /*
                if (!finalRouteItems.get(i).getAdditionalText().equals("")) {
                    if (finalRouteItems.get(i + 1).getType() != null) {
                        if (finalRouteItems.get(i + 1).getType().getType() == MapItemType.Crossing) {
                            finalRouteItems.get(i + 1).setAdditionalText("the " + finalRouteItems.get(i).getAdditionalText());
                            finalRouteItems.get(i).setAdditionalText("");
                            i++;
                        } else {
                            if (finalRouteItems.get(i).getType() != null) {
                                if (!(finalRouteItems.get(i).getType().getType() == MapItemType.Lift ||
                                        finalRouteItems.get(i).getType().getType() == MapItemType.Ramp)) {
                                }
                            } else {
                                finalRouteItems.get(i).setAdditionalText("towards " + finalRouteItems.get(i).getAdditionalText());
                            }
                        }
                    } else {
                        finalRouteItems.get(i).setAdditionalText("towards " + finalRouteItems.get(i).getAdditionalText());
                    }
                }
                */
            }

            Log.write("Y");
            boolean electricWheelchairNonAccessible = false;
            boolean mechanicalWheelchairNonAccessible = false;
            boolean downwardGrade = false;
            boolean upwardGrade = false;
            boolean noInformation = false;
            for (int i = 0; i < finalRouteItems.size(); i++) {
                if (finalRouteItems.get(i).getQuality() != null) {
                    if (belongingOfThisRoute == RouteBelonging.Wheelchair
                            && !finalRouteItems.get(i).getQuality().isAccessibleForWheelchair()) {
                        electricWheelchairNonAccessible = true;
                    }
                    if (belongingOfThisRoute == RouteBelonging.ElectricWheelchair
                            && !finalRouteItems.get(i).getQuality().isAccessibleForElectricWheelchair()) {
                        mechanicalWheelchairNonAccessible = true;
                    }
                    if (finalRouteItems.get(i).getQuality().getGrade() == MapItemQuality.GradeFromFirstPointToSecond) {
                        upwardGrade = true;
                    }
                    if (finalRouteItems.get(i).getQuality().getGrade() == MapItemQuality.GradeFromSecondPointToFirst) {
                        downwardGrade = true;
                    }
                } else {
                    if (finalRouteItems.get(i).isSegment()) {
                        noInformation = true;
                    }
                }
            }


            Log.write("Z");
            if (electricWheelchairNonAccessible) {
                RouteItem rItem = new RouteItem(RouteItem.Warning);
                rItem.setFirstText(StringHandler.getString(R.string.if_you_are_electric_wheelchair_user_probably_you_need_a_helper_on_some_parts_of_this_route));
                finalRouteItems.add(0, rItem);
            }
            if (mechanicalWheelchairNonAccessible) {
                RouteItem rItem = new RouteItem(RouteItem.Warning);
                rItem.setFirstText(StringHandler.getString(R.string.if_you_are_mechanical_wheelchair_user_probably_you_need_a_helper_on_some_parts_of_this_route));
                finalRouteItems.add(0, rItem);
            }
            if (downwardGrade) {
                RouteItem rItem = new RouteItem(RouteItem.Warning);
                rItem.setFirstText(StringHandler.getString(R.string.there_are_parts_that_have_downward_gradient_int_this_route));
                finalRouteItems.add(0, rItem);
            }
            if (upwardGrade) {
                RouteItem rItem = new RouteItem(RouteItem.Warning);
                rItem.setFirstText(StringHandler.getString(R.string.there_are_parts_that_have_upward_gradient_int_this_route));
                finalRouteItems.add(0, rItem);
            }
            if (noInformation) {
                RouteItem rItem = new RouteItem(RouteItem.Warning);
                rItem.setFirstText(StringHandler.getString(R.string.there_is_no_information_about_some_parts_of_this_route));
                finalRouteItems.add(0, rItem);
            }

            Log.write("AA");
            double Length = 0;
            for (int i = 0; i < finalRouteItems.size(); i++) {
                Length += finalRouteItems.get(i).getLength();
            }

            Log.write("AB");
            RouteItem captionItem = new RouteItem(RouteItem.Caption);
            captionItem.setTitle(StringHandler.getString(R.string.route_built));
            captionItem.setFirstText(StringHandler.getString(R.string.route_distance) + (int) Length + " " + StringHandler.getString(R.string.meters));
            captionItem.setSecondText(StringHandler.getString(R.string.time_of_walking_approximately) + (int) (Length / 70) + " " + StringHandler.getString(R.string.minutes));
            finalRouteItems.add(0, captionItem);


            Log.write("AC");
            routeItems = finalRouteItems;
        } catch (Exception e) {
            Log.writeException(e);
        }
    }

    private boolean checkTheBeginningOfNongroundPassage(RouteItem a, RouteItem b, RouteItem c) {
        if (b.getType() != null && c.getType() != null
                && (b.getType().getType() == MapItemType.Lift ||
                b.getType().getType() == MapItemType.Ramp)) {

            if (a.getType() != null) {
                if (a.getType().getType() != MapItemType.OverheadPassage &&
                        c.getType().getType() == MapItemType.OverheadPassage ||
                        a.getType().getType() != MapItemType.UndergroundPassage &&
                                c.getType().getType() == MapItemType.UndergroundPassage) {
                    return true;
                }
            } else {
                if (c.getType().getType() == MapItemType.UndergroundPassage ||
                        c.getType().getType() == MapItemType.OverheadPassage) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkTheEndOfNongroundPassage(RouteItem a, RouteItem b, RouteItem c) {
        if (b.getType() != null && a.getType() != null
                && (b.getType().getType() == MapItemType.Lift ||
                b.getType().getType() == MapItemType.Ramp)) {

            if (c.getType() != null) {
                if (a.getType().getType() == MapItemType.OverheadPassage &&
                        c.getType().getType() != MapItemType.OverheadPassage ||
                        a.getType().getType() == MapItemType.UndergroundPassage &&
                                c.getType().getType() != MapItemType.UndergroundPassage) {
                    return true;
                }
            } else {
                if (a.getType().getType() == MapItemType.UndergroundPassage ||
                        a.getType().getType() == MapItemType.OverheadPassage) {
                    return true;
                }
            }
        }

        return false;
    }

}
