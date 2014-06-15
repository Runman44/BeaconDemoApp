/**
 * @author Dennis Anderson
 * @version 1.1
 */
package nl.mranderson.estimotebeacons;

import java.util.ArrayList;

import com.estimote.sdk.Beacon;

public class BeaconContainer {

	private static BeaconContainer singletonItem;
	private ArrayList<Beacon> beacons;

	private BeaconContainer() {
	}

	public static BeaconContainer getSingletonObject() {
		if (singletonItem == null)
			singletonItem = new BeaconContainer();
		return singletonItem;
	}

	public ArrayList<Beacon> getBeacons() {
		return beacons;
	}

	public void setBeacons(ArrayList<Beacon> beacons) {
		this.beacons = beacons;
	}

}