package com.chewuwuyou.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:颜文字
 * @author:yuyong
 * @date:2015-4-1上午11:18:35
 * @version:1.2.1
 */
public class ChatInputUtils {

	/**
	 * 表情文件名数组
	 */
	private static List<String> fileNames = null;
	private static List<String> emotions_static_fileNames = null;

	/**
	 * 获取静态gif集合
	 * 
	 * @param ctx
	 * @param path
	 */
	public static List<String> getFileList(Context ctx, String path) {
		try {
			emotions_static_fileNames = Arrays.asList(ctx.getAssets()
					.list(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return emotions_static_fileNames;
	}

	public static void showContent(Context context, String content,
			TextView mTV, Map<String, String> mYwzMap) {
		Bitmap bitmap;
		ImageSpan imageSpan;
		// 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
		SpannableString spannableString = new SpannableString(content);
		if (content.contains("#IMG") && content.contains("GMI#")) {
			mTV.setWidth(100);
			mTV.setHeight(100);
			MyLog.i("YUY",
					"截取图片地址 = " + content.substring(5, content.length() - 4));
			bitmap = ImageUtils.drawableToBitmap(context.getResources()
					.getDrawable(R.drawable.bg_defaultbg));
			// bitmap=ImageUtils.getBitmap(NetworkUtil.IMAGE_BASE_URL
			// + content.substring(5, content.length() - 4));
			// bitmap =
			// FinalBitmap.create(context).getBitmapFromCache(NetworkUtil.IMAGE_BASE_URL
			// + content.substring(5, content.length() - 4));
			MyLog.i("YUY", "获取图片的bitmap = " + bitmap);
			imageSpan = new ImageSpan(context, bitmap);
			spannableString.setSpan(imageSpan, 0, content.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		((TextView) mTV).setText(spannableString);
	}

	public static void showImageFace(Context context, String content,
			TextView v, Map<String, String> ywzMap) {

		List<String> ywzList = checkYWZ(content);
		Bitmap bitmap;
		ImageSpan imageSpan;
		// 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
		SpannableString spannableString = new SpannableString(content);
		int start = 0;
		for (int i = 0; i < ywzList.size(); i++) {
			boolean flag = true;

			if (null != ywzMap.get(ywzList.get(i))) {
				if (content.indexOf("[" + ywzList.get(i) + "]", start) >= 0) {
					bitmap = getImageFromAssetsFile(context,
							"face/" + ywzMap.get(ywzList.get(i)) + ".png");
					imageSpan = new ImageSpan(context, bitmap);
					spannableString.setSpan(imageSpan, start, start
							+ ywzList.get(i).length() + 2,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					start = start + ywzList.get(i).length() + 2;
					flag = false;
				}
			}

			if (flag) {
				start = start + ywzList.get(i).length();
			}
		}

		((TextView) v).setText(spannableString);
	}

	public static SpannableString displayFacePic(Context context,
			String content, Map<String, String> ywzMap) {

		List<String> ywzList = checkYWZ(content);
		Bitmap bitmap;
		ImageSpan imageSpan;
		// 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
		SpannableString spannableString = new SpannableString(content);
		int start = 0;
		for (int i = 0; i < ywzList.size(); i++) {
			boolean flag = true;
			if (null != ywzMap.get(ywzList.get(i))) {

				String value = ywzMap.get(ywzList.get(i));
				if (content.indexOf(ywzList.get(i), start) >= 0) {
					bitmap = getImageFromAssetsFile(context,
							"face/" + value.substring(0, value.length() - 1)
									+ ".png");
					imageSpan = new ImageSpan(context, bitmap);
					spannableString.setSpan(imageSpan, start, start
							+ ywzList.get(i).length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					start = start + ywzList.get(i).length();
					flag = false;
				}
			}

			if (flag) {
				start = start + ywzList.get(i).length();
			}
		}
		return spannableString;
	}

	/**
	 * 最新显示表情图表 新老兼容
	 * 
	 * @param context
	 * @param content
	 * @param ywzMap
	 * @return
	 */
	public static SpannableString displayBigFacePic(Context context,
			String content, Map<String, String> ywzMap) {
		List<String> ywzList = checkYWZ(content);
		Bitmap bitmap;
		ImageSpan imageSpan;
		// 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像 修改表情包 直接修改bigface
		SpannableString spannableString = new SpannableString(content);
		int start = 0;
		for (int i = 0; i < ywzList.size(); i++) {
			boolean flag = true;
			if (null != ywzMap.get("[" + ywzList.get(i) + "]")) {
				if (content.indexOf("[" + ywzList.get(i) + "]", start) >= 0) {
					bitmap = getImageFromAssetsFile(context,
							"face/" + ywzMap.get("[" + ywzList.get(i) + "]")
									+ ".png");
					imageSpan = new ImageSpan(context, bitmap);
					spannableString.setSpan(imageSpan, start, start
							+ ywzList.get(i).length() + 2,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					start = start + ywzList.get(i).length() + 2;
					flag = false;
				}
			}
			if (flag) {
				start = start + ywzList.get(i).length();
			}
		}
		return spannableString;
	}

	/**
	 * 添加表情
	 * 
	 * @param ctx
	 * @param et
	 * @param bitmap
	 */
	public static void addPic(Context ctx, EditText et, Bitmap bitmap,
			String inputMess) {
		ImageSpan imageSpan = new ImageSpan(ctx, bitmap);
		SpannableString spannableString = new SpannableString(inputMess);
		spannableString.setSpan(imageSpan, 0, spannableString.length(),
				SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		Editable e = et.getText();
		int st = et.getSelectionStart();
		int en = et.getSelectionEnd();
		e.replace(st, en, spannableString);
	}

	/**
	 * 获取表情文件名
	 * 
	 * @param path
	 */
	@SuppressWarnings("deprecation")
	public static List<Map<String, Object>> getFilePaths(Context mContext,
			String path) {
		List<Map<String, Object>> cateList = new ArrayList<Map<String, Object>>();
		try {
			fileNames = Arrays.asList(mContext.getAssets().list(path));
			if (fileNames != null) {
				List<String> returnArray = new ArrayList<String>();
				for (String fileName : fileNames) {
					String tmp = fileNames.get(fileNames.indexOf(fileName));
					returnArray.add(tmp);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < fileNames.size(); ++i) {
			InputStream open = null;
			try {
				String temp = path + "/" + fileNames.get(i);
				MyLog.i("YUY", "---------表情地址---" + temp);
				open = mContext.getAssets().open(temp);
				// modify start by yuyong 修改内存溢出问题 redmine 913 2016/04/25
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inPreferredConfig = Bitmap.Config.RGB_565;
				opt.inPurgeable = true;
				opt.inInputShareable = true;
				Bitmap bitmap = BitmapFactory.decodeStream(open, null, opt);
				// Bitmap bitmap = BitmapFactory.decodeStream(open);
				// modify end by yuyong 修改内存溢出问题 redmine 913 2016/04/25
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name",
						fileNames.get(i).substring(0,
								fileNames.get(i).indexOf(".")));
				map.put("iv", bitmap);
				map.put("cate_id", i);
				cateList.add(map);

				// Assign the bitmap to an ImageView in this layout
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (open != null) {
					try {
						open.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return cateList;
	}

	/**
	 * 以最省内存方式读取本地资源图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 显示颜文字
	 * 
	 * @param ywzMap
	 *            颜文字json（包含表情的名称及颜文字的内容）
	 * @param context
	 * @param s
	 *            所要发送的内容
	 * @param v
	 *            显示的文本
	 * @param cateList
	 *            解析表情的map
	 */
	public static void showFaceImg(Map<String, String> ywzMap, Context context,
			String s, View v, List<Map<String, Object>> cateList) {

		Bitmap bitmap;
		ImageSpan imageSpan;
		// 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
		SpannableString spannableString = new SpannableString(s);
		for (int i = 0; i < cateList.size(); i++) {
			MyLog.i("MainActivity", "查找：" + cateList.get(i).get("name"));
			int start = 0;
			while (s.indexOf(cateList.get(i).get("name").toString(), start) >= 0) {
				start = s
						.indexOf(cateList.get(i).get("name").toString(), start);
				bitmap = (Bitmap) cateList.get(i).get("iv");
				imageSpan = new ImageSpan(context, bitmap);
				// 用ImageSpan对象替换字符
				spannableString.setSpan(imageSpan, start - 1, start + 2,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				start = start + 3;
			}
		}
		((TextView) v).setText(spannableString);

	}

	/**
	 * 你好！[wx][cf]哈哈
	 * 
	 * @param content
	 *            (颜文字和普通文字的组合)
	 */
	public static List<String> checkYWZ(String content/*
													 * , Map<String, String>
													 * ywzMap, Context context,
													 * List<Map<String, Object>>
													 * cateList
													 */) {

		String[] theArray = new String[] { "", content };
		List<String> dividedStrings = new ArrayList<String>();
		while (!theArray[1].isEmpty()) {

			theArray = checkProtocol(theArray[1]);
			// 如果是颜文字
			// if (theArray[0].startsWith("[")) {
			//
			// } else {
			//
			// }

			dividedStrings.add(theArray[0]);
		}

		return dividedStrings;
	}

	public static String[] checkProtocol(String str) {
		// 第一個是找出來的，第二個是截取后的
		try {
			String[] returnArray = new String[] { "", str };

			if (str.startsWith("[")) {
				int endIndex = str.indexOf("]");
				if (endIndex <= 0) {
					returnArray[0] = str.substring(0);
					returnArray[1] = "";
				} else {
					returnArray[0] = str.substring(1, endIndex);
					returnArray[1] = str.substring(endIndex + 1);
				}
				// 如果不是颜文字就只截取一个字
			} else if (str.length() > 0) {
				returnArray[0] = str.substring(0, 1);
				returnArray[1] = str.substring(1);
			}
			return returnArray;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getKeyByValue(String value, Map<String, String> ywzMap) {
		String str = "";
		for (String key : ywzMap.keySet()) {
			String tvalue = ywzMap.get(key);
			if (value.equals(tvalue)) {
				str = key;
			}
		}
		return str;

	}

	/**
	 * 从Aassets中获取图片
	 * 
	 * @param fileName
	 * @return
	 */
	public static Bitmap getImageFromAssetsFile(Context ctx, String fileName) {
		Bitmap image = null;
		AssetManager am = ctx.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}

	/**
	 * 从Aassets获取图片
	 * 
	 * @param ctx
	 * @param fileName
	 * @return
	 */
	public static InputStream getIsFromAssetsFile(Context ctx, String fileName) {
		InputStream is = null;
		AssetManager am = ctx.getResources().getAssets();
		try {
			is = am.open(fileName);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
}
