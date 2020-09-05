package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Serafim on 03.06.2017.
 */

public class ViewsMain extends CustomViewGroup{

    CustomImageButton ButtonOptions;
    CustomEditText DepartureEditText;
    CustomEditText DestinationEditText;
    CustomImageButton ButtonIncreaseZoom;
    CustomImageButton ButtonDecreaseZoom;

    Activity activity;

    public ViewsMain(Activity activity) {
        super("MainViews");
        this.activity = activity;

        ButtonOptions = (CustomImageButton) CustomView.getViewById(R.id.buttonOptions);
        DepartureEditText = (CustomEditText) CustomView.getViewById(R.id.editTextDeparture);
        DestinationEditText = (CustomEditText) CustomView.getViewById(R.id.editTextDestination);
        ButtonIncreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonIncreaseZoom);
        ButtonDecreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonDecreaseZoom);

        super.addView(ButtonOptions);
        super.addView(DepartureEditText);
        super.addView(DestinationEditText);
        super.addView(ButtonIncreaseZoom);
        super.addView(ButtonDecreaseZoom);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == ButtonOptions.getView().getId()) {
            CustomViewGroup.open("OptionsListViews", true, false);
            return true;
        }
        if (Id == R.id.buttonBack) {
            return true;
        }
        return false;
    }

    public void specialOpen() {
        System.hideSoftKeyboard(activity);

        try {
            Map.draw(GoogleMapHandler.getGoogleMap(), false);
        } catch (Exception e) {
            Log.write(e.getMessage());
        }
    }

    public void handleCameraMove() {
        Map.redrawPoints(GoogleMapHandler.getGoogleMap(), false);
    };

    public void specialClose() {
        Map.remove();
    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
