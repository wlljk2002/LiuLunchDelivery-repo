package com.techbow.liulunchdelivery.listmap;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.techbow.liulunchdelivery.ActivityListMap;
import com.techbow.liulunchdelivery.R;
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
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		AVGeoPoint curLoc = new AVGeoPoint(FragmentMap.locationData.latitude, FragmentMap.locationData.longitude);
		AVQuery<AVObject> queryGeo = new AVQuery<AVObject>("DistributionGeo");
		queryGeo.whereNear("point", curLoc);
		queryGeo.setLimit(6); //获取最接近用户地点的6条数据
		List<AVObject> nearPlaces = null;
		try {
			nearPlaces = queryGeo.find();
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		for (AVObject place : nearPlaces) {
			DistributionGeo geo = new DistributionGeo();
			geo.setPoint(place.getAVGeoPoint("point"));
			geo.setDistributionSiteObjectId(place.getString("distributionSiteObjectId"));
			Log.w("Geo", "Geo point (" + geo.getPoint().getLatitude() + "," + geo.getPoint().getLongitude() + ")");
			distributionGeoList.add(geo);
			
			AVQuery<AVObject> querySite = new AVQuery<AVObject>("DistributeSite");
			AVObject objSite = null;
			try {
				objSite = querySite.get(geo.getDistributionSiteObjectId());
			} catch (AVException e) {
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
		
//		AVQuery<AVObject> query = new AVQuery<AVObject>("DistributeSite");
//		List<AVObject> objects = null;
//		query.setLimit(10);
//		try {
//			objects = query.find();
//		} catch (AVException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		for (AVObject obj : objects) {
//			DistributionSite site = new DistributionSite();
//			site.setName(obj.getString("name"));
//			site.setAddress(obj.getString("address"));
//			distributionSiteList.add(site);
//		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (distributionSiteList.size() == 0) {
			Toast.makeText(context, "Network seems not working, please take a check and try again...", Toast.LENGTH_LONG).show();
			return;
		}
		distributionListViewAdapter.notifyDataSetChanged();
		
		// first we need to get mapview reference from context, complicated...
		ActivityListMap activity = (ActivityListMap) context;
		ViewPager pager = (ViewPager)activity.findViewById(R.id.listMapPager);
		FragmentPagerAdapter f = (FragmentPagerAdapter) pager.getAdapter();
		FragmentMap fragmentMap = (FragmentMap)f.instantiateItem(pager, 1);
		mapView = fragmentMap.mMapView;
		mapView.getMap().clear();
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
				context.startActivity(intent);
  				return true;
  			}
  		});
	}

}
