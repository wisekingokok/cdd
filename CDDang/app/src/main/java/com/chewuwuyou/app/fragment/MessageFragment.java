package com.chewuwuyou.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.barcode.view.SweepListView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ChatUserInfo;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.UserChatHistory;
import com.chewuwuyou.app.ui.ClientHelpActivity;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.ui.MyAttentionActivity;
import com.chewuwuyou.app.ui.OnDateMessageActivity;
import com.chewuwuyou.app.ui.SearchFriendActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.eim.activity.im.ChatActivity;
import com.chewuwuyou.eim.manager.ContacterManager;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.ChartHisBean;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.model.User;
import com.chewuwuyou.eim.view.RecentChartAdapter;

import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息历史界面
 */
public class MessageFragment extends ChatBaseFragment implements
        OnClickListener {

    private View mContentView;

    // private EditText mQueryET;// 搜索好友输入框
    // private ImageButton mClearSearchIBtn;
    // private BadgeView mAddFriendMsgView;
    private WakeLock mWakeLock;
    private View mMsgView;// main_msg_view
    private ImageView mGetKnowIV;// get_knew_iv
    private SweepListView mContactMessageList = null;
    private RecentChartAdapter mNoticeAdapter = null;
    private List<ChartHisBean> mInviteNotices = new ArrayList<ChartHisBean>();
    private StringBuilder mUserIdBuilder;// 用户id拼接用于查询用户头像及昵称
    private List<ChatUserInfo> mUserInfos;// 用户的头像和昵称的集合
    private TextView mTodayShiXunTV, mServerHelpTV, mMyCollectTV;// 今日时讯、客服帮助、收藏
    // private List<Notice> mNotices;// 取得加我好友的信息
    private List<UserChatHistory> mUserChatHistories;// 历史消息记录

    /**
     * 更新数据
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            deleteChatMsg(Integer.parseInt(msg.obj.toString()));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.message_fm, null);
        initView();
        initData();
        initEvent();
        return mContentView;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {

        // mAddFriendMsgView = new BadgeView(getActivity(), mFrindIBtn);//
        // 在按钮上显示加我的好友数
        // mAddFriendMsgView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        mContactMessageList = (SweepListView) mContentView
                .findViewById(R.id.main_invite_list);
        // mQueryET = (EditText) mContentView.findViewById(R.id.query);
        // mClearSearchIBtn = (ImageButton) mContentView
        // .findViewById(R.id.search_clear);
        mMsgView = (View) mContentView.findViewById(R.id.main_msg_notify_view);
        mGetKnowIV = (ImageView) mMsgView.findViewById(R.id.get_knew_iv);
        mTodayShiXunTV = (TextView) mContentView
                .findViewById(R.id.today_shixun_tv);
        mServerHelpTV = (TextView) mContentView
                .findViewById(R.id.server_help_tv);
        mMyCollectTV = (TextView) mContentView.findViewById(R.id.my_collect_tv);

    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        // android.view.ViewGroup.LayoutParams layout = mTitleHeight
        // .getLayoutParams();
        // mTitleHeight.getLayoutParams();
        // DisplayMetrics metric = new DisplayMetrics();
        // getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // int density = metric.densityDpi;
        // layout.height = 64 * density / 240; // 240当前手机的像素
        // mTitleHeight.setLayoutParams(layout);
        mUserChatHistories = new ArrayList<UserChatHistory>();
        if (mNoticeAdapter == null) {
            mNoticeAdapter = new RecentChartAdapter(this.getActivity(),
                    mUserChatHistories, mHandler);
        }
        mContactMessageList.setAdapter(mNoticeAdapter);
        // 因为目前的提示需要重新设计 所以注释起来
        // mMsgView.setVisibility(CacheTools
        // .getOtherCacheData("main_msg_get_knew") != null
        // && CacheTools.getOtherCacheData("main_msg_get_knew")
        // .equals("1") ? View.GONE : View.VISIBLE);
        mMsgView.setVisibility(View.GONE);
    }

    /**
     * 事件点击
     */
    @Override
    protected void initEvent() {

        // mClearSearchIBtn.setOnClickListener(this);
        mTodayShiXunTV.setOnClickListener(this);
        mServerHelpTV.setOnClickListener(this);
        mMyCollectTV.setOnClickListener(this);
        mGetKnowIV.setOnClickListener(this);
        // mQueryET.addTextChangedListener(new TextWatcher() {
        // public void onTextChanged(CharSequence s, int start, int before,
        // int count) {
        //
        // // adapter.getFilter().filter(s);
        // if (s.length() > 0) {
        // mClearSearchIBtn.setVisibility(View.VISIBLE);
        // } else {
        // mClearSearchIBtn.setVisibility(View.INVISIBLE);
        // }
        // }
        //
        // public void beforeTextChanged(CharSequence s, int start, int count,
        // int after) {
        // }
        //
        // public void afterTextChanged(Editable s) {
        // }
        // });

        mContactMessageList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String jid = mUserChatHistories.get(arg2 - 1).getFrom();
                String room = mUserChatHistories.get(arg2 - 1).getRoom();// 获得群聊房间ID
                User user = ContacterManager.getNickname(jid,
                        XmppConnectionManager.getInstance().getConnection());
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                UserChatHistory notice = mUserChatHistories.get(arg2-1);
                String userName="";
                if (!TextUtils.isEmpty(notice.getRoom())) {
                    userName = notice.getRoom().split("@")[0];
                } else if (user!=null&&user.getJID().equals("xiaoding@iz232jtyxeoz")) {
                    userName = user.getName();
                } else if (user!=null&&user.getJID().equals("xiaodang@iz232jtyxeoz")) {
                    userName = user.getName();
                } else if(user!=null){
                    userName = CarFriendQuanUtils.showCarFriendName(
                            notice.getNoteName(), notice.getNickName(), user.getName());
                }
                if (!TextUtils.isEmpty(room)) {// 判断群聊情况
                    intent.putExtra("to", room);
                } else if (user == null) {
                    intent.putExtra("to", jid);
                } else {
                    intent.putExtra("to", user.getJID());
                }
                MyLog.e("YUY", "聊天中的title-------" + userName);
                intent.putExtra("title", userName);
                startActivity(intent);
            }
        });
    }

    private void getUserChatHistory() {
        mUserChatHistories.clear();
        mUserIdBuilder = new StringBuilder("");
        // from = 5@iz232jtyxeoz
        mInviteNotices = MessageManager.getInstance(getActivity())
                .getRecentContactsWithLastMsg();
        if (mInviteNotices != null && mInviteNotices.size() > 0) {
            UserChatHistory chatHistory;
            for (int i = 0; i < mInviteNotices.size(); i++) {
                chatHistory = new UserChatHistory();
                chatHistory.setId(mInviteNotices.get(i).getId());
                chatHistory.setMessage(mInviteNotices.get(i).getContent());
                chatHistory.setTime(mInviteNotices.get(i).getNoticeTime());
                chatHistory.setRoom(mInviteNotices.get(i).getRoom());
                chatHistory.setFrom(mInviteNotices.get(i).getFrom());
                chatHistory.setNoticeSum(mInviteNotices.get(i).getNoticeSum());
                mUserChatHistories.add(chatHistory);
                // 是否存在room数据 -- 也就是群聊
                if (!TextUtils.isEmpty(mInviteNotices.get(i).getRoom())) {// 群聊

                } else {
                    String userId = mInviteNotices.get(i).getFrom();
                    if (!userId.contains("xiaoding")
                            && !userId.contains("xiaodang")) {
                        mUserIdBuilder.append(userId.split("@")[0]).append("-");
                    }
                }
            }
        }

        // 只有群聊或者只有小叮小当
        if (TextUtils.isEmpty(mUserIdBuilder.toString())) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // 刷新适配器
                    mNoticeAdapter.notifyDataSetChanged();
                }
            });

        } else if (!TextUtils.isEmpty(mUserIdBuilder.toString())) {
            AjaxParams params = new AjaxParams();
            params.put("ids",
                    mUserIdBuilder.substring(0, mUserIdBuilder.length() - 1));
            requestNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    switch (msg.what) {
                        case com.chewuwuyou.app.utils.Constant.NET_DATA_SUCCESS:
                            mUserInfos = ChatUserInfo.parseList(msg.obj.toString());// 获取用户的昵称、备注等信息
                            for (int i = 0; i < mUserInfos.size(); i++) {
                                for (int j = 0; j < mUserChatHistories.size(); j++) {
                                    if (mUserInfos
                                            .get(i)
                                            .getId()
                                            .equals(mUserChatHistories.get(j)
                                                    .getFrom().split("@")[0])) {
                                        mUserChatHistories.get(j).setUrl(
                                                mUserInfos.get(i).getUrl());
                                        mUserChatHistories.get(j).setNoteName(
                                                mUserInfos.get(i).getNoteName());
                                        mUserChatHistories.get(j).setNickName(
                                                mUserInfos.get(i).getNick());
                                        mUserChatHistories.get(j).setUserName(
                                                mUserInfos.get(i).getUserName());
                                    }
                                }

                            }
                            mNoticeAdapter.notifyDataSetChanged();
                            break;
                        case Constant.NET_DATA_FAIL:
                            ToastUtil.toastShow(getActivity(),
                                    ((DataError) msg.obj).getErrorMessage());
                            break;
                        default:
                            break;
                    }
                }
            }, params, NetworkUtil.GET_CHAT_USER_INFO, false, 1);
        }

    }

    @Override
    protected void subscripUserReceive(String subFrom) {
        Notice notice = new Notice();
        notice.setFrom(subFrom);
        notice.setNoticeType(Notice.CHAT_MSG);
    }

    /**
     * 有新的聊天消息进来
     */
    @Override
    protected void msgReceive(Notice notice) {
        // MyLog.i("YUY", "notice = " + notice.getContent());
        if (CacheTools.getUserData("role") != null) {
            getUserChatHistory();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWakeLock.release();
    }

    @SuppressWarnings({"deprecation", "static-access"})
    @Override
    public void onResume() {
        // 设置不操作屏幕会自动熄屏
        PowerManager pm = (PowerManager) getActivity().getSystemService(
                getActivity().POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, "DPA");
        mWakeLock.acquire();
        // TODO 取得加好友的消息 显示出来
        // NoticeManager noticeManager =
        // NoticeManager.getInstance(getActivity());
        // if (noticeManager != null) {
        // mNotices = noticeManager
        // .getUnReadNoticeListByType(Notice.ADD_FRIEND);
        // }
        // if (mNotices != null && mNotices.size() > 0) {
        // mAddFriendMsgView.setText(String.valueOf(mNotices.size()));
        // mAddFriendMsgView.show();
        // } else {
        // mAddFriendMsgView.hide();
        // }
        // TODO 如果之前已登陆，则刷新看看 太耗资源
        if (CacheTools.getUserData("role") != null) {
            getUserChatHistory();
        }
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_right_ibtn:
                if (CacheTools.getUserData("role") != null) {
                    intent = new Intent(getActivity(), SearchFriendActivity.class);
                } else {
                    intent = new Intent(this.getActivity(), LoginActivity.class);
                }
                startActivity(intent);
                break;

            // case R.id.search_clear:
            // mQueryET.getText().clear();
            // break;

            case R.id.get_knew_iv:
                mMsgView.setVisibility(View.GONE);
                CacheTools.setOtherCacheData("main_msg_get_knew", "1");
                break;
            case R.id.today_shixun_tv:

                Intent ondateIntent = new Intent(getActivity(),
                        OnDateMessageActivity.class);
                startActivity(ondateIntent);
                break;
            case R.id.server_help_tv:
                Intent helpIntent = new Intent(getActivity(),
                        ClientHelpActivity.class);
                startActivity(helpIntent);
                break;
            case R.id.my_collect_tv:
                Intent myCollectIntent = new Intent(getActivity(),
                        MyAttentionActivity.class);
                startActivity(myCollectIntent);
                break;
            default:
                break;
        }

    }

    /**
     * 删除聊天消息
     *
     * @param position
     */
    private void deleteChatMsg(int position) {

        // 删除聊天记录
        if (!TextUtils.isEmpty(mUserChatHistories.get(position).getRoom())) {// 删除群消息
            MessageManager.getInstance(getActivity()).delChatRoom(
                    mUserChatHistories.get(position).getRoom());
        } else {
            MessageManager.getInstance(getActivity()).delChatHisWithSb(
                    mUserChatHistories.get(position).getFrom());
        }
        mUserChatHistories.remove(position);
        /* mNoticeAdapter.notifyDataSetChanged(); */

        mNoticeAdapter = new RecentChartAdapter(this.getActivity(),
                mUserChatHistories, mHandler);
        mContactMessageList.setAdapter(mNoticeAdapter);

        // final Dialog dialog = new Dialog(getActivity(),
        // R.style.myDialogTheme);
        // LayoutInflater inflater = LayoutInflater.from(getActivity());
        // final LinearLayout layout = (LinearLayout) inflater.inflate(
        // R.layout.chat_msg_manager_dialog, null);
        //
        // layout.setAlpha(100);
        // dialog.setContentView(layout);
        // WindowManager windowManager = getActivity().getWindowManager();
        // Display display = windowManager.getDefaultDisplay();
        // WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // lp.width = (int) (display.getWidth() - 50); // 设置宽度
        // dialog.getWindow().setAttributes(lp);
        // dialog.show();
        // dialog.setCanceledOnTouchOutside(true);
        // ((TextView) layout.findViewById(R.id.delete_chat_msg_tv))
        // .setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        // dialog.dismiss();
        // // 删除聊天记录
        // if (!TextUtils.isEmpty(mUserChatHistories.get(position)
        // .getRoom())) {// 删除群消息
        // MessageManager.getInstance(getActivity())
        // .delChatRoom(
        // mUserChatHistories.get(position)
        // .getRoom());
        // } else {
        // MessageManager.getInstance(getActivity())
        // .delChatHisWithSb(
        // mUserChatHistories.get(position)
        // .getFrom());
        // }
        //
        // mUserChatHistories.remove(position);
        // mNoticeAdapter.notifyDataSetChanged();
        //
        // }
        // });
    }

}
