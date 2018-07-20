package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.MyLog;

/**
 * 门店录单订单详情
 *
 * @author zengys
 */
public class OrderDetailsElementActivity extends CDDBaseActivity implements
        OnClickListener {

    private ImageButton mImageBack;
    private TextView mTextTitle;
    private TextView mTextRightTitle;
    private TextView mTextOrderType;
    // private TextView mTextOrderMoney;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private TextView mTextOrderServices;
    // private TextView mTextOrderInkman;
    // private TextView mTextOrderPhone;
    // private TextView mTextOrderTime;
    // private TextView mTextOrderDate;
    // private TextView mTextOrderDescribe;
    private TextView mTextPaymentTime;
    private TextView mTextPaymentDate;
    private LinearLayout mLinearDetailsElement;
    private Button mBtnPay;
    private String mElementType;
    private RelativeLayout mRelativeElement;
    private TextView mTextServeTime;
    private TextView mTextServeDate;
    private TextView mTextokServeTime;
    private TextView mTextokServeDate;
    private TextView TextRepeal;
    private RelativeLayout mRelativeMoney;
    private TextView mTextDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_details_layout);
        // 新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        mElementType = bundle.getString("ElementType");
//		initView();
//		initEvent();
//		initData();
    }

    @Override
    protected void initView() {
//        mTextTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
//        mTextRightTitle = (TextView) findViewById(R.id.sub_header_bar_right_tv);
//        mTextOrderType = (TextView) findViewById(R.id.text_ordertype);
//        // mTextOrderMoney = (TextView) findViewById(R.id.text_money);
//        mTextOrderServices = (TextView) findViewById(R.id.text_services);
//        // mTextOrderInkman = (TextView) findViewById(R.id.text_inkman);
//        // mTextOrderPhone = (TextView) findViewById(R.id.text_phone);
//        // mTextOrderTime = (TextView) findViewById(R.id.text_time);
//        // mTextOrderDate = (TextView) findViewById(R.id.text_date);
//        // mTextOrderDescribe = (TextView) findViewById(R.id.text_describe);
//        mLinearDetailsElement = (LinearLayout) findViewById(R.id.linear_details_element);
//        mRelativeElement = (RelativeLayout) findViewById(R.id.relative_element);
//        mImageBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
//        mRelativeMoney = (RelativeLayout) findViewById(R.id.relative_money);
//        mTextDemo = (TextView) findViewById(R.id.text_demo);
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断

    }

    @Override
    protected void initEvent() {
//		addview();
        mImageBack.setOnClickListener(this);
    }

    /**
     * 根据订单不同状态添加相应的控件或修改控件属性
     * <p/>
     * 状态 mElementType，
     * 状态值为“1，2，3，4，5，6”，状态值分别对应“未付款，已付款，服务中，已完成，撤销订单，退款订单”,分别在代码95行，114号，
     * 192行，329行， 513行,611行
     */
    @SuppressWarnings("deprecation")
//	private void addview() {
//
//		DisplayMetrics metric = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metric);
//		int densityDpi = metric.densityDpi;
//
//		mTextTitle.setText("订单详情");
//		if (mElementType.equals("1")) {
//			// 设置"确认付款"Button控件的宽高
//			LinearLayout.LayoutParams btnLayout = new LinearLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//
//			mTextRightTitle.setText("撤销订单");
//			mTextRightTitle.setVisibility(View.VISIBLE);
//			mTextRightTitle.setOnClickListener(this);
//			mTextOrderType.setText("未付款");
//			mBtnPay = new Button(this);
//			mBtnPay.setText("确认付款");
//			mBtnPay.setTextSize(16);
//			mBtnPay.setGravity(Gravity.CENTER);
//			mBtnPay.setBackgroundDrawable(this.getResources().getDrawable(
//					R.drawable.balance_blue));
//			btnLayout.setMargins(0, 60 * densityDpi / 320, 0, 0);
//
//			mBtnPay.setLayoutParams(btnLayout);
//			mLinearDetailsElement.addView(mBtnPay);
//		} else if (mElementType.equals("2")) {
//
//			mTextRightTitle.setText("退款");
//			mTextRightTitle.setVisibility(View.VISIBLE);
//			mTextRightTitle.setOnClickListener(this);
//			mTextOrderType.setText("已付款");
//
//			// 创建 “录单时间： 2016-05-04 00：23：23” 下的 横线 view
//			View view = new View(this);
//			RelativeLayout.LayoutParams viewLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 1);
//			viewLayout.setMargins(0, 20 * densityDpi / 320, 0,
//					20 * densityDpi / 320);
//			int idview = 1;
//			view.setId(idview);
//			view.setBackgroundColor(Color.parseColor("#7f7f7f"));
//			viewLayout.addRule(RelativeLayout.BELOW, R.id.text_time);
//			view.setLayoutParams(viewLayout);
//			mRelativeElement.addView(view);// 加载“录单时间： 2016-05-04 00：23：23” 下的
//											// 横线 view(完成)
//
//			// 创建 “确认付款时间” TextView
//			RelativeLayout.LayoutParams textLayout1 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextView textview1 = new TextView(this);
//			textview1.setText("确认付款时间:");
//			textview1.setTextSize(16);
//			textview1.setTextColor(Color.parseColor("#000000"));
//			textLayout1.setMargins(34 * densityDpi / 320, 0, 0,
//					20 * densityDpi / 320);
//			textLayout1.addRule(RelativeLayout.BELOW, view.getId());
//			textLayout1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			textview1.setLayoutParams(textLayout1);
//			mRelativeElement.addView(textview1); // 加载 “确认付款时间” TextView(完成)
//
//			// 创建 “付款时间” TextView
//			RelativeLayout.LayoutParams textLayout2 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextPaymentTime = new TextView(this);
//			int paytimeId = 2;
//			mTextPaymentTime.setId(paytimeId);
//			mTextPaymentTime.setText("09:13:24");
//			mTextPaymentTime.setTextSize(16);
//			mTextPaymentTime.setTextColor(Color.parseColor("#afafaf"));
//			textLayout2.setMargins(0, 0, 34 * densityDpi / 320,
//					20 * densityDpi / 320);
//			textLayout2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			textLayout2.addRule(RelativeLayout.BELOW, view.getId());
//			mTextPaymentTime.setLayoutParams(textLayout2);
//			mRelativeElement.addView(mTextPaymentTime);// 加载“付款时间” TextView(完成)
//
//			// 创建“付款日期” TextView
//			RelativeLayout.LayoutParams textLayout3 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextPaymentDate = new TextView(this);
//			mTextPaymentDate.setText("2016-05-20");
//			mTextPaymentDate.setTextSize(16);
//			mTextPaymentDate.setTextColor(Color.parseColor("#afafaf"));
//			textLayout3.setMargins(0, 0, 20 * densityDpi / 320,
//					20 * densityDpi / 320);
//			textLayout3.addRule(RelativeLayout.LEFT_OF,
//					mTextPaymentTime.getId());
//			MyLog.i("yang", mTextPaymentTime.getId() + "");
//			textLayout3.addRule(RelativeLayout.BELOW, view.getId());
//			mTextPaymentDate.setLayoutParams(textLayout3);
//			mRelativeElement.addView(mTextPaymentDate);// 加载“付款日期” TextView(完成)
//
//			// 创建“开始服务”Button
//			mBtnPay = new Button(this);
//			mBtnPay.setText("开始服务");
//			mBtnPay.setTextSize(16);
//			mBtnPay.setGravity(Gravity.CENTER);
//			mBtnPay.setBackgroundDrawable(this.getResources().getDrawable(
//					R.drawable.balance_blue));
//			LinearLayout.LayoutParams btnLayout = new LinearLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mBtnPay.setLayoutParams(btnLayout);
//			mLinearDetailsElement.addView(mBtnPay);// 加载“开始服务”Button(完成)
//
//		} else if (mElementType.equals("3")) {
//			mTextRightTitle.setText("退款");
//			mTextRightTitle.setVisibility(View.VISIBLE);
//			mTextRightTitle.setOnClickListener(this);
//			mTextOrderType.setText("服务中");
//
//			// 创建 “录单时间： 2016-05-04 00：23：23” 下的 横线 view
//			View view = new View(this);
//			RelativeLayout.LayoutParams viewLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 1);
//			viewLayout.setMargins(0, 20 * densityDpi / 320, 0,
//					20 * densityDpi / 320);
//			int idview = 1;
//			view.setId(idview);
//			view.setBackgroundColor(Color.parseColor("#7f7f7f"));
//			viewLayout.addRule(RelativeLayout.BELOW, R.id.text_time);
//			view.setLayoutParams(viewLayout);
//			mRelativeElement.addView(view);// 加载“录单时间： 2016-05-04 00：23：23” 下的
//											// 横线 view(完成)
//
//			// 创建 “确认付款时间” TextView
//			RelativeLayout.LayoutParams textLayout1 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextView textview1 = new TextView(this);
//			textview1.setText("确认付款时间:");
//			textview1.setTextSize(16);
//			textview1.setTextColor(Color.parseColor("#000000"));
//			textLayout1.setMargins(34 * densityDpi / 320, 0, 0,
//					20 * densityDpi / 320);
//			textLayout1.addRule(RelativeLayout.BELOW, view.getId());
//			textLayout1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			textview1.setLayoutParams(textLayout1);
//			mRelativeElement.addView(textview1); // 加载 “确认付款时间” TextView(完成)
//
//			// 创建 “付款时间” TextView
//			RelativeLayout.LayoutParams textLayout2 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextPaymentTime = new TextView(this);
//			int paytimeId = 2;
//			mTextPaymentTime.setId(paytimeId);
//			mTextPaymentTime.setText("09:13:24");
//			mTextPaymentTime.setTextSize(16);
//			mTextPaymentTime.setTextColor(Color.parseColor("#afafaf"));
//			textLayout2.setMargins(0, 0, 34 * densityDpi / 320,
//					20 * densityDpi / 320);
//			textLayout2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			textLayout2.addRule(RelativeLayout.BELOW, view.getId());
//			mTextPaymentTime.setLayoutParams(textLayout2);
//			mRelativeElement.addView(mTextPaymentTime);// 加载“付款时间” TextView(完成)
//
//			// 创建“付款日期” TextView
//			RelativeLayout.LayoutParams textLayout3 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextPaymentDate = new TextView(this);
//			mTextPaymentDate.setId(3);
//			mTextPaymentDate.setText("2016-05-20");
//			mTextPaymentDate.setTextSize(16);
//			mTextPaymentDate.setTextColor(Color.parseColor("#afafaf"));
//			textLayout3.setMargins(0, 0, 20 * densityDpi / 320,
//					20 * densityDpi / 320);
//			textLayout3.addRule(RelativeLayout.LEFT_OF,
//					mTextPaymentTime.getId());
//			MyLog.i("yang", mTextPaymentTime.getId() + "");
//			textLayout3.addRule(RelativeLayout.BELOW, view.getId());
//			mTextPaymentDate.setLayoutParams(textLayout3);
//			mRelativeElement.addView(mTextPaymentDate);// 加载“付款日期” TextView(完成)
//
//			// 创建 “确认付款时间： 2016-05-20 19：13：24” 下的 横线 view
//			View gapview = new View(this);
//
//			RelativeLayout.LayoutParams gapviewLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 1);
//
//			gapviewLayout.addRule(RelativeLayout.BELOW,
//					mTextPaymentDate.getId());
//			gapviewLayout.setMargins(0, 0, 0, 20 * densityDpi / 320);
//			gapview.setId(3000);
//			gapview.setBackgroundColor(Color.parseColor("#7f7f7f"));
//			gapview.setLayoutParams(gapviewLayout);
//			mRelativeElement.addView(gapview);// 加载“确认付款时间： 2016-05-20 19：13：24”
//												// 下的 横线 view(完成)
//
//			// 创建 “开始服务时间” TextView
//			RelativeLayout.LayoutParams Layoutserve = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextView Textserve = new TextView(this);
//			Textserve.setText("开始服务时间:");
//			Textserve.setTextSize(16);
//			Textserve.setTextColor(Color.parseColor("#000000"));
//			Layoutserve.setMargins(34 * densityDpi / 320, 0, 0,
//					20 * densityDpi / 320);
//			Layoutserve.addRule(RelativeLayout.BELOW, gapview.getId());
//			Layoutserve.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			Textserve.setLayoutParams(Layoutserve);
//			mRelativeElement.addView(Textserve); // 加载 “开始服务时间” TextView(完成)
//
//			// 创建 “开始服务时间” TextView
//			RelativeLayout.LayoutParams LayoutserveTime = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextServeTime = new TextView(this);
//			mTextServeTime.setId(3001);
//			mTextServeTime.setText("09:13:24");
//			mTextServeTime.setTextSize(16);
//			mTextServeTime.setTextColor(Color.parseColor("#afafaf"));
//			LayoutserveTime.setMargins(0, 0, 34 * densityDpi / 320,
//					20 * densityDpi / 320);
//			LayoutserveTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			LayoutserveTime.addRule(RelativeLayout.BELOW, gapview.getId());
//			mTextServeTime.setLayoutParams(LayoutserveTime);
//			mRelativeElement.addView(mTextServeTime);// 加载 “开始服务时间” TextView(完成)
//
//			// 创建“付款日期” TextView
//			RelativeLayout.LayoutParams LayoutserveDate = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextServeDate = new TextView(this);
//			mTextServeDate.setId(3002);
//			mTextServeDate.setText("2016-05-20");
//			mTextServeDate.setTextSize(16);
//			mTextServeDate.setTextColor(Color.parseColor("#afafaf"));
//			LayoutserveDate.setMargins(0, 0, 20 * densityDpi / 320,
//					20 * densityDpi / 320);
//			LayoutserveDate.addRule(RelativeLayout.LEFT_OF,
//					mTextServeTime.getId());
//			LayoutserveDate.addRule(RelativeLayout.BELOW, gapview.getId());
//			mTextServeDate.setLayoutParams(LayoutserveDate);
//			mRelativeElement.addView(mTextServeDate);// 加载“付款日期” TextView(完成)
//
//			// 创建“开始服务”Button
//			mBtnPay = new Button(this);
//			mBtnPay.setText("开始服务");
//			mBtnPay.setTextSize(16);
//			mBtnPay.setGravity(Gravity.CENTER);
//			mBtnPay.setBackgroundDrawable(this.getResources().getDrawable(
//					R.drawable.balance_blue));
//			LinearLayout.LayoutParams btnLayout = new LinearLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mBtnPay.setLayoutParams(btnLayout);
//			mLinearDetailsElement.addView(mBtnPay);// 加载“开始服务”Button(完成)
//		} else if (mElementType.equals("4")) {
//			mTextOrderType.setText("已完成");
//
//			// 创建 “录单时间： 2016-05-04 00：23：23” 下的 横线 view
//			View view = new View(this);
//			RelativeLayout.LayoutParams viewLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 1);
//			viewLayout.setMargins(0, 20 * densityDpi / 320, 0,
//					20 * densityDpi / 320);
//			int idview = 1;
//			view.setId(idview);
//			view.setBackgroundColor(Color.parseColor("#7f7f7f"));
//			viewLayout.addRule(RelativeLayout.BELOW, R.id.text_time);
//			view.setLayoutParams(viewLayout);
//			mRelativeElement.addView(view);// 加载“录单时间： 2016-05-04 00：23：23” 下的
//											// 横线 view(完成)
//
//			// 创建 “确认付款时间” TextView
//			RelativeLayout.LayoutParams textLayout1 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextView textview1 = new TextView(this);
//			textview1.setText("确认付款时间:");
//			textview1.setTextSize(16);
//			textview1.setTextColor(Color.parseColor("#000000"));
//			textLayout1.setMargins(34 * densityDpi / 320, 0, 0,
//					20 * densityDpi / 320);
//			textLayout1.addRule(RelativeLayout.BELOW, view.getId());
//			textLayout1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			textview1.setLayoutParams(textLayout1);
//			mRelativeElement.addView(textview1); // 加载 “确认付款时间” TextView(完成)
//
//			// 创建 “付款时间” TextView
//			RelativeLayout.LayoutParams textLayout2 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextPaymentTime = new TextView(this);
//			int paytimeId = 2;
//			mTextPaymentTime.setId(paytimeId);
//			mTextPaymentTime.setText("09:13:24");
//			mTextPaymentTime.setTextSize(16);
//			mTextPaymentTime.setTextColor(Color.parseColor("#afafaf"));
//			textLayout2.setMargins(0, 0, 34 * densityDpi / 320,
//					20 * densityDpi / 320);
//			textLayout2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			textLayout2.addRule(RelativeLayout.BELOW, view.getId());
//			mTextPaymentTime.setLayoutParams(textLayout2);
//			mRelativeElement.addView(mTextPaymentTime);// 加载“付款时间” TextView(完成)
//
//			// 创建“付款日期” TextView
//			RelativeLayout.LayoutParams textLayout3 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextPaymentDate = new TextView(this);
//			mTextPaymentDate.setId(3);
//			mTextPaymentDate.setText("2016-05-20");
//			mTextPaymentDate.setTextSize(16);
//			mTextPaymentDate.setTextColor(Color.parseColor("#afafaf"));
//			textLayout3.setMargins(0, 0, 20 * densityDpi / 320,
//					20 * densityDpi / 320);
//			textLayout3.addRule(RelativeLayout.LEFT_OF,
//					mTextPaymentTime.getId());
//			textLayout3.addRule(RelativeLayout.BELOW, view.getId());
//			mTextPaymentDate.setLayoutParams(textLayout3);
//			mRelativeElement.addView(mTextPaymentDate);// 加载“付款日期” TextView(完成)
//
//			// 创建 “确认付款时间： 2016-05-20 19：13：24” 下的 横线 view
//			View gapview = new View(this);
//
//			RelativeLayout.LayoutParams gapviewLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 1);
//
//			gapviewLayout.addRule(RelativeLayout.BELOW,
//					mTextPaymentDate.getId());
//			gapviewLayout.setMargins(0, 0, 0, 20 * densityDpi / 320);
//			gapview.setId(4000);
//			gapview.setBackgroundColor(Color.parseColor("#7f7f7f"));
//			gapview.setLayoutParams(gapviewLayout);
//			mRelativeElement.addView(gapview);// 加载“确认付款时间： 2016-05-20 19：13：24”
//												// 下的 横线 view(完成)
//
//			// 创建 “开始服务时间” TextView
//			RelativeLayout.LayoutParams Layoutserve = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextView Textserve = new TextView(this);
//			Textserve.setText("开始服务时间:");
//			Textserve.setTextSize(16);
//			Textserve.setTextColor(Color.parseColor("#000000"));
//			Layoutserve.setMargins(34 * densityDpi / 320, 0, 0,
//					20 * densityDpi / 320);
//			Layoutserve.addRule(RelativeLayout.BELOW, gapview.getId());
//			Layoutserve.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			Textserve.setLayoutParams(Layoutserve);
//			mRelativeElement.addView(Textserve); // 加载 “开始服务时间” TextView(完成)
//
//			// 创建 “开始服务时间” TextView
//			RelativeLayout.LayoutParams LayoutserveTime = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextServeTime = new TextView(this);
//			mTextServeTime.setId(4001);
//			mTextServeTime.setText("09:13:24");
//			mTextServeTime.setTextSize(16);
//			mTextServeTime.setTextColor(Color.parseColor("#afafaf"));
//			LayoutserveTime.setMargins(0, 0, 34 * densityDpi / 320,
//					20 * densityDpi / 320);
//			LayoutserveTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			LayoutserveTime.addRule(RelativeLayout.BELOW, gapview.getId());
//			mTextServeTime.setLayoutParams(LayoutserveTime);
//			mRelativeElement.addView(mTextServeTime);// 加载 “开始服务时间” TextView(完成)
//
//			// 创建“开始服务日期” TextView
//			RelativeLayout.LayoutParams LayoutserveDate = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextServeDate = new TextView(this);
//			mTextServeDate.setId(4002);
//			mTextServeDate.setText("2016-05-20");
//			mTextServeDate.setTextSize(16);
//			mTextServeDate.setTextColor(Color.parseColor("#afafaf"));
//			LayoutserveDate.setMargins(0, 0, 20 * densityDpi / 320,
//					20 * densityDpi / 320);
//			LayoutserveDate.addRule(RelativeLayout.LEFT_OF,
//					mTextServeTime.getId());
//			LayoutserveDate.addRule(RelativeLayout.BELOW, gapview.getId());
//			mTextServeDate.setLayoutParams(LayoutserveDate);
//			mRelativeElement.addView(mTextServeDate);// 加载“开始服务” TextView(完成)
//
//			// 创建 “完成服务时间： 2016-05-20 09：13：24” 下的 横线 view
//			View okserveView = new View(this);
//			RelativeLayout.LayoutParams okserveLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 1);
//			okserveView.setId(4003);
//			okserveView.setBackgroundColor(Color.parseColor("#7f7f7f"));
//			okserveLayout.setMargins(0, 0, 0, 20 * densityDpi / 320);
//			okserveLayout.addRule(RelativeLayout.BELOW, mTextServeDate.getId());
//			okserveView.setLayoutParams(okserveLayout);
//			mRelativeElement.addView(okserveView);// 加载“完成服务时间： 2016-05-20
//													// 09：13：24” 下的 横线 view(完成)
//
//			// 创建 “完成服务时间 ” TextView
//			RelativeLayout.LayoutParams LayoutokServe = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextView TextokServe = new TextView(this);
//			TextokServe.setText("完成服务时间:");
//			TextokServe.setTextSize(16);
//			TextokServe.setTextColor(Color.parseColor("#000000"));
//			LayoutokServe.setMargins(34 * densityDpi / 320, 0, 0,
//					20 * densityDpi / 320);
//			LayoutokServe.addRule(RelativeLayout.BELOW, okserveView.getId());
//			LayoutokServe.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			TextokServe.setLayoutParams(LayoutokServe);
//			mRelativeElement.addView(TextokServe); // 加载 “完成服务时间 ” TextView(完成)
//
//			// 创建 “完成服务时间 09:13:24” TextView
//			RelativeLayout.LayoutParams LayoutokServeTime = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextokServeTime = new TextView(this);
//			mTextokServeTime.setId(4004);
//			mTextokServeTime.setText("09:13:24");
//			mTextokServeTime.setTextSize(16);
//			mTextokServeTime.setTextColor(Color.parseColor("#afafaf"));
//			LayoutokServeTime.setMargins(0, 0, 34 * densityDpi / 320,
//					20 * densityDpi / 320);
//			LayoutokServeTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			LayoutokServeTime
//					.addRule(RelativeLayout.BELOW, okserveView.getId());
//			mTextokServeTime.setLayoutParams(LayoutokServeTime);
//			mRelativeElement.addView(mTextokServeTime);// 加载 “完成服务时间 09:13:24”
//														// TextView(完成)
//
//			// 创建“完成服务日期” TextView
//
//			RelativeLayout.LayoutParams LayoutokServeDate = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextokServeDate = new TextView(this);
//			mTextokServeDate.setId(4005);
//			mTextokServeDate.setText("2016-05-20");
//			mTextokServeDate.setTextSize(16);
//			mTextokServeDate.setTextColor(Color.parseColor("#afafaf"));
//			LayoutokServeDate.setMargins(0, 0, 20 * densityDpi / 320,
//					20 * densityDpi / 320);
//			LayoutokServeDate.addRule(RelativeLayout.LEFT_OF,
//					mTextokServeTime.getId());
//			LayoutokServeDate
//					.addRule(RelativeLayout.BELOW, okserveView.getId());
//			mTextokServeDate.setLayoutParams(LayoutokServeDate);
//			mRelativeElement.addView(mTextokServeDate);// 加载“完成服务日期”
//														// TextView(完成)
//
//		} else if (mElementType.equals("5")) {
//			mTextOrderType.setText("撤销订单");
//
//			// 创建 “录单时间： 2016-05-04 00：23：23” 下的 横线 view
//			View view = new View(this);
//			RelativeLayout.LayoutParams viewLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 1);
//			viewLayout.setMargins(0, 20 * densityDpi / 320, 0,
//					20 * densityDpi / 320);
//			int idview = 1;
//			view.setId(idview);
//			view.setBackgroundColor(Color.parseColor("#7f7f7f"));
//			viewLayout.addRule(RelativeLayout.BELOW, R.id.text_time);
//			view.setLayoutParams(viewLayout);
//			mRelativeElement.addView(view);// 加载“录单时间： 2016-05-04 00：23：23” 下的
//											// 横线 view(完成)
//
//			// 创建 “撤销订单时间” TextView
//			RelativeLayout.LayoutParams textLayout1 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextView textview1 = new TextView(this);
//			textview1.setText("撤销订单时间：");
//			textview1.setTextSize(16);
//			textview1.setTextColor(Color.parseColor("#000000"));
//			textLayout1.setMargins(34 * densityDpi / 320, 0, 0,
//					20 * densityDpi / 320);
//			textLayout1.addRule(RelativeLayout.BELOW, view.getId());
//			textLayout1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			textview1.setLayoutParams(textLayout1);
//			mRelativeElement.addView(textview1); // 加载 “撤销订单时间” TextView(完成)
//
//			// 创建 “撤销订单时间” TextView
//			RelativeLayout.LayoutParams textLayout2 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextPaymentTime = new TextView(this);
//			int paytimeId = 2;
//			mTextPaymentTime.setId(paytimeId);
//			mTextPaymentTime.setText("09:13:24");
//			mTextPaymentTime.setTextSize(16);
//			mTextPaymentTime.setTextColor(Color.parseColor("#afafaf"));
//			textLayout2.setMargins(0, 0, 34 * densityDpi / 320,
//					20 * densityDpi / 320);
//			textLayout2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			textLayout2.addRule(RelativeLayout.BELOW, view.getId());
//			mTextPaymentTime.setLayoutParams(textLayout2);
//			mRelativeElement.addView(mTextPaymentTime);// 加载“撤销订单时间”
//														// TextView(完成)
//
//			// 创建“撤销订单日期” TextView
//			RelativeLayout.LayoutParams textLayout3 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextPaymentDate = new TextView(this);
//			mTextPaymentDate.setText("2016-05-20");
//			mTextPaymentDate.setTextSize(16);
//			mTextPaymentDate.setTextColor(Color.parseColor("#afafaf"));
//			textLayout3.setMargins(0, 0, 20 * densityDpi / 320,
//					20 * densityDpi / 320);
//			textLayout3.addRule(RelativeLayout.LEFT_OF,
//					mTextPaymentTime.getId());
//			MyLog.i("yang", mTextPaymentTime.getId() + "");
//			textLayout3.addRule(RelativeLayout.BELOW, view.getId());
//			mTextPaymentDate.setLayoutParams(textLayout3);
//			mRelativeElement.addView(mTextPaymentDate);// 加载“撤销订单日期”
//														// TextView(完成)
//
//			// 创建“撤销原因：” TextView
//			TextView textRepealCause = new TextView(this);
//
//			textRepealCause.setText("撤销原因：");
//			textRepealCause.setTextSize(16);
//			RelativeLayout.LayoutParams LayoutRepealCause = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			textRepealCause.setPadding(34 * densityDpi / 320, 20, 0,
//					20 * densityDpi / 320);
//
//			textRepealCause.setGravity(Gravity.LEFT);
//			textRepealCause.setLayoutParams(LayoutRepealCause);
//			mLinearDetailsElement.addView(textRepealCause);
//
//			// 创建“撤销原因” 内容 RelativeLayout
//
//			RelativeLayout.LayoutParams RelativeRepealCause = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 240 * densityDpi / 320);
//
//			RelativeLayout RelativeRepeal = new RelativeLayout(this);
//			RelativeRepeal.setPadding(34 * densityDpi / 320,
//					34 * densityDpi / 320, 34 * densityDpi / 320,
//					34 * densityDpi / 320);
//			RelativeRepeal.setBackgroundColor(Color.parseColor("#ffffff"));
//
//			RelativeLayout.LayoutParams TextRepealCause = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextRepeal = new TextView(this);
//			TextRepeal.setText("说法说法师傅阿瓦尔师傅斯蒂芬师傅阿斯蒂芬师傅发速度");
//			TextRepeal.setLayoutParams(TextRepealCause);
//			RelativeRepeal.addView(TextRepeal);
//			RelativeRepeal.setLayoutParams(RelativeRepealCause);
//			mLinearDetailsElement.addView(RelativeRepeal);
//
//		} else if (mElementType.equals("6")) {
//			mTextOrderType.setText("退款订单");
//			mTextOrderServices.setText(10000.00 + "元");
//			mTextDemo.setText("订单金额：");
//
//			// 创建“服务项目”上方的横线View
//			RelativeLayout.LayoutParams ViewmoneyLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 1);
//			View viewMoney = new View(this);
//			viewMoney.setId(6000);
//			viewMoney.setBackgroundColor(Color.parseColor("#7f7f7f"));
//			ViewmoneyLayout.addRule(RelativeLayout.BELOW, R.id.text_services);
//			ViewmoneyLayout.setMargins(0, 20 * densityDpi / 320, 0,
//					20 * densityDpi / 320);
//			viewMoney.setLayoutParams(ViewmoneyLayout);
//			mRelativeMoney.addView(viewMoney);
//
//			RelativeLayout.LayoutParams demoLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//
//			TextView textDemo = new TextView(this);
//			textDemo.setText("服务项目：");
//			textDemo.setTextSize(16);
//			textDemo.setTextColor(Color.parseColor("#000000"));
//			demoLayout.setMargins(34 * densityDpi / 320, 0, 0, 0);
//			demoLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			demoLayout.addRule(RelativeLayout.BELOW, viewMoney.getId());
//			textDemo.setLayoutParams(demoLayout);
//			mRelativeMoney.addView(textDemo);
//
//			RelativeLayout.LayoutParams moneyLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//
//			TextView textServerType = new TextView(this);
//			textServerType.setText("违章处理");
//			textServerType.setTextSize(16);
//			textServerType.setTextColor(Color.parseColor("#000000"));
//			moneyLayout.setMargins(0, 0, 34 * densityDpi / 320, 0);
//			moneyLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			moneyLayout.addRule(RelativeLayout.BELOW, viewMoney.getId());
//			textServerType.setLayoutParams(moneyLayout);
//			mRelativeMoney.addView(textServerType);
//
//			// 创建 “录单时间： 2016-05-04 00：23：23” 下的 横线 view
//			View view = new View(this);
//			RelativeLayout.LayoutParams viewLayout = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 1);
//			viewLayout.setMargins(0, 20 * densityDpi / 320, 0,
//					20 * densityDpi / 320);
//			int idview = 1;
//			view.setId(idview);
//			view.setBackgroundColor(Color.parseColor("#7f7f7f"));
//			viewLayout.addRule(RelativeLayout.BELOW, R.id.text_time);
//			view.setLayoutParams(viewLayout);
//			mRelativeElement.addView(view);// 加载“录单时间： 2016-05-04 00：23：23” 下的
//											// 横线 view(完成)
//
//			// 创建 “退款订单时间” TextView
//			RelativeLayout.LayoutParams textLayout1 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextView textview1 = new TextView(this);
//			textview1.setText("退款订单时间：");
//			textview1.setTextSize(16);
//			textview1.setTextColor(Color.parseColor("#000000"));
//			textLayout1.setMargins(34 * densityDpi / 320, 0, 0,
//					20 * densityDpi / 320);
//			textLayout1.addRule(RelativeLayout.BELOW, view.getId());
//			textLayout1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			textview1.setLayoutParams(textLayout1);
//			mRelativeElement.addView(textview1); // 加载“退款订单时间” TextView(完成)
//
//			// 创建 “退款订单时间” TextView
//			RelativeLayout.LayoutParams textLayout2 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextPaymentTime = new TextView(this);
//			int paytimeId = 2;
//			mTextPaymentTime.setId(paytimeId);
//			mTextPaymentTime.setText("09:13:24");
//			mTextPaymentTime.setTextSize(16);
//			mTextPaymentTime.setTextColor(Color.parseColor("#afafaf"));
//			textLayout2.setMargins(0, 0, 34 * densityDpi / 320,
//					20 * densityDpi / 320);
//			textLayout2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			textLayout2.addRule(RelativeLayout.BELOW, view.getId());
//			mTextPaymentTime.setLayoutParams(textLayout2);
//			mRelativeElement.addView(mTextPaymentTime);// 加载“退款订单时间”
//														// TextView(完成)
//
//			// 创建“退款订单日期” TextView
//			RelativeLayout.LayoutParams textLayout3 = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			mTextPaymentDate = new TextView(this);
//			mTextPaymentDate.setText("2016-05-20");
//			mTextPaymentDate.setTextSize(16);
//			mTextPaymentDate.setTextColor(Color.parseColor("#afafaf"));
//			textLayout3.setMargins(0, 0, 20 * densityDpi / 320,
//					20 * densityDpi / 320);
//			textLayout3.addRule(RelativeLayout.LEFT_OF,
//					mTextPaymentTime.getId());
//			MyLog.i("yang", mTextPaymentTime.getId() + "");
//			textLayout3.addRule(RelativeLayout.BELOW, view.getId());
//			mTextPaymentDate.setLayoutParams(textLayout3);
//			mRelativeElement.addView(mTextPaymentDate);// 加载“退款订单日期”
//														// TextView(完成)
//
//			// 创建“退款原因：” TextView
//			TextView textRepealCause = new TextView(this);
//
//			textRepealCause.setText("退款原因：");
//			textRepealCause.setTextSize(16);
//			RelativeLayout.LayoutParams LayoutRepealCause = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			// LayoutRepealCause.setMargins(34, 20, 0, 20);
//			textRepealCause.setPadding(34 * densityDpi / 320,
//					20 * densityDpi / 320, 0, 20 * densityDpi / 320);
//
//			textRepealCause.setGravity(Gravity.LEFT);
//			textRepealCause.setLayoutParams(LayoutRepealCause);
//			mLinearDetailsElement.addView(textRepealCause);
//
//			// 创建“退款原因” 内容 RelativeLayout
//
//			RelativeLayout.LayoutParams RelativeRepealCause = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT, 240 * densityDpi / 320);
//
//			RelativeLayout RelativeRepeal = new RelativeLayout(this);
//			RelativeRepeal.setPadding(34 * densityDpi / 320,
//					34 * densityDpi / 320, 34 * densityDpi / 320,
//					34 * densityDpi / 320);
//			RelativeRepeal.setBackgroundColor(Color.parseColor("#ffffff"));
//
//			RelativeLayout.LayoutParams TextRepealCause = new RelativeLayout.LayoutParams(
//					ViewGroup.LayoutParams.WRAP_CONTENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextRepeal = new TextView(this);
//			TextRepeal.setText("说法说法师傅阿瓦尔师傅斯蒂芬师傅阿斯蒂芬师傅发速度");
//			TextRepeal.setTextColor(Color.parseColor("#ffffff"));
//			TextRepeal.setLayoutParams(TextRepealCause);
//			RelativeRepeal.addView(TextRepeal);
//			RelativeRepeal.setLayoutParams(RelativeRepealCause);
//			mLinearDetailsElement.addView(RelativeRepeal);
//
//		}
//
//	}

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finish();
                break;

            case R.id.sub_header_bar_right_tv:

                Intent intent = new Intent();

                if (mElementType.equals("1")) {
                    intent.setClass(OrderDetailsElementActivity.this,
                            RepealOrderActivity.class);
                } else if (mElementType.equals("2") || mElementType.equals("3")) {
                    intent.setClass(OrderDetailsElementActivity.this,
                            RefundOrderActivity.class);
                }

                startActivity(intent);

                break;

            default:
                break;
        }

    }
}
