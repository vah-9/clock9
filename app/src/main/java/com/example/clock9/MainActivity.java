package com.example.clock9;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.TextClock;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

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

        InitializeComponents();

    }

    private void InitializeComponents(){
        InitializeClock();
    }

    private void InitializeClock(){
        TextClock textClock = findViewById(R.id.textClock);
        textClock.setFormat12Hour(null);

        float scaleX;
        float scaleY;
        String formatString;

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            formatString = "HH\nmm\nss";
            scaleY = 1f;
            scaleX = 1.5f;
        } else {
            formatString = "HH:mm:ss";
            scaleY = 1.5f;
            scaleX = 1f;
        }
        textClock.setFormat24Hour(formatString);
        textClock.setScaleY(scaleY);
        textClock.setScaleX(scaleX);
        textClock.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 144);
    }


}