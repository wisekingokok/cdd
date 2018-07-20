package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AreaInfo;
import com.chewuwuyou.app.bean.CompanyService;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.fragment.AreaFragment;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NewAdvantageActivity extends BaseFragmentActivity implements View.OnClickListener {

    public static final String PUBLISH_TYPE = "type";
    private static final int CHOOSE_ADDRESS = 10;
    private EditText mEditText_title;
    private EditText mEditText_content;
    private TextView mTextView_title;
    private TextView mTextView_right;
    private TextView mTextView_address;
    private String province;
    private String city;
    private String area;
    private ImageButton mBackIBtn;// 返回
    private FragmentManager fragmentManager;
    private int mType;
    private String provinceId;
    private String cityId;
    private String areaId;

    public void setCity(String privoce, String city, String area) {

        this.province = privoce;
        this.city = city;
        this.area = area;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advantage);
        initView();
        initData();
        initEvent();

    }


    protected void initView() {
        mTextView_address = (TextView) findViewById(R.id.new_advantage_address);
        mTextView_title = (TextView) findViewById(R.id.sub_header_bar_tv);
        mTextView_right = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mEditText_content = (EditText) findViewById(R.id.content);
        mEditText_title = (EditText) findViewById(R.id.title);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        // 三级联动 fragment 城市选择
        fragmentManager = getSupportFragmentManager();
    }


    protected void initData() {

        mTextView_right.setText("发布");
        mTextView_right.setVisibility(View.VISIBLE);
        mType = getIntent().getIntExtra(PUBLISH_TYPE, 0);
        if (mType == 0)
            mTextView_title.setText("发布优势");
        else
            mTextView_title.setText("发布求助");
        getAddress();

    }

    private void getAddress() {
        // 获取缓存的地址信息
        province = CacheTools.getUserData("province");
        city = CacheTools.getUserData("city");
        area = CacheTools.getUserData("district");

        provinceId = CacheTools.getUserData("provinceId");
        cityId = CacheTools.getUserData("cityId");
        areaId = CacheTools.getUserData("districtId");

        if (!TextUtils.isEmpty(province) || !TextUtils.isEmpty(city) || !TextUtils.isEmpty(area))
            mTextView_address.setText("服务地址：" + province + city + area);
        else {
            mTextView_address.setText("定位中……");
            getMyLocation();
        }

    }


    protected void initEvent() {

        mTextView_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mEditText_title.getText().toString())) {
                    ToastUtil.toastShow(NewAdvantageActivity.this, "标题不能为空哦！");
                } else if (TextUtils.isEmpty(mEditText_content.getText().toString())) {
                    ToastUtil.toastShow(NewAdvantageActivity.this, "内容不能为空哦！");
                } else if (mEditText_title.getText().toString().length() < 4) {
                    ToastUtil.toastShow(NewAdvantageActivity.this, "标题字数不能少于4个字哦！");
                } else if (mEditText_content.getText().toString().length() < 4) {
                    ToastUtil.toastShow(NewAdvantageActivity.this, "内容字数不能少于4个字哦！");
                } else
                    publish();

            }
        });

        mBackIBtn.setOnClickListener(this);
        mTextView_address.setOnClickListener(this);

        mEditText_content.setFilters(new InputFilter[]{EMOJI_FILTER});

        mEditText_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 150)
                {
                    ToastUtil.toastShow(NewAdvantageActivity.this,"输入内容不能超过150字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    ToastUtil.toastShow(AppContext.getInstance(), "目前暂不支持输入表情符号，会导致发表失败哟");
                    return "";
                }
            }
            return null;
        }
    };

    private void publish() {

        final ProgressDialog mProgressDialog = ProgressDialog.show(this, null, "发布中", false, false);
        AjaxParams params = new AjaxParams();
        params.put("type", String.valueOf(mType + 1));
        params.put("province", province);
        params.put("provinceId", provinceId);
        params.put("city", city);
        params.put("cityId", cityId);
        params.put("area", area);
        params.put("areaId", areaId);
        params.put("content", mEditText_content.getText().toString());
        params.put("title", mEditText_title.getText().toString());

        NetworkUtil.postMulti(NetworkUtil.ADD_SJYS, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String mS) {
                super.onSuccess(mS);
                mProgressDialog.cancel();
                new AlertDialog.Builder(NewAdvantageActivity.this).setMessage("发布成功！").setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_OK);
                        NewAdvantageActivity.this.finish();
                    }
                }).show();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(NewAdvantageActivity.this, "发布失败");
                mProgressDialog.cancel();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.new_advantage_address:
                Intent chooseCityIntent = new Intent(this, AreaSelectActivity.class);
                startActivityForResult(chooseCityIntent, CHOOSE_ADDRESS);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_ADDRESS && resultCode == RESULT_OK) {

            if (data.getStringExtra("province").toString().equals(data.getStringExtra("city").toString())) {
                mTextView_address.setText(data.getStringExtra("city"));
            } else {
                mTextView_address.setText("服务地址：" + data.getStringExtra("province") + data.getStringExtra("city") + data.getStringExtra("district"));
            }
            area = data.getStringExtra("district");
            city = data.getStringExtra("city");
            province = data.getStringExtra("province");

            provinceId = data.getStringExtra("provinceId");
            cityId = data.getStringExtra("cityId");
            areaId = data.getStringExtra("districtId");

        }
    }

    public static void launch(Context mContext, Bundle m) {
        Intent mIntent = new Intent(mContext, NewAdvantageActivity.class);
        if (m != null)
            mIntent.putExtras(m);
        mContext.startActivity(mIntent);

    }

    public MyLocationListenner myListener = new MyLocationListenner();// 定位
    public LocationClient mLocationClient;

    /**
     * 定位自己的位置
     */
    public void getMyLocation() {
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();// 设定定位参数
        mLocationClient.start();// 开始定位
    }

    // 定位
    // 设置相关参数
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.disableCache(false);// 禁止启用缓存定位
        option.setPoiNumber(5); // 最多返回POI个数
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.setPoiDistance(1000); // poi查询距离
        option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
        mLocationClient.setLocOption(option);

    }

    /**
     * 监听函数，更新位置
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        // 接收位置信息
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                CacheTools.setUserData("province", location.getProvince());
                CacheTools.setUserData("city", location.getCity());
                CacheTools.setUserData("district", location.getDistrict());
                CacheTools.setUserData("Lat", location.getLatitude() + "");
                CacheTools.setUserData("Lng", location.getLongitude() + "");
                city = CacheTools.getUserData("city");
                province = CacheTools.getUserData("province");
                area = CacheTools.getUserData("district");
                mTextView_address.setText(province + city + area);
                // 退出时销毁定位
                mLocationClient.stop();
            }
        }


        // 接收POI信息函数
        @Override
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }


}
