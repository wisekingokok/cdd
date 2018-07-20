package com.chewuwuyou.rong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Userfriend;
import com.chewuwuyou.app.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchUserAdapter extends BaseAdapter {
    private Context context;
    private List<Userfriend> list;
    private String str = "";

    public SearchUserAdapter(Context context) {
        this.context = context;
        list = new ArrayList<Userfriend>();
    }

    public void setData(List<Userfriend> list, String str) {
        this.list = list;
        this.str = str;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.rong_chat_search_item, null);
            vh.mUserNameTV = (TextView) convertView.findViewById(R.id.username);
            vh.mImageIV = (ImageView) convertView.findViewById(R.id.child_item_head);
            vh.num = (TextView) convertView.findViewById(R.id.num);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.mNickName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Userfriend info = list.get(position);
        String name = TextUtils.isEmpty(info.getName()) ? info.getNickname() : info.getName();

        if (!TextUtils.isEmpty(info.getName()) && !TextUtils.isEmpty(info.getNickname())) {
            vh.mNickName.setVisibility(View.VISIBLE);//设置昵称
            vh.mNickName.setText("(" + info.getNickname() + ")");
        } else
            vh.mNickName.setVisibility(View.GONE);

        if (info.getUserId().equals("_null")) {
            vh.mImageIV.setVisibility(View.GONE);
            vh.mUserNameTV.setText(name);
            vh.title.setVisibility(View.GONE);
            vh.num.setVisibility(View.GONE);
        } else {
            vh.mImageIV.setVisibility(View.VISIBLE);
            vh.mUserNameTV.setText(name);
            if (TextUtils.isEmpty(info.getPhone())) {
                vh.title.setVisibility(View.GONE);
                vh.num.setVisibility(View.GONE);
            } else {
                vh.title.setText("电话：");
                vh.title.setVisibility(View.VISIBLE);
                vh.num.setVisibility(View.VISIBLE);
                vh.num.setText(info.getPhone());
            }
            addSpan(vh.mUserNameTV, str);
            addSpan(vh.num, str);
        }
        if (!TextUtils.isEmpty(info.getPortraitUri())) {
            ImageUtils.displayImage(info.getPortraitUri(), vh.mImageIV, 8);
        } else {
            vh.mImageIV.setImageResource(R.drawable.user_fang_icon);
        }
        return convertView;
    }

    class ViewHolder {
        TextView mUserNameTV;
        ImageView mImageIV;
        TextView num;
        TextView title;
        TextView mNickName;
    }

    private void addSpan(TextView textView, String string) {
        if (TextUtils.isEmpty(textView.getText().toString())) return;
        String str = textView.getText().toString();
        int bstart = str.indexOf(string);
        if (bstart < 0) return;
        int bend = bstart + string.length();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.BLUE), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(style);
    }
}
