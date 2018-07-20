package com.chewuwuyou.app.transition_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_entity.PaymentBean;

import java.util.List;

/**
 * Created by yuyong on 16/10/17.
 */

public class PaymentAdapter extends BaseAdapter {
    private Context mContext;
    private List<PaymentBean> mPayments;
    private LayoutInflater mInflater;

    public PaymentAdapter(Context context, List<PaymentBean> payments) {
        this.mContext = context;
        this.mPayments = payments;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mPayments.size();
    }

    @Override
    public Object getItem(int position) {
        return mPayments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = mInflater.inflate(R.layout.payment_item, null);
            listItemView.paymentIV = (ImageView) convertView.findViewById(R.id.payment_iv);
            listItemView.paymentTV = (TextView) convertView.findViewById(R.id.payment_tv);
            listItemView.paymentDesTV = (TextView) convertView.findViewById(R.id.payment_des_tv);
            listItemView.paymentRB = (RadioButton) convertView.findViewById(R.id.payment_rb);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        if (position == 2) {
            listItemView.paymentDesTV.setTextColor(mContext.getResources().getColor(
                    R.color.red));
        } else {
            listItemView.paymentDesTV.setTextColor(mContext.getResources().getColor(
                    R.color.common_text_color));
        }
        listItemView.paymentRB.setVisibility(mPayments.get(position).isCheck()?View.VISIBLE:View.GONE);
        listItemView.paymentIV.setImageResource(mPayments.get(position).getResId());
        listItemView.paymentTV.setText(mPayments.get(position).getPayment());
        listItemView.paymentDesTV.setText(mPayments.get(position).getPayDesc());
        listItemView.paymentRB.setChecked(mPayments.get(position).isCheck());
        return convertView;
    }

    class ListItemView {
        ImageView paymentIV;// 支付图标
        TextView paymentTV;// 支付名称
        TextView paymentDesTV;// 支付描述
        RadioButton paymentRB;// 是否选中
    }


}
