package com.chewuwuyou.rong.bean;

import java.util.List;

/**
 * Created by xxy on 2016/9/23 0023.
 */
public class GroupNTFMsgDataBean {

    private String operatorNickname;
    private String targetGroupName;
    private String targetUserDisplayName;
    private String timestamp;
    private List<String> targetUserDisplayNames;
    private List<String> targetUserIds;

    private String targetUserId;

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTargetUserDisplayName() {
        return targetUserDisplayName;
    }

    public void setTargetUserDisplayName(String targetUserDisplayName) {
        this.targetUserDisplayName = targetUserDisplayName;
    }

    public String getOperatorNickname() {
        return operatorNickname;
    }

    public void setOperatorNickname(String operatorNickname) {
        this.operatorNickname = operatorNickname;
    }

    public String getTargetGroupName() {
        return targetGroupName;
    }

    public void setTargetGroupName(String targetGroupName) {
        this.targetGroupName = targetGroupName;
    }

    public List<String> getTargetUserDisplayNames() {
        return targetUserDisplayNames;
    }

    public void setTargetUserDisplayNames(List<String> targetUserDisplayNames) {
        this.targetUserDisplayNames = targetUserDisplayNames;
    }

    public List<String> getTargetUserIds() {
        return targetUserIds;
    }

    public void setTargetUserIds(List<String> targetUserIds) {
        this.targetUserIds = targetUserIds;
    }
}
