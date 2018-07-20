package com.chewuwuyou.app.transition_view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindingBankCardActivtiy extends BaseTitleActivity {

    @BindView(R.id.edit_bank_number)//输入银行卡号
    EditText editBankNumber;
    @BindView(R.id.add_bank_next)//下一步
    Button addBankNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binding_bank_card_activtiy);
        initView();
    }


    private void initView(){
        ButterKnife.bind(this);
        setBarTitle(R.string.add_bank_title);
        setLeftBarBtnImage(R.drawable.backbutton);
    }

    @OnClick({R.id.add_bank_next})
    public void onClick(View view) {
        Intent intent = new Intent(BindingBankCardActivtiy.this,BindingBankCardErActivtiy.class);
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
