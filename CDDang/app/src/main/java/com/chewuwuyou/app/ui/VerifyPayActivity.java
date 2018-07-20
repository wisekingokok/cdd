package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ScreenUtils;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * 忘记支付密码验证密保问题
 * 
 * @author zengys
 * 
 */
@SuppressLint("HandlerLeak")
public class VerifyPayActivity extends CDDBaseActivity implements
		OnClickListener, OnLayoutChangeListener {

	private Dialog mDialogSubmit;
	// private Timer mTimer;
	private TextView mTitleTV;// 标题
	private ImageButton mBackIBtn;// 返回上一页
	private Button mSetProblenSubmit;// 提交
	private TextView mQuestionOneTV;// 问题一
	private EditText mAnswerOneET;// 问题一答案
	private TextView mQuestionTwoTV;// 问题二
	private EditText mAnswerTwoET;// 问题二答案
	private LinearLayout mKeyboardLayout;

	// 屏幕高度
	private int screenHeight = 0;
	// 软件盘弹起后所占高度阀值
	private int keyHeight = 0;

	private String mProblemOneName;// 问题一名称
	private String mProblemTwoName;// 问题二名称
	private String mProblemOne;// 问题一
	private String mProblemTwo;// 问题二
	private RelativeLayout mTitleHeight;// 标题布局高度
	public static VerifyPayActivity mUpdatePayPasswordActivity = null;
	private TextView mRefreshTV;// 刷新重新获得密保问题
	Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mDialogSubmit.dismiss();
				getProblem();

				break;
			}
			super.handleMessage(msg);
		}

	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verifypay_layout);
		mUpdatePayPasswordActivity = this;
		initView();
		initEvent();
		initData();

	}

	/*
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);// 标题
		mTitleTV.setText("验证保密问题");
		mRefreshTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
		mRefreshTV.setText("刷新");
		mRefreshTV.setVisibility(View.VISIBLE);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mSetProblenSubmit = (Button) findViewById(R.id.set_problen_submit);
		mDialogSubmit = DialogUtil.wrongPayDialog(VerifyPayActivity.this);
		// mTimer = new Timer(true);

		mQuestionOneTV = (TextView) findViewById(R.id.prompt_yi);
		mAnswerOneET = (EditText) findViewById(R.id.editText1);
		mQuestionTwoTV = (TextView) findViewById(R.id.prompt_er);
		mAnswerTwoET = (EditText) findViewById(R.id.edit_answer_two);
		mKeyboardLayout = (LinearLayout) findViewById(R.id.keyboardLayout);

		// 获取屏幕高度
		screenHeight = ScreenUtils.getScreenHeight(VerifyPayActivity.this);

		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_problen_submit:

			if (mQuestionOneTV.getText().toString().equals("请选择问题")) {
				ToastUtil.toastShow(VerifyPayActivity.this, "信息显示错误");
			} else if (mQuestionTwoTV.getText().toString().equals("请选择问题")) {
				ToastUtil.toastShow(VerifyPayActivity.this, "信息显示错误");
			} else if (TextUtils.isEmpty(mAnswerOneET.getText().toString())) {
				ToastUtil.toastShow(VerifyPayActivity.this, "请输入答案");
			} else if (TextUtils.isEmpty(mAnswerTwoET.getText().toString())) {
				ToastUtil.toastShow(VerifyPayActivity.this, "请输入答案");
			} else {
				setUpMyProblem();
			}
			break;
		case R.id.sub_header_bar_left_ibtn:// 返回上一页
			finishActivity();
			break;
		case R.id.sub_header_bar_right_tv:
			getProblem();
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		getProblem();
	}

	/**
	 * 获得密保问题
	 */
	private void getProblem() {
		// TODO Auto-generated method stub

		// 获得密保问题
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					MyLog.i("YUY", "密保问题 = " + msg.obj.toString());
					// [{"securityQuestion2":"您最喜欢的车是？"},{"securityQuestion3":"您的第一个电话号码是?"}]

					try {
						JSONArray jArray = new JSONArray(msg.obj.toString());
						mProblemOneName = jArray.getJSONObject(0).toString()
								.substring(2, 19);
						mProblemTwoName = jArray.getJSONObject(1).toString()
								.substring(2, 19);
						MyLog.i("YUY", "问题一 = " + mProblemOneName + " 问题二 = "
								+ mProblemTwoName);
						mProblemOne = jArray.getJSONObject(0).getString(
								mProblemOneName);
						mProblemTwo = jArray.getJSONObject(1).getString(
								mProblemTwoName);
						mQuestionOneTV.setText(mProblemOne);
						mQuestionTwoTV.setText(mProblemTwo);
						mAnswerOneET.setText("");
						mAnswerTwoET.setText("");
						mAnswerOneET.requestFocus();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		}, null, NetworkUtil.TWO_QUESTION, false, 0);

	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		mSetProblenSubmit.setOnClickListener(this);
		mRefreshTV.setOnClickListener(this);
	}

	private void timeShowDialog() {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);// 告诉主线程执行任务
			}
		}).start();
	}

	private void Submit() {
		if (TextUtils.isEmpty(mAnswerOneET.getText().toString().trim())
				|| TextUtils.isEmpty(mAnswerTwoET.getText().toString().trim())) {
			mSetProblenSubmit.setEnabled(false);
			// mSetProblenSubmit.setClickable(false);
			mSetProblenSubmit.getBackground().setAlpha(100);
		} else {
			// mSetProblenSubmit.setClickable(false);
			mSetProblenSubmit.setEnabled(true);
			mSetProblenSubmit.getBackground().setAlpha(255);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 添加layout大小发生改变监听器
		mKeyboardLayout.addOnLayoutChangeListener(this);
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right,
			int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		// TODO Auto-generated method stub
		// old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

		// System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " +
		// oldBottom);
		// System.out.println(left + " " + top +" " + right + " " + bottom);

		// 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

		} else if (oldBottom != 0 && bottom != 0
				&& (bottom - oldBottom > keyHeight)) {

			Submit();
		}
	}

	private void setUpMyProblem() {

		if (!TextUtils.isEmpty(mAnswerOneET.getText().toString().trim())
				&& !TextUtils.isEmpty(mAnswerTwoET.getText().toString().trim())) {
			AjaxParams params = new AjaxParams();
			params.put(mProblemOneName, mProblemOne);
			// 获取密保问题对应的密保答案名称securityQuestion2---securityAnswer2
			String sercurityAnswerNameOne = "securityAnswer"
					+ mProblemOneName.substring(mProblemOneName.length() - 1,
							mProblemOneName.length());
			params.put(sercurityAnswerNameOne, mAnswerOneET.getText()
					.toString().trim());
			params.put(mProblemTwoName, mProblemTwo);
			String sercurityAnswerNameTwo = "securityAnswer"
					+ mProblemTwoName.substring(mProblemTwoName.length() - 1,
							mProblemTwoName.length());
			params.put(sercurityAnswerNameTwo, mAnswerTwoET.getText()
					.toString().trim());
			requestNet(new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:
						MyLog.i("YUY", "验证密保信息 = " + msg.obj.toString());
						try {
							JSONObject jo = new JSONObject(msg.obj.toString());
							if (jo.getString("re").equals("true")) {
								Intent intent = new Intent(
										VerifyPayActivity.this,
										SetPassWord.class);
								// intent.putExtra("isUpdate", 1);// 判断是设置新密码
								intent.putExtra("clickType",Constant.CLICK_FORGET_PAYPASS);// 传递是忘记密码
								intent.putExtra("Identification",getIntent().getIntExtra("Identification", 0));// 传递是忘记密码
								startActivity(intent);
								finishActivity();
							} else {
								// 密保问题验证不正确 这里是否需要重新获取随机密保问题
								mDialogSubmit.show();
								timeShowDialog();
								// ToastUtil.toastShow(VerifyPayActivity.this,
								// "密保问题回答不正确");
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						break;
					case Constant.NET_DATA_FAIL:
						ToastUtil.toastShow(VerifyPayActivity.this,
								((DataError) msg.obj).getErrorMessage());
						break;
					default:
						break;
					}
				}
			}, params, NetworkUtil.FORGET_PAY_PASSWORD, false, 1);
		} else {
			ToastUtil.toastShow(VerifyPayActivity.this, "请输入完整密保信息");

		}

	}

}
