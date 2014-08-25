package com.techbow.liulunchdelivery.Utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

public class FileAccessor {
	private static File bitmapDir = createSdcardDir("TuanManager" + File.separator);//创建目录
	
	private static String getSdcardPath() {
		//得到当前外部存储设备的目录SDCARD
		return Environment.getExternalStorageDirectory() + File.separator;
	}
	public static File createSdcardDir(String dirName) {
		File dir = new File(getSdcardPath() + dirName);
		if(dir.exists()) {
			return dir;
		}
		else {
			try {
				if(dir.mkdirs()) {
					return dir;
				}
				return null;
			} catch (SecurityException e) {
				return null;
			}
		}
	}
	
	public static final String TUANMANAGERIMAGEDIR = getSdcardPath() + "TuanManager" + File.separator;
	
	public static File getSdFile(String fileName) {
		if(bitmapDir == null) {
			return null;
		}
		File file = new File(fileName);
		if(file.exists()) {
			return file;
		}
		else {
			try {
				file.createNewFile();
				return file;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	//因为java删除文件内容只有一种实现方法，就是把整个文件重写
	public static boolean cleanFile(String fileName) {
		String cleanStr = "";
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(getSdFile(fileName)));
			bos.write(cleanStr.getBytes());
			bos.flush();
			bos.close();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean isFileExist(String fileName){
		File file = new File(fileName);
		return file.exists();
	}
	public static void deleteSdFile(String fileName) {
		if(isFileExist(fileName)) {
			getSdFile(fileName).delete();
		}
	}
	
	public static boolean writeSdFile(String fileName, String text) {
		boolean ret = true;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(text.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = false;
		} finally {
			try {
				if(fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ret = false;
			}
		}
		return ret;
	}
	public static String readSdFile(String fileName) {
		String ret = null;
		FileInputStream fis = null;
		try {
			fis= new FileInputStream(fileName);
			int len = fis.available();
			byte[] buf = new byte[len];
			fis.read(buf);
			ret = EncodingUtils.getString(buf, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fis = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fis = null;
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fis = null;
			}
		}
		return ret;
	}
}