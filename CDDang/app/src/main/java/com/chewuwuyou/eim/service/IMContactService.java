package com.chewuwuyou.eim.service;

import java.util.Calendar;
import java.util.Collection;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.ui.SearchFriendActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.ContacterManager;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.model.User;
import com.chewuwuyou.eim.util.DateUtil;
import com.chewuwuyou.eim.util.StringUtil;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 联系人服务.
 *
 * @author sxk
 */
public class IMContactService extends BaseService {

    private Roster roster = null;
    private Context context;
    /* 声明对象变量 */
    private NotificationManager myNotiManager;

    // //这种service只会启动一个
    // private static boolean started = false;

    // private static final int SHOW_DIALOG = 0;
    // private Handler mHandler = new Handler() {
    // public void handleMessage(android.os.Message msg) {
    // switch (msg.what) {
    // case SHOW_DIALOG: // 还未实现
    // Notice notice = (Notice) msg.obj;
    // showAddFriendDialag(notice);
    // break;
    // default:
    // break;
    // }
    // }
    // };

    @Override
    public void onCreate() {
        MyLog.e("XXXXXXXX", "IMContactService create!!!!!!!!!!!!!!!!!!!!");
        context = this;
        // ACTION_USE = Constant.CONTACT_STARTED;
        super.onCreate();
    }

    // 此方法是为了可以在Acitity中获得服务的实例
    public class ServiceBinder extends Binder {
        public IMContactService getService() {
            return IMContactService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        MyLog.e("XXXXXXXX", "IMContactService onBind!!!!!!!!!!!!!!!!!!!!");
        init();
        addSubscriptionListener();
        // AppContext.getInstance().doContactStarted();
        return new ServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        MyLog.e("XXXXXXXX",
                "IMContactService onUnbind&&&&&&&&&&&&&&&&&&&&&&&&&");
        XmppConnectionManager.getInstance().getConnection()
                .removePacketListener(subscriptionPacketListener);
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        addSubscriptionListener();
        // Intent intentL = new Intent(ACTION_USE);
        // context.sendBroadcast(intentL);
        // flags = START_STICKY;
        MyLog.e("XXXXXXXX",
                "IMContactService Command Started!!!!!!!!!!!!!!!!!!!!");
        int returnInt = super.onStartCommand(intent, flags, startId);
        return returnInt;
    }

    private void init() {
        /* 初始化对象 */
        myNotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initRoster();
    }

    /**
     * 添加一个监听，监听好友添加请求。
     */
    private void addSubscriptionListener() {
        PacketFilter filter = new PacketFilter() {
            @Override
            public boolean accept(Packet packet) {
                if (packet instanceof Presence) {
                    Presence presence = (Presence) packet;
                    if (presence.getType().equals(Presence.Type.subscribe)) {
                        return true;
                    }
                }
                return false;
            }
        };
        XmppConnectionManager.getInstance().getConnection()
                .addPacketListener(subscriptionPacketListener, filter);
    }

    /**
     * 初始化花名册 服务重启时，更新花名册
     */
    private void initRoster() {
        roster = XmppConnectionManager.getInstance().getConnection()
                .getRoster();
        roster.removeRosterListener(rosterListener);
        roster.addRosterListener(rosterListener);
        ContacterManager.init(XmppConnectionManager.getInstance()
                .getConnection());
    }

    private PacketListener subscriptionPacketListener = new PacketListener() {

        @Override
        public void processPacket(final Packet packet) {
            String user = getSharedPreferences(Constant.LOGIN_SET, 0)
                    .getString(Constant.USERNAME, null);
            if (packet.getFrom().contains(user))
                return;
            // 如果是自动接收所有请求，则回复一个添加信息
            if (Roster.getDefaultSubscriptionMode().equals(
                    SubscriptionMode.accept_all)) {
                Presence subscription = new Presence(Presence.Type.subscribe);
                subscription.setTo(packet.getFrom());
                try {
                    XmppConnectionManager.getInstance().getConnection()
                            .sendPacket(subscription);
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            } else {
                if (ContacterManager.getByUserJid(packet.getFrom(), XmppConnectionManager.getInstance().getConnection()) != null) {//判断此好友在好友在我的列表中
                    MyLog.e("YUY", "------通讯录已存在联系人");
                    sendSubscribe(Presence.Type.subscribed, packet.getFrom());
                    sendSubscribe(Presence.Type.subscribe, packet.getFrom());
                    return;

                }
                final NoticeManager noticeManager = NoticeManager
                        .getInstance(context);
                final Notice notice = new Notice();
                notice.setTitle("好友请求");
                notice.setNoticeType(Notice.ADD_FRIEND);
                notice.setContent(packet.getFrom()
                        + "申请加您为好友");
                notice.setFrom(packet.getFrom());
                notice.setTo(packet.getTo());
                notice.setNoticeTime(DateUtil.date2Str(Calendar.getInstance(),
                        Constant.MS_FORMART));
                notice.setStatus(Notice.UNREAD);
                long noticeId = noticeManager.saveNotice(notice);
                if (noticeId != -1) {//由于加好友发送两次通知 此条注释
                    MyLog.i("YUY", "-------发送好友请求广播----------");
                    Intent intent = new Intent();
                    intent.setAction(Constant.ADD_FRIEND_QEQUEST);
                    intent.putExtra("notice", notice);
                    sendBroadcast(intent);
//									setNotiType(R.drawable.notification_ic_luncher,
//											getResources().getString(R.string.new_message),
//											notice.getContent(), SearchFriendActivity.class);
                }
//				AjaxParams params=new AjaxParams();
//				if(packet.getFrom().contains("@")){
//					params.put("ids", packet.getFrom().split("@")[0]);
//				}else{
//					params.put("ids", packet.getFrom());
//				}
//				NetworkUtil.postMulti(NetworkUtil.GET_USER_INFO, params, new AjaxCallBack<String>() {
//					@Override
//					public void onSuccess(String o) {
//						super.onSuccess(o);
//						try {
//							JSONObject jo=new JSONObject(o);
//							if(jo.getInt("result")==1){
//								PersonHome	mPersonHome = PersonHome.parse(jo.getJSONArray("data").get(0).toString());
//								if(!TextUtils.isEmpty(mPersonHome.getNick())){
//									notice.setContent(mPersonHome.getNick()
//											+ "申请加您为好友");
//								}else{
//									notice.setContent(packet.getFrom()
//											+ "申请加您为好友");
//								}
//								notice.setFrom(packet.getFrom());
//								notice.setTo(packet.getTo());
//								notice.setNoticeTime(DateUtil.date2Str(Calendar.getInstance(),
//										Constant.MS_FORMART));
//								notice.setStatus(Notice.UNREAD);
//								long noticeId = noticeManager.saveNotice(notice);
//								MyLog.e("YUY","notice_to------------- "+packet.getTo()+" -------------noticeId = "+noticeId+" ----------------"+notice.getContent());
//								if (noticeId != -1) {//由于加好友发送两次通知 此条注释
//									MyLog.i("YUY","-------发送好友请求广播----------");
//									Intent intent = new Intent();
//									intent.setAction(Constant.ADD_FRIEND_QEQUEST);
//									intent.putExtra("notice", notice);
//									sendBroadcast(intent);
////									setNotiType(R.drawable.notification_ic_luncher,
////											getResources().getString(R.string.new_message),
////											notice.getContent(), SearchFriendActivity.class);
//								}
//							}
//
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//
//					}
//
//					@Override
//					public void onFailure(Throwable t, int errorNo, String strMsg) {
//						super.onFailure(t, errorNo, strMsg);
//					}
//				});


            }
        }

    };

    /**
     * 发出Notification的method.
     *
     * @param iconId       图标
     * @param contentTitle 标题
     * @param contentText  你内容
     * @param activity
     * @author sxk
     * @update 2012-5-14 下午12:01:55
     */
    @SuppressWarnings({"rawtypes", "deprecation"})
    private void setNotiType(int iconId, String contentTitle,
                             String contentText, Class activity) {
        /*
         * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
        Intent notifyIntent = new Intent(this, activity);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /* 创建PendingIntent作为设置递延运行的Activity */
        PendingIntent appIntent = PendingIntent.getActivity(this, 0,
                notifyIntent, 0);

		/* 创建Notication，并设置相关参数 */
        Notification myNoti = new Notification();
        myNoti.flags = Notification.FLAG_AUTO_CANCEL;
        /* 设置statusbar显示的icon */
        myNoti.icon = iconId;
        /* 设置statusbar显示的文字信息 */
        myNoti.tickerText = contentTitle;
        /* 设置notification发生时同时发出默认声音 */
        AudioManager volMgr = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        switch (volMgr.getRingerMode()) {// 获取系统设置的铃声模式
            case AudioManager.RINGER_MODE_SILENT:// 静音模式，值为0，这时候不震动，不响铃
                myNoti.sound = null;
                myNoti.vibrate = null;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:// 震动模式，值为1，这时候震动，不响铃
                myNoti.sound = null;
                myNoti.defaults |= Notification.DEFAULT_VIBRATE;
                break;

            case AudioManager.RINGER_MODE_NORMAL:// 常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动

                if (CacheTools.getUserData("isSound") == null
                        || CacheTools.getUserData("isSound").equals("1")) {
                    if (CacheTools.getUserData("isShake") == null
                            || CacheTools.getUserData("isShake").equals("1")) {
                        // 11模式
                        myNoti.defaults = Notification.DEFAULT_SOUND;
                        myNoti.defaults |= Notification.DEFAULT_VIBRATE;
                    } else {
                        // 10模式
                        myNoti.defaults = Notification.DEFAULT_SOUND;
                        myNoti.vibrate = null;
                    }
                } else {
                    if (CacheTools.getUserData("isShake") == null
                            || CacheTools.getUserData("isShake").equals("1")) {
                        // 01模式
                        myNoti.sound = null;
                        myNoti.defaults |= Notification.DEFAULT_VIBRATE;
                    } else {
                        // 00模式
                        myNoti.sound = null;
                        myNoti.vibrate = null;
                    }
                }
                // 获取软件的设置
                break;
            default:
                myNoti.defaults = Notification.DEFAULT_SOUND;
                myNoti.defaults |= Notification.DEFAULT_VIBRATE;
                break;

        }
        /* 设置Notification留言条的参数 */
//        myNoti.setLatestEventInfo(this, contentTitle, contentText, appIntent);
        /* 送出Notification */
        myNotiManager.notify(0, myNoti);
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

    @Override
    public void onDestroy() {
        // 释放资源
        XmppConnectionManager.getInstance().getConnection()
                .removePacketListener(subscriptionPacketListener);
        ContacterManager.destroy();
        super.onDestroy();
    }

    private RosterListener rosterListener = new RosterListener() {

        @Override
        public void presenceChanged(Presence presence) {
            Log.i("XXXXXXXX", "!!!!presenceChanged!!!!!!!");
            Intent intent = new Intent();
            intent.setAction(Constant.ROSTER_PRESENCE_CHANGED);
            String subscriber = presence.getFrom().substring(0,
                    presence.getFrom().indexOf("/"));
            RosterEntry entry = roster.getEntry(subscriber);
            //TODO 可能报空指针异常
            if (ContacterManager.contacters.containsKey(subscriber)) {
                // 将状态改变之前的user广播出去
                intent.putExtra(User.userKey,
                        ContacterManager.contacters.get(subscriber));
                ContacterManager.contacters.remove(subscriber);
                ContacterManager.contacters.put(subscriber,
                        ContacterManager.transEntryToUser(entry, roster));
            }
            sendBroadcast(intent);
        }

        @Override
        public void entriesUpdated(Collection<String> addresses) {
            for (String address : addresses) {
                Intent intent = new Intent();
                intent.setAction(Constant.ROSTER_UPDATED);
                // 获得状态改变的entry
                RosterEntry userEntry = roster.getEntry(address);
                User user = ContacterManager
                        .transEntryToUser(userEntry, roster);
                if (ContacterManager.contacters.get(address) != null) {
                    // 这里发布的是更新前的user
                    intent.putExtra(User.userKey,
                            ContacterManager.contacters.get(address));
                    // 将发生改变的用户更新到userManager
                    ContacterManager.contacters.remove(address);
                    ContacterManager.contacters.put(address, user);
                }
                sendBroadcast(intent);
                // 用户更新，getEntries会更新
                // roster.getUnfiledEntries中的entry不会更新
            }
        }

        @Override
        public void entriesDeleted(Collection<String> addresses) {
            for (String address : addresses) {
                Intent intent = new Intent();
                intent.setAction(Constant.ROSTER_DELETED);
                User user = null;
                if (ContacterManager.contacters.containsKey(address)) {
                    user = ContacterManager.contacters.get(address);
                    ContacterManager.contacters.remove(address);
                }
                intent.putExtra(User.userKey, user);
                sendBroadcast(intent);
            }
        }

        @Override
        public void entriesAdded(Collection<String> addresses) {
            for (String address : addresses) {
                Intent intent = new Intent();
                intent.setAction(Constant.ROSTER_ADDED);
                RosterEntry userEntry = roster.getEntry(address);
                User user = ContacterManager
                        .transEntryToUser(userEntry, roster);
                ContacterManager.contacters.put(address, user);
                intent.putExtra(User.userKey, user);
                sendBroadcast(intent);
            }
        }
    };

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
            XmppConnectionManager.getInstance().getConnection().sendPacket(presence);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
