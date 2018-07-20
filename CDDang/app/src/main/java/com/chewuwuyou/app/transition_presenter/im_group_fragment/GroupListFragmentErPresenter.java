package com.chewuwuyou.app.transition_presenter.im_group_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chewuwuyou.app.transition_entity.BaseListBean;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.im_group.GroupListFragmentMondel;
import com.chewuwuyou.app.transition_presenter.BasePresenter;
import com.chewuwuyou.app.transition_view.activity.im_group.EstablishGroupFirstStepErActivtiy;
import com.chewuwuyou.app.transition_view.activity.im_group_fragment.GroupListErFragment;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.widget.SelectFirentDialog;
import com.chewuwuyou.rong.bean.WholeGroup;
import com.chewuwuyou.app.transition_view.activity.RongChatActivity;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * 群列表
 * liuchun
 */

public class GroupListFragmentErPresenter extends BasePresenter {

    private Context mContext;//上下文
    private String mGroup;
    private Message mMessage;
    private GroupListErFragment mGroupListErFragment;
    private GroupListFragmentMondel mGroupListMondel;

    public GroupListFragmentErPresenter(Context context, GroupListErFragment groupListErFragment) {
        super(context);
        this.mContext = context;
        this.mGroupListErFragment = groupListErFragment;
        mGroupListMondel = new GroupListFragmentMondel();
    }

    /**
     * 创建群
     */
    public void createGroup() {
        Intent intent = new Intent(mContext, EstablishGroupFirstStepErActivtiy.class);
        intent.putExtra("type", Constant.GROUP_LIST);
        mContext.startActivity(intent);
    }

    /**
     * 接收传递的群以及消息
     */
    public void groupMessage() {
        Bundle bundle = mGroupListErFragment.getArguments();
        mGroup = bundle.getString("group");
        mMessage = bundle.getParcelable("message");
    }

    /**
     * 请求群数据
     */
    public void requestGroupData() {
        rx.Observable<BaseListBean> observable = mGroupListMondel.requestGroupMondel(CacheTools.getUserData("rongUserId"));
        observable.compose(this.<BaseListBean>applySchedulers()).subscribe(defaultSubscriber(new Action1<BaseListBean>() {
            @Override
            public void call(BaseListBean baseListBean) {
                mGroupListErFragment.networkRequest.setVisibility(View.GONE);
                mGroupListErFragment.networkAbnormalLayout.setVisibility(View.GONE);
                if (baseListBean.getData().size() == 0) {
                    mGroupListErFragment.groupListwu.setVisibility(View.VISIBLE);
                } else {
                    mGroupListErFragment.swipeList.smoothScrollTo(0, 30);
                    mGroupListErFragment.mwholeGroups.clear();
                    mGroupListErFragment.mwholeGroupser.clear();
                    for (int i = 0; i < baseListBean.getData().size(); i++) {
                        if (baseListBean.getData().get(i).getCreateFlag().equals("0")) {
                            mGroupListErFragment.mwholeGroups.add(baseListBean.getData().get(i));
                        } else {
                            mGroupListErFragment.mwholeGroupser.add(baseListBean.getData().get(i));
                        }
                    }
                    mGroupListErFragment.myestablishgrouptv.setText("我创建的群组（" + mGroupListErFragment.mwholeGroups.size() + "）");

                    if (mGroupListErFragment.mwholeGroups.size() >= 10) {
                        mGroupListErFragment.subHeaderBarRightTv.setVisibility(View.GONE);

                    } else {
                        mGroupListErFragment.subHeaderBarRightTv.setVisibility(View.VISIBLE);
                    }
                    mGroupListErFragment.myjoingrouptv.setText("我加入的群组（" + mGroupListErFragment.mwholeGroupser.size() + "）");

                    if (mGroupListErFragment.mwholeGroups.size() == 0 && mGroupListErFragment.mwholeGroupser.size() == 0) {
                        mGroupListErFragment.groupListwu.setVisibility(View.VISIBLE);
                        mGroupListErFragment.swipeList.setVisibility(View.GONE);
                    }
                    if (mGroupListErFragment.mwholeGroups.size() == 0 && mGroupListErFragment.mwholeGroupser.size() > 0) {
                        mGroupListErFragment.groupListwu.setVisibility(View.GONE);
                        mGroupListErFragment.myEstablishGroup.setVisibility(View.GONE);
                        mGroupListErFragment.myJoinGroup.setVisibility(View.VISIBLE);
                    } else {
                        mGroupListErFragment.groupListwu.setVisibility(View.VISIBLE);
                        mGroupListErFragment.myEstablishGroup.setVisibility(View.GONE);
                    }

                    if (mGroupListErFragment.mwholeGroupser.size() == 0 && mGroupListErFragment.mwholeGroups.size() > 0) {
                        mGroupListErFragment.groupListwu.setVisibility(View.GONE);
                        mGroupListErFragment.myJoinGroup.setVisibility(View.GONE);
                        mGroupListErFragment.myEstablishGroup.setVisibility(View.VISIBLE);
                    } else {
                        mGroupListErFragment.groupListwu.setVisibility(View.GONE);
                        mGroupListErFragment.myJoinGroup.setVisibility(View.VISIBLE);
                        mGroupListErFragment.myEstablishGroup.setVisibility(View.GONE);
                    }

                    if (mGroupListErFragment.mwholeGroups.size() > 0 && mGroupListErFragment.mwholeGroupser.size() > 0) {
                        mGroupListErFragment.groupListwu.setVisibility(View.GONE);
                        mGroupListErFragment.myEstablishGroup.setVisibility(View.VISIBLE);
                        mGroupListErFragment.myJoinGroup.setVisibility(View.VISIBLE);
                    }
                    mGroupListErFragment.mGroupListAdapt.notifyDataSetChanged();
                    mGroupListErFragment.mGroupListAdapter.notifyDataSetChanged();
                }
            }
        }, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                mGroupListErFragment.networkRequest.setVisibility(View.GONE);
                mGroupListErFragment.networkAbnormalLayout.setVisibility(View.VISIBLE);
                return true;
            }
        }));

    }

    /**
     * 我加入的群
     */
    public void onClickItemJoinGroup(int position){
        Intent intent;
        if (!TextUtils.isEmpty(mGroup)) {
            WholeGroup userFrined = new WholeGroup();
            userFrined.setImGroupMemberCount(mGroupListErFragment.mwholeGroupser.get(position).getImGroupMemberCount());
            userFrined.setGroupAnnouncement(mGroupListErFragment.mwholeGroupser.get(position).getGroupAnnouncement());
            userFrined.setGroupMain(mGroupListErFragment.mwholeGroupser.get(position).getGroupMain());
            userFrined.setGroupValidate(mGroupListErFragment.mwholeGroupser.get(position).getGroupValidate());
            userFrined.setRemarks(mGroupListErFragment.mwholeGroupser.get(position).getRemarks());
            userFrined.setCreatedAt(mGroupListErFragment.mwholeGroupser.get(position).getCreatedAt());
            userFrined.setCreateFlag(mGroupListErFragment.mwholeGroupser.get(position).getCreateFlag());
            userFrined.setGroupImgUrl(mGroupListErFragment.mwholeGroupser.get(position).getGroupImgUrl());
            userFrined.setGroupName(mGroupListErFragment.mwholeGroupser.get(position).getGroupName());
            userFrined.setGroupSize(mGroupListErFragment.mwholeGroupser.get(position).getGroupSize());
            userFrined.setGroupType(mGroupListErFragment.mwholeGroupser.get(position).getGroupType());
            userFrined.setId(mGroupListErFragment.mwholeGroupser.get(position).getId());
            userFrined.setUpdatedAt(mGroupListErFragment.mwholeGroupser.get(position).getUpdatedAt());

            SelectFirentDialog selectFirentDialog = SelectFirentDialog.getIntense(mMessage, userFrined);
            selectFirentDialog.show(mGroupListErFragment.getFragmentManager(), "SelectFirentDialog");
            selectFirentDialog.setFinishCallback(new SelectFirentDialog.FinishCallback() {
                @Override
                public void finishActivity(boolean isFinishActivity) {
                    if (isFinishActivity) {
                        mGroupListErFragment.getActivity().finish();
                    }
                }
            });
        } else {
            intent = new Intent(mContext, RongChatActivity.class);
            intent.putExtra(RongChatMsgFragment.KEY_TARGET, String.valueOf(mGroupListErFragment.mwholeGroupser.get(position).getId()));
            intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.GROUP);
            mContext.startActivity(intent);

        }
    }
    /**
     * 我加入的群
     */
    public void onClickItemEstablishGroup(int position){
        if (!TextUtils.isEmpty(mGroup)) {
            WholeGroup userFrined = new WholeGroup();
            userFrined.setImGroupMemberCount(mGroupListErFragment.mwholeGroups.get(position).getImGroupMemberCount());
            userFrined.setGroupAnnouncement(mGroupListErFragment.mwholeGroups.get(position).getGroupAnnouncement());
            userFrined.setGroupMain(mGroupListErFragment.mwholeGroups.get(position).getGroupMain());
            userFrined.setGroupValidate(mGroupListErFragment.mwholeGroups.get(position).getGroupValidate());
            userFrined.setRemarks(mGroupListErFragment.mwholeGroups.get(position).getRemarks());
            userFrined.setCreatedAt(mGroupListErFragment.mwholeGroups.get(position).getCreatedAt());
            userFrined.setCreateFlag(mGroupListErFragment.mwholeGroups.get(position).getCreateFlag());
            userFrined.setGroupImgUrl(mGroupListErFragment.mwholeGroups.get(position).getGroupImgUrl());
            userFrined.setGroupName(mGroupListErFragment.mwholeGroups.get(position).getGroupName());
            userFrined.setGroupSize(mGroupListErFragment.mwholeGroups.get(position).getGroupSize());
            userFrined.setGroupType(mGroupListErFragment.mwholeGroups.get(position).getGroupType());
            userFrined.setId(mGroupListErFragment.mwholeGroups.get(position).getId());
            userFrined.setUpdatedAt(mGroupListErFragment.mwholeGroups.get(position).getUpdatedAt());

            SelectFirentDialog selectFirentDialog = SelectFirentDialog.getIntense(mMessage, userFrined);
            selectFirentDialog.show(mGroupListErFragment.getFragmentManager(), "SelectFirentDialog");
            selectFirentDialog.setFinishCallback(new SelectFirentDialog.FinishCallback() {
                @Override
                public void finishActivity(boolean isFinishActivity) {
                    if (isFinishActivity) {
                        mGroupListErFragment.getActivity().finish();
                    }
                }
            });
        } else {
            Intent intent = new Intent(mContext, RongChatActivity.class);
            intent.putExtra(RongChatMsgFragment.KEY_TARGET, String.valueOf(mGroupListErFragment.mwholeGroups.get(position).getId()));
            intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.GROUP);
            mContext.startActivity(intent);
        }
    }
}
