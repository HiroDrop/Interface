package com.example.transformer.interface1.sensor;

import java.util.EventListener;

/**
 * Created by h-simada on 2017/07/29.
 */

public class MotionNotifier{
    private MotionListener listener = null;

    public void checkMotion(){

    }

    public void setListener(MotionListener l){
        this.listener = l;
    }
}
