package com.chewuwuyou.app.bean;

/**
 * Created by CLOUD on 2016/9/13.
 */
public class Group {

    String friendGroupName;
    String groupId;

    public String getFriendGroupName() {
        return friendGroupName;
    }

    public void setFriendGroupName(String friendGroupName) {
        this.friendGroupName = friendGroupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Group(String friendGroupName) {
        this.friendGroupName = friendGroupName;
    }
}
