package com.techbow.liulunchdelivery;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class CustomerViewPager extends ViewPager {
	public CustomerViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean canScroll(View arg0, boolean arg1, int arg2,
			int arg3, int arg4) {
		// TODO Auto-generated method stub
		if (getCurrentItem() == 1) {
			return true;
		}
		return super.canScroll(arg0, arg1, arg2, arg3, arg4);
	}
}
