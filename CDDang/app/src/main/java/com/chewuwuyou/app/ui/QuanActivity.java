package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.FaceVPAdapter;
import com.chewuwuyou.app.adapter.SNSAdapter;
import com.chewuwuyou.app.bean.QuanItem;
import com.chewuwuyou.app.bean.QuanPingItem;
import com.chewuwuyou.app.bean.QuanPingReplyItem;
import com.chewuwuyou.app.bean.QuanTuItem;
import com.chewuwuyou.app.bean.QuanZanItem;
import com.chewuwuyou.app.callback.FilterEnterActionListener;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ChatInputUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.ExpressionUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.RemoveEmojiWatcher;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.viewcache.QuanItemViewCache;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @describe:圈的AC
 * @author:XH
 * @created:
 */
public class QuanActivity extends CDDBaseActivity implements OnRefreshListener2<ListView> {

    // header bar
    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackIBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;
    @ViewInject(id = R.id.sub_header_bar_right_tv, click = "onAction")
    private TextView mHeaderBarRightIBtn;

    // 去评
    @ViewInject(id = R.id.quan_to_ping_ll, click = "onAction")
    private LinearLayout mToPingLL;
    @ViewInject(id = R.id.to_ping_et, click = "onAction")
    private EditText mToPingET;
    @ViewInject(id = R.id.to_ping_send_btn, click = "onAction")
    private Button mToPingSendBtn;
    @ViewInject(id = R.id.to_ping_face_iv, click = "onAction")
    private ImageView mToPingFaceIV;
    @ViewInject(id = R.id.to_ping_keysoft_iv, click = "onAction")
    private ImageView mToPingKeysoftIV;
    // @ViewInject(id = R.id.to_ping_face_gv)
    // private GridView mToPingFaceGV;
    // private FaceAdapter mFaceAdapter;

    private String mOtherId = null;
    private String mOtherName = null;
    private List<QuanItem> mData;
    private QuanAdapter mAdapter;
    private PullToRefreshListView mPullToRefreshListView;
    private int mCurcor;
    private boolean mIsRefreshing = false;

    // 展示大图要用
    @ViewInject(id = R.id.quan_container)
    private RelativeLayout mContainer;
    private HackyViewPager mExpandedImageViewPager;

    // 消息处理
    public static final int TOGGLE_ZAN = 111;// 赞或者取消赞一个动态
    public static final int PING_QUAN = 112;// 评价一个动态
    public static final int REPLY_QUAN_PING = 113;// 回复一个评论
    public static final int SHOW_DEL_QUAN_PING_DIALOG = 115;// 删除一个圈评
    public static final int ADD_FACE_PIC = 116;// 添加表情
    private RelativeLayout mTitleHeight;// 标题布局高度
    private String mBackImg;
    private String mHeadImg;
    private boolean mIsSetEmptyTV = false;

    private int columns = 7, rows = 3;// 表情图标每页6列4行
    private List<View> faceViews = new ArrayList<View>();// 每页显示的表情view
    private List<String> staticFacesList;// 表情列表
    private LinearLayout mDotsLayout;
    private ViewPager mFaceViewPager;
    private LayoutInflater mInflater;
    private LinearLayout mFaceLL;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            QuanItem quan;
            QuanPingItem quanPing;
            Bundle bundle;

            switch (msg.what) {
                case PING_QUAN:// 评价一个圈文
                    quan = (QuanItem) msg.obj;
                    showInputMethod(quan);
                    break;
                case TOGGLE_ZAN:// 切换赞一个动态
                    quan = (QuanItem) msg.obj;
                    toggleZan(quan);
                    break;
                case SHOW_DEL_QUAN_PING_DIALOG:
                    bundle = msg.getData();
                    quan = (QuanItem) bundle.getSerializable(Constant.QUAN_MSG_BUNDLE.KEY_QUAN);
                    quanPing = (QuanPingItem) bundle.getSerializable(Constant.QUAN_MSG_BUNDLE.KEY_QUAN_PING);
                    showDeletePingDialog(quan, quanPing);
                    break;
                case REPLY_QUAN_PING:
                    bundle = msg.getData();
                    quan = (QuanItem) bundle.getSerializable(Constant.QUAN_MSG_BUNDLE.KEY_QUAN);
                    quanPing = (QuanPingItem) bundle.getSerializable(Constant.QUAN_MSG_BUNDLE.KEY_QUAN_PING);
                    showInputMethod(quan, quanPing);
                    break;
                case ADD_FACE_PIC:// 添加一个表情
                    SpannableString spannableString = (SpannableString) msg.obj;// 文本框显示表情

                    Editable editable = mToPingET.getText();

                    int st = mToPingET.getSelectionStart();
                    int en = mToPingET.getSelectionEnd();
                    editable.replace(st, en, spannableString);
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
        setContentView(R.layout.quan_layout);
        mOtherId = getIntent().getStringExtra("other_id");
        mOtherName = getIntent().getStringExtra("other_name");
        initView();
        initData();
        initEvent();
        getAllQuan(true);
    }

    @Override
    protected void initView() {
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.quan_lv);
        mExpandedImageViewPager = (HackyViewPager) findViewById(R.id.quan_expanded_image_viewpager);
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mFaceLL = (LinearLayout) findViewById(R.id.face_ll);
        // 表情下小圆点
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
        mFaceViewPager = (ViewPager) findViewById(R.id.face_viewpager);
        mFaceViewPager.setOnPageChangeListener(new PageChange());
        mToPingET.addTextChangedListener(new RemoveEmojiWatcher(mToPingET));
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断

        if (mOtherName != null) {
            mHeaderTV.setText(new StringBuilder().append(mOtherName).append("的心情"));
        } else {
            mHeaderTV.setText("好友车圈");
            mHeaderBarRightIBtn.setVisibility(View.VISIBLE);
            mHeaderBarRightIBtn.setText("发布");
        }
        staticFacesList = ExpressionUtil.initStaticFaces(this);// 初始化表情图
        initFaceViewPager();// 初始化表情
        ExpressionUtil.getFaceStrMap(QuanActivity.this);// 获取新的表情数据Map
        // mFaceAdapter = new FaceAdapter(QuanActivity.this, mHandler);
        // mToPingFaceGV.setAdapter(mFaceAdapter);

        if (mData == null) {
            mData = new ArrayList<QuanItem>();
        }
        if (mAdapter == null) {
            mAdapter = new QuanAdapter(QuanActivity.this, mHandler, mData, mExpandedImageViewPager, mContainer);
        }

        if (mPullToRefreshListView != null) {
            mPullToRefreshListView.setAdapter(mAdapter);
        }
        mToPingET.setHint("我也来说一句");

    }

    @Override
    protected void initEvent() {
        mPullToRefreshListView.setOnRefreshListener(this);
        mToPingET.setOnEditorActionListener(new FilterEnterActionListener());
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

        mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mToPingLL.setVisibility(View.GONE);
                mToPingET.setText("");
                mToPingET.setHint("我也来说一句");
                mToPingKeysoftIV.setVisibility(View.GONE);
                mToPingFaceIV.setVisibility(View.VISIBLE);
                mFaceLL.setVisibility(View.GONE);
                hideKeyboard();

            }
        });
    }

    private QuanItem quan;

    public void onAction(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            // 退出
            case R.id.sub_header_bar_left_ibtn:
                finish();
                break;
            // 新建一个心情
            case R.id.sub_header_bar_right_tv:
                intent.setClass(this, AddQuanActivity.class);
                startActivity(intent);
                break;
            // 点击发送按钮时发布评论
            case R.id.to_ping_send_btn:
                if (mToPingET.getTag(R.id.ping_et_key_quan_ping) != null) {
                    QuanItem quan = ((QuanItem) mToPingET.getTag(R.id.ping_et_key_quan));
                    QuanPingItem quanPing = ((QuanPingItem) mToPingET.getTag(R.id.ping_et_key_quan_ping));
                    replyQuanPing(quan, quanPing);
                    mToPingET.setTag(R.id.ping_et_key_quan, null);
                    mToPingET.setTag(R.id.ping_et_key_quan_ping, null);
                } else {
                    if (((QuanItem) mToPingET.getTag(R.id.ping_et_key_quan)) != null)
                        quan = ((QuanItem) mToPingET.getTag(R.id.ping_et_key_quan));
                    pingQuan(quan);
                    mToPingET.setTag(R.id.ping_et_key_quan, null);
                }
                break;
            case R.id.to_ping_face_iv:
                mToPingFaceIV.setVisibility(View.GONE);
                mToPingKeysoftIV.setVisibility(View.VISIBLE);
                if (mFaceLL.getVisibility() == View.GONE) {
                    mFaceLL.setVisibility(View.VISIBLE);
                }
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
    private void showInputMethod(QuanItem quan) {
        showKeyboard();
        mToPingLL.setVisibility(View.VISIBLE);
        mToPingET.setText("");
        mToPingET.setHint("我也来说一句");
        mToPingET.setTag(R.id.ping_et_key_quan, quan);
        mToPingET.requestFocus();
    }

    // 显示输入框同时弹出软键盘
    private void showInputMethod(QuanItem quan, QuanPingItem quanPing) {
        showKeyboard();
        mToPingLL.setVisibility(View.VISIBLE);
        mToPingET.setText("");
        mToPingET.setTag(R.id.ping_et_key_quan, quan);
        mToPingET.setTag(R.id.ping_et_key_quan_ping, quanPing);
        mToPingET.setHint(getString(R.string.quan_reply_ping, CarFriendQuanUtils.showCarFriendName(quanPing)));
        // 这样写是效率很低，后面改进

        mToPingET.requestFocus();
    }

    // 切换赞状态
    private void toggleZan(final QuanItem quan) {

        AjaxParams params = new AjaxParams();
        // 传入动态id和uid
        params.put("quanWenId", String.valueOf(quan.getId()));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        // 提示切换赞成功，改变赞人数和赞图标样式
                        QuanZanItem quanZan = QuanZanItem.parse(msg.obj.toString());
                        // 如果以前没赞过，那现在要显示赞
                        if (quanZan != null) {
                            if (quan.getZaned().equals("0")) {
                                quan.setZaned(1 + "");
                                quan.setQuanzancnt(quan.getQuanzancnt() + 1);
                                quan.getQuanzan().add(quanZan);
                                Toast.makeText(QuanActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                            } else {
                                quan.setZaned(0 + "");
                                quan.setQuanzancnt(quan.getQuanzancnt() - 1);
                                quan.getQuanzan().remove(quanZan);
                                for (QuanZanItem z : quan.getQuanzan()) {
                                    if (z.getId() == quanZan.getId()) {
                                        quan.getQuanzan().remove(z);
                                        break;
                                    }
                                }
                                Toast.makeText(QuanActivity.this, "取消赞", Toast.LENGTH_SHORT).show();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                    case Constant.NET_DATA_NULL:
                        // 如果以前赞过，那现在取消赞
                        quan.setZaned(0 + "");
                        quan.setQuanzancnt(quan.getQuanzancnt() - 1);
                        for (QuanZanItem z : quan.getQuanzan()) {
                            if (z.getFriendId().equals(CacheTools.getUserData("userId"))) {
                                quan.getQuanzan().remove(z);
                                break;
                            }
                        }

                        break;
                    case Constant.NET_DATA_FAIL:
                        // 提示切换赞失败
                        Toast.makeText(QuanActivity.this, "失败，请重新尝试", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }, params, NetworkUtil.QUAN_TOGGLE_ZAN, false, 0);
    }

    // 评论一个圈文
    private void pingQuan(final QuanItem quan) {
        AjaxParams params = new AjaxParams();
        // 传入动态id和uid
        params.put("quanWenId", String.valueOf(quan.getId()));
        params.put("content", String.valueOf(mToPingET.getText().toString()));
        requestNet(new Handler() {

            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (msg.what) {
                            case Constant.NET_DATA_SUCCESS:
                                QuanPingItem ping = QuanPingItem.parse(msg.obj.toString());
                                if (ping != null) {
                                    quan.setPinged(1 + "");
                                    quan.setQuanpingcnt(quan.getQuanpingcnt() + 1);
                                    quan.getQuanping().add(ping);
                                }
                                mAdapter.notifyDataSetChanged();
                                hideKeyboard();
                                mToPingLL.setVisibility(View.GONE);
                                break;
                            case Constant.NET_DATA_FAIL:
                                Toast.makeText(QuanActivity.this, "评论失败,请重试...", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        }, params, NetworkUtil.PING_QUAN, false, 0);
    }

    // 回复一个评论
    private void replyQuanPing(final QuanItem quan, QuanPingItem quanPing) {
        AjaxParams params = new AjaxParams();
        // 传入动态id和uid
        params.put("quanWenId", String.valueOf(quan.getId()));
        params.put("quanPingId", String.valueOf(quanPing.getId()));

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

                        QuanPingItem ping = QuanPingItem.parse(msg.obj.toString());

                        if (ping != null) {
                            quan.setPinged(1 + "");
                            quan.setQuanpingcnt(quan.getQuanpingcnt() + 1);
                            quan.getQuanping().add(ping);
                        }
                        mAdapter.notifyDataSetChanged();
                        hideKeyboard();
                        mToPingLL.setVisibility(View.GONE);
                        break;
                    case Constant.NET_DATA_FAIL:
                        Toast.makeText(QuanActivity.this, "评失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }, params, NetworkUtil.REPLY_QUAN_PING, false, 0);
    }

    // 获得所有圈文
    private void getAllQuan(final boolean refresh) {
        String request_url;
        if (refresh) {
            mCurcor = 0;
        }
        AjaxParams params = new AjaxParams();
        if (mOtherId != null) {
            params.put("otherId", mOtherId);
            request_url = NetworkUtil.GET_MY_QUAN;
        } else {
            request_url = NetworkUtil.ALL_QUAN;
        }
        params.put("start", String.valueOf(mCurcor));

        mPullToRefreshListView.setRefreshing();
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (!mIsSetEmptyTV) {
                    TextView tv = new TextView(QuanActivity.this);
                    tv.setGravity(Gravity.CENTER);
                    tv.setText("没有动态");
                    tv.setTextColor(getResources().getColor(R.color.empty_text_color));
                    mPullToRefreshListView.setEmptyView(tv);
                    mIsSetEmptyTV = true;
                }

                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mIsRefreshing = false;
                        mPullToRefreshListView.onRefreshComplete();
                        try {
                            JSONObject data = new JSONObject(msg.obj.toString());
                            mBackImg = data.getString("imageBack");
                            mHeadImg = data.getString("headImg");
                            List<QuanItem> response = QuanItem.parseList(data.getString("quan").toString());
                            if (response != null) {
                                if (refresh) {
                                    mPullToRefreshListView.onLoadMore();
                                    mData.clear();
                                }
                                mData.addAll(response);
                                mAdapter.notifyDataSetChanged();
                                mCurcor = mData.size();
                                if (response.size() < Constant.QUAN_PAGE_SIZE) {
                                    mPullToRefreshListView.onLoadComplete();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    default:
                        mPullToRefreshListView.onRefreshComplete();
                        mIsRefreshing = false;
                        break;
                }
            }

        }, params, request_url, false, 1);
    }

    private void delQuanPing(final QuanItem quan, final QuanPingItem quanPing) {
        AjaxParams params = new AjaxParams();
        params.put("quanPingId", String.valueOf(quanPing.getId()));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Toast.makeText(QuanActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        quan.setQuanpingcnt(quan.getQuanpingcnt() - 1);
                        quan.getQuanping().remove(quanPing);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case Constant.NET_DATA_FAIL:
                        Toast.makeText(QuanActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

        }, params, NetworkUtil.DEL_QUAN_PING, false, 0);
    }

    // 弹出dialog删除一个评价
    private void showDeletePingDialog(final QuanItem quan, final QuanPingItem quanPing) {
        mDeletePingDialog = new AlertDialog.Builder(QuanActivity.this).setTitle("确定删除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delQuanPing(quan, quanPing);
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

    @SuppressWarnings("rawtypes")
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getAllQuan(true);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getAllQuan(false);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    public class QuanAdapter extends SNSAdapter implements OnClickListener {

        private List<QuanItem> mData;
        private Handler mHandler;
        private Map<String, String> mFaceCharacterMap;

        public QuanAdapter(Activity context, Handler handler, List<QuanItem> data, HackyViewPager viewPager,
                           View container) {
            super(context, viewPager, container);
            this.mData = data;

            this.mHandler = handler;
            this.mFaceCharacterMap = JsonUtil.getFaceStrMap(mContext);
            mTuWidth = (mOutMetrics.widthPixels
                    - 3 * mContext.getResources().getDimensionPixelSize(R.dimen.quan_padding)
                    - mContext.getResources().getDimensionPixelSize(R.dimen.quan_avatar_width)
                    - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.quan_tu_interval)) / 3;
            mTuHeight = mTuWidth;
        }

        @Override
        public int getCount() {
            return mData.size() <= 0 ? 1 : mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            QuanItemViewCache viewCache = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.quan_item, null);
                viewCache = new QuanItemViewCache(convertView);
                convertView.setTag(viewCache);
            } else {
                viewCache = (QuanItemViewCache) convertView.getTag();
            }
            if (position == 0) {
                viewCache.getmQuanBackgroundRL().setVisibility(View.VISIBLE);
                viewCache.getmQuanBackgroundRL().setOnClickListener(this);

                if (mBackImg != null) {
                    ImageUtils.displayImage(QuanActivity.this, mBackImg, viewCache.getmQuanBackgroundIV(), 0,
                            mOutMetrics.widthPixels, mOutMetrics.widthPixels, ScalingLogic.CROP,
                            R.drawable.bg_defaultbg, R.drawable.bg_defaultbg);
                }
                if (!TextUtils.isEmpty(mHeadImg)) {
                    ImageUtils.displayImage(QuanActivity.this, mHeadImg, viewCache.getmQuanBackgroundMyAvatarIV(), 0,
                            mContext.getResources().getDimensionPixelSize(R.dimen.quan_big_avatar_width),
                            mContext.getResources().getDimensionPixelSize(R.dimen.quan_big_avatar_height),
                            ScalingLogic.CROP, R.drawable.image_default, R.drawable.image_default);
                } else {
                    viewCache.getmQuanBackgroundMyAvatarIV().setImageResource(R.drawable.user_fang_icon);
                }
                viewCache.getmQuanBackgroundMyAvatarIV().setOnClickListener(this);

            } else {
                viewCache.getmQuanBackgroundRL().setVisibility(View.GONE);
            }
            if (position == 0 && (mData == null || mData.size() <= 0)) {
                viewCache.getLl_bottom().setVisibility(View.GONE);
                viewCache.getNullTV().setVisibility(View.VISIBLE);
                return convertView;
            } else {
                viewCache.getLl_bottom().setVisibility(View.VISIBLE);
                viewCache.getNullTV().setVisibility(View.GONE);
            }
            final Integer position_integer = Integer.valueOf(position);
            final QuanItem quan = mData.get(position);

            if (quan != null) {
                ImageUtils.displayImage(quan.getUrl(), viewCache.getmQuanItemAvatarIV(), 360, R.drawable.user_yuan_icon,
                        R.drawable.user_yuan_icon);
                viewCache.getmQuanItemNameTV().setText(CarFriendQuanUtils.showCarFriendName(quan));
                viewCache.getmQuanItemContentTV()
                        .setText(ChatInputUtils.displayBigFacePic(mContext, quan.getContent(), mFaceCharacterMap));
                // viewCache.getmQuanItemContentMoreTV().setVisibility(View.GONE);
                if (quan.getTus().size() > 0) {
                    viewCache.getmQuanItemTuLL().setVisibility(View.VISIBLE);
                } else {
                    viewCache.getmQuanItemTuLL().setVisibility(View.GONE);
                }
                List<QuanTuItem> tus = quan.getTus();
                if (tus.size() >= 7) {
                    LinearLayout.LayoutParams fl_params = (LinearLayout.LayoutParams) viewCache.getmQuanItemTuFL()
                            .getLayoutParams();
                    fl_params.height = 3 * mTuHeight
                            + 2 * mContext.getResources().getDimensionPixelSize(R.dimen.quan_small_tu_margin);
                    viewCache.getmQuanItemTuFL().setLayoutParams(fl_params);
                } else if (tus.size() > 3 && tus.size() < 7) {
                    LinearLayout.LayoutParams fl_params = (LinearLayout.LayoutParams) viewCache.getmQuanItemTuFL()
                            .getLayoutParams();
                    fl_params.height = 2 * mTuHeight
                            + 1 * mContext.getResources().getDimensionPixelSize(R.dimen.quan_small_tu_margin);
                    viewCache.getmQuanItemTuFL().setLayoutParams(fl_params);
                } else {
                    LinearLayout.LayoutParams fl_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    viewCache.getmQuanItemTuFL().setLayoutParams(fl_params);
                }
                final List<String> tuUrls = new ArrayList<String>();
                for (QuanTuItem tu : tus) {
                    tuUrls.add(tu.getUrl());
                }
                viewCache.getmQuanItemTuFL().removeAllViews();
                for (int i = 0; i < tus.size(); i++) {
                    final ImageView tuIV = new ImageView(mContext);
                    tuIV.setAdjustViewBounds(true);
                    tuIV.setScaleType(ScaleType.FIT_XY);

                    FrameLayout.LayoutParams iv_params;
                    if (tus.size() == 1) {
                        iv_params = new FrameLayout.LayoutParams(new LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    } else {
                        iv_params = new FrameLayout.LayoutParams(new LayoutParams(mTuWidth, mTuHeight));
                    }

                    switch (i) {
                        case 0:
                            iv_params.gravity = Gravity.LEFT | Gravity.TOP;
                            break;
                        case 1:
                            iv_params.gravity = Gravity.CENTER | Gravity.TOP;
                            break;
                        case 2:
                            iv_params.gravity = Gravity.RIGHT | Gravity.TOP;
                            break;
                        case 3:
                            if (tus.size() < 7) {
                                iv_params.gravity = Gravity.LEFT | Gravity.BOTTOM;
                            } else {
                                iv_params.gravity = Gravity.LEFT | Gravity.CENTER;
                            }
                            break;
                        case 4:
                            if (tus.size() < 7) {
                                iv_params.gravity = Gravity.CENTER | Gravity.BOTTOM;
                            } else {
                                iv_params.gravity = Gravity.CENTER | Gravity.CENTER;
                            }
                            break;
                        case 5:
                            if (tus.size() < 7) {
                                iv_params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
                            } else {
                                iv_params.gravity = Gravity.RIGHT | Gravity.CENTER;
                            }
                            break;
                        case 6:
                            iv_params.gravity = Gravity.LEFT | Gravity.BOTTOM;
                            break;
                        case 7:
                            iv_params.gravity = Gravity.CENTER | Gravity.BOTTOM;
                            break;
                        case 8:
                            iv_params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
                            break;
                        default:
                            break;
                    }
                    iv_params.setMargins(2, 2, 2, 2);
                    tuIV.setLayoutParams(iv_params);

                    tuIV.setImageResource(R.drawable.image_default);
                    String url = tus.get(i).getUrl();

                    if (tus.size() == 1) {
                        ImageUtils.displayImage(QuanActivity.this, url, tuIV, 0, (int) Math.ceil(2 * mTuWidth),
                                3 * mTuHeight, ScalingLogic.FIT, R.drawable.image_default, R.drawable.image_default);
                    } else {
                        ImageUtils.displayImage(QuanActivity.this, url, tuIV, 0, mTuWidth, mTuHeight, ScalingLogic.CROP,
                                R.drawable.image_default, R.drawable.image_default);
                    }

                    final int viewPagerPosition = i;

                    /**
                     * 点击图片放大
                     */
                    tuIV.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent();
                            intent.putStringArrayListExtra("url", (ArrayList<String>) tuUrls);
                            intent.putExtra("viewPagerPosition", String.valueOf(viewPagerPosition));
                            intent.setClass(QuanActivity.this, VehicleQuanVewPager.class);
                            startActivity(intent);
                        }
                    });
                    viewCache.getmQuanItemTuFL().addView(tuIV);
                }

                viewCache.getmQuanItemAvatarIV().setOnClickListener(this);
                // viewCache.getmQuanItemContentMoreTV().setOnClickListener(this);
                viewCache.getmQuanItemPingIV().setOnClickListener(this);
                viewCache.getmQuanItemPingTV().setOnClickListener(this);
                viewCache.getmQuanItemZanIV().setOnClickListener(this);
                viewCache.getmQuanItemDeleteTV().setOnClickListener(this);
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyyMMddHHmmss").parse(quan.getPublishTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                viewCache.getmQuanItemDateTV()
                        .setText(DateTimeUtil.beforeDate(date, new Date(System.currentTimeMillis())));

                viewCache.getmQuanItemDeleteTV()
                        .setVisibility(quan.getZiji() != null && quan.getZiji().equals("1") ? View.VISIBLE : View.GONE);
                // 后面要改的
                viewCache.getmQuanItemZanIV().setImageResource(
                        Integer.parseInt(quan.getZaned()) == 1 ? R.drawable.zan_yes : R.drawable.zan_no);
                // viewCache.getmQuanItemZanTV().setText(mContext.getResources().getString(R.string.item_zan_ping_cnt,
                // quan.getQuanzancnt()));
                viewCache.getmQuanItemPingIV()
                        .setImageResource(Integer.parseInt(quan.getZaned()) == 1 ? R.drawable.reply : R.drawable.reply);
                // viewCache.getmQuanItemPingTV().setText(mContext.getResources().getString(R.string.item_zan_ping_cnt,
                // quan.getQuanpingcnt()));
                if (quan.getQuanpingcnt() == 0 && quan.getQuanzancnt() == 0) {
                    viewCache.getmZanAndPingDetailLL().setVisibility(View.GONE);
                } else {
                    viewCache.getmZanAndPingDetailLL().setVisibility(View.VISIBLE);
                }
                viewCache.getmPingDetailLL().setVisibility(quan.getQuanpingcnt() > 0 ? View.VISIBLE : View.GONE);
                viewCache.getmPingDetailLL().removeAllViews();
                for (final QuanPingItem quanPing : quan.getQuanping()) {
                    TextView pingTV = new TextView(mContext);
                    pingTV.setText(displayPing(quanPing));
                    pingTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12); // 12SP

                    viewCache.getmPingDetailLL().addView(pingTV);
                    pingTV.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // 如果是自己发的评论，就可以删除，短按删除，长按既可以删除也可以拷贝
                            // 如果是别人发的，就是弹出回复框
                            Message msg = new Message();
                            if (quanPing.getFriendId().equals(CacheTools.getUserData("userId"))) {
                                msg.what = QuanActivity.SHOW_DEL_QUAN_PING_DIALOG;
                            } else {
                                msg.what = QuanActivity.REPLY_QUAN_PING;
                            }
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constant.QUAN_MSG_BUNDLE.KEY_QUAN, quan);
                            bundle.putSerializable(Constant.QUAN_MSG_BUNDLE.KEY_QUAN_PING, quanPing);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                    });
                }
                // 赞人名列表
                viewCache.getmZanDetailLL().setVisibility(quan.getQuanzancnt() > 0 ? View.VISIBLE : View.GONE);
                viewCache.getmZanDetailTV().setText(displayZan(quan));

                if (viewCache.getmZanDetailLL().getVisibility() == View.GONE
                        || viewCache.getmPingDetailLL().getVisibility() == View.GONE) {
                    viewCache.getmZanDetailDividerView().setVisibility(View.GONE);
                } else {
                    viewCache.getmZanDetailDividerView().setVisibility(View.VISIBLE);
                }
            }

            viewCache.getmQuanItemAvatarIV().setTag(position_integer);
            viewCache.getmQuanItemPingIV().setTag(position_integer);
            viewCache.getmQuanItemPingTV().setTag(position_integer);
            viewCache.getmQuanItemZanIV().setTag(position_integer);
            viewCache.getmQuanItemDeleteTV().setTag(position_integer);
            viewCache.getQuanItemJubao().setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    final EditText editText = new EditText(QuanActivity.this);
                    if (Integer.parseInt(CacheTools.getUserData("userId")) == (quan.getUserId())) {
                        ToastUtil.toastShow(QuanActivity.this, "自己不能举报自己");
                    } else {
                        new AlertDialog.Builder(QuanActivity.this).setTitle("请输入举报原因").setView(editText)
                                .setPositiveButton("确定", new Dialog.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        AjaxParams params = new AjaxParams();
                                        // 传入帖子id和uid
                                        params.put("type", Constant.juBaoTypeQuan);
                                        params.put("relateId", String.valueOf(quan.getId()));
                                        params.put("reason", editText.getText().toString());

                                        requestNet(new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                // stub
                                                super.handleMessage(msg);
                                                switch (msg.what) {
                                                    case Constant.NET_DATA_SUCCESS:
                                                        ToastUtil.toastShow(QuanActivity.this, "已提交");
                                                        break;
                                                    case Constant.NET_DATA_FAIL:
                                                        ToastUtil.toastShow(QuanActivity.this, "举报失败，再试试");
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                        }, params, NetworkUtil.MAKE_ACCU, false, 0);
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                }
            });
            return convertView;
        }

        public void updateData(List<QuanItem> data) {
            this.mData = data;
            this.notifyDataSetChanged();
        }

        @Override
        public void onClick(View v) {
            Message msg;
            Object tag = v.getTag();
            Integer position_integer = null;
            QuanItem quan = null;
            if (tag instanceof Integer)
                position_integer = (Integer) tag;
            if (position_integer != null) {
                quan = (QuanItem) mData.get(position_integer.intValue());
            }
            switch (v.getId()) {
                case R.id.quan_item_avatar_iv:
                    // 进入到个人详情中
                    // Intent intent = new Intent(mContext,
                    // PersonalHomeActivity.class);
                    // intent.putExtra("userId", quan.getUserId());
                    // mContext.startActivity(intent);

                    Intent intent = new Intent(QuanActivity.this, PersonalHomeActivity2.class);
                    intent.putExtra("userId", quan.getUserId());
//				intent.putExtra("other_name", quan.getUserName());
//				intent.putExtra("ziji", quan.getZiji());// 是不是自己发的，1是自己发的，0不是自己发的
                    startActivity(intent);
                    break;
                // case R.id.quan_item_content_more_tv:
                // // 文本展开
                // break;
                case R.id.quan_item_ping_iv:
                    // 发消息给Activity去弹出评价框和键盘
                    msg = new Message();
                    msg.what = QuanActivity.PING_QUAN;
                    msg.obj = quan;
                    mHandler.sendMessage(msg);
                    break;
                case R.id.quan_item_ping_tv:
                    // 发消息给Activity去弹出评价框和键盘
                    msg = new Message();
                    msg.what = QuanActivity.PING_QUAN;
                    msg.obj = quan;
                    mHandler.sendMessage(msg);
                    break;
                case R.id.quan_item_zan_iv:
                    // 切换赞状态
                    msg = new Message();
                    msg.what = QuanActivity.TOGGLE_ZAN;
                    msg.obj = quan;
                    mHandler.sendMessage(msg);
                    break;
                case R.id.quan_bg_my_avatar_iv:
                    // 删除一个评论
                    // 进入到个人详情中
                    Intent intent2 = new Intent(mContext, PersonalHomeActivity2.class);
                    intent2.putExtra("userId", Integer.parseInt(CacheTools.getUserData("userId")));
                    mContext.startActivity(intent2);
                    break;

                case R.id.quan_item_delete_tv:
                    // 删除
                    showDeleteQuanDialog(quan);

                    break;
                default:
                    break;
            }
        }

        private AlertDialog mDeleteQuanDialog;// 删圈提示

        private void showDeleteQuanDialog(final QuanItem quan) {
            mDeleteQuanDialog = new AlertDialog.Builder(QuanActivity.this).setTitle("确定删除")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delQuan(quan);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            mDeleteQuanDialog.setCanceledOnTouchOutside(true);
            mDeleteQuanDialog.show();
        }

        @SuppressLint("HandlerLeak")
        private void delQuan(final QuanItem quan) {
            AjaxParams params = new AjaxParams();
            params.put("quanWenId", String.valueOf(quan.getId()));
            requestNet(new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            Toast.makeText(QuanActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            mData.remove(quan);
                            mAdapter.notifyDataSetChanged();
                            break;
                        case Constant.NET_DATA_FAIL:
                            Toast.makeText(QuanActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }

            }, params, NetworkUtil.DEL_QUAN, false, 0);

        }

        private SpannableString displayPing(QuanPingItem ping) {
            StringBuilder sb = new StringBuilder();
            SpannableString ssb = null;
            String name = CarFriendQuanUtils.showCarFriendName(ping);
            QuanPingReplyItem toWho = ping.getToWho();

            try {
                if (toWho != null && toWho.getName() != null && toWho.getId() > 0) {
                    String toname = toWho.getName();
                    int length = mContext.getResources().getString(R.string.reply).length();
                    sb.append(name).append(mContext.getResources().getString(R.string.reply)).append(toname).append(":")
                            .append(ping.getContent());
                    ssb = new SpannableString(sb.toString());
                    ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), 0, name.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#434343")), name.length(),
                            name.length() + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), name.length() + length,
                            name.length() + length + toname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#434343")),
                            name.length() + length + toname.length(), sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else {
                    sb.append(name).append(":").append(ping.getContent());
                    ssb = new SpannableString(sb.toString());
                    if (!TextUtils.isEmpty(name)) {
                        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), 0, name.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ssb.setSpan(new ClickSpan(Integer.parseInt(ping.getFriendId())), 0, name.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#434343")), name.length(), sb.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                }
                List<String> ywzList = ChatInputUtils.checkYWZ(ping.getContent());
                Bitmap bitmap;
                ImageSpan imageSpan;
                int ssb_start = ssb.toString().indexOf(":") + 1;
                int content_start = 0;
                for (int i = 0; i < ywzList.size(); i++) {
                    boolean flag = true;

                    if (null != mFaceCharacterMap.get("[" + ywzList.get(i) + "]")) {
                        if (ping.getContent().indexOf("[" + ywzList.get(i) + "]", content_start) >= 0) {
                            bitmap = ChatInputUtils.getImageFromAssetsFile(mContext,
                                    "face/" + mFaceCharacterMap.get("[" + ywzList.get(i) + "]") + ".png");
                            imageSpan = new ImageSpan(mContext, bitmap);

                            ssb.setSpan(imageSpan, ssb_start, ssb_start + ywzList.get(i).length() + 2,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ssb_start = ssb_start + ywzList.get(i).length() + 2;
                            content_start = content_start + ywzList.get(i).length() + 2;
                            flag = false;
                        }
                    }

                    if (flag) {
                        ssb_start = ssb_start + ywzList.get(i).length();
                        content_start = content_start + ywzList.get(i).length();
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            return ssb;
        }

        public SpannableString displayZan(QuanItem QuanItem) {
            StringBuilder builder = new StringBuilder();
            SpannableString ssb = null;
            List<Integer> zanIndex = new ArrayList<Integer>();
            int index = 0;
            String name;
            // 统计每个昵称的位置，为后面设置点击效果做分段准备
            for (QuanZanItem zan : QuanItem.getQuanzan()) {
                index++;
                name = TextUtils.isEmpty(zan.getNoteName()) ? zan.getNickName() : zan.getNoteName();
                index = builder.length() + name.length();
                zanIndex.add(index);
                builder.append(name + ", ");
            }
            // 去掉最后一个逗号和空格
            if (builder.toString().endsWith(", ")) {
                ssb = new SpannableString((builder.toString().substring(0, builder.length() - 2)));
            } else {
                ssb = new SpannableString(builder.toString());
            }
            int position = 0;
            int lastNameIndex = 0;
            // 统计每个昵称的位置，为后面设置点击效果做分段准备
            for (Integer nameindex : zanIndex) {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), lastNameIndex, nameindex,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new ClickSpan(Integer.parseInt(QuanItem.getQuanzan().get(position).getFriendId())),
                        lastNameIndex, nameindex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                lastNameIndex = nameindex;
                position++;
            }

            return ssb;

        }

        public class ClickSpan extends ClickableSpan implements OnClickListener {
            private int uid;

            public ClickSpan(int uid) {
                this.uid = uid;
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PersonalHomeActivity2.class);
                intent.putExtra("userId", uid);
                mContext.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllQuan(true);
    }

    public interface IsFinish {
        public void isFinish();
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
