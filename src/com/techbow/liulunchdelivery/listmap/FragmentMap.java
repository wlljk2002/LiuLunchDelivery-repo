package com.techbow.liulunchdelivery.listmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.MapView;
import com.techbow.liulunchdelivery.R;

public class FragmentMap extends Fragment {
	MapView mMapView = null;
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	static public double lat, lon;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		mMapView = (MapView) rootView.findViewById(R.id.bmapView);
		
		mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
	    mLocationClient.registerLocationListener( myListener );    //注册监听函数
	    mLocationClient.start();
	    LocationClientOption option = new LocationClientOption();
	    option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
	    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
	    //option.setOpenGps(true);
	    //option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
	    option.setIsNeedAddress(true);//返回的定位结果包含地址信息
	    option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
	    mLocationClient.setLocOption(option);
	    if (mLocationClient != null && mLocationClient.isStarted())
	    	mLocationClient.requestLocation();
	    else 
	    	Log.w("LocSDK4", "locClient is null or not started");
		return rootView;
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
		            return ;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			//final double lat, lon;
			lat = location.getLatitude();
			lon = location.getLongitude();
			sb.append("\nlatitude : ");
			sb.append(lat);
			sb.append("\nlontitude : ");
			sb.append(lon);
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			} 
 			Log.w("baiduLoc", sb.toString());
 			
 			FragmentList.asyncTask.execute();
 			
// 			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					DistributionSite distribution = new DistributionSite();
//					distribution.setName("上海交大");
//					distribution.setAddress("闽行区东川路800号");
//					distribution.setSetTodayObjectId("53ee1a09e4b00eb68958aa0f");
//					distribution.setSetTomorrowObjectId("53ee1a09e4b00eb68958aa0f");
//					distribution.setSetThirdObjectId("53ee1a09e4b00eb68958aa0f");
//					distribution.setSetFourthObjectId("53ee1a09e4b00eb68958aa0f");
//					distribution.setSetFifthObjectId("53ee1a09e4b00eb68958aa0f");
//					DistributionGeo geo = new DistributionGeo();
//					for(int i = 0; i < 11; i++) {
//						
//						geo.setPoint(new AVGeoPoint(lat + (i * 0.00001), lon + (i * 0.00001)));
//						geo.setDistributionSiteObjectId(distribution.saveToCloud());
//						geo.saveToCloud();
//					}
//				}
//			}).start();

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.onDestroy();
		mLocationClient.stop();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
	}

}
