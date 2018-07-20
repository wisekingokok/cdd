package com.chewuwuyou.app.transition_view.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.GroupSetUpEssential;
import com.chewuwuyou.app.bean.PoiAddress;
import com.chewuwuyou.app.transition_adapter.RongMsgAdapter;
import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_presenter.RongChatMsgPresenter;
import com.chewuwuyou.app.transition_view.activity.GroupRedPacketSendActivity;
import com.chewuwuyou.app.transition_view.activity.HairRedActivtiy;
import com.chewuwuyou.app.transition_view.activity.PersonalSendRedActivtiy;
import com.chewuwuyou.app.transition_view.activity.RedPacketDetailActivity;
import com.chewuwuyou.app.transition_view.activity.TransferAccountActivity;
import com.chewuwuyou.app.transition_view.activity.base.BaseFragment;
import com.chewuwuyou.app.transition_view.activity.im_group.GroupSetUpErActivtiy;
import com.chewuwuyou.app.ui.CollectReleaseOrderActivity;
import com.chewuwuyou.app.ui.MainActivityEr;
import com.chewuwuyou.app.utils.AlertDialog;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.XSwipeRefreshLayout;
import com.chewuwuyou.eim.activity.im.BaiduMapActivity;
import com.chewuwuyou.rong.bean.CDDGifMsg;
import com.chewuwuyou.rong.bean.CDDLBSMsg;
import com.chewuwuyou.rong.bean.CDDYWZMsg;
import com.chewuwuyou.rong.bean.ChangeGroupBean;
import com.chewuwuyou.rong.bean.GroupBean;
import com.chewuwuyou.rong.bean.RecallMsgBean;
import com.chewuwuyou.rong.bean.RefreshBean;
import com.chewuwuyou.rong.bean.RongUserBean;
import com.chewuwuyou.rong.bean.SendMsgBean;
import com.chewuwuyou.rong.utils.Constant;
import com.chewuwuyou.rong.utils.KeyboardUtil;
import com.chewuwuyou.rong.utils.RongApi;
import com.chewuwuyou.rong.utils.RongMsgType;
import com.chewuwuyou.rong.utils.RongVoicePlayer;
import com.chewuwuyou.rong.view.SetUpActivity;
import com.chewuwuyou.rong.view.chatview.EaseChatExtendMenu;
import com.chewuwuyou.rong.view.chatview.EaseChatInputMenu;
import com.chewuwuyou.rong.view.chatview.EaseEmojicon;
import com.chewuwuyou.rong.view.chatview.EaseVoiceRecorderView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.VoiceMessage;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * 聊天界面
 * Created by xxy on 2016/9/6 0006.
 */
public class RongChatMsgFragment extends BaseFragment {
    public static final String KEY_TARGET = "chat_key_target";
    public static final String KEY_TYPE = "chat_key_type";
    private static final int REQUEST_IMAGE = 22;
    @BindView(R.id.input_menu)
    EaseChatInputMenu inputMenu;////    inputMenu.hideExtendMenuContainer();//用于初始状态
    @BindView(R.id.voice_recorder)
    EaseVoiceRecorderView voiceRecorderView;
    @BindView(R.id.swipe_list)
    XSwipeRefreshLayout swipe_list;
    @BindView(R.id.chat_list)
    ListView chat_list;
    @BindView(R.id.sub_header_bar_left_ibtn)
    ImageButton back;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.sub_header_bar_tv)
    TextView title;
    @BindView(R.id.sub_header_bar_right_tv)
    TextView setting;
    private RongMsgAdapter adapter;
    private Conversation.ConversationType conversationType;
    private String targetId;
    public String sendId;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");//yyyy-MM-dd H:m:s
    private AlphaAnimation animation;
    private GroupBean groupBean;
    private Long sendTime = 0L;
    private RongChatMsgPresenter presenter;
    public static final String TARGETID_KEY = "targetId";

    /**
     * CacheTools.setUserData("rongUserId", tokenBean.getData().getUserId());
     * CacheTools.setUserData("rongName", tokenBean.getData().getName());
     * CacheTools.setUserData("rongPortraitUri", tokenBean.getData().getPortraitUri());
     *
     * @param conversationType
     * @param targetId
     * @return
     */
    public static RongChatMsgFragment getInstance(Conversation.ConversationType conversationType, String targetId) {
        RongChatMsgFragment myChatFragment = new RongChatMsgFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TYPE, conversationType);
        bundle.putString(KEY_TARGET, targetId);
        myChatFragment.setArguments(bundle);
        return myChatFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rong_chat, null);
        ButterKnife.bind(this, view);
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(chatInputMenuListener);
        swipe_list.setOnRefreshListener(onRefreshListener);
        if (adapter == null)
            adapter = new RongMsgAdapter(getActivity(), null, sendId, conversationType, targetId);
        chat_list.setAdapter(adapter);
        adapter.setScrollTo(new RongMsgAdapter.ScrollTo() {
            @Override
            public void scroll(int positon) {
                chat_list.setSelection(positon);
            }
        });
        adapter.setGetReSendCallback(new RongMsgAdapter.GetReSendCallback() {
            @Override
            public IRongCallback.ISendMessageCallback get() {
                return getSendMsgCallback();
            }
        });
        adapter.setDeleteMsgCallBack(new RongMsgAdapter.DeleteMsgCallBack() {
            @Override
            public void deleteMsgCallBack() {
                presenter.getLatestMessages(false, 0, adapter.getCount(), conversationType, targetId);
            }
        });
        adapter.setUnGroupCallback(new RongMsgAdapter.UnGroupCallback() {
            @Override
            public void unGroup() {
                setting.setVisibility(View.GONE);
            }
        });
        adapter.setRedBClickListener(new RongMsgAdapter.RedBClickListener() {
            @Override
            public void redBClick(String redBId, boolean isSend) {
                presenter.whetherGrabRedPacket(sendId, redBId, isSend);
            }
        });
        title.setText(targetId);
        EventBus.getDefault().register(this);
        swipe_list.callOnRefreshing();
        switch (conversationType) {
            case PRIVATE:
                if (targetId.equals(Constant.USER_ID_TYPE.SYSTEM_MSG)) {
                    title.setText("车当当");
                    registerExtendMenuItem();
                } else if (targetId.equals(Constant.USER_ID_TYPE.ORDER_MSG)) {
                    title.setText("订单提醒");
                    registerExtendMenuItem();
                } else {
                    presenter.getUserById(targetId);
                }
                break;
            case GROUP:
                presenter.getGroupById(targetId);
                presenter.isInGroup(targetId, sendId);
                break;
            case CUSTOMER_SERVICE:
                startService();
                registerExtendMenuItem();
                break;
        }
        inputMenu.setKeyboardCallback(new EaseChatInputMenu.KeyboardCallback() {
            @Override
            public void hideKeyboard() {
                KeyboardUtil.dismissKeyboard(getActivity());
            }
        });
        inputMenu.registerExtendMenuItem("发送图片", R.drawable.tv_picture, R.drawable.tv_picture, easeChatExtendMenuItemClickListener);
        inputMenu.registerExtendMenuItem("分享位置", R.drawable.tv_local, R.drawable.tv_local, easeChatExtendMenuItemClickListener);
//        inputMenu.registerExtendMenuItem("粉碎聊天", R.drawable.delete_chat, R.drawable.delete_chat, easeChatExtendMenuItemClickListener);
        //订单提醒及系统提醒时不显示输入菜单栏
        if (targetId.equals(Constant.USER_ID_TYPE.ORDER_MSG)) {
            title.setText("订单提醒");
        }
        return view;
    }

    public void setSettingVisibility() {
        setting.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendId = CacheTools.getUserData("rongUserId");
        Bundle bundle = getArguments();
        if (bundle != null) {
            conversationType = (Conversation.ConversationType) bundle.getSerializable(KEY_TYPE);
            targetId = bundle.getString(KEY_TARGET);
        }
        presenter = new RongChatMsgPresenter(getActivity(), this);
        //进入时把所有消息变为已读
        presenter.ClearMessagesUnreadStatus(conversationType, targetId);
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (RongApi.getCurrentConnectionStatus()) {
            case CONNECTED://连接成功。
                break;
            case CONNECTING://连接中
                break;
            case DISCONNECTED://断开连接
                RongApi.reconnect(new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        e("token失效");
                    }

                    @Override
                    public void onSuccess(String s) {
                        e("重连成功");
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        e("重连失败");
                    }
                });
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线。
                showToast("帐号已在其他手机登录");
                break;
            case NETWORK_UNAVAILABLE://网络不可用。
                showToast("网络不可用,请检查网络连接");
                break;
            case SERVER_INVALID://服务器异常或无法连接。
                showToast("IM服务异常");
                break;
            case TOKEN_INCORRECT://Token 不正确。
                showToast("网络不可用,请检查网络连接");
                break;
        }
    }

    /**
     * 接收消息
     *
     * @param message
     */
    public void onEventMainThread(Message message) {
        if (message.getTargetId().equals(targetId)) {
            String objectName = message.getObjectName();
            if (objectName.equals(RongMsgType.CDD_TXT_MSG)) {//颜文字
                adapter.addMessage(message);
            } else if (objectName.equals(RongMsgType.GIF_TXT_MSG)) {
                adapter.addMessage(message);
            } else if (objectName.equals(RongMsgType.CDD_LBS_MSG)) {
                adapter.addMessage(message);
            } else if (objectName.equals(RongMsgType.RC_TXT_MSG)) {
                adapter.addMessage(message);
            } else if (objectName.equals(RongMsgType.RC_VC_MSG)) {
                adapter.addMessage(message);
            } else if (objectName.equals(RongMsgType.RC_IMG_MSG)) {
                adapter.addMessage(message);
            } else if (objectName.equals(RongMsgType.RC_IMG_TXT_MSG)) {//图文暂时无用
                // adapter.addMessage(message);
            } else if (objectName.equals(RongMsgType.RC_CHE_MSG)) {//撤回消息
                adapter.addMessage(message);
            } else if (objectName.equals(RongMsgType.RONG_GROUP_NOTIFITION)) {
                adapter.addMessage(message);
            } else if (objectName.equals(RongMsgType.CDD_HB_MSG)) {
                adapter.addMessage(message);
            } else if (objectName.equals(RongMsgType.CDD_ZZ_MSG)) {
                adapter.addMessage(message);
            }
            presenter.ClearMessagesUnreadStatus(conversationType, targetId);
        }
    }

    public void onEventMainThread(RefreshBean refreshBean) {
        presenter.getLatestMessages(false, 0, adapter.getCount(), conversationType, targetId);
    }

    /**
     * 发送消息后收到的通知
     *
     * @param sendMsgBean
     */
    public void onEventMainThread(SendMsgBean sendMsgBean) {
        if (sendMsgBean.message.getTargetId().equals(targetId))
            presenter.getLatestMessages(false, sendMsgBean.reCount, adapter.getCount(), conversationType, targetId);
    }

    /**
     * 解散群关闭聊天通知
     */
    public void onEventMainThread(GroupSetUpEssential groupSetUpEssential) {
        getActivity().finish();
    }

    /**
     * 连接状态监听
     */
    public void onEventMainThread(RongIMClient.ConnectionStatusListener.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED://连接成功。
                presenter.getLatestMessages(false, 0, adapter.getCount(), conversationType, targetId);
                break;
            case CONNECTING://连接中
                break;
            case DISCONNECTED://断开连接
                RongApi.reconnect(new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                    }

                    @Override
                    public void onSuccess(String s) {
                        presenter.getLatestMessages(false, 0, adapter.getCount(), conversationType, targetId);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线。
//                Toast.makeText(getActivity(), "帐号已在其他手机登录", Toast.LENGTH_SHORT).show();
                DialogUtil.loginInOtherPhoneDialog(getActivity());
                break;
            case NETWORK_UNAVAILABLE://网络不可用。
                Toast.makeText(getActivity(), "网络不可用,请检查网络连接", Toast.LENGTH_SHORT).show();
                break;
            case SERVER_INVALID://服务器异常或无法连接。
                Toast.makeText(getActivity(), "IM服务异常", Toast.LENGTH_SHORT).show();
                break;
            case TOKEN_INCORRECT://Token 不正确。
                Toast.makeText(getActivity(), "网络不可用,请检查网络连接", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /**
     * 撤回消息
     *
     * @param recallMsgBean
     */
    public void onEventMainThread(RecallMsgBean recallMsgBean) {
        if (recallMsgBean.getRecallNotificationMessage().getOperatorId().equals(targetId)) {
            presenter.getLatestMessages(false, 0, adapter.getCount(), conversationType, targetId);
        }
    }

    private ProgressDialog progressDialog = null;

    public void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage("加载中...");
        progressDialog.show();
    }

    public void disProgressBar() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void toActivity(RedPacketDetailEntity entity) {
        Intent intent = new Intent(getActivity(), HairRedActivtiy.class);
        intent.putExtra(HairRedActivtiy.ENTITY_KEY, entity);
        startActivity(intent);
    }

    public void toDetailActivity(RedPacketDetailEntity entity) {
        RedPacketDetailActivity.launch(getActivity(), entity);
    }

    /**
     * 群信息变更通知
     *
     * @param changeGroupBean
     */
    public void onEventMainThread(ChangeGroupBean changeGroupBean) {
        switch (conversationType) {
            case PRIVATE:
                break;
            case GROUP:
                if (!TextUtils.isEmpty(changeGroupBean.targetId) && targetId.equals(changeGroupBean.targetId))
                    presenter.getGroupById(targetId);
                break;
            case CUSTOMER_SERVICE:
                break;
        }
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.getLatestMessages(true, 0, adapter.getCount(), conversationType, targetId);
        }
    };

    public void setRefreshing() {
        swipe_list.setRefreshing(false);
    }

    public void setSelection(int i) {
        chat_list.setSelection(i);
    }

    public void setMessage(boolean bool, List<Message> messages) {
        adapter.setMessage(bool, messages);
    }

    public void setTitle(String titleStr) {
        title.setText(titleStr);
    }

    public void setTitle(SpannableStringBuilder spannableStringBuilder) {
        title.setText(spannableStringBuilder);
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public void setTitleCompoundDrawablesWithIntrinsicBounds(Drawable drawable) {
        title.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    public void setGroupBean(GroupBean groupBean) {
        this.groupBean = groupBean;
        adapter.setGroupBean(groupBean);
    }

    public void setRongUserBean(RongUserBean rongUserBean) {
        adapter.setRongUserBean(rongUserBean);
    }

    @OnClick({R.id.sub_header_bar_left_ibtn, R.id.sub_header_bar_right_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                if (!AppManager.isExsitMianActivity(MainActivityEr.class, getActivity())) {//判断首页是否在堆栈中
                    Intent intent = new Intent(getActivity(), MainActivityEr.class);
                    startActivity(intent);
                }
                getActivity().finish();
                break;
            case R.id.sub_header_bar_right_tv:
                if (conversationType == Conversation.ConversationType.PRIVATE) {
                    Intent intent = new Intent(getActivity(), SetUpActivity.class);
                    intent.putExtra(SetUpActivity.USER_ID_KEY, targetId);
                    intent.putExtra(SetUpActivity.TYPE_KEY, conversationType);
                    intent.putExtra(SetUpActivity.TARGET_KEY, targetId);
                    startActivity(intent);
                } else if (conversationType == Conversation.ConversationType.GROUP) {
                    Intent intent = new Intent(getActivity(), GroupSetUpErActivtiy.class);
                    intent.putExtra("groupId", targetId);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 发送消息接口
     */
    private EaseChatInputMenu.ChatInputMenuListener chatInputMenuListener = new EaseChatInputMenu.ChatInputMenuListener() {

        @Override
        public void onSendMessage(String content) {
            sendMsg(CDDYWZMsg.obtain(content), content);
        }

        @Override
        public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
            return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {
                @Override
                public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) { // 发送语音消息
                    if (voiceTimeLength > 60) {
                        ToastUtil.toastShow(getActivity(), "亲.录音过长");
                    }
                    Uri uri = Uri.fromFile(new File(voiceFilePath));
                    sendMsg(VoiceMessage.obtain(uri, voiceTimeLength), "[语音]");
                }
            });
        }

        @Override
        public void onBigExpressionClicked(EaseEmojicon emojicon) {//发送大表情(动态表情)
            String str = emojicon.getEmojiText();
            sendMsg(CDDGifMsg.obtain("#EMO" + str.substring(0, str.length() - 4) + ".gifOME#"), "[表情]");
        }
    };


    private EaseChatExtendMenu.EaseChatExtendMenuItemClickListener easeChatExtendMenuItemClickListener = new EaseChatExtendMenu.EaseChatExtendMenuItemClickListener() {
        @Override
        public void onClick(int itemId, View view) {
            Intent intent;
            switch (itemId) {
                case R.drawable.tv_picture://发送图片
                    Acp.getInstance(getActivity()).request(new AcpOptions.Builder()
                                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                            , Manifest.permission.CAMERA)
                                    .build(),
                            new AcpListener() {
                                @Override
                                public void onGranted() {

                                    Intent intent1 = new Intent(getActivity(), MultiImageSelectorActivity.class);
                                    intent1.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                                    intent1.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                                    intent1.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                                    startActivityForResult(intent1, REQUEST_IMAGE);
                                }

                                @Override
                                public void onDenied(List<String> permissions) {
                                    ToastUtil.toastShow(getActivity(), permissions.toString() + "权限拒绝");
                                }
                            });
                    break;
                case R.drawable.tv_local://分享位置
                    intent = new Intent(getActivity(), BaiduMapActivity.class);
                    startActivityForResult(intent, Constant.REQUEST_CODE_LOCATION);
                    break;
                case R.drawable.delete_chat://粉碎聊天
                    deleteMsg();
                    break;
                case R.drawable.tv_call://电话联系
                    DialogUtil.call(presenter.getmUserTelephone(), getActivity());
                    break;
                case R.drawable.tv_order://给TA下单
                    toOrder();
                    break;
                case R.drawable.mail_address://邮寄地址
                    sendMsg(CDDYWZMsg.obtain(presenter.getAddress()), presenter.getAddress());
                    break;
                case R.drawable.zhuanzhang://转账
                    TransferAccountActivity.start(getActivity(), targetId);
                    break;
                case R.drawable.hongbao_icon:
                    if (conversationType == Conversation.ConversationType.PRIVATE) {
                        intent = new Intent(getActivity(), PersonalSendRedActivtiy.class);//个人红包发布
                    } else {
                        intent = new Intent(getActivity(), GroupRedPacketSendActivity.class);//群红包发布
                    }
                    if (!TextUtils.isEmpty(targetId)) {
                        intent.putExtra(TARGETID_KEY, targetId);
                        startActivity(intent);
                    }

                    break;
            }
        }
    };

    /**
     * 删除聊天消息
     */
    private void deleteMsg() {
        final AlertDialog dialog1 = new AlertDialog(getActivity()).builder();
        dialog1.setTitle("温馨提示");
        dialog1.setMsg("点击确认删除本地聊天记录!");
        dialog1.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongApi.clearMessages(conversationType, targetId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        swipe_list.callOnRefreshing();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog1.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialog1.show();
    }

    /**
     * 向TA下单 区分是商家还是客服
     */
    private void toOrder() {
        if (Integer.valueOf(presenter.getmIsBusOrKefu()) == com.chewuwuyou.app.utils.Constant.CHAT_USER_ROLE.BUSINESS
                || Integer.valueOf(presenter.getmIsBusOrKefu()) == com.chewuwuyou.app.utils.Constant.CHAT_USER_ROLE.SERVER) {// 当前聊天的商家或客服
            if (targetId.equals(CacheTools.getUserData("userId"))) {
                ToastUtil.toastShow(getActivity(), "不能给自己下单");
                return;
            }
            Intent intent = new Intent(getActivity(), CollectReleaseOrderActivity.class);
            intent.putExtra("businessId", presenter.getmBusinesId());
            intent.putExtra("handlerId", presenter.getmHandlerId());
            intent.putExtra("isBusOrKefu", presenter.getmIsBusOrKefu());
            startActivity(intent);
        } else {
            ToastUtil.toastShow(getActivity(), "不是商家，不能给TA下单");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        switch (requestCode) {
            case Constant.REQUEST_CODE_LOCATION:
                PoiAddress poiAddress = (PoiAddress) data.getSerializableExtra(Constant.BAIDU_LOCATION);
                if (poiAddress != null) {//发送位置信息
                    CDDLBSMsg cddlbsMsg = CDDLBSMsg.obtain(poiAddress.getLat(), poiAddress.getLng(), poiAddress.getPoiAddress(), Uri.parse(poiAddress.getImgUrl()));
                    cddlbsMsg.setExtra(poiAddress.getPoiAddressDel());
                    sendMsg(cddlbsMsg, "[位置]");
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.unable_to_get_loaction), Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_IMAGE:
                final List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (final String url : paths) {
                            sendImgMsg(ImageMessage.obtain(Uri.parse("file://" + url), Uri.parse("file://" + url)), "[图片]");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                break;
        }
    }


    private Animation getAnimation() {
        if (animation != null) return animation;
        /** 设置透明度渐变动画 */
        animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);//设置动画持续时间
//animation.setRepeatCount(int repeatCount);//设置重复次数
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//animation.setStartOffset(long startOffset);//执行前的等待时间
        return animation;
    }

    /**
     * 统一发送消息
     *
     * @param messageContent
     * @return
     */
    private void sendMsg(MessageContent messageContent, String pushC) {
        Message message = Message.obtain(targetId, conversationType, messageContent);
        if (conversationType.equals(Conversation.ConversationType.GROUP) && groupBean == null) {//判断群信息为空时不发送群信息
            ToastUtil.toastShow(getActivity(), "网络异常，请求群信息失败");
            return;
        }
        if (conversationType.equals(Conversation.ConversationType.GROUP) && groupBean.getId() == -1) {
            ToastUtil.toastShow(getActivity(), "群已被解散,不能发送消息!");
            return;
        }
        Long thisTime = new Date().getTime();
        if (sendTime != 0L && thisTime - sendTime < 500) {
            tip.setVisibility(View.VISIBLE);
            tip.startAnimation(getAnimation());
            return;
        }
        sendTime = new Date().getTime();
        String pushAuthor = conversationType.equals(Conversation.ConversationType.GROUP) ? (TextUtils.isEmpty(groupBean.getGroupName()) ? targetId : groupBean.getGroupName()) + "(" + groupBean.getImGroupMemberCount() + ")" : CacheTools.getUserData("nickName");
        RongApi.sendMessage(message, pushAuthor + ":" + pushC, simpleDateFormat.format(new Date()), getSendMsgCallback());
    }

    private void sendImgMsg(ImageMessage imageMessage, String pushC) {
        Message message = Message.obtain(targetId, conversationType, imageMessage);
        if (conversationType.equals(Conversation.ConversationType.GROUP) && groupBean == null) {//判断群信息为空时不发送群信息
            ToastUtil.toastShow(getActivity(), "网络异常，请求群信息失败");
            return;
        }
        if (conversationType.equals(Conversation.ConversationType.GROUP) && groupBean.getId() == -1) {
            ToastUtil.toastShow(getActivity(), "群已被解散,不能发送消息!");
            return;
        }
        Long thisTime = new Date().getTime();
        if (sendTime != 0L && thisTime - sendTime < 500) {
            tip.setVisibility(View.VISIBLE);
            tip.startAnimation(getAnimation());
            return;
        }
        sendTime = new Date().getTime();
        String pushAuthor = conversationType.equals(Conversation.ConversationType.GROUP) ? (TextUtils.isEmpty(groupBean.getGroupName()) ? targetId : groupBean.getGroupName()) + "(" + groupBean.getImGroupMemberCount() + ")" : CacheTools.getUserData("nickName");
        RongIMClient.getInstance().sendImageMessage(message, pushAuthor + ":" + pushC, simpleDateFormat.format(new Date()), getSendImgMsgCallback());
    }

    private RongIMClient.SendImageMessageCallback getSendImgMsgCallback() {
        return new RongIMClient.SendImageMessageCallback() {
            @Override
            public void onAttached(Message message) {
                EventBus.getDefault().post(message);
                presenter.getLatestMessages(false, 1, adapter.getCount(), conversationType, targetId);
            }

            @Override
            public void onSuccess(Message message) {
                presenter.getLatestMessages(false, 0, adapter.getCount(), conversationType, targetId);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                presenter.getLatestMessages(false, 0, adapter.getCount(), conversationType, targetId);
            }

            @Override
            public void onProgress(Message message, int i) {

            }
        };
    }

    /**
     * 取得回调
     *
     * @return
     */
    private IRongCallback.ISendMessageCallback getSendMsgCallback() {
        return new IRongCallback.ISendMessageCallback() {

            @Override
            public void onAttached(Message message) {
                EventBus.getDefault().post(message);
                presenter.getLatestMessages(false, 1, adapter.getCount(), conversationType, targetId);
            }

            @Override
            public void onSuccess(Message message) {
                presenter.getLatestMessages(false, 0, adapter.getCount(), conversationType, targetId);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                presenter.getLatestMessages(false, 0, adapter.getCount(), conversationType, targetId);
            }
        };
    }

    /**
     * 注册底部菜单扩展栏item
     */
    public void registerExtendMenuItem() {
        if ((CacheTools.getUserData("role").contains("2") || CacheTools.getUserData("role").contains("3")) && !TextUtils.isEmpty(presenter.getAddress()))
            inputMenu.registerExtendMenuItem("邮寄地址", R.drawable.mail_address, R.drawable.mail_address, easeChatExtendMenuItemClickListener);
        switch (conversationType) {
            case GROUP:
                inputMenu.registerExtendMenuItem("红包", R.drawable.hongbao_icon, R.drawable.hongbao_icon, easeChatExtendMenuItemClickListener);
                break;
            case PRIVATE:
                if (targetId.equals(Constant.USER_ID_TYPE.ORDER_MSG) || targetId.equals(Constant.USER_ID_TYPE.ADD_FRIEND) || targetId.equals(Constant.USER_ID_TYPE.SYSTEM_MSG)) {
                    inputMenu.setVisibility(View.GONE);
                }
                if (presenter.isHavePhone())
                    inputMenu.registerExtendMenuItem("电话联系", R.drawable.tv_call, R.drawable.tv_call, easeChatExtendMenuItemClickListener);
                if (presenter.isHaveOrder())
                    inputMenu.registerExtendMenuItem("给TA下单", R.drawable.tv_order, R.drawable.tv_order, easeChatExtendMenuItemClickListener);
                if (!TextUtils.isEmpty(presenter.getBlack()) && !(targetId.equals(Constant.USER_ID_TYPE.ORDER_MSG) || targetId.equals(Constant.USER_ID_TYPE.ADD_FRIEND) || targetId.equals(Constant.USER_ID_TYPE.SYSTEM_MSG)) && !presenter.getBlack().equals("1")) {
                    inputMenu.registerExtendMenuItem("转账", R.drawable.zhuanzhang, R.drawable.zhuanzhang, easeChatExtendMenuItemClickListener);
                    inputMenu.registerExtendMenuItem("红包", R.drawable.hongbao_icon, R.drawable.hongbao_icon, easeChatExtendMenuItemClickListener);
                }
                break;
        }
    }

    public void showDialog() {
        AlertDialog dialog = new AlertDialog(getActivity()).builder();
        dialog.setTitle("温馨提示");
        dialog.setMsg("连接客服失败");
        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onStop() {
        RongVoicePlayer.getInstance().stopPlayVoice();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 是否消耗返回事件
     */
    public boolean onBackPressed() {
        return inputMenu.onBackPressed();//输入框 是否消费
    }

    private void startService() {
        CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
        CSCustomServiceInfo csInfo = csBuilder.nickName("车当当").build();
        presenter.startCustomService(targetId, csInfo);
    }

}

