package com.chewuwuyou.rong.bean;


import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

@MessageTag(
        value = "CDD:TransferMoneyMsg",
        flag = 3
)
public class CDDZZMsg extends MessageContent {
    private static final String TAG = "CDDZZMsg";
    private String TransferMoneyID;//转账ID
    private String money;//转账资金
    private String remake;//备注
    protected String extra;//融云消息预留字段,同样预留
    public static final Creator<CDDZZMsg> CREATOR = new Creator() {
        public CDDZZMsg createFromParcel(Parcel source) {
            return new CDDZZMsg(source);
        }

        public CDDZZMsg[] newArray(int size) {
            return new CDDZZMsg[size];
        }
    };

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("TransferMoneyID", this.getTransferMoneyID());
            jsonObj.put("money", this.getMoney());
            if (!TextUtils.isEmpty(this.getRemake())) {
                jsonObj.put("remake", this.getRemake());
            }
            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            if (this.getJsonMentionInfo() != null) {
                jsonObj.putOpt("mentionedInfo", this.getJsonMentionInfo());
            }
        } catch (JSONException var4) {
            RLog.e("CDDZZMsg", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    protected CDDZZMsg() {
    }

    public static CDDZZMsg obtain(String TransferMoneyID, String Remake, String money) {
        CDDZZMsg model = new CDDZZMsg();
        model.setTransferMoneyID(TransferMoneyID);
        model.setRemake(Remake);
        model.setMoney(money);
        return model;
    }

    public CDDZZMsg(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            ;
        }
        Log.e("jsonObj", jsonStr);
        try {
            JSONObject e = new JSONObject(jsonStr);
            if (e.has("TransferMoneyID")) {
                this.setTransferMoneyID(e.optString("TransferMoneyID"));
            }
            if (e.has("money")) {
                this.setMoney(e.optString("money"));
            }
            if (e.has("remake")) {
                this.setRemake(e.optString("remake"));
            }
            if (e.has("extra")) {
                this.setExtra(e.optString("extra"));
            }

            if (e.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(e.getJSONObject("user")));
            }

            if (e.has("mentionedInfo")) {
                this.setMentionedInfo(this.parseJsonToMentionInfo(e.getJSONObject("mentionedInfo")));
            }
        } catch (JSONException var4) {
            RLog.e("CDDZZMsg", "JSONException " + var4.getMessage());
        }

    }

    public String getTransferMoneyID() {
        return TransferMoneyID;
    }

    public void setTransferMoneyID(String TransferMoneyID) {
        this.TransferMoneyID = TransferMoneyID;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String Remake) {
        this.remake = Remake;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.TransferMoneyID);
        ParcelUtils.writeToParcel(dest, this.remake);
        ParcelUtils.writeToParcel(dest, this.money);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, this.getMentionedInfo());
    }

    public CDDZZMsg(Parcel in) {
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setTransferMoneyID(ParcelUtils.readFromParcel(in));
        this.setRemake(ParcelUtils.readFromParcel(in));
        this.setMoney(ParcelUtils.readFromParcel(in));
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setMentionedInfo((MentionedInfo) ParcelUtils.readFromParcel(in, MentionedInfo.class));
    }

    public CDDZZMsg(String TransferMoneyID, String Remake, String money) {
        this.setMoney(money);
        this.setRemake(Remake);
        this.setTransferMoneyID(TransferMoneyID);
    }
}
