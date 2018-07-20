package com.chewuwuyou.app.ui;

import java.util.List;

import org.achartengine.GraphicalView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.ChartLibrary;
import com.chewuwuyou.app.utils.ChartLibrary.CHARTTYPE;
import com.chewuwuyou.app.utils.DBHelper;

/**
 * @describe：数据图表
 * @author ：caixuemei
 * @created ：2014-7-7下午3:44:46
 */
public class DataChartActivity extends CDDBaseActivity {

	private TextView mNaviBarTitle;
	private ImageButton mNaviBarBack;
	private TextView mCategory;// 消费类别
	private RelativeLayout mTitleHeight;// 标题布局高度

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.data_chart_ac);

		init();

		setupCharView();
	}

	/**
	 * 1，按月份查并且不是总数---这个特点需要实现；2，需要去掉数字；
	 */
	private void setupCharView() {

		DBHelper dh = new DBHelper(this);

		// For Lines
		List<String>[] monthsAndMoneys = dh.queryMoneyByMonth(mCategory
				.getText().toString().equals("所有") ? "" : mCategory.getText()
				.toString());
		double[] xValuesForLine = new double[monthsAndMoneys[0].size()];
		double[] yValuesForLine = new double[monthsAndMoneys[0].size()];
		String[] namesForLine = new String[monthsAndMoneys[0].size()];
		for (int i = 0, size = monthsAndMoneys[0].size(); i < size; i++) {
			xValuesForLine[i] = i + 1;
			yValuesForLine[i] = Double.parseDouble(monthsAndMoneys[1].get(i));
			namesForLine[i] = monthsAndMoneys[0].get(i)
					.substring(2, monthsAndMoneys[0].get(i).length())
					.replace('-', '/');
		}

		String[] namesForPillow = getResources().getStringArray(
				R.array.record_select_category);
		double[] xValuesForPillow = new double[namesForPillow.length];
		double[] yValuesForPillow = new double[namesForPillow.length];
		for (int i = 0, size = namesForPillow.length; i < size; i++) {
			xValuesForPillow[i] = i + 1;
			yValuesForPillow[i] = dh.queryMoneyWithCategoryAndMonth(
					namesForPillow[i], "");
		}

		dh.close();

		RelativeLayout lineView = (RelativeLayout) findViewById(R.id.line_show);
		lineView.removeAllViews();
		GraphicalView gViewLine = ChartLibrary.execute(this, CHARTTYPE.LINE,
				xValuesForLine, yValuesForLine, namesForLine);
		lineView.addView(gViewLine, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		RelativeLayout pillowView = (RelativeLayout) findViewById(R.id.pillow_show);
		pillowView.removeAllViews();
		GraphicalView gViewPillow = ChartLibrary.execute(this,
				CHARTTYPE.PILLOW, xValuesForPillow, yValuesForPillow,
				namesForPillow);
		pillowView.addView(gViewPillow, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mNaviBarTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		mNaviBarTitle.setText(R.string.data_chart);
		mNaviBarBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mNaviBarBack.setVisibility(View.VISIBLE);
		mCategory = (TextView) findViewById(R.id.category);

		// 返回
		mNaviBarBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishActivity();
			}
		});

		// // 广告
		// mCommonMsgTxt.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// Intent intent = new Intent(DataChartActivity.this,
		// CommonAdvActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putInt("locationbgId", R.drawable.common_bg);
		// bundle.putInt("advId", R.string.car_bottom_adv_ac_adv);
		// bundle.putInt("titleId", R.string.data_chart);
		// bundle.putInt("bottombg", R.drawable.common_bottom_yellow);
		// intent.putExtras(bundle);
		// startActivity(intent);
		// }
		// });

		// 消费类别
		mCategory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				StatService.onEvent(DataChartActivity.this,
						"ClickDataChartCategoryTV", "点击数据图消费类别TV");
				Intent intent = new Intent(DataChartActivity.this,
						RecordSelectCategoryActivity.class);
				// 需要“全部”选项
				intent.putExtra("all", 1);
				startActivityForResult(intent, 20);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (30 == resultCode) {

			String choicecategory = data.getExtras()
					.getString("choicecategory");
			mCategory.setText(choicecategory);
			setupCharView();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(DataChartActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(DataChartActivity.this);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}
}
