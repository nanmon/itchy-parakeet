package com.hoohaainc.framework.implementation;

import android.view.MotionEvent;
import android.view.View;

import com.hoohaainc.framework.Input;

import java.util.List;

/**
 * Created by nancio on 11/07/15.
 */
public interface TouchHandler extends View.OnTouchListener{
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
    public List<Input.TouchEvent> getTouchEvents();
}
