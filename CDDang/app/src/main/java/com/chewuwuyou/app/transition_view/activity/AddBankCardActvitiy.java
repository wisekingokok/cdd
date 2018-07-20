package com.chewuwuyou.app.transition_view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_constant.Constants;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBankCardActvitiy extends BaseTitleActivity {

    @BindView(R.id.edit_bank_username)
    TextView editBankUsername;
    @BindView(R.id.edit_bank_number)
    EditText editBankNumber;
    @BindView(R.id.add_bank_next)
    Button addBankNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_card_actvitiy);
        initView();
    }

    /**
     * 初始化
     */
    public void initView(){
        ButterKnife.bind(this);
        setBarTitle(R.string.add_bank_title);
        setLeftBarBtnImage(R.drawable.backbutton);
    }

    /**
     * 点击事件
     * @param view
     */
    @OnClick(R.id.add_bank_next)
    public void onClick(View view) {//下一步
        Intent intent = new Intent(AddBankCardActvitiy.this,AddBankMobileActvitiy.class);
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
