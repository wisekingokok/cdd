package com.chewuwuyou.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

public class FileUtils {

	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/Photo_LJ/";

	@SuppressWarnings("unused")
	public static void saveBitmap(Bitmap bm, String picName) {
		if(bm==null){
			return;
		}
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public static void saveBitmapByPath(Bitmap bm, String path) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(path);
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDir();
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	/**
	 * 通过Base32将Bitmap转换成Base64字符串
	 * 
	 * @param bit
	 * @return
	 */
	public static String Bitmap2StrByBase64(Bitmap bit) {

		Bitmap immagex = bit;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
		return imageEncoded;

//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		bit.compress(CompressFormat.JPEG, 100, bos);// 参数100表示不压缩
//		byte[] bytes = bos.toByteArray();
//		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}


	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	//把bitmap转换成String
	public static String bitmapToString(String filePath) {
		Bitmap bm = getSmallBitmap(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
		//这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);
	}


}
