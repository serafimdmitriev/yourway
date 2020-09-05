package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Serafim on 02.06.2017.
 */

public class CustomScrollViewDefaultItem {
    View Item;
    TextView Title;

    public CustomScrollViewDefaultItem(Context context, String text, int iconId) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Item =  inflater.inflate(R.layout.default_scrollview_row, null);

        Title = (TextView) Item.findViewById(R.id.textDefaultListItem);
        Title.setText(text);

        ImageView icon = (ImageView) Item.findViewById(R.id.iconDefaultListItem);
        icon.setImageResource(iconId);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    View getView() {
        return Item;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        Item.setOnClickListener(listener);
    }

    void setText(String text) {
        Title.setText(text);
    }

    void setTextTypeface(int style) {
        Title.setTypeface(Title.getTypeface(), style);
    }
}
