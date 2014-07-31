package com.techbow.liulunchdelivery.List;

import java.util.List;

import com.techbow.liulunchdelivery.parameter.DistributionSite;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DistributionListViewAdapter extends BaseAdapter {
	private List<DistributionSite> distributionSiteList;
	private Context context;

	public DistributionListViewAdapter(List<DistributionSite> distributionSiteList,
			Context context) {
		super();
		this.distributionSiteList = distributionSiteList;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return distributionSiteList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return distributionSiteList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		DistributionListItem item = new DistributionListItem(context);
		item.updateview(distributionSiteList.get(arg0));
		return item;
	}

}
