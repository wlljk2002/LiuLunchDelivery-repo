package com.techbow.liulunchdelivery.listmap;

import java.util.List;

import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
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
		
		// first we need to get mapview reference from context, complicated...
		ActivityListMap activity = (ActivityListMap) context;
		ViewPager pager = (ViewPager)activity.findViewById(R.id.listMapPager);
		FragmentPagerAdapter f = (FragmentPagerAdapter) pager.getAdapter();
		FragmentMap fragmentMap = (FragmentMap)f.instantiateItem(pager, 1);
		mapView = fragmentMap.mMapView;
		mapView.getMap().clear();
		dialog = fragmentMap.dialog;
		dialog.changeStatusWord("Loading near lunch distibution sites, please wait...");
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		ParseGeoPoint curLoc = new ParseGeoPoint(FragmentMap.locationData.latitude, FragmentMap.locationData.longitude);
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
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        //构建Marker图标  
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.maker);
        for (int i = 0; i < distributionGeoList.size(); i++) {
			// 位置  
            latLng = new LatLng(distributionGeoList.get(i).getPoint().getLatitude(), distributionGeoList.get(i).getPoint().getLongitude());  
            // 图标  
            overlayOptions = new MarkerOptions().position(latLng).icon(markerIcon).zIndex(5);  
            marker = (Marker) (mapView.getMap().addOverlay(overlayOptions));  
            Bundle bundle = new Bundle();
            bundle.putSerializable("distributionSite", distributionSiteList.get(i));
            bundle.putString("distributionSiteObjectId", distributionGeoList.get(i).getDistributionSiteObjectId());
            marker.setExtraInfo(bundle);
		}
        //对Marker的点击
        mapView.getMap().setOnMarkerClickListener(new OnMarkerClickListener()
  		{
  			@Override
  			public boolean onMarkerClick(final Marker marker)
  			{
  				//获得marker中的数据
  				DistributionSite site = (DistributionSite) marker.getExtraInfo().getSerializable("distributionSite");
				//Toast.makeText(context, "site " + site.getName() + " is chosen", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, ActivityLunch.class);
				intent.putExtra("distributionSite", site);
				intent.putExtra("distributionSiteObjectId", marker.getExtraInfo().getString("distributionSiteObjectId"));
				context.startActivity(intent);
  				return true;
  			}
  		});
        LatLng point = new LatLng(distributionGeoList.get(0).getPoint().getLatitude(), distributionGeoList.get(0).getPoint().getLongitude());
		
		// 基于原来的status上改变指定的参数
		MapStatus mapStatus = new MapStatus.Builder(mapView.getMap().getMapStatus()).target(point).build();
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
		mapView.getMap().animateMapStatus(mapStatusUpdate); // 改变,这里设置成随着移动而改变位置
		
		dialog.hide();
	}
}
