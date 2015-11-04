package com.hoohaa.hoohaalauncher;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import util.xmlparser.XmlManager;

public class MainActivity extends AppCompatActivity implements UncaughtExceptionHandler {
	
	DrawerLayout drawer;
	GridView appsGrid;
	AppsListAdapter adapter;
	GridContainer homeGrid;
	Dock dock;
	ToolbarView toolbar;
	PackageManager pm;
	PackReceiver pReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pm = getPackageManager();
		drawer = (DrawerLayout)findViewById(R.id.drawer);
		appsGrid = (GridView)findViewById(R.id.apps_grid);
		homeGrid = (GridContainer)findViewById(R.id.home_grid);
		dock = (Dock)findViewById(R.id.dock);
		toolbar = (ToolbarView)findViewById(R.id.toolbar);
		
		LauncherMan.init(getResources().getConfiguration().orientation, drawer, toolbar);
		
		appsGrid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				drawer.closeDrawer(GravityCompat.END);
				return true;
			}
		});
		
		new LoadApps().execute(true);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		pReceiver = new PackReceiver();
		registerReceiver(pReceiver, filter);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		XmlManager.writeApps(this, PackMan.getAllByName());
		List<Container> containers = new ArrayList<>();
		containers.add(homeGrid);
		containers.add(dock);
		XmlManager.newWriteItems(this, containers);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(pReceiver);
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
			startActivity(settingsIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	@Override
	public void onBackPressed() {
		drawer.closeDrawers();
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		XmlManager.writeApps(this, PackMan.getAllByName());
		List<Container> containers = new ArrayList<>();
		containers.add(homeGrid);
		containers.add(dock);
		XmlManager.newWriteItems(this, containers);
		unregisterReceiver(pReceiver);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(LauncherMan.getOrientation() != newConfig.orientation){
			setContentView(R.layout.activity_main);
			drawer = (DrawerLayout)findViewById(R.id.drawer);
			appsGrid = (GridView)findViewById(R.id.apps_grid);
			homeGrid = (GridContainer)findViewById(R.id.home_grid);
			dock = (Dock)findViewById(R.id.dock);
			toolbar = (ToolbarView)findViewById(R.id.toolbar);
			
			appsGrid.setAdapter(adapter);
			LauncherMan.init(newConfig.orientation, drawer, toolbar);
		}
		super.onConfigurationChanged(newConfig);
	}
	
	public class LoadApps extends AsyncTask<Boolean, Integer, Boolean> {
		
		private static final int PACKS = 1;
		private static final int ITEMS = 2;
		
		private List<PackMan.Pack> packs = null;
		private HashMap<String, List<Item>> containers = null;

		@Override
		protected Boolean doInBackground(Boolean... params) {
			
			if(params[0]){ //OnCreate
				packs = XmlManager.readApps(MainActivity.this);
				boolean existsXmlFile = packs != null;
				if(existsXmlFile) PackMan.init(packs);
				else{
					final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
					mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					List<ResolveInfo> packsList = pm.queryIntentActivities(mainIntent, 0);
					PackMan.init(packsList, pm);
					packs = PackMan.getAllByName();
				}
				publishProgress(PACKS);
				containers = XmlManager.readItems(MainActivity.this);
				if(containers != null) publishProgress(ITEMS);
				if(!existsXmlFile){
					for(int i=0; i<packs.size(); ++i){
						packs.get(i).cacheIcon(getApplicationContext());
					}
					XmlManager.writeApps(MainActivity.this, packs != null ? packs : PackMan.getAllByName());
					return false;
				}
			}else{ //App installed/uninstalled/changed
				final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
				mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				List<ResolveInfo> packsList = pm.queryIntentActivities(mainIntent, 0);
				PackMan.init(packsList, pm);
				packs = PackMan.getAllByName();
				publishProgress(PACKS);
				for(int i=0; i<packs.size(); ++i){
					packs.get(i).cacheIcon(getApplicationContext());
				}
				XmlManager.writeApps(MainActivity.this, packs != null ? packs : PackMan.getAllByName());
			}
			
			return true;
			
		}
		
		
		@Override
		protected void onProgressUpdate(Integer... values) {
				if(values[0] == PACKS){
					adapter = new AppsListAdapter(MainActivity.this, packs);
					appsGrid.setAdapter(adapter);
					super.onProgressUpdate(values);
				}else if(values[0] == ITEMS){
					List<Item> items = containers.get(XmlManager.NODE_CONTAINER_GRID);
					if(items != null)
						for(int i=0; i<items.size(); ++i)
							homeGrid.addView(items.get(i));
					items = containers.get(XmlManager.NODE_CONTAINER_DOCK);
					if(items != null)
						for(int i=0; i<items.size(); ++i)
							dock.addView(items.get(i));
				}
		}
		
		@Override
		protected void onPostExecute(Boolean xml) {
			//Toast.makeText(MainActivity.this, "done asynck tas", Toast.LENGTH_SHORT).show();
			if(!xml)
				dock.addView(new ActionItem(MainActivity.this, LauncherMan.ACTION_OPEN_DRAWER), 
						new Dock.LayoutParams(2));
			super.onPostExecute(xml);
		}
	}
	
	public class PackReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//setPacks(false);
			//intent.getData().getHost(); <- package
			new LoadApps().execute(false);
		}
		
	}

	
}
