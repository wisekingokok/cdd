package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.http.AjaxParams;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.barcode.view.SweepListView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.BalanceOrderGridAdapter;
import com.chewuwuyou.app.adapter.BalanceTimeGridAdapter;
import com.chewuwuyou.app.adapter.MyBalanceAdapter;
import com.chewuwuyou.app.bean.Balance;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ScreenUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.WalletUtil;
import com.chewuwuyou.app.widget.MyGridView;

/**
 * @author zys 账单明细
 */
public class WalletDetailActivity extends CDDBaseActivity implements
        OnClickListener {
    private TextView mTitleTv;
    private SweepListView mPullToRefreshListView;
    private ImageButton mImageBack;
    private TextView mTextRight;
    private String[] mTimeTypes = {"全部", "今天", "昨天", "七天", "一个月", "三个月"};
    private String[] mOrderTypes = {"全部", "充值", "提现", "订单收入", "订单付款", "订单退款", "提现退款", "转账", "红包"};
    private String[] mOrderStutus = {"0", "1", "2", "3", "4", "5"};
    private MyGridView mServiceGV;
    private BalanceTimeGridAdapter mGridAdapter;
    private List<Map<String, Object>> listOrderItems;
    private List<Map<String, Object>> listTimeItems;
    private ImageView mImageShadow;
    private MyGridView mBalanceGrid;
    private BalanceOrderGridAdapter mBlance_Grid_Adapter;
    private PopupWindow popupWindow;
    private Context mContext;
    private MyBalanceAdapter mAdapter;

    private Button mOkBtn;// 筛选
    private RelativeLayout mTitleHeight;// 标题布局高度
    private String mStartTime = "";// 开始时间
    private String mEndTime = "";// 结束时间

    private String mOrderStatus = "";// 选择的订单类型
    // private List<String> mDataTimeList;// 对订单时间进行统计
    private List<Balance> mAllBalances;// 所有的账单详情
    private TextView mWalletDataEmptyTV;// 账单数据为空

    private int mCurcor = 0;
    // private boolean mIsRefreshing = false;
    private int mTimeGridItemNum = 0; // 表示时间gridview 子选项
    private int mOrderGridItemNum = 0; // 表示订单gridview 子选项

    /**
     * 更新数据
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.SEND_Handler:
                    MyLog.i("YUY", "收到删除消息");
                    AjaxParams params = new AjaxParams();
                    params.put("personalId", msg.obj.toString());
                    dancelOrder(params);// 根据传递的账户名称删除数据
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_detail_layout);
        initView();
        initData();
        initEvent();

    }

    @Override
    protected void initView() {
        mTitleTv = (TextView) findViewById(R.id.sub_header_bar_tv);
        mPullToRefreshListView = (SweepListView) findViewById(R.id.tally_status_order_lv);
        // mTallyStatusOrderLV.setGroupIndicator(null);

        mImageBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mTextRight = (TextView) findViewById(R.id.sub_header_bar_right_tv);

        mImageShadow = (ImageView) findViewById(R.id.image_shadow);
        mContext = getApplicationContext();
        mWalletDataEmptyTV = (TextView) findViewById(R.id.wallet_data_empty_tv);
        listOrderItems = getOrderListItems();
        listTimeItems = getTimeListItems();

        // mList = getData();
        // mExpanAdapter = new MyBalanceAdapter(mContext, mList);
        // mTallyStatusOrderLV.setAdapter(mExpanAdapter);
        mPullToRefreshListView.setFocusable(false);// 让listview失去焦点
        mPullToRefreshListView.smoothScrollToPosition(0, 20);// 设置显示的位置
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        // mDataTimeList = new ArrayList<String>();
        // mBalanceMap = new HashMap<String, List<Balance>>();
        mAllBalances = new ArrayList<Balance>();
        if (mAdapter == null) {
            mAdapter = new MyBalanceAdapter(mContext, mAllBalances, mHandler);
            mPullToRefreshListView.setAdapter(mAdapter);

        }
        getWallet(true);
    }

    @Override
    protected void initEvent() {

        // 设置标题
        mTitleTv.setText("钱包明细");
        mImageBack.setOnClickListener(this);
        mTextRight.setText("筛选");
        mTextRight.setVisibility(View.VISIBLE);
        mTextRight.setOnClickListener(this);
        // mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        Intent intent = new Intent(WalletDetailActivity.this,
                                BalanceRecordActivity.class);
                        intent.putExtra(Constant.BALANCE_DETAIL_BEAN,
                                mAllBalances.get(arg2 - 1));
                        startActivity(intent);
                    }
                });

    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finish();
                break;

            case R.id.sub_header_bar_right_tv:
                showPopupWindow(mTextRight);

                break;

            default:
                break;
        }

    }

	/* 使用popupwindow来实现 “筛选” 弹出框 */

    @SuppressWarnings("deprecation")
    private void showPopupWindow(View view) {

        mImageShadow.setVisibility(View.VISIBLE);

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(WalletDetailActivity.this)
                .inflate(R.layout.balance_screen, null);
        mServiceGV = (MyGridView) contentView.findViewById(R.id.service_gv);
        mBalanceGrid = (MyGridView) contentView.findViewById(R.id.balance_grid);
        mOkBtn = (Button) contentView.findViewById(R.id.ok_btn);
        int width = ScreenUtils.getScreenWidth(WalletDetailActivity.this);
        int hight = ScreenUtils.getScreenHeight(WalletDetailActivity.this);
        popupWindow = new PopupWindow(contentView, width * 440 / 720, hight,
                true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // mImageShadow.setVisibility(View.GONE);
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mImageShadow.setVisibility(View.GONE);
                    // popupWindow.dismiss();
                }

                return false;

                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

		/* popupWindow消失事件 */
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                mImageShadow.setVisibility(View.GONE);

            }
        });
        //
        // popupWindow.setFocusable(false);
        //
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 设置popupwindow显示位置
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int densityDpi = metric.densityDpi;
        int exceed = densityDpi / 160 * 20;// popupwindow背景图片多出的箭头高度+ll_credit控件到title控件多白出高度

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int[] height = new int[2];
        // mTallyStatusOrderLV.getLocationOnScreen(height);// ll_credit到手机屏幕顶端高度

        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0],
                height[1] - exceed);

		/* 初始化时间类型 gridview 及 关联适配器 */
        mGridAdapter = new BalanceTimeGridAdapter(listTimeItems,
                WalletDetailActivity.this, mTimeGridItemNum);
        mServiceGV.setAdapter(mGridAdapter);

		/* 初始化订单类型 gridview 及 关联适配器 */
        mBlance_Grid_Adapter = new BalanceOrderGridAdapter(listOrderItems,
                WalletDetailActivity.this, mOrderGridItemNum);
        mBalanceGrid.setAdapter(mBlance_Grid_Adapter);

		/* gridview 时间选择点击事件 */
        mServiceGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                mTimeGridItemNum = arg2;
                mGridAdapter.setSeclection(arg2);
                mGridAdapter.notifyDataSetChanged();
                getBalanceTime(mTimeTypes[arg2]);
            }
        });

		/* gridview 订单类型点击事件 */
        mBalanceGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                mOrderGridItemNum = arg2;
                mBlance_Grid_Adapter.setSeclection(arg2);
                mBlance_Grid_Adapter.notifyDataSetChanged();
                mOrderStatus = mOrderStutus[arg2];
            }

        });
        mOkBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mImageShadow.setVisibility(View.GONE);
                popupWindow.dismiss();
                getWallet(true);
            }
        });
    }

    /**
     * 初始化 时间类型 timetype
     */
    private List<Map<String, Object>> getTimeListItems() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < mTimeTypes.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("timetype", mTimeTypes[i]); // 时间类型

            listItems.add(map);
        }

        return listItems;
    }

    /**
     * 初始化 订单类型ordertye
     */
    private List<Map<String, Object>> getOrderListItems() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < mOrderTypes.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ordertye", mOrderTypes[i]); // 物品标题
            listItems.add(map);
        }

        return listItems;
    }

    /**
     * 传入时间类型 "全部", "今天", "昨天", "七天", "一个月", "三个月"
     *
     * @param timeType
     */
    private void getBalanceTime(String timeType) {
        if (timeType.equals("全部")) {
            mStartTime = "";
            mEndTime = "";
        } else if (timeType.equals("今天")) {
            mStartTime = DateTimeUtil.getSystemTime() + " 00:00:00";
            mEndTime = DateTimeUtil.getSystemTime() + " 23:59:59";
        } else if (timeType.equals("昨天")) {
            mStartTime = WalletUtil.getStartDate('5') + " 00:00:00";
            mEndTime = WalletUtil.getStartDate('5') + " 23:59:59";
        } else if (timeType.equals("七天")) {
            mStartTime = WalletUtil.getStartDate('6') + " 00:00:00";
            mEndTime = DateTimeUtil.getSystemTime() + " 23:59:59";
        } else if (timeType.equals("一个月")) {
            mStartTime = WalletUtil.getStartDate('7') + " 00:00:00";
            mEndTime = DateTimeUtil.getSystemTime() + " 23:59:59";
        } else if (timeType.equals("三个月")) {
            mStartTime = WalletUtil.getStartDate('8') + " 00:00:00";
            mEndTime = DateTimeUtil.getSystemTime() + " 23:59:59";
        }
        MyLog.i("YUY", "mStartTime = " + mStartTime + " mEndTime = " + mEndTime);
    }

    private void getWallet(final boolean refresh) {
        if (refresh) {
            mCurcor = 0;
        }
        // mPullToRefreshListView.setRefreshing();
        AjaxParams params = new AjaxParams();
        params.put("start", String.valueOf(mCurcor));
        params.put("startTime", mStartTime);
        params.put("endTime", mEndTime);
        params.put("type", mOrderStatus);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mAllBalances.clear();
                        List<Balance> mRefreshData = Balance.parseList(msg.obj
                                .toString());
                        if (refresh) {
                            if (msg.obj.toString().equals("[]")
                                    || msg.obj.toString() == null) {
                                mWalletDataEmptyTV.setVisibility(View.VISIBLE);

                            } else {
                                mWalletDataEmptyTV.setVisibility(View.GONE);
                            }
                        } else {
                            // 是加载后面的订单
                            if (mRefreshData == null) {
                                ToastUtil.toastShow(WalletDetailActivity.this,
                                        "没有更多数据了");
                                return;
                            }
                        }

                        mAllBalances.addAll(mRefreshData);
                        mAdapter.notifyDataSetChanged();
                        mCurcor = mAllBalances.size();
                        // if (mRefreshData.size() < 20) {
                        // mPullToRefreshListView.onLoadComplete();
                        // }
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.QUERY_MINGXI, false, 1);
    }

    /**
     * 删除账单明细
     */
    private void dancelOrder(AjaxParams params) {

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mAllBalances.remove(Constant.CCCOUNT_WITHDRAWLS);
                        MyLog.i("YUY", "删除账单明细 = " + msg.obj.toString());
                        mAdapter = new MyBalanceAdapter(mContext, mAllBalances,
                                mHandler);
                        mPullToRefreshListView.setAdapter(mAdapter);
                        break;

                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(WalletDetailActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:

                        break;
                }
            }
        }, params, NetworkUtil.DECEL_MINGXI, false, 0);

    }

}
