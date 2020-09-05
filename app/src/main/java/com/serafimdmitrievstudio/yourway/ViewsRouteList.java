package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Serafim on 03.06.2017.
 */

public class ViewsRouteList extends CustomViewGroup {
    CustomRelativeLayout BackgroundBlackout;
    CustomScrollView RouteListView;
    CustomImageButton ButtonCloseList;

    public ViewsRouteList() {
        super("RouteListViews");

        BackgroundBlackout = (CustomRelativeLayout) CustomView.getViewById(R.id.RelativeLayoutBlackout);
        RouteListView = (CustomScrollView) CustomView.getViewById(R.id.ScrollViewRoute);
        ButtonCloseList = (CustomImageButton) CustomView.getViewById(R.id.buttonCloseList);

        super.addView(BackgroundBlackout);
        super.addView(RouteListView);
        super.addView(ButtonCloseList);
    }

    public CustomScrollView getListView() {
        return RouteListView;
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == BackgroundBlackout.getView().getId()) {
            CustomViewGroup.open("RouteViews", true, false);
            return true;
        }
        if (Id == ButtonCloseList.getView().getId()) {
            CustomViewGroup.open("RouteViews", true, false);
            return true;
        }
        if (Id == R.id.buttonBack) {
                GoogleMapHandler.getGoogleMap().clear();
                LocationHandler.restoreLastDepartureAndDestination();
                CustomViewGroup.back();
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
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RouteListView.getView().setBackground(context.getResources().getDrawable(R.drawable.scroll_view_background_left_horizontal));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            RouteListView.getView().setBackground(context.getResources().getDrawable(R.drawable.scroll_view_background_left));
        }
    };
}
