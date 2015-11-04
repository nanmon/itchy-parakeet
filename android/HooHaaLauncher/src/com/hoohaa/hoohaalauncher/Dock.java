package com.hoohaa.hoohaalauncher;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

public class Dock extends Container {
	
	private static int length = 5;

	private View dragging = null;
	private DragEvent dragEvent = null;
	private Paint paint = new Paint();
	private int width, height, cellWidth, cellHeight;
	
	public Dock(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public Dock(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}	

	public Dock(Context c) {
		super(c);
		init(c);
	}
	
	
	private void init(Context c){
		setWillNotDraw(false);
		paint.setColor(Color.WHITE);
	}
	
	@Override
	public void addView(View child) {
		ViewGroup.LayoutParams lpp = child.getLayoutParams();
		if(lpp == null)
			lpp = generateDefaultLayoutParams();
		
		if(lpp instanceof LayoutParams){
			LayoutParams lp = (LayoutParams)lpp,
					olp;
			for(int i=0; i<getChildCount(); ++i){
				olp =(LayoutParams)getChildAt(i).getLayoutParams();
				if(olp.index == lp.index)
					return; //TODO: folder
			}
			super.addView(child, lp);
			if(child instanceof AppItem)
				((AppItem)child).setDrawMode(AppItem.DRAW_SOLO_ICON);
		}
	}
	
	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, params);
		if(child instanceof AppItem)
			((AppItem)child).setDrawMode(AppItem.DRAW_SOLO_ICON);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		boolean isPort = LauncherMan.getOrientation() == Configuration.ORIENTATION_PORTRAIT;
		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = getMeasuredWidth() - getPaddingRight();
		int bottom = getMeasuredHeight() - getPaddingBottom();
		width = right-left;
		height = bottom-top;
		
		int count = getChildCount();
		int iLeft, iTop, p;
		
		if(isPort){
			cellWidth = width/length;
			cellHeight = height;
			for(int i=0; i<count; ++i){
				View child = getChildAt(i);
				LayoutParams lp = (LayoutParams)child.getLayoutParams();
				p = lp.index;
				child.measure(MeasureSpec.makeMeasureSpec(lp.width*cellWidth, MeasureSpec.AT_MOST), 
						MeasureSpec.makeMeasureSpec(lp.height*cellHeight, MeasureSpec.AT_MOST));
				iLeft = (lp.width*cellWidth-child.getMeasuredWidth())/2;
				iTop = (lp.height*cellHeight-child.getMeasuredHeight())/2;
				child.layout(left+iLeft+p*cellWidth, top+iTop, 
						left+iLeft+p*cellWidth+child.getMeasuredWidth(), 
						top+iTop+child.getMeasuredHeight());
			}
		}else{
			cellWidth = width;
			cellHeight = height/length;
			for(int i=0; i<count; ++i){
				View child = getChildAt(i);
				LayoutParams lp = (LayoutParams)child.getLayoutParams();
				p = lp.index;
				child.measure(MeasureSpec.makeMeasureSpec(lp.width*cellWidth, MeasureSpec.AT_MOST), 
						MeasureSpec.makeMeasureSpec(lp.height*cellHeight, MeasureSpec.AT_MOST));
				iLeft = (lp.width*cellWidth-child.getMeasuredWidth())/2;
				iTop = (lp.height*cellHeight-child.getMeasuredHeight())/2;
				child.layout(left+iLeft, top+iTop+p+cellHeight, 
						left+iLeft+child.getMeasuredWidth(), 
						top+iTop+p*cellHeight+child.getMeasuredHeight());
			}
		}
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(dragEvent != null){
			if(LauncherMan.getOrientation() == Configuration.ORIENTATION_LANDSCAPE){
				for(int i=1; i<length; ++i){
					canvas.drawLine(getPaddingLeft(), 
							getPaddingTop()+i*height/length, 
							getPaddingLeft() + width, 
							getPaddingTop()+i*height/length, paint);
				}
			}else{
				for(int j=1; j<length; ++j){
					canvas.drawLine(getPaddingLeft() + j*width/length, 
							getPaddingTop(), 
							getPaddingLeft() + j*width/length, 
							getPaddingTop()+height, paint);
				}
			}
		}
	}
	
	public List<Item> getAllItems(){
		ArrayList<Item> items = new ArrayList<>();
		for(int i=0; i<getChildCount(); ++i){
			items.add((Item)getChildAt(i));
		}
		//TODO: Collections.sort(items, comparator);
		return items;
	}
	

	@Override
	public void onChildDrag(View child) {
		child.setVisibility(View.INVISIBLE);
		dragging = child;
	}
	
	@Override
	public boolean onDragEvent(DragEvent event) {
		final int action = event.getAction();
		switch(action){
		case DragEvent.ACTION_DRAG_STARTED:
			dragEvent = event;
			if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
				invalidate();
				return true;
			}
			dragEvent = null;
			return false;
		case DragEvent.ACTION_DRAG_ENTERED:
			return true;
		case DragEvent.ACTION_DRAG_LOCATION:
			LauncherMan.onDrag(event);
			return true;
		case DragEvent.ACTION_DRAG_EXITED:
			return true;
		case DragEvent.ACTION_DROP:
			ClipData.Item item = event.getClipData().getItemAt(0);
			int x = (int)((event.getX()-getPaddingLeft())/cellWidth), 
					y = (int)((event.getY()-getPaddingTop())/cellHeight);
			x = Math.max(x, y);
			if(x < 0 || x >= length){
				return false;
			}
			if(dragging != null){
				LayoutParams olp;
				for(int i=0; i<getChildCount(); ++i){
					olp =(LayoutParams)getChildAt(i).getLayoutParams();
					if(olp.index == x){
						dragging.setVisibility(View.VISIBLE);
						return false; //TODO: folder
					}
				}
				
			}
			Item itemView = null;
			if(item.getText().equals("AppItem"))
				itemView = new AppItem(getContext(), event.getClipData().getItemAt(1).getText().toString());
			addView(itemView, new LayoutParams(x));
			
			return true;
		case DragEvent.ACTION_DRAG_ENDED:
			if(dragging != null){
				if(event.getResult())
					removeView(dragging);
				else dragging.setVisibility(View.VISIBLE);
				dragging = null;
			}
			dragEvent = null;
			invalidate();
		}
		return false;
	}

	
	
	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(0);
	}
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		if(LauncherMan.getOrientation() != newConfig.orientation){
			
		}
		super.onConfigurationChanged(newConfig);
	}
	
	public static class LayoutParams extends ViewGroup.LayoutParams {
		
		public int index;

		public LayoutParams(int index) {
			super(1,1);
			this.index = index;
		}
	}

}
