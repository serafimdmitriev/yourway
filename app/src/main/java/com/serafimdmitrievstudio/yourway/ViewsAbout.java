package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Serafim on 03.03.2018.
 */

public class ViewsAbout extends CustomViewGroup {

    CustomLinearLayout AboutLinearLayout;

    ViewsAbout(Activity activity) {
        super("AboutViews");

        AboutLinearLayout = (CustomLinearLayout) CustomView.getViewById(R.id.aboutLinearLayout);

        super.addView(AboutLinearLayout);
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
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            AboutLinearLayout.getView().setBackground(context.getResources().getDrawable(R.drawable.background_white_horizontal));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            AboutLinearLayout.getView().setBackground(context.getResources().getDrawable(R.drawable.background_white));
        }
    };
}
