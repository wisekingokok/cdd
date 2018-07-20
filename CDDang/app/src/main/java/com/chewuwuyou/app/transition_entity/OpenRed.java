package com.chewuwuyou.app.transition_entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuyong on 16/10/13.
 */

public class OpenRed implements Serializable{

        /**
         * id :
         * size : 1
         * money : 20
         * type : 0
         * leaveMessage : 恭喜发财
         * accid : 20444
         * payType :
         * unitPrice :
         * status :
         * redCode :
         * accidFriend : 24900
         * finRedPacketsInDtos : [{"accid":24900,"redPacketsOutId":8,"money":20,"leaveMessage":"","nickName":"11","headImg":"http://image.cddang.com/spng//upload/2016080911024124900393.jpg","theBest":0,"createAt":"2016-10-13 18:37:18"}]
         * nickName : 好污
         * headImg : http://image.cddang.com/spng//upload/2016100813555820444208.jpg
         * remainSize : 1
         * remainMoney : 20
         */

        private String id;
        private int size;
        private int money;
        private int type;
        private String leaveMessage;
        private int accid;
        private String payType;
        private String unitPrice;
        private String status;
        private String redCode;
        private int accidFriend;
        private String nickName;
        private String headImg;
        private int remainSize;
        private int remainMoney;

        private List<FinRedPacketsInDtosBean> finRedPacketsInDtos;

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

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
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

        public int getAccid() {
            return accid;
        }

        public void setAccid(int accid) {
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
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

        public int getRemainSize() {
            return remainSize;
        }

        public void setRemainSize(int remainSize) {
            this.remainSize = remainSize;
        }

        public int getRemainMoney() {
            return remainMoney;
        }

        public void setRemainMoney(int remainMoney) {
            this.remainMoney = remainMoney;
        }

        public List<FinRedPacketsInDtosBean> getFinRedPacketsInDtos() {
            return finRedPacketsInDtos;
        }

        public void setFinRedPacketsInDtos(List<FinRedPacketsInDtosBean> finRedPacketsInDtos) {
            this.finRedPacketsInDtos = finRedPacketsInDtos;
        }
   }
