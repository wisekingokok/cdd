package com.chewuwuyou.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.NearByFriend;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.Tools;

import java.util.List;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:附近的车友
 * @author:yuyong
 * @date:2015-4-9下午1:59:23
 * @version:1.2.1
 */
public class NearByCarFriendAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<NearByFriend> mNearByFriends;

    public NearByCarFriendAdapter(Context context, List<NearByFriend> nearByFriends) {
        this.mContext = context;
        this.mNearByFriends = nearByFriends;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public class ListItemView {
        ImageView mCarFriendHeadIV;
        TextView mCarFriendNameTV;
        TextView mCarFriendDistanceTV;
        TextView mCarFriendSingTureTV;// 车友心情
        TextView mCarFriendEgeTV;// 车友年龄
        ImageView mCarFriendSexIV;// 性别图片
    }

    @Override
    public int getCount() {
        return mNearByFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return mNearByFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = layoutInflater.inflate(R.layout.nearby_car_friend_item, null);
            listItemView.mCarFriendHeadIV = (ImageView) convertView.findViewById(R.id.car_friend_head_iv);
            listItemView.mCarFriendNameTV = (TextView) convertView.findViewById(R.id.car_friend_name_tv);
            listItemView.mCarFriendDistanceTV = (TextView) convertView.findViewById(R.id.car_friend_distance_tv);
            listItemView.mCarFriendSingTureTV = (TextView) convertView.findViewById(R.id.car_friend_singture_tv);
            listItemView.mCarFriendSexIV = (ImageView) convertView.findViewById(R.id.car_friend_sex_iv);
            listItemView.mCarFriendEgeTV = (TextView) convertView.findViewById(R.id.car_friend_nianling_tv);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        NearByFriend nearByFriend = mNearByFriends.get(position);
        if (!TextUtils.isEmpty(nearByFriend.getUrl())) {
            ImageUtils.displayImage(nearByFriend.getUrl(), listItemView.mCarFriendHeadIV, 15);
        } else {
            listItemView.mCarFriendHeadIV.setImageResource(R.drawable.user_fang_icon);
        }
        // 设置年龄
        try {
            if (mNearByFriends.get(position).getAge() > 0) {
                listItemView.mCarFriendEgeTV.setText(mNearByFriends.get(position).getAge() + "岁");
            } else {
                listItemView.mCarFriendEgeTV.setText("暂未设置年龄");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        listItemView.mCarFriendNameTV.setText(
                CarFriendQuanUtils.showCarFriendName(nearByFriend.getNoteName(), nearByFriend.getNickName(), "无名氏"));
        listItemView.mCarFriendSingTureTV.setText(nearByFriend.getSignature());
        double distance;
        try {
            distance = Tools.DistanceOfTwoPoints(Double.parseDouble(CacheTools.getUserData("Lat")),
                    Double.parseDouble(CacheTools.getUserData("Lng")), nearByFriend.getLat(), nearByFriend.getLng());
        } catch (Exception e) {
            distance = 0.0;
        }

        if (distance > 1) {
            listItemView.mCarFriendDistanceTV.setText(distance + "km");
        } else if (distance < 1 && distance > 0.5) {
            listItemView.mCarFriendDistanceTV.setText("500m~1km");
        } else {
            listItemView.mCarFriendDistanceTV.setText("500m以内");
        }
        if (nearByFriend.getSex() == 0) {
            listItemView.mCarFriendSexIV.setImageResource(R.drawable.man);
        } else if (nearByFriend.getSex() == 1) {
            listItemView.mCarFriendSexIV.setImageResource(R.drawable.woman);
        } else {
            listItemView.mCarFriendSexIV.setImageResource(R.drawable.icon_nosex);
        }
        return convertView;
    }

    public void updateData(List<NearByFriend> data) {
        this.mNearByFriends = data;
        for (int i = 0; i < mNearByFriends.size(); i++) {
            if (mNearByFriends.get(i).getZiji().equals("1")) {//区分是自己的情况
                mNearByFriends.remove(i);
            }
        }
    }

}
