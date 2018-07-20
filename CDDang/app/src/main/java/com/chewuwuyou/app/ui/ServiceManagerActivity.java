package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ProPrice;
import com.chewuwuyou.app.tools.EditInputFilter;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.XListView;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:商家对自己服务的管理
 * @author:yuyong
 * @date:2015-9-28下午5:01:38
 * @version:1.2.1
 */
public class ServiceManagerActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private XListView mIllegalServiceLV;
	private XListView mVehicleServiceLV;
	private XListView mLicenseServiceLV;
	private RelativeLayout mTitleHeight;// 标题布局高度
	private TextView mSaveTV;// 保存
	private List<ProPrice> mProPrices;// 商家同意管理服务价格
	private List<ProPrice> mIllegalProPrices;// 违章服务价格
	private List<ProPrice> mVehicleProPrices;// 车辆服务价格
	private List<ProPrice> mLicenseProPrices;// 价格服务价格
	private StringBuilder mServiceIdSB;// 保存时拼接的服务Id
	private StringBuilder mServicePriceSB;// 保存时拼接的服务价格
	private StringBuilder mServiceTypeSB;// 服务类型
	private ServiceManagerAdapter mIllegalServiceAdapter;
	private ServiceManagerAdapter mLicenseServiceAdapter;
	private ServiceManagerAdapter mVehicleServiceAdapter;
	// private CheckBox mIllegalCB;// 选中服务
	// private int mIllegalServicePosition;
	private LinearLayout mServiceManageLL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.service_manager_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mIllegalServiceLV = (XListView) findViewById(R.id.illegal_service_manager_list);
		mVehicleServiceLV = (XListView) findViewById(R.id.vehicle_service_manager_list);
		mLicenseServiceLV = (XListView) findViewById(R.id.license_service_manager_list);
		mSaveTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
		mSaveTV.setVisibility(View.VISIBLE);
		// mIllegalCB = (CheckBox) findViewById(R.id.illegal_check_cb);
		mServiceManageLL = (LinearLayout) findViewById(R.id.service_manage_ll);
		mServiceManageLL.setFocusable(true);
		mServiceManageLL.setFocusableInTouchMode(true);
		mServiceManageLL.requestFocus();

	}

	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mTitleTV.setText("服务管理");
		mSaveTV.setText("保存");
		mProPrices = new ArrayList<ProPrice>();
		mIllegalProPrices = new ArrayList<ProPrice>();
		mVehicleProPrices = new ArrayList<ProPrice>();
		mLicenseProPrices = new ArrayList<ProPrice>();
		AjaxParams params = new AjaxParams();
		params.put("busId", CacheTools.getUserData("userId"));
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					MyLog.i("YUY", "商家服务选项的管理 = " + msg.obj.toString());
					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						mProPrices = ProPrice.parseList(jo.getJSONArray(
								"prices").toString());
						// for (int i = 0; i < mProPrices.size(); i++) {
						// if (mProPrices.get(i).getId() == 20) {
						// mIllegalServicePosition = i;
						// }
						// }
						for (int i = 0; i < mProPrices.size(); i++) {
							if (mProPrices.get(i).getType() == 1) {

								// if (!mProPrices.get(i).getProjectName()
								// .equals("上门取资料")
								// && mProPrices.get(i).getProjectNum() == 101)
								// {
								mIllegalProPrices.add(mProPrices.get(i));
								// if (mProPrices.get(i).getPrice() > 0) {
								// mIllegalCB.setChecked(true);
								// } else {
								// mIllegalCB.setChecked(false);
								// }
								// }
							} else if (mProPrices.get(i).getType() == 2) {
								if (!mProPrices.get(i).getProjectName()
										.equals("上门取车")) {
									mVehicleProPrices.add(mProPrices.get(i));
								}

							} else {
								if (!mProPrices.get(i).getProjectName()
										.equals("上门取资料")) {
									mLicenseProPrices.add(mProPrices.get(i));
								}

							}
						}
						getListViewHeight(mIllegalServiceLV, mIllegalProPrices);
						getListViewHeight(mVehicleServiceLV, mVehicleProPrices);
						getListViewHeight(mLicenseServiceLV, mLicenseProPrices);
						mIllegalServiceAdapter = new ServiceManagerAdapter(
								ServiceManagerActivity.this, mIllegalProPrices,
								mIllegalServiceLV);
						mIllegalServiceLV.setAdapter(mIllegalServiceAdapter);

						mVehicleServiceAdapter = new ServiceManagerAdapter(
								ServiceManagerActivity.this, mVehicleProPrices,
								mVehicleServiceLV);
						mVehicleServiceLV.setAdapter(mVehicleServiceAdapter);

						mLicenseServiceAdapter = new ServiceManagerAdapter(
								ServiceManagerActivity.this, mLicenseProPrices,
								mLicenseServiceLV);
						mLicenseServiceLV.setAdapter(mLicenseServiceAdapter);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		}, params, NetworkUtil.GET_ALL_PRO_PRICE, false, 0);
	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		mSaveTV.setOnClickListener(this);
		// mIllegalCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// if (arg1) {
		// mProPrices.get(mIllegalServicePosition).setPrice(10);
		// } else {
		// mProPrices.get(mIllegalServicePosition).setPrice(0);
		// }
		//
		// }
		// });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.sub_header_bar_right_tv:
			mServiceIdSB = new StringBuilder("");
			mServicePriceSB = new StringBuilder("");
			mServiceTypeSB = new StringBuilder("");

			for (int i = 0; i < mProPrices.size(); i++) {
				if (mProPrices.get(i).getPrice() > 0
						&& mProPrices.get(i).getPrice() < 10) {
					ToastUtil.toastShow(ServiceManagerActivity.this,
							"服务价格必须大于10");
					return;
				}
				ProPrice proPrice = mProPrices.get(i);
				mServiceIdSB.append(proPrice.getId()).append("-");
				mServiceTypeSB.append(proPrice.getType()).append("-");
				mServicePriceSB.append(proPrice.getPrice()).append("-");
			}
			String ids = null;
			String prices = null;
			String serviceTypes = null;
			if (mServiceIdSB.length() > 0) {
				ids = mServiceIdSB.substring(0, mServiceIdSB.length() - 1)
						.toString();
				prices = mServicePriceSB.substring(0,
						mServicePriceSB.length() - 1).toString();
				serviceTypes = mServiceTypeSB.substring(0,
						mServiceTypeSB.length() - 1).toString();
			}
			AjaxParams params = new AjaxParams();
			params.put("ids", ids);
			params.put("prices", prices);
			params.put("types", serviceTypes);
			params.put("businessType", "1");// 1为商家设定的价格，2为地区价格
			requestNet(new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:
						ToastUtil.toastShow(ServiceManagerActivity.this,
								"更新服务价格成功！");
						finishActivity();
						break;

					default:
						break;
					}
				}
			}, params, NetworkUtil.UPDATE_SERVICE_PRICE, false, 0);
			break;
		default:
			break;
		}
	}

	private int getListViewHeight(XListView mView, List<ProPrice> proPrices) {
		int turnon = 0;
		int turnoff = 0;
		int viewHeight = 0;
		for (int i = 0; i < proPrices.size(); i++) {
			if (proPrices.get(i).getPrice() > 0) {
				turnon++;
			} else {
				turnoff++;
			}
		}
		viewHeight = turnon * 96 + turnoff * 48;
		mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				viewHeight));
		return viewHeight;
	}

	private void UpdateListViewHeight(XListView mView, boolean isTurnoff) {
		if (isTurnoff) {
			mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					mView.getHeight() + 48));
		} else {
			mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					mView.getHeight() - 48));
		}

	}

	class ServiceManagerAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater layoutInflater;
		private List<ProPrice> mSerProPrices;
		private HashMap<Integer, Boolean> isSelected;
		private XListView mListView;

		public ServiceManagerAdapter(Context context, List<ProPrice> proPrices,
				XListView listView) {
			this.mContext = context;
			layoutInflater = LayoutInflater.from(mContext);
			this.mSerProPrices = proPrices;
			this.mListView = listView;
			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < mSerProPrices.size(); i++) {
				isSelected.put(i, mSerProPrices.get(i).getPrice() > 0 ? true
						: false);
			}
		}

		public class ViewHolder {

			TextView serviceNameTV;
			TextView serviceFeeTV;
			CheckBox serviceCB;
			EditText servicePriceET;
			LinearLayout settingPriceLL;
		}

		@Override
		public int getCount() {
			return mSerProPrices.size();
		}

		@Override
		public Object getItem(int position) {
			return mSerProPrices.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.service_manager_item, null);
				holder = new ViewHolder();
				holder.serviceNameTV = (TextView) convertView
						.findViewById(R.id.pro_name_tv);
				holder.serviceFeeTV = (TextView) convertView
						.findViewById(R.id.pro_fees_tv);
				holder.serviceCB = (CheckBox) convertView
						.findViewById(R.id.is_check_cb);
				holder.servicePriceET = (EditText) convertView
						.findViewById(R.id.service_manager_item_et);
				holder.settingPriceLL = (LinearLayout) convertView
						.findViewById(R.id.setting_service_price_ll);
				holder.servicePriceET.setTag(position);
				holder.settingPriceLL.setTag(position);
				InputFilter[] filters = { new EditInputFilter(4000),
						new InputFilter.LengthFilter(7) /* 这里限制输入的长度为7个字母 */};
				holder.servicePriceET.setFilters(filters);

				class MyTextWatcher implements TextWatcher {
					private ViewHolder mHolder;

					public MyTextWatcher(ViewHolder holder) {
						mHolder = holder;
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						if (s != null && !"".equals(s.toString())) {
							int position = (Integer) mHolder.servicePriceET
									.getTag();
							for (int i = 0; i < mProPrices.size(); i++) {
								if (mSerProPrices.get(position).getId() == mProPrices
										.get(i).getId()) {
									mProPrices.get(i).setPrice(
											Integer.parseInt(s.toString()));
								}
							}
						}
					}
				}
				class MyCheck implements OnCheckedChangeListener {
					private ViewHolder mHolder;

					public MyCheck(ViewHolder holder) {
						mHolder = holder;
					}

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int position = (Integer) holder.settingPriceLL.getTag();
						isSelected.put(position, isChecked);
						if (isChecked) {
							mHolder.settingPriceLL.setVisibility(View.VISIBLE);
							UpdateListViewHeight(mListView, true);
						} else {
							mHolder.settingPriceLL.setVisibility(View.GONE);
							UpdateListViewHeight(mListView, false);
							for (int i = 0; i < mProPrices.size(); i++) {
								if (mSerProPrices.get(position).getId() == mProPrices
										.get(i).getId()) {
									mProPrices.get(i).setPrice(0);
								}
							}
						}
					}

				}
				holder.serviceCB
						.setOnCheckedChangeListener(new MyCheck(holder));
				holder.servicePriceET.addTextChangedListener(new MyTextWatcher(
						holder));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.servicePriceET.setTag(position);
				holder.settingPriceLL.setTag(position);
			}
			ProPrice proPrice = mSerProPrices.get(position);

			if (isSelected.get(position)) {
				holder.settingPriceLL.setVisibility(View.VISIBLE);
			} else {
				holder.settingPriceLL.setVisibility(View.GONE);
			}
			holder.serviceCB.setChecked(isSelected.get(position));
			holder.serviceNameTV.setText(proPrice.getProjectName());
			holder.servicePriceET.setText(String.valueOf(proPrice
					.getPrice()));
			if (proPrice.getType() == Constant.SERVICE_TYPE.ILLEGAL_SERVICE) {
				holder.serviceFeeTV.setVisibility(View.GONE);
			} else {
				holder.serviceFeeTV.setVisibility(View.VISIBLE);
				holder.serviceFeeTV.setText("规费：" + proPrice.getFees());
			}

			return convertView;

		}
	}

}
