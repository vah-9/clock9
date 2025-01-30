package com.example.clock9;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextClock;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class MainActivity extends AppCompatActivity {

    SettingsData settingsData; // настройки приложения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initializeSettings();

    }

    private void initializeSettings(){

        settingsData = new SettingsData();
        File file = new File(getFilesDir(), SettingsData.settingsDataFileName);
        // считываем из файла настройки
        try (FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            settingsData = (SettingsData) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            // если не удалось прочитать файл, ставим параметр по умолчанию
            Log.d("ReadError",e.toString());
            int font_size_default_integer = getResources().getInteger(R.integer.font_size_default_integer);
            settingsData.font_size_land = font_size_default_integer;
            settingsData.font_size_port = font_size_default_integer;
        }
        updateClock();
    }

    public void onScreenTap(View v){
        // прячем или показываем кнопки, при нажатии на экран
        invertVisibility(R.id.btIncrease);
        invertVisibility(R.id.btDecrease);
        invertVisibility(R.id.btSave);
    }

    private void invertVisibility(int id){
        View obj = findViewById(id);
        if (obj.getVisibility() == Button.VISIBLE){
            obj.setVisibility(Button.INVISIBLE);
        }else{
            obj.setVisibility(Button.VISIBLE);
        }
    }


    public void btSave_onClick(View v) {

        try (FileOutputStream outputStream = openFileOutput(SettingsData.settingsDataFileName, Context.MODE_PRIVATE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            // сереализуем и сохраняем в файл настройки
            objectOutputStream.writeObject(settingsData);
            objectOutputStream.flush();
        }
        catch (IOException e) {
            Log.d("WriteError",e.toString());
        }
    }

    public void btIncrease_onClick(View v){
        // увеличиваем соответствующий размер шрифта
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            settingsData.font_size_port += 2;
        }else{
            settingsData.font_size_land += 2;
        }
        updateClock();
    }

    public void btDecrease_onClick(View v){
        // уменьшаем соответствующий размер шрифта
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            settingsData.font_size_port -= 2;
        }else{
            settingsData.font_size_land -= 2;
        }
        updateClock();
    }

    private void updateClock(){

        TextClock textClock = findViewById(R.id.textClock);
        // устанавливаем размер шрифта из настроек, в зависимости от ориентации экрана
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            textClock.setTextSize(TypedValue.COMPLEX_UNIT_DIP, settingsData.font_size_port);
        }else{
            textClock.setTextSize(TypedValue.COMPLEX_UNIT_DIP, settingsData.font_size_land);
        }

    }

}