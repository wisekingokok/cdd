package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ExamineBook;
import com.chewuwuyou.app.utils.DialogUtil;

import net.tsz.afinal.annotation.view.ViewInject;

public class BusinessAuthenticationAdopt extends CDDBaseActivity implements View.OnClickListener {


    @ViewInject(id = R.id.sub_header_bar_tv)// 标题
    private TextView mTitleTV;
    @ViewInject(id = R.id.sub_header_bar_left_ibtn)// 标题
    private ImageButton mBackIBtn;// 返回上一页
    @ViewInject(id = R.id.examine_img)// 状态图片
    private ImageView mExamineImg;
    @ViewInject(id = R.id.examine_information)// 失败信息
    private TextView mExamineInformation;
    @ViewInject(id = R.id.examine_reason)// 失败原因
    private TextView mExamineReason;
    @ViewInject(id = R.id.examine_fail)// 失败隐藏
    private LinearLayout mexamineFail;
    @ViewInject(id = R.id.login_btn)// 提交
    private Button mSubmit;
    private ExamineBook response;//接收传递过来的信息
    @ViewInject(id = R.id.examine_customer)// 联系客服
    private TextView mExamineCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_authentication_adopt);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mTitleTV.setText("商家认证");

    }

    @Override
    protected void initData() {

        int index = getIntent().getIntExtra("index",0);

        switch (index){
            case 0:
                finishActivity();
                break;
            case 1:
                mExamineInformation.setText("资料审核成功,请重新登录");
                mExamineInformation.setTextColor(getResources().getColor(R.color.black));
                mExamineImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_adopt));
                break;
            case 2:
                mExamineInformation.setText("资料审核中");
                mExamineInformation.setTextColor(getResources().getColor(R.color.black));
                mExamineImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_in));
                break;
            case 3:
                mExamineInformation.setText("资料审核失败");
                mExamineImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_fail_sh));
                if(!TextUtils.isEmpty(getIntent().getStringExtra("fail"))){
                    String sdsd = getIntent().getStringExtra("fail");
                    mExamineReason.setText(sdsd.substring(0,sdsd.indexOf("^")));
                }
                if (getIntent().getSerializableExtra("response") != null) {
                    response = (ExamineBook) getIntent().getSerializableExtra("response");
                }
                mexamineFail.setVisibility(View.VISIBLE);
                mSubmit.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mExamineCustomer.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;
            case R.id.login_btn://重新提交
                intent = new Intent(this,IDVerificationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("response", response);
                intent.putExtras(bundle);
                startActivity(intent);
                finishActivity();
                break;
            case R.id.examine_customer://联系客服
                DialogUtil.call(mExamineCustomer.getText().toString(),BusinessAuthenticationAdopt.this);
                break;
            default:
                break;
        }
    }
}
