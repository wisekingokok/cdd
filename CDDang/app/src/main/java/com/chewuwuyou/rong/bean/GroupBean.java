package com.chewuwuyou.rong.bean;

/**
 * Created by xxy on 2016/9/17 0017.
 */
public class GroupBean {

    /**
     * id : 113
     * groupName : 健健康康
     * groupType : 0
     * groupSize : 200
     * groupValidate : 0
     * groupMain : 62612
     * groupAnnouncement : 刚刚嘎嘎嘎嘎
     * createdAt : 1473856084000
     * updatedAt : 1474102250000
     * remarks :
     * groupImgUrl : http://p0.ifengimg.com/cmpp/2016/09/14/12/b047841f-946e-450d-bd20-070f37202b82_size50_w600_h432.jpg
     * createFlag :
     * imGroupMemberCount : 5
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
