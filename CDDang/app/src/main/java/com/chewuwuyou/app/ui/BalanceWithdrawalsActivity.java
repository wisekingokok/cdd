package com.chewuwuyou.app.ui;

import java.text.DecimalFormat;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.tools.EditInputFilterThousand;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.KeyboardUtil;
import com.chewuwuyou.app.utils.KeyboardUtil.InputFinishListener;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.WalletUtil;

/**
 * @describe:余额提现
 * @author:liuchun
 */
public class BalanceWithdrawalsActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitel;// 标题
	private ImageButton mSubHeaderBarLeftIbtn;// 返回上一页
	private TextView mSubHeaderBarRightTv;// 提现说明
	private LinearLayout mChoiceAccount;
	private TextView mAccountNameTV, mAccountNoTV;
	private EditText mWithdrawalsInsufficient;
	private TextView mBalanceInsufficient;// 余额不足
	private TextView mWithdrawalsProcedures;// 手续费
	private TextView mArrivalMoney;// 到账金额
	private double mFeiLv;// 手续费率
	private double mMaxMoney;// 最大可提现金额
	private double mShouXuFei;// 手续费
	private double mDaoZhangMoney;// 到账金额
	private Button mBalanceBtn;// 提现
	private DecimalFormat mDF = new DecimalFormat("#0.00");// 保留两位小数
	private String mDefaultAccount;// 默认提现账户
	private Dialog dialog;
	private RelativeLayout mTitleHeight;// 标题布局高度
	private KeyboardUtil keyBoard;
	private TextView mXieyiTV;// 点击查看服务协议

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balance_withdrawals);
		initView();// 初始化
		initData();
		initEvent();// 点击事件
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mTitel = (TextView) findViewById(R.id.sub_header_bar_tv);
		mTitel.setText("余额提现");
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mSubHeaderBarRightTv = (TextView) findViewById(R.id.sub_header_bar_right_tv);
		mAccountNameTV = (TextView) findViewById(R.id.account_name);
		mAccountNoTV = (TextView) findViewById(R.id.account_phone);
		mWithdrawalsInsufficient = (EditText) findViewById(R.id.withdrawals_insufficient);// 输入提现金额
		InputFilter[] filters = { new EditInputFilterThousand(50000, 2),new InputFilter.LengthFilter(8) };
		mWithdrawalsInsufficient.setFilters(filters);

		mBalanceInsufficient = (TextView) findViewById(R.id.balance_insufficient);// 余额不足
		mWithdrawalsProcedures = (TextView) findViewById(R.id.withdrawals_procedures);// 手续费
		mArrivalMoney = (TextView) findViewById(R.id.arrival_money);// 到账金额
		mSubHeaderBarRightTv.setVisibility(View.VISIBLE);
		mSubHeaderBarRightTv.setText("提现说明");
		mChoiceAccount = (LinearLayout) findViewById(R.id.choice_account);// 点击切换账户
		mBalanceBtn = (Button) findViewById(R.id.balance_btn);
		mXieyiTV = (TextView) findViewById(R.id.xieyi_tv);
		// setPricePoint(mWithdrawalsInsufficient);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一页
			BalanceWithdrawalsActivity.this.finishActivity();
			break;
		case R.id.sub_header_bar_right_tv:// 提现说明
			intent = new Intent(BalanceWithdrawalsActivity.this,
					DepositExplainActivity.class);
			startActivity(intent);
			break;
		case R.id.choice_account:// 点击切换账户
			intent = new Intent(BalanceWithdrawalsActivity.this,
					ChoiceaCcountActivity.class);
			intent.putExtra("accountNo", mDefaultAccount);
			startActivityForResult(intent, 20);
			break;
		case R.id.balance_btn:
			if (TextUtils.isEmpty(mDefaultAccount)) {
				ToastUtil.toastShow(BalanceWithdrawalsActivity.this,
						"选择提现账户为空，不能提现");
			} else {
				createWalletPayDialog(BalanceWithdrawalsActivity.this);// 弹起输入支付密码的弹框
			}

			break;

		case R.id.xieyi_tv:

			intent = new Intent(BalanceWithdrawalsActivity.this,
					AgreementActivity.class);
			intent.putExtra("type", 4);
			startActivity(intent);
			break;

		case R.id.xieyi_tv_two:

			intent = new Intent(BalanceWithdrawalsActivity.this,
					AgreementActivity.class);
			intent.putExtra("type", 7);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mMaxMoney = Double.valueOf(getIntent().getStringExtra("amount"));
		if (mMaxMoney >= 50000) {
			mWithdrawalsInsufficient.setHint("单次最大可提现50000.00");
		} else {
			mWithdrawalsInsufficient.setHint("最大可提现" + mDF.format(mMaxMoney));
		}

		// 请求提现默认账户和费率
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					MyLog.i("YUY", "提现默认账户和费率 = " + msg.obj.toString());
					// {"shouxufeilv":"0.004","morenAccountNo":"123456687","morenAccount":"aa"}
					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						mAccountNameTV.setText(jo.getString("morenAccount"));
						mDefaultAccount = jo.getString("morenAccountNo");
						WalletUtil.showAccount(mDefaultAccount, mAccountNoTV);
						mFeiLv = Double.valueOf(jo.getString("shouxufeilv"));
						MyLog.i("YUY", "金额 = " + mMaxMoney + " 费率 = " + mFeiLv
								+ " 手续费 = " + mShouXuFei);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;
				case Constant.NET_DATA_FAIL:
					ToastUtil.toastShow(BalanceWithdrawalsActivity.this,
							((DataError) msg.obj).getErrorMessage());
					break;
				default:
					break;
				}
			}
		}, null, NetworkUtil.MAKE_AWITHDRAW_PAGE, false, 0);
	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mXieyiTV.setOnClickListener(this);
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
		mSubHeaderBarRightTv.setOnClickListener(this);
		mChoiceAccount.setOnClickListener(this);
		mWithdrawalsInsufficient
				.addTextChangedListener(new EditChangedListener());
		mBalanceBtn.setOnClickListener(this);
	}

	/**
	 * 回调方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			mAccountNameTV.setText(data.getStringExtra("name"));
			mDefaultAccount = data.getStringExtra("phone");
			WalletUtil.showAccount(mDefaultAccount, mAccountNoTV);
		}

	}

	/**
	 * 对提现金额的输入监听
	 * 
	 * @author yuyong
	 * 
	 */
	class EditChangedListener implements TextWatcher {
		// private CharSequence temp;// 监听前的文本
		// private int editStart;// 光标开始位置
		// private int editEnd;// 光标结束位置
		// private final int charMaxNum = getIntent().getStringExtra("amount")
		// .length();

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {

//			// 通过正则表达式验证输入方式
//			if (!TextUtils.isEmpty(s.toString()) && s.length() <= 6&& s.toString().contains(".")) {mWithdrawalsInsufficient.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s.toString().indexOf(".") + 3) });
//			} else {
//				mWithdrawalsInsufficient.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
//			}

			if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(".")) {

				mShouXuFei = WalletUtil.getSxf(Double.valueOf(mWithdrawalsInsufficient.getText().toString())* mFeiLv);// 动态算出手续费

				// TODO暂时不需要手续费
				// if (mShouXuFei <= 2) {
				// mShouXuFei = 2;
				// }
				mDaoZhangMoney = Double.valueOf(s.toString()) - mShouXuFei;// 动态算出到账金额

				mWithdrawalsProcedures.setText(String.valueOf(mShouXuFei));
				mArrivalMoney.setText(mDF.format(mDaoZhangMoney));
				if (Double.valueOf(s.toString()) == 0) {// 检测输入提现金额为0时的变动
					mBalanceInsufficient.setVisibility(View.GONE);
					mBalanceBtn
							.setBackgroundResource(R.drawable.common_gray_btn_bg);
					mBalanceBtn.setClickable(false);
					mBalanceBtn.setEnabled(false);

					mBalanceBtn.setTextColor(getResources().getColor(
							R.color.common_text_color));
				} else if (Double.valueOf(s.toString()) < 100) {// 提现金额必须大于100
					mBalanceInsufficient.setText("提现金额必须大于100");
					mBalanceInsufficient.setVisibility(View.VISIBLE);
					mBalanceBtn
							.setBackgroundResource(R.drawable.common_gray_btn_bg);
					mBalanceBtn.setClickable(false);
					mBalanceBtn.setEnabled(false);
					mBalanceBtn.setTextColor(getResources().getColor(
							R.color.common_text_color));

				} else if (Double.valueOf(s.toString()) > mMaxMoney) {// 输入金额大于最大提现金额时
					mBalanceInsufficient.setText("余额不足，无法提现");
					mBalanceInsufficient.setVisibility(View.VISIBLE);
					mBalanceBtn
							.setBackgroundResource(R.drawable.common_gray_btn_bg);
					mBalanceBtn.setClickable(false);
					mBalanceBtn.setEnabled(false);
					mBalanceBtn.setTextColor(getResources().getColor(
							R.color.common_text_color));
				} else if (Double.valueOf(s.toString()) > 50000) {// 单次提现金额不得大于50000
					mBalanceInsufficient.setText("单次提现最大金额50000");
					mBalanceInsufficient.setVisibility(View.VISIBLE);
					mBalanceBtn
							.setBackgroundResource(R.drawable.common_gray_btn_bg);
					mBalanceBtn.setClickable(false);
					mBalanceBtn.setEnabled(false);
					mBalanceBtn.setTextColor(getResources().getColor(
							R.color.common_text_color));
				} else {
					mBalanceInsufficient.setVisibility(View.GONE);
					mBalanceBtn
							.setBackgroundResource(R.drawable.common_blue_btn);
					mBalanceBtn.setClickable(true);
					mBalanceBtn.setEnabled(true);
					mBalanceBtn.setTextColor(getResources().getColor(
							R.color.white));

				}
			} else {
				mBalanceInsufficient.setVisibility(View.GONE);
				mWithdrawalsProcedures.setText("0.00");
				mArrivalMoney.setText("0.00");
			}

			// editStart = mWithdrawalsInsufficient.getSelectionStart();
			// editEnd = mWithdrawalsInsufficient.getSelectionEnd();
			// if (temp.length() > charMaxNum) {
			// s.delete(editStart - 1, editEnd);
			// int tempSelection = editStart;
			// mWithdrawalsInsufficient.setText(s);
			// mWithdrawalsInsufficient.setSelection(tempSelection);
			// }

		}
	};

	/**
	 * 判断支付密码是否正确
	 * 
	 * @param text
	 * @param mTextWrong
	 * @param keyBoard
	 */
	private void startBalance(String text, final TextView mTextWrong,
			final KeyboardUtil keyBoard) {
//		NetworkUtil.postNoHeader(NetworkUtil.GET_RATE, null,
//				new AjaxCallBack<String>() {
//					@Override
//					public void onSuccess(String t) {
//						super.onSuccess(t);
//						MyLog.i("YUY", "所有费率 = " + t);
//						try {
//							JSONObject jo = new JSONObject(t);
//							if (jo.getInt("result") == 1) {
//
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//
//					}
//
//					@Override
//					public void onFailure(Throwable t, int errorNo,
//							String strMsg) {
//						super.onFailure(t, errorNo, strMsg);
//					}
//				});

		// 提现账户(account账户名、accountNo 账户号
		// 提现金额amount
		// 手续费 counterFee
		// 到账金额 arrivalAmount
		AjaxParams params = new AjaxParams();
		params.put("account", mAccountNameTV.getText().toString());
		params.put("accountNo", mDefaultAccount);
		params.put("amount", mWithdrawalsInsufficient.getText().toString());
		params.put("payPass", MD5Util.getMD5(text));// 缺少MD5加密
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					MyLog.i("YUY", "提现 = " + msg.obj.toString());
					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						if (jo.getString("re").contains("提现申请成功")) {
							ToastUtil.toastShow(
									BalanceWithdrawalsActivity.this, "提现申请成功");
							finishActivity();
						} else {
							// ToastUtil.toastShow(
							// BalanceWithdrawalsActivity.this,
							// jo.getString("re"));
							mTextWrong.setText(jo.getString("re").toString());
							mTextWrong.setVisibility(View.VISIBLE);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case Constant.NET_DATA_FAIL:
					// ToastUtil.toastShow(BalanceWithdrawalsActivity.this,
					// ((DataError) msg.obj).getErrorMessage());
					mTextWrong.setText(((DataError) msg.obj).getErrorMessage());
					mTextWrong.setVisibility(View.VISIBLE);
					keyBoard.clearText();
					// dialog.dismiss();
					break;
				default:
					ToastUtil.toastShow(BalanceWithdrawalsActivity.this,"提现发起失败");
					break;
				}
			}

		}, params, NetworkUtil.MAKE_AWITHDRAW, false, 0);
	}

	@SuppressWarnings("deprecation")
	public void createWalletPayDialog(final Context context) {
		dialog = new Dialog(context, R.style.myDialogTheme);
		LayoutInflater inflater = LayoutInflater.from(context);
		final LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.wallet_pay_layout, null);
		dialog.setContentView(layout);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.mystyle); // 添加动画

		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);

		ImageView closePayIV = (ImageView) layout
				.findViewById(R.id.wallet_pay_close_iv);
		TextView mForgetPayPassTV = (TextView) layout
				.findViewById(R.id.forget_pay_password_tv);
		final TextView mTextWrong = (TextView) layout
				.findViewById(R.id.text_wrong);

		mForgetPayPassTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, VerifyPayActivity.class);
				intent.putExtra("clilckType", Constant.CLICK_FORGET_PAYPASS);
				startActivity(intent);
			}
		});
		closePayIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		LinearLayout layout_input = (LinearLayout) layout
				.findViewById(R.id.layout_input);

		KeyboardView keyboardView = (KeyboardView) layout
				.findViewById(R.id.keyboard_view);
		keyBoard = new KeyboardUtil(this, this, keyboardView, layout_input,
				new InputFinishListener() {

					@Override
					public void inputHasOver(String text) {
						// 发起提现
						// walletPay(dialog);
						startBalance(text, mTextWrong, keyBoard);
					}
				});
		keyBoard.showKeyboard();
		dialog.show();
		layout_input.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				keyBoard.showKeyboard();
				return false;
			}
		});
	}

}
