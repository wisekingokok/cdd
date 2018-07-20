package com.chewuwuyou.app.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.Toast;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.ui.LunchPageActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ChatUtils;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.WaitingDialog;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.ContacterManager;
import com.chewuwuyou.eim.manager.ContacterManager.MRosterGroup;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.IMMessage;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.model.User;
import com.chewuwuyou.eim.util.DateUtil;
import com.chewuwuyou.eim.util.StringUtil;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public abstract class ContacterBaseFragment extends Fragment {

    private ContacterReceiver receiver = null;
    private WaitingDialog mWaitingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiver = new ContacterReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ROSTER_ADDED);
        filter.addAction(Constant.ROSTER_DELETED);
        filter.addAction(Constant.ROSTER_PRESENCE_CHANGED);
        filter.addAction(Constant.ROSTER_UPDATED);
        filter.addAction(Constant.ROSTER_SUBSCRIPTION);
        // 好友请求
        filter.addAction(Constant.NEW_MESSAGE_ACTION);
        filter.addAction(Constant.ACTION_SYS_MSG);
        filter.addAction(Constant.ACTION_RECONNECT_STATE);
        // 启动service完成的Action
        filter.addAction(Constant.CONTACT_STARTED);
        filter.addAction(Constant.CHAT_STARTED);
        filter.addAction(Constant.SYSTEM_MSG_STARTED);
        filter.addAction(Constant.RE_CONNECT_STARTED);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class ContacterReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // 此处将User和Notice放入if内，是防止冲突

            if (Constant.ROSTER_ADDED.equals(action)) {
                User user = intent.getParcelableExtra(User.userKey);
                addUserReceive(user);
            }
            if (Constant.ROSTER_DELETED.equals(action)) {
                User user = intent.getParcelableExtra(User.userKey);
                deleteUserReceive(user);
            }
            if (Constant.ROSTER_PRESENCE_CHANGED.equals(action)) {
                User user = intent.getParcelableExtra(User.userKey);
                changePresenceReceive(user);
            }
            if (Constant.ROSTER_UPDATED.equals(action)) {
                User user = intent.getParcelableExtra(User.userKey);
                updateUserReceive(user);
            }
            if (Constant.ROSTER_SUBSCRIPTION.equals(action)) {
                subscripUserReceive(intent
                        .getStringExtra(Constant.ROSTER_SUB_FROM));
            }
            if (Constant.NEW_MESSAGE_ACTION.equals(action)) {
                MyLog.e("YUY", "ConterBaseFragment新消息----------------");
                Notice notice = (Notice) intent.getSerializableExtra("notice");
                // intent.putExtra("noticeId", noticeId);
                msgReceive(notice);
            }
            if (Constant.ACTION_RECONNECT_STATE.equals(action)) {
                boolean isSuccess = intent.getBooleanExtra(
                        Constant.RECONNECT_STATE, true);
                {
                    doContactStarted();
                }
                handReConnect(isSuccess);
            }
            if (Constant.CONTACT_STARTED.equals(action))
                if (Constant.CHAT_STARTED.equals(action)) {
                    doChatStarted();
                }
            if (Constant.SYSTEM_MSG_STARTED.equals(action)) {
                doSystemMsgStarted();
            }
            if (Constant.RE_CONNECT_STARTED.equals(action)) {
                doReConnectStarted();
            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    /**
     * Contact Service 启动时
     */
    protected abstract void doContactStarted();

    /**
     * Chat Service 启动时
     */
    protected abstract void doChatStarted();

    /**
     * System Msg Service 启动时
     */
    protected abstract void doSystemMsgStarted();

    /**
     * Reconnect Service 启动时
     */
    protected abstract void doReConnectStarted();

    /**
     * roster添加了一个subcriber
     *
     * @param user
     */
    protected abstract void addUserReceive(User user);

    /**
     * roster删除了一个subscriber
     *
     * @param user
     */
    protected abstract void deleteUserReceive(User user);

    /**
     * roster中的一个subscriber的状态信息信息发生了改变
     *
     * @param user
     */
    protected abstract void changePresenceReceive(User user);

    /**
     * roster中的一个subscriber信息更新了
     *
     * @param user
     */
    protected abstract void updateUserReceive(User user);

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
     * 修改这个好友的昵称
     *
     * @param jid
     * @param nickname
     */
    protected void setNickname(String jid, String nickname) {
        /*
         * ContacterManager.addUserToGroup(user,user.getGroupName(),
		 * ConnectionUtils.getConnection());
		 */
        ContacterManager.setNickname(jid, nickname, XmppConnectionManager
                .getInstance().getConnection());
    }

    /**
     * 把一个好友添加到一个组中 先移除当前分组，然后添加到新分组
     *
     * @param user
     * @param groupName
     */
    protected void addUserToGroup(final User user, final String groupName) {

        if (null == user) {
            return;
        }
        if (StringUtil.notEmpty(groupName) && Constant.ALL_FRIEND != groupName
                && Constant.NO_GROUP_FRIEND != groupName) {
            ContacterManager.addUserToGroup(user, groupName,
                    XmppConnectionManager.getInstance().getConnection());
        }
    }

    /**
     * 把一个好友从组中删除
     *
     * @param user
     * @param groupName
     */
    protected void removeUserFromGroup(User user, String groupName) {

        if (null == user) {
            return;
        }
        if (StringUtil.notEmpty(groupName)
                && !Constant.ALL_FRIEND.equals(groupName)
                && !Constant.NO_GROUP_FRIEND.equals(groupName))
            ContacterManager.removeUserFromGroup(user, groupName,
                    XmppConnectionManager.getInstance().getConnection());

    }

    /**
     * 添加一个联系人
     *
     * @param userJid  联系人JID
     * @param nickname 联系人昵称
     * @param groups   联系人添加到哪些组
     * @throws XMPPException
     */
    protected void createSubscriber(String userJid, String nickname,
                                    String[] groups) throws XMPPException {
        try {
            XmppConnectionManager.getInstance().getConnection().getRoster()
                    .createEntry(userJid, nickname, groups);
        } catch (NotLoggedInException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一个联系人
     *
     * @param userJid 联系人的JID
     * @throws XMPPException
     */
    protected void removeSubscriber(String userJid) throws XMPPException {
        ContacterManager.deleteUser(userJid);

    }

    /**
     * 修改一个组的组名
     *
     * @param oldGroupName
     * @param newGroupName
     */
    protected void updateGroupName(String oldGroupName, String newGroupName) {
        try {
            if (XmppConnectionManager.getInstance().getConnection() != null) {
                XmppConnectionManager.getInstance().getConnection().getRoster().getGroup(oldGroupName).setName(newGroupName);
            }
            RosterGroup rGroup = XmppConnectionManager.getInstance().getConnection().getRoster()
                    .getGroup(oldGroupName);
            if (rGroup != null) {
                XmppConnectionManager.getInstance().getConnection().getRoster()
                        .getGroup(oldGroupName).setName(newGroupName);
            } else {
                ToastUtil.toastShow(getActivity(), "组用户为空，不能修改");
            }
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这添加分组.
     *
     * @param newGroupName
     * @author sxk
     * @update 2012-6-28 下午3:52:41
     */
    protected void addGroup(String newGroupName) {
        ContacterManager.addGroup(newGroupName, XmppConnectionManager
                .getInstance().getConnection());

    }

    /**
     * 创建一个聊天
     *
     * @param user
     */
    public void createChat(User user) {
//		Intent intent = new Intent(getActivity(), ChatActivity.class);
//		intent.putExtra("to", user.getJID());
//		MyLog.i("YUY", "---------创建聊天时的用户id-----" + user.getJID());
//		startActivity(intent);
        AppContext.createChat(getActivity(), user.getJID());
    }

    /**
     * 冲连接返回
     *
     * @param isSuccess
     */
    protected abstract void handReConnect(boolean isSuccess);

    /**
     * 判断用户名是否存在
     *
     * @param userJid
     * @param groups
     * @return
     */
    protected boolean isExitJid(String userJid, List<MRosterGroup> groups) {
        for (MRosterGroup g : groups) {
            List<User> users = g.getUsers();
            if (users != null && users.size() > 0) {
                for (User u : users) {
                    if (u.getJID().equals(userJid)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

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
                    Toast toast = Toast.makeText(getActivity(), "网络不可用，请检查您的网络是否连接",
                            Toast.LENGTH_LONG);
                    // 可以控制toast显示的位置
                    toast.setGravity(Gravity.BOTTOM, 0, 10);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getActivity(), "网络异常，请稍后再试",
                            Toast.LENGTH_LONG);
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
                            // ToastUtil.toastShow(mContext, "数据异常");
                            break;
                    }
                } catch (Exception e) {
                    // handler.sendEmptyMessage(Constant.NET_DATA_EXCEPTION);
                    // ToastUtil.toastShow(mContext, "数据异常");
                    e.printStackTrace();
                }
            }
        });
    }

    protected void showWaitingDialog() {
        if (getActivity() != null && getActivity().isFinishing()) {
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

}
