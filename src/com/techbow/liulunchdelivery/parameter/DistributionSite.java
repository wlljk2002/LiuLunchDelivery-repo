package com.techbow.liulunchdelivery.parameter;

import java.io.Serializable;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class DistributionSite implements Serializable{
	private static final long serialVersionUID = 2L;
	private String geoPointObjectId;
	private String name;
	private String address;
	private String setYesterdayObjectId;
	private String setTodayObjectId;
	private String setTomorrowObjectId;
	
	public DistributionSite() {
		super();
		geoPointObjectId = null;
		name = null;
		address = null;
		setYesterdayObjectId = null;
		setTodayObjectId = null;
		setTomorrowObjectId = null;
	}
	
	public String getGeoPointObjectId() {
		return geoPointObjectId;
	}
	public void setGeoPointObjectId(String geoPointObjectId) {
		this.geoPointObjectId = geoPointObjectId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSetYesterdayObjectId() {
		return setYesterdayObjectId;
	}
	public void setSetYesterdayObjectId(String setYesterdayObjectId) {
		this.setYesterdayObjectId = setYesterdayObjectId;
	}
	public String getSetTodayObjectId() {
		return setTodayObjectId;
	}
	public void setSetTodayObjectId(String setTodayObjectId) {
		this.setTodayObjectId = setTodayObjectId;
	}
	public String getSetTomorrowObjectId() {
		return setTomorrowObjectId;
	}
	public void setSetTomorrowObjectId(String setTomorrowObjectId) {
		this.setTomorrowObjectId = setTomorrowObjectId;
	}
	
	public void saveToCloud() {
		final AVObject distributeSite = new AVObject("DistributeSite");
		distributeSite.put("geoPointObjectId", geoPointObjectId);
		distributeSite.put("name", name);
		distributeSite.put("address", address);
		distributeSite.put("setYesterdayObjectId", setYesterdayObjectId);
		distributeSite.put("setTodayObjectId", setTodayObjectId);
		distributeSite.put("setTomorrowObjectId", setTomorrowObjectId);
		distributeSite.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					Log.w("Avos", "object id =" + distributeSite.getObjectId());
				} else {
					Log.w("Avos", "save in background fail");
				}
			}
		});
	}
}
