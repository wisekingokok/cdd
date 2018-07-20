package com.chewuwuyou.app.transition_view.fragment;

import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_view.activity.base.BaseFragment;
import com.chewuwuyou.app.transition_view.fragment.RongMessageFragment;
import com.chewuwuyou.app.ui.AddfriendActivity;
import com.chewuwuyou.app.ui.CaptureActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.widget.BadgeView;
import com.chewuwuyou.rong.bean.ContactNotificationMessage;
import com.chewuwuyou.rong.view.RongContacterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 消息界面 Created by xxy
 */
public class RongChatFragment extends BaseFragment {
    @BindView(R.id.message)
    RadioButton mRadMessage;
    @BindView(R.id.maillist)
    RadioButton mMaillist;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.tishi)
    View mView_tishi;
    @BindView(R.id.more_tv)
    TextView mMoreTV;

    private View mContentView;
    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private PopupWindow mPopWindow;// 添加好友及扫一扫的popwindow
    private TextView mAddFriendTV, mScanTV;// 添加好友、扫一扫
    private BadgeView mAddFriendMsgView;// 添加好友消息的条数


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.rong_chat_fragment, null);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, mContentView);
        mFragments = new Fragment[2];
        fragmentManager = getChildFragmentManager();
        if (mFragments[0] == null) {
            mFragments[0] = new RongMessageFragment();
            fragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[0]).commit();
        }
        if (mFragments[1] == null) {
            mFragments[1] = new RongContacterFragment();
            fragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[1]).commit();
        }
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]);
        fragmentTransaction.show(mFragments[0]).commit();// 设置第几页显示
        // 显示添加好友的信息数量
        mAddFriendMsgView = new BadgeView(getActivity(), mMoreTV);
        mAddFriendMsgView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        initPop();
        return mContentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {//解决崩溃重合问题
    }

    private void initPop() {
        View mPopView = getActivity().getLayoutInflater().inflate(R.layout.main_more_popwindow, null);
        this.mAddFriendTV = (TextView) mPopView.findViewById(R.id.msg_add_friend_tv);
        this.mScanTV = (TextView) mPopView.findViewById(R.id.msg_scan_tv);
        this.mPopWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.mPopWindow.setFocusable(true);
        this.mPopWindow.setOutsideTouchable(true);
        this.mPopWindow.setBackgroundDrawable(new BitmapDrawable());

        mAddFriendTV.setOnClickListener(popClick);
        mScanTV.setOnClickListener(popClick);
        mPopView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mPopWindow.dismiss();
            }
        });
    }

    private OnClickListener popClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    //当收到消息 显示小圆点
    public void onEventMainThread(ContactNotificationMessage sendMsgBean) {
        if (mRadMessage.isChecked())
            mView_tishi.setVisibility(View.VISIBLE);
        else
            mView_tishi.setVisibility(View.GONE);

    }

    //当收到取消消息 隐藏小圆点
    public void onEventMainThread(String sendMsgBean) {

        mView_tishi.setVisibility(View.GONE);//当点击新朋友的时候，收到消息。取消提示

    }

    @OnClick({R.id.message, R.id.maillist, R.id.more_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message:// 消息
                onclickSwitch(0);
                break;
            case R.id.maillist:// 通讯录
                mView_tishi.setVisibility(View.GONE);
                onclickSwitch(1);
                break;
            case R.id.more_tv:
                mPopWindow.showAsDropDown(mMoreTV, 5, 5);
                break;
        }
    }

    /**
     * 首页切换
     *
     * @param id
     */
    private void onclickSwitch(int id) {
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]);
        switch (id) {
            case 0:
                fragmentTransaction.show(mFragments[0]).commit();// 消息
                mRadMessage.setBackgroundColor(getResources().getColor(R.color.white));
                mMaillist.setBackgroundResource(R.drawable.business_right_isservice);
                break;
            case 1:
                fragmentTransaction.show(mFragments[1]).commit();// 通讯录
                mMaillist.setBackgroundColor(getResources().getColor(R.color.white));
                mRadMessage.setBackgroundResource(R.drawable.business_left_isservice);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CacheTools.getUserData("role") == null) {
            i("未登录");
            return;
        }
    }
}
