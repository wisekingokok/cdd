package com.chewuwuyou.app.transition_utils;

import android.content.Context;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_exception.DefaultExceptionHandling;
import com.chewuwuyou.app.widget.WaitingDialog;

import rx.Subscriber;

/**
 * Created by ZQ on 2016/10/14.
 */

public abstract class NtUtls <T>extends Subscriber<ResponseBean<T>> {
    WaitingDialog mdialog;
    Context context;

    public NtUtls(Context context) {
        this.context = context;
        mdialog=new WaitingDialog(context);
    }

    public NtUtls(Subscriber<?> subscriber, Context context) {
        super(subscriber);
        this.context = context;
        mdialog=new WaitingDialog(context);
    }

    public NtUtls(Subscriber<?> subscriber, boolean shareSubscriptions, Context context) {
        super(subscriber, shareSubscriptions);
        this.context = context;
        mdialog=new WaitingDialog(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        mdialog.show();
    }

    @Override
    public void onCompleted() {
        mdialog.dismiss();
    }

    @Override
    public void onError(Throwable e) {
        DefaultExceptionHandling.handling(AppContext.getInstance(),e);
        mdialog.dismiss();
    }

}
