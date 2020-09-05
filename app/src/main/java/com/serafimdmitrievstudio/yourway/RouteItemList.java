package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Serafim on 10.05.2017.
 */

public class RouteItemList {
    ArrayList<RouteItem> routeItemList;

    RouteItemList() {
        routeItemList = new ArrayList<>();
    }

    /*

    public void addPoint(LatLng point) {
        routeItemList.add(new RouteItem(point));
    }

    public void addSegment(LatLng point1, LatLng point2) {
        routeItemList.add(new RouteItem(point1, point2));
    }

    public void addRoute(Route route) {
        for (int i = 0; i < route.getList().size(); i++) {
            routeItemList.add(new RouteItem(route.getList().get(i)));
        }
    }
*/

    public void normalize(){

        /*
        routeItemList.get(0).enableDepartureMode();
        routeItemList.get(0).setDirection(RouteDirection.getDirection(routeItemList.get(0).getPoints(), routeItemList.get(1).getPoints()));
        routeItemList.get(routeItemList.size()-1).enableDestinationMode();

        Log.write("for (int i = 1; i < routeItemList.size() - 2; i += 2)");
        for (int i = 1; i < routeItemList.size() - 2; i += 2) {
            Log.write(" routeItemList.get(i+1).setDirectionRelativelyToNextSegment");
            routeItemList.get(i + 1).setDirectionRelativelyToNextSegment(RouteDirection.getDirection(routeItemList.get(i).getPoints(), routeItemList.get(i + 2).getPoints()));
        }
        */
    }

    ArrayList<RouteItem> getList(){
        return routeItemList;
    }


    /*
    RouteItemList(ArrayList<MapItem> mapItemList){
        routeItemList = new ArrayList<>();
        polyline = new PolylineOptions();
        Log.write("routeItemList initialized");


        Log.write("Entering to mapItemList cycle");
        for (int i = 0; i < mapItemList.size(); i++) {
            Log.write("If mapItemList.get(i) instanceof MapPoint");
            if (mapItemList.get(i) instanceof MapPoint) {

                Log.write("Create new routeItem");
                routeItemList.add(new RouteItem());

                Log.write("getDefaultTitle");
                routeItemList.get(i).setTitle(((MapPoint)mapItemList.get(i)).getDefaultTitle());
                Log.write("getDefaultIcon");
                routeItemList.get(i).setIcon(((MapPoint)mapItemList.get(i)).getDefaultIcon());
                Log.write("getDefaultDescription1");
                routeItemList.get(i).setDescription1(((MapPoint)mapItemList.get(i)).getDefaultDescription1());
                Log.write("getDefaultDescription2");
                routeItemList.get(i).setDescription2(((MapPoint)mapItemList.get(i)).getDefaultDescription2());
                Log.write("getDefaultActionString");
                routeItemList.get(i).setActionString(((MapPoint)mapItemList.get(i)).getDefaultActionString());
            }
            Log.write("mapItemList.get(i) instanceof MapSegment");
            if (mapItemList.get(i) instanceof MapSegment) {

                Log.write("Create new routeItem");
                routeItemList.add(new RouteItem());

                Log.write("getDefaultTitle");
                routeItemList.get(i).setTitle(((MapSegment)mapItemList.get(i)).getDefaultTitle());
                Log.write("getDefaultIcon");
                routeItemList.get(i).setIcon(((MapSegment)mapItemList.get(i)).getDefaultIcon());
                Log.write("getDefaultDescription1");
                routeItemList.get(i).setDescription1(((MapSegment)mapItemList.get(i)).getDefaultDescription1());
                Log.write("getDefaultDescription2");
                routeItemList.get(i).setDescription2(((MapSegment)mapItemList.get(i)).getDefaultDescription2());
                Log.write("getDefaultActionString");
                routeItemList.get(i).setActionString(((MapSegment)mapItemList.get(i)).getDefaultActionString());
            }
        }

        Log.write("enableDepartureMode");
        routeItemList.get(0).enableDepartureMode();
        Log.write("enableDestinationMode");
        routeItemList.get(routeItemList.size()-1).enableDestinationMode();

        polyline = new PolylineOptions();
        for (int i = 0; i < mapItemList.size(); i+= 2) {
            polyline.add(((MapPoint)mapItemList.get(i)).getLatLng());
        }
        polyline.color(Color.GREEN).width(15);
    };

    ArrayList<RouteItem> getList(){
        return routeItemList;
    }

    PolylineOptions getPolyLine(){
        return polyline;
    }

    */
}
