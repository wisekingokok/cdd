package com.chewuwuyou.app.transition_model;

import com.chewuwuyou.app.transition_service.SimpleService;
import com.chewuwuyou.app.transition_entity.SimpleEntity;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by Yogi on 16/9/24.
 */

public class SimpleModel extends BaseModel {
    public Observable<SimpleEntity> getName() {
        Retrofit retrofit = getDefaultRetrofit();
        return retrofit.create(SimpleService.class)
                .get("苏州市");
    }

}
