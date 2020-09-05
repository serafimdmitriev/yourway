package com.serafimdmitrievstudio.yourway;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Serafim on 07.09.2017.
 */

final class InternalStorage{

    private InternalStorage() {}

    static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fileOutputStream = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    static Object readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fileInputStream = context.openFileInput(key);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return objectInputStream.readObject();
    }
}
