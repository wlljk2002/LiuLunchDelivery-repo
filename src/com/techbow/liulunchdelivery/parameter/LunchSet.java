package com.techbow.liulunchdelivery.parameter;

import java.io.Serializable;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

public class LunchSet implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String url;
	private String price;
	
	public LunchSet() {
		super();
		// TODO Auto-generated constructor stub
		name = null;
		url = null;
		price = null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	public void saveToCloud() {
		ParseObject lunchSet = new ParseObject("LunchSet");
		lunchSet.put("name", name);
		lunchSet.put("url", url);
		lunchSet.put("price", price);
		lunchSet.saveInBackground(new com.parse.SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					Log.w("Parse", "save in background fail");
				}
			}
		});
	}
}
