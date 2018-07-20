package com.chewuwuyou.app.transition_view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_entity.TransferAccountDetail;
import com.chewuwuyou.app.transition_presenter.TransferAccountsDetailPresenter;
import com.chewuwuyou.app.transition_utils.NLog;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;
import com.chewuwuyou.app.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * create by zengqiang 2016.10.10
 */

public class TransferAccountsDetailActivity extends BaseTitleActivity {


//    TransferAccountsdetails

    @BindView(R.id.iv_sign_success)
    ImageView mImageviewSignSuccess;
    @BindView(R.id.tv_trans_money)
    TextView mTextTransMoney;
    @BindView(R.id.tv_trans_account)
    TextView mTextTransAccount;
    @BindView(R.id.tv_mywallet)
    TextView mTextMywallet;
    @BindView(R.id.tv_trans_time)
    TextView mTextTransTime;
    @BindView(R.id.network_again)
    TextView networkAgain;
    @BindView(R.id.network_abnormal_layout)
    LinearLayout networkAbnormalLayout;
    @BindView(R.id.network_request)
    LinearLayout mNetworkRequest;

    //    收款方
    public static final int PAYEE = 0X001;
    //    发送方
    public static final int PAYER = 0X002;

    public static final String TYPE_KEY = "type_key";
    public static final String ID_KEY = "id_key";
    private int mType = PAYEE;
    private TransferAccountsDetailPresenter mPresenter;
    private String mTransferID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_accounts_detail);
        ButterKnife.bind(this);
        initView();
        initData();

    }


    protected void initView() {
//        actionBar
        setLeftBarBtnImage(R.drawable.backbutton);
        setBarTitle(getString(R.string.transfer_deatil_title));

    }

    private void initData() {
        mPresenter = new TransferAccountsDetailPresenter(this);
//        mType 初始化，获取转账id,收款人姓名，角色
        Intent intent = getIntent();
        if (intent!=null){
            mTransferID=intent.getStringExtra(ID_KEY);
            mType=intent.getIntExtra(TYPE_KEY,0);
            Log.e("tag",mTransferID+"mTransferID"+mType+"mType");
        }

        mPresenter.netRequestAgain(mTransferID);


    }

    public void updateView(TransferAccountDetail entity) {
        NLog.i(entity);
        if (entity == null) {
           showErroPager();
            return;
        }
        if (entity.getExists().trim().equals("1")){
            showErroPager();
            return;
        }

//      转账的时间
        mTextTransTime.setText(getString(R.string.transfer_time)+entity.getTime());
        //        转账的描述,金额
        judgeRole(entity.getActual() + "", entity.getFee() + "", entity.getMoney() + "", entity.getCollectorPerson());

    }

    private void judgeRole(String actual, String free, String money, String payeeName) {
        if (mType == PAYEE) {
            //        转账的金额
            mTextTransMoney.setText("+" + money);
            mTextTransMoney.setTextColor(getResources().getColor(R.color.color_ff4e00));
            mTextMywallet.setVisibility(View.VISIBLE);
            mTextTransAccount.setText("(实际转账:  +" + actual +"  "+ "手续费:  —" + free + ")");

        } else if (mType == PAYER) {
            mTextTransMoney.setText("-" + money);
            mTextTransMoney.setTextColor(getResources().getColor(R.color.color_191919));
            mTextMywallet.setVisibility(View.GONE);
            mTextTransAccount.setText("已转入" + payeeName + "的钱包");
        }else{
            showErroPager();
            return;
        }
    }

    @Override
    protected void onTitleBarClick(View view, int title_tag) {
        switch (title_tag) {
            case TITLE_TAG_LEFT_BTN:
                finish();
                break;
        }
    }

    @OnClick({R.id.tv_mywallet, R.id.network_again})
    public void onClick(View view) {
        switch (view.getId()) {
//            goto mywallet
            case R.id.tv_mywallet:
                mPresenter.goMyWallet();
                break;
            case R.id.network_again:
                mPresenter.netRequestAgain(mTransferID);
                break;
        }
    }


    public void showErroPager() {
        if (mNetworkRequest.getVisibility() == View.VISIBLE) {
            mNetworkRequest.setVisibility(View.GONE);
        }
        networkAbnormalLayout.setVisibility(View.VISIBLE);
    }

    public void dismissErroPager() {
        networkAbnormalLayout.setVisibility(View.GONE);
    }

    public void showNetRequest() {
        if (networkAbnormalLayout.getVisibility() == View.VISIBLE) {
            networkAbnormalLayout.setVisibility(View.GONE);
        }
        mNetworkRequest.setVisibility(View.VISIBLE);
    }

    public void dismissNetRequest() {
        mNetworkRequest.setVisibility(View.GONE);
    }

}
