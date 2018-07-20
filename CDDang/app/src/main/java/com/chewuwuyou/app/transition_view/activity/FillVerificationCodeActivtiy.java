package com.chewuwuyou.app.transition_view.activity;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;
import com.chewuwuyou.app.ui.BaseActivity;
import com.chewuwuyou.app.ui.RegisterActivity;
import com.chewuwuyou.app.utils.RegexValidateUtil;
import com.chewuwuyou.app.utils.SmsContent;
import com.chewuwuyou.app.utils.ToastUtil;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FillVerificationCodeActivtiy extends BaseTitleActivity {

    @BindView(R.id.barTitle)//标题
            TextView barTitle;
    @BindView(R.id.leftBarBtn)//返回上一页
            ImageButton leftBarBtn;
    @BindView(R.id.mobile_phone)//手机号码
            TextView mobilePhone;
    @BindView(R.id.edit_mobile_phone)//输入验证码
            EditText editMobilePhone;
    @BindView(R.id.obtain_code)//获取验证码
            TextView obtainCode;
    @BindView(R.id.add_bank_next)//下一步
            Button addBankNext;
    @BindView(R.id.leftBarTV)
    TextView leftBarTV;
    @BindView(R.id.rightBarTV)
    TextView rightBarTV;
    @BindView(R.id.rightBarBtn)
    ImageButton rightBarBtn;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.activity_fill_verification_code_activtiy)
    LinearLayout activityFillVerificationCodeActivtiy;

    private TimeCount time;

    private SmsContent mSmsContent;// 监听验证码的接受并自动填充

    private String telephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_verification_code_activtiy);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    public void initView() {
        ButterKnife.bind(this);
        setBarTitle(R.string.add_bank_title);
        setLeftBarBtnImage(R.drawable.backbutton);
    }

    /**
     * 返回上一页
     *
     * @param view
     * @param title_tag TITLE_TAG_LEFT_BTN 左边按钮,
     *                  TITLE_TAG_LEFT_TV 左边文字按钮,
     *                  TITLE_TAG_RIGHT_BTN ;//右边按钮,
     */
    @Override
    protected void onTitleBarClick(View view, int title_tag) {
        switch (title_tag) {
            case TITLE_TAG_LEFT_BTN:
                finish();
                break;
        }
    }

    @OnClick({R.id.add_bank_next,R.id.obtain_code})
    public void onClick(View view) {//提交

        switch (view.getId()){
            case R.id.add_bank_next://下一步
                break;
            case R.id.obtain_code://获取验证码
                obtainCode.setClickable(false);
                telephone = editMobilePhone.getText().toString().trim();
                time = new TimeCount(120000, 1000);// 构造CountDownTimer对象
                time.start();
                break;
        }

    }

    /**
     * 动态获取短信
     */
    private void getSms() {
        mSmsContent = new SmsContent(FillVerificationCodeActivtiy.this, new Handler(), editMobilePhone);
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mSmsContent);
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            obtainCode.setClickable(true);
            obtainCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_obtain));
            obtainCode.setText(getResources().getString(R.string.bank_obtain_verification));

        }
        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            obtainCode.setText(millisUntilFinished / 1000 + getResources().getString(R.string.bank_obtain_miao));
            obtainCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_huise_obtain));
            obtainCode.setClickable(false);
        }
    }
}
