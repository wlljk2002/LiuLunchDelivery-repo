package com.techbow.liulunchdelivery.parameter;

import java.io.Serializable;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class LunchSet implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String picObjectId;	//according AVFile pic;
	private String thumbnailUrl;
	private String price;
	
	public LunchSet() {
		super();
		// TODO Auto-generated constructor stub
		name = null;
		picObjectId = null;
		thumbnailUrl = null;
		price = null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getObjectId() {
		return picObjectId;
	}
	public void setObjectId(String objectId) {
		this.picObjectId = objectId;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	public void saveToCloud() {
		AVObject lunchSet = new AVObject("LunchSet");
		lunchSet.put("name", name);
		lunchSet.put("picObjectId", picObjectId);
		lunchSet.put("thumbnailUrl", thumbnailUrl);
		lunchSet.put("price", price);
		lunchSet.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					Log.w("Avos", "save in background fail");
				}
			}
		});
	}
}
