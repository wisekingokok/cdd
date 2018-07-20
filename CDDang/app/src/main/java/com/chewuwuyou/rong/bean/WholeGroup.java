package com.chewuwuyou.rong.bean;


import java.io.Serializable;

public class WholeGroup implements Serializable {
    private int id;
    private String groupName;
    private String groupType;
    private String groupSize;
    private String groupValidate;
    private String groupMain;
    private String groupAnnouncement;
    private String createdAt;
    private String updatedAt;
    private String remarks;
    private String groupImgUrl;
    private String createFlag;
    private String imGroupMemberCount;


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

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(String groupSize) {
        this.groupSize = groupSize;
    }

    public String getGroupValidate() {
        return groupValidate;
    }

    public void setGroupValidate(String groupValidate) {
        this.groupValidate = groupValidate;
    }

    public String getGroupMain() {
        return groupMain;
    }

    public void setGroupMain(String groupMain) {
        this.groupMain = groupMain;
    }

    public String getGroupAnnouncement() {
        return groupAnnouncement;
    }

    public void setGroupAnnouncement(String groupAnnouncement) {
        this.groupAnnouncement = groupAnnouncement;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getImGroupMemberCount() {
        return imGroupMemberCount;
    }

    public void setImGroupMemberCount(String imGroupMemberCount) {
        this.imGroupMemberCount = imGroupMemberCount;
    }
}
