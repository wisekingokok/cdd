package com.chewuwuyou.app.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class GroupSetUpMemberInformationEr {


    private String friend_id;
    private String accid;
    private String head_image_url;
    private String user_friend_name;
    private String user_own_name;
    private String user_group_name;
    private String sortLetters;  //显示数据拼音的首字母
    private boolean isSelected;//判断是否选中

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public GroupSetUpMemberInformationEr() {
    }

    public GroupSetUpMemberInformationEr(String friend_id, String head_image_url, String user_friend_name, String user_own_name, String accid, String user_group_name) {
        this.friend_id = friend_id;
        this.head_image_url = head_image_url;
        this.user_friend_name = user_friend_name;
        this.user_own_name = user_own_name;
        this.accid = accid;
        this.user_group_name = user_group_name;
    }

    public GroupSetUpMemberInformationEr(String friend_id, String accid, String head_image_url, String user_friend_name, String user_own_name, String user_group_name, String sortLetters, boolean isSelected) {
        this.friend_id = friend_id;
        this.accid = accid;
        this.head_image_url = head_image_url;
        this.user_friend_name = user_friend_name;
        this.user_own_name = user_own_name;
        this.user_group_name = user_group_name;
        this.sortLetters = sortLetters;
        this.isSelected = isSelected;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getHead_image_url() {
        return head_image_url;
    }

    public void setHead_image_url(String head_image_url) {
        this.head_image_url = head_image_url;
    }

    public String getUser_friend_name() {
        return user_friend_name;
    }

    public void setUser_friend_name(String user_friend_name) {
        this.user_friend_name = user_friend_name;
    }

    public String getUser_own_name() {
        return user_own_name;
    }

    public void setUser_own_name(String user_own_name) {
        this.user_own_name = user_own_name;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getUser_group_name() {
        return user_group_name;
    }

    public void setUser_group_name(String user_group_name) {
        this.user_group_name = user_group_name;
    }
}
