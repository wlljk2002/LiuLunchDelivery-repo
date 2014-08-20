package com.techbow.liulunchdelivery.lunch;

import java.util.List;

import com.techbow.liulunchdelivery.parameter.LunchSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapterLunch extends FragmentPagerAdapter {
	private Context context;
	private List<LunchSet> lunchSetList;
	private List<Bitmap> bitmapList;
	
	public PagerAdapterLunch(FragmentManager fm, Context context) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.context = context;
		lunchSetList = ((ActivityLunch)context).lunchSetList;
		bitmapList = ((ActivityLunch)context).bitmapList;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return FragmentLunch.newInstance(lunchSetList.get(position), bitmapList.get(position));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lunchSetList.size();	//list and map
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return "today";
		case 1:
			return "tomorrow";
		case 2:
			return "third";
		case 3:
			return "fourth";
		case 4:
			return "fifth";
		}
		return null;
	}
}
