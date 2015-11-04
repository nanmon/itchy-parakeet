package com.hoohaa.hoohaalauncher.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HomeGridDbHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "HomeGrid.db";
	
	public HomeGridDbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBContracts.AppEntry.SQL_CREATE_ENTRIES);
		db.execSQL(DBContracts.GridLayoutEntry.SQL_CREATE_ENTRIES);
		db.execSQL(DBContracts.ContainerEntry.SQL_CREATE_ENTRIES);
		db.execSQL(DBContracts.ItemEntry.SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DBContracts.AppEntry.SQL_DELETE_ENTRIES);
		db.execSQL(DBContracts.GridLayoutEntry.SQL_DELETE_ENTRIES);
		db.execSQL(DBContracts.ContainerEntry.SQL_DELETE_ENTRIES);
		db.execSQL(DBContracts.ItemEntry.SQL_DELETE_ENTRIES);
		onCreate(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
	
}