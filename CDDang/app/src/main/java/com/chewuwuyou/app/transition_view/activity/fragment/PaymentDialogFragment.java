package com.chewuwuyou.app.transition_view.activity.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_adapter.PaymentAdapter;
import com.chewuwuyou.app.transition_presenter.PaymentPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by yuyong on 16/10/17.
 * 选择支付方式dialog
 */

public class PaymentDialogFragment extends DialogFragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.lv_payment)
    ListView lvPayment;
    private PaymentPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setWindowAnimations(R.style.mystyle); // 添加动画
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_payment_dialog, container);
        ButterKnife.bind(this, view);
        presenter=new PaymentPresenter(getActivity(),PaymentDialogFragment.this);
        presenter.initPayment();
        return view;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        super.onResume();
    }

    @OnClick(R.id.iv_close)
    public void onClick() {
        dismiss();

    }
    @OnItemClick(R.id.lv_payment)
    public void onItemClick(){


    }



    public void showPayment(PaymentAdapter adapter){
        lvPayment.setAdapter(adapter);
    }

}
