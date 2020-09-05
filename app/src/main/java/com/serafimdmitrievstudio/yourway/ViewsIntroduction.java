package com.serafimdmitrievstudio.yourway;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Serafim on 03.03.2018.
 */

public class ViewsIntroduction extends CustomViewGroup {
    Activity activity;

    private CustomRelativeLayout IntroductionRelativeLayout;
    private LinearLayout currentLinearLayout;
    private Animation OpeningAnimation;
    private Animation ClosingAnimation;

    final private LinearLayout introductionLayout1;
    final private LinearLayout introductionLayout2;
    final private LinearLayout introductionLayout3;
    final private LinearLayout introductionLayout4;
    final private LinearLayout introductionLayout5;

    ViewsIntroduction(Activity activity) {
            super("IntroductionViews");

        this.activity = activity;

        OpeningAnimation = AnimationUtils.loadAnimation(activity, R.anim.appear_leftward);
        ClosingAnimation = AnimationUtils.loadAnimation(activity, R.anim.remove_rightward);

        IntroductionRelativeLayout = (CustomRelativeLayout) CustomView.getViewById(R.id.introductionRelativeLayout);

        introductionLayout1 = (LinearLayout) activity.findViewById(R.id.introduction1);
        introductionLayout2 = (LinearLayout) activity.findViewById(R.id.introduction2);
        introductionLayout3 = (LinearLayout) activity.findViewById(R.id.introduction3);
        introductionLayout4 = (LinearLayout) activity.findViewById(R.id.introduction4);
        introductionLayout5 = (LinearLayout) activity.findViewById(R.id.introduction5);
        Button nextButton1 = (Button) activity.findViewById(R.id.introduction1NextButton);
        Button nextButton2 = (Button) activity.findViewById(R.id.introduction2NextButton);
        Button nextButton3 = (Button) activity.findViewById(R.id.introduction3NextButton);
        Button nextButton4 = (Button) activity.findViewById(R.id.introduction4NextButton);
        Button nextButton5 = (Button) activity.findViewById(R.id.introduction5NextButton);

        nextButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIntroductionLayout(introductionLayout2);
            }
        });
        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIntroductionLayout(introductionLayout3);
            }
        });
        nextButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIntroductionLayout(introductionLayout4);
            }
        });
        nextButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIntroductionLayout(introductionLayout5);
            }
        });
        nextButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.back();
                SharedPreferencesHandler.setValue(SharedPreferencesHandler.A_P_FIRSTUSING, false);
            }
        });

        ImageView mapImageView = (ImageView)activity.findViewById(R.id.introduction2MapImageView);
        mapImageView.setImageResource(R.drawable.icon_world_map);
        mapImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        super.addView(IntroductionRelativeLayout);
    }

    public void handleTouch(int Id) {
    }

    private void openIntroductionLayout(final LinearLayout introductionLayout) {
        OpeningAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                introductionLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        introductionLayout.startAnimation(OpeningAnimation);
        currentLinearLayout = introductionLayout;
    }

    private void closeIntroductionLayout(final LinearLayout introductionLayout) {
        ClosingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                introductionLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        introductionLayout.startAnimation(ClosingAnimation);
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == R.id.buttonBack) {
            if (currentLinearLayout == introductionLayout5) {
                closeIntroductionLayout(introductionLayout5);
                currentLinearLayout = introductionLayout4;
                return true;
            }
            if (currentLinearLayout == introductionLayout4) {
                closeIntroductionLayout(introductionLayout4);
                currentLinearLayout = introductionLayout3;
                return true;
            }
            if (currentLinearLayout == introductionLayout3) {
                closeIntroductionLayout(introductionLayout3);
                currentLinearLayout = introductionLayout2;
                return true;
            }
            if (currentLinearLayout == introductionLayout2) {
                closeIntroductionLayout(introductionLayout2);
                currentLinearLayout = introductionLayout1;
                return true;
            }
        }

        return false;
    }

    public void specialOpen() {

    }

    public void handleCameraMove() {

    };

    public void specialClose() {
        System.requestGPSPermission(activity);
    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            introductionLayout1.setBackground(context.getResources().getDrawable(R.drawable.background_white_horizontal));
            introductionLayout2.setBackground(context.getResources().getDrawable(R.drawable.background_blue_horizontal));
            introductionLayout3.setBackground(context.getResources().getDrawable(R.drawable.background_red_horizontal));
            introductionLayout4.setBackground(context.getResources().getDrawable(R.drawable.background_yellow_horizontal));
            introductionLayout5.setBackground(context.getResources().getDrawable(R.drawable.background_green_horizontal));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            introductionLayout1.setBackground(context.getResources().getDrawable(R.drawable.background_white));
            introductionLayout2.setBackground(context.getResources().getDrawable(R.drawable.background_blue));
            introductionLayout3.setBackground(context.getResources().getDrawable(R.drawable.background_red));
            introductionLayout4.setBackground(context.getResources().getDrawable(R.drawable.background_yellow));
            introductionLayout5.setBackground(context.getResources().getDrawable(R.drawable.background_green));
        }
    };
}
