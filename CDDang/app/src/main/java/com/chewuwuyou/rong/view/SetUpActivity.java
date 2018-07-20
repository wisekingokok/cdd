package com.chewuwuyou.rong.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.ui.BusinessPersonalCenterActivity;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ImageLoaderBuilder;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.rong.bean.ClearMessagesBean;
import com.chewuwuyou.rong.bean.RefreshBean;
import com.chewuwuyou.rong.bean.RongUserBean;
import com.chewuwuyou.rong.utils.CDDRongApi;
import com.chewuwuyou.rong.utils.Constant;
import com.chewuwuyou.rong.utils.RongApi;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

import ch.ielse.view.SwitchView;
import de.greenrobot.event.EventBus;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 群设置
 * liuchun
 */
public class SetUpActivity extends CDDBaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//标题
    private TextView mTitel;
    @ViewInject(id = R.id.switchView)
    private SwitchView switchView;//屏蔽群消息
    @ViewInject(id = R.id.delete_chat_record)
    private TextView mDeleteChatRecord;//删除聊天记录
    @ViewInject(id = R.id.network_request)//网络动画
    private LinearLayout mNetworkRequest;
    @ViewInject(id = R.id.network_abnormal_layout)//网络访问
    private LinearLayout mNetworkAbnormalLayout;
    @ViewInject(id = R.id.sub_detail)
    private TextView sub_detail;
    @ViewInject(id = R.id.icon)
    ImageView icon;
    @ViewInject(id = R.id.nike)
    TextView nike;
    @ViewInject(id = R.id.user_info_ll)
    LinearLayout userInfoLL;//根据会话的ID显示隐藏用户信息

    private String userId;
    private Conversation.ConversationType conversationType;
    private String targetId;

    public static final String USER_ID_KEY = "userId";
    public static final String TYPE_KEY = "type_key";
    public static final String TARGET_KEY = "target_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_ac);
        userId = getIntent().getStringExtra(USER_ID_KEY);
        conversationType = (Conversation.ConversationType) getIntent().getSerializableExtra(TYPE_KEY);
        targetId = getIntent().getStringExtra(TARGET_KEY);
        initView();
        initData();
        initEvent();
    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();//返回上一页
                break;
            case R.id.delete_chat_record://删除聊天记录
                GroupOperation();
                break;
            case R.id.icon:
                Intent intent = new Intent(this, PersonalHomeActivity2.class);
                intent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, userId);
                startActivity(intent);
                break;
        }
    }

    /**
     * 初始化界面
     */
    @Override
    protected void initView() {
        mTitel.setText("设置");
        if (targetId.equals(Constant.SERVER_ID) || targetId.equals(Constant.USER_ID_TYPE.ORDER_MSG) || targetId.equals(Constant.USER_ID_TYPE.SYSTEM_MSG)) {
            userInfoLL.setVisibility(View.GONE);
        }
        if (targetId.equals(Constant.USER_ID_TYPE.ORDER_MSG) || targetId.equals(Constant.USER_ID_TYPE.SYSTEM_MSG)) {
            mDeleteChatRecord.setText("清空提醒记录");
        }

    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        if (targetId.equals(Constant.USER_ID_TYPE.ORDER_MSG) || targetId.equals(Constant.USER_ID_TYPE.SYSTEM_MSG))//是订单消息和系统提醒不进行查询
            return;
        CDDRongApi.getUserById(userId, CacheTools.getUserData("rongUserId"), new CDDRongApi.NetWorkCallback<List<RongUserBean>>() {
            @Override
            public void onSuccess(String id, List<RongUserBean> o) {
                if (o == null || o.size() <= 0) return;
                final RongUserBean rongUserBean = o.get(0);
                List<RongUserBean.UrlsBean> urlsBeanns = rongUserBean.getUrls();
                if (urlsBeanns == null || urlsBeanns.size() <= 0) return;
                nike.setText(TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? targetId : rongUserBean.getNick() : rongUserBean.getNoteName());
                ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getImgServer(urlsBeanns.get(0).getUrl())).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).roundedImage(8).displayImage(icon);
                if (rongUserBean.getIsBusOrKeFu() == com.chewuwuyou.app.utils.Constant.CHAT_USER_ROLE.BUSINESS) {
                    sub_detail.setVisibility(View.VISIBLE);
                    mTitel.setText(rongUserBean.getRealName());
                    sub_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SetUpActivity.this, BusinessPersonalCenterActivity.class);
                            intent.putExtra("businessId", rongUserBean.getBusinessId());
                            intent.putExtra("position", "-1");
                            startActivity(intent);
                        }
                    });
                } else
                    sub_detail.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                t.printStackTrace();
            }
        });

    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mDeleteChatRecord.setOnClickListener(this);
        icon.setOnClickListener(this);
        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongApi.setConversationNotificationStatus(conversationType, targetId, !switchView.isOpened() ? Conversation.ConversationNotificationStatus.NOTIFY : Conversation.ConversationNotificationStatus.DO_NOT_DISTURB, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        switch (conversationNotificationStatus) {
                            case DO_NOT_DISTURB:
                                switchView.setOpened(true);
                                break;
                            case NOTIFY:
                                switchView.setOpened(false);
                                break;
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toast.makeText(SetUpActivity.this, "设置失败,请重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        RongApi.getConversationNotificationStatus(conversationType, targetId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                switch (conversationNotificationStatus) {
                    case DO_NOT_DISTURB:
                        switchView.setOpened(true);
                        break;
                    case NOTIFY:
                        switchView.setOpened(false);
                        break;
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    /**
     * 是否退群、是否清空聊天记录
     */
    public void GroupOperation() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.dialog_record, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle); // 添加动画

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        TextView mGroupEmptyMessageTV = (TextView) layout.findViewById(R.id.group_empty_message);
        TextView mGroupEmptyCancelTV = (TextView) layout.findViewById(R.id.group_empty_cancel);
        mGroupEmptyMessageTV.setOnClickListener(new View.OnClickListener() {//清空
            @Override
            public void onClick(View v) {
                RongApi.clearMessages(conversationType, targetId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        EventBus.getDefault().post(new RefreshBean());
                        EventBus.getDefault().post(new ClearMessagesBean());
                        Toast.makeText(SetUpActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toast.makeText(SetUpActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
        mGroupEmptyCancelTV.setOnClickListener(new View.OnClickListener() {//消息
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
