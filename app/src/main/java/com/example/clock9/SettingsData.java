package com.example.clock9;

import java.io.Serializable;

public class SettingsData implements Serializable {

    public static String settingsDataFileName = "settingsData.json";
    public int font_size_port;
    public int font_size_land;
    public CharSequence format_24_port = "HH\nmm\nss";
    public CharSequence format_24_land = "HH:mm:ss";

    public boolean isScheduleFlashingEnabled = true;


}
