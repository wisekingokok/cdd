package com.chewuwuyou.app.transition_view.activity.im_group;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_presenter.im_group.GroupSetUpErPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;
import com.chewuwuyou.app.widget.MyGridView;
import com.chewuwuyou.rong.utils.RongApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 群设置
 * liuchun
 */

public class GroupSetUpErActivtiy extends BaseTitleActivity {

    @BindView(R.id.network_request)
    public LinearLayout networkRequest;//网络动画
    @BindView(R.id.network_again)//重新加载
    public TextView networkAgain;
    @BindView(R.id.network_abnormal_layout)//网络异常
    public LinearLayout networkAbnormalLayout;
    @BindView(R.id.group_member)//群成员
    public MyGridView groupMember;
    @BindView(R.id.group_grew_tv)//全部群成员显示
    public TextView groupGrewTv;
    @BindView(R.id.all_group_lay)//全部群成员点击事件
    public LinearLayout allGroupLay;
    @BindView(R.id.group_portrait)//群头像
    public ImageView groupPortrait;
    @BindView(R.id.group_right)//群头像图标
    public ImageView groupRight;
    @BindView(R.id.group_portrait_lay)//群头像点击
    public LinearLayout groupPortraitLay;
    @BindView(R.id.group_name)//群名称显示
    public TextView groupName;
    @BindView(R.id.group_name_lay)//群名称点击事件
    public LinearLayout groupNameLay;
    @BindView(R.id.group_notice)//群功能内容
    public TextView groupNotice;
    @BindView(R.id.group_notice_lay)//群公告点击事件
    public LinearLayout groupNoticeLay;
    @BindView(R.id.group_notice_message)//显示群公告内容
    public TextView groupNoticeMessage;
    @BindView(R.id.group_notice_message_lay)//是否显示群公告内容
    public LinearLayout groupNoticeMessageLay;
    @BindView(R.id.switchView)//消息屏蔽
    public SwitchView switchView;
    @BindView(R.id.group_nickname)//群昵称
    public TextView groupNickname;
    @BindView(R.id.group_nickname_lay)//群昵称点击事件
    public LinearLayout groupNicknameLay;
    @BindView(R.id.administration_group_lay)//移交管理权点击事件
    public LinearLayout administrationGroupLay;
    @BindView(R.id.delete_chat_record)//删除聊天记录点击
    public LinearLayout deleteChatRecord;
    @BindView(R.id.exit_group)//解散或退群
    public Button exitGroup;

    private GroupSetUpErPresenter mGroupSetUpErPresenter;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_setup_ac);
        initView();
        initEvent();
    }

    /**
     * 初始化
     */
    public void initView() {
        ButterKnife.bind(this);
        mGroupSetUpErPresenter = new GroupSetUpErPresenter(GroupSetUpErActivtiy.this);
        mGroupSetUpErPresenter.initView();//接收传递的参数
        mGroupSetUpErPresenter.groupEssentiaLinformation();
        mGroupSetUpErPresenter.groupNewsShield();//群屏蔽
        setBarTitle("群创建");
        setLeftBarBtnImage(R.drawable.backbutton);
    }

    private void initDate() {

    }

    /**
     * title监听事件
     *
     * @param view
     * @param title_tag TITLE_TAG_LEFT_BTN 左边按钮,
     *                  TITLE_TAG_LEFT_TV 左边文字按钮,
     *                  TITLE_TAG_RIGHT_BTN ;//右边按钮,
     */
    @Override
    protected void onTitleBarClick(View view, int title_tag) {
        switch (title_tag) {
            case TITLE_TAG_LEFT_BTN:
                finish();
                break;
        }
    }
    private void initEvent() {
        /**
         * 消息屏蔽的点击事件
         */
        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongApi.setConversationNotificationStatus(Conversation.ConversationType.GROUP, mGroupSetUpErPresenter.groupId, !switchView.isOpened() ? Conversation.ConversationNotificationStatus.NOTIFY : Conversation.ConversationNotificationStatus.DO_NOT_DISTURB, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        switch (conversationNotificationStatus) {
                            case DO_NOT_DISTURB:
                                mGroupSetUpErPresenter.networkShield("1");
                                break;
                            case NOTIFY:
                                mGroupSetUpErPresenter.networkShield("0");
                                break;
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        showToast("设置失败,请重试");
                    }
                });
            }
        });
    }

    @OnClick({R.id.group_member, R.id.group_grew_tv, R.id.all_group_lay, R.id.group_portrait, R.id.group_right, R.id.group_portrait_lay, R.id.group_name, R.id.group_name_lay, R.id.group_notice, R.id.group_notice_lay, R.id.group_notice_message, R.id.group_notice_message_lay, R.id.switchView, R.id.group_nickname, R.id.group_nickname_lay, R.id.administration_group_lay, R.id.delete_chat_record, R.id.exit_group})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_member:
                //群名称
                mGroupSetUpErPresenter.groupName();
                break;
            case R.id.group_grew_tv:
                break;
            case R.id.all_group_lay://全部群成员
                mGroupSetUpErPresenter.wholeGroupMember();
                break;
            case R.id.group_portrait:
                break;
            case R.id.group_right:
                break;
            case R.id.group_portrait_lay://群头像
                mGroupSetUpErPresenter.groupPortrait();
                break;
            case R.id.group_name:
                break;
            case R.id.group_name_lay:
                break;
            case R.id.group_notice:
                break;
            case R.id.group_notice_lay://群公告
                mGroupSetUpErPresenter.groupNotice();
                break;
            case R.id.group_notice_message:
                break;
            case R.id.group_notice_message_lay:
                break;
            case R.id.switchView:
                break;
            case R.id.group_nickname:
                break;
            case R.id.group_nickname_lay://群昵称
               mGroupSetUpErPresenter.groupNickname();
                break;
            case R.id.administration_group_lay://移交管理权
               mGroupSetUpErPresenter.transferAdministration();
                break;
            case R.id.delete_chat_record://移交管理权
                mGroupSetUpErPresenter.transferAdministration();
                break;
            case R.id.exit_group:
                break;
        }
    }
}
