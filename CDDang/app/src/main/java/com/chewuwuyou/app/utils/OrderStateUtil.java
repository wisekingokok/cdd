package com.chewuwuyou.app.utils;

import android.widget.TextView;

/**
 * 设置订单状态的工具类
 * 
 * @author Administrator
 */
public class OrderStateUtil {

	/**
	 * 显示不同订单状态
	 * 
	 * @param orderStatus
	 *            //状态
	 * @param flag
	 *            判断是发单方还是接单方
	 * @param statusShowTV
	 *            显示订单状态的文本
	 */
	public static void orderStatusShow(int orderStatus, int flag,
			TextView statusShowTV) {

		if (flag == 3) {
			switch (orderStatus) {
			case 1:
				statusShowTV.setText("待商家确认");
				break;
			case 2:
				statusShowTV.setText("订单已取消");
				break;
			case 3:
				statusShowTV.setText("待付款");
				break;
			case 4:
				statusShowTV.setText("已付款");
				break;
			case 5:
				statusShowTV.setText("商家服务中");
				break;
			case 6:
				statusShowTV.setText("需确认服务");
				break;
			case 7:
				statusShowTV.setText("订单已完成");
				break;
			case 8:
				statusShowTV.setText("待派发");
				break;
			case 9:
				statusShowTV.setText("订单已完成");
				break;
			case 10:
				statusShowTV.setText("订单已完成");
				break;
			case 11:
				statusShowTV.setText("退款申请中");
				break;
			case 12:
				statusShowTV.setText("退款处理中");
				break;
			case 13:
				statusShowTV.setText("退款成功");
				break;
			case 21:
				statusShowTV.setText("门店订单待付款");
				break;
			case 22:
				statusShowTV.setText("门店订单已完成");
				break;
			case 23:
				statusShowTV.setText("门店订单已付款");
				break;
			case 24:
				statusShowTV.setText("门店订单服务中");
				break;
			case 27:
				statusShowTV.setText("等待接单");
				break;
			case 28:
				statusShowTV.setText("报价中");
				break;
			case 29:
				statusShowTV.setText("订单已完成");
				break;
			case 32:
				statusShowTV.setText("退款成功");
				break;
			case 33:
					statusShowTV.setText("纠纷处理中");
					break;
				
			default:
				break;
			}
		} else {
			switch (orderStatus) {
			case 1:
				statusShowTV.setText("待确认价格");
				break;
			case 2:
				statusShowTV.setText("订单已取消");
				break;
			case 3:
				statusShowTV.setText("待用户付款");
				break;
			case 4:
				statusShowTV.setText("用户已付款");
				break;
			case 5:
				statusShowTV.setText("服务中");
				break;
			case 6:
				statusShowTV.setText("服务完成");
				break;
			case 7:
				statusShowTV.setText("用户已确认完成");
				break;
			case 8:
				statusShowTV.setText("待派发");
				break;
			case 9:
				statusShowTV.setText("用户已评价");
				break;
			case 10:
				statusShowTV.setText("已结算");
				break;
			case 11:
				statusShowTV.setText("用户申请退款");
				break;
			case 12:
				statusShowTV.setText("退款处理中");
				break;
			case 13:
				statusShowTV.setText("已退款给客户");
				break;
			case 21:
				statusShowTV.setText("门店订单待付款");
				break;
			case 22:
				statusShowTV.setText("门店订单已完成");
				break;
			case 23:
				statusShowTV.setText("门店订单已付款");
				break;
			case 24:
				statusShowTV.setText("门店订单服务中");
				break;
			case 27:
				statusShowTV.setText("等待接单");
				break;
			case 28:
				statusShowTV.setText("报价中");
				break;
			case 29:
				statusShowTV.setText("结算入余额");
				break;
			case 32:
				statusShowTV.setText("已退款给客户");
				break;
			case 33:
			    statusShowTV.setText("纠纷处理中");
					break;
			default:
				break;
			}
		}
	}

	/**
	 * 
	 * @param OrderState
	 *            当前状态
	 * @param originalStat
	 *            上一个状态
	 * @return
	 */
	public static String orderStateTime(int OrderState, int originalStat) {
		switch (OrderState) {
		case 1:
			return "订单创建时间";
		case 2:
			return "订单取消时间";
		case 3:
			return "确认报价时间";
		case 4:
			if (originalStat == 11) {
				return "取消退款时间";
			} else {
				return "完成支付时间";
			}
		case 5:
			if (originalStat == 11) {
				return "取消退款时间";
			} else {
				return "服务开始时间";
			}

		case 6:
			if (originalStat == 11) {
				return "取消退款时间";
			} else {
				return "服务完成时间";
			}
		case 7:
			return "确认完成时间";
		case 10:
			return "订单结算时间";
		case 11:
			return "申请退款时间";
		case 12:
			return "同意退款时间";
		case 13:
			return "退款成功时间";
		case 27:
			return "报价中";
		case 28:
			return "报价中";
		case 29:
			if(originalStat == 33){
				return "客服处理时间";
			}else{
				return "订单结算时间";
			}
		case 32:
			if(originalStat == 11){
				return "退款处理时间";
			}else{
				return "客服处理时间";
			}
		case 33:
			return "客服介入时间";
		case 10000:
			return "异常退款时间";
		}
		return null;
	}
}
