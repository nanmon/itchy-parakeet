package com.hoohaa.hoohaalauncher;

import java.util.List;

import com.hoohaa.hoohaalauncher.PackMan.Pack;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class AppsListAdapter extends ArrayAdapter<PackMan.Pack>{

	public AppsListAdapter(Context context, List<Pack> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Pack pack = getItem(position);
		if(convertView == null){
			convertView = new AppItem(getContext(), pack.mPackage);
		}else ((AppItem)convertView).setApp(pack.mPackage);
		
		return convertView;
	}
	

}
