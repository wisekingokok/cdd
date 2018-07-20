package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.BanKuaiItem;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.viewcache.HotTieClassificationItemViewCache;

import java.util.List;

public class HotTieClassificationAdapter extends BaseAdapter implements
        View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BanKuaiItem> mData;
    // private String mBackImg;

//	public HotTieClassificationAdapter(Context context, List<BanKuaiItem> data,
//			String backImg) {
//		this.mContext = context;
//		this.mInflater = LayoutInflater.from(context);
//		this.mData = data;
//		// this.mBackImg = backImg;
//	}

    public HotTieClassificationAdapter(Context context, List<BanKuaiItem> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HotTieClassificationItemViewCache viewCache = null;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.hot_tie_classification_item, null);
            viewCache = new HotTieClassificationItemViewCache(convertView);
            convertView.setTag(viewCache);
        } else {
            viewCache = (HotTieClassificationItemViewCache) convertView
                    .getTag();
        }
        final BanKuaiItem item = mData.get(position);

//		if (position == 0) {
//			viewCache.getmClassificationBgTuRL().setVisibility(View.VISIBLE);
//			viewCache.getmClassificationBgTuIV().setImageResource(
//					R.drawable.qicheyushenghuo);
//		} else {
//			
//		}
        viewCache.getmClassificationBgTuRL().setVisibility(View.GONE);
        ImageUtils.displayImage(item.getPhoto(), viewCache.getmClassificationTuIV(), 0, R.drawable.image_default, R.drawable.image_default);
        viewCache.getmClassificationNameTV().setText(item.getTitle());
        viewCache.getClassificationTodayCntTitleTv().setText(item.getSubtile());
        viewCache.getmClassificationTuIV().setOnClickListener(this);
        final Integer position_integer = Integer.valueOf(position);
        viewCache.getmClassificationTuIV().setTag(position_integer);
        return convertView;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.classification_tu_iv:
                Object tag = arg0.getTag();
                Integer position_integer = null;
                if (tag instanceof Integer)
                    position_integer = (Integer) tag;
                Intent intent = new Intent(mContext, HotTieActivity.class);
                intent.putExtra("banId", mData.get(position_integer).getId());
                intent.putExtra("banTitle", mData.get(position_integer).getTitle());
                if (mData.get(position_integer).getTitle().equals("车展")) {
                    intent.putExtra("banImageId",
                            R.drawable.hot_tie_header_iv_chezhan);
                } else if (mData.get(position_integer).getTitle().equals("自驾")) {
                    intent.putExtra("banImageId", R.drawable.hot_tie_header_iv_zijia);
                } else if (mData.get(position_integer).getTitle().equals("改装")) {
                    intent.putExtra("banImageId",
                            R.drawable.hot_tie_header_iv_gaizhuang);
                } else if (mData.get(position_integer).getTitle().equals("风景")) {
                    intent.putExtra("banImageId",
                            R.drawable.hot_tie_header_iv_fengjing);
                } else if (mData.get(position_integer).getTitle().equals("城市")) {
                    intent.putExtra("banImageId",
                            R.drawable.hot_tie_header_iv_chengshi);
                } else if (mData.get(position_integer).getTitle().equals("摩托车")) {
                    intent.putExtra("banImageId",
                            R.drawable.hot_tie_header_iv_motuoche);
                }
                mContext.startActivity(intent);
                break;

            default:
                break;
        }

    }

}
