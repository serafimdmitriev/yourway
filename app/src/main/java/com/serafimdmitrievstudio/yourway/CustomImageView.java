package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Serafim on 05.06.2017.
 */

public class CustomImageView extends CustomView {
    Animation OpeningAnimation;
    Animation ClosingAnimation;

    public CustomImageView(final View view) {
        super(view);

        OpeningAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.appear_leftward);
        ClosingAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.remove_leftward);

        OpeningAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    };

    public void setImage(int defaultImage) {
        ((ImageView)view).setImageResource(defaultImage);
        ((ImageView)view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    public void changeImageWithAnimation(final int defaultImage) {
        ClosingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                ((ImageView)view).setImageResource(defaultImage);
                ((ImageView)view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                view.startAnimation(OpeningAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(ClosingAnimation);
    }

    public ImageView getView() {
        return (ImageView) view;
    }
}
