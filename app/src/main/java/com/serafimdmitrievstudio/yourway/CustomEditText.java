package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;

/**
 * Created by Serafim on 06.05.2017.
 */

public class CustomEditText extends CustomView{

    public CustomEditText(View view) {
        super(view);
    };
    public EditText getView() {
        return (EditText) view;
    }
}
