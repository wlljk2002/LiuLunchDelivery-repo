package com.techbow.liulunchdelivery.lunch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.techbow.liulunchdelivery.R;
import com.techbow.liulunchdelivery.parameter.DistributionGeo;
import com.techbow.liulunchdelivery.parameter.DistributionSite;
import com.techbow.liulunchdelivery.parameter.LunchSet;

public class ActivityLunch extends ActionBarActivity implements
		ActionBar.TabListener {
	private PagerAdapterLunch mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private ActionBar actionBar;
	public String distributionSiteObjectId;
	public DistributionSite distributionSite;
	public List<LunchSet> lunchSetList;
	public List<Bitmap> bitmapList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lunch);
		distributionSiteObjectId = getIntent().getStringExtra("distributionSiteObjectId");
		Serializable obj = getIntent().getSerializableExtra("distributionSite");
		distributionSite = (DistributionSite) obj;
		lunchSetList = new ArrayList<LunchSet>();
		bitmapList = new ArrayList<Bitmap>();
		
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        View customerView = getLayoutInflater().inflate(R.layout.actionbar_logo, null);
	    actionBar.setCustomView(customerView);
	    actionBar.setDisplayShowCustomEnabled(true);
	            
		mSectionsPagerAdapter = new PagerAdapterLunch(
				getSupportFragmentManager(), this);
		mViewPager = (ViewPager) findViewById(R.id.lunchPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		new LunchAsyncTask(this, mSectionsPagerAdapter, actionBar).execute();
		
	}
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(arg0.getPosition());
	}
	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
