package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * Created by CLOUD on 2016/9/14.
 */
public class SearchGroupData implements Serializable {


    private String del_flag;
    private String updated_at;
    private String group_id;
    private String group_name;
    private String accid;
    private String created_at;
    private String mask_message;
    private String id;
    private String remark_name;
    private String remarks;
    private String status;
    private String group_img_url;
    private String head_image_url;
    private String user_own_name;
    private String user_group_name;

    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMask_message() {
        return mask_message;
    }

    public void setMask_message(String mask_message) {
        this.mask_message = mask_message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark_name() {
        return remark_name;
    }

    public void setRemark_name(String remark_name) {
        this.remark_name = remark_name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroup_img_url() {
        return group_img_url;
    }

    public void setGroup_img_url(String group_img_url) {
        this.group_img_url = group_img_url;
    }

    public String getHead_image_url() {
        return head_image_url;
    }

    public void setHead_image_url(String head_image_url) {
        this.head_image_url = head_image_url;
    }

    public String getUser_own_name() {
        return user_own_name;
    }

    public void setUser_own_name(String user_own_name) {
        this.user_own_name = user_own_name;
    }

    public String getUser_group_name() {
        return user_group_name;
    }

    public void setUser_group_name(String user_group_name) {
        this.user_group_name = user_group_name;
    }
}
