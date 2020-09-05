package com.serafimdmitrievstudio.yourway;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    Context context;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        Log.initialize();
        Log.write("00");
        ErrorLayout.initialize(this);
        Log.write("01");
        TipLayout.initialize(this);
        Log.write("01.1");
        LoadingLayout.initialize(this);
        Log.write("02");
        SharedPreferencesHandler.initialize(this);
        Log.write("03");
        MapPointFinder.initialize(this);
        Log.write("04");
        LocationHandler.initialize();
        Log.write("04_1");
        StringHandler.initialize(context.getResources());

        Log.write("05");
        CustomView.initializeAllViews(this);
        new ViewsMain(this);
        new ViewsDeparture(this);
        new ViewsDepartureChoose();
        new ViewsDestination(this);
        new ViewsDestinationChoose();
        new ViewsRouteList();
        new ViewsRoute();
        new ViewsOptionsList(this);
        new ViewsEditorList(this);
        new ViewsAddSegment(this);
        new ViewsAddSegmentSettings(this);
        new ViewsAddLiftOrRamp(this);
        new ViewsAddNongroundPassages(this);
        new ViewsAddNongroundPassageSettings(this);
        new ViewsSetRouteParams(this);
        new ViewsChoosePoint(this);
        new ViewsSendMessage(this);
        new ViewsAbout(this);
        new ViewsEditorIntro(this);
        new ViewsRateUs(this);
        new ViewsIntroduction(this);

        Log.write("20");
        Map.load();

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMapHandler.setGoogleMap(googleMap);
        final Activity activity = this;

        try {
            GoogleMapHandler.getGoogleMap().setMyLocationEnabled(true);
            GoogleMapHandler.animateToMyLocation(context);

        } catch (SecurityException e) {

            long timeOfDontAskAboutGPS = SharedPreferencesHandler.mSettings.getLong(SharedPreferencesHandler.A_P_TIMEOFDONTASKABOUTGPS, 0);
            if (timeOfDontAskAboutGPS == 0) {
                timeOfDontAskAboutGPS = java.lang.System.currentTimeMillis();
                SharedPreferencesHandler.setValue(SharedPreferencesHandler.A_P_TIMEOFDONTASKABOUTGPS, timeOfDontAskAboutGPS);
            }

            if ((java.lang.System.currentTimeMillis()
                    - SharedPreferencesHandler.mSettings.getLong(SharedPreferencesHandler.A_P_TIMEOFDONTASKABOUTGPS, 0))
                    > System.millisecondsInDay * 3
                    && !SharedPreferencesHandler.mSettings.getBoolean(SharedPreferencesHandler.A_P_DONTASKABOUTGPS, false)
                    && !SharedPreferencesHandler.mSettings.getBoolean(SharedPreferencesHandler.A_P_FIRSTUSING, true)) {

                System.requestGPSPermission(this);

            } else {
                GoogleMapHandler.animateToLastLocation(context);
            }
        } catch (Exception e) {
            GoogleMapHandler.animateToLastLocation(context);
        }

        Map.draw(GoogleMapHandler.getGoogleMap(), false);

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (CustomViewGroup.getOpenedViewGroup().getName().equals("MainViews")) {
                    GoogleMapHandler.getGoogleMap().animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 16)));
                    CustomViewGroup.open("ChoosePointViews", true, false);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 99: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        GoogleMapHandler.getGoogleMap().setMyLocationEnabled(true);
                        GoogleMapHandler.animateToMyLocation(context);
                    } catch (SecurityException e){
                        ErrorLayout.show(StringHandler.getString(R.string.cannot_use_GPS));
                        GoogleMapHandler.animateToLastLocation(context);
                    }
                }
                SharedPreferencesHandler.setValue(SharedPreferencesHandler.A_P_DONTASKABOUTGPS, true);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        ErrorLayout.show(StringHandler.getString(R.string.something_went_wrong_with_google_api));
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.checkPlayServices(context);

        CustomViewGroup.resume();

        if (SharedPreferencesHandler.mSettings.getInt(SharedPreferencesHandler.A_P_USERID, 0) == 0) {

            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://accesspassed.com:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServerApi serverApi = retrofit.create(ServerApi.class);

                Call<ServerUserIdResponse> call = serverApi.getUserId("userId");

                call.enqueue(new Callback<ServerUserIdResponse>() {
                    @Override
                    public void onResponse(Call<ServerUserIdResponse> call, Response<ServerUserIdResponse> response) {
                        try {
                            Log.write(Integer.toString(response.body().getUserId()));
                            int userId = response.body().getUserId();
                            SharedPreferencesHandler.setValue(SharedPreferencesHandler.A_P_USERID, userId);
                            Log.write(Integer.toString(userId));
                        } catch (Exception e) {
                            Log.write(e.getMessage());
                            Log.write(response.body().getError());
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerUserIdResponse> call, Throwable t) {
                        Log.write(call.toString());
                        Log.write(t.toString());
                    }
                });
            } catch (Exception e) {
                // nothing bad happened
            }
        }

        if (SharedPreferencesHandler.mSettings.getBoolean(SharedPreferencesHandler.A_P_FIRSTUSING, true)) {

            CustomViewGroup.open("IntroductionViews", true, false);
        } else {
            long timeOfDontAskAboutRate = SharedPreferencesHandler.mSettings.getLong(SharedPreferencesHandler.A_P_TIMEOFDONTASKABOUTRATE, 0);
            if (timeOfDontAskAboutRate == 0) {
                timeOfDontAskAboutRate = java.lang.System.currentTimeMillis();
                SharedPreferencesHandler.setValue(SharedPreferencesHandler.A_P_TIMEOFDONTASKABOUTRATE, timeOfDontAskAboutRate);
            }

            if ((java.lang.System.currentTimeMillis()
                    - timeOfDontAskAboutRate)
                    > System.millisecondsInDay * 5
                    && !SharedPreferencesHandler.mSettings.getBoolean(SharedPreferencesHandler.A_P_DONTASKABOUTRATE, false)) {
                CustomViewGroup.open("RateUsViews", true, false);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            CustomViewGroup.openTheLastViewGroupAgain();
            CustomViewGroup.staticHandleConfigurationChange(this, newConfig);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            CustomViewGroup.openTheLastViewGroupAgain();
            CustomViewGroup.staticHandleConfigurationChange(this, newConfig);
        }
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        CustomViewGroup.open("MainViews", true, false);
    }
    */

    @Override
    public void onBackPressed() {
        try {
            if (!CustomViewGroup.getOpenedViewGroup().handleClick(this, CustomView.getViewById(R.id.buttonBack).getView().getId())) {
                CustomViewGroup.back();
            }
        } catch (NullPointerException e) {

        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferencesHandler.setValue(SharedPreferencesHandler.A_P_TIMEOFLASTUSING, java.lang.System.currentTimeMillis());
    }
}
