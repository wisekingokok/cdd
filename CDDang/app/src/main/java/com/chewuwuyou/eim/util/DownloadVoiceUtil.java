package com.chewuwuyou.eim.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;

/**
 * 下载语音工具类
 * @author Administrator
 *
 */
public class DownloadVoiceUtil {
	
	private String SDPATH;

	private int FILESIZE = 10 * 1024;

	public DownloadVoiceUtil() {
		// 得到当前外部存储设备的目录()
		SDPATH = Environment.getExternalStorageDirectory() + "/";

		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}

		SDPATH = sdDir.getAbsolutePath()
				+ "/Android/data/com.chewuwuyou.app/voice/";
	}
	
	
	public String getSDPATH() {
		return SDPATH;
	}
	
	/**
	 * 语音函数
	 * @param ip 网络地址
	 * @param url 存入sdcard地址
	 */
	public boolean downloadVoice(String ip,String filename){
		try {
			URL voiceUrl = new URL(ip); 
			//打开连接
			HttpURLConnection conn = (HttpURLConnection) voiceUrl.openConnection();  
			conn.connect();
			if(200 == conn.getResponseCode()){
			  //获取文件流
	            InputStream is = conn.getInputStream();  
	            
	            File file=this.write2SDFromInput(filename, is);
	            if(file!=null){
	            	return true;
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public File write2SDFromInput(String fileName, InputStream input) {
		File file = null;
		FileOutputStream output = null;
		try {
		
			createSDDir(SDPATH);
			file = createSDFile(SDPATH + fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[FILESIZE];

			int length;
			while ((length = (input.read(buffer))) > 0) {
				output.write(buffer, 0, length);
			}

			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}
