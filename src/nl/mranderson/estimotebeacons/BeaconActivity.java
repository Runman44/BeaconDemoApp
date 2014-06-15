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
		startService(beaconDiscoverServiceIntent);
		registerReceiver(broadcastReceiver, new IntentFilter(
				BeaconDiscoverService.BROADCAST_ACTION));
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
		stopService(beaconDiscoverServiceIntent);
	}

	public void setBeacon(Beacon beacon) {
		MainActivity.clickedBeacon = beacon;
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			BeaconContainer beaconContainer = BeaconContainer
					.getSingletonObject();
			ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
			for (Beacon beacon : beaconContainer.getBeacons()) {
				beaconList.add(beacon);
			}
			fillListView(beaconList);
		}
	};

	private void fillListView(final ArrayList<Beacon> beacons) {
		listView = (ListView) findViewById(R.id.list);
		adapter = new BeaconAdapter(this, R.layout.item_beacon, beacons);
		listView.setAdapter(adapter);
		findViewById(R.id.spinning).setVisibility(View.GONE);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setBeacon(beacons.get(position));
				Toast.makeText(BeaconActivity.this, "Selected Beacon",
						Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}

}
