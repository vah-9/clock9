package com.example.clock9;


import java.io.Serializable;

public class SettingsData implements Serializable {
    // сериализуемый класс для хранения настроек приложения
    // можно было хранить их в хэшмапе, но я уже реализовал этот класс, мб переделаю
    public static SettingsData settingsData;
    public static String settingsDataFileName = "settingsData.json";
    public int font_size_port;
    public int font_size_land;

}
