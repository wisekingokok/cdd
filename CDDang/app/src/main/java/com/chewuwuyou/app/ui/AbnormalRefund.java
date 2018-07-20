package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.DialogUtil;

import net.tsz.afinal.annotation.view.ViewInject;

public class AbnormalRefund extends CDDBaseActivity implements View.OnClickListener {
    /**
     * 订单异常退款
     * @param savedInstanceState
     */
    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//订单标题
    private TextView mTitel;
    @ViewInject(id = R.id.order_refund_money)//退款金额
    private TextView mOrderRefundMoney;
    @ViewInject(id = R.id.abnormal_money)//金额异常
    private Button mAbnormalMoney;
    @ViewInject(id = R.id.abnormal_service)//联系客服
    private Button mAbnormalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abnormal_refund);
        initView();
        initData();
        initEvent();
    }



    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.abnormal_money://跳转取消订单界面
                if(!TextUtils.isEmpty(getIntent().getStringExtra("tasId"))){
                    intent = new Intent(AbnormalRefund.this,OrderActivity.class);
                    intent.putExtra("taskId",getIntent().getStringExtra("tasId"));
                    startActivity(intent);
                    finishActivity();
                    OrderActivity.orderActivity.finishActivity();
                }
                break;
            case R.id.abnormal_service:
                DialogUtil.call(mAbnormalService.getText().toString().trim(), this);
                break;
        }
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mTitel.setText("订单详情");
        mSubHeaderBarLeftIbtn.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(getIntent().getStringExtra("money"))){
            mOrderRefundMoney.setText(getIntent().getStringExtra("money")+"元");
        }
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {

    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mAbnormalMoney.setOnClickListener(this);
        mAbnormalService.setOnClickListener(this);
    }

}
