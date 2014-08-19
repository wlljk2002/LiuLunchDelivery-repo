package com.techbow.liulunchdelivery.Utils;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

//�������Ҫ������ʵ��ͼƬ���첽����
public class AsyncImageLoader {
	//ͼƬ�������
	//����ͼƬ��URL��ֵ��һ��SoftReference���󣬸ö���ָ��һ��Drawable����
	private static Map<String, SoftReference<Bitmap>> imageCache = 
		new HashMap<String, SoftReference<Bitmap>>();
	
	public static Bitmap getCachedImage(String imageUrl) {
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> softReference=imageCache.get(imageUrl);
			if(softReference.get() != null){
				return softReference.get();
			}
		}
		return null;
	}
	public static void clearImageCache() {
		imageCache.clear();
	}
	//ʵ��ͼƬ���첽����
	public Bitmap loadImage(final String imageUrl, final ImageCallback callback){
		//��ѯ���棬�鿴��ǰ��Ҫ���ص�ͼƬ�Ƿ��Ѿ������ڻ��浱��
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> softReference=imageCache.get(imageUrl);
			if(softReference.get() != null){
				return softReference.get();
			}
		}
		
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				callback.imageLoaded((Bitmap) msg.obj);
			}
		};
		//give BoughtListCallBack a chance to load image from local storage
		callback.imageLoaded(null);
		
		//�¿���һ���̣߳����߳����ڽ���ͼƬ������
		new Thread(){
			@Override
			public void run() {
				Bitmap bitmap = loadImageFromUrl(imageUrl);
				if (bitmap == null) {
					System.out.println("image draw fail");
				}
				imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
				Message message = handler.obtainMessage(0, bitmap);
				handler.sendMessage(message);
			};
		}.start();
		return null;
	}
	//�÷������ڸ���ͼƬ��URL��������������ͼƬ
//	protected Drawable loadImageFromUrl(String imageUrl) {
//		try {
//			//����ͼƬ��URL������ͼƬ��������һ��Drawable����
//			return Drawable.createFromStream(new URL(imageUrl).openStream(), "src.jpg");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	protected Bitmap loadImageFromUrl(String imageUrl) {
		try {
			//����ͼƬ��URL������ͼƬ��������һ��Drawable����
			return BitmapFactory.decodeStream(new URL(imageUrl).openStream());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//�ص��ӿ�
	public interface ImageCallback{
		public void imageLoaded(Bitmap imageBitmap);
	}
}
