package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Serafim on 03.03.2018.
 */

class StringHandler {
    private static Resources resources;

    static void initialize(Resources res) {
        resources = res;
    }

    static String getString(int Id) {
        return resources.getString(Id);
    }
}
