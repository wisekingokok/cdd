package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.TimelineAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.LogisticsCompany;
import com.chewuwuyou.app.bean.LogisticsInformation;
import com.chewuwuyou.app.bean.Offer;
import com.chewuwuyou.app.bean.Ordertime;
import com.chewuwuyou.app.bean.RefundReason;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.bean.TracesEntity;
import com.chewuwuyou.app.tools.EditInputBiaoQing;
import com.chewuwuyou.app.tools.MyListView;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.OrderStateUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.WLUtil;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @describe:订单
 * @author:liuchun
 */
public class OrderActivity extends CDDBaseActivity implements OnClickListener {

    @ViewInject(id = R.id.order_status)//订单状态
    private TextView mOrderStatus;
    @ViewInject(id = R.id.ordear_income)//订单金额
    private EditText mOrdearIncome;
    @ViewInject(id = R.id.ordear_service_fee)//是否显示平台服务费
    private LinearLayout mOrdearServiceFee;
    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//订单标题
    private TextView mTitel;
    @ViewInject(id = R.id.sub_header_bar_right_tv)//更多
    private TextView mSubHeaderBarRight;
    @ViewInject(id = R.id.ordear_transaction_money)//订单金额
    private TextView mOrdearTransactionMoney;
    @ViewInject(id = R.id.order_portrait)//订单联系人头像
    private ImageView mOrderPortrait;
    @ViewInject(id = R.id.ordear_contact_name)//订单联系人昵称
    private TextView mOrdearName;
    @ViewInject(id = R.id.order_news)//聊天
    private ImageButton mOrderNews;
    @ViewInject(id = R.id.order_telephone)//订单联系人电话
    private ImageButton mOrderTelephone;
    @ViewInject(id = R.id.fill_logisics)//填写物流信息
    private LinearLayout mFillLogisics;
    @ViewInject(id = R.id.order_logisics_name)//物流公司名称
    private TextView mOrderLogisicsName;
    @ViewInject(id = R.id.logisics_flowing)//物流信息
    private TextView mLogisicsFlowing;
    @ViewInject(id = R.id.logisics_time)//物流信息
    private TextView mLogisicsTime;
    @ViewInject(id = R.id.logistics_details_lay)//点击查看更多物流详情
    private TextView mLogisticsDetailsLay;
    @ViewInject(id = R.id.return_logisics)//填写回寄物流信息
    private LinearLayout mReturnLogisics;
    @ViewInject(id = R.id.business_order_huise)//间距
    private TextView mBusinessOrderHuise;
    @ViewInject(id = R.id.order_no)//订单号
    private TextView mOrderNo;
    @ViewInject(id = R.id.order_serve_project)//服务项目
    private TextView mOrderServeProject;
    @ViewInject(id = R.id.order_serve_region)//办理地区
    private TextView mOrderServeRegion;
    @ViewInject(id = R.id.order_bill_need)//票据信息
    private TextView mOrderBillNeed;
    @ViewInject(id = R.id.order_daiqren_establish)//订单创建时间
    private TextView mOrderDaiqrenEstaBlish;
    @ViewInject(id = R.id.order_img_line)//虚线
    private ImageView mOrderImgLine;
    @ViewInject(id = R.id.list_time_shaft)//订单流水
    private MyListView mListTimeShaft;
    @ViewInject(id = R.id.order_state_open)// 展开 隐藏
    private ImageView mOrderStateOpen;
    @ViewInject(id = R.id.apply_refund)//根据状态判断是否显示或隐藏
    private LinearLayout mPpplyRefund;
    @ViewInject(id = R.id.order_state_reason)//申请退款理由
    private TextView mOrderStateReason;
    @ViewInject(id = R.id.refund_agreement_ly)//是否显示退款协议
    private LinearLayout mRefundAgreementLy;
    @ViewInject(id = R.id.refund_agreement_tv)//退款协议
    private TextView mRefundAgreementTv;
    @ViewInject(id = R.id.order_confirm_linear)//根据状态显示按钮
    private LinearLayout mOrderConfirmLinear;
    @ViewInject(id = R.id.order_pending_payment_confirm)//根据状态显示按钮
    private Button mOrderPendingPaymentConfirm;
    @ViewInject(id = R.id.network_request)//网络动画
    private LinearLayout mNetworkRequest;


    @ViewInject(id = R.id.business_order_framen)//订单信息详情
    private FrameLayout mBusinessOrder;
    @ViewInject(id = R.id.common_order_time)//是否显示列表
    private LinearLayout mCommonOrderTime;
    @ViewInject(id = R.id.logistics)//是否显示物流信息
    private LinearLayout mLogistics;
    @ViewInject(id = R.id.logisics_serial)//物流信息
    private LinearLayout mLogisicsSerial;
    @ViewInject(id = R.id.ordet_price)// 待确认是否显示修改价格
    private LinearLayout mOrdetPrice;
    @ViewInject(id = R.id.order_daiqren)// 待确认订单创建时间-是否隐藏
    private LinearLayout mOrderDaiqren;
    @ViewInject(id = R.id.ordet_button_price)// 确认修改价格
    private Button mOrdetButtonPrice;
    @ViewInject(id = R.id.order_qq_phone)// 联系电话
    private LinearLayout mOrderPhone;
    @ViewInject(id = R.id.order_qq_mailbox)// 邮箱
    private LinearLayout mOrderMailbox;
    @ViewInject(id = R.id.ordear_start_name_money)// 订单金额
    private TextView mOrdearStartNameMoney;
    @ViewInject(id = R.id.order_income)// 订单收入
    private TextView mOrderIncome;
    @ViewInject(id = R.id.order_status_title)//订单状态
    private TextView mOrderStatusTitle;
    @ViewInject(id = R.id.order_top)//订单状态
    private LinearLayout mOrderTop;
    @ViewInject(id = R.id.order_gao)//线条高度
    private TextView mOrderGao;
    @ViewInject(id = R.id.order_scroll)//线条高度
    private ScrollView mOrderScroll;
    @ViewInject(id = R.id.abnormal_reason)//异常
    private TextView mAbnormalReason;
    @ViewInject(id = R.id.network_abnormal_layout)
    private LinearLayout mNetworkAbnormalLayout;
    @ViewInject(id = R.id.network_again)
    private TextView mNetworkAgain;
    @ViewInject(id = R.id.fuwufei_tv)
    private TextView mServicePriceTV;//服务费
    @ViewInject(id = R.id.order_status_img)
    private ImageView mOrderStatusImg;//服务费

    private int orderStart;//判断是否有运营进入

    private Task mTask;//订单实体信息
    private List<LogisticsCompany> mLogisticsCompanies;//物流公司集合
    private LogisticsInformation information;//物流信息实体
    private List<Ordertime> orderStateList;// 订单状态集合，用于用户点击展开
    private List<Ordertime> orderStateListEr;// 订单状态集合，用于用户点击隐藏
    private TimelineAdapter timelineAdapter;//订单流水适配器
    private int status, flag;//status订单状态，flag判断是接单方还是发单方
    private String type;
    private List<RefundReason> mListFund;// 退款理由集合
    private CommonAdapter<RefundReason> mCommonAdapter;// 退款理由适配器
    private String reason;// 判断是否选中退款理由
    private boolean mHideboolean = true;// 判断是否显示隐藏订单时间
    private List<TracesEntity> mLogisitcsTraces;//物流详情集合
    private int mDistanceBottom;
    private int mDistance = 0;
    public static OrderActivity orderActivity;
    private String maxPrice, minPrice;//最大确认价格、最小确认价格
    private String servicePrice;//服务费
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bun = msg.getData();
            switch (msg.what) {
                case 1:
                    mLogistics.setVisibility(View.VISIBLE);
                    mLogisicsFlowing.setText(bun.getString("AcceptStation"));//物流信息
                    mLogisicsTime.setText(bun.getString("AcceptTime"));//物流时间
                    ConnectedLogistics(status);
                    break;
                case 2:
                    mLogistics.setVisibility(View.VISIBLE);
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogisicsFlowing.setText("暂无物流信息");

                    for (int i = 0; i < mLogisticsCompanies.size(); i++) {
                        if (mLogisticsCompanies.get(i).getCommpanyCode().equals(bun.getString("ShipperCode"))) {
                            mOrderLogisicsName.setText(mLogisticsCompanies.get(i).getCommpanyName() + "    " + bun.getString("LogisticCode"));
                        }
                    }

                    if (flag != 3 && status == Constant.ORDER_START_SERVICE_COMPLETE && !mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("") && mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {//服务完成
                        mLogisicsSerial.setVisibility(View.VISIBLE);
                        mLogistics.setVisibility(View.VISIBLE);
                        mFillLogisics.setVisibility(View.GONE);
                        mReturnLogisics.setVisibility(View.VISIBLE);
                    } else if (flag != 3 && status == Constant.ORDER_START_SERVICE_COMPLETE && !mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("") && !mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                        mLogisicsSerial.setVisibility(View.VISIBLE);
                        mLogistics.setVisibility(View.VISIBLE);
                        mFillLogisics.setVisibility(View.GONE);
                        mReturnLogisics.setVisibility(View.GONE);
                    } else {
                        mLogisicsSerial.setVisibility(View.VISIBLE);
                        mLogistics.setVisibility(View.VISIBLE);
                        mFillLogisics.setVisibility(View.GONE);
                        mReturnLogisics.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    mBusinessOrder.setVisibility(View.GONE);
                    mNetworkRequest.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(getIntent().getStringExtra("taskId"))) {
                        AjaxParams params = new AjaxParams();
                        params.put("taskId", getIntent().getStringExtra("taskId"));
                        networkOrder(params);
                    }
                    break;
                default:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);
        orderActivity = this;
        initView();// 初始化控件
        initData();// 业务逻辑处理
        initEvent();// 事件监听
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mTitel.setText("订单详情");
        mListTimeShaft.setFocusable(false);// 让listview失去焦点
        mListTimeShaft.smoothScrollToPosition(0, 20);// 设置显示的位置

        mLogisticsCompanies = LogisticsCompany.parseList(getFromAssets("wuliu.json"));//物流公司名称
        information = new LogisticsInformation();


        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int density = metric.densityDpi;
        mDistanceBottom = 68 * density / 240;
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:// 返回上一页
                if (AppManager.isExsitMianActivity(MainActivityEr.class, OrderActivity.this) == false) {//判断首页是否在堆栈中
                    startActivity(new Intent(OrderActivity.this, MainActivityEr.class));
                }
                finishActivity();
                break;
            case R.id.network_again:// 重新加载
                mNetworkRequest.setVisibility(View.VISIBLE);
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(getIntent().getStringExtra("taskId"))) {
                    AjaxParams params = new AjaxParams();
                    params.put("taskId", getIntent().getStringExtra("taskId"));
                    networkOrder(params);
                }
                break;
            case R.id.return_logisics:// 寄物流信息
                intent = new Intent(this, LogistisCompanyAtivtiy.class);
                intent.putExtra("taskId", mTask.getId() + "");
                intent.putExtra("flag", flag + "");
                intent.putExtra("expressNo", mTask.getExpressNo());
                intent.putExtra("companyNo", mTask.getCompanyNo());
                startActivity(intent);
                break;
            case R.id.fill_logisics:// 填写物流信息)
                intent = new Intent(this, LogistisCompanyAtivtiy.class);
                intent.putExtra("taskId", mTask.getId() + "");
                intent.putExtra("flag", flag + "");
                intent.putExtra("expressNo", mTask.getExpressNo());
                intent.putExtra("companyNo", mTask.getCompanyNo());
                startActivity(intent);
                break;
            case R.id.logisics_serial:// 物流详情
                intent = new Intent(this, LogisticsDetailsActivtiy.class);
                intent.putExtra("expressNo", mTask.getExpressNo());
                intent.putExtra("companyNo", mTask.getCompanyNo());
                intent.putExtra("revExpressNo", mTask.getRevExpressNo());
                intent.putExtra("revCompanyNo", mTask.getRevCompanyNo());
                startActivity(intent);
                break;
            case R.id.sub_header_bar_right_tv:// 更多
                if (mSubHeaderBarRight.getText().toString().equals("取消订单")) {
                    orderDialog("", "你是否取消订单", "");
                } else if (mSubHeaderBarRight.getText().toString().equals("申请退款")) {
                    createWalletPayDialog(this);// 申请退款
                } else if (mSubHeaderBarRight.getText().toString().equals("联系客服")) {
                    createListViewqDialog(this);// 联系客服
                }
                break;
            case R.id.refund_agreement_tv:// 点击进入条款
                intent = new Intent(OrderActivity.this, AgreementActivity.class);
                intent.putExtra("type", 5);
                startActivity(intent);
                break;
            case R.id.order_pending_payment_confirm:// 底部按钮
                if (mOrderPendingPaymentConfirm.getText().equals("付款")) {
                    intent = new Intent(this, CommonPayActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable(Constant.TASK_SER, mTask);
                    intent.putExtra("status", mTask.getStatus());
                    if (!TextUtils.isEmpty(mTask.getUserDescription())) {
                        intent.putExtra("orderDescription", "");
                    } else {
                        intent.putExtra("orderDescription", mTask.getProjectName());
                    }
                    intent.putExtra("payMoney",
                            String.valueOf(mTask.getPaymentAmount()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (mOrderPendingPaymentConfirm.getText().equals("确认报价")) {
                    if (mOrdearIncome.getText().toString().contains(".")) {
                        String sdsa = mOrdearIncome.getText().toString().substring(mOrdearIncome.getText().toString().indexOf(".") + 1);
                        if (sdsa.length() > 2) {
                            ToastUtil.toastShow(OrderActivity.this, "输入金额保留两位小数");
                            return;
                        }
                    }
                    //motify start by yuyong 20160919修改最大确认价格和最小确认价格 从服务器获取
                    if (mOrdearIncome.getText().toString().equals(".")) {
                        ToastUtil.toastShow(this, "订单金额输入不符格");
                    } else if (TextUtils.isEmpty(mOrdearIncome.getText().toString().trim()) || Double.valueOf(mOrdearIncome.getText().toString().trim()) < Double.valueOf(minPrice)) {
                        ToastUtil.toastShow(this, "订单金额不低于" + minPrice + "元");
                    } else if (Double.parseDouble(mOrdearIncome.getText().toString()) > Double.valueOf(maxPrice)) {
                        ToastUtil.toastShow(this, "订单金额不能大于" + maxPrice + "元");
                        //motify end by yuyong 20160919修改最大确认价格和最小确认价格 从服务器获取
                    } else {
                        if (mTask.getProvideBill().equals("1")) {
                            ConfirmDialog("车当当提醒你", "此单需要您提供相关办理票据并确定报价" + mOrdearIncome.getText().toString() + "元", "");
                        } else {
                            orderDialog("报价", "请问是否报价", "确认报价");
                        }
                    }
                } else if (mOrderPendingPaymentConfirm.getText().equals("去评价")) {
                    intent = new Intent(this, OrderEvaluateActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable(Constant.TASK_SER, mTask);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (mOrderPendingPaymentConfirm.getText().equals("查看评价")) {
                    if (mTask.getIsPinglun().equals("true")) {
                        intent = new Intent(this, OrderDetailsSee.class);
                        intent.putExtra("id", mTask.getId());
                        startActivity(intent);
                    } else {
                        ToastUtil.toastShow(OrderActivity.this, "用户暂未评价");
                    }
                } else if (mOrderPendingPaymentConfirm.getText().equals("确认完成")) {
                    orderDialog("提示", "是否确认完成", "确认完成");
                } else if (mOrderPendingPaymentConfirm.getText().equals("完成服务")) {
                    orderDialog("提示", "是否确认完成服务", "完成服务");
                } else if (mOrderPendingPaymentConfirm.getText().equals("开始服务")) {
                    orderDialog("提示", "是否确认开始服务", "开始服务");
                } else if (mOrderPendingPaymentConfirm.getText().equals("取消退款")) {
                    orderDialog("提示", "是否取消退款", "取消退款");
                } else if (mOrderPendingPaymentConfirm.getText().equals("同意退款")) {
                    orderDialog("提示", "是否同意退款", "同意退款");
                }
                break;
            case R.id.order_news://聊天
                AppContext.createChat(OrderActivity.this, flag == 3 ? mTask.getFacilitatorId() : mTask.getUserId());
                break;
            case R.id.order_telephone:// 电话
                DialogUtil.call(mTask.getExactPhone(), this);
                break;
            case R.id.order_state_open:// 显示隐藏订单时间
                orderStateListEr.clear();
                if (mHideboolean == true) {
                    for (int i = 0; i < orderStateList.size(); i++) {
                        orderStateListEr.add(new Ordertime(orderStateList.get(i)
                                .getTime(), orderStateList.get(i)
                                .getOrderresulttype(), orderStateList.get(i)
                                .getOrderresult()));
                    }
                    mOrderStateOpen
                            .setImageResource(R.drawable.order_state_stop_img);
                    if (orderStart == 33) {
                        mAbnormalReason.setVisibility(View.VISIBLE);

                        if (flag == 2 || flag == 1) {
                            if (status == Constant.ORDER_START_ORDER_BALANCE) {
                                mAbnormalReason.setText("处理结果：付款，如有疑问请联系客服：4008333955");
                            } else if (status == Constant.ORDER_START_REFUND_COMLETE2) {
                                mAbnormalReason.setText("全额退款，如有疑问请联系客服：4008333955");
                            }
                        } else {
                            if (status == Constant.ORDER_START_ORDER_BALANCE) {
                                mAbnormalReason.setText("处理结果：付款，如有疑问请联系客服：4008333955");
                            } else if (status == Constant.ORDER_START_REFUND_COMLETE2) {
                                mAbnormalReason.setText("全额退款，如有疑问请联系客服：4008333955");
                            }
                        }
                    } else if (Constant.ORDER_START_CANCEL_ORDER == status && !TextUtils.isEmpty(mTask.getReason())) {
                        mAbnormalReason.setVisibility(View.VISIBLE);
                        mAbnormalReason.setText(mTask.getReason());
                    } else {
                        mAbnormalReason.setVisibility(View.GONE);
                    }
                    mHideboolean = false;
                } else {
                    if (orderStateList.size() > 0) {
                        orderStateListEr.add(new Ordertime(orderStateList.get(0)
                                .getTime(), orderStateList.get(0)
                                .getOrderresulttype(), orderStateList.get(0)
                                .getOrderresult()));
                    }
                    mOrderStateOpen
                            .setImageResource(R.drawable.order_state_open_img);
                    if (!TextUtils.isEmpty(mTask.getReason())) {
                        mAbnormalReason.setVisibility(View.GONE);
                    }
                    if (orderStart == 33) {
                        mAbnormalReason.setVisibility(View.GONE);
                    } else if (Constant.ORDER_START_CANCEL_ORDER == status && !TextUtils.isEmpty(mTask.getReason())) {
                        mAbnormalReason.setVisibility(View.GONE);
                    } else {
                        mAbnormalReason.setVisibility(View.GONE);
                    }
                    mHideboolean = true;
                }
                timelineAdapter.notifyDataSetChanged();// 刷新适配器
            case R.id.ordet_button_price:// 待确认-修改价格

                break;
            default:
                break;
        }
    }

    /**
     * 业务逻辑处理
     */
    @Override
    protected void initData() {
        orderStateList = new ArrayList<Ordertime>();
        orderStateListEr = new ArrayList<Ordertime>();
        orderStateListEr.clear();
        orderStateList.clear();
        timelineAdapter = new TimelineAdapter(this, orderStateListEr);// 绑定订单事件适配器
        mListTimeShaft.setDividerHeight(0);
        mListTimeShaft.setAdapter(timelineAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Message messag = new Message();
        messag.what = 3;
        handler.sendMessage(messag);

    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mRefundAgreementTv.setOnClickListener(this);
        mReturnLogisics.setOnClickListener(this);
        mFillLogisics.setOnClickListener(this);
        mLogisicsSerial.setOnClickListener(this);
        mSubHeaderBarRight.setOnClickListener(this);
        mOrderPendingPaymentConfirm.setOnClickListener(this);
        mOrderNews.setOnClickListener(this);
        mOrderTelephone.setOnClickListener(this);
        mOrderStateOpen.setOnClickListener(this);
        mNetworkAgain.setOnClickListener(this);
    }

    /**
     * 请求接口访问网络
     *
     * @param params
     */
    private void networkOrder(AjaxParams params) {

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        orderStateListEr.clear();
                        orderStateList.clear();
                        mTask = Task.parse(msg.obj.toString());

                        if (mTask == null) {
                            mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                            mNetworkAbnormalLayout.setVisibility(View.VISIBLE);

                        } else {
                            mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                            mNetworkAbnormalLayout.setVisibility(View.GONE);
                            mBusinessOrder.setVisibility(View.VISIBLE);
                            try {
                                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                                /**
                                 * status 订单状态 flag 判断是发单方还是接单方
                                 */
                                JSONArray array = jsonObject.getJSONArray("timeAndStatus");
                                List<Ordertime> stateList = new ArrayList<Ordertime>();
                                if (array != null) {
                                    for (int i = 0; i < array.length(); i++) {
                                        try {
                                            stateList.add(new Ordertime(array.getJSONObject(i).getString("time"), array.getJSONObject(i).getString("targetStatus"), array.getJSONObject(i).getString("originalStat")));
                                            if (Integer.parseInt(array.getJSONObject(i).getString("targetStatus")) == 33 || Integer.parseInt(array.getJSONObject(i).getString("originalStat")) == 33) {
                                                orderStart = 33;
                                            }
                                        } catch (Exception e) {
                                            stateList.add(new Ordertime(array.getJSONObject(i).getString("time"), array.getJSONObject(i).getString("targetStatus"), "0"));
                                            if (Integer.parseInt(array.getJSONObject(i).getString("targetStatus")) == 33) {
                                                orderStart = 33;
                                            }
                                        }

                                    }
                                    for (int j = 0; j < stateList.size(); j++) {
                                        orderStateList.add(new Ordertime(stateList.get(stateList.size() - 1 - j).getTime(), stateList.get(stateList.size() - 1 - j).getOrderresulttype(), stateList.get(stateList.size() - 1 - j).getOrderresult()));
                                    }
                                    timelineAdapter.notifyDataSetChanged();
                                }
                                if (orderStateList.size() > 0) {
                                    orderStateListEr.add(new Ordertime(orderStateList.get(0).getTime(), orderStateList.get(0).getOrderresulttype(), orderStateList.get(0).getOrderresult()));
                                }
                                mOrderStateOpen.setImageResource(R.drawable.order_state_open_img);
                                status = Integer.parseInt(jsonObject.getString("status"));
                                flag = Integer.parseInt(jsonObject.getString("flag"));
                                //motify start by yuyong 20160919服务价格、服务费限制从服务器取
                                maxPrice = jsonObject.getString("max");//取得最大的确认价格
                                minPrice = jsonObject.getString("min");//取得最小的确认价格
                                servicePrice = jsonObject.getString("fuwufei");//取得服务费
                                //motify end by yuyong 20160919服务价格、服务费限制从服务器取
                                startOrder(status, flag);//根据不同的订单状态显示不同的信息
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        if (((DataError) msg.obj).getErrorCode() == 621) {// 已与其他服务商成交
                            // 无权访问该订单
                            finishActivity();
                        }
                        mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                        mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                        mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

        }, params, NetworkUtil.QUERY_A_TASK, false, 1);
    }

    /**
     * @param id   判断当前订单状态
     * @param flag 等于3是发单方，不等于3是接单方
     */
    public void startOrder(int id, int flag) {

        if (flag != 3) {
            mOrderStatusImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.serice_flow_business));
        } else {
            mOrderStatusImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.me_order_icon));
        }
        mSubHeaderBarRight.setVisibility(View.GONE);// 标题栏右边按钮

        OrderStateUtil.orderStatusShow(id, flag, mOrderStatus);//根据角色和订单状态显示
        DecimalFormat df = new DecimalFormat("######0.00");

        mOrdearIncome.setText(df.format(mTask.getPaymentAmount()) + "");// 订单金额

        mOrdearTransactionMoney.setText(df.format(mTask.getPaymentAmount()) + "");//订单金额
        ImageUtils.displayImage(mTask.getUrl(), mOrderPortrait, 10, R.drawable.user_fang_icon, R.drawable.user_fang_icon);//联系人头像
        mOrdearName.setText(mTask.getName());//联系人昵称
        mOrderNo.setText(mTask.getOrderNum());//订单号
        mServicePriceTV.setText("-" + servicePrice + "元");
        mOrderServeProject.setText(ServiceUtils.getProjectName(mTask.getProjectName()));//服务项目

        if (flag == 3) {
            switch (Integer.parseInt(mTask.getExceptionFlag())) {
                case Constant.Order_Abnormal.ORDER_ONE:

                    break;
                case Constant.Order_Abnormal.ORDER_TWO:
                    Intent intent = new Intent(OrderActivity.this, AbnormalRefund.class);
                    intent.putExtra("money", mOrdearIncome.getText().toString());
                    intent.putExtra("tasId", mTask.getId());
                    startActivity(intent);
                    break;
                case Constant.Order_Abnormal.ORDER_THREE:

                    break;
            }
        }
        /**
         * 服务地区
         */
        if (TextUtils.isEmpty(mTask.getTaskLocation())) {
            if (TextUtils.isEmpty(mTask.getTaskCity())) {
                mOrderServeRegion.setText(mTask.getTaskProvince()
                        + mTask.getTaskCity() + mTask.getTaskDistrict());
            } else {
                mOrderServeRegion.setText(mTask.getTaskProvince() + ","
                        + mTask.getTaskCity() + "," + mTask.getTaskDistrict());
            }
        } else {
            mOrderServeRegion.setText(mTask.getTaskLocation());
        }
        /**
         * 是否需要票据
         */
        if (mTask.getProvideBill().equals("1")) {
            mOrderBillNeed.setText("需要票据");
        } else {
            mOrderBillNeed.setText("不需要票据");
        }
        mOrderDaiqrenEstaBlish.setText(mTask.getPubishTime().substring(0, 19));// 订单创建时间

        if (!mTask.getFlag().equals("3") && !mTask.getFacilitatorId().equals(CacheTools.getUserData("userId")) && !mTask.getFacilitatorId().equals("0")
                && !TextUtils.isEmpty(mTask.getFacilitatorId())
                && type.equals("1")) {// B类商家接单失败
            if (!TextUtils.isEmpty(mTask.getUrl())) {
                ImageUtils.displayImage(mTask.getUrl(), mOrderPortrait, 10, R.drawable.user_fang_icon, R.drawable.user_fang_icon);
            }
            mBusinessOrder.setVisibility(View.VISIBLE);
            mOrdearIncome.setFocusable(false);
            mCommonOrderTime.setVisibility(View.GONE);// 隐藏时间
            mOrderStatus.setText("已与其他服务商成交");
            mOrderStatus.setBackgroundResource(R.drawable.ordear_status_yes);
            mOrderStatus.setTextColor(getResources().getColor(R.color.white));
            getOffers(); // 1、请求我的报价信息
            return;
        }
        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("") && mTask.getRevExpressNo().equals("") && mTask.getRevCompanyNo().equals("")) {
            mLogistics.setVisibility(View.GONE);
        } else if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("") || !mTask.getRevExpressNo().equals("") && !mTask.getRevCompanyNo().equals("")) {
            for (int i = 0; i < mLogisticsCompanies.size(); i++) {
                if (mLogisticsCompanies.get(i).getCommpanyCode().equals(mTask.getRevCompanyNo())) {
                    mOrderLogisicsName.setText(mLogisticsCompanies.get(i).getCommpanyName() + "    " + mTask.getRevExpressNo());
                }
            }

            mLogisicsSerial.setVisibility(View.VISIBLE);
            mLogistics.setVisibility(View.VISIBLE);
        } else if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("") || mTask.getRevExpressNo().equals("") && mTask.getRevCompanyNo().equals("")) {

            for (int i = 0; i < mLogisticsCompanies.size(); i++) {
                if (mLogisticsCompanies.get(i).getCommpanyCode().equals(mTask.getCompanyNo())) {
                    mOrderLogisicsName.setText(mLogisticsCompanies.get(i).getCommpanyName() + "    " + mTask.getExpressNo());
                }
            }
            mLogisicsSerial.setVisibility(View.VISIBLE);
            mLogistics.setVisibility(View.VISIBLE);
        } else {
            mLogistics.setVisibility(View.GONE);
            mLogisicsSerial.setVisibility(View.GONE);
        }
        switch (id) {
            case Constant.ORDER_START_PENDING_CONFIRMATION://待商家确认
                mSubHeaderBarRight.setVisibility(View.VISIBLE);// 标题栏右边按钮
                mSubHeaderBarRight.setText("取消订单");
                mOrderImgLine.setVisibility(View.GONE);
                mOrderGao.setVisibility(View.GONE);
                if (flag != 3) {
                    mOrdearIncome.setFocusable(true);
                    mOrdearIncome.setSelection(mOrdearIncome.getText().length());
                    mOrdearIncome.setBackgroundResource(R.drawable.login_frame);
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务
                    mCommonOrderTime.setVisibility(View.GONE);// 隐藏listview
                    mOrderPendingPaymentConfirm.setVisibility(View.VISIBLE);
                    mOrderPendingPaymentConfirm.setText("确认报价");
                    mOrdetPrice.setVisibility(View.VISIBLE);
                    mOrderDaiqren.setVisibility(View.VISIBLE);// 待确认-显示确认报价
                    mOrdetButtonPrice.setText("修改价格");
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);
                    mOrderImgLine.setVisibility(View.VISIBLE);// 显示线条

                } else {
                    mOrdearIncome.setFocusable(false);
                    mOrderScroll.setPadding(0, 0, 0, mDistanceBottom);
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务
                    mCommonOrderTime.setVisibility(View.GONE);// 隐藏listview
                    mOrderDaiqren.setVisibility(View.VISIBLE);// 显示创建时间
                    mOrderPendingPaymentConfirm.setVisibility(View.GONE);// 待确认按钮是否隐藏
                    mOrderPendingPaymentConfirm.setClickable(false);
                    mOrdetPrice.setVisibility(View.GONE);
                    mOrderPendingPaymentConfirm.setClickable(false);
                    mOrderImgLine.setVisibility(View.VISIBLE);// 显示线条
                }
                break;
            case Constant.ORDER_START_PAYMENTS://代付款
                mOrdearIncome.setFocusable(false);
                mSubHeaderBarRight.setVisibility(View.VISIBLE);// 标题栏右边按钮
                mSubHeaderBarRight.setText("取消订单");
                mOrderImgLine.setVisibility(View.GONE);
                mOrderGao.setVisibility(View.GONE);
                if (flag != 3) {
                    mOrderConfirmLinear.setVisibility(View.GONE);
                } else {
                    mOrderScroll.setPadding(0, 0, 0, mDistanceBottom);
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);
                    mOrderPendingPaymentConfirm.setText("付款");
                }
                break;
            case Constant.ORDER_START_ALREADY_PAID://已付款
                mOrdearIncome.setFocusable(false);
                if (flag != 3) {
                    // 接单方
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);
                    mOrdearStartNameMoney.setText("订单金额");
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);// 底部确认按钮
                    mOrderPendingPaymentConfirm.setText("开始服务");
                    mOrderScroll.setPadding(0, 0, 0, mDistanceBottom);
                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            mLogistics.setVisibility(View.VISIBLE);
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }
                } else {
                    mOrderScroll.setPadding(0, 0, 0, mDistance);
                    // 发单方
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);
                    mSubHeaderBarRight.setVisibility(View.VISIBLE);// 标题栏右边按钮
                    mSubHeaderBarRight.setText("申请退款");
                    mOrdearStartNameMoney.setText("订单金额");
                    mOrderConfirmLinear.setVisibility(View.GONE);// 底部确认按钮s

                    if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                        mLogistics.setVisibility(View.VISIBLE);
                        mFillLogisics.setVisibility(View.VISIBLE);
                        mLogisicsSerial.setVisibility(View.GONE);
                        mReturnLogisics.setVisibility(View.GONE);
                    } else {

                        mFillLogisics.setVisibility(View.GONE);
                        visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                    }
                }
                break;
            case Constant.ORDER_START_SERVICE://服务进行中
                mBusinessOrder.setVisibility(View.VISIBLE);
                mOrdearIncome.setFocusable(false);
                if (flag != 3) {
                    // 接单方
                    mOrderScroll.setPadding(0, 0, 0, mDistanceBottom);
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);//底部按钮
                    mOrderPendingPaymentConfirm.setText("完成服务");
                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            mLogistics.setVisibility(View.VISIBLE);
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }

                } else {
                    mOrderConfirmLinear.setVisibility(View.GONE);// 隐藏底部按钮
                    mSubHeaderBarRight.setVisibility(View.VISIBLE);// 标题栏右边按钮
                    mSubHeaderBarRight.setText("申请退款");
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费

                    if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                        mLogistics.setVisibility(View.VISIBLE);
                        mFillLogisics.setVisibility(View.VISIBLE);
                        mLogisicsSerial.setVisibility(View.GONE);
                        mReturnLogisics.setVisibility(View.GONE);
                    } else {
                        mFillLogisics.setVisibility(View.GONE);
                        visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                    }
                }
                break;
            case Constant.ORDER_START_SERVICE_COMPLETE:// 服务完成
                mOrdearIncome.setFocusable(false);//不可编辑
                if (flag != 3) {
                    mOrdearStartNameMoney.setText("订单金额");
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                    mOrderConfirmLinear.setVisibility(View.GONE);// 隐藏确认按钮
                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.VISIBLE);
                            mReturnLogisics.setVisibility(View.VISIBLE);
                        } else {
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        mLogistics.setVisibility(View.VISIBLE);
                        mReturnLogisics.setVisibility(View.GONE);
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }
                } else {
                    mOrderScroll.setPadding(0, 0, 0, mDistanceBottom);
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);
                    mOrdearStartNameMoney.setText("订单金额");
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                    mSubHeaderBarRight.setVisibility(View.VISIBLE);// 标题栏右边按钮
                    mSubHeaderBarRight.setText("申请退款");
                    mOrderPendingPaymentConfirm.setText("确认完成");

                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }

                }
                break;
            case Constant.ORDER_START_COMPLETE:// 订单完成
                mOrdearIncome.setFocusable(false);//不可编辑
                if (flag != 3) {
                    mOrdearTransactionMoney.setVisibility(View.GONE);// 显示订单金额
                    mOrderIncome.setVisibility(View.VISIBLE);
                    mOrdearServiceFee.setVisibility(View.VISIBLE);// 显示平台服务费
                    //motify start by yuyong 20160919服务费从服务器获取


                    //motify end by yuyong 20160919服务费从服务器获取
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);//底部按钮
                    mOrderPendingPaymentConfirm.setText("查看评价");
                    if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                        mLogistics.setVisibility(View.GONE);
                    } else {
                        visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                    }
                } else {
                    mOrdearTransactionMoney.setVisibility(View.GONE);// 显示订单金额
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);//底部按钮
                    mOrderPendingPaymentConfirm.setText("去评价");
                    if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                        mLogistics.setVisibility(View.GONE);
                    } else {
                        visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                    }
                    mOrderScroll.setPadding(0, 0, 0, mDistanceBottom);
                }
                break;
            case Constant.ORDER_START_ORDER_COMPLETE:// 已完成订单
                mOrdearIncome.setFocusable(false);//不可编辑
                if (flag != 3) {
                    float sd = Float.parseFloat(mTask.getPaymentAmount().toString());
                    double d = sd - Double.parseDouble(servicePrice);

                    mOrdearServiceFee.setVisibility(View.VISIBLE);// 显示平台服务费
                    //motify start by yuyong 20160919服务费从服务器获取

                    mOrdearIncome.setText(df.format(d));
                    //motify end by yuyong 20160919服务费从服务器获取
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);//底部按钮
                    mOrderPendingPaymentConfirm.setText("查看评价");

                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            mLogistics.setVisibility(View.VISIBLE);
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }
                } else {
                    mOrdearTransactionMoney.setVisibility(View.VISIBLE);//底部按钮
                    mOrderIncome.setVisibility(View.VISIBLE);
                    if (mTask.getIsPinglun().equals("true")) {
                        mOrderConfirmLinear.setVisibility(View.GONE);
                    } else {
                        mOrderConfirmLinear.setVisibility(View.VISIBLE);
                        mOrderPendingPaymentConfirm.setText("去评价");
                    }
                    float sd = Float.parseFloat(mTask.getPaymentAmount().toString());
                    mOrdearIncome.setText(sd - Double.parseDouble(servicePrice) + "");// 支付金额
                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }
                }
                break;
            case Constant.ORDER_START_CANCEL_ORDER: // 取消订单成功 ， 对方取消订单
                OrderStateUtil.orderStatusShow(id, flag, mOrderStatusTitle);
                mOrdearIncome.setFocusable(false);
                if (flag != 3) {
                    mOrderConfirmLinear.setVisibility(View.GONE);// 隐藏确认按钮
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                } else {
                    mOrderConfirmLinear.setVisibility(View.GONE);// 隐藏确认按钮
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                    if (mTask.getFacilitatorId().equals("0") || mTask.getFacilitatorId().equals("")) {// B类商家发布的订单在没有任何人接单的情况下取消
                        mOrderTop.setVisibility(View.GONE);
                        mOrderStatusTitle.setVisibility(View.VISIBLE);
                        mBusinessOrderHuise.setVisibility(View.GONE);
                    }
                }
                break;
            case Constant.ORDER_START_APPLY_REFUND: // 申请退款中
                mSubHeaderBarRight.setVisibility(View.VISIBLE);// 标题栏右边按钮
                mSubHeaderBarRight.setText("联系客服");
                mRefundAgreementLy.setVisibility(View.VISIBLE);//显示退款协议
                mOrdearIncome.setFocusable(false);
                if (flag != 3) {
                    // 接单方
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);
                    mPpplyRefund.setVisibility(View.VISIBLE);
                    mOrderScroll.setPadding(0, 0, 0, mDistanceBottom);
                    mOrderPendingPaymentConfirm.setText("同意退款");
                    mOrderStateReason.setText("   " + mTask.getReason());// 显示退款理由

                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            mLogistics.setVisibility(View.VISIBLE);
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }

                } else {
                    // 发单方
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                    mOrderConfirmLinear.setVisibility(View.VISIBLE);
                    mOrderPendingPaymentConfirm.setText("取消退款");
                    mOrderScroll.setPadding(0, 0, 0, mDistanceBottom);
                    mPpplyRefund.setVisibility(View.VISIBLE);
                    mOrderStateReason.setText("   " + mTask.getReason());// 显示退款理由
                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }
                }
                break;
            case Constant.ORDER_START_REFUND_PROCESSING: // 退款处理中
                mOrdearTransactionMoney.setVisibility(View.VISIBLE);// 隐藏订单金额
                mOrdearIncome.setFocusable(false);
                mOrderPendingPaymentConfirm.setVisibility(View.GONE);
                // 发单方
                mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                    if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                        mLogistics.setVisibility(View.GONE);
                    } else {
                        visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                    }
                } else {
                    visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                }
                break;
            case Constant.ORDER_START_ORDER_BALANCE: //结算入余额
                mOrdearTransactionMoney.setVisibility(View.VISIBLE);// 隐藏订单金额
                mOrdearIncome.setFocusable(false);
                if (flag != 3) {
                    mOrderScroll.setPadding(0, 0, 0, mDistanceBottom);
                    mOrderIncome.setVisibility(View.VISIBLE);
                    mOrdearStartNameMoney.setText("订单金额:");
                    mOrdearServiceFee.setVisibility(View.VISIBLE);// 显示平台服务费
                    //motify start by yuyong 20160919服务费从服务器获取

                    //motify end by yuyong 20160919服务费从服务器获取
                    float sd = Float
                            .parseFloat(mTask.getPaymentAmount().toString());
                    double d = sd - Double.parseDouble(servicePrice);
                    mOrdearIncome.setText(df.format(d));

                    if (mTask.getIsPinglun().equals("false")) {
                        mOrderConfirmLinear.setVisibility(View.GONE);
                    } else {
                        mOrderConfirmLinear.setVisibility(View.VISIBLE);
                        mOrderPendingPaymentConfirm.setText("查看评价");
                    }
                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            mLogistics.setVisibility(View.VISIBLE);
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }
                } else {
                    mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                    mOrdearTransactionMoney.setVisibility(View.GONE);
                    mOrderScroll.setPadding(0, 0, 0, mDistanceBottom);

                    if (orderStart == 33) {
                        mOrderConfirmLinear.setVisibility(View.GONE);
                    } else {
                        if (mTask.getIsPinglun().equals("true")) {
                            mOrderPendingPaymentConfirm.setText("查看评价");
                            mOrderConfirmLinear.setVisibility(View.VISIBLE);
                        } else {
                            mOrderConfirmLinear.setVisibility(View.VISIBLE);
                            mOrderPendingPaymentConfirm.setText("去评价");
                        }
                    }

                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }
                }
                break;
            case Constant.ORDER_START_REFUND_COMLETE2:// 退款入余额

                if (orderStart == 33) {
                    mPpplyRefund.setVisibility(View.GONE);
                } else {
                    mPpplyRefund.setVisibility(View.VISIBLE);
                }

                mRefundAgreementLy.setVisibility(View.GONE);
                mOrdearIncome.setFocusable(false);
                // 接单方
                mOrdearServiceFee.setVisibility(View.GONE);// 隐藏平台服务费
                mOrderConfirmLinear.setVisibility(View.GONE);

                mOrderStateReason.setText("   " + mTask.getReason());// 显示退款理由
                if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                    if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                        mLogistics.setVisibility(View.GONE);
                    } else {
                        visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                    }
                } else {
                    visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                }
                break;
            case Constant.ORDER_OPERATE_COMLETE2:// 运营进入
                mOrdearIncome.setFocusable(false);
                mPpplyRefund.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(mTask.getReason())) {
                    mPpplyRefund.setVisibility(View.VISIBLE);
                    mOrderStateReason.setText(mTask.getReason());
                } else {
                    mAbnormalReason.setVisibility(View.GONE);
                }

                if (flag != 3) {
                    // 接单方
                    mOrdearStartNameMoney.setText("订单金额");
                    mRefundAgreementLy.setVisibility(View.GONE);
                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            mLogistics.setVisibility(View.VISIBLE);
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }
                } else {
                    // 发单方
                    mOrdearStartNameMoney.setText("订单金额");
                    if (mTask.getRevCompanyNo().equals("") && mTask.getRevExpressNo().equals("")) {
                        if (mTask.getExpressNo().equals("") && mTask.getCompanyNo().equals("")) {
                            mLogistics.setVisibility(View.GONE);
                        } else {
                            visitLogistics(mTask.getCompanyNo(), mTask.getExpressNo(), flag);
                        }
                    } else {
                        visitLogistics(mTask.getRevCompanyNo(), mTask.getRevExpressNo(), flag);
                    }
                }
                break;

        }
    }

    /**
     * 请求报价信息
     */
    private void getOffers() {
        AjaxParams params = new AjaxParams();
        params.put("taskId", getIntent().getStringExtra("taskId"));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:

                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            List<Offer> mOffers = Offer.parseList(jo.getJSONArray(
                                    "offers").toString());
                            for (int i = 0; i < mOffers.size(); i++) {
                                if (mOffers.get(i).getUserId()
                                        .equals(CacheTools.getUserData("userId"))) {// 我已报价（我已接单）只能进行
                                    // 显示我的报价信息
                                    DecimalFormat df = new DecimalFormat(
                                            "######0.00");
                                    mOrdearIncome.setText(df.format(mOffers.get(i)
                                            .getOffer()) + "");// 支付金额
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderActivity.this, ((DataError) msg.obj).getErrorMessage());
                        break;

                    default:
                        break;
                }
            }
        }, params, NetworkUtil.GET_OFFER, false, 1);
    }


    /**
     * 待确认-报价
     */
    public void getOffer(AjaxParams params) {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        OrderActivity.this.finishActivity();// 关闭activtiy
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.BUSINESS_UPDATE_PRICE, false, 0);
    }

    /**
     * 完成服务
     */
    public void getCompleteService(AjaxParams params) {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        finishActivity();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        ToastUtil.toastShow(OrderActivity.this, "确认完成失败");
                        break;
                }
            }
        }, params, NetworkUtil.FINISH_SERVICE, false, 0);
    }

    /**
     * 确认完成
     */
    @SuppressLint("HandlerLeak")
    public void getServiceComplete(AjaxParams params) {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
//                        try {
//                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                            if (jsonObject.opt("status")!=null&&jsonObject.optString("status").equals("33")) {
//                                finishActivity();
//                            } else {
                        Intent intent = new Intent(OrderActivity.this,
                                OrderEvaluateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.TASK_SER, mTask);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finishActivity();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }


                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.CONFIRM_TASK_URL, false, 0);
    }

    /**
     * 开始服务
     */
    public void getStartService(AjaxParams params) {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        finishActivity();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.START_SERVICE, false, 0);
    }

    /**
     * 取消退款
     */
    public void cancelRefund(AjaxParams params) {

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        finishActivity();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderActivity.this,((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.APPLY_FOR_TUIKUAN, false, 0);
    }

    /**
     * 同意退款
     */
    public void agreeRefund(AjaxParams params) {

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        finishActivity();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.APPLY_FOR_TUIKUAN, false, 0);
    }

    /**
     * 取消订单
     */
    public void cancelOrder(AjaxParams params) {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        ToastUtil.toastShow(OrderActivity.this, "订单已取消");
                        finishActivity();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.CLOSE_TASK, false, 0);
    }

    /**
     * 提示用户是否进行操作
     */
    public void ConfirmDialog(String title, String context, final String txet) {
        new com.chewuwuyou.app.utils.AlertDialog(this).builder().setTitle(title)
                .setMsg(context)
                .setPositiveButton("取消", new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                    }
                }).setNegativeButton("确定", new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AjaxParams params = new AjaxParams();
                params.put("taskId", mTask.getId());
                params.put("paymentAmount", mOrdearIncome.getText().toString().trim());
                params.put("provideBill", "1");
                getOffer(params);
            }
        }).show();
    }


    /**
     * 提示用户是否进行操作
     */
    public void orderDialog(String title, String context, final String txet) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(OrderActivity.this);
        dialog.setTitle(title);
        dialog.setMessage(context);
        dialog.setCancelable(false);
        dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                AjaxParams params = new AjaxParams();
                if (txet.equals("确认报价")) {
                    params.put("taskId", mTask.getId());
                    params.put("paymentAmount", mOrdearIncome.getText()
                            .toString().trim());
                    getOffer(params);
                } else if (txet.equals("确认完成")) {
                    params.put("id", mTask.getId());
                    getServiceComplete(params);
                } else if (txet.equals("完成服务")) {
                    params.put("taskId", mTask.getId());
                    getCompleteService(params);
                } else if (txet.equals("开始服务")) {
                    params.put("taskId", mTask.getId());
                    getStartService(params);
                } else if (txet.equals("取消退款")) {
                    params.put("orderId", mTask.getOrderNum());
                    params.put("st", Constant.TUIKUAN.CANCEL_TUIKUAN);
                    cancelRefund(params);
                } else if (txet.equals("同意退款")) {
                    params.put("orderId", mTask.getOrderNum());
                    params.put("st", Constant.TUIKUAN.AGREE_TUIKUAN);
                    agreeRefund(params);
                } else {// 取消訂單
                    params.put("id", mTask.getId());
                    cancelOrder(params);
                }

            }
        });
        dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        dialog.create().show();
    }

    /**
     * 选择申请退款理由
     *
     * @param context //退款理由
     */
    @SuppressWarnings("deprecation")
    public void createWalletPayDialog(final Context context) {

        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.order_choice_reason, null);

        mListFund = new ArrayList<RefundReason>();// 退款理由集合
        mListFund.add(new RefundReason("交易金额有误", false));
        mListFund.add(new RefundReason("商家服务沟通情况不符", false));
        mListFund.add(new RefundReason("未按约定时间完成服务", false));
        mListFund.add(new RefundReason("商家涉嫌违章服务", false));
        mListFund.add(new RefundReason("无理由退款", false));
        mListFund.add(new RefundReason("其他", false));

        mCommonAdapter = new CommonAdapter<RefundReason>(OrderActivity.this,
                mListFund, R.layout.bueiness_order_reason_item) {

            @Override
            public void convert(ViewHolder holder, RefundReason t, int p) {
                holder.setText(R.id.bueiness_reason, t.getReason());
                holder.setChecked(R.id.bueiness_reason_radio, t.isSelected());
            }
        };
        ListView listView = (ListView) layout
                .findViewById(R.id.refund_reason_list);
        ImageView mBusinessReasonChoice = (ImageView) layout
                .findViewById(R.id.business_reason_choice);
        Button mBusinessOrderButton = (Button) layout
                .findViewById(R.id.business_order_button);

        listView.setAdapter(mCommonAdapter);// 绑定适配器

        mBusinessReasonChoice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reason = "";
                dialog.dismiss();
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                for (int i = 0; i < mListFund.size(); i++) {
                    if (position == i) {
                        reason = mListFund.get(position).getReason();

                        mListFund.get(position).setSelected(true);
                    } else {
                        mListFund.get(i).setSelected(false);
                    }
                }
                mCommonAdapter.notifyDataSetChanged();
            }

        });

        mBusinessOrderButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(reason)) {
                    ToastUtil.toastShow(OrderActivity.this, "请选择退款理由");
                } else if (reason.equals("其他")) {
                    dialog.dismiss();
                    reason = "";
                    inputRefundreason(context);
                } else {
                    dialog.dismiss();
                    AjaxParams params = new AjaxParams();
                    params.put("orderId", mTask.getOrderNum());
                    params.put("st", Constant.TUIKUAN.APPLY_TUIKUAN);
                    params.put("rea", reason);
                    networkRefund(params);
                }
            }
        });
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        reason = "";
        /*
         * window.setWindowAnimations(R.style.myrightstyle); // 添加动画
		 */
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);

        dialog.show();
    }

    /**
     * 输入退款理由
     *
     * @param context
     */
    public void inputRefundreason(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.order_details_dialog, null);

        final EditText mOrderDialog = (EditText) layout
                .findViewById(R.id.order_dialog_ed);
        InputFilter[] filterBiao = {new EditInputBiaoQing(),
                new InputFilter.LengthFilter(100), new InputFilter() {

            @Override
            public CharSequence filter(CharSequence src, int start,
                                       int end, Spanned dst, int dstart, int dend) {
                if (src.length() < 1) {
                    return null;
                } else {
                    char temp[] = (src.toString()).toCharArray();
                    char result[] = new char[temp.length];
                    for (int i = 0, j = 0; i < temp.length; i++) {
                        if (temp[i] == ' ') {
                            continue;
                        } else {
                            result[j++] = temp[i];
                        }
                    }
                    return String.valueOf(result).trim();
                }

            }

        }};
        mOrderDialog.setFilters(filterBiao);// 限制字数为, 不能输入表情,过滤空格

        ImageView mBusinessOrderInput = (ImageView) layout
                .findViewById(R.id.business_order_input);
        Button mOrderConfirm = (Button) layout.findViewById(R.id.order_confirm);
        mBusinessOrderInput.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mOrderConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderDialog.length() < 6) {
                    ToastUtil.toastShow(OrderActivity.this, "退款理由必须6字以上内容");
                } else {
                    dialog.dismiss();
                    AjaxParams params = new AjaxParams();
                    params.put("orderId", mTask.getOrderNum());
                    params.put("st", Constant.TUIKUAN.APPLY_TUIKUAN);
                    params.put("rea", mOrderDialog.getText().toString().trim());
                    networkRefund(params);
                }

            }
        });
        // }

        layout.getBackground().setAlpha(100);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        /*
         * window.setWindowAnimations(R.style.myrightstyle); // 添加动画
		 */
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }

    @SuppressWarnings("deprecation")
    public void createListViewqDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.order_listview_details, null);

        mOrderPhone = (LinearLayout) layout.findViewById(R.id.order_qq_phone);
        mOrderMailbox = (LinearLayout) layout
                .findViewById(R.id.order_qq_mailbox);

//		layout.getBackground().setAlpha(100);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		/*
		 * window.setWindowAnimations(R.style.myrightstyle); // 添加动画
		 */
        /**
         * 联系电话
         */
        mOrderPhone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        // 邮箱
        mOrderMailbox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    /**
     * 提交退款理由
     */
    public void networkRefund(AjaxParams params) {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        ToastUtil.toastShow(OrderActivity.this, "您的退款申请已提交，等待商家确认");
                        finishActivity();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(OrderActivity.this, "退款申请提交失败");
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.APPLY_FOR_TUIKUAN, false, 0);

    }

    /**
     * 访问物流信息
     */
    public void visitLogistics(final String number, final String OddNumbers, final int flag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(WLUtil.getOrderTracesByJson(number, OddNumbers));
                    mLogisitcsTraces = new ArrayList<TracesEntity>();
                    for (int i = 0; i < jsonObject.getJSONArray("Traces").length(); i++) {
                        TracesEntity entity = new TracesEntity();
                        entity.setAcceptTime(jsonObject.getJSONArray("Traces").getJSONObject(i).getString("AcceptTime"));
                        entity.setAcceptStation(jsonObject.getJSONArray("Traces").getJSONObject(i).getString("AcceptStation"));
                        mLogisitcsTraces.add(entity);
                    }
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    if (mLogisitcsTraces.size() > 0) {
                        message.what = 1;
                        bundle.putString("ShipperCode", jsonObject.getString("ShipperCode"));
                        bundle.putString("LogisticCode", jsonObject.getString("LogisticCode"));
                        bundle.putString("AcceptTime", mLogisitcsTraces.get(mLogisitcsTraces.size() - 1).getAcceptTime());
                        bundle.putString("AcceptStation", mLogisitcsTraces.get(mLogisitcsTraces.size() - 1).getAcceptStation());
                    } else {
                        message.what = 2;
                        bundle.putString("ShipperCode", jsonObject.getString("ShipperCode"));
                        bundle.putString("LogisticCode", jsonObject.getString("LogisticCode"));
                        bundle.putString("Reason", jsonObject.getString("Reason"));
                    }
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 接单方物流
     */
    private void ConnectedLogistics(int status) {
        /**
         * 根据状态判断是否显示物流信息
         */
        switch (status) {
            case Constant.ORDER_START_ALREADY_PAID://已付款


                if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                    mFillLogisics.setVisibility(View.GONE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    mLogisicsSerial.setVisibility(View.VISIBLE);//显示
                } else {
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    mFillLogisics.setVisibility(View.VISIBLE);//填写物流信息
                }

                break;
            case Constant.ORDER_START_SERVICE://服务进行中
                if (flag != 3) {
                    if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                        mLogisicsSerial.setVisibility(View.VISIBLE);
                        mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    }
                } else {
                    if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                        mLogisicsSerial.setVisibility(View.VISIBLE);
                        mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    } else {
                        mLogisicsSerial.setVisibility(View.VISIBLE);
                        mFillLogisics.setVisibility(View.VISIBLE);//显示物流信息
                    }
                }

                break;
            case Constant.ORDER_START_SERVICE_COMPLETE://商家服务完成
                /**
                 * 判断回寄是否填写
                 */
                if (flag != 3) {
                    if (!mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                        mLogisicsSerial.setVisibility(View.VISIBLE);
                        mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                        mReturnLogisics.setVisibility(View.GONE);
                    } else {
                        /**
                         * 判断是否填写物流
                         */
                        mLogisicsSerial.setVisibility(View.VISIBLE);
                        mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                        mReturnLogisics.setVisibility(View.VISIBLE);//显示回寄物流信息
                    }
                } else {
                    if (!mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                        mLogisicsSerial.setVisibility(View.VISIBLE);
                        mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                        mReturnLogisics.setVisibility(View.GONE);
                    } else if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                        mLogisicsSerial.setVisibility(View.VISIBLE);
                        mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    } else {
                        mLogistics.setVisibility(View.GONE);
                    }
                }
                break;
            case Constant.ORDER_START_APPLY_REFUND://申请退款中
                /**
                 * 判断回寄是否填写
                 */
                if (!mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    return;
                } else if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                } else {
                    mLogistics.setVisibility(View.GONE);//显示物流信息
                }

                break;
            case Constant.ORDER_START_REFUND_PROCESSING://退款处理中

                /**
                 * 判断回寄是否填写
                 */
                if (!mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    return;
                } else if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                } else {
                    mLogistics.setVisibility(View.GONE);//显示物流信息
                }
                /**
                 * 判断是否填写物流
                 */
//				if(!mTask.getExpressNo().equals("")&&!mTask.getCompanyNo().equals("")){
//
//				}
                break;
            case Constant.ORDER_START_REFUND_COMPLETE://退款完成
                /**
                 * 判断回寄是否填写
                 */
                if (!mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    return;
                } else if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                } else {
                    mLogistics.setVisibility(View.GONE);//显示物流信息
                }
                /**
                 * 判断是否填写物流
                 */
//				if(!mTask.getExpressNo().equals("")&&!mTask.getCompanyNo().equals("")){
//
//				}
                break;
            case Constant.ORDER_START_ORDER_COMPLETE://订单已完成
                /**
                 * 判断回寄是否填写
                 */
                if (!mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    return;
                } else if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                } else {
                    mLogistics.setVisibility(View.GONE);//显示物流信息
                }
                /**
                 * 判断是否填写物流
                 */
//				if(!mTask.getExpressNo().equals("")&&!mTask.getCompanyNo().equals("")){
//
//				}
                break;
            case Constant.ORDER_START_ORDER_BALANCE://结算入余额
                /**
                 * 判断回寄是否填写
                 */
                if (!mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    return;
                } else if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                } else {
                    mLogistics.setVisibility(View.GONE);//显示物流信息
                }
                /**
                 * 判断是否填写物流
                 */
                if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {

                }
                break;
            case Constant.ORDER_START_REFUND_COMLETE2://退款入余额
                /**
                 * 判断回寄是否填写
                 */
                if (!mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    return;
                } else if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                } else {
                    mLogistics.setVisibility(View.GONE);//显示物流信息
                }
                /**
                 * 判断是否填写物流
                 */
//				if(!mTask.getExpressNo().equals("")&&!mTask.getCompanyNo().equals("")){
//
//				}
                break;
            case Constant.ORDER_START_COMPLETE://订单完成
                /**
                 * 判断回寄是否填写
                 */
                if (!mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    return;
                } else if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                } else {
                    mLogistics.setVisibility(View.GONE);//显示物流信息
                }
                /**
                 * 判断是否填写物流
                 */
//				if(!mTask.getExpressNo().equals("")&&!mTask.getCompanyNo().equals("")){
//
//				}
                break;
            case Constant.ORDER_OPERATE_COMLETE2://运营进入
                /**
                 * 判断回寄是否填写
                 */
                if (!mTask.getRevCompanyNo().equals("") && !mTask.getRevExpressNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                    return;
                } else if (!mTask.getExpressNo().equals("") && !mTask.getCompanyNo().equals("")) {
                    mLogisicsSerial.setVisibility(View.VISIBLE);
                    mLogistics.setVisibility(View.VISIBLE);//显示物流信息
                } else {
                    mLogistics.setVisibility(View.GONE);//显示物流信息
                }
                /**
                 * 判断是否填写物流
                 */
//				if(!mTask.getExpressNo().equals("")&&!mTask.getCompanyNo().equals("")){
//
//				}
                break;
        }
    }
}
