package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

/**
 * Created by Serafim on 06.06.2017.
 */

public class ViewsAddSegmentSettings extends CustomViewGroup {
    CustomScrollView SettingsListView;
    private CustomImageButton ButtonBack;
    CustomTextView SettingsTitleTextView;

    final AddingItemSettingsHandler settingsHandler;

    View.OnClickListener saveButtonListener;

    Animation shakeAnimation;

    public ViewsAddSegmentSettings(final Activity activity) {
        super("AddSegmentSettingsViews");

        SettingsListView = (CustomScrollView) CustomView.getViewById(R.id.ScrollViewSettings);
        ButtonBack = (CustomImageButton) CustomView.getViewById(R.id.buttonBack);
        SettingsTitleTextView = (CustomTextView) CustomView.getViewById(R.id.settingsTitle);

        settingsHandler = new AddingItemSettingsHandler();
        settingsHandler.initialize(activity);

        saveButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!settingsHandler.getWheelchairAccessible(activity) &&
                        !settingsHandler.getElectricWheelchairAccessible(activity)) {
                    ErrorLayout.show(StringHandler.getString(R.string.please_choose_at_least_one_of_levels_of_accessibility));

                } else {

                    if (settingsHandler.getStreetName().contains(";") ||
                            settingsHandler.getStreetName().contains("'")) {

                        ErrorLayout.show(StringHandler.getString(R.string.street_name_shouldnt_contain_symbols));
                    } else {

                        Map.Cache.setQualityForSegmentsAndPoints(new MapItemQuality(settingsHandler.getWheelchairAccessible(activity),
                                settingsHandler.getElectricWheelchairAccessible(activity),
                                settingsHandler.getGrade(activity),
                                settingsHandler.getGeneralState(activity)));
                        Map.Cache.setStreetName(settingsHandler.getStreetName());

                        Map.Cache.applyChanges(true);
                    }

                    GoogleMapHandler.clear();

                    CustomViewGroup.open("MainViews", true, false);
                }
            }
        };


        int bottomPadding = (activity.getResources().getDisplayMetrics().heightPixels)/2;
        super.setMapPadding(0, bottomPadding/4 , 0 , bottomPadding);

        super.addView(SettingsListView);
        super.addView(ButtonBack);
        super.addView(SettingsTitleTextView);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == ButtonBack.getView().getId()) {
            Map.Cache.removeLastPoint();
            CustomViewGroup.back();
            //CustomViewGroup.open("AddSegmentViews", true, false);
            return true;
        }
        return false;
    }

    public void specialOpen() {
        if (Map.isRoadDrawingModeEnabled()) {
            settingsHandler.setMode(AddingItemSettingsHandler.AddRoadMode);
        }
        if (Map.isGroundPassageDrawingModeEnabled()) {
            settingsHandler.setMode(AddingItemSettingsHandler.AddPassageMode);
        }
        settingsHandler.getSave().setOnClickListener(saveButtonListener);
        SettingsListView.getView().scrollTo(0,0);
        settingsHandler.reset();

        TipLayout.openForTime(StringHandler.getString(R.string.set_parameters));

        Map.Cache.animateCameraToPoints();
    }
    public void handleCameraMove() {

    };

    public void specialClose() {
    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
