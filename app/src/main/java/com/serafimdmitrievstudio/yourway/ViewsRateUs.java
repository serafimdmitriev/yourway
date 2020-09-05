package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

/**
 * Created by Serafim on 03.03.2018.
 */

public class ViewsRateUs extends CustomViewGroup{

    CustomRelativeLayout RateUsRelativeLayout;
    CustomRelativeLayout BackgroundBlackout;

    ViewsRateUs(final Activity activity) {
        super("RateUsViews");

        BackgroundBlackout = (CustomRelativeLayout) CustomView.getViewById(R.id.RelativeLayoutBlackout);
        RateUsRelativeLayout = (CustomRelativeLayout) CustomView.getViewById(R.id.rateUsRelativeLayout);

        Button NotNowButton = (Button)activity.findViewById(R.id.notNowRateUsButton);
        NotNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesHandler.setValue(SharedPreferencesHandler.A_P_TIMEOFDONTASKABOUTGPS, java.lang.System.currentTimeMillis());
                CustomViewGroup.back();
            }
        });

        Button RateNowButton = (Button)activity.findViewById(R.id.rateNowRateUsButton);
        RateNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.back();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.serafimdmitrievstudio.yourway"));
                activity.startActivity(intent);

                SharedPreferencesHandler.setValue(SharedPreferencesHandler.A_P_DONTASKABOUTRATE, true);
            }
        });

        super.addView(BackgroundBlackout);
        super.addView(RateUsRelativeLayout);
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
