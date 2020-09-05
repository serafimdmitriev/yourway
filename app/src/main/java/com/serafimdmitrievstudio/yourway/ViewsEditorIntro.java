package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Button;

/**
 * Created by Serafim on 03.03.2018.
 */

public class ViewsEditorIntro extends CustomViewGroup {

    CustomLinearLayout EditorIntroLinearLayout;

    ViewsEditorIntro(Activity activity) {
        super("EditorIntroViews");

        EditorIntroLinearLayout = (CustomLinearLayout) CustomView.getViewById(R.id.editorIntroLinearLayout);

        Button NextButton = (Button)activity.findViewById(R.id.editorIntroNextButton);
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesHandler.setValue(SharedPreferencesHandler.A_P_EDITORUSED, true);
                CustomViewGroup.open("EditorListViews", true, false);
            }
        });

        super.addView(EditorIntroLinearLayout);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {

        return false;
    }

    public void specialOpen() {

    }

    public void handleCameraMove() {

    };

    public void specialClose() {

    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            EditorIntroLinearLayout.getView().setBackground(context.getResources().getDrawable(R.drawable.background_white_horizontal));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            EditorIntroLinearLayout.getView().setBackground(context.getResources().getDrawable(R.drawable.background_white));
        }
    };
}
