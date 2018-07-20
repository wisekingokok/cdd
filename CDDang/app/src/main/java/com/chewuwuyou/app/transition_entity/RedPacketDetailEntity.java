package com.chewuwuyou.app.transition_entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CLOUD on 2016/10/10.
 */
public class RedPacketDetailEntity implements Serializable {

    /**
     * id : 212
     * size : 1
     * money : 13
     * type : 0
     * leaveMessage : 恭喜发财，大吉大利！
     * accid : 17547
     * payType :
     * unitPrice :
     * status : 1
     * redCode :
     * accidFriend : 32002
     * finRedPacketsInDtos : []
     * nickName : 春春33
     * headImg : http://101.204.230.251/spng//upload/2016082221554117547209.jpg
     * remainSize :
     * remainMoney :
     * myMoney :
     * lootAllTime :
     * createdAt : 1476869853000
     * remarks :
     * receiveStatus : 0
     */

    private String id;
    private int size;
    private Double money;
    private int type;
    private String leaveMessage;
    private String accid;
    private String payType;
    private String unitPrice;
    private int status;
    private String redCode;
    private int accidFriend;
    private String nickName;
    private String headImg;
    private String remainSize;
    private String remainMoney;
    private String myMoney;
    private String lootAllTime;
    private long createdAt;
    private String remarks;
    private String receiveStatus;
    public List<RedPerson> finRedPacketsInDtos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRedCode() {
        return redCode;
    }

    public void setRedCode(String redCode) {
        this.redCode = redCode;
    }

    public int getAccidFriend() {
        return accidFriend;
    }

    public void setAccidFriend(int accidFriend) {
        this.accidFriend = accidFriend;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getRemainSize() {
        return remainSize;
    }

    public void setRemainSize(String remainSize) {
        this.remainSize = remainSize;
    }

    public String getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(String remainMoney) {
        this.remainMoney = remainMoney;
    }

    public String getMyMoney() {
        return myMoney;
    }

    public void setMyMoney(String myMoney) {
        this.myMoney = myMoney;
    }

    public String getLootAllTime() {
        return lootAllTime;
    }

    public void setLootAllTime(String lootAllTime) {
        this.lootAllTime = lootAllTime;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public List<RedPerson> getFinRedPacketsInDtos() {
        return finRedPacketsInDtos;
    }

    public void setFinRedPacketsInDtos(List<RedPerson> finRedPacketsInDtos) {
        this.finRedPacketsInDtos = finRedPacketsInDtos;
    }

    public static class RedPerson implements Serializable {

        /**
         * "accid": 24900,领取者id
         * "redPacketsOutId": 8,红包id
         * "money": 20,领取金额
         * "leaveMessage": "",留言
         * "nickName": "11",昵称
         * "headImg": "http://image.cddang.com/spng//upload/2016080911024124900393.jpg",头像
         * "theBest": 0,是否是最佳手气（0不是，1是）
         * "createAt": "2016-10-13 18:37:18"领取时间
         */

        private int accid;
        private int redPacketsOutId;
        private double money;
        private String leaveMessage;
        private String nickName;
        private String headImg;
        private int theBest;
        private String createAt;


        public double getMoney() {
            return money;
        }

        public void setMoney(double mMoney) {
            money = mMoney;
        }

        public int getAccid() {
            return accid;
        }

        public void setAccid(int accid) {
            this.accid = accid;
        }

        public int getRedPacketsOutId() {
            return redPacketsOutId;
        }

        public void setRedPacketsOutId(int redPacketsOutId) {
            this.redPacketsOutId = redPacketsOutId;
        }


        public String getLeaveMessage() {
            return leaveMessage;
        }

        public void setLeaveMessage(String leaveMessage) {
            this.leaveMessage = leaveMessage;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getTheBest() {
            return theBest;
        }

        public void setTheBest(int theBest) {
            this.theBest = theBest;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }
    }
}
