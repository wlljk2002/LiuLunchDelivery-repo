package com.techbow.liulunchdelivery.listmap;

import java.util.ArrayList;
import java.util.List;

import com.techbow.liulunchdelivery.R;
import com.techbow.liulunchdelivery.lunch.ActivityLunch;
import com.techbow.liulunchdelivery.parameter.DistributionGeo;
import com.techbow.liulunchdelivery.parameter.DistributionSite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentList extends Fragment {
	private List<DistributionSite> distributionSiteList;
	private List<DistributionGeo> distributionGeoList;
	private ListView distributionListView;
	private DistributionListViewAdapter distributionListViewAdapter;
	static public DistributionListMapAsyncTask asyncTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		distributionSiteList = new ArrayList<DistributionSite>();
		distributionGeoList = new ArrayList<DistributionGeo>();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		distributionListView = (ListView) rootView.findViewById(R.id.distributionList);
		distributionListViewAdapter = new DistributionListViewAdapter(distributionSiteList, getActivity());
		distributionListView.setAdapter(distributionListViewAdapter);
		distributionListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DistributionSite site = (DistributionSite) distributionListView.getItemAtPosition(position);
				Toast.makeText(getActivity(), "site " + site.getName() + " is chosen", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getActivity(), ActivityLunch.class);
				intent.putExtra("distributionSite", site);
				startActivity(intent);
			}
		});
		asyncTask = new DistributionListMapAsyncTask(distributionSiteList, distributionGeoList, distributionListViewAdapter, getActivity());
		return rootView;
	}

}
