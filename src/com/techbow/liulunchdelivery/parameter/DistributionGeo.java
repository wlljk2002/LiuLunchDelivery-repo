package com.techbow.liulunchdelivery.parameter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class DistributionGeo {
	private AVGeoPoint point;
	private String distributionSiteObjectId;
	public DistributionGeo() {
		super();
		// TODO Auto-generated constructor stub
		point = null;
		distributionSiteObjectId = null;
	}
	public AVGeoPoint getPoint() {
		return point;
	}
	public void setPoint(AVGeoPoint point) {
		this.point = point;
	}
	public String getDistributionSiteObjectId() {
		return distributionSiteObjectId;
	}
	public void setDistributionSiteObjectId(String distributionSiteObjectId) {
		this.distributionSiteObjectId = distributionSiteObjectId;
	}
	
	public void saveToCloud() {
		final AVObject distributeSite = new AVObject("DistributionGeo");
		distributeSite.put("point", point);
		distributeSite.put("distributionSiteObjectId", distributionSiteObjectId);
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
