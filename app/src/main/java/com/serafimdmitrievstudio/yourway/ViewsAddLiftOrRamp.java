package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Serafim on 26.08.2017.
 */

public class ViewsAddLiftOrRamp extends CustomViewGroup {
    CustomScrollView BottomLayout;
    LiftOrRampItem liftOrRampItem;
    SaveOrNextStepItem saveOrNextStepItem;
    CustomImageButton ButtonBack;
    private CustomImageButton ButtonIncreaseZoom;
    private CustomImageButton ButtonDecreaseZoom;
    private CustomImageView CenterIcon;

    ViewsAddLiftOrRamp(Activity activity) {
        super("AddLiftOrRampViews");

        ButtonBack = (CustomImageButton) CustomView.getViewById(R.id.buttonBack);
        ButtonIncreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonIncreaseZoom);
        ButtonDecreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonDecreaseZoom);
        CenterIcon = (CustomImageView) CustomView.getViewById(R.id.centerIcon);

        BottomLayout = (CustomScrollView) CustomView.getViewById(R.id.ScrollViewLiftOrRamp);
        liftOrRampItem = new LiftOrRampItem(activity);
        liftOrRampItem.getLiftRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (liftOrRampItem.isRampEnabled()) {
                    liftOrRampItem.enableLift();
                    CenterIcon.changeImageWithAnimation(R.drawable.icon_lift);
                }
            }
        });
        liftOrRampItem.getRampRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (liftOrRampItem.isLiftEnabled()) {
                    liftOrRampItem.enableRamp();
                    CenterIcon.changeImageWithAnimation(R.drawable.icon_ramp);
                }
            }
        });


        saveOrNextStepItem = new SaveOrNextStepItem(activity);
        saveOrNextStepItem.getAddOneMoreButton().setText(StringHandler.getString(R.string.save));
        saveOrNextStepItem.getAddOneMoreButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapPointFinder.addPointToCache(true, true);
                if (liftOrRampItem.isLiftEnabled()) {
                    Map.Cache.setLastPointAs(MapItemType.Lift);
                }
                if (liftOrRampItem.isRampEnabled()) {
                    Map.Cache.setLastPointAs(MapItemType.Ramp);
                }

                setNextStepButtonVisibility();

                MapPointFinder.animateCameraToPoint();
            }
        });
        saveOrNextStepItem.getNextStepButton().setText(StringHandler.getString(R.string.next_step));
        saveOrNextStepItem.getNextStepButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.open("AddNongroundPassagesViews", true, false);
            }
        });

        BottomLayout.addItem(saveOrNextStepItem.getView());
        BottomLayout.addItem(liftOrRampItem.getView());

        super.addView(BottomLayout);
        super.addView(ButtonBack);
        super.addView(ButtonIncreaseZoom);
        super.addView(ButtonDecreaseZoom);
        super.addView(CenterIcon);
    }

    private void setNextStepButtonVisibility() {
        if (Map.Cache.getSizeOfPointsArray() < 2) {
            saveOrNextStepItem.getNextStepButton().setVisibility(View.INVISIBLE);
        } else {
            saveOrNextStepItem.getNextStepButton().setVisibility(View.VISIBLE);
        }
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == ButtonBack.getView().getId()) {
            if (!Map.Cache.removeLastPoint()) {
                CustomViewGroup.back();
                //CustomViewGroup.open("EditorListViews", true, false);
            }
            setNextStepButtonVisibility();
            return true;
        }
        return false;
    }

    public void specialOpen() {
        MapPointFinder.clear();
        
        liftOrRampItem.enableLift();

        CenterIcon.setImage(R.drawable.icon_lift);
        TipLayout.openForTime(StringHandler.getString(R.string.set_at_least_2_lifts_or_ramps));
        setNextStepButtonVisibility();

        Map.draw(GoogleMapHandler.getGoogleMap(), true);
        MapPointFinder.searchInWholeDatabase(MapItemType.Road);
    }

    public void handleCameraMove() {
        MapPointFinder.searchInWholeDatabase(MapItemType.Road);
    };

    public void specialClose() {

        TipLayout.close();
        Map.remove();
        Map.Cache.redrawCache();

        MapPointFinder.removeCaptureArea();
    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
