package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AccountWithdrawal;
import com.chewuwuyou.app.bean.AllGroup;
import com.chewuwuyou.app.bean.ChatPersonal;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.WalletUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe:群创建 第一步 适配器
 * @author:liuchun
 * @created:
 */
public class EstablishGroupFirstStepAdapter extends BaseExpandableListAdapter {


    private Context mContext;
//    private List<String> mFatherGroup;


    private List<AllGroup> mGroupList;

    public EstablishGroupFirstStepAdapter(Context mContext,List<AllGroup> mGroupList) {
        this.mContext = mContext;
        this.mGroupList = mGroupList;
    }

    /**
     * 父视图的个数
     *
     * @return
     */
    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    /**
     * 子视图的个数
     *
     * @param position
     * @return
     */
    @Override
    public int getChildrenCount(int position) {
        return mGroupList.get(position).getFriends().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroupList.get(groupPosition).getFriends().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 父视图显示
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_friends_father, null);
            groupHolder = new GroupHolder();
            groupHolder.mGroupPersonal = (TextView) convertView.findViewById(R.id.group_personal);
            groupHolder.mFriendsImgStop = (ImageView) convertView.findViewById(R.id.friends_img_stop);

            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        if (isExpanded) {
            groupHolder.mFriendsImgStop.setImageResource(R.drawable.friends_father_let);
        } else {
            groupHolder.mFriendsImgStop.setImageResource(R.drawable.friends_father_img);

        }
        groupHolder.mGroupPersonal.setText(mGroupList.get(groupPosition).getFriendGroupName());//显示父视图文字
        return convertView;
    }

    /**
     * 子视图显示
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_son_father, null);
            itemHolder = new ItemHolder();
            itemHolder.mGroupHeadName = (TextView) convertView.findViewById(R.id.group_head_name);
            itemHolder.mGroupSelected = (ImageView) convertView.findViewById(R.id.group_selected);
            itemHolder.mGroupHeadPortrait = (ImageView) convertView.findViewById(R.id.group_head_portrait);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        MyLog.i("------>组" + groupPosition + "孩子" + childPosition + mGroupList.get(groupPosition).getFriends().get(childPosition).getNickname());
        itemHolder.mGroupHeadName.setText(mGroupList.get(groupPosition).getFriends().get(childPosition).getNickname());
        if (mGroupList.get(groupPosition).getFriends().get(childPosition).isSelected() == true) {
            itemHolder.mGroupSelected.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bluechecked_icon));
        } else {
            itemHolder.mGroupSelected.setImageDrawable(mContext.getResources().getDrawable(R.drawable.checkbox_uncheck));
        }
        ImageUtils.displayImage(mGroupList.get(groupPosition).getFriends().get(childPosition).getPortraitUri(), itemHolder.mGroupHeadPortrait, 8, R.drawable.user_fang_icon,
                R.drawable.user_fang_icon);
        return convertView;
    }

    /**
     * 如需点击事件 要返回true
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        TextView mGroupPersonal;
        ImageView mFriendsImgStop;

    }

    class ItemHolder {
        TextView mGroupHeadName;
        ImageView mGroupSelected, mGroupHeadPortrait;
    }
}
