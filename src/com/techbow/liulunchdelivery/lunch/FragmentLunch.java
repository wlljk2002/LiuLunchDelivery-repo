package com.techbow.liulunchdelivery.lunch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;
import com.techbow.liulunchdelivery.R;
import com.techbow.liulunchdelivery.parameter.LunchOrder;
import com.techbow.liulunchdelivery.parameter.LunchSet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentLunch extends Fragment {
	//String owner;
	private ImageView setImage;
	private TextView setName;
	private TextView setPrice;
	private ImageView imageAdd;
	private ImageView imageMinus;
	private EditText editOrder;
	private ImageView imageShare;
	private EditText editPhone;
	private Button order;
	private LunchSet lunchSet;
	private Bitmap bitmap;
	private int date;
	
	public static FragmentLunch newInstance(int date, LunchSet lunchSet, Bitmap bitmap) {  
		FragmentLunch fragment = new FragmentLunch();  
        Bundle bundle = new Bundle();
        bundle.putInt("date", date);
        bundle.putSerializable("lunchSet", lunchSet);  
        bundle.putParcelable("bitmap", bitmap);  
        fragment.setArguments(bundle);  
        return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_lunch, container, false);
		setImage = (ImageView) rootView.findViewById(R.id.setImage);
		setName = (TextView) rootView.findViewById(R.id.setName);
		setPrice = (TextView) rootView.findViewById(R.id.setPrice);
		imageAdd = (ImageView) rootView.findViewById(R.id.imageAdd);
		imageMinus = (ImageView) rootView.findViewById(R.id.imageMinus);
		editOrder = (EditText) rootView.findViewById(R.id.editOrder);
		imageShare = (ImageView) rootView.findViewById(R.id.imageShare);
		editPhone = (EditText) rootView.findViewById(R.id.editPhone);
		order = (Button) rootView.findViewById(R.id.order);
//		AVFile.withObjectIdInBackground("53f35c69e4b026a90a4189ad", new GetFileCallback<AVFile>() {
//			
//			@Override
//			public void done(AVFile arg0, AVException arg1) {
//				// TODO Auto-generated method stub
//				arg0.getDataInBackground(new GetDataCallback() {
//					
//					@Override
//					public void done(byte[] arg0, AVException arg1) {
//						// TODO Auto-generated method stub
//						Bitmap bm = BitmapFactory.decodeStream(new ByteArrayInputStream(arg0));
//						lunchImage.setImageBitmap(bm);
//					}
//				});
//			}
//		});
		Bundle bundle = getArguments();
		date = bundle.getInt("date");
		lunchSet = (LunchSet) bundle.getSerializable("lunchSet");
		bitmap = bundle.getParcelable("bitmap");
		setImage.setImageBitmap(bitmap);
		setName.setText(lunchSet.getName());
		setPrice.setText("$ " + lunchSet.getPrice());
		AVUser currentUser = AVUser.getCurrentUser();
		if (currentUser != null) {
			editPhone.setText(currentUser.getUsername());
		}
		imageAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int order = Integer.valueOf(editOrder.getText().toString()).intValue();
				order++;
				editOrder.setText(String.valueOf(order));
			}
		});
		imageMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int order = Integer.valueOf(editOrder.getText().toString()).intValue();
				if (order >= 1) {
					order--;
				}
				editOrder.setText(String.valueOf(order));
			}
		});
		imageShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent shareIntent = new Intent();
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Liu Lunch Delivery");
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_STREAM,
						Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null, null)));
				shareIntent.setType("image/jpeg"); 
				shareIntent.putExtra(Intent.EXTRA_TEXT, lunchSet.getName() + "\n" + lunchSet.getPrice());
				startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_list_title)));
			}
		});
		order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int amount = Integer.valueOf(editOrder.getText().toString()).intValue();
				if (amount == 0) {
					Toast.makeText(getActivity(), "Order amount is not supposed to be 0...",  Toast.LENGTH_SHORT).show();
					return;
				}
				if (editPhone.getText() == null) {
					Toast.makeText(getActivity(), "Please input your phone number...",  Toast.LENGTH_SHORT).show();
					return;
				}
				LunchOrder order = new LunchOrder();
				order.setDistributionSiteObjectId(((ActivityLunch)getActivity()).distributionSiteObjectId);
				order.setSetObjectId(((ActivityLunch)getActivity()).distributionSite.getSomedayObjectId(date));
				order.setDate(String.valueOf(date));
				order.setAmount(editOrder.getText().toString());
				order.setPhone(editPhone.getText().toString());
				order.saveToCloud(getActivity());
			}
		});
		return rootView;
	}
}
