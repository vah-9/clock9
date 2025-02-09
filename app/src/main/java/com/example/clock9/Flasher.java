package com.example.clock9;

import android.graphics.Color;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;
import java.util.Date;

public class Flasher implements Runnable {

    MainActivity mainActivity;
    Flasher(MainActivity _mainActivity){
        mainActivity = _mainActivity;
    }

    @Override
    public void run() {
        Date currentTime = Calendar.getInstance().getTime();
        Log.d("delay currentTimeB", Long.toString(currentTime.getTime()));
        FlashBackground();
        currentTime = Calendar.getInstance().getTime();
        Log.d("delay currentTimeA", Long.toString(currentTime.getTime()));

    }

    private void FlashBackground(){
        ConstraintLayout l = mainActivity.findViewById(R.id.main);

        l.setBackgroundColor(Color.parseColor("white"));
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {

        }
        l.setBackgroundColor(Color.parseColor("black"));
    }

}
