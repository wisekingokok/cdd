package com.chewuwuyou.app.transition_view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithDrawResultActivity extends BaseTitleActivity {

    @BindView(R.id.tv_withdrawmoney)
    TextView mTvWithdrawmoney;
    @BindView(R.id.tv_banklastnum)
    TextView mTvBanklastnum;
    @BindView(R.id.tv_receivetime)
    TextView mTvReceivetime;
    @BindView(R.id.tv_ok)
    TextView mTvOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw_result);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setBarTitle("结果详情");
    }

    @OnClick(R.id.tv_ok)
    public void onClick() {
    }
}
