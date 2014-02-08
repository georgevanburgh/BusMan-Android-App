package com.intrivix.android.busman;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class MapPane extends Activity {
	long timeLastUpdated = 0;
	final long UPDATE_INTERVAL = 2000;
	final long MAX_UPDATE_INTERVAL = 20000;
	String lastUpdatedProviderType = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		
		Location loc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		// Get a handle to the Map Fragment
				GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();
				LatLng sydney = new LatLng(loc.getLongitude(),
						loc.getLatitude());

				map.setMyLocationEnabled(true);
				map.setTrafficEnabled(true);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

				map.addMarker(new MarkerOptions().title("You!")
						.snippet("You are here (Well we think :P").position(sydney));

		

	}
}
