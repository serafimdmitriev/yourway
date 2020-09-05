package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.serafimdmitrievstudio.yourway.CustomViewGroup;

/**
 * Created by Serafim on 27.08.2017.
 */

public class ViewsAddNongroundPassages extends CustomViewGroup {
    CustomRelativeLayout BottomLayout;
    SaveOrNextStepItem saveOrNextStepItem;
    CustomImageButton ButtonBack;
    private CustomImageButton ButtonIncreaseZoom;
    private CustomImageButton ButtonDecreaseZoom;
    private CustomImageView CenterIcon;

    ViewsAddNongroundPassages(Activity activity) {
        super("AddNongroundPassagesViews");

        ButtonBack = (CustomImageButton) CustomView.getViewById(R.id.buttonBack);
        ButtonIncreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonIncreaseZoom);
        ButtonDecreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonDecreaseZoom);
        CenterIcon = (CustomImageView) CustomView.getViewById(R.id.centerIcon);

        BottomLayout = (CustomRelativeLayout) CustomView.getViewById(R.id.NongroundPassagesRelativeLayout);
        saveOrNextStepItem = new SaveOrNextStepItem(activity);

        saveOrNextStepItem = new SaveOrNextStepItem(activity);
        saveOrNextStepItem.getAddOneMoreButton().setText(StringHandler.getString(R.string.save));
        saveOrNextStepItem.getAddOneMoreButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MapPointFinder.addPointToCache(false, true)) {
                    Map.Cache.removePolylineToScreenCenter();
                    MapPointFinder.animateCameraToPoint();
                }
                else {
                    ErrorLayout.show(StringHandler.getString(R.string.connect_center_of_screen_with_one_of_existing_points_or_roads));
                }
            }
        });
        saveOrNextStepItem.getNextStepButton().setText(StringHandler.getString(R.string.next_step));
        saveOrNextStepItem.getNextStepButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Map.Cache.analyzeBounds()) {
                        CustomViewGroup.open("AddNongroundPassageSettingsViews", true, false);
                    } else {
                        ErrorLayout.show(StringHandler.getString(R.string.all_lifts_or_ramps_should_be_connected_whith_each_other));
                    }
                } catch (Exception e) {
                    Log.write(e.getMessage());
                    for (StackTraceElement el : e.getStackTrace()) {
                        Log.write(el.toString());
                    }
                }
            }
        });
        BottomLayout.getView().addView(saveOrNextStepItem.getView());

        super.addView(BottomLayout);
        super.addView(ButtonBack);
        super.addView(ButtonIncreaseZoom);
        super.addView(ButtonDecreaseZoom);
        super.addView(CenterIcon);

    }

    @Override
    public void specialOpen() {
        Map.Cache.createPinnedActions();

        Map.Cache.animateCameraToPoints();

        CenterIcon.changeImageWithAnimation(R.drawable.icon_point);
        TipLayout.openForTime(StringHandler.getString(R.string.connect_lifts_or_ramps_with_each_other));
        MapPointFinder.searchInPreActionsCache(null);
        Map.Cache.redrawPolylineToScreenCenter();
    }

    @Override
    public void specialClose() {
        Map.Cache.removePolylineToScreenCenter();
        MapPointFinder.removeCaptureArea();
        TipLayout.close();
    }

    @Override
    public void handleCameraMove() {
        Map.Cache.redrawPolylineToScreenCenter();
        MapPointFinder.searchInPreActionsCache(null);
    }

    @Override
    public void handleTouch(int Id) {
    }

    @Override
    public boolean handleClick(Activity activity, int Id) {
        if (Id == ButtonBack.getView().getId()) {
            if (!Map.Cache.removeLastPoint()) {
                Map.Cache.removePinnedActions();
                CustomViewGroup.back();
                //CustomViewGroup.open("AddLiftOrRampViews", true, false);
            } else {
                Map.Cache.redrawPolylineToScreenCenter();
            }
            return true;
        }
        return false;
    }

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
