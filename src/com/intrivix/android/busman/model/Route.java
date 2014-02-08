package com.intrivix.android.busman.model;

import java.util.Vector;

public class Route {
	
	private String mRouteName;
	private int totalEta;
	private float mPrice = -1;
	
	private Vector<RouteSection> mRouteSections = new Vector<RouteSection>();
	
	public Route(String name, int eta) {
		mRouteName = name;
		totalEta = eta;
	}
	
	/**
	 * Set the optional price for the route.
	 * @param price The new price.
	 */
	public void setPrice(float price) {
		mPrice = price;
	}
	
	public float getPrice() {
		return mPrice;
	}
	
	/**
	 * Add a new section to this route.
	 * @param section The new section of this route.
	 */
	public void addSection(RouteSection section) {
		mRouteSections.add(section);
	}
	
	/**
	 * Get the sections of this route.
	 * @return The sections of this route.
	 */
	public Vector<RouteSection> getRouteSections() {
		return mRouteSections;
	}
	
	/**
	 * Get the name of this route. (Walking, Bus, Tram, etc.)
	 * n@return The name of this route object.
	 */
	public String getRouteName() {
		return mRouteName;
	}
	
	/**
	 * Get the ETA of this route from start to finish.
	 * @return The ETA.
	 */
	public int getETA() {
		return totalEta;
	}

}
