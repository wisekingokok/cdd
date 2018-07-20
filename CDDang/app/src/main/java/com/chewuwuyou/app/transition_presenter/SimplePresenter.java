package com.chewuwuyou.app.transition_presenter;

import com.chewuwuyou.app.transition_entity.SimpleEntity;
import com.chewuwuyou.app.transition_model.SimpleModel;
import com.chewuwuyou.app.transition_view.activity.SimpleActivity;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Yogi on 16/9/24.
 */

public class SimplePresenter extends BasePresenter {
    private SimpleActivity activity;
    private SimpleModel model;


    public SimplePresenter(SimpleActivity activity) {
        super(activity);
        this.activity = activity;
        model = new SimpleModel();
    }

    public void getName() {
        Observable o = model.getName();
        jsonLifecycle(o);//加入其他属性
        o.subscribe(new Action1<SimpleEntity>() {
                    @Override
                    public void call(SimpleEntity simpleEntity) {
                        activity.updateView(simpleEntity);
                    }
                });
    }


}
