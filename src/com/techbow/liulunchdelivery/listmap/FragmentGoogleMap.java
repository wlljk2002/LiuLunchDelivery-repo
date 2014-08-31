package com.techbow.liulunchdelivery.listmap;

import org.apache.http.util.LangUtils;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
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
	public MapView mapView;
	private GoogleMap map;
	private LocationClient locationClient;
	private boolean isFirstLocate = true;
	public LoadingAndWaitDialog dialog;
	public static LatLng curPosition;
	
    // These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(60000)         // 5 seconds
            .setFastestInterval(5000)    // 16ms = 60fps
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
            map.setBuildingsEnabled(true);
            MapsInitializer.initialize(getActivity());
        }
		locationClient = new LocationClient(
                getActivity().getApplicationContext(),
                this,  // ConnectionCallbacks
                this); // OnConnectionFailedListener
		
		dialog = new LoadingAndWaitDialog(getActivity());
		dialog.changeStatusWord("Locating your position, please wait...");
		dialog.show();
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
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location == null)
            return ;
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude()); // 纬度
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude()); // 经度
		sb.append("\naltitude : ");
		sb.append(location.getAltitude()); // 高度
		sb.append("\nspeed : ");
		sb.append(location.getSpeed()); // 速度
		Log.w("google location", sb.toString());
		if(isFirstLocate) {
			FragmentList.asyncTask.execute();
			isFirstLocate = false;
			
			curPosition = new LatLng(location.getLatitude(), location.getLongitude());
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(curPosition, 16); //zoom level 16
			map.moveCamera(update);
		}
	}
}