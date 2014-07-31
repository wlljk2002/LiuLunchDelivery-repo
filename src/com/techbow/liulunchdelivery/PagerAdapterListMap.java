package com.techbow.liulunchdelivery;

import java.util.Locale;

import com.techbow.liulunchdelivery.List.FragmentList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapterListMap extends FragmentPagerAdapter {
	private Context context;
	public PagerAdapterListMap(FragmentManager fm, Context context) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			return new FragmentList();
		case 1:
			return new FragmentMap();
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;	//list and map
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return context.getString(R.string.title_section2).toUpperCase(l);
		}
		return null;
	}
}
