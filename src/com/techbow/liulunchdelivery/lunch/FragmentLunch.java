package com.techbow.liulunchdelivery.lunch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;
import com.techbow.liulunchdelivery.R;
import com.techbow.liulunchdelivery.parameter.LunchSet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentLunch extends Fragment {
	//String owner;
	private ImageView lunchImage;
	private TextView setPrice;
	private LunchSet lunchSet;
	private Bitmap bitmap;
	
	public static FragmentLunch newInstance(LunchSet lunchSet, Bitmap bitmap) {  
		FragmentLunch fragment = new FragmentLunch();  
        Bundle bundle = new Bundle();  
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
		lunchImage = (ImageView) rootView.findViewById(R.id.lunchImage);
		setPrice = (TextView) rootView.findViewById(R.id.setPrice);
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
		lunchSet = (LunchSet) bundle.getSerializable("lunchSet");
		bitmap = bundle.getParcelable("bitmap");
		lunchImage.setImageBitmap(bitmap);
		setPrice.setText("$" + lunchSet.getPrice());
		return rootView;
	}
}
