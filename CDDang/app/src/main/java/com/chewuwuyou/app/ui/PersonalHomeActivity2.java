package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.PersonPicGridAdapter;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.bean.TuItem;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.callback.PullFragmentCallBack;
import com.chewuwuyou.app.eventbus.MessageEvent;
import com.chewuwuyou.app.fragment.ActivityFragment;
import com.chewuwuyou.app.fragment.FriendCircleFragment;
import com.chewuwuyou.app.fragment.InvitationFragment;
import com.chewuwuyou.app.utils.Bimp;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.MyGridView;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.Mode;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshScrollView;
import com.chewuwuyou.eim.manager.ContacterManager;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.app.transition_view.activity.RongChatActivity;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imlib.model.Conversation;

public class PersonalHomeActivity2 extends BaseFragmentActivity implements
        OnRefreshListener2<ScrollView>, View.OnClickListener {
    public static final String FRIENDID = "user_id";
    public static final String NEW_FRIENDID = "new_user_id";
    public static final String IS_YANZHENG = "is_yanzheng";
    public static final String FRIEND_GROUP_ID = "friend_group_id";
    public static Fragment[] mFragments;
    private static final int UPDATE_DATA = 111;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private RadioGroup tabs;
    private PullToRefreshScrollView scrollView;
    private boolean mIsRefreshing = false;// 翻页要用

    private MyGridView mUserPicPullGV;// 图片
    private RelativeLayout mContainer;// 头部
    private TextView mTitleTV;// title
    private ImageButton mBackIBtn;// 返回
    private TextView mTextView_more;//更多
    private ImageView myPortrait;// 头像
    private ImageView topBg;// 背景
    //  private TextView mUpdatePersonInfoIBtn;// 修改个人信息及查看他人帖子等
    private HackyViewPager mExpandedImageViewPager;
    private TextView remark;// 备注
    private TextView mood;// 心情
    private TextView hobby;// 爱好
    private TextView car;// 爱车
    private TextView userName;// 用户名
    private TextView age;// 年龄
    private TextView constellation;// 星座
    private TextView profession;// 职业
    private TextView city;// 城市
    private RelativeLayout mTitleHeight;// 标题布局高度
    private RelativeLayout topRl;// 头部
    private LinearLayout qr_ll, remark_ll;
    //   private Button mBeizhuBtn;
    private View hScrollView;

    private PopupWindow mMingXiPopWindow = null;
    private View mMingXiPopView;
    // private Button friend_group;
    // private Button black_him_btn, jubao_btn;


    private Button mButton_add;// 添加车友
    private Button mButton_chat;// 跟他聊天
    private View mLayout_add_chat;//聊天，加好友 的layout


    private boolean isAdd = false;

    private List<TuItem> mPicList;// 存储照片墙的所有图片,使用bitmap来进行默认存储新增
    private PersonPicGridAdapter adapter;
    private PersonHome mPersonHome;
    private int pagerNum = 0;// 用于标志当前页数

//    private PopupWindow mPopWindow;//更多
//    private View mPopView;//view
//    private TextView mTextview_lahei;//拉黑
//    private TextView mTextView_delete;//删除好友
//    private TextView mTextView_fenzu;//分组

    private FrameLayout mFrameLayout;
    private TextView mTextView_state;//被拉黑后不显示对方的圈子活动等信息

    private String mFriendId;//聊天好友的id
    private String new_mFriendId;//新的好友ID
    private String mFriendGroupId;//聊天好友属于哪个分组
    private int userId_int;//有些地方传的 int类型。。。。
    private String userId_string;//有些是string

    private boolean isFriend;

    private int yanzheng = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    try {
                        JSONArray jArray = new JSONArray(msg.obj.toString());
                        Log.e("--", "" + msg.obj.toString());
                        mPersonHome = PersonHome.parse(jArray.get(0).toString());
                        mFriendId = mPersonHome.getUserId();
                        if (String.valueOf(mPersonHome.getUserId()).equals(
                                CacheTools.getUserData("userId"))) {// 自己
                            mTextView_more.setVisibility(View.VISIBLE);
                            mTextView_more.setText("编辑");
                            qr_ll.setVisibility(View.VISIBLE);
                            remark_ll.setVisibility(View.GONE);

                            MyLog.i("自己");
                        } else if ("1".equals(mPersonHome.getFriend())) {// 是好友
                            MyLog.i("好友");
                            mTextView_more.setVisibility(View.VISIBLE);
                            mTextView_more.setText("更多");
                            isFriend = true;
                            relation();//判读是否是黑名单用户

                            mButton_chat.setVisibility(View.VISIBLE);
                            mButton_add.setVisibility(View.GONE);

                            qr_ll.setVisibility(View.GONE);
                            remark_ll.setVisibility(View.VISIBLE);
                        } else if ("0".equals(mPersonHome.getFriend())) {// 不是好友
                            MyLog.i("不是好友");
                            mTextView_more.setText("更多");
                            isShenqin();

                            isFriend = false;
                            mTextView_more.setVisibility(View.VISIBLE);
                            relation();//判读是否是黑名单用户

                            mButton_chat.setVisibility(View.VISIBLE);
                            mButton_add.setVisibility(View.VISIBLE);

                            qr_ll.setVisibility(View.GONE);
                            remark_ll.setVisibility(View.GONE);//隐藏备注栏
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updateUI();
                    break;
            }
        }
    };

    //申请验证信息
    private void isShenqin() {


        if ("1".equals(mPersonHome.getIsRequestFriend())) {

            mButton_add.setText("通过验证");
            //   remark.setVisibility(View.GONE);
        } else {
            mButton_add.setText("加为好友");
            //    remark.setVisibility(View.VISIBLE);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_home);
        initView();
        initEvent();
    }

    @SuppressWarnings("deprecation")
    protected void initView() {
        mFriendId = getIntent().getStringExtra(FRIENDID);
        new_mFriendId = getIntent().getStringExtra(NEW_FRIENDID);

        userId_int = getIntent().getIntExtra("userId", 0);
        userId_string = getIntent().getStringExtra("userId");

        if (TextUtils.isEmpty(new_mFriendId)) {
            new_mFriendId = userId_int == 0 ? userId_string : String.valueOf(userId_int);
        }

        mFriendGroupId = getIntent().getStringExtra(FRIEND_GROUP_ID);
        yanzheng = getIntent().getIntExtra(IS_YANZHENG, 0);

        tabs = (RadioGroup) findViewById(R.id.tabs);
        mLayout_add_chat = findViewById(R.id.ll_bottom);
        //   mUpdatePersonInfoIBtn = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        scrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView);
        mUserPicPullGV = (MyGridView) findViewById(R.id.person_pic_pull);
        mContainer = (RelativeLayout) findViewById(R.id.container);
        mExpandedImageViewPager = (HackyViewPager) findViewById(R.id.expanded_image_viewpager);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mTextView_more = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mTextView_more.setOnClickListener(this);
        mTextView_more.setVisibility(View.GONE);
        mTextView_more.setText(" ");
        remark = (TextView) findViewById(R.id.remark);
        mood = (TextView) findViewById(R.id.car_friend_mood_tv);
        hobby = (TextView) findViewById(R.id.hobby);
        car = (TextView) findViewById(R.id.car);
        userName = (TextView) findViewById(R.id.user_name);
        age = (TextView) findViewById(R.id.age);
        constellation = (TextView) findViewById(R.id.constellation);
        profession = (TextView) findViewById(R.id.profession);
        city = (TextView) findViewById(R.id.city);
        myPortrait = (ImageView) findViewById(R.id.my_portrait);
        topBg = (ImageView) findViewById(R.id.topBg);
        topRl = (RelativeLayout) findViewById(R.id.rl);
        remark_ll = (LinearLayout) findViewById(R.id.remark_ll);
        qr_ll = (LinearLayout) findViewById(R.id.qr_ll);

        mButton_chat = (Button) findViewById(R.id.chat);
        mButton_add = (Button) findViewById(R.id.add_friend);//当是荣验证信息进来的时候，可以通过好友


        hScrollView = findViewById(R.id.hScrollView);

        mMingXiPopView = getLayoutInflater().inflate(
                R.layout.ming_xi_pop_window, null);
        this.mMingXiPopWindow = new PopupWindow(mMingXiPopView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.mMingXiPopWindow.setBackgroundDrawable(new BitmapDrawable());
        this.mMingXiPopWindow.setFocusable(true);
        this.mMingXiPopWindow.setOutsideTouchable(true);

        mFragments = new Fragment[3];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_service);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_news);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragment_query);

        onclickSwitch(0);// 设置初始显示第一个页面
        // tab事件，切换页面
        tabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_invitation:
                        onclickSwitch(0);
                        break;
                    case R.id.tab_ring:
                        onclickSwitch(1);
                        break;
                    case R.id.tab_activity:
                        onclickSwitch(2);
                        break;
                }
            }
        });
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        scrollView.setMode(Mode.BOTH);
        scrollView.setOnRefreshListener(this);
        mFrameLayout = (FrameLayout) findViewById(R.id.layout);
        mTextView_state = (TextView) findViewById(R.id.personal_state);

        if (yanzheng == 1) {
            mButton_add.setText("通过验证");
            remark.setVisibility(View.GONE);
        } else {
            mButton_add.setText("加为好友");
            remark.setVisibility(View.VISIBLE);
        }
        // 动态高度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        int height = (50 * screenW) / 75;
        LayoutParams params = (LayoutParams) topRl.getLayoutParams();
        params.height = height;
        topRl.setLayoutParams(params);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) topBg.getLayoutParams();
        layoutParams.height = height;
        topBg.setLayoutParams(layoutParams);
    }

    /**
     * 获取车友信息数据
     */
    private void getDate(int userId) {
        AjaxParams params = new AjaxParams();
        params.put("ids", TextUtils.isEmpty(new_mFriendId) ? String.valueOf(userId) : new_mFriendId);
        MyLog.e("YUY", "请求个人信息的userId = " + userId);
        requestNet(mHandler, params, NetworkUtil.GET_USER_INFO, false, 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    protected void initData() {
        // mCarModels = CarModel.parseBrands(getFromAssets("mobile_models"));
        if (getIntent().getIntExtra("skip", 0) == 1) {
            getDate(Integer.valueOf(CacheTools.getUserData("userId")));
        } else if (getIntent().getIntExtra("skip", 0) == 106) {
            getDate(Integer.valueOf(getIntent().getStringExtra("userId")));
        } else {

            getDate(getIntent().getIntExtra("userId", 0));
        }


    }

    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        // mUpdatePersonInfoIBtn.setOnClickListener(this);
        mButton_chat.setOnClickListener(this);
        mButton_add.setOnClickListener(this);
        this.remark_ll.setOnClickListener(this);
        // this.jubao_btn.setOnClickListener(this);
        // this.black_him_btn.setOnClickListener(this);

        // this.friend_group.setOnClickListener(this);
        qr_ll.setOnClickListener(this);

        for (int i = 0; i < mFragments.length; i++) {// 设置回调 用于与Fragment通信
            ((FragmentCallBackBuilder) mFragments[i])
                    .setFragmentCallBack(fragmentCallBack);
        }
    }

    private void updateUI() {
        if (!TextUtils.isEmpty(mPersonHome.getImageBack())) {
            // ImageUtils.displayImage(mPersonHome.getImageBack(),
            // mPersonPicBgIV, 0);
            ImageUtils.displayImage(mPersonHome.getImageBack(), topBg, 0,
                    mOutMetrics.widthPixels, mOutMetrics.widthPixels,
                    ScalingLogic.CROP);
        } else {
            topBg.setImageResource(R.drawable.bg_defaultbg);
        }

        mPicList = mPersonHome.getUrls();
        if (mPersonHome.getUrls().size() > 9) {
            mPicList = mPersonHome.getUrls().subList(0, 9);
        } else {
            mPicList = mPersonHome.getUrls();
        }
        List<TuItem> tuItems = new ArrayList<>();
        if (mPicList != null && mPicList.size() > 1) {
            for (int i = 1; i < mPicList.size(); i++) {
                tuItems.add(mPicList.get(i));
            }
        }
        adapter = new PersonPicGridAdapter(this, tuItems, mUserPicPullGV,
                mExpandedImageViewPager, mContainer);
        mUserPicPullGV.setAdapter(adapter);
        mTitleTV.setText(CarFriendQuanUtils.showCarFriendName(
                mPersonHome.getNoteName(), mPersonHome.getNick(),
                mPersonHome.getUserName()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(mPersonHome.getAge());
        if (mPersonHome.getSex().equals("0")) {
            Drawable drawable = getResources().getDrawable(R.drawable.man);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2,
                    drawable.getMinimumHeight() / 2);
            age.setCompoundDrawables(drawable, null, null, null);
        } else if (mPersonHome.getSex().equals("1")) {
            Drawable drawable = getResources().getDrawable(R.drawable.woman);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2,
                    drawable.getMinimumHeight() / 2);
            age.setCompoundDrawables(drawable, null, null, null);
        } else {
            buffer.append("  性别不详");
        }
        age.setText(buffer.toString());
        String pro = mPersonHome.getPro();
        Log.e("000", "职业" + pro);
        profession.setText(TextUtils.isEmpty(pro) ? "职业" : pro);
        userName.setText(mPersonHome.getNick());
        // .setText(mPersonHome.getDDNumber())
        String note = mPersonHome.getNoteName();
        remark.setText(note == null || note.equals("") ? "未设置" : note);// 备注
        String sign = mPersonHome.getSign();
        mood.setText(sign == null || sign.equals("") ? "..." : sign);// 心情
        String hobbyStr = mPersonHome.getHobby();
        hobby.setText(hobbyStr == null || hobbyStr.equals("") ? "未设置"
                : hobbyStr);// 爱好
        String carBrand = mPersonHome.getCarBrand();
        car.setText(carBrand == null || carBrand.equals("") ? "未设置" : carBrand);// 爱车
        String star = mPersonHome.getStarsit();
        constellation.setText(star == null || star.equals("") ? "星座" : star);// 星座
        String loc = mPersonHome.getLocation();
        city.setText(loc == null || loc.equals("") ? "城市" : loc);// 城市

        if (mPicList.size() > 0) {
            ImageUtils.displayUserHeadImage(mPicList.get(0).getUrl(),
                    myPortrait, 8);
            hScrollView.setVisibility(View.VISIBLE);
        } else if (mPicList.size() <= 1) {
            hScrollView.setVisibility(View.GONE);
        }
        ((InvitationFragment) mFragments[0]).setPersonHome(mPersonHome);
        ((FriendCircleFragment) mFragments[1]).setPersonHome(mPersonHome);
        ((ActivityFragment) mFragments[2]).setPersonHome(mPersonHome);
        if (adapter.getCount() <= 0) {
            hScrollView.setVisibility(View.GONE);
        } else {
            hScrollView.setVisibility(View.VISIBLE);
        }
        // 动态高度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        int height = (50 * screenW) / 75;
        LayoutParams params = (LayoutParams) topRl.getLayoutParams();
        params.height = height;
        topRl.setLayoutParams(params);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) topBg.getLayoutParams();
        layoutParams.height = height;
        topBg.setLayoutParams(layoutParams);
    }


    /**
     * 切换
     *
     * @param id
     */
    private void onclickSwitch(int id) {
        pagerNum = id;
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
        tabs.setFocusable(true);
        tabs.setFocusableInTouchMode(true);
        tabs.requestFocus();
        switch (id) {
            case 0:
                fragmentTransaction.show(mFragments[0]).commit();// 服务
                break;
            case 1:
                fragmentTransaction.show(mFragments[1]).commit();// 消息
                break;
            case 2:
                fragmentTransaction.show(mFragments[2]).commit();// 车圈
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if (getIntent().getIntExtra("skip", 0) == 1) {
        // getDate(Integer.valueOf(CacheTools.getUserData("userId")));
        // mUpdatePersonInfoIBtn.setVisibility(View.VISIBLE);
        // mUpdatePersonInfoIBtn.setImageResource(R.drawable.btn_addnew);
        // }
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv:
                if (mPersonHome == null) {
                    ToastUtil.toastShow(this, "数据获取失败导致无法跳转，请确认网络是否连接");
                } else if (String.valueOf(mPersonHome.getUserId()).equals(
                        CacheTools.getUserData("rongUserId")) || String.valueOf(mPersonHome.getUserId()).equals(
                        CacheTools.getUserData("userId"))) {
                    Intent intent = new Intent(this, PersonInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.PERSONINFO_SER, mPersonHome);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, UPDATE_DATA);
                } else {

                    if (isFriend)
                        showFriendDialog();
                    else
                        showNotFriendDialog();

                }
                break;
            case R.id.remark_ll:// 设置备注


                remark();


                break;

            case R.id.delete_friend_btn:// 删除好友
                if (getIntent().getIntExtra("userId", 0) != 0) {
                    showDeleteDialog(getIntent().getExtras().getInt("userId")
                            + "@iz232jtyxeoz");
                    mMingXiPopWindow.dismiss();
                }

                break;
            case R.id.qr_ll:
                if (mPersonHome != null) {
                    Intent barcodeIntent = new Intent(this,
                            PersonMyBarCodeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.PERSONINFO_SER, mPersonHome);
                    barcodeIntent.putExtras(bundle);
                    startActivity(barcodeIntent);
                } else {
                    ToastUtil.toastShow(this, "数据获取失败导致无法跳转，请确认网络是否连接");
                }
                break;
            case R.id.chat:
                if (mPersonHome == null) {
                    Toast.makeText(this, "此用户无效!", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent = new Intent(this, RongChatActivity.class);
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PRIVATE);
                intent.putExtra(RongChatMsgFragment.KEY_TARGET, TextUtils.isEmpty(new_mFriendId) ? mFriendId : new_mFriendId);
                startActivity(intent);


                break;
            case R.id.add_friend:


                if (mButton_add.getText().toString().contains("加为"))
                    addFriend();
                else
                    tongGuo();

                break;
            case R.id.item_personl_friend_lahei:


                break;

            case R.id.item_personl_friend_delete:

                new AlertDialog.Builder(this) //

                        .setTitle("提示")
                        .setMessage("将联系人“" + mTitleTV.getText().toString() + "”删除,同时删除与该联系人的聊天记录")
                        .setCancelable(true)
                        .setPositiveButton("" +
                                "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                delete();


                            }
                        })

                        .create().show();

                break;
            case R.id.item_personl_friend_group:

                fenzu();


                break;
            default:
                break;
        }
    }

    private void tongGuo() {


        final ProgressDialog m = DialogUtil.showProgressDialog(PersonalHomeActivity2.this);

        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("friendId", new_mFriendId);
        NetworkUtil.postMulti(NetworkUtil.AGREE_FRIEND, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                m.dismiss();

//                FinalDb db = FinalDb.create(PersonalHomeActivity2.this);
//                NewFriend mProgressDialog=db.findById()
//
//                notice.setAddState(NewFriend.AGREE);
//                db.save(notice);


                try {
                    JSONObject jsonObject = new JSONObject(s);

                    ErrorCodeUtil.doErrorCode(PersonalHomeActivity2.this, jsonObject.getInt("code"), jsonObject.getString("message"));

                    if (jsonObject.getInt("code") == 0) {

                        mButton_add.setVisibility(View.GONE);
                        mButton_chat.setVisibility(View.VISIBLE);
                        initData();

                    } else {

                        ToastUtil.toastShow(PersonalHomeActivity2.this,
                                jsonObject.getString("message"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                m.dismiss();
                ToastUtil.toastShow(PersonalHomeActivity2.this, strMsg);

            }
        });


    }

    //加好友
    private void addFriend() {

        View v = LayoutInflater.from(PersonalHomeActivity2.this).inflate(R.layout.view_edittext, null);
        final EditText mcontent = (EditText) v.findViewById(R.id.input);
        mcontent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

        new AlertDialog.Builder(PersonalHomeActivity2.this)
                .setTitle("验证信息")
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final ProgressDialog m = ProgressDialog.show(PersonalHomeActivity2.this, null, "请稍后……", false, true);
                        m.show();

                        // TODO Auto-generated method stub
                        AjaxParams params = new AjaxParams();


                        params.put("userId", CacheTools.getUserData("rongUserId"));
                        params.put("friendId", new_mFriendId);
                        params.put("groupId", "1");
                        params.put("remarks", mcontent.getText().toString());


                        new NetworkUtil().postMulti(NetworkUtil.ADD_FRIEND, params, new AjaxCallBack<String>() {
                            @Override
                            public void onSuccess(String t) {
                                // TODO Auto-generated method stub
                                super.onSuccess(t);

                                m.cancel();
                                try {
                                    JSONObject jsonObject = new JSONObject(t);
                                    ErrorCodeUtil.doErrorCode(PersonalHomeActivity2.this, jsonObject.getInt("code"), jsonObject.getString("message"));

                                    if (jsonObject.getInt("code") == 0) {
                                        Toast.makeText(PersonalHomeActivity2.this, R.string.wait_friend_agree, Toast.LENGTH_SHORT).show();

                                    } else {
                                        ToastUtil.toastShow(PersonalHomeActivity2.this,
                                                jsonObject.getString("message"));

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                m.cancel();
                            }
                        });


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                    }
                })
                .create().show();


    }

    //备注
    private void remark() {


        Intent mIntent = new Intent(this, BeiZhuActivity.class);
        mIntent.putExtra(BeiZhuActivity.FRIEND_ID, new_mFriendId);
        startActivity(mIntent);


//        final ProgressDialog md = ProgressDialog.show(PersonalHomeActivity2.this, null, "请稍后……", false, true);
//
//        View view = LayoutInflater.from(this).inflate(R.layout.view_edittext, null);
//        final EditText mcontent = (EditText) view.findViewById(R.id.input);
//        mcontent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
//        mcontent.setHint("请设置对方的备注名");
//        new AlertDialog.Builder(this)
//                .setTitle("验证信息")
//                .setView(view)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        AjaxParams params = new AjaxParams();
//                        params.put("userId", CacheTools.getUserData("rongUserId"));
//                        params.put("friendId", new_mFriendId);
//                        params.put("name", mcontent.getText().toString());
//                        NetworkUtil.postMulti(NetworkUtil.ALTER_FRIEND_NAME, params, new AjaxCallBack<String>() {
//                            @Override
//                            public void onSuccess(String s) {
//                                super.onSuccess(s);
//                                MyLog.i("success");
//                                ToastUtil.toastShow(PersonalHomeActivity2.this, "修改备注成功");
//                                md.dismiss();
//                                remark.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        PersonalHomeActivity2.this.finish();
//                                    }
//                                }, 1000);
//                            }
//
//                            @Override
//                            public void onFailure(Throwable t, int errorNo, String strMsg) {
//                                super.onFailure(t, errorNo, strMsg);
//                                ToastUtil.toastShow(PersonalHomeActivity2.this, "修改备注失败");
//                                md.dismiss();
//                            }
//                        });
//
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(@NonNull DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//
//                    }
//                })
//                .create().show();


    }

    private void fenzu() {


        Intent mIntent = new Intent(this, FriendFenzuActivity.class);
        mIntent.putExtra(FRIEND_GROUP_ID, mFriendGroupId);
        mIntent.putExtra(FRIENDID, mFriendId);
        this.startActivity(mIntent);


    }

    ProgressDialog mProgressDialog;

    //删除某个联系人
    private void delete() {


        new AlertDialog.Builder(this) //

                .setTitle("提示")
                .setMessage("将联系人“" + mTitleTV.getText().toString() + "”删除，同时删除与该联系人的聊天记录")
                .setCancelable(false)
                .setPositiveButton("" +
                        "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        AjaxParams params = new AjaxParams();
                        params.put("userId", CacheTools.getUserData("rongUserId"));
                        params.put("friendId", mFriendId);

                        NetworkUtil.postMulti(NetworkUtil.DELETE_FRIEND, params, new AjaxCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                super.onSuccess(s);


                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    ErrorCodeUtil.doErrorCode(PersonalHomeActivity2.this, jsonObject.getInt("code"), jsonObject.getString("message"));


                                    if (jsonObject.getInt("code") == 0) {
                                        mProgressDialog = DialogUtil.showProgressDialog(PersonalHomeActivity2.this);
                                        ToastUtil.toastShow(PersonalHomeActivity2.this, "删除成功");

                                        EventBus.getDefault().post(new MessageEvent(NEW_FRIENDID, FRIEND_GROUP_ID));
                                        finishActivity();
                                    } else {
                                        ToastUtil.toastShow(PersonalHomeActivity2.this, jsonObject.getString("message"));

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                mProgressDialog.dismiss();

                                ToastUtil.toastShow(PersonalHomeActivity2.this, strMsg);
                            }
                        });


//                        RongApi.removeConversation(notice.getConversationType(), notice.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
//                            @Override
//                            public void onSuccess(Boolean aBoolean) {
//                                EventBus.getDefault().post(new ClearMessagesBean(notice.getConversationType(), notice.getTargetId()));
//                            }
//
//                            @Override
//                            public void onError(RongIMClient.ErrorCode errorCode) {
//                                Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
//                            }
//                        });


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .create().show();


    }


    private void lahei() {


        new AlertDialog.Builder(this) //

                .setTitle("提示")
                .setMessage("加入黑名单后，你将收不到TA的消息，并且不能互看对方信息")
                .setCancelable(true)
                .setPositiveButton("" +
                        "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AjaxParams params = new AjaxParams();
                        params.put("userId", CacheTools.getUserData("rongUserId"));
                        params.put("friendId", new_mFriendId);

                        NetworkUtil.postMulti(NetworkUtil.LAHEI_FIREND, params, new AjaxCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                super.onSuccess(s);

                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    ErrorCodeUtil.doErrorCode(PersonalHomeActivity2.this, jsonObject.getInt("code"), jsonObject.getString("message"));

                                    if (jsonObject.getInt("code") == 0) {

                                        mProgressDialog = DialogUtil.showProgressDialog(PersonalHomeActivity2.this);
                                        ToastUtil.toastShow(PersonalHomeActivity2.this, "已加入黑名单");

                                        mString_lahei = "解除黑名单";

                                        EventBus.getDefault().post(new MessageEvent(NEW_FRIENDID, FRIEND_GROUP_ID));
                                        mLayout_add_chat.setVisibility(View.GONE);
                                        finishActivity();
                                    } else {
                                        ToastUtil.toastShow(PersonalHomeActivity2.this, jsonObject.getString("message"));

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                }
                                ToastUtil.toastShow(PersonalHomeActivity2.this, strMsg);
                            }
                        });


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .create().show();


    }

    //隐藏对方的动态
    private void hideActivity() {
        mTextView_state.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);
        mLayout_add_chat.setVisibility(View.GONE);
    }

    //显示对方的动态
    private void showActivity() {
        mTextView_state.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);
        mLayout_add_chat.setVisibility(View.VISIBLE);
    }


    private void removeLahei() {

        mProgressDialog = DialogUtil.showProgressDialog(PersonalHomeActivity2.this);
        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("friendId", new_mFriendId);
        params.put("groupId", "1");

        NetworkUtil.postMulti(NetworkUtil.REMOVE_FIREND_TO_ZU, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                mProgressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    ErrorCodeUtil.doErrorCode(PersonalHomeActivity2.this, jsonObject.getInt("code"), jsonObject.getString("message"));


                    ToastUtil.toastShow(PersonalHomeActivity2.this, "已移出黑名单");
                    mString_lahei = "加入黑名单";
                    EventBus.getDefault().post(new MessageEvent(NEW_FRIENDID, FRIEND_GROUP_ID));
                    finishActivity();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                mProgressDialog.dismiss();
                ToastUtil.toastShow(PersonalHomeActivity2.this, "网络错误");
            }
        });


//                    }
//                })
//
//                .create().show();
    }

    //好友关系
    private void relation() {
//        AjaxParams params = new AjaxParams();
//        params.put("other", String.valueOf(mPersonHome.getUserId()));
//        params.put("me", CacheTools.getUserData("userId"));
//
//        requestNet(new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {
//                    case Constant.NET_DATA_SUCCESS:
//                        MyLog.i(msg.obj.toString());
//                        try {
//                            JSONObject mProgressDialog = new JSONObject(msg.obj.toString());
//
//                            int i = mProgressDialog.getInt("code");
//                            if (i == 1) {
//                                mTextview_lahei.setText("移除黑名单");
//                                mTextView_state.setText("您已把他加入黑名单，不能查看对方的动态");
//                                mTextView_state.setVisibility(View.VISIBLE);
//                                mLayout_add_chat.setVisibility(View.GONE);//隐藏聊天按钮
//                            } else {
//                                mTextView_state.setVisibility(View.GONE);
//                                mTextview_lahei.setText("加入黑名单");
//                                mLayout_add_chat.setVisibility(View.VISIBLE);
//                            }
//
//                        } catch (JSONException mE) {
//                            mE.printStackTrace();
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }, params, NetworkUtil.IS_BLACKFRIEND, false, 0);

        if ("1".equals(mPersonHome.getBlack())) {
            //自己拉黑别人
            MyLog.i("我拉黑了别人");
            hideActivity();
            mString_lahei = "解除黑名单";
            mTextView_state.setText("您已把他加入黑名单，不能查看对方的动态");
            mLayout_add_chat.setVisibility(View.GONE);//隐藏聊天加好友按钮

        } else if ("2".equals(mPersonHome.getBlack())) {
            //自己被拉黑
            MyLog.i("别人拉黑了我");
            mString_lahei = "加入黑名单";
            hideActivity();
            mTextView_state.setText("您被对方加入黑名单，不能查看对方的动态");
            mLayout_add_chat.setVisibility(View.GONE);//隐藏聊天加好友框

        } else {


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UPDATE_DATA:
                // if (resultCode == RESULT_OK) {
                // getDate(Integer.valueOf(CacheTools.getUserData("userId")));
//                initData();
                // }
                break;
            default:
                break;
        }
    }


    private void showDeleteDialog(final String jid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                getResources().getString(R.string.delete_user_confim))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // api删除
                                try {
                                    ContacterManager.deleteUser(jid);
                                    mButton_chat.setVisibility(View.GONE);
                                    mButton_add
                                            .setVisibility(View.VISIBLE);

//                                    mUpdatePersonInfoIBtn
//                                            .setVisibility(View.GONE);
                                    // 删除数据库
                                    NoticeManager.getInstance(
                                            PersonalHomeActivity2.this)
                                            .delNoticeHisWithSb(jid);
                                    MessageManager.getInstance(
                                            PersonalHomeActivity2.this)
                                            .delChatHisWithSb(jid);

                                } catch (XMPPException e) {
                                    MyLog.e("YUY", e.toString());
                                }
                                dialog.dismiss();

                            }
                        })
                .setNegativeButton(getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });
        builder.show();

    }

    /**
     * 修改备注
     *
     * @param jid
     */
    private void setNotename(final String jid) {
        final EditText name_input = new EditText(this);
        name_input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        name_input.setHint("输入备注");
        new AlertDialog.Builder(this).setTitle("修改备注").setView(name_input)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = name_input.getText().toString();
                        if (!"".equals(name)) {
                            ContacterManager.setNickname(jid, name,
                                    XmppConnectionManager.getInstance()
                                            .getConnection());
                        }

                    }
                }).setNegativeButton("取消", null).show();
    }

    private FragmentCallBack fragmentCallBack = new FragmentCallBack() {

        @Override
        public void callback(int pager, Object obj) {
            switch (pager) {
                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
            }
            // 千万别忘了告诉控件刷新完毕了哦！
            mIsRefreshing = false;
            scrollView.onRefreshComplete();
        }
    };

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {// 刷新
        if (!mIsRefreshing) {
            mIsRefreshing = true;
            initData();
            ((PullFragmentCallBack) mFragments[pagerNum]).pullDown();
        } else {
            scrollView.onRefreshComplete();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {// 加载
        if (!mIsRefreshing) {
            mIsRefreshing = true;
            ((PullFragmentCallBack) mFragments[pagerNum]).pullUp();
        } else {
            scrollView.onRefreshComplete();
        }

    }


    String mString_lahei = "加入黑名单"; //拉黑字符串


    //是好友，弹出此dialog
    public void showFriendDialog() {

        String[] strs = new String[]{"好友分组", "删除好友", mString_lahei};
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(this);
        menuView.setCancelButtonTitle("取 消");// before add items

        menuView.addItems(strs);
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {

                if (itemPosition == 0)
                    fenzu();
                else if (itemPosition == 1)
                    delete();
                else {

                    if (mString_lahei.contains("加入"))
                        lahei();
                    else
                        removeLahei();
                }

            }
        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();

    }

    //不是好友，弹出此dialog
    public void showNotFriendDialog() {

        String[] strs = new String[]{mString_lahei};
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(this);
        menuView.setCancelButtonTitle("取 消");// before add items

        menuView.addItems(strs);
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {


                if (mString_lahei.contains("加入"))//加入黑名单
                    lahei();
                else
                    removeLahei();//移除黑名单

            }
        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();

    }

}
