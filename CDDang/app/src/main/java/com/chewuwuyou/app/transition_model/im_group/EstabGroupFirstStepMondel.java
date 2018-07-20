package com.chewuwuyou.app.transition_model.im_group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_entity.AddGroupMember;
import com.chewuwuyou.app.transition_entity.BaseListBean;
import com.chewuwuyou.app.transition_entity.EstablishGroup;
import com.chewuwuyou.app.transition_model.BaseModel;
import com.chewuwuyou.app.transition_presenter.BasePresenter;
import com.chewuwuyou.app.transition_service.im_group.GroupService;
import com.chewuwuyou.app.transition_utils.HttpBase;
import com.chewuwuyou.app.transition_view.activity.im_group.EstablishGroupFirstStepErActivtiy;
import com.chewuwuyou.app.transition_view.activity.im_group_fragment.EstablishGroupFirstStepErFragment;
import com.chewuwuyou.app.utils.Constant;

import io.rong.imlib.model.Message;

/**
 * 群列表
 * liuchun
 */

public class EstabGroupFirstStepMondel extends BaseModel {

    /**
     * 添加群成员
     * @return
     */
    public rx.Observable<AddGroupMember> addGroupMondel(String groupName, String groupId, String loginUserId, String userIds) {
        return HttpBase.getInstance().createApi(GroupService.class).addGroupMember(groupName,groupId,loginUserId,userIds);
    }
    /**
     * 群成员列表
     * @param userId 用户id
     * @return
     */
    public rx.Observable<EstablishGroup> groupMondelList(String userId) {
        return HttpBase.getInstance().createApi(GroupService.class).friendsList(userId);
    }

    /**
     * 创建群添加成员
     * @param userId 用户id
     * @return
     */
    public rx.Observable<EstablishGroup> EstablisFilterGroupMember(String userId,String groupId) {
        return HttpBase.getInstance().createApi(GroupService.class).EstablishFilterGroupMember(userId,groupId);
    }


}
