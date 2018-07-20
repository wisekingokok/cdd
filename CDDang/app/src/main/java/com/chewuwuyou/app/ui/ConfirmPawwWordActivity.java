package com.chewuwuyou.app.ui;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @describe:确认密码
 * @author:刘春
 */
public class ConfirmPawwWordActivity extends CDDBaseActivity implements
		android.view.View.OnClickListener {

	private TextView confirTitle;
	private TextView mKeyboardYi, mKeyboardEr, mKeyboardSan, mKeyboardSi,
			mKeyboardWu, mKeyboardLiu, mKeyboardQi, mKeyboardBa, mKeyboardJiu,
			mKeyboardShi, mKeyboardShiYi, mKeyboardShiEr;

	private EditText mConfirmEditPsYi, mConfirmEditPsEr, mConfirmEditPsSan,
			mConfirmEditPsSi, mConfirmEditPsWu, mConfirmEditPsLiu;
	// private ImageView mConfirmImgpsYi, mConfirmImgpsEr, mConfirmImgpsSan,
	// mConfirmImgpsSi, mConfirmImgpsWu, mConfirmImgpsLiu;
	// private String paww;
	private ArrayList<String> mlist;
	private ArrayList<String> list;
	// private AlertDialog.Builder abuilder;
	private Dialog mDialogSubmit;
	// private Timer timer;
	// private String mPayExit;
	private ImageButton mSubHeaderBarLeftIbtn;
	// private LayoutInflater mLinearLayout;// 加載dialog佈局
	// private TextView mDialogTetil, mDialogContent;// 标题，内容;
	// private Button mWsCancel, mWsConfirm;// 取消,确认
	// private Dialog mDdialog;// 关闭提示框
	// private static ConfirmPawwWordActivity mConfir;
	private int mClickType;
	private RelativeLayout mTitleHeight;// 标题布局高度
	public static ConfirmPawwWordActivity mConfir;
	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mDialogSubmit.dismiss();
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_password);
		mConfir = this;
		initView();// 初始化控件
		initEvent();// 点击事件

	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mClickType = getIntent().getIntExtra("clickType", 0);
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);// 返回上一页

		confirTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		if (getIntent().getIntExtra("isUpdate", 0) == 1) {
			confirTitle.setText("设置新密码");
		} else {
			confirTitle.setText("设置支付密码");
		}

		mConfirmEditPsYi = (EditText) findViewById(R.id.confirm_edit_ps_yi);
		mConfirmEditPsEr = (EditText) findViewById(R.id.confirm_edit_ps_er);
		mConfirmEditPsSan = (EditText) findViewById(R.id.confirm_edit_ps_san);
		mConfirmEditPsSi = (EditText) findViewById(R.id.confirm_edit_ps_si);
		mConfirmEditPsWu = (EditText) findViewById(R.id.confirm_edit_ps_wu);
		mConfirmEditPsLiu = (EditText) findViewById(R.id.confirm_edit_ps_liu);
		//
		// mConfirmImgpsYi = (ImageView) findViewById(R.id.confirm_img_ps_yi);
		// mConfirmImgpsEr = (ImageView) findViewById(R.id.confirm_img_ps_er);
		// mConfirmImgpsSan = (ImageView) findViewById(R.id.confirm_img_ps_san);
		// mConfirmImgpsSi = (ImageView) findViewById(R.id.confirm_img_ps_si);
		// mConfirmImgpsWu = (ImageView) findViewById(R.id.confirm_img_ps_wu);
		// mConfirmImgpsLiu = (ImageView) findViewById(R.id.confirm_img_ps_liu);

		mKeyboardYi = (TextView) findViewById(R.id.confirm_keyboard_yi);
		mKeyboardEr = (TextView) findViewById(R.id.confirm_keyboard_er);
		mKeyboardSan = (TextView) findViewById(R.id.confirm_keyboard_san);
		mKeyboardSi = (TextView) findViewById(R.id.confirm_keyboard_si);
		mKeyboardWu = (TextView) findViewById(R.id.confirm_keyboard_wu);
		mKeyboardLiu = (TextView) findViewById(R.id.confirm_keyboard_liu);
		mKeyboardQi = (TextView) findViewById(R.id.confirm_keyboard_qi);
		mKeyboardBa = (TextView) findViewById(R.id.confirm_keyboard_ba);
		mKeyboardJiu = (TextView) findViewById(R.id.confirm_keyboard_jiu);
		mKeyboardShi = (TextView) findViewById(R.id.confirm_keyboard_reset);
		mKeyboardShiYi = (TextView) findViewById(R.id.confirm_keyboard_ning);
		mKeyboardShiEr = (TextView) findViewById(R.id.confirm_keyboard_delete);

		Intent intent = getIntent();
		mlist = intent.getStringArrayListExtra("passList");
		// mPayExit = intent.getStringExtra("passExit");
		list = new ArrayList<String>();

		// mDialogSubmit =
		// DialogUtil.submitDialog(ConfirmPawwWordActivity.this);
		// timer = new Timer(true);

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initEvent() {
		mKeyboardYi.setOnClickListener(this);
		mKeyboardEr.setOnClickListener(this);
		mKeyboardSan.setOnClickListener(this);
		mKeyboardSi.setOnClickListener(this);
		mKeyboardWu.setOnClickListener(this);
		mKeyboardLiu.setOnClickListener(this);
		mKeyboardQi.setOnClickListener(this);
		mKeyboardBa.setOnClickListener(this);
		mKeyboardJiu.setOnClickListener(this);
		mKeyboardShi.setOnClickListener(this);
		mKeyboardShiYi.setOnClickListener(this);
		mKeyboardShiEr.setOnClickListener(this);
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
	}

	/**
	 * 添加
	 */
	public void pubss() {
		if (list.size() == 1) {
			mConfirmEditPsYi.setVisibility(View.GONE);
		} else if (list.size() == 2) {
			mConfirmEditPsEr.setVisibility(View.GONE);
		} else if (list.size() == 3) {
			mConfirmEditPsSan.setVisibility(View.GONE);
		} else if (list.size() == 4) {
			mConfirmEditPsSi.setVisibility(View.GONE);
		} else if (list.size() == 5) {
			mConfirmEditPsWu.setVisibility(View.GONE);
		} else if (list.size() == 6) {
			mConfirmEditPsLiu.setVisibility(View.GONE);
		}
	}

	/**
	 * 减少
	 */
	public void isfj() {

		if (list.size() == 1) {
			list.remove(0);
			mConfirmEditPsYi.setVisibility(View.VISIBLE);
		} else if (list.size() == 2) {
			list.remove(1);
			mConfirmEditPsEr.setVisibility(View.VISIBLE);
		} else if (list.size() == 3) {
			list.remove(2);
			mConfirmEditPsSan.setVisibility(View.VISIBLE);
		} else if (list.size() == 4) {
			list.remove(3);
			mConfirmEditPsSi.setVisibility(View.VISIBLE);
		} else if (list.size() == 5) {
			list.remove(4);
			mConfirmEditPsWu.setVisibility(View.VISIBLE);
		} else if (list.size() == 6) {
			list.remove(5);
			mConfirmEditPsLiu.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 重置
	 */
	public void chongzhi() {
		list.clear();// 清空集合数据
		mConfirmEditPsYi.setVisibility(View.VISIBLE);
		mConfirmEditPsEr.setVisibility(View.VISIBLE);
		mConfirmEditPsSan.setVisibility(View.VISIBLE);
		mConfirmEditPsSi.setVisibility(View.VISIBLE);
		mConfirmEditPsWu.setVisibility(View.VISIBLE);
		mConfirmEditPsLiu.setVisibility(View.VISIBLE);
	}

	/**
	 * 进度圈 时间限制
	 */
	// private TimerTask Time() {
	//
	// TimerTask task = new TimerTask() {
	// public void run() {
	// Message message = new Message();
	// message.what = 1;
	// handler.sendMessage(message);
	// }
	// };
	//
	// timer.schedule(task, 1000, 1000); // 延时1000ms后执行，1000ms执行一次
	// return task;
	//
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一页
			finishActivity();
			break;
		case R.id.confirm_keyboard_yi:
			if (list.size() < 6) {
				list.add("1");
				pubss();
			}
			if (list.size() == 6) {
				vertifyPayPassword();
			}
			break;
		case R.id.confirm_keyboard_er:
			if (list.size() < 6) {
				list.add("2");
				pubss();
			}
			if (list.size() == 6) {
				vertifyPayPassword();
			}
			break;
		case R.id.confirm_keyboard_san:
			if (list.size() < 6) {
				list.add("3");
				pubss();
			}
			if (list.size() == 6) {
				vertifyPayPassword();
			}

			break;
		case R.id.confirm_keyboard_si:
			if (list.size() < 6) {
				list.add("4");
				pubss();
			}
			if (list.size() == 6) {
				vertifyPayPassword();
			}

			break;
		case R.id.confirm_keyboard_wu:
			if (list.size() < 6) {
				list.add("5");
				pubss();
			}
			if (list.size() == 6) {
				vertifyPayPassword();
			}

			break;
		case R.id.confirm_keyboard_liu:

			if (list.size() < 6) {
				list.add("6");
				pubss();
			}
			if (list.size() == 6) {
				vertifyPayPassword();
			}

			break;
		case R.id.confirm_keyboard_qi:

			if (list.size() < 6) {
				list.add("7");
				pubss();
			}
			if (list.size() == 6) {
				vertifyPayPassword();
			}

			break;
		case R.id.confirm_keyboard_ba:
			if (list.size() < 6) {
				list.add("8");
				pubss();
			}

			if (list.size() == 6) {
				vertifyPayPassword();
			}

			break;
		case R.id.confirm_keyboard_jiu:
			if (list.size() < 6) {
				list.add("9");
				pubss();
			}
			if (list.size() == 6) {
				vertifyPayPassword();
			}
			break;
		case R.id.confirm_keyboard_reset:
			chongzhi();
			break;
		case R.id.confirm_keyboard_ning:
			if (list.size() < 6) {
				list.add("0");
				pubss();
			}
			if (list.size() == 6) {
				vertifyPayPassword();
			}
			break;
		case R.id.confirm_keyboard_delete:
			isfj();
			break;

		default:
			break;
		}

	}

	/**
	 * 关闭activtiy
	 */
	public void CloseActivity() {
		SetPassWord.msetPassword.finishActivity();
		finishActivity();
	}

	/**
	 * 验证支付密码问题 1、设置密码第二次验证 2、修改新的密码 3、忘记支付密码
	 */
	private void vertifyPayPassword() {

		Intent intent = new Intent();
		if (list.get(0).equals(mlist.get(0))
				&& list.get(1).equals(mlist.get(1))
				&& list.get(2).equals(mlist.get(2))
				&& list.get(3).equals(mlist.get(3))
				&& list.get(4).equals(mlist.get(4))
				&& list.get(5).equals(mlist.get(5))) {
			String listName = mlist.get(0) + mlist.get(1) + mlist.get(2)
					+ mlist.get(3) + mlist.get(4) + mlist.get(5);
			if(getIntent().getIntExtra("Identification", 0) == Constant.CLICK_TIXIAN_ACCOUNT||getIntent().getIntExtra("Identification", 0) == Constant.CLICK_SAFE_SETTING||getIntent().getIntExtra("Identification", 0) == Constant.CLICK_BALANCE_TIXIAN){//设置提现账户
				intent.putExtra("passList", listName);
				intent.putExtra("Identification", getIntent().getIntExtra("Identification", 0));
				intent.setClass(ConfirmPawwWordActivity.this,SetSecurityProblem.class);
				startActivity(intent);
				CloseActivity();// 关闭activtiy
			}else if(getIntent().getIntExtra("Identification", 0) == Constant.CLICK_FORGET_PAYPASS||getIntent().getIntExtra("Identification", 0) == Constant.CLICK_UPDATE_PAYPASS){//忘记密码
				AjaxParams params = new AjaxParams();
				params.put("newPayPass", MD5Util.getMD5(listName));
				requestNet(new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						switch (msg.what) {
						case Constant.NET_DATA_SUCCESS:
							// 设置新密码
							try {
								JSONObject jo = new JSONObject(
										msg.obj.toString());
								if (jo.getString("re").equals("true")) {
									// 成功时关闭
									ToastUtil.toastShow(ConfirmPawwWordActivity.this,"设置新支付密码成功");
									CloseActivity();// 关闭activtiy
									if(getIntent().getIntExtra("Identification", 0) == Constant.CLICK_FORGET_PAYPASS){//忘记密码
										VerifyPayActivity.mUpdatePayPasswordActivity.finishActivity();
									}
									
								} else {
									ToastUtil.toastShow(
											ConfirmPawwWordActivity.this,
											"设置异常，请重新设置");
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

							break;
						case Constant.NET_DATA_FAIL:
							ToastUtil.toastShow(ConfirmPawwWordActivity.this,
									((DataError) msg.obj).getErrorMessage());
							break;
						default:
							break;
						}
					}
				}, params, NetworkUtil.UPDATE_PAY_PASSWORD, false, 0);
			}
		} else {
			if(mClickType == Constant.CLICK_SETTING_PAYPASS){
				intent.setClass(ConfirmPawwWordActivity.this, SetPassWord.class);
				intent.putExtra("again", 1);// 1 密码不一致，重新输入
				intent.putExtra("clickType", Constant.CLICK_SETTING_PAYPASS);
				intent.putExtra("Identification", getIntent().getIntExtra("Identification", 0));
				startActivity(intent);
				CloseActivity();// 关闭activtiy
			}else{
				intent.setClass(ConfirmPawwWordActivity.this, SetPassWord.class);
				intent.putExtra("again", 1);// 1 密码不一致，重新输入
				intent.putExtra("Identification", getIntent().getIntExtra("Identification", 0));
				startActivity(intent);
				CloseActivity();// 关闭activtiy
				
			}
		}

	}

}
