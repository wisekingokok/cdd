package com.chewuwuyou.rong.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.SearchGroupData;
import com.chewuwuyou.app.utils.ImageUtils;

import java.util.List;

/**
 * 群搜索
 * @author liuchun
 */
public class SearchGroupAdapter extends BaseAdapter {

    private Context mContext;
    private List<SearchGroupData> mWholeGroupList;

    public SearchGroupAdapter(List<SearchGroupData> mWholeGroupList, Context mContext) {
        this.mWholeGroupList = mWholeGroupList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mWholeGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWholeGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (view == null) {
            mViewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group_list, null);
            mViewHolder.mGroupHeadPortrait = (ImageView) view.findViewById(R.id.group_head_portrait);
            mViewHolder.mGroupHeadName = (TextView) view.findViewById(R.id.group_head_name);
            mViewHolder.mGroupNumber = (TextView) view.findViewById(R.id.group_number);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        mViewHolder.mGroupNumber.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(mWholeGroupList.get(position).getGroup_name())){
            mViewHolder.mGroupHeadName.setText(mWholeGroupList.get(position).getGroup_name());
        }else if(!TextUtils.isEmpty(mWholeGroupList.get(position).getUser_group_name())){
            mViewHolder.mGroupHeadName.setText(mWholeGroupList.get(position).getUser_group_name());
        }else{
            mViewHolder.mGroupHeadName.setText(mWholeGroupList.get(position).getUser_own_name());
        }

        if(!TextUtils.isEmpty(mWholeGroupList.get(position).getGroup_img_url())){
            ImageUtils.displayImage(mWholeGroupList.get(position).getGroup_img_url(), mViewHolder.mGroupHeadPortrait, 0);
        }else{
            ImageUtils.displayImage(mWholeGroupList.get(position).getHead_image_url(), mViewHolder.mGroupHeadPortrait, 0);
        }
        return view;
    }
    class ViewHolder{
        private ImageView mGroupHeadPortrait;
        private TextView mGroupHeadName,mGroupNumber;
    }

}
