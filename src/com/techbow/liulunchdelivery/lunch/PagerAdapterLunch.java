package com.techbow.liulunchdelivery.lunch;

import java.util.Locale;

import com.techbow.liulunchdelivery.listmap.FragmentList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapterLunch extends FragmentPagerAdapter {
	private Context context;
	public PagerAdapterLunch(FragmentManager fm, Context context) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			return new FragmentLunch();
		case 1:
			return new FragmentLunch();
		case 2:
			return new FragmentLunch();
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;	//list and map
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return "yesterday";
		case 1:
			return "today";
		case 2:
			return "tomorrow";
		}
		return null;
	}
}
