package com.serafimdmitrievstudio.yourway;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Serafim on 18.02.2018.
 */

public class CustomTextView extends CustomView {
    public CustomTextView(View view) {
        super(view);
    };

    public TextView getView() {
        return (TextView) view;
    }
}
