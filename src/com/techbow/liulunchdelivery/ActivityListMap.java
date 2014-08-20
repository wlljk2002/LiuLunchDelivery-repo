package com.techbow.liulunchdelivery;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.baidu.mapapi.SDKInitializer;
import com.techbow.liulunchdelivery.Utils.FileAccessor;
import com.techbow.liulunchdelivery.parameter.LunchSet;

public class ActivityListMap extends ActionBarActivity implements
		ActionBar.TabListener, NavigationDrawerFragment.NavigationDrawerCallbacks{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private PagerAdapterListMap mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private CustomerViewPager mViewPager;
	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	private ActionBar actionBar;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.activity_list_map);
		
		// Set up the action bar.
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//actionBar.setDisplayShowTitleEnabled(false);
		View customerView = getLayoutInflater().inflate(R.layout.actionbar_logo, null);
	    actionBar.setCustomView(customerView);
	    actionBar.setDisplayShowCustomEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new PagerAdapterListMap(
				getSupportFragmentManager(), this);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (CustomerViewPager) findViewById(R.id.listMapPager);
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
		//���´�����ͼʹmap view���ᱻviewpager��ҳ��Ч�����ã�����
//		mViewPager.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if (mViewPager.getCurrentItem() == 1) {
//					return true;
//				}
//				return false;
//			}
//		});
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			ActionBar.Tab tab = actionBar.newTab().setCustomView(R.layout.actionbar_tab);
			TextView text = (TextView) tab.getCustomView().findViewById(R.id.tab_title);
			text.setText(mSectionsPagerAdapter.getPageTitle(i));
			tab.setTabListener(this);
			actionBar.addTab(tab);
		}

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		AVOSCloud.initialize(this, "g6giwuvkt2mhbhh8c3bwmozzloys0gsypjq6z9ptn2ssxpu0", "vq9oc8t714mv4xth2294n4y5ez6dqpag3u6ztrejypf8lqw9");
		AVAnalytics.trackAppOpened(getIntent());
		
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				String file = "http___s2.lashouimg.com_zt_220_201302_18_136117721885094800.jpg";
//				try {
//					AVFile pic = AVFile.withFile(file, FileAccessor.getSdFile(FileAccessor.TUANMANAGERIMAGEDIR + file));
//					pic.save();
//					Log.w("AvosFile", "object id =" + pic.getObjectId());
//					String url = pic.getThumbnailUrl(false, 200, 100);
//					Log.w("AvosFile", "url =" + url);
//					Thread.sleep(3000);
//					LunchSet set = new LunchSet();
//					set.setName("�ڽ�ţ��");
//					set.setObjectId(pic.getObjectId());
//					set.setThumbnailUrl(url);
//					set.setPrice("27");
//					set.saveToCloud();
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (AVException e) {
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//actionBar.removeAllTabs();
		
	}
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		mViewPager.setCurrentItem(position);
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.list_map, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			Toast.makeText(this, "action setting.", Toast.LENGTH_SHORT).show();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
