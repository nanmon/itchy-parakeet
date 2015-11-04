package com.hoohaa.hoohaalauncher;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public abstract class Container extends ViewGroup {

	public Container(Context c){
		super(c);
	}

	public Container(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public Container(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public abstract void onChildDrag(View child);
	public abstract List<Item> getAllItems();
	
}
