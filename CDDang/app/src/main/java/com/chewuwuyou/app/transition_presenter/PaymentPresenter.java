package com.chewuwuyou.app.transition_presenter;

import android.content.Context;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_adapter.PaymentAdapter;
import com.chewuwuyou.app.transition_entity.PaymentBean;
import com.chewuwuyou.app.transition_view.activity.fragment.PaymentDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyong on 16/10/17.
 * 支付方式presenter
 */

public class PaymentPresenter extends BasePresenter {

    private PaymentDialogFragment paymentDialogFragment;
    private PaymentAdapter adapter;
    private List<PaymentBean> mPayments;
    private String[] paymentStr = {"支付宝快捷支付", "钱包支付"};// "微信支付",
    private Integer[] paymentImgIds = {R.drawable.zhifubao, R.drawable.wallet_icon};// R.drawable.weixin,
    private String[] paymentDesStr = {"推荐有支付宝账号的用户使用", "余额优先支付"};// ,

    public PaymentPresenter(Context context, PaymentDialogFragment fragment) {
        super(context);
        this.paymentDialogFragment = fragment;
    }

    /**
     * 初始化支付方式
     */
    public void initPayment() {
        //TODO 支付方式从后台接口获取
        mPayments = new ArrayList<>();
        for (int i = 0; i < paymentDesStr.length; i++) {
            mPayments.add(new PaymentBean(paymentImgIds[i], paymentStr[i], paymentDesStr[i], i == 0 ? true : false));
        }
        adapter = new PaymentAdapter(paymentDialogFragment.getActivity(), mPayments);
        paymentDialogFragment.showPayment(adapter);
    }

}
