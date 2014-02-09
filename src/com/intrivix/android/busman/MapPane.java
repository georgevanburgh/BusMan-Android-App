package com.intrivix.android.busman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.intrivix.android.busman.network.APITask;
import com.intrivix.android.busman.screen.MainActivity;

public class MapPane extends Activity implements LocationListener {

	private GoogleMap map;
	private LocationManager locationManager;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;
	private String sillyGoogleString = null;
	
	private String URL = "http://maps.googleapis.com/maps/api/directions/json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		

    	ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
    	pairs.add(new BasicNameValuePair("origin", "London"));
    	pairs.add(new BasicNameValuePair("destination", "Manchester"));
    	pairs.add(new BasicNameValuePair("sensor", "true"));
    	String getReqAddon = APITask.buildGetParameters(pairs);
    	
		APITask.callApi(URL+getReqAddon, "", APITask.REQUEST_GET,
					getSillyGoogleStringHandler);
		
		
		
		
		//System.out.println(theString);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

		map.setMyLocationEnabled(true);
		map.setTrafficEnabled(true);
		map.setBuildingsEnabled(true);

	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,
				14);
		map.animateCamera(cameraUpdate);
		locationManager.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	
	private Handler getSillyGoogleStringHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //System.out.println("GET ROUTE API RESULT: " + msg.obj);
            if(msg == null || msg.obj == null){
				MainActivity.showError(MapPane.this);
				return;
			}
			try {
				JSONObject object = new JSONObject((String) msg.obj);
				
				JSONArray array = object.getJSONArray("routes");
				
				JSONObject o_polyLine = array.getJSONObject(array.length()-1).getJSONObject("overview_polyline");
				sillyGoogleString = o_polyLine.getString("points");
				
				List<LatLng> decodedPath = PolyUtil.decode(sillyGoogleString);

				map.addPolyline(new PolylineOptions().addAll(decodedPath).color(
						Color.BLUE));

				if (object.has("status")
						&& object.get("status").equals("OK")) {

				} else {
					// TODO there was an error....
					MainActivity.showError(MapPane.this);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

}
