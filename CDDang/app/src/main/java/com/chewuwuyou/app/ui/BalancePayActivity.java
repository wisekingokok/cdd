package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chewuwuyou.app.R;

/**
 * 余额支付
 */
public class BalancePayActivity extends CDDBaseActivity implements
		android.view.View.OnClickListener {

	// private Button mBtnaffirm; // 确认
	private AlertDialog.Builder abuilder;// 密码错误dialog
	private int mBalanceNum = 4;
	private AlertDialog.Builder abuilderTwo;// 密码错误dialog
	private int mDialogNum;
	private Timer timer;
	private EditText mConfirmEditPsYi, mConfirmEditPsEr, mConfirmEditPsSan,
			mConfirmEditPsSi, mConfirmEditPsWu, mConfirmEditPsLiu;
	private ArrayList<String> list;
	private ImageButton mBackIBtn;// 返回
	private RelativeLayout mTitleHeight;//标题布局高度
	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (mDialogNum == 1) {
					abuilderTwo.setTitle("由于您输入错误次数过多，您的支付密码将被封锁1小时，敬请您的谅解");

					abuilderTwo.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							mDialogNum = 0;

						}
					});
					abuilderTwo.create().show();
				}

				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balance_pay_layout);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// mBtnaffirm = (Button) findViewById(R.id.btn_affirm);
		abuilderTwo = new AlertDialog.Builder(this);
		timer = new Timer(true);

		mConfirmEditPsYi = (EditText) findViewById(R.id.balance_edit_ps_yi);
		mConfirmEditPsEr = (EditText) findViewById(R.id.balance_edit_ps_er);
		mConfirmEditPsSan = (EditText) findViewById(R.id.balance_edit_ps_san);
		mConfirmEditPsSi = (EditText) findViewById(R.id.balance_edit_ps_si);
		mConfirmEditPsWu = (EditText) findViewById(R.id.balance_edit_ps_wu);
		mConfirmEditPsLiu = (EditText) findViewById(R.id.balance_edit_ps_liu);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		list = new ArrayList<String>();

	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mBackIBtn.setOnClickListener(this);
	}

	public void balanceonclick(View v) {

		switch (v.getId()) {
		case R.id.btn_affirm:

			if (mBalanceNum == 1) {
				abuilder = new AlertDialog.Builder(this);
				abuilder.setTitle("密码有误");
				abuilder.setMessage("为了你的账户安全,您还有" + mBalanceNum
						+ "次机会,再次输入错误，支付密码将被锁定，是否继续输入");
				abuilder.setPositiveButton("是", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						mBalanceNum = mBalanceNum - 1;

						mDialogNum = 1;
						Toast.makeText(BalancePayActivity.this,
								String.valueOf(mDialogNum), Toast.LENGTH_SHORT)
								.show();

						Time();
					}
				});
				abuilder.setNegativeButton("否", null);
				abuilder.create().show();

			} else {

				abuilder = new AlertDialog.Builder(this);
				abuilder.setTitle("密码有误");
				abuilder.setMessage("为了你的账户安全,您还有" + mBalanceNum + "次机会是否继续输入");
				abuilder.setPositiveButton("是", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						mBalanceNum = mBalanceNum - 1;
						Toast.makeText(BalancePayActivity.this,
								String.valueOf(mBalanceNum), Toast.LENGTH_SHORT)
								.show();

					}
				});
				abuilder.setNegativeButton("否", null);
				abuilder.create().show();
			}

			break;

		case R.id.balance_keyboard_yi:
			if (list.size() < 6) {
				list.add("1");
				pubss();
			}
			break;
		case R.id.balance_keyboard_er:
			if (list.size() < 6) {
				list.add("2");
				pubss();
			}
			break;
		case R.id.balance_keyboard_san:
			if (list.size() < 6) {
				list.add("3");
				pubss();
			}
			break;
		case R.id.balance_keyboard_si:
			if (list.size() < 6) {
				list.add("4");
				pubss();
			}
			break;
		case R.id.balance_keyboard_wu:
			if (list.size() < 6) {
				list.add("5");
				pubss();
			}
			break;
		case R.id.balance_keyboard_liu:
			if (list.size() < 6) {
				list.add("6");
				pubss();
			}
			break;

		case R.id.balance_keyboard_qi:
			if (list.size() < 6) {
				list.add("7");
				pubss();
			}
			break;

		case R.id.balance_keyboard_ba:
			if (list.size() < 6) {
				list.add("8");
				pubss();
			}
			break;

		case R.id.balance_keyboard_jiu:
			if (list.size() < 6) {
				list.add("9");
				pubss();
			}
			break;

		case R.id.balance_keyboard_reset:
			chongzhi();
			break;
		default:
			break;

		case R.id.balance_keyboard_delete:
			isfj();
			break;
		}

	}

	/**
	 * "封锁密码1小时提示"dialog 时间限制
	 */
	private TimerTask Time() {

		TimerTask task = new TimerTask() {
			public void run() {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		};

		timer.schedule(task, 1000, 1000000); // 延时1000ms后执行，1000ms执行一次
		return task;

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;

		default:
			break;
		}
	}

}
