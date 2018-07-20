package com.chewuwuyou.app.transition_presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.chewuwuyou.app.transition_exception.CustomError;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_exception.DefaultExceptionHandling;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.JpushUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleProvider;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xxy on 2016/10/10 0010.
 */

public class BasePresenter {
    protected Context context;
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;

    public BasePresenter(Context context) {
        this.context = context;
    }


    /**
     * 对 ACTIVITY 生命周期进行管理
     *
     * @return
     */
    protected LifecycleProvider getActivityLifecycleProvider() {

        LifecycleProvider provider = null;
        if (null != context && context instanceof LifecycleProvider) {
            provider = (LifecycleProvider) context;
        }
        return provider;
    }

    protected void doError(Throwable error) {
        CustomException customException = DefaultExceptionHandling.handling(context.getApplicationContext(), error);
        switch (customException.getErrorCode()) {//如果是登录异常那么直接抛出,结束所有请求
            case CustomError.LOGIN_PAST:
                loginPast(customException);
            case CustomError.LOGINE_IN_OTHER_PHONE:
                loginPast(customException);
            case CustomError.NOT_BUSINESS:
                loginPast(customException);
            default:

                break;
        }
    }

    public void doDestroy() {
        this.context = null;
    }

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                LifecycleProvider provider = getActivityLifecycleProvider();
                if (provider != null) {
                    observable.compose(provider.bindToLifecycle());
                }
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

            }
        };
    }

    public void jsonLifecycle(Observable observable) {
        observable.subscribeOn(Schedulers.io())// 在非UI线程中执行getUser
                .observeOn(AndroidSchedulers.mainThread());// 在UI线程中执行结果
        LifecycleProvider provider = getActivityLifecycleProvider();
        if (provider != null) {
            observable.compose(provider.bindToLifecycle());
        }

    }

    /**
     * 登录失效
     *
     * @param customException
     */
    protected void loginPast(CustomException customException) {
        ToastUtil.toastShow(context, customException.getMessage());
        context.startActivity(new Intent(context, LoginActivity.class));
        ((Activity) context).finish();
    }

    /**
     * 如果要使用默认的异常处理和结束操作请使用此方法(不提供隐藏提示的方法)
     *
     * @param action1
     * @param <T>
     * @return
     */
    protected <T> Subscriber<T> defaultSubscriber(Action1<T> action1) {
        return defaultSubscriber(action1, null, null);
    }

    /**
     * 如果要使用默认的异常处理和结束操作请使用此方法(不提供隐藏提示的方法)
     *
     * @param action1
     * @param <T>
     * @return
     */
    protected <T> Subscriber<T> defaultSubscriber(Action1<T> action1, final Func1<CustomException, Boolean> func1) {
        return defaultSubscriber(action1, null, func1);
    }

    /**
     * 如果要使用默认的异常处理和结束操作请使用此方法(不提供隐藏提示的方法)
     *
     * @param action1
     * @param <T>
     * @return
     */
    protected <T> Subscriber<T> defaultSubscriber(Action1<T> action1, final Action0 cancelProgress) {
        return defaultSubscriber(action1, cancelProgress, null);
    }

    /**
     * 如果要使用默认的异常处理和结束操作请使用此方法
     *
     * @param action1
     * @param <T>
     * @param cancelProgress 提供隐藏提示的方法
     * @return
     */
    protected <T> Subscriber<T> defaultSubscriber(final Action1<T> action1, final Action0 cancelProgress, final Func1<CustomException, Boolean> func1) {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                if (cancelProgress != null)
                    cancelProgress.call();
            }

            @Override
            public void onError(Throwable e) {
                defaultErrorAction(func1).call(e);
                if (cancelProgress != null)
                    cancelProgress.call();
            }

            @Override
            public void onNext(T t) {
                action1.call(t);
            }
        };
    }

    /**
     * 默认异常处理(提示,结束progressBar)
     *
     * @return
     */
    protected Action1<Throwable> defaultErrorAction(final Func1<CustomException, Boolean> func1) {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                CustomException customException;
                if (CustomException.class.isInstance(throwable))
                    customException = (CustomException) throwable;
                else
                    customException = DefaultExceptionHandling.handling(context.getApplicationContext(), throwable);
                switch (customException.getErrorCode()) {
                    case CustomError.LOGIN_PAST:
                        loginPast(customException);
                        break;
                    case CustomError.LOGINE_IN_OTHER_PHONE:
                        loginPast(customException);
                        break;
                    case CustomError.NOT_BUSINESS:
                        loginPast(customException);
                        break;
                    default:
                        if (func1 != null && func1.call(customException)) return;
                        ToastUtil.toastShow(context, customException.getMessage());
                        break;
                }
            }
        };
    }


    //jpush start
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    MyLog.i("YUY", "推送Alias ----- " + logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    MyLog.i("YUY", "推送Alias ----- " + logs);
                    if (JpushUtil.isConnected(context.getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        MyLog.i("YUY", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    MyLog.i("YUY", "推送Alias ----- " + logs);
                    break;
            }
            MyLog.e("YUY", "推送Alias ----- " + logs);

        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    MyLog.i("YUY", "推送tag ----- " + logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    MyLog.i("YUY", "推送tag ----- " + logs);
                    if (JpushUtil.isConnected(context.getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i("YUY", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    MyLog.i("YUY", "推送tag ----- " + logs);
                    break;
            }
            MyLog.e("YUY", "推送tag ----- " + logs);
        }

    };


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("YUY", "Set alias in handler.");
                    JPushInterface.setAliasAndTags(context, (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d("YUY", "Set tags in handler.");
                    JPushInterface.setAliasAndTags(context, null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i("YUY", "Unhandled msg - " + msg.what);
                    break;
            }
        }
    };

    /**
     * 初始化极光推送并设置标签和别名
     */
    public void initUserJpush() {
        final String alias = "cwwy" + CacheTools.getUserData("userId");
        MyLog.e("YUY", "推送别名 ----------" + alias);
        MyLog.e("YUY", "推送registerId = " + JPushInterface.getRegistrationID(context.getApplicationContext()));
        JPushInterface.init(context.getApplicationContext());// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        JPushInterface.resumePush(context.getApplicationContext());
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, JpushUtil.pushSetTag()));//调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias)); //调用JPush API设置Alias
    }


    /**
     * 注销极光推送
     */
    public void unregisterJPush() {
        JPushInterface.stopPush(context.getApplicationContext());
    }
    //jpush end
}
