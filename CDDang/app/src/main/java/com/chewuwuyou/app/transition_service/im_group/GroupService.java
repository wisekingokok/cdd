package com.chewuwuyou.app.transition_service.im_group;

import com.chewuwuyou.app.bean.GroupSetUpEssential;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.transition_entity.AddGroupMember;
import com.chewuwuyou.app.transition_entity.BaseListBean;
import com.chewuwuyou.app.transition_entity.EstablishGroup;
import com.chewuwuyou.app.transition_entity.EstablishGroupSuccess;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.ResponseNListBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_model.BaseModel;
import com.chewuwuyou.app.transition_utils.HttpBase;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.rong.bean.WholeGroup;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 群列表
 * liuchun
 */

public interface GroupService {

    /**
     * @param userId 用户id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/im/group/v1/selectAllImGroupsByUserId")
    Observable<BaseListBean> groupListService(@Field("userId") String userId);

    /**
     * 过滤已经添加过的成员
     */
    @FormUrlEncoded
    @POST("/api/im/group/v1/joinGroupBatch")
    Observable<AddGroupMember> addGroupMember(@Field("groupName") String groupName, @Field("groupId") String groupId, @Field("loginUserId") String loginUserId, @Field("userIds") String userIds);

    /**
     * 获取好友列表
     */
    @FormUrlEncoded
    @POST("/api/im/friend/v1/friendGroup")
    Observable<EstablishGroup> friendsList(@Field("userId") String userId);

    /**
     *创建群添加成员
     */
    @FormUrlEncoded
    @POST("/api/im/friend/v1/friendGroupWithFilter")
    Observable<EstablishGroup> EstablishFilterGroupMember(@Field("userId") String userId,@Field("groupId") String groupId);

    /**
     * 创建群
     */
    @FormUrlEncoded
    @POST("/api/im/group/v1/addImGroup")
    Observable<EstablishGroupSuccess> groupCreation(@Field("userIds") String userIds, @Field("groupMain") String groupMain, @Field("groupName") String groupName, @Field("groupImgUrl") String groupImgUrl);

    /**
     * 群基本信息
     */
    @FormUrlEncoded
    @POST("/api/im/group/v1/getImGroupInfoByGroupIdAndUserId")
    Observable<ResponseNBean<GroupSetUpEssential>> getGroupBasic(@Field("groupId") String groupId, @Field("userId") String userId);

    /**
     * 群成员信息
     */
    @FormUrlEncoded
    @POST("/api/im/group/v1/getAllGroupMemberInfo")
    Observable<ResponseNListBean<GroupSetUpMemberInformationEr>> getGroupMember(@Field("groupId") String groupId, @Field("userId") String userId);

    /**
     * 屏蔽群消息
     */
    @FormUrlEncoded
    @POST("/api/im/group/v1/updateImGroupMemberMaskMessage")
    Observable<ResponseNBean<String>> getNetworkShield(@Field("groupId") String groupId, @Field("userId") String userId,@Field("maskMessage") String maskMessage);

}
