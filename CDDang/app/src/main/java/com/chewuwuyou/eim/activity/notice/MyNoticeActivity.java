
package com.chewuwuyou.eim.activity.notice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.eim.activity.ActivitySupport;
import com.chewuwuyou.eim.activity.LoginActivity;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.ContacterManager.MRosterGroup;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.model.User;
import com.chewuwuyou.eim.util.StringUtil;
import com.chewuwuyou.eim.view.NoticeAdapter;

/**
 * 我的消息.
 * 
 * @author sxk
 */
public class MyNoticeActivity extends ActivitySupport {
    private ImageView titleBack;
    private ListView noticeList = null;
    private NoticeAdapter noticeAdapter = null;
    private List<Notice> inviteNotices = new ArrayList<Notice>();
    private ContacterReceiver receiver = null;
    private NoticeManager noticeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_notice);
        init();
    }

    @Override
    protected void onPause() {
        // 卸载广播接收器
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        // 好友请求
        filter.addAction(Constant.ROSTER_SUBSCRIPTION);
        filter.addAction(Constant.ACTION_SYS_MSG);
        registerReceiver(receiver, filter);
        super.onResume();
    }

    private void init() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        titleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        receiver = new ContacterReceiver();
        noticeList = (ListView) findViewById(R.id.my_notice_list);
        noticeManager = NoticeManager.getInstance(context);
        inviteNotices = noticeManager.getNoticeListByTypeAndPage(Notice.All);
        //noticeAdapter = new NoticeAdapter(context, inviteNotices);
        //noticeList.setAdapter(noticeAdapter);
        noticeList.setOnItemClickListener(inviteListClick);
    }

    private class ContacterReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Notice notice = (Notice) intent.getSerializableExtra("notice");
            // String action = intent.getAction();
            inviteNotices.add(notice);
            refresh();
        }
    }

    private OnItemClickListener inviteListClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                long arg3) {
            final Notice notice = (Notice) view.findViewById(R.id.new_content)
                    .getTag();
            // 消息类型判断
            if (Notice.ADD_FRIEND == notice.getNoticeType()
                    && notice.getStatus() == Notice.UNREAD) {// 添加好友
                showAddFriendDialag(notice);
            } else if (Notice.SYS_MSG == notice.getNoticeType()) {// 系统通知
                Intent intent = new Intent(context,
                        SystemNoticeDetailActivity.class);
                intent.putExtra("notice_id", notice.getId());
                startActivityForResult(intent, 1);
            }

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { // resultCode为回传的标记
            case 1:
                refresh();
            default:
                break;
        }
    }

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
     * 删除一个条目
     * 
     */
	// private void removeInviteNotice(String subFrom) {
	// for (Notice notice : inviteNotices) {
	// if (subFrom.equals(notice.getId())) {
	// inviteNotices.remove(notice);
	// break;
	// }
	// }
	// refresh();
	// }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_notice_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clearall:
                NoticeManager.getInstance(context).delAll();
                refresh();
                break;
            case R.id.menu_relogin:
                Intent intent = new Intent();
                intent.setClass(context, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_exit:
                isExit();
                break;
        }
        return true;

    }

    /**
     * .弹出添加好友的对话框
     * 
     * @param notice
     * @author sxk
     * @update 2012-7-3 下午4:50:53
     */
    private void showAddFriendDialag(final Notice notice) {
        final String subFrom = notice.getFrom();
        new AlertDialog.Builder(context)
                .setMessage(subFrom + "请求添加您为好友")
                .setTitle("提示")
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 接受请求
                        sendSubscribe(Presence.Type.subscribed, subFrom);
                        sendSubscribe(Presence.Type.subscribe, subFrom);
                        // removeInviteNotice(notice.getId());
                        NoticeManager noticeManager = NoticeManager
                                .getInstance(context);
                        noticeManager.updateAddFriendStatus(
                                notice.getId(),
                                Notice.READ,
                                "已经同意"
                                        + StringUtil.getUserNameByJid(notice
                                                .getFrom()) + "的好友申请", Notice.AGREE);
                        // addDuiFang(subFrom);
                        try {
                            createSubscriber(subFrom, null, null);
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                        refresh();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendSubscribe(Presence.Type.unsubscribe, subFrom);
                        // removeInviteNotice(notice.getId());
                        NoticeManager noticeManager = NoticeManager
                                .getInstance(context);
                        noticeManager.updateAddFriendStatus(
                                notice.getId(),
                                Notice.READ,
                                "已经拒绝"
                                        + StringUtil.getUserNameByJid(notice
                                                .getFrom()) + "的好友申请", Notice.REFUSE);
                        refresh();
                    }
                }).show();
    }

    protected void addDuiFang(final String subFrom) {

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.add_also_alert, null);

        new AlertDialog.Builder(context)
                .setMessage("您也想添加" + subFrom + "为好友么？")
                .setTitle("提示")
                .setView(layout)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText editText = (EditText) layout.findViewById(R.id.nick_name);
                        String nickname = editText.getText().toString();
                        if (StringUtil.empty(nickname)) {
                            nickname = null;
                        }
                        // List<MRosterGroup> rGroups =
                        // ContacterManager.getGroups(XmppConnectionManager
                        // .getInstance().getConnection().getRoster());
                        // if (isExitJid(subFrom, rGroups)) {
                        // showToast(getResources().getString(
                        // R.string.username_exist));
                        // // return;
                        // }
                        try {
                            createSubscriber(subFrom, nickname, null);
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("不要", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 添加一个联系人
     * 
     * @param userJid 联系人JID
     * @param nickname 联系人昵称
     * @param groups 联系人添加到哪些组
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
     * 判断用户名是否存在
     * 
     * @param userName
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

    public  void refresh() {
        inviteNotices = noticeManager.getNoticeListByTypeAndPage(Notice.All);
        Collections.sort(inviteNotices);
        noticeAdapter.setNoticeList(inviteNotices);
        noticeAdapter.notifyDataSetChanged();
    }
}
