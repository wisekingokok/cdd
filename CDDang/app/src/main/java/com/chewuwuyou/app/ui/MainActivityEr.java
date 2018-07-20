package com.chewuwuyou.app.ui;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.GroupSetUpEssential;
import com.chewuwuyou.app.bean.MainRelease;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.fragment.MainSearchFragment;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.VersionUtil;
import com.chewuwuyou.app.widget.BadgeView;
import com.chewuwuyou.app.widget.MyToast;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.rong.bean.ClearMessagesBean;
import com.chewuwuyou.rong.bean.RecallMsgBean;
import com.chewuwuyou.rong.bean.RefreshBean;
import com.chewuwuyou.rong.bean.SendMsgBean;
import com.chewuwuyou.rong.utils.RongApi;
import com.chewuwuyou.app.transition_view.activity.RongChatActivity;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;

import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class MainActivityEr extends BaseFragmentActivity implements OnClickListener, FragmentCallBack {

    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private long lastTryExitTime = 0;
    private CommonAdapter<MainRelease> mAdapter;
    private List<MainRelease> mMainReleases;// 发布订单、活动、车圈、论坛、图文分享等的集合

    private int[] mImgResIds = {R.drawable.null_bg, R.drawable.null_bg, R.drawable.null_bg,
            R.drawable.main_fabudingdan, R.drawable.main_fabuhuodong, R.drawable.main_haoyouchequan,
            R.drawable.main_tuwen_fenxiang};
    private String[] mTypes = {"", "", "", "发布订单", "发布活动", "好友车圈", "图文分享"};

    private LayoutInflater mInflater;
    private LinearLayout mMainService, mMainNews, mMainRim, mMainMy;
    private ImageView mServiceImg, mNewsImg, mRimImg, mMyImg, mMainLuanzi;
    private TextView mServiceTxt, mNewsTxt, mRimTxt, mMyTxt;
    private GridView mReleaseGV;// 发布订单、活动等
    private ImageView mDismissDialogIV;// 隐藏对话框

    private ImageView mNewsIV;// 显示所有消息条数
    private BadgeView mMegNewsBaView;
    private TextView msg_size;
    private List<Notice> mNotices;
    private int thisPager = 0;

    private TextView mDotRemindView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainer);
        initView();
        initData();
        initEvent();
        if (getIntent().getStringExtra(RongChatMsgFragment.KEY_TARGET) != null) {
            Intent intent = new Intent(this, RongChatActivity.class);
            intent.putExtra(RongChatMsgFragment.KEY_TYPE, getIntent().getSerializableExtra(RongChatMsgFragment.KEY_TYPE));
            intent.putExtra(RongChatMsgFragment.KEY_TARGET, getIntent().getStringExtra(RongChatMsgFragment.KEY_TARGET));
            startActivity(intent);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        EventBus.getDefault().register(this);
        mMainService = (LinearLayout) findViewById(R.id.main_service);
        mMainNews = (LinearLayout) findViewById(R.id.main_news);
        mMainRim = (LinearLayout) findViewById(R.id.main_rim);
        mMainMy = (LinearLayout) findViewById(R.id.main_my);
        mServiceImg = (ImageView) findViewById(R.id.service_img);
        mNewsImg = (ImageView) findViewById(R.id.news_img);
        mRimImg = (ImageView) findViewById(R.id.rim_img);
        mMyImg = (ImageView) findViewById(R.id.my_img);
        mMainLuanzi = (ImageView) findViewById(R.id.main_luanzi);
        mServiceTxt = (TextView) findViewById(R.id.service_txt);
        mNewsTxt = (TextView) findViewById(R.id.news_txt);
        mRimTxt = (TextView) findViewById(R.id.rim_txt);
        mMyTxt = (TextView) findViewById(R.id.my_txt);
        msg_size = (TextView) findViewById(R.id.msg_size);
        mDotRemindView = (TextView) findViewById(R.id.remind_view);
        if (CacheTools.getUserData("isRead") != null) {
            if (CacheTools.getUserData("isRead").equals("0")) {
                mDotRemindView.setVisibility(View.VISIBLE);
            } else {
                mDotRemindView.setVisibility(View.GONE);
            }
        }
        mFragments = new Fragment[5];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_news); // 消息
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_query); // 车圈
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragment_service); // 服务
        mFragments[3] = fragmentManager.findFragmentById(R.id.fragment_my); // 我的
        mFragments[4] = fragmentManager.findFragmentById(R.id.fragment_search);// 搜索
//        ((FragmentCallBackBuilder) mFragments[1]).setFragmentCallBack(this);
        ((FragmentCallBackBuilder) mFragments[4]).setFragmentCallBack(this);
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1])
                .hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]);
        if (getIntent().getIntExtra("skipTo", 0) == Constant.SEND_MSG_BY_HISTORY) {//消息转发
            fragmentTransaction.show(mFragments[1]).commit();// 设置第几页显示
        } else {
            fragmentTransaction.show(mFragments[0]).commit();// 设置第几页显示
        }
        mNewsIV = (ImageView) findViewById(R.id.news_img);
        // 显示添加好友的信息数量
        mMegNewsBaView = new BadgeView(MainActivityEr.this, mNewsIV);
        mMegNewsBaView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_news:// 消息
                onclickSwitch(0);
                break;
            case R.id.main_rim:// 车圈
                onclickSwitch(1);
                break;
            case R.id.main_service:// 服务
                onclickSwitch(2);
                break;
            case R.id.main_my:// 我的
                onclickSwitch(3);
                break;
            case R.id.main_luanzi:
                createReleaseDialog(MainActivityEr.this);
                break;
            default:
                break;
        }

    }

    /**
     * 首页奇幻
     *
     * @param id
     */
    private void onclickSwitch(int id) {
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1])
                .hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]);
        thisPager = id;
        switch (id) {
            case 0:
                fragmentTransaction.show(mFragments[0]).commit();// 消息

                mNewsImg.setImageResource(R.drawable.img_news_selected);
                mNewsTxt.setTextColor(getResources().getColor(R.color.main_xuanz));

                mRimImg.setImageResource(R.drawable.img_rim_no_selected);
                mRimTxt.setTextColor(getResources().getColor(R.color.main_huise));

                mServiceImg.setImageResource(R.drawable.img_service_no_selected);
                mServiceTxt.setTextColor(getResources().getColor(R.color.main_huise));

                mMyImg.setImageResource(R.drawable.img_my_no_selected);
                mMyTxt.setTextColor(getResources().getColor(R.color.main_huise));

                break;
            case 1:
                fragmentTransaction.show(mFragments[1]).commit();// 车圈

                mNewsImg.setImageResource(R.drawable.img_news_no_selected);
                mNewsTxt.setTextColor(getResources().getColor(R.color.main_huise));

                mRimImg.setImageResource(R.drawable.img_rim_selected);
                mRimTxt.setTextColor(getResources().getColor(R.color.main_xuanz));

                mServiceImg.setImageResource(R.drawable.img_service_no_selected);
                mServiceTxt.setTextColor(getResources().getColor(R.color.main_huise));


                mMyImg.setImageResource(R.drawable.img_my_no_selected);
                mMyTxt.setTextColor(getResources().getColor(R.color.main_huise));
                break;
            case 2:

                fragmentTransaction.show(mFragments[2]).commit();// 服务

                mNewsImg.setImageResource(R.drawable.img_news_no_selected);
                mNewsTxt.setTextColor(getResources().getColor(R.color.main_huise));

                mRimImg.setImageResource(R.drawable.img_rim_no_selected);
                mRimTxt.setTextColor(getResources().getColor(R.color.main_huise));

                mServiceImg.setImageResource(R.drawable.img_service_selected);
                mServiceTxt.setTextColor(getResources().getColor(R.color.main_xuanz));

                mMyImg.setImageResource(R.drawable.img_my_no_selected);
                mMyTxt.setTextColor(getResources().getColor(R.color.main_huise));

                break;
            case 3:
                fragmentTransaction.show(mFragments[3]).commit();// 我的

                mServiceImg.setImageResource(R.drawable.img_service_no_selected);
                mServiceTxt.setTextColor(getResources().getColor(R.color.main_huise));

                mNewsImg.setImageResource(R.drawable.img_news_no_selected);
                mNewsTxt.setTextColor(getResources().getColor(R.color.main_huise));

                mRimImg.setImageResource(R.drawable.img_rim_no_selected);
                mRimTxt.setTextColor(getResources().getColor(R.color.main_huise));

                mMyImg.setImageResource(R.drawable.img_my_selected);
                mMyTxt.setTextColor(getResources().getColor(R.color.main_xuanz));

                break;
            case 4:
                fragmentTransaction.show(mFragments[4]).commit();
                break;
            default:
                break;
        }
    }


    public void onEventMainThread(RefreshBean refreshBean) {
        pushMsgSize();
    }

    /**
     * 发送消息后收到的通知
     *
     * @param sendMsgBean
     */
    public void onEventMainThread(SendMsgBean sendMsgBean) {
        pushMsgSize();
    }

    /**
     * 解散群关闭聊天通知
     */
    public void onEventMainThread(GroupSetUpEssential groupSetUpEssential) {
        pushMsgSize();
    }

    /**
     * 撤回消息
     *
     * @param recallMsgBean
     */
    public void onEventMainThread(RecallMsgBean recallMsgBean) {

    }

    public void onEventMainThread(io.rong.imlib.model.Message message) {//接收过来业务大厅订单消息显示小红点
        if (message.getTargetId().equals("4")) {
            mDotRemindView.setVisibility(View.VISIBLE);
        }
        pushMsgSize();
    }

    public void onEventMainThread(EventBusAdapter busAdapter) {//接收过来业务大厅订单消息显示小红点

        if (busAdapter != null && busAdapter.getOrderRemind().equals("0")) {
            CacheTools.setUserData("isRead", "1");
            mDotRemindView.setVisibility(View.GONE);
        }
    }


    /**
     * 逻辑处理
     */
    private void initData() {
        mInflater = LayoutInflater.from(MainActivityEr.this);
        VersionUtil.judgeVersionMsg(MainActivityEr.this, true);// 检测版本信息
        mMainReleases = new ArrayList<MainRelease>();
        MainRelease mainRelease;
        for (int i = 0; i < mImgResIds.length; i++) {
            mainRelease = new MainRelease();
            mainRelease.setImgResId(mImgResIds[i]);
            mainRelease.setReleaseType(mTypes[i]);
            mainRelease.id = i;
            mMainReleases.add(mainRelease);
        }
        if (CacheTools.getUserData("role") != null) {

            if (!CacheTools.getUserData("role").contains("2") && !CacheTools.getUserData("role").contains("3")) {
                mMainReleases.remove(3);
            }
        }
        setUpMyLocation();
    }

    /**
     * 事件监听
     */
    private void initEvent() {
        mMainService.setOnClickListener(this);
        mMainNews.setOnClickListener(this);
        mMainRim.setOnClickListener(this);
        mMainMy.setOnClickListener(this);
        mMainLuanzi.setOnClickListener(this);

        pushMsgSize();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (thisPager == 4) {
                onclickSwitch(1);
                return true;
            }
            if ((System.currentTimeMillis() - lastTryExitTime) > 2000) {
                StatService.onEvent(this, "ClickMainActKey", "双击主界面返回硬键");
                MyToast.showToast(this, R.string.exit_app_notify, Toast.LENGTH_SHORT);
                lastTryExitTime = System.currentTimeMillis();
            } else {
                PackageManager pm = getPackageManager();
                ResolveInfo homeInfo =
                        pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    ActivityInfo ai = homeInfo.activityInfo;
//                    Intent startIntent = new Intent(Intent.ACTION_MAIN);
//                    startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                    startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
                    RongApi.disconnect();
//                    finishActivity();
                    AppManager.getAppManager().finishAllActivity();
//                    startActivitySafely(startIntent);
                    return true;
                } else
                    return super.onKeyDown(keyCode, event);
            }
            return true;
        }
        return false;
    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            MyLog.e("YUY", "---后台运行null--");
        } catch (SecurityException e) {
            MyLog.e("YUY", "---后台运行null--");
        }
    }

    /**
     * 发布订单、活动、车圈等的dialog
     *
     * @param context
     */
    @SuppressWarnings("deprecation")
    private void createReleaseDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.main_release_ac, null);
        mReleaseGV = (GridView) layout.findViewById(R.id.main_release_gv);
        TextView day = (TextView) layout.findViewById(R.id.today_day_tv);
        TextView week = (TextView) layout.findViewById(R.id.today_week_tv);
        TextView year_and_month = (TextView) layout.findViewById(R.id.today_year_and_month_tv);
        Calendar calendar = Calendar.getInstance();
        day.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
        week.setText(DateTimeUtil.getWeek());
        year_and_month.setText(calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.YEAR));
        addAnotation(true, dialog);
        // layout.getBackground().setAlpha(20);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle); // 添加动画
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        lp.height = (int) (display.getHeight());
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        mDismissDialogIV = (ImageView) layout.findViewById(R.id.delete_iv);
        mDismissDialogIV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                addAnotation(false, dialog);
                mReleaseGV.startLayoutAnimation();
            }
        });
        mAdapter = new CommonAdapter<MainRelease>(MainActivityEr.this, mMainReleases, R.layout.main_release_item) {

            @Override
            public void convert(ViewHolder holder, MainRelease t, int p) {
                holder.setImageResource(R.id.release_iv, t.getImgResId());
                holder.setText(R.id.release_type_tv, t.getReleaseType());
            }
        };
        mReleaseGV.setAdapter(mAdapter);
        mReleaseGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dialog.dismiss();
                Intent intent = null;
                switch (mAdapter.getItem(arg2).id) {
                    case 3:
                        intent = new Intent(MainActivityEr.this, // 发布订单
                                NewReleaseHallActivity.class);
                        break;
                    case 4:
                        intent = new Intent(MainActivityEr.this, // 发布活动
                                AddYueActivity.class);
                        break;
                    case 5:
                        intent = new Intent(MainActivityEr.this, // 发布圈
                                AddQuanActivity.class);
                        break;
                    case 6:
                        // 去掉发布论坛
                        // intent = new Intent(MainActivityEr.this,
                        // AddTieActivity.class);//发布论坛
                        // intent.putExtra("type", 1);
                        intent = new Intent(MainActivityEr.this, AddHotTieActivity.class);// 图文分享
                        intent.putExtra("type", 1);
                        break;
                    // case 7:
                    // intent = new Intent(MainActivityEr.this,
                    // AddHotTieActivity.class);// 图文分享
                    // intent.putExtra("type", 1);
                    // break;
                    default:
                        break;
                }
                if (intent != null)
                    startActivity(intent);
            }
        });
    }

    /**
     * 发送消息后收到的通知
     *
     * @param clearMessagesBean
     */
    public void onEventMainThread(ClearMessagesBean clearMessagesBean) {
        pushMsgSize();
    }

    private void pushMsgSize() {
//        RongApi.getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
//            @Override
//            public void onSuccess(final Integer intr) {
//                RongApi.getUnreadCount(Conversation.ConversationType.PRIVATE, com.chewuwuyou.rong.utils.Constant.USER_ID_TYPE.TASK_HALL, new RongIMClient.ResultCallback<Integer>() {
//                    @Override
//                    public void onSuccess(Integer inter) {
//                        Log.e("--", intr + "  " + inter);
//                        int integer = intr - inter;
//                        if (integer <= 0) {
//                            msg_size.setVisibility(View.GONE);
//                            return;
//                        }
//                        msg_size.setVisibility(View.VISIBLE);
//                        if (integer >= 99) {
//                            msg_size.setText("99+");
//                            return;
//                        }
//                        msg_size.setText(integer + "");
//                    }
//
//                    @Override
//                    public void onError(RongIMClient.ErrorCode errorCode) {
//                        msg_size.setVisibility(View.GONE);
//                    }
//                });
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//                msg_size.setVisibility(View.GONE);
//            }
//        });

        RongApi.clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, com.chewuwuyou.rong.utils.Constant.USER_ID_TYPE.TASK_HALL, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                RongApi.getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        if (integer <= 0) {
                            msg_size.setVisibility(View.GONE);
                            return;
                        }
                        msg_size.setVisibility(View.VISIBLE);
                        if (integer >= 99) {
                            msg_size.setText("99+");
                            return;
                        }
                        msg_size.setText(integer + "");
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 添加动画
     */
    private void addAnotation(boolean isIn, final Dialog dialog) {
        if (isIn) {
            Animation set = AnimationUtils.loadAnimation(this, R.anim.dialog_item_enter);
            LayoutAnimationController controller = new LayoutAnimationController(set);
            controller.setDelay(0.2f);
            controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
            mReleaseGV.setLayoutAnimation(controller);
        } else {
            Animation set = AnimationUtils.loadAnimation(this, R.anim.dialog_item_exit);
            LayoutAnimationController controller = new LayoutAnimationController(set);
            controller.setDelay(0f);
            controller.setOrder(LayoutAnimationController.ORDER_RANDOM);
            mReleaseGV.setLayoutAnimation(controller);
            mReleaseGV.setVisibility(View.GONE);
            dialog.dismiss();
            mReleaseGV.setLayoutAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }
            });
        }
    }

    @Override
    public void callback(int pager, Object obj) {
        switch (pager) {
            case 1:// 消息界面过来的
                ((MainSearchFragment) mFragments[4]).setData(obj);
                onclickSwitch(4);
                break;
            case 4:// 搜索页面过来
                onclickSwitch(1);
                break;
            default:
                break;
        }
    }


//    @Override
//    protected void onResume() {
//        isForeground = true;
//        NoticeManager noticeManager = NoticeManager.getInstance(MainActivityEr.this);
//        if (noticeManager != null) {
//            mNotices = noticeManager.getUnReadNoticeListByType(Notice.UNREAD);
//        }
//        for (int i = 0; i < mNotices.size(); i++) {
//            if (mNotices.get(i).getNoticeType() != Notice.CHAT_MSG
//                    || mNotices.get(i).getNoticeType() != Notice.ADD_FRIEND) {// 如果是聊天消息或添加好友消息就显示,否则就隐藏
//                mNotices.remove(i);
//
//            }
//        }
//        if (mNotices.size() > 0) {
//            mMegNewsBaView.setText(String.valueOf(mNotices.size()));
//            mMegNewsBaView.show();
//        } else {
//            mMegNewsBaView.hide();
//        }
//        super.onResume();
//    }

    private void setUpMyLocation() {
        if (CacheTools.getUserData("Lat") == null) {//位置信息为空 不上传
            return;
        }
        AjaxParams params = new AjaxParams();
        params.put("lat", CacheTools.getUserData("Lat"));
        params.put("lng", CacheTools.getUserData("Lng"));
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("YUY", "-----------上传位置信息----" + msg.obj);
                        break;
                    case Constant.NET_DATA_FAIL:
                        MyLog.e("YUY", "上传位置信息失败");
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.SET_UP_LOCATION, false, 1);
    }
}
