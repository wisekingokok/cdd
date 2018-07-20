package com.chewuwuyou.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.TuItem;
import com.chewuwuyou.app.ui.VehicleQuanVewPager;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.viewcache.CaptureItemViewCache;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class PersonPicGridAdapter extends SNSAdapter {

    private List<TuItem> mData;
    private MyGridView mTusGV;

    public PersonPicGridAdapter(Activity context, List<TuItem> data, MyGridView tusGV, HackyViewPager viewPager, View container) {
        super(context, viewPager, container);
        this.mData = data;
        this.mTusGV = tusGV;
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        CaptureItemViewCache viewCache = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.capture_head_item, null);
            viewCache = new CaptureItemViewCache(convertView);
            convertView.setTag(viewCache);
        } else {
            viewCache = (CaptureItemViewCache) convertView.getTag();
        }
        // final ImageView imageView;
        // if (convertView == null) {
        // imageView = new ImageView(mContext);
        // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // } else {
        // imageView = (ImageView) convertView;
        // }
//		LayoutParams params = new LayoutParams(mColumWidth, mColumWidth);
//		viewCache.getmIV().setLayoutParams(params);

        // 修改成服务器集群，求不需要拼接基本ip
        // StringBuilder builder = new
        // StringBuilder(NetworkUtil.IMAGE_BASE_URL);
        // builder.append(url);
        // final String tempUrl = builder.toString();
        // mFinalBitmap.display(viewCache.getmIV(), tempUrl);
        final ArrayList<String> tuUrls = new ArrayList<String>();
        for (TuItem tu : mData) {
            tuUrls.add(tu.getUrl());
        }
        ImageUtils.displayImage(mData.get(position).getUrl(), viewCache.getmIV(), 8, R.drawable.image_default, R.drawable.image_default);
        viewCache.getmIV().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
              //  zoomImageFromThumb(view, tuUrls, mTusGV, position);
                Intent intent = new Intent(mContext, VehicleQuanVewPager.class);
                intent.putStringArrayListExtra("url",tuUrls);
                intent.putExtra("viewPagerPosition",position+"");

                mContext.startActivity(intent);

            }
        });
        return convertView;
    }


}