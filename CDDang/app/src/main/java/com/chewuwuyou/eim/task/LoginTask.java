package com.chewuwuyou.eim.task;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.eim.activity.ActivitySupport;
import com.chewuwuyou.eim.activity.IActivitySupport;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.IMMessage;
import com.chewuwuyou.eim.model.LoginConfig;

/**
 * 登录异步任务.
 *
 * @author sxk
 */
public class LoginTask extends AsyncTask<String, Integer, Integer> {
    private ProgressDialog pd;
    private Context context;
    private AppContext application;
    private IActivitySupport activitySupport;
    private LoginConfig loginConfig;
    private String LOGIN_SUCCESS = "xmpp_login_success";

    public LoginTask(IActivitySupport activitySupport, LoginConfig loginConfig) {
        this.activitySupport = activitySupport;
        this.loginConfig = loginConfig;
        this.pd = activitySupport.getProgressDialog();
        this.context = activitySupport.getContext();
    }

    public LoginTask(Context context, LoginConfig loginConfig, boolean flag) {
        this.loginConfig = loginConfig;
        this.context = context;
    }

    public LoginTask(AppContext application, LoginConfig loginConfig) {
        this.loginConfig = loginConfig;
        this.application = application;
        this.context = application;
    }

    @Override
    protected void onPreExecute() {
        if (pd != null) {
            pd.setTitle("请稍等");
            pd.setMessage("正在登录...");
            pd.show();
        }
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) {
        XMPPConnection connection = XmppConnectionManager.getInstance()
                .getConnection();

        if (loginConfig.getUsername() == null
                || loginConfig.getUsername().isEmpty()
                || loginConfig.getPassword() == null
                || loginConfig.getPassword().isEmpty()) {
            // 信息不详，不登录
            return Constant.LOGIN_INFO_UNFULFILL;
        }
        // 有并且还在链接
        if (connection != null && connection.isConnected()
                && connection.isAuthenticated()) {
            // 不连接了
            return Constant.ALREADY_LOGINED;
        }
        return login();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected void onPostExecute(Integer result) {
        Log.e("SXKTEST", "userName:" + loginConfig.getUsername());
        if (pd != null)
            pd.dismiss();
        switch (result) {
            case Constant.LOGIN_SECCESS: // 登录成功
                // Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent();
                // intent.setClass(context, MainActivity.class);
                Log.e("SXKTEST",
                        loginConfig.getUsername()
                                + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!LOGINTASK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                // 告诉可以接受service启动完毕，表达意图
                // 开始自制的心跳包
                startHeartBeat();

                if (activitySupport != null) {
                    // CacheTools.setUserData("chatIsLogin", "true");
                    activitySupport.saveLoginConfig(loginConfig);// 保存用户配置信息
                    activitySupport.startService(); // 初始化各项服务
                } else if (application != null) {
                    // application.startService();
                } else if (context != null) {
                    // CacheTools.setUserData("chatIsLogin", "true");
                    ActivitySupport.saveLoginConfigStatic(context, loginConfig);
                    ActivitySupport.startServiceStatic(context);
                }
                // PingManager pm =
                // activitySupport.statDoLogin(); //做一些登陆后的处理 --- 放到Service启动完毕去执行
                // context.startActivity(intent);
                break;
            case Constant.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.message_invalid_username_password),
                        Toast.LENGTH_SHORT).show();
                break;
            case Constant.SERVER_UNAVAILABLE:// 服务器连接失败
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.message_server_unavailable),
                        Toast.LENGTH_SHORT).show();
                break;
            case Constant.LOGIN_ERROR:// 未知异常
                MyLog.i("YUY", "未知异常，即使通讯为初始化成功。");
                // Intent intent = new Intent(context, LoginActivity.class);
                // context.startActivity(intent);
            case Constant.ALREADY_LOGINED:// 已经登录
                MyLog.e("SXKTEST", "已经连接并登录，什么都不用做");
                break;
        }

        // 登录动作完成
        super.onPostExecute(result);
    }

    private void startHeartBeat() {
        PingManager pm = PingManager.getInstanceFor(XmppConnectionManager
                .getInstance().getConnection());
        pm.registerPingFailedListener(new PingFailedListener() {

            @Override
            public void pingFailed() {
                // 需要重连
                // CacheTools.setUserData("chatIsLogin", "false");
                MyLog.i("SXKTEST", "需要重连");
                Intent intent = new Intent(Constant.ACTION_PING_OUT);
                context.sendBroadcast(intent);
            }
        });
        // 59秒检测
        pm.setPingInterval(59);
    }

    // 登录
    private Integer login() {
        String username = loginConfig.getUsername();
        String password = loginConfig.getPassword();
        try {
            XMPPConnection connection = XmppConnectionManager.getInstance()
                    .getConnection();

            // 已连接并已授权，还登录什么
            if (connection != null && connection.isConnected()
                    && connection.isAuthenticated()) {
                return Constant.ALREADY_LOGINED;
            }

            MyLog.e("SXKTEST", "准备开始连接");
            if (connection != null) {
                connection.disconnect();
                MyLog.e("SXKTEST", "断开连接");
            }

            if (!connection.isConnected()) {
                MyLog.e("SXKTEST", "开始连接");

                connection.connect();
            } else {

                MyLog.e("SXKTEST", "已经连接了，不连");
            }

            if (!connection.isAuthenticated()) {

                connection.login(username, password); // 登录
                MyLog.e("SXKTEST", "开始授权登陆");
            } else {

                MyLog.e("SXKTEST", "已经登录，先不管");
            }

            // if (activitySupport != null)
            // OfflineMsgManager.getInstance(activitySupport).dealOfflineMsg(connection);//处理离线消息
            // else if (context != null)
            // OfflineMsgManager.getInstance(context).dealOfflineMsg(connection);//处理离线消息
            // connection.sendPacket(new Presence(Presence.Type.available));
            // //快速的available可能导致某些Service甚至Lisenter还未启动就来信息了
            // loginConfig.setOnline(true);

            // if (loginConfig.isNovisible()) {// 隐身登录
            // Presence presence = new Presence(Presence.Type.unavailable);
            // Collection<RosterEntry> rosters = connection.getRoster()
            // .getEntries();
            // for (RosterEntry rosterEntry : rosters) {
            // presence.setTo(rosterEntry.getUser());
            // connection.sendPacket(presence);
            // }
            // }
            loginConfig.setUsername(username);
            // if (loginConfig.isRemember()) {// 保存密码
            // loginConfig.setPassword(password);
            // } else {
            // loginConfig.setPassword("");
            // }
            return Constant.LOGIN_SECCESS;
        } catch (Exception xee) {
            if (xee instanceof XMPPErrorException) {
                XMPPErrorException xe = (XMPPErrorException) xee;
                final XMPPError error = xe.getXMPPError();
                String errorCondition;
                if (error != null) {
                    errorCondition = error.getCondition();
                } else {
                    return Constant.LOGIN_ERROR;
                }
                if (errorCondition.equals(Condition.forbidden)) {
                    return Constant.LOGIN_ERROR_ACCOUNT_PASS;
                } else if (errorCondition.equals(Condition.not_authorized)) {
                    return Constant.LOGIN_ERROR_ACCOUNT_PASS;
                } else {
                    return Constant.SERVER_UNAVAILABLE;
                }
            } else {
                return Constant.LOGIN_ERROR;
            }
            // if (xee instanceof XMPPErrorException) {
            // XMPPErrorException xe = (XMPPErrorException) xee;
            // final XMPPError error = xe.getXMPPError();
            // String errorCondition;
            // if (error != null) {
            // errorCondition = error.getCondition();
            // } else {
            // return Constant.LOGIN_ERROR;
            // }
            // if (errorCondition.equals(Condition.forbidden)) {
            // return Constant.LOGIN_ERROR_ACCOUNT_PASS;
            // }else if (errorCondition.equals(Condition.not_authorized)) {
            // return Constant.LOGIN_ERROR_ACCOUNT_PASS;
            // } else {
            // return Constant.SERVER_UNAVAILABLE;
            // }
            // // } else {
            // return Constant.LOGIN_ERROR;
            // }
            // return Constant.LOGIN_SECCESS;
        }
    }
}
