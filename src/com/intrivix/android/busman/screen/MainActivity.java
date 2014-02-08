package com.intrivix.android.busman.screen;

import com.intrivix.android.busman.MapPane;
import com.intrivix.android.busman.R;
import com.intrivix.android.busman.R.array;
import com.intrivix.android.busman.R.drawable;
import com.intrivix.android.busman.R.id;
import com.intrivix.android.busman.R.layout;
import com.intrivix.android.busman.R.menu;
import com.intrivix.android.busman.R.string;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDrawerItems;
    
    private int mCurrentFragment = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


        mTitle = mDrawerTitle = getTitle();
        mDrawerItems = getResources().getStringArray(R.array.draw_menu_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
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
        switch(item.getItemId()) {
        case R.id.action_settings:
            // TODO launch the settings activity..
            /* EXAMPLE INTENT TO LAUNCH ACTIVITY
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            startActivity(intent);
            */
            return true;
        case R.id.action_map_pane:
        	//For Richard's testing...
            Intent intent = new Intent(MainActivity.this, MapPane.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
    	if(mCurrentFragment == position) {
            mDrawerLayout.closeDrawer(mDrawerList);
    		return;
    	}
    	
    	if(position == 1) {
            Intent intent = new Intent(MainActivity.this, RouteMap.class);
            startActivity(intent);
    		return;
    	}
    	
        // update the main content by replacing fragments
        Fragment fragment = new HomeFragment();
        //Bundle args = new Bundle();
        //args.putInt(HomeFragment.ARG_PLANET_NUMBER, position);
        //fragment.setArguments(args);
        

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

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
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            
            mToAddress = (EditText) rootView.findViewById(R.id.toAddress);
            mFromAddress = (EditText) rootView.findViewById(R.id.fromAddress);
            
            mNavigateButton = (Button) rootView.findViewById(R.id.navigateButton);
            mNavigateButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                	//For now, just create a Toast message with the input data
                	//TODO to send an API request to do the navigation...
                	
                	String toAddress = mToAddress.getText().toString();
                	String fromAddress = mFromAddress.getText().toString();
                	if(fromAddress.isEmpty()) {
                		//TODO grab the device's current location...
                	}
                	
                	Toast.makeText(HomeFragment.this.getActivity(),
                					"To Address: " + toAddress
                					+ ", From Address: " + fromAddress,
                					Toast.LENGTH_SHORT).show();
            	}
            });
            
            
            return rootView;
        }
    }

}
