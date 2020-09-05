package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Serafim on 06.05.2017.
 */

public class CustomImageButton extends CustomView {
    public CustomImageButton(View view) {
        super(view);
    };

    public void setImages(Context context, final int normalImage, final int pressedImage) {
        ((ImageView)view).setImageResource(normalImage);
        ((ImageView)view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((ImageView)view).setImageResource(pressedImage);
                        break;
                    case MotionEvent.ACTION_UP:
                        ((ImageView)view).setImageResource(normalImage);
                        break;
                }
                return false;
            }
        });
    }

    void setBackgroundColor(Activity activity, int color) {
        view.setBackgroundColor(activity.getResources().getColor(color));
    }

    public ImageButton getView() {
        return (ImageButton) view;
    }
}
