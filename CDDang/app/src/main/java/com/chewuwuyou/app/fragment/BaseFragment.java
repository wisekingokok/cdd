package com.chewuwuyou.app.fragment;

import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.ui.LunchPageActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.WaitingDialog;

@SuppressLint("InlinedApi")
public abstract class BaseFragment extends Fragment {
    WaitingDialog waitingDialog;
    private WaitingDialog mWaitingDialog;
    protected static final DisplayMetrics mOutMetrics = new DisplayMetrics();
    protected int mTuWidth = 0;
    protected int mTuHeight = 0;

    protected int mBgTuWidth = 0;
    protected int mBgTuHeight = 0;
    public boolean isTitleDisplay = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            isTitleDisplay = true;
        } else {
            isTitleDisplay = false;
        }
        mWaitingDialog = new WaitingDialog(getActivity());

        mImm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mOutMetrics);
        mTuWidth = (mOutMetrics.widthPixels
                - 2
                * getResources().getDimensionPixelSize(R.dimen.quan_padding)
                - getResources().getDimensionPixelSize(
                R.dimen.quan_avatar_width) - 2 * getResources()
                .getDimensionPixelSize(R.dimen.quan_tu_interval)) / 3;
        mTuHeight = mTuWidth;

        mBgTuWidth = mOutMetrics.widthPixels;
        mBgTuHeight = (mOutMetrics.widthPixels * 2) / 3;

    }


    /**
     * 判断手机版本是否是4.4以上 不是显示false里面的布局
     */
    public void isTitle(RelativeLayout mTitleHeight) {
        android.view.ViewGroup.LayoutParams layout = mTitleHeight.getLayoutParams();
        mTitleHeight.getLayoutParams();
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int density = metric.densityDpi;
        // 240当前手机的像素
        if (isTitleDisplay) {
            layout.height = 130 * density / 320;
        } else {
            layout.height = 64 * density / 240;
        }
        mTitleHeight.setLayoutParams(layout);
    }


    // 规范代码，所有继承它的activity都得实现这三个方法，让代码整洁
    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    /**
     * 显示滚动的对话框
     *
     * @param title   标题
     * @param message 消息内容
     * @author huangzhihao
     */
    public void showProgressDialog(Context context, final String title,
                                   final String message) {
        postRunnable(new Runnable() {
            @Override
            public void run() {
                showProgress(title, message);
            }
        });
    }

    public void showProgress(String title, String message) {
        if (null == waitingDialog) {
            waitingDialog = new WaitingDialog(getActivity());
        }
        if (waitingDialog.isShowing()) {
            waitingDialog.dismiss();
        }
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (title != null) {
            waitingDialog.setTitle(title);
        }
        if (message != null) {
            waitingDialog.setMessage(message);
        }
        waitingDialog.setIndeterminate(false);
        // waitingDialog.setCancelable(true);
        waitingDialog.show();
    }

    public void showProgress(String message) {
        showProgress(null, message);
    }

    /**
     * 隐藏滚动的对话框
     */
    public void dismissProgressDialog() {
        postRunnable(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog1();
            }
        });
    }

    /**
     * 隐藏滚动的对话框
     */
    public void dismissProgressDialog1() {
        if (waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.dismiss();
        }
        waitingDialog = null;
    }

    private void postRunnable(Runnable r) {
        if (getActivity() != null && getView() != null) {
            getView().post(r);
        }
    }


    protected void showWaitingDialog() {
        if (mWaitingDialog != null && !mWaitingDialog.isShowing()) {
            mWaitingDialog.show();
        }
    }

    protected void dismissWaitingDialog() {
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
    }

    @SuppressWarnings("static-access")
    public void requestNet(final Handler handler, AjaxParams params,
                           final String url, final boolean isCache, final int isCricle) {
        new NetworkUtil().postMulti(url, params, new AjaxCallBack<String>() {

            @Override
            public void onStart() {
                super.onStart();
                if (isCricle == 0) {
                    showWaitingDialog();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                MyLog.i("YUY", "======fail===" + strMsg);
                if (isCricle == 0) {
                    dismissWaitingDialog();
                }
                Message msg = new Message();
                msg.what = Constant.NET_REQUEST_FAIL;
                handler.sendMessage(msg);
                if (IsNetworkUtil.isNetworkAvailable(getActivity()) == false) {
                    ToastUtil.toastShow(getActivity(), "网络不可用，请检查您的网络是否连接");
                } else {
                    ToastUtil.toastShow(getActivity(), "网络异常，请稍后再试");
                }
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                MyLog.i("YUY", "url =" + url + "=====t = " + t);
                if (isCricle == 0) {
                    dismissWaitingDialog();
                }
                super.onSuccess(t);
                try {
                    JSONObject response = new JSONObject(t);
                    MyLog.i("YUY", "xxxxxxxxxxxxx返回数据" + response.toString());
                    switch (response.getInt("result")) {
                        case 0:// 数据有问题的情况
                            // json格式:{"result":0,"data":{"errorCode":0,"errorMessage":"用户未登录"}}
                            int errorCode = response.getJSONObject("data").getInt("errorCode");
                            if (errorCode == ErrorCodeUtil.IndividualCenter.LOGIN_PAST) {
                                // 没有登录跳到登录页面
                                ToastUtil.toastShow(getActivity(), "您长时间未操作,请重新登录");
                                Tools.clearInfo(getActivity());
                                Intent intent = new Intent(getActivity(), LunchPageActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else if (errorCode == ErrorCodeUtil.IndividualCenter.LOGINED_IN_OTHER_PHONE) {
                                DialogUtil.loginInOtherPhoneDialog(getActivity());
                            } else if (errorCode == ErrorCodeUtil.IndividualCenter.ROLE_CHANGE || errorCode == ErrorCodeUtil.Business.NOT_BUSINESS) {//角色变更
                                DialogUtil.roleChange(getActivity());
                            } else {
                                // 其他异常处理
                                Message msg = new Message();
                                msg.obj = DataError.parse(response
                                        .getString("data"));
                                msg.what = Constant.NET_DATA_FAIL;
                                handler.sendMessage(msg);
                            }
                            break;
                        case 1:// 成功返回:{"result":1,"data":obj}
                            Message msg = new Message();
                            if (response.getString("data") != null
                                    && !"".equals(response.getString("data"))) {
                                if (isCache) {
                                    CacheTools.saveCacheStr(url,
                                            response.getString("data"));
                                }
                                msg.obj = response.getString("data");
                                msg.what = Constant.NET_DATA_SUCCESS;
                                handler.sendMessage(msg);
                            } else {
                                msg.what = Constant.NET_DATA_NULL;
                                handler.sendMessage(msg);
                            }

                            break;
                        default:
                            // handler.sendEmptyMessage(Constant.NET_DATA_EXCEPTION);
                            // ToastUtil.toastShow(mContext, "数据异常");
                            break;
                    }
                } catch (Exception e) {
                    // handler.sendEmptyMessage(Constant.NET_DATA_EXCEPTION);
                    // ToastUtil.toastShow(mContext, "数据异常");
                    Message msg = new Message();
                    msg.what = Constant.NET_JSON_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });
    }

    private InputMethodManager mImm;

    protected void showKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 100);
    }

    protected void hideKeyboard() {
        try {
            if (mImm.isActive()) {
                mImm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                .getApplicationWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
        }
    }
}
