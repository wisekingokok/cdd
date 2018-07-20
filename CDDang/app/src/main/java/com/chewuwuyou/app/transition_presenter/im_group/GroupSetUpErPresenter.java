package com.chewuwuyou.app.transition_presenter.im_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.GroupSetUpMemberAdapter;
import com.chewuwuyou.app.bean.GroupSetUpEssential;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.transition_entity.BaseListBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.ResponseNListBean;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.im_group.GroupSetUpErMondel;
import com.chewuwuyou.app.transition_presenter.BasePresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;
import com.chewuwuyou.app.transition_view.activity.im_group.GroupSetUpErActivtiy;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.TokenObtain;
import com.chewuwuyou.app.widget.MyGridView;
import com.chewuwuyou.rong.utils.RongApi;
import com.chewuwuyou.rong.view.GroupMemberListActivtiy;
import com.chewuwuyou.rong.view.GroupNoticeActivtiy;
import com.chewuwuyou.rong.view.GroupSetUpActivtiy;
import com.chewuwuyou.rong.view.SetUpGroupNickNameaActivtiy;
import com.chewuwuyou.rong.view.TransferGroupManagementActivtiy;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.ielse.view.SwitchView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 群设置
 * liuchun
 */

public class GroupSetUpErPresenter extends BasePresenter {

    private GroupSetUpErActivtiy mGroupSetUpErActivtiy;

    private final int GROUP_NOTICE = 10;//群公告
    private final int GROUP_NAME = 20;//群名称
    private final int GROUP_NICKNAME = 30;//群昵称
    private final int TRANSFER_GROUP = 40;//移交管理权
    private final int ADD_GROUP = 50;//添加成员
    private final int DELETE_GROUP = 60;//删除成员

    private int maskMessage;//判断是否屏蔽消息
    private GroupSetUpErMondel groupSetUpErMondel;
    public String groupId;
    private List<GroupSetUpMemberInformationEr> mAccountList;
    private GroupSetUpMemberAdapter memberAdapter;

    public ResponseNBean<GroupSetUpEssential> mBaseListBean;
    public GroupSetUpErPresenter(GroupSetUpErActivtiy mGroupSetUpErActivtiy) {
        super(mGroupSetUpErActivtiy);
        this.mGroupSetUpErActivtiy = mGroupSetUpErActivtiy;
        groupSetUpErMondel = new GroupSetUpErMondel();
        mAccountList = new ArrayList<GroupSetUpMemberInformationEr>();
    }


    /**
     * 接收传递过来的参数
     */

    public void initView(){
        groupId = mGroupSetUpErActivtiy.getIntent().getStringExtra("groupId");
    }

    /**
     * 访问群基本信息
     */
    public void groupEssentiaLinformation(){

        rx.Observable<ResponseNBean<GroupSetUpEssential>> observable = groupSetUpErMondel.getGroupBasic(groupId,CacheTools.getUserData("rongUserId"));
        observable.compose(this.<ResponseNBean<GroupSetUpEssential>>applySchedulers()).subscribe(defaultSubscriber(new Action1<ResponseNBean<GroupSetUpEssential>>() {
            @Override
            public void call(ResponseNBean<GroupSetUpEssential> baseListBean) {

                mBaseListBean = baseListBean;
                Glide.with(mGroupSetUpErActivtiy).load(baseListBean.getData().getGroup_img_url()).crossFade().into(mGroupSetUpErActivtiy.groupPortrait);

                mGroupSetUpErActivtiy.groupName.setText(baseListBean.getData().getGroup_name() + "  ");

                mGroupSetUpErActivtiy.groupNotice.setText(baseListBean.getData().getMask_message() + "  ");
                mGroupSetUpErActivtiy.groupNickname.setText(baseListBean.getData().getRemark_name() + "  ");
                mGroupSetUpErActivtiy.groupGrewTv.setText("全部群组员（" + baseListBean.getData().getMemberCount() + "）");
                maskMessage = Integer.parseInt(baseListBean.getData().getMask_message());

                if (TextUtils.isEmpty(baseListBean.getData().getGroup_announcement())) {
                    mGroupSetUpErActivtiy.groupNotice.setText("暂无公告  ");
                    mGroupSetUpErActivtiy.groupNoticeMessageLay.setVisibility(View.GONE);
                } else {
                    mGroupSetUpErActivtiy.groupNotice.setText("");
                    mGroupSetUpErActivtiy.groupNoticeMessageLay.setVisibility(View.VISIBLE);
                    mGroupSetUpErActivtiy.groupNoticeMessage.setText(baseListBean.getData().getGroup_announcement());
                }
                if (baseListBean.getData().getGroup_main().equals("0")) {
                    mGroupSetUpErActivtiy.groupNoticeMessageLay.setVisibility(View.VISIBLE);
                } else {
                    mGroupSetUpErActivtiy.groupNoticeMessageLay.setVisibility(View.GONE);
                }
                groupMember();
            }
        }, null,new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                mGroupSetUpErActivtiy.networkRequest.setVisibility(View.GONE);
                mGroupSetUpErActivtiy.networkAbnormalLayout.setVisibility(View.VISIBLE);
                return false;
            }
        }));
    }

    /**
     * 访问得到前30个群成员
     */
    public void groupMember(){
        rx.Observable<ResponseNListBean<GroupSetUpMemberInformationEr>> observable = groupSetUpErMondel.getGroupMember(groupId,CacheTools.getUserData("rongUserId"));
        observable.compose(this.<ResponseNListBean<GroupSetUpMemberInformationEr>>applySchedulers()).subscribe(defaultSubscriber(new Action1<ResponseNListBean<GroupSetUpMemberInformationEr>>() {
            @Override
            public void call(ResponseNListBean<GroupSetUpMemberInformationEr> groupNListBean) {
                mGroupSetUpErActivtiy.networkRequest.setVisibility(View.GONE);
                mGroupSetUpErActivtiy.networkAbnormalLayout.setVisibility(View.GONE);

                List<GroupSetUpMemberInformationEr> data = new ArrayList<GroupSetUpMemberInformationEr>();
                mAccountList.addAll(groupNListBean.getData());
                if (mAccountList.size() > 30) {
                    data.add(new GroupSetUpMemberInformationEr("", "", "", "", "gengduo", ""));
                }
                if (mBaseListBean.getData().getGroup_main().equals("0")) {
                    if (groupNListBean.getData().size() > 30) {
                        mAccountList = groupNListBean.getData().subList(0, 29);
                    }
                    data.add(new GroupSetUpMemberInformationEr("", "", "", "", "jia", ""));
                    if (groupNListBean.getData().size() > 1) {
                        data.add(new GroupSetUpMemberInformationEr("", "", "", "", "jian", ""));
                    }
                    mAccountList.addAll(data);
                } else {
                    if (groupNListBean.getData().size() > 30) {
                        mAccountList = groupNListBean.getData().subList(0, 26);
                    }
                    data.add(new GroupSetUpMemberInformationEr("", "", "", "", "jia", ""));
                    mAccountList.addAll(data);
                }
                if (!mBaseListBean.getData().getGroup_main().equals("0")) {
                    mGroupSetUpErActivtiy.groupRight.setVisibility(View.GONE);
                }
                memberAdapter = new GroupSetUpMemberAdapter(mGroupSetUpErActivtiy, mAccountList);
                mGroupSetUpErActivtiy.groupMember.setAdapter(memberAdapter);
            }
        }, null, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                mGroupSetUpErActivtiy.networkRequest.setVisibility(View.GONE);
                mGroupSetUpErActivtiy.networkAbnormalLayout.setVisibility(View.VISIBLE);
                return false;
            }
        }));
    }

    /**
     * 群公告
     */
    public void groupNotice(){
        Intent intent;
        if (mBaseListBean.getData().getGroup_main().equals("0")) {
            intent = new Intent(mGroupSetUpErActivtiy, GroupNoticeActivtiy.class);
            if (!TextUtils.isEmpty(mBaseListBean.getData().getGroup_announcement())) {
                intent.putExtra("gonggao", mGroupSetUpErActivtiy.groupNoticeMessage.getText().toString());
            }
            intent.putExtra("groupmain", mBaseListBean.getData().getGroup_main());
            intent.putExtra("groupId", groupId);
            mGroupSetUpErActivtiy.startActivityForResult(intent, GROUP_NOTICE);
        } else {
            if (TextUtils.isEmpty(mBaseListBean.getData().getGroup_announcement())) {
                ToastUtil.toastShow(mGroupSetUpErActivtiy, "暂无公告");
            } else {
                intent = new Intent(mGroupSetUpErActivtiy, GroupNoticeActivtiy.class);
                if (!TextUtils.isEmpty(mBaseListBean.getData().getGroup_announcement())) {
                    intent.putExtra("gonggao", mGroupSetUpErActivtiy.groupNoticeMessage.getText().toString());
                }
                intent.putExtra("groupSize", mBaseListBean.getData().getMemberCount());
                intent.putExtra("groupNamae", mBaseListBean.getData().getGroup_name());
                intent.putExtra("groupmain", mBaseListBean.getData().getGroup_main());
                intent.putExtra("groupId", groupId);
                mGroupSetUpErActivtiy.startActivityForResult(intent, GROUP_NOTICE);
            }
        }
    }

    /**
     * 群昵称
     */
    public void groupNickname(){
        Intent intent = new Intent(mGroupSetUpErActivtiy, SetUpGroupNickNameaActivtiy.class);
        intent.putExtra("group_name", "1");
        intent.putExtra("nic", mGroupSetUpErActivtiy.groupNickname.getText().toString().trim());
        intent.putExtra("groupId", groupId);
        mGroupSetUpErActivtiy.startActivityForResult(intent, GROUP_NICKNAME);

    }

    /**
     * 群头像
     */
    public void groupPortrait(){
        if (TextUtils.isEmpty(CacheTools.getUserData("qiniutoken"))) {
            TokenObtain tokenObtain = new TokenObtain();
            tokenObtain.Group(mGroupSetUpErActivtiy);
        }
        if (mBaseListBean.getData().getGroup_main().equals("0")) {
            Intent intent = new Intent(mGroupSetUpErActivtiy, MultiImageSelectorActivity.class);
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
            mGroupSetUpErActivtiy.startActivityForResult(intent, 2);
        } else {
            ToastUtil.toastShow(mGroupSetUpErActivtiy, "您当前没有权限修改群图片");
        }
    }

    /**
     * 退群
     */
    public void retreatGroup(){

    }

    /**
     * 解散群
     */
    public void dissolutionGroup(){



    }

    /**
     * 群消息屏蔽
     */
    public void groupNewsShield(){
        /**
         * 获取融云的消息状态
         */
        RongApi.getConversationNotificationStatus(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                switch (conversationNotificationStatus) {
                    case DO_NOT_DISTURB:
                        mGroupSetUpErActivtiy.switchView.setOpened(true);
                        break;
                    case NOTIFY:
                        mGroupSetUpErActivtiy.switchView.setOpened(false);
                        break;
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }
    /**
     * 群名称
     */
    public void groupName(){
        if (mBaseListBean.getData().getGroup_main().equals("0")) {
            Intent intent = new Intent(mGroupSetUpErActivtiy, SetUpGroupNickNameaActivtiy.class);
            intent.putExtra("group_name", "0");
            intent.putExtra("mingc", mGroupSetUpErActivtiy.groupName.getText().toString().trim());
            intent.putExtra("groupId", groupId);
            mGroupSetUpErActivtiy.startActivityForResult(intent, GROUP_NAME);
        } else {
            ToastUtil.toastShow(mGroupSetUpErActivtiy, "您当前没有权限修改群名称");
        }
    }

    /**
     * 全部群成员
     */
    public void wholeGroupMember(){
        Intent intent = new Intent(mGroupSetUpErActivtiy, GroupMemberListActivtiy.class);
        intent.putExtra("groupName", mGroupSetUpErActivtiy.groupName.getText().toString());
        intent.putExtra("groupId", groupId);
        mGroupSetUpErActivtiy.startActivity(intent);
    }

    /**
     * 移交管理权
     */
    public void transferAdministration(){
        if (mBaseListBean.getData().getGroup_main().equals("0")) {
            Intent intent = new Intent(mGroupSetUpErActivtiy, TransferGroupManagementActivtiy.class);
            intent.putExtra("groupId", groupId);
            intent.putExtra("id", String.valueOf(mAccountList.get(0).getAccid()));
            intent.putExtra("groupName", mGroupSetUpErActivtiy.groupName.getText().toString());
            mGroupSetUpErActivtiy.startActivityForResult(intent, TRANSFER_GROUP);
        }
    }


    /**
     * 屏蔽
     */
    public void networkShield(final String isShield) {

        rx.Observable<ResponseNBean<String>> observable = groupSetUpErMondel.getNetworkShield(groupId,CacheTools.getUserData("rongUserId"),isShield);
        observable.compose(this.<ResponseNBean<String>>applySchedulers()).subscribe(defaultSubscriber(new Action1<ResponseNBean<String>>() {
            @Override
            public void call(ResponseNBean<String> groupNListBean) {
                if (isShield.equals("0")) {
                    mGroupSetUpErActivtiy.switchView.setOpened(false);
                } else {
                    mGroupSetUpErActivtiy.switchView.setOpened(true);
                }
            }
        }, null, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                mGroupSetUpErActivtiy.networkRequest.setVisibility(View.GONE);
                mGroupSetUpErActivtiy.networkAbnormalLayout.setVisibility(View.VISIBLE);
                return false;
            }
        }));

    }
}
