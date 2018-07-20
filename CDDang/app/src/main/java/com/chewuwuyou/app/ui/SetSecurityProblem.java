package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @describe:设置密保问题
 * @author:liuchun
 */
public class SetSecurityProblem extends CDDBaseActivity implements
		android.view.View.OnClickListener {

	private TextView mPromptYi, mPromptEr, mPromptSan;
	private TextView setSecuritTitle;// 标题
	private ImageButton mSubHeaderBarLeftIbtn;
	private LayoutInflater mLinearLayout;// 加載dialog佈局
	private TextView mDialogTetil;// 标题，内容;
	private Button mWsCancel, mWsConfirm;// 取消,确认
	private Dialog mDdialog;// 关闭提示框
	private EditText mProblemEdYi, mProblemEdEr, mProblemEdSan;
	private Button mProblenSubmit;// 提交
	private String passWord;
	private RelativeLayout mTitleHeight;// 标题布局高度
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_securit_problem);
		initView();// 初始化控件
		initData();
		initEvent();// 点击事件
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {

		// 注册EventBus
//		EventBus.getDefault().register(this);
		mPromptYi = (TextView) findViewById(R.id.prompt_yi);
		mPromptEr = (TextView) findViewById(R.id.prompt_er);
		mPromptSan = (TextView) findViewById(R.id.prompt_san);
		setSecuritTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mProblemEdYi = (EditText) findViewById(R.id.problem_ed_yi);
		mProblemEdEr = (EditText) findViewById(R.id.problem_ed_er);
		mProblemEdSan = (EditText) findViewById(R.id.problem_ed_san);
		mProblenSubmit = (Button) findViewById(R.id.set_problen_submit);// 提交
	}

/*	*//**
	 * EventBus接收传递的数据
	 * 
	 * @param busAdapter
	 *//*
	 public void onEventMainThread(EventBusAdapter busAdapter) {
	    ispage = busAdapter.getIsPage();
	 }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);// 反注册EventBus
	}*/

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		setSecuritTitle.setText("设置密保");
		Intent intent = getIntent();
		passWord = intent.getStringExtra("passList");
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initEvent() {
		mPromptYi.setOnClickListener(this);
		mPromptEr.setOnClickListener(this);
		mPromptSan.setOnClickListener(this);
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
		mProblenSubmit.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			switch (requestCode) {
			case 1:
				mPromptYi.setText(data.getStringExtra("problem"));
				break;
			case 2:
				mPromptEr.setText(data.getStringExtra("problem"));
				break;
			case 3:
				mPromptSan.setText(data.getStringExtra("problem"));
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一页

			mLinearLayout = LayoutInflater.from(SetSecurityProblem.this);
			View myLoginView = mLinearLayout.inflate(
					R.layout.withdrawals_close_dialog, null);
			mDdialog = new AlertDialog.Builder(SetSecurityProblem.this)
					.create();
			mDdialog.setCancelable(false);// 点击其他空白区域不关闭
			mDdialog.show();
			mDdialog.getWindow().setContentView(myLoginView);
			mDialogTetil = (TextView) myLoginView
					.findViewById(R.id.ws_dialog_titel);
			mWsCancel = (Button) myLoginView.findViewById(R.id.ws_cancel);// 取消
			mWsConfirm = (Button) myLoginView.findViewById(R.id.ws_confirm);// 确认
			mWsCancel.setOnClickListener(this);// dialog取消
			mWsConfirm.setOnClickListener(this);// dialog确认
			mDialogTetil.setText("为了你的账户安全，建议你设置支付密码,如果选择放弃,你之前的操作将无效");
			break;
		case R.id.ws_cancel:// 取消
			mDdialog.dismiss();
			break;
		case R.id.ws_confirm:// 确认
			finishActivity();
			break;
		case R.id.set_problen_submit:// 提交

			if (!mPromptYi.getText().toString().trim().equals("请选择问题")
					&& !mPromptEr.getText().toString().trim().equals("请选择问题")
					&& !mPromptSan.getText().toString().trim().equals("请选择问题")
					&& !TextUtils.isEmpty(mProblemEdYi.getText().toString()
							.trim())
					&& !TextUtils.isEmpty(mProblemEdEr.getText().toString()
							.trim())
					&& !TextUtils.isEmpty(mProblemEdSan.getText().toString()
							.trim())) {
				MyLog.i("YUY", "设置密码参数 = " + passWord + " = "
						+ mPromptYi.getText().toString().trim() + " = "
						+ mProblemEdYi.getText().toString().trim() + " = "
						+ mPromptEr.getText().toString().trim() + " = "
						+ mProblemEdEr.getText().toString().trim() + " = "
						+ mPromptSan.getText().toString().trim() + " = "
						+ mProblemEdSan.getText().toString().trim());
				AjaxParams params = new AjaxParams();
				params.put("payPass", MD5Util.getMD5(passWord));// 支付密码缺MD5加密
				params.put("securityQuestion1", mPromptYi.getText().toString()
						.trim());
				params.put("securityAnswer1", mProblemEdYi.getText().toString()
						.trim());
				params.put("securityQuestion2", mPromptEr.getText().toString()
						.trim());
				params.put("securityAnswer2", mProblemEdEr.getText().toString()
						.trim());
				params.put("securityQuestion3", mPromptSan.getText().toString()
						.trim());
				params.put("securityAnswer3", mProblemEdSan.getText()
						.toString().trim());
				getPaymentPassword(params);

			} else {
				ToastUtil.toastShow(SetSecurityProblem.this, "请完善密保问题");
			}

			/*
			 * abuilder = new AlertDialog.Builder(this); abuilder.setTitle("");
			 * abuilder.setMessage("设置密码成功，是否立即支付");
			 * abuilder.setPositiveButton(R.string.confirm, new
			 * OnClickListener() {
			 * 
			 * public void onClick(DialogInterface dialog, int which) { Intent
			 * intent = new Intent(SetSecurityProblem.this, SetPassWord.class);
			 * startActivity(intent); } });
			 * abuilder.setNegativeButton(R.string.cancel, null);
			 * abuilder.create().show();
			 */
			/* mDialogSubmit.show(); */
			break;
		case R.id.prompt_yi:
			intent = new Intent(SetSecurityProblem.this,
					ChoiceProblemActivity.class);
			intent.putExtra("Promptyi", mPromptYi.getText().toString().trim());
			intent.putExtra("Prompter", mPromptEr.getText().toString().trim());
			intent.putExtra("Promptsan", mPromptSan.getText().toString().trim());
			startActivityForResult(intent, 1);
			break;
		case R.id.prompt_er:
			intent = new Intent(SetSecurityProblem.this,
					ChoiceProblemActivity.class);
			intent.putExtra("Promptyi", mPromptYi.getText().toString().trim());
			intent.putExtra("Prompter", mPromptEr.getText().toString().trim());
			intent.putExtra("Promptsan", mPromptSan.getText().toString().trim());
			startActivityForResult(intent, 2);
			break;
		case R.id.prompt_san:
			intent = new Intent(SetSecurityProblem.this,
					ChoiceProblemActivity.class);
			intent.putExtra("Promptyi", mPromptYi.getText().toString().trim());
			intent.putExtra("Prompter", mPromptEr.getText().toString().trim());
			intent.putExtra("Promptsan", mPromptSan.getText().toString().trim());
			startActivityForResult(intent, 3);
			break;
		default:
			break;
		}
	}

	/**
	 * 访问网络提交问题以及支付密码
	 */
	private void getPaymentPassword(AjaxParams params) {

		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					// 返回re false设置失败 true设置成功
					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						if (jo.getString("re").equals("true")) {
							// 在使用钱包支付时 跳转到本页面 设置完成后 依然跳转到支付界面进行支付
							ToastUtil.toastShow(SetSecurityProblem.this,
									"支付密码设置成功");
							Constant.IS_SET_PAYPASS = 1;// 标识支付密码设置成功
 
							Intent intent;
							if (Constant.IS_PAGE == 1) {
								SetPassWord.msetPassword.finishActivity();
								ConfirmPawwWordActivity.mConfir.finishActivity();
							} else if(getIntent().getIntExtra("Identification", 0) == Constant.CLICK_TIXIAN_ACCOUNT){//提现账户
								intent = new Intent(SetSecurityProblem.this,AddCcountWithdrawalsActivity.class);
								intent.putExtra("Identification", getIntent().getIntExtra("Identification", 0));
								startActivity(intent);
								SetPassWord.msetPassword.finishActivity();
								ConfirmPawwWordActivity.mConfir.finishActivity();
							}else if(getIntent().getIntExtra("Identification", 0) == Constant.CLICK_SAFE_SETTING){//安全设置
								intent = new Intent(SetSecurityProblem.this,PayPasswordManagerActivity.class);
								startActivity(intent);
								SetPassWord.msetPassword.finishActivity();
								ConfirmPawwWordActivity.mConfir.finishActivity();
							}else if(getIntent().getIntExtra("Identification", 0) == Constant.CLICK_BALANCE_TIXIAN){
								intent = new Intent(SetSecurityProblem.this,AddCcountWithdrawalsActivity.class);
								intent.putExtra("Identification", getIntent().getIntExtra("Identification", 0));
								startActivity(intent);
								SetPassWord.msetPassword.finishActivity();
								ConfirmPawwWordActivity.mConfir.finishActivity();
							}
							finishActivity();
						} else {
							ToastUtil.toastShow(SetSecurityProblem.this,
									"支付密码设置失败");
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}

			}
		}, params, NetworkUtil.SUBMIT_PAYMENT_PASSWORD, false, 0);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mLinearLayout = LayoutInflater.from(SetSecurityProblem.this);
			View myLoginView = mLinearLayout.inflate(
					R.layout.withdrawals_close_dialog, null);
			mDdialog = new AlertDialog.Builder(SetSecurityProblem.this)
					.create();
			mDdialog.setCancelable(false);// 点击其他空白区域不关闭
			mDdialog.show();
			mDdialog.getWindow().setContentView(myLoginView);
			mDialogTetil = (TextView) myLoginView
					.findViewById(R.id.ws_dialog_titel);
			mWsCancel = (Button) myLoginView.findViewById(R.id.ws_cancel);// 取消
			mWsConfirm = (Button) myLoginView.findViewById(R.id.ws_confirm);// 确认
			mWsCancel.setOnClickListener(this);// dialog取消
			mWsConfirm.setOnClickListener(this);// dialog确认
			mDialogTetil.setText("为了你的账户安全，建议你设置支付密码,如果选择放弃,你之前的操作将无效");
			return true;
		}
		return false;
	}
}
