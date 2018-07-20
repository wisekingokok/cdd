package com.chewuwuyou.app.transition_model.im_group;

import android.view.View;

import com.chewuwuyou.app.bean.GroupSetUpEssential;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.transition_entity.BaseListBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.ResponseNListBean;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.BaseModel;
import com.chewuwuyou.app.transition_presenter.BasePresenter;
import com.chewuwuyou.app.transition_service.im_group.GroupService;
import com.chewuwuyou.app.transition_utils.HttpBase;
import com.chewuwuyou.app.transition_view.activity.im_group.GroupSetUpErActivtiy;
import com.chewuwuyou.app.utils.CacheTools;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 群设置
 * liuchun
 */

public class GroupSetUpErMondel extends BaseModel {

    /**
     * 群基本信息
     * @param groupId 群id
     * @param userId  用户id
     * @return
     */
    public rx.Observable<ResponseNBean<GroupSetUpEssential>> getGroupBasic(String groupId,String userId) {
        return HttpBase.getInstance().createApi(GroupService.class).getGroupBasic(groupId,userId);
    }

    /**
     * 群成员信息
     * @param groupId 群id
     * @param userId 用户id
     * @return
     */
    public rx.Observable<ResponseNListBean<GroupSetUpMemberInformationEr>> getGroupMember(String groupId, String userId) {
        return HttpBase.getInstance().createApi(GroupService.class).getGroupMember(groupId,userId);
    }

    /**
     * 群成员信息
     * @param groupId 群id
     * @param userId 用户id
     * @return
     */
    public rx.Observable<ResponseNBean<String>> getNetworkShield(String groupId, String userId,String maskMessage) {
        return HttpBase.getInstance().createApi(GroupService.class).getNetworkShield(groupId,userId,maskMessage);
    }
}
