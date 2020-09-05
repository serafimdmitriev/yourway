package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


class RouteSplash {
    static final int Warning = 1;
    static final int OnlyText = 4;
    static final int TitleAndText = 5;
    static final int TitleAndTwoTexts = 6;

    private TextView title = null;
    private TextView firstText = null;
    private TextView secondText = null;
    private TextView additionalText = null;
    private ImageView icon = null;
    private RelativeLayout qualitySignal;

    private View route_splash = null;

    RouteSplash (Context context, int type){

        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (type) {
            case Warning: {
                route_splash =  inflater.inflate(R.layout.route_warning, null);

                firstText = (TextView) route_splash.findViewById(R.id.textTextWarning);
                icon = (ImageView) route_splash.findViewById(R.id.imgIconWarning);

                this.icon.setImageResource(R.drawable.icon_warning);
                this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } break;

            case TitleAndTwoTexts: {
                route_splash =  inflater.inflate(R.layout.route_caption, null);

                title = (TextView) route_splash.findViewById(R.id.textCaptionTitle);
                firstText = (TextView) route_splash.findViewById(R.id.textCaptionFirstText);
                secondText = (TextView) route_splash.findViewById(R.id.textCaptionSecondText);
            } break;

            case TitleAndText: {
                route_splash =  inflater.inflate(R.layout.route_item, null);

                title = (TextView) route_splash.findViewById(R.id.textTitleItem);
                firstText = (TextView) route_splash.findViewById(R.id.textTextItem);
                additionalText = (TextView) route_splash.findViewById(R.id.additionTextItem);
                icon = (ImageView) route_splash.findViewById(R.id.imgIconItem);
                ImageView iconBackground = (ImageView) route_splash.findViewById(R.id.imgIconItemBackground);
                qualitySignal = (RelativeLayout) route_splash.findViewById(R.id.relativeLayoutItemQuality);

                title.setVisibility(View.INVISIBLE);
                icon.setVisibility(View.INVISIBLE);

                iconBackground.setImageResource(R.drawable.icon_road_vertical);
                iconBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } break;

            case OnlyText: {
                route_splash =  inflater.inflate(R.layout.route_item_verysmall, null);

                firstText = (TextView) route_splash.findViewById(R.id.textTextItemVerysmall);
                icon = (ImageView) route_splash.findViewById(R.id.imgIconItemVerysmall);
                ImageView iconBackground = (ImageView) route_splash.findViewById(R.id.imgIconItemVerysmallBackground);
                qualitySignal = (RelativeLayout) route_splash.findViewById(R.id.relativeLayoutItemVerysmallQuality);

                firstText.setVisibility(View.INVISIBLE);
                icon.setVisibility(View.INVISIBLE);

                iconBackground.setImageResource(R.drawable.icon_road_vertical);
                iconBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } break;
        }
    }

    TextView getAdditionalTextView() {
        return additionalText;
    }

    TextView getFirstTextView() {
        return firstText;
    }

    View getView() {
        if (route_splash == null) {
            Log.write("route_splash = null");
        }
        return route_splash;
    }

    void setTitle(String title) {
        if (this.title != null) {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(title);
        }
    }

    void setFirstText(String text) {
        if (this.firstText != null) {
            this.firstText.setVisibility(View.VISIBLE);
            this.firstText.setText(text);
        }
    }

    void setSecondText(String text) {
        if (this.secondText != null) {
            this.secondText.setVisibility(View.VISIBLE);
            this.secondText.setText(text);
        }
    }

    void setIcon(Integer iconId) {
        if (iconId != null && icon != null) {
            this.icon.setVisibility(View.VISIBLE);
            this.icon.setImageResource(iconId);
            this.icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

    void setQualitySignal(Context context, Integer color) {
        if (color != null && qualitySignal != null) {
            qualitySignal.setBackgroundColor(context.getResources().getColor(color));
        }
    }
}
