/**
 * Copyright (c) 2014, Dennis Anderson. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * listview adapter to fill the listview with beacon information. 
 */
package nl.mranderson.estimotebeacons;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

public class BeaconAdapter extends ArrayAdapter<Beacon> {

	private Context context;
	private ArrayList<Beacon> objects;

	public BeaconAdapter(Context context, int resource,
			ArrayList<Beacon> objects) {
		super(context, resource, objects);
		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View item_beacon_view;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			item_beacon_view = (View) inflater.inflate(R.layout.item_beacon,
					null);
		} else {
			item_beacon_view = convertView;
		}
		// adds the beacon information in the list.
		final Beacon beacon = objects.get(position);
		TextView name = (TextView) item_beacon_view.findViewById(R.id.name);
		TextView prox = (TextView) item_beacon_view.findViewById(R.id.prox);
		name.setText("Name:" + beacon.getName());
		prox.setText("Proximity:" + Utils.computeProximity(beacon));

		return item_beacon_view;
	}
}