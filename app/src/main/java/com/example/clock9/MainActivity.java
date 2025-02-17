package com.example.clock9;


import android.content.Intent;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class MainActivity extends AppCompatActivity implements Flashable {
    SettingsData settingsData;
    long[] flashesStartingTimings = new long[17];
    ExecutorService executorService;
    boolean isFlashing = false;

    final long CLOCK_UPDATE_MS_ERROR = 200L;

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
        executorService = new ScheduledThreadPoolExecutor(16);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSetting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        settingsData = SettingsData.settingsData;
        updateClockView();
    }

    private void initializeSettings(){
        settingsData = SettingsData.settingsData = SettingsData.readFromFile(this);
        updateClockView();
    }

    public void onScreenTap(View v){
        // прячем или показываем кнопки, при нажатии на экран
        invertVisibility(R.id.btIncrease);
        invertVisibility(R.id.btDecrease);
        invertVisibility(R.id.btSettings);
        invertVisibility(R.id.btFlash);
    }

    public void btFlash_onClick(View v){
        Button button = findViewById(R.id.btFlash);
        isFlashing = !isFlashing;

        if(isFlashing){
            button.setText(getText(R.string.stop_flash));
             startFlashing();
        }else{
            button.setText(getText(R.string.start_flash));
            stopFlashing();
        }

    }
    private void startFlashing(){

        ((MyClock)findViewById(R.id.textClock)).addListener(this);

        long currentTime = Calendar.getInstance().getTime().getTime();
        long flashCycleDuration = (long)settingsData.flash_cycle_duration * 1000;
        long startFlashingTiming = currentTime + flashCycleDuration - currentTime % flashCycleDuration - CLOCK_UPDATE_MS_ERROR;
        flashesStartingTimings[16] = startFlashingTiming;
        for (int i = 0; i < settingsData.flashes_timings.length; i++) {
            int timingInSeconds = settingsData.flashes_timings[i];
            if (timingInSeconds <= 0){
                flashesStartingTimings[i] = -1;
                continue;
            }
            flashesStartingTimings[i] = startFlashingTiming + (long)timingInSeconds*1000;
        }

    }

    private void flashBackground(){
        ConstraintLayout l = findViewById(R.id.main);
        MyClock t = findViewById(R.id.textClock);





        var black = getResources().getColor(R.color.black);
        var white = getResources().getColor(R.color.white);
        Log.d("delay clock text",t.getText().toString());
        t.setTextColor(black);
        l.setBackgroundColor(white);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Log.d("delay clock text",e.toString());
        }
        l.setBackgroundColor(black);
        t.setTextColor(white);

    }
    private void stopFlashing(){
        ((MyClock)findViewById(R.id.textClock)).removeListener(this);
    }

    private void invertVisibility(int id){
        View obj = findViewById(id);
        if (obj.getVisibility() == Button.VISIBLE){
            obj.setVisibility(Button.INVISIBLE);
        }else{
            obj.setVisibility(Button.VISIBLE);
        }
    }
    public void saveSetting(){
        SettingsData.saveToFile(this);
    }
    public void btIncrease_onClick(View v){
        // увеличиваем соответствующий размер шрифта
        var orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            settingsData.font_size_port += 2;
        }else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            settingsData.font_size_land += 2;
        }
        updateClockView();
    }
    public void btSettings_onClick(View v){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }
    public void btDecrease_onClick(View v){
        // уменьшаем соответствующий размер шрифта
        var orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            settingsData.font_size_port -= 2;
        }else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            settingsData.font_size_land -= 2;
        }
        updateClockView();
    }
    private void updateClockView(){

        TextClock textClock = findViewById(R.id.textClock);

        var orientation = getResources().getConfiguration().orientation;
        // устанавливаем размер шрифта из настроек, в зависимости от ориентации экрана
        int fontSize = 0;
        String timeFormat = "";
        float scaleY = 1f;
        float scaleX = 1f;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fontSize = settingsData.font_size_port;
            timeFormat = settingsData.format_24_port;
            scaleY = settingsData.scaleY_port;
            scaleX = settingsData.scaleX_port;
        }else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            fontSize = settingsData.font_size_land;
            timeFormat = settingsData.format_24_land;
            scaleY = settingsData.scaleY_land;
            scaleX = settingsData.scaleX_land;
        }

        textClock.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        textClock.setFormat24Hour(timeFormat);
        textClock.setScaleY(scaleY);
        textClock.setScaleX(scaleX);

    }

    @Override
    public void onFlash() {

        long currentTime = Calendar.getInstance().getTime().getTime();
        boolean isItTimeForFlash = false;
        long flashCycleDuration = (long)settingsData.flash_cycle_duration * 1000;
        for (int i = 0; i < flashesStartingTimings.length; i++) {

            if(flashesStartingTimings[i] <= 0){
                continue;
            }

            if (flashesStartingTimings[i] <= currentTime){
                flashesStartingTimings[i] += flashCycleDuration - currentTime % flashCycleDuration - CLOCK_UPDATE_MS_ERROR;
                isItTimeForFlash = true;
            }


        }
        if(isItTimeForFlash){
            executorService.execute(()->flashBackground());
        }

    }
}