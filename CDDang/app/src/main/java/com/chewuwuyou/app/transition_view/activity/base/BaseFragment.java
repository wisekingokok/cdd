package com.chewuwuyou.app.transition_view.activity.base;


import android.view.Gravity;
import android.widget.Toast;

import com.chewuwuyou.app.transition_utils.LogUtils;
import com.chewuwuyou.app.transition_utils.StatusBarCompat;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by xxy on 2016/7/21.
 */
public class BaseFragment extends RxFragment {
    private Toast toast;

    @Override
    public void onPause() {
        super.onPause();
        if (toast != null)
            toast.cancel();
    }

    /**
     * 控制activity颜色
     *
     * @param colorId
     */
    public void setBarColor(int colorId) {
        StatusBarCompat.compat(getActivity(), getResources().getColor(colorId));
    }

    /**
     * 显示toast
     *
     * @param msg ,
     *            默认底部显示
     */
    public void showToast(String msg) {
        showToast(msg, Gravity.BOTTOM);
    }

    /**
     * 显示toast
     *
     * @param msg
     * @param gravity 显示的位置(Gravity.BOTTOM)
     */
    public void showToast(String msg, int gravity) {
        if (toast == null) {
            toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
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
