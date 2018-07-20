package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.BuildConfig;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.FaceVPAdapter;
import com.chewuwuyou.app.adapter.SNSAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.QuanItem;
import com.chewuwuyou.app.bean.QuanPingItem;
import com.chewuwuyou.app.bean.QuanPingReplyItem;
import com.chewuwuyou.app.bean.QuanTuItem;
import com.chewuwuyou.app.bean.QuanZanItem;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ChatInputUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.ExpressionUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.viewcache.CaptureItemViewCache;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.MyGridView;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class QuanDetailActivity extends CDDBaseActivity {
    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackIBtn;// 左键,返回按钮
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;// 标题

    // 去评
    @ViewInject(id = R.id.quan_detail_avatar_iv, click = "onAction")
    private ImageView mQuanDetailAvatarIV;// 评价窗口

    @ViewInject(id = R.id.quan_detail_name_tv, click = "onAction")
    private TextView mQuanDetailNameTV;// 评价窗口

    @ViewInject(id = R.id.quan_detail_content_tv, click = "onAction")
    private TextView mQuanDetailContentTV;// 评价窗口

    private MyGridView mQuanDetailTuGV;// 评价窗口

    @ViewInject(id = R.id.quan_detail_date_tv, click = "onAction")
    private TextView mQuanDetailDateTV;// 评价窗口

    // @ViewInject(id = R.id.quan_detail_delete_tv, click = "onAction")
    // private TextView mQuanDetailDeleteTV;// 评价窗口

    @ViewInject(id = R.id.quan_detail_ping_tv, click = "onAction")
    private TextView mQuanDetailPingIV;// 评价窗口

    @ViewInject(id = R.id.quan_detail_zan_iv, click = "onAction")
    private ImageView mQuanDetailZanIV;// 评价窗口

    @ViewInject(id = R.id.ping_detail_ll, click = "onAction")
    private LinearLayout mPingDetailLL;// 评价窗口

    @ViewInject(id = R.id.zan_detail_ll, click = "onAction")
    private LinearLayout mZanDetailLL;// 评价窗口

    @ViewInject(id = R.id.zan_detail_tv, click = "onAction")
    private TextView mZanDetailTV;// 评价窗口

    // 去评
    @ViewInject(id = R.id.quan_detail_to_ping_ll, click = "onAction")
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
    // private GridView mToPingFaceGV;// 表情列表
    // private FaceAdapter mFaceAdapter;
    @ViewInject(id = R.id.zan_detail_divider_view)
    private View mZanDetailDividerView;// zan_detail_divider_view
    @ViewInject(id = R.id.zan_and_ping_detail_ll)
    private LinearLayout mZanAndPingDetailLL;// android:id="@+id/zan_and_ping_detail_ll"
    private Map<String, String> mFaceCharacterMap;
    // 展示大图要用
    @ViewInject(id = R.id.quan_detail_container)
    private RelativeLayout mContainer;
    private HackyViewPager mExpandedImageViewPager;

    // 消息处理
    public static final int TOGGLE_ZAN = 111;// 赞或者取消赞一个动态
    public static final int PING_QUAN = 112;// 评价一个动态
    public static final int REPLY_QUAN_PING = 113;// 回复一个评论
    public static final int ADD_FACE_PIC = 114;// 添加表情
    public static final int REPLY_PING = 116;
    public static final int DELETE_PING = 117;

    private int columns = 7, rows = 3;// 表情图标每页6列4行
    private List<View> faceViews = new ArrayList<View>();// 每页显示的表情view
    private List<String> staticFacesList;// 表情列表
    private LinearLayout mDotsLayout;
    private ViewPager mFaceViewPager;
    private LayoutInflater mInflater;
    private LinearLayout mFaceLL;

    // private Handler mHandler = new Handler() {
    // public void handleMessage(Message msg) {
    //
    // switch (msg.what) {
    // case ADD_FACE_PIC:// 添加一个表情
    // SpannableString spannableString = (SpannableString) msg.obj;
    // Editable editable = mToPingET.getText();
    // int st = mToPingET.getSelectionStart();
    // int en = mToPingET.getSelectionEnd();
    // editable.replace(st, en, spannableString);
    // break;
    // case REPLY_PING:// 评价一个PING
    // final QuanPingItem ping = (QuanPingItem) msg.obj;
    // showInputMethod(ping);
    // break;
    // case DELETE_PING:// 删除一个评价
    // QuanPingItem item = (QuanPingItem) msg.obj;
    // showDeletePingDialog(item);
    // break;
    // default:
    // break;
    // }
    // }
    // };

    private AlertDialog mDeletePingDialog;// 删评提示
    private QuanItem mQuanDetail;
    private QuanTuGridAdapter mTuAdapter;
    private ArrayList<String> mTuUrls = new ArrayList<String>();
    private RelativeLayout mTitleHeight;// 标题布局高度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quan_detail_layout);

        mQuanDetail = (QuanItem) getIntent().getExtras().getSerializable(
                Constant.QUAN_MSG_BUNDLE.KEY_QUAN);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {

        mExpandedImageViewPager = (HackyViewPager) findViewById(R.id.quan_detail_expanded_image_viewpager);
        mQuanDetailTuGV = (MyGridView) findViewById(R.id.quan_detail_tus_gv);

        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mFaceLL = (LinearLayout) findViewById(R.id.face_ll);
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);// 表情下小圆点
        mFaceViewPager = (ViewPager) findViewById(R.id.face_viewpager);
        mFaceViewPager.setOnPageChangeListener(new PageChange());
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mHeaderTV.setText("心情");
        this.mFaceCharacterMap = JsonUtil
                .getFaceStrMap(QuanDetailActivity.this);
        staticFacesList = ExpressionUtil.initStaticFaces(this);// 初始化表情图
        initFaceViewPager();// 初始化表情
        ExpressionUtil.getFaceStrMap(QuanDetailActivity.this);// 获取新的表情数据Map
        // mFaceAdapter = new FaceAdapter(QuanDetailActivity.this, mHandler);
        // mToPingFaceGV.setAdapter(mFaceAdapter);
        notifyDataChanged();

    }

    private void notifyDataChanged() {
        if (mQuanDetail != null) {
            ImageUtils.displayImage(mQuanDetail.getUrl(), mQuanDetailAvatarIV,
                    360, R.drawable.user_yuan_icon, R.drawable.user_yuan_icon);
            mQuanDetailNameTV.setText(CarFriendQuanUtils
                    .showCarFriendName(mQuanDetail));
            mQuanDetailContentTV.setText(ChatInputUtils.displayFacePic(
                    QuanDetailActivity.this, mQuanDetail.getContent(),
                    mFaceCharacterMap));
            if (mQuanDetail.getTus().size() > 0) {
                mQuanDetailTuGV.setVisibility(View.VISIBLE);
            } else {
                mQuanDetailTuGV.setVisibility(View.GONE);
            }
            List<QuanTuItem> tus = mQuanDetail.getTus();
            mTuUrls.clear();
            for (QuanTuItem tu : tus) {
                mTuUrls.add(tu.getUrl());
            }
            mTuAdapter = new QuanTuGridAdapter(QuanDetailActivity.this,
                    mTuUrls, mExpandedImageViewPager, mContainer,
                    mQuanDetailTuGV);
            mQuanDetailTuGV.setAdapter(mTuAdapter);
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyyMMddHHmmss").parse(mQuanDetail
                        .getPublishTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mQuanDetailDateTV.setText(DateTimeUtil.beforeDate(date, new Date(
                    System.currentTimeMillis())));
            // mQuanDetailDeleteTV.setVisibility(Integer.parseInt(mQuanDetail.getZiji())
            // == 1 ? View.VISIBLE : View.GONE);
            // 后面要改的
            mQuanDetailZanIV.setImageResource(Integer.parseInt(mQuanDetail
                    .getZaned()) == 1 ? R.drawable.iconheart
                    : R.drawable.iconhear);
            // mQuanDetailPingIV.setImageResource(Integer.parseInt(mQuanDetail.getZaned())
            // == 1 ? R.drawable.reply : R.drawable.reply);
            if (mQuanDetail.getQuanpingcnt() == 0
                    && mQuanDetail.getQuanzancnt() == 0) {
                mZanAndPingDetailLL.setVisibility(View.GONE);
            } else {
                mZanAndPingDetailLL.setVisibility(View.VISIBLE);
            }
            mPingDetailLL
                    .setVisibility(mQuanDetail.getQuanpingcnt() > 0 ? View.VISIBLE
                            : View.GONE);
            mPingDetailLL.removeAllViews();
            for (final QuanPingItem quanPing : mQuanDetail.getQuanping()) {
                TextView pingTV = new TextView(QuanDetailActivity.this);
                pingTV.setText(displayPing(quanPing));
                pingTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12); // 12SP
                mPingDetailLL.addView(pingTV);
                pingTV.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // 如果是自己发的评论，就可以删除，短按删除，长按既可以删除也可以拷贝
                        // 如果是别人发的，就是弹出回复框
                        if (quanPing.getFriendId().equals(
                                CacheTools.getUserData("userId"))) {
                            showDeletePingDialog(quanPing);
                        } else {
                            showInputMethod(quanPing);
                        }

                    }
                });
            }
            // 赞人名列表
            mZanDetailLL
                    .setVisibility(mQuanDetail.getQuanzancnt() > 0 ? View.VISIBLE
                            : View.GONE);
            mZanDetailTV.setText(displayZan(mQuanDetail));

            if (mZanDetailLL.getVisibility() == View.GONE
                    || mPingDetailLL.getVisibility() == View.GONE) {
                mZanDetailDividerView.setVisibility(View.GONE);
            } else {
                mZanDetailDividerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void initEvent() {

        mToPingET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(mToPingET.getText().toString())) {
                    mToPingSendBtn
                            .setBackgroundResource(R.drawable.common_blue_btn_bg);
                    mToPingSendBtn.setText("确认");
                    mToPingSendBtn.setTextColor(getResources().getColor(
                            R.color.white));
                    mToPingSendBtn.setClickable(true);
                } else {
                    mToPingSendBtn
                            .setBackgroundResource(R.drawable.common_gray_btn_bg);
                    mToPingSendBtn.setText("发送");
                    mToPingSendBtn.setTextColor(getResources().getColor(
                            R.color.to_ping_send_btn_default_color));
                    mToPingSendBtn.setClickable(false);
                }
            }
        });
    }

    // 弹出dialog删除一个评价
    private void showDeletePingDialog(final QuanPingItem ping) {
        mDeletePingDialog = new AlertDialog.Builder(QuanDetailActivity.this)
                .setTitle("确定删除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delQuanPing(ping);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        mDeletePingDialog.setCanceledOnTouchOutside(true);
        mDeletePingDialog.show();
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.to_ping_send_btn:
                if (mToPingET.getTag(R.id.ping_et_key_quan_ping) != null) {
                    QuanPingItem quanPing = ((QuanPingItem) mToPingET
                            .getTag(R.id.ping_et_key_quan_ping));
                    replyQuanPing(quanPing);
                    mToPingET.setTag(R.id.ping_et_key_quan_ping, null);
                } else {
                    pingQuan();
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
            case R.id.quan_detail_avatar_iv:
                Intent intent = new Intent(QuanDetailActivity.this,
                        PersonalHomeActivity2.class);
                intent.putExtra("userId", mQuanDetail.getUserId());
                startActivity(intent);
                break;

            case R.id.quan_detail_ping_tv:
                showInputMethod(mQuanDetail);
                break;

            case R.id.quan_detail_zan_iv:
                toggleZan();
                break;
            default:
                break;
        }
    }

    // 显示输入框同时弹出软键盘
    private void showInputMethod(QuanItem quan) {
        showKeyboard();
        mToPingLL.setVisibility(View.VISIBLE);
        mToPingET.setHint("我也来说一句");
        mToPingET.setText("");
        mToPingET.setTag(R.id.ping_et_key_quan, quan);
        mToPingET.requestFocus();
    }

    // 显示输入框同时弹出软键盘
    private void showInputMethod(QuanPingItem quanPing) {
        showKeyboard();
        mToPingLL.setVisibility(View.VISIBLE);
        mToPingET.setTag(R.id.ping_et_key_quan_ping, quanPing);
        mToPingET.setText("");
        // 这样写是效率很低，后面改进
        mToPingET.setHint(getString(R.string.quan_reply_ping,
                CarFriendQuanUtils.showCarFriendName(quanPing)));
        mToPingET.requestFocus();
    }

    // 删除一条帖评
    private void delQuanPing(final QuanPingItem quanPing) {
        AjaxParams params = new AjaxParams();
        params.put("quanPingId", String.valueOf(quanPing.getId()));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Toast.makeText(QuanDetailActivity.this, "删除成功",
                                Toast.LENGTH_SHORT).show();
                        mQuanDetail.setQuanpingcnt(mQuanDetail.getQuanpingcnt() - 1);
                        mQuanDetail.getQuanping().remove(quanPing);
                        notifyDataChanged();
                        break;
                    case Constant.NET_DATA_FAIL:
                        Toast.makeText(QuanDetailActivity.this, "删除失败",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

        }, params, NetworkUtil.DEL_QUAN_PING, false, 0);
    }

    // 切换赞状态
    private void toggleZan() {

        AjaxParams params = new AjaxParams();
        // 传入动态id和uid
        params.put("quanWenId", String.valueOf(mQuanDetail.getId()));
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

                            if (mQuanDetail.getZaned().equals("0")) {
                                mQuanDetail.setZaned(1 + "");
                                mQuanDetail.setQuanzancnt(mQuanDetail
                                        .getQuanzancnt() + 1);
                                mQuanDetail.getQuanzan().add(quanZan);
                                Toast.makeText(QuanDetailActivity.this, "点赞成功",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                mQuanDetail.setZaned(0 + "");
                                mQuanDetail.setQuanzancnt(mQuanDetail
                                        .getQuanzancnt() - 1);
                                mQuanDetail.getQuanzan().remove(quanZan);
                                for (QuanZanItem z : mQuanDetail.getQuanzan()) {
                                    if (z.getId() == quanZan.getId()) {
                                        mQuanDetail.getQuanzan().remove(z);
                                        break;
                                    }
                                }
                                Toast.makeText(QuanDetailActivity.this, "取消赞",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        notifyDataChanged();
                        break;
                    case Constant.NET_DATA_NULL:
                        // 如果以前赞过，那现在取消赞
                        mQuanDetail.setZaned(0 + "");
                        mQuanDetail.setQuanzancnt(mQuanDetail.getQuanzancnt() - 1);
                        for (QuanZanItem z : mQuanDetail.getQuanzan()) {
                            if (z.getFriendId().equals(
                                    CacheTools.getUserData("userId"))) {
                                mQuanDetail.getQuanzan().remove(z);
                                break;
                            }
                        }
                        notifyDataChanged();
                        break;
                    case Constant.NET_DATA_FAIL:
                        // 提示切换赞失败
                        DataError mDataError = (DataError) msg.obj;
                        Toast.makeText(QuanDetailActivity.this, mDataError.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }, params, NetworkUtil.QUAN_TOGGLE_ZAN, false, 0);


    }

    // 评论一个圈文
    private void pingQuan() {
        AjaxParams params = new AjaxParams();
        // 传入动态id和uid
        params.put("quanWenId", String.valueOf(mQuanDetail.getId()));
        params.put("content", String.valueOf(mToPingET.getText().toString()));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Toast.makeText(QuanDetailActivity.this, "评论成功",
                                Toast.LENGTH_SHORT).show();
                        QuanPingItem ping = QuanPingItem.parse(msg.obj.toString());

                        if (ping != null) {
                            mQuanDetail.setPinged(1 + "");
                            mQuanDetail.setQuanpingcnt(mQuanDetail.getQuanpingcnt() + 1);
                            mQuanDetail.getQuanping().add(ping);
                        }
                        hideKeyboard();
                        mToPingLL.setVisibility(View.GONE);
                        notifyDataChanged();
                        break;
                    case Constant.NET_DATA_FAIL:
                        Toast.makeText(QuanDetailActivity.this, "评失败",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }, params, NetworkUtil.PING_QUAN, false, 0);
    }

    // 回复一个评论
    private void replyQuanPing(QuanPingItem quanPing) {
        AjaxParams params = new AjaxParams();
        // 传入动态id和uid
        params.put("quanWenId", String.valueOf(mQuanDetail.getId()));
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
                        Toast.makeText(QuanDetailActivity.this, "评论成功",
                                Toast.LENGTH_SHORT).show();
                        QuanPingItem ping = QuanPingItem.parse(msg.obj.toString());

                        if (ping != null) {
                            mQuanDetail.setPinged(1 + "");
                            mQuanDetail.setQuanpingcnt(mQuanDetail.getQuanpingcnt() + 1);
                            mQuanDetail.getQuanping().add(ping);
                        }
                        hideKeyboard();
                        mToPingLL.setVisibility(View.GONE);
                        notifyDataChanged();
                        break;
                    case Constant.NET_DATA_FAIL:
                        Toast.makeText(QuanDetailActivity.this, "评论失败",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }, params, NetworkUtil.REPLY_QUAN_PING, false, 0);
    }

    private SpannableString displayPing(QuanPingItem ping) {
        StringBuilder sb = new StringBuilder();
        SpannableString ssb = null;
        String name = CarFriendQuanUtils.showCarFriendName(ping);
        QuanPingReplyItem toWho = ping.getToWho();

        try {
            if (toWho != null && toWho.getName() != null && toWho.getId() > 0) {
                String toname = toWho.getName();
                int length = getResources().getString(R.string.reply).length();
                sb.append(name)
                        .append(getResources().getString(R.string.reply))
                        .append(toname).append(":").append(ping.getContent());
                ssb = new SpannableString(sb.toString());
                ssb.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#5b6e82")),
                        0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#434343")),
                        name.length(), name.length() + length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#5b6e82")),
                        name.length() + length,
                        name.length() + length + toname.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#434343")),
                        name.length() + length + toname.length(), sb.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else {
                sb.append(name).append(":").append(ping.getContent());
                ssb = new SpannableString(sb.toString());
                ssb.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#5b6e82")),
                        0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(
                        new ClickSpan(Integer.parseInt(ping.getFriendId())), 0,
                        name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            List<String> ywzList = ChatInputUtils.checkYWZ(ping
                    .getContent());
            Bitmap bitmap;
            ImageSpan imageSpan;
            int ssb_start = ssb.toString().indexOf(":") + 1;
            int content_start = 0;
            for (int i = 0; i < ywzList.size(); i++) {
                boolean flag = true;

                if (null != mFaceCharacterMap.get("[" + ywzList.get(i) + "]")) {
                    if (ping.getContent().indexOf(
                            "[" + ywzList.get(i) + "]", content_start) >= 0) {
                        bitmap = ChatInputUtils.getImageFromAssetsFile(
                                QuanDetailActivity.this,
                                "face/"
                                        + mFaceCharacterMap.get("[" + ywzList
                                        .get(i) + "]") + ".png");
                        imageSpan = new ImageSpan(QuanDetailActivity.this, bitmap);
                        ssb.setSpan(imageSpan, ssb_start, ssb_start
                                        + ywzList.get(i).length() + 2,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ssb_start = ssb_start + ywzList.get(i).length() + 2;
                        content_start = content_start
                                + ywzList.get(i).length() + 2;
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
            name = TextUtils.isEmpty(zan.getNoteName()) ? zan.getNickName()
                    : zan.getNoteName();
            index = builder.length() + name.length();
            zanIndex.add(index);
            builder.append(name + ", ");
        }
        // 去掉最后一个逗号和空格
        if (builder.toString().endsWith(", ")) {
            ssb = new SpannableString((builder.toString().substring(0,
                    builder.length() - 2)));
        } else {
            ssb = new SpannableString(builder.toString());
        }
        int position = 0;
        int lastNameIndex = 0;
        // 统计每个昵称的位置，为后面设置点击效果做分段准备
        for (Integer nameindex : zanIndex) {
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")),
                    lastNameIndex, nameindex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(
                    new ClickSpan(Integer.parseInt(QuanItem.getQuanzan()
                            .get(position).getFriendId())), lastNameIndex,
                    nameindex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
            Intent intent = new Intent(QuanDetailActivity.this,
                    PersonalHomeActivity2.class);
            intent.putExtra("userId", uid);
            startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

    }

    public class QuanTuGridAdapter extends SNSAdapter {
        private ArrayList<String> mTuData;
        private MyGridView mTusGV;

        public QuanTuGridAdapter(Activity context, ArrayList<String> data,
                                 HackyViewPager viewPager, View container, MyGridView tusGV) {
            super(context, viewPager, container);
            this.mTuData = data;
            this.mTusGV = tusGV;
        }

        private int getH() {
            // 动态高度
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenW = dm.widthPixels;
            return screenW / 4;
        }

        public int getCount() {
            return mTuData.size();
        }

        public Object getItem(int position) {
            return mTuData.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            CaptureItemViewCache viewCache = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.capture_item, null);
                viewCache = new CaptureItemViewCache(convertView);
                convertView.setTag(viewCache);
            } else {
                viewCache = (CaptureItemViewCache) convertView.getTag();
            }
            LayoutParams params = (LayoutParams) viewCache.getmIV().getLayoutParams();
            params.height = getH();
            viewCache.getmIV().setLayoutParams(params);
            ImageUtils.displayImage(mContext, mTuData.get(position),
                    viewCache.getmIV(), 0, mTuWidth, mTuHeight,
                    ScalingLogic.FIT, R.drawable.image_default,
                    R.drawable.image_load_fail);

            viewCache.getmIV().setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, VehicleQuanVewPager.class);
                    intent.putStringArrayListExtra("url", mTuData);
                    intent.putExtra("viewPagerPosition", position + "");
                    mContext.startActivity(intent);
                    // zoomImageFromThumb(view, mTuData, mTusGV, position);
                }
            });

            return convertView;
        }
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
                    staticFacesList, columns, rows, mToPingET));
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