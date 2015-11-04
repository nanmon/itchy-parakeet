package com.hoohaa.hoohaalauncher;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public abstract class Item extends View {
	
	public static final String APP_ITEM = "AppItem";
	final float dp, sp;
	
	private GestureDetector gestureD;

	public Item(Context context) {
		super(context);
		init();
		dp = context.getResources().getDisplayMetrics().density;
		sp = context.getResources().getDisplayMetrics().scaledDensity;
	}

	public Item(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
		dp = context.getResources().getDisplayMetrics().density;
		sp = context.getResources().getDisplayMetrics().scaledDensity;
	}

	public Item(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		dp = context.getResources().getDisplayMetrics().density;
		sp = context.getResources().getDisplayMetrics().scaledDensity;
	}
	
	private void init(){
		gestureD = new GestureDetector(getContext(), new GestureListener(this),
				new Handler(Looper.getMainLooper()));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//aspect_ratio = 13:21
		int width = -1, height = -1;
		if(MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED){
			width = (int)(MeasureSpec.getSize(widthMeasureSpec));
		}if(MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED){
			height = (int)(MeasureSpec.getSize(heightMeasureSpec));
		}
		if(width == -1 && height == -1){
			width = (int)(65 * dp);
			height = (int)(105 * dp);
		}else if(width == -1){
			width = height*13/21;
		}else if(height == -1){
			height = width*21/13;
		}else{
			if(width*21/13 > height*13/21)
				width = height*13/21;
			else height = width*21/13;
		}
		
		setMeasuredDimension(width, height);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureD.onTouchEvent(event);
	}
	
	public abstract void onClick();
	public abstract boolean onLongClick();

	
	public static class Builder implements BaseBuilder, AppItem.Builder, ActionItem.Builder {
		
		String action, packageName;
		ViewGroup.LayoutParams lp;
		
		public Builder(){
			action = LauncherMan.ACTION_OPEN_DRAWER;
			packageName = "com.hoohaa.hoohaalauncher";
			lp = new ViewGroup.LayoutParams(1, 1);
		}
		
		public BaseBuilder setLayoutParams(ViewGroup.LayoutParams lp){
			this.lp = lp;
			return this;
		}
		
		public AppItem.Builder configAppItem(){
			return this;
		}
		
		public ActionItem.Builder configActionItem(){
			return this;
		}

		@Override
		public ActionItem.Builder setAction(String a) {
			action = a;
			return this;
		}

		@Override
		public ActionItem buildActionItem(Context context) {
			ActionItem item = new ActionItem(context, action);
			item.setLayoutParams(lp);
			return item;
		}

		@Override
		public AppItem.Builder setPackage(String p) {
			packageName = p;
			return this;
		}

		@Override
		public AppItem buildAppItem(Context c) {
			AppItem app = new AppItem(c, packageName);
			app.setLayoutParams(lp);
			return app;
		}
	}
	
	public interface BaseBuilder {
		public BaseBuilder setLayoutParams(ViewGroup.LayoutParams lp);
		public AppItem.Builder configAppItem();
		public ActionItem.Builder configActionItem();
	}
	
	private static class GestureListener extends GestureDetector.SimpleOnGestureListener {
		
		private Item item;
		
		public GestureListener(Item item){
			this.item = item;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			item.onClick();
			item.performClick();
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			item.onLongClick();
			item.performLongClick();
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
	
}
