package com.chewuwuyou.app.transition_view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_constant.Constants;
import com.chewuwuyou.app.transition_presenter.GroupRedPacketSendPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;
import com.chewuwuyou.app.utils.RemoveEmojiWatcher;
import com.chewuwuyou.app.widget.WaitingDialog;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class GroupRedPacketSendActivity extends BaseActivity {
    @BindView(R.id.leftBarBtn)
    ImageButton mLeftBarBtn;
    @BindView(R.id.barTitle)
    TextView mBarTitle;
    @BindView(R.id.rightBarTV)
    TextView mRightBarTV;
    @BindView(R.id.toolbar)
    RelativeLayout mToolbar;
    @BindView(R.id.edt_redpacket_num)
    EditText mEdtRedpacketNum;
    @BindView(R.id.text_kind_account)
    TextView mTextKindAccount;
    @BindView(R.id.edt_redpacket_money)
    EditText mEdtRedpacketMoney;
    @BindView(R.id.text_red_kind_detal)
    TextView mTextRedKindDetal;
    @BindView(R.id.text_red_kind)
    TextView mTextRedKind;
    @BindView(R.id.text_account)
    TextView mTextAccount;
    @BindView(R.id.tv_action_pay)
    TextView mActionPay;
    @BindView(R.id.text_paykind_detail)
    TextView mTextPaykindDetail;
    @BindView(R.id.text_paykind)
    TextView mTextPaykind;
    @BindView(R.id.text_redpacket_remark)
    EditText mEditRedpacketRemark;
    @BindView(R.id.tv_numlimit)
    TextView mTvNumlimit;
    @BindView(R.id.tv_moneylimit)
    TextView mTvMoneylimit;


    private String PAYTYPE;
    // 红包的类型
    public final int REDTYPE_RANDOM = 2;
    public final int REDTYPE_NORMAL = 1;
    private int mRedType = REDTYPE_RANDOM;
    //    支付的方式
    private String mPayType;
    //    单个红包金额
    private double mRedMoney;
    //    红包数量
    private int mRedCount;
    //    红吧的总金额
    private double mAllRedMoeney;

    private GroupRedPacketSendPresenter mPresenter;

    public WaitingDialog mDialog;
    private String mTargetId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_red_packet_send);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }



    private void initView() {
        EventBus.getDefault().register(this);
        PAYTYPE = getResources().getString(R.string.paykind);
        mPresenter = new GroupRedPacketSendPresenter(this);
//        初始化actionbar
        mBarTitle.setVisibility(View.VISIBLE);
        mLeftBarBtn.setVisibility(View.VISIBLE);
        mLeftBarBtn.setImageResource(R.drawable.backbutton);
        mRightBarTV.setVisibility(View.VISIBLE);
        mBarTitle.setText(R.string.group_redpacket_title);
        mRightBarTV.setText(R.string.group_redpacket_righttitle);
        mToolbar.setBackgroundResource(R.color.color_fb4d56);
        setBarColor(R.color.color_fb4d56);

        mDialog = new WaitingDialog(this);

/*//        初始化支付方式
        String userData = CacheTools.getUserData(PAYTYPE);
        if (TextUtils.isEmpty(userData)){
            return;
        }
        setPayType(userData);*/
    }
    private void initData() {
        mTargetId = getIntent().getStringExtra(RongChatMsgFragment.TARGETID_KEY);
    }

    private void initEvent() {
        mEdtRedpacketNum.requestFocus();

        mEdtRedpacketNum.addTextChangedListener(new RedpacketTw(mEdtRedpacketNum));
        mEdtRedpacketMoney.addTextChangedListener(new RedpacketTw(mEdtRedpacketMoney));
        mEditRedpacketRemark.addTextChangedListener(new RemoveEmojiWatcher(mEditRedpacketRemark));

    }


    @OnClick({R.id.leftBarBtn, R.id.text_red_kind, R.id.tv_action_pay, R.id.text_paykind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leftBarBtn:
                finish();
                break;
            case R.id.text_red_kind:
//                默认是随机红包，点击更改就转换成普通红包 随机红包之间切换
                mPresenter.switchRedType();
                break;
            case R.id.tv_action_pay:
                Editable redpacketNum = mEdtRedpacketNum.getText();
                Editable redpacketMoney = mEdtRedpacketMoney.getText();
                Editable redpacketRemarkText = mEditRedpacketRemark.getText();
                mPresenter.actionPay(redpacketNum, redpacketMoney, redpacketRemarkText, mRedType,mTargetId);
                break;
         /*   case R.id.text_paykind:
//                第一次默认是支付宝，后续默认是上次的支付方式，点击跳转支付方式选择页面
                mPresenter.switchPayKind();
                break;*/
        }
    }
/*//设置支付的方式
    public void setPayType(String payKind){
        if (!TextUtils.isEmpty(payKind)){
            mPayType =payKind;
            mTextPaykindDetail.setText(getString(R.string.red_kind_use)+mPayType+getString(R.string.red_kind_use_pay));
        }


    }*/


    // 点击更换红包类型
    public void switchRedKind() {
        switch (mRedType) {
            case REDTYPE_RANDOM:
//                转换成普通红包
                mTextKindAccount.setText(R.string.red_single_money);
                mTextRedKind.setText(R.string.action_changto_random);
                mTextRedKindDetal.setText(R.string.red_state_nromal);
                mRedType = REDTYPE_NORMAL;
                mEdtRedpacketMoney.setText("");
                break;
            case REDTYPE_NORMAL:
//                转换成随机红包
                mTextKindAccount.setText(R.string.red_all_money);
                mTextRedKind.setText(R.string.action_changeto_normalred);
                mTextRedKindDetal.setText(R.string.red_state_random);
                mRedType = REDTYPE_RANDOM;
                mEdtRedpacketMoney.setText("");
                break;
        }

    }

    public int getmRedType() {
        return mRedType;
    }

    public double getmAllRedMoeney() {
        return mAllRedMoeney;
    }

    public void showNumHint(String hint) {
        mTvNumlimit.setVisibility(View.VISIBLE);
        mTvNumlimit.setText(hint);
    }

    public void showMoneyHint(String hint) {
        mTvMoneylimit.setVisibility(View.VISIBLE);
        mTvMoneylimit.setText(hint);
    }

    /***
     * 通过红包数量，单个红包金额，红包总金额3个参数来做输入判断
     */


    private int MAXCOUNT = 200;
    private double MAXMONEY = 200;
    private double MAXSINGLEMONEY = 200;

    //    TextWatcher的监听，用于判断红包的金额和个数限制
    class RedpacketTw implements TextWatcher {
        EditText mEditText;
        String mTemp;

        public RedpacketTw(EditText mEditText) {
            this.mEditText = mEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mTemp = mEditText.getText().toString().trim();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getTotalMoney(mEdtRedpacketNum.getText(), mEdtRedpacketMoney.getText(), mEditText, mTemp);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    private void getTotalMoney(Editable redCountEditable, Editable redMoneyEditable, EditText mEditText, String mTemp) {
        mRedCount = 0;
        mRedMoney = 0;
        mTvNumlimit.setVisibility(View.GONE);
        mTvMoneylimit.setVisibility(View.GONE);
        if (TextUtils.isEmpty(redCountEditable)) {
            mRedCount = 0;
        } else {
            mRedCount = Integer.parseInt(redCountEditable.toString());
        }
        if (TextUtils.isEmpty(redMoneyEditable)) {
            mRedMoney = 0;
        } else {
            String s = redMoneyEditable.toString();
            if (s.contains(".")) {
                if (s.length() - 1 - s.indexOf(".") > 2) {
                    mEdtRedpacketMoney.setText(mTemp);
                    mEdtRedpacketMoney.setSelection(mEditText.getText().toString().length());
                    getTotalMoney(mEdtRedpacketNum.getText(), mEdtRedpacketMoney.getText(), mEdtRedpacketMoney, mTemp);
                    return;
                }
                if (s.length() == 1) {
                    mRedMoney = 0;
                } else {
                    mRedMoney = Double.parseDouble(redMoneyEditable.toString());
                }
            } else {
                mRedMoney = Double.parseDouble(redMoneyEditable.toString());
            }
        }
        if (mRedType == REDTYPE_RANDOM) {
            if (mRedCount == 0 || mRedMoney == 0) {
                mAllRedMoeney = 0;
            } else {
                mAllRedMoeney = Double.parseDouble(new DecimalFormat(getString(R.string.decimal_format)).format(mRedMoney));
            }
        } else if (mRedType == REDTYPE_NORMAL) {
            mAllRedMoeney = Double.parseDouble(new DecimalFormat(getString(R.string.decimal_format)).format(mRedMoney * mRedCount));
        }
        mTextAccount.setText(getString(R.string.rmb_sign) + new DecimalFormat(getString(R.string.decimal_format)).format(mAllRedMoeney));

    }


    public void onEventMainThread(String sd) {
        if(sd.equals(Constants.SEND_RED_PACKET_SUCCESS)){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
