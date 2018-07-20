package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.tools.EditInputBiaoQing;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @describe:订单评价
 * @author:liuchun
 */
public class OrderEvaluateActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitel;// 标题
	private ImageButton mSubHeaderBarLeftIbtn;// 返回上一页
	private EditText mOrderEvaluate;
	private Task mTask;
	private RelativeLayout mTitleHeight;// 标题布局高度
	private RatingBar mOrderRating;// 等级
	private Button mOrderEvaluateSubmina;// 提交评价

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_evaluate_ac);

		initView();
		initData();
		initEvent();

	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mTask = (Task) getIntent().getSerializableExtra(Constant.TASK_SER);// 接受实体
		mTitel = (TextView) findViewById(R.id.sub_header_bar_tv);// 标题
		mTitel.setText("订单评价");
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);// 返回上一页
		mOrderEvaluate = (EditText) findViewById(R.id.evaluate_context);// 订单评价内容
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		mOrderRating = (RatingBar) findViewById(R.id.order_rating);
		mOrderEvaluateSubmina = (Button) findViewById(R.id.order_evaluate_submian);// 提交

		/* 过滤表情 */

		InputFilter[] filters = { new EditInputBiaoQing(),
				new InputFilter.LengthFilter(300), new InputFilter() {
					@Override
					public CharSequence filter(CharSequence src, int start,
							int end, Spanned dst, int dstart, int dend) {
						if (src.length() < 1) {
							return null;
						} else {
							char temp[] = (src.toString()).toCharArray();
							char result[] = new char[temp.length];
							for (int i = 0, j = 0; i < temp.length; i++) {
								if (temp[i] == ' ') {
									continue;
								} else {
									result[j++] = temp[i];
								}
							}
							return String.valueOf(result).trim();
						}
					}
				} };
		mOrderEvaluate.setFilters(filters);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回
			OrderEvaluateActivity.this.finishActivity();
			break;

		case R.id.order_evaluate_submian:// 提交评价
			int rating = (int) mOrderRating.getRating();
			if (rating == 0) {
				ToastUtil.toastShow(OrderEvaluateActivity.this, "请选择星级");
			} else {
				AjaxParams params = new AjaxParams();
				params.put("taskId", mTask.getId());
				params.put("businessId", mTask.getBusinessId());
				params.put("content", mOrderEvaluate.getText().toString()
						.trim());
				params.put("star", rating + "");
				getNetworkRequest(params);
			}
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
		isTitle(mTitleHeight);// 根据不同手机判断
		mTask = (Task) getIntent().getSerializableExtra(Constant.TASK_SER);
	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mOrderEvaluateSubmina.setOnClickListener(this);
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
	}

	/**
	 * 提交评价
	 * 
	 * @param params
	 */
	public void getNetworkRequest(AjaxParams params) {
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					finishActivity();
					break;
				default:
					break;
				}
			}
		}, params, NetworkUtil.ADD_EVALUATE, false, 0);
	}
}
