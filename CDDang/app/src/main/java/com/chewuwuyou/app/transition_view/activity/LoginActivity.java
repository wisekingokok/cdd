package com.chewuwuyou.app.transition_view.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.BuildConfig;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_presenter.LoginPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;
import com.chewuwuyou.app.ui.ForgetPasswordActivity;
import com.chewuwuyou.app.ui.MainActivityEr;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.EditTextWithDelete;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yuyong on 16/10/13.
 * 登录
 */

public class LoginActivity extends BaseActivity {


    @BindView(R.id.iv_login_back)
    ImageView ivLoginBack;
    @BindView(R.id.et_telephone)
    EditTextWithDelete etTelephone;
    @BindView(R.id.et_password)
    EditTextWithDelete etPassword;
    @BindView(R.id.tv_forgetpassword)
    TextView tvForgetpassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transition_activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this);
        tvForgetpassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        tvForgetpassword.setTextColor(getResources().getColor(R.color.new_blue));
    }


    @OnClick({R.id.iv_login_back, R.id.tv_forgetpassword, R.id.btn_login})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_login_back:
                finish();
                break;
            case R.id.tv_forgetpassword:
                intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_login:
                loginPresenter.login(BuildConfig.VERSION_NAME, etTelephone.getText().toString(), etPassword.getText().toString(), Tools.getPhoneIM(LoginActivity.this));
                break;
        }
    }

    /**
     * 跳转首页
     */
    public void intoFirstPage() {
        startActivity(new Intent(LoginActivity.this, MainActivityEr.class));
        finish();
    }


}
