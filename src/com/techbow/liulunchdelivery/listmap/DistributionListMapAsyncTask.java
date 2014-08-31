package com.techbow.liulunchdelivery.listmap;

import java.util.List;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.techbow.liulunchdelivery.ActivityListMap;
import com.techbow.liulunchdelivery.R;
import com.techbow.liulunchdelivery.Utils.LoadingAndWaitDialog;
import com.techbow.liulunchdelivery.lunch.ActivityLunch;
import com.techbow.liulunchdelivery.parameter.DistributionGeo;
import com.techbow.liulunchdelivery.parameter.DistributionSite;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

public class DistributionListMapAsyncTask extends AsyncTask<Void, Void, Void> {
	private List<DistributionSite> distributionSiteList;
	private List<DistributionGeo> distributionGeoList;
	private DistributionListViewAdapter distributionListViewAdapter;
	private Context context;
	private MapView mapView;
	private LoadingAndWaitDialog dialog;

	public DistributionListMapAsyncTask(
			List<DistributionSite> distributionSiteList,
			List<DistributionGeo> distributionGeoList,
			DistributionListViewAdapter distributionListViewAdapter,
			Context context) {
		super();
		this.distributionSiteList = distributionSiteList;
		this.distributionGeoList = distributionGeoList;
		this.distributionListViewAdapter = distributionListViewAdapter;
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		distributionSiteList.clear();	//this would also clear the adapter
		distributionGeoList.clear();
		distributionListViewAdapter.notifyDataSetChanged();
		
		// first we need to get MapView reference from context, complicated...
		ActivityListMap activity = (ActivityListMap) context;
		ViewPager pager = (ViewPager)activity.findViewById(R.id.listMapPager);
		FragmentPagerAdapter f = (FragmentPagerAdapter) pager.getAdapter();
		FragmentGoogleMap fragmentMap = (FragmentGoogleMap)f.instantiateItem(pager, 1);
		mapView = fragmentMap.mapView;
		mapView.getMap().clear();
		dialog = fragmentMap.dialog;
		dialog.changeStatusWord("Loading near lunch distibution sites, please wait...");
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		ParseGeoPoint curLoc = new ParseGeoPoint(FragmentGoogleMap.curPosition.latitude, FragmentGoogleMap.curPosition.longitude);
		ParseQuery<ParseObject> queryGeo = new ParseQuery<ParseObject>("DistributionGeo");
		queryGeo.whereNear("point", curLoc);
		queryGeo.setLimit(6); //获取最接近用户地点的6条数据
		List<ParseObject> nearPlaces = null;
		try {
			nearPlaces = queryGeo.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		for (ParseObject place : nearPlaces) {
			DistributionGeo geo = new DistributionGeo();
			geo.setPoint(place.getParseGeoPoint("point"));
			geo.setDistributionSiteObjectId(place.getString("distributionSiteObjectId"));
			Log.w("Geo", "Geo point (" + geo.getPoint().getLatitude() + "," + geo.getPoint().getLongitude() + ")");
			distributionGeoList.add(geo);
			
			ParseQuery<ParseObject> querySite = new ParseQuery<ParseObject>("DistributeSite");
			ParseObject objSite = null;
			try {
				objSite = querySite.get(geo.getDistributionSiteObjectId());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DistributionSite site = new DistributionSite();
			site.setName(objSite.getString("name"));
			site.setAddress(objSite.getString("address"));
			site.setSetTodayObjectId(objSite.getString("setTodayObjectId"));
			site.setSetTomorrowObjectId(objSite.getString("setTomorrowObjectId"));
			site.setSetThirdObjectId(objSite.getString("setThirdObjectId"));
			site.setSetFourthObjectId(objSite.getString("setFourthObjectId"));
			site.setSetFifthObjectId(objSite.getString("setFifthObjectId"));
			site.setThumbnailUrl(objSite.getString("thumbnailUrl"));
			distributionSiteList.add(site);
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (distributionSiteList.size() == 0) {
			Toast.makeText(context, "Network seems not working, please take a check and try again...", Toast.LENGTH_LONG).show();
			dialog.hide();
			return;
		}
		distributionListViewAdapter.notifyDataSetChanged();
		
		LatLng latLng = null;
        Marker marker = null;
        //构建Marker图标  
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.maker);
        for (int i = 0; i < distributionGeoList.size(); i++) {
			// 位置  
            latLng = new LatLng(distributionGeoList.get(i).getPoint().getLatitude(), distributionGeoList.get(i).getPoint().getLongitude());  
            // 图标
            marker = mapView.getMap().addMarker(new MarkerOptions()
	            .position(latLng)
	            .snippet(String.valueOf(i))
	            .icon(markerIcon));
        }
        //对Marker的点击
        mapView.getMap().setOnMarkerClickListener(new OnMarkerClickListener()
  		{
  			@Override
  			public boolean onMarkerClick(Marker marker)
  			{
  				//获得marker中的数据
  				int idx = Integer.valueOf(marker.getSnippet()).intValue();
  				DistributionSite site = distributionSiteList.get(idx);
				//Toast.makeText(context, "site " + site.getName() + " is chosen", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, ActivityLunch.class);
				intent.putExtra("distributionSite", site);
				intent.putExtra("distributionSiteObjectId", distributionGeoList.get(idx).getDistributionSiteObjectId());
				context.startActivity(intent);
  				return true;
  			}
  		});
        LatLng point = new LatLng(distributionGeoList.get(0).getPoint().getLatitude(), distributionGeoList.get(0).getPoint().getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(point, 16); //zoom level 16
        mapView.getMap().moveCamera(update);
        
		dialog.hide();
	}
}
