package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Driving;
import com.chewuwuyou.app.extras.FancyCoverFlow;
import com.chewuwuyou.app.extras.FancyCoverFlowAdapter;
import com.chewuwuyou.app.extras.RoundedImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class DrivingAdapter extends FancyCoverFlowAdapter {

    private int mItemWidth = 0;
    private int mItemHeight = 0;
    private Context mContext;
    private List<Driving> mDrivingsList;

    public DrivingAdapter(Context mContext, List<Driving> mDrivingsList) {
        // 如果当前item正在播放，则设置设置播放动画
        this.mContext = mContext;
        this.mDrivingsList = mDrivingsList;
        mItemWidth = dp2px(100);
        mItemHeight = dp2px(100);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Driving getItem(int position) {
        return mDrivingsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 刷新指定item
     *
     * @param index    用于集合取数据
     * @param position item在listview中的位置
     */
    public void updateItem(int index, int position) {

        Toast.makeText(mContext, "" + index, Toast.LENGTH_SHORT).show();

    }

    class ViewHolder {
        LinearLayout viewLL;
        RoundedImageView itemImg;
    }

    @Override
    public View getCoverFlowItem(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.view_cover_flow_item, null);
            viewHolder = new ViewHolder();
            viewHolder.itemImg = (RoundedImageView) convertView.findViewById(R.id.item_img);
            viewHolder.viewLL = (LinearLayout) convertView.findViewById(R.id.view_ll);
            convertView.setLayoutParams(new FancyCoverFlow.LayoutParams(mItemWidth, mItemHeight));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final int index = position % mDrivingsList.size();
        if (mDrivingsList.size() > index) {
            final Driving mVoiceUser = mDrivingsList.get(index);
            if (mVoiceUser != null) {
                if (mVoiceUser.getDrivingId() == 1) {
                    viewHolder.itemImg
                            .setImageDrawable(mContext.getResources().getDrawable(R.drawable.qichepeijian));
                }
                if (mVoiceUser.getDrivingId() == 2) {
                    viewHolder.itemImg
                            .setImageDrawable(mContext.getResources().getDrawable(R.drawable.automobile_four_s));
                }
                if (mVoiceUser.getDrivingId() == 3) {
                    viewHolder.itemImg
                            .setImageDrawable(mContext.getResources().getDrawable(R.drawable.gas_station));
                }
                if (mVoiceUser.getDrivingId() == 4) {
                    viewHolder.itemImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.parking));

                }
                if (mVoiceUser.getDrivingId() == 5) {
                    viewHolder.itemImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.repair));
                }
                if (mVoiceUser.getDrivingId() == 6) {
                    viewHolder.itemImg
                            .setImageDrawable(mContext.getResources().getDrawable(R.drawable.auto_beauty));
                }
                if (mVoiceUser.getDrivingId() == 7) {
                    viewHolder.itemImg
                            .setImageDrawable(mContext.getResources().getDrawable(R.drawable.driver_school));
                }
            }
        }
        return convertView;
    }
    /**
     * 屏幕的密度
     *
     * @param dipValue
     * @return
     */
    public int dp2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}