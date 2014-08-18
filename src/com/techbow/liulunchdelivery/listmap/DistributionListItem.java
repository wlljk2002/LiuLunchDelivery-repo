package com.techbow.liulunchdelivery.listmap;


import com.techbow.liulunchdelivery.R;
import com.techbow.liulunchdelivery.parameter.DistributionSite;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DistributionListItem extends LinearLayout {
	private ImageView imageView;
	private TextView name;
	private TextView address;
	private TextView lunchName;
		
	public DistributionListItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		View view = LayoutInflater.from(context).inflate(R.layout.distribution_list_item, null);
		imageView = (ImageView) view.findViewById(R.id.lunchTodayPic);
		name = (TextView) view.findViewById(R.id.distributionName);
		address = (TextView) view.findViewById(R.id.distributionAddress);
		lunchName = (TextView) view.findViewById(R.id.lunchName);
		addView(view);
	}
	public void updateview (DistributionSite site) {
		name.setText(site.getName());
		address.setText(site.getAddress());
	}
}
