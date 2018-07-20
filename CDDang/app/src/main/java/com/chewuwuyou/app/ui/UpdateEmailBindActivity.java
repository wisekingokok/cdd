
package com.chewuwuyou.app.ui;

import java.util.Random;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.SmsUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @describe:修改邮箱绑定
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-11-25下午5:49:26
 */
public class UpdateEmailBindActivity extends BaseActivity {
    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;
    /**
     * 新的邮箱地址
     */
    @ViewInject(id = R.id.new_email_address)
    private TextView mEmailAddress;
    /**
     * 验证码
     */
    @ViewInject(id = R.id.update_email_phone_code)
    private TextView mCode;
    /**
     * 获取验证码
     */
    @ViewInject(id = R.id.email_bind_code_btn, click = "onAction")
    private Button msendCodeBtn;

    /**
     * 修改绑定绑定
     */
    @ViewInject(id = R.id.update_email_btn, click = "onAction")
    private Button mUpdateEmailBtn;

    /**
     * 修改的邮箱地址
     */
    private String newEmail = "";

    /**
     * 验证码
     */
    private String authCode;

    /**
     * 发送验证码 计时
     */
    private TimeCount time;

    /**
     * 发送验证码的手机
     */
    private String telephone;
    private RelativeLayout mTitleHeight;//标题布局高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_email_bind_ac);
    	mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
        mHeaderTV.setText(R.string.update_email_bind_title);

    }

    public void onAction(View v) {

        switch (v.getId()) {
		    case R.id.sub_header_bar_left_ibtn:
		        finishActivity();
		        break;
            case R.id.email_bind_code_btn:
                newEmail = mEmailAddress.getText().toString();
                // 判断邮箱地址是否为空
                if (TextUtils.isEmpty(newEmail)) {
                    ToastUtil.showToast(UpdateEmailBindActivity.this, R.string.email_is_not_null);
                } else if (!newEmail.matches(RegularUtil.verifyEmail)) {
                    ToastUtil.showToast(UpdateEmailBindActivity.this, R.string.email_geshi_error);
                } else {
                	testEmail();
                }
                break;
            case R.id.update_email_btn:
                newEmail = mEmailAddress.getText().toString();
                if (TextUtils.isEmpty(newEmail)) {
                    ToastUtil.showToast(UpdateEmailBindActivity.this, R.string.email_is_not_null);
                } else if (!newEmail.matches(RegularUtil.verifyEmail)) {
                    ToastUtil.showToast(UpdateEmailBindActivity.this, R.string.email_geshi_error);
                } else if (!authCode.equals(mCode.getText().toString())) {
                    ToastUtil.showToast(UpdateEmailBindActivity.this, R.string.authcode_erro);
                } else {// 修改邮箱
                	sendEmail();
                }
                break;
            default:
                break;
        }

    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            msendCodeBtn.setClickable(true);
            authCode = "";
            msendCodeBtn.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            msendCodeBtn.setText(millisUntilFinished / 1000 + "秒");
            msendCodeBtn.setClickable(false);
        }
    }

    /**
     * 获取验证码
     */
    public void sendAuthcode() {
        int intCount = 0;
        intCount = (new Random()).nextInt(9999);

        if (intCount < 1000) {
            intCount += 1000;
        }
        authCode = String.valueOf(intCount);
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        telephone = CacheTools.getUserData("telephone");
        MyLog.i("YUY", "================="+telephone);
        // final String authcodetime = msendCodeBtn.getText().toString();
        // System.out.println("====tele" + telephone);
        AjaxParams params = new AjaxParams();
        params.put("telephone", telephone);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmsUtil.sendSmsCodeUseTemp(telephone,
                        authCode);
                time.start();
            }
        }).start();

    }
    
    /**
     * 发送Email
     */
    private void sendEmail() {

        String email = mEmailAddress.getText().toString();
        AjaxParams params = new AjaxParams();
        params.put("email", email);
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        final Dialog dialog = new Dialog(UpdateEmailBindActivity.this,
                                R.style.myDialogTheme);
                        LayoutInflater inflater = LayoutInflater.from(UpdateEmailBindActivity.this);
                        final RelativeLayout layout = (RelativeLayout) inflater.inflate(
                                R.layout.bind_email_dialog, null);
                        layout.setAlpha(100);
                        dialog.setContentView(layout);
                        dialog.show();
                        // 确定
                        Button btnok = (Button) layout.findViewById(R.id.btnok);
                        btnok.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                // 进入邮箱
                                ToastUtil.showToast(UpdateEmailBindActivity.this,
                                        R.string.update_email_bind_success);
                                dialog.dismiss();
                				setResult(RESULT_OK, null);
                                finishActivity();
                            }
                        });

                        break;

                    default:
                        break;
                }
            }

        }, params, NetworkUtil.UPDATE_EMAIL_URL, false, 0);

    }
    
    private void testEmail() {

        String email = mEmailAddress.getText().toString();
        MyLog.i("xuhan", "email = " + email);
        AjaxParams params = new AjaxParams();
        params.put("email", email);
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                    	sendAuthcode();
                        break;

                    default:
                        ToastUtil.showToast(UpdateEmailBindActivity.this,
                                R.string.email_early_use);
                        break;
                }
            }

        }, params, NetworkUtil.TEST_EMAIL_URL, false, 0);
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(UpdateEmailBindActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(UpdateEmailBindActivity.this);
	}
}
