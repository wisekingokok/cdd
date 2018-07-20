package com.chewuwuyou.app.transition_presenter;

import android.content.Context;

import com.chewuwuyou.app.transition_view.activity.WithdrawHomeActivity;

/**
 * Created by ZQ on 2016/10/20.
 */

public class WithdrawHomePresenter extends BasePresenter{
    private WithdrawHomeActivity mActivity;
    private double withDrawMoney=200;


    public WithdrawHomePresenter(WithdrawHomeActivity activity) {
        super(activity);
        this.mActivity=activity;

    }
    public void withdrawAll(){
        mActivity.showCalculate();
        if (withDrawMoney>0){
            mActivity.withdrawNom();
        }

    }

}
