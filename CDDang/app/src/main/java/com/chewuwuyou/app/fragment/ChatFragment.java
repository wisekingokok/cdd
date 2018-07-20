package com.chewuwuyou.app.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.ui.AddfriendActivity;
import com.chewuwuyou.app.ui.CaptureActivity;
import com.chewuwuyou.app.ui.SearchFriendActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.widget.BadgeView;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.model.Notice;

import java.util.List;

/**
 * 消息界面 1、当日时讯 2、客服帮助 3、我的收藏 4、最新消息Created by yuyong on 16-3-28.
 */
public class ChatFragment extends BaseFragment implements OnClickListener, FragmentCallBackBuilder, FragmentCallBack {

    private View mContentView;
    private RadioButton mRadMessage, mMaillist;

    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView mMoreTV;// 更多：添加好友、扫一扫
    private View mPopView;
    private PopupWindow mPopWindow;// 添加好友及扫一扫的popwindow
    private TextView mAddFriendTV, mScanTV;// 添加好友、扫一扫
    private BadgeView mAddFriendMsgView;// 添加好友消息的条数
    private List<Notice> mNotices;
    private ContacterReceiver mReceiver = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.chat_fragment, null);
        initView();
        initData();
        initEvent();
        return mContentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver();
    }

    /**
     * 初始化控件
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void initView() {
        mRadMessage = (RadioButton) mContentView.findViewById(R.id.message);
        mMaillist = (RadioButton) mContentView.findViewById(R.id.maillist);
        mMoreTV = (TextView) mContentView.findViewById(R.id.more_tv);
        mFragments = new Fragment[2];
        fragmentManager = getChildFragmentManager();
//		mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_message); // 消息
//		mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_mailllist); // 通讯录
        if (mFragments[0] == null) {
            mFragments[0] = new MessageFragment();
            fragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[0]).commit();
        }
        if (mFragments[1] == null) {
            mFragments[1] = new ContacterFragment();
            fragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[1]).commit();
        }
        ((FragmentCallBackBuilder) mFragments[1]).setFragmentCallBack(this);
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]);
        fragmentTransaction.show(mFragments[0]).commit();// 设置第几页显示

        mPopView = getActivity().getLayoutInflater().inflate(R.layout.main_more_popwindow, null);
        this.mPopWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.mPopWindow.setFocusable(true);
        this.mPopWindow.setOutsideTouchable(true);
        this.mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        this.mAddFriendTV = (TextView) mPopView.findViewById(R.id.msg_add_friend_tv);
        this.mScanTV = (TextView) mPopView.findViewById(R.id.msg_scan_tv);
        // 显示添加好友的信息数量
        mAddFriendMsgView = new BadgeView(getActivity(), mMoreTV);
        mAddFriendMsgView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {

    }

    /**
     * 事件点击
     */
    @Override
    protected void initEvent() {
        mRadMessage.setOnClickListener(this);
        mMaillist.setOnClickListener(this);
        mMoreTV.setOnClickListener(this);
        mAddFriendTV.setOnClickListener(this);
        mScanTV.setOnClickListener(this);
        mPopView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mPopWindow.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message:// 消息
                onclickSwitch(0);
                break;
            case R.id.maillist:// 通讯录
                onclickSwitch(1);
                break;
            case R.id.more_tv:
                mPopWindow.showAsDropDown(mMoreTV, 5, 5);
                break;
            case R.id.msg_add_friend_tv:
                Intent Searintent = new Intent(getActivity(), AddfriendActivity.class);
                startActivity(Searintent);
                mPopWindow.dismiss();
                break;
            case R.id.msg_scan_tv:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivity(intent);
                mPopWindow.dismiss();
                break;
            default:
                break;
        }

    }

    /**
     * 首页切换
     *
     * @param id
     */
    @SuppressLint("NewApi")
    private void onclickSwitch(int id) {
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]);
        switch (id) {
            case 0:
                fragmentTransaction.show(mFragments[0]).commit();// 消息
                mRadMessage.setBackgroundColor(getResources().getColor(R.color.white));
                mMaillist.setBackground(getResources().getDrawable(R.drawable.business_right_isservice));
                break;
            case 1:
                fragmentTransaction.show(mFragments[1]).commit();// 通讯录
                mMaillist.setBackgroundColor(getResources().getColor(R.color.white));
                mRadMessage.setBackground(getResources().getDrawable(R.drawable.business_left_isservice));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CacheTools.getUserData("role") == null) {
            MyLog.i("YUY", "未登录");
            return;
        }
        NoticeManager noticeManager = NoticeManager.getInstance(getActivity());
        if (noticeManager != null) {
            mNotices = noticeManager.getUnReadNoticeListByType(Notice.ADD_FRIEND);
        }
        if (mNotices != null && mNotices.size() > 0) {
            mAddFriendMsgView.setText(String.valueOf(mNotices.size()));
            mAddFriendMsgView.show();
        } else {
            mAddFriendMsgView.hide();
        }
    }

    private FragmentCallBack callback;

    @Override
    public void setFragmentCallBack(FragmentCallBack callback) {
        this.callback = callback;
    }

    @Override
    public void callback(int pager, Object obj) {
        if (callback != null)
            callback.callback(1, obj);
    }

    private class ContacterReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Constant.ADD_FRIEND_QEQUEST.equals(action)) {
                NoticeManager noticeManager = NoticeManager.getInstance(getActivity());
                if (noticeManager != null) {
                    mNotices = noticeManager.getUnReadNoticeListByType(Notice.ADD_FRIEND);
                }
                if (mNotices != null && mNotices.size() > 0) {
                    mAddFriendMsgView.setText(String.valueOf(mNotices.size()));
                    mAddFriendMsgView.show();
                } else {
                    mAddFriendMsgView.hide();
                }
            }
        }
    }

    /**
     * 注册及时通讯所需要的广播
     */
    private void registerReceiver() {
        mReceiver = new ContacterReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ROSTER_SUBSCRIPTION);  // 好友请求
        filter.addAction(Constant.NEW_MESSAGE_ACTION);
        filter.addAction(Constant.ADD_FRIEND_QEQUEST);
        // 启动service完成的Action
        filter.addAction(Constant.CONTACT_STARTED);
        filter.addAction(Constant.CHAT_STARTED);
        filter.addAction(Constant.SYSTEM_MSG_STARTED);
        filter.addAction(Constant.RE_CONNECT_STARTED);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
}
