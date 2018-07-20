package com.chewuwuyou.app.ui;

import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarCityItem;
import com.chewuwuyou.app.bean.CarProvinceItem;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.tools.DateCompare;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.VehicleTypeUtil;
import com.chewuwuyou.app.widget.RadioGroup;
import com.chewuwuyou.app.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @describe:添加车辆
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-11-3下午7:43:19
 */
@SuppressLint({ "HandlerLeak", "InflateParams" })
public class AddVehicleActivity extends BaseActivity {
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	@ViewInject(id = R.id.sub_header_bar_right_tv, click = "onAction")
	private TextView mHeaderRightTV;// 添加车辆
	@ViewInject(id = R.id.vehicle_plate_num_et)
	private EditText mVehiclePlateNumET;// 车牌号

	@ViewInject(id = R.id.vehicle_frame_number_ll)
	private LinearLayout mFrameNumLL;// 发动机号

	@ViewInject(id = R.id.vehicle_engine_number_ll)
	private LinearLayout mEngineNumLL;// 发动机号

	@ViewInject(id = R.id.vehicle_engine_num_et)
	private EditText mEngineNumET;// 发动机号
	@ViewInject(id = R.id.vehicle_frame_num_et)
	private EditText mFrameNumET;// 车架号

	@ViewInject(id = R.id.vehicle_engine_info_iv, click = "onAction")
	private ImageView mEngineInfoIV;// 发动机号

	@ViewInject(id = R.id.vehicle_frame_info_iv, click = "onAction")
	private ImageView mFrameInfoIV;// 发动机号

	@ViewInject(id = R.id.vehicle_nianjian_notify_date_tv, click = "onAction")
	private TextView mNianjianNotifyDateTV;

	@ViewInject(id = R.id.vehicle_baoxian_notify_date_tv, click = "onAction")
	private TextView mBaoxianNotifyDateTV;

	@ViewInject(id = R.id.save_btn, click = "onAction")
	private Button mSaveBtn;// 保存信息

	@ViewInject(id = R.id.use_xieyi_ll, click = "onAction")
	private LinearLayout mUseXieyiLL;// 查看用户协议
	private TextView mVehicleType;// 车辆类型
	private LinearLayout mChoiceVehicleTypeLayout;// 选择车辆类型及品牌
	private String plateNum;
	private String engineNum;
	private String frameNum;
	private String vehicleType;
	private String insureTime;
	private String annualSurveyTime;
	// private ViolationDate mViolation;// 请求的违章信息
	// private Vehicle mVehicle;// 车辆对象
	@ViewInject(id = R.id.car_plate_districe_coding_tv, click = "onAction")
	private TextView mCarPlateDistriceCodingTV;
	public static final int RESULT_FROM_CHOOSE_CITY = 1;
	private List<CarProvinceItem> mData;
	private CarCityItem mProvAndCityConf;
	private String mVerifyFrameNum;
	private String mVerifyEngineNum;
	private RelativeLayout mTitleHeight;// 标题布局高度
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NET_DATA_SUCCESS:
				ToastUtil.toastShow(AddVehicleActivity.this, "添加车辆成功！");
				MyLog.i("YUY", "添加成功");
				// mVehicle = new Vehicle();
				// mVehicle.setPlateNumber(mCarPlateDistriceCodingTV.getText()
				// .toString() + plateNum.toUpperCase());
				// mVehicle.setEngineNumber(engineNum);
				// mVehicle.setFrameNumber(frameNum);
				// mVehicle.setHpzl(VehicleTypeUtil.vehicleTypeToCode(vehicleType));
				// MyLog.i("YUY", "==查询违章的车牌号和发动机号===" +
				// mVehicle.getPlateNumber() + "======" +
				// engineNum.substring(engineNum.length() - 6,
				// engineNum.length())
				// + "==" + getIntent().getExtras().getInt("tag"));
				// if (getIntent().getIntExtra("tag", 0) == 1) {
				// // 请求查询违章
				// AjaxParams params = new AjaxParams();
				// params.put("plateNumber", mVehicle.getPlateNumber());
				// params.put(
				// "engineNumber",
				// mVehicle.getEngineNumber().substring(
				// engineNum.length() - 6, engineNum.length()));
				// requestNet(new Handler() {
				// @Override
				// public void handleMessage(Message msg) {
				// switch (msg.what) {
				// case Constant.NET_DATA_SUCCESS:
				// mViolation = ViolationDate.parse(msg.obj
				// .toString());
				// // illQuery = "";
				// if (mViolation != null
				// && mViolation.getItem().size() > 0) {
				// Intent intent = new Intent(
				// AddVehicleActivity.this,
				// VehicleViolationActivity.class);
				// intent.putExtra("tag", 1);
				// Bundle bundle = new Bundle();
				// bundle.putSerializable(
				// Constant.VEHICLE_SER, mVehicle);
				// intent.putExtras(bundle);
				// startActivity(intent);
				//
				// }
				// break;
				// }
				// super.handleMessage(msg);
				// }
				// }, params, NetworkUtil.VIOLATION_QUERY_URL, false, 0);
				// }
				finishActivity();
				break;
			case Constant.NET_DATA_FAIL:
				ToastUtil.toastShow(AddVehicleActivity.this,
						((DataError) msg.obj).getErrorMessage());
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_vehicle_ac);
		initView();

	}

	private void initView() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mHeaderTV.setText(R.string.add_vehicle_title);
		mData = CarProvinceItem.parseList(getFromAssets("get_all_configs.json"));
		mVehiclePlateNumET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (!TextUtils.isEmpty(mVehiclePlateNumET.getText().toString())) {
					String province_short_name = mCarPlateDistriceCodingTV
							.getText().toString();
					String car_head = new StringBuilder(
							mCarPlateDistriceCodingTV.getText().toString())
							.append(mVehiclePlateNumET.getText().toString()
									.charAt(0)).toString();
					for (CarProvinceItem province : mData) {
						if (province.getProvince_short_name().equals(
								province_short_name)) {
							for (CarCityItem city : province.getCitys()) {
								if (city.getCar_head().equals(car_head)) {
									mProvAndCityConf = city;
									break;
								}
							}
							mProvAndCityConf = province.getCitys().get(0);
							break;
						}
					}
					if (mProvAndCityConf != null) {
						updateUI(mProvAndCityConf.getEngineno(),
								mProvAndCityConf.getClassno());
					} else {
						updateUI(0, 0);
					}
				} else {
					mProvAndCityConf = null;
					updateUI(0, 0);
				}
			}
		});
		mVehicleType = (TextView) findViewById(R.id.vehicle_type);
		mChoiceVehicleTypeLayout = (LinearLayout) findViewById(R.id.vehilce_type_layout);
		// mHeaderRightTV.setVisibility(View.VISIBLE);
		// mHeaderRightTV.setText(R.string.add);
		mChoiceVehicleTypeLayout.setOnClickListener(chooseVehicleTypeListener);// 选择车辆类型监听
		// mEngineInfoIV.setOnClickListener(null);// 显示详情
		// mFrameInfoIV.setOnClickListener(null);// 显示详情

	}

	/**
	 * 选择车辆类型进行监听
	 */
	OnClickListener chooseVehicleTypeListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			StatService.onEvent(AddVehicleActivity.this,
					"clickChoiceVehicleType", "点击选择车类型");
			final Dialog dialog = new Dialog(AddVehicleActivity.this,
					R.style.myDialogTheme);
			LayoutInflater inflater = LayoutInflater
					.from(AddVehicleActivity.this);
			final LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.choose_vehicle_type_ac, null);

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
					mVehicleType.setText(((RadioButton) layout
							.findViewById(checkedId)).getText().toString());
				}
			});
		}
	};

	/**
	 * 选择车辆类型进行监听
	 */
	OnClickListener chooseVehicleCityListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(AddVehicleActivity.this,
					CarPlateProvinceActivity.class);
			startActivityForResult(intent, RESULT_FROM_CHOOSE_CITY);
		};
	};

	/**
	 * 对添加车辆进行监听
	 */
	OnClickListener addVehicleClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			StatService.onEvent(AddVehicleActivity.this, "clickAddVehicle",
					"点击选择车辆保险起算时间");
			addVehicle();
		}
	};

	/**
	 * 添加车辆
	 */
	@SuppressLint("DefaultLocale")
	private void addVehicle() {

		plateNum = mCarPlateDistriceCodingTV.getText().toString()
				+ mVehiclePlateNumET.getText().toString().trim().toUpperCase();
		vehicleType = mVehicleType.getText().toString();
		engineNum = mEngineNumET.getText().toString().trim();
		frameNum = mFrameNumET.getText().toString().trim();
		insureTime = mBaoxianNotifyDateTV.getText().toString();
		annualSurveyTime = mNianjianNotifyDateTV.getText().toString();

		if (TextUtils.isEmpty(plateNum)) {
			ToastUtil.showToast(AddVehicleActivity.this,
					R.string.please_input_plate_num);
		} else if (!plateNum.matches(RegularUtil.verifyPlateNumber)) {
			ToastUtil.showToast(AddVehicleActivity.this,
					R.string.platenum_error);
		} else if (TextUtils.isEmpty(vehicleType)) {
			ToastUtil.showToast(AddVehicleActivity.this,
					R.string.please_choose_vehicle_type);
		} else if (!TextUtils.isEmpty(frameNum)
				&& !frameNum.matches(mVerifyFrameNum)) {
			Toast.makeText(AddVehicleActivity.this, "车架号不对", Toast.LENGTH_SHORT)
					.show();
		} else if (!TextUtils.isEmpty(engineNum)
				&& !engineNum.matches(mVerifyEngineNum)) {
			Toast.makeText(AddVehicleActivity.this, "发动机号不对",
					Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(insureTime)) {
			ToastUtil.toastShow(AddVehicleActivity.this, "请选择保险到期时间");
		} else if (TextUtils.isEmpty(annualSurveyTime)) {
			ToastUtil.toastShow(AddVehicleActivity.this, "请选择年检到期时间");
		} else if (DateCompare.compare_date(insureTime,
				DateCompare.getPhoneDate()) != 1) {
			ToastUtil.toastShow(AddVehicleActivity.this, "保险日期不能小于当前日期");
		} else if (DateCompare.compare_date(annualSurveyTime,DateCompare.getPhoneDate()) != 1) {
			ToastUtil.toastShow(AddVehicleActivity.this, "年检日期不能小于当前日期");
		} else {
			AjaxParams params = new AjaxParams();
			params.put("plateNumber", plateNum);
			params.put("engineNumber", engineNum);
			params.put("frameNumber", frameNum);
			params.put("hpzl", VehicleTypeUtil.vehicleTypeToCode(vehicleType));
			params.put("insuranceTime", insureTime);
			params.put("annualSurveyTime", annualSurveyTime);
			requestNet(mHandler, params, NetworkUtil.ADD_CAR_URL, false, 0);
			finish();
			ChooseVehicleActivity.mChooseVehicleActivity.finishActivity();
		}
	}

	public void onAction(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.save_btn:
			addVehicle();
			break;
		case R.id.use_xieyi_ll:
			Intent yssmIntent = new Intent(AddVehicleActivity.this,
					AgreementActivity.class);
			yssmIntent.putExtra("type", 8);
			startActivity(yssmIntent);
			break;
		case R.id.car_plate_districe_coding_tv:
			DialogUtil.districeCodingDialog(AddVehicleActivity.this,
					mCarPlateDistriceCodingTV);
			break;
		case R.id.vehicle_nianjian_notify_date_tv:
			DialogUtil.showTimeDialog(AddVehicleActivity.this,
					mNianjianNotifyDateTV);
			break;
		case R.id.vehicle_baoxian_notify_date_tv:
			DialogUtil.showTimeDialog(AddVehicleActivity.this,
					mBaoxianNotifyDateTV);
			break;
		case R.id.vehicle_engine_info_iv:
			DialogUtil.showHelpPageDialog(AddVehicleActivity.this);
			break;
		case R.id.vehicle_frame_info_iv:
			DialogUtil.showHelpPageDialog(AddVehicleActivity.this);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(AddVehicleActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(AddVehicleActivity.this);
	}

	private void updateUI(int engineno, int classno) {
		// 发动机号位数
		if (engineno == -1) {
			mEngineNumLL.setVisibility(View.VISIBLE);
			mEngineNumET.setHint("填写完整发动机号");
			mEngineNumET.setFilters(new InputFilter[0]);
			mVerifyEngineNum = RegularUtil.verifyEngineNumber;
		} else if (engineno == 0) {
			mEngineNumLL.setVisibility(View.GONE);
		} else {
			mEngineNumLL.setVisibility(View.VISIBLE);
			mEngineNumET.setHint("请填写发动机号后面" + engineno + "位");
			InputFilter[] engine_filters = { new LengthFilter(engineno) };
			mEngineNumET.setFilters(engine_filters);
			mVerifyEngineNum = new StringBuilder("^[a-zA-Z0-9]").append("{")
					.append(engineno).append("}$").toString();
		}

		// 车架号位数
		if (classno == -1) {
			mFrameNumLL.setVisibility(View.VISIBLE);
			mFrameNumET.setHint("填写完整车架号");
			mFrameNumET.setFilters(new InputFilter[0]);
			mVerifyFrameNum = RegularUtil.verifyVehicleFrameNumber;
		} else if (classno == 0) {
			mFrameNumLL.setVisibility(View.GONE);
		} else {
			mFrameNumLL.setVisibility(View.VISIBLE);
			mFrameNumET.setHint("请填写车架号后面" + classno + "位");
			InputFilter[] engine_filters = { new LengthFilter(classno) };
			mFrameNumET.setFilters(engine_filters);
			mVerifyFrameNum = new StringBuilder("^[a-zA-Z0-9]").append("{")
					.append(classno).append("}$").toString();
		}
	}

}
