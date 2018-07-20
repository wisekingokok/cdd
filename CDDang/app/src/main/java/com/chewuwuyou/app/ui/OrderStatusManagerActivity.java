package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.BusinessOrderAdapter;
import com.chewuwuyou.app.adapter.OrderStatusAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.bean.ValueTask;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshBase.State;
import com.chewuwuyou.app.widget.PullToRefreshListView;

import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:商家通过我的工作台选择订单不同状态的展示：待处理订单、服务中订单、已完成订单、已取消订单、退款的订单、门店新订单
 * @author:yuyong
 * @date:2015-9-29上午10:58:36
 * @version:1.2.1
 */
public class OrderStatusManagerActivity extends CDDBaseActivity
        implements OnClickListener, OnRefreshListener2<ListView> {
    private TextView mHeaderRightTV;
    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private TextView sub_header_bar_left_tv;
    private PullToRefreshListView mPullToRefreshListView;
    private int mOrderStatus;// 区分商家要查看的订单状态
    private int mOrderType;// 区分订单是发出还是收到
    private RelativeLayout mTitleHeight;// 标题布局高度
    private boolean mIsRefreshing = false;// 翻页要用
    private List<ValueTask> mData;// 订单集合
    private TextView mOrderEmptyTV;// 相关订单为空时显示
    private Button del;

    private OrderStatusAdapter mAdapter;
    private ImageButton mImageTop;
    private boolean isNetWork = false;

    /**
     * 包括订单开始时间，结束时间,状态,订单来自
     */
    private String mStatus = "";

    public static String mType;// 区分由已取消的订单进入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_status_manager_ac);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mHeaderRightTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.order_status_manager_list);
        mOrderEmptyTV = (TextView) findViewById(R.id.order_empty_tv);
        mImageTop = (ImageButton) findViewById(R.id.btn_top);
        del = (Button) findViewById(R.id.del);
        sub_header_bar_left_tv = (TextView) findViewById(R.id.sub_header_bar_left_tv);
        sub_header_bar_left_tv.setText("全选");
        mImageTop.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mOrderStatus = getIntent().getExtras().getInt("orderStatus");
        mOrderType = getIntent().getExtras().getInt("orderType");
        mType = getIntent().getStringExtra("type");
        if (mData == null) {
            mData = new ArrayList<ValueTask>();
        }
        if (mAdapter == null) {
            mAdapter = new OrderStatusAdapter(OrderStatusManagerActivity.this, mData);
        }
        this.mPullToRefreshListView.setAdapter(mAdapter);
        orderType(mOrderStatus);
    }

    @Override
    protected void initEvent() {
        del.setOnClickListener(this);
        sub_header_bar_left_tv.setOnClickListener(this);
        mHeaderRightTV.setOnClickListener(this);
        mBackIBtn.setOnClickListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (mAdapter.getOperating()) {
                    ValueTask task = (ValueTask) mAdapter.getItem(arg2 - 1);
                    task.isCheck = !task.isCheck;
                    mAdapter.notifyDataSetChanged();
                    return;
                }

                ValueTask task = mData.get(arg2 - 1);
                String flag = task.getFlag();
                String status = task.getStatus();
                Intent intent = null;
                if (status.equals("2") && flag.equals("3")
                        && task.getFacilitatorId().equals(CacheTools.getUserData("userId"))) {
                    intent = new Intent(OrderStatusManagerActivity.this, NewRobOrderDetailsActivity.class);
                    intent.putExtra("isBCancel", 1);// 区分已与其他服务商成交
                } else if (status.equals("28") && (flag.equals("1") || flag.equals("2"))) {
                    intent = new Intent(OrderStatusManagerActivity.this, NewRobOrderDetailsActivity.class);
                } else if (flag.equals("3") && (status.equals("27") || status.equals("28"))) {
                    intent = new Intent(OrderStatusManagerActivity.this, ToquoteAcitivity.class);
                } else {
                    intent = new Intent(OrderStatusManagerActivity.this, OrderActivity.class);
                }
                intent.putExtra("type", mType);
                intent.putExtra("taskId", mData.get(arg2 - 1).getId());
                startActivity(intent);

				/*
                 * switch (key) { case value:
				 *
				 * break;
				 *
				 * default: break; }
				 *
				 * Task task = mData.get(arg2 - 1); Intent intent; String flag =
				 * task.getFlag(); String status = task.getStatus(); if
				 * (status.equals("28") && (flag.equals("1") ||
				 * flag.equals("2"))) { intent = new Intent(
				 * OrderStatusManagerActivity.this,
				 * RobOrderDetailsActivity.class); } else if (flag.equals("3")
				 * && (status.equals("27") || status.equals("28"))) { intent =
				 * new Intent( OrderStatusManagerActivity.this,
				 * ReleaseOrderDetailsActivity.class); } else if
				 * (flag.equals("1") || flag.equals("2")) {// 驾校服务，违章服务，....
				 * intent = new Intent( OrderStatusManagerActivity.this,
				 * BusinessOrderDetaisActivity.class); } else { intent = new
				 * Intent( OrderStatusManagerActivity.this,
				 * OverseasOrderDetailsActivity.class); } Bundle bundle = new
				 * Bundle(); bundle.putSerializable(Constant.ORDER_SER_KEY,
				 * task); intent.putExtras(bundle); intent.putExtra("taskId",
				 * task.getId()); startActivity(intent);
				 */

            }
        });
    }

    /**
     * 根据相应状态获取订单列表
     */
    private void getOrder(final boolean refresh) {
        int size = mAdapter.getCount();
        if (refresh) {
            mData.clear();
            mAdapter.notifyDataSetChanged();
            size = 0;
        }
        mPullToRefreshListView.setRefreshing();
        AjaxParams params = new AjaxParams();
//        if (!TextUtils.isEmpty(mStatus)) {
//            params.put("status", mStatus);
//        }
//        if (!TextUtils.isEmpty(mStartTime)) {
//            params.put("startTime", mStartTime);
//        }
//        if (!TextUtils.isEmpty(mEndTime)) {
//            params.put("endTime", mEndTime);
//        }
//        if (!TextUtils.isEmpty(mWhere)) {// mWhere参数待传
//            params.put("where", mWhere);
//        }
        params.put("statusFlag", mStatus);
        params.put("start", String.valueOf(size));
        if (mOrderStatus == Constant.ORDERSTATUSMANAGE.WAIT_PAY || mOrderStatus == Constant.ORDERSTATUSMANAGE.IN_SERVICE) {
            isNetWork = true;
            mHeaderRightTV.setVisibility(View.VISIBLE);//VISIBLE
            mHeaderRightTV.setText("编辑");
            del.setVisibility(View.GONE);
            sub_header_bar_left_tv.setVisibility(View.GONE);
            mBackIBtn.setVisibility(View.VISIBLE);
        }
        if (mAdapter != null)
            mAdapter.setOperating(false);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // if (Task.parseList(msg.obj.toString()).size() == 0) {
                // // mOrderEmptyTV.setVisibility(View.VISIBLE);
                // // mPullToRefreshListView.onRefreshComplete();// 关闭刷新
                // } else {
                isNetWork = false;
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mIsRefreshing = false;
                        mPullToRefreshListView.onRefreshComplete();
                        List<ValueTask> mRefreshData = ValueTask.parseList(msg.obj.toString());

                        if (refresh) {
                            mPullToRefreshListView.onLoadMore();
                            mData.clear();
                            if (msg.obj.toString().equals("[]")) {
                                mOrderEmptyTV.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mPullToRefreshListView.onLoadMore();
                            // 是加载后面的订单
                            if (mRefreshData == null) {
                                ToastUtil.toastShow(OrderStatusManagerActivity.this, "没有更多数据了");
                                return;
                            }
                        }

                        if (mRefreshData.size() < 1) {
                            mPullToRefreshListView.onLoadComplete();
                        }
                        mData.addAll(mRefreshData);
                        bTBOrderSort(mData);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderStatusManagerActivity.this, ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        mPullToRefreshListView.onRefreshComplete();
                        mIsRefreshing = false;
                        break;
                }
            }
            // }
        }, params, NetworkUtil.GET_ALL_ORDER_FOR_BUS, false, 1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;

            case R.id.btn_top:
                mPullToRefreshListView.getRefreshableView().setSelection(0);
                break;
            case R.id.sub_header_bar_right_tv:// 编辑按钮
                if (isNetWork || mAdapter == null || mPullToRefreshListView.getState() != State.RESET) {
                    ToastUtil.toastShow(this, "数据加载中");
                    return;
                }
                if (mAdapter.getOperating()) {// 编辑状态
                    mAdapter.setOperating(false);
                    mHeaderRightTV.setText("编辑");
                    mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                    del.setVisibility(View.GONE);
                    sub_header_bar_left_tv.setVisibility(View.GONE);
                    mBackIBtn.setVisibility(View.VISIBLE);
                } else {
                    mAdapter.setOperating(true);
                    mHeaderRightTV.setText("取消");
                    mPullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    del.setVisibility(View.VISIBLE);
                    sub_header_bar_left_tv.setVisibility(View.VISIBLE);
                    mBackIBtn.setVisibility(View.GONE);
                }
                break;
            case R.id.sub_header_bar_left_tv:
                mAdapter.selectAll();
                break;
            case R.id.del:
                String ids = mAdapter.getCheckItemIds();// 取得被选中的List(可修改)
                if (TextUtils.isEmpty(ids)) {
                    ToastUtil.toastShow(this, "未选择删除项");
                    return;
                }
                AjaxParams params = new AjaxParams();
                params.put("ids", ids);
                MyLog.i("YUY", "----------------------" + ids);
                requestNet(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case Constant.NET_DATA_SUCCESS:
                                ToastUtil.toastShow(OrderStatusManagerActivity.this, "删除成功");
                                getOrder(true);
                                mAdapter.setOperating(false);
                                mHeaderRightTV.setText("编辑");
                                mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                                del.setVisibility(View.GONE);
                                sub_header_bar_left_tv.setVisibility(View.GONE);
                                mBackIBtn.setVisibility(View.VISIBLE);
                                break;
                            case Constant.NET_DATA_FAIL:
                                ToastUtil.toastShow(OrderStatusManagerActivity.this, ((DataError) msg.obj).getErrorMessage());
                                break;
                            default:
                                ToastUtil.toastShow(OrderStatusManagerActivity.this, "删除失败");
                                break;
                        }
                    }
                }, params, NetworkUtil.DELETA_TASK, false, 1);
                break;
            default:
                break;
        }
    }

    /**
     * 根据点击的订单查询数据
     * @param mOrderStatus
     */
    public void orderType(int mOrderStatus){
        switch (mOrderStatus) {
            case Constant.ORDERSTATUSMANAGE.TODAY_ORDER:// 今日订单
                mTitleTV.setText("待处理订单");
                if (mOrderType == Constant.ORDER_TYPE.GET_ORDER) {
//                    mWhere = "1,2";// mWhere接口带更改。
                    // mStartTime = DateTimeUtil.getSystemTime();
                    // mEndTime = DateTimeUtil.getTomoData();
                    // MyLog.i("YUY", "开始时间 = " + DateTimeUtil.getSystemTime() +
                    // " = "
                    // + DateTimeUtil.getTomoData());
                    mStatus = "1";
                } else {
                    mStatus = "7";
//                    mWhere = "3";
                }

                break;
            case Constant.ORDERSTATUSMANAGE.NO_COMFIRM_PRICE:// 未确认费用
                mTitleTV.setText("服务中订单");
                if (mOrderType == Constant.ORDER_TYPE.GET_ORDER) {
//                    mWhere = "1,2";
                    // mStatus = "5,6,18,19,24";
                /*
                 * modify by 曾阳松 2016/4/25 功能 #895 由于门店录单与钱包余额冲突：需要进行拆分。 取消24
				 */
                    mStatus = "2";
                } else {
                    mStatus = "8";
//                    mWhere = "3";
                }

                break;
            case Constant.ORDERSTATUSMANAGE.WAIT_PAY:// 已完成订单
                mHeaderRightTV.setVisibility(View.VISIBLE);//VISIBLE
                mHeaderRightTV.setText("编辑");
                mTitleTV.setText("已完成订单");
                if (mOrderType == Constant.ORDER_TYPE.GET_ORDER) {
                    // mStatus = "7,9,10,20,22";
                /*
                 * modify by 曾阳松 2016/4/25 功能 #895 由于门店录单与钱包余额冲突：需要进行拆分。 取消22
				 */
                    // 加入29结算入余额
                    mStatus = "3";
//                    mWhere = "3";
                } else {
                    mStatus = "9";
//                    mWhere = "3";
                }
                break;
            case Constant.ORDERSTATUSMANAGE.IN_SERVICE:// 正在服务
                mHeaderRightTV.setVisibility(View.VISIBLE);//VISIBLE
                mHeaderRightTV.setText("编辑");
                mTitleTV.setText("已取消订单");
                if (mOrderType == Constant.ORDER_TYPE.GET_ORDER) {
                    mStatus = "4";
//                    mWhere = "1,2";
                } else {
                    mStatus = "10";
//                    mWhere = "3";
                }

                break;
            case Constant.ORDERSTATUSMANAGE.CUSTOMERS_CHEDAN:// 退款中订单
                mTitleTV.setText("退款中订单");
                if (mOrderType == Constant.ORDER_TYPE.GET_ORDER) {
                    mStatus = "5";
//                    mWhere = "1,2";
                } else {
                    mStatus = "11";
//                    mWhere = "3";
                }

                break;
            case Constant.ORDERSTATUSMANAGE.CUSTOMERS_EVALUATE:// 纠纷中订单
                mTitleTV.setText("纠纷中订单");
                if (mOrderType == Constant.ORDER_TYPE.GET_ORDER) {
                    mStatus = "6";
//                    mWhere = "1,2";
                } else {
                    mStatus = "12";
//                    mWhere = "3";
                }
                break;
            default:
                break;
        }
    }


    /**
     * 对商家订单进行排序
     */
    private void bTBOrderSort(List<ValueTask> mTasks) {
        if (mTasks != null) {
            Collections.sort(mTasks, new Comparator<ValueTask>() {

                @Override
                public int compare(ValueTask lhs, ValueTask rhs) {
                    Date date1 = DateTimeUtil.stringToDate(lhs.getPubishTime());
                    Date date2 = DateTimeUtil.stringToDate(rhs.getPubishTime());
                    // 对日期字段进行升序，如果欲降序可采用after方法
                    if (date1.before(date2)) {
                        return 1;
                    }
                    return -1;
                }
            });

        }

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getOrder(true);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getOrder(false);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrder(true);
    }
}
