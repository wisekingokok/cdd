package com.chewuwuyou.app.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CarAccountBookListAdapter;
import com.chewuwuyou.app.utils.DBHelper;
import com.chewuwuyou.app.utils.DBHelper.DBFIELD;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.ToastUtil;

public class CarAccountDetailsActivity extends CDDBaseActivity {

    private TextView mNaviBarTitle;
    private ImageButton mNaviBarBack;
    private TextView mDateStartTV, mDateEndTV;// 选择日期
    private Calendar mCalendar;
    // private TextView mRemark;// 提示信息；
    private TextView mType;// 消费类型
    // 账本List
    private ListView mCarAccountList;
    private CarAccountBookListAdapter mCarAccountBookListAdapter;
    private String startDate = "", endDate = "", categoryStr = "所有";// 消费月份和消费时间
    private ImageButton mSjtbBtn;// 查看数据图表
    private TextView mCategorySumTV;// 消费总额：根据选择的起始时间计算总额
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private RelativeLayout mTitleHeight;// 标题布局高度
    private boolean mEject = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.details_ac);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(CarAccountDetailsActivity.this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatService.onPause(CarAccountDetailsActivity.this);
    }

    /**
     * 初始化布局
     */
    private void init() {

        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.chewuwuyou.app.ui.CarAccountDetailsActivity");// 接受传递过来的广播

        this.registerReceiver(mReceiver, intentFilter);

        mNaviBarTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
        mNaviBarTitle.setText(R.string.details);
        mNaviBarBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mDateStartTV = (TextView) findViewById(R.id.date_start_tv);
        mDateEndTV = (TextView) findViewById(R.id.date_end_tv);
        // mRemark = (TextView) findViewById(R.id.remark);
        mCarAccountList = (ListView) findViewById(R.id.categoryList);
        mCategorySumTV = (TextView) findViewById(R.id.category_sum_tv);
        mType = (TextView) findViewById(R.id.type);
        mSjtbBtn = (ImageButton) findViewById(R.id.sub_header_bar_right_ibtn);
        mSjtbBtn.setVisibility(View.VISIBLE);
        mSjtbBtn.setImageResource(R.drawable.btn_tj);
        mSjtbBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                StatService.onEvent(CarAccountDetailsActivity.this,
                        "clickSjtbBtn", "点击数据图表按钮");
                Intent intent = new Intent(CarAccountDetailsActivity.this,
                        DataChartActivity.class);
                startActivity(intent);
            }
        });
        reloadListView();

        // 返回
        mNaviBarBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finishActivity();
            }
        });

        mType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                StatService.onEvent(CarAccountDetailsActivity.this,
                        "clickSjtbType", "点击数据图表消费类型");
                Intent intent = new Intent(CarAccountDetailsActivity.this,
                        RecordSelectCategoryActivity.class);
                intent.putExtra("all", 1);
                startActivityForResult(intent, 20);
            }
        });

        // 选择开始时间
        mDateStartTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                StatService.onEvent(CarAccountDetailsActivity.this,
                        "clickSjtbStartDate", "点击数据图表开始时间");
                createDatePickerDialog();
            }

        });
        // 选择结束时间
        mDateEndTV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                StatService.onEvent(CarAccountDetailsActivity.this, "clickSjtbEndDate", "点击数据图表结束时间");
                createDatePickerDialog1();
                mEject = false;
            }
        });
        // 给ListView选项设置响应
        mCarAccountList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                List<Map<String, String>> carAccountBook = mCarAccountBookListAdapter
                        .getCarAccountBook();
                Intent intent = new Intent(CarAccountDetailsActivity.this,
                        ConsumptionDetailsActivity.class);
                // intent.putExtra("type", "del");
                intent.putExtra(DBFIELD.ID,
                        carAccountBook.get(arg2).get(DBFIELD.ID));
                intent.putExtra(DBFIELD.MONEY,
                        carAccountBook.get(arg2).get(DBFIELD.MONEY));
                intent.putExtra(DBFIELD.CATEGORY,
                        carAccountBook.get(arg2).get(DBFIELD.CATEGORY));
                intent.putExtra(DBFIELD.TIME,
                        carAccountBook.get(arg2).get(DBFIELD.TIME));
                intent.putExtra(DBFIELD.COMMENT,
                        carAccountBook.get(arg2).get(DBFIELD.COMMENT));
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (30 == resultCode) {
            mType.setText(data.getStringExtra("choicecategory"));
            categoryStr = data.getStringExtra("choicecategory");
            // DBHelper dh = new DBHelper(CarAccountDetailsActivity.this);
            // mCarAccountBookListAdapter.setCarAccountBook(dh
            // .queryCarBookWithCategoryAndBetweenMonth(categoryStr, startDate,
            // endDate));
            // dh.close();
            // mCarAccountBookListAdapter.notifyDataSetChanged();
            whenDataSet();

        }
    }

    private void reloadListView() {
        // 默认显示所有
        // 判断是按月份还是总数
        // String monthOrTotal = getIntent().getStringExtra("type");
        // if (monthOrTotal.equals("month")) {
        // mRemark.setText("点击按钮选择月份");

        // 初始ListView内容
        // DBHelper dh = new DBHelper(this);
        // SimpleDateFormat CashierInputFilter = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        // String date = CashierInputFilter.format(new java.util.Date());
        // mCarAccountBookListAdapter = new CarAccountBookListAdapter(this,
        // dh.queryCarBookWithMonth(date));
        // mCarAccountList.setAdapter(mCarAccountBookListAdapter);
        DBHelper dh = new DBHelper(this);
        mCarAccountBookListAdapter = new CarAccountBookListAdapter(this,
                dh.loadAll());
        mCarAccountList.setAdapter(mCarAccountBookListAdapter);
        double sumMoney = 0.0;
        for (int i = 0, size = dh.loadAll().size(); i < size; i++) {
            sumMoney += Double.valueOf(dh.loadAll().get(i).get("money"));
        }
        if (sumMoney > 0) {
            mCategorySumTV.setText(getString(R.string.price_contain_suffix,
                    sumMoney));
            ;
        } else {
            mCategorySumTV.setText("0.0元");
        }
        dh.close();

        // } else if (monthOrTotal.equals("total")) {
        // mRemark.setVisibility(View.GONE);
        // mDate.setVisibility(View.GONE);
        // // 初始ListView内容
        // DBHelper dh = new DBHelper(this);
        // mCarAccountBookListAdapter = new CarAccountBookListAdapter(this,
        // dh.loadAll());
        // mCarAccountList.setAdapter(mCarAccountBookListAdapter);
        // dh.close();
        // }
    }

    // 创建开始日期对话框
    private void createDatePickerDialog() {

        Dialog dialog = null;
        mCalendar = Calendar.getInstance();
        dialog = new DatePickerDialog(CarAccountDetailsActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker dp, int year, int month,
                                          int dayOfMonth) {
                        startDate = year
                                + "-"
                                + ((("" + (month + 1)).length() == 1) ? ("0" + (month + 1))
                                : ("" + (month + 1))) + "-"
                                + ((("" + dayOfMonth).length() == 1) ? ("0" + dayOfMonth)
                                : ("" + dayOfMonth));

                        mDateStartTV.setText(startDate);
                        whenDataSet();
                    }
                }, mCalendar.get(Calendar.YEAR), // 传入年份
                mCalendar.get(Calendar.MONTH), // 传入月份
                mCalendar.get(Calendar.DAY_OF_MONTH)); // 传入天数
        dialog.show();
    }

    // 创建结束对话框
    private void createDatePickerDialog1() {

        Dialog dialog = null;
        mCalendar = Calendar.getInstance();
        dialog = new DatePickerDialog(CarAccountDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int year, int month,
                                  int dayOfMonth) {

                endDate = year + "-" + ((("" + (month + 1)).length() == 1) ? ("0" + (month + 1)) : ("" + (month + 1))) + "-" + ((("" + dayOfMonth).length() == 1) ? ("0" + dayOfMonth)
                        : ("" + dayOfMonth));

                try {
                    if (mEject == false) {
                        if (dateFormat.parse(endDate).getTime() >= dateFormat.parse(startDate).getTime()) {
                            mDateEndTV.setText(endDate);
                            whenDataSet();
                            mEject = true;
                        } else {
                            mEject = true;
                            ToastUtil.toastShow(CarAccountDetailsActivity.this, "开始时间不能大于结束时间");
                            return;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, mCalendar.get(Calendar.YEAR), // 传入年份
                mCalendar.get(Calendar.MONTH), // 传入月份
                mCalendar.get(Calendar.DAY_OF_MONTH)); // 传入天数
        dialog.show();
    }

    // 设定日期时动作
    private void whenDataSet() {
        DBHelper dh = new DBHelper(CarAccountDetailsActivity.this);
        // mCarAccountBookListAdapter.setCarAccountBook(dh.queryCarBookWithMonth(monthStr));
        mCarAccountBookListAdapter.setCarAccountBook(dh
                .queryCarBookWithCategoryAndBetweenMonth(categoryStr,
                        startDate, endDate));
        double sumMoney = 0.0;
        for (int i = 0, size = dh.queryCarBookWithCategoryAndBetweenMonth(
                categoryStr, startDate, endDate).size(); i < size; i++) {
            sumMoney += Double.valueOf(dh
                    .queryCarBookWithCategoryAndBetweenMonth(categoryStr,
                            startDate, endDate).get(i).get("money"));
        }
        if (sumMoney > 0) {
            mCategorySumTV.setText(getString(R.string.price_contain_suffix,
                    sumMoney));
            ;
        } else {
            mCategorySumTV.setText("0.0元");
        }
        dh.close();
        mCarAccountBookListAdapter.notifyDataSetChanged();

    }

    /**
     * 监听广播
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action
                    .equals("com.chewuwuyou.app.ui.CarAccountDetailsActivity")) {
                DBHelper dh = new DBHelper(CarAccountDetailsActivity.this);
                mCarAccountBookListAdapter = new CarAccountBookListAdapter(
                        CarAccountDetailsActivity.this, dh.loadAll());
                mCarAccountList.setAdapter(mCarAccountBookListAdapter);
                double sumMoney = 0.0;
                for (int i = 0, size = dh.loadAll().size(); i < size; i++) {
                    sumMoney += Double
                            .valueOf(dh.loadAll().get(i).get("money"));
                }
                if (sumMoney > 0) {
                    mCategorySumTV.setText(getString(
                            R.string.price_contain_suffix, sumMoney));
                    ;
                } else {
                    mCategorySumTV.setText("0.0元");
                }
            }
        }
    };

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

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
