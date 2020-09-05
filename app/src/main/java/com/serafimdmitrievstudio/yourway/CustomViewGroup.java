package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Serafim on 07.05.2017.
 */

public abstract class CustomViewGroup {
    private static String openedViewGroupName = null;
    private static HashMap<String, CustomViewGroup> ViewGroupsList;

    private static ArrayList<String> ViewGroupsStack;

    static void setOpenedViewGroup(String name) {
        openedViewGroupName = name;
    }

    static boolean back() {
        if (ViewGroupsStack.size() > 1) {
            return open(ViewGroupsStack.get(ViewGroupsStack.size()-2), false, false);
        }
        return false;
    }

    static void resume() {
        if (ViewGroupsStack.size() == 0) {
            openedViewGroupName = "MainViews";
            ViewGroupsStack.add(openedViewGroupName);
        } else {
            openTheLastViewGroupAgain();
            //open(ViewGroupsStack.get(ViewGroupsStack.size()-1), null, false);
        }
    }

    static boolean openTheLastViewGroupAgain() {
        return open(openedViewGroupName, null, true);
    }


    public static boolean open(String Name, Boolean forward, boolean ignoreCoincidence) {
        if (getOpenedViewGroup().isOpened() && (!getOpenedViewGroup().Name.equals(Name) || ignoreCoincidence)) {
            try {

                Log.write(getOpenedViewGroup().getName() + "are closing");
                Log.write(Name + "are opening");
                Log.write("ViewGroupsStack.size: " + Integer.toString(ViewGroupsStack.size()));

                getOpenedViewGroup().specialClose();

                if (GoogleMapHandler.getGoogleMap() != null) GoogleMapHandler.getGoogleMap()
                        .setPadding(getViewGroupByName(Name).getMapPadding().left,
                        getViewGroupByName(Name).getMapPadding().top,
                        getViewGroupByName(Name).getMapPadding().right,
                        getViewGroupByName(Name).getMapPadding().bottom);

                getViewGroupByName(Name).specialOpen();

                ArrayList<CustomView> ViewsToClose = new ArrayList<>();
                ArrayList<CustomView> ViewsToOpen = new ArrayList<>();

                for (int i = 0; i < CustomView.getAllViews().size(); i++) {
                    if (getViewGroupByName(Name).ViewGroup.contains(CustomView.getAllViews().get(i)) &&
                            CustomView.getAllViews().get(i).getVisibility() == View.INVISIBLE) {
                        ViewsToOpen.add(CustomView.getAllViews().get(i));
                    }
                    if (!getViewGroupByName(Name).ViewGroup.contains(CustomView.getAllViews().get(i)) &&
                            CustomView.getAllViews().get(i).getVisibility() == View.VISIBLE) {
                        ViewsToClose.add(CustomView.getAllViews().get(i));
                    }
                }

                for (int i = 0; i < ViewsToOpen.size(); i++) {
                    ViewsToOpen.get(i).open();
                }
                for (int i = 0; i < ViewsToClose.size(); i++) {
                    ViewsToClose.get(i).close();
                }

                openedViewGroupName = Name;

                if (forward != null) {
                    if (forward) {
                        ViewGroupsStack.add(openedViewGroupName);
                    } else {
                        ViewGroupsStack.remove(ViewGroupsStack.size() - 1);
                    }
                }

                return true;
            } catch (Exception e) {
                Log.write(e.getMessage());
                ErrorLayout.show(StringHandler.getString(R.string.error_wrong_viewgroup_name));
                return false;
            }
        }

        return false;
    }

    static CustomViewGroup getOpenedViewGroup() {
        return ViewGroupsList.get(openedViewGroupName);
    }

    static CustomViewGroup getViewGroupByName(String Name) {
        return ViewGroupsList.get(Name);
    }

    private ArrayList<CustomView> ViewGroup;
    private String Name;
    private Rect MapPadding;

    CustomViewGroup(String Name){
        if (ViewGroupsStack == null) {
            ViewGroupsStack = new ArrayList<>();
        }

        if (ViewGroupsList == null) {
            ViewGroupsList = new HashMap<>();
        }
        ViewGroupsList.put(Name, this);

        this.Name = Name;

        if (openedViewGroupName == null) {
            openedViewGroupName = Name;
        }

        MapPadding = new Rect();
        MapPadding.set(0, 0, 0, 0);
    }

    public void setMapPadding(int left, int top, int right, int bottom) {
        MapPadding.set(left, top, right, bottom);
    }

    public Rect getMapPadding() {
        return MapPadding;
    }

    void addView(CustomView view) {
        if (ViewGroup == null) {
            ViewGroup = new ArrayList<>();
        }
        ViewGroup.add(view);
    }

    public boolean isOpened() {
        for (int i = 0 ; i < ViewGroup.size(); i++) {
            if (ViewGroup.get(i).isOpeningOrClosing()) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return openedViewGroupName;
    }

    public boolean isVisible() {
        for (CustomView view : ViewGroup) {
            if (view.getView().getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return false;
    }

    public abstract void handleTouch(int Id);
    public abstract boolean handleClick(Activity activity, int Id);
    public abstract void specialOpen();
    public abstract void specialClose();
    public abstract void handleCameraMove();
    public abstract void handleConfigurationChange(Context context, Configuration newConfig);

    public static void staticHandleConfigurationChange(Context context, Configuration newConfig){
        for (CustomViewGroup customViewGroup : ViewGroupsList.values()) {
            customViewGroup.handleConfigurationChange(context, newConfig);
        }
    };
}
