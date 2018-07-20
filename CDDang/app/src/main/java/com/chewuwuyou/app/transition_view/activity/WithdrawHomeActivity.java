package com.chewuwuyou.app.transition_view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_presenter.WithdrawHomePresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithdrawHomeActivity extends BaseTitleActivity {

    @BindView(R.id.iv_banklogo)
    ImageView mIvBanklogo;
    @BindView(R.id.tv_bankname)
    TextView mTvBankname;
    @BindView(R.id.tv_bank_lastnum)
    TextView mTvBankLastnum;
    @BindView(R.id.re_choosecard)
    RelativeLayout mReChoosecard;
    @BindView(R.id.edt_withdraw)
    EditText meEdtWithdraw;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.tv_allwithdraw)
    TextView mTvAllwithdraw;
    @BindView(R.id.tv_free)
    TextView mTvFree;
    @BindView(R.id.tv_actual)
    TextView mTvActual;
    @BindView(R.id.ll_calculate)
    LinearLayout mLlCalculate;
    @BindView(R.id.tv_withdraw)
    TextView mTvWithdraw;
    @BindView(R.id.tv_agreement)
    TextView mTvAgreement;
    private WithdrawHomePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_home);
        ButterKnife.bind(this);
        mPresenter = new WithdrawHomePresenter(this);
        initView();
    }

    private void initView() {
        setLeftBarBtnImage(R.drawable.backbutton);
        setBarTitle("余额提现");
        setRightBarTV("提现说明");
    }

    @Override
    protected void onTitleBarClick(View view, int title_tag) {
        switch (title_tag) {
            case TITLE_TAG_LEFT_BTN:
                finish();
                break;
            case TITLE_TAG_RIGHT_TV:

                break;
        }
    }

    @OnClick({R.id.re_choosecard, R.id.tv_allwithdraw, R.id.tv_withdraw, R.id.tv_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.re_choosecard:
                break;
            case R.id.tv_allwithdraw:
                mPresenter.withdrawAll();
                break;
            case R.id.tv_withdraw:
                break;
            case R.id.tv_agreement:
                break;
        }
    }
    public void showCalculate(){
        mLlCalculate.setVisibility(View.VISIBLE);
    }
    public void withdrawNom(){
        mTvWithdraw.setClickable(true);
        mTvWithdraw.setBackgroundResource(R.drawable.button_withdraw_normal);
    }
}
