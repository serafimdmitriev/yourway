package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Serafim on 01.06.2017.
 */

public class ListTitle {
    String text;

    public ListTitle(String text) {
        this.text = text;
    }

    View getView(Context context) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View title =  inflater.inflate(R.layout.title, null);

        TextView TitleView = (TextView) title.findViewById(R.id.titleText);
        TitleView.setText(text);

        return title;
    }
}
