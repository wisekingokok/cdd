package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.OfferInfoAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.tools.MyListView;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.OrderStateUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zengys 我发布的
 */
public class ToquoteAcitivity extends CDDBaseActivity implements
        OnClickListener {

    private LinearLayout mLinearOrderTime;
    private MyListView mToquoteMylist;
    private ImageButton mImageBack;
    private TextView mTextTitle;
    private TextView mTextServeDemo;
    private TextView mTextArea;
    private TextView mTextOrderTime;
    private TextView mTextBusinessPersons;
    private OfferInfoAdapter mAdapter;
    private Button mBtnCancel;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private TextView mOrderStatusTV;// 订单状态
    private TextView mOrderNumTV;// 订单号
    public static ToquoteAcitivity mActivity;
    private LinearLayout mAllLL;

    @ViewInject(id = R.id.order_bill_need)
    private TextView mOrderBillNeed;
    private Task mTask;
    @ViewInject(id = R.id.network_request)//网络动画
    private LinearLayout mNetworkRequest;
    @ViewInject(id = R.id.network_abnormal_layout)
    private LinearLayout mNetworkAbnormalLayout;
    @ViewInject(id = R.id.network_again)
    private TextView mNetworkAgain;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    GuaranteeBusiness(msg.obj.toString());
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.toquote_layout);
        initView();
        initData();
        initEvent();

    }

    @Override
    protected void initView() {
        mLinearOrderTime = (LinearLayout) findViewById(R.id.order_daiqren);
        mToquoteMylist = (MyListView) findViewById(R.id.toquote_mylist);
        mImageBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mTextTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
        mTextArea = (TextView) findViewById(R.id.order_serve_region);
        mTextServeDemo = (TextView) findViewById(R.id.order_serve_project);
        mTextOrderTime = (TextView) findViewById(R.id.order_daiqren_establish);
        mTextBusinessPersons = (TextView) findViewById(R.id.text_business_persons);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mOrderStatusTV = (TextView) findViewById(R.id.order_status_tv);
        mOrderNumTV = (TextView) findViewById(R.id.order_no);

        mAllLL = (LinearLayout) findViewById(R.id.toquote_ll);
        mAllLL.setFocusable(true);
        mAllLL.setFocusableInTouchMode(true);
        mAllLL.requestFocus();
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        getData();
    }

    @Override
    protected void initEvent() {
        mTextTitle.setText("订单详情");
        mTextTitle.setVisibility(View.VISIBLE);
        mLinearOrderTime.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mNetworkAgain.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                if (AppManager.isExsitMianActivity(MainActivityEr.class, ToquoteAcitivity.this)==false) {//判断首页是否在堆栈中
                    startActivity(new Intent(ToquoteAcitivity.this, MainActivityEr.class));
                }
                finishActivity();
                break;
            case R.id.btn_cancel:
                cancelOrder();
                break;

            case R.id.network_again:
                mNetworkRequest.setVisibility(View.VISIBLE);
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                getData();
                break;

            default:
                break;
        }

    }

    private void cancelOrder() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                ToquoteAcitivity.this);
        dialog.setTitle("关闭任务");
        dialog.setMessage("确认关闭任务吗？");
        dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                AjaxParams params = new AjaxParams();
                params.put("id", mTask.getId());
                requestNet(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case Constant.NET_DATA_SUCCESS:
                                ToastUtil.toastShow(ToquoteAcitivity.this, "订单已取消");
                                finishActivity();
                                break;
                            default:
                                break;
                        }
                    }
                }, params, NetworkUtil.CLOSE_TASK, false, 0);
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

    private void getData() {
        AjaxParams params = new AjaxParams();
        params.put("taskId", getIntent().getStringExtra("taskId"));
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mNetworkRequest.setVisibility(View.GONE);
                        mNetworkAbnormalLayout.setVisibility(View.GONE);
                        mTask = Task.parse(msg.obj.toString());
                        if(!mTask.getStatus().equals("27")&&!mTask.getStatus().equals("28")){
                            Intent intent = new Intent(ToquoteAcitivity.this,OrderActivity.class);
                            intent.putExtra("taskId",mTask.getId());
                            startActivity(intent);
                            finishActivity();
                            return;
                        }
                        mTextServeDemo.setText(ServiceUtils.getProjectName(mTask
                                .getProjectName()));
                        setTaskLocation(mTask, mTextArea);
                        if (mTask.getProvideBill().equals("1")) {
                            mOrderBillNeed.setText("需要票据");
                        } else {
                            mOrderBillNeed.setText("不需要票据");
                        }

                        mTextOrderTime.setText(mTask.getPubishTime().substring(0, 19));

                        if (Integer.valueOf(mTask.getStatus()) == 2) {
                            finishActivity();
                        } else if (Integer.valueOf(mTask.getStatus()) == 27) {
                            OrderStateUtil.orderStatusShow(
                                    Integer.parseInt(mTask.getStatus()),
                                    Integer.parseInt(mTask.getFlag()),
                                    mOrderStatusTV);// 订单状态
                        } else {
                            if (mTask.getOfferCnt() == 0) {
                                mOrderStatusTV.setText("等待报价");
                            } else {
                                OrderStateUtil.orderStatusShow(
                                        Integer.parseInt(mTask.getStatus()),
                                        Integer.parseInt(mTask.getFlag()),
                                        mOrderStatusTV);// 订单状态
                            }
                        }
                        mOrderNumTV.setText(mTask.getOrderNum());
                        mTextBusinessPersons.setText("当前共" + mTask.getOffers().size()
                                + "位");
                        mAdapter = new OfferInfoAdapter(ToquoteAcitivity.this, mTask, handler);
                        mToquoteMylist.setAdapter(mAdapter);



                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(ToquoteAcitivity.this, ((DataError) msg.obj).getErrorMessage());
                        mNetworkRequest.setVisibility(View.GONE);
                        mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        mNetworkRequest.setVisibility(View.GONE);
                        mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }, params, NetworkUtil.BUSINESS_BILLING, false, 0);
    }


    private void setTaskLocation(Task task, TextView locationTV) {
        if (!TextUtils.isEmpty(task.getTaskDistrict())) {
            locationTV.setText(task.getTaskProvince() + ","
                    + task.getTaskCity() + "," + task.getTaskDistrict());
        } else if (!TextUtils.isEmpty(task.getTaskCity())) {
            locationTV.setText(task.getTaskProvince() + ","
                    + task.getTaskCity());
        } else {
            locationTV.setText(task.getTaskLocation());
        }
    }

    /**
     * 选择担保商家
     */
    private void GuaranteeBusiness(String off) {
        AjaxParams params = new AjaxParams();
        params.put("offerId", off);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {


                            JSONObject jsonObject = new JSONObject(msg.obj.toString());

                            Intent intent = new Intent(ToquoteAcitivity.this, CommonPayActivity.class);
                            intent.putExtra("payMoney", jsonObject.getString("price"));
                            intent.putExtra("orderDescription", mTask.getProjectName());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constant.TASK_SER, mTask);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finishActivity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(ToquoteAcitivity.this, ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.SETUP_ONE_PRICE, false, 0);
    }
}
