package com.example.transformer.interface1.sensor;

import java.util.EventListener;

/**
 * Created by h-simada on 2017/07/29.
 */

public interface MotionListener extends EventListener{
    public void inclinedToRight();
    public void inclinedToLeft();
    public void shuffled();
}
