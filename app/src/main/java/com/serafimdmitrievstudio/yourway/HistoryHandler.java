package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Serafim on 04.06.2017.
 */

public class HistoryHandler {
    private static ArrayList<CustomPlace> history;
    private static String KEY = "HISTORY";

    static private boolean checkHistory() {
        if (history == null) {
            history = new ArrayList<>();
            return false;
        } else {
            return true;
        }
    }

    static void removeHistoryPlace(CustomPlace place) {
        checkHistory();

        history.remove(place);
    }

    static void addHistoryPlace(CustomPlace place, Activity activity) {
        checkHistory();

        for (int i = 0; i < history.size(); i++) {
            try {
                if (history.get(i).equals(place)) {
                    history.remove(i);
                    i--;
                }
            } catch (Exception e) {
                    Log.write(e.getMessage());
            }
        }

        getHistory(activity).add(0, place);

        if (getHistory(activity).size() > 10) {
            getHistory(activity).remove(getHistory(activity).size()-1);
        }

        ArrayList<CustomPlaceSerialization> historyToWrite = new ArrayList<>();
        for (CustomPlace placeToWrite : getHistory(activity)) {
            historyToWrite.add(placeToWrite.serialize());
        }

        try {
            InternalStorage.writeObject(activity, KEY, historyToWrite);
        } catch (IOException e) {
            Log.write(e.getMessage());
        }

        //SharedPreferencesHandler.mSettings.
    }

    static ArrayList<CustomPlace> getHistory(Activity activity) {
        if (!checkHistory()) {
            try {
                ArrayList<CustomPlaceSerialization> historyToRead
                        = (ArrayList<CustomPlaceSerialization>)InternalStorage.readObject(activity, KEY);

                for (CustomPlaceSerialization placeToRead : historyToRead) {
                    history.add(placeToRead.deserialize());
                }

            } catch (Exception e) {
                Log.write(e.getMessage());
                history = new ArrayList<>();
            }
        }

        return history;
    }

    public static void addHistoryString(String s) {
        ArrayList<String> history = new ArrayList<>(SharedPreferencesHandler.mSettings.getStringSet(SharedPreferencesHandler.A_P_HISTORY, new HashSet<String>()));

        if (history.contains(s)) {
            history.remove(s);
        }  else {
            for (int i = 2; i < history.size(); i++) {
                history.remove(i);
            }
        }

        history.add(0, s);

        SharedPreferences.Editor editor = SharedPreferencesHandler.mSettings.edit();
        editor.putStringSet(SharedPreferencesHandler.A_P_HISTORY, new HashSet<String>(history));
        editor.apply();

    }

    public static ArrayList<String> getHistoryString() {
        return new ArrayList<>(SharedPreferencesHandler.mSettings.getStringSet(SharedPreferencesHandler.A_P_HISTORY, new HashSet<String>()));
    }
}
