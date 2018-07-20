package com.chewuwuyou.app.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Balance;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.WalletUtil;

/**
 * 钱包明细适配器
 *
 * @author yuyong
 */
public class MyBalanceAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<Balance> mBalances;// 明细集合
    private Handler mHandler;

    public MyBalanceAdapter(Context context, List<Balance> balances,
                            Handler mHandler) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.mBalances = balances;
        this.mHandler = mHandler;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mBalances.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mBalances.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder = null;
        if (convertView == null) {
            itemHolder = new ItemHolder();
            convertView = layoutInflater.inflate(R.layout.detail_child_layout,
                    null);
            itemHolder.name = (TextView) convertView
                    .findViewById(R.id.text_name);

            itemHolder.balance = (TextView) convertView
                    .findViewById(R.id.text_balance);

            itemHolder.avatar = (ImageView) convertView
                    .findViewById(R.id.image_avatar);

            itemHolder.type = (TextView) convertView
                    .findViewById(R.id.text_balance_type);

            itemHolder.date = (TextView) convertView
                    .findViewById(R.id.text_date);
            itemHolder.accountDelete = (TextView) convertView
                    .findViewById(R.id.delete);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }

        Balance balance = mBalances.get(position);

        // 提现的账单 用的 acountno 作为昵称
        if (balance.getType().equals("2") || balance.getType().equals("10")) {
            String mobile = balance.getAccountno();
            WalletUtil.showAccount(mobile, itemHolder.name);
        } else {
            itemHolder.name.setText(balance.getNickName());
        }

        itemHolder.date
                .setText(DateTimeUtil.parseDateFormat(balance.getTime()));

        // 设置金额控件 颜色
        if (WalletUtil.getWalletBalanceType(balance.getType()).equals("+")) {
            itemHolder.balance.setTextColor(mContext.getResources().getColor(
                    R.color.balance_orange));
        } else {
            itemHolder.balance.setTextColor(mContext.getResources().getColor(
                    R.color.black));
        }
        // 显示金额
        DecimalFormat df = new DecimalFormat("######0.00");
        itemHolder.balance.setText(WalletUtil.getWalletBalanceType(balance
                .getType()) + df.format(balance.getAmount()));
        itemHolder.type.setText(WalletUtil.getWalletDesType(balance.getType()));


        if (!TextUtils.isEmpty(balance.getIcon()) && !balance.getType().equals("11")) {
            ImageUtils.displayImage(balance.getIcon(), itemHolder.avatar, 10);
        } else {
            itemHolder.avatar.setImageResource(WalletUtil
                    .getWalletIconType(balance.getType()));
        }
        if (balance.getAccess().equals("1")
                && Integer.parseInt(balance.getType()) == Constant.BALANCE_TYPE.RECHARGE) {// 支付宝充值
            itemHolder.avatar.setImageResource(R.drawable.zhifubao);
            itemHolder.name.setText("支付宝充值");
        } else if (balance.getAccess().equals("2")) {// 微信充值
            itemHolder.avatar.setImageResource(R.drawable.weixin);
            itemHolder.name.setText("微信充值");
        } else {//TODO 后期开放的其他支付方式

        }
        //11异常退款
        if (balance.getType().equals("11")) {
            if (balance.getAccess().equals("1")) {// 支付宝充值
                itemHolder.name.setText("支付宝");
                itemHolder.avatar.setImageResource(R.drawable.zhifubao);
            } else if (balance.getAccess().equals("2")) {// 微信充值
                itemHolder.name.setText("微信");
                itemHolder.avatar.setImageResource(R.drawable.weixin);
            } else {//TODO 后期开放的其他支付方式
                itemHolder.avatar.setImageResource(R.drawable.ic_launcher);
                itemHolder.name.setText("车当当信息科技有限公司");
            }

        }

        itemHolder.accountDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Constant.CCCOUNT_WITHDRAWLS = position;
                Message message = new Message();
                message.what = Constant.SEND_Handler;
                message.obj = String.valueOf(mBalances.get(position)
                        .getPersonalAccountId());
                mHandler.sendMessage(message);
            }
        });

        return convertView;
    }

    class ItemHolder {
        public ImageView avatar;
        public TextView name;
        public TextView type;
        public TextView balance;
        public TextView date;
        public TextView accountDelete;
    }

}
