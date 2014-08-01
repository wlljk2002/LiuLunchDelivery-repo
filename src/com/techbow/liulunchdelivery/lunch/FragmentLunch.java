package com.techbow.liulunchdelivery.lunch;

import com.techbow.liulunchdelivery.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentLunch extends Fragment {
	String owner;
	
	public FragmentLunch() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_lunch, container, false);
		return rootView;
	}
}
