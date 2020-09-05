package com.serafimdmitrievstudio.yourway;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Serafim on 01.06.2017.
 */

public class CustomRelativeLayout extends CustomView {

    public CustomRelativeLayout(View layout) {
        super(layout);
    };

    public RelativeLayout getView() {
        return (RelativeLayout) view;
    }
}
