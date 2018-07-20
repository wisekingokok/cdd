package com.chewuwuyou.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AppVersion;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.KeyboardUtil;
import com.chewuwuyou.app.utils.KeyboardUtil.InputFinishListener;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.VersionUtil;
import com.chewuwuyou.app.widget.MyToast;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 可跳转至登录和注册
 *
 * @author yuyong
 */
public class LunchPageActivity extends CDDBaseActivity implements
        OnClickListener {

    private Button mLoginBtn;
    private Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lunch_page_ac);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mRegisterBtn = (Button) findViewById(R.id.regist_btn);
    }

    @Override
    protected void initData() {
        VersionUtil.judgeVersionMsg(LunchPageActivity.this, false);// 检测版本信息
    }

    @Override
    protected void initEvent() {
        mLoginBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_btn:
                // 仿支付宝支付
                // createWalletPayDetailsDialog(LunchPageActivity.this);
                Intent intent = new Intent(LunchPageActivity.this,
                        LoginActivity.class);
                startActivity(intent);

                break;
            case R.id.regist_btn:
                Intent registIntent = new Intent(LunchPageActivity.this,
                        RegisterActivity.class);
                startActivity(registIntent);

                break;

            default:
                break;
        }
    }

    private long lastTryExitTime = 0;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - lastTryExitTime) > 2000) {
                StatService.onEvent(LunchPageActivity.this, "ClickMainActKey", "双击主界面返回硬键");
                MyToast.showToast(LunchPageActivity.this, R.string.exit_app_notify, Toast.LENGTH_SHORT);
                lastTryExitTime = System.currentTimeMillis();
            } else {
                MyLog.e("SXKTEST", "Main terminate killAll");
                AppManager.getAppManager().finishAllActivity();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.runFinalizersOnExit(true);
                System.exit(0);
                Intent intenn = new Intent();
                intenn.setAction("android.intent.action.MAIN");
                intenn.addCategory("android.intent.category.HOME");
                startActivity(intenn);
                // moveTaskToBack(true);
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public void createWalletPayDetailsDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.wallet_pay_details_layout, null);
        layout.getBackground().setAlpha(100);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle); // 添加动画

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        ImageView closePayIV = (ImageView) layout
                .findViewById(R.id.wallet_pay_close_iv);
        Button payBtn = (Button) layout.findViewById(R.id.ok_pay_btn);
        closePayIV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        payBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                createWalletPayDialog(LunchPageActivity.this);
            }
        });

    }

    @SuppressWarnings("deprecation")
    public void createWalletPayDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.wallet_pay_layout, null);
        layout.getBackground().setAlpha(100);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.myrightstyle); // 添加动画

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);

        ImageView closePayIV = (ImageView) layout
                .findViewById(R.id.wallet_pay_close_iv);
        closePayIV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                createWalletPayDetailsDialog(context);
            }
        });
        LinearLayout layout_input = (LinearLayout) layout
                .findViewById(R.id.layout_input);

        KeyboardView keyboardView = (KeyboardView) layout
                .findViewById(R.id.keyboard_view);
        final KeyboardUtil keyBoard = new KeyboardUtil(this, this,
                keyboardView, layout_input, new InputFinishListener() {

            @Override
            public void inputHasOver(String text) {
                dialog.dismiss();
                Toast.makeText(context, "输入完成:" + text,
                        Toast.LENGTH_LONG).show();
            }
        });
        keyBoard.showKeyboard();
        dialog.show();
        layout_input.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoard.showKeyboard();
                return false;
            }
        });
    }


}
