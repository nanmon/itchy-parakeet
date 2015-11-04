package com.hoohaa.hoohaalauncher;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToolbarView extends LinearLayout {
	
	final static int MODE_NORMAL = 0;
	final static int MODE_DRAG_FROM_CONTAINER = 1;
	final static int MODE_DRAG_FROM_ADAPTER_VIEW = 2;
	
	private int mode = 0;
	private LinearLayout layout;

	public ToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ToolbarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ToolbarView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		layout = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.toolbar_view, this);
		
		AutoCompleteTextView actv = (AutoCompleteTextView)layout.findViewById(R.id.google_autocomplete);
		actv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
				intent.putExtra(SearchManager.QUERY, v.getText().toString());
				if(intent.resolveActivity(getContext().getPackageManager()) != null){
					getContext().startActivity(intent);
				}
				return false;
			}
		});
		/*gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
						
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				onClick();
				performClick();
				return true;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}
		});*/
	}
	
	/*
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		String text = "Google";
		paint.setColor(Color.WHITE);
		int dp5 = (int)(10*dp);
		if(mode == MODE_DRAG_FROM_CONTAINER){
			paint.setColor(Color.RED);
			text = "X";
			canvas.drawCircle(LauncherMan.getDragX(), getHeight()/2, Math.min(getWidth(), getHeight())/2, paint);
			canvas.drawText(text, LauncherMan.getDragX(), getHeight()/2, textPaint);
			return;
		}else if(mode == MODE_DRAG_FROM_ADAPTER_VIEW){
			paint.setColor(Color.GREEN);
			text = "Information";
		}
		canvas.drawRect(dp5, dp5, getWidth()-dp5, getHeight()-dp5, paint);
		canvas.drawText(text, getWidth()/2, getHeight()/2, textPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
	
	public void onClick(){
		Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
		if(intent.resolveActivity(getContext().getPackageManager()) != null){
			((Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(20);
			getContext().startActivity(intent);
		}
	}*/
	
	@Override
	public boolean onDragEvent(DragEvent event) {
		switch(event.getAction()){
		case DragEvent.ACTION_DRAG_STARTED:
			if(event.getClipDescription().getLabel().equals("Container")){
				mode = MODE_DRAG_FROM_CONTAINER;
				layout.findViewById(R.id.google_layout).setVisibility(View.GONE);
				layout.findViewById(R.id.delete_layout).setVisibility(View.VISIBLE);
				return true;
			}else if(event.getClipDescription().getLabel().equals("NotContainer")){
				mode = MODE_DRAG_FROM_ADAPTER_VIEW;
				layout.findViewById(R.id.google_layout).setVisibility(View.GONE);
				layout.findViewById(R.id.options_layout).setVisibility(View.VISIBLE);
				return true;
			}
			return false;
		case DragEvent.ACTION_DRAG_LOCATION:
			LauncherMan.onDrag(event);
			return true;
		case DragEvent.ACTION_DRAG_ENTERED:
		case DragEvent.ACTION_DRAG_EXITED:
			return true;
		case DragEvent.ACTION_DROP:
			if(mode == MODE_DRAG_FROM_CONTAINER){
				return true;
			}else if(mode == MODE_DRAG_FROM_ADAPTER_VIEW){

				Intent intent;
				switch((int)(event.getX()*3/getWidth())){
				case 0: //uninstall
					
					intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, 
							Uri.parse(event.getClipData().getItemAt(1).getText().toString()));
					getContext().startActivity(intent);
					break;
				case 1: //info
					intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					intent.setData(Uri.parse("package:"+event.getClipData().getItemAt(1).getText()));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(intent);
					break;
				case 2: //options
				}
				
				return true;
				
			}
		case DragEvent.ACTION_DRAG_ENDED:
			mode = MODE_NORMAL;
			layout.findViewById(R.id.delete_layout).setVisibility(View.GONE);
			layout.findViewById(R.id.options_layout).setVisibility(View.GONE);
			layout.findViewById(R.id.google_layout).setVisibility(View.VISIBLE);
			invalidate();
			return false;
		}
		return super.onDragEvent(event);
	}
}
