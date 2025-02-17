package com.example.clock9;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;



public class SettingsData implements Serializable {

    public static SettingsData settingsData = null;
    public static String settingsDataFileName = "settingsData.json";
    public int font_size_port = 144;
    public int font_size_land = 144;
    public String format_24_port = "HH\nmm\nss";
    public String format_24_land = "HH:mm:ss";
    public float scaleX_port = 1f;
    public float scaleY_port = 1f;
    public float scaleX_land = 1f;
    public float scaleY_land = 1f;
    public boolean isScheduleFlashingEnabled = true;

    public int flash_cycle_duration = 60;
    public int[] flashes_timings = new int[16];

    public static SettingsData readFromFile(AppCompatActivity mainActivity){
        File file = new File(mainActivity.getFilesDir(), settingsDataFileName);
        // считываем из файла настройки
        try (FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (SettingsData) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // если не удалось прочитать файл, ставим параметр по умолчанию
            Log.d("ReadError",e.toString());
        }

        return new SettingsData();
    }

    public static void saveToFile(AppCompatActivity mainActivity){
        try (FileOutputStream outputStream = mainActivity.openFileOutput(settingsDataFileName, Context.MODE_PRIVATE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            // сереализуем и сохраняем в файл настройки
            objectOutputStream.writeObject(SettingsData.settingsData);
            objectOutputStream.flush();
        }
        catch (IOException e) {
            Log.d("WriteError",e.toString());
        }
    }
}
