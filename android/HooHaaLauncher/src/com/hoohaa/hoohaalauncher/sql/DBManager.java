package com.hoohaa.hoohaalauncher.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	
	private static HomeGridDbHelper mDbHelper;
	
	public static void init(Context context){
		mDbHelper = new HomeGridDbHelper(context);
	}
	
	public static long insertContainer(int containerType, int antecesorId){
		SQLiteDatabase wdb = mDbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(DBContracts.ContainerEntry.COLUMN_NAME_CONTAINER_TYPE, containerType);
		
		long id = wdb.insert(DBContracts.ContainerEntry.TABLE_NAME, null, values);
		
		values.put(DBContracts.ContainerEntry.COLUMN_NAME_NEXT_CONTAINER_ID, id);
		wdb.update(
				DBContracts.ContainerEntry.TABLE_NAME, 
				values, DBContracts.ContainerEntry._ID+" = ?", 
				new String[] {antecesorId+""});
		return id;
	}
	
	public static long insertItem(int itemId, int itemType, int containerId, 
			int containerType, ContentValues layout){
		
		SQLiteDatabase wdb = mDbHelper.getWritableDatabase();
		long layoutId = -1;
		if(containerType == DBContracts.CONTAINER_TYPE_GRID){
			layoutId = wdb.insert(
					DBContracts.GridLayoutEntry.TABLE_NAME, null, layout);
		}
		
		ContentValues values = new ContentValues();
		values.put(DBContracts.ItemEntry.COLUMN_NAME_ITEM_ID, itemId);
		values.put(DBContracts.ItemEntry.COLUMN_NAME_ITEM_TYPE, itemType);
		values.put(DBContracts.ItemEntry.COLUMN_NAME_CONTAINER_ID, containerId);
		values.put(DBContracts.ItemEntry.COLUMN_NAME_LAYOUT_ID, layoutId);
		
		
		return wdb.insert(DBContracts.ItemEntry.TABLE_NAME, null, values);
	}
	
	public static long insertApp(String packageName, String activityName, String label){
		SQLiteDatabase wdb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBContracts.AppEntry.COLUMN_NAME_PACKAGE, packageName);
		values.put(DBContracts.AppEntry.COLUMN_NAME_ACTIVITY, activityName);
		values.put(DBContracts.AppEntry.COLUMN_NAME_LABEL, label);
		
		return wdb.insert(DBContracts.AppEntry.TABLE_NAME, null, values);
	}
	
	public static Cursor queryApps(){
		SQLiteDatabase rdb = mDbHelper.getReadableDatabase();
		
		String sort = DBContracts.AppEntry.COLUMN_NAME_LABEL + " ASC";
		
		return rdb.query(
				true, 
				DBContracts.AppEntry.TABLE_NAME, 
				null, null, null, null, null, sort, null);
	}
	
	public static Cursor queryItems(){
		SQLiteDatabase rdb = mDbHelper.getReadableDatabase();
		
		final String tItems = DBContracts.ItemEntry.TABLE_NAME;
		final String tContainers = DBContracts.ContainerEntry.TABLE_NAME;
		final String tApps = DBContracts.AppEntry.TABLE_NAME;
		final String tGridLayouts = DBContracts.GridLayoutEntry.TABLE_NAME;
		return rdb.rawQuery("SELECT " +
				tItems+"."+DBContracts.ItemEntry._ID+","+
				tApps+"."+DBContracts.AppEntry.COLUMN_NAME_PACKAGE+","+
				tApps+"."+DBContracts.AppEntry.COLUMN_NAME_ACTIVITY+","+
				tApps+"."+DBContracts.AppEntry.COLUMN_NAME_LABEL+","+
				tContainers+"."+DBContracts.ContainerEntry._ID+","+
				tContainers+"."+DBContracts.ContainerEntry.COLUMN_NAME_CONTAINER_TYPE+","+
				tGridLayouts+"."+DBContracts.GridLayoutEntry.COLUMN_NAME_X+","+
				tGridLayouts+"."+DBContracts.GridLayoutEntry.COLUMN_NAME_Y+","+
				tGridLayouts+"."+DBContracts.GridLayoutEntry.COLUMN_NAME_WIDTH+","+
				tGridLayouts+"."+DBContracts.GridLayoutEntry.COLUMN_NAME_HEIGHT+
				" FROM "+
				tItems+
				" LEFT JOIN "+tApps+" ON "+tItems+"."+DBContracts.ItemEntry.COLUMN_NAME_ITEM_ID
				+"="+tApps+"."+DBContracts.AppEntry._ID+
				" LEFT JOIN "+tContainers + " ON "+tItems+"."+DBContracts.ItemEntry.COLUMN_NAME_CONTAINER_ID
				+"="+tContainers+"."+DBContracts.ContainerEntry._ID+
				" LEFT JOIN "+tGridLayouts+" ON "+tItems+"."+DBContracts.ItemEntry.COLUMN_NAME_LAYOUT_ID
				+"="+tGridLayouts+"."+DBContracts.GridLayoutEntry._ID, null);
		
		
	}
}
