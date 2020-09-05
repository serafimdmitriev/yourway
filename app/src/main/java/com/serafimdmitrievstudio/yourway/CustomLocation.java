package com.serafimdmitrievstudio.yourway;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Security;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


class CustomLocation {
    private Marker marker;
    private MarkerOptions markerOptions;
    private int editTextId;
    private CustomPlace place;
    private String RelatedViewsName;
    private boolean shown;

    private CustomPlace lastPlace;

    CustomLocation() {
        place = new CustomPlace();
        markerOptions = new MarkerOptions()
                .anchor((float) 0.5, (float) 0.5);
        shown = false;
    }

    void saveMainCity(String locality) {
        SharedPreferences.Editor editor = SharedPreferencesHandler.mSettings.edit();
        editor.putString(SharedPreferencesHandler.A_P_MAINCITY, locality);
        editor.apply();
    }

    void setIcon(int Id) {
        markerOptions.icon(BitmapDescriptorFactory.fromResource(Id));
    }

    void setEditTextId(int Id) {
        editTextId = Id;
    }

    void setRelatedViewsName(String name) {
        RelatedViewsName = name;
    }

    void remove() {
        lastPlace = new CustomPlace(place);

        getEditText().setText("");
        if (marker != null) {
            marker.remove();
        }
        place.latLng = null;
        place.title = "";
        shown = false;
    }

    void restoreLastState() {
        place = lastPlace;
        getEditText().setText(place.title);

        //markerOptions.position(place.latLng);
        try {
            marker = GoogleMapHandler.getGoogleMap().addMarker(markerOptions);
        } catch (Exception e) {
        }

        shown = true;
    }

    private EditText getEditText() {
        return ((CustomEditText)CustomView.getViewById(editTextId)).getView();
    }


    private Location getLastKnownLocation(Activity activity) {
        LocationManager mLocationManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location location = null;
            try {
                location = mLocationManager.getLastKnownLocation(provider);
            } catch (SecurityException e) {
                ErrorLayout.show(StringHandler.getString(R.string.cannot_use_GPS));
                CustomViewGroup.open(RelatedViewsName, true, false);
            }
            if (location == null) {
                continue;
            }
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }
        return bestLocation;
    }

    boolean handleGPSAddress(final Activity activity) {
        if (System.getGPSPERMISSION(activity)) {
            Location departureLocation = getLastKnownLocation(activity);
            try {
                place.latLng = new LatLng(departureLocation.getLatitude(), departureLocation.getLongitude());
                place.title = getStringFromAddress(getAddressFromLatLng(activity, place.latLng));

                HistoryHandler.addHistoryPlace(new CustomPlace(place), activity);
                saveMainCity(place.title);
            } catch (Exception e) {
                ErrorLayout.show(StringHandler.getString(R.string.cannot_get_address));
                CustomViewGroup.open(RelatedViewsName, true, false);
                return false;
            }
        } else {
            new AlertDialog.Builder(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle(R.string.yourway_can_use_your_GPS_location_only_if_you_accepted_location_permission)
                    .setMessage(R.string.please_accept_location_permission)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    98);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()
                    .show();

            return false;
        }

        return true;
    }

    boolean handleMarker(Activity activity) {
        place.latLng = GoogleMapHandler.getGoogleMap().getCameraPosition().target;

        try {
            place.title = getStringFromAddress(getAddressFromLatLng(activity, place.latLng));

            HistoryHandler.addHistoryPlace(new CustomPlace(place), activity);
            saveMainCity(place.title);
        } catch (Exception e) {
            Log.writeException(e);
            ErrorLayout.show(StringHandler.getString(R.string.cannot_get_adress_of_this_point));
            CustomViewGroup.open(RelatedViewsName, true, false);
            return false;
        }
        return true;
    }

    boolean handleAddressAndShow(final Activity activity, final boolean secondLocationShown, final boolean buildRoute, final boolean openMainViews) {
        String locationString = getEditText().getText().toString();

        if (locationString.equals("")) {
            ErrorLayout.show(activity.getString(R.string.please_type_something_firstly));
            return false;
        }

        if (locationString.equals(place.title) && isShown()) {
            show(activity, secondLocationShown, buildRoute, openMainViews);
            return true;
        }

        PendingResult<AutocompletePredictionBuffer> result =
                Places.GeoDataApi.getAutocompletePredictions(((MainActivity)activity).getGoogleApiClient(), locationString,
                        GoogleMapHandler.getGoogleMap().getProjection().getVisibleRegion().latLngBounds,
                        new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build());

        result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
            @Override
            public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                try {
                    String Id = autocompletePredictions.get(0).getPlaceId();
                    String Title = autocompletePredictions.get(0).getFullText(null).toString();
                    handlePlaceAndShow(Id, Title, activity, secondLocationShown, buildRoute, openMainViews);
                } catch (Exception e) {
                    ErrorLayout.show(StringHandler.getString(R.string.wrong_place_name));
                }
                autocompletePredictions.release();
            }
        });

        /*

        List<Address> addressList;
        Address address;

        if (!locationString.equals("")) {
            try {
                Geocoder geocoder = new Geocoder(activity.getApplicationContext());
                addressList = geocoder.getFromLocationName(locationString, 1);
                address = addressList.get(0);

                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                title = getStringFromAddress(getAddressFromLatLng(activity, latLng));

                HistoryHandler.addHistoryString(title);

            } catch (Exception e) {
                remove();
                ErrorLayout.show("Cannot find Address. Please, check your Internet connection or try to choose another point.");
                CustomViewGroup.open(RelatedViewsName);
                return false;
            }

            return true;
        } else {
            ErrorLayout.show("Cannot find Address. Please, try to choose another point.");
            return false;
        }
        */
        return true;
    }

    boolean handlePlaceAndShow(String placeId, final String text, final Activity activity, final boolean secondLocationShown, final boolean buildRoute, final boolean openMainViews) {
        Places.GeoDataApi.getPlaceById(((MainActivity)activity).getGoogleApiClient(), placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            try {
                                final Place myPlace = places.get(0);
                                place.latLng = myPlace.getLatLng();
                                place.title = text;

                                HistoryHandler.addHistoryPlace(new CustomPlace(place), activity);
                                saveMainCity(place.title);

                                show(activity, secondLocationShown, buildRoute, openMainViews);
                            } catch (Exception e) {
                                ErrorLayout.show(StringHandler.getString(R.string.wrong_place_name));
                            }
                        }
                        if (places.getStatus().isInterrupted()){
                            ErrorLayout.show(StringHandler.getString(R.string.google_places_are_not_available_now));
                        }
                        places.release();
                    }
                });
        return true;
    }

    private String getStringFromAddress(Address address) {
        String text = address.getCountryName();
        if (address.getLocality() != null) text += ", " + address.getLocality();
        if (address.getLocality() == null || !address.getLocality().equals(address.getAddressLine(0)))
            text += ", " + address.getAddressLine(0);

        if (address.getLocality() != null) {
            SharedPreferences.Editor editor = SharedPreferencesHandler.mSettings.edit();
            editor.putString(SharedPreferencesHandler.A_P_MAINCITY, address.getLocality());
            editor.apply();
        }

        return text;
    }

    private Address getAddressFromLatLng(Activity activity, LatLng position) throws Exception {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
        return addresses.get(0);
    }

    boolean handleCustomPlace(Activity activity, CustomPlace place) {
        this.place.title = place.title;
        this.place.latLng = place.latLng;

        HistoryHandler.removeHistoryPlace(place);
        HistoryHandler.addHistoryPlace(new CustomPlace(place), activity);

        return true;
    }

    LatLng getLatLng() {
        return place.latLng;
    }

    void setLatLng(LatLng latLng) {
        this.place.latLng = latLng;
    }

    void show(Activity activity, boolean secondLocationShown, boolean buildRoute, boolean openMainViews) {
        if (marker != null) {
            marker.remove();
        }

        getEditText().setText(place.title);
        markerOptions.position(place.latLng);
        marker = GoogleMapHandler.getGoogleMap().addMarker(markerOptions);
        shown = true;

        if (!secondLocationShown) {
            GoogleMapHandler.animateCamera(place.latLng, true);
            if (openMainViews) CustomViewGroup.open("MainViews", true, false);
        } else {
            LocationHandler.checkMarkers(activity, buildRoute, openMainViews);
        }
    }

    boolean isShown() {
        return shown;
    }
}
