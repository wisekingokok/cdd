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
import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.rong.bean.WholeGroup;

import java.util.List;

/**
 * 聊天消息适配器
 *
 * @author xxy
 */
public class DeleteGroupAdapter extends BaseAdapter {

    private Context mContext;
    private List<GroupSetUpMemberInformationEr> mWholeGroupList;

    public DeleteGroupAdapter(List<GroupSetUpMemberInformationEr> mWholeGroupList, Context mContext) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_delete_group, null);
            mViewHolder.mGroupHeadPortrait = (ImageView) view.findViewById(R.id.group_head_portrait);
            mViewHolder.mGroupHeadName = (TextView) view.findViewById(R.id.group_head_name);
            mViewHolder.mGroupSelected = (ImageView) view.findViewById(R.id.group_selected);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        if (mWholeGroupList.get(position).isSelected() == true) {
            mViewHolder.mGroupSelected.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bluechecked_icon));
        } else {
            mViewHolder.mGroupSelected.setImageDrawable(mContext.getResources().getDrawable(R.drawable.checkbox_uncheck));
        }

        if(!TextUtils.isEmpty(mWholeGroupList.get(position).getUser_friend_name())&&!mWholeGroupList.get(position).getUser_friend_name().equals("null")){
            mViewHolder.mGroupHeadName.setText(mWholeGroupList.get(position).getUser_friend_name());
        }else if(!TextUtils.isEmpty(mWholeGroupList.get(position).getUser_group_name())&&!mWholeGroupList.get(position).getUser_group_name().equals("null")){
            mViewHolder.mGroupHeadName.setText(mWholeGroupList.get(position).getUser_group_name());
        }else{
            mViewHolder.mGroupHeadName.setText(mWholeGroupList.get(position).getUser_own_name());
        }

        ImageUtils.displayImage(mWholeGroupList.get(position).getHead_image_url(), mViewHolder.mGroupHeadPortrait, 0);
        return view;
    }
    class ViewHolder{
        private ImageView mGroupHeadPortrait,mGroupSelected;
        private TextView mGroupHeadName;
    }

}
