package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.FaceVPAdapter;
import com.chewuwuyou.app.adapter.YueDetailAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.YueDetailEntity;
import com.chewuwuyou.app.bean.YueDetailHeaderEntity;
import com.chewuwuyou.app.bean.YuePingItem;
import com.chewuwuyou.app.bean.ZanItem;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ExpressionUtil;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YueDetailActivity extends BaseActivity implements OnRefreshListener2<ListView> {
    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackIBtn;// 左键,返回按钮
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;// 标题
    @ViewInject(id = R.id.sub_header_bar_right_tv, click = "onAction")
    private TextView mHeaderBarRightTV;
    // 去评
    @ViewInject(id = R.id.yue_detail_to_ping_ll, click = "onAction")
    private LinearLayout mToPingLL;// 评价窗口
    @ViewInject(id = R.id.to_ping_et, click = "onAction")
    private EditText mToPingET;// 评价输入框
    @ViewInject(id = R.id.to_ping_send_btn, click = "onAction")
    private Button mToPingSendBtn;// 评价发送
    @ViewInject(id = R.id.to_ping_face_iv, click = "onAction")
    private ImageView mToPingFaceIV;// 表情
    @ViewInject(id = R.id.to_ping_keysoft_iv, click = "onAction")
    private ImageView mToPingKeysoftIV;// 软键盘
    // @ViewInject(id = R.id.to_ping_face_gv)
    // private GridView mToPingFaceGV;//表情列表
    // private FaceAdapter mFaceAdapter;
    private AlertDialog mDeleteYueDialog;
    // 展示数据
    private int mYueId;
    private String mYueZiji;
    private ArrayList<Object> mData;
    private YueDetailAdapter mAdapter;
    private PullToRefreshListView mPullToRefreshListView;
    private int mCurcor;// 翻页要用
    private boolean mIsRefreshing = false;// 上拉下拉要用

    // 展示大图要用
    @ViewInject(id = R.id.yue_container)
    private RelativeLayout mContainer;
    private HackyViewPager mExpandedImageViewPager;

    // 消息处理
    public static final int TOGGLE_ZAN = 111;// 赞或者取消赞一个动态
    public static final int PING_YUE = 112;// 评价一个动态
    public static final int REPLY_YUE_PING = 113;// 回复一个评论
    public static final int ADD_FACE_PIC = 114;// 添加表情
    public static final int EDIT_TIE = 115;
    public static final int REPLY_PING = 116;
    public static final int DELETE_PING = 117;
    private boolean mIsSetEmptyTV = false;
    private RelativeLayout mTitleHeight;// 标题布局高度

    private int columns = 7, rows = 3;// 表情图标每页6列4行
    private List<View> faceViews = new ArrayList<View>();// 每页显示的表情view
    private List<String> staticFacesList;// 表情列表
    private LinearLayout mDotsLayout;
    private ViewPager mFaceViewPager;
    private LayoutInflater mInflater;
    private LinearLayout mFaceLL;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case PING_YUE:// 评价一个圈文
                    mToPingKeysoftIV.setVisibility(View.GONE);
                    mToPingFaceIV.setVisibility(View.VISIBLE);
                    if (mFaceLL.getVisibility() == View.VISIBLE) {
                        mFaceLL.setVisibility(View.GONE);
                    }
                    showInputMethod();
                    break;
                case TOGGLE_ZAN:// 切换赞一个动态
                    toggleZan();
                    break;
                case REPLY_PING:// 评价一个PING
                    final YuePingItem ping = (YuePingItem) msg.obj;
                    mToPingKeysoftIV.setVisibility(View.GONE);
                    mToPingFaceIV.setVisibility(View.VISIBLE);
                    if (mFaceLL.getVisibility() == View.VISIBLE) {
                        mFaceLL.setVisibility(View.GONE);
                    }
                    mToPingET.setText("");
                    showInputMethod(ping);
                    break;
                case DELETE_PING:// 删除一个评价
                    YuePingItem item = (YuePingItem) msg.obj;
                    showDeletePingDialog(item);
                    break;
                case ADD_FACE_PIC:// 添加一个表情
                    SpannableString spannableString = (SpannableString) msg.obj;
                    Editable editable = mToPingET.getText();
                    int st = mToPingET.getSelectionStart();
                    int en = mToPingET.getSelectionEnd();
                    editable.replace(st, en, spannableString);
                    break;

                case Constant.NET_DATA_SUCCESS:
                    Toast.makeText(YueDetailActivity.this, "成功提交", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.NET_DATA_FAIL:
                    Toast.makeText(YueDetailActivity.this, ((DataError) msg.obj).getErrorMessage(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case EDIT_TIE:
                    setTheme(R.style.ActionSheetStyleIOS7);
                    ActionSheet menuView = new ActionSheet(YueDetailActivity.this);
                    menuView.setCancelButtonTitle("取 消");// before add items
                    if (mYueZiji.equals("1")) {// 自己发的帖子
                        menuView.addItems("删除");
                    } else {
                        menuView.addItems("举报");
                    }
                    menuView.setItemClickListener(new MenuItemClickListener() {

                        @Override
                        public void onItemClick(int itemPosition) {
                            switch (itemPosition) {
                                case 0:
                                    if (mYueZiji.equals("1")) {// 自己发的帖子
                                        showDeleteYueDialog(mYueId);
                                    } else {
                                        final EditText editText = new EditText(YueDetailActivity.this);
                                        new AlertDialog.Builder(YueDetailActivity.this).setTitle("请输入举报原因").setView(editText)
                                                .setPositiveButton("确定", new OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        AjaxParams params = new AjaxParams();
                                                        // 传入帖子id和uid
                                                        params.put("type", "2");
                                                        params.put("relateId", String.valueOf(mYueId));
                                                        params.put("reason", editText.getText().toString());
                                                        requestNet(new Handler() {
                                                            @Override
                                                            public void handleMessage(Message msg) {
                                                                super.handleMessage(msg);
                                                                switch (msg.what) {
                                                                    case Constant.NET_DATA_SUCCESS:
                                                                        Toast.makeText(YueDetailActivity.this, "已提交",
                                                                                Toast.LENGTH_SHORT).show();
                                                                        break;
                                                                    case Constant.NET_DATA_FAIL:
                                                                        Toast.makeText(YueDetailActivity.this, "举报失败，再试试",
                                                                                Toast.LENGTH_SHORT).show();
                                                                        break;
                                                                    default:
                                                                        break;
                                                                }
                                                            }
                                                        }, params, NetworkUtil.MAKE_ACCU, false, 0);
                                                    }
                                                }).setNegativeButton("取消", null).show();
                                    }
                                    break;

                                default:
                                    break;
                            }
                        }
                    });
                    menuView.setCancelableOnTouchMenuOutside(true);
                    menuView.showMenu();
                    break;
                default:
                    break;
            }
        }
    };
    private AlertDialog mDeletePingDialog;// 删评提示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_detail_layout);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
        initData();
        initEvent();
        getYueDetail(true);
    }


    protected void initView() {
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.yue_detail_lv);
        mExpandedImageViewPager = (HackyViewPager) findViewById(R.id.yue_detail_expanded_image_viewpager);
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mFaceLL = (LinearLayout) findViewById(R.id.face_ll);
        // 表情下小圆点
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
        mFaceViewPager = (ViewPager) findViewById(R.id.face_viewpager);
        mFaceViewPager.setOnPageChangeListener(new PageChange());
    }


    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mYueId = getIntent().getIntExtra("id", 0);
        mYueZiji = getIntent().getStringExtra("ziji");

        mHeaderTV.setText("活动详情");
        if (mYueZiji.equals("0")) {
            mHeaderBarRightTV.setVisibility(View.VISIBLE);
            mHeaderBarRightTV.setText("想参加");
        } else if (mYueZiji.equals("1")) {
            mHeaderBarRightTV.setVisibility(View.GONE);
        }
        staticFacesList = ExpressionUtil.initStaticFaces(this);// 初始化表情图
        initFaceViewPager();// 初始化表情
        ExpressionUtil.getFaceStrMap(YueDetailActivity.this);// 获取新的表情数据Map
        // mFaceAdapter = new FaceAdapter(YueDetailActivity.this, mHandler);
        // mToPingFaceGV.setAdapter(mFaceAdapter);

        if (mData == null) {
            mData = new ArrayList<Object>();
        }
        if (mAdapter == null) {
            mAdapter = new YueDetailAdapter(YueDetailActivity.this, mHandler, mData, mExpandedImageViewPager,
                    mContainer);
        }
        if (mPullToRefreshListView != null) {
            mPullToRefreshListView.setAdapter(mAdapter);
        }
        mToPingET.setHint("回复本活动");
    }


    protected void initEvent() {
        mPullToRefreshListView.setOnRefreshListener(this);
        // mPullToRefreshListView.setOnItemClickListener(new
        // OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> arg0, View arg1, final int
        // arg2, long arg3) {
        // if (arg2 > 1) {
        // final YuePingItem item = (YuePingItem) mData.get(arg2-1);
        // if (item == null) {
        // return;
        // }
        // if (item.getFriendId().equals(CacheTools.getUserData("userId"))) {
        // showDeletePingDialog(item);
        // } else {
        // showInputMethod(item);
        // }
        // }
        // }
        //
        // });
        mToPingET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(mToPingET.getText().toString())) {
                    mToPingSendBtn.setBackgroundResource(R.drawable.common_blue_btn_bg);
                    mToPingSendBtn.setText("确认");
                    mToPingSendBtn.setTextColor(getResources().getColor(R.color.white));
                    mToPingSendBtn.setClickable(true);
                } else {
                    mToPingSendBtn.setBackgroundResource(R.drawable.common_gray_btn_bg);
                    mToPingSendBtn.setText("发送");
                    mToPingSendBtn.setTextColor(getResources().getColor(R.color.to_ping_send_btn_default_color));
                    mToPingSendBtn.setClickable(false);
                }
            }
        });
    }

    // 弹出dialog删除一个评价
    private void showDeletePingDialog(final YuePingItem ping) {
        mDeletePingDialog = new AlertDialog.Builder(YueDetailActivity.this).setTitle("确定删除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delYuePing(ping);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        mDeletePingDialog.setCanceledOnTouchOutside(true);
        mDeletePingDialog.show();
    }

    private void getYueDetail(final boolean refresh) {
        if (refresh) {
            mCurcor = 0;
        }
        mPullToRefreshListView.setRefreshing();
        AjaxParams params = new AjaxParams();
        params.put("id", String.valueOf(mYueId));
        params.put("start", String.valueOf(mCurcor));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!mIsSetEmptyTV) {
                    TextView tv = new TextView(YueDetailActivity.this);
                    tv.setGravity(Gravity.CENTER);
                    tv.setText("没有详情");
                    tv.setTextColor(getResources().getColor(R.color.empty_text_color));
                    mPullToRefreshListView.setEmptyView(tv);
                    mIsSetEmptyTV = true;
                }
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mIsRefreshing = false;
                        mPullToRefreshListView.onRefreshComplete();
                        if (refresh) {
                            mPullToRefreshListView.onLoadMore();
                            YueDetailEntity response = YueDetailEntity.parse(msg.obj.toString());
                            if (response == null)
                                return;
                            mData.clear();
                            mData.add(response.getHeader());
                            mData.addAll(response.getYueping());
                            mAdapter.notifyDataSetChanged();
                            mCurcor = mData.size() - 1;
                            if (response.getYueping().size() < Constant.YUE_DETAIL_PAGE_SIZE)
                                mPullToRefreshListView.onLoadComplete();
                        } else {
                            // 是加载后面的评论，所以可直接
                            try {
                                JSONObject data = new JSONObject(msg.obj.toString());
                                List<YuePingItem> response = YuePingItem.parseList(data.getString("yueping").toString());
                                if (response == null) {
                                    return;
                                }
                                mData.addAll(response);
                                mAdapter.notifyDataSetChanged();
                                mCurcor = mData.size() - 1;
                                if (response.size() < Constant.YUE_DETAIL_PAGE_SIZE) {
                                    mPullToRefreshListView.onLoadComplete();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(YueDetailActivity.this, " JSONException e = " + e.getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                    default:
                        mPullToRefreshListView.onRefreshComplete();
                        mIsRefreshing = false;
                        break;
                }
                mToPingLL.setVisibility(View.GONE);
            }

        }, params, NetworkUtil.YUE_DETAIL, false, 1);
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv:
                // Intent intent = new Intent(YueDetailActivity.this,
                // YueApplyActivity.class);
                // intent.putExtra("id", mYueId);
                // startActivity(intent);

                AjaxParams params = new AjaxParams();
                params.put("yueId", String.valueOf(mYueId));
                requestNet(mHandler, params, NetworkUtil.YUE_APPLY, false, 0);
                break;
            case R.id.to_ping_send_btn:
                if (mToPingET.getTag(R.id.ping_et_key_yue_ping) != null) {
                    YuePingItem yuePing = ((YuePingItem) mToPingET.getTag(R.id.ping_et_key_yue_ping));
                    replyYuePing(yuePing);
                    mToPingET.setTag(R.id.ping_et_key_yue_ping, null);
                } else {
                    pingYue();
                }
                break;
            case R.id.to_ping_face_iv:
                mToPingFaceIV.setVisibility(View.GONE);
                mToPingKeysoftIV.setVisibility(View.VISIBLE);
                if (mFaceLL.getVisibility() == View.GONE)
                    mFaceLL.setVisibility(View.VISIBLE);
                hideKeyboard();
                break;
            case R.id.to_ping_keysoft_iv:
                mToPingKeysoftIV.setVisibility(View.GONE);
                mToPingFaceIV.setVisibility(View.VISIBLE);
                if (mFaceLL.getVisibility() == View.VISIBLE) {
                    mFaceLL.setVisibility(View.GONE);
                }
                showKeyboard();
                break;
            default:
                break;
        }
    }

    // 显示输入框同时弹出软键盘
    private void showInputMethod() {
        showKeyboard();
        mToPingLL.setVisibility(View.VISIBLE);
        mToPingET.setText("");
        mToPingET.setHint("回复本活动");
        mToPingET.requestFocus();
    }

    // 显示输入框同时弹出软键盘
    private void showInputMethod(YuePingItem yuePing) {
        showKeyboard();
        mToPingLL.setVisibility(View.VISIBLE);
        mToPingET.setTag(R.id.ping_et_key_yue_ping, yuePing);
        mToPingET.setText("");
        // 这样写是效率很低，后面改进
        mToPingET.setHint(getString(R.string.quan_reply_ping, CarFriendQuanUtils.showCarFriendName(yuePing)));
        mToPingET.requestFocus();
    }

    // 删除一条帖评
    private void delYuePing(final YuePingItem yuePing) {
        AjaxParams params = new AjaxParams();
        params.put("yuePingId", String.valueOf(yuePing.getId()));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Toast.makeText(YueDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        ((YueDetailHeaderEntity) mData.get(0))
                                .setYuepingcnt(((YueDetailHeaderEntity) mData.get(0)).getYuepingcnt() - 1);
                        mData.remove(yuePing);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case Constant.NET_DATA_FAIL:
                        Toast.makeText(YueDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

        }, params, NetworkUtil.DEL_YUE_PING, false, 0);
    }

    // 切换赞状态
    private void toggleZan() {

        AjaxParams params = new AjaxParams();
        // 传入动态id和uid
        params.put("yueId", String.valueOf(mYueId));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:

                        // 提示切换赞成功，改变赞人数和赞图标样式
                        ZanItem yueZan = ZanItem.parse(msg.obj.toString());
                        // 如果以前没赞过，那现在要显示赞
                        if (yueZan != null) {

                            if (((YueDetailHeaderEntity) mData.get(0)).getZaned().equals("0")) {
                                ((YueDetailHeaderEntity) mData.get(0)).setZaned(1 + "");
                                ((YueDetailHeaderEntity) mData.get(0))
                                        .setYuezancnt(((YueDetailHeaderEntity) mData.get(0)).getYuezancnt() + 1);
                                ((YueDetailHeaderEntity) mData.get(0)).getYuezan().add(yueZan);
                                Toast.makeText(YueDetailActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                            } else {
                                ((YueDetailHeaderEntity) mData.get(0)).setZaned(0 + "");
                                ((YueDetailHeaderEntity) mData.get(0))
                                        .setYuezancnt(((YueDetailHeaderEntity) mData.get(0)).getYuezancnt() - 1);
                                ((YueDetailHeaderEntity) mData.get(0)).getYuezan().remove(yueZan);
                                for (ZanItem z : ((YueDetailHeaderEntity) mData.get(0)).getYuezan()) {
                                    if (z.getId() == yueZan.getId()) {
                                        ((YueDetailHeaderEntity) mData.get(0)).getYuezan().remove(z);
                                        break;
                                    }
                                }
                                Toast.makeText(YueDetailActivity.this, "取消赞", Toast.LENGTH_SHORT).show();
                            }

                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                    case Constant.NET_DATA_NULL:
                        // 如果以前赞过，那现在取消赞
                        ((YueDetailHeaderEntity) mData.get(0)).setZaned(0 + "");
                        ((YueDetailHeaderEntity) mData.get(0))
                                .setYuezancnt(((YueDetailHeaderEntity) mData.get(0)).getYuezancnt() - 1);
                        for (ZanItem z : ((YueDetailHeaderEntity) mData.get(0)).getYuezan()) {
                            if (z.getFriendId().equals(CacheTools.getUserData("userId"))) {
                                ((YueDetailHeaderEntity) mData.get(0)).getYuezan().remove(z);
                                break;
                            }
                        }

                        break;
                    case Constant.NET_DATA_FAIL:
                        // 提示切换赞失败
                        Toast.makeText(YueDetailActivity.this, "失败，请重新尝试", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }, params, NetworkUtil.YUE_TOGGLE_ZAN, false, 0);
    }

    // 评论一个圈文
    private void pingYue() {
        AjaxParams params = new AjaxParams();
        // 传入动态id和uid
        params.put("yueyueId", String.valueOf(mYueId));
        params.put("content", String.valueOf(mToPingET.getText().toString()));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Toast.makeText(YueDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        YuePingItem ping = YuePingItem.parse(msg.obj.toString());

                        if (ping != null) {
                            ((YueDetailHeaderEntity) mData.get(0)).setPinged(1 + "");
                            ((YueDetailHeaderEntity) mData.get(0))
                                    .setYuepingcnt(((YueDetailHeaderEntity) mData.get(0)).getYuepingcnt() + 1);
                            mData.add(ping);
                        }
                        mAdapter.notifyDataSetChanged();
                        mPullToRefreshListView.getRefreshableView().setSelection(mData.size());
                        hideKeyboard();
                        mToPingLL.setVisibility(View.GONE);
                        break;
                    case Constant.NET_DATA_FAIL:
                        Toast.makeText(YueDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }, params, NetworkUtil.PING_YUE, false, 0);
    }

    // 回复一个评论
    private void replyYuePing(YuePingItem yuePing) {
        AjaxParams params = new AjaxParams();
        // 传入动态id和uid
        params.put("yueyueId", String.valueOf(mYueId));
        params.put("yuePingId", String.valueOf(yuePing.getId()));
        params.put("content", String.valueOf(mToPingET.getText().toString()));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        // 提示评论发送成功
                        // 评论+1
                        // 评论的iv是否要变样式(已评论)
                        Toast.makeText(YueDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        YuePingItem ping = YuePingItem.parse(msg.obj.toString());

                        if (ping != null) {
                            ((YueDetailHeaderEntity) mData.get(0)).setPinged(1 + "");
                            ((YueDetailHeaderEntity) mData.get(0))
                                    .setYuepingcnt(((YueDetailHeaderEntity) mData.get(0)).getYuepingcnt() + 1);
                            mData.add(ping);
                        }
                        mAdapter.notifyDataSetChanged();
                        hideKeyboard();
                        mToPingLL.setVisibility(View.GONE);
                        break;
                    case Constant.NET_DATA_FAIL:
                        Toast.makeText(YueDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }, params, NetworkUtil.REPLY_YUE_PING, false, 0);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getYueDetail(true);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getYueDetail(false);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    private void showDeleteYueDialog(final int yueId) {
        mDeleteYueDialog = new AlertDialog.Builder(YueDetailActivity.this).setTitle("确定删除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delYue(yueId);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        mDeleteYueDialog.setCanceledOnTouchOutside(true);
        mDeleteYueDialog.show();
    }

    private void delYue(int yueId) {
        AjaxParams params = new AjaxParams();
        params.put("yueId", String.valueOf(yueId));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Toast.makeText(YueDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        finishActivity();
                        // mData.remove(position);
                        break;
                    case Constant.NET_DATA_FAIL:
                        Toast.makeText(YueDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

        }, params, NetworkUtil.DEL_YUE, false, 0);

    }

    /**
     * 初始化表情
     */
    private void initFaceViewPager() {
        int pagesize = ExpressionUtil.getPagerCount(staticFacesList.size(), columns, rows);
        // 获取页数
        for (int i = 0; i < pagesize; i++) {
            faceViews.add(ExpressionUtil.viewPagerItem(this, i, staticFacesList, columns, rows, mToPingET));
            LayoutParams params = new LayoutParams(16, 16);
            mDotsLayout.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(faceViews);
        mFaceViewPager.setAdapter(mVpAdapter);
        mDotsLayout.getChildAt(0).setSelected(true);
    }

    /**
     * 表情页切换时，底部小圆点
     *
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View layout = mInflater.inflate(R.layout.dot_image, null);
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

}