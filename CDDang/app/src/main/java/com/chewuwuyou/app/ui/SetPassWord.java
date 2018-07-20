package com.chewuwuyou.app.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

import de.greenrobot.event.EventBus;

public class SetPassWord extends CDDBaseActivity implements
		android.view.View.OnClickListener {

	private TextView mTitle;// 标题,关闭
	private TextView mkeyboard_oneYi, mkeyboard_oneEr, mkeyboard_oneSan, mkeyboard_oneSi,
			mkeyboard_oneWu, mkeyboard_oneLiu, mkeyboard_oneQi, mkeyboard_oneBa, mkeyboard_oneJiu,
			mkeyboard_oneShi, mkeyboard_oneShiYi, mkeyboard_oneShiEr;
	private EditText mEditPsYi, mEditPsEr, mEditPsSan, mEditPsSi, mEditPsWu,
			mEditPsLiu;
	// private ImageView mImgPsYi, mImgPsEr, mImgPsSan, mImgPsSi, mImgPsWu,
	// mImgPsLiu;
	private ImageButton mSubHeaderBarLeftIbtn;
	private ArrayList<String> list;
	private TextView passwordConsistent;
	// private String mPayWord;
	private LayoutInflater mLinearLayout;// 加载dialog佈局
	private TextView mDialogTetil;// 标题，内容;
	private Button mWsCancel, mWsConfirm;// 取消,确认
	private Dialog mDdialog;// 关闭提示框
	private RelativeLayout mTitleHeight;// 标题布局高度
	public static SetPassWord msetPassword = null;
	/**
	 * 点击类型 包含设置密码、修改密码、忘记密码
	 */
	private int mClickType;// 点击类型,类型

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_password);
		msetPassword = this;
		initView();// 初始化控件
		initData();
		initEvent();// 点击事件
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {

		mClickType = getIntent().getIntExtra("clickType", 0);
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);// 返回上一页
		passwordConsistent = (TextView) findViewById(R.id.password_consistent);// 主题显示
		mTitle = (TextView) findViewById(R.id.sub_header_bar_tv);// 主题显示
		mTitle.setText("设置支付密码");

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

		Intent intent = getIntent();
		// mPayWord = intent.getStringExtra("payWord").toString();

		if (intent.getIntExtra("again", 0) == 1) {
			passwordConsistent.setVisibility(View.VISIBLE);
		} else {
			passwordConsistent.setVisibility(View.GONE);
		}
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
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一个页面

			mLinearLayout = LayoutInflater.from(SetPassWord.this);
			View myLoginView = mLinearLayout.inflate(
					R.layout.withdrawals_close_dialog, null);
			mDdialog = new AlertDialog.Builder(SetPassWord.this).create();
			mDdialog.setCancelable(false);// 点击其他空白区域不关闭
			mDdialog.show();
			mDdialog.getWindow().setContentView(myLoginView);
			mDialogTetil = (TextView) myLoginView
					.findViewById(R.id.ws_dialog_titel);

			mWsCancel = (Button) myLoginView.findViewById(R.id.ws_cancel);// 取消
			mWsConfirm = (Button) myLoginView.findViewById(R.id.ws_confirm);// 确认
			mWsCancel.setOnClickListener(this);// dialog取消
			mWsConfirm.setOnClickListener(this);// dialog确认
			mDialogTetil.setText("确定要放弃支付密码的修改？");
			
			break;
		case R.id.ws_cancel:// 取消
			mDdialog.dismiss();
			break;
		case R.id.ws_confirm:// 确认
			// if (mPayWord.equals("1")) {
			finishActivity();
			EventBus.getDefault().unregister(this);// 反注册EventBus
			// }
			/*
			 * intent = new Intent(this,WalletActivity.class);
			 * startActivity(intent);
			 */
			break;
		case R.id.keyboard_one_yi:
			if (list.size() < 6) {
				list.add("1");
				HidePassWord();
			}
			if (list.size() == 6) {
				toClass();
			}
			break;
		case R.id.keyboard_one_er:
			if (list.size() < 6) {
				list.add("2");
				HidePassWord();
			}
			if (list.size() == 6) {
				toClass();
			}
			break;
		case R.id.keyboard_one_san:
			if (list.size() < 6) {
				list.add("3");
				HidePassWord();
			}
			if (list.size() == 6) {
				toClass();
			}

			break;
		case R.id.keyboard_one_si:
			if (list.size() < 6) {
				list.add("4");
				HidePassWord();
			}
			if (list.size() == 6) {
				toClass();
			}

			break;
		case R.id.keyboard_one_wu:
			if (list.size() < 6) {
				list.add("5");
				HidePassWord();
			}
			if (list.size() == 6) {
				toClass();
			}
			break;
		case R.id.keyboard_one_liu:
			if (list.size() < 6) {
				list.add("6");
				HidePassWord();
			}
			if (list.size() == 6) {
				toClass();
			}

			break;
		case R.id.keyboard_one_qi:

			if (list.size() < 6) {
				list.add("7");
				HidePassWord();
			}
			if (list.size() == 6) {
				toClass();
			}

			break;
		case R.id.keyboard_one_ba:
			if (list.size() < 6) {
				list.add("8");
				HidePassWord();
			}
			if (list.size() == 6) {
				toClass();
			}

			break;
		case R.id.keyboard_one_jiu:
			if (list.size() < 6) {
				list.add("9");
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
				toClass();
			}
			break;
		case R.id.keyboard_one_delete:
			DeletePassWord();
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
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
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

	}

	/**
	 * 跳转
	 */
	private void toClass() {
		
		Intent intent = new Intent(SetPassWord.this,
				ConfirmPawwWordActivity.class);
		intent.putStringArrayListExtra("passList", list);
		intent.putExtra("Identification", getIntent().getIntExtra("Identification", 0));
		
		if (mClickType != 0) {
			intent.putExtra("clickType", mClickType);
		} else {
			intent.putExtra("clickType", Constant.SETPASSWORD);
		}
		startActivity(intent);
		finishActivity();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mLinearLayout = LayoutInflater.from(SetPassWord.this);
			View myLoginView = mLinearLayout.inflate(
					R.layout.withdrawals_close_dialog, null);
			mDdialog = new AlertDialog.Builder(SetPassWord.this).create();
			mDdialog.setCancelable(false);// 点击其他空白区域不关闭
			mDdialog.show();
			mDdialog.getWindow().setContentView(myLoginView);
			mDialogTetil = (TextView) myLoginView
					.findViewById(R.id.ws_dialog_titel);

			mWsCancel = (Button) myLoginView.findViewById(R.id.ws_cancel);// 取消
			mWsConfirm = (Button) myLoginView.findViewById(R.id.ws_confirm);// 确认
			mWsCancel.setOnClickListener(this);// dialog取消
			mWsConfirm.setOnClickListener(this);// dialog确认
			mDialogTetil.setText("确定要放弃支付密码的修改？");
			return true;
		}
		return false;
	}

}
