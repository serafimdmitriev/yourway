package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Serafim on 03.06.2017.
 */

public class ViewsRoute extends CustomViewGroup {
    CustomImageButton ButtonClose;
    CustomImageButton ButtonOpenRouteList;

    public ViewsRoute() {
        super("RouteViews");

        ButtonClose = (CustomImageButton) CustomView.getViewById(R.id.buttonClose);
        ButtonOpenRouteList = (CustomImageButton) CustomView.getViewById(R.id.buttonOpenRouteList);

        super.addView(ButtonClose);
        super.addView(ButtonOpenRouteList);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == ButtonClose.getView().getId()) {
            LocationHandler.removeDeparture();
            LocationHandler.removeDestination();
            GoogleMapHandler.clear();
            return true;
        }
        return false;
    }

    public void specialOpen() {

    }

    public void handleCameraMove() {

    };

    public void specialClose() {

    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
