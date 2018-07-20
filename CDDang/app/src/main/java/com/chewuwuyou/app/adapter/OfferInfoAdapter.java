package com.chewuwuyou.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.utils.AlertDialog;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.eim.activity.im.ChatActivity;

/***
 * 报价信息适配器
 *
 * @author yuyong
 */
public class OfferInfoAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Task mTask;
    private Handler handler;

    public class ListItemV { // 自定义控件集合

        ImageView headIV, chatIV, callIV;
        TextView businessNameTV, ratingNumTV, offerTV;
        Button payBtn;
        RatingBar ratingBar;

    }

    public OfferInfoAdapter(Context context, Task task, Handler handler) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mTask = task;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return mTask.getOffers().size();
    }

    @Override
    public Object getItem(int position) {
        return mTask.getOffers().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItemV listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemV();
            convertView = mLayoutInflater.inflate(R.layout.toquote_item_layout, null);
            listItemView.headIV = (ImageView) convertView.findViewById(R.id.business_head_iv);
            listItemView.chatIV = (ImageView) convertView.findViewById(R.id.chat_btn);
            listItemView.callIV = (ImageView) convertView.findViewById(R.id.call_btn);
            listItemView.businessNameTV = (TextView) convertView.findViewById(R.id.business_name_tv);
            listItemView.ratingNumTV = (TextView) convertView.findViewById(R.id.star_num_tv);
            listItemView.offerTV = (TextView) convertView.findViewById(R.id.service_price_tv);
            listItemView.payBtn = (Button) convertView.findViewById(R.id.pay_btn);
            listItemView.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar_rb);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemV) convertView.getTag();
        }
        /**
         * 商家头像
         */
        ImageUtils.displayImage(mTask.getOffers().get(position).getUrl(), listItemView.headIV, 10, R.drawable.user_fang_icon, R.drawable.user_fang_icon);

        listItemView.businessNameTV.setText(mTask.getOffers().get(position).getBusName());
        listItemView.ratingNumTV.setText(String.valueOf(mTask.getOffers().get(position).getStar()) + "分");
        if (Double.valueOf(mTask.getOffers().get(position).getOffer()) == 0) {
            listItemView.offerTV.setText("未报价");
            listItemView.offerTV.setTextColor(mContext.getResources().getColor(
                    R.color.common_text_color));
            listItemView.payBtn.setBackground(mContext.getResources()
                    .getDrawable(R.drawable.common_gray_btn_bg));
            listItemView.payBtn.setEnabled(false);
        } else {
            listItemView.offerTV
                    .setText("￥" + String.valueOf(mTask.getOffers().get(position).getOffer()));
            listItemView.offerTV.setTextColor(mContext.getResources().getColor(
                    R.color.orange));
            listItemView.payBtn.setBackground(mContext.getResources()
                    .getDrawable(R.drawable.tuquoteitem_yellow));
            listItemView.payBtn.setEnabled(true);
        }

        /**
         * 商家评分
         */
        listItemView.ratingBar.setRating(Float.valueOf(mTask.getOffers().get(position).getStar()));

        /**
         * 联系电话
         */
        listItemView.callIV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DialogUtil.call(mTask.getOffers().get(position).getExactPhone(), mContext);
            }
        });

        /**
         * 发送消息
         */
        listItemView.chatIV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AppContext.createChat(mContext,
                        String.valueOf(mTask.getOffers().get(position).getUserId()));

            }
        });
        /**
         * 是否选择该商家报价
         */
        listItemView.payBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new AlertDialog(mContext).builder().setTitle("车当当提醒您")
                        .setMsg("确认选择该商家作为您的服务商")
                        .setPositiveButton("取消", new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {

                            }
                        }).setNegativeButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // 在此确定报价信息
                        Message message = new Message();
                        message.what = 1;
                        message.obj = String.valueOf(mTask.getOffers().get(position).getId());
                        handler.sendMessage(message);
                    }
                }).show();

            }
        });

        return convertView;
    }
}
