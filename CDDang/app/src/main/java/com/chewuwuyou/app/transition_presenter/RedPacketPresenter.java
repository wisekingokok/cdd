package com.chewuwuyou.app.transition_presenter;

import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.RedPacketModel;
import com.chewuwuyou.app.transition_view.activity.RedPacketDetailActivity;

import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by CLOUD on 2016/10/13.
 */

public class RedPacketPresenter extends BasePresenter {

    private RedPacketDetailActivity mRedPacketDetailActivity;
    private RedPacketModel mRedPacketModel;

    public RedPacketPresenter(RedPacketDetailActivity context) {
        super(context);
        mRedPacketDetailActivity=context;
        mRedPacketModel = new RedPacketModel();
    }

    /**
     * @param userId
     * @param id     红包id
     * @param page   分页(红包个数很多的时候，需要分页)
     */
    public void getData(String userId, String id, int pageNum, final int page) {

        mRedPacketModel.getRedDetail(userId, id, pageNum, page).compose(this.<ResponseNBean<RedPacketDetailEntity>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<ResponseNBean<RedPacketDetailEntity>>() {
                    @Override
                    public void call(ResponseNBean<RedPacketDetailEntity> mRedPacketDetailEntityResponseBean) {

                        mRedPacketDetailActivity.setData(mRedPacketDetailEntityResponseBean.getData());

                    }
                }, new Func1<CustomException, Boolean>() {
                    @Override
                    public Boolean call(CustomException mE) {

                        mRedPacketDetailActivity.setError();
                        return false;
                    }


                }));

    }


}
