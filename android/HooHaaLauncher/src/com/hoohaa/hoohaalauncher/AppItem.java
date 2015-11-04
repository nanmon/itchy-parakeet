package com.hoohaa.hoohaalauncher;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;

public class AppItem extends Item{
	
	public static final int DRAW_NORMAL = 0;
	public static final int DRAW_SOLO_ICON = 1;
	
	private String packageName;
	private Bitmap icon;
	private String text;
	private int drawMode = 0;
	
	int iconSize, iconPadding;
	int textWidth, textHeight, textX, textY;
	Rect iconRect = new Rect(), textRect = new Rect();
	Paint iconPaint = new Paint(), textPaint = new Paint();

	
	public AppItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, (String)getTag());
	}

	public AppItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, (String)getTag());
	}

	public AppItem(Context context, String packageName) {
		super(context);
		init(context, packageName);
	}
	
	
	private void init(Context context, String packageName){
		this.packageName = packageName;
		PackMan.Pack pack = PackMan.get(packageName);
		if(pack.mIcon != null)
			icon = Tools.drawableToBitmap(pack.mIcon);
		else icon = pack.getCachedIcon(getContext());
		text = pack.mName;
		
		iconSize = (int)(65 * dp);
		iconPadding = (int)(3 * dp);
		textWidth = (int)(65 * dp);
		textHeight = (int)(40 * dp);
		textX = 0;
		textY = (int)(71 * dp); //65 + 3 + 3
		
		iconPaint.setAntiAlias(true);
		iconPaint.setDither(true);
		
		textPaint.setTextSize(11 * sp);
		textPaint.setColor(Color.WHITE);
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setShadowLayer(2, 0, 0, Color.BLACK);
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int xIni = getPaddingLeft();
		int yIni = getPaddingTop();
		int ancho = getWidth() - getPaddingRight() - xIni;
		iconRect.set(xIni + iconPadding, yIni + iconPadding, 
				xIni + ancho - iconPadding, yIni + ancho - iconPadding);
		if(icon != null)
			icon = Bitmap.createScaledBitmap(icon, iconRect.width(), iconRect.height(), true);
		//int alto = getHeight() - getPaddingBottom() - yIni;
		textX = (getWidth())/2;
		textY = yIni + ancho + (int)(6 * sp) + iconPadding;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(icon, null, iconRect, iconPaint);
		switch(drawMode){
		case DRAW_NORMAL:
			int iBreak = textPaint.breakText(text, true, getWidth(), null);
			if(iBreak < text.length()){
				canvas.drawText(text.substring(0, iBreak), textX, textY, textPaint);
				canvas.drawText(text.substring(iBreak), textX, textY + textPaint.getTextSize(), textPaint);
			}else
				canvas.drawText(text, textX, textY, textPaint);
			break;
		}
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int lRatio = 13;
		int rRatio = 21;
		if(drawMode == DRAW_SOLO_ICON){
			lRatio = rRatio = 1;
		}
		//aspect_ratio = 13:21
		int width = -1, height = -1;
		if(MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED){
			width = (int)(MeasureSpec.getSize(widthMeasureSpec));
		}if(MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED){
			height = (int)(MeasureSpec.getSize(heightMeasureSpec));
		}
		if(width == -1 && height == -1){
			width = (int)(65 * dp);
			height = width*rRatio*lRatio;
		}else if(width == -1){
			width = height*lRatio/rRatio;
		}else if(height == -1){
			height = width*rRatio/lRatio;
		}else{
			if(width*rRatio/lRatio > height*lRatio/rRatio)
				width = height*lRatio/rRatio;
			else height = width*rRatio/lRatio;
		}
		
		setMeasuredDimension(width, height);
	}
	

	@Override
	public void onClick() {
		((Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(20);
		PackMan.startActivity(getContext(), packageName);
	}
	
	@Override
	public boolean onLongClick() {
		((Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(20);
		ClipData.Item item = new ClipData.Item(packageName);
		String label;
		if(getParent() instanceof Container){
			((Container)getParent()).onChildDrag(this);
			label = "Container";
		}else label = "NotContainer";
		ClipData data = ClipData.newPlainText(label, "AppItem");
		data.addItem(item);
		
		View.DragShadowBuilder shadow = new View.DragShadowBuilder(this);
		this.startDrag(data, shadow, null, 0);
		return false;
	}
	
	public void setApp(String activityName){
		this.packageName = activityName;
		PackMan.Pack pack = PackMan.get(activityName);
		if(pack.mIcon != null)
			icon = Tools.drawableToBitmap(pack.mIcon);
		else icon = pack.getCachedIcon(getContext());
		if(!iconRect.isEmpty())
			icon = Bitmap.createScaledBitmap(icon, iconRect.width(), iconRect.height(), true);
		text = pack.mName;
	}
	
	public String getApp(){
		return packageName;
	}
	
	public String getLabel(){
		return text;
	}
	
	public void setLabel(String text){
		this.text = text;
	}
	
	public PackMan.Pack getPack(){
		return PackMan.get(packageName);
	}
	
	public void setDrawMode(int mode){
		drawMode = mode;
	}
	
	public static interface Builder {
		
		final String PACKAGE = "package";
				
		public Builder setPackage(String p);
		public AppItem buildAppItem(Context c);
	}
	
}
