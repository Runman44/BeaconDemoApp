/**
 * Copyright (c) 2014, Dennis Anderson. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This class will interact with the proximity of the selected beacon. The background image will change depending on the proximity of the beacon.
 */
package nl.mranderson.estimotebeacons;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

public class MainActivity extends Activity {

	private Intent beaconDiscoverServiceIntent;
	public static Beacon clickedBeacon = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkBluetoothEnabled();
		beaconDiscoverServiceIntent = new Intent(this,
				BeaconDiscoverService.class);
	}

	@Override
	public void onResume() {
		super.onResume();
		// start the service
		startService(beaconDiscoverServiceIntent);
		// register broadcastreceiver
		registerReceiver(broadcastReceiver, new IntentFilter(
				BeaconDiscoverService.BROADCAST_ACTION));
	}

	@Override
	public void onPause() {
		super.onPause();
		// unregister the broadcastreceiver
		unregisterReceiver(broadcastReceiver);
		// stop the service
		stopService(beaconDiscoverServiceIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.beacons) {
			// open BeaconActivity on menu item clicked.
			Intent intent = new Intent(MainActivity.this, BeaconActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Checks if bluetooth is enabled. if bluetooth isn't supported or isn't
	 * enabled there will be a warning given to the user
	 */
	private void checkBluetoothEnabled() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast warning = Toast.makeText(this,
					"Warning: Device does not support Bluetooth",
					Toast.LENGTH_LONG);
			warning.show();
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				Toast warning = Toast.makeText(this,
						"Warning: Bluetooth is not enabled", Toast.LENGTH_LONG);
				warning.show();
			}
		}
	}

	/**
	 * When a beacon is found it will check if it is the same beacon that has
	 * been selected and will then set the background image that is
	 * corresponding to the proximity.
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			View background = findViewById(R.id.main_advertisement);
			if (clickedBeacon != null) {

				BeaconContainer beaconContainer = BeaconContainer
						.getSingletonObject();
				for (Beacon beacon : beaconContainer.getBeacons()) {
					if ((beacon.getProximityUUID().equals(clickedBeacon
							.getProximityUUID()))
							&& (beacon.getMinor() == clickedBeacon.getMinor())) {

						switch (Utils.computeProximity(beacon)) {
						case UNKNOWN:
							background
									.setBackgroundResource(R.drawable.nobeacon);
							break;
						case IMMEDIATE:
							background
									.setBackgroundResource(R.drawable.proximity1_3);
							break;
						case NEAR:
							background
									.setBackgroundResource(R.drawable.proximity1_2);
							break;
						case FAR:
							background
									.setBackgroundResource(R.drawable.proximity1_1);
							break;
						default:
							break;
						}

					}
				}

			}
		}
	};
}