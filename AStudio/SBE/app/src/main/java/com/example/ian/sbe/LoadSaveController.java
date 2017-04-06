package com.example.ian.sbe;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ian on 2017-04-05.
 */

public class LoadSaveController extends Activity {
    // thanks to the following for serialization code
    // http://www.coderzheaven.com/2012/07/25/serialization-android-simple-example/
    public static void serialize(String filename, Context context) {
        saveObject(Entity.allEntities(), filename, context);
        /*
        for (Entity entity : Entity.allEntities()) {
            saveObject(entity, filename, context);
        }*/
        /*
        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            String str = "";
            for (Entity entity : Entity.allEntities()) {
                str += Integer.toString(entity.getId()) + ",";
            }
            //outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static void saveObject(Serializable obj, String filename, Context context){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE)); //Select where you wish to save the file...
            oos.writeObject(obj); // write the class as an 'object'
            oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
            oos.close();// close the stream
        }
        catch(Exception ex) {
            Log.v("SerializationSaveErr: ",ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static ArrayList<Entity> deserialize(String filename, Context context) {
        return (ArrayList<Entity>) loadSerializedObject(filename, context);
    }

    private static Object loadSerializedObject(String filename, Context context) {
        return loadSerializedObject(new File(context.getFilesDir(), filename));
    }

    private static Object loadSerializedObject(File f) {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            return o;
        }
        catch(Exception ex)
        {
            Log.v("SerializationReadErr: ",ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
