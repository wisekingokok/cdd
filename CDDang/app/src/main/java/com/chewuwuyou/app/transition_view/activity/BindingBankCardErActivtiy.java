package com.chewuwuyou.app.transition_view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindingBankCardErActivtiy extends BaseTitleActivity {

    @BindView(R.id.bank_img)//银行卡图片
            ImageView bankImg;
    @BindView(R.id.bank_name)//银行卡名称
            TextView bankName;
    @BindView(R.id.bank_remarks)//银行卡卡号
            TextView bankRemarks;
    @BindView(R.id.name)//本人姓名
            EditText name;
    @BindView(R.id.oneself_sfz)//本人身份证
            EditText oneselfSfz;
    @BindView(R.id.bank_next_mobile)//本人手机号
            EditText bankNextMobile;
    @BindView(R.id.add_bank_next)//下一步
            Button addBankNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_bank_card);
        initView();
    }

    private void initView(){
        ButterKnife.bind(this);
        setBarTitle(R.string.add_bank_title);
        setLeftBarBtnImage(R.drawable.backbutton);
    }
    @OnClick(R.id.add_bank_next)
    public void onClick() {
        Intent intent = new Intent(BindingBankCardErActivtiy.this,FillVerificationCodeActivtiy.class);
        startActivity(intent);
    }

    /**
     * 返回上一页
     * @param view
     * @param title_tag TITLE_TAG_LEFT_BTN 左边按钮,
     *                  TITLE_TAG_LEFT_TV 左边文字按钮,
     *                  TITLE_TAG_RIGHT_BTN ;//右边按钮,
     */
    @Override
    protected void onTitleBarClick(View view, int title_tag) {
        switch (title_tag){
            case TITLE_TAG_LEFT_BTN:
                finish();
                break;
        }
    }
}
