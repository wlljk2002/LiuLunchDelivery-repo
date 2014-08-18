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
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.techbow.liulunchdelivery.R;

public class FragmentMap extends Fragment {
	MapView mMapView = null;
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	/** ��ǰ��λ������ */
	static public MyLocationData locationData = null;
	private boolean isFirstLocate = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		mMapView = (MapView) rootView.findViewById(R.id.bmapView);
		
		mLocationClient = new LocationClient(getActivity().getApplicationContext());     //����LocationClient��
	    mLocationClient.registerLocationListener( myListener );    //ע���������
	    mLocationClient.start();
	    LocationClientOption option = new LocationClientOption();
	    option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
	    option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
	    option.setOpenGps(true); // ��GPS
	    option.setTimeOut(10 * 1000); // ��ʱ
	    option.setScanSpan(60 * 1000);//���÷���λ����ļ��ʱ��Ϊ5000ms
	    option.setIsNeedAddress(true);//���صĶ�λ���������ַ��Ϣ
	    option.setNeedDeviceDirect(true);//���صĶ�λ��������ֻ���ͷ�ķ���
	    mLocationClient.setLocOption(option);
	    if (mLocationClient != null && mLocationClient.isStarted())
	    	mLocationClient.requestLocation();
	    else 
	    	Log.w("LocSDK4", "locClient is null or not started");
	    
	    // ������λͼ��
	    mMapView.getMap().setMyLocationEnabled(true);
		// ���ðٶȵ�ͼ��һЩ״̬,�Ŵ�ȼ�,
	    mMapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder(mMapView.getMap().getMapStatus()).zoom(16).build())); // ���÷Ŵ�ȼ�Ϊ16
		// ���ö�λͼ����ʾ��ʽ,һ��Ҫ�����˶�λͼ���������
	    mMapView.getMap().setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null)); // ����Ϊ����ģʽ,��ʾ�ֻ�����ļ�ͷ
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
			sb.append(location.getLocType()); // ��λ��ʽ
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude()); // γ��
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude()); // ����
			sb.append("\nradius : ");
			sb.append(location.getRadius()); // ��λ����
			sb.append("\ndirection : ");
			sb.append(location.getDirection()); // �ֻ���ǰ����
			sb.append("\naltitude : ");
			sb.append(location.getAltitude()); // �߶�
			sb.append("\bspeed : ");
			sb.append(location.getSpeed()); // �ٶ�
			sb.append("\noperators : ");
			sb.append(location.getOperators());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber()); // ������
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr()); // ��ϸ��ַ��Ϣ
				sb.append("\nprovince:");
				sb.append(location.getProvince()); // ʡ
				sb.append("\ncity:");
				sb.append(location.getCity()); // ����
				sb.append("\ndistrict:");
				sb.append(location.getDistrict()); // ����
			}
			Log.w("baiduLoc", sb.toString());
			locationData = new MyLocationData.Builder().accuracy(location.getRadius()) // ��λ�ľ�ȷ��
				.direction(location.getDirection()) // ��λ�ķ��� 0~360��
				.latitude(location.getLatitude()).longitude(location.getLongitude()) // ��γ��
				.speed(location.getSpeed()).build();
			if(isFirstLocate) {
				FragmentList.asyncTask.execute();
				isFirstLocate = false;
			}
			
			// ���ö�λ���ݵ���λͼ����,������ʾ��ǰλ�õ��Ǹ����ԲȦ
			mMapView.getMap().setMyLocationData(locationData);
 
			LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
			
			// ����ԭ����status�ϸı�ָ���Ĳ���
			MapStatus mapStatus = new MapStatus.Builder(mMapView.getMap().getMapStatus()).target(point).build();
			MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
			mMapView.getMap().animateMapStatus(mapStatusUpdate); // �ı�,�������ó������ƶ����ı�λ��
 			
// 			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					DistributionSite distribution = new DistributionSite();
//					distribution.setName("�Ϻ�����");
//					distribution.setAddress("����������·800��");
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
