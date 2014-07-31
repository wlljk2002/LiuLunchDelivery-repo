package com.techbow.liulunchdelivery.List;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.techbow.liulunchdelivery.parameter.DistributionSite;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DistributionListAsyncTask extends AsyncTask<Void, Void, Void> {
	private List<DistributionSite> distributionSiteList;
	private DistributionListViewAdapter distributionListViewAdapter;
	private Context context;

	public DistributionListAsyncTask(
			List<DistributionSite> distributionSiteList,
			DistributionListViewAdapter distributionListViewAdapter,
			Context context) {
		super();
		this.distributionSiteList = distributionSiteList;
		this.distributionListViewAdapter = distributionListViewAdapter;
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		distributionSiteList.clear();	//this would also clear the adapter
		distributionListViewAdapter.notifyDataSetChanged();
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		AVQuery<AVObject> query = new AVQuery<AVObject>("DistributeSite");
		List<AVObject> objects = null;
		query.setLimit(10); // 限制最多10个结果
		try {
			objects = query.find();
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (AVObject obj : objects) {
			DistributionSite site = new DistributionSite();
			site.setName(obj.getString("name"));
			site.setAddress(obj.getString("address"));
			distributionSiteList.add(site);
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (distributionSiteList.size() == 0) {
			Toast.makeText(context, "Network seems not working, please take a check and try again...", Toast.LENGTH_LONG).show();
			return;
		}
		distributionListViewAdapter.notifyDataSetChanged();
	}

}
