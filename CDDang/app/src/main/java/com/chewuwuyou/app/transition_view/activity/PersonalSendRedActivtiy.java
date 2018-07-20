package com.chewuwuyou.app.transition_view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.transition_constant.Constants;
import com.chewuwuyou.app.transition_presenter.PersonalSendPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;
import com.chewuwuyou.app.transition_view.activity.fragment.PayDialogFragment;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;
import com.chewuwuyou.app.widget.WaitingDialog;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 个人发布红包
 * liuchun
 */
public class PersonalSendRedActivtiy extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.barTitle)//标题
    public TextView barTitle;
    @BindView(R.id.leftBarBtn)//返回上一页
    public ImageButton leftBarBtn;
    @BindView(R.id.rightBarTV)//说明
    public TextView rightBarTV;
    @BindView(R.id.toolbar)//标题背景颜色
    public RelativeLayout toolbar;
    @BindView(R.id.edt_redpacket_num)//金额填写
     public EditText edtRedpacketNum;
    @BindView(R.id.text_unit)
    TextView textUnit;
    @BindView(R.id.text_redpacket_remark)//提示语
    public EditText textRedpacketRemark;
    @BindView(R.id.text_account)//显示金额
    public TextView textAccount;
    @BindView(R.id.tv_action_pay)//赛钱进红包
    public Button tvActionPay;
    @BindView(R.id.wallet_error)//错误信息显示
     public TextView walletError;
    @BindView(R.id.activity_group_red_packet_send)
    public  LinearLayout activityGroupRedPacketSend;
    private String mTemp;

    private PersonalSendPresenter mPersonalSendPresenter;

    public String mTargetId;


    public WaitingDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_send_red_activtiy);
        initView();
        initEvent();
    }

    /**
     * 初始化
     */
    private void initView() {
        ButterKnife.bind(this);
        waitingDialog = new WaitingDialog(PersonalSendRedActivtiy.this);
        edtRedpacketNum.requestFocus();
        EventBus.getDefault().register(this);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_fb4d56));//设置标题背景颜色为红色
        barTitle.setVisibility(View.VISIBLE);
        barTitle.setText(getResources().getString(R.string.hair_red_title));
        leftBarBtn.setVisibility(View.VISIBLE);
        leftBarBtn.setImageResource(R.drawable.backbutton);
        rightBarTV.setVisibility(View.VISIBLE);
        rightBarTV.setText(getString(R.string.red_explain));
        setBarColor(R.color.color_fb4d56);
        mPersonalSendPresenter = new PersonalSendPresenter(PersonalSendRedActivtiy.this);
        mTargetId = getIntent().getStringExtra(RongChatMsgFragment.TARGETID_KEY);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftBarBtn://返回上一页
                finish();
                break;
            case R.id.tv_action_pay://赛钱进红包
                if (TextUtils.isEmpty(edtRedpacketNum.getText().toString())) {
                    walletError.setVisibility(View.VISIBLE);
                    walletError.setText(getResources().getString(R.string.hair_red_money_empty));
                    return;
                }else if(edtRedpacketNum.getText().toString().equals(".")){
                    walletError.setVisibility(View.VISIBLE);
                    walletError.setText(getResources().getString(R.string.red_incorrect));
                }else {
                    mPersonalSendPresenter.networkRequest();
                }
                break;
            case R.id.rightBarTV://说明
                mPersonalSendPresenter.redExplain();
                break;
        }
    }

    /**
     * 事件监听
     */
    public void initEvent() {
        leftBarBtn.setOnClickListener(this);
        tvActionPay.setOnClickListener(this);
        rightBarTV.setOnClickListener(this);
        edtRedpacketNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTemp = edtRedpacketNum.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(edtRedpacketNum.getText().toString())){
                    walletError.setVisibility(View.GONE);
                }else if(edtRedpacketNum.getText().toString().equals(".")){
                    textAccount.setText("¥"+"0.00");
                }
                mPersonalSendPresenter.inputPersonal();
            }
        });
    }

    /**
     * 输入监听
     */
    public void inputPersonalAc() {
        if(edtRedpacketNum.getText().toString().equals(".")){
              return;
        }
        if (!TextUtils.isEmpty(edtRedpacketNum.getText().toString())) {
            if (edtRedpacketNum.getText().toString().contains(".")) {

                String sdsa = edtRedpacketNum.getText().toString().substring(edtRedpacketNum.getText().toString().indexOf(".") + 1);
                if (sdsa.length() > 2) {
                    showToast(getString(R.string.input_money));
                    edtRedpacketNum.setText(mTemp);
                    edtRedpacketNum.setSelection(mTemp.length() - 1);
                    return;
                }
                DecimalFormat df = new DecimalFormat("######0.00");
                textAccount.setText("¥" + df.format(Double.parseDouble(edtRedpacketNum.getText().toString() + "")));
            } else {
                textAccount.setText("¥" + edtRedpacketNum.getText().toString() + ".00");
            }
        } else {
            textAccount.setText("¥0.00");
            return;
        }
    }

    /**
     * 支付
     */
    public void showPayDialog(){
        PayDialogFragment payDialogFragment = new PayDialogFragment();
        payDialogFragment.show(getSupportFragmentManager(), "paydialog");
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
