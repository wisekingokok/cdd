package com.chewuwuyou.app.ui;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.tools.ZysUtilsBtn;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

public class PayWordVerifyActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitle;// 标题,关闭
	private TextView mkeyboard_oneYi, mkeyboard_oneEr, mkeyboard_oneSan, mkeyboard_oneSi,
			mkeyboard_oneWu, mkeyboard_oneLiu, mkeyboard_oneQi, mkeyboard_oneBa, mkeyboard_oneJiu,
			mkeyboard_oneShi, mkeyboard_oneShiYi, mkeyboard_oneShiEr;
	private EditText mEditPsYi, mEditPsEr, mEditPsSan, mEditPsSi, mEditPsWu,
			mEditPsLiu;
	// private ImageView mImgPsYi, mImgPsEr, mImgPsSan, mImgPsSi, mImgPsWu,
	// mImgPsLiu;
	private ImageButton mSubHeaderBarLeftIbtn;
	private ArrayList<String> list;// 密码集合
	private TextView passwordConsistent;
	// private String mPayWord, mPage;
	// private LayoutInflater mLinearLayout;// 加載dialog佈局
	// private TextView mDialogTetil, mDialogContent;// 标题，内容;
	// private Button mWsCancel, mWsConfirm;// 取消,确认
	// private Dialog mDdialog;// 关闭提示框
	public static PayWordVerifyActivity mUpdatePayPasswordActivity = null;
	public int IS_UPDATE_PASSWORD = 1;

	private TextView mForgetPayPassTV;// 忘记支付密码，在修改密码时显示
	// prompt_input_ps_tv
	private TextView mPromptInputTV;// 输入密码描述一
	// prompt_six_ps_tv
	private TextView mPromptSixTV;// 输入密码描述二
	private RelativeLayout mTitleHeight;// 标题布局高度
	private Bundle bundle;
	private int Identification;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_password);
		mUpdatePayPasswordActivity = this;
		initView();// 初始化控件
		initEvent();// 点击事件
		bundle = new Bundle();
		bundle = this.getIntent().getExtras();
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);// 返回上一页
		passwordConsistent = (TextView) findViewById(R.id.password_consistent);// 主题显示
		mPromptInputTV = (TextView) findViewById(R.id.prompt_input_ps_tv);
		mPromptSixTV = (TextView) findViewById(R.id.prompt_six_ps_tv);
		mPromptSixTV.setVisibility(View.GONE);
		mPromptInputTV.setText("请输入您的密码");
		mTitle = (TextView) findViewById(R.id.sub_header_bar_tv);// 主题显示
		mTitle.setText("输入密码");
		mForgetPayPassTV = (TextView) findViewById(R.id.forget_paypass_tv);
		mForgetPayPassTV.setVisibility(View.VISIBLE);
		mForgetPayPassTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		list = new ArrayList<String>();
		mEditPsYi = (EditText) findViewById(R.id.edit_ps_yi);
		mEditPsEr = (EditText) findViewById(R.id.edit_ps_er);
		mEditPsSan = (EditText) findViewById(R.id.edit_ps_san);
		mEditPsSi = (EditText) findViewById(R.id.edit_ps_si);
		mEditPsWu = (EditText) findViewById(R.id.edit_ps_wu);
		mEditPsLiu = (EditText) findViewById(R.id.edit_ps_liu);

		// mImgPsYi = (ImageView) findViewById(R.id.img_ps_yi);
		// mImgPsEr = (ImageView) findViewById(R.id.img_ps_er);
		// mImgPsSan = (ImageView) findViewById(R.id.img_ps_san);
		// mImgPsSi = (ImageView) findViewById(R.id.img_ps_si);
		// mImgPsWu = (ImageView) findViewById(R.id.img_ps_wu);
		// mImgPsLiu = (ImageView) findViewById(R.id.img_ps_liu);

		mkeyboard_oneYi = (TextView) findViewById(R.id.keyboard_one_yi);
		mkeyboard_oneEr = (TextView) findViewById(R.id.keyboard_one_er);
		mkeyboard_oneSan = (TextView) findViewById(R.id.keyboard_one_san);
		mkeyboard_oneSi = (TextView) findViewById(R.id.keyboard_one_si);
		mkeyboard_oneWu = (TextView) findViewById(R.id.keyboard_one_wu);
		mkeyboard_oneLiu = (TextView) findViewById(R.id.keyboard_one_liu);
		mkeyboard_oneQi = (TextView) findViewById(R.id.keyboard_one_qi);
		mkeyboard_oneBa = (TextView) findViewById(R.id.keyboard_one_ba);
		mkeyboard_oneJiu = (TextView) findViewById(R.id.keyboard_one_jiu);
		mkeyboard_oneShi = (TextView) findViewById(R.id.keyboard_one_reset);
		mkeyboard_oneShiYi = (TextView) findViewById(R.id.keyboard_one_ning);
		mkeyboard_oneShiEr = (TextView) findViewById(R.id.keyboard_one_delete);
		passwordConsistent.setVisibility(View.GONE);// 兩次輸入密码不相符，请重新输入不显示
		// Intent intent = getIntent();
		// mPayWord = intent.getStringExtra("payWord").toString();
		//
		// if (intent.getStringExtra("again").equals("1")) {
		// passwordConsistent.setVisibility(View.VISIBLE);
		// } else {
		// passwordConsistent.setVisibility(View.GONE);
		// }

	}

	@Override
	protected void onResume() {
		super.onResume();
		list.clear();// 清空
		Reset();// 重置
	}

	/**
	 * 点击事件
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一个页面
			// mLinearLayout =
			// LayoutInflater.from(UpdatePayPasswordActivity.this);
			// View myLoginView = mLinearLayout.inflate(
			// R.layout.withdrawals_close_dialog, null);
			// mDdialog = new
			// AlertDialog.Builder(UpdatePayPasswordActivity.this)
			// .create();
			// mDdialog.setCancelable(false);// 点击其他空白区域不关闭
			// mDdialog.show();
			// mDdialog.getWindow().setContentView(myLoginView);
			// mDialogTetil = (TextView) myLoginView
			// .findViewById(R.id.ws_dialog_titel);
			// mDialogContent = (TextView) myLoginView
			// .findViewById(R.id.ws_dialog_context);
			// mWsCancel = (Button) myLoginView.findViewById(R.id.ws_cancel);//
			// 取消
			// mWsConfirm = (Button)
			// myLoginView.findViewById(R.id.ws_confirm);// 确认
			// mWsCancel.setOnClickListener(this);// dialog取消
			// mWsConfirm.setOnClickListener(this);// dialog确认
			// mDialogTetil.setText("确定要放弃支付密码的创建吗？");
			// mDialogContent.setText("为了你的账户安全，建议你设置支付密码");
			finishActivity();
			break;
		case R.id.forget_pay_password_tv:// 跳转至忘记支付密码
			intent = new Intent(PayWordVerifyActivity.this,
					VerifyPayActivity.class);
			startActivity(intent);
			finishActivity();
			break;
		// case R.id.ws_cancel:// 取消
		// break;
		// case R.id.ws_confirm:// 确认
		// finishActivity();
		// break;
		case R.id.keyboard_one_yi:
			if (list.size() < 6) {
				list.add("1");
				HidePassWord();
			}
			if (list.size() == 6) {
				if (ZysUtilsBtn.isFastClick()) {
					return;
				}
				toClass();
			}
			break;
		case R.id.keyboard_one_er:
			if (list.size() < 6) {
				list.add("2");
				HidePassWord();
			}
			if (list.size() == 6) {
				if (ZysUtilsBtn.isFastClick()) {
					return;
				}
				toClass();

			}
			break;
		case R.id.keyboard_one_san:
			if (list.size() < 6) {
				list.add("3");
				HidePassWord();
			}
			if (list.size() == 6) {
				if (ZysUtilsBtn.isFastClick()) {
					return;
				}
				toClass();
			}

			break;
		case R.id.keyboard_one_si:
			if (list.size() < 6) {
				list.add("4");
				HidePassWord();
			}
			if (list.size() == 6) {
				if (ZysUtilsBtn.isFastClick()) {
					return;
				}
				toClass();
			}

			break;
		case R.id.keyboard_one_wu:
			if (list.size() < 6) {
				list.add("5");
				HidePassWord();
			}
			if (list.size() == 6) {
				if (ZysUtilsBtn.isFastClick()) {
					return;
				}
				toClass();
			}

			break;
		case R.id.keyboard_one_liu:
			if (list.size() < 6) {
				list.add("6");
				HidePassWord();
			}
			if (list.size() == 6) {
				HidePassWord();
				if (ZysUtilsBtn.isFastClick()) {
					return;
				}
				toClass();
			}

			break;
		case R.id.keyboard_one_qi:

			if (list.size() < 6) {
				list.add("7");
				HidePassWord();
			}
			if (list.size() == 6) {
				if (ZysUtilsBtn.isFastClick()) {
					return;
				}
				toClass();
			}

			break;
		case R.id.keyboard_one_ba:
			if (list.size() < 6) {
				list.add("8");
				HidePassWord();
			}
			if (list.size() == 6) {
				if (ZysUtilsBtn.isFastClick()) {
					return;
				}
				toClass();
			}

			break;
		case R.id.keyboard_one_jiu:
			if (list.size() < 6) {
				list.add("9");
				if (ZysUtilsBtn.isFastClick()) {
					return;
				}
				HidePassWord();
			}
			if (list.size() == 6) {
				toClass();
			}
			break;
		case R.id.keyboard_one_reset:
			Reset();
			break;
		case R.id.keyboard_one_ning:
			if (list.size() < 6) {
				list.add("0");
				HidePassWord();
			}
			if (list.size() == 6) {
				if (ZysUtilsBtn.isFastClick()) {
					return;
				}
				toClass();
			}
			break;
		case R.id.keyboard_one_delete:
			DeletePassWord();
			break;
		case R.id.forget_paypass_tv:
			Identification = Constant.CLICK_FORGET_PAYPASS;
			intent = new Intent(PayWordVerifyActivity.this,VerifyPayActivity.class);
			intent.putExtra("Identification", Identification);
			intent.putExtra("clilckType", Constant.CLICK_FORGET_PAYPASS);
			startActivity(intent);
			finishActivity();
			break;
		default:
			break;
		}
	}

	/**
	 * 业务逻辑处理
	 */
	@Override
	protected void initData() {

	}

	/**
	 * 跳转
	 */
	private void toClass() {
		String listName = list.get(0) + list.get(1) + list.get(2) + list.get(3)
				+ list.get(4) + list.get(5);
		// 传入旧的支付密码验证是否正确
		AjaxParams params = new AjaxParams();
		params.put("payPass", MD5Util.getMD5(listName));
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					MyLog.i("YUY", "设置密码是否成功 = " + msg.obj);
					// 返回re false设置失败 true设置成功
					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						if (jo.getString("re").equals("true")) {
							Intent intent = new Intent();
							// int mWithDrawNum = 0;
							int mWithDrawNum = bundle.getInt("WithDrawNum");
							MyLog.i("yang", mWithDrawNum + "");
							// 在使用钱包支付时 跳转到本页面 设置完成后 依然跳转到支付界面进行支付
							if (mWithDrawNum > 0) {// 直接跳转提现账户列表页面

								intent.setClass(PayWordVerifyActivity.this,
										CccountWithdrawalsActivity.class);

							} else {
								intent.setClass(PayWordVerifyActivity.this,
										AddCcountWithdrawalsActivity.class);
							}
							startActivity(intent);
							finishActivity();
						} else {
							ToastUtil.toastShow(PayWordVerifyActivity.this,
									"旧密码不正确");
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;
				case Constant.NET_DATA_FAIL:
					if (((DataError) msg.obj).getErrorMessage().equals(
							"支付密码输入错误,你的账户已被冻结")) {
						Intent intent = new Intent(PayWordVerifyActivity.this,
								AccountBlockingActivity.class);
						startActivity(intent);
						finishActivity();
						return;
					}
					ToastUtil.toastShow(PayWordVerifyActivity.this,
							((DataError) msg.obj).getErrorMessage());
					Reset();
					break;
				default:
					break;
				}
			}
		}, params, NetworkUtil.JUDGE_PAY_PASSWORD, false, 1);

	}

	/**
	 * 添加
	 */
	public void HidePassWord() {
		if (list.size() == 1) {
			mEditPsYi.setVisibility(View.GONE);
		} else if (list.size() == 2) {
			mEditPsEr.setVisibility(View.GONE);
		} else if (list.size() == 3) {
			mEditPsSan.setVisibility(View.GONE);
		} else if (list.size() == 4) {
			mEditPsSi.setVisibility(View.GONE);
		} else if (list.size() == 5) {
			mEditPsWu.setVisibility(View.GONE);
		} else if (list.size() == 6) {
			mEditPsLiu.setVisibility(View.GONE);
		}
	}

	/**
	 * 减少
	 */
	public void DeletePassWord() {

		if (list.size() == 1) {
			list.remove(0);
			mEditPsYi.setVisibility(View.VISIBLE);
		} else if (list.size() == 2) {
			list.remove(1);
			mEditPsEr.setVisibility(View.VISIBLE);
		} else if (list.size() == 3) {
			list.remove(2);
			mEditPsSan.setVisibility(View.VISIBLE);
		} else if (list.size() == 4) {
			list.remove(3);
			mEditPsSi.setVisibility(View.VISIBLE);
		} else if (list.size() == 5) {
			list.remove(4);
			mEditPsWu.setVisibility(View.VISIBLE);
		} else if (list.size() == 6) {
			list.remove(5);
			mEditPsLiu.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 重置
	 */
	public void Reset() {
		list.clear();// 清空集合数据
		mEditPsYi.setVisibility(View.VISIBLE);
		mEditPsEr.setVisibility(View.VISIBLE);
		mEditPsSan.setVisibility(View.VISIBLE);
		mEditPsSi.setVisibility(View.VISIBLE);
		mEditPsWu.setVisibility(View.VISIBLE);
		mEditPsLiu.setVisibility(View.VISIBLE);
	}

	/**
	 * 监听事件
	 */
	@Override
	protected void initEvent() {
		mkeyboard_oneYi.setOnClickListener(this);
		mkeyboard_oneEr.setOnClickListener(this);
		mkeyboard_oneSan.setOnClickListener(this);
		mkeyboard_oneSi.setOnClickListener(this);
		mkeyboard_oneWu.setOnClickListener(this);
		mkeyboard_oneLiu.setOnClickListener(this);
		mkeyboard_oneQi.setOnClickListener(this);
		mkeyboard_oneBa.setOnClickListener(this);
		mkeyboard_oneJiu.setOnClickListener(this);
		mkeyboard_oneShi.setOnClickListener(this);
		mkeyboard_oneShiYi.setOnClickListener(this);
		mkeyboard_oneShiEr.setOnClickListener(this);
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
		mForgetPayPassTV.setOnClickListener(this);

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
			return true;
		}
		return false;
	}
}
