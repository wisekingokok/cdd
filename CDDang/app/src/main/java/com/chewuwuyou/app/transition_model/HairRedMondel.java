package com.chewuwuyou.app.transition_model;

import com.chewuwuyou.app.transition_entity.OpenRed;
import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_presenter.BasePresenter;
import com.chewuwuyou.app.transition_service.RongMsgService;
import com.chewuwuyou.app.transition_service.UserService;
import com.chewuwuyou.app.transition_utils.HttpBase;
import com.chewuwuyou.app.transition_view.activity.HairRedActivtiy;
import com.chewuwuyou.app.utils.CacheTools;

import rx.functions.Action1;

public class HairRedMondel extends BaseModel{


    /**
     *
     * @param userId 用户id
     * @param redPacketId 红包id
     * @return
     */
    public rx.Observable<ResponseNBean<RedPacketDetailEntity>> getOpenRed(String userId, String redPacketId) {
        return HttpBase.getInstance().createApi(RongMsgService.class).getOpenRed(userId, redPacketId);
    }
}
