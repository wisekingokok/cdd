package com.chewuwuyou.app.transition_model.im_group;

import android.database.Observable;

import com.chewuwuyou.app.transition_entity.BaseListBean;
import com.chewuwuyou.app.transition_model.BaseModel;
import com.chewuwuyou.app.transition_service.im_group.GroupService;
import com.chewuwuyou.app.transition_utils.HttpBase;
import com.chewuwuyou.rong.bean.WholeGroup;

import java.util.List;

/**
 * 群列表
 * liuchun
 */

public class GroupListFragmentMondel extends BaseModel {

    /**
     * @param userId 用户id
     * @return
     */
    public rx.Observable<BaseListBean> requestGroupMondel(String userId) {
        return HttpBase.getInstance().createApi(GroupService.class).groupListService(userId);
    }
}
