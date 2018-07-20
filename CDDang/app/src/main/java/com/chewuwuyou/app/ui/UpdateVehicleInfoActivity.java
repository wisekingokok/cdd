package com.chewuwuyou.app.ui;

import java.io.IOException;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cheshouye.api.client.WeizhangClient;
import com.cheshouye.api.client.json.InputConfigJson;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarCityItem;
import com.chewuwuyou.app.bean.CarProvinceItem;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Vehicle;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.utils.VehicleTypeUtil;
import com.chewuwuyou.app.widget.RadioGroup;
import com.chewuwuyou.app.widget.RadioGroup.OnCheckedChangeListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @version 1.1.0
 * @describe:修改或删除车辆信息
 * @author:yuyong
 * @created:2015-1-5下午3:27:54
 */
public class UpdateVehicleInfoActivity extends BaseActivity {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackBtn;

    // 选择品牌类型
    @ViewInject(id = R.id.car_plate_districe_coding_tv, click = "onAction")
    private TextView mChooseVehiclePlateDistriceCodingTV;
    // 选择保险到期时间
    @ViewInject(id = R.id.vehicle_frame_info_iv, click = "onAction")
    private ImageView mChooseVehicleFrameInfoIV;
    // 车架号
    @ViewInject(id = R.id.vehicle_frame_num_et)
    private EditText mVehicleFrameNumET;
    // 发动机号码
    @ViewInject(id = R.id.remark_name_et)
    private EditText mRemarkNameET;
    // 车辆图片
    @ViewInject(id = R.id.vehicle_brand_iv)
    private ImageView mVehicleBrandIV;
    // 编辑行驶证正页图片
    @ViewInject(id = R.id.vehicle_engine_info_iv, click = "onAction")
    private ImageView mVehicleEngineInfoIV;
    // 车辆信息实体
    private Vehicle mVehicle;
    // 选择车辆类型
    @ViewInject(id = R.id.choose_vehicle_type_ll, click = "onAction")
    private LinearLayout mChooseVehicleTypeLL;
    // 选择品牌类型
    @ViewInject(id = R.id.choose_vehicle_brand_ll, click = "onAction")
    private LinearLayout mChooseVehicleBrandLL;
    // 车牌号码
    @ViewInject(id = R.id.vehicle_plate_num_et)
    private EditText mVehiclePlateNoET;
    // 车辆类型
    @ViewInject(id = R.id.vehicle_type_tv)
    private TextView mVehicleTypeTV;
    // 品牌类型
    @ViewInject(id = R.id.vehicle_model_tv)
    private TextView mVehicleModelTV;
    // 发动机号码
    @ViewInject(id = R.id.vehicle_engine_num_et)
    private EditText mVehicleEngineNumET;
    // 保存修改
    @ViewInject(id = R.id.update_vehicle_btn, click = "onAction")
    private Button mUpdateBtn;
    // 年检时间
    @ViewInject(id = R.id.vehicle_nianjian_notify_date_tv, click = "onAction")
    private TextView mNianjianNotifyDateTV;
    // 保险时间
    @ViewInject(id = R.id.vehicle_baoxian_notify_date_tv, click = "onAction")
    private TextView mBaoxianNotifyDateTV;
    @ViewInject(id = R.id.use_xieyi_ll, click = "onAction")
    private LinearLayout mUserXyLL;
    private int mLen_chejia;// 判断查询是否需要车架号 0：不需要 1：需要
    private int mLen_engine;// 判断查询是否需要发动机号0:不需要
    private List<CarProvinceItem> mData;

    private LinearLayout mVehicleFrameNumLL;// 车架号布局
    private LinearLayout mVehicleEngineNumLL;// 发动机号布局
    private RelativeLayout mTitleHeight;// 标题布局高度
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    Log.i("YUY", "修改Car信息成功");
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.VEHICLE_SER, mVehicle);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case Constant.NET_DATA_FAIL:
                    ToastUtil.toastShow(UpdateVehicleInfoActivity.this,
                            ((DataError) msg.obj).getErrorMessage());
                    break;
                default:
                    setResult(RESULT_CANCELED, null);
                    finish();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_vehicle_ac);
        Intent intent = getIntent();
        if (intent != null) {
            mVehicle = (Vehicle) intent
                    .getSerializableExtra(Constant.VEHICLE_SER);
        }
        initView();
    }

    public void initView() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mData = CarProvinceItem
                .parseList(getFromAssets("get_all_configs.json"));
        ((TextView) findViewById(R.id.sub_header_bar_tv))
                .setText(R.string.vehicle_manager_title);
        mVehicleFrameNumLL = (LinearLayout) findViewById(R.id.vehicle_frame_num_ll);
        mVehicleEngineNumLL = (LinearLayout) findViewById(R.id.vehicle_engine_number_ll);

//		mVehiclePlateNoET.setClickable(false);
//		mVehiclePlateNoET.setFocusable(false);
        String province_short_name = String.valueOf(mVehicle.getPlateNumber()
                .charAt(0));
        String car_head = mVehicle.getPlateNumber().substring(0, 1);
        setQueryItem(getCityId(province_short_name, car_head));
        if (mVehicle != null) {
            // 编辑vehicle info
            String imgUri = Tools.getVehicleImage(
                    UpdateVehicleInfoActivity.this, mVehicle.getBrand(),
                    mVehicle.getModelNumber());
            ImageLoader.getInstance().displayImage(
                    imgUri,
                    mVehicleBrandIV,
                    new DisplayImageOptions.Builder()
                            .showImageForEmptyUri(R.drawable.a1_audi)
                            .showImageOnFail(R.drawable.a1_audi)
                            .cacheInMemory(true).cacheOnDisc(true).build());
            mChooseVehiclePlateDistriceCodingTV.setText(mVehicle
                    .getPlateNumber().substring(0, 1));
            mVehiclePlateNoET.setText(mVehicle.getPlateNumber().substring(1));
            VehicleTypeUtil.codeToType(mVehicle.getHpzl(), mVehicleTypeTV);
            if (!TextUtils.isEmpty(mVehicle.getBrand())) {
                mVehicleModelTV.setText(new StringBuffer()
                        .append(mVehicle.getBrand()).append("/")
                        .append(mVehicle.getModelNumber()));
            }
            mVehicleEngineNumET.setText(mVehicle.getEngineNumber());
            mVehicleFrameNumET.setText(mVehicle.getFrameNumber());
            mRemarkNameET.setText(mVehicle.getNoteName());
            mNianjianNotifyDateTV.setText(mVehicle.getAnnualSurveyTime());
            mBaoxianNotifyDateTV.setText(mVehicle.getInsuranceTime());

        }
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.use_xieyi_ll:
                Intent yssmIntent = new Intent(UpdateVehicleInfoActivity.this,
                        AgreementActivity.class);
                yssmIntent.putExtra("type", 8);
                startActivity(yssmIntent);
                break;
            case R.id.vehicle_nianjian_notify_date_tv:
                DialogUtil.showTimeDialog(UpdateVehicleInfoActivity.this,
                        mNianjianNotifyDateTV);
                break;
            case R.id.vehicle_baoxian_notify_date_tv:
                DialogUtil.showTimeDialog(UpdateVehicleInfoActivity.this,
                        mBaoxianNotifyDateTV);
                break;
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;

            case R.id.choose_vehicle_type_ll:
                StatService.onEvent(UpdateVehicleInfoActivity.this,
                        "clickChoiceVehicleType", "点击选择车类型");
                final Dialog dialog = new Dialog(UpdateVehicleInfoActivity.this,
                        R.style.myDialogTheme);
                LayoutInflater inflater = LayoutInflater
                        .from(UpdateVehicleInfoActivity.this);
                final LinearLayout layout = (LinearLayout) inflater.inflate(
                        R.layout.choose_vehicle_type_ac, null);
                layout.setAlpha(100);
                dialog.setContentView(layout);
                dialog.show();
                RadioGroup mGroup = (RadioGroup) layout
                        .findViewById(R.id.vehicle_type_group);
                mGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        dialog.dismiss();
                        MyLog.i("YUY",
                                String.valueOf(checkedId) + "     "
                                        + group.getCheckedRadioButtonId());
                        mVehicleTypeTV.setText(((RadioButton) layout
                                .findViewById(checkedId)).getText().toString());
                    }
                });
                break;
            case R.id.choose_vehicle_brand_ll:
                StatService.onEvent(UpdateVehicleInfoActivity.this,
                        "clickChooseVhicelBrand", "点击选择车品牌");
                Intent intent = new Intent(UpdateVehicleInfoActivity.this,
                        ChooseCarBrandActivity.class);
                startActivityForResult(intent, 20);
                break;

            case R.id.update_vehicle_btn:
                String id = mVehicle.getId();
                String vehicleModle = mVehicleModelTV.getText().toString().trim();
                String brand = null;
                String modelNumber = null;
                if (vehicleModle.contains("/")) {
                    brand = vehicleModle.split("/")[0];
                    modelNumber = vehicleModle.split("/")[1];
                }
                String hpzl = mVehicleTypeTV.getText().toString().trim();
                String plateNumber = mChooseVehiclePlateDistriceCodingTV.getText()
                        .toString() + mVehiclePlateNoET.getText().toString().trim();
                String frameNumber = mVehicleFrameNumET.getText().toString().trim();
                String engineNumber = mVehicleEngineNumET.getText().toString().trim();
                String annualSurveyTime = mNianjianNotifyDateTV.getText().toString().trim();
                String insureTime = mBaoxianNotifyDateTV.getText().toString().trim();
                MyLog.i("YUY", "保险时间 ============= " + insureTime);
                if (TextUtils.isEmpty(hpzl)) {
                    ToastUtil.showToast(UpdateVehicleInfoActivity.this,
                            R.string.please_choose_vehicle_type);
                } else if (!plateNumber.matches(RegularUtil.verifyPlateNumber)) {
                    ToastUtil
                            .toastShow(UpdateVehicleInfoActivity.this, "请输入正确的车牌号");
                } else if (TextUtils.isEmpty(brand)) {
                    ToastUtil.showToast(UpdateVehicleInfoActivity.this,
                            R.string.please_choose_vehicle_brand);
                } else if (mLen_chejia != 0 && TextUtils.isEmpty(frameNumber)) {
                    ToastUtil.showToast(UpdateVehicleInfoActivity.this,
                            R.string.please_input_frame_num);
                } else if (mLen_engine != 0 && TextUtils.isEmpty(engineNumber)) {
                    ToastUtil.showToast(UpdateVehicleInfoActivity.this,
                            R.string.please_input_engine_num);
                } else {
                    mVehicle.setBrand(brand);
                    mVehicle.setHpzl(VehicleTypeUtil.vehicleTypeToCode(hpzl));
                    mVehicle.setModelNumber(modelNumber);
                    mVehicle.setPlateNumber(plateNumber);
                    mVehicle.setFrameNumber(frameNumber);
                    mVehicle.setEngineNumber(engineNumber);
                    mVehicle.setNoteName(mRemarkNameET.getText().toString());
                    AjaxParams params = new AjaxParams();
                    params.put("id", id);
                    params.put("brand", brand);
                    params.put("hpzl", VehicleTypeUtil.vehicleTypeToCode(hpzl));
                    params.put("modelNumber", modelNumber);
                    params.put("plateNumber", plateNumber);
                    if (mLen_chejia != 0) {
                        params.put("frameNumber", frameNumber);
                    }
                    if (mLen_engine != 0) {
                        params.put("engineNumber", engineNumber);
                    }
                    params.put("insuranceTime", insureTime);
                    params.put("annualSurveyTime", annualSurveyTime);
                    params.put("noteName", mRemarkNameET.getText().toString());
                    requestNet(mHandler, params, NetworkUtil.UPDATE_VEHICLE_URL,
                            false, 0);
                }
                break;
            case R.id.delete_vehicle_btn:
                StatService.onEvent(UpdateVehicleInfoActivity.this,
                        "clickDeleteVehicleBtn", "点击车辆管理删除按钮");
                try {
                    Vehicle vehicle = (Vehicle) CacheTools.getObject(CacheTools
                            .getUserData("telephone"));
                    if (vehicle != null && vehicle.getId().equals(mVehicle.getId())) {
                        CacheTools.clearObject(CacheTools.getUserData("telephone"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                new AlertDialog.Builder(UpdateVehicleInfoActivity.this)
                        .setTitle("删除车辆")
                        .setMessage("确定删除" + mVehicle.getPlateNumber() + "车辆吗？")
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int arg1) {
                                        dialog.dismiss();
                                    }
                                })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            final DialogInterface dialog, int arg1) {
                                        AjaxParams params2 = new AjaxParams();
                                        params2.put("id", mVehicle.getId());
                                        requestNet(
                                                new Handler() {
                                                    @Override
                                                    public void handleMessage(
                                                            Message msg) {
                                                        // method stub
                                                        super.handleMessage(msg);
                                                        switch (msg.what) {
                                                            case Constant.NET_DATA_SUCCESS:
                                                                dialog.dismiss();
                                                                finishActivity();
                                                                break;
                                                            case Constant.NET_DATA_FAIL:
                                                                dialog.dismiss();
                                                                ToastUtil
                                                                        .toastShow(
                                                                                UpdateVehicleInfoActivity.this,
                                                                                ((DataError) msg.obj)
                                                                                        .getErrorMessage());
                                                                break;
                                                            default:
                                                                dialog.dismiss();
                                                                break;
                                                        }
                                                    }
                                                }, params2,
                                                NetworkUtil.DELETE_VEHICLE_URL,
                                                false, 0);
                                    }
                                }).show();

                break;
            case R.id.car_plate_districe_coding_tv:
                DialogUtil.districeCodingDialog(UpdateVehicleInfoActivity.this,
                        mChooseVehiclePlateDistriceCodingTV);
                break;

            case R.id.vehicle_engine_info_iv:
                DialogUtil.showHelpPageDialog(UpdateVehicleInfoActivity.this);
                break;
            case R.id.vehicle_frame_info_iv:
                DialogUtil.showHelpPageDialog(UpdateVehicleInfoActivity.this);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            mVehicleModelTV.setText(new StringBuffer()
                    .append(data.getStringExtra("brandName")).append("/")
                    .append(data.getStringExtra("name")).toString());
            String imgUri = Tools.getVehicleImage(
                    UpdateVehicleInfoActivity.this,
                    data.getStringExtra("brand"), data.getStringExtra("model"));
            setImage(mVehicleBrandIV, imgUri);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(UpdateVehicleInfoActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(UpdateVehicleInfoActivity.this);
    }

    // 根据城市的配置设置查询项目
    private void setQueryItem(int cityId) {

        InputConfigJson cityConfig = WeizhangClient.getInputConfig(cityId);

        // 没有初始化完成的时候;
        if (cityConfig != null) {
            // CityInfoJson city = WeizhangClient.getCity(cityId);

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

    private int getCityId(String province_short_name, String car_head) {
        for (CarProvinceItem province : mData) {
            if (province.getProvince_short_name().equals(province_short_name)) {
                for (CarCityItem city : province.getCitys()) {
                    if (city.getCar_head().equals(car_head)) {
                        return city.getCity_id();

                    }
                }
                return province.getCitys().get(0).getCity_id();

            }
        }
        return 0;
    }
}
