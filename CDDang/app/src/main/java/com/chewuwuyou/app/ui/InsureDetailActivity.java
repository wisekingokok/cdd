package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;

public class InsureDetailActivity extends CDDBaseActivity implements
		OnClickListener, OnCheckedChangeListener {
	// delete start 暂不需要提交保险信息
	// private Handler mHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case Constant.NET_DATA_SUCCESS:
	// MyLog.i("YUY", "添加成功");
	// mSubmit.setEnabled(true);
	// break;
	// case Constant.NET_DATA_FAIL:
	// mSubmit.setEnabled(true);
	// break;
	//
	// default:
	// break;
	// }
	// super.handleMessage(msg);
	// };
	// };
	// delete end
	// 交强险rl,第三者责任险RL,玻璃单独破碎险RL,车身划痕险rl
	private RelativeLayout mRlcompulsorylayout, mRlthirdpartlayout,
			mRlglasslayout, mRlbodyscratcheslayout;
	// 第三者责任险cb,车辆损失险cb,全车盗抢险cb,玻璃单独破碎险cb,自燃损失险CB,不计免赔险cb,无过责任险cb,车上人员责任险cb,车身划痕险CB,涉水险cb
	private CheckBox mCbthirdpart, mCblost, mCbrob, mCbglass, mCbnaturalloss,
			mCbnondeductible, mCbnonehadliability, mCbpassengerliability,
			mCbbodyscratches, mCbwading;
	// 新车保险指导价tv,新车9折优惠价tv,裸车售价(元)tv,交强险tv,商业保险tv,电话投保tv
	private TextView mTvtotalprice, mTvtotalpricediscount, mTvspecprice,
			mTvcompulsory, mTvbusinessinsuranceprice, mPhoneInsure;
	private AlertDialog dialog;
	// private Button mSubmit;
	// private EditText mInsureContact, mInsureTel;

	private int mCompulsoryIndex = 0;
	private int mThirdpartIndex = 0;
	private int mGrassIndex = 0;
	private int mBodyscratchesIndex = 0;
	private int mSpecPrice = 0;// 裸车售价
	private int mCompulsoryPrice = 0;// 强交险
	private int mBuzInsuePrice = 0;// 商业保险
	private int mThirdpartPrice = 0;// 第三者责任险
	private int mlostPrice = 0;// 车辆损失险
	private int mRobPrice = 0;// 全车盗抢险
	private int mGlassPrice = 0;// 玻璃单独破碎险
	private int mNaturallossPrice = 0;// 自燃损失险
	private int mNondeductiblePrice = 0;// 不计免赔险
	private int mNonehadliabilityPrice = 0;// 无过责任险
	private int mPassengerliabilityPrice = 0;// 车上人员责任险
	private int mBodyscratchesPrice = 0;// 车身划痕险
	private int mWadingPrice = 0;// 涉水险

	// add by x
	private TextView mTVthirdpartgo;// tvthirdpartgo
	private TextView mTVglassrightgo;// tvglassrightgo
	private TextView mTVbodyscratchesrightgo;// tvbodyscratchesrightgo
	private RelativeLayout mTitleHeight;// 标题布局高度

	// end by x

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insure_detail_ac);
		initView();
	}

	private void showDialog(ListAdapter adapter, final int title) {
		// dialog = new
		// AlertDialog.Builder(this).setTitle("title").setMessage("message").create();
		dialog = new AlertDialog.Builder(this).create();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.mystyle); // 添加动画
		dialog.show();
		window.setContentView(R.layout.insurance_view);
		// View insurance_view =
		// LayoutInflater.from(this).inflate(R.layout.insurance_view, null);
		Button cancle = (Button) window.findViewById(R.id.btncancel);
		GridView gridView = (GridView) window.findViewById(R.id.gvinsurance);
		TextView textView = (TextView) window.findViewById(R.id.tvtitle);
		textView.setText(title);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (title) {
				case R.string.insure_compulsory_item:
					mTvcompulsory.setText(getCompulsory(arg2) + "");
					dialog.dismiss();
					break;
				case R.string.insure_thirdpart_item:
					mCbthirdpart.setText(getThirdpart(true, arg2) + "");
					dialog.dismiss();
					break;
				case R.string.insure_glass_item:
					mCbglass.setText(getGlass(true, arg2) + "");
					dialog.dismiss();
					break;
				case R.string.insure_bodyscratches_item:
					mCbbodyscratches.setText(getBodyscratches(true, arg2) + "");
					dialog.dismiss();
					break;
				}
				initData();
			}
		});
		cancle.setOnClickListener(this);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btncancel:
			StatService.onEvent(InsureDetailActivity.this,
					"ClickInsureDetailCancelBtn", "点击保险详情取消按钮");
			dialog.dismiss();
			break;
		case R.id.rlcompulsorylayout:
			StatService.onEvent(InsureDetailActivity.this,
					"ClickInsureDetailCompulRL", "点击保险详情交强险");
			List<String> datas = new ArrayList<String>();
			datas.add("家用6座以下");// 950
			datas.add("家用6座及以上");// 1100
			showDialog(new InsureAdapter(getApplicationContext(), datas,
					mCompulsoryIndex), R.string.insure_compulsory_item);
			break;
		case R.id.rlthirdpartlayout:
			StatService.onEvent(InsureDetailActivity.this,
					"ClickInsureDetaithirdpartRL", "点击保险详情第三者责任险");
			if (mCbthirdpart.isChecked()) {
				List<String> datas1 = new ArrayList<String>();
				datas1.add("  5万");
				datas1.add(" 10万");
				datas1.add(" 20万");
				datas1.add(" 50万");
				datas1.add("100万");
				showDialog(new InsureAdapter(getApplicationContext(), datas1,
						mThirdpartIndex), R.string.insure_thirdpart_item);
			}
			break;
		case R.id.rlglasslayout:
			StatService.onEvent(InsureDetailActivity.this,
					"ClickInsureDetaiGlassRL", "点击保险详情玻璃单独破碎险");
			if (mCbglass.isChecked()) {
				List<String> datas2 = new ArrayList<String>();
				datas2.add("进口");
				datas2.add("国产");
				showDialog(new InsureAdapter(getApplicationContext(), datas2,
						mGrassIndex), R.string.insure_glass_item);
			}
			break;
		case R.id.rlbodyscratcheslayout:
			StatService.onEvent(InsureDetailActivity.this,
					"ClickInsureDetaiBodyScratchRL", "点击保险详情车身划痕险");
			if (mCbbodyscratches.isChecked()) {
				List<String> datas3 = new ArrayList<String>();
				datas3.add("2千");
				datas3.add("5千");
				datas3.add("1万");
				datas3.add("2万");
				showDialog(new InsureAdapter(getApplicationContext(), datas3,
						mBodyscratchesIndex),
						R.string.insure_bodyscratches_item);
			}
			break;
		// delete start 2014-12-25
		// case R.id.insure_submit:
		// if (!"".equals(mInsureContact.getText().toString().trim())
		// && mInsureTel.getText().toString().matches("^1[0-9]{10}$")) {
		// AjaxParams params = new AjaxParams();
		// params.put("vehiclePrice", mSpecPrice);// 裸车售价
		// params.put("needInsurance", mCompulsoryPrice);// 交强险
		// params.put("businessInsurance", mBuzInsuePrice);// 商业保险合计
		// params.put("threeInsurance", mThirdpartPrice);// 第三者责任险
		// params.put("vehicleLossInsurance", mlostPrice);// 车辆损失险
		// params.put("stealInsurance", mRobPrice);// 全车盗抢险
		// params.put("glassBrokenInsurance", mGlassPrice);// 玻璃单独破碎险
		// params.put("naturalLossInsurance", mNaturallossPrice);// 自然损失险
		// params.put("freeLossInsurance", mNondeductiblePrice);// 不计免赔费
		// params.put("noResponseInsurance", mNonehadliabilityPrice);//
		// 无过责任险
		// params.put("personSafeInsurance", mPassengerliabilityPrice);//
		// 车上人员责任险
		// params.put("vehicleScratchInsurance", mBodyscratchesPrice);//
		// 车身划痕险
		// params.put("wadeInsurance", mWadingPrice);// 涉水险
		// params.put("countMoney", (mCompulsoryPrice + mBuzInsuePrice));//
		// 保险合计价格
		// params.put("telephone", mInsureTel.getText().toString());// 联系电话
		// params.put("username", mInsureContact.getText().toString());// 联系人姓名
		// mSubmit.setEnabled(false);
		// requestNet(mHandler, params, NetworkUtil.ADD_INSURANCE_INFO, false,
		// 0);
		// } else {
		// ToastUtil.toastShow(InsureDetailActivity.this, "输入信息有误请重新输入");
		// }
		// delete end
		}
	}

	/**
	 * 2、交强险 = 家用6座以下950元/年，家用6座及以上1100元/年 交通事故责任强制保险 =
	 * 家用6座以下950元/年，家用6座及以上1100元/年
	 * 
	 * @param isSelected
	 * @param compulsoryIndex
	 * @return
	 */
	public int getCompulsory(int compulsoryIndex) {
		switch (compulsoryIndex) {
		case 0:
			mCompulsoryPrice = 950;
			break;
		case 1:
			mCompulsoryPrice = 1100;
			break;
		}
		mTvcompulsory.setText(mCompulsoryPrice + "");
		mCompulsoryIndex = compulsoryIndex;
		return mCompulsoryPrice;
	}

	/**
	 * 3、第三者责任险： 赔付额度： 5万 --->516 10万--->746 20万--->924 50万 --->1252
	 * 100万--->1630
	 * 
	 * @param isSelected
	 * @param thirdpartIndex
	 * @return
	 */
	public int getThirdpart(boolean isSelected, int thirdpartIndex) {
		switch (thirdpartIndex) {
		case 0:
			mThirdpartPrice = 516;
			break;
		case 1:
			mThirdpartPrice = 746;
			break;
		case 2:
			mThirdpartPrice = 924;
			break;
		case 3:
			mThirdpartPrice = 1252;
			break;
		case 4:
			mThirdpartPrice = 1630;
			break;
		}
		mThirdpartIndex = thirdpartIndex;
		if (!isSelected) {
			mThirdpartPrice = 0;
		}
		mCbthirdpart.setText(mThirdpartPrice + "");
		return mThirdpartPrice;
	}

	/**
	 * 4、车辆损失险 =基础保费+裸车价格×1.0880% 基础保费 = 459
	 * 
	 * @param isSelected
	 * @return
	 */
	public int getLostMoney(boolean isSelected) {
		mlostPrice = 459 + (int) (0.01088D * this.mSpecPrice);
		if (!isSelected) {
			mlostPrice = 0;
		}
		mCblost.setText(mlostPrice + "");
		return mlostPrice;
	}

	/**
	 * 5、全车盗抢险 = 基础保费+裸车价格×费率 基础保费= 102 费率 = 0.004505D
	 * 
	 * @param isSelected
	 * @return
	 */
	public int getRobMoney(boolean isSelected) {
		mRobPrice = 102 + (int) (0.004505D * this.mSpecPrice);
		if (!isSelected) {
			mRobPrice = 0;
		}
		mCbrob.setText(mRobPrice + "");
		return mRobPrice;
	}

	/**
	 * 6、 玻璃单独破碎险 = 进口新车购置价×0.25%， 玻璃单独破碎险 = 国产新车购置价×0.15%
	 * 
	 * @param grassIndex
	 * @param isChina
	 *            是否国产
	 * @return
	 */
	public int getGlass(boolean isSelected, int grassIndex) {
		switch (grassIndex) {
		case 0:// 国产
			mGlassPrice = (int) (mSpecPrice * 0.0015D);
			break;
		case 1:// 进口
			mGlassPrice = (int) (mSpecPrice * 0.0025D);
			break;
		}
		mGrassIndex = grassIndex;

		if (!isSelected) {
			mGlassPrice = 0;
		}
		mCbglass.setText(mGlassPrice + "");
		return mGlassPrice;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(InsureDetailActivity.this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(InsureDetailActivity.this);
	}

	/**
	 * 7、车身划痕险 赔付额度：(0<裸车价格<=300000) 2千--->400 5千--->570 1万--->760 2万--->1140
	 * 赔付额度：(300000<裸车价格<=500000) 2千--->585 5千--->900 1万--->1170 2万--->1780
	 * 赔付额度：(500000<裸车价格<=99999999.900000006D) 2千--->850 5千--->1100 1万--->1500
	 * 2万--->2250
	 * 
	 * @param isSelected
	 * @param bodyscratchesIndex
	 * @return
	 */
	public int getBodyscratches(boolean isSelected, int bodyscratchesIndex) {
		if (mSpecPrice > 0 && mSpecPrice <= 300000) {
			switch (bodyscratchesIndex) {
			case 0:
				mBodyscratchesPrice = 400;
				break;
			case 1:
				mBodyscratchesPrice = 570;
				break;
			case 2:
				mBodyscratchesPrice = 760;
				break;
			case 3:
				mBodyscratchesPrice = 1140;
				break;
			}
		} else if (mSpecPrice > 300000 && mSpecPrice <= 500000) {
			switch (bodyscratchesIndex) {
			case 0:
				mBodyscratchesPrice = 585;
				break;
			case 1:
				mBodyscratchesPrice = 900;
				break;
			case 2:
				mBodyscratchesPrice = 1170;
				break;
			case 3:
				mBodyscratchesPrice = 1780;
				break;
			}
		} else if (mSpecPrice > 500000 && mSpecPrice <= 99999999.900000006D) {
			switch (bodyscratchesIndex) {
			case 0:
				mBodyscratchesPrice = 850;
				break;
			case 1:
				mBodyscratchesPrice = 1100;
				break;
			case 2:
				mBodyscratchesPrice = 1500;
				break;
			case 3:
				mBodyscratchesPrice = 2250;
				break;
			}
		}

		mBodyscratchesIndex = bodyscratchesIndex;
		if (!isSelected) {
			mBodyscratchesPrice = 0;
		}
		mCbbodyscratches.setText(mBodyscratchesPrice + "");
		return mBodyscratchesPrice;
	}

	/**
	 * 8、自燃损失险 = 新车购置价(裸车价格)×0.15% 自燃损失险 =新车购置价×0.15%
	 * 
	 * @param isSelected
	 * @return
	 */
	public int getNaturalloss(boolean isSelected) {
		mNaturallossPrice = (int) (0.0015D * this.mSpecPrice);// 自燃损失险
																// =新车购置价×0.15%
		if (!isSelected) {
			mNaturallossPrice = 0;
		}
		mCbnaturalloss.setText(mNaturallossPrice + "");
		return mNaturallossPrice;
	}

	/**
	 * 10、不计免赔特约险 = (车辆损失险+第三者责任险)×20%
	 * 
	 * @param isSelected
	 * @return
	 */
	public int getNondeductible(boolean isSelected) {
		mNondeductiblePrice = (int) ((mlostPrice + mThirdpartPrice) * 0.2D);
		if (!isSelected) {
			mNondeductiblePrice = 0;
		}
		mCbnondeductible.setText(mNondeductiblePrice + "");
		return mNondeductiblePrice;
	}

	/**
	 * 11、无过责任险 = 第三者责任险保险费×20%
	 * 
	 * @param isSelected
	 * @return
	 */
	public int getNonehadliability(boolean isSelected) {
		mNonehadliabilityPrice = (int) (mThirdpartPrice * 0.2D);
		if (!isSelected) {
			mNonehadliabilityPrice = 0;
		}
		mCbnonehadliability.setText(mNonehadliabilityPrice + "");
		return mNonehadliabilityPrice;
	}

	/**
	 * 12、车上人员责任险 = 每人保费50元，可根据车辆的实际座位数填写 （小车默认5人 250元）
	 * 
	 * @param isSelected
	 * @return
	 */
	public int getPassengerliability(boolean isSelected) {
		mPassengerliabilityPrice = 5 * 50;
		if (!isSelected) {
			mPassengerliabilityPrice = 0;
		}
		mCbpassengerliability.setText(mPassengerliabilityPrice + "");
		return mPassengerliabilityPrice;
	}

	/**
	 * 13、 . 涉水险 = (int)(0.00075D * this.mSpecPrice);
	 * 
	 * @param isSelected
	 * @return
	 */
	public int getWading(boolean isSelected) {
		mWadingPrice = (int) (0.00075D * this.mSpecPrice);
		if (!isSelected) {
			mWadingPrice = 0;
		}
		mCbwading.setText(mWadingPrice + "");
		return mWadingPrice;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		initData();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		((TextView) findViewById(R.id.sub_header_bar_tv)).setText("保险计算");
		findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finishActivity();
					}
				});
		String price = getIntent().getStringExtra("price");
		if (price != null) {
			mSpecPrice = Integer.parseInt(price);
		}

		mRlcompulsorylayout = (RelativeLayout) findViewById(R.id.rlcompulsorylayout);
		mRlcompulsorylayout.setOnClickListener(this);
		mRlthirdpartlayout = (RelativeLayout) findViewById(R.id.rlthirdpartlayout);
		mRlthirdpartlayout.setOnClickListener(this);
		mRlglasslayout = (RelativeLayout) findViewById(R.id.rlglasslayout);
		mRlglasslayout.setOnClickListener(this);
		mRlbodyscratcheslayout = (RelativeLayout) findViewById(R.id.rlbodyscratcheslayout);
		mRlbodyscratcheslayout.setOnClickListener(this);
		mPhoneInsure = (TextView) findViewById(R.id.phone_insure);
		mCbthirdpart = (CheckBox) findViewById(R.id.cbthirdpart);
		mCbthirdpart.setOnClickListener(this);
		mCbthirdpart.setOnCheckedChangeListener(this);
		mCblost = (CheckBox) findViewById(R.id.cblost);
		mCblost.setOnClickListener(this);
		mCblost.setOnCheckedChangeListener(this);
		mCbrob = (CheckBox) findViewById(R.id.cbrob);
		mCbrob.setOnClickListener(this);
		mCbrob.setOnCheckedChangeListener(this);
		mCbglass = (CheckBox) findViewById(R.id.cbglass);
		mCbglass.setOnClickListener(this);
		mCbglass.setOnCheckedChangeListener(this);
		mCbnaturalloss = (CheckBox) findViewById(R.id.cbnaturalloss);
		mCbnaturalloss.setOnClickListener(this);
		mCbnaturalloss.setOnCheckedChangeListener(this);
		mCbnonehadliability = (CheckBox) findViewById(R.id.cbnonehadliability);
		mCbnonehadliability.setOnClickListener(this);
		mCbnonehadliability.setOnCheckedChangeListener(this);
		mCbpassengerliability = (CheckBox) findViewById(R.id.cbpassengerliability);
		mCbpassengerliability.setOnClickListener(this);
		mCbpassengerliability.setOnCheckedChangeListener(this);
		mCbbodyscratches = (CheckBox) findViewById(R.id.cbbodyscratches);
		mCbbodyscratches.setOnClickListener(this);
		mCbbodyscratches.setOnCheckedChangeListener(this);
		mCbwading = (CheckBox) findViewById(R.id.cbwading);
		mCbwading.setOnClickListener(this);
		mCbwading.setOnCheckedChangeListener(this);
		mCbbodyscratches = (CheckBox) findViewById(R.id.cbbodyscratches);
		mCbbodyscratches.setOnClickListener(this);
		mCbbodyscratches.setOnCheckedChangeListener(this);

		mCbnondeductible = (CheckBox) findViewById(R.id.cbnondeductible);
		mCbnondeductible.setOnClickListener(this);
		mCbnondeductible.setOnCheckedChangeListener(this);
		mTvbusinessinsuranceprice = (TextView) findViewById(R.id.tvbusinessinsuranceprice);

		mTvtotalprice = (TextView) findViewById(R.id.tvtotalprice);

		mTvspecprice = (TextView) findViewById(R.id.tvspecprice);
		mTvspecprice.setText(Integer.parseInt(price) + "");
		mTvtotalpricediscount = (TextView) findViewById(R.id.tvtotalpricediscount);
		mTvtotalpricediscount.setOnClickListener(this);
		mTvcompulsory = (TextView) findViewById(R.id.tvcompulsory);
		// mTvcompulsory.setOnClickListener(this);

		// mTvcompulsory = (TextView) findViewById(R.id.tvcompulsory);
		// mTvcompulsory.setOnClickListener(this);
		// mSubmit = (Button) findViewById(R.id.insure_submit);
		// mSubmit.setOnClickListener(this);
		// mInsureContact = (EditText) findViewById(R.id.insure_contact);
		// mInsureTel = (EditText) findViewById(R.id.insure_tel);

		// add by x
		mTVthirdpartgo = (TextView) findViewById(R.id.tvthirdpartgo);
		mTVglassrightgo = (TextView) findViewById(R.id.tvglassrightgo);
		mTVbodyscratchesrightgo = (TextView) findViewById(R.id.tvbodyscratchesrightgo);
		// end by x

		initData();

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

		// mSpecPrice = 0;// 裸车售价
		mThirdpartPrice = getThirdpart(mCbthirdpart.isChecked(),
				mThirdpartIndex);// 第三者责任险

		mTVthirdpartgo.setVisibility(mCbthirdpart.isChecked() ? View.VISIBLE
				: View.INVISIBLE);
		mTVglassrightgo.setVisibility(mCbglass.isChecked() ? View.VISIBLE
				: View.INVISIBLE);
		mTVbodyscratchesrightgo
				.setVisibility(mCbbodyscratches.isChecked() ? View.VISIBLE
						: View.INVISIBLE);

		mlostPrice = getLostMoney(mCblost.isChecked());// 车辆损失险
		mRobPrice = getRobMoney(mCbrob.isChecked());// 全车盗抢险
		mGlassPrice = getGlass(mCbglass.isChecked(), mGrassIndex);// 玻璃单独破碎险
		mNaturallossPrice = getNaturalloss(mCbnaturalloss.isChecked());// 自燃损失险
		mNondeductiblePrice = getNondeductible(mCbnondeductible.isChecked());// 不计免赔险
		mNonehadliabilityPrice = getNonehadliability(mCbnonehadliability
				.isChecked());// 无过责任险
		mPassengerliabilityPrice = getPassengerliability(mCbpassengerliability
				.isChecked());// 车上人员责任险
		mBodyscratchesPrice = getBodyscratches(mCbbodyscratches.isChecked(),
				mBodyscratchesIndex);// 车身划痕险
		mWadingPrice = getWading(mCbwading.isChecked());// 涉水险

		mBuzInsuePrice = mThirdpartPrice + mlostPrice + mRobPrice + mGlassPrice
				+ mNaturallossPrice + mNondeductiblePrice
				+ mNonehadliabilityPrice + mPassengerliabilityPrice
				+ mBodyscratchesPrice + mWadingPrice;// 商业保险
		mCompulsoryPrice = getCompulsory(mCompulsoryIndex);// 强交险
		mTvbusinessinsuranceprice.setText(mBuzInsuePrice + "");
		mTvtotalprice.setText((mCompulsoryPrice + mBuzInsuePrice) + "");
		mTvtotalpricediscount
				.setText((int) ((mCompulsoryPrice + mBuzInsuePrice) * 0.9D)
						+ "");
		mPhoneInsure
				.setText((int) ((mCompulsoryPrice + mBuzInsuePrice) * 0.9D * 0.85D)
						+ "");

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}
}

class InsureAdapter extends BaseAdapter {
	List<String> mDatas;
	Context mContext;
	private int mIndex;

	public InsureAdapter(Context context, List<String> datas, int index) {
		mDatas = datas;
		mContext = context;
		mIndex = index;
	}

	@Override
	public int getCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckedTextView textview;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.insuranceview_item, null);
			textview = (CheckedTextView) convertView
					.findViewById(R.id.insure_item);

			convertView.setTag(textview);
		} else {
			textview = (CheckedTextView) convertView.getTag();
		}
		if (mIndex == position) {
			textview.setChecked(true);
		}
		textview.setText(mDatas.get(position));
		return convertView;
	}
}
