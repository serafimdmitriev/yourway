package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Serafim on 10.05.2017.
 */

public class Log {
    static String FILENAME = "";

    static void initialize() {
        FILENAME = Environment.getExternalStorageDirectory().toString() + "/Log.txt";

        Date now = new Date();
        String log = Long.toString(now.getTime()) + " Log file has been created \n";

        try {
            // отрываем поток для записи
            FileOutputStream outputStream = new FileOutputStream (new File(FILENAME), false);
            outputStream.write(log.getBytes());
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void write(String log) {

        log = log + "\n";

        try {
            // отрываем поток для записи
            FileOutputStream outputStream = new FileOutputStream (new File(FILENAME), true);
            outputStream.write(log.getBytes());
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void writeException(Exception e) {
        write(e.getMessage());
        for (StackTraceElement el : e.getStackTrace()) {
            write(el.toString());
        }
    }
}
