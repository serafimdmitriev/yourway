package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

/**
 * Created by Serafim on 05.06.2017.
 */

public class ViewsEditorList extends CustomViewGroup {
    CustomRelativeLayout BackgroundBlackout;
    CustomScrollView EditorListView;
    CustomImageButton ButtonBack;

    public ViewsEditorList(final Activity activity) {
        super("EditorListViews");

        BackgroundBlackout = (CustomRelativeLayout) CustomView.getViewById(R.id.RelativeLayoutBlackout);
        EditorListView = (CustomScrollView) CustomView.getViewById(R.id.ScrollViewEditor);
        ButtonBack = (CustomImageButton) CustomView.getViewById(R.id.buttonBack);

        final ListTitle EditorTitle = new ListTitle("Editor");
        EditorListView.addItem(EditorTitle.getView(activity));

        final CustomScrollViewDefaultItem AddRoadItem = new CustomScrollViewDefaultItem(activity, StringHandler.getString(R.string.add_new_road), R.drawable.icon_road);
        EditorListView.addItem(AddRoadItem.getView());
        AddRoadItem.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map.enableRoadDrawingMode();
                CustomViewGroup.open("AddSegmentViews", true, false);
            }
        });

        final CustomScrollViewDefaultItem AddGroundPassageItem = new CustomScrollViewDefaultItem(activity, StringHandler.getString(R.string.add_new_ground_passage), R.drawable.icon_zebra);
        EditorListView.addItem(AddGroundPassageItem.getView());
        AddGroundPassageItem.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map.enableGroundPassageDrawingMode();
                CustomViewGroup.open("AddSegmentViews", true, false);
            }
        });

        final CustomScrollViewDefaultItem AddUndergroundPassageItem = new CustomScrollViewDefaultItem(activity, StringHandler.getString(R.string.add_new_underground_passage), R.drawable.icon_go_down);
        EditorListView.addItem(AddUndergroundPassageItem.getView());
        AddUndergroundPassageItem.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map.enableUndergroundPassageDrawingMode();
                CustomViewGroup.open("AddLiftOrRampViews", true, false);
            }
        });

        final CustomScrollViewDefaultItem AddOverheadPassageItem = new CustomScrollViewDefaultItem(activity, StringHandler.getString(R.string.add_new_overhead_passage), R.drawable.icon_go_up);
        EditorListView.addItem(AddOverheadPassageItem.getView());
        AddOverheadPassageItem.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map.enableOverheadPassageDrawingMode();
                CustomViewGroup.open("AddLiftOrRampViews", true, false);
            }
        });

        super.addView(BackgroundBlackout);
        super.addView(EditorListView);
        super.addView(ButtonBack);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == BackgroundBlackout.getView().getId()) {
            CustomViewGroup.back();
            //CustomViewGroup.open("OptionsListViews", true, false);
            return true;
        }
        if (Id == ButtonBack.getView().getId()) {
            CustomViewGroup.back();
            //CustomViewGroup.open("OptionsListViews", true, false);
            return true;
        }
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
            EditorListView.getView().setBackground(context.getResources().getDrawable(R.drawable.scroll_view_background_right_horizontal));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            EditorListView.getView().setBackground(context.getResources().getDrawable(R.drawable.scroll_view_background_right));
        }
    };
}
