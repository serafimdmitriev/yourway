package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

/**
 * Created by Serafim on 03.06.2017.
 */

public class ViewsOptionsList extends CustomViewGroup {
    CustomRelativeLayout BackgroundBlackout;
    CustomScrollView OptionsListView;
    CustomImageButton ButtonCloseList;

    public ViewsOptionsList(final Activity activity) {
        super("OptionsListViews");

        BackgroundBlackout = (CustomRelativeLayout) CustomView.getViewById(R.id.RelativeLayoutBlackout);
        OptionsListView = (CustomScrollView) CustomView.getViewById(R.id.ScrollViewOptions);
        ButtonCloseList = (CustomImageButton) CustomView.getViewById(R.id.buttonCloseList);

        final ListTitle OptionsTitle = new ListTitle(StringHandler.getString(R.string.options));
        OptionsListView.addItem(OptionsTitle.getView(activity));
        final CustomScrollViewDefaultItem EditorItem = new CustomScrollViewDefaultItem(activity, StringHandler.getString(R.string.editor), R.drawable.icon_pencil);
        OptionsListView.addItem(EditorItem.getView());
        EditorItem.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreferencesHandler.mSettings.getBoolean(SharedPreferencesHandler.A_P_EDITORUSED, false)) {
                    CustomViewGroup.open("EditorListViews", true, false);
                } else {
                    CustomViewGroup.open("EditorIntroViews", true, false);
                }
            }
        });
        final CustomScrollViewDefaultItem WriteUsItem = new CustomScrollViewDefaultItem(activity, StringHandler.getString(R.string.write_us), R.drawable.icon_message);
        OptionsListView.addItem(WriteUsItem.getView());
        WriteUsItem.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.open("SendMessageViews", true, false);
            }
        });
        final CustomScrollViewDefaultItem AboutItem = new CustomScrollViewDefaultItem(activity, StringHandler.getString(R.string.about), R.drawable.icon_question);
        OptionsListView.addItem(AboutItem.getView());
        AboutItem.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.open("AboutViews", true, false);
            }
        });

        super.addView(BackgroundBlackout);
        super.addView(OptionsListView);
        super.addView(ButtonCloseList);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == BackgroundBlackout.getView().getId()) {
            CustomViewGroup.back();
            //CustomViewGroup.open("MainViews", true, false);
            return true;
        }
        if (Id == ButtonCloseList.getView().getId()) {
            CustomViewGroup.back();
            //CustomViewGroup.open("MainViews", true, false);
            return true;
        }
        /*
        if (Id == CustomView.getViewById(R.id.buttonBack).getView().getId()) {
            CustomViewGroup.back();
            //CustomViewGroup.open("MainViews", true, false);
            return true;
        }
        */
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
            OptionsListView.getView().setBackground(context.getResources().getDrawable(R.drawable.scroll_view_background_left_horizontal));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            OptionsListView.getView().setBackground(context.getResources().getDrawable(R.drawable.scroll_view_background_left));
        }
    };

}
