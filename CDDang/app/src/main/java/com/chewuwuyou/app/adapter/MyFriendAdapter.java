package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AllGroup;
import com.chewuwuyou.app.bean.Userfriend;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.widget.SwipeLayout;


import java.util.List;

/**
 * @describe:我的好友adapter
 * @author:tangming
 * @created:
 */
public class MyFriendAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<AllGroup> mGroupList;
    private SwipeLayout lastOpenedSwipeLayout;

    public MyFriendAdapter(Context mContext, List<AllGroup> mGroupList) {
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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_friends_father, null);
            groupHolder = new GroupHolder();
            groupHolder.mGroupPersonal = (TextView) convertView.findViewById(R.id.group_personal);
            groupHolder.mFriendsImgStop = (ImageView) convertView.findViewById(R.id.friends_img_stop);
            groupHolder.mTextView_child_count = (TextView) convertView.findViewById(R.id.group_child_count);
            groupHolder.friends_lay= (TextView) convertView.findViewById(R.id.group_child_count);

            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        if (isExpanded) {
            groupHolder.mFriendsImgStop.setImageResource(R.drawable.friends_father_let);
            groupHolder.friends_lay.setVisibility(View.GONE);
        } else {
            groupHolder.mFriendsImgStop.setImageResource(R.drawable.friends_father_img);
            groupHolder.friends_lay.setVisibility(View.VISIBLE);
        }
        //  groupHolder.mGroupPersonal.setText(mGroupList.get(groupPosition).getFriendGroupName());//显示父视图文字
        groupHolder.mGroupPersonal.setText(mGroupList.get(groupPosition).getFriendGroupName());//显示父视图文字
        groupHolder.mTextView_child_count.setText("(" + mGroupList.get(groupPosition).getFriends().size() + ")");

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemHolder itemHolder = null;

        final Userfriend mUserFriend = mGroupList.get(groupPosition).getFriends().get(childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_mail_father, null);

            itemHolder = new ItemHolder();

            itemHolder.mName = (TextView) convertView
                    .findViewById(R.id.group_head_name);
            itemHolder.mGroupHeadPortrait = (ImageView) convertView
                    .findViewById(R.id.group_head_portrait);

            itemHolder.mTextView_beizhu = (TextView) convertView.findViewById(R.id.beizhu);
            itemHolder.mSwipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);

            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(mGroupList.get(groupPosition).getFriends().get(childPosition).getName()))//nickname是昵称，name是备注
            itemHolder.mName.setText(mGroupList.get(groupPosition).getFriends().get(childPosition).getName());
        else if (!TextUtils.isEmpty(mGroupList.get(groupPosition).getFriends().get(childPosition).getNickname()))
            itemHolder.mName.setText(mGroupList.get(groupPosition).getFriends().get(childPosition).getNickname());
        else
            itemHolder.mName.setText(mGroupList.get(groupPosition).getFriends().get(childPosition).getUserId());
        ImageUtils.displayImage(mGroupList.get(groupPosition).getFriends().get(childPosition).getPortraitUri(), itemHolder.mGroupHeadPortrait, 8, R.drawable.user_fang_icon,
                R.drawable.user_fang_icon);


        final ItemHolder finalItemHolder = itemHolder;
        itemHolder.mTextView_beizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onBeizhu(groupPosition, childPosition);

                }
            }
        });


        itemHolder.mSwipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                //当前 item 被打开时，记录下此 item
                lastOpenedSwipeLayout = swipeLayout;
            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {
                //当前 item 将要打开时关闭上一次打开的 item
                if (lastOpenedSwipeLayout != null) {
                    lastOpenedSwipeLayout.close();
                }
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }
        });


        itemHolder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, PersonalHomeActivity2.class);
                Bundle bundle = new Bundle();
//                bundle.putSerializable(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PRIVATE);
//                bundle.putString(RongChatMsgFragment.KEY_TARGET, mGroupList.get(groupPosition).getFriends().get(childPosition).getUserId());
//                mIntent.putExtras(bundle);
//                mContext.startActivity(mIntent);

                mIntent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, mUserFriend.getUserId());
                mIntent.putExtra(PersonalHomeActivity2.FRIEND_GROUP_ID, mGroupList.get(groupPosition).getGroupId() + "");
                mContext.startActivity(mIntent);


            }
        });


        itemHolder.mGroupHeadPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, PersonalHomeActivity2.class);
                Bundle bundle = new Bundle();
//                bundle.putSerializable(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PRIVATE);
//                bundle.putString(RongChatMsgFragment.KEY_TARGET, mGroupList.get(groupPosition).getFriends().get(childPosition).getUserId());
//                mIntent.putExtras(bundle);
//                mContext.startActivity(mIntent);

                mIntent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, mUserFriend.getUserId());
                mIntent.putExtra(PersonalHomeActivity2.FRIEND_GROUP_ID, mGroupList.get(groupPosition).getGroupId() + "");
                mContext.startActivity(mIntent);


            }
        });





        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupHolder {
        TextView mGroupPersonal;
        ImageView mFriendsImgStop;
        TextView mTextView_child_count;
        TextView friends_lay;

    }

    class ItemHolder {
        TextView mName;
        ImageView mGroupHeadPortrait;
        public SwipeLayout mSwipeLayout;
        TextView mTextView_beizhu;

    }


    public interface onBeizhuListener {

        void onBeizhu(int group, int child);
    }

    public onBeizhuListener mListener;

    public void setListener(onBeizhuListener mListener) {
        this.mListener = mListener;
    }


}
