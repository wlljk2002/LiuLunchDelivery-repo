package com.techbow.liulunchdelivery.parameter;

import java.io.Serializable;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class LunchOrder implements Serializable{
	private static final long serialVersionUID = 3L;
	private String distributionSiteObjectId;
	private String setObjectId;
	private String date;
	private String amount;
	private String phone;
	private String userObjectId;
	
	public LunchOrder() {
		super();
		distributionSiteObjectId = null;
		setObjectId = null;
		date = null;
		amount = null;
		phone = null;
		userObjectId = null;
	}
	
	public String getDistributionSiteObjectId() {
		return distributionSiteObjectId;
	}
	public void setDistributionSiteObjectId(String distributionSiteObjectId) {
		this.distributionSiteObjectId = distributionSiteObjectId;
	}
	public String getSetObjectId() {
		return setObjectId;
	}
	public void setSetObjectId(String setObjectId) {
		this.setObjectId = setObjectId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String mount) {
		this.amount = mount;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserObjectId() {
		return userObjectId;
	}
	public void setUserObjectId(String userObjectId) {
		this.userObjectId = userObjectId;
	}
	
	public void saveToCloud(final Context context) {
		AVObject lunchSet = new AVObject("LunchOrder");
		lunchSet.put("distributionSiteObjectId", distributionSiteObjectId);
		lunchSet.put("setObjectId", setObjectId);
		lunchSet.put("date", date);
		lunchSet.put("amount", amount);
		lunchSet.put("phone", phone);
		lunchSet.put("userObjectId", userObjectId);
		lunchSet.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					Toast.makeText(context, "submit order fail, could be network error!", Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(context, "Congratulations! Your order has been accepted!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
