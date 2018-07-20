package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.MyWorkAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.CarAssistantServiceItem;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.MyGridView;
import com.chewuwuyou.app.widget.SegmentControl;
import com.chewuwuyou.app.widget.SegmentControl.OnSegmentControlClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:我的秘书台
 * @author:yuyong
 * @date:2015-9-25下午3:49:33
 * @version:1.2.1
 */
@SuppressLint("HandlerLeak")
public class MyWorkActivity extends CDDBaseActivity implements OnClickListener {

    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private List<String> mWorks;
    private List<String> mOverSeasWorks;// 外派工作
    private MyGridView mMyWorkGV;// 我的秘书
    private MyGridView mWorkManagerGV;// 管理工具
    private MyWorkAdapter mWorkAdapter;
    private List<CarAssistantServiceItem> mCarAssistantItems;
    private String[] carAssistantStrs;
    private int[] imgResIds = {R.drawable.workbench_data,
            R.drawable.order_query_img, R.drawable.workbench_fuwu,
            R.drawable.workbench_mingpian, R.drawable.workbench_pingjia,
            R.drawable.workbench_kehu, R.drawable.workbench_dingdanluru,
            R.drawable.weizhangtongdao,
            R.drawable.workbench_weizhangdaima, R.drawable.weizhangchaxun, R.drawable.mail_address, R.drawable.workbench_qiyefuwu};
    private CommonAdapter<CarAssistantServiceItem> mAdapter;

    private SegmentControl mSeControl;
    private int mOrderType = Constant.ORDER_TYPE.GET_ORDER;
    private int mSeControlNum = 0;// 区分发出的订单还是收到的订单
    private List<String> mGetOrderStatusNumbers;// 收到订单的条数统计
    private List<String> mOverseasOrderStatusNumbers;// 外派订单的条数统计
    private RelativeLayout mTitleHeight;// 标题布局高度
    private TextView mUpdateDataTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_work_ac);
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mMyWorkGV = (MyGridView) findViewById(R.id.my_work_gv);
        mWorkManagerGV = (MyGridView) findViewById(R.id.worak_manager_gv);
        mSeControl = (SegmentControl) findViewById(R.id.segment_control);
        mUpdateDataTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mUpdateDataTV.setVisibility(View.VISIBLE);
        mUpdateDataTV.setText("刷新");
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mGetOrderStatusNumbers = new ArrayList<String>();
        mOverseasOrderStatusNumbers = new ArrayList<String>();

        mTitleTV.setText("秘书服务");
        mWorks = new ArrayList<String>();
        mWorks.add("待处理订单");
        mWorks.add("服务中订单");
        mWorks.add("已完成订单");
        mWorks.add("已取消订单");
        mWorks.add("退款中订单");
        mWorks.add("纠纷中订单");

        mOverSeasWorks = new ArrayList<String>();
        mOverSeasWorks.add("待处理订单");
        mOverSeasWorks.add("服务中订单");
        mOverSeasWorks.add("已完成订单");
        mOverSeasWorks.add("已取消订单");
        mOverSeasWorks.add("退款中订单");
        mOverSeasWorks.add("纠纷中订单");
        carAssistantStrs = getResources().getStringArray(
                R.array.my_work_manager_arr);
        mCarAssistantItems = new ArrayList<CarAssistantServiceItem>();
        for (int i = 0; i < imgResIds.length; i++) {
            CarAssistantServiceItem carItem = new CarAssistantServiceItem();
            carItem.setImgResId(imgResIds[i]);
            carItem.setNameTV(carAssistantStrs[i]);
            mCarAssistantItems.add(carItem);
        }
        mAdapter = new CommonAdapter<CarAssistantServiceItem>(
                MyWorkActivity.this, mCarAssistantItems,
                R.layout.work_manage_item) {

            @Override
            public void convert(ViewHolder holder, CarAssistantServiceItem t, int p) {
                holder.setText(R.id.work_manager_item_name_tv, t.getNameTV());
                holder.setImageResource(R.id.work_manager_iv, t.getImgResId());

            }
        };
        mWorkManagerGV.setAdapter(mAdapter);
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        mUpdateDataTV.setOnClickListener(this);
        mMyWorkGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent(MyWorkActivity.this,
                        OrderStatusManagerActivity.class);
                intent.putExtra("orderType", mOrderType);
                switch (arg2) {
                    case 0:
                        intent.putExtra("type", "0");
                        intent.putExtra("orderStatus",
                                Constant.ORDERSTATUSMANAGE.TODAY_ORDER);
                        Constant.mType = "0";
                        break;
                    case 1:
                        intent.putExtra("type", "0");
                        intent.putExtra("orderStatus",
                                Constant.ORDERSTATUSMANAGE.NO_COMFIRM_PRICE);
                        Constant.mType = "0";
                        break;
                    case 2:
                        intent.putExtra("type", "0");
                        intent.putExtra("orderStatus",
                                Constant.ORDERSTATUSMANAGE.WAIT_PAY);
                        Constant.mType = "0";
                        break;
                    case 3:

                        intent.putExtra("type", "1");
                        intent.putExtra("orderStatus",
                                Constant.ORDERSTATUSMANAGE.IN_SERVICE);
                        Constant.mType = "1";
                        break;
                    case 4:
                        intent.putExtra("type", "0");
                        intent.putExtra("orderStatus",
                                Constant.ORDERSTATUSMANAGE.CUSTOMERS_CHEDAN);
                        Constant.mType = "0";
                        break;
                    case 5:
                        intent.putExtra("type", "0");
                        intent.putExtra("orderStatus",
                                Constant.ORDERSTATUSMANAGE.CUSTOMERS_EVALUATE);
                        Constant.mType = "0";
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });

        mWorkManagerGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                switch (arg2) {
                    case 0:
                        StatService.onEvent(MyWorkActivity.this,
                                "clickBusiCentActDataLL", "点击查看数据报表");
                        Intent dataStatisticIntent = new Intent(
                                MyWorkActivity.this, DataReportActivity.class);
                        dataStatisticIntent.putExtra("issue", mSeControlNum);
                        startActivity(dataStatisticIntent);
                        break;
                    case 1:
//                        StatService.onEvent(MyWorkActivity.this,
//                                "clickBusiCentActOrderManagerLL", "点击查看订单管理");
//                        Intent orderManagerIntent = new Intent(MyWorkActivity.this,
//                                OrderManagerActivity.class);
//                        startActivity(orderManagerIntent);

                        StatService.onEvent(MyWorkActivity.this,
                                "clickOrderManagerSearchBtn", "点击订单管理搜索按钮");
                        Intent orderManagerIntent = new Intent(MyWorkActivity.this,
                                TaskSearchActivity.class);
                        orderManagerIntent.setAction("com.chewuwuyou.app.search_order");
                        startActivity(orderManagerIntent);

                        break;
                    case 2://ServiceManagerActivity
                        StatService.onEvent(MyWorkActivity.this,
                                "clickBusiCentActServiceManagerLL", "点击查看服务管理");
                        Intent serviceManagerintent = new Intent(
                                MyWorkActivity.this, ServiceAdministrationActivity.class);
                        startActivity(serviceManagerintent);
                        // if (CacheTools.getUserData("role").contains("3")) {
                        // AlertDialog dialog = new AlertDialog(
                        // MyWorkActivity.this).builder();
                        // dialog.setTitle("车当当温馨提示");
                        // dialog.setMsg("此功能正在维护中...");
                        // dialog.show();
                        // } else {
                        // StatService.onEvent(MyWorkActivity.this,
                        // "clickBusiCentActServiceManagerLL", "点击查看服务管理");
                        // Intent serviceManagerintent = new Intent(
                        // MyWorkActivity.this,
                        // ServiceManagerActivity.class);
                        // startActivity(serviceManagerintent);
                        //
                        // }
                        break;
                    case 3:
                        StatService.onEvent(MyWorkActivity.this,
                                "clickBusiCentActMyMingPianLL", "点击查看我的名片");
                        Intent intent = new Intent(MyWorkActivity.this,
                                BusinessCenterActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        StatService.onEvent(MyWorkActivity.this,
                                "clickBusiCentActEvaluateLL", "点击查看客户评价");
                        Intent busiIntent = new Intent(MyWorkActivity.this, EvaluateListActivity.class);
                        startActivity(busiIntent);
                        break;
                    case 5:
                        StatService.onEvent(MyWorkActivity.this,
                                "clickBusiCentActClientManagerLL", "点击查看客户管理");
                        Intent clientManagerIntent = new Intent(
                                MyWorkActivity.this, CollectMeActivity.class);
                        startActivity(clientManagerIntent);
                        break;
                    case 6:
                        StatService.onEvent(MyWorkActivity.this,
                                "clickBusiCentActRecordOrderLL", "点击查看门店录单");
                        // AlertDialog dialog = new AlertDialog(MyWorkActivity.this)
                        // .builder();
                        // dialog.setTitle("车当当温馨提示");
                        // dialog.setMsg("此功能正在维护中...");
                        // dialog.show();

                        ToastUtil.toastShow(MyWorkActivity.this, "敬请期待");
//					Intent recordIntent = new Intent(MyWorkActivity.this,
//							StoreRecordActivity.class);
//					startActivity(recordIntent);

                        break;
                    case 7:
                        Intent illegalIntent = new Intent(MyWorkActivity.this,
                                AllIllegalQueryActivity.class);// 违章通道
                        startActivity(illegalIntent);
                        break;
                    case 8:
                        StatService.onEvent(MyWorkActivity.this,
                                "clickBusiCentActIllegalCodeQueryLL", "点击查看违章代码查询");
                        Intent illegalCodeQueryIntent = new Intent(
                                MyWorkActivity.this, IllegalCodeQueryActivity.class);
                        startActivity(illegalCodeQueryIntent);
                        break;
                    case 9:
                        StatService.onEvent(MyWorkActivity.this,
                                "clickBusiCentActIllegalCodeQueryLL", "点击查看违章查询");
                        Intent illegalQueryIntent = new Intent(MyWorkActivity.this,
                                IllegalQueryActivity.class);
                        startActivity(illegalQueryIntent);
                        break;


                    case 10:
                        Intent TenIntent = new Intent(MyWorkActivity.this,
                                MailAddressActivtiy.class);// 邮寄地址
                        startActivity(TenIntent);
                        break;
                    case 11:
                        Intent mIntent = new Intent(MyWorkActivity.this,
                                CompanyServiceActivity.class);// 企业服务

                        startActivity(mIntent);
                        break;

                    default:
                        break;
                }
            }
        });

        mSeControl
                .setOnSegmentControlClickListener(new OnSegmentControlClickListener() {

                    @Override
                    public void onSegmentControlClick(int index) {
                        if (index == 0) {
                            mSeControlNum = 0;
                            mOrderType = Constant.ORDER_TYPE.GET_ORDER;
                            mWorkAdapter = new MyWorkAdapter(
                                    MyWorkActivity.this, mWorks,
                                    mGetOrderStatusNumbers);
                            mMyWorkGV.setAdapter(mWorkAdapter);
                        } else {
                            mSeControlNum = 1;
                            mOrderType = Constant.ORDER_TYPE.OVERSEAS_ORDER;
                            mWorkAdapter = new MyWorkAdapter(
                                    MyWorkActivity.this, mOverSeasWorks,
                                    mOverseasOrderStatusNumbers);
                            mMyWorkGV.setAdapter(mWorkAdapter);
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv:
                mSeControl.setSelectedIndex(mSeControlNum);
                getData();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 访问数据获取订单条数
     */
    private void getData() {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mGetOrderStatusNumbers.clear();
                        mOverseasOrderStatusNumbers.clear();
                        MyLog.i("YUY", "订单数据 = " + msg.obj.toString());

                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            mGetOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("waitProcessOrderOther")));
                            mGetOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("inServiceOrderOther")));
                            mGetOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("finishedOrderOther")));
                            mGetOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("canceledOrderOther")));
                            mGetOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("withDrawOrderOther")));
                            mGetOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("alreadyWithDrawStatusOther")));

                            mOverseasOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("waitProcessOrderMe")));
                            mOverseasOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("inServiceOrderMe")));
                            mOverseasOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("finishedOrderMe")));
                            mOverseasOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("canceledOrderMe")));
                            mOverseasOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("withDrawOrderMe")));
                            mOverseasOrderStatusNumbers.add(String.valueOf(jo
                                    .getInt("alreadyWithDrawStatusMe")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (mSeControlNum == 0) {
                            mWorkAdapter = new MyWorkAdapter(MyWorkActivity.this,
                                    mWorks, mGetOrderStatusNumbers);
                            mMyWorkGV.setAdapter(mWorkAdapter);
                        } else {
                            mWorkAdapter = new MyWorkAdapter(MyWorkActivity.this,
                                    mWorks, mOverseasOrderStatusNumbers);
                            mMyWorkGV.setAdapter(mWorkAdapter);
                        }
                        break;

                    default:
                        break;
                }
            }
        }, null, NetworkUtil.ORDER_FENERA_SITUATION, false, 0);

    }
}
