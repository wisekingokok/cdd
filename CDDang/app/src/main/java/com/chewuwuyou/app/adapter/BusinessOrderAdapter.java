package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.OrderStateUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.eim.activity.im.ChatActivity;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:
 * @author:yuyong
 * @date:2015-6-10下午5:48:03
 * @version:1.2.1
 */
public class BusinessOrderAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<Task> mTasks;// 任务集合
    private boolean isOperating = false;// 是否是操作模式

    private class ListItemView {
        /**
         * 点对点订单
         */
        TextView mOrderType;// 订单类型
        TextView mOrderPrice;// 订单价格
        TextView mOrderNum;// 订单号
        TextView mOrderTimeTV;// 订单时间
        TextView mOrderStatus;// 订单状态
        ImageView mOrderHandlerIV;// 订单处理的头像
        TextView mNameHandlerTV;// 订单处理者的名称
        LinearLayout mOrderHandlerLayout;// 点击图片进行聊天
        TextView mBusinessType;

        /**
         * 业务大厅订单
         */
        TextView mTextServeType, mTextStatus, mTextProjecctName, mTextOrdernum, mTextOrdertime;// 订单类型，订单状态，订单号，订单时间

        CheckBox checkBox;

    }

    public BusinessOrderAdapter(Context context, List<Task> mTasks) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.mTasks = mTasks;
    }

    public void setOperating(boolean isOperating) {
        this.isOperating = isOperating;
        notifyDataSetChanged();
    }

    public boolean getOperating() {
        return isOperating;
    }

    /**
     * 取得被选中的Task
     *
     * @return
     */
    public String getCheckItemIds() {
        StringBuffer buffer = new StringBuffer();
        if (mTasks == null || mTasks.size() == 0)
            return buffer.toString();
        int size = mTasks.size();
        boolean isCheck = false;
        for (int i = 0; i < size; i++) {
            Task task = mTasks.get(i);
            if (task.isCheck) {
                buffer.append(task.getId()).append("-");
                isCheck = true;
            }
        }
        return isCheck ? buffer.toString().substring(0, buffer.toString().length() - 1) : buffer.toString();
    }

    public void selectAll() {
        for (int i = 0; i < mTasks.size(); i++) {
            Task task = mTasks.get(i);
            task.isCheck = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mTasks.size();
    }

    @Override
    public Object getItem(int position) {
        return mTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = null;
        final Task task = mTasks.get(position);
        listItemView = new ListItemView();
        convertView = createViewByMessage(task);

        if (task.getStatus().equals("2") && (task.getFacilitatorId().equals("0") || task.getFacilitatorId().equals(""))
                && task.getFlag().equals("3")) {
            listItemView.mTextServeType = (TextView) convertView.findViewById(R.id.text_service_type);
            listItemView.mTextStatus = (TextView) convertView.findViewById(R.id.text_order_status_tv);
            listItemView.mTextOrdernum = (TextView) convertView.findViewById(R.id.text_ordernum);
            listItemView.mTextOrdertime = (TextView) convertView.findViewById(R.id.text_ordertime);
            listItemView.mTextProjecctName = (TextView) convertView.findViewById(R.id.service_project_tv);
        } else if (task.getStatus().equals("27") || task.getStatus().equals("28")) {
            listItemView.mTextServeType = (TextView) convertView.findViewById(R.id.text_service_type);
            listItemView.mTextStatus = (TextView) convertView.findViewById(R.id.text_order_status_tv);
            listItemView.mTextOrdernum = (TextView) convertView.findViewById(R.id.text_ordernum);
            listItemView.mTextOrdertime = (TextView) convertView.findViewById(R.id.text_ordertime);
            listItemView.mTextProjecctName = (TextView) convertView.findViewById(R.id.service_project_tv);

        } else {
            listItemView.mOrderType = (TextView) convertView.findViewById(R.id.order_type_tv);
            listItemView.mOrderPrice = (TextView) convertView.findViewById(R.id.order_price_tv);
            listItemView.mOrderNum = (TextView) convertView.findViewById(R.id.order_number_tv);
            listItemView.mOrderTimeTV = (TextView) convertView.findViewById(R.id.order_time_tv);
            listItemView.mOrderStatus = (TextView) convertView.findViewById(R.id.order_status_tv);
            listItemView.mOrderHandlerIV = (ImageView) convertView.findViewById(R.id.order_handler_head_iv);
            listItemView.mNameHandlerTV = (TextView) convertView.findViewById(R.id.order_handler_name_tv);
            listItemView.mOrderHandlerLayout = (LinearLayout) convertView.findViewById(R.id.order_handler_layout);
            listItemView.mBusinessType = (TextView) convertView.findViewById(R.id.business_type);
        }
        listItemView.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        convertView.setTag(listItemView);
        if (isOperating) {
            listItemView.checkBox.setVisibility(View.VISIBLE);
            listItemView.checkBox.setChecked(task.isCheck);
        } else {
            listItemView.checkBox.setVisibility(View.GONE);
            task.isCheck = false;
        }
        if (task.getStatus().equals("2") && (task.getFacilitatorId().equals("0") || task.getFacilitatorId().equals(""))
                && task.getFlag().equals("3")) {// 我作为B类商家在业务大厅发布的业务
            // 没有选择任何服务商进行关联就取消订单
            listItemView.mTextServeType.setText(task.getType());
            listItemView.mTextProjecctName.setText(ServiceUtils.getProjectName(task.getProjectName()));
            listItemView.mTextOrdernum.setText("订单号" + task.getOrderNum());
            listItemView.mTextOrdertime.setText(task.getPubishTime());
            listItemView.mTextStatus.setText("订单已取消");

        } else if (task.getStatus().equals("27") || task.getStatus().equals("28")) {
            listItemView.mTextServeType.setText(task.getType());
            listItemView.mTextProjecctName.setText(ServiceUtils.getProjectName(task.getProjectName()));
            listItemView.mTextOrdernum.setText("订单号" + task.getOrderNum());
            listItemView.mTextOrdertime.setText(task.getPubishTime());
            OrderStateUtil.orderStatusShow(Integer.parseInt(task.getStatus()), Integer.parseInt(task.getFlag()),
                    listItemView.mTextStatus);
        } else {
            listItemView.mOrderTimeTV.setText(task.getPubishTime());// 订单时间
            listItemView.mOrderType.setText(task.getType());// 订单类型
            listItemView.mOrderNum.setText("订单号" + task.getOrderNum());// 订单号

            DecimalFormat df = new DecimalFormat("######0.00");
            listItemView.mOrderPrice.setText("¥" + df.format(task.getPaymentAmount()));// 金额
            if (!task.getFlag().equals("3") && !task.getFacilitatorId().equals(CacheTools.getUserData("userId"))
                    && !task.getFacilitatorId().equals("0") && !TextUtils.isEmpty(task.getFacilitatorId())
                    && Constant.mType.equals("1")) {// 判断B类订单已与其他服务商成交
                listItemView.mOrderStatus.setText("接单失败");
                listItemView.mOrderPrice.setVisibility(View.GONE);
                listItemView.mOrderStatus.setBackgroundResource(R.drawable.baojia_frame);
                listItemView.mOrderStatus.setTextColor(Color.parseColor("#e05f20"));
            } else {
                OrderStateUtil.orderStatusShow(Integer.parseInt(task.getStatus()), Integer.parseInt(task.getFlag()),
                        listItemView.mOrderStatus);
                listItemView.mOrderPrice.setVisibility(View.VISIBLE);
            }
            listItemView.mBusinessType.setText(ServiceUtils.getProjectName(task.getProjectName()));

            listItemView.mNameHandlerTV.setText(task.getName());// 订单发布人名称
            if (!TextUtils.isEmpty(task.getUrl())) {
                ImageUtils.displayImage(task.getUrl(), listItemView.mOrderHandlerIV, 10);
            }
            listItemView.mOrderHandlerLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (task.getExactPhone().equals(CacheTools.getUserData("telephone"))) {
                        AppContext.createChat(mContext,String.valueOf(task.getUserId()));

                    } else {
                        if (task.getStatus().equals("27") || task.getStatus().equals("28")) {// 报价中和已报价未与商家产生联系

                        } else {
                            AppContext.createChat(mContext,String.valueOf(task.getUserId()));
                        }
                    }

                }
            });
        }

        return convertView;
    }

    private View createViewByMessage(Task task) {
        if (task.getStatus().equals("27") || task.getStatus().equals("28")) {
            return layoutInflater.inflate(R.layout.business_hall_order, null);
        } else if (task.getStatus().equals("2")
                && (task.getFacilitatorId().equals("0") || task.getFacilitatorId().equals(""))
                && task.getFlag().equals("3")) {
            return layoutInflater.inflate(R.layout.business_hall_order, null);
        } else {
            return layoutInflater.inflate(R.layout.business_order_item, null);
        }
    }
    /*
     * class pjBtnListener implements OnClickListener { private int position;
	 * 
	 * pjBtnListener(int pos) { position = pos; }
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * StatService.onEvent(mContext, "clickTaskManaAdapterPjBtn",
	 * "点击Task管理list每项的评估"); Intent intent = new Intent(mContext,
	 * EvaluateActivity.class); Bundle bundle = new Bundle();
	 * bundle.putSerializable("pjTask", mTasks.get(position));
	 * intent.putExtras(bundle); mContext.startActivity(intent);
	 * 
	 * 
	 * // 跳转到另一个ACTIVITY并传参数 // Intent it = new Intent(context,
	 * OnClickActivity.class); // it.putExtra("ps", "" + position); //
	 * context.startActivity(it);
	 * 
	 * // 此方法点击按钮去掉当前ITEM // int vid = v.getId(); // if (vid ==
	 * holder.buttonClose.getId()) // removeItem(position); } }
	 */

}
