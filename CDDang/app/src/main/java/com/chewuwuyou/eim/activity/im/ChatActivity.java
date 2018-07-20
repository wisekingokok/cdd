package com.chewuwuyou.eim.activity.im;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.FaceVPAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.ui.CollectReleaseOrderActivity;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ChatUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ExpressionUtil;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.utils.VoiceUtils;
import com.chewuwuyou.app.widget.PasteEditText;
import com.chewuwuyou.eim.activity.LoginActivity;
import com.chewuwuyou.eim.manager.ContacterManager;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.IMMessage;
import com.chewuwuyou.eim.model.User;
import com.chewuwuyou.eim.util.StringUtil;
import com.chewuwuyou.eim.view.MessageAdapter;
import com.chewuwuyou.eim.view.VoicePlayClickListener;
import com.hp.hpl.sparta.Sparta;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

@SuppressLint({"SdCardPath", "HandlerLeak", "InflateParams",
        "ClickableViewAccessibility"})
public class ChatActivity extends AChatActivity implements OnClickListener,
        ViewBinder

{
    private ImageButton mBackBtn;
    private MessageAdapter adapter = null;
    private PasteEditText messageInputET = null;
    private Button messageSendBtn = null;
    private int recordCount;
    private View listHead;
    private Button listHeadButton;
    private User user;// 聊天人ƒ
    private TextView tvChatTitle;
    private String to_name;
    private Button mChatAddBtn;// 聊天添加表情、图片
    private LinearLayout layout_more, layout_add;
    private ListView listView;
    private LinearLayout mFaceLL;// 展示表情的layout
    private TabHost mFaceTabhost; // 展示表情的tabhost
    private ImageView mFaceIV;
    private ImageView mFaceCheckIV;
    public static final int ADD_FACE_PIC = 114;// 添加表情
    private RelativeLayout mChatRL;// 聊天layout
    private Button mVioceSetBtn;// 切换语音
    private Button mKeyBordBtn;// 切换键盘
    private LinearLayout mSendVioceLL;// 点击发送语音
    private TextView mRecordingHint;
    private RelativeLayout mRecordingContainerRL;
    private ImageView mRecordingIV;// 根据声音频率切换图片的显示效果
    private TextView mPictureTV, mCameraTV, mMyLocationTV, mToOrderTV,
            mPhoneTV, mDeleteChatTV, mMailAddressTV;// 相册，拍照，位置，向TA下单，电话联系,粉碎聊天
    public static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;
    private File cameraFile;
    public static final int REQUEST_CODE_LOCATION = 1;
    public static final int REQUEST_CODE_PICTURE = 2;
    public static final int REQUEST_CODE_VOICE = 3;
    public static final int REQUEST_CODE_CAMERA = 4;
    private static final int POLL_INTERVAL = 300;
    private String mFileName = null;// 语音保存路径
    private MediaRecorder mRecorder = null;// 用于完成录音
    private static final String PATH = "/sdcard/MyVoiceForder/Record/";// 录音存储路径
    private VoiceRcdTimeTask mVoiceRcdTimeTask;
    private int mRcdStartTime = 0;// 录制的开始时间
    private int mRcdVoiceDelayTime = 1000;
    private int mRcdVoiceStartDelayTime = 300;
    private ScheduledExecutorService mExecutor;// 录制计时器
    private long mStartRecorderTime;
    private long mEndRecorderTime;
    // private int mServiceType = 0;
    public static ChatActivity activityInstance = null;
    private ImageButton mFriendInfoIBtn;
    private ViewPager mFaceViewPager;
    private LinearLayout mDotsLayout;
    private ViewPager mGifViewPager;
    private LinearLayout mGifDotsLayout;
    private int columns = 7, rows = 3;// 表情图标每页6列4行
    private int gifColmns = 5, gifRows = 2;// gif表情每页5列2行
    private List<View> faceViews = new ArrayList<View>();// 每页显示的表情view
    private List<View> gifViews = new ArrayList<View>();
    private List<String> staticFacesList;// 表情列表
    private List<String> staticGifsList;// gif动画
    private LayoutInflater inflater;
    private String mUserName;// 用户名
    private String mIsBusOrKefu;// 判断是商家还是客服
    private String mBusinesId;// 商家ID
    private String mHandlerId;// 客服ID（订单处理者的ID）
    private String mUserTelephone;// 用户电话
    private String mBlack;//黑名单关系
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = getAmplitude();
            VoiceUtils.setVoiceImage(amp, mRecordingIV);
            mVoiceHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };
    private StringBuilder mMailAddressSB = new StringBuilder("");//存储邮寄地址
    private PopupWindow mPopupWindow;//聊天信息操作popwindow

    /**
     * 记录语音时长
     */
    private class VoiceRcdTimeTask implements Runnable {
        @SuppressWarnings("unused")
        int time = 0;

        public VoiceRcdTimeTask(int startTime) {
            time = startTime;
        }

        @Override
        public void run() {
            time++;
        }
    }

    private Handler mVoiceHandler = new Handler();
    private RelativeLayout mTitleHeight;
    private Handler mRefreshHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    recordCount = MessageManager.getInstance(context)
                            .getChatCountWithSb(to);
                    MyLog.i("YUY", "聊天记录条数 = " + recordCount);
                    if (recordCount <= 0) {
                        listHead.setVisibility(View.GONE);
                    } else {
                        listHead.setVisibility(View.VISIBLE);
                    }
                    refreshUI();
                    break;

                default:
                    break;
            }
        }

    };

    private Handler mGifHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 12:
                    sendGifMessage(msg.obj.toString());
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        activityInstance = this;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        staticFacesList = ExpressionUtil.initStaticFaces(this);// 初始化表情图
        staticGifsList = ExpressionUtil.initStaticGifs(this);// 初始化gif图
        ExpressionUtil.getFaceStrMap(ChatActivity.this);// 获取新的表情数据Map
        // ExpressionUtil.getOldFaceStrMap(ChatActivity.this);//老的表情数据
        initView();
        initFaceViewPager();// 初始化表情
        initGifViewPager();// 初始化gif图
        initDate();
        initEvent();
    }

    private void initView() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        android.view.ViewGroup.LayoutParams layout = mTitleHeight
                .getLayoutParams();
        mTitleHeight.getLayoutParams();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int density = metric.densityDpi;
        // 240当前手机的像素
        layout.height = 64 * density / 240;
        mTitleHeight.setLayoutParams(layout);
        // 表情下小圆点
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
        mFaceViewPager = (ViewPager) findViewById(R.id.face_viewpager);
        mFaceViewPager.setOnPageChangeListener(new PageChange());

        mGifDotsLayout = (LinearLayout) findViewById(R.id.gif_dots_container);
        mGifViewPager = (ViewPager) findViewById(R.id.gif_viewpager);
        mGifViewPager.setOnPageChangeListener(new PageChange());
        mFriendInfoIBtn = (ImageButton) findViewById(R.id.sub_header_bar_right_ibtn);
        mFriendInfoIBtn.setImageResource(R.drawable.top_btn_me);

        // 先看user，不为空的时候，必须保证不是小叮小当才能点击;当user为空的时候，看to，同理，to还要有不是conference的要求
        if ((user != null && user.getJID().contains("@")
                && !user.getJID().contains("xiaoding") && !user.getJID()
                .contains("xiaodang"))
                || (user == null && to.contains("@")
                && !to.contains("xiaoding") && !to.contains("xiaodang") && !to
                .contains("conference"))) {
            mFriendInfoIBtn.setVisibility(View.VISIBLE);
        } else {
            mFriendInfoIBtn.setVisibility(View.GONE);
        }
        mBackBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mBackBtn.setOnClickListener(this);
        layout_more = (LinearLayout) findViewById(R.id.layout_more);
        layout_add = (LinearLayout) findViewById(R.id.layout_add);
        mChatAddBtn = (Button) findViewById(R.id.btn_chat_add);
        mFaceIV = (ImageView) findViewById(R.id.face_iv);
        mFaceCheckIV = (ImageView) findViewById(R.id.iv_emoticons_checked);
        mChatRL = (RelativeLayout) findViewById(R.id.chat_edit_rl);
        mChatRL.setBackgroundResource(R.drawable.input_bar_bg_normal);
        mVioceSetBtn = (Button) findViewById(R.id.voice_btn);
        mKeyBordBtn = (Button) findViewById(R.id.keyboard_btn);
        mSendVioceLL = (LinearLayout) findViewById(R.id.speak_ll);
        mRecordingHint = (TextView) findViewById(R.id.recording_hint);
        mRecordingContainerRL = (RelativeLayout) findViewById(R.id.recording_container);
        mRecordingIV = (ImageView) findViewById(R.id.mic_image);
        mPictureTV = (TextView) findViewById(R.id.tv_picture);
        mCameraTV = (TextView) findViewById(R.id.tv_camera);
        mMyLocationTV = (TextView) findViewById(R.id.tv_location);
        mToOrderTV = (TextView) findViewById(R.id.tv_order);
        mPhoneTV = (TextView) findViewById(R.id.tv_call);
        mFaceLL = (LinearLayout) findViewById(R.id.face_ll);
        mFaceTabhost = (TabHost) findViewById(R.id.face_tabhost);
        tvChatTitle = (TextView) findViewById(R.id.sub_header_bar_tv);// 与谁聊天
        listView = (ListView) findViewById(R.id.chat_list);
        listView.setCacheColorHint(0);
        LayoutInflater mynflater = LayoutInflater.from(context);
        listHead = mynflater.inflate(R.layout.chatlistheader, null);
        listHeadButton = (Button) listHead.findViewById(R.id.buttonChatHistory);
        listHeadButton.setOnClickListener(chatHistoryCk);
        listView.addHeaderView(listHead);
        listView.setAdapter(adapter);
        messageInputET = (PasteEditText) findViewById(R.id.chat_content);
        messageSendBtn = (Button) findViewById(R.id.chat_sendbtn);
        messageSendBtn.setVisibility(View.GONE);
        mMailAddressTV = (TextView) findViewById(R.id.tv_mail_address);//邮寄地址
        mFaceTabhost.setup();
        // TODO 改为带有选中效果 参考tab_bar_czs
        mFaceTabhost.addTab(mFaceTabhost.newTabSpec("face")
                .setIndicator(createTabIndicator(R.drawable.d_hehe))
                .setContent(R.id.tab_face));
        mFaceTabhost.addTab(mFaceTabhost.newTabSpec("gif")
                .setIndicator(createTabIndicator(R.drawable.section0_emotion0))
                .setContent(R.id.tab_gif));
        mDeleteChatTV = (TextView) findViewById(R.id.tv_delete_chat);//粉碎聊天
        mDeleteChatTV.setVisibility(View.VISIBLE);
//        View contentView = LayoutInflater.from(ChatActivity.this).inflate(
//                R.layout.chat_msg_do_popwindow, null);
//        mPopupWindow = new PopupWindow(contentView,
//                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
//        mPopupWindow.setTouchable(true);
    }

    private void initDate() {
        // mServiceType = getIntent().getIntExtra("serviceType", 0);
        user = ContacterManager.getByUserJid(to, XmppConnectionManager
                .getInstance().getConnection());
        if (null == user) {
            to_name = StringUtil.getUserNameByJid(to);
            // 找不到user，并且是聊天室的情况
            if (to.contains("conference")) {
                tvChatTitle.setText(to_name);
                to_name = "";
            }
        } else {
            to_name = StringUtil.getUserNameByJid(user.getJID());
            // to_name = user.getName() == null ? user.getJID() :
            // user.getName();
            if (to_name.contains("xiaoding") || to_name.contains("xiaodang")) {
                tvChatTitle.setText(user.getName());
            }
        }
        tvChatTitle.setText(title);
        if (!to_name.contains("xiaoding") && !to_name.contains("xiaodang")) {
            AjaxParams params = new AjaxParams();
            params.put("ids", to_name);
            MyLog.e("YUY", "聊天中查询个人信息的ID = " + to_name);
            requestNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            // isBusOrKeFu 0：普通用户；1：商家；2：客服
                            // busUserId //商家的UserId
                            // handlerId 客服的handlerId
                            try {
                                JSONArray jArray = new JSONArray(msg.obj.toString());
                                PersonHome mPersonHome = PersonHome.parse(jArray.get(0).toString());
                                String noteName = mPersonHome.getNoteName();
                                String nickName = mPersonHome.getNick();
                                String userName = mPersonHome.getUserName();
                                mUserName = CarFriendQuanUtils.showCarFriendName(noteName, nickName, userName);
                                if (TextUtils.isEmpty(title)) {
                                    tvChatTitle.setText(mUserName);
                                }
                                mIsBusOrKefu = String.valueOf(mPersonHome.getIsBusOrKeFu());
                                mBusinesId = String.valueOf(mPersonHome.getBusUserId());
                                mHandlerId = String.valueOf(mPersonHome.getHandlerId());
                                mUserTelephone = mPersonHome.getTelephone();
                                mBlack = String.valueOf(mPersonHome.getBlack());
                                if (mPersonHome.getIsBusOrKeFu() == Constant.CHAT_USER_ROLE.BUSINESS
                                        || mPersonHome.getIsBusOrKeFu() == Constant.CHAT_USER_ROLE.SERVER) {// 如果是商家或客服才显示出给TA下单
                                    mToOrderTV.setVisibility(View.VISIBLE);
                                    mPhoneTV.setVisibility(View.VISIBLE);
                                }
                                JSONObject jo = jArray.getJSONObject(0).getJSONObject("mailingAddress");
                                if (!TextUtils.isEmpty(jo.getString("receiver"))) {//判断联系人不为空
                                    mMailAddressTV.setVisibility(View.VISIBLE);//显示信息为空
                                    mMailAddressSB.append("联系人:").append(jo.getString("receiver")).append("\n");
                                    mMailAddressSB.append("联系电话:").append(jo.getString("phone")).append("\n");
                                    mMailAddressSB.append("邮寄地址:").append(jo.getString("region")).append(jo.getString("address")).append("\n");
                                    mMailAddressSB.append("邮编:").append(jo.getString("zipCode")).append("\n");
                                }
                                MyLog.e("YUY", "聊天人的名称 = " + tvChatTitle.getText().toString());
                                if (TextUtils.isEmpty(tvChatTitle.getText().toString())) {
                                    tvChatTitle.setText("null");
                                }
                                if ("2".equals(mPersonHome.getBusType())) {
                                    String str = "品牌-" + tvChatTitle.getText().toString();
                                    int roleStart = 0;
                                    int roleEnd = 3;
                                    int start = 4;
                                    int end = str.length();
                                    SpannableStringBuilder style = new SpannableStringBuilder(str);
                                    style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.b_pinpai)), roleStart, roleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    style.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                    tvChatTitle.setText(style);
                                    tvChatTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.pinpai), null, null, null);
                                } else if ("3".equals(mPersonHome.getBusType())) {
                                    String str = "会员-" + tvChatTitle.getText().toString();
                                    int roleStart = 0;
                                    int roleEnd = 3;
                                    int start = 4;
                                    int end = str.length();
                                    SpannableStringBuilder style = new SpannableStringBuilder(str);
                                    style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.f77b00)), roleStart, roleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    style.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                    tvChatTitle.setText(style);
                                    tvChatTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.huiyuan), null, null, null);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            break;

                        default:
                            break;
                    }
                }
            }, params, NetworkUtil.GET_USER_INFO, false, 1);
        }

        adapter = new MessageAdapter(ChatActivity.this, getMessages(), to);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
                .getInstance(), true, true)); // 设置滚动时不加载图片
    }

    private void initEvent() {
        mMailAddressTV.setOnClickListener(this);
        mFriendInfoIBtn.setOnClickListener(this);
        mSendVioceLL.setOnTouchListener(new PressToSpeakListen());
        mChatAddBtn.setOnClickListener(this);
        mFaceIV.setOnClickListener(this);
        mFaceCheckIV.setOnClickListener(this);
        mPictureTV.setOnClickListener(this);
        mCameraTV.setOnClickListener(this);
        mMyLocationTV.setOnClickListener(this);
        mToOrderTV.setOnClickListener(this);
        mPhoneTV.setOnClickListener(this);
        mDeleteChatTV.setOnClickListener(this);
        messageInputET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                if (!TextUtils.isEmpty(arg0)) {
                    mChatAddBtn.setVisibility(View.GONE);
                    messageSendBtn.setVisibility(View.VISIBLE);
                } else {
                    mChatAddBtn.setVisibility(View.VISIBLE);
                    messageSendBtn.setVisibility(View.GONE);
                }
                MyLog.i("YUY", "-------聊天输入---->>>" + arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                MyLog.i("YUY", "---------聊天输入-beforeTextChanged-----" + arg0);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                MyLog.i("YUY", "---------聊天输入--afterTextChanged------" + arg0);
            }
        });
        mChatRL.requestFocus();
        messageInputET.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mChatRL.setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    mChatRL.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        messageInputET.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mChatRL.setBackgroundResource(R.drawable.input_bar_bg_active);
                layout_more.setVisibility(View.GONE);
                layout_add.setVisibility(View.GONE);
                mFaceLL.setVisibility(View.GONE);
            }
        });
        messageInputET.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(
                        ChatActivity.this);
                dialog.setTitle("粘贴消息");
                dialog.setMessage("是否粘贴消息?");
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                messageInputET.setText(Tools
                                        .paste(ChatActivity.this));
                                arg0.dismiss();
                            }
                        });
                dialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                            }
                        });
                dialog.show();
                return false;
            }
        });
        messageSendBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String message = messageInputET.getText().toString();
                if ("".equals(message)) {
                    Toast.makeText(ChatActivity.this, "不能为空",
                            Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if (!TextUtils.isEmpty(mBlack)&&(mBlack.equals("2") || mBlack.equals("3"))) {
                            ToastUtil.toastShow(ChatActivity.this, "你已被对方拉黑，不能发送消息");
                        } else {
                            sendMessage(message);
                            messageInputET.setText("");
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            listView.setSelection(listView.getCount() - 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyLog.e("YUY", "消息发送失败");
                        messageInputET.setText(message);
                    }
                    closeInput();
                }
            }
        });

    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude() / 2700.0);
        else
            return 0;

    }

    private View createTabIndicator(int resId) {
        View tabIndicator = LayoutInflater.from(this).inflate(
                R.layout.face_tabhost_indicator, null);
        ImageView mFaceTabIV = (ImageView) tabIndicator
                .findViewById(R.id.tab_face_iv);
        mFaceTabIV.setImageResource(resId);
        return tabIndicator;
    }

    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        mChatRL.setVisibility(View.GONE);
        layout_more.setVisibility(View.GONE);
        layout_add.setVisibility(View.GONE);
        mSendVioceLL.setVisibility(View.VISIBLE);
        messageSendBtn.setVisibility(View.GONE);
        mChatAddBtn.setVisibility(View.VISIBLE);
        mKeyBordBtn.setVisibility(View.VISIBLE);
        mVioceSetBtn.setVisibility(View.GONE);

    }

    /**
     * 显示键盘图标
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        mChatRL.setVisibility(View.VISIBLE);
        layout_more.setVisibility(View.GONE);
        layout_add.setVisibility(View.GONE);
        messageInputET.requestFocus();
        mSendVioceLL.setVisibility(View.GONE);
        mVioceSetBtn.setVisibility(View.VISIBLE);
        mKeyBordBtn.setVisibility(View.GONE);
        if (TextUtils.isEmpty(messageInputET.getText())) {
            mChatAddBtn.setVisibility(View.VISIBLE);
            messageSendBtn.setVisibility(View.GONE);
        } else {
            mChatAddBtn.setVisibility(View.GONE);
            messageSendBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void receiveNewMessage(IMMessage message) {
        MyLog.i("YUY", "--收到新的消息--" + message.getContent());
        // adapter.notifyDataSetChanged();
        // adapter.refreshSelectLast();
        refreshUIWithNewMessage();

    }

    @Override
    protected void refreshMessage(List<IMMessage> messages) {
        refreshUI();
        // adapter.refresh();
        // adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {

            @Override
            public void run() {
                mRefreshHandler.sendEmptyMessage(5);
            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.menu_return_main_page:
                // intent.setClass(context, MainActivity.class);
                // startActivity(intent);
                // finish();
                break;
            case R.id.menu_relogin:
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
     * 点击进入聊天记录
     */
    private OnClickListener chatHistoryCk = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent in = new Intent(context, ChatHistoryActivity.class);
            in.putExtra("to", to);
            in.putExtra("userName", tvChatTitle.getText().toString());
            MyLog.e("YUY", "进入聊天记录的参数------------to = " + to + "  title = " + tvChatTitle.getText().toString());
            startActivity(in);
        }
    };

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!Tools.hasSdcard()) {
                        String st4 = getResources().getString(
                                R.string.Send_voice_need_sdcard_support);
                        Toast.makeText(ChatActivity.this, st4, Toast.LENGTH_SHORT)
                                .show();
                        return false;
                    }
                    mFileName = PATH + UUID.randomUUID().toString() + ".m4a";
                    File directory = new File(mFileName).getParentFile();
                    if (!directory.exists() && !directory.mkdirs()) {
                        String st4 = getResources().getString(
                                R.string.Send_voice_need_sdcard_support);
                        Toast.makeText(ChatActivity.this, st4, Toast.LENGTH_SHORT)
                                .show();
                        return false;
                    }
                    mRecordingContainerRL.setVisibility(View.VISIBLE);
                    mRecordingIV.setImageResource(R.drawable.record_animate_01);
                    mRecordingHint.setText(R.string.move_up_to_cancel);

                    // mVoiceRcdTimeTask = new VoiceRcdTimeTask(mRcdStartTime);
                    startVoice();

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    MyLog.e("YUY", "event.getY() = " + event.getY());
                    if (event.getY() < 0) {
                        mRecordingHint
                                .setText(getString(R.string.release_to_cancel));
                        mRecordingHint
                                .setBackgroundResource(R.drawable.recording_text_hint_bg);
                        mIfToSendRecorder = false;
                    } else {
                        mRecordingHint
                                .setText(getString(R.string.move_up_to_cancel));
                        mRecordingHint.setBackgroundColor(Color.TRANSPARENT);
                        mIfToSendRecorder = true;
                    }
                    // stopVoice();
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    mRecordingContainerRL.setVisibility(View.INVISIBLE);
                    stopVoice();

                    return true;
                default:
                    mRecordingContainerRL.setVisibility(View.INVISIBLE);
                    return false;
            }
        }
    }

    /**
     * 隐藏软键盘 hideSoftInputView
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this
                .getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_ibtn:
                Intent friendIntent = new Intent(ChatActivity.this,
                        PersonalHomeActivity2.class);
                if (null == user) {
                    to_name = StringUtil.getUserNameByJid(to);
                } else {
                    to_name = StringUtil.getUserNameByJid(user.getJID());
                }
                if (!TextUtils.isEmpty(to_name)) {
                    friendIntent.putExtra("userId", Integer.parseInt(to_name));
                    startActivity(friendIntent);
                }
                break;
            case R.id.btn_chat_add:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (layout_more.getVisibility() == View.GONE) {
                            layout_more.setVisibility(View.VISIBLE);
                            layout_add.setVisibility(View.VISIBLE);
                            mFaceLL.setVisibility(View.GONE);
                            hideSoftInputView();
                        } else {
                            if (mFaceLL.getVisibility() == View.VISIBLE) {
                                mFaceLL.setVisibility(View.GONE);
                                layout_more.setVisibility(View.VISIBLE);
                                layout_add.setVisibility(View.VISIBLE);
                            } else {
                                layout_more.setVisibility(View.GONE);
                            }

                        }
                        mFaceIV.setVisibility(View.VISIBLE);
                        mFaceCheckIV.setVisibility(View.GONE);
                    }
                });

                break;
            case R.id.face_iv:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (layout_more.getVisibility() == View.GONE) {
                            layout_more.setVisibility(View.VISIBLE);
                            layout_add.setVisibility(View.GONE);
                            mFaceLL.setVisibility(View.VISIBLE);
                            mFaceCheckIV.setVisibility(View.VISIBLE);
                            mFaceIV.setVisibility(View.GONE);
                            hideSoftInputView();
                        } else {
                            if (layout_add.getVisibility() == View.VISIBLE) {
                                mFaceLL.setVisibility(View.VISIBLE);
                                layout_more.setVisibility(View.VISIBLE);
                                mFaceCheckIV.setVisibility(View.VISIBLE);
                                mFaceIV.setVisibility(View.GONE);
                                layout_add.setVisibility(View.GONE);
                            } else {
                                layout_more.setVisibility(View.GONE);
                            }

                        }
                    }
                });

                break;
            case R.id.iv_emoticons_checked:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mFaceIV.setVisibility(View.VISIBLE);
                        mFaceCheckIV.setVisibility(View.GONE);
                        mFaceLL.setVisibility(View.GONE);
                        layout_more.setVisibility(View.GONE);
                        messageInputET.setFocusable(true);
                        messageInputET.setFocusableInTouchMode(true);
                        messageInputET.requestFocus();
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(messageInputET, 0);
                    }
                });
                break;
            case R.id.tv_picture:
                Intent intent = new Intent(ChatActivity.this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                startActivityForResult(intent, REQUEST_CODE_PICTURE);
//                selectPicFromLocal();
                break;
            case R.id.tv_camera:
                selectPicFromCamera();
                break;
            case R.id.tv_location:
                startActivityForResult(new Intent(ChatActivity.this,
                        BaiduMapActivity.class), REQUEST_CODE_LOCATION);
                break;
            case R.id.tv_order:// 下单
                toOrder();
                break;
            case R.id.tv_call:
                DialogUtil.call(mUserTelephone, ChatActivity.this);
                break;
            case R.id.tv_delete_chat://粉碎聊天
                MyLog.i("YUY", "--------------------------粉碎聊天");
                MessageManager.getInstance(context).delChatHisWithSb(to);
                message_pool.clear();
                adapter = new MessageAdapter(ChatActivity.this, message_pool, to);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listHeadButton.setVisibility(View.GONE);
                break;
            case R.id.tv_mail_address://发送邮寄地址
                try {
                    if (!TextUtils.isEmpty(mBlack)&&(mBlack.equals("2") || mBlack.equals("3"))) {
                        ToastUtil.toastShow(ChatActivity.this, "你已被对方拉黑，不能发送消息");
                    } else {
                        sendMessage(mMailAddressSB.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 向TA下单 区分是商家还是客服
     */
    private void toOrder() {

        if (Integer.valueOf(mIsBusOrKefu) == Constant.CHAT_USER_ROLE.BUSINESS
                || Integer.valueOf(mIsBusOrKefu) == Constant.CHAT_USER_ROLE.SERVER) {// 当前聊天的商家或客服
            MyLog.i("YUY", "登录 - 下单 -" + CacheTools.getUserData("businessId") + " ---" + mBusinesId);
            if (mBusinesId.equals(CacheTools.getUserData("businessId"))) {
                ToastUtil.toastShow(ChatActivity.this, "不能给自己下单");
                return;
            }
            Intent intent = new Intent(ChatActivity.this,
                    CollectReleaseOrderActivity.class);
            intent.putExtra("businessId", mBusinesId);
            intent.putExtra("handlerId", mHandlerId);
            intent.putExtra("isBusOrKefu", mIsBusOrKefu);
            startActivity(intent);
        } else {
            ToastUtil.toastShow(ChatActivity.this, "不是商家，不能给TA下单");
        }

    }

    @Override
    public boolean setViewValue(View view, Object data, String arg2) {
        if ((view instanceof ImageView) && (data instanceof Bitmap)) {
            ImageView imageView = (ImageView) view;
            Bitmap bmp = (Bitmap) data;
            imageView.setImageBitmap(bmp);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOCATION:
                    double latitude = data.getDoubleExtra("latitude", 0);
                    double longitude = data.getDoubleExtra("longitude", 0);
                    String locationAddress = data.getStringExtra("address");
                    if (locationAddress != null && !locationAddress.equals("")) {
                        sendLocationMsg(latitude, longitude, locationAddress);
                    } else {
                        String st = getResources().getString(
                                R.string.unable_to_get_loaction);
                        Toast.makeText(ChatActivity.this, st, Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
                case REQUEST_CODE_CAMERA:
                    try {

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inJustDecodeBounds = false;
                        Bitmap bmp = BitmapFactory.decodeFile(cameraFile.getPath(),
                                opts);
                        FileUtils.saveBitmapByPath(ImageUtils.comp(bmp),
                                cameraFile.getPath());
                        AjaxParams params = new AjaxParams();
                        params.put("file", cameraFile);
                        requestNet(new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case Constant.NET_DATA_SUCCESS:
                                        MyLog.i("YUY",
                                                "聊天上传图片成功 = " + msg.obj.toString());
                                        try {
                                            sendIMG(msg.obj.toString());
                                        } catch (Exception e) {
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }, params, NetworkUtil.UPLOADE_CAHT_IMG, false, 0);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
                case REQUEST_CODE_PICTURE:
                    // 发送本地图片
                    if (data != null) {
                        List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        for (String path : paths) {
                            MyLog.e("YUY", "上传图片的路径 = " + path);
                            uploadFileBase64(FileUtils.bitmapToString(path));
                        }
                    }
//                    if (data != null) {
//                        Uri selectedImage = data.getData();
//                        if (selectedImage != null) {
//                            sendPicByUri(selectedImage);
//                        }
//                    }
                    break;
                case REQUEST_CODE_VOICE:

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 发送位置信息
     *
     * @param latitude
     * @param longitude
     * @param locationAddress
     */
    private void sendLocationMsg(double latitude, double longitude,
                                 String locationAddress) {
        try {
            if (!TextUtils.isEmpty(mBlack)&&(mBlack.equals("2") || mBlack.equals("3"))) {
                ToastUtil.toastShow(ChatActivity.this, "你已被对方拉黑，不能发送消息");
            } else {
                sendMessage("#LOC" + locationAddress + "{" + latitude + ","
                        + longitude + "}" + "COL#");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);

    }

    /**
     * 发送图片消息
     *
     * @param fileName
     */
    private void sendIMG(String fileName) {

        try {
            if (!TextUtils.isEmpty(mBlack)&&(mBlack.equals("2") || mBlack.equals("3"))) {
                ToastUtil.toastShow(ChatActivity.this, "你已被对方拉黑，不能发送消息");
            } else {
                sendMessage("#IMG" + fileName + "GMI#");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);

    }

    /**
     * 照相获取图片
     */
    public void selectPicFromCamera() {
        if (Tools.hasSdcard() == false) {
            String st = getResources().getString(
                    R.string.sd_card_does_not_exist);
            Toast.makeText(getApplicationContext(), st, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        cameraFile = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/车当当", ChatUtils.setFileName());
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                        MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_PICTURE);
    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {
        // String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, null, null,
                null, null);
        String st8 = getResources().getString(R.string.cant_find_pictures);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory.decodeFile(picturePath, opts);
            String newPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/车当当" + ChatUtils.setFileName();
            FileUtils.saveBitmapByPath(ImageUtils.comp(bmp), newPath);
            uploade(new File(newPath));
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory
                    .decodeFile(selectedImage.getPath(), opts);
            String newPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/车当当" + ChatUtils.setFileName();
            FileUtils.saveBitmapByPath(ImageUtils.comp(bmp), newPath);
            uploade(new File(newPath));
        }
    }

    private void uploade(File file) {
        try {
            AjaxParams params = new AjaxParams();
            params.put("file ", file);
            requestNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            try {
                                sendIMG(msg.obj.toString());
                            } catch (Exception e) {
                            }
                            break;
                        default:
                            break;
                    }
                }

            }, params, NetworkUtil.UPLOADE_CAHT_IMG, false, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 开始录音
     */
    private void startVoice() {
        // 设置录音保存路径

        // Toast.makeText(getApplicationContext(), "开始录音", Toast.LENGTH_SHORT)
        // .show();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT); //设置音频源
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); //设置文件输出格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//设置音频编码方式
        mRecorder.setMaxDuration(MAX_LENGTH);
        mRecorder.setOutputFile(mFileName);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
            ToastUtil.toastShow(this, "录音失败，请检查是否开启相关权限");
        } catch (IOException e1) {
            ToastUtil.toastShow(this, "录音失败，请检查是否开启相关权限");
            e1.printStackTrace();
        } catch (Exception mE) {
            mE.printStackTrace();
            //   ToastUtil.toastShow(this, "录音失败，请检查是否开启相关权限");

            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("录音失败，请检查是否开启录音权限")
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                            try {
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })

                    .create().show();


        }
        mVoiceHandler.postDelayed(mPollTask, POLL_INTERVAL);
        mStartRecorderTime = System.currentTimeMillis()
                + mRcdVoiceStartDelayTime;
        mVoiceRcdTimeTask = new VoiceRcdTimeTask(mRcdStartTime);
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
            // mRcdVoiceStartDelayTime延迟运行任务 mRcdVoiceDelayTime周期性执行任务。
            mExecutor.scheduleAtFixedRate(mVoiceRcdTimeTask,
                    mRcdVoiceStartDelayTime, mRcdVoiceDelayTime,
                    TimeUnit.MILLISECONDS);
        }

    }

    private boolean mIfToSendRecorder = false;

    /**
     * 停止录音
     */
    private void stopVoice() {
        SystemClock.sleep(1000);// 为了解决播放不完的问题，因为QQ取消点击时又录音了1s左右
        mVoiceHandler.removeCallbacks(mPollTask);
        if (mExecutor != null && !mExecutor.isShutdown()) {
            mExecutor.shutdown();
            mExecutor = null;
        }
        try {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mEndRecorderTime = System.currentTimeMillis();
            int mVoiceTime = (int) ((mEndRecorderTime - mStartRecorderTime) / 1000);
            if (mIfToSendRecorder && mVoiceTime > 0.5) {
                uploadeVoice(new File(mFileName), mVoiceTime);
            } else {
                recordDefault("时间太短");
            }

        } catch (RuntimeException stopException) {

            stopException.printStackTrace();
            recordDefault("录音失败");
        } catch (Exception ep) {
            ep.printStackTrace();
        }

    }

    public void recordDefault(String reason) {
        mRecordingContainerRL.setVisibility(View.VISIBLE);
        mRecordingHint.setText(reason);
        mRecordingIV.setImageResource(R.drawable.record_too_short);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mRecordingContainerRL.setVisibility(View.INVISIBLE);
            }
        }, 1000);
        FileUtils.delFile(mFileName);
    }

    private void uploadeVoice(File file, final int voiceTime) {

        try {
            AjaxParams params = new AjaxParams();
            params.put("file", file);
            requestNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {

                        case Constant.NET_DATA_SUCCESS:
                            MyLog.i("YUY", "--------上传语音成功---" + msg.obj.toString());
                            sendVoice(msg.obj.toString(), voiceTime);
                            break;
                        case Constant.NET_DATA_FAIL:
                            ToastUtil.toastShow(ChatActivity.this, "发送语音失败");
                            break;
                        default:
                            break;
                    }
                }
            }, params, NetworkUtil.UPLOADE_CHAT_VOICE, false, 0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void sendVoice(String message, int voiceTime) {


        try {
            if (!TextUtils.isEmpty(mBlack)&&(mBlack.equals("2") || mBlack.equals("3"))) {
                ToastUtil.toastShow(ChatActivity.this, "你已被对方拉黑，不能发送消息");
            } else {
                sendMessage(mFileName + "#VOI" + message + "{" + voiceTime + "}"
                        + "IOV#");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (!TextUtils.isEmpty(mBlack)&&(mBlack.equals("2") || mBlack.equals("3"))) {
                    ToastUtil.toastShow(ChatActivity.this, "你已被对方拉黑，不能发送消息");
                } else {
                    sendMessage(message);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (VoicePlayClickListener.isPlaying
                && VoicePlayClickListener.currentPlayListener != null) {
            // 停止语音播放
            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }
    }

    public ListView getListView() {
        return listView;
    }

    public List<IMMessage> getMessage() {
        return getMessages();
    }

    private void refreshUIWithNewMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.refreshSelectLast();
            }
        });
    }

    private void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.refresh();
            }
        });
    }

    /**
     * 初始化表情
     */
    private void initFaceViewPager() {
        int pagesize = ExpressionUtil.getPagerCount(staticFacesList.size(),
                columns, rows);
        // 获取页数
        for (int i = 0; i < pagesize; i++) {
            faceViews.add(ExpressionUtil.viewPagerItem(this, i,
                    staticFacesList, columns, rows, messageInputET));
            LayoutParams params = new LayoutParams(16, 16);
            mDotsLayout.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(faceViews);
        mFaceViewPager.setAdapter(mVpAdapter);
        mDotsLayout.getChildAt(0).setSelected(true);
    }

    /**
     * 初始化表情
     */
    private void initGifViewPager() {
        int pagesize = ExpressionUtil.getPagerCount(staticGifsList.size(),
                gifColmns, gifRows);
        // 获取页数
        for (int i = 0; i < pagesize; i++) {
            gifViews.add(ExpressionUtil.viewPagerGifItem(this, i,
                    staticGifsList, gifColmns, gifRows, messageInputET,
                    mGifHandler));
            LayoutParams params = new LayoutParams(16, 16);
            mGifDotsLayout.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(gifViews);
        mGifViewPager.setAdapter(mVpAdapter);
        mGifDotsLayout.getChildAt(0).setSelected(true);
    }

    /**
     * 表情页切换时，底部小圆点
     *
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View layout = inflater.inflate(R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    /**
     * 表情页改变时，dots效果也要跟着改变
     */
    class PageChange implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(arg0).setSelected(true);
        }
    }

    public void sendGifMessage(String messageContent) {
        try {
            if (!TextUtils.isEmpty(mBlack)&&(mBlack.equals("2") || mBlack.equals("3"))) {
                ToastUtil.toastShow(ChatActivity.this, "你已被对方拉黑，不能发送消息");
            } else {
                sendMessage(messageContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadFileBase64(String file) {
        AjaxParams params = new AjaxParams();
        params.put("imgStr", file);
        params.put("file", "");
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("YUY", "上传图片成功");
                        sendIMG(msg.obj.toString());
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(ChatActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        ToastUtil.toastShow(ChatActivity.this, "上传出错了");
                        break;
                }
            }
        }, params, NetworkUtil.UPLOADE_CAHT_IMG, false, 0);
    }
}

