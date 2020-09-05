package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLngBounds;

import java.lang.*;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LocationHandler {
    private static CustomLocation departure;
    private static CustomLocation destination;

    private static Integer belongingOfThisRoute;

    static void initialize() {
        departure = new CustomLocation();
        departure.setIcon(R.drawable.icon_departure_marker);
        destination = new CustomLocation();
        destination.setIcon(R.drawable.icon_destination_marker);
    }

    static void setBelongingOfThisRoute(Integer whose) {
        belongingOfThisRoute = whose;
    }

    static void setDepartureEditTextId(int Id) {
        departure.setEditTextId(Id);
    }

    static void setDestinationEditTextId(int Id) {
        destination.setEditTextId(Id);
    }

    static void setDepartureRelatedViews(String relatedViews) {
        departure.setRelatedViewsName(relatedViews);
    }

    static void setDestinationRelatedViews(String relatedViews) {
        destination.setRelatedViewsName(relatedViews);
    }

    static void removeDeparture() {
        departure.remove();
    }

    static void removeDestination() {
        destination.remove();
    }

    static void remove(CustomLocation location) {
        location.remove();
    }

    static void handleGPSAndShowDeparture(Activity activity) {
        if (departure.handleGPSAddress(activity)) {
            departure.show(activity, destination.isShown(), true, true);
        }
    }

    static void handleMarkerAndShowDeparture(Activity activity) {
        if (departure.handleMarker(activity)) {
            departure.show(activity, destination.isShown(), true, true);
        }
    }

    static void handleMarkerAndShowDestination(Activity activity) {
        if (destination.handleMarker(activity)) {
            destination.show(activity, departure.isShown(), true, true);
        }
    }

    static void handleAddressAndShowDeparture(Activity activity) {
        departure.handleAddressAndShow(activity, destination.isShown(), true, true);
    }

    static void handleAddressAndShowDestination(Activity activity) {
        destination.handleAddressAndShow(activity, departure.isShown(), true, true);
    }

    static void handlePlaceAndShowDeparture(String Id, String text, Activity activity) {
        departure.handlePlaceAndShow(Id, text, activity, destination.isShown(), true, true);
    }

    static void handlePlaceAndShowDestination(String Id, String text, Activity activity) {
        destination.handlePlaceAndShow(Id, text, activity, departure.isShown(), true, true);
    }

    static void handleCustomPlaceAndShowDeparture(Activity activity, CustomPlace customPlace) {
        if (departure.handleCustomPlace(activity, customPlace)) {
            departure.show(activity, destination.isShown(), true, true);
        }
    }

    static void handleCustomPlaceAndShowDestination(Activity activity, CustomPlace customPlace) {
        if (destination.handleCustomPlace(activity, customPlace)) {
            destination.show(activity, departure.isShown(), true, true);
        }
    }

    static void restoreLastDepartureAndDestination() {
        departure.restoreLastState();
        destination.restoreLastState();
    }

    static void checkMarkers(Activity activity, boolean buildRoute, boolean openMainViews) {
        Log.write(Boolean.toString(buildRoute));
        if (buildRoute) {
            Log.write("1505");
            CustomViewGroup.open("SetRouteParamsViews", true, false);

            //BuildRouteAsyncTask buildRouteAsyncTask = new BuildRouteAsyncTask();
            //buildRouteAsyncTask.execute(activity);

            //buildRoute(activity);

            /*if (!buildRoute(activity)) {
                CustomViewGroup.open("MainViews", true, false);
                ErrorLayout.show("Departure and destination are same point. Choose another departure or destination.");
            }
            */

        } else {
            Log.write("1506");
            if (openMainViews) {
                Log.write("1507");
                CustomViewGroup.open("MainViews", true, false);
            }
        }
    }

    static class BuildRouteAsyncTask extends AsyncTask<Activity, Object, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingLayout.open();
        }

        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);

            switch (value) {
                case "ErrorWhileBuildRouteFinal": {
                    LoadingLayout.close();
                    ErrorLayout.show(StringHandler.getString(R.string.route_hasnt_been_found));
                    CustomViewGroup.open("MainViews", true, false);
                }
                break;
                case "RouteBuilt": {
                    LoadingLayout.close();
                    Log.write("RouteListViews.open");
                    CustomViewGroup.open("RouteListViews", true, false);
                }
                break;
                case "NoConnection": {
                    LoadingLayout.close();
                    ErrorLayout.show(StringHandler.getString(R.string.impossible_to_find_route));
                    CustomViewGroup.open("MainViews", true, false);
                }
                break;
            }
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);

            switch ((String)values[0]) {
                case "LOADING": {
                    Log.write("01 " + values[1]);
                    LoadingLayout.setText((String) (values[1]));
                }
                break;
                case "CAMERAUPDATE": {
                    Log.write("CAMERAUPDATE start: " + java.lang.System.currentTimeMillis());
                    Log.write("02");
                    GoogleMapHandler.getGoogleMap().animateCamera((CameraUpdate) (values[1]));
                    Log.write("CAMERAUPDATE end: " + java.lang.System.currentTimeMillis());
                }
                break;
                case "DEPARTURE": {
                    Log.write("03");
                    departure.remove();
                    Log.write("031");
                }
                break;
                case "DESTINATION": {
                    Log.write("04");
                    destination.remove();
                    Log.write("041");
                }
                break;
                case "POLYLINES" : {
                    //Log.write("POLYLINES start: " + java.lang.System.currentTimeMillis());
                    ((RouteItem) (values[1])).draw(GoogleMapHandler.getGoogleMap());
                    /*
                    try {
                        Log.write("05");
                        ((RouteFinal) (values[1])).draw(GoogleMapHandler.getGoogleMap());
                    } catch (Exception e) {
                        Log.write(e.getMessage());
                    }
                    Log.write("POLYLINES end: " + java.lang.System.currentTimeMillis());
                    */
                }
                break;
                case "ROUTEHASBEENBUILT" : {
                    try {
                        Activity activity = (Activity) values[1];
                        Integer itemNumber = (Integer) values[2];
                        RouteItem routeItem = (RouteItem) values[3];
                        CustomScrollView RouteListView = (CustomScrollView) CustomView.getViewById(R.id.ScrollViewRoute);

                        if (itemNumber == 0) {
                            RouteListView.clear();
                            RouteListView.getView().scrollTo(0,0);
                        }

                        routeItem.generateSplash(activity, GoogleMapHandler.getGoogleMap());
                        RouteListView.addItem(routeItem.getSplash());

                        //((RouteFinal) (values[2])).fill(activity, RouteListView, GoogleMapHandler.getGoogleMap());
                    } catch (Exception e) {

                        Log.write(e.getMessage());
                    }
                }
                break;
            }
        }

        @Override
        protected String doInBackground(Activity... params) {
            final Activity activity = params[0];

            final String departureLatLngString = Double.toString(departure.getLatLng().latitude) + "," + Double.toString(departure.getLatLng().longitude);
            final String destinationLatLngString = Double.toString(destination.getLatLng().latitude) + ","  + Double.toString(destination.getLatLng().longitude);
            final String googleMapsKey = activity.getResources().getString(R.string.google_maps_key);

            publishProgress("LOADING", StringHandler.getString(R.string.sending_the_request_to_server));

            Log.write("001");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com") //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                    .build();

            publishProgress("LOADING", StringHandler.getString(R.string.creating_the_route_service_request));

            Log.write("002");
            RouteApi routeApi = retrofit.create(RouteApi.class);

            publishProgress("LOADING", StringHandler.getString(R.string.getting_the_route));

            RouteResponse response;
            try {
                response = routeApi.getRoute(departureLatLngString, destinationLatLngString,
                        "walking", googleMapsKey, Locale.getDefault().toString()).execute().body();
            } catch (Exception e) {
                Log.write(e.getMessage());
                return "NoConnection";
            }

            Log.write("https://maps.googleapis.com/maps/api/directions/json?origin="
                    + departureLatLngString
                    + "&destination=" + destinationLatLngString
                    + "&mode=walking"
                    + "&key=" + googleMapsKey);

            Log.write("004");
            for (int WaitingTime = 0;;) {
                if (response != null) {
                    break;
                } else {
                    if (WaitingTime < 15000) {
                        try {
                            Thread.sleep(1);
                            WaitingTime++;
                        } catch (Exception e) {
                            Log.write(e.getMessage());
                        }
                    } else {
                        return "NoConnection";
                    }
                }
            }

            Log.write("public void success");
            publishProgress("LOADING", StringHandler.getString(R.string.request_has_been_recieved));

            RouteFinal routeFinal = new RouteFinal();

            try {
                publishProgress("LOADING", StringHandler.getString(R.string.route_building_started));

                response.initialize();

                //String stringpoints = response.getPoints();
                //List<LatLng> listpoints = PolyUtil.decode(stringpoints);

                routeFinal.build(response.getStartAddress(),
                        response.getEndAddress(),
                        response.getStreetNames(),
                        response.getPolylinePoints(),
                        belongingOfThisRoute);

                publishProgress("LOADING", StringHandler.getString(R.string.route_building_finished));

            } catch (Exception e) {
                Log.write(e.getMessage());

                return "ErrorWhileBuildRouteFinal";
            }

            publishProgress("LOADING", StringHandler.getString(R.string.camera_animation_started));

            CameraUpdate track;

            int size = activity.getResources().getDisplayMetrics().widthPixels;
            LatLngBounds latLngBounds = routeFinal.getLatLngBounds();
            Log.write("101111111111111111111111");
            if (latLngBounds == null) {
                return "ErrorWhileBuildRouteFinal";
            }
            track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 60);


            publishProgress("CAMERAUPDATE", track);
            publishProgress("LOADING", StringHandler.getString(R.string.camera_animation_finished));
            publishProgress("LOADING", StringHandler.getString(R.string.route_drawing_started));
            publishProgress("DEPARTURE", activity);
            publishProgress("DESTINATION", activity);

            ArrayList<RouteItem> routeItems;
            try {
                Log.write("50");
                routeFinal.normalize();
                Log.write("51");
                routeItems = routeFinal.getItems();
                Log.write("52");
            } catch (Exception e) {
                Log.writeException(e);
                return "ErrorWhileBuildRouteFinal";
            }

            Log.write("POLYLINES start: " + java.lang.System.currentTimeMillis());
            for (int i = 1; i < routeItems.size()-1; i++) {
                publishProgress("POLYLINES", routeItems.get(i));
            }
            publishProgress("POLYLINES", routeItems.get(0));
            publishProgress("POLYLINES", routeItems.get(routeItems.size()-1));
            Log.write("POLYLINES end: " + java.lang.System.currentTimeMillis());


            //RouteListView.getView().scrollTo(0,0);

            /*
            CustomScrollView RouteListView = (CustomScrollView) CustomView.getViewById(R.id.ScrollViewRoute);
            RouteListView.clear();
            for (int i = 0; i < routeItems.size(); i++) {
                routeItems.get(i).generateSplash(activity, GoogleMapHandler.getGoogleMap());
                RouteListView.addItem(routeItems.get(i).getSplash());
            }
            */

            Log.write("ROUTEHASBEENBUILT start: " + java.lang.System.currentTimeMillis());
            for (int i = 0; i < routeItems.size(); i++) {
                publishProgress("ROUTEHASBEENBUILT", activity, i, routeItems.get(i));
                try {
                    Thread.sleep(15);
                } catch (Exception e) {
                    Log.write(e.getMessage());
                }
            }
            Log.write("ROUTEHASBEENBUILT end: " + java.lang.System.currentTimeMillis());

            publishProgress("LOADING", StringHandler.getString(R.string.almost_everything_is_ready));

            return "RouteBuilt";
        }
    }
}
