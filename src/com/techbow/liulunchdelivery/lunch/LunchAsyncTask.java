package com.techbow.liulunchdelivery.lunch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.techbow.liulunchdelivery.R;
import com.techbow.liulunchdelivery.Utils.LoadingAndWaitDialog;
import com.techbow.liulunchdelivery.parameter.DistributionSite;
import com.techbow.liulunchdelivery.parameter.LunchSet;

import android.app.Activity;
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
	private LoadingAndWaitDialog dialog;
	
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
		dialog = new LoadingAndWaitDialog((Activity)context);
		dialog.changeStatusWord("Loading lunch set information, please wait a bit...");
		dialog.show();
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("LunchSet");
		ParseObject lunch;
		try {
			for (int i = 0; i < 5; i++) {
				lunch = query.get(distributionSite.getSomedayObjectId(i));
				LunchSet set = new LunchSet();
				set.setName(lunch.getString("name"));
				set.setPrice(lunch.getString("price"));
				set.setUrl(lunch.getString("url"));
				Bitmap bm = BitmapFactory.decodeStream(new URL(set.getUrl()).openStream());
				lunchSetList.add(set);
				bitmapList.add(bm);
			}
		} catch (ParseException e) {
		    e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (lunchSetList.size() == 0) {
			Toast.makeText(context, "Server is not responding, please take a check and try again...", Toast.LENGTH_LONG).show();
			dialog.hide();
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
		dialog.hide();
	}
}