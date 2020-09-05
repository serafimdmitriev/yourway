package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Serafim on 05.06.2017.
 */

public class ViewsAddSegment extends CustomViewGroup {
    private CustomImageButton ButtonNext;
    private CustomImageButton ButtonBack;
    private CustomImageView CenterIcon;
    private CustomImageButton ButtonIncreaseZoom;
    private CustomImageButton ButtonDecreaseZoom;

    ViewsAddSegment(Activity activity) {
        super("AddSegmentViews");

        ButtonNext = (CustomImageButton) CustomView.getViewById(R.id.buttonNext);
        ButtonBack = (CustomImageButton) CustomView.getViewById(R.id.buttonBack);
        CenterIcon = (CustomImageView) CustomView.getViewById(R.id.centerIcon);
        ButtonIncreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonIncreaseZoom);
        ButtonDecreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonDecreaseZoom);

        super.addView(ButtonNext);
        super.addView(ButtonBack);
        super.addView(CenterIcon);
        super.addView(ButtonIncreaseZoom);
        super.addView(ButtonDecreaseZoom);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == ButtonNext.getView().getId()) {

            if (MapPointFinder.addPointToCache(false, true)){
                if (Map.Cache.getSizeOfPointsArray() == 2) {
                    CustomViewGroup.open("AddSegmentSettingsViews", true, false);
                } else {
                    MapPointFinder.animateCameraToPoint();
                }
            }
            return true;
        }
        if (Id == ButtonBack.getView().getId()) {
            if (!Map.Cache.removeLastPoint()) {
                GoogleMapHandler.clear();
                CustomViewGroup.back();
                //CustomViewGroup.open("EditorListViews", true, false);
            } else {
                Map.Cache.redrawPolylineToScreenCenter();
            }
            return true;
        }
        return false;
    }

    public void specialOpen() {
        MapPointFinder.clear();
        CenterIcon.setImage(R.drawable.icon_point);

        if (Map.isRoadDrawingModeEnabled()) {
            TipLayout.openForTime(StringHandler.getString(R.string.set_points_of_new_road));
        }
        if (Map.isGroundPassageDrawingModeEnabled()) {
            TipLayout.openForTime(StringHandler.getString(R.string.set_points_of_new_passage));
        }

        Map.draw(GoogleMapHandler.getGoogleMap(), true);
        MapPointFinder.searchInWholeDatabase(MapItemType.Road);
        Map.Cache.redrawPolylineToScreenCenter();
    }

    public void handleCameraMove() {
        Map.Cache.redrawPolylineToScreenCenter();
        MapPointFinder.searchInWholeDatabase(MapItemType.Road);
    }

    public void specialClose() {
        TipLayout.close();
        Map.Cache.removePolylineToScreenCenter();
        MapPointFinder.removeCaptureArea();
    }

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}

