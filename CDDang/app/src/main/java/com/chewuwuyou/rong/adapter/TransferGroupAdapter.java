package com.chewuwuyou.rong.adapter;

import android.content.Context;
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
 *移交管理员适配器
 * @author liuchun
 */
public class TransferGroupAdapter extends BaseAdapter {

    private Context mContext;
    private List<GroupSetUpMemberInformationEr> mWholeGroupList;

    public TransferGroupAdapter(List<GroupSetUpMemberInformationEr> mWholeGroupList, Context mContext) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_transfer_group, null);
            mViewHolder.mGroupHeadPortrait = (ImageView) view.findViewById(R.id.group_head_portrait);
            mViewHolder.mGroupHeadName = (TextView) view.findViewById(R.id.group_head_name);
            mViewHolder.mCatalog = (TextView) view.findViewById(R.id.catalog);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            mViewHolder.mCatalog.setVisibility(View.VISIBLE);
            mViewHolder.mCatalog.setText(mWholeGroupList.get(position).getSortLetters());
        }else{
            mViewHolder.mCatalog.setVisibility(View.GONE);
        }
        mViewHolder.mGroupHeadName.setText(mWholeGroupList.get(position).getUser_friend_name());
        ImageUtils.displayImage(mWholeGroupList.get(position).getHead_image_url(), mViewHolder.mGroupHeadPortrait, 10, R.drawable.user_fang_icon,
                R.drawable.user_fang_icon);
        return view;
    }
    class ViewHolder{
        private ImageView mGroupHeadPortrait;
        private TextView mGroupHeadName,mCatalog;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mWholeGroupList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mWholeGroupList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }
}
