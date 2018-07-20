package com.chewuwuyou.app.transition_presenter.im_group_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chewuwuyou.app.adapter.EstablishGroupFirstStepAdapter;
import com.chewuwuyou.app.bean.AllGroup;
import com.chewuwuyou.app.bean.Userfriend;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.transition_entity.AddGroupMember;
import com.chewuwuyou.app.transition_entity.BaseListBean;
import com.chewuwuyou.app.transition_entity.EstablishGroup;
import com.chewuwuyou.app.transition_entity.EstablishGroupSuccess;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.im_group.EstabGroupFirstStepMondel;
import com.chewuwuyou.app.transition_model.im_group.GroupListFragmentMondel;
import com.chewuwuyou.app.transition_presenter.BasePresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseFragment;
import com.chewuwuyou.app.transition_view.activity.im_group.GroupEstablishSecondErActivtiy;
import com.chewuwuyou.app.transition_view.activity.im_group.GroupListErActivtiy;
import com.chewuwuyou.app.transition_view.activity.im_group_fragment.EstablishGroupFirstStepErFragment;
import com.chewuwuyou.app.transition_view.activity.im_group_fragment.GroupListErFragment;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.SelectFirentDialog;
import com.chewuwuyou.rong.view.EstablishGroupFirstStepActivtiy;
import com.chewuwuyou.rong.view.GroupEstablishSecondActivtiy;
import com.chewuwuyou.rong.view.GroupSearchActivtiy;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imlib.model.Message;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * 创建群第一步
 * liuchun
 */

public class EstablishGroupFirstStepErPresenter extends BasePresenter {

    private Context mContext;//上下文
    private String mType,mGroupId,mGroupName;
    private Message mMessage;
    private EstablishGroupFirstStepErFragment mGroupListErFragment;
    private List<AllGroup> mChatPersonals;
    private int indexGroup;
    private int addroup = 0;
    private EstabGroupFirstStepMondel mEstabGroupFirstStepMondel;
    private EstablishGroupFirstStepAdapter mEstablishGroupAdapter;//适配器
    private final int ADD_GROUP = 10;

    public EstablishGroupFirstStepErPresenter(Context context, EstablishGroupFirstStepErFragment groupListErFragment) {
        super(context);
        this.mContext = context;
        this.mGroupListErFragment = groupListErFragment;
        mEstabGroupFirstStepMondel = new EstabGroupFirstStepMondel();
    }
    /**
     * 接收传递过来的参数
     */
    public void initData(){
        // 注册EventBus
        EventBus.getDefault().register(this);
        Bundle bundle = mGroupListErFragment.getArguments();
        mType = bundle.getString("type");
        mGroupId = bundle.getString("groupId");
        mGroupName = bundle.getString("groupName");
        mMessage = bundle.getParcelable("msg");

        if (mType.equals(Constant.GROUP_SET_UP) || mType.equals(Constant.WHOLE_GROUP)) {
            mGroupListErFragment.subHeaderBarTv.setText("选择好友");
            mGroupListErFragment.subHeaderBarRightTv.setText("确认");
        } else if (mType.equals(Constant.FORWARD_GROUP)) {
            mGroupListErFragment.subHeaderBarRightTv.setText("转发");
            mGroupListErFragment.forwardGroup.setVisibility(View.VISIBLE);
        } else {
            mGroupListErFragment.subHeaderBarTv.setText("创建群");
            mGroupListErFragment.subHeaderBarRightTv.setText("下一步");
        }
    }

    /**
     * 转发到群
     */
    public void forwardGroup(){
        Intent intent = new Intent(mContext, GroupListErActivtiy.class);
        intent.putExtra("group", Constant.GROUP_ZHUANFA);
        intent.putExtra("message", mMessage);
        mContext.startActivity(intent);
    }

    /**
     * 下一步
     */
    public void groupNextStep(){
        mGroupListErFragment.isClick(false);
        List<Userfriend> mUserFrined = new ArrayList<>();
        String addGroup = "";
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
            if (mType.equals(Constant.GROUP_SET_UP) || mType.equals(Constant.WHOLE_GROUP)) {
                if (!TextUtils.isEmpty(addGroup)) {
                    addGroupMondel(addGroup);
                } else {
                    ((BaseFragment)mGroupListErFragment).showToast("请选择好友");
                    mGroupListErFragment.isClick(true);
                }
            } else if (mType.equals(Constant.FORWARD_GROUP)) {
                if (!TextUtils.isEmpty(addGroup)) {
                    if (addroup > 9) {
                        ((BaseFragment)mGroupListErFragment).showToast("最多只能选择9个好友进行转发哦");
                        mGroupListErFragment.isClick(true);
                    } else {
                        SelectFirentDialog selectFirentDialog = SelectFirentDialog.getIntense(mMessage, mUserFrined);
                        selectFirentDialog.show(mGroupListErFragment.getFragmentManager(), "SelectFirentDialog");
                   //     selectFirentDialog.setFinishCallback(this);
                        mGroupListErFragment.isClick(true);
                    }

                } else {
                    ((BaseFragment)mGroupListErFragment).showToast("请选择好友");
                    mGroupListErFragment.isClick(true);
                }
            } else {
                if (!TextUtils.isEmpty(addGroup)) {
                    Intent intent = new Intent(mContext, GroupEstablishSecondErActivtiy.class);
                    intent.putExtra("addGroup", addGroup);
                    mGroupListErFragment.startActivityForResult(intent, 1);
                    mGroupListErFragment.isClick(true);
                } else {
                    ((BaseFragment)mGroupListErFragment).showToast("请选择好友");
                    mGroupListErFragment.isClick(true);
                }
            }
        } else {
            mGroupListErFragment.isClick(true);
        }
    }

    /**
     * 添加群成员
     */
    private void addGroupMondel(String addGroup) {
        rx.Observable<AddGroupMember> observable = mEstabGroupFirstStepMondel.addGroupMondel(mGroupName, mGroupId, CacheTools.getUserData("rongUserId"), addGroup);
        observable.compose(this.<AddGroupMember>applySchedulers()).subscribe(defaultSubscriber(new Action1<AddGroupMember>() {
            @Override
            public void call(AddGroupMember groupMember) {
                mGroupListErFragment.isClick(true);
                Intent intent = new Intent();
                mGroupListErFragment.getActivity().setResult(mGroupListErFragment.getActivity().RESULT_OK, intent);
                mGroupListErFragment.getActivity().finish();
            }
        }, new Action0() {
            @Override
            public void call() {
                mGroupListErFragment.networkRequest.setVisibility(View.GONE);
                mGroupListErFragment.networkAbnormalLayout.setVisibility(View.GONE);
            }
        }, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                mGroupListErFragment.networkRequest.setVisibility(View.GONE);
                mGroupListErFragment.networkAbnormalLayout.setVisibility(View.VISIBLE);
                return false;
            }
        }));
    }
    /**
     *创建群选择群成员列表
     */
     public void EstablishGroup(){
         rx.Observable<EstablishGroup> observable = mEstabGroupFirstStepMondel.EstablisFilterGroupMember(CacheTools.getUserData("rongUserId"),mGroupId);
         observable.compose(this.<EstablishGroup>applySchedulers()).subscribe(defaultSubscriber(new Action1<EstablishGroup>() {
             @Override
             public void call(EstablishGroup establishGroup) {
                 mGroupListErFragment.isClick(true);
                 mChatPersonals = establishGroup.getData();
                 mEstablishGroupAdapter = new EstablishGroupFirstStepAdapter(mContext, mChatPersonals);
                 mGroupListErFragment.establishGroupFirst.setAdapter(mEstablishGroupAdapter);
                 for (int i = 0; i < mEstablishGroupAdapter.getGroupCount(); i++) {
                     mGroupListErFragment.establishGroupFirst.expandGroup(i);
                 }
             }
         }, new Action0() {
             @Override
             public void call() {
                 mGroupListErFragment.networkRequest.setVisibility(View.GONE);
                 mGroupListErFragment.networkAbnormalLayout.setVisibility(View.GONE);
             }
         }, new Func1<CustomException, Boolean>() {
             @Override
             public Boolean call(CustomException e) {
                 mGroupListErFragment.networkRequest.setVisibility(View.GONE);
                 mGroupListErFragment.networkAbnormalLayout.setVisibility(View.VISIBLE);
                 return false;
             }
         }));
     }

    /**
     * 群成员列表
     */
    private void GroupMemberList() {
        rx.Observable<EstablishGroup> observable = mEstabGroupFirstStepMondel.groupMondelList(CacheTools.getUserData("rongUserId"));
        observable.compose(this.<EstablishGroup>applySchedulers()).subscribe(defaultSubscriber(new Action1<EstablishGroup>() {
            @Override
            public void call(EstablishGroup establishGroup) {
                mGroupListErFragment.networkRequest.setVisibility(View.GONE);
                mGroupListErFragment.networkAbnormalLayout.setVisibility(View.GONE);
                mChatPersonals = establishGroup.getData();
                mEstablishGroupAdapter = new EstablishGroupFirstStepAdapter(mContext, mChatPersonals);

                mGroupListErFragment.establishGroupFirst.setAdapter(mEstablishGroupAdapter);
                for (int i = 0; i < mEstablishGroupAdapter.getGroupCount(); i++) {
                    mGroupListErFragment.establishGroupFirst.expandGroup(i);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mGroupListErFragment.networkRequest.setVisibility(View.GONE);
                mGroupListErFragment.networkAbnormalLayout.setVisibility(View.GONE);
            }
        }, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                mGroupListErFragment.networkRequest.setVisibility(View.GONE);
                mGroupListErFragment.networkAbnormalLayout.setVisibility(View.VISIBLE);
                return false;
            }
        }));
    }

    /**
     * 根据界面点击访问不同的的接口
     */
    public void visitInterface(){
        if (mType.equals(Constant.GROUP_SET_UP)) {
            EstablishGroup();
        } else {
            GroupMemberList();
        }
    }

    /**
     * EventBus接收传递的数据
     *
     * @param busAdapter
     */
    public void onEventMainThread(EstablishGroupSuccess busAdapter) {
            mGroupListErFragment.getActivity().finish();
    }

    /**
     * onActivityResult
     */
    public void onGroupResult(int requestCode,Intent data){
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
                    if (mType.equals("1") || mType.equals("2")) {
                        mGroupListErFragment.subHeaderBarRightTv.setText("确认(" + addroup + ")");
                    } else if (mType.equals(Constant.FORWARD_GROUP)) {
                        mGroupListErFragment.subHeaderBarRightTv.setText("转发(" + addroup + ")");

                    } else {
                        mGroupListErFragment.subHeaderBarRightTv.setText("下一步(" + addroup + ")");
                    }
                    mEstablishGroupAdapter.notifyDataSetChanged();
                    mGroupListErFragment.isClick(true);
                    break;

            }
        }
    }

    /**
     * 搜索
     */
    public void groupMondelSearch(){
        Intent intent = new Intent(mContext, GroupSearchActivtiy.class);
        intent.putExtra("search_type", Constant.GROUP_FRIENDS);
        intent.putExtra("list", (Serializable) mChatPersonals);
        mGroupListErFragment.getActivity().startActivityForResult(intent, ADD_GROUP);
        mGroupListErFragment.isClick(true);
    }

    /**
     * 点击事件
     */
    public void onclickItemExpandable(int groupPosition,int childPosition){
        if (mType.equals("6")) {
            if (addroup >= 9) {
                ((BaseFragment)mGroupListErFragment).showToast("最多只能选择9个好友进行转发哦");
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
                if (mType.equals(Constant.GROUP_SET_UP) || mType.equals(Constant.WHOLE_GROUP)) {
                    mGroupListErFragment.subHeaderBarRightTv.setText("确认(" + addroup + ")");
                } else if (mType.equals(Constant.FORWARD_GROUP)) {
                    mGroupListErFragment.subHeaderBarRightTv.setText("转发(" + addroup + ")");
                    mGroupListErFragment.forwardGroup.setVisibility(View.VISIBLE);
                } else {
                    mGroupListErFragment.subHeaderBarRightTv.setText("下一步(" + addroup + ")");
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
            if (mType.equals(Constant.GROUP_SET_UP) || mType.equals(Constant.WHOLE_GROUP)) {
                mGroupListErFragment.subHeaderBarRightTv.setText("确认(" + addroup + ")");
            } else if (mType.equals(Constant.FORWARD_GROUP)) {
                mGroupListErFragment.subHeaderBarRightTv.setText("转发(" + addroup + ")");
                mGroupListErFragment.forwardGroup.setVisibility(View.VISIBLE);
            } else {
                mGroupListErFragment.subHeaderBarRightTv.setText("下一步(" + addroup + ")");
            }
            mEstablishGroupAdapter.notifyDataSetChanged();
        }
    }
}
