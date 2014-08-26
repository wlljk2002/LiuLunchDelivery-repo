package com.techbow.liulunchdelivery.parameter;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class DistributionGeo {
	private ParseGeoPoint point;
	private String distributionSiteObjectId;
	public DistributionGeo() {
		super();
		// TODO Auto-generated constructor stub
		point = null;
		distributionSiteObjectId = null;
	}
	public ParseGeoPoint getPoint() {
		return point;
	}
	public void setPoint(ParseGeoPoint point) {
		this.point = point;
	}
	public String getDistributionSiteObjectId() {
		return distributionSiteObjectId;
	}
	public void setDistributionSiteObjectId(String distributionSiteObjectId) {
		this.distributionSiteObjectId = distributionSiteObjectId;
	}
	
	public void saveToCloud() {
		final ParseObject distributeSite = new ParseObject("DistributionGeo");
		distributeSite.put("point", point);
		distributeSite.put("distributionSiteObjectId", distributionSiteObjectId);
		distributeSite.saveInBackground(new com.parse.SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					Log.w("Parse", "object id =" + distributeSite.getObjectId());
				} else {
					Log.w("Parse", "save in background fail");
				}
			}
		});
	}
}
