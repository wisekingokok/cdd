package com.chewuwuyou.eim.activity.im;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.fragment.ContacterFragment;
import com.chewuwuyou.app.fragment.MainSearchFragment;
import com.chewuwuyou.app.ui.BaseFragmentActivity;
import com.chewuwuyou.app.utils.MyLog;

/**
 * Created by yuyong on 16/8/24.
 * 消息转发
 */
public class MessageTransActivity extends BaseFragmentActivity implements View.OnClickListener, FragmentCallBack {

    private FragmentManager fragmentManager;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private ImageButton mBackBtn;
    private TextView mTitleTV;
    private Fragment[] mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_trans_layout);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mFragments = new Fragment[2];
        mFragments[0] = fragmentManager.findFragmentById(R.id.list); // 通讯录
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_search); // 搜索
        ((FragmentCallBackBuilder) mFragments[0]).setFragmentCallBack(this);
        ((ContacterFragment)mFragments[0]).setMessage(getIntent().getStringExtra("msg"));
        hideAll(fragmentTransaction);
        fragmentTransaction.show(mFragments[0]).commit();
        mBackBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mBackBtn.setOnClickListener(this);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mTitleTV.setText("选择");
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);//

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finish();
                break;
        }
    }

    @Override
    public void callback(int pager, Object obj) {
        MyLog.e("YUY", "pager = " + pager + "--" + obj);
        switch (pager) {
            case 1:// 搜索页面过来
                mTitleHeight.setVisibility(View.GONE);
                ((MainSearchFragment)mFragments[1]).setMessage(obj,getIntent().getStringExtra("msg"));
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                hideAll(transaction);
                transaction.show(mFragments[1]).commit();
                break;
            default:
                break;
        }
    }

    private void hideAll(FragmentTransaction transaction) {
        if (mFragments[0] != null) {
            transaction.hide(mFragments[0]);
        }
        if (mFragments[1] != null) {
            transaction.hide(mFragments[1]);
        }
    }

}
