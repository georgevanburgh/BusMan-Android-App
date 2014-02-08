package com.intrivix.android.busman.screen;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.intrivix.android.busman.R;
import com.intrivix.android.busman.adapter.RouteOptionsAdapter;
import com.intrivix.android.busman.model.Route;

public class RouteMap extends Activity {
	
	private ListView mRouteOptionsListView;
	
	private Button mNavButton;
	
	private RouteOptionsAdapter mRouteOptionsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_map);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mNavButton = (Button) findViewById(R.id.map_nav_button);
        mNavButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO make this do something...
			}
        });
        
        ArrayList<Route> routes = new ArrayList<Route>();
        Route r = new Route("Route 1", 10);
        routes.add(r);
        r = new Route("Bus", 20);
        r.setPrice(2.50f);
        routes.add(r);
        r = new Route("Route 3", 66);
        r.setPrice(2.80f);
        routes.add(r);
        r = new Route("Walking", 60);
        routes.add(r);
        
        mRouteOptionsAdapter = new RouteOptionsAdapter(routes, this);
        
        mRouteOptionsListView = (ListView) findViewById(R.id.route_options_list);
        mRouteOptionsListView.setDividerHeight(0);
        mRouteOptionsListView.setAdapter(mRouteOptionsAdapter);
        mRouteOptionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            	//TODO do something special when this item is clicked...
            }
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO create a menu
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons TODO actually create a menu
        switch(item.getItemId()) {
        case R.id.action_settings:
            // TODO launch the settings activity..
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    

}
