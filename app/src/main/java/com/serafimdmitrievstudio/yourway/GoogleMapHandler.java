package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

//import com.bricolsoftconsulting.*;


import java.util.List;
import java.util.Locale;

/**
 * Created by Serafim on 04.06.2017.
 */

public class GoogleMapHandler {
    static private GoogleMap mMap = null;

    static void setGoogleMap(GoogleMap map) {
        mMap = map;
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CustomViewGroup.getOpenedViewGroup().handleCameraMove();
            }
        });
    }

    static void animateCamera(LatLng ToPosition1, LatLng ToPosition2) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(ToPosition1);
        builder.include(ToPosition2);
        LatLngBounds bounds = builder.build();

        int padding = 50;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.animateCamera(cu);
    }

    static void clear() {
        Map.remove();
        mMap.clear();
    }

    static void animateCamera(LatLng ToPosition, boolean changeZoom) {
        if (changeZoom) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(ToPosition, 16)));
        } else {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(ToPosition, mMap.getCameraPosition().zoom)));
        }
    }

    static GoogleMap getGoogleMap() {
        return mMap;
    }

    static boolean animateToMyLocation(Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location location = null;
            try {
                location = mLocationManager.getLastKnownLocation(provider);
            } catch (SecurityException e) {
                //nothing bad happened
            }
            if (location == null) {
                continue;
            }
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }

        if (bestLocation != null) {
            LatLng myLatLng = new LatLng(bestLocation.getLatitude(),
                    bestLocation.getLongitude());

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(myLatLng, 13)));

            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(myLatLng.latitude, myLatLng.longitude, 1);
                if (addresses != null && addresses.get(0).getLocality() != null) {
                    SharedPreferences.Editor editor = SharedPreferencesHandler.mSettings.edit();
                    editor.putString(SharedPreferencesHandler.A_P_MAINCITY, addresses.get(0).getLocality());
                    editor.apply();
                }
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        return false;
    }

    static void animateToLastLocation(Context context) {
        try {
            int zoom = 13;

            Geocoder geocoder = new Geocoder(context);
            String mainCity = SharedPreferencesHandler.mSettings.getString(SharedPreferencesHandler.A_P_MAINCITY, "");

            if (mainCity.equals("")) {
                mainCity = Locale.getDefault().getCountry();
                zoom = 3;
            }

            List<Address> addressList = geocoder.getFromLocationName(mainCity, 1);
            Address address = addressList.get(0);

            LatLng CenterOfMap = new LatLng(address.getLatitude(), address.getLongitude());

            GoogleMapHandler.getGoogleMap().animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(CenterOfMap, zoom)));
        } catch (Exception e) {
            Log.writeException(e);
            //Nothing bad happened
        }
    }
}

/*
            Log.write("111");
            com.bricolsoftconsulting.geocoderplus.Geocoder geocoder = new com.bricolsoftconsulting.geocoderplus.Geocoder(Locale.getDefault());

            Log.write("112");
            String mainCity = SharedPreferencesHandler.mSettings.getString(SharedPreferencesHandler.A_P_MAINCITY, "");

            Log.write("113");
            if (mainCity.equals("")) {
                mainCity = Locale.getDefault().getCountry();
            }

            Log.write("114");
            List<com.bricolsoftconsulting.geocoderplus.Address> addressList = geocoder.getFromLocationName(mainCity, 1);
            com.bricolsoftconsulting.geocoderplus.Address address = addressList.get(0);

            Log.write("115");
            LatLng addressSouthWest = new LatLng(address.getViewPort().getSouthWest().getLatitude(),
                    address.getViewPort().getSouthWest().getLongitude());

            Log.write("116");
            LatLng addressNorthEast = new LatLng(address.getViewPort().getNorthEast().getLatitude(),
                    address.getViewPort().getNorthEast().getLongitude());

            Log.write("117");
            LatLngBounds addressBounds = new LatLngBounds(addressSouthWest, addressNorthEast);
            Log.write("118");
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(addressBounds, 0);

            //LatLng CenterOfMap = new LatLng(address.getLatitude(), address.getLongitude());

            Log.write("119");
            GoogleMapHandler.getGoogleMap().animateCamera(cameraUpdate);

 */
