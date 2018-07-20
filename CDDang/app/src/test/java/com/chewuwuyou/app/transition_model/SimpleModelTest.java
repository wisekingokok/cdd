package com.chewuwuyou.app.transition_model;

import com.chewuwuyou.app.transition_entity.SimpleEntity;

import org.junit.Test;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Yogi on 16/9/26.
 */
public class SimpleModelTest {
    @Test
    public void getAddress() throws Exception {
        SimpleModel model = new SimpleModel();
        model.getName()
                .subscribeOn(Schedulers.io())// 在非UI线程中执行getUser
                .observeOn(AndroidSchedulers.mainThread())// 在UI线程中执行结果
                .subscribe(new Action1<SimpleEntity>() {
                    @Override
                    public void call(SimpleEntity simpleEntity) {
                        System.out.print(simpleEntity.address);
                    }
                });
    }

}