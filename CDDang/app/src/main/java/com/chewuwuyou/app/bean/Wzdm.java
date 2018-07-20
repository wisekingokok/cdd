package com.chewuwuyou.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by CLOUD on 2016/8/5.
 */
public class Wzdm implements Parcelable {


    /**
     * action : aa
     * code : 101
     * punish_money : 200
     * score_reduce : 6
     */

    private String action;
    private String code;
    private String punish_money;
    private int score_reduce;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPunish_money() {
        return punish_money;
    }

    public void setPunish_money(String punish_money) {
        this.punish_money = punish_money;
    }

    public int getScore_reduce() {
        return score_reduce;
    }

    public void setScore_reduce(int score_reduce) {
        this.score_reduce = score_reduce;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeString(this.code);
        dest.writeString(this.punish_money);
        dest.writeInt(this.score_reduce);
    }

    public Wzdm() {
    }

    protected Wzdm(Parcel in) {
        this.action = in.readString();
        this.code = in.readString();
        this.punish_money = in.readString();
        this.score_reduce = in.readInt();
    }

    public static final Parcelable.Creator<Wzdm> CREATOR = new Parcelable.Creator<Wzdm>() {
        @Override
        public Wzdm createFromParcel(Parcel source) {
            return new Wzdm(source);
        }

        @Override
        public Wzdm[] newArray(int size) {
            return new Wzdm[size];
        }
    };



    public static List<Wzdm> parseList(String WzdmJson) {
        List<Wzdm> Wzdms = null;
        if (WzdmJson != null) {
            Gson g = new Gson();
            Wzdms = g.fromJson(WzdmJson, new TypeToken<List<Wzdm>>() {
            }.getType());
        }
        return Wzdms;
    }
}
