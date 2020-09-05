package com.serafimdmitrievstudio.yourway;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

/**
 * Created by Serafim on 04.06.2017.
 */

final class System {

    static final long millisecondsInDay = 86400000;

    static boolean getGPSPERMISSION(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    static void hideSoftKeyboard(Activity activity) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
        try {
            if (activity.getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.write(e.getMessage());
        }
    }


    static LatLng findIntersectionPoint(LatLng start1, LatLng end1, LatLng start2, LatLng end2) {
        double start1X = start1.latitude;
        double start1Y = start1.longitude;
        double end1X = end1.latitude;
        double end1Y = end1.longitude;
        double start2X = start2.latitude;
        double start2Y = start2.longitude;
        double end2X = end2.latitude;
        double end2Y = end2.longitude;

        double dir1X = end1X - start1X;
        double dir1Y = end1Y - start1Y;
        double dir2X = end2X - start2X;
        double dir2Y = end2Y - start2Y;

        //считаем уравнения прямых проходящих через отрезки
        double a1 = -dir1Y;
        double b1 = +dir1X;
        double d1 = -(a1*start1X + b1*start1Y);

        double a2 = -dir2Y;
        double b2 = +dir2X;
        double d2 = -(a2*start2X + b2*start2Y);

        //подставляем концы отрезков, для выяснения в каких полуплоскотях они
        double seg1_line2_start = a2*start1X + b2*start1Y + d2;
        double seg1_line2_end = a2*end1X + b2*end1Y + d2;

        double seg2_line1_start = a1*start2X + b1*start2Y + d1;
        double seg2_line1_end = a1*end2X + b1*end2Y + d1;

        //если концы одного отрезка имеют один знак, значит он в одной полуплоскости и пересечения нет.
        if (seg1_line2_start * seg1_line2_end >= 0 || seg2_line1_start * seg2_line1_end >= 0)
            return null;

        double u = seg1_line2_start / (seg1_line2_start - seg1_line2_end);
        double out_intersectionX = start1X + u*dir1X;
        double out_intersectionY = start1Y + u*dir1Y;

        LatLng out_intersection = new LatLng(out_intersectionX, out_intersectionY);

        return out_intersection;
    }

    public static boolean checkPlayServices(Context context) {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int resultCode = api.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(resultCode))
                api.getErrorDialog(((Activity) context), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            else {
                ErrorLayout.show(StringHandler.getString(R.string.yourway_cannot_be_used_google_play_services_unavailable));
                ((Activity) context).finish();
            }
            return false;
        }
        return true;
    }

    public static void requestGPSPermission(final Activity activity) {
        new AlertDialog.Builder(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(R.string.you_can_use_more_features)
                .setMessage(R.string.please_accept_location_permission)
                .setPositiveButton(R.string.i_want_use_it, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                99);
                    }
                })
                .setNegativeButton(R.string.ask_me_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferencesHandler.setValue(SharedPreferencesHandler.A_P_TIMEOFDONTASKABOUTGPS, java.lang.System.currentTimeMillis());
                        GoogleMapHandler.animateToLastLocation(activity);
                    }
                })
                .create()
                .show();
    };
}
