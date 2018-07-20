package com.chewuwuyou.app.adapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Banner;
import com.chewuwuyou.app.ui.BannerToActivity;
import com.chewuwuyou.app.ui.VehicleQuanVewPager;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.jakewharton.salvage.RecyclingPagerAdapter;
import com.uk.co.senab.photoview.PhotoView;
import com.uk.co.senab.photoview.PhotoViewAttacher;

/**
 * viewPager 循环
 * @author xiehy
 *
 */
public class VehicleQuanViewPagerAdapter extends RecyclingPagerAdapter {

	private Context mContext;
	private List<String> list;
	private LayoutInflater layoutInflater;
	private boolean mIsInfiniteLoop;
	private File finalImageFile;
	private String type="";
	private String nameTime;
	private TextView mVehiclePreservation;

	public VehicleQuanViewPagerAdapter(Context context, List<String> list,String type) {
		this.mContext = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(mContext);
		mIsInfiniteLoop = false;
		this.type = type;
	}

	public int getCount() {
		// Infinite loop
		return mIsInfiniteLoop ? Integer.MAX_VALUE : list.size();
	}

	/**
	 * get really position
	 *
	 * @param position
	 * @return
	 */
	// private int getPosition(int position) {
	// return mIsInfiniteLoop ? position % mSize : position;
	// }

	public View getView(final int position, View convertView, ViewGroup container) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.vehicleview, null);
			holder.imageView = (PhotoView) convertView.findViewById(R.id.iv_ig);
			mVehiclePreservation = (TextView) convertView.findViewById(R.id.vehicle_preservation);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			if(!TextUtils.isEmpty(type)&&type.equals("rong_chat")){
				holder.imageView.setImageBitmap(FileUtils.getSmallBitmap(list.get(position)));
			}else{
				ImageUtils.displayImage(list.get(position),holder.imageView, 0, R.drawable.default_baise,R.drawable.default_baise);
			}

		holder.imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
				VehicleQuanVewPager.mVehicleQuanVewPager.finishActivity();
			}
		});

		/**
		 * 保存图片到本地
		 */
		mVehiclePreservation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(TextUtils.isEmpty(list.get(position))){
					ToastUtil.toastShow(mContext,"图片地址不能为空");
					return;
				}
				mVehiclePreservation.setClickable(false);
				//创建缓存目录，系统一运行就得创建缓存目录的，
				File cdd = new File(Environment.getExternalStorageDirectory(), "chedangdang");
				if(!cdd.exists()){
					cdd.mkdirs();
				}

				nameTime =String.valueOf(System.currentTimeMillis())+".png";


				finalImageFile = new File(cdd, nameTime);

				if (finalImageFile.exists()) {
					finalImageFile.delete();
				}
				try {
					finalImageFile.createNewFile();

					if(!TextUtils.isEmpty(type)&&type.equals("rong_chat")){

						img(FileUtils.getSmallBitmap(list.get(position)),finalImageFile);
					}else{
						urlNetwork(ServerUtils.getImgServer(list.get(position)));
					}
				} catch (IOException e) {
					e.printStackTrace();
					mVehiclePreservation.setClickable(true);
				}
			}
		});

		return convertView;
	}

	private static class ViewHolder {
		PhotoView imageView;

	}


	private void urlNetwork(final String urlPath){
		//开启子线程
		new Thread(){
			public void run() {
				try {
						img(getBitmap(urlPath),finalImageFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public Bitmap getBitmap(String url) {
		Bitmap bm = null;
		try {
			URL iconUrl = new URL(url);
			URLConnection conn = iconUrl.openConnection();
			HttpURLConnection http = (HttpURLConnection) conn;

			int length = http.getContentLength();

			conn.connect();
			// 获得图像的字符流
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, length);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();// 关闭流
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}


	private void img(Bitmap bitmap,File cdd){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(cdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			mVehiclePreservation.setClickable(true);
		}
		if (bitmap == null) {
			return;
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		try {
			ToastUtil.toastShow(mContext,"保存成功，请到/chedangdang目录下查看");
			fos.flush();
			fos.close();
			mVehiclePreservation.setClickable(true);
		} catch (IOException e) {
			mVehiclePreservation.setClickable(true);
			ToastUtil.toastShow(mContext,"保存失败");
			System.out.println("++++++++++++++androider++a+++"+nameTime);
			if (!TextUtils.isEmpty(Environment.getExternalStorageDirectory() + "/chedangdang/"+nameTime)) {
				File file = new File(Environment.getExternalStorageDirectory() + "/chedangdang/"+nameTime);
				if (file.exists())
					file.delete();
			}

			e.printStackTrace();
		}
	}


	/** 保存方法 */
	public void saveBitmap(Bitmap picName) {
		File cdd = new File(Environment.getExternalStorageDirectory(), "chedangdang");
		if(!cdd.exists()){
			cdd.mkdirs();
		}

		try {
			FileOutputStream out = new FileOutputStream(cdd);
			picName.compress(Bitmap.CompressFormat.PNG, 360, out);
			out.flush();
			out.close();
			mVehiclePreservation.setClickable(true);
			ToastUtil.toastShow(mContext,"保存成功，请到/chedangdang目录下查看");
		} catch (FileNotFoundException e) {
			mVehiclePreservation.setClickable(true);
			// TODO Auto-generated catch block
			e.printStackTrace();
			ToastUtil.toastShow(mContext,"保存失败");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			mVehiclePreservation.setClickable(true);
			e.printStackTrace();
			ToastUtil.toastShow(mContext,"保存失败");
		}

	}
}
