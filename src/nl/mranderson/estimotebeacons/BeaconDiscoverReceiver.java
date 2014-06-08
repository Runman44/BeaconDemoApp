package nl.mranderson.estimotebeacons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BeaconDiscoverReceiver extends BroadcastReceiver {
    private Intent beaconDiscoverServiceIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
    	beaconDiscoverServiceIntent = new Intent(context, BeaconDiscoverService.class);
		context.startService(beaconDiscoverServiceIntent);
	}
}