package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Serafim on 17.02.2018.
 */

public class ViewsSetRouteParams extends CustomViewGroup {

    CustomLinearLayout RouteParamsLayout;
    CustomRelativeLayout BuildRouteLayout;

    ImageButton BuildRouteButton;

    ImageButton ElderlyPersonButton ;
    ImageButton WheelchairButton;
    ImageButton ElectricWheelchairButton;
    ImageButton PedestrianButton;

    int WhoseIsThisRoute = RouteBelonging.Wheelchair;

    CustomEditText DepartureEditText;
    CustomEditText DestinationEditText;

    CustomRelativeLayout BackgroundBlackout;

    public ViewsSetRouteParams(final Activity activity) {
        super("SetRouteParamsViews");

        RouteParamsLayout = (CustomLinearLayout) CustomView.getViewById(R.id.LinearLayoutParams);
        BuildRouteLayout = (CustomRelativeLayout) CustomView.getViewById(R.id.buildRouteRelativeLayout);
        DepartureEditText = (CustomEditText) CustomView.getViewById(R.id.editTextDeparture);
        DestinationEditText = (CustomEditText) CustomView.getViewById(R.id.editTextDestination);
        BackgroundBlackout = (CustomRelativeLayout) CustomView.getViewById(R.id.RelativeLayoutBlackout);

        super.addView(BackgroundBlackout);
        super.addView(DepartureEditText);
        super.addView(DestinationEditText);
        super.addView(RouteParamsLayout);
        super.addView(BuildRouteLayout);

        BuildRouteButton = (ImageButton) activity.findViewById(R.id.buildRouteButton);
        BuildRouteButton.setImageResource(R.drawable.icon_search);

        ElderlyPersonButton = (ImageButton) activity.findViewById(R.id.elderlyPersonButton);
        ElderlyPersonButton.setImageResource(R.drawable.icon_elderly_person);
        WheelchairButton = (ImageButton) activity.findViewById(R.id.wheelchairButton);
        WheelchairButton.setImageResource(R.drawable.icon_wheelchair);
        WheelchairButton.setBackgroundColor(activity.getResources().getColor(R.color.colorLightGray));
        ElectricWheelchairButton = (ImageButton)activity.findViewById(R.id.electricWheelchairButton);
        ElectricWheelchairButton.setImageResource(R.drawable.icon_electric_wheelchair);
        PedestrianButton = (ImageButton) activity.findViewById(R.id.pedestrianButton);
        PedestrianButton.setImageResource(R.drawable.icon_pedestrian);

        ElderlyPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableAllButtons(activity);
                ElderlyPersonButton.setBackgroundColor(activity.getResources().getColor(R.color.colorLightGray));
                WhoseIsThisRoute = RouteBelonging.ElderlyPerson;
            }
        });
        WheelchairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableAllButtons(activity);
                WheelchairButton.setBackgroundColor(activity.getResources().getColor(R.color.colorLightGray));
                WhoseIsThisRoute = RouteBelonging.Wheelchair;
            }
        });
        ElectricWheelchairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableAllButtons(activity);
                ElectricWheelchairButton.setBackgroundColor(activity.getResources().getColor(R.color.colorLightGray));
                WhoseIsThisRoute = RouteBelonging.ElectricWheelchair;
            }
        });
        PedestrianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableAllButtons(activity);
                PedestrianButton.setBackgroundColor(activity.getResources().getColor(R.color.colorLightGray));
                WhoseIsThisRoute = RouteBelonging.Pedestrian;
            }
        });

        BuildRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationHandler.setBelongingOfThisRoute(WhoseIsThisRoute);
                LocationHandler.BuildRouteAsyncTask buildRouteAsyncTask = new LocationHandler.BuildRouteAsyncTask();
                buildRouteAsyncTask.execute(activity);
            }
        });

        BuildRouteButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((ImageView)view).setImageResource(R.drawable.icon_search_pressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        ((ImageView)view).setImageResource(R.drawable.icon_search);
                        break;
                }
                return false;
            }
        });
    }

    private void disableAllButtons(Activity activity) {
        ElderlyPersonButton.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        WheelchairButton.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        ElectricWheelchairButton.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        PedestrianButton.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {

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
