package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Serafim on 11.05.2017.
 */

public class ErrorLayout {
    private static RelativeLayout relativeLayout;
    private static TextView textView;
    private static Animation animation;

    static void initialize(Activity activity) {
        relativeLayout = (RelativeLayout) activity.findViewById(R.id.errorLayout);
        textView = (TextView) activity.findViewById(R.id.errorText);
        animation = AnimationUtils.loadAnimation(activity, R.anim.appear_and_remove_error);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                relativeLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    static void show(String textError){
        textView.setText(textError);
        relativeLayout.startAnimation(animation);
    }
}
