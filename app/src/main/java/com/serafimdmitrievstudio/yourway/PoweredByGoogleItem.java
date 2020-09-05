package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Serafim on 07.09.2017.
 */

public class PoweredByGoogleItem {
    View powered_by_google_item;

    PoweredByGoogleItem(Context context) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        powered_by_google_item =  inflater.inflate(R.layout.powered_by_google_item, null);
    }

    View getView() {
        return powered_by_google_item;
    }
}
