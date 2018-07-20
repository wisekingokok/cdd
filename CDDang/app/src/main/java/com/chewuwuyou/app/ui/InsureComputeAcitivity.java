package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.MyGridView;

/**
 * 保险助手
 * 
 * @author Administrator
 */
@SuppressLint("ViewHolder")
public class InsureComputeAcitivity extends CDDBaseActivity {
	private EditText mCarPrice;
	private RelativeLayout mTitleHeight;// 标题布局高度
	InputMethodManager inputManager;
	private MyGridView mBaoxianGV;// 保险GridView
	private int[] image_baoxian = { R.drawable.anbang_bang,
			R.drawable.ansheng_bao, R.drawable.guoyuan_bao,
			R.drawable.pingan_bao, R.drawable.renbao_bao,
			R.drawable.renshou_bao, R.drawable.sanxing_bao,
			R.drawable.taipingyang_bao, R.drawable.yongcheng_bao,
			R.drawable.zhonghua_bao };
	private List<Map<String, Object>> mBaoxianItems;
	private BaoxianAdatper mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insure_compute_ac);
		initView();
		initData();

	}

	public void onAction(View v) {
		String carPrice = mCarPrice.getText().toString().trim();
		if (TextUtils.isEmpty(carPrice)) {
			ToastUtil.toastShow(InsureComputeAcitivity.this, "请输入车辆裸车售价");
		} else if (!(carPrice.matches(RegularUtil.verifyVehiclePrice))
				|| Integer.parseInt(carPrice) < 10000) {
			ToastUtil.toastShow(InsureComputeAcitivity.this, "裸车售价不小于10000");
		} else {
			Intent intent = new Intent(this, InsureDetailActivity.class);
			intent.putExtra("price", carPrice);
			startActivity(intent);
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(InsureComputeAcitivity.this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(InsureComputeAcitivity.this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

		Timer timer = new Timer();
		timer.schedule(new TimerTask()

		{

			public void run()

			{

				inputManager = (InputMethodManager) mCarPrice.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(mCarPrice, 0);

			}

		},

		998);

		mBaoxianItems = getCarListItems();
		mAdapter = new BaoxianAdatper(mBaoxianItems, this);
		mBaoxianGV.setAdapter(mAdapter);
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mCarPrice = (EditText) findViewById(R.id.insure_buy_car_price);
		mCarPrice.setSelection(mCarPrice.getText().length());// 设置光标在末尾
		/**
		 * 自动获取焦点弹出软键盘
		 */
		mCarPrice.setFocusable(true);
		mCarPrice.setFocusableInTouchMode(true);
		mCarPrice.requestFocus();

		mBaoxianGV = (MyGridView) findViewById(R.id.baoxian_gv);

		((TextView) findViewById(R.id.sub_header_bar_tv)).setText("保险助手");
		findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finishActivity();
					}
				});

	}

	class BaoxianAdatper extends BaseAdapter {
		private List<Map<String, Object>> listItems;
		private Context mContext;

		public BaoxianAdatper(List<Map<String, Object>> listItems,
				Context mContext) {
			super();
			this.listItems = listItems;
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub

			LinearLayout view = (LinearLayout) LinearLayout.inflate(mContext,
					R.layout.baoxian_item, null);

			ImageView image = (ImageView) view.findViewById(R.id.baoxian_image);
			image.setImageResource((Integer) listItems.get(arg0).get(
					"baoxian"));

			return view;
		}

	}

	// 初始化 保险图片
	private List<Map<String, Object>> getCarListItems() {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < image_baoxian.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("baoxian", image_baoxian[i]);
			listItems.add(map);
		}

		return listItems;
	}
}
