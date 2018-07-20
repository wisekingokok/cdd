package com.chewuwuyou.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.EstablishGroupFirstStepAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.AllGroup;
import com.chewuwuyou.app.bean.BusinessServicePro;
import com.chewuwuyou.app.bean.Userfriend;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.transition_view.activity.im_group.GroupListErActivtiy;
import com.chewuwuyou.app.ui.SearchNewFriendActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.SelectFirentDialog;
import com.chewuwuyou.rong.view.EstablishGroupFirstStepActivtiy;
import com.chewuwuyou.rong.view.GroupEstablishSecondActivtiy;
import com.chewuwuyou.rong.view.GroupListActivtiy;
import com.chewuwuyou.rong.view.GroupMemberListActivtiy;
import com.chewuwuyou.rong.view.GroupSearchActivtiy;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imlib.model.Message;

/**
 * 创建群 第一步
 * liuchun
 */
public class EstablishGroupFirstStepFragment extends com.chewuwuyou.app.fragment.BaseFragment implements View.OnClickListener, SelectFirentDialog.FinishCallback {


    private ImageButton mSubHeaderBarLeftIbtn;
    private TextView mTitel;
    private TextView mHeadberBarRight;

    private LinearLayout mNetworkRequest;//网络动画
    private LinearLayout mNetworkAbnormalLayout;//网络访问
    private TextView mNetworkAgain;

    private ExpandableListView mEstablishGroupFirst;
    private LinearLayout mGroupSearch;
    private EstablishGroupFirstStepAdapter mEstablishGroupAdapter;
    private List<AllGroup> mChatPersonals;

    private LinearLayout mForwardGroup;
    private View mContextView;
    private int index = 0;

    private String group;
    private String type;
    private String groupName;
    private Message message;

    private int addroup = 0;
    private final int ADD_GROUP = 10;
    private int indexGroup;

    public EstablishGroupFirstStepFragment(String group, String type, String groupName, Message message) {
        this.group = group;
        this.type = type;
        this.groupName = groupName;
        this.message = message;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContextView = inflater.inflate(R.layout.item_establish_group_first_step, null);

        initView();
        initData();
        initEvent();
        return mContextView;
    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn://返回上一页
                getActivity().finish();
                break;
            case R.id.forward_group://转发群
                intent = new Intent(getActivity(), GroupListErActivtiy.class);
                intent.putExtra("group", Constant.GROUP_ZHUANFA);
                intent.putExtra("message", message);
                startActivity(intent);
                break;
            case R.id.sub_header_bar_right_tv://下一步

                mHeadberBarRight.setClickable(false);
                String addGroup = "";
                List<Userfriend> mUserFrined = new ArrayList<>();

                if (mChatPersonals != null) {
                    for (int i = 0; i < mChatPersonals.size(); i++) {
                        for (int j = 0; j < mChatPersonals.get(i).getFriends().size(); j++) {
                            if (mChatPersonals.get(i).getFriends().get(j).isSelected() == true) {
                                indexGroup += 1;
                                addGroup += mChatPersonals.get(i).getFriends().get(j).getUserId() + "-";
                                Userfriend userfriend = new Userfriend();
                                userfriend.setName(mChatPersonals.get(i).getFriends().get(j).getName());
                                userfriend.setId(mChatPersonals.get(i).getFriends().get(j).getId());
                                userfriend.setPortraitUri(mChatPersonals.get(i).getFriends().get(j).getPortraitUri());
                                userfriend.setUserId(mChatPersonals.get(i).getFriends().get(j).getUserId());
                                mUserFrined.add(userfriend);
                            }
                        }
                    }
                    if (type.equals("1") || type.equals("2")) {
                        if (!TextUtils.isEmpty(addGroup)) {
                            EstablishGroup(addGroup);
                        } else {
                            ToastUtil.toastShow(getActivity(), "请选择好友");
                            mHeadberBarRight.setClickable(true);
                        }
                    } else if (type.equals(Constant.FORWARD_GROUP)) {
                        if (!TextUtils.isEmpty(addGroup)) {
                            if (addroup > 9) {
                                ToastUtil.toastShow(getActivity(), "最多只能选择9个好友进行转发哦");
                                mHeadberBarRight.setClickable(true);
                            } else {
                                SelectFirentDialog selectFirentDialog = SelectFirentDialog.getIntense(message, mUserFrined);
                                selectFirentDialog.show(getFragmentManager(), "SelectFirentDialog");
                                selectFirentDialog.setFinishCallback(this);
                                mHeadberBarRight.setClickable(true);
                            }

                        } else {
                            ToastUtil.toastShow(getActivity(), "请选择好友");
                            mHeadberBarRight.setClickable(true);
                        }
                    } else {
                        if (!TextUtils.isEmpty(addGroup)) {
                            intent = new Intent(getActivity(), GroupEstablishSecondActivtiy.class);
                            intent.putExtra("addGroup", addGroup);
                            startActivityForResult(intent, 1);
                            mHeadberBarRight.setClickable(true);
                        } else {
                            ToastUtil.toastShow(getActivity(), "请选择好友");
                            mHeadberBarRight.setClickable(true);
                        }
                    }
                } else {
                    mHeadberBarRight.setClickable(true);
                }
                break;
            case R.id.group_search://搜索
                intent = new Intent(getActivity(), GroupSearchActivtiy.class);
                intent.putExtra("search_type", Constant.GROUP_FRIENDS);
                intent.putExtra("list", (Serializable) mChatPersonals);
                startActivityForResult(intent, ADD_GROUP);
                mHeadberBarRight.setClickable(true);
                break;
            case R.id.network_again://点击重新访问
                mNetworkRequest.setVisibility(View.VISIBLE);
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                friendsGroup();//获取好友列表
                break;

        }
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        // 注册EventBus
        EventBus.getDefault().register(this);

        mForwardGroup = (LinearLayout) mContextView.findViewById(R.id.forward_group);
        mNetworkAgain = (TextView) mContextView.findViewById(R.id.network_again);
        mNetworkAbnormalLayout = (LinearLayout) mContextView.findViewById(R.id.network_abnormal_layout);
        mNetworkRequest = (LinearLayout) mContextView.findViewById(R.id.network_request);
        mGroupSearch = (LinearLayout) mContextView.findViewById(R.id.group_search);
        mEstablishGroupFirst = (ExpandableListView) mContextView.findViewById(R.id.establish_group_first);
        mGroupSearch = (LinearLayout) mContextView.findViewById(R.id.group_search);
        mEstablishGroupFirst = (ExpandableListView) mContextView.findViewById(R.id.establish_group_first);
        mTitel = (TextView) mContextView.findViewById(R.id.sub_header_bar_tv);
        mHeadberBarRight = (TextView) mContextView.findViewById(R.id.sub_header_bar_right_tv);
        mSubHeaderBarLeftIbtn = (ImageButton) mContextView.findViewById(R.id.sub_header_bar_left_ibtn);


        mHeadberBarRight.setVisibility(View.VISIBLE);

        if (type.equals("1") || type.equals("2")) {
            mTitel.setText("选择好友");
            mHeadberBarRight.setText("确认");
        } else if (type.equals(Constant.FORWARD_GROUP)) {
            mHeadberBarRight.setText("转发");
            mForwardGroup.setVisibility(View.VISIBLE);
        } else {
            mTitel.setText("创建群");
            mHeadberBarRight.setText("下一步");
        }

    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {

        if (type.equals(Constant.GROUP_SET_UP)) {
            AddGroup();
        } else {
            friendsGroup();//获取好友列表
        }
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mGroupSearch.setOnClickListener(this);
        mNetworkAgain.setOnClickListener(this);
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mHeadberBarRight.setOnClickListener(this);
        mForwardGroup.setOnClickListener(this);
        // 设置item点击的监听器
        mEstablishGroupFirst.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (type.equals("6")) {
                    if (addroup >= 9) {
                        ToastUtil.toastShow(getActivity(), "最多只能选择9个好友进行转发哦");
                    } else {
                        if (mChatPersonals.get(groupPosition).getFriends().get(childPosition).isSelected() == true) {
                            mChatPersonals.get(groupPosition).getFriends().get(childPosition).setSelected(false);
                            addroup -= 1;
                        } else {
                            mChatPersonals.get(groupPosition).getFriends().get(childPosition).setSelected(true);
                            for (int i = 0; i < mChatPersonals.size(); i++) {
                                for (int j = 0; j < mChatPersonals.get(i).getFriends().size(); j++) {
                                    if (groupPosition == i && j == childPosition) {
                                        if (mChatPersonals.get(i).getFriends().get(j).isSelected() == true) {
                                            addroup += 1;
                                        }
                                    }
                                }
                            }
                        }
                        if (type.equals("1") || type.equals("2")) {
                            mHeadberBarRight.setText("确认(" + addroup + ")");
                        } else if (type.equals(Constant.FORWARD_GROUP)) {
                            mHeadberBarRight.setText("转发(" + addroup + ")");
                            mForwardGroup.setVisibility(View.VISIBLE);
                        } else {
                            mHeadberBarRight.setText("下一步(" + addroup + ")");
                        }
                        mEstablishGroupAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (mChatPersonals.get(groupPosition).getFriends().get(childPosition).isSelected() == true) {
                        mChatPersonals.get(groupPosition).getFriends().get(childPosition).setSelected(false);
                        addroup -= 1;
                    } else {
                        mChatPersonals.get(groupPosition).getFriends().get(childPosition).setSelected(true);
                        for (int i = 0; i < mChatPersonals.size(); i++) {
                            for (int j = 0; j < mChatPersonals.get(i).getFriends().size(); j++) {
                                if (groupPosition == i && j == childPosition) {
                                    if (mChatPersonals.get(i).getFriends().get(j).isSelected() == true) {
                                        addroup += 1;
                                    }
                                }
                            }
                        }
                    }
                    if (type.equals("1") || type.equals("2")) {
                        mHeadberBarRight.setText("确认(" + addroup + ")");
                    } else if (type.equals(Constant.FORWARD_GROUP)) {
                        mHeadberBarRight.setText("转发(" + addroup + ")");
                        mForwardGroup.setVisibility(View.VISIBLE);
                    } else {
                        mHeadberBarRight.setText("下一步(" + addroup + ")");
                    }
                    mEstablishGroupAdapter.notifyDataSetChanged();
                }

                return false;
            }
        });
    }

    /**
     * 网络好友列表
     */
    private void friendsGroup() {
        AjaxParams params = new AjaxParams();
        MyLog.e("YUY", "登录融云的用户Id = " + CacheTools.getUserData("rongUserId"));
        params.put("userId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.GET_ALL_FRIEND, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess
                    (String s) {
                super.onSuccess(s);
                mNetworkRequest.setVisibility(View.GONE);
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    ErrorCodeUtil.doErrorCode(getActivity(), jsonObject.optInt("code"), jsonObject.optString("message"));
                    if (jsonObject.getString("code").equals("0")) {
                        mHeadberBarRight.setClickable(true);
                        mNetworkAbnormalLayout.setVisibility(View.GONE);
                        MyLog.e("YUY", "用户信息 ------" + jsonObject);
                        mChatPersonals = AllGroup.parseList(jsonObject.getJSONArray("data").toString());
                        mEstablishGroupAdapter = new EstablishGroupFirstStepAdapter(getActivity(), mChatPersonals);

                        mEstablishGroupFirst.setAdapter(mEstablishGroupAdapter);
                        for (int i = 0; i < mEstablishGroupAdapter.getGroupCount(); i++) {
                            mEstablishGroupFirst.expandGroup(i);
                        }
                    } else {
                        ToastUtil.toastShow(getActivity(), jsonObject.getString("message"));
                        mHeadberBarRight.setClickable(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                ToastUtil.toastShow(getActivity(), "网络异常");
                mHeadberBarRight.setClickable(true);

            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case ADD_GROUP:

                    for (int i = 0; i < mChatPersonals.size(); i++) {
                        for (int j = 0; j < mChatPersonals.get(i).getFriends().size(); j++) {
                            if (mChatPersonals.get(i).getFriends().get(j).getUserId().equals(data.getStringExtra("userId"))) {
                                if (mChatPersonals.get(i).getFriends().get(j).isSelected() == false) {
                                    mChatPersonals.get(i).getFriends().get(j).setSelected(true);
                                    addroup += 1;
                                }
                            }
                        }
                    }
                    if (type.equals("1") || type.equals("2")) {
                        mHeadberBarRight.setText("确认(" + addroup + ")");
                    } else if (type.equals(Constant.FORWARD_GROUP)) {
                        mHeadberBarRight.setText("转发(" + addroup + ")");

                    } else {
                        mHeadberBarRight.setText("下一步(" + addroup + ")");
                    }
                    mEstablishGroupAdapter.notifyDataSetChanged();
                    mHeadberBarRight.setClickable(true);
                    break;

            }
        }
    }

    @Override
    public void finishActivity(boolean isFinishActivity) {
        if (isFinishActivity) {
            getActivity().finish();
        }
    }

    /**
     * EventBus接收传递的数据
     *
     * @param busAdapter
     */
    public void onEventMainThread(EventBusAdapter busAdapter) {
        if (busAdapter.getGroupEstablish().equals("0")) {
            getActivity().finish();
        }
    }


    public void AddGroup() {

        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("groupId", group);

        NetworkUtil.get(NetworkUtil.ADD_GROUP_MEMBE, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess
                    (String s) {
                super.onSuccess(s);
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    ErrorCodeUtil.doErrorCode(getActivity(), jsonObject.optInt("code"), jsonObject.optString("message"));
                    if (jsonObject.getString("code").equals("0")) {
                        mNetworkRequest.setVisibility(View.GONE);
                        mNetworkAbnormalLayout.setVisibility(View.GONE);
                        mHeadberBarRight.setClickable(true);
                        mChatPersonals = AllGroup.parseList(jsonObject.getJSONArray("data").toString());
                        mEstablishGroupAdapter = new EstablishGroupFirstStepAdapter(getActivity(), mChatPersonals);
                        mEstablishGroupFirst.setAdapter(mEstablishGroupAdapter);
                        for (int i = 0; i < mEstablishGroupAdapter.getGroupCount(); i++) {
                            mEstablishGroupFirst.expandGroup(i);
                        }
                    } else {
                        ToastUtil.toastShow(getActivity(), jsonObject.getString("message"));
                        mHeadberBarRight.setClickable(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                mNetworkRequest.setVisibility(View.GONE);
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                ToastUtil.toastShow(getActivity(), "网络异常");
                mHeadberBarRight.setClickable(true);
            }
        });

    }

    /**
     * 添加群成员
     */
    private void EstablishGroup(String addGroup) {
        AjaxParams params = new AjaxParams();
        params.put("groupName", groupName);
        params.put("groupId", group);
        params.put("loginUserId", CacheTools.getUserData("rongUserId"));
        params.put("userIds", addGroup);
        NetworkUtil.get(NetworkUtil.ADD_SET_GROUP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess
                    (String s) {
                super.onSuccess(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("code").equals("0")) {
                        mHeadberBarRight.setClickable(true);
                        Intent intent = new Intent();
                        getActivity().setResult(getActivity().RESULT_OK, intent);
                        getActivity().finish();
                    } else {
                        ToastUtil.toastShow(getActivity(), jsonObject.getString("message"));
                        mHeadberBarRight.setClickable(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(getActivity(), "网络异常");
                mHeadberBarRight.setClickable(true);
            }
        });
    }
}
