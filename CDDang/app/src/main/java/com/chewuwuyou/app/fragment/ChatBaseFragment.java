package com.chewuwuyou.app.fragment;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.ui.LunchPageActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.WaitingDialog;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.Notice;

public abstract class ChatBaseFragment extends Fragment {
    private ContacterReceiver mReceiver = null;
    private WaitingDialog mWaitingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerReceiver();
    }

    protected void initView() {

    }

    protected void initData() {

    }

    protected void initEvent() {

    }

    private class ContacterReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MyLog.i("YUY", "输出的Action = " + action);
            if (Constant.ROSTER_SUBSCRIPTION.equals(action)) {
                subscripUserReceive(intent
                        .getStringExtra(Constant.ROSTER_SUB_FROM));
            }
            if (Constant.NEW_MESSAGE_ACTION.equals(action)) {
                MyLog.e("YUY", "ChatBaseFragment新消息----------------");
                Notice notice = (Notice) intent.getSerializableExtra("notice");
                // intent.putExtra("noticeId", noticeId);
                msgReceive(notice);
            }
        }
    }

    @Override
    public void onPause() {
        // this.getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getActivity().unregisterReceiver(mReceiver);
    }

    /**
     * 收到一个好友添加请求
     *
     * @param subFrom
     */
    protected abstract void subscripUserReceive(String subFrom);

    /**
     * 有新消息进来
     *
     * @param notice
     */
    protected abstract void msgReceive(Notice notice);

    /**
     * 回复一个presence信息给用户
     *
     * @param type
     * @param to
     */
    protected void sendSubscribe(Presence.Type type, String to) {
        Presence presence = new Presence(type);
        presence.setTo(to);
        try {
            XmppConnectionManager.getInstance().getConnection()
                    .sendPacket(presence);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param handler  消息句柄用于通知UI线程更新UI
     * @param params   请求参数
     * @param url      请求地址
     * @param isCache  是否缓存
     * @param isCricle 是否转圈
     */
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
                msg.what = com.chewuwuyou.app.utils.Constant.NET_REQUEST_FAIL;
                handler.sendMessage(msg);
                if (IsNetworkUtil.isNetworkAvailable(getActivity()) == false) {
                    Toast toast = Toast.makeText(getActivity(),
                            "网络不可用，请检查您的网络是否连接", Toast.LENGTH_LONG);
                    // 可以控制toast显示的位置
                    toast.setGravity(Gravity.BOTTOM, 0, 10);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "网络异常，请稍后再试", Toast.LENGTH_LONG);
                    // 可以控制toast显示的位置
                    toast.setGravity(Gravity.BOTTOM, 0, 10);
                    toast.show();

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
                    switch (response.getInt("result")) {
                        case 0:// 数据有问题的情况
                            // json格式:{"result":0,"data":{"errorCode":0,"errorMessage":"用户未登录"}}
                            int errorCode = response.getJSONObject("data").getInt("errorCode");
                            if (errorCode == 0) {
                                // 没有登录跳到登录页面
                                ToastUtil.toastShow(getActivity(), "您长时间未操作,请重新登录");
                                Tools.clearInfo(getActivity());
                                Intent intent = new Intent(getActivity(),
                                        LunchPageActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else if (errorCode == ErrorCodeUtil.IndividualCenter.LOGINED_IN_OTHER_PHONE) {
                                CacheTools.clearAllUserData();
                                CacheTools.clearAllObject();
                                CacheTools.clearObject(CacheTools
                                        .getUserData("telephone"));
                                NetworkUtil.removeCookie(getActivity());
                                NetworkUtil.clearCookie();
                                JPushInterface.stopPush(getActivity());// 注销
                                CacheTools.setUserData("isNotification",
                                        com.chewuwuyou.app.utils.Constant.JPUSH_STATUS.JPUSH_CLOSE);
                                DialogUtil
                                        .loginInOtherPhoneDialog(getActivity());
                            } else if (errorCode == ErrorCodeUtil.IndividualCenter.LOGINED_IN_OTHER_PHONE || errorCode == ErrorCodeUtil.Business.NOT_BUSINESS) {
                                DialogUtil.loginInOtherPhoneDialog(getActivity());
                            } else {
                                // 其他异常处理
                                Message msg = new Message();
                                msg.obj = DataError.parse(response
                                        .getString("data"));
                                msg.what = com.chewuwuyou.app.utils.Constant.NET_DATA_FAIL;
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
                                msg.what = com.chewuwuyou.app.utils.Constant.NET_DATA_SUCCESS;
                                handler.sendMessage(msg);
                            } else {
                                msg.what = com.chewuwuyou.app.utils.Constant.NET_DATA_NULL;
                                handler.sendMessage(msg);
                            }

                            break;
                        default:
                            // handler.sendEmptyMessage(Constant.NET_DATA_EXCEPTION);
                            // ToastUtil.toastShow(getActivity(), "数据异常");
                            break;
                    }
                } catch (Exception e) {
                    // handler.sendEmptyMessage(Constant.NET_DATA_EXCEPTION);
                    // ToastUtil.toastShow(getActivity(), "数据异常");
                    e.printStackTrace();
                }
            }
        });
    }

    protected void showWaitingDialog() {
        if (getContext().isFinishing()) {
            dismissWaitingDialog();
            return;
        }
        if (mWaitingDialog != null && !mWaitingDialog.isShowing()) {
            mWaitingDialog.show();
        }
    }

    protected void dismissWaitingDialog() {
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
    }

    public FragmentActivity getContext() {
        return this.getActivity();
    }

    /**
     * 用户在其他地方登陆
     */
    public void loginInOtherPhoneDialog(final FragmentActivity ac) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ac);
        builder.setTitle("您的账户在另一台手机登陆");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(ac, LunchPageActivity.class);
                CacheTools.clearAllUserData();
                CacheTools.clearAllObject();
                CacheTools.clearUserData("userId");
                CacheTools.clearUserData("telephone");
                CacheTools.clearUserData("token");
                CacheTools.clearObject(CacheTools.getUserData("telephone"));
                NetworkUtil.clearCookie();
                XMPPConnection co = XmppConnectionManager.getInstance()
                        .getConnectionNoError();
                if (co != null) {
                    try {
                        co.disconnect();
                    } catch (NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
                ac.startActivity(intent);
                ac.finish();
            }
        });
        builder.create().show();
    }

    /**
     * 注册及时通讯所需要的广播
     */
    private void registerReceiver() {
        mReceiver = new ContacterReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ROSTER_SUBSCRIPTION);
        // 好友请求
        filter.addAction(Constant.NEW_MESSAGE_ACTION);
        // 启动service完成的Action
        filter.addAction(Constant.CONTACT_STARTED);
        filter.addAction(Constant.CHAT_STARTED);
        filter.addAction(Constant.SYSTEM_MSG_STARTED);
        filter.addAction(Constant.RE_CONNECT_STARTED);
        this.getActivity().registerReceiver(mReceiver, filter);

    }
}
