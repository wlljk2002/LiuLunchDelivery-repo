package com.techbow.liulunchdelivery.lunch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;
import com.techbow.liulunchdelivery.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentLunch extends Fragment {
	//String owner;
	private ImageView lunchImage;
	
	public FragmentLunch() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_lunch, container, false);
		lunchImage = (ImageView) rootView.findViewById(R.id.lunchImage);
		AVFile.withObjectIdInBackground("53f35c69e4b026a90a4189ad", new GetFileCallback<AVFile>() {
			
			@Override
			public void done(AVFile arg0, AVException arg1) {
				// TODO Auto-generated method stub
				arg0.getDataInBackground(new GetDataCallback() {
					
					@Override
					public void done(byte[] arg0, AVException arg1) {
						// TODO Auto-generated method stub
						Bitmap bm = BitmapFactory.decodeStream(new ByteArrayInputStream(arg0));
						lunchImage.setImageBitmap(bm);
					}
				});
			}
		});
		return rootView;
	}
}
