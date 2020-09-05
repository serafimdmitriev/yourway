package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Serafim on 06.06.2017.
 */

public class TipLayout{
    private static RelativeLayout relativeLayout;
    private static TextView textView;
    private static Animation OpeningAnimation;
    private static Animation ClosingAnimation;
    private static Animation OpeningAndClosingAnimation;

    static void initialize(Activity activity){
        relativeLayout = (RelativeLayout) activity.findViewById(R.id.RelativeLayoutTip);
        textView = (TextView) activity.findViewById(R.id.TextViewTip);
        OpeningAnimation = AnimationUtils.loadAnimation(activity, R.anim.appear_leftward);
        ClosingAnimation = AnimationUtils.loadAnimation(activity, R.anim.remove_rightward);
        OpeningAndClosingAnimation = AnimationUtils.loadAnimation(activity, R.anim.appear_and_remove_tip);
    }

    private static void openForTime() {
        OpeningAndClosingAnimation.setAnimationListener(new Animation.AnimationListener() {
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
        relativeLayout.startAnimation(OpeningAndClosingAnimation);
    }

    static void openForTime(final String text) {
        if (relativeLayout.getVisibility() == View.VISIBLE ) {
            ClosingAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    relativeLayout.setVisibility(View.INVISIBLE);
                    textView.setText(text);
                    openForTime();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            relativeLayout.startAnimation(ClosingAnimation);
        } else {
            textView.setText(text);
            openForTime();
        }
    }

    private static void open() {
        if (relativeLayout.getVisibility() == View.INVISIBLE ) {
            OpeningAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    relativeLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            relativeLayout.startAnimation(OpeningAnimation);
        }
    }

    static void open(final String text) {
        if (relativeLayout.getVisibility() == View.VISIBLE ) {
            ClosingAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    relativeLayout.setVisibility(View.INVISIBLE);
                    textView.setText(text);
                    open();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            relativeLayout.startAnimation(ClosingAnimation);
        } else {
            textView.setText(text);
            open();
        }

    }

    static void close() {
        if (relativeLayout.getVisibility() == View.VISIBLE ) {
            ClosingAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    relativeLayout.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            relativeLayout.startAnimation(ClosingAnimation);
        }
    }
}
