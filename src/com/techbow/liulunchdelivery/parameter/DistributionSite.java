package com.techbow.liulunchdelivery.parameter;

import java.io.Serializable;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;

public class DistributionSite implements Serializable{
	private static final long serialVersionUID = 2L;
	private String geoPointObjectId;
	private String name;
	private String address;
	private String setTodayObjectId;
	private String setTomorrowObjectId;
	private String setThirdObjectId;
	private String setFourthObjectId;
	private String setFifthObjectId;
	private String thumbnailUrl;
	
	public DistributionSite() {
		super();
		geoPointObjectId = null;
		name = null;
		address = null;
		setTodayObjectId = null;
		setTomorrowObjectId = null;
		setThirdObjectId = null;
		setFourthObjectId = null;
		setFifthObjectId = null;
		thumbnailUrl = null;
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
	public String getSetThirdObjectId() {
		return setThirdObjectId;
	}
	public void setSetThirdObjectId(String setThirdObjectId) {
		this.setThirdObjectId = setThirdObjectId;
	}
	public String getSetFourthObjectId() {
		return setFourthObjectId;
	}
	public void setSetFourthObjectId(String setFourthObjectId) {
		this.setFourthObjectId = setFourthObjectId;
	}
	public String getSetFifthObjectId() {
		return setFifthObjectId;
	}
	public void setSetFifthObjectId(String setFifthObjectId) {
		this.setFifthObjectId = setFifthObjectId;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String saveToCloud() {
		final AVObject distributeSite = new AVObject("DistributeSite");
		distributeSite.put("geoPointObjectId", geoPointObjectId);
		distributeSite.put("name", name);
		distributeSite.put("address", address);
		distributeSite.put("setTodayObjectId", setTodayObjectId);
		distributeSite.put("setTomorrowObjectId", setTomorrowObjectId);
		distributeSite.put("setThirdObjectId", setThirdObjectId);
		distributeSite.put("setFourthObjectId", setFourthObjectId);
		distributeSite.put("setFifthObjectId", setFifthObjectId);
		distributeSite.put("thumbnailUrl", thumbnailUrl);
//		distributeSite.saveInBackground(new SaveCallback() {
//			
//			@Override
//			public void done(AVException e) {
//				// TODO Auto-generated method stub
//				if (e == null) {
//					Log.w("Avos", "object id =" + distributeSite.getObjectId());
//				} else {
//					Log.w("Avos", "save in background fail");
//				}
//			}
//		});
		try {
			distributeSite.save();
			Log.w("Avos", "object id =" + distributeSite.getObjectId());
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return distributeSite.getObjectId();
	}
}
