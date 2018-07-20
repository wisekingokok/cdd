package com.chewuwuyou.app.transition_view.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.transition_utils.LogUtils;
import com.chewuwuyou.app.transition_utils.StatusBarCompat;
import com.trello.rxlifecycle.components.support.RxFragmentActivity;

/**
 * Created by xxy on 2016/10/10 0010.
 */

public class BaseActivity extends RxFragmentActivity {
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (toast != null)
            toast.cancel();
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    /**
     * 状态栏颜色
     */
    protected void setBarColor(int colorId) {
        StatusBarCompat.compat(this, getResources().getColor(colorId));
    }

    protected Activity getActivity() {
        return this;
    }

    /**
     * 显示toast
     *
     * @param msg ,
     *            默认底部显示
     */
    protected void showToast(String msg) {
        showToast(msg, Gravity.BOTTOM);
    }

    /**
     * 显示toast
     *
     * @param msg
     * @param gravity 显示的位置(Gravity.BOTTOM)
     */
    protected void showToast(String msg, int gravity) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, 0, 100);
        } else
            toast.setText(msg);
        toast.show();
    }

    /**
     * Log日志
     *
     * @param msg
     */
    protected void e(String msg) {
        LogUtils.e(this.getClass(), msg);
    }

    /**
     * Log日志
     *
     * @param msg
     */
    protected void w(String msg) {
        LogUtils.w(this.getClass(), msg);
    }

    /**
     * Log日志
     *
     * @param msg
     */
    protected void i(String msg) {
        LogUtils.i(this.getClass(), msg);
    }
}
