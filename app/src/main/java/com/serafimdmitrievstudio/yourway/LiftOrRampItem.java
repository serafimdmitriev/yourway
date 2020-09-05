package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Serafim on 26.08.2017.
 */

public class LiftOrRampItem {
    private View Item;

    private RelativeLayout LiftRelativeLayout;
    private ImageView LiftImageView;
    private TextView LiftTextView;
    private RelativeLayout RampRelativeLayout;
    private ImageView RampImageView;
    private TextView RampTextView;

    int EnabledColor;
    int DisabledColor;
    int EnabledTextColor;
    int DisabledTextColor;

    boolean LiftIsEnabled;

    public LiftOrRampItem(final Context context) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Item = inflater.inflate(R.layout.lift_or_ramp, null);

        LiftRelativeLayout = (RelativeLayout) Item.findViewById(R.id.LiftRelativeLayout);
        LiftImageView = (ImageView) Item.findViewById(R.id.imageViewLift);
        LiftTextView = (TextView) Item.findViewById(R.id.textViewLift);

        RampRelativeLayout = (RelativeLayout) Item.findViewById(R.id.RampRelativeLayout);
        RampImageView = (ImageView) Item.findViewById(R.id.imageViewRamp);
        RampTextView = (TextView) Item.findViewById(R.id.textViewRamp);

        LiftImageView.setImageResource(R.drawable.icon_lift);
        LiftImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        RampImageView.setImageResource(R.drawable.icon_ramp);
        RampImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        EnabledColor = context.getResources().getColor(R.color.colorWhite);
        DisabledColor = context.getResources().getColor(R.color.colorGray);
        EnabledTextColor = context.getResources().getColor(R.color.colorLightBlack);
        DisabledTextColor = context.getResources().getColor(R.color.colorLightGrayTransparent);

    }

    public RelativeLayout getLiftRelativeLayout() {
        return LiftRelativeLayout;
    }
    public RelativeLayout getRampRelativeLayout() {
        return RampRelativeLayout;
    }

    public void enableLift() {
        LiftRelativeLayout.setBackgroundColor(EnabledColor);
        LiftImageView.setImageAlpha(255);
        LiftTextView.setTextColor(EnabledTextColor);

        RampRelativeLayout.setBackgroundColor(DisabledColor);
        RampImageView.setImageAlpha(128);
        RampTextView.setTextColor(DisabledTextColor);

        LiftIsEnabled = true;
    }

    public void enableRamp() {
        RampRelativeLayout.setBackgroundColor(EnabledColor);
        RampImageView.setImageAlpha(255);
        RampTextView.setTextColor(EnabledTextColor);

        LiftRelativeLayout.setBackgroundColor(DisabledColor);
        LiftImageView.setImageAlpha(128);
        LiftTextView.setTextColor(DisabledTextColor);

        LiftIsEnabled = false;
    }

    public boolean isLiftEnabled() {
        return LiftIsEnabled;
    }

    public boolean isRampEnabled() {
        return !LiftIsEnabled;
    }

    public View getView() {
        return Item;
    }


}
