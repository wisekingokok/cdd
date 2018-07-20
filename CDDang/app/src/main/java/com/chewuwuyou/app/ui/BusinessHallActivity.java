package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.fragment.DrivingFragment;
import com.chewuwuyou.app.fragment.IllegalPaymentFragment;
import com.chewuwuyou.app.fragment.VehicleServiceFragment;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @author zengys 新的业务大厅
 * 
 */
public class BusinessHallActivity extends BaseFragmentActivity implements OnClickListener,IllegalPaymentFragment.CallBackValue,DrivingFragment.CallBackValue,VehicleServiceFragment.CallBackValue{

	private ImageButton mImageBack;//返回上一页
	private TextView mTextTitle;//标题
	private TextView mTextRight;//发布
	private TextView mTextBusinessCity;//服务地区
	private RadioButton mRabtnRreak;//违章代缴
	private RadioButton mRabtnCars;//车辆服务
	private RadioButton mRabtnTicket;//驾证服务

	private Fragment[] mFragments;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	/**
	 * 服务地区
	 */
	private String mServiceLoc;
	// 省市区ID
	private String mProvinceId;
	private String mCityId;
	private String mDistrictId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_hall_layout);

		initView();
		initEvent();
		initData();
	}

	/**
	 * 初始化
	 */
	private void initView() {
		mTextTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		mImageBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mTextRight = (TextView) findViewById(R.id.sub_header_bar_right_tv);
		mTextBusinessCity = (TextView) findViewById(R.id.text_business_city);
		mRabtnRreak = (RadioButton) findViewById(R.id.peccancy_Rbn);
		mRabtnCars = (RadioButton) findViewById(R.id.accreditation_Rbn);
		mRabtnTicket = (RadioButton) findViewById(R.id.ticket_Rbn);

		mFragments = new Fragment[3];
		fragmentManager = getSupportFragmentManager();
		if (mFragments[0] == null) {
			mFragments[0] = new IllegalPaymentFragment();
			fragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[0]).commit();
		}
		if (mFragments[1] == null) {
			mFragments[1] = new VehicleServiceFragment();
			fragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[1]).commit();
		}
		if (mFragments[2] == null) {
			mFragments[2] = new DrivingFragment();
			fragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[2]).commit();
		}
		fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
		fragmentTransaction.show(mFragments[0]).commit();// 设置第几页显示
	}

	/**
	 * 业务逻辑处理
	 */
	private void initData() {
		mTextTitle.setText("发布大厅");
		mTextRight.setText("发布");
		mTextRight.setVisibility(View.VISIBLE);
	}

	/**
	 * 事件监听
	 */
	private void initEvent() {
		mImageBack.setOnClickListener(this);
		mTextRight.setOnClickListener(this);
		mRabtnRreak.setOnClickListener(this);
		mRabtnCars.setOnClickListener(this);
		mRabtnTicket.setOnClickListener(this);
	}

	/**
	 * 点击事件
	 * @param arg0
     */
	@Override
	public void onClick(View arg0) {
		Intent intent;
		fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
		Drawable mRadioback = getResources().getDrawable(R.drawable.message_radio);
		switch (arg0.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			if (AppManager.isExsitMianActivity(MainActivityEr.class, BusinessHallActivity.this) == false) {//判断首页是否在堆栈中
				startActivity(new Intent(BusinessHallActivity.this, MainActivityEr.class));
			}
			finishActivity();
			break;
		case R.id.peccancy_Rbn://违章代缴
			fragmentTransaction.show(mFragments[0]).commit();// 违章代缴
			mRabtnRreak.setCompoundDrawablesWithIntrinsicBounds(
					null, null, null, mRadioback);
			mRabtnCars.setCompoundDrawablesWithIntrinsicBounds(
					null, null, null, null);
			mRabtnTicket.setCompoundDrawablesWithIntrinsicBounds(
					null, null, null, null);
			break;
		case R.id.accreditation_Rbn://车辆服务
			fragmentTransaction.show(mFragments[1]).commit();// 车辆服务
			mRabtnCars.setCompoundDrawablesWithIntrinsicBounds(
					null, null, null, mRadioback);
			mRabtnRreak.setCompoundDrawablesWithIntrinsicBounds(
					null, null, null, null);
			mRabtnTicket.setCompoundDrawablesWithIntrinsicBounds(
					null, null, null, null);
			break;
		case R.id.ticket_Rbn://驾证服务
			fragmentTransaction.show(mFragments[2]).commit();// 驾证服务
			mRabtnTicket.setCompoundDrawablesWithIntrinsicBounds(null, null, null, mRadioback);
			mRabtnRreak.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			mRabtnCars.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			break;
		 case R.id.sub_header_bar_right_tv:
			 if(!TextUtils.isEmpty(mTextBusinessCity.getText().toString())){
				 intent = new Intent(this,NewReleaseHallActivity.class);
				 intent.putExtra("serviceLoc", mTextBusinessCity.getText().toString());
				 intent.putExtra("provinceId", mProvinceId);
				 intent.putExtra("cityId", mCityId);
				 intent.putExtra("districtId", mDistrictId);
				 startActivity(intent);
			 }else{
				 ToastUtil.toastShow(this,"正在获取服务地区，请稍后...");
			 }
		 break;
		default:
			break;
		}
	}

	/**
	 * 接收回调的省市区ID
     */
	@Override
	public void SendMessageValue(String serviceLoc, String provinceId, String cityId, String districtId) {
		mTextBusinessCity.setText(serviceLoc);
		mProvinceId = provinceId;
		mCityId = cityId;
		mDistrictId = districtId;
	}
}
