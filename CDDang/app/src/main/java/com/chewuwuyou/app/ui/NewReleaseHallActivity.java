package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ReleaseProAdapter;
import com.chewuwuyou.app.bean.CityName;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.DistrictName;
import com.chewuwuyou.app.bean.ServicePro;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.MyGridView;

/**
 * 发布大厅
 *
 * @author zengys
 */
@SuppressLint("HandlerLeak")
public class NewReleaseHallActivity extends CDDBaseActivity implements
        OnClickListener {

    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private MyGridView mProjectGV;// 服务选项选择
    public static List<ServicePro> mAllServicePros;// 所有服务项目
    private List<ServicePro> mIllegalServiceList;
    private List<ServicePro> mVehicleServiceList;
    private List<ServicePro> mLicenseServiceList;
    private ReleaseProAdapter mProAdapter;
    private RadioGroup mServiceTypeRG;// 选择服务选项
    private ImageView mImageOne;
    private ImageView mImageTwo;
    private ImageView mImageThree;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private TextView mServiceLocationTV;// 服务地区
    // private EditText mContactET;// 联系人
    // private EditText mContactPhoneET;// 联系电话
    private Button mReleaseServiceBtn;// 发布
    private ServicePro mServicePro;// 选择的服务选项

    private int mRadioCheckId;// 标识选择的服务选项

    private String mProvinceId;
    private String mCityId;
    private String mDistrictId;
    private String mProvinceName;
    private String mCityName;
    private String mDistrictName;

    private List<CityName> mCityNames;
    private List<DistrictName> mDistrictNames;// 通过城市id查询地区的数据
    private TextView mXieyiTV;// 点击查看服务协议
    private TextView mXieyiTV_Two;

    private String city, province, district;// 接收定位 市 省 区
    private TextView mSubBarRightTV;//发布说明

    private TextView mReleaseOption;//是否需要票据
    private RadioButton mReleaseNeed, mReleaseNoNeed;//需要票据，不需要票据

    private boolean isSelected = false;
    private boolean isUnchecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_hall_layout);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mServiceLocationTV = (TextView) findViewById(R.id.choose_city_tv);
        mProjectGV = (MyGridView) findViewById(R.id.service_gv);
        mServiceTypeRG = (RadioGroup) findViewById(R.id.choose_type_rgroup);
        mImageOne = (ImageView) findViewById(R.id.image_one);
        mImageTwo = (ImageView) findViewById(R.id.image_two);
        mImageThree = (ImageView) findViewById(R.id.image_three);
        // mContactET = (EditText) findViewById(R.id.order_contact_et);
        // mContactPhoneET = (EditText)
        // findViewById(R.id.order_contact_phone_et);
        mReleaseServiceBtn = (Button) findViewById(R.id.service_release_btn);
        mXieyiTV = (TextView) findViewById(R.id.chedangdang_tv);
        mXieyiTV_Two = (TextView) findViewById(R.id.xieyi_tv_two);
        mSubBarRightTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mReleaseNeed = (RadioButton) findViewById(R.id.release_need);
        mReleaseNoNeed = (RadioButton) findViewById(R.id.release_no_need);
        mReleaseOption = (TextView) findViewById(R.id.release_option);
    }

    @Override
    protected void initData() {
        mXieyiTV_Two.setOnClickListener(this);
        mXieyiTV.setOnClickListener(this);
        if (TextUtils.isEmpty(getIntent().getStringExtra("serviceLoc"))) {
            mServiceLocationTV.setText("请选择城市");
        } else {
            mServiceLocationTV
                    .setText(getIntent().getStringExtra("serviceLoc"));
        }
        // 查看是否有当前城市的缓存，如果没有就重新定位，并讲定位结果存入缓存
//		if (CacheTools.getUserData("city") == null) {
//			getMyLocation();
//		}
        mSubBarRightTV.setVisibility(View.VISIBLE);
        mSubBarRightTV.setText("发布说明");
        // 设置当前城市
        city = CacheTools.getUserData("city");
        province = CacheTools.getUserData("province");
        district = CacheTools.getUserData("district");
//		if (TextUtils.isEmpty(city)) {
//			mServiceLocationTV.setText("请选择城市");
//		} else {
//			mServiceLocationTV.setText(province + city + district);
//			setCity();// 查询定位城市ID
//		}

        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断沉浸式状态栏
        // mContactET.setText(CacheTools.getUserData("nickName"));
        // mContactPhoneET.setText(CacheTools.getUserData("telephone"));
        mTitleTV.setText("发布订单");
        mProvinceId = getIntent().getStringExtra("provinceId");
        mCityId = getIntent().getStringExtra("cityId");
        mDistrictId = getIntent().getStringExtra("districtId");
        MyLog.i("YUY", "省市区ID = " + mProvinceId + " = " + mCityId + " = "
                + mDistrictId);
        getProData();
    }

    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        mReleaseServiceBtn.setOnClickListener(this);
        mServiceLocationTV.setOnClickListener(this);
        mReleaseServiceBtn.setOnClickListener(this);
        mSubBarRightTV.setOnClickListener(this);
        mReleaseNeed.setOnClickListener(this);
        mReleaseNoNeed.setOnClickListener(this);

        mServiceTypeRG
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup arg0, int arg1) {
                        mProjectGV.setVisibility(View.VISIBLE);
                        mServicePro = null;
                        switch (arg1) {
                            case R.id.rabtn_ralease_break:// 违章服务
                                mRadioCheckId = 0;
                                mImageOne.setVisibility(View.VISIBLE);
                                mImageTwo.setVisibility(View.INVISIBLE);
                                mImageThree.setVisibility(View.INVISIBLE);
                                mXieyiTV_Two.setVisibility(View.VISIBLE);
                                mProAdapter = new ReleaseProAdapter(
                                        mIllegalServiceList,
                                        NewReleaseHallActivity.this);
                                mProjectGV.setAdapter(mProAdapter);

                                break;
                            case R.id.accreditation_Rbn:// 车辆服务
                                mRadioCheckId = 1;
                                mImageOne.setVisibility(View.INVISIBLE);
                                mImageTwo.setVisibility(View.VISIBLE);
                                mImageThree.setVisibility(View.INVISIBLE);
                                mXieyiTV_Two.setVisibility(View.GONE);
                                mProAdapter = new ReleaseProAdapter(
                                        mVehicleServiceList,
                                        NewReleaseHallActivity.this);
                                mProjectGV.setAdapter(mProAdapter);
                                break;
                            case R.id.ticket_Rbn:// 驾证服务
                                mRadioCheckId = 2;
                                mXieyiTV_Two.setVisibility(View.GONE);
                                mImageOne.setVisibility(View.INVISIBLE);
                                mImageTwo.setVisibility(View.INVISIBLE);
                                mImageThree.setVisibility(View.VISIBLE);
                                mProAdapter = new ReleaseProAdapter(
                                        mLicenseServiceList,
                                        NewReleaseHallActivity.this);
                                mProjectGV.setAdapter(mProAdapter);
                                break;
                            default:
                                break;
                        }
                    }
                });

        mProjectGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                mProAdapter.setSeclection(arg2);
                mProAdapter.notifyDataSetChanged();
                if (mRadioCheckId == 0) {
                    mServicePro = mIllegalServiceList.get(arg2);
                } else if (mRadioCheckId == 1) {
                    mServicePro = mVehicleServiceList.get(arg2);
                } else {
                    mServicePro = mLicenseServiceList.get(arg2);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            mCityName = data.getStringExtra("city");
            mProvinceName = data.getStringExtra("province");
            mDistrictName = data.getStringExtra("district");
            mDistrictId = data.getStringExtra("districtId");
            mCityId = data.getStringExtra("cityId");
            mProvinceId = data.getStringExtra("provinceId");

            if (mCityId == null) {
                getCityIdAndDisId();
            }
            if (TextUtils.isEmpty(mDistrictName)) {
                mDistrictId = "0";
                if (mProvinceName.equals(mCityName)) {
                    mServiceLocationTV.setText(mCityName);
                } else {
                    mServiceLocationTV.setText(mProvinceName + mCityName);
                }
            } else {
                if (mProvinceName.equals(mCityName)
                        && mCityName.equals(mDistrictName)) {
                    mServiceLocationTV.setText(mDistrictName);
                } else if (mCityName.equals(mProvinceName)
                        && !mCityName.equals(mDistrictName)) {
                    mServiceLocationTV.setText(mCityName + mDistrictName);
                } else if (mCityName.equals(mDistrictName)
                        && !mProvinceName.equals(mDistrictName)) {
                    mServiceLocationTV.setText(mDistrictName);
                } else {
                    mServiceLocationTV.setText(mProvinceName + mCityName
                            + mDistrictName);
                }

            }
        }

    }

    /**
     * 发布大厅请求服务选项数据方法
     */
    @SuppressLint("HandlerLeak")
    private void getProData() {
        mAllServicePros = new ArrayList<ServicePro>();
        mIllegalServiceList = new ArrayList<ServicePro>();
        mVehicleServiceList = new ArrayList<ServicePro>();
        mLicenseServiceList = new ArrayList<ServicePro>();
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            JSONArray mJsonArray = jo.getJSONArray("projects");
                            for (int i = 0; i < mJsonArray.length(); i++) {
                                mAllServicePros.add(new ServicePro(Integer
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
                            for (int i = 0; i < mAllServicePros.size(); i++) {// 筛选掉上门服务和其他服务
                                String projectName = mAllServicePros.get(i)
                                        .getProjectName();
                                ServicePro servicePro = mAllServicePros.get(i);
                                int mserviceType = mAllServicePros.get(i).getType();
                                if (projectName.contains("上门")
                                        || projectName.contains("其他")) {
                                } else if (mserviceType == 1) {
                                    mIllegalServiceList.add(servicePro);
                                } else if (mserviceType == 2) {
                                    mVehicleServiceList.add(servicePro);
                                } else {
                                    mLicenseServiceList.add(servicePro);
                                }

                            }
                            mProAdapter = new ReleaseProAdapter(
                                    mIllegalServiceList,
                                    NewReleaseHallActivity.this);
                            mProjectGV.setAdapter(mProAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        break;
                }
            }

        }, null, NetworkUtil.GET_ALL_PRO, false, 1);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.choose_city_tv:
                Constant.CITY_CHOOSE = 1;
                intent = new Intent(NewReleaseHallActivity.this,
                        AreaSelectActivity.class);
                startActivityForResult(intent, 20);
                break;
            case R.id.service_release_btn:
                releaseService();
                break;
            case R.id.chedangdang_tv:
                intent = new Intent(NewReleaseHallActivity.this,
                        AgreementActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.xieyi_tv_two:
                intent = new Intent(NewReleaseHallActivity.this,
                        AgreementActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
                break;

            case R.id.sub_header_bar_right_tv:
                intent = new Intent(NewReleaseHallActivity.this, RechargeExplainActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
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
            default:
                break;
        }
    }

    /**
     * 发布服务
     */
    private void releaseService() {

        if (TextUtils.isEmpty(mProvinceId) || TextUtils.isEmpty(mCityId)) {
            ToastUtil.toastShow(NewReleaseHallActivity.this, "请选择服务办理地区");
        } else if (mServicePro == null) {
            ToastUtil.toastShow(NewReleaseHallActivity.this, "请选择要办理的服务选项");
        } else if (isSelected != true && isUnchecked != true) {
            mReleaseOption.setVisibility(View.VISIBLE);
        } else {
            AjaxParams params = new AjaxParams();
            params.put("projectNum",
                    String.valueOf(mServicePro.getProjectNum()));
            params.put("taskType", String.valueOf(mServicePro.getType()));
            params.put("provinceId", mProvinceId);
            params.put("cityId", mCityId);
            params.put("districtId", mDistrictId);
            if (isSelected == true) {
                params.put("provideBill", "1");
            } else {
                params.put("provideBill", "0");
            }
            requestNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            MyLog.i("YUY", "B类商家发布订单 = " + msg.obj.toString());
                            AlertDialog.Builder dialog = new AlertDialog.Builder(
                                    NewReleaseHallActivity.this);
                            dialog.setCancelable(false);
                            dialog.setTitle("温馨提示");
                            dialog.setMessage("订单发布成功！是否前往订单管理页面？");
                            dialog.setPositiveButton("确认",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            Intent intent = new Intent(
                                                    NewReleaseHallActivity.this,
                                                    OrderStatusManagerActivity.class);
                                            intent.putExtra(
                                                    "orderType",
                                                    Constant.ORDER_TYPE.OVERSEAS_ORDER);
                                            intent.putExtra(
                                                    "orderStatus",
                                                    Constant.ORDERSTATUSMANAGE.TODAY_ORDER);
                                            startActivity(intent);
                                        }
                                    });
                            dialog.setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            Intent intent = new Intent();
                                            intent.putExtra(
                                                    "orderType",
                                                    Constant.ORDER_TYPE.OVERSEAS_ORDER);
                                            intent.putExtra(
                                                    "orderStatus",
                                                    Constant.ORDERSTATUSMANAGE.TODAY_ORDER);

                                            finishActivity();
                                        }
                                    });
                            dialog.create().show();
                            break;
                        case Constant.NET_DATA_FAIL:
                            ToastUtil.toastShow(NewReleaseHallActivity.this, ((DataError) msg.obj).getErrorMessage());
                            break;
                        default:
                            ToastUtil.toastShow(NewReleaseHallActivity.this, "发布订单失败");
                            break;
                    }
                }

            }, params, NetworkUtil.B_NEW_TASK, false, 0);
        }
    }

    /**
     * 获取当前定位地区的省份及城市id
     */
    private void getCityIdAndDisId() {
        if (CacheTools.getUserData("citysData") != null) {
            mCityNames = CityName.parses(CacheTools.getUserData("citysData")
                    .toString());
            // 通过比较查询城市id 必须要求服务器的本地数据与定位的数据的一致
            for (CityName city : mCityNames) {
                if (mCityName.equals(city.getCityName())) {
                    mCityId = String.valueOf(city.getId());
                    mProvinceId = String.valueOf(city.getProvinceId());
                }
            }
            getDistrictId(mCityId);
        } else {
            getAllCity();// 查询所有城市
        }

    }

    /**
     * 获取所有城市的数据
     */
    private void getAllCity() {
        NetworkUtil.postNoHeader(NetworkUtil.GET_ALL_CITY, null,
                new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        try {
                            JSONObject jo = new JSONObject(t.toString());
                            CacheTools.setUserData(
                                    "citysData",
                                    jo.getJSONObject("data")
                                            .getJSONArray("cities").toString());// 存入所有的城市数据
                            getCityIdAndDisId();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {

                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    /**
     * 通过城市id查询地区数据从而获得地区id
     *
     * @param cityId
     */
    @SuppressLint("HandlerLeak")
    private void getDistrictId(String cityId) {

        AjaxParams params = new AjaxParams();
        params.put("cityId", cityId);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            mDistrictNames = DistrictName.parses(jo.getJSONArray(
                                    "districts").toString());
                            for (DistrictName district : mDistrictNames) {
                                if (mDistrictName.equals(district.getDistrictName())) {
                                    mDistrictId = String.valueOf(district.getId());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.GET_DISTRICT_BY_CITY, false, 1);

    }

    /**
     * 查询定位城市ID
     */
    @SuppressLint("HandlerLeak")
    private void setCity() {
        AjaxParams params = new AjaxParams();

        params.put("provinceName", province);
        params.put("cityName", city);
        params.put("districtName", district);

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("yang", msg.obj.toString());

                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            mProvinceId = String.valueOf(jo.getInt("provinceId"));
                            mCityId = String.valueOf(jo.getInt("cityId"));
                            mDistrictId = String.valueOf(jo.getInt("districtId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    default:
                        break;
                }
            }

        }, params, NetworkUtil.INQUIRE_CITY_ID, false, 0);
    }

}
