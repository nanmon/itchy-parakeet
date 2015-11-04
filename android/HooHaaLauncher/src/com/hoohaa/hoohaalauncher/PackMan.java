package com.hoohaa.hoohaalauncher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

public class PackMan {
	
	private static Comparator<Pack> byName = new Comparator<Pack>(){
		@Override
		public int compare(Pack lhs, Pack rhs) {
			return lhs.mName.toUpperCase(Locale.ENGLISH).compareTo(rhs.mName.toUpperCase(Locale.ENGLISH));
		}
		
	};
	
	private static HashMap<String, Pack> packs = new HashMap<>();
	
	public static void init(List<Pack> ps){
		Pack p;
		for(int i=0; i<ps.size(); ++i){
			p = ps.get(i);
			packs.put(p.mPackage, p);
		}
	}
	
	public static void init(List<ResolveInfo> appsList, PackageManager pm){
		String packName;
		ResolveInfo app;
		for(int i=0; i<appsList.size(); ++i){
			app = appsList.get(i);
			packName = app.activityInfo.packageName;
			packs.put(packName, new Pack(
					app.loadLabel(pm).toString(),
					app.activityInfo.name,
					packName,
					app.loadIcon(pm)));
		}
	}
	
	public static List<Pack> getAllByName(){
		List<Pack> p = new ArrayList<Pack>(packs.values());
		Collections.sort(p, byName);
		return p;
	}
	
	public static Pack get(String packageName){
		return packs.get(packageName);
	}
	
	public static void startActivity(Context context, String packageName){
		Intent launchIntent = new Intent(Intent.ACTION_MAIN);
		launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ComponentName cp = new ComponentName(
				packageName, 
				get(packageName).mActivity);
		launchIntent.setComponent(cp);
		context.startActivity(launchIntent);
	}
	
	public static class Pack{
		public String mName;
		public String mActivity;
		public String mPackage;
		public Drawable mIcon;
		
		
		public Pack(String name, String activity, String packageName, Drawable icon){
			mName = name;
			mActivity = activity;
			mPackage = packageName;
			mIcon = icon;
		}
		
		public void cacheIcon(Context context){
			
			new File(context.getFilesDir().getAbsolutePath() 
					+ "/cachedApps/").mkdir();
			
			if(mIcon != null){
				String iconLocation = context.getFilesDir().getAbsolutePath()
						+ "/cachedApps/" + mPackage + mName;
				FileOutputStream fos = null;
				try{
					fos = new FileOutputStream(iconLocation);
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
				
				if(fos != null){
					Tools.drawableToBitmap(mIcon).compress(
							Bitmap.CompressFormat.PNG, 
							100, fos);
					try{
						fos.flush();
						fos.close();
					}catch(IOException e){
						e.printStackTrace();
					}
				}else
					iconLocation = null;
			}
		}
		
		public Bitmap getCachedIcon(Context context){
			String iconLocation = context.getFilesDir().getAbsolutePath()
					+ "/cachedApps/" + mPackage + mName;
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Config.ARGB_8888;
			options.inDither = true;
			
			File cachedIcon = new File(iconLocation);
			if(cachedIcon.exists()){
				return BitmapFactory.decodeFile(cachedIcon.getAbsolutePath(), options);
			}
			return null;
		}
		
		public void deleteIcon(Context context){
			String iconLocation = context.getFilesDir().getAbsolutePath()
					+ "/cachedApps/" + mPackage + mName;
			if(iconLocation != null)
				new File(iconLocation).delete();
		}
	}
	
}
