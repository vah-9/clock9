package com.example.clock9;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextClock;
import java.util.ArrayList;

public class MyClock extends TextClock  {

    private final ArrayList<Flashable> listeners = new ArrayList<>();

    public MyClock(Context context) {
        super(context);
    }

    public MyClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyClock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void addListener(Flashable listener){
        listeners.add(listener);
    }

    public void removeListener(Flashable listener){
        listeners.remove(listener);
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        Log.d("clock",this.getText().toString());

        fireFlash();
    }

    private void fireFlash(){
        if (listeners == null) {
            return;
        }
        for (Flashable listener :
                listeners) {
            if (listener != null) {
                listener.onFlash();
            }

        }
    }

}
