/**
 * Copyright (c) 2014, Dennis Anderson. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This service will post a notification when a beacon is found.
 */

package nl.mranderson.estimotebeacons;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

public class BeaconDiscoverService extends Service {

	private BeaconManager beaconManager;
	private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId",
			null, null, null);
	private static final int NOTIFICATION_ID = 123;
	private NotificationManager notificationManager;
	public static final String BROADCAST_ACTION = "nl.spwned.estimotespwned.beacondiscoveryservice";
	private Intent intent;
	private final IBinder mBinder = new LocalBinder();

	@Override
	public void onCreate() {
		super.onCreate();

		intent = new Intent(BROADCAST_ACTION);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		beaconManager = new BeaconManager(getApplicationContext());
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		setRanging();
	}

	@Override
	public void onDestroy() {
		try {
			// stop searching for beacons
			beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	public class LocalBinder extends Binder {
		BeaconDiscoverService getService() {
			return BeaconDiscoverService.this;
		}
	}

	private void setRanging() {
		// searching for beacons
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {

			@Override
			public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
				// for every beacon that has been found. Check its proximity.
				for (Beacon beacon : beacons) {
					switch (Utils.computeProximity(beacon)) {
					case FAR:
						postNotification("Welcome, please search me!");
						break;
					case NEAR:
						postNotification("You are getting closer!");
						break;
					default:
						break;
					}
				}
				// save every found beacon in the container
				BeaconContainer beaconContainer = BeaconContainer
						.getSingletonObject();
				beaconContainer.setBeacons(new ArrayList<Beacon>(beacons));

				// send message to boradcastreceiver
				sendBroadcast(intent);
			}
		});
	}

	/**
	 * Setup and build a notification
	 * 
	 * @param msg
	 *            message to be send to the user
	 */
	private void postNotification(String msg) {
		Intent notifyIntent = new Intent(getApplicationContext(),
				BeaconActivity.class);
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivities(
				getApplicationContext(), 0, new Intent[] { notifyIntent },
				PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(
				getApplicationContext()).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(getString(R.string.app_name))
				.setContentText(msg).setAutoCancel(true)
				.setContentIntent(pendingIntent).build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notificationManager.notify(NOTIFICATION_ID, notification);
	}

}