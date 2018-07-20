package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ChatUserInfo;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.SearchFriend;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.view.NoticeAdapter;

/**
 * @version 1.1.0
 * @describe:通过电话号查找或二维码扫描查找好友
 * @author:yuyong
 * @created:2015-2-12下午5:03:04
 */
public class SearchFriendActivity extends BaseActivity {

    /**
     * 输入电话查找好友
     */
    @ViewInject(id = R.id.search_friend_phone_et)
    private EditText mSearchPhoneET;
    /**
     * 点击查找按钮
     */
    @ViewInject(id = R.id.search_friend_ibtn, click = "onAction")
    private ImageButton mSearchFriendIBtn;
    /**
     * 扫描二维码查找
     */
    // @ViewInject(id = R.id.barcode_scan_ll, click = "onAction")
    // private LinearLayout mBarCodeSearchFriendLL;
    /**
     * 返回
     */
    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackIBtn;

    @ViewInject(id = R.id.sub_header_bar_right_ibtn, click = "onAction")
    private ImageButton mBarCodeSanIBtn;
    /**
     * 标题
     */
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mTitleTV;
    /**
     * 好友请求列表
     */
    @ViewInject(id = R.id.friend_get_list)
    private ListView mFriendGetList;
    /**
     * 添加通讯录好友
     */
    @ViewInject(id = R.id.add_contacts_friend_ll, click = "onAction")
    private LinearLayout mAddContactsFriendLL;
    // private ChatUser chatUser;// 通讯用户实体
    private List<Notice> inviteNotices = new ArrayList<Notice>();
    private NoticeManager noticeManager;
    private NoticeAdapter noticeAdapter = null;
    //	private List<ChatUserInfo> chatUsers = new ArrayList<ChatUserInfo>();
    private RelativeLayout mTitleHeight;// 标题布局高度

    FinalDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_ac);
        mTitleTV.setText(R.string.search_friend_title);
        initView();
    }

    private void initView() {
        //	db = FinalDb.create(this);

        // closeInput();
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        getSystemService(Context.INPUT_METHOD_SERVICE);
        mBarCodeSanIBtn.setImageResource(R.drawable.icon_sm);
        mBarCodeSanIBtn.setVisibility(View.VISIBLE);
        noticeManager = NoticeManager.getInstance(SearchFriendActivity.this);
        inviteNotices = noticeManager.getUnReadNoticeListByType(Notice.ADD_FRIEND);
        String userIds = "";
        for (int i = 0; i < inviteNotices.size(); i++) {
            if (inviteNotices.get(i).getAddState() == 0) {
                userIds += inviteNotices.get(i).getFrom().split("@")[0] + "-";
            }
        }
//        if (!TextUtils.isEmpty(userIds)) {
//            AjaxParams params = new AjaxParams();
//            params.put("ids", userIds.substring(0, userIds.length() - 1));
//            requestNet(new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    switch (msg.what) {
//                        case Constant.NET_DATA_SUCCESS:
//                            MyLog.e("YUY", "msg.obj.toString() = " + msg.obj.toString());
//                            chatUsers = ChatUserInfo.parseList(msg.obj.toString());
//                            noticeAdapter = new NoticeAdapter(SearchFriendActivity.this, inviteNotices);
//                            mFriendGetList.setAdapter(noticeAdapter);
//                            break;
//
//                        default:
//                            break;
//                    }
//                }
//            }, params, NetworkUtil.GET_CHAT_USER_INFO, false, 0);
//        }

    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.search_friend_ibtn:
                searchFriend();
                break;
            // case R.id.barcode_scan_ll:
            // Intent intent = new Intent(SearchFriendActivity.this,
            // CaptureActivity.class);
            // startActivity(intent);
            // break;
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_ibtn:
                Intent intent = new Intent(SearchFriendActivity.this, CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.add_contacts_friend_ll:
                Intent addContactsIntent = new Intent(SearchFriendActivity.this, AddContactsFriendActivity.class);
                startActivity(addContactsIntent);

                break;
        }
    }

    private void searchFriend() {
        String searchStr = mSearchPhoneET.getText().toString().trim();

        if (TextUtils.isEmpty(searchStr)) {
            ToastUtil.toastShow(SearchFriendActivity.this, "请输入好友关键字便于查询");
        } else if (!searchStr.matches(RegularUtil.verifyTelephone)) {
            Intent intent = new Intent(SearchFriendActivity.this, SearchFriendListActivity.class);
            intent.putExtra("searchStr", searchStr);
            startActivity(intent);
        } else {
            AjaxParams params = new AjaxParams();
            params.put("tels", searchStr);
            requestNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            if (!TextUtils.isEmpty(msg.obj.toString())) {
                                try {
                                    JSONObject jo = new JSONObject(msg.obj.toString());
                                    if (jo.getJSONArray("info").length() > 0) {
                                        SearchFriend searchFriend = SearchFriend
                                                .parse(jo.getJSONArray("info").get(0).toString());
                                        Intent intent = new Intent(SearchFriendActivity.this, PersonalHomeActivity2.class);
                                        intent.putExtra("userId", searchFriend.getId());
                                        startActivity(intent);
                                    } else {
                                        ToastUtil.toastShow(SearchFriendActivity.this, "无此手机号码");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ToastUtil.toastShow(SearchFriendActivity.this, "无此手机号码");
                            }
                            break;
                        case Constant.NET_DATA_FAIL:
                            ToastUtil.toastShow(SearchFriendActivity.this, ((DataError) msg.obj).getErrorMessage());
                            break;
                        default:
                            break;
                    }
                }
            }, params, NetworkUtil.GET_CONTACTS_FRIEND, false, 1);
        }

    }

    // private OnItemClickListener inviteListClick = new OnItemClickListener() {
    //
    // @Override
    // public void onItemClick(AdapterView<?> arg0, View view, int arg2,
    // long arg3) {
    // final Notice notice = (Notice) view.findViewById(R.id.new_content)
    // .getTag();
    // // 消息类型判断
    // if (Notice.ADD_FRIEND == notice.getNoticeType()
    // && notice.getStatus() == Notice.UNREAD) {// 添加好友
    // showAddFriendDialag(notice);
    // } else if (Notice.SYS_MSG == notice.getNoticeType()) {// 系统通知
    // Intent intent = new Intent(SearchFriendActivity.this,
    // SystemNoticeDetailActivity.class);
    // intent.putExtra("notice_id", notice.getId());
    // startActivityForResult(intent, 1);
    // }
    //
    // }
    // };

    // private void showAddFriendDialag(final Notice notice) {
    // final String subFrom = notice.getFrom();
    // new AlertDialog.Builder(SearchFriendActivity.this)
    // .setMessage(subFrom + "请求添加您为好友")
    // .setTitle("提示")
    // .setPositiveButton("添加", new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // // 接受请求
    // sendSubscribe(Presence.Type.subscribed, subFrom);
    // sendSubscribe(Presence.Type.subscribe, subFrom);
    // // removeInviteNotice(notice.getId());
    // NoticeManager noticeManager = NoticeManager
    // .getInstance(SearchFriendActivity.this);
    // noticeManager.updateAddFriendStatus(
    // notice.getId(),
    // Notice.READ,
    // "已经同意"
    // + StringUtil.getUserNameByJid(notice
    // .getFrom()) + "的好友申请", Notice.AGREE);
    //
    // // addDuiFang(subFrom);
    // try {
    // createSubscriber(subFrom, null, null);
    // } catch (XMPPException e) {
    // e.printStackTrace();
    // }
    // refresh();
    // }
    // })
    // .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // sendSubscribe(Presence.Type.unsubscribe, subFrom);
    // // removeInviteNotice(notice.getId());
    // NoticeManager noticeManager = NoticeManager
    // .getInstance(SearchFriendActivity.this);
    // noticeManager.updateAddFriendStatus(
    // notice.getId(),
    // Notice.READ,
    // "已经拒绝"
    // + StringUtil.getUserNameByJid(notice
    // .getFrom()) + "的好友申请", Notice.REFUSE);
    // refresh();
    // }
    // }).show();
    // }

    /**
     * 添加一个联系人
     *
     * @param userJid  联系人JID
     * @param nickname 联系人昵称
     * @param groups   联系人添加到哪些组
     * @throws XMPPException
     */
    protected void createSubscriber(String userJid, String nickname, String[] groups) throws XMPPException {
        try {
            XmppConnectionManager.getInstance().getConnection().getRoster().createEntry(userJid, nickname, groups);
        } catch (NotLoggedInException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回复一个presence信息给用户
     *
     * @param type
     * @param to
     */
    // protected void sendSubscribe(Presence.Type type, String to) {
    // Presence presence = new Presence(type);
    // presence.setTo(to);
    // try {
    // XmppConnectionManager.getInstance().getConnection()
    // .sendPacket(presence);
    // } catch (NotConnectedException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * 关闭键盘事件
     *
     * @author sxk
     * @update 2012-7-4 下午2:34:34
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
