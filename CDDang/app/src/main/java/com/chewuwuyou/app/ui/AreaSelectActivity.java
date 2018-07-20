package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CityListViewAdapter;
import com.chewuwuyou.app.bean.AreaInfo;
import com.chewuwuyou.app.bean.LatelyCity;
import com.chewuwuyou.app.fragment.AreaFragment;
import com.chewuwuyou.app.fragment.AreaFragment.OnFragmentInteractionListener;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.eim.db.DBctiy;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author ZYS 新三级城市选择
 */

public class AreaSelectActivity extends BaseFragmentActivity implements OnFragmentInteractionListener {

    private Fragment mOneFragment;
    private Fragment mTwoFragment;
    private Fragment mThreeFragment;
    private TextView nowmodel_tv;// 标题,当前位置,三个最近访问城市
    private String city, province, district;// 接收定位 市 省 区
    private int provinceId, cityId, districtId;// 省市区ID
    public LocationClient mLocationClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private CityListViewAdapter mCityGridAdapter;
    private ListView mLatelycity;// 最近访问城市
    private List<LatelyCity> mRecentList;// 最近访问城市集合
    private DBctiy dbCity = new DBctiy(this);
    private TextView selectAddress;
    private RadioButton provinceBtn, cityBtn, districtBtn;
    private ImageView close;
    private LinearLayout ll;
    private View listFL;
    private ListView listView;
    private EditText searchET;
    private ImageButton clear;
    private TextView cancel;
    private FragmentManager fragmentManager;
    private String proName;
    private boolean isChanged = false;
    private int pageNum = 0;
    private CityListViewAdapter listAdapter;
    private View bg;
    private View rootView;
    public static AreaInfo selectInfo = new AreaInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_select);
        initView();
        initData();
    }

    protected void initView() {
        // 当前位置的显示
        nowmodel_tv = (TextView) findViewById(R.id.nowmodel_tv);
        mLatelycity = (ListView) findViewById(R.id.lately_city);// 最近访问城市
        selectAddress = (TextView) findViewById(R.id.selectAddress);
        close = (ImageView) findViewById(R.id.close);
        selectAddress = (TextView) findViewById(R.id.selectAddress);
        provinceBtn = (RadioButton) findViewById(R.id.province);
        cityBtn = (RadioButton) findViewById(R.id.city);
        districtBtn = (RadioButton) findViewById(R.id.district);
        ll = (LinearLayout) findViewById(R.id.ll);
        listFL = findViewById(R.id.listFL);
        listView = (ListView) findViewById(R.id.listView);
        searchET = (EditText) findViewById(R.id.searchET);
        clear = (ImageButton) findViewById(R.id.clear);
        cancel = (TextView) findViewById(R.id.cancel);
        bg = findViewById(R.id.bg);
        rootView = findViewById(R.id.rootView);
        listView.setAdapter(listAdapter = new CityListViewAdapter(this, null));
        // 三级联动 fragment 城市选择
        fragmentManager = getSupportFragmentManager();
        setSltTab(0, "", null, "", false);

        selectAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setVisibility(View.VISIBLE);
            }
        });
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.CITY_CHOOSE == 0) finishActivity();
                else
                    ll.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        findViewById(R.id.listShadow).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        provinceBtn.setOnClickListener(click);
        cityBtn.setOnClickListener(click);
        districtBtn.setOnClickListener(click);
        clear.setOnClickListener(click);
        cancel.setOnClickListener(click);
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(StringFilter(s.toString()))) {
                    listAdapter.setData(null);
                    listFL.setVisibility(View.GONE);
                } else {
                    listFL.setVisibility(View.VISIBLE);
                    search(StringFilter(s.toString()));
                }
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LatelyCity info = (LatelyCity) listAdapter.getItem(position);
                hideKeyboard();
                if (info.getProvinceId() == -1) return;
                if (info.getDistrict() == null && info.getCity() == null) {//省
                    setSltTab(0, "", null, info.getProvince(), true);
                    AreaInfo areaInfo = new AreaInfo();
                    areaInfo.setId(info.getProvinceId());
                    areaInfo.setProvinceName(info.getProvince());
                    onFragmentInteraction(areaInfo, "");
                    cityBtn.setText("市");
                } else if (info.getDistrict() == null) {//市级
                    setSltTab(0, "", null, info.getProvince(), true);
                    AreaInfo areaInfo = new AreaInfo();
                    areaInfo.setId(info.getProvinceId());
                    areaInfo.setProvinceName(info.getProvince());
                    AreaInfo areaInfo2 = new AreaInfo();
                    areaInfo2.setId(info.getCityId());
                    areaInfo2.setCityName(info.getCity());
                    onFragmentInteraction(areaInfo, info.getCity());
                    onFragmentInteraction(areaInfo2, "");
                } else {
                    AreaInfo areaInfo = new AreaInfo();
                    areaInfo.setProvinceId(info.getProvinceId());
                    areaInfo.setProvinceName(info.getProvince());
                    areaInfo.setCityId(info.getCityId());
                    areaInfo.setCityName(info.getCity());
                    areaInfo.setDistrictName(info.getDistrict());
                    areaInfo.setId(info.getDistrictId());
                    cityDb(mRecentList, String.valueOf(areaInfo.getCityId()),
                            String.valueOf(areaInfo.getProvinceId()),
                            String.valueOf(areaInfo.getId()),
                            areaInfo.getProvinceName(), areaInfo.getCityName(),
                            areaInfo.getDistrictName());// 保存到数据库
                    Intent intent = new Intent();
                    intent.putExtra("chooseDistrict", areaInfo.getProvinceName()
                            + areaInfo.getCityName() + areaInfo.getDistrictName() + "");
                    intent.putExtra("city", areaInfo.getCityName() + "");
                    intent.putExtra("district", areaInfo.getDistrictName() + "");
                    intent.putExtra("provinceId", areaInfo.getProvinceId() + "");
                    intent.putExtra("province", areaInfo.getProvinceName() + "");
                    intent.putExtra("cityId", areaInfo.getCityId() + "");
                    intent.putExtra("districtId", areaInfo.getId() + "");
                    setResult(RESULT_OK, intent);
                    Constant.CITY_CHOOSE = 0;
                    finish();
                }
                ll.setVisibility(View.VISIBLE);
//                listFL.setVisibility(View.GONE);
            }
        });

        switch (Constant.CITY_CHOOSE) {
            case 0:
                listFL.setVisibility(View.GONE);
                bg.setVisibility(View.GONE);
                rootView.setVisibility(View.GONE);
                ll.setVisibility(View.VISIBLE);
                break;
        }
    }

    // 过滤特殊字符
    public static String StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    protected void hideKeyboard() {
        InputMethodManager mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            if (mImm.isActive()) {
                mImm.hideSoftInputFromWindow(getCurrentFocus()
                                .getApplicationWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
        }
    }


    //    SEARCH_LOCATION
    private void search(String str) {
        AjaxParams params = new AjaxParams();
        params.put("address", str);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS://{"adresses":[{"ids":"28_225_1878","name":"四川省成都市"}]}
                        try {
                            JSONObject obj = new JSONObject(msg.obj.toString());
                            JSONArray adresses = obj.getJSONArray("adresses");
                            List<LatelyCity> latelyCityList = new ArrayList<LatelyCity>();
                            if (adresses.length() <= 0) {
                                LatelyCity latelyCity = new LatelyCity();
                                latelyCity.setProvince("未搜索到该地址");
                                latelyCity.setProvinceId(-1);
                                latelyCityList.add(latelyCity);
                            }
                            for (int i = 0; i < adresses.length(); i++) {
                                LatelyCity latelyCity = new LatelyCity();
                                JSONObject address = adresses.getJSONObject(i);
                                String id = address.getString("ids");
                                String name = address.getString("name");
                                String[] ids = id.split("_");
                                String[] names = name.split("_");
                                if (ids.length == 1) {
                                    latelyCity.setProvinceId(Integer.parseInt(ids[0]));
                                    latelyCity.setProvince(names[0]);
                                } else if (ids.length == 2) {
                                    latelyCity.setProvinceId(Integer.parseInt(ids[0]));
                                    latelyCity.setProvince(names[0]);
                                    latelyCity.setCityId(Integer.parseInt(ids[1]));
                                    latelyCity.setCity(names[1]);
                                } else if (ids.length == 3) {
                                    latelyCity.setProvinceId(Integer.parseInt(ids[0]));
                                    latelyCity.setProvince(names[0]);
                                    latelyCity.setCityId(Integer.parseInt(ids[1]));
                                    latelyCity.setCity(names[1]);
                                    latelyCity.setDistrictId(Integer.parseInt(ids[2]));
                                    latelyCity.setDistrict(names[2]);
                                }
                                latelyCityList.add(latelyCity);
                            }
                            listAdapter.setData(latelyCityList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            List<LatelyCity> latelyCityList = new ArrayList<LatelyCity>();
                            LatelyCity latelyCity = new LatelyCity();
                            latelyCity.setProvince("未搜索到该地址");
                            latelyCity.setProvinceId(-1);
                            latelyCityList.add(latelyCity);
                            listAdapter.setData(latelyCityList);
                        }
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.SEARCH_LOCATION, false, 0);
    }

    private OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.province:
                    setSltTab(0, "", null, "", false);
                    break;
                case R.id.city:
                    setSltTab(1, null, null, "", false);
                    break;
                case R.id.district:
                    if (!isChanged)
                        setSltTab(2, null, null, "", false);
                    else setCheck();
                    break;
                case R.id.clear:
                    searchET.setText("");
                    break;
                case R.id.cancel:
                    finishActivity();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (ll.isShown()) {
            if (Constant.CITY_CHOOSE == 0) finishActivity();
            else
                ll.setVisibility(View.GONE);
            return;
        } else if (listFL.isShown()) {
            listFL.setVisibility(View.GONE);
        } else
            super.onBackPressed();
    }

    /**
     * 设置当前选中标签
     *
     * @param index
     */
    private void setSltTab(int index, String id, String name, String selectName, boolean needNew) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideView(transaction);
        switch (index) {
            case 0:
                if (needNew || mOneFragment == null) {
                    mOneFragment = AreaFragment.newInstance(1, id, name, selectName);
                    transaction.add(R.id.content, mOneFragment);
                } else {
                    transaction.show(mOneFragment);
                }
                break;
            case 1:
                if (needNew || mTwoFragment == null) {
                    if (id == null && name == null) {
                        setCheck();
                        return;
                    }
                    selectInfo.setProvinceId(Integer.parseInt(id));
                    selectInfo.setProvinceName(name);
                    mTwoFragment = AreaFragment.newInstance(2, id, name, selectName);
                    transaction.add(R.id.content, mTwoFragment);
                } else {
                    transaction.show(mTwoFragment);
                }
                break;
            case 2:
                if (needNew || mThreeFragment == null) {
                    if (id == null && name == null) {
                        setCheck();
                        return;
                    }
                    mThreeFragment = AreaFragment.newInstance(3, id, name, selectName);
                    transaction.add(R.id.content, mThreeFragment);
                    selectInfo.setCityId(Integer.parseInt(id));
                    selectInfo.setCityName(name);
                } else {
                    transaction.show(mThreeFragment);
                }
                break;
        }
        transaction.commit();
        pageNum = index;
    }

    private void setCheck() {
        switch (pageNum) {
            case 0:
                provinceBtn.setChecked(true);
                break;
            case 1:
                cityBtn.setChecked(true);
                break;
        }
    }

    private void hideView(FragmentTransaction transaction) {
        if (mOneFragment != null) {
            transaction.hide(mOneFragment);
        }
        if (mTwoFragment != null) {
            transaction.hide(mTwoFragment);
        }
        if (mThreeFragment != null) {
            transaction.hide(mThreeFragment);
        }
    }

    protected void initData() {
        // 查看是否有当前城市的缓存，如果没有就重新定位，并讲定位结果存入缓存
        if (CacheTools.getUserData("city") == null) {
            getMyLocation();
        }
        // 设置当前城市
        city = CacheTools.getUserData("city");
        province = CacheTools.getUserData("province");
        district = CacheTools.getUserData("district");
        if (TextUtils.isEmpty(city)) {
            nowmodel_tv.setText("定位中.....");
        } else {
            nowmodel_tv.setText(province + city + district);
            setCity();// 查询定位城市ID
        }

        nowmodel_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (nowmodel_tv.getText().toString().trim().equals("定位中.....")) {

                } else {
                    Intent intent = new Intent();
                    intent.putExtra("city", CacheTools.getUserData("city")); // 返回定位城市
                    intent.putExtra("province",
                            CacheTools.getUserData("province").trim()); // 返回定位城市
                    intent.putExtra("district",
                            CacheTools.getUserData("district").trim()); // 返回定位城市

                    intent.putExtra("provinceId", provinceId + "");// 返回省ID
                    intent.putExtra("cityId", cityId + "");// 返回市ID
                    intent.putExtra("districtId", districtId + "");// 返回区ID

                    setResult(RESULT_OK, intent);
                    finishActivity();
                }
            }
        });

        mRecentList = new ArrayList<LatelyCity>();
        mRecentList = dbCity.seltelCtiy();// 查询城市
        mCityGridAdapter = new CityListViewAdapter(AreaSelectActivity.this,
                mRecentList);
        mLatelycity.setAdapter(mCityGridAdapter);// 最近访问城市集合

        /**
         * 点击最近访问城市
         */
        mLatelycity.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("chooseDistrict", mRecentList.get(position)
                        .getCity() + mRecentList.get(position).getDistrict());
                intent.putExtra("city", mRecentList.get(position).getCity());
                intent.putExtra("district", mRecentList.get(position)
                        .getDistrict());
                intent.putExtra("provinceId", mRecentList.get(position)
                        .getProvinceId() + "");
                intent.putExtra("province", mRecentList.get(position)
                        .getProvince());
                intent.putExtra("cityId", mRecentList.get(position).getCityId()
                        + "");
                intent.putExtra("districtId", mRecentList.get(position)
                        .getDistrictId() + "");
                setResult(RESULT_OK, intent);
                finishActivity();
            }
        });
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                switch (fragmentManager.getBackStackEntryCount()) {
                    case 0:
                        provinceBtn.setChecked(true);
                        provinceBtn.setText("省");
                        break;
                    case 1:
                        cityBtn.setChecked(true);
                        cityBtn.setText("市");
                        break;
                    case 2:
                        districtBtn.setChecked(true);
                        districtBtn.setText("区");
                        break;
                    case 3:
                        break;
                }
            }
        });
    }

    /**
     * 返回上一个Fragment
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    finish();
                }
                break;
        }
        return true;
    }

    /**
     * 处理交互，hide前一个fragment，并且调用addToBackStack让Fragment可以点击back的时候显示前一个fragment
     * 如果是第三级地区则直接返回地区选择数据给上个Activity
     *
     * @param areaInfo 被点击的地区信息
     */

    @Override
    public void onFragmentInteraction(AreaInfo areaInfo, String selectName) {
        if (areaInfo == null)
            return;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        /**
         *
         * 根据省、市、区三个接口的数据形式来判断传相应的接口和请求字段
         *
         * 1、areaInfo里只有第一级地区名，那就请求城市； 2、areaInfo里只有第二级地区名，那就请求区；
         * 3、areaInfo里第一，第二，第三级地区名都有，说明已选择完地区，应做相应的逻辑处理
         */

        Intent intent;
        if (areaInfo.getCityName() == null && areaInfo.getDistrictName() == null) {// 点击第一次地区选择
            transaction.hide(mOneFragment);
            selectInfo.setProvinceId(areaInfo.getProvinceId());
            selectInfo.setProvinceName(areaInfo.getProvinceName());
            setSltTab(1, String.valueOf(areaInfo.getId()), areaInfo.getProvinceName(), selectName, true);
            cityBtn.setChecked(true);
            if (proName == null || areaInfo.getProvinceName().equals(proName))
                isChanged = false;
            else
                isChanged = true;
            proName = areaInfo.getProvinceName();
            provinceBtn.setText(areaInfo.getProvinceName());
            cityBtn.setText("市");
        } else if (areaInfo.getProvinceName() == null && areaInfo.getDistrictName() == null) {// 点击第二次地区选择
            // 判断二级城市里 澳门特别行政区 和 新疆自治区里的二级城市 (注：这两地方没有第三级地区);新增：湖北省仙桃市也没有第三级地区
            isChanged = false;
            if (areaInfo.getId() == 341 || areaInfo.getId() == 343
                    || areaInfo.getId() == 347 || areaInfo.getId() == 349
                    || areaInfo.getId() == 389 || areaInfo.getId() == 390
                    || areaInfo.getId() == 391 || areaInfo.getId() == 392
                    || areaInfo.getId() == 393 || areaInfo.getId() == 363
                    || areaInfo.getId() == 173 || areaInfo.getId() == 210
                    || areaInfo.getId() == 205 || areaInfo.getId() == 272) {
                intent = new Intent();
                if (areaInfo.getId() != 363 && areaInfo.getId() != 173 && areaInfo.getId() != 210 && areaInfo.getId() != 205 && areaInfo.getId() != 272) {
                    intent.putExtra("province", "新疆生产建设兵团");
                    intent.putExtra("provinceId", "35");
                    cityDb(mRecentList, areaInfo.getId() + "", "35", "",
                            "新疆生产建设兵团", areaInfo.getCityName() + "", "");
                } else if (areaInfo.getId() == 173) {
                    intent.putExtra("province", "湖北省");
                    intent.putExtra("provinceId", "17");
                    cityDb(mRecentList, areaInfo.getId() + "", "17", "", "湖北省",
                            areaInfo.getCityName() + "", "");
                } else if (areaInfo.getId() == 210) {//东莞
                    intent.putExtra("province", "广东省");
                    intent.putExtra("provinceId", "19");
                    cityDb(mRecentList, areaInfo.getId() + "", "19", "",
                            "广东省", areaInfo.getCityName() + "", "");
                } else if (areaInfo.getId() == 205) {//中山
                    intent.putExtra("province", "广东省");
                    intent.putExtra("provinceId", "19");
                    cityDb(mRecentList, areaInfo.getId() + "", "19", "",
                            "广东省", areaInfo.getCityName() + "", "");
                } else if (areaInfo.getId() == 272) {// 琼州黎族自治区
                    intent.putExtra("province", "海南省");
                    intent.putExtra("provinceId", "20");
                    cityDb(mRecentList, areaInfo.getId() + "", "20", "",
                            "海南省", areaInfo.getCityName() + "", "");
                } else {
                    intent.putExtra("province", "澳门特别行政区");
                    intent.putExtra("provinceId", "33");
                    cityDb(mRecentList, areaInfo.getId() + "", "33", "",
                            "澳门特别行政区", areaInfo.getCityName() + "", "");
                }
                intent.putExtra("cityId", areaInfo.getId() + "");
                intent.putExtra("district", "");
                intent.putExtra("chooseDistrict", areaInfo.getCityName());
                intent.putExtra("city", areaInfo.getCityName() + "");
                intent.putExtra("districtId", "0");
                setResult(RESULT_OK, intent);
                finish();
            } else {
                transaction.hide(mTwoFragment);
                selectInfo.setCityId(areaInfo.getCityId());
                selectInfo.setCityName(areaInfo.getCityName());
                setSltTab(2, String.valueOf(areaInfo.getId()), areaInfo.getCityName(), "", true);
                districtBtn.setChecked(true);
                cityBtn.setText(areaInfo.getCityName());
            }
        } else {// 点击第三次地区选择
            cityDb(mRecentList, String.valueOf(areaInfo.getCityId()),
                    String.valueOf(areaInfo.getProvinceId()),
                    String.valueOf(areaInfo.getId()),
                    areaInfo.getProvinceName(), areaInfo.getCityName(),
                    areaInfo.getDistrictName());// 保存到数据库

            intent = new Intent();
            intent.putExtra("chooseDistrict", areaInfo.getProvinceName()
                    + areaInfo.getCityName() + areaInfo.getDistrictName() + "");
            intent.putExtra("city", areaInfo.getCityName() + "");
            intent.putExtra("district", areaInfo.getDistrictName() + "");
            intent.putExtra("provinceId", areaInfo.getProvinceId() + "");
            intent.putExtra("province", areaInfo.getProvinceName() + "");
            intent.putExtra("cityId", areaInfo.getCityId() + "");
            intent.putExtra("districtId", areaInfo.getId() + "");
            setResult(RESULT_OK, intent);
            Constant.CITY_CHOOSE = 0;
            finish();
        }

    }

    /**
     * 定位自己的位置
     */
    public void getMyLocation() {
        mLocationClient = new LocationClient(AreaSelectActivity.this);
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
    private class MyLocationListenner implements BDLocationListener {

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
                CacheTools.setUserData("addrStr", location.getAddrStr());
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

    /**
     * 保存到数据库
     */
    public void cityDb(List<LatelyCity> list, String cityid, String provinceId,
                       String districtId, String provinceName, String cityName, String district) {
        if (!isHaveArea(list, provinceName, cityName, district)) {
            if (list.size() >= 3)
                dbCity.deltelArea(list.get(2).getProvince(), list.get(2).getCity(), list.get(2).getDistrict());
            dbCity.addCtiy(cityid, provinceId, districtId, provinceName, cityName, district);
        }
    }

    private boolean isHaveArea(List<LatelyCity> list, String provinceName, String cityName, String district) {
        for (int i = 0; i < list.size(); i++) {
            LatelyCity info = list.get(i);
            if (info.getDistrict().equals(district) && info.getCity().equals(cityName) && info.getProvince().equals(provinceName))
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        Constant.CITY_CHOOSE = 0;
        selectInfo = new AreaInfo();
        super.onDestroy();
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
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("yang", msg.obj.toString());

                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            provinceId = jo.getInt("provinceId");
                            cityId = jo.getInt("cityId");
                            districtId = jo.getInt("districtId");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
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
