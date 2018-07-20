package com.chewuwuyou.app.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.SelectFirentDialog;
import com.chewuwuyou.app.widget.XListView;
import com.chewuwuyou.rong.adapter.GroupListAdapter;
import com.chewuwuyou.rong.bean.WholeGroup;
import com.chewuwuyou.rong.view.EstablishGroupFirstStepActivtiy;
import com.chewuwuyou.rong.view.GroupSearchActivtiy;
import com.chewuwuyou.app.transition_view.activity.RongChatActivity;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * 活动
 *
 * @author 向
 */
@SuppressLint("ValidFragment")
public class GroupListFragment extends BaseFragment implements View.OnClickListener {

    private View mContentView;
    private ImageButton mSubHeaderBarLeftIbtn;//返回上一页
    private TextView mTitel;//订单标题
    private TextView mHeadberBarRight;//创建群
    private LinearLayout mMyEstablishGroup;//我创建群的布局
    private TextView mMyEstablishGroupTV;//我创建的群数量
    private LinearLayout mMyJoinGroup;//我加入群的布局
    private TextView mMyJoinGroupTV;//我加入的群数量
    private LinearLayout mGroupSearch;//群搜索
    private LinearLayout mNetworkRequest;    //网络动画
    private LinearLayout mNetworkAbnormalLayout;//网络访问
    private TextView mNetworkAgain;//重新加载
    private XListView mEstablishGroupList;//我创建的群组
    private XListView mJoinGroupList;//我加入的群
    private ScrollView mSwipeList;//我加入的群
    private LinearLayout mGroupListwu;

    private List<WholeGroup> mwholeGroups;//我创建
    private List<WholeGroup> mwholeGroupser;//我加入
    private GroupListAdapter mGroupListAdapt;
    private GroupListAdapter mGroupListAdapter;

    private final int ADD_GROUP = 10;//创建群

    private String group;
    private Message mMessage;

    public GroupListFragment(String group, Message mMessage) {
        this.group = group;
        this.mMessage = mMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.group_list_activtiy, null);
        initView();
        initData();
        initEvent();
     return mContentView;
    }

    /**
     * 初始化
     */
    protected void initView() {
        mSubHeaderBarLeftIbtn = (ImageButton) mContentView.findViewById(R.id.sub_header_bar_left_ibtn);
        mTitel = (TextView) mContentView.findViewById(R.id.sub_header_bar_tv);
        mHeadberBarRight = (TextView) mContentView.findViewById(R.id.sub_header_bar_right_tv);
        mMyEstablishGroup = (LinearLayout) mContentView.findViewById(R.id.my_establish_group);
        mMyEstablishGroupTV = (TextView) mContentView.findViewById(R.id.my_establish_group_tv);
        mMyJoinGroup = (LinearLayout) mContentView.findViewById(R.id.my_join_group);
        mGroupSearch = (LinearLayout) mContentView.findViewById(R.id.group_search);
        mMyJoinGroupTV = (TextView) mContentView.findViewById(R.id.my_join_group_tv);
        mNetworkRequest = (LinearLayout) mContentView.findViewById(R.id.network_request);
        mNetworkAbnormalLayout = (LinearLayout) mContentView.findViewById(R.id.network_abnormal_layout);
        mNetworkAgain = (TextView) mContentView.findViewById(R.id.network_again);
        mEstablishGroupList = (XListView) mContentView.findViewById(R.id.my_establish_group_list);
        mJoinGroupList = (XListView) mContentView.findViewById(R.id.my_join_group_list);
        mSwipeList = (ScrollView) mContentView.findViewById(R.id.swipe_list);
        mGroupListwu = (LinearLayout) mContentView.findViewById(R.id.group_listwu);

        mTitel.setText("群组");
        mHeadberBarRight.setText("创建群");
        mwholeGroups = new ArrayList<>();
        mwholeGroupser = new ArrayList<WholeGroup>();
        mGroupListAdapt = new GroupListAdapter(mwholeGroups, getActivity());
        mEstablishGroupList.setAdapter(mGroupListAdapt);
        mGroupListAdapter = new GroupListAdapter(mwholeGroupser, getActivity());
        mJoinGroupList.setAdapter(mGroupListAdapter);
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
            case R.id.sub_header_bar_right_tv://创建群
                intent = new Intent(getActivity(), EstablishGroupFirstStepActivtiy.class);
                intent.putExtra("type", Constant.GROUP_LIST);
                startActivity(intent);
                break;
            case R.id.group_search://群搜索
                intent = new Intent(getActivity(), GroupSearchActivtiy.class);
                intent.putExtra("search_type", Constant.GROUP_LIST_SEARCH);
                intent.putExtra("group", group);
                intent.putExtra("message", mMessage);
                startActivity(intent);
                break;
            case R.id.network_again://重新加载
                mNetworkRequest.setVisibility(View.VISIBLE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                groupList();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mNetworkRequest.setVisibility(View.VISIBLE);//关闭网络动画
        mNetworkAbnormalLayout.setVisibility(View.GONE);
        groupList();//访问网络获取群信息
    }


    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {

    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mHeadberBarRight.setOnClickListener(this);
        mGroupSearch.setOnClickListener(this);
        mNetworkAgain.setOnClickListener(this);
        /**
         * 我创建的群
         */
        mEstablishGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(group)) {
                    WholeGroup userFrined = new WholeGroup();
                    userFrined.setImGroupMemberCount(mwholeGroups.get(position).getImGroupMemberCount());
                    userFrined.setGroupAnnouncement(mwholeGroups.get(position).getGroupAnnouncement());
                    userFrined.setGroupMain(mwholeGroups.get(position).getGroupMain());
                    userFrined.setGroupValidate(mwholeGroups.get(position).getGroupValidate());
                    userFrined.setRemarks(mwholeGroups.get(position).getRemarks());
                    userFrined.setCreatedAt(mwholeGroups.get(position).getCreatedAt());
                    userFrined.setCreateFlag(mwholeGroups.get(position).getCreateFlag());
                    userFrined.setGroupImgUrl(mwholeGroups.get(position).getGroupImgUrl());
                    userFrined.setGroupName(mwholeGroups.get(position).getGroupName());
                    userFrined.setGroupSize(mwholeGroups.get(position).getGroupSize());
                    userFrined.setGroupType(mwholeGroups.get(position).getGroupType());
                    userFrined.setId(mwholeGroups.get(position).getId());
                    userFrined.setUpdatedAt(mwholeGroups.get(position).getUpdatedAt());

                    SelectFirentDialog selectFirentDialog = SelectFirentDialog.getIntense(mMessage, userFrined);
                    selectFirentDialog.show(getFragmentManager(), "SelectFirentDialog");
                    selectFirentDialog.setFinishCallback(new SelectFirentDialog.FinishCallback() {
                        @Override
                        public void finishActivity(boolean isFinishActivity) {
                            if (isFinishActivity) {
                                getActivity().finish();
                            }
                        }
                    });
                } else {
                    Intent intent = new Intent(getActivity(), RongChatActivity.class);
                    intent.putExtra(RongChatMsgFragment.KEY_TARGET, String.valueOf(mwholeGroups.get(position).getId()));
                    intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.GROUP);
                    startActivity(intent);
                }
            }
        });

        /**
         * 我加入的群
         */
        mJoinGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (!TextUtils.isEmpty(group)) {
                    WholeGroup userFrined = new WholeGroup();
                    userFrined.setImGroupMemberCount(mwholeGroupser.get(position).getImGroupMemberCount());
                    userFrined.setGroupAnnouncement(mwholeGroupser.get(position).getGroupAnnouncement());
                    userFrined.setGroupMain(mwholeGroupser.get(position).getGroupMain());
                    userFrined.setGroupValidate(mwholeGroupser.get(position).getGroupValidate());
                    userFrined.setRemarks(mwholeGroupser.get(position).getRemarks());
                    userFrined.setCreatedAt(mwholeGroupser.get(position).getCreatedAt());
                    userFrined.setCreateFlag(mwholeGroupser.get(position).getCreateFlag());
                    userFrined.setGroupImgUrl(mwholeGroupser.get(position).getGroupImgUrl());
                    userFrined.setGroupName(mwholeGroupser.get(position).getGroupName());
                    userFrined.setGroupSize(mwholeGroupser.get(position).getGroupSize());
                    userFrined.setGroupType(mwholeGroupser.get(position).getGroupType());
                    userFrined.setId(mwholeGroupser.get(position).getId());
                    userFrined.setUpdatedAt(mwholeGroupser.get(position).getUpdatedAt());

                    SelectFirentDialog selectFirentDialog = SelectFirentDialog.getIntense(mMessage, userFrined);
                    selectFirentDialog.show(getFragmentManager(), "SelectFirentDialog");
                    selectFirentDialog.setFinishCallback(new SelectFirentDialog.FinishCallback() {
                        @Override
                        public void finishActivity(boolean isFinishActivity) {
                            if (isFinishActivity) {
                                getActivity().finish();
                            }
                        }
                    });
                } else {
                    intent = new Intent(getActivity(), RongChatActivity.class);
                    intent.putExtra(RongChatMsgFragment.KEY_TARGET, String.valueOf(mwholeGroupser.get(position).getId()));
                    intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.GROUP);
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * 获取群列表
     */
    private void groupList() {
        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.GROUP_LIST, params, new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String o) {
                        super.onSuccess(o);
                        try {
                            MyLog.e("---","--群信息 = "+o);
                            JSONObject jsonObject = new JSONObject(o);
                            mSwipeList.smoothScrollTo(0,30);
                            mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                            mNetworkAbnormalLayout.setVisibility(View.GONE);
                            mwholeGroups.clear();
                            mwholeGroupser.clear();
                            ErrorCodeUtil.doErrorCode(getActivity(),jsonObject.optInt("code"),jsonObject.optString("message"));
                            if(TextUtils.isEmpty(jsonObject.get("data").toString())){
                                mGroupListwu.setVisibility(View.VISIBLE);
                                mHeadberBarRight.setVisibility(View.VISIBLE);
                            }else{

                                if(jsonObject.getString("code").equals("0")){
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0;i<jsonArray.length();i++){
                                        if(jsonArray.getJSONObject(i).getString("createFlag").equals("0")){
                                            WholeGroup wholeGroup = new WholeGroup();
                                            wholeGroup.setId(jsonArray.getJSONObject(i).getInt("id"));
                                            wholeGroup.setGroupName(jsonArray.getJSONObject(i).getString("groupName"));
                                            wholeGroup.setGroupType(jsonArray.getJSONObject(i).getString("groupType"));
                                            wholeGroup.setGroupImgUrl(jsonArray.getJSONObject(i).getString("groupImgUrl"));
                                            wholeGroup.setGroupSize(String.valueOf(jsonArray.getJSONObject(i).getInt("groupSize")));
                                            wholeGroup.setGroupValidate(String.valueOf(jsonArray.getJSONObject(i).getInt("groupValidate")));
                                            wholeGroup.setGroupMain(String.valueOf(jsonArray.getJSONObject(i).getInt("groupMain")));
                                            wholeGroup.setGroupAnnouncement(jsonArray.getJSONObject(i).getString("groupAnnouncement"));
                                            wholeGroup.setCreatedAt(String.valueOf(jsonArray.getJSONObject(i).getInt("createdAt")));
                                            wholeGroup.setUpdatedAt(String.valueOf(jsonArray.getJSONObject(i).getInt("updatedAt")));
                                            wholeGroup.setRemarks(jsonArray.getJSONObject(i).getString("remarks"));
                                            wholeGroup.setCreateFlag(jsonArray.getJSONObject(i).getString("createFlag"));
                                            wholeGroup.setImGroupMemberCount(jsonArray.getJSONObject(i).getString("imGroupMemberCount"));
                                            mwholeGroups.add(wholeGroup);
                                        }else{
                                            WholeGroup wholeGroup = new WholeGroup();
                                            wholeGroup.setId(jsonArray.getJSONObject(i).getInt("id"));
                                            wholeGroup.setGroupName(jsonArray.getJSONObject(i).getString("groupName"));
                                            wholeGroup.setGroupType(jsonArray.getJSONObject(i).getString("groupType"));
                                            wholeGroup.setGroupImgUrl(jsonArray.getJSONObject(i).getString("groupImgUrl"));
                                            wholeGroup.setGroupSize(String.valueOf(jsonArray.getJSONObject(i).getInt("groupSize")));
                                            wholeGroup.setGroupValidate(String.valueOf(jsonArray.getJSONObject(i).getInt("groupValidate")));
                                            wholeGroup.setGroupMain(String.valueOf(jsonArray.getJSONObject(i).getInt("groupMain")));
                                            wholeGroup.setGroupAnnouncement(jsonArray.getJSONObject(i).getString("groupAnnouncement"));
                                            wholeGroup.setCreatedAt(String.valueOf(jsonArray.getJSONObject(i).getInt("createdAt")));
                                            wholeGroup.setUpdatedAt(String.valueOf(jsonArray.getJSONObject(i).getInt("updatedAt")));
                                            wholeGroup.setRemarks(jsonArray.getJSONObject(i).getString("remarks"));
                                            wholeGroup.setCreateFlag(jsonArray.getJSONObject(i).getString("createFlag"));
                                            wholeGroup.setImGroupMemberCount(jsonArray.getJSONObject(i).getString("imGroupMemberCount"));
                                            mwholeGroupser.add(wholeGroup);
                                        }
                                    }

                                    mMyEstablishGroupTV.setText("我创建的群组（"+mwholeGroups.size()+"）");

                                    if(mwholeGroups.size()>=10){
                                        mHeadberBarRight.setVisibility(View.GONE);

                                    }else{
                                        mHeadberBarRight.setVisibility(View.VISIBLE);
                                    }
                                    mMyJoinGroupTV.setText("我加入的群组（"+mwholeGroupser.size()+"）");

                                    if(mwholeGroups.size()==0&&mwholeGroupser.size()==0){
                                        mGroupListwu.setVisibility(View.VISIBLE);
                                        mSwipeList.setVisibility(View.GONE);
                                    }

                                    if(mwholeGroups.size()==0&&mwholeGroupser.size()>0){
                                        mGroupListwu.setVisibility(View.GONE);
                                        mMyEstablishGroup.setVisibility(View.GONE);
                                        mMyJoinGroup.setVisibility(View.VISIBLE);
                                    }else{
                                        mGroupListwu.setVisibility(View.VISIBLE);
                                        mMyEstablishGroup.setVisibility(View.GONE);
                                    }

                                    if(mwholeGroupser.size()==0&&mwholeGroups.size()>0) {
                                        mGroupListwu.setVisibility(View.GONE);
                                        mMyJoinGroup.setVisibility(View.GONE);
                                        mMyEstablishGroup.setVisibility(View.VISIBLE);
                                    }else{
                                        mGroupListwu.setVisibility(View.GONE);
                                        mMyJoinGroup.setVisibility(View.VISIBLE);
                                        mMyEstablishGroup.setVisibility(View.GONE);
                                    }

                                    if(mwholeGroups.size()>0&&mwholeGroupser.size()>0){
                                        mGroupListwu.setVisibility(View.GONE);
                                        mMyEstablishGroup.setVisibility(View.VISIBLE);
                                        mMyJoinGroup.setVisibility(View.VISIBLE);
                                    }
                                    mGroupListAdapt.notifyDataSetChanged();
                                    mGroupListAdapter.notifyDataSetChanged();
                                }else{
                                    ToastUtil.toastShow(getActivity(),jsonObject.getString("message"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);

                        ToastUtil.toastShow(getActivity(), "获取群列表失败");
                        mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                        mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                    }
                }

        );
    }
}
