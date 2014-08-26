package com.techbow.liulunchdelivery.listmap;

import java.io.FileNotFoundException;

import android.os.Bundle;
import android.os.Looper;
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
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.parse.ParseGeoPoint;
import com.techbow.liulunchdelivery.R;
import com.techbow.liulunchdelivery.Utils.LoadingAndWaitDialog;
import com.techbow.liulunchdelivery.parameter.DistributionGeo;
import com.techbow.liulunchdelivery.parameter.DistributionSite;

public class FragmentMap extends Fragment {
	public MapView mMapView = null;
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	/** 当前定位的数据 */
	static public MyLocationData locationData = null;
	private boolean isFirstLocate = true;
	public LoadingAndWaitDialog dialog;
	
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
	    option.setOpenGps(true); // 打开GPS
	    option.setTimeOut(10 * 1000); // 超时
	    //option.setScanSpan(60 * 1000);//设置发起定位请求的间隔时间为5000ms
	    option.setIsNeedAddress(true);//返回的定位结果包含地址信息
	    option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
	    mLocationClient.setLocOption(option);
	    if (mLocationClient != null && mLocationClient.isStarted())
	    	mLocationClient.requestLocation();
	    else 
	    	Log.w("LocSDK4", "locClient is null or not started");
	    
	    // 开启定位图层
	    mMapView.getMap().setMyLocationEnabled(true);
		// 设置百度地图的一些状态,放大等级,
	    mMapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder(mMapView.getMap().getMapStatus()).zoom(16).build())); // 设置放大等级为16
		// 配置定位图层显示方式,一定要开启了定位图层后再设置
	    mMapView.getMap().setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null)); // 设置为跟随模式,显示手机方向的箭头
		dialog = new LoadingAndWaitDialog(getActivity());
		dialog.changeStatusWord("Locating your position, please wait...");
		dialog.show();
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
			sb.append(location.getLocType()); // 定位方式
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude()); // 纬度
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude()); // 经度
			sb.append("\nradius : ");
			sb.append(location.getRadius()); // 定位精度
			sb.append("\ndirection : ");
			sb.append(location.getDirection()); // 手机当前方向
			sb.append("\naltitude : ");
			sb.append(location.getAltitude()); // 高度
			sb.append("\bspeed : ");
			sb.append(location.getSpeed()); // 速度
			sb.append("\noperators : ");
			sb.append(location.getOperators());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber()); // 卫星数
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr()); // 详细地址信息
				sb.append("\nprovince:");
				sb.append(location.getProvince()); // 省
				sb.append("\ncity:");
				sb.append(location.getCity()); // 城市
				sb.append("\ndistrict:");
				sb.append(location.getDistrict()); // 区县
			}
			Log.w("baiduLoc", sb.toString());
			locationData = new MyLocationData.Builder().accuracy(location.getRadius()) // 定位的精确度
				.direction(location.getDirection()) // 定位的方向 0~360度
				.latitude(location.getLatitude()).longitude(location.getLongitude()) // 经纬度
				.speed(location.getSpeed()).build();
			if(isFirstLocate) {
				FragmentList.asyncTask.execute();
				isFirstLocate = false;
			}
			
			// 设置定位数据到定位图层上,就是显示当前位置的那个点和圆圈
			mMapView.getMap().setMyLocationData(locationData);
 
			LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
			
			// 基于原来的status上改变指定的参数
			MapStatus mapStatus = new MapStatus.Builder(mMapView.getMap().getMapStatus()).target(point).build();
			MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
			mMapView.getMap().animateMapStatus(mapStatusUpdate); // 改变,这里设置成随着移动而改变位置
 			
// 			for (int i = 0; i < 11; i++) {
// 				final int idx = i;
// 				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						Looper.prepare();
//						final DistributionSite distribution = new DistributionSite();
//						distribution.setSetTodayObjectId("J7YJeHaJKQ");
//						distribution.setSetTomorrowObjectId("J7YJeHaJKQ");
//						distribution.setSetThirdObjectId("J7YJeHaJKQ");
//						distribution.setSetFourthObjectId("J7YJeHaJKQ");
//						distribution.setSetFifthObjectId("J7YJeHaJKQ");
//						distribution.setThumbnailUrl("http://files.parsetfss.com/146d6cba-218a-4d4b-b4b9-e2201de92be7/tfss-1498454a-5406-4cb2-b199-8fcbced7e82b-file");
//						
//						final DistributionGeo geo = new DistributionGeo();
//						double lat = locationData.latitude + ((double)idx * 0.001);
//						double lon = locationData.longitude + ((double)idx * 0.001);
//						geo.setPoint(new ParseGeoPoint(lat, lon));
//						distribution.setName("上海交大" + idx);
//						GeoCoder geoCoder = GeoCoder.newInstance();
//						geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(lat, lon)));
//						geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
//							@Override
//							public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
//								// TODO Auto-generated method stub
//								distribution.setAddress(arg0.getAddress());
//								geo.setDistributionSiteObjectId(distribution.saveToCloud());
//								geo.saveToCloud();
//							}
//							@Override
//							public void onGetGeoCodeResult(GeoCodeResult arg0) {
//								// TODO Auto-generated method stub
//							}
//						});
//						Looper.loop();
//					}
//				}).start();
// 				try {
//					Thread.sleep(4000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
// 			}
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
