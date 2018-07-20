package com.chewuwuyou.app.transition_view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.barcode.view.SweepListView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_presenter.RongMsgPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseFragment;
import com.chewuwuyou.app.ui.ClientHelpActivity;
import com.chewuwuyou.app.ui.MyAttentionActivity;
import com.chewuwuyou.app.ui.OnDateMessageActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.widget.XSwipeRefreshLayout;
import com.chewuwuyou.app.transition_adapter.RongMsgHistoryAdapter;
import com.chewuwuyou.rong.bean.ChangeGroupBean;
import com.chewuwuyou.rong.bean.ClearMessagesBean;
import com.chewuwuyou.rong.bean.RecallMsgBean;
import com.chewuwuyou.rong.bean.SendMsgBean;
import com.chewuwuyou.rong.utils.RongApi;
import com.chewuwuyou.app.transition_view.activity.RongChatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;


/**
 * 消息历史界面
 */
public class RongMessageFragment extends BaseFragment {
    View mContentView;
    @BindView(R.id.main_msg_notify_view)
    View mMsgView;// main_msg_view
    @BindView(R.id.swipeLayout)
    XSwipeRefreshLayout swipeLayout;
    @BindView(R.id.get_knew_iv)
    ImageView mGetKnowIV;// get_knew_iv
    @BindView(R.id.main_invite_list)
    SweepListView mContactMessageList = null;
    @BindView(R.id.today_shixun_tv)
    TextView mTodayShiXunTV;
    @BindView(R.id.server_help_tv)
    TextView mServerHelpTV;
    @BindView(R.id.my_collect_tv)
    TextView mMyCollectTV;// 今日时讯、客服帮助、收藏
    @BindView(R.id.null_con)
    View null_con;

    private RongMsgHistoryAdapter adapter = null;
    private RongMsgPresenter rongMsgPresenter;
    private Unbinder unbinder;


    public static RongMessageFragment getInstance() {
        RongMessageFragment myChatFragment = new RongMessageFragment();
        return myChatFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rongMsgPresenter = new RongMsgPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.rong_message_fm, null);
        unbinder = ButterKnife.bind(this, mContentView);
        mMsgView.setVisibility(View.GONE);
        mContactMessageList.setOnItemClickListener(onItemClickListener);
        if (adapter == null)
            adapter = new RongMsgHistoryAdapter(getActivity());
        mContactMessageList.setAdapter(adapter);
        swipeLayout.setOnRefreshListener(onRefreshListener);
        swipeLayout.callOnRefreshing();
        EventBus.getDefault().register(this);
        return mContentView;
    }

    /**
     * 加载数据
     *
     * @param conversations
     */
    public void setData(List<Conversation> conversations) {
        adapter.setData(conversations);
    }

    public void showNullView() {
        null_con.setVisibility(View.VISIBLE);
    }

    public void disNullView() {
        null_con.setVisibility(View.GONE);
    }


    /**
     * 隐藏刷新
     */
    public void setRefreshing() {
        swipeLayout.setRefreshing(false);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            rongMsgPresenter.onRefresh();
        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Intent intent = new Intent(getActivity(), RongChatActivity.class);
            Conversation conversation = (Conversation) adapter.getItem(arg2 - 1);
            intent.putExtra(RongChatMsgFragment.KEY_TYPE, conversation.getConversationType());
            intent.putExtra(RongChatMsgFragment.KEY_TARGET, conversation.getTargetId());
            startActivity(intent);
        }
    };

    @OnClick({R.id.get_knew_iv, R.id.today_shixun_tv, R.id.server_help_tv, R.id.my_collect_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_knew_iv:
                mMsgView.setVisibility(View.GONE);
                CacheTools.setOtherCacheData("main_msg_get_knew", "1");
                break;
            case R.id.today_shixun_tv:
                startActivity(new Intent(getActivity(), OnDateMessageActivity.class));
                break;
            case R.id.server_help_tv:
                startActivity(new Intent(getActivity(), ClientHelpActivity.class));
                break;
            case R.id.my_collect_tv:
                startActivity(new Intent(getActivity(), MyAttentionActivity.class));
                break;
        }
    }

    /**
     * 接收消息
     *
     * @param message
     */
    public void onEventMainThread(Message message) {
        swipeLayout.callOnRefreshing();
    }

    /**
     * 转发消息
     *
     * @param message
     */
    public void onEventMainThread(SendMsgBean message) {
        swipeLayout.callOnRefreshing();
    }

    public void onEventMainThread(RecallMsgBean recallMsgBean) {
        swipeLayout.callOnRefreshing();
    }

    public void onEventMainThread(ClearMessagesBean clearMessagesBean) {
        swipeLayout.callOnRefreshing();
    }

    /**
     * 群信息变更通知
     *
     * @param changeGroupBean
     */
    public void onEventMainThread(ChangeGroupBean changeGroupBean) {
        adapter.changeGroup(changeGroupBean);
    }

    /**
     * 连接状态监听
     */
    public void onEventMainThread(RongIMClient.ConnectionStatusListener.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED://连接成功。
                swipeLayout.callOnRefreshing();
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
                        swipeLayout.callOnRefreshing();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线。
                DialogUtil.loginInOtherPhoneDialog(getActivity());
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

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroy();
    }
}
