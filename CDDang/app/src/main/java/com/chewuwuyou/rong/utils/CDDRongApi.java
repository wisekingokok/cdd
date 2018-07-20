package com.chewuwuyou.rong.utils;

import android.text.TextUtils;
import android.util.Log;

import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.rong.bean.GroupBean;
import com.chewuwuyou.rong.bean.NewBaseNetworkBean;
import com.chewuwuyou.rong.bean.RongUserBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * Created by xxy on 2016/9/17 0017.
 */
public class CDDRongApi {

    public interface NetWorkCallback<T> {
        void onSuccess(String id, T t);

        void onFailure(Throwable t, int errorNo, String strMsg);
    }

    /**
     * 判断是否在群里
     *
     * @param groupId
     * @param accid
     * @param netWorkCallback
     */
    public static void isInGroup(String groupId, final String accid, final NetWorkCallback<Boolean> netWorkCallback) {
        AjaxParams params = new AjaxParams();
        params.put("groupId", groupId);
        params.put("accid", accid);
        NetworkUtil.postMulti(NetworkUtil.IS_IN_GROUP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Gson gson = new Gson();
                try {
                    NewBaseNetworkBean<String> newBase = gson.fromJson(s, new TypeToken<NewBaseNetworkBean<String>>() {
                    }.getType());
                    netWorkCallback.onSuccess(accid, TextUtils.isEmpty(newBase.getData()) ? true : !newBase.getData().equals("1"));
                } catch (Exception e) {
                    onFailure(e.getCause(), 10052, "数据解析错误");
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                netWorkCallback.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * 取得人信息
     *
     * @param id
     * @param userId
     * @param netWorkCallback
     */
    public static void getUserById(final String id, String userId, final NetWorkCallback<List<RongUserBean>> netWorkCallback) {
        AjaxParams params = new AjaxParams();
        params.put("ids", id);
        NetworkUtil.postMulti(NetworkUtil.GET_USER_INFO, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Gson gson = new Gson();
                try {
                    NewBaseNetworkBean<List<RongUserBean>> newBase = gson.fromJson(s, new TypeToken<NewBaseNetworkBean<List<RongUserBean>>>() {
                    }.getType());
                    netWorkCallback.onSuccess(id, newBase.getData());
                } catch (Exception e) {
                    onFailure(e.getCause(), 10052, "数据解析错误");
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                netWorkCallback.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * 取得群信息
     *
     * @param groupId
     * @param netWorkCallback
     */
    public static void getGroupById(final String groupId, final NetWorkCallback<GroupBean> netWorkCallback) {
        NetworkUtil.get(NetworkUtil.XMPP_HOST_URL + "/api/im/group/v1/getImGroupByPrimaryKey/" + groupId, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Gson gson = new Gson();
                try {
                    NewBaseNetworkBean<GroupBean> newBase = gson.fromJson(s, new TypeToken<NewBaseNetworkBean<GroupBean>>() {
                    }.getType());
                    netWorkCallback.onSuccess(groupId, newBase.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(e.getCause(), 10052, "数据解析错误");
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                netWorkCallback.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * 获取群成员信息
     *
     * @param groupId
     * @param userId
     * @param netWorkCallback
     */
    public static void getGroupMeber(final String groupId, String userId, final NetWorkCallback<List<GroupSetUpMemberInformationEr>> netWorkCallback) {
        AjaxParams params = new AjaxParams();
        params.put("groupId", groupId);
        params.put("userId", userId);
        NetworkUtil.get(NetworkUtil.GROUP_SET_UP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                MyLog.e("---", "解析群成员信息 = " + s);
                Gson gson = new Gson();
                try {
                    NewBaseNetworkBean<List<GroupSetUpMemberInformationEr>> newBase = gson.fromJson(s, new TypeToken<NewBaseNetworkBean<List<GroupSetUpMemberInformationEr>>>() {
                    }.getType());
                    netWorkCallback.onSuccess(groupId, newBase.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(e.getCause(), 10052, "数据解析错误");
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                netWorkCallback.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * 刷新当前用户信息
     *
     * @param userId
     */
    public static void refreshUser(final String userId, String name, String url, final NetWorkCallback<Boolean> netWorkCallback) {
        AjaxParams params = new AjaxParams();
        params.put("userId", userId);
        params.put("name", name);
        if (url != null)
            params.put("portraitUri", url);
        NetworkUtil.postMulti(NetworkUtil.XMPP_HOST_URL + "/api/im/user/v1/refreshUser", params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Gson gson = new Gson();
                try {
                    NewBaseNetworkBean<String> bean = gson.fromJson(s, new TypeToken<NewBaseNetworkBean<String>>() {
                    }.getType());
                    netWorkCallback.onSuccess(userId, bean.getCode() == 0);
                } catch (Exception e) {
                    onFailure(e, 10058, "数据解析错误");
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                netWorkCallback.onFailure(t, errorNo, strMsg);
            }
        });
    }
}
