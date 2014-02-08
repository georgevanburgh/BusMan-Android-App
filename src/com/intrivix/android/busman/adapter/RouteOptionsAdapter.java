package com.intrivix.android.busman.adapter;

import java.text.DecimalFormat;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.intrivix.android.busman.R;
import com.intrivix.android.busman.model.Route;

public class RouteOptionsAdapter extends BaseAdapter {
	
	private Vector<Route> mRoutes;
	
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	public RouteOptionsAdapter(Vector<Route> routes, Context context) {
		//TODO some needed setup
		mRoutes = routes;
		mContext = context;
	    mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setRoute(Vector<Route> newRoutes) {
		mRoutes = newRoutes;
	}

	@Override
	public int getCount() {
		return mRoutes.size();
	}

	@Override
	public Object getItem(int position) {
		return mRoutes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.map_route_list_item, parent, false);
		}
		
		Route thisRoute = (Route) getItem(position);
		
		TextView name = (TextView) convertView.findViewById(R.id.route_name);
		name.setText(thisRoute.getRouteName());

		TextView price = (TextView) convertView.findViewById(R.id.price_text);
		price.setText(thisRoute.getPrice() < 0 ? "" : thisRoute.getPrice()+"");
		
		TextView eta = (TextView) convertView.findViewById(R.id.eta_text);
		eta.setText(getETAString(thisRoute.getETA()));
		
		return convertView;
	}
	
	private String getETAString(int eta) {
		if(eta < 60) {
			return eta + "m";
		} else {
			return eta/60 + "h" + (eta%60 == 0 ? "" : eta%60 + "m");
		}
	}
	
	private String getPriceFormat(float price) {
		
		String str = "£%.2f";
		;
		
		return "";
	}

}
