package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CollectionReleaseProAdapter;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.ChooseType;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.ServicePro;
import com.chewuwuyou.app.tools.EditInputFilter;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.MyGridView;

/**
 * 从收藏入口进入发布订单
 *
 * @author yuyong
 */
public class CollectReleaseOrderActivity extends CDDBaseActivity implements
        OnClickListener {

    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private MyGridView mProjectGV;// 服务选项选择
    // public static List<ServicePro> mAllServicePros;// 所有服务项目
    private List<ServicePro> mIllegalServiceList;
    private List<ServicePro> mVehicleServiceList;
    private List<ServicePro> mLicenseServiceList;
    private CollectionReleaseProAdapter mProAdapter;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private EditText mInputPriceET;// 输入价格
    private TextView mAgreementTV_1, mAgreementTV_2;// 服务地区,协议
    private Button mReleaseServiceBtn;// 发布
    private String mServicePro;// 选择的服务选项
    private int mServiceType;// 标识选择的服务选项
    private String mReleaseUrl;
    // private TrafficBroker mTrafficBroker;// 经纪人
    private List<ChooseType> mServiceTypes;// 类型
    private GridView mChooseTypeGV, mChooseImgGV;// 选择类型、选中服务选项底部的效果
    private CommonAdapter<ChooseType> mChooseTypeAdapter;
    private CommonAdapter<ChooseType> mChoosesAdapter;
    private List<ServicePro> mServicePros = new ArrayList<ServicePro>();// 所有的服务项目
    private LinearLayout mWzServiceXieYiLL;// 违章服务

    private TextView mReleaseOption;//是否需要票据
    private RadioButton mReleaseNeed, mReleaseNoNeed;//需要票据，不需要票据

    private boolean isSelected = false;
    private boolean isUnchecked = false;
    private String maxPrice;//最大金额
    private String minPrice;//最小金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_new_order_ac);
        initView();
        initData();
        initEvent();

    }

    @Override
    protected void initView() {
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mProjectGV = (MyGridView) findViewById(R.id.service_gv);
        mInputPriceET = (EditText) findViewById(R.id.pay_money_et);
        mAgreementTV_1 = (TextView) findViewById(R.id.chedangdang_tv);
        mAgreementTV_2 = (TextView) findViewById(R.id.xieyi_tv_two);
        mReleaseServiceBtn = (Button) findViewById(R.id.service_release_btn);
        mChooseTypeGV = (GridView) findViewById(R.id.service_type_gv);
        mChooseImgGV = (GridView) findViewById(R.id.choose_img_gv);
        mWzServiceXieYiLL = (LinearLayout) findViewById(R.id.wz_service_xieyi_ll);
        mReleaseNeed = (RadioButton) findViewById(R.id.release_need);
        mReleaseNoNeed = (RadioButton) findViewById(R.id.release_no_need);
        mReleaseOption = (TextView) findViewById(R.id.release_option);
    }

    @Override
    protected void initData() {
        mServiceTypes = new ArrayList<ChooseType>();
        /* 设置输入的服务费最大值为5000,最小值为0.00 */
//		InputFilter[] filters = { new EditInputFilter(4999),new InputFilter.LengthFilter(7) /* 这里限制输入的长度为7个字母 */};
//		mInputPriceET.setFilters(filters);

        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mTitleTV.setText("新建订单");
        // mTrafficBroker = (TrafficBroker) getIntent().getSerializableExtra(
        // Constant.TRAFFIC_SER);
        getProData();
        // if (mTrafficBroker == null) {// 即时通讯直接下单 没有商家传入
        // return;
        // }
        // mServiceLocationTV.setText(mTrafficBroker.getLocation());

    }

    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        mAgreementTV_1.setOnClickListener(this);
        mAgreementTV_2.setOnClickListener(this);
        mReleaseServiceBtn.setOnClickListener(this);
        mWzServiceXieYiLL.setVisibility(View.VISIBLE);
        mReleaseNeed.setOnClickListener(this);
        mReleaseNoNeed.setOnClickListener(this);
        mProjectGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                mProAdapter.setSeclection(arg2);
                mProAdapter.notifyDataSetChanged();
                if (mServiceType == Constant.SERVICE_TYPE.ILLEGAL_SERVICE) {
                    mServicePro = String.valueOf(mIllegalServiceList.get(arg2)
                            .getProjectNum());
                } else if (mServiceType == Constant.SERVICE_TYPE.CAR_SERVICE) {
                    mServicePro = String.valueOf(mVehicleServiceList.get(arg2)
                            .getProjectNum());
                } else {
                    mServicePro = String.valueOf(mLicenseServiceList.get(arg2)
                            .getProjectNum());
                }

            }
        });

        mChooseTypeGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                for (int i = 0; i < mServiceTypes.size(); i++) {
                    if (arg2 == i) {
                        mServiceTypes.get(arg2).setChoose(true);
                    } else {
                        mServiceTypes.get(i).setChoose(false);
                    }
                }
                mServiceType = mServiceTypes.get(arg2).getServiceType();
                mChooseTypeAdapter.notifyDataSetChanged();
                mChoosesAdapter.notifyDataSetChanged();
                mServicePro = "";
                if (mServiceTypes.get(arg2).getServiceType() == Constant.SERVICE_TYPE.ILLEGAL_SERVICE) {
                    mWzServiceXieYiLL.setVisibility(View.VISIBLE);
                    mReleaseUrl = NetworkUtil.VIOLATION_SERVICE_URL;
                    mProAdapter = new CollectionReleaseProAdapter(
                            mIllegalServiceList,
                            CollectReleaseOrderActivity.this);
                    mProjectGV.setAdapter(mProAdapter);
                } else if (mServiceTypes.get(arg2).getServiceType() == Constant.SERVICE_TYPE.CAR_SERVICE) {
                    mWzServiceXieYiLL.setVisibility(View.GONE);
                    mReleaseUrl = NetworkUtil.VEHICLE_SERVICE_URL;
                    mProAdapter = new CollectionReleaseProAdapter(
                            mVehicleServiceList,
                            CollectReleaseOrderActivity.this);
                    mProjectGV.setAdapter(mProAdapter);
                } else {
                    mWzServiceXieYiLL.setVisibility(View.GONE);
                    mReleaseUrl = NetworkUtil.LICENCE_SERVICE_URL;
                    mProAdapter = new CollectionReleaseProAdapter(
                            mLicenseServiceList,
                            CollectReleaseOrderActivity.this);
                    mProjectGV.setAdapter(mProAdapter);
                }

            }
        });

    }

    /**
     * 发布大厅请求服务选项数据方法
     */
    private void getProData() {
        mIllegalServiceList = new ArrayList<ServicePro>();
        mVehicleServiceList = new ArrayList<ServicePro>();
        mLicenseServiceList = new ArrayList<ServicePro>();
        AjaxParams params = new AjaxParams();
        params.put("userBId", getIntent().getStringExtra("businessId"));//服务费
        params.put("newVer", "y");
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("YUY", "-----车辆服务选项---" + msg.obj.toString());
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            maxPrice = jo.getString("max");
                            minPrice = jo.getString("min");
                            JSONArray mJsonArray = jo.getJSONArray("prices");
                            for (int i = 0; i < mJsonArray.length(); i++) {
                                mServicePros.add(new ServicePro(Integer
                                        .parseInt(mJsonArray.getJSONObject(i)
                                                .getString("id")), Double
                                        .parseDouble(mJsonArray.getJSONObject(i)
                                                .getString("fees")), Integer
                                        .parseInt(mJsonArray.getJSONObject(i)
                                                .getString("type")), mJsonArray
                                        .getJSONObject(i).getString("projectName"),
                                        Integer.parseInt(mJsonArray
                                                .getJSONObject(i).getString(
                                                        "projectNum")), mJsonArray
                                        .getJSONObject(i).getString(
                                                "projectImg"), mJsonArray
                                        .getJSONObject(i).getString(
                                                "serviceDesc"), String
                                        .valueOf(mJsonArray
                                                .getJSONObject(i)
                                                .getJSONArray(
                                                        "serviceFolder"))));
                            }
                            initProData();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        break;
                }
            }
        }, params, NetworkUtil.SELECT_SERVICE_ADMINISTRATION, false, 0);

    }

    private void initProData() {
        for (int i = 0; i < mServicePros.size(); i++) {
            System.out.println("服务类型 = " + mServicePros.get(i).getType());
            if (mServicePros.get(i).getProjectName().contains("上门")
                    || mServicePros.get(i).getProjectName().contains("其他")) {

            } else if (mServicePros.get(i).getType() == Constant.SERVICE_TYPE.ILLEGAL_SERVICE) {
                mIllegalServiceList.add(mServicePros.get(i));
            } else if (mServicePros.get(i).getType() == Constant.SERVICE_TYPE.CAR_SERVICE) {
                mVehicleServiceList.add(mServicePros.get(i));
            } else if (mServicePros.get(i).getType() == Constant.SERVICE_TYPE.LICENCE_SERVICE) {
                mLicenseServiceList.add(mServicePros.get(i));
            }
        }
        if (mIllegalServiceList.size() > 0) {
            mReleaseUrl = NetworkUtil.VIOLATION_SERVICE_URL;
            mProAdapter = new CollectionReleaseProAdapter(mIllegalServiceList,
                    CollectReleaseOrderActivity.this);
            mProjectGV.setAdapter(mProAdapter);
        } else if (mVehicleServiceList.size() > 0) {
            mReleaseUrl = NetworkUtil.VEHICLE_SERVICE_URL;
            mProAdapter = new CollectionReleaseProAdapter(mVehicleServiceList,
                    CollectReleaseOrderActivity.this);
            mProjectGV.setAdapter(mProAdapter);
        } else if (mLicenseServiceList.size() > 0) {
            mReleaseUrl = NetworkUtil.LICENCE_SERVICE_URL;
            mProAdapter = new CollectionReleaseProAdapter(mLicenseServiceList,
                    CollectReleaseOrderActivity.this);
            mProjectGV.setAdapter(mProAdapter);
        } else {
            mReleaseUrl = "";// 解决商家被收藏后关闭掉所有的服务选项
        }
        ChooseType chooseType;
        if (mIllegalServiceList.size() > 0) {
            chooseType = new ChooseType();
            chooseType.setChoose(false);
            chooseType.setTypeName("违章代缴");
            chooseType.setServiceType(Constant.SERVICE_TYPE.ILLEGAL_SERVICE);
            mServiceTypes.add(chooseType);
        }
        if (mVehicleServiceList.size() > 0) {
            chooseType = new ChooseType();
            chooseType.setChoose(false);
            chooseType.setTypeName("车辆服务");
            chooseType.setServiceType(Constant.SERVICE_TYPE.CAR_SERVICE);
            mServiceTypes.add(chooseType);
        }
        if (mLicenseServiceList.size() > 0) {
            chooseType = new ChooseType();
            chooseType.setChoose(false);
            chooseType.setTypeName("驾证服务");
            chooseType.setServiceType(Constant.SERVICE_TYPE.LICENCE_SERVICE);
            mServiceTypes.add(chooseType);
        }
        if (mServiceTypes.size() > 0) {
            mServiceTypes.get(0).setChoose(true);
            mServiceType = mServiceTypes.get(0).getServiceType();
            mChooseTypeAdapter = new CommonAdapter<ChooseType>(
                    CollectReleaseOrderActivity.this, mServiceTypes,
                    R.layout.choose_type_item) {

                @SuppressLint("ResourceAsColor")
                @Override
                public void convert(ViewHolder holder, ChooseType t, int p) {

                    holder.setText(R.id.choose_type_item_tv, t.getTypeName());
                    if (t.isChoose()) {
                        holder.setBackgroundRes(R.id.choose_type_item_tv,
                                R.drawable.common_business_type_yes);
                        holder.setTextColor(R.id.choose_type_item_tv, R.color.white);
                    } else {
                        holder.setBackgroundRes(R.id.choose_type_item_tv,
                                R.drawable.service_type_radio_bg);
                        holder.setTextColor(R.id.choose_type_item_tv, R.color.black);
                    }
                }
            };
            mChooseTypeGV.setAdapter(mChooseTypeAdapter);
            mChoosesAdapter = new CommonAdapter<ChooseType>(
                    CollectReleaseOrderActivity.this, mServiceTypes,
                    R.layout.choose_img_item) {

                @Override
                public void convert(ViewHolder holder, ChooseType t, int p) {
                    if (t.isChoose()) {
                        holder.setVisible(R.id.choose_iv, true);
                    } else {
                        holder.setVisible(R.id.choose_iv, false);
                    }
                }
            };
            mChooseImgGV.setAdapter(mChoosesAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.service_release_btn:// 发布服务
                releaseService();

                break;
            case R.id.release_need:
                isSelected = true;
                isUnchecked = false;
                mReleaseOption.setVisibility(View.GONE);
                break;
            case R.id.release_no_need:
                isUnchecked = true;
                isSelected = false;
                mReleaseOption.setVisibility(View.GONE);
                break;
            case R.id.chedangdang_tv:// 车当当服务协议
                Intent intent = new Intent(this, AgreementActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;

            case R.id.xieyi_tv_two:// 车当当关于第三方服务商违章代办声明
                Intent intent2 = new Intent(this, AgreementActivity.class);
                intent2.putExtra("type", 3);
                startActivity(intent2);
                break;

            default:
                break;
        }
    }

    private void releaseService() {

        String price = mInputPriceET.getText().toString().trim();

        if (mInputPriceET.getText().toString().contains(".")) {
            String sdfk = mInputPriceET.getText().toString().substring(mInputPriceET.getText().toString().indexOf(".") + 1);
            if (sdfk.length() > 2) {
                ToastUtil.toastShow(CollectReleaseOrderActivity.this, "金额保留两位小数");
                return;
            }
        }
        if (mInputPriceET.getText().toString().equals(".")) {
            return;
        } else if (TextUtils.isEmpty(price)) {
            ToastUtil.toastShow(CollectReleaseOrderActivity.this, "请输入订单金额");
        }else if(TextUtils.isEmpty(maxPrice)){
            ToastUtil.toastShow(CollectReleaseOrderActivity.this,"网络异常，请检查网络是否连接");
        }else if (Double.parseDouble(mInputPriceET.getText().toString()) > Double.valueOf(maxPrice)) {
            ToastUtil.toastShow(CollectReleaseOrderActivity.this, "订单金额需小于" + maxPrice + "元");
        } else if (TextUtils.isEmpty(mReleaseUrl)) {
            ToastUtil.toastShow(CollectReleaseOrderActivity.this, "该商家暂无服务选项");
        } else if (Double.valueOf(price) < Double.valueOf(minPrice)) {
            ToastUtil.toastShow(CollectReleaseOrderActivity.this, "订单金额不低于" + minPrice + "元");
        } else if (TextUtils.isEmpty(mServicePro)) {
            ToastUtil.toastShow(CollectReleaseOrderActivity.this, "请选择服务项目");
        } else if (isSelected != true && isUnchecked != true) {
            mReleaseOption.setVisibility(View.VISIBLE);
            return;
        } else {
            AjaxParams params = new AjaxParams();
            if (Integer.parseInt(getIntent().getStringExtra("isBusOrKefu")) == Constant.CHAT_USER_ROLE.BUSINESS) {// 从即时通讯给商家下单
                params.put("userBId", getIntent().getStringExtra("businessId"));
            } else if (Integer.parseInt(getIntent().getStringExtra(
                    "isBusOrKefu")) == Constant.CHAT_USER_ROLE.SERVER) {// 从即时通讯给客服下单
                params.put("userBId", getIntent().getStringExtra("businessId"));
                params.put("handlerId", getIntent().getStringExtra("handlerId"));
            }
            if (isSelected == true) {
                params.put("provideBill", "1");
            } else {
                params.put("provideBill", "0");
            }
            params.put("projectName", mServicePro);
            params.put("spSta", "1");// 平台担保支付
            params.put("paymentAmount", price);
            requestNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:

                            AlertDialog.Builder dialog = new AlertDialog.Builder(
                                    CollectReleaseOrderActivity.this);
                            dialog.setCancelable(false);
                            dialog.setMessage("发布订单成功！");
                            dialog.setNegativeButton("继续发布",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            finishActivity();

                                        }
                                    });
                            if (CacheTools.getUserData("role").contains("2")
                                    || CacheTools.getUserData("role").contains("3")) {// 区分是A类商家和B类商家发单
                                dialog.setPositiveButton("进入秘书服务",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface arg0, int arg1) {

                                                Intent intent = new Intent(
                                                        CollectReleaseOrderActivity.this,
                                                        MyWorkActivity.class);
                                                startActivity(intent);
                                                finishActivity();
                                            }
                                        });

                            } else {
                                dialog.setPositiveButton("进入订单管理",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface arg0, int arg1) {
                                                Intent intent = new Intent(
                                                        CollectReleaseOrderActivity.this,
                                                        TaskManagerActivity.class);
                                                startActivity(intent);
                                                finishActivity();
                                            }
                                        });
                            }

                            dialog.show();

                            break;
                        case Constant.NET_DATA_FAIL:
                            ToastUtil.toastShow(CollectReleaseOrderActivity.this,
                                    ((DataError) msg.obj).getErrorMessage());
                            break;
                        default:
                            break;
                    }
                }

            }, params, mReleaseUrl, false, 0);
        }

    }
}
