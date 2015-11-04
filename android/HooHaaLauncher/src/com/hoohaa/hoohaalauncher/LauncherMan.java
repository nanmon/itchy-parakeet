package com.hoohaa.hoohaalauncher;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.DragEvent;

public class LauncherMan {
	public static final String ACTION_OPEN_DRAWER = "open_drawer";
	
	private static DrawerLayout drawer;
	private static ToolbarView toolbar;
	private static int orientation;
	private static float dragX, dragY;
	
	public static void init(int orientation, DrawerLayout drawer, ToolbarView toolbar){
		LauncherMan.drawer = drawer;
		LauncherMan.orientation = orientation;
		LauncherMan.toolbar = toolbar;
	}
	
	public static void doAction(String action){
		switch(action){
		case ACTION_OPEN_DRAWER:
			drawer.openDrawer(GravityCompat.END);
		}
	}
	
	public static int getOrientation(){
		return orientation;
	}
	
	public static void onDrag(DragEvent e){
		dragX = e.getX();
		dragY = e.getY();
		toolbar.invalidate();
	}
	
	public static float getDragX(){
		return dragX;
	}
	
	public static float getDragY(){
		return dragY;
	}
}
