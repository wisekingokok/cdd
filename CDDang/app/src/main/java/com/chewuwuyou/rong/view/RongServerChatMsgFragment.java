package com.chewuwuyou.rong.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.GroupSetUpEssential;
import com.chewuwuyou.app.ui.MainActivityEr;
import com.chewuwuyou.app.utils.AlertDialog;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.widget.XSwipeRefreshLayout;
import com.chewuwuyou.rong.adapter.RongServerMsgAdapter;
import com.chewuwuyou.rong.bean.CDDGifMsg;
import com.chewuwuyou.rong.bean.CDDYWZMsg;
import com.chewuwuyou.rong.bean.ClearMessagesBean;
import com.chewuwuyou.rong.bean.GroupBean;
import com.chewuwuyou.rong.bean.RecallMsgBean;
import com.chewuwuyou.rong.bean.RefreshBean;
import com.chewuwuyou.rong.bean.RongUserBean;
import com.chewuwuyou.rong.bean.SendMsgBean;
import com.chewuwuyou.rong.utils.Constant;
import com.chewuwuyou.rong.utils.RongApi;
import com.chewuwuyou.rong.utils.RongMsgType;
import com.chewuwuyou.rong.view.chatview.EaseChatExtendMenu;
import com.chewuwuyou.rong.view.chatview.EaseChatInputMenu;
import com.chewuwuyou.rong.view.chatview.EaseEmojicon;
import com.chewuwuyou.rong.view.chatview.EaseVoiceRecorderView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imlib.CustomServiceConfig;
import io.rong.imlib.ICustomServiceListener;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.CSGroupItem;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.CustomServiceMode;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.VoiceMessage;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 客服聊天界面
 * Created by xxy on 2016/9/6 0006.
 */
public class RongServerChatMsgFragment extends Fragment {
    public static final String KEY_TARGET = "chat_key_target";
    public static final String KEY_TYPE = "chat_key_type";
    private static final int REQUEST_IMAGE = 22;

    private EaseChatInputMenu inputMenu;////    inputMenu.hideExtendMenuContainer();//用于初始状态
    private EaseVoiceRecorderView voiceRecorderView;
    private XSwipeRefreshLayout swipe_list;
    private ListView chat_list;
    private RongServerMsgAdapter adapter;
    private Conversation.ConversationType conversationType;
    private String targetId;
    private ImageButton back;
    private TextView title;
    private TextView setting;
    public String sendId;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");//yyyy-MM-dd H:m:s
    private GroupBean groupBean;
    private RongUserBean targetUser;
    private TextView mChangeServerTV;//转人工客服
    private ImageView mChangeInputIV;//切换输入方式
    private RelativeLayout mChangeServerRL;//切换人工客服
    private ImageView mChangeServiceIV;//切换到可切换人工的操作
    private FrameLayout mInputLL;//输入布局
    private CustomServiceConfig mCustomServiceConfig = null;

    /**
     * CacheTools.setUserData("rongUserId", tokenBean.getData().getUserId());
     * CacheTools.setUserData("rongName", tokenBean.getData().getName());
     * CacheTools.setUserData("rongPortraitUri", tokenBean.getData().getPortraitUri());
     *
     * @param conversationType
     * @param targetId
     * @return
     */
    public static RongServerChatMsgFragment getInstance(Conversation.ConversationType conversationType, String targetId) {
        RongServerChatMsgFragment myChatFragment = new RongServerChatMsgFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TYPE, conversationType);
        bundle.putString(KEY_TARGET, targetId);
        myChatFragment.setArguments(bundle);
        return myChatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboard();
        sendId = CacheTools.getUserData("rongUserId");
        Bundle bundle = getArguments();
        if (bundle != null) {
            conversationType = (Conversation.ConversationType) bundle.getSerializable(KEY_TYPE);
            targetId = bundle.getString(KEY_TARGET);
        }
        //进入时把所有消息变为已读
        RongApi.clearMessagesUnreadStatus(conversationType, targetId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                EventBus.getDefault().post(new ClearMessagesBean(conversationType, targetId));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("RongChatMsgFragment", "重置状态失败");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rong_server_chat, null);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        voiceRecorderView = (EaseVoiceRecorderView) view.findViewById(R.id.voice_recorder);
        inputMenu = (EaseChatInputMenu) view.findViewById(R.id.input_menu);
        swipe_list = (XSwipeRefreshLayout) view.findViewById(R.id.swipe_list);
        chat_list = (ListView) view.findViewById(R.id.chat_list);
        back = (ImageButton) view.findViewById(R.id.sub_header_bar_left_ibtn);
        title = (TextView) view.findViewById(R.id.sub_header_bar_tv);
        setting = (TextView) view.findViewById(R.id.sub_header_bar_right_tv);
        mChangeServerTV = (TextView) view.findViewById(R.id.change_person_service_tv);
        mChangeInputIV = (ImageView) view.findViewById(R.id.change_input_iv);
        mChangeServerRL = (RelativeLayout) view.findViewById(R.id.change_server_rl);
        mChangeServiceIV = (ImageView) view.findViewById(R.id.change_server_iv);
        mInputLL = (FrameLayout) view.findViewById(R.id.input_server_ll);
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(chatInputMenuListener);
        swipe_list.setOnRefreshListener(onRefreshListener);
        if (adapter == null)
            adapter = new RongServerMsgAdapter(getActivity(), null, sendId, mCustomServiceConfig);
        chat_list.setAdapter(adapter);
        adapter.setScrollTo(new RongServerMsgAdapter.ScrollTo() {
            @Override
            public void scroll(int positon) {
                chat_list.setSelection(positon);
            }
        });
        adapter.setGetReSendCallback(new RongServerMsgAdapter.GetReSendCallback() {
            @Override
            public IRongCallback.ISendMessageCallback get() {
                return getSendMsgCallback();
            }

        });
        adapter.setDeleteMsgCallBack(new RongServerMsgAdapter.DeleteMsgCallBack() {
            @Override
            public void deleteMsgCallBack() {
                getLatestMessages(false, 0);
            }
        });
        back.setOnClickListener(onClick);
        setting.setOnClickListener(onClick);
        title.setText("车当当客服");
        mChangeServerTV.setOnClickListener(onClick);
        mChangeInputIV.setOnClickListener(onClick);
        mChangeServiceIV.setOnClickListener(onClick);
        mInputLL.setOnClickListener(onClick);
        EventBus.getDefault().register(this);
        swipe_list.callOnRefreshing();
        startService();
        registerExtendMenuItem();
        return view;
    }

    private RongIMClient.SendImageMessageCallback getSendImgMsgCallback() {
        return new RongIMClient.SendImageMessageCallback() {
            @Override
            public void onAttached(Message message) {
                EventBus.getDefault().post(message);
                getLatestMessages(false, 1);
            }

            @Override
            public void onSuccess(Message message) {
                getLatestMessages(false, 0);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                getLatestMessages(false, 0);
            }

            @Override
            public void onProgress(Message message, int i) {

            }
        };
    }


    /**
     * 接收消息
     *
     * @param message
     */
    public void onEventMainThread(Message message) {
        MyLog.i("YUY", "收到客服消息 = " + message.getObjectName());
        if (message.getSenderUserId().equals(targetId)) {
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
            } else if (objectName.equals(RongMsgType.RONG_SERVER_NOTIFITION)) {
                adapter.addMessage(message);
            }
        }
    }

    public void onEventMainThread(RefreshBean refreshBean) {
        getLatestMessages(false, 0);
    }

    /**
     * 发送消息后收到的通知
     *
     * @param sendMsgBean
     */
    public void onEventMainThread(SendMsgBean sendMsgBean) {
        if (sendMsgBean.message.getTargetId().equals(targetId))
            getLatestMessages(false, sendMsgBean.reCount);
    }

    /**
     * 解散群关闭聊天通知
     */
    public void onEventMainThread(GroupSetUpEssential groupSetUpEssential) {
        getActivity().finish();
    }


    /**
     * 撤回消息
     *
     * @param recallMsgBean
     */
    public void onEventMainThread(RecallMsgBean recallMsgBean) {
        if (recallMsgBean.getRecallNotificationMessage().getOperatorId().equals(targetId)) {
            getLatestMessages(false, 0);
        }
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getLatestMessages(true, 0);
        }
    };

    /**
     * 取本地消息
     *
     * @param isPull
     * @param addSize
     */
    private void getLatestMessages(final boolean isPull, int addSize) {
        final int oldCount = adapter.getCount();
        int count = oldCount + (isPull ? 10 : 0) + addSize;
        RongApi.getLatestMessages(conversationType, targetId, count < 10 ? 10 : count, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                if (messages == null) messages = new ArrayList<>();
                Collections.reverse(messages);
                adapter.setMessage(!isPull, messages);
                if (isPull)
                    chat_list.setSelection(messages.size() - oldCount);
                swipe_list.setRefreshing(false);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                swipe_list.setRefreshing(false);
            }
        });
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sub_header_bar_left_ibtn:
                    if (AppManager.isExsitMianActivity(MainActivityEr.class, getActivity()) == false) {//判断首页是否在堆栈中
                        startActivity(new Intent(getActivity(), MainActivityEr.class));
                    }
                    getActivity().finish();
                    break;
                case R.id.sub_header_bar_right_tv:
                    if (conversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {//客服点击设置
                        Intent intent = new Intent(getActivity(), SetUpActivity.class);
                        intent.putExtra(SetUpActivity.TARGET_KEY, Constant.SERVER_ID);
                        intent.putExtra(SetUpActivity.TYPE_KEY, Conversation.ConversationType.CUSTOMER_SERVICE);
                        intent.putExtra(SetUpActivity.USER_ID_KEY, Constant.SERVER_ID);
                        startActivity(intent);
                    }
                    break;
                case R.id.change_person_service_tv://启动在线客服
                    MyLog.i("YUY", "启动在线客服");
                    RongIMClient.getInstance().switchToHumanMode(targetId);

                    break;
                case R.id.change_input_iv:
                    mChangeServerRL.setVisibility(View.GONE);
                    mInputLL.setVisibility(View.VISIBLE);//切换为输入菜单
                    break;
                case R.id.change_server_iv:
                    mChangeServerRL.setVisibility(View.VISIBLE);
                    mInputLL.setVisibility(View.GONE);//切换可切换客服模式
                    break;
            }
        }
    };

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
                    Intent intent1 = new Intent(getActivity(), MultiImageSelectorActivity.class);
                    intent1.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                    intent1.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                    intent1.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                    startActivityForResult(intent1, REQUEST_IMAGE);
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
        dialog1.setNegativeButton("取消", null);
        dialog1.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        switch (requestCode) {
            case REQUEST_IMAGE:
                List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for (String url : paths) {
                    sendMsg(ImageMessage.obtain(Uri.parse("file://" + url), Uri.parse("file://" + url)), "[图片]");
                }
                break;
        }
    }

    /**
     * 统一发送消息
     *
     * @param messageContent
     * @return
     */
    private void sendMsg(MessageContent messageContent, String pushC) {
        Message message = Message.obtain(targetId, conversationType, messageContent);
        RongApi.sendMessage(message, pushC, String.valueOf(System.currentTimeMillis()), getSendMsgCallback());
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
                getLatestMessages(false, 1);
            }

            @Override
            public void onSuccess(Message message) {
                getLatestMessages(false, 0);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                getLatestMessages(false, 0);
            }
        };
    }

    /**
     * 注册底部菜单扩展栏item
     */
    protected void registerExtendMenuItem() {
        inputMenu.registerExtendMenuItem("发送图片", R.drawable.tv_picture, R.drawable.tv_picture, easeChatExtendMenuItemClickListener);
        //订单提醒及系统提醒时不显示输入菜单栏
        if (targetId.equals(Constant.USER_ID_TYPE.ORDER_MSG + "") || targetId.equals(Constant.USER_ID_TYPE.ADD_FRIEND + "") || targetId.equals(Constant.USER_ID_TYPE.SYSTEM_MSG + "")) {
            inputMenu.setVisibility(View.GONE);
        }
        switch (conversationType) {
            case GROUP:
                break;
            case PRIVATE:
                break;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        RongIMClient.getInstance().stopCustomService(targetId);
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
        RongIMClient.getInstance().startCustomService(targetId, new ICustomServiceListener() {

            @Override
            public void onSuccess(CustomServiceConfig customServiceConfig) {
                MyLog.i("YUY", "启动客服onSuccess " + customServiceConfig.companyName + " 头像 = " + customServiceConfig.companyIcon);
//                title.setText(customServiceConfig.companyName);
                mCustomServiceConfig = customServiceConfig;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                MyLog.i("YUY", "启动客服失败");
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
            public void onModeChanged(CustomServiceMode customServiceMode) {
                MyLog.i("YUY", "启动客服onModeChanged " + customServiceMode);
                if (customServiceMode.equals(customServiceMode.CUSTOM_SERVICE_MODE_HUMAN)) {
                    mChangeServerRL.setVisibility(View.GONE);
                    mInputLL.setVisibility(View.VISIBLE);//切换为输入菜单
                    mChangeServiceIV.setVisibility(View.GONE);
                    inputMenu.setPadding(0, 0, 0, 0);
                } else {
                    mChangeServiceIV.setVisibility(View.VISIBLE);
                    inputMenu.setPadding(150, 0, 0, 0);
                }
            }

            @Override
            public void onQuit(String s) {
                MyLog.i("YUY", "启动客服onQuit");
            }

            @Override
            public void onPullEvaluation(String s) {
                MyLog.i("YUY", "启动客服onPullEvaluation");
            }

            @Override
            public void onSelectGroup(List<CSGroupItem> list) {
                MyLog.i("YUY", "启动客服onSelectGroup");
            }
        }, csInfo);
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        ;
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}

