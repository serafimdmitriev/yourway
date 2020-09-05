package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Serafim on 26.02.2018.
 */

class ViewsChoosePoint extends CustomViewGroup {

    CustomTextView setAsDepartureTextView;
    CustomTextView setAsDestinationTextView;
    CustomImageView CenterIcon;
    CustomImageButton ButtonIncreaseZoom;
    CustomImageButton ButtonDecreaseZoom;


    ViewsChoosePoint(final Activity activity) {
        super("ChoosePointViews");

        setAsDepartureTextView = (CustomTextView) CustomView.getViewById(R.id.setAsDepartureTextView);
        setAsDestinationTextView = (CustomTextView) CustomView.getViewById(R.id.setAsDestinationTextView);
        CenterIcon = (CustomImageView) CustomView.getViewById(R.id.centerIcon);
        ButtonIncreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonIncreaseZoom);
        ButtonDecreaseZoom = (CustomImageButton) CustomView.getViewById(R.id.buttonDecreaseZoom);

        setAsDepartureTextView.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationHandler.handleMarkerAndShowDeparture(activity);
            }
        });

        setAsDestinationTextView.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationHandler.handleMarkerAndShowDestination(activity);
            }
        });

        super.addView(CenterIcon);
        super.addView(ButtonIncreaseZoom);
        super.addView(ButtonDecreaseZoom);
        super.addView(setAsDepartureTextView);
        super.addView(setAsDestinationTextView);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {

        return false;
    }

    public void specialOpen() {
        CenterIcon.setImage(R.drawable.icon_point_marker);
    }

    public void handleCameraMove() {

    };

    public void specialClose() {

    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
