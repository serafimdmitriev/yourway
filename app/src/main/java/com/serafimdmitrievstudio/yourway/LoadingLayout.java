package com.serafimdmitrievstudio.yourway;


import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingLayout {
    private static RelativeLayout BlackoutLoadingLayout;
    private static RelativeLayout LoadingLayout;
    private static TextView LoadingTextView;

    private static Animation LoadingAnimation;
    private static Animation OpeningAnimation;
    private static Animation ClosingAnimation;

    static void initialize(Activity activity) {
        BlackoutLoadingLayout = (RelativeLayout) activity.findViewById(R.id.RelativeLayoutBlackoutLoading);
        LoadingLayout = (RelativeLayout) activity.findViewById(R.id.RelativeLayoutLoading);
        LoadingTextView = (TextView) activity.findViewById(R.id.TextViewLoading);

        LoadingAnimation = AnimationUtils.loadAnimation(activity, R.anim.infinite_loading);
        LoadingAnimation.setRepeatCount(Animation.INFINITE);
        LoadingAnimation.setRepeatMode(Animation.REVERSE);
        OpeningAnimation = AnimationUtils.loadAnimation(activity, R.anim.appear_from_void);
        OpeningAnimation.setDuration(100);
        ClosingAnimation = AnimationUtils.loadAnimation(activity, R.anim.remove_to_void);
        ClosingAnimation.setDuration(100);
    }

    static void setText(String text) {
        LoadingTextView.setText(text);
    }

    static void open() {
        BlackoutLoadingLayout.setVisibility(View.VISIBLE);
        BlackoutLoadingLayout.startAnimation(OpeningAnimation);
        LoadingLayout.startAnimation(LoadingAnimation);
    }

    static void close() {
        ClosingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                BlackoutLoadingLayout.setVisibility(View.INVISIBLE);
                LoadingLayout.clearAnimation();
                LoadingAnimation.cancel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        BlackoutLoadingLayout.startAnimation(ClosingAnimation);
    }
}
