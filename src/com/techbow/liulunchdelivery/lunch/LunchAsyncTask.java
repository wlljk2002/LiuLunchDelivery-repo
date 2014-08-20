package com.techbow.liulunchdelivery.lunch;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.techbow.liulunchdelivery.R;
import com.techbow.liulunchdelivery.parameter.DistributionSite;
import com.techbow.liulunchdelivery.parameter.LunchSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.widget.TextView;
import android.widget.Toast;

public class LunchAsyncTask extends AsyncTask<Void, Void, Void> {
	private Context context;
	private DistributionSite distributionSite;
	private List<LunchSet> lunchSetList;
	private List<Bitmap> bitmapList;
	private PagerAdapterLunch pagerAdapter;
	private ActionBar actionBar;
	
	public LunchAsyncTask(Context context, PagerAdapterLunch pagerAdapter, ActionBar actionBar) {
		super();
		this.context = context;
		this.pagerAdapter = pagerAdapter;
		this.actionBar = actionBar;
		distributionSite = ((ActivityLunch)context).distributionSite;
		lunchSetList = ((ActivityLunch)context).lunchSetList;
		bitmapList = ((ActivityLunch)context).bitmapList;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		lunchSetList.clear();
		bitmapList.clear();
		pagerAdapter.notifyDataSetChanged();
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		AVQuery<AVObject> query = new AVQuery<AVObject>("LunchSet");
		AVObject lunch;
		AVFile pic;
		try {
			for (int i = 0; i < 5; i++) {
				lunch = query.get(distributionSite.getSomedayObjectId(i));
				LunchSet set = new LunchSet();
				set.setName(lunch.getString("name"));
				set.setObjectId(lunch.getString("picObjectId"));
				set.setPrice(lunch.getString("price"));
				set.setThumbnailUrl(lunch.getString("thumbnailUrl"));
				pic = AVFile.withObjectId(set.getObjectId());
				Bitmap bm = BitmapFactory.decodeStream(new ByteArrayInputStream(pic.getData()));
				lunchSetList.add(set);
				bitmapList.add(bm);
			}
		} catch (AVException e) {
		    e.printStackTrace();
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (lunchSetList.size() == 0) {
			Toast.makeText(context, "Network seems not working, please take a check and try again...", Toast.LENGTH_LONG).show();
			return;
		}
		pagerAdapter.notifyDataSetChanged();
		for (int i = 0; i < pagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			ActionBar.Tab tab = actionBar.newTab().setCustomView(R.layout.actionbar_tab);
			TextView text = (TextView) tab.getCustomView().findViewById(R.id.tab_title);
			text.setText(pagerAdapter.getPageTitle(i));
			tab.setTabListener((ActivityLunch)context);
			actionBar.addTab(tab);
		}
	}
}