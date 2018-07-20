package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ChatUserInfo;
import com.chewuwuyou.app.fragment.MainSearchFragment;
import com.chewuwuyou.app.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class MainSearchAdapter extends BaseAdapter {
    private Context context;
    private List<ChatUserInfo> list;

    public MainSearchAdapter(Context context) {
        this.context = context;
        list = new ArrayList<ChatUserInfo>();
    }

    public void setData(List<ChatUserInfo> list) {
        this.list = list;
        if (list.size() == 0)
            list.add(new ChatUserInfo(MainSearchFragment.NULL_USER_ID, "未搜索到结果"));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_item, null);
            vh.mMoodTV = (TextView) convertView.findViewById(R.id.mood);
            vh.mUserNameTV = (TextView) convertView.findViewById(R.id.username);
            vh.mImageIV = (ImageView) convertView.findViewById(R.id.child_item_head);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ChatUserInfo info = list.get(position);
        if (info.getId().equals(MainSearchFragment.NULL_USER_ID)) {
            vh.mMoodTV.setVisibility(View.GONE);
            vh.mImageIV.setVisibility(View.GONE);
        } else {
            vh.mMoodTV.setVisibility(View.VISIBLE);
            vh.mImageIV.setVisibility(View.VISIBLE);
        }
        vh.mMoodTV.setText(info.getSignature());
        String nick = info.getNick();
        vh.mUserNameTV.setText(TextUtils.isEmpty(nick) ? info.getUserName() : nick);
        if (!TextUtils.isEmpty(info.getUrl())) {
            ImageUtils.displayImage(info.getUrl(), vh.mImageIV, 360);
        } else {
            vh.mImageIV.setImageResource(R.drawable.user_yuan_icon);
        }
        return convertView;
    }

    class ViewHolder {
        TextView mMoodTV;
        TextView mUserNameTV;
        ImageView mImageIV;
    }
}
