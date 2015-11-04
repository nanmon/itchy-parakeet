package com.hoohaa.hoohaalauncher.sql;

import android.provider.BaseColumns;

public final class DBContracts {
	
	private static final String INT_TYPE = " INTEGER";
	private static final String TEXT_TYPE = " TEXT";
	
	public static final int CONTAINER_TYPE_GRID = 1;
	
	public static final int ITEM_TYPE_APP = 1;
	
	public static final String COMPLETE_ITEM_VIEW = "complete_item_view";
			
			
			
			//" AS SELECT A.time AS Start, B.Time AS Stop" +
	//"FROM time A, time B WHERE A.id=B.id";
	
	/* insert()
	 * mDbHelper = new HomGridHelper(getContext())
	 * db = mDbHelper.getWritableDatabase()
	 * ContentValues values = new ContentValues()
	 * values.put(HomeGridEntry.COLUMN_NAME_ENTRY_ID, id)
	 * ...
	 * long newRowId = db.insert(HomeGridEntry.TABLE_NAME, null, values);
	 * */
	
	/* read() 
	 * mDbHelper = new HomGridHelper(getContext())
	 * db = mDbHelper.getReadableDatabase()
	 * String[] projection = { HomeGridEntry._ID, ... }
	 * String sortOrder = HomeGridEntry.COLUMN_NAME_UPDATED + " DESC";
	 * Cursor c = db.query(
	 * 		HomeGridEntry.TABLE_NAME,
	 * 		projection,
	 * 		selection, //columns for the WHERE clause
	 * 		selectionArgs, //values for the WHERE clause
	 * 		null, //don't group the rows
	 * 		null, //don't filter by row groups
	 * 		sortOrder);
	 * */
	
	public static abstract class ItemEntry implements BaseColumns {
		public static final String TABLE_NAME = "items";
		public static final String COLUMN_NAME_ITEM_ID = "item_id";
		public static final String COLUMN_NAME_ITEM_TYPE = "item_type";
		public static final String COLUMN_NAME_CONTAINER_ID = "container_id";
		//public static final String COLUMN_NAME_CONTAINER_TYPE = "container_type";
		public static final String COLUMN_NAME_LAYOUT_ID = "layout_id";
		
		static final String SQL_CREATE_ENTRIES =
				"CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
				ItemEntry._ID + " INTEGER PRIMARY KEY," +
				ItemEntry.COLUMN_NAME_ITEM_ID + " INTEGER FOREIGN KEY," +
				ItemEntry.COLUMN_NAME_ITEM_TYPE + INT_TYPE + "," +
				ItemEntry.COLUMN_NAME_CONTAINER_ID + " INTEGER FOREIGN KEY," +
				ItemEntry.COLUMN_NAME_LAYOUT_ID + "INTEGER FOREIGN KEY )";
		
		static final String SQL_DELETE_ENTRIES = 
				"DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;
		
	}
	
	public static abstract class ContainerEntry implements BaseColumns {
		public static final String TABLE_NAME = "containers";
		public static final String COLUMN_NAME_CONTAINER_TYPE = "container_type";
		public static final String COLUMN_NAME_NEXT_CONTAINER_ID = "next_container_id";
		
		static final String SQL_CREATE_ENTRIES =
				"CREATE TABLE " + TABLE_NAME + " (" +
				_ID + " INTEGER PRIMARY KEY," +
				COLUMN_NAME_CONTAINER_TYPE + INT_TYPE + "," +
				COLUMN_NAME_NEXT_CONTAINER_ID + INT_TYPE + " )";
		
		static final String SQL_DELETE_ENTRIES =
				"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public static abstract class GridLayoutEntry implements BaseColumns {
		public static final String TABLE_NAME = "grid_layouts";
		public static final String COLUMN_NAME_X = "equis";
		public static final String COLUMN_NAME_Y = "ye";
		public static final String COLUMN_NAME_WIDTH = "width";
		public static final String COLUMN_NAME_HEIGHT = "height";
		
		static final String SQL_CREATE_ENTRIES = 
				"CREATE TABLE " + TABLE_NAME + " (" +
				_ID + " INTEGER PRIMARY KEY," +
				COLUMN_NAME_X + INT_TYPE + "," +
				COLUMN_NAME_Y + INT_TYPE + "," +
				COLUMN_NAME_WIDTH + INT_TYPE + "," +
				COLUMN_NAME_HEIGHT + INT_TYPE + " )";
		
		static final String SQL_DELETE_ENTRIES = 
				"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public static abstract class AppEntry implements BaseColumns {
		public static final String TABLE_NAME = "apps";
		public static final String COLUMN_NAME_PACKAGE = "package";
		public static final String COLUMN_NAME_ACTIVITY = "activity";
		public static final String COLUMN_NAME_LABEL = "label";
		//public static final String COLUMN_NAME_ICON = "icon";
		
		static final String SQL_CREATE_ENTRIES =
				"CREATE TABLE" + TABLE_NAME + " (" +
				_ID + " INTEGER PRIMARY KEY," +
				COLUMN_NAME_PACKAGE + TEXT_TYPE + "," +
				COLUMN_NAME_ACTIVITY + TEXT_TYPE + "," +
				COLUMN_NAME_LABEL + TEXT_TYPE + " )";
		
		static final String SQL_DELETE_ENTRIES = 
				"DROP TABLE IF EXISTS " + TABLE_NAME;
		
	}
	
}
