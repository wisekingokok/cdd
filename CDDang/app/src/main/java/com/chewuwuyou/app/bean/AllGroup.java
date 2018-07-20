package com.chewuwuyou.app.bean;

import com.chewuwuyou.app.widget.ListViewForScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.annotation.sqlite.Id;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CLOUD on 2016/9/14.
 */
public class AllGroup implements Serializable {


    /**
     * groupId : 1
     * userId : 0
     * friendGroupName : 我的好友
     * friendSize : 0
     */


    private int id;
    private int groupId;
    private int userId;
    private String friendGroupName;
    private int friendSize;
    private List<Userfriend> friends;


    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFriendGroupName() {
        return friendGroupName;
    }

    public void setFriendGroupName(String friendGroupName) {
        this.friendGroupName = friendGroupName;
    }

    public int getFriendSize() {
        return friendSize;
    }

    public void setFriendSize(int friendSize) {
        this.friendSize = friendSize;
    }


    public List<Userfriend> getFriends() {
        return friends;
    }

    public void setFriends(List<Userfriend> friends) {
        this.friends = friends;
    }

    public static List<AllGroup> parseList(String s) {
        List<AllGroup> allGroups = null;
        if (s != null) {
            Gson g = new Gson();
            allGroups = g.fromJson(s, new TypeToken<List<AllGroup>>() {
            }.getType());
        }
        return allGroups;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
