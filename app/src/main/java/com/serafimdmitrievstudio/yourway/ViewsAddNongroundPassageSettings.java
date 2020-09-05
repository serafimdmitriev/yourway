package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

/**
 * Created by Serafim on 01.09.2017.
 */

public class ViewsAddNongroundPassageSettings extends CustomViewGroup {
        CustomScrollView SettingsListView;
        CustomImageButton ButtonBack;
        CustomTextView SettingsTitleTextView;

        final AddingItemSettingsHandler settingsHandler;

        View.OnClickListener saveButtonListener;

        public ViewsAddNongroundPassageSettings(final Activity activity) {
            super("AddNongroundPassageSettingsViews");

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
                        Map.Cache.setQualityForSegmentsAndPoints(new MapItemQuality(settingsHandler.getWheelchairAccessible(activity),
                                settingsHandler.getElectricWheelchairAccessible(activity),
                                settingsHandler.getGrade(activity),
                                settingsHandler.getGeneralState(activity)));
                        Map.Cache.applyChanges(true);

                        //Map.applyCacheChanges(true);

                        GoogleMapHandler.clear();
                        CustomViewGroup.open("MainViews", true, false);
                    }
                }
            };


            int bottomPadding = (activity.getResources().getDisplayMetrics().heightPixels) / 2;
            super.setMapPadding(0, bottomPadding / 4, 0, bottomPadding);

            super.addView(SettingsListView);
            super.addView(ButtonBack);
            super.addView(SettingsTitleTextView);
        }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == ButtonBack.getView().getId()) {
            CustomViewGroup.back();
            //CustomViewGroup.open("AddNongroundPassagesViews", true, false);
            return true;
        }
        return false;
    }

    public void specialOpen() {
        settingsHandler.setMode(AddingItemSettingsHandler.AddNongroundPassageMode);
        settingsHandler.getSave().setOnClickListener(saveButtonListener);
        SettingsListView.getView().scrollTo(0,0);
        settingsHandler.reset();

        TipLayout.openForTime(StringHandler.getString(R.string.set_parameters));

        Map.Cache.animateCameraToPoints();
    }

    public void handleCameraMove() {

    }

    public void specialClose() {
    }

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
