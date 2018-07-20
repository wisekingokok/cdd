package com.chewuwuyou.app.ui;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Balance;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.WalletUtil;

/**
 * 账单详情 zys
 */
@SuppressLint({"DefaultLocale", "HandlerLeak"})
public class BalanceRecordActivity extends CDDBaseActivity implements
        OnClickListener {

    private TextView mTitleTv; // 标题
    private ImageButton mImageBack; // 返回
    private TextView mTextRight;

    private TextView mBalanceStatusTV, mBalanceMoneyTV, mTextAymentway,
            text_shouru_tixian, text_feilv, text_fangshi;// 账单状态,账单金额,提现账户,付款方式
    /**
     * 账单图标 支付账单:显示对方头像 支付宝支付、支付宝提现：显示支付宝logo 微信支付：显示微信logo
     */
    private ImageView mImageView_icon;
    /**
     * 账单名称 提现：显示提现账户 支付宝支付、微信支付、余额支付统一收款方的名称
     */
    private TextView mTextView_name;
    private TextView mBalanceNumberTV, mBalanceTypeTV, mBalanceTimeTV,
            mBalanceOrderNum, mTextOrderBalance, mTextpingtaiBalance;// 账单流水号,账单类型，账单时间,订单号

    private Balance mBalance;// 传过来的账单实体
    private RelativeLayout mTitleHeight;// 标题布局高度

    private RelativeLayout mRelativeLiushui, mRelativeZhanghu,
            mRelativeOrdernum, mRelativeAymentway, mRelativeOrderZong,
            mRelativePingtai;// 账单流水号，提现账户，订单号,付款方式,微信手续费，订单总金额，平台服务费
    DecimalFormat df = new DecimalFormat("######0.00");

    private double mFeiLv, mPayRate;// 微信费率, 订单支付费率

    // type=6时，access=1表示支付宝支付，access=2表示微信支付

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_record_layout);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {

        mTitleTv = (TextView) findViewById(R.id.sub_header_bar_tv);
        mImageBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mTextRight = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mBalanceStatusTV = (TextView) findViewById(R.id.balance_status_tv);
        mBalanceMoneyTV = (TextView) findViewById(R.id.balance_money_tv);
        mImageView_icon = (ImageView) findViewById(R.id.balance_icon_iv);
        mTextView_name = (TextView) findViewById(R.id.balance_name_tv);
        mBalanceNumberTV = (TextView) findViewById(R.id.balance_number_tv);
        mBalanceTypeTV = (TextView) findViewById(R.id.balance_type_tv);
        mBalanceTimeTV = (TextView) findViewById(R.id.balance_time_tv);
        mRelativeZhanghu = (RelativeLayout) findViewById(R.id.relative_zhanghu);
        mRelativeOrdernum = (RelativeLayout) findViewById(R.id.relative_ordernum);
        mBalanceOrderNum = (TextView) findViewById(R.id.balance_ordernum);
        mRelativeLiushui = (RelativeLayout) findViewById(R.id.relative_liushui);
        mRelativeAymentway = (RelativeLayout) findViewById(R.id.relative_ayment_way);
        mTextAymentway = (TextView) findViewById(R.id.text_ayment_way);
        mRelativeOrderZong = (RelativeLayout) findViewById(R.id.relative_order_zong);
        mRelativePingtai = (RelativeLayout) findViewById(R.id.relative_pingtai);
        mTextOrderBalance = (TextView) findViewById(R.id.text_order_balance);
        mTextpingtaiBalance = (TextView) findViewById(R.id.text_pingtai_balance);
        text_shouru_tixian = (TextView) findViewById(R.id.text_shouru_tixian);
        text_feilv = (TextView) findViewById(R.id.text_feilv);
        text_fangshi = (TextView) findViewById(R.id.text_fangshi);
    }

    @Override
    protected void initData() {

        mBalance = (Balance) getIntent().getSerializableExtra(
                Constant.BALANCE_DETAIL_BEAN);

        setPayRates();

        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断

        // 设置账单状态

        mBalanceStatusTV.setText(WalletUtil.getWalletDetailsType(mBalance
                .getType()));

        // 设置金额颜色
        if (WalletUtil.getWalletBalanceType(mBalance.getType()).equals("+")) {
            mBalanceMoneyTV.setTextColor(getResources().getColor(
                    R.color.balance_orange));
        } else {
            mBalanceMoneyTV
                    .setTextColor(getResources().getColor(R.color.black));
        }

        mBalanceMoneyTV.setText(WalletUtil.getWalletBalanceType(mBalance
                .getType()) + df.format(mBalance.getAmount()));// 设置金额

        if (!TextUtils.isEmpty(mBalance.getIcon()) && !"11".equals(mBalance.getType())) {
            ImageUtils.displayImage(mBalance.getIcon(), mImageView_icon, 10);
        } else {

            if (Integer.valueOf(mBalance.getType()) == Constant.BALANCE_TYPE.RECHARGE) {//当是充值的时候，选择相关充值的图标

                switch (Integer.valueOf(mBalance.getAccess())) {
                    case 1:

                        mImageView_icon.setImageResource(R.drawable.zhifubao);

                        break;
                    case 2:
                        mImageView_icon.setImageResource(R.drawable.weixin);

                        break;
                    case 3:


                }


            } else
                mImageView_icon.setImageResource(WalletUtil.getWalletIconType(mBalance
                        .getType()));


        }

        /**
         * 提现的情况 用户昵称使用accountno字段
         */
//        if (mBalance.getType().equals("2") || mBalance.getType().equals("10")) {
//            String mobile = mBalance.getAccountno();
//            WalletUtil.showAccount(mobile, mTextView_name);
//        } else {
//            mTextView_name.setText(mBalance.getNickName());// 用户昵称
//            mRelativeZhanghu.setVisibility(View.GONE);// 提现账户 Relative
//        }


        mBalanceNumberTV.setText(mBalance.getLiushui());// 账单流水号
        mBalanceOrderNum.setText(mBalance.getTransactionNum());// 订单号
        mBalanceTypeTV.setText(WalletUtil.getWalletDesType(mBalance.getType()));// 账单类型
        mBalanceTimeTV
                .setText(DateTimeUtil.parseDateFormat(mBalance.getTime()));// 创建时间

        //  mTextView_name.setText(WalletUtil.getWalletDesType(mBalance.getType()));
        mTextView_name.setText(mBalance.getNickName());

        //提现的情况
        // 提现的账单 用的 acountno 作为昵称
        if (mBalance.getType().equals("2") || mBalance.getType().equals("10")) {
            String mobile = mBalance.getAccountno();
            WalletUtil.showAccount(mobile, mTextView_name);
        } else {
            mTextView_name.setText(mBalance.getNickName());
        }


        if (Integer.valueOf(mBalance.getType()) == Constant.BALANCE_TYPE.RECHARGE) {
            if (mBalance.getAccess().equals("1")) {// 支付宝充值
                mTextView_name.setText("支付宝");
                //        mImageView_icon.setImageResource(R.drawable.zhifubao);
            } else if (mBalance.getAccess().equals("2")) {// 微信充值
                mTextView_name.setText("微信");
                //     mImageView_icon.setImageResource(R.drawable.weixin);
            } else mTextView_name.setText(mBalance.getNickName());//避免后台出现什么jb莫名其妙的情况。

        }


        // 付款方式

        if (mBalance.getType().equals("5")) {
            mRelativeAymentway.setVisibility(View.VISIBLE);
            mTextAymentway.setText("余额支付");
        } else if (mBalance.getType().equals("6")) {
            mRelativeAymentway.setVisibility(View.VISIBLE);
            if (mBalance.getAccess().equals("1")) {
                mTextAymentway.setText("支付宝支付");
            } else if (mBalance.getAccess().equals("2")) {
                mTextAymentway.setText("微信支付");
            } else {
                mTextAymentway.setText("银联支付");
            }

        } else if (mBalance.getType().equals("7")
                || mBalance.getType().equals("8")
                || mBalance.getType().equals("9")) {

            mRelativeAymentway.setVisibility(View.VISIBLE);
            text_fangshi.setText("退款方式");
            mTextAymentway.setText("钱包");

            //异常退款
        } else if ("11".equals(mBalance.getType())) {
            if (mBalance.getAccess().equals("1")
                    && Integer.parseInt(mBalance.getType()) == Constant.BALANCE_TYPE.RECHARGE) {// 支付宝充值
                mTextView_name.setText("支付宝");
                mImageView_icon.setImageResource(R.drawable.zhifubao);
            } else if (mBalance.getAccess().equals("2")) {// 微信充值
                mTextView_name.setText("微信");
                mImageView_icon.setImageResource(R.drawable.weixin);
            } else if (TextUtils.isEmpty(mBalance.getAccess())) {//TODO 后期开放的其他支付方式
                mTextView_name.setText("车当当信息科技有限公司");
                mImageView_icon.setImageResource(R.drawable.ic_launcher);
            } else
                mTextView_name.setText(mBalance.getNickName());//避免为空出现啥意外的情况

        } else {
            mRelativeAymentway.setVisibility(View.GONE);
        }

        // 订单有些类型没有相应的字段

        /**
         * 订单号
         */
        if (TextUtils.isEmpty(mBalance.getTransactionNum())) {
            mRelativeOrdernum.setVisibility(View.GONE);
        } else {
            mRelativeOrdernum.setVisibility(View.VISIBLE);
        }

        /**
         * 账单流水号
         */
        if (TextUtils.isEmpty(mBalance.getLiushui().toString())
                || mBalance.getLiushui() == null) {
            mRelativeLiushui.setVisibility(View.GONE);
        } else {
            mRelativeLiushui.setVisibility(View.VISIBLE);
        }

        /**
         * 提现账户
         */
        /*
         *
		 * if (mBalance.getType().equals("2") ||
		 * mBalance.getType().equals("10")) {
		 * mRelativeZhanghu.setVisibility(View.VISIBLE); } else {
		 * mRelativeZhanghu.setVisibility(View.GONE); }
		 */

        /**
         * Type=4 设置订单总金额,平台服务费; Type=2或者Type=10 设置 提现金额，手续费用
         */
        if (mBalance.getType().equals("4")) {
            mRelativeOrderZong.setVisibility(View.VISIBLE);
            mRelativePingtai.setVisibility(View.VISIBLE);
            double feiyong=mBalance.getAmount()+Double.valueOf(mBalance.getFuwufei());
            mTextOrderBalance.setText(df.format(feiyong) + "元");
            mTextpingtaiBalance.setText("-"+mBalance.getFuwufei()+"元");
        }
        //11异常退款
        if (mBalance.getType().equals("11")) {
            mBalanceStatusTV.setText("退款成功");
            if (mBalance.getAccess().equals("1")) {// 支付宝充值
                mTextView_name.setText("支付宝");
                mImageView_icon.setImageResource(R.drawable.zhifubao);
            } else if (mBalance.getAccess().equals("2")) {// 微信充值
                mTextView_name.setText("微信");
                mImageView_icon.setImageResource(R.drawable.weixin);
            } else {//TODO 后期开放的其他支付方式
                mImageView_icon.setImageResource(R.drawable.ic_launcher);
                mTextView_name.setText("车当当信息科技有限公司");
            }

        }


    }

    /**
     * 微信，支付宝订单支付费率
     */

    private void setPayRates() {
        // 订单支付费率

        if (mBalance.getType().equals("6")
                ) {

            mRelativeOrderZong.setVisibility(View.VISIBLE);
            mRelativePingtai
                    .setVisibility(View.VISIBLE);

            Double mjine = WalletUtil.SXF(Double
                    .valueOf(mBalance.getAmount()
                            / (1 + mPayRate)));// 订单金额
            String result = String.format("%.2f", mjine);
            mFeiLv = WalletUtil
                    .getSxf(mjine * mPayRate);// 手续费用
            text_shouru_tixian.setText("订单金额");


            mTextOrderBalance.setText("￥" + mBalance.getDzAmount());
            text_feilv.setText("手续费用");
            mTextpingtaiBalance.setText("￥" + mBalance.getSxfAmount());

        }

        if (mBalance.getType().equals("6")
                ) {
            mRelativeOrderZong
                    .setVisibility(View.VISIBLE);
            mRelativePingtai
                    .setVisibility(View.VISIBLE);

            text_shouru_tixian.setText("订单金额");
            mTextOrderBalance.setText("￥" + mBalance.getDzAmount());
            text_feilv.setText("手续费用");
            mTextpingtaiBalance.setText("￥" + mBalance.getSxfAmount());

        }

        if (mBalance.getType().equals("2")
                || mBalance.getType().equals("10")) {

            mRelativeOrderZong
                    .setVisibility(View.VISIBLE);
            mRelativePingtai
                    .setVisibility(View.VISIBLE);
            mFeiLv = WalletUtil.getSxf(Double
                    .valueOf(mBalance.getAmount())
                    * mPayRate);// 手续费用

            text_shouru_tixian.setText("到账金额");
            mTextOrderBalance.setText("￥" + mBalance.getDzAmount());
            text_feilv.setText("手续费用");
            mTextpingtaiBalance.setText("￥" + mBalance.getSxfAmount());

        }
        if (mBalance.getType().equals("2")
                || mBalance.getType().equals("10")) {
            mRelativeOrderZong
                    .setVisibility(View.VISIBLE);
            mRelativePingtai
                    .setVisibility(View.VISIBLE);

            text_shouru_tixian.setText("到账金额");
            mTextOrderBalance.setText("￥" + mBalance.getDzAmount());
            text_feilv.setText("手续费用");
            mTextpingtaiBalance.setText("￥" + mBalance.getSxfAmount());
        }
    }

    @Override
    protected void initEvent() {

        mTitleTv.setText("账单详情");
        mImageBack.setOnClickListener(this);

        mTextRight.setText("帮助");
        mTextRight.setVisibility(View.VISIBLE);
        mTextRight.setOnClickListener(this);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finish();
                break;

            case R.id.sub_header_bar_right_tv:

                Intent intent = new Intent(BalanceRecordActivity.this,
                        BalanceHelpActivity.class);
                startActivity(intent);

                break;

            default:
                break;
        }

    }
}
