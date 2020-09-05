package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Serafim on 03.06.2017.
 */

public class ViewsDepartureChoose extends CustomViewGroup {
    CustomImageButton ButtonNext;
    CustomImageView CenterIcon;
    CustomImageButton ButtonIncreaseZoom;
    CustomImageButton ButtonDecreaseZoom;


    public ViewsDepartureChoose() {
        super("DepartureChooseViews");

        ButtonNext = (CustomImageButton) CustomView.getViewById(R.id.buttonNext);
        CenterIcon = (CustomImageView) CustomView.getViewById(R.id.centerIcon);
        ButtonIncreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonIncreaseZoom);
        ButtonDecreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonDecreaseZoom);

        super.addView(ButtonNext);
        super.addView(CenterIcon);
        super.addView(ButtonIncreaseZoom);
        super.addView(ButtonDecreaseZoom);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == ButtonNext.getView().getId()) {
            LocationHandler.handleMarkerAndShowDeparture(activity);
            return true;
        }
        return false;
    }

    public void specialOpen() {
        CenterIcon.setImage(R.drawable.icon_departure);
        Map.draw(GoogleMapHandler.getGoogleMap(), false);
    }

    public void handleCameraMove() {

    };

    public void specialClose() {

    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
