package com.chewuwuyou.app.bean;

import com.google.gson.Gson;

/**
 * 群基本信息
 * liuchun
 */
public class GroupSetUpEssential{

    private String group_main;
    private String group_name;
    private String group_img_url;
    private String group_announcement;
    private String mask_message;
    private int id;
    private String remark_name;
    private String memberCount;

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }

    public String getGroup_main() {
        return group_main;
    }

    public void setGroup_main(String group_main) {
        this.group_main = group_main;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_img_url() {
        return group_img_url;
    }

    public void setGroup_img_url(String group_img_url) {
        this.group_img_url = group_img_url;
    }

    public String getGroup_announcement() {
        return group_announcement;
    }

    public void setGroup_announcement(String group_announcement) {
        this.group_announcement = group_announcement;
    }

    public String getMask_message() {
        return mask_message;
    }

    public void setMask_message(String mask_message) {
        this.mask_message = mask_message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark_name() {
        return remark_name;
    }

    public void setRemark_name(String remark_name) {
        this.remark_name = remark_name;
    }
    public static GroupSetUpEssential parse(String pingJson) {
        GroupSetUpEssential ping = null;
        if (pingJson != null) {
            Gson g = new Gson();
            ping = g.fromJson(pingJson, GroupSetUpEssential.class);
        }
        return ping;
    }
}
