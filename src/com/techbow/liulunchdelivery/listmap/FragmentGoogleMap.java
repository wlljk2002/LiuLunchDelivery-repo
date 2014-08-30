package com.techbow.liulunchdelivery.listmap;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.parse.ParseGeoPoint;
import com.techbow.liulunchdelivery.R;
import com.techbow.liulunchdelivery.Utils.LoadingAndWaitDialog;
import com.techbow.liulunchdelivery.parameter.DistributionGeo;
import com.techbow.liulunchdelivery.parameter.DistributionSite;

public class FragmentGoogleMap extends Fragment
	implements
	ConnectionCallbacks,
	OnConnectionFailedListener,
	LocationListener {
	private MapView mapView;
	private GoogleMap map;
	private LocationClient locationClient;
    // These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_google_map, container, false);
		mapView = (MapView) rootView.findViewById(R.id.googleMap);
		mapView.onCreate(savedInstanceState);
		map = mapView.getMap();
		if (map != null) {
            map.setMyLocationEnabled(true);
        }
		locationClient = new LocationClient(
                getActivity().getApplicationContext(),
                this,  // ConnectionCallbacks
                this); // OnConnectionFailedListener
		return rootView;
	}
	@Override
	public void onResume() {
        super.onResume();
        mapView.onResume();
        if (locationClient != null) {
        	locationClient.connect();
        }
    }
	@Override
    public void onPause() {
		mapView.onPause();
		super.onPause();
        if (locationClient != null) {
            locationClient.disconnect();
        }
    }
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mapView.onDestroy();
		super.onDestroy();
	}
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		mapView.onLowMemory();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		locationClient.requestLocationUpdates(
                REQUEST,
                this);  // LocationListener
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}
}