package com.chewuwuyou.app.transition_utils;

import com.baidu.location.BDLocation;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * 定位事件总线（请只用于定位-用于其他将会有不可预知错误）
 */
public class RxLocBus {
    private static volatile RxLocBus defaultInstance;
    private final BehaviorSubject bus;// PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    BDLocation bdLocation = null;

    public RxLocBus() {
        bus = BehaviorSubject.create(bdLocation);
    }

    public static RxLocBus getDefault() {
        RxLocBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxLocBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxLocBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    // 提供了一个新的事件
    public void post(Object o) {
        bus.onNext(o);
    }

    /**
     * BDLocation为null则正在定位中
     *
     * @return
     */
    public Observable<BDLocation> toObserverable() {
//        return bus.ofType(eventType);
        return bus;
//        这里感谢小鄧子的提醒: ofType = filter + cast
//        return bus.filter(new Func1<Object, Boolean>() {
//            @Override
//            public Boolean call(Object o) {
//                return eventType.isInstance(o);
//            }
//        }) .cast(eventType);
    }
}