package com.intrivix.android.busman.screen;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Toast;

import com.intrivix.android.busman.MapPane;
import com.intrivix.android.busman.R;
import com.intrivix.android.busman.network.APITask;

//

public class MainActivity extends Activity {
	private class PlacesAutoCompleteAdapter {

	}

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mDrawerItems;

	private int mCurrentFragment = -1;

	ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mDrawerItems = getResources().getStringArray(R.array.draw_menu_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawerItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
		// /fancy auto compleate
		//AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.activity_main);
		//autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this,
			//	R.layout.list_item));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_settings:
			// TODO launch the settings activity..
			/*
			 * EXAMPLE INTENT TO LAUNCH ACTIVITY Intent intent = new
			 * Intent(Intent.ACTION_WEB_SEARCH);
			 * intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			 * startActivity(intent);
			 */
			return true;
		case R.id.action_map_pane:
			// For Richard's testing...
			Intent intent = new Intent(MainActivity.this, MapPane.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		if (mCurrentFragment == position) {
			mDrawerLayout.closeDrawer(mDrawerList);
			return;
		}

		if (position == 1) {
			Intent intent = new Intent(MainActivity.this, RouteMap.class);
			startActivity(intent);
			return;
		}

		// update the main content by replacing fragments
		Fragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		// args.putInt(HomeFragment.ARG_PLANET_NUMBER, position);
		// fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mCurrentFragment = position;
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class HomeFragment extends Fragment {

		private Button mNavigateButton;
		private EditText mToAddress, mFromAddress;

		public HomeFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);

			mToAddress = (EditText) rootView.findViewById(R.id.toAddress);
			mFromAddress = (EditText) rootView.findViewById(R.id.fromAddress);

			mNavigateButton = (Button) rootView
					.findViewById(R.id.navigateButton);
			mNavigateButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// For now, just create a Toast message with the input data
					// TODO to send an API request to do the navigation...

					String toAddress = mToAddress.getText().toString();
					String fromAddress = mFromAddress.getText().toString();
					if (fromAddress.isEmpty()) {
						// TODO grab the device's current location...
					}

					Toast.makeText(
							HomeFragment.this.getActivity(),
							"To Address: " + toAddress + ", From Address: "
									+ fromAddress, Toast.LENGTH_SHORT).show();

					ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("start", "m144aq"));
					pairs.add(new BasicNameValuePair("dest", "m145rl"));
					String getReqAddon = APITask.buildGetParameters(pairs);

					APITask.callApi(
							APITask.GET_ROUTE_URL + getReqAddon,
							"",
							APITask.REQUEST_GET,
							((MainActivity) HomeFragment.this.getActivity()).getRouteResultHandler);
				}
			});

			return rootView;
		}
	}

	private Handler getRouteResultHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			System.out.println("GET ROUTE API RESULT: " + msg.obj);
			if (msg == null || msg.obj == null) {
				MainActivity.showError(MainActivity.this);
				if (progress != null)
					progress.dismiss();
				return;
			}
			try {
				JSONObject object = new JSONObject((String) msg.obj);

				if (object.has("status")
						&& object.get("status").equals("succeded")) {

				} else {
					// TODO there was an error....
					MainActivity.showError(MainActivity.this);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (progress != null)
				progress.dismiss();
		}
	};

	public static void showError(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Unexpected Error")
				.setMessage("An unexpected error occurred. Sorry.")
				.setCancelable(true)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public abstract class AutoClassHolder {
		private static final String LOG_TAG = "BusMan";

		private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
		private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
		private static final String OUT_JSON = "/json";

		private static final String API_KEY = "AIzaSyD9QUYAyQd2lOiysKGoqgMPsvKTqAhPDp4";

		private ArrayList<String> autocomplete(String input) {
			ArrayList<String> resultList = null;

			HttpURLConnection conn = null;
			StringBuilder jsonResults = new StringBuilder();
			try {
				StringBuilder sb = new StringBuilder(PLACES_API_BASE
						+ TYPE_AUTOCOMPLETE + OUT_JSON);
				sb.append("?sensor=false&key=" + API_KEY);
				sb.append("&components=country:uk");
				sb.append("&input=" + URLEncoder.encode(input, "utf8"));

				URL url = new URL(sb.toString());
				conn = (HttpURLConnection) url.openConnection();
				InputStreamReader in = new InputStreamReader(
						conn.getInputStream());

				// Load the results into a StringBuilder
				int read;
				char[] buff = new char[1024];
				while ((read = in.read(buff)) != -1) {
					jsonResults.append(buff, 0, read);
				}
			} catch (MalformedURLException e) {
				Log.e(LOG_TAG, "Error processing Places API URL", e);
				return resultList;
			} catch (IOException e) {
				Log.e(LOG_TAG, "Error connecting to Places API", e);
				return resultList;
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}

			try {
				// Create a JSON object hierarchy from the results
				JSONObject jsonObj = new JSONObject(jsonResults.toString());
				JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

				// Extract the Place descriptions from the results
				resultList = new ArrayList<String>(predsJsonArray.length());
				for (int i = 0; i < predsJsonArray.length(); i++) {
					resultList.add(predsJsonArray.getJSONObject(i).getString(
							"description"));
				}
			} catch (JSONException e) {
				Log.e(LOG_TAG, "Cannot process JSON results", e);
			}

			return resultList;
		}

		private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
				implements Filterable {
			private ArrayList<String> resultList;

			public PlacesAutoCompleteAdapter(Context context, int resource) {
				super(context, resource);
				// TODO Auto-generated constructor stub
			}

			@Override
			public Filter getFilter() {
				Filter filter = new Filter() {
					@Override
					protected FilterResults performFiltering(
							CharSequence constraint) {
						FilterResults filterResults = new FilterResults();
						if (constraint != null) {
							// Retrieve the autocomplete results.
							resultList = autocomplete(constraint.toString());

							// Assign the data to the FilterResults
							filterResults.values = resultList;
							filterResults.count = resultList.size();
						}
						return filterResults;
					}

					@Override
					protected void publishResults(CharSequence constraint,
							FilterResults results) {
						if (results != null && results.count > 0) {
							notifyDataSetChanged();
						} else {
							notifyDataSetInvalidated();
						}
					}
				};
				return filter;
			}

			@Override
			public String getItem(int index) {
				return resultList.get(index);
			}

			@Override
			public int getCount() {
				return resultList.size();
			}

		}
	}

}
