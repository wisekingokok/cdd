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

import java.util.List;

/**
 * 聊天消息适配器
 *
 * @author xxy
 */
public class WholeGroupAdapter extends BaseAdapter {

    private Context mContext;
    private List<GroupSetUpMemberInformationEr> mWholeGroupList;

    public WholeGroupAdapter(List<GroupSetUpMemberInformationEr> mWholeGroupList, Context mContext) {
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
        if(!TextUtils.isEmpty(mWholeGroupList.get(position).getUser_group_name())){
            mViewHolder.mGroupHeadName.setText(mWholeGroupList.get(position).getUser_group_name());
        }else{
            mViewHolder.mGroupHeadName.setText(mWholeGroupList.get(position).getUser_own_name());
        }
        mViewHolder.mGroupNumber.setVisibility(View.GONE);
        ImageUtils.displayImage(mWholeGroupList.get(position).getHead_image_url(), mViewHolder.mGroupHeadPortrait, 10, R.drawable.user_fang_icon,
                R.drawable.user_fang_icon);
        return view;
    }
    class ViewHolder{
        private ImageView mGroupHeadPortrait;
        private TextView mGroupHeadName,mGroupNumber;
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
