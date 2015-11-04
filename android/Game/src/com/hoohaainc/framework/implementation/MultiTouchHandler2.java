package com.hoohaainc.framework.implementation;

import java.util.ArrayList;
import java.util.List;

import com.hoohaainc.framework.Input.TouchEvent;
import com.hoohaainc.framework.Pool;

import android.view.MotionEvent;
import android.view.View;

public class MultiTouchHandler2 implements TouchHandler {
	
	private final int MAX_POINTERS = 10;
	
	private float scaleX, scaleY;
	private TouchEvent[] actualBuffer = new TouchEvent[MAX_POINTERS];
	private TouchEvent[] nextBuffer = new TouchEvent[MAX_POINTERS];
	private List<TouchEvent> events = new ArrayList<>(MAX_POINTERS);
	private Pool<TouchEvent> touchEventPool;
	
	public MultiTouchHandler2(View view, float scaleX, float scaleY){
        Pool.PoolObjectFactory<TouchEvent> factory =
                new Pool.PoolObjectFactory<TouchEvent>() {
                    @Override
                    public TouchEvent createObject() {
                        return new TouchEvent();
                    }
                };
        touchEventPool = new Pool<>(factory, 100);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		TouchEvent touch;
		int id;
		for(int i=0; i<MAX_POINTERS; ++i){
			if(i >= event.getPointerCount()){
				
			}
			id = event.getPointerId(i);
			
			if(i == event.getActionIndex()){
				touch = touchEventPool.newObject();
			}else touch = actualBuffer[id];
			
			touch.x = (int)(event.getX(i)*scaleX);
			touch.y = (int)(event.getY(i)*scaleY);
			touch.pointer = id;
			switch(event.getActionMasked()){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				touch.type = TouchEvent.TOUCH_DOWN;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
				touch.type = TouchEvent.TOUCH_UP;
				break;
			case MotionEvent.ACTION_MOVE:
				touch.type = TouchEvent.TOUCH_DRAGGED;
				break;
			default: return false;
			}
			if(actualBuffer[id] != null && actualBuffer[id].type == TouchEvent.TOUCH_DOWN)
				nextBuffer[id] = touch;
			else actualBuffer[id] = touch;
		}
		return true;
	}

	@Override
	public boolean isTouchDown(int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getTouchX(int pointer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTouchY(int pointer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		for(int i=0; i<events.size(); ++i)
			touchEventPool.free(events.get(i));
		events.clear();
		for(int i=0; i<MAX_POINTERS; ++i){
			if(actualBuffer[i] != null){
				events.add(actualBuffer[i]);
				actualBuffer[i] = nextBuffer[i];
				nextBuffer[i] = null;
			}
			
		}
		return events;
	}

}
