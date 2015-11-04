package com.hoohaa.hoohaalauncher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GridContainer extends Container {
	
	private int width, height;
	protected int nRows = 4, nColumns = 4;
	
	private View dragging = null;
	private DragEvent dragEvent = null;
	private Paint paint = new Paint();
	
	private Comparator<Item> comparator = new Comparator<Item>(){

		@Override
		public int compare(Item lhs, Item rhs) {
			LayoutParams llp = (LayoutParams)lhs.getLayoutParams(),
					rlp = (LayoutParams)rhs.getLayoutParams();
			int il = llp.row*nColumns + llp.column,
					ir = rlp.row*nColumns + rlp.column;
			return il > ir ? 1 : -1;
		}
		
	};
	
	public GridContainer(Context c){
		super(c);
		init();
	}
	
	public GridContainer(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}



	public GridContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		paint.setColor(Color.WHITE);
		setWillNotDraw(false);
		if(isInEditMode()){
			for(int y=0; y<nRows; ++y){
				for(int x=0; x<nColumns; ++x){
					TextView t = new TextView(this.getContext());
					t.setLayoutParams(new LayoutParams(y,x));
					t.setText(y*nColumns+x+"");
					t.setBackgroundColor(Color.rgb((int)(Math.random()*255), 
							(int)(Math.random()*255), (int)(Math.random()*255)));
					addView(t);
				}
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = getMeasuredWidth() - getPaddingRight();
		int bottom = getMeasuredHeight() - getPaddingBottom();
		width = right-left;
		height = bottom-top;
		
		int count = getChildCount();
		int iLeft, iTop;
		for(int i=0; i<count; ++i){
			View child = getChildAt(i);
			LayoutParams lp = (LayoutParams)child.getLayoutParams();
			child.measure(MeasureSpec.makeMeasureSpec(lp.width*width/nColumns, MeasureSpec.AT_MOST), 
					MeasureSpec.makeMeasureSpec(lp.height*height/nRows, MeasureSpec.AT_MOST));
			iLeft = (lp.width*width/nColumns-child.getMeasuredWidth())/2;
			iTop = (lp.height*height/nRows-child.getMeasuredHeight())/2;
			child.layout(left+iLeft+lp.column*width/nColumns, top+iTop+lp.row*height/nRows, 
					left+iLeft+lp.column*width/nColumns+child.getMeasuredWidth(), 
					top+iTop+lp.row*height/nRows+child.getMeasuredHeight());
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(dragEvent != null){
			for(int i=1; i<nRows; ++i){
				canvas.drawLine(getPaddingLeft(), 
						getPaddingTop()+i*height/nRows, 
						getPaddingLeft() + width, 
						getPaddingTop()+i*height/nRows, paint);
			}
			for(int j=1; j<nColumns; ++j){
				canvas.drawLine(getPaddingLeft() + j*width/nColumns, 
						getPaddingTop(), 
						getPaddingLeft() + j*width/nColumns, 
						getPaddingTop()+height, paint);
			}
		}
	}
	
	@Override
	public List<Item> getAllItems(){
		ArrayList<Item> items = new ArrayList<>();
		for(int i=0; i<getChildCount(); ++i){
			items.add((Item)getChildAt(i));
		}
		Collections.sort(items, comparator);
		return items;
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
				if(olp.row == lp.row && olp.column == lp.column)
					return; //TODO: folder
			}
			super.addView(child, lp);
		}
	}
	
	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(0,0);
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
			if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
				dragEvent = event;
				invalidate();
				return true;
			}
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
			int x = (int)((event.getX()-getPaddingLeft())*nColumns/width), 
					y = (int)((event.getY()-getPaddingTop())*nRows/height);
			if(x < 0 || x >= nColumns || y < 0 || y >= nRows){
				return false;
			}
			if(dragging != null){
				LayoutParams olp;
				for(int i=0; i<getChildCount(); ++i){
					olp =(LayoutParams)getChildAt(i).getLayoutParams();
					if(olp.row == y && olp.column == x){
						dragging.setVisibility(View.VISIBLE);
						return false; //TODO: folder
					}
				}
				
			}
			Item itemView = null;
			if(item.getText().toString().equals("AppItem"))
				itemView = new AppItem(getContext(), event.getClipData().getItemAt(1).getText().toString());
			addView(itemView, new LayoutParams(y,x));
			
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
	
	public static class LayoutParams extends ViewGroup.LayoutParams {

		public int row, column;
		
		public LayoutParams(int row, int column) {
			super(1, 1);
			this.row = row;
			this.column = column;
		}
		
	}
	
	public static class Builder {
		private ArrayList<Item> items;
		
		public Builder addItem(Item item){
			items.add(item);
			return this;
		}
		
		public GridContainer build(Context c){
			GridContainer grid = new GridContainer(c);
			for(int i=0; i<items.size(); ++i)
				grid.addView(items.get(i));
			return grid;
		}
	}

}
