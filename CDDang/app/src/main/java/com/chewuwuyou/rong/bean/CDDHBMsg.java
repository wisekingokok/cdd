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
        value = "CDD:RedPacketMsg",
        flag = 3
)
public class CDDHBMsg extends MessageContent {
    private static final String TAG = "CDDHBMsg";
    private String RedPacketID;//红包ID
    private String RedPacketType;//红包类型
    private String RedPacketRemake;//备注
    protected String extra;//融云消息预留字段,同样预留
    public static final Creator<CDDHBMsg> CREATOR = new Creator() {
        public CDDHBMsg createFromParcel(Parcel source) {
            return new CDDHBMsg(source);
        }

        public CDDHBMsg[] newArray(int size) {
            return new CDDHBMsg[size];
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
            jsonObj.put("RedPacketID", this.getRedPacketID());
            jsonObj.put("RedPacketType", this.getRedPacketType());
            if (!TextUtils.isEmpty(this.getRedPacketRemake())) {
                jsonObj.put("RedPacketRemake", this.getRedPacketRemake());
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
            RLog.e("CDDHBMsg", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    protected CDDHBMsg() {
    }

    public static CDDHBMsg obtain(String RedPacketID, String RedPacketRemake, String RedPacketType) {
        CDDHBMsg model = new CDDHBMsg();
        model.setRedPacketID(RedPacketID);
        model.setRedPacketRemake(RedPacketRemake);
        model.setRedPacketType(RedPacketType);
        return model;
    }

    public CDDHBMsg(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            ;
        }
        Log.e("jsonObj", jsonStr);
        try {
            JSONObject e = new JSONObject(jsonStr);
            if (e.has("RedPacketID")) {
                this.setRedPacketID(e.optString("RedPacketID"));
            }
            if (e.has("RedPacketType")) {
                this.setRedPacketType(e.optString("RedPacketType"));
            }
            if (e.has("RedPacketRemake")) {
                this.setRedPacketRemake(e.optString("RedPacketRemake"));
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
            RLog.e("CDDHBMsg", "JSONException " + var4.getMessage());
        }

    }

    public String getRedPacketID() {
        return RedPacketID;
    }

    public void setRedPacketID(String redPacketID) {
        RedPacketID = redPacketID;
    }

    public String getRedPacketRemake() {
        return RedPacketRemake;
    }

    public void setRedPacketRemake(String redPacketRemake) {
        RedPacketRemake = redPacketRemake;
    }

    public String getRedPacketType() {
        return RedPacketType;
    }

    public void setRedPacketType(String redPacketType) {
        RedPacketType = redPacketType;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.RedPacketID);
        ParcelUtils.writeToParcel(dest, this.RedPacketRemake);
        ParcelUtils.writeToParcel(dest, this.RedPacketType);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, this.getMentionedInfo());
    }

    public CDDHBMsg(Parcel in) {
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setRedPacketID(ParcelUtils.readFromParcel(in));
        this.setRedPacketRemake(ParcelUtils.readFromParcel(in));
        this.setRedPacketType(ParcelUtils.readFromParcel(in));
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setMentionedInfo((MentionedInfo) ParcelUtils.readFromParcel(in, MentionedInfo.class));
    }

    public CDDHBMsg(String RedPacketID, String RedPacketRemake, String RedPacketType) {
        this.setRedPacketType(RedPacketType);
        this.setRedPacketRemake(RedPacketRemake);
        this.setRedPacketID(RedPacketID);
    }
}
