package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:申请退款
 * @author:yuyong
 * @date:2015-6-29下午2:12:55
 * @version:1.2.1
 */
public class ApplyTuiKuanActivity extends CDDBaseActivity implements
		OnClickListener {
	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private TextView mOrderServiceContentTV;
	private TextView mOrderDescriptionTV;
	private TextView mOrderNumberTV;
	private TextView mOrderTimeTV;
	private TextView mPayMoneyTV;
	private EditText mApplyTuiKuanReasonET;
	private Button mApplyTuiKuanBtn;
	private Task mTask;// 订单任务实体
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apply_tuikan_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mOrderServiceContentTV = (TextView) findViewById(R.id.order_content_tv);
		mOrderDescriptionTV = (TextView) findViewById(R.id.order_description_tv);
		mOrderNumberTV = (TextView) findViewById(R.id.order_number_tv);
		mOrderTimeTV = (TextView) findViewById(R.id.order_time_tv);
		mPayMoneyTV = (TextView) findViewById(R.id.order_price_tv);
		mApplyTuiKuanBtn = (Button) findViewById(R.id.apply_tuikuan_btn);
		mApplyTuiKuanReasonET = (EditText) findViewById(R.id.tuikuan_reason_et);
	}

	@Override
	protected void initData() {
		mTask = (Task) getIntent().getSerializableExtra(Constant.TASK_SER);
		mTitleTV.setText(R.string.apply_tuikuan_title);
		mOrderNumberTV.setText(mTask.getOrderNum());
		mPayMoneyTV.setText(mTask.getPaymentAmount() + "");
		mOrderTimeTV.setText(mTask.getPubishTime());
		if (!TextUtils.isEmpty(mTask.getProjectName())) {
			mOrderServiceContentTV.setText(mTask.getType() + "-"
					+ ServiceUtils.getProjectName(mTask.getProjectName()));
		} else {
			mOrderServiceContentTV.setText(mTask.getType());
		}
		if (!TextUtils.isEmpty(mTask.getUserDescription())) {
			mOrderDescriptionTV.setText(mTask.getUserDescription());
		} else {
			mOrderDescriptionTV.setText("暂无描述");
		}
	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		mApplyTuiKuanBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.apply_tuikuan_btn:
			if (mTask.getStatus().equals("4") || mTask.getStatus().equals("5")) {
				applyForReimburse();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 申请退款 st是状态，1，申请退款；2，同意退款；
	 */
	private void applyForReimburse() {
		String applyReason = mApplyTuiKuanReasonET.getText().toString().trim();
		if (TextUtils.isEmpty(applyReason)) {
			ToastUtil.toastShow(ApplyTuiKuanActivity.this, "请输入退款理由");
		} else if (applyReason.length() < 6) {
			ToastUtil.toastShow(ApplyTuiKuanActivity.this, "请输入6个字以上的退款理由");
		} else {
			AjaxParams params = new AjaxParams();
			params.put("orderId", mTask.getOrderNum());
			params.put("st", Constant.TUIKUAN.APPLY_TUIKUAN);
			params.put("rea", applyReason);
			MyLog.i("YUY", "orderId = " + mTask.getOrderNum() + " st = "
					+ Constant.TUIKUAN.APPLY_TUIKUAN + " rea = " + applyReason);
			requestNet(new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:
						MyLog.i("YUY", "申请退款成功!");
//						finishActivity();
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								ApplyTuiKuanActivity.this);
						dialog.setTitle("申请退款");
						dialog.setMessage("您的退款申请已提交，等待商家确认。");
						dialog.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										arg0.dismiss();
										finishActivity();
									}
								});
						dialog.create();
						dialog.show();
						break;
					case Constant.NET_DATA_FAIL:
						MyLog.i("YUY", "申请退款失败!");
						break;
					default:
						break;
					}
				}
			}, params, NetworkUtil.APPLY_FOR_TUIKUAN, false, 1);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
