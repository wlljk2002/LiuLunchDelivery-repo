package com.techbow.liulunchdelivery.Utils;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

//该类的主要作用是实现图片的异步加载
public class AsyncImageLoader {
	//图片缓存对象
	//键是图片的URL，值是一个SoftReference对象，该对象指向一个Drawable对象
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
	//实现图片的异步加载
	public Bitmap loadImage(final String imageUrl, final ImageCallback callback){
		//查询缓存，查看当前需要下载的图片是否已经存在于缓存当中
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
		
		//新开辟一个线程，该线程用于进行图片的下载
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
	//该方法用于根据图片的URL，从网络上下载图片
//	protected Drawable loadImageFromUrl(String imageUrl) {
//		try {
//			//根据图片的URL，下载图片，并生成一个Drawable对象
//			return Drawable.createFromStream(new URL(imageUrl).openStream(), "src.jpg");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	protected Bitmap loadImageFromUrl(String imageUrl) {
		try {
			//根据图片的URL，下载图片，并生成一个Drawable对象
			return BitmapFactory.decodeStream(new URL(imageUrl).openStream());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//回调接口
	public interface ImageCallback{
		public void imageLoaded(Bitmap imageBitmap);
	}
}
