package com.chewuwuyou.app.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CarBookGridAdapter;
import com.chewuwuyou.app.tools.EditInputBiaoQing;
import com.chewuwuyou.app.tools.EditInputFilterThousand;
import com.chewuwuyou.app.utils.DBHelper;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.MyGridView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author zengys
 * 
 *         爱车账本
 */


public class AccountBookActivity extends CDDBaseActivity implements OnClickListener {

	private ImageButton mImageBtnBack;
	private TextView mTextRight;
	private MyGridView mCartypeGV;// 账本类型
	private int[] imgBlueResIds = { R.drawable.book_jiayou_blue, R.drawable.book_xiche_blue,
			R.drawable.book_tingche_blue, R.drawable.book_guolu_blue, R.drawable.book_weixiu_blue,
			R.drawable.book_baoyang_blue, R.drawable.book_zhuangshi_blue,
			R.drawable.book_chedai_blue, R.drawable.book_weizhang_blue, R.drawable.book_chexian_blue,
			R.drawable.book_qita_blue, };

	private int[] imgWhiteResIds = { R.drawable.book_jiayou_white, R.drawable.book_xiche_white,
			R.drawable.book_tingche_white, R.drawable.book_guolu_white, R.drawable.book_weixiu_white,
			R.drawable.book_baoyang_white, R.drawable.book_zhuangshi_white,
			R.drawable.book_chedai_white, R.drawable.book_weizhang_white, R.drawable.book_chexian_white,
			R.drawable.book_qita_white, };
	private String[] carBookStrs = { "加油", "洗车", "停车", "过路", "维修", "保养", "装饰","车贷", "违章", "车险", "其他" };
	private List<Map<String, Object>> mCarAssistantItems;
	private CarBookGridAdapter mAdapter;
	private int mCarGridItemNum = 0;// 表示账本类型子选项
	private EditText mEditMoney, mEditRemark;
	private TextView mTextDate;
	private Calendar mCalendar;
	private Button mBtnSave;
	private TextView mSumMoney;

	private RelativeLayout mLinear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_book_layout);
		initView();
		initEvent();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

		mImageBtnBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mTextRight = (TextView) findViewById(R.id.sub_header_bar_right_tv);
		mCartypeGV = (MyGridView) findViewById(R.id.car_book_gridview);
		mEditMoney = (EditText) findViewById(R.id.consumption_money_edit);
		mTextDate = (TextView) findViewById(R.id.consumption_date_text);
		mEditRemark = (EditText) findViewById(R.id.beizhu_edit);
		mBtnSave = (Button) findViewById(R.id.jiyibi_btn);
		mSumMoney = (TextView) findViewById(R.id.text_money_all);
		mLinear = (RelativeLayout) findViewById(R.id.linear_pass);

		mCarAssistantItems = getCarListItems();
		mAdapter = new CarBookGridAdapter(mCarAssistantItems, this, mCarGridItemNum);

		mCartypeGV.setAdapter(mAdapter);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

		DBHelper dh = new DBHelper(this);
		// 总金额
		double totalMoney = dh.queryMoneyWithCategoryAndMonth("", "");
		BigDecimal b2 = new BigDecimal(totalMoney);
		double b3 = b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		dh.close();

		// mSumMoney.setText(getString(R.string.price_contain_suffix, b3));
		DecimalFormat df = new DecimalFormat("#0.00");
		mSumMoney.setText(df.format(b3) + "");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

		mTextRight.setText("查看明细");
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setOnClickListener(this);
		mImageBtnBack.setOnClickListener(this);
		mTextDate.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);

		mCartypeGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				mCarGridItemNum = arg2;
				mAdapter.setSeclection(arg2);
				mAdapter.notifyDataSetChanged();
			}
		});

		InputFilter[] filters = { new EditInputFilterThousand(100000, 2),
				new InputFilter.LengthFilter(9) /* 这里限制输入的长度为9个字符 */ };
		mEditMoney.setFilters(filters);// 限制金额最大值为999999.9

		InputFilter[] filterBiao = { new EditInputBiaoQing(), new InputFilter.LengthFilter(150) };
		mEditRemark.setFilters(filterBiao);// 限制备注 不能输入表情
//		增加备注的字符限制弹窗
		mEditRemark.addTextChangedListener(new TextWatcher() {
			String mTemp=null;
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				mTemp=mEditRemark.getText().toString();
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (TextUtils.isEmpty(mEditRemark.getText())){
					return;
				}
				if (mEditRemark.getText().toString().length()>=120){
					mEditRemark.setText(mTemp);
					mEditRemark.setSelection(mEditRemark.getText().length());
					ToastUtil.toastShow(AccountBookActivity.this,"超过字数限制");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		//controlKeyboardLayout(mLinear, mEditMoney);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.sub_header_bar_left_ibtn:

			finish();
			break;

		case R.id.sub_header_bar_right_tv:
			Intent intent = new Intent(AccountBookActivity.this, CarAccountDetailsActivity.class);
			intent.putExtra("type", "total");
			startActivity(intent);
			break;

		case R.id.consumption_date_text:
			StatService.onEvent(AccountBookActivity.this, "clickOrderActDateLL", "点击记一笔日期");
			createDatePickerDialog();
			break;
		case R.id.jiyibi_btn:
			StatService.onEvent(AccountBookActivity.this, "clickOrderActSaveBtn", "点击记一笔保存或删除按钮");
			if (TextUtils.isEmpty(mEditMoney.getText().toString())) {
				ToastUtil.toastShow(AccountBookActivity.this, "请输入金额");
			} else if (Double.valueOf(mEditMoney.getText().toString()) <= 0) {
				ToastUtil.toastShow(AccountBookActivity.this, "金额必须大于0");
			} else if (TextUtils.isEmpty(mTextDate.getText().toString())) {
				ToastUtil.toastShow(this, "请选择时间");
			} else {

				// 金额只保留两位小数
				BigDecimal b = new BigDecimal(Double.valueOf(mEditMoney.getText().toString()));
				double b1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

				// 保存

				DBHelper dh = new DBHelper(AccountBookActivity.this);
				dh.insertCarBookTableWith(String.valueOf(b1), carBookStrs[mCarGridItemNum].toString(),
						mTextDate.getText().toString(), mEditRemark.getText().toString());
				dh.close();
				initData();
				mEditMoney.setText("");
				mEditRemark.setText("");
				AlertDialog.Builder dialog = new AlertDialog.Builder(AccountBookActivity.this);
				dialog.setTitle("保存信息");
				dialog.setMessage("您的消费已记录成功！可在消费明细中查看详情");
				dialog.setPositiveButton("再记一笔", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						StatService.onEvent(AccountBookActivity.this, "clickOrderActOkBtn", "点击记一笔保存按钮");
						arg0.dismiss();
					}
				});
				dialog.setNegativeButton("消费明细", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						StatService.onEvent(AccountBookActivity.this, "clickOrderActCancleBtn", "点击记一笔查看消费明细");
						Intent intent = new Intent(AccountBookActivity.this, CarAccountDetailsActivity.class);
						intent.putExtra("type", "total");
						startActivity(intent);
						arg0.dismiss();
					}
				});
				dialog.show();

			}

			break;

		default:
			break;
		}

	}

	// 初始化 账本类型
	private List<Map<String, Object>> getCarListItems() {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < carBookStrs.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("carBookStrs", carBookStrs[i]);
			map.put("carBookImgBlueResIds", imgBlueResIds[i]);
			map.put("carBookImgWhiteResIds", imgWhiteResIds[i]);
			listItems.add(map);
		}

		return listItems;
	}

	// 创建日期对话框
	private void createDatePickerDialog() {

		Dialog dialog = null;
		mCalendar = Calendar.getInstance();
		dialog = new DatePickerDialog(AccountBookActivity.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
				mTextDate.setText(year + "-"
						+ ((("" + (month + 1)).length() == 1) ? ("0" + (month + 1)) : ("" + (month + 1))) + "-"
						+ ((("" + (dayOfMonth)).length() == 1) ? ("0" + (dayOfMonth)) : ("" + (dayOfMonth))));

			}
		}, mCalendar.get(Calendar.YEAR), // 传入年份
				mCalendar.get(Calendar.MONTH), // 传入月份
				mCalendar.get(Calendar.DAY_OF_MONTH)); // 传入天数
		dialog.show();
	}

	/**
	 * @param root
	 *            最外层布局，需要调整的布局
	 * @param scrollToView
	 *            被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
	 */
	private void controlKeyboardLayout(final View root, final View scrollToView) {
		root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				// 获取root在窗体的可视区域
				root.getWindowVisibleDisplayFrame(rect);
				// 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
				int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;

				DisplayMetrics metric = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metric);
				int densityDpi = metric.densityDpi;// 获取手机DPI

				// 若不可视区域高度大于100，则键盘显示
				if (rootInvisibleHeight > 100) {
					int[] location = new int[2];
					// 获取scrollToView在窗体的坐标
					scrollToView.getLocationInWindow(location);
					// 计算root滚动高度，使scrollToView在可见区域
					int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
					MyLog.i("yang", srollHeight + "" + "....." + densityDpi);
					// root.scrollTo(0, srollHeight);
					root.scrollTo(0, 90);
				} else {
					// 键盘隐藏
					root.scrollTo(0, 0);
				}
			}
		});
	}

}
