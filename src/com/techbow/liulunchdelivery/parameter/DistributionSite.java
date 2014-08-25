package com.techbow.liulunchdelivery.parameter;

import java.io.Serializable;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.parse.ParseException;
import com.parse.ParseObject;

public class DistributionSite implements Serializable{
	private static final long serialVersionUID = 2L;
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
		name = null;
		address = null;
		setTodayObjectId = null;
		setTomorrowObjectId = null;
		setThirdObjectId = null;
		setFourthObjectId = null;
		setFifthObjectId = null;
		thumbnailUrl = null;
	}
	
	public String getSomedayObjectId(int i) {
		String objectId = null;
		switch (i) {
		case 0:
			objectId = setTodayObjectId;
			break;
		case 1:
			objectId = setTomorrowObjectId;
			break;
		case 2:
			objectId = setThirdObjectId;
			break;
		case 3:
			objectId = setFourthObjectId;
			break;
		case 4:
			objectId = setFifthObjectId;
			break;
		default:
			break;
		}
		return objectId;
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
		final ParseObject distributeSite = new ParseObject("DistributeSite");
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
			Log.w("Parse", "object id =" + distributeSite.getObjectId());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return distributeSite.getObjectId();
	}
}
