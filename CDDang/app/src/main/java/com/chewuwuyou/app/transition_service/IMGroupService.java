package com.chewuwuyou.app.transition_service;

import com.chewuwuyou.rong.bean.GroupBean;
import com.chewuwuyou.rong.bean.NewBaseNetworkBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xxy on 2016/10/17 0017.
 */

public interface IMGroupService {

    /**
     * 获取群信息
     *
     * @param groupId ID
     * @return
     */
    @POST("api/im/group/v1/getImGroupByPrimaryKey/{groupId}")
    Observable<NewBaseNetworkBean<GroupBean>> getGroupById(@Path("groupId") String groupId);

    /**
     * 判断是否是群成员
     *
     * @param groupId ID
     * @return
     */
    @POST("api/im/group/v1/getGroupMemberByGroupIdAndAccid")
    Observable<NewBaseNetworkBean<Integer>> isInGroup(@Query("groupId") String groupId, @Query("accid") String accid);


}
