package com.chewuwuyou.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chewuwuyou.app.ui.BlacklistActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by CLOUD on 2016/8/10.
 */
public class BlackList implements Parcelable {


    /**
     * headImage :
     * me : 22010
     * nickName : 陈晓峰
     * other : 22030
     * sortLetters:首字母
     */
    private String dangDangHao;
    private String headImage;
    private String me;//我的id
    private String nickName;
    private String other;//对方的id
    public String sortLetters;  //显示数据拼音的首字母

    public String getDangDangHao() {
        return dangDangHao;
    }

    public void setDangDangHao(String mDangDangHao) {
        dangDangHao = mDangDangHao;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String mSortLetters) {
        sortLetters = mSortLetters;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getE() {
        return me;
    }

    public void setE(String e) {
        me = e;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.headImage);
        dest.writeString(this.me);
        dest.writeString(this.nickName);
        dest.writeString(this.other);
        dest.writeString(this.sortLetters);
    }

    public BlackList(String mHeadImage, String mMe, String mOther, String mNickName, String mSortLetters) {
        headImage = mHeadImage;
        me = mMe;
        other = mOther;
        nickName = mNickName;
        sortLetters = mSortLetters;
    }

    public BlackList() {
    }

    protected BlackList(Parcel in) {
        this.headImage = in.readString();
        this.me = in.readString();
        this.nickName = in.readString();
        this.other = in.readString();
        this.sortLetters = in.readString();
    }

    public static final Parcelable.Creator<BlackList> CREATOR = new Parcelable.Creator<BlackList>() {
        @Override
        public BlackList createFromParcel(Parcel source) {
            return new BlackList(source);
        }

        @Override
        public BlackList[] newArray(int size) {
            return new BlackList[size];
        }
    };


    public static List<BlackList> parseList(String userJson) {
        List<BlackList> csts = null;
        if (userJson != null) {
            Gson g = new Gson();
            csts = g.fromJson(userJson, new TypeToken<List<BlackList>>() {
            }.getType());
        }
        return csts;
    }


}
