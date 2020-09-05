package com.serafimdmitrievstudio.yourway;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Serafim on 17.02.2018.
 */

public class CustomLinearLayout extends CustomView {

    public CustomLinearLayout(View layout) {
        super(layout);
    };

    public LinearLayout getView() {
        return (LinearLayout) view;
    }
}
