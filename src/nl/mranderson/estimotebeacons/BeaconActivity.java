/**
 * Copyright (c) 2014, Dennis Anderson. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This class shows a list of all beacons that can be found. 
 * When one beacon has been clicked, this beacon will be the selected beacon where the RangeListener will react to in the MainActivity.
 */
package nl.mranderson.estimotebeacons;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;

public class BeaconActivity extends Activity {

	private ListView listView;
	private ArrayAdapter<Beacon> adapter;
	private Intent beaconDiscoverServiceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon);
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

	private void setCurrentBeacon(Beacon beacon) {
		MainActivity.clickedBeacon = beacon;
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			BeaconContainer beaconContainer = BeaconContainer
					.getSingletonObject();
			ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
			for (Beacon beacon : beaconContainer.getBeacons()) {
				// add every discovered beacon in the list
				beaconList.add(beacon);
			}
			fillListView(beaconList);
		}
	};

	private void fillListView(final ArrayList<Beacon> beacons) {
		// show list of all beacons
		listView = (ListView) findViewById(R.id.list);
		adapter = new BeaconAdapter(this, R.layout.item_beacon, beacons);
		listView.setAdapter(adapter);
		findViewById(R.id.spinning).setVisibility(View.GONE);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// set selected beacon as the beacon who will be watched on.
				setCurrentBeacon(beacons.get(position));
				Toast.makeText(BeaconActivity.this, "Selected Beacon",
						Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}

}
