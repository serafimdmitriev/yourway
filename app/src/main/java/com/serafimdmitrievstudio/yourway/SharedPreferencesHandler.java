package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Serafim on 04.06.2017.
 */

public class SharedPreferencesHandler {
    static final String APP_PREFERENCES = "mysettings";
    static final String A_P_HISTORY = "time";
    static final String A_P_MAINCITY = "maincity";
    static final String A_P_TIMEOFFIRSTUSING = "timeoffirstusing";
    static final String A_P_TIMEOFLASTUSING = "dayoflastusing";
    static final String A_P_TIMEOFDONTASKABOUTGPS = "timeofdontaskaboutgps";
    static final String A_P_DONTASKABOUTGPS = "dontaskaboutgps";
    static final String A_P_USERID = "userid";
    static final String A_P_EDITORUSED = "editorused";
    static final String A_P_DONTASKABOUTRATE = "dontaskaboutrate";
    static final String A_P_TIMEOFDONTASKABOUTRATE = "timeofdontaskaboutrate";
    static final String A_P_FIRSTUSING = "firstusing";
    static SharedPreferences mSettings;

    static void initialize(Activity activity) {
        mSettings = activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    static void setValue(String field, int value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(field, value);
        editor.apply();
    }

    static void setValue(String field, long value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putLong(field, value);
        editor.apply();
    }

    static void setValue(String field, String value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(field, value);
        editor.apply();
    }

    static void setValue(String field, Set<String> value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putStringSet(field, value);
        editor.apply();
    }

    static void setValue(String field, boolean value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(field, value);
        editor.apply();
    }
}
