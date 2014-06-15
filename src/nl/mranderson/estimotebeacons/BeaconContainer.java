/**
 * Copyright (c) 2014, Dennis Anderson. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This class is a singleton.
 * It keeps a array of beacons that has been found. 
 */
package nl.mranderson.estimotebeacons;

import java.util.ArrayList;

import com.estimote.sdk.Beacon;

public class BeaconContainer {

	private static BeaconContainer singletonItem;
	private ArrayList<Beacon> beacons;

	/**
	 * private constructor
	 */
	private BeaconContainer() {
	}

	/**
	 * This method will return a container filled with beacons. This container
	 * is a singleton and will be created once.
	 * 
	 * @return beaconContainer filled with beacons
	 */
	public static BeaconContainer getSingletonObject() {
		if (singletonItem == null)
			singletonItem = new BeaconContainer();
		return singletonItem;
	}

	// getter
	public ArrayList<Beacon> getBeacons() {
		return beacons;
	}

	// setter
	public void setBeacons(ArrayList<Beacon> beacons) {
		this.beacons = beacons;
	}

}