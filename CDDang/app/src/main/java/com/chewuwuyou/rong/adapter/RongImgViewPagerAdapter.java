package com.chewuwuyou.rong.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.ImageLoaderBuilder;
import com.chewuwuyou.app.utils.ToastUtil;
import com.jakewharton.salvage.RecyclingPagerAdapter;
import com.uk.co.senab.photoview.PhotoView;
import com.uk.co.senab.photoview.PhotoViewAttacher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * xxy
 */
public class RongImgViewPagerAdapter extends RecyclingPagerAdapter {

    private Context mContext;
    private List<String> list;
    private LayoutInflater layoutInflater;
    private boolean mIsInfiniteLoop;
    private File finalImageFile;
    private String nameTime;
    private TextView mVehiclePreservation;

    public RongImgViewPagerAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(mContext);
        mIsInfiniteLoop = false;
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
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.vehicleview, null);
            holder.imageView = (PhotoView) convertView.findViewById(R.id.iv_ig);
            mVehiclePreservation = (TextView) convertView.findViewById(R.id.vehicle_preservation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!list.get(position).contains("http://")) {
            ImageLoaderBuilder.Builder().loadFromSDCard(list.get(position)).showImageForEmptyUri(R.drawable.default_baise).showImageOnFail(R.drawable.default_baise).showImageOnLoading(R.drawable.default_baise).displayImage(holder.imageView);
        } else {
            ImageLoaderBuilder.Builder().loadFromHttp(list.get(position)).showImageForEmptyUri(R.drawable.default_baise).showImageOnFail(R.drawable.default_baise).showImageOnLoading(R.drawable.default_baise).displayImage(holder.imageView);
        }
        holder.imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                EventBusAdapter bsAdapter = new EventBusAdapter();
                bsAdapter.setEventimg("0");
                EventBus.getDefault().post(bsAdapter);// 像适配器传递值
            }
        });

        /**
         * 保存图片到本地
         */
        mVehiclePreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("++++androiderad+++++++++++++++++"+list.get(position));
                if(TextUtils.isEmpty(list.get(position))){
                    ToastUtil.toastShow(mContext,"图片地址不能为空");
                    return;
                }
                mVehiclePreservation.setClickable(false);
                //创建缓存目录，系统一运行就得创建缓存目录的，
                File cdd = new File(Environment.getExternalStorageDirectory(), "chedangdang");
                if (!cdd.exists()) {
                    cdd.mkdirs();
                }
                nameTime = String.valueOf(System.currentTimeMillis()) + ".png";
                System.out.println("++++++++++++++androider++a+++"+nameTime);
                finalImageFile = new File(cdd, nameTime);
                if (finalImageFile.exists()) {
                    finalImageFile.delete();
                }
                try {
                    finalImageFile.createNewFile();
                    if (!list.get(position).contains("http://")) {
                        saveBitmap(FileUtils.getSmallBitmap(list.get(position)));
                    } else {
                        urlNetwork(list.get(position));
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


    private void urlNetwork(final String urlPath) {
        //开启子线程
        new Thread() {
            public void run() {
                try {
                    img(getBitmap(urlPath), finalImageFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    mVehiclePreservation.setClickable(true);
                }
            }
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
        } catch (Exception e) {
            e.printStackTrace();
            mVehiclePreservation.setClickable(true);
        }
        return bm;
    }


    private void img(Bitmap bitmap, File cdd) {
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
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastShow(mContext, "保存成功，请到/chedangdang目录下查看");
                    mVehiclePreservation.setClickable(true);
                }
            });
            fos.flush();
            fos.close();
        } catch (IOException e) {
            mVehiclePreservation.setClickable(true);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastShow(mContext, "保存失败");
                }
            });
            if (!TextUtils.isEmpty(Environment.getExternalStorageDirectory() + "/chedangdang/"+nameTime)) {
                File file = new File(Environment.getExternalStorageDirectory() + "/chedangdang/"+nameTime);
                if (file.exists())
                    file.delete();
            }
            e.printStackTrace();
        }
    }


    /**
     * 保存方法
     */
    public void saveBitmap(Bitmap picName) {
        File f = new File(Environment.getExternalStorageDirectory(), "chedangdang");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            picName.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastShow(mContext, "保存成功，请到/chedangdang目录下查看");
                    mVehiclePreservation.setClickable(true);

                }
            });
        } catch (FileNotFoundException e) {
            mVehiclePreservation.setClickable(true);
            e.printStackTrace();
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastShow(mContext, "保存失败");
                }
            });
        } catch (IOException e) {
            mVehiclePreservation.setClickable(true);
            e.printStackTrace();
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastShow(mContext, "保存失败");
                }
            });
        }

    }
}

