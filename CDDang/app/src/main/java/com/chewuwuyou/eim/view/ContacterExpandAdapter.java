package com.chewuwuyou.eim.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ChatUserInfo;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.ContacterManager.MRosterGroup;
import com.chewuwuyou.eim.model.User;


import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
@SuppressLint("InflateParams")
public class ContacterExpandAdapter extends BaseExpandableListAdapter {

    private List<MRosterGroup> groups = null;
    private LayoutInflater inflater;
    private Context mContext;
    private Map<String, ChatUserInfo> mUserMap;

    public static final int GROUP_TAG = R.string.group_position;
    public static final int CHILD_TAG = R.string.child_position;
    public static final int CHILD_TAG_NULL = R.string.child_position_null;

    private List<String> getList = new ArrayList<>();//将要请求List
    private List<String> isGet = new ArrayList<>();//正在请求List

//    private SwipeLayout currentExpandedSwipeLayout;

    public ContacterExpandAdapter(Context context, List<MRosterGroup> groups, Map<String, ChatUserInfo> userMap) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.groups = groups;
        this.mUserMap = userMap == null ? new HashMap<String, ChatUserInfo>() : userMap;
    }

    public void setContacter(List<MRosterGroup> groups, Map<String, ChatUserInfo> userMap) {
        this.groups = groups;
        this.mUserMap = userMap == null ? new HashMap<String, ChatUserInfo>() : userMap;
        notifyDataSetChanged();
    }

    public void addMap(String key, ChatUserInfo userInfo) {
        this.mUserMap.put(key, userInfo);
        notifyDataSetChanged();
    }

    @Override
    public User getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getUsers().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        User user = groups.get(groupPosition).getUsers().get(childPosition);
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder(convertView = inflater.inflate(R.layout.chat_item, null));
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        convertView.setTag(GROUP_TAG, groupPosition);
        convertView.setTag(CHILD_TAG, childPosition);
        childViewHolder.mImageIV.setTag(user);
        childViewHolder.mImageIV.setImageResource(R.drawable.user_yuan_icon);
//        childViewHolder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
//        childViewHolder.mSwipeLayout.addDrag(SwipeLayout.DragEdge.Right, childViewHolder.mSwipeLayout.findViewWithTag("Bottom2"));
        if (user != null) {
            if (groups != null && groups.size() > 0)
                user.setGroupName(groups.get(groupPosition).getName());
            if (user.isAvailable()) {
                childViewHolder.mUserNameTV.setTextColor(Color.BLACK);
                childViewHolder.mImageIV.setAlpha(1.0f);
            } else {
                childViewHolder.mUserNameTV.setTextColor(Color.GRAY);
                childViewHolder.mImageIV.setAlpha(0.5f);
            }
            if (mUserMap != null && mUserMap.size() > 0
                    && !user.getJID().equals(Constant.XIAODANG)
                    && !user.getJID().equals(Constant.XIAODING)
                    && mUserMap.get(user.getJID().split("@")[0]) != null) {
                ChatUserInfo chatUserInfo = mUserMap.get(user.getJID().split("@")[0]);
                if (!TextUtils.isEmpty(chatUserInfo.getUrl())) {
                    ImageUtils.displayImage(chatUserInfo.getUrl(), childViewHolder.mImageIV, 360, R.drawable.user_yuan_icon, R.drawable.user_yuan_icon);
                } else {
                    childViewHolder.mImageIV.setImageResource(R.drawable.user_yuan_icon);
                }
                childViewHolder.mUserNameTV.setText(CarFriendQuanUtils.showCarFriendName(chatUserInfo.getNoteName(), chatUserInfo.getNick(), user.getName())
                        + "[" + (user.isAvailable() ? "在线" : "离线") + "]");
                childViewHolder.mMoodTV.setText(chatUserInfo.getSignature());
            }
            if (user.getJID().equals(Constant.XIAODING)) {
                childViewHolder.mUserNameTV.setText(user.getName() + "[在线]");
                childViewHolder.mImageIV.setImageResource(R.drawable.xiaodangdi);
                childViewHolder.mUserNameTV.setTextColor(Color.BLACK);
                childViewHolder.mImageIV.setAlpha(1.0f);
            } else if (user.getJID().equals(Constant.XIAODANG)) {
                childViewHolder.mUserNameTV.setText(user.getName() + "[在线]");
                childViewHolder.mImageIV.setImageResource(R.drawable.xiaodangmei);
                childViewHolder.mUserNameTV.setTextColor(Color.BLACK);
                childViewHolder.mImageIV.setAlpha(1.0f);
            }
        }
//        childViewHolder.mSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
//            @Override
//            public void onStartOpen(SwipeLayout layout) {
//                if (currentExpandedSwipeLayout != null && currentExpandedSwipeLayout != layout)
//                    currentExpandedSwipeLayout.close(true);
//
//            }
//
//            @Override
//            public void onOpen(SwipeLayout layout) {
//                currentExpandedSwipeLayout = layout;
//            }
//
//            @Override
//            public void onStartClose(SwipeLayout layout) {
//            }
//
//            @Override
//            public void onClose(SwipeLayout layout) {
//            }
//
//            @Override
//            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
//
//            }
//
//            @Override
//            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
//
//            }
//        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getUsers().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder(convertView = inflater.inflate(R.layout.chat_group, null));
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        convertView.setTag(GROUP_TAG, groupPosition);
        convertView.setTag(CHILD_TAG, CHILD_TAG_NULL);
        if (isExpanded) {
            groupViewHolder.mIsTurnOffIV.setBackgroundResource(R.drawable.icon_arrow1);
        } else {
            groupViewHolder.mIsTurnOffIV.setBackgroundResource(R.drawable.icon_arrow0);
        }
        groupViewHolder.mGroupNameTV.setText(groups.get(groupPosition).getName());
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(groups.get(groupPosition).getCount());
        buffer.append("]");
        groupViewHolder.mOnLineNoTV.setText(buffer.toString());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class GroupViewHolder {
        TextView mOnLineNoTV;
        TextView mGroupNameTV;
        ImageView mIsTurnOffIV;

        public GroupViewHolder(View v) {
            mOnLineNoTV = (TextView) v.findViewById(R.id.onlineno);
            mGroupNameTV = (TextView) v.findViewById(R.id.groupname);
            mIsTurnOffIV = (ImageView) v.findViewById(R.id.is_turn_off_iv);
        }
    }

    class ChildViewHolder {
        private TextView mMoodTV;
        private TextView mUserNameTV;
        private ImageView mImageIV;
     //   public SwipeLayout mSwipeLayout;

        public ChildViewHolder(View v) {
            mMoodTV = (TextView) v.findViewById(R.id.mood);
            mUserNameTV = (TextView) v.findViewById(R.id.username);
            mImageIV = (ImageView) v.findViewById(R.id.child_item_head);
        //    mSwipeLayout = (SwipeLayout) v.findViewById(R.id.swipe);
        }
    }

    public String getKey(int groupPosition, int childPosition) {
        if (groups.get(groupPosition).getUsers() != null) {
            String userJID = groups.get(groupPosition).getUsers().get(childPosition).getJID();
            return userJID.substring(0, userJID.indexOf("@"));
        } else {
            return "";
        }

    }

    public void getDetails(int groupPosition, int childPosition, boolean isClear) {
        if (isClear) getList.clear();
        getList.add(getKey(groupPosition, childPosition));
    }

    public void start() {
        for (int i = 0; i < getList.size(); i++) {
            getDetail(getList.get(i));
        }
    }

    /**
     * @param key userJID.substring(0, userJID.indexOf("@"))
     */
    private void getDetail(final String key) {
        if (isGet.contains(key) || mUserMap.get(key) != null)
            return;
        AjaxParams params = new AjaxParams();
        isGet.add(key);
        params.put("ids", key);
        new NetworkUtil().postMulti(NetworkUtil.GET_CHAT_USER_INFO, params, new AjaxCallBack<String>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                isGet.remove(key);
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject response = new JSONObject(t);
                    switch (response.getInt("result")) {
                        case 0:// 数据有问题的情况
                            break;
                        case 1:// 成功返回:{"result":1,"data":obj}
                            Message msg = new Message();
                            if (response.getString("data") != null && !"".equals(response.getString("data"))) {
                                msg.obj = response.getString("data");
                                msg.what = com.chewuwuyou.app.utils.Constant.NET_DATA_SUCCESS;
                                List<ChatUserInfo> mUserInfos = ChatUserInfo.parseList(msg.obj.toString());
                                for (int i = 0; i < mUserInfos.size(); i++) {
                                    addMap(key, mUserInfos.get(0));
                                }
                                notifyDataSetChanged();
                            } else {
                                msg.what = com.chewuwuyou.app.utils.Constant.NET_DATA_NULL;
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isGet.remove(key);
                }
                isGet.remove(key);
            }
        });
    }
}
