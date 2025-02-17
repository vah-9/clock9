package com.example.clock9;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    SettingsData settingsData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fillFieldFromSettings();
    }
    private void fillFieldFromSettings(){
        settingsData = SettingsData.settingsData;

        ((TextInputEditText)findViewById(R.id.tiFormatLand)).setText(settingsData.format_24_land);
        ((TextInputEditText)findViewById(R.id.tiFormatPort)).setText(settingsData.format_24_port);
        ((TextInputEditText)findViewById(R.id.tiLandScaleX)).setText(Float.toString(settingsData.scaleX_land));
        ((TextInputEditText)findViewById(R.id.tiLandScaleY)).setText(Float.toString(settingsData.scaleY_land));
        ((TextInputEditText)findViewById(R.id.tiPortScaleX)).setText(Float.toString(settingsData.scaleX_port));
        ((TextInputEditText)findViewById(R.id.tiPortScaleY)).setText(Float.toString(settingsData.scaleY_port));

        ((TextInputEditText)findViewById(R.id.tiFlasherCycle)).setText(Integer.toString(settingsData.flash_cycle_duration));
        ((TextInputEditText)findViewById(R.id.tiFlasherFlashes)).setText(listAsString(settingsData.flashes_timings));
    }
    private String listAsString(int[] array){
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            if (array[i] > 0){
                result.append(Integer.toString(array[i]));
                result.append(';');
            }
        }
        if (result.length() == 0){
            return "";
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }
    public void btSave_onClick(View v){
        settingsData.format_24_land = ((TextInputEditText)findViewById(R.id.tiFormatLand)).getText().toString();
        settingsData.format_24_port = ((TextInputEditText)findViewById(R.id.tiFormatPort)).getText().toString();

        float tiLandScaleX = SettingsData.settingsData.scaleX_land;
        try {
            tiLandScaleX = Float.parseFloat(((TextInputEditText)findViewById(R.id.tiLandScaleX)).getText().toString());
        }catch (Exception ignored){}
        settingsData.scaleX_land = tiLandScaleX;

        float tiLandScaleY = SettingsData.settingsData.scaleY_land;
        try {
            tiLandScaleY = Float.parseFloat(((TextInputEditText)findViewById(R.id.tiLandScaleY)).getText().toString());
        }catch (Exception ignored){}
        settingsData.scaleY_land = tiLandScaleY;

        float tiPortScaleX = SettingsData.settingsData.scaleX_port;
        try {
            tiPortScaleX = Float.parseFloat(((TextInputEditText)findViewById(R.id.tiPortScaleX)).getText().toString());
        }catch (Exception ignored){}
        settingsData.scaleX_port = tiPortScaleX;

        float tiPortScaleY = SettingsData.settingsData.scaleY_port;
        try {
            tiPortScaleY = Float.parseFloat(((TextInputEditText)findViewById(R.id.tiPortScaleY)).getText().toString());
        }catch (Exception ignored){}
        settingsData.scaleY_port = tiPortScaleY;

        int tiFlasherCycle = SettingsData.settingsData.flash_cycle_duration;
        try {
            tiFlasherCycle = Integer.parseInt(((TextInputEditText)findViewById(R.id.tiFlasherCycle)).getText().toString());
        }catch (Exception ignored){}
        settingsData.flash_cycle_duration = tiFlasherCycle;

        int[] tiFlasherFlashes = SettingsData.settingsData.flashes_timings;
        try {
            String timingsString = ((TextInputEditText)findViewById(R.id.tiFlasherFlashes)).getText().toString();
            var timingsStringArray = timingsString.split(";");
            for (int i = 0; i < timingsStringArray.length || i < tiFlasherFlashes.length; i++) {

                try {
                    int t = Integer.parseInt(timingsStringArray[i]);
                    if (t > 0)
                        tiFlasherFlashes[i] = t;

                }catch (Exception ignored){}

            }

        }catch (Exception ignored){}
        settingsData.flashes_timings = tiFlasherFlashes;

        SettingsData.settingsData = settingsData;
        SettingsData.saveToFile(this);

    }

    public void btReset_onClick(View v){
        settingsData = new SettingsData();
        fillFieldFromSettings();
    }
    public void btClose_onClick(View v){
        this.finish();
    }


}