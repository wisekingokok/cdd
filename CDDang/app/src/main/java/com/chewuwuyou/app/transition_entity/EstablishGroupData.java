package com.chewuwuyou.app.transition_entity;

import java.io.Serializable;

/**
 * Created by yuyong on 16/10/13.
 */

public class EstablishGroupData implements Serializable{


    /**
     * id : 100000219
     * groupName : 提交
     * groupType : 0
     * groupSize : 200
     * groupValidate : 0
     * groupMain : 17547
     * groupAnnouncement :
     * createdAt : 1476697248000
     * updatedAt : 1476697248000
     * remarks :
     * groupImgUrl : http://img.cddang.com/1476697230813
     * createFlag :
     * imGroupMemberCount : 3
     */

    private int id;
    private String groupName;
    private int groupType;
    private int groupSize;
    private int groupValidate;
    private int groupMain;
    private String groupAnnouncement;
    private long createdAt;
    private long updatedAt;
    private String remarks;
    private String groupImgUrl;
    private String createFlag;
    private String imGroupMemberCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public int getGroupValidate() {
        return groupValidate;
    }

    public void setGroupValidate(int groupValidate) {
        this.groupValidate = groupValidate;
    }

    public int getGroupMain() {
        return groupMain;
    }

    public void setGroupMain(int groupMain) {
        this.groupMain = groupMain;
    }

    public String getGroupAnnouncement() {
        return groupAnnouncement;
    }

    public void setGroupAnnouncement(String groupAnnouncement) {
        this.groupAnnouncement = groupAnnouncement;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getGroupImgUrl() {
        return groupImgUrl;
    }

    public void setGroupImgUrl(String groupImgUrl) {
        this.groupImgUrl = groupImgUrl;
    }

    public String getCreateFlag() {
        return createFlag;
    }

    public void setCreateFlag(String createFlag) {
        this.createFlag = createFlag;
    }

    public String getImGroupMemberCount() {
        return imGroupMemberCount;
    }

    public void setImGroupMemberCount(String imGroupMemberCount) {
        this.imGroupMemberCount = imGroupMemberCount;
    }
}
