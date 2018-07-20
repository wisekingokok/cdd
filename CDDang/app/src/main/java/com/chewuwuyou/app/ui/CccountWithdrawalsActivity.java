package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.barcode.view.SweepListView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.AccountWithdrawalsAdapter;
import com.chewuwuyou.app.bean.AccountWithdrawal;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.WalletUtil;

/**
 * @version 1.1.0
 * @describe:账户提现列表
 * @author:liuchun
 */
public class CccountWithdrawalsActivity extends CDDBaseActivity implements
        OnClickListener {

    private SweepListView mSwipeListView;// qq侧滑并列表显示
    private AccountWithdrawalsAdapter mAccountAdapter;// 适配器
    private List<AccountWithdrawal> mAccountList;// 账户集合
    private AccountWithdrawal mWithdrawal;// 账户对象
    private ImageButton mSubHeaderBarLeftIbtn;
    private TextView mSubHeaderBarTv;// 标题

    private TextView mAccountName, mAccountPhone;// 名称，电话
    // private ImageView accountImg, ccountRadio;// 图片
    private LinearLayout mDefaultAccount;// 默认账户
    private TextView mAddAccountTV;// 添加账户
    private String mUpadteAccountNo;// 修改的默认账户
    private RelativeLayout mTitleHeight;// 标题布局高度
    private TextView mTextView_mianze;//免责声明
    /**
     * 更新数据
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.SEND_ADAPTER:
                    Bundle bundle = msg.getData();
                    mAccountName.setText(bundle.getString("name"));
                    mUpadteAccountNo = bundle.getString("phone");
                    WalletUtil.showAccount(mUpadteAccountNo, mAccountPhone);
                    break;
                case Constant.SEND_Handler:
                    MyLog.i("YUY", "收到删除消息");
                    AjaxParams params = new AjaxParams();
                    params.put("accountNo", msg.obj.toString());
                    getDeleteAccount(params);// 根据传递的账户名称删除数据
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_withdrawals_ac);

        initView();// 初始化控件
        initData();// 逻辑处理、
        initEvent();// 事件监听
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mTextView_mianze = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mTextView_mianze.setVisibility(View.VISIBLE);
        mTextView_mianze.setText("免责声明");
        mAccountList = new ArrayList<AccountWithdrawal>();// 账户集合
        mSwipeListView = (SweepListView) findViewById(R.id.swipe_list);// 列表
        mAddAccountTV = (TextView) findViewById(R.id.textView_tianjia);
        mAddAccountTV.setVisibility(View.VISIBLE);// 添加支付宝账户
        mAddAccountTV.setText("添加");
        mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mSubHeaderBarTv = (TextView) findViewById(R.id.sub_header_bar_tv);
        mAccountName = (TextView) findViewById(R.id.account_name);// 名称
        mAccountPhone = (TextView) findViewById(R.id.account_phone);// 电话
        mDefaultAccount = (LinearLayout) findViewById(R.id.default_account);// 默认账户
        mSubHeaderBarTv.setText("提现账户管理");
        mSwipeListView.setFocusable(false);// 让listview失去焦点
        mSwipeListView.smoothScrollToPosition(0, 20);// 设置显示的位置
    }

    /**
     * onClick点击事件
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_right_tv:// 免责声明

                intent = new Intent(CccountWithdrawalsActivity.this,
                        AgreementActivity.class);
                intent.putExtra("type", 12);
                startActivity(intent);

                break;
            case R.id.sub_header_bar_left_ibtn:// 返回上一页
                if (!TextUtils.isEmpty(mUpadteAccountNo)) {
                    AjaxParams params = new AjaxParams();
                    params.put("accountNo", mUpadteAccountNo);
                    getUpdateAccount(params);
                    finishActivity();
                }

                break;
            case R.id.textView_tianjia://添加账户

                if (mAccountList.size() == 2) {
                    ToastUtil.toastShow(CccountWithdrawalsActivity.this,
                            "最多只能添加3个提现账户");
                } else {
                    intent = new Intent(CccountWithdrawalsActivity.this,
                            AddCcountWithdrawalsActivity.class);
                    startActivity(intent);
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
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        // mAccountAdapter = new AccountWithdrawalsAdapter(
        // CccountWithdrawalsActivity.this, mAccountList, mHandler);
        // mSwipeListView.setAdapter(mAccountAdapter);
        // setListViewHeightBasedOnChildren(mSwipeListView);// 动态设置listview的高度
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtils.isEmpty(mUpadteAccountNo)) {
                AjaxParams params = new AjaxParams();
                params.put("accountNo", mUpadteAccountNo);
                getUpdateAccount(params);
                finishActivity();
            }
        }
        return false;
    }

    /**
     * 监听方法
     */
    @Override
    protected void initEvent() {

        mTextView_mianze.setOnClickListener(this);
        mAddAccountTV.setOnClickListener(this);// 添加賬戶
        mSubHeaderBarLeftIbtn.setOnClickListener(this);// 返回上一頁
        mSwipeListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                DisplayMetrics metric = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metric);
                int density = metric.densityDpi;

                int d = position * (76 * density / 240); // 移动的dp density每个手机的像素
                // 240当前手机的像素
                int f = position * (8 * density / 240);
                int sd = position * (30 * density / 240);

                Animation translateIn = new TranslateAnimation(0, 0, 0,
                        -(d + f + sd));
                translateIn.setDuration(500);
                translateIn.setFillAfter(true);
                view.startAnimation(translateIn);

                Bundle bundle = new Bundle();
                bundle.putString("name", mAccountList.get(position - 1)
                        .getAccountName());
                bundle.putString("phone", mAccountList.get(position - 1)
                        .getAccountPhone());
                Message message = new Message();
                message.what = Constant.SEND_ADAPTER;
                message.setData(bundle);

                mAccountList.remove(position - 1);

                mHandler.sendMessage(message);// 发送消息
                mAccountList.add(0, new AccountWithdrawal(mAccountName
                        .getText().toString(), mUpadteAccountNo));

                mAccountAdapter = new AccountWithdrawalsAdapter(
                        CccountWithdrawalsActivity.this, mAccountList, mHandler);
                mSwipeListView.setAdapter(mAccountAdapter);
                setListViewHeightBasedOnChildren(mSwipeListView);// 动态设置listview的高度
            }
        });

    }

    public static void setListViewHeightBasedOnChildren(SweepListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 访问网络数据获取所有账户
     */
    private void getAllAccount() {

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("YUY", "提现账户数据  = " + msg.obj.toString());
                        // {"result":1,"data":[{"no":"123456688","name":"绞尽脑汁"},{"no":"123456","name":"理科"}]}
                        try {
                            JSONArray jArray = new JSONArray(msg.obj.toString());
                            mAccountList.clear();
                            if (jArray.length() == 0) {
                                ToastUtil.toastShow(
                                        CccountWithdrawalsActivity.this, "暂无账户信息");
                                return;
                            }
                            mDefaultAccount.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jArray.length(); i++) {

                                MyLog.i("YUY", "name = "
                                        + jArray.getJSONObject(i).getString("name")
                                        + " no = "
                                        + jArray.getJSONObject(i).getString("no"));

                                mWithdrawal = new AccountWithdrawal(jArray
                                        .getJSONObject(i).getString("name"), jArray
                                        .getJSONObject(i).getString("no"));
                                mAccountList.add(mWithdrawal);
                            }
                            mAccountName.setText(jArray.getJSONObject(0).getString(
                                    "name"));
                            mUpadteAccountNo = jArray.getJSONObject(0).getString(
                                    "no");
                            WalletUtil.showAccount(mUpadteAccountNo, mAccountPhone);
                            mAccountList.remove(0);
                            mAccountAdapter = new AccountWithdrawalsAdapter(
                                    CccountWithdrawalsActivity.this, mAccountList,
                                    mHandler);
                            mSwipeListView.setAdapter(mAccountAdapter);
                            setListViewHeightBasedOnChildren(mSwipeListView);// 动态设置listview的高度
                            // mAccountAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        }, null, NetworkUtil.GET_ALL_WITHDRAWALS, false, 1);
    }

    /**
     * 删除账户
     */
    private void getDeleteAccount(AjaxParams params) {

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mAccountList.remove(Constant.CCCOUNT_WITHDRAWLS);
                    /*
                     * MyLog.i("YUY", "删除提现账户 = " + msg.obj.toString());
					 * mAccountAdapter.notifyDataSetChanged();
					 */

                        mAccountAdapter = new AccountWithdrawalsAdapter(
                                CccountWithdrawalsActivity.this, mAccountList,
                                mHandler);
                        mSwipeListView.setAdapter(mAccountAdapter);
                    /*
                     * getAllAccount();
					 */
                        break;
                }
            }
        }, params, NetworkUtil.DELETE_WITHDRAWALS, false, 0);
    }

    /**
     * 修改默认账户
     */
    public void getUpdateAccount(AjaxParams params) {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("YUY", "设置默认账户 = " + msg.obj.toString());
                        break;
                }
            }
        }, params, NetworkUtil.GET_DEFAULT_WITHDRAWALS, false, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAccount();// 访问网络获取所有网络
    }
}
