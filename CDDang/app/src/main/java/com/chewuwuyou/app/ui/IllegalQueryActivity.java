package com.chewuwuyou.app.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cheshouye.api.client.WeizhangClient;
import com.cheshouye.api.client.json.CityInfoJson;
import com.cheshouye.api.client.json.InputConfigJson;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.IllegalScore;
import com.chewuwuyou.app.bean.ViolationDate;
import com.chewuwuyou.app.bean.WeizhangChildItem;
import com.chewuwuyou.app.bean.WeizhangGroupItem;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.Once;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.VehicleTypeUtil;
import com.chewuwuyou.app.widget.RadioGroup;
import com.chewuwuyou.app.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:违章查询
 * @author:yuyong
 * @date:2015-12-30下午5:20:41
 * @version:1.2.1
 */
public class IllegalQueryActivity extends CDDBaseActivity implements
        OnClickListener {
    public static boolean ISFIRST_OPEN = true;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private LinearLayout mChooseVehicleTypeLL;// 选择车辆类型
    private TextView mVehicleTypeTV;// 车辆类型
    private TextView mVehicleCodeTV;// 车辆代码：川
    private EditText mVehiclePlateNumET;// 车牌号
    private LinearLayout mChooseCityLL;// 选择城市
    private TextView mQueryCityTV;// 选择查询城市
    private LinearLayout mVehicleFrameNumLL;// 车架号布局
    private LinearLayout mVehicleEngineNumLL;// 发动机号布局
    private EditText mVehicleFrameNumET;// 车架号
    private EditText mVehicleEngineNumET;// 发动机号
    private ImageView mVehicleFrameHelpIV;// 车架号的帮助
    private ImageView mVehicleEngineHelpIV;// 发动机号的帮助
    private Button mQueryBtn;// 查询Btn

    private int mLen_chejia;// 判断查询是否需要车架号 0：不需要 1：需要
    private int mLen_engine;// 判断查询是否需要发动机号0:不需要

    private String mVehicleType;// 车辆类型:(小型汽车：02)
    private String mVehiclePlateNum;// 车牌号吗
    private String mQueryCity;// 查询城市
    private String mFrameNum;// 车架号
    private String mEngineNum;// 发动机号

    private String mCityId;// 城市Id

    private String mQueryIllegalUrl;// 查询违章地址
    private WeizhangGroupItem mWeizhangGroupItem;// 违章详情

    private ViolationDate mViolationDate;// 江苏的
    private List<IllegalScore> mIllegalScores;// 查询扣分情况

    private String mVehicleCode = "川";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.illegal_query_ac);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mChooseVehicleTypeLL = (LinearLayout) findViewById(R.id.choose_vehicle_type_ll);
        mVehicleTypeTV = (TextView) findViewById(R.id.vehicle_type_tv);
        mVehicleCodeTV = (TextView) findViewById(R.id.car_plate_districe_coding_tv);
        mVehiclePlateNumET = (EditText) findViewById(R.id.vehicle_plate_num_et);
        mChooseCityLL = (LinearLayout) findViewById(R.id.choose_city_ll);
        mQueryCityTV = (TextView) findViewById(R.id.choose_city_tv);
        mVehicleFrameNumET = (EditText) findViewById(R.id.vehicle_frame_num_et);
        mVehicleEngineNumET = (EditText) findViewById(R.id.vehicle_engine_num_et);
        mVehicleFrameHelpIV = (ImageView) findViewById(R.id.vehicle_frame_info_iv);
        mVehicleEngineHelpIV = (ImageView) findViewById(R.id.vehicle_engine_info_iv);
        mQueryBtn = (Button) findViewById(R.id.illegal_query_btn);
        mVehicleFrameNumLL = (LinearLayout) findViewById(R.id.vehicle_frame_num_ll);
        mVehicleEngineNumLL = (LinearLayout) findViewById(R.id.vhicle_engine_num_ll);
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mTitleTV.setText("违章查询");

    }

    @Override
    protected void initEvent() {
        // TODO Auto-generated method stub
        mBackIBtn.setOnClickListener(this);
        mChooseVehicleTypeLL.setOnClickListener(chooseVehicleTypeListener);
        mVehicleCodeTV.setOnClickListener(this);
        mVehicleFrameHelpIV.setOnClickListener(this);
        mVehicleEngineHelpIV.setOnClickListener(this);
        mChooseCityLL.setOnClickListener(this);
        mQueryBtn.setOnClickListener(this);
        mVehiclePlateNumET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if (mVehicleCode.equals("苏") && arg0.length() > 0
                        && arg0.toString().substring(0, 1).equals("E")) {
                    mVehicleFrameNumLL.setVisibility(View.VISIBLE);
                    mVehicleEngineNumLL.setVisibility(View.GONE);
                    mVehicleFrameNumET.setHint("请输入车架号后7位");
                    mVehicleFrameNumET
                            .setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                                    7)});
                } else if (mVehicleCode.equals("苏") && arg0.length() > 0
                        && !arg0.toString().substring(0, 1).equals("E")) {
                    mVehicleEngineNumLL.setVisibility(View.VISIBLE);
                    mVehicleFrameNumLL.setVisibility(View.GONE);
                    mVehicleEngineNumET.setHint("请输入发动机号后6位");
                    mVehicleEngineNumET
                            .setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                                    6)});
                }
            }
        });

        showDeclair();

    }

    private void showDeclair() {


        if (ISFIRST_OPEN) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(IllegalQueryActivity.this);
            mBuilder.setTitle("提示");
            mBuilder.setMessage(R.string.wzsm);
            mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            mBuilder.show();
            ISFIRST_OPEN = !ISFIRST_OPEN;
        }


    }

    /**
     * 选择车辆类型进行监听
     */
    OnClickListener chooseVehicleTypeListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            StatService.onEvent(IllegalQueryActivity.this,
                    "clickChoiceVehicleType", "点击选择车类型");
            final Dialog dialog = new Dialog(IllegalQueryActivity.this,
                    R.style.myDialogTheme);
            LayoutInflater inflater = LayoutInflater
                    .from(IllegalQueryActivity.this);
            final LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.choose_vehicle_type_ac, null);
            dialog.setContentView(layout);
            dialog.show();
            RadioGroup mGroup = (RadioGroup) layout
                    .findViewById(R.id.vehicle_type_group);
            mGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    MyLog.i("YUY",
                            String.valueOf(checkedId) + "     "
                                    + group.getCheckedRadioButtonId());
                    mVehicleTypeTV.setText(((RadioButton) layout
                            .findViewById(checkedId)).getText().toString());
                }
            });
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.car_plate_districe_coding_tv:
                districeCodingDialog(IllegalQueryActivity.this, mVehicleCodeTV);
                break;
            case R.id.vehicle_engine_info_iv:
                DialogUtil.showHelpPageDialog(IllegalQueryActivity.this);
                break;
            case R.id.vehicle_frame_info_iv:
                DialogUtil.showHelpPageDialog(IllegalQueryActivity.this);
                break;
            case R.id.choose_city_ll:
                Intent intent = new Intent(IllegalQueryActivity.this,
                        IllegalQueryChooseProvinceActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.illegal_query_btn:
                if (TextUtils.isEmpty(mVehicleTypeTV.getText().toString())) {
                    ToastUtil.toastShow(this, "请选择车辆类型");
                } else if (TextUtils.isEmpty(mVehiclePlateNumET.getText().toString())) {
                    ToastUtil.toastShow(this, "请输入车牌号");
                } else if (TextUtils.isEmpty(mVehicleFrameNumET.getText().toString())) {
                    ToastUtil.toastShow(this, "请输入车架号");
                } else if (TextUtils.isEmpty(mVehicleEngineNumET.getText().toString())) {
                    ToastUtil.toastShow(this, "请输入发动机号");
                } else {
                    queryIllegal();
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        switch (requestCode) {
            case 0:
                Bundle bundle = data.getExtras();
                String ShortName = bundle.getString("short_name");
                mVehicleCodeTV.setText(ShortName);
                break;
            case 1:
                Bundle bundle1 = data.getExtras();
                // String cityName = bundle1.getString("city_name");
                mCityId = bundle1.getString("city_id");
                // query_city.setText(cityName);
                // query_city.setTag(cityId);
                // InputConfigJson inputConfig =
                // WeizhangClient.getInputConfig(Integer.parseInt(cityId));
                // System.out.println(inputConfig.toJson());
                setQueryItem(Integer.parseInt(mCityId));

                break;
        }
    }

    // 根据城市的配置设置查询项目
    private void setQueryItem(int cityId) {

        InputConfigJson cityConfig = WeizhangClient.getInputConfig(cityId);

        // 没有初始化完成的时候;
        if (cityConfig != null) {
            CityInfoJson city = WeizhangClient.getCity(cityId);

            mQueryCityTV.setText(city.getCity_name());
            mQueryCityTV.setTag(cityId);

            mLen_chejia = cityConfig.getClassno();
            mLen_engine = cityConfig.getEngineno();

            // 车架号
            if (mLen_chejia == 0) {
                mVehicleFrameNumLL.setVisibility(View.GONE);
            } else {
                mVehicleFrameNumLL.setVisibility(View.VISIBLE);
                setMaxlength(mVehicleFrameNumET, mLen_chejia);
                if (mLen_chejia == -1) {
                    mVehicleFrameNumET.setHint("请输入完整车架号");
                } else if (mLen_chejia > 0) {
                    mVehicleFrameNumET.setHint("请输入车架号后" + mLen_chejia + "位");
                }
            }

            // 发动机号
            if (mLen_engine == 0) {
                mVehicleEngineNumLL.setVisibility(View.GONE);
            } else {
                mVehicleEngineNumLL.setVisibility(View.VISIBLE);
                setMaxlength(mVehicleEngineNumET, mLen_engine);
                if (mLen_engine == -1) {
                    mVehicleEngineNumET.setHint("请输入完整车发动机号");
                } else if (mLen_engine > 0) {
                    mVehicleEngineNumET.setHint("请输入发动机后" + mLen_engine + "位");
                }
            }
        }
    }

    // 设置/取消最大长度限制
    private void setMaxlength(EditText et, int maxLength) {
        if (maxLength > 0) {
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    maxLength)});
        } else { // 不限制
            et.setFilters(new InputFilter[]{});
        }
    }

    /**
     * 查询违章
     *
     * @param
     */
    public void queryIllegal() {

        final ProgressDialog mProgressDialog = ProgressDialog.show(this, "提示", "正在查询中", true, true);

        mVehicleType = VehicleTypeUtil.vehicleTypeToCode(mVehicleTypeTV
                .getText().toString());
        String province_short_name = mVehicleCodeTV.getText().toString();
        mVehiclePlateNum = mVehicleCodeTV.getText().toString()
                + mVehiclePlateNumET.getText().toString();
        mQueryCity = mQueryCityTV.getText().toString();
        mFrameNum = mVehicleFrameNumET.getText().toString();
        mEngineNum = mVehicleEngineNumET.getText().toString();

        String carInfoDetails = "{hphm=" + mVehiclePlateNum + "&classno="
                + mFrameNum + "&engineno=" + mEngineNum + "&city_id=" + mCityId
                + "&car_type=" + mVehicleType + "}";

        MyLog.i("YUY", "carInfoDetails  = " + carInfoDetails);
        String carInfo = null;
        try {
            carInfo = URLEncoder.encode(carInfoDetails, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long time = System.currentTimeMillis();
        String sign = "888" + carInfoDetails + String.valueOf(time)
                + "98d5ad2b0bd453805a2fdd445e9e8085";

        if (province_short_name.equals("苏")) {// 使用江苏的查询方式 现同意修改为车首页接口查询
            queryJiangSuIllegal();
        } else {
            mQueryIllegalUrl = NetworkUtil.CSY_QUERY_ILLEGAL_URL + "timestamp="
                    + String.valueOf(time) + "&car_info=" + carInfo + "&sign="
                    + MD5Util.getMD5(sign) + "&app_id=888";
            MyLog.i("YUY", "请求全地址 = " + mQueryIllegalUrl);
            NetworkUtil.postNoHeader(mQueryIllegalUrl, null,
                    new AjaxCallBack<String>() {
                        @Override
                        public void onSuccess(String t) {
                            mProgressDialog.cancel();
                            // TODO Auto-generated method stub
                            super.onSuccess(t);
                            MyLog.i("YUY", "违章详情   = " + t);
                            mWeizhangGroupItem = WeizhangGroupItem.parse(t
                                    .toString());
                            if (mWeizhangGroupItem == null
                                    || mWeizhangGroupItem.getHistorys().size() == 0) {
                                ToastUtil.toastShow(IllegalQueryActivity.this,
                                        "暂未查到违章信息");
                                return;
                            }
                            Intent intent = new Intent(
                                    IllegalQueryActivity.this,
                                    IllegalDetailsActivity.class);// 传递查出的参数
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("weizhangGroupItem",
                                    mWeizhangGroupItem);
                            intent.putExtras(bundle);
                            intent.putExtra("queryCity", mQueryCity);
                            startActivity(intent);

                        }
                        @Override
                        public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
                            // TODO Auto-generated method stub
                            mProgressDialog.cancel();
                            super.onFailure(t, errorNo, strMsg);
                            MyLog.i("YUY", "查询失败" + strMsg);
                        }
                    });

        }

    }

    // 查询江苏的违章 现统一改为车首页接口查询,留着备用
    @SuppressWarnings("static-access")
    public void queryJiangSuIllegal() {
        final AjaxParams params = new AjaxParams();
        params.put("plateNumber", mVehiclePlateNum);
        if (mVehiclePlateNum.contains("苏E")) {
            params.put("engineNumber", mFrameNum);
        } else {
            params.put("engineNumber", mEngineNum);
        }
        // TODO Auto-generated method stub
        new NetworkUtil().postMulti(NetworkUtil.VIOLATION_QUERY_URL, params,
                new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                        MyLog.i("YUY", "车辆违章信息 = " + t);

                        try {
                            JSONObject jo = new JSONObject(t.toString());
                            if (jo.getInt("result") == 1) {
                                mViolationDate = ViolationDate.parse(jo
                                        .getString("data"));
                                if (Integer.parseInt(mViolationDate.getCount()) == 0) {
                                    ToastUtil.toastShow(
                                            IllegalQueryActivity.this,
                                            "暂未查到您的违章信息");
                                    return;
                                }
                                if (mViolationDate != null
                                        && mViolationDate.getItem().size() > 0) {
                                    getScoreByIllegalDe();
                                }

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 根据违章服务的内容查询扣分
     *
     * @param :违章详情
     * @param :违章金额
     */
    @SuppressWarnings("static-access")
    private void getScoreByIllegalDe() {
        mIllegalScores = new ArrayList<IllegalScore>();
        AjaxParams params = new AjaxParams();
        String illegalDetails = null;
        for (int j = 0; j < mViolationDate.getItem().size(); j++) {
            illegalDetails += mViolationDate.getItem().get(j).getWfnr() + "-";
        }
        params.put("ids",
                illegalDetails.substring(0, illegalDetails.length() - 1));
        new NetworkUtil().postMulti(NetworkUtil.QUERY_SCORE, params,
                new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object t) {
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                        try {
                            JSONObject jo = new JSONObject(t.toString());
                            if (jo.getInt("result") == 1) {
                                mIllegalScores = IllegalScore.parseList(jo
                                        .getString("data"));
                                mWeizhangGroupItem = new WeizhangGroupItem();
                                mWeizhangGroupItem.setStatus(0);
                                mWeizhangGroupItem.setCount(Integer
                                        .parseInt(mViolationDate.getCount()));
                                int total_money = 0, total_score = 0;
                                List<WeizhangChildItem> weizhangChildItems = new ArrayList<WeizhangChildItem>();
                                WeizhangChildItem weizhangChildItem;
                                for (int i = 0; i < mViolationDate.getItem()
                                        .size(); i++) {
                                    total_money += Integer
                                            .parseInt(mViolationDate.getItem()
                                                    .get(i).getWfje());
                                    weizhangChildItem = new WeizhangChildItem();
                                    weizhangChildItem.setInfo(mViolationDate
                                            .getItem().get(i).getWfnr());

                                    weizhangChildItem.setMoney(Integer
                                            .parseInt(mViolationDate.getItem()
                                                    .get(i).getWfje()));
                                    weizhangChildItem
                                            .setOccur_date(mViolationDate
                                                    .getItem().get(i).getWfsj());
                                    weizhangChildItem
                                            .setOccur_area(mViolationDate
                                                    .getItem().get(i).getWfdz());
                                    weizhangChildItem.setStatus("N");

                                    for (int j = 0; j < mIllegalScores.size(); j++) {
                                        if (mViolationDate
                                                .getItem()
                                                .get(i)
                                                .getWfnr()
                                                .contains(
                                                        mIllegalScores
                                                                .get(j)
                                                                .getServiceName())) {
                                            total_score += Integer
                                                    .parseInt(mIllegalScores
                                                            .get(j)
                                                            .getScoreReduce());
                                            weizhangChildItem.setFen(Integer
                                                    .valueOf(mIllegalScores
                                                            .get(j)
                                                            .getScoreReduce()));
                                        }
                                        weizhangChildItems
                                                .add(weizhangChildItem);

                                    }

                                }
                                mWeizhangGroupItem
                                        .setHistorys(weizhangChildItems);
                                mWeizhangGroupItem.setTotal_money(total_money);
                                mWeizhangGroupItem.setTotal_score(total_score);

                            }
                            Intent intent = new Intent(
                                    IllegalQueryActivity.this,
                                    IllegalDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("weizhangGroupItem",
                                    mWeizhangGroupItem);
                            intent.putExtras(bundle);
                            intent.putExtra("queryCity", "江苏全省");
                            startActivity(intent);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 选择车辆地区编码
     *
     * @param ac
     * @param TV
     */
    @SuppressWarnings("deprecation")
    public void districeCodingDialog(final Activity ac, final TextView TV) {
        final Dialog dialog = new Dialog(ac, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(ac);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.districe_coding_dialog, null);
        layout.setAlpha(100);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        WindowManager windowManager = ac.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        final String[] array = ac.getResources().getStringArray(
                R.array.palte_location_code);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("textItem", array[i]);// 按序号添加ItemText
            items.add(item);
        }
        GridView mDistriceCodingGrid = (GridView) layout
                .findViewById(R.id.districe_coding_grid);
        // 实例化一个适配器
        SimpleAdapter adapter = new SimpleAdapter(ac, items,
                R.layout.plate_diqu_item, new String[]{"textItem"},
                new int[]{R.id.item_text});
        mDistriceCodingGrid.setAdapter(adapter);
        mDistriceCodingGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                TV.setText(array[arg2]);
                mVehicleCode = array[arg2];
                if (array[arg2].equals("苏")) {
                    mChooseCityLL.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });
        Button cancelBtn = (Button) layout.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);

    }
}
