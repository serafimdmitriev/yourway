package com.serafimdmitrievstudio.yourway;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

class Route {
    private ArrayList<MapItem> routeItems;
    private ArrayList<MapPoint> currentRoutePoints; //номера узлов текущей "дороги"
    private ArrayList<MapSegment> currentRouteSegments; //номера узлов текущей "дороги"
    private ArrayList<MapPoint> routePoints; //номера узлов текущей "дороги"
    private ArrayList<MapSegment> routeSegments; //номера узлов текущей "дороги"
    private HashMap<MapPoint, Boolean> pointInclude; //true, если i-ая вершина включена в путь

    private Boolean found;

    private double commonLength; //найденный "вес" маршрута
    private double currentLength; //текущий "вес" маршрута;

    private int belongingOfThisRoute;

    private MapSegment find (int belongingOfThisRoute, MapPoint s, MapPoint c) { //вес пути из s и c или 0, если пути нет
        for (int i = 0; i < Map.getSegments().size(); i++)
            if (Map.getSegments().get(i).equals(s, c)
                    && (belongingOfThisRoute == RouteBelonging.Wheelchair
                    && Map.getSegments().get(i).getQuality().isAccessibleForWheelchair()
                    || belongingOfThisRoute == RouteBelonging.ElectricWheelchair
                    && Map.getSegments().get(i).getQuality().isAccessibleForElectricWheelchair()
                    || belongingOfThisRoute == RouteBelonging.ElderlyPerson
                    || belongingOfThisRoute == RouteBelonging.Pedestrian)) {
                return Map.getSegments().get(i);
            }
        return null;
    }

    private void step (int belongingOfThisRoute, MapPoint startPoint, MapPoint finishPoint) { //рекурсивный поиск шага пути

         if (startPoint.equals(finishPoint)) { //путь найден
             found = true; //поставить флажок "найдено"
             commonLength = currentLength; //запомнить общий вес пути

             Log.write("0011" + Integer.toString(currentRoutePoints.size()));

             routePoints.clear();
             for (int i = 0; i < currentRoutePoints.size(); i++) {
                 routePoints.add(currentRoutePoints.get(i));
             }

             Log.write("0012" + Integer.toString(currentRouteSegments.size()));

             routeSegments.clear();
             for (int i = 0; i < currentRouteSegments.size(); i++) {
                 routeSegments.add(currentRouteSegments.get(i));
             }

        } else { //выбор очередной точки

            for (int i = 0; i < Map.getPoints().size(); i++) {
                MapSegment currentSegment = find(belongingOfThisRoute,startPoint, Map.getPoints().get(i)); //есть ли путь из s в c

                if (currentSegment != null
                        && !pointInclude.get(Map.getPoints().get(i))
                        && (commonLength == 0
                        || currentLength + currentSegment.getLength() < commonLength)) { //нужная точка не включена?
                    currentRoutePoints.add(Map.getPoints().get(i)); //включить точку в путь
                    currentRouteSegments.add(currentSegment);
                    pointInclude.put(Map.getPoints().get(i), true); //пометить как включенную
                    currentLength += currentSegment.getLength(); //учесть в общем весе пути

                    step (belongingOfThisRoute, Map.getPoints().get(i), finishPoint); //вызвать себя для поиска следующей точки

                    Log.write("0013" + Integer.toString(routePoints.size()));
                    currentRoutePoints.remove(Map.getPoints().get(i)); //вернуть всё как было
                    Log.write("0014" + Integer.toString(routeSegments.size()));
                    currentRouteSegments.remove(currentSegment);
                    pointInclude.put(Map.getPoints().get(i), false);
                    currentLength -= currentSegment.getLength();
                }
            }
        }
    }

    public boolean build(int belongingOfThisRoute, MapPoint startPoint, MapPoint finishPoint) {
        if (!startPoint.equals(finishPoint)) {
            routeItems = new ArrayList<>();

            currentRoutePoints = new ArrayList<>();
            currentRouteSegments = new ArrayList<>();
            routePoints = new ArrayList<>();
            routeSegments = new ArrayList<>();
            pointInclude = new HashMap<>();
            for (int i = 0; i < Map.getPoints().size(); i++)
                pointInclude.put(Map.getPoints().get(i), false);


            commonLength = 0;
            currentLength = 0;

            currentRoutePoints.add(startPoint);
            pointInclude.put(startPoint, true);  //и пометили как включённую
            found = false; //но путь пока не найден
            step(belongingOfThisRoute, startPoint, finishPoint); //ищем вторую точку

            if (found) {
                if (routePoints.size() == routeSegments.size() + 1) {

                    routeItems.add(routePoints.get(0));
                    Log.write(Integer.toString(routeSegments.size()));
                    for (int i = 0; i < routeSegments.size(); i++) {

                        routeItems.add(routeSegments.get(i));
                        routeItems.add(routePoints.get(i + 1));
                    }

                    return true;
                    //return this;

                } else {
                    return false; //Wrong solution
                }
            } else {
                return false; //No way
            }
        }
        return false;
    }

    public void setRoute(Route route) {
        this.routeItems = route.getList();
        this.routePoints = route.getPoints();
        this.routeSegments = route.getSegments();
        this.commonLength = route.getLength();
    }

    ArrayList<MapItem> getList() {
        return routeItems;
    }

    double getLength() {
        return commonLength;
    }

    public boolean build(int belongingOfThisRoute, ArrayList<MapPoint> startPoints, ArrayList<MapPoint> finishPoints) {
        this.belongingOfThisRoute = belongingOfThisRoute;

        ArrayList<Route> foundRoutes = new ArrayList<>();
        for (int i1 = 0; i1 < startPoints.size(); i1++) {
            for (int i2 = 0; i2 < finishPoints.size(); i2++) {
                Route route = new Route();
                if (route.build(belongingOfThisRoute, startPoints.get(i1), finishPoints.get(i2))) {
                    foundRoutes.add(route);
                }
            }
        }

        double Distance = 0;
        Integer NumberOfNeededRoute = null;
        for (int i = 0; i < foundRoutes.size(); i++) {
            if (foundRoutes.get(i).getLength() > Distance) {
                Distance = foundRoutes.get(i).getLength();
                NumberOfNeededRoute = i;
            }
        }

        if (NumberOfNeededRoute != null) {
            this.setRoute(foundRoutes.get(NumberOfNeededRoute));
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<MapPoint> getPoints() {
        return routePoints;
    }

    public ArrayList<MapSegment> getSegments() {
        return routeSegments;
    }

    ArrayList<RouteItem> getRouteItems() {
        ArrayList<RouteItem> newRouteItems = new ArrayList<>();

        Log.write(Integer.toString(routeItems.size()));

        for (int i = 0; i < routeItems.size(); i++) {
            if (routeItems.get(i).getType().getType() == MapItemType.Lift ||
                    routeItems.get(i).getType().getType() == MapItemType.Ramp ) {
                RouteItem routeItem = new RouteItem(RouteItem.RoutePart);
                routeItem.addPoint((MapPoint) routeItems.get(i));

                newRouteItems.add(routeItem);

                //routeItem.setYourWayItem(true);
                Log.write("MapPoint newRouteItems.add(routeItem);");
            }

            if (routeItems.get(i).getType().getType() == MapItemType.Crossing ||
                    routeItems.get(i).getType().getType() == MapItemType.OverheadPassage ||
                    routeItems.get(i).getType().getType() == MapItemType.UndergroundPassage ||
            routeItems.get(i).getType().getType() == MapItemType.Road) {

                RouteItem routeItem = new RouteItem(RouteItem.RoutePart);
                MapSegment segment = ((MapSegment) routeItems.get(i));

                if (segment.getPoint1().equals((MapPoint) routeItems.get(i-1))
                        && segment.getPoint2().equals((MapPoint) routeItems.get(i+1)))  {
                    routeItem.addPoint(segment.getPoint1());
                    routeItem.addPoint(segment.getPoint2());
                    routeItem.setQuality(belongingOfThisRoute, segment.getQuality());
                    routeItem.setType(segment.getType());
                } else {
                    routeItem.addPoint(segment.getPoint2());
                    routeItem.addPoint(segment.getPoint1());
                    routeItem.setQuality(belongingOfThisRoute, segment.getQuality().getItemWithReversedGrade());
                    routeItem.setType(segment.getType());
                }

                String additionalText = ((MapSegment) routeItems.get(i)).getStreetName();
                if (additionalText == null) additionalText = "";
                routeItem.setAdditionalText(additionalText);

                //routeItem.setYourWayItem(true);

                newRouteItems.add(routeItem);
                Log.write("MapSegment newRouteItems.add(routeItem);");
            }
        }

        return newRouteItems;
    }

    LatLng getFirstPoint() {
        return routePoints.get(0).getLatLng();
    }

    LatLng getLastPoint() {
        return routePoints.get(routePoints.size()-1).getLatLng();
    }
}
