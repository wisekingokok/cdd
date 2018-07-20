package com.chewuwuyou.app.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class CacheTools {
    private static final String USER_DATA = "userdata";
    private static final String CACHE_DATA = "cachedata";
    private static final String VEHICLE_DATE = "vehicledate";
    private static final String USER_LOGIN_DATA = "userlogindata";
    private static final String OTHER_TO_CACHE = "othertocache";

    private static SharedPreferences mUserSharedPre;
    private static SharedPreferences mCacheSharedPre;
    private static SharedPreferences mVehicleSharedPre;
    private static SharedPreferences mUserLoginSharePre;
    private static SharedPreferences mOtherNeedToCacheSharePre;

    public static void initCache(Context context) {
        mUserSharedPre = context.getSharedPreferences(USER_DATA, 0);
        mCacheSharedPre = context.getSharedPreferences(CACHE_DATA, 0);
        mVehicleSharedPre = context.getSharedPreferences(VEHICLE_DATE, 0);
        mUserLoginSharePre = context.getSharedPreferences(USER_LOGIN_DATA, 0);
        mOtherNeedToCacheSharePre = context.getSharedPreferences(OTHER_TO_CACHE, 0);
    }


    public static void setUserLoginData(String name, String val) {
        SharedPreferences.Editor editor = mUserLoginSharePre.edit();
        editor.putString(name, val);
        editor.commit();
    }

    public static String getUserLoginData(String url) {
        return mUserLoginSharePre.getString(url, null);
    }

    public static void clearUserLoginData() {
        SharedPreferences.Editor editor = mUserLoginSharePre.edit();
        editor.clear();
        editor.commit();
    }

    public static void setUserData(String name, String val) {
        SharedPreferences.Editor editor = mUserSharedPre.edit();
        editor.putString(name, val);
        editor.commit();
    }

    public static void setUserData(String name, int val) {
        SharedPreferences.Editor editor = mUserSharedPre.edit();
        editor.putInt(name, val);
        editor.commit();
    }

    public static void clearUserData(String name) {
        SharedPreferences.Editor editor = mUserSharedPre.edit();
        editor.remove(name);
        editor.commit();
    }

    public static void clearAllUserData() {
        SharedPreferences.Editor editor = mUserSharedPre.edit();
        editor.clear();
        editor.commit();
    }

    public static String getUserData(String name) {
        return mUserSharedPre.getString(name, null);
    }

    public static int getUserData(String name, int defaul) {
        return mUserSharedPre.getInt(name, defaul);
    }

    public static String getCacheStr(String url) {
        return mCacheSharedPre.getString(url, null);
    }

    public static boolean saveCacheStr(String url, String str) {
        boolean isSuccess = false;
        if (str != null) {
            SharedPreferences.Editor editor = mCacheSharedPre.edit();
            editor.putString(url, str);
            editor.commit();
            isSuccess = editor.commit();
        }
        return isSuccess;
    }

    public static boolean saveObject(String key, Object valueObject)
            throws IOException {
        boolean isSuccess = false;
        if (valueObject != null) {
            SharedPreferences.Editor editor = mCacheSharedPre.edit();
            String value = serialization(valueObject);
            editor.putString(key, value);
            isSuccess = editor.commit();
        }
        return isSuccess;
    }

    public static Object getObject(String key) throws IOException,
            ClassNotFoundException {
        Object obj = null;
        String strObject = mCacheSharedPre.getString(key, null);
        if (strObject != null) {
            obj = deSerialization(strObject);
        }
        return obj;
    }

    /**
     * 存储车辆信息
     *
     * @param key
     * @param valueObject
     * @return
     * @throws IOException
     */
    public static boolean saveVehicleObject(String key, Object valueObject)
            throws IOException {
        boolean isSuccess = false;
        if (valueObject != null) {
            SharedPreferences.Editor editor = mVehicleSharedPre.edit();
            String value = serialization(valueObject);
            editor.putString(key, value);
            isSuccess = editor.commit();
        }
        return isSuccess;
    }

    public static Object getVehicleObject(String key) throws IOException,
            ClassNotFoundException {
        Object obj = null;
        String strObject = mVehicleSharedPre.getString(key, null);
        if (strObject != null) {
            obj = deSerialization(strObject);
        }
        return obj;
    }

    public static void clearObject(String name) {
        SharedPreferences.Editor editor = mCacheSharedPre.edit();
        editor.remove(name);
        editor.commit();
    }

    public static void clearAllObject() {
        SharedPreferences.Editor editor = mUserSharedPre.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 序列化对象
     *
     * @param object
     * @return
     * @throws IOException
     */
    private static String serialization(Object object) throws IOException {
        long startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        long endTime = System.currentTimeMillis();
        Log.d("serial", "序列化耗时为:" + (endTime - startTime));
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Object deSerialization(String str) throws IOException,
            ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        long endTime = System.currentTimeMillis();
        Log.d("serial", "反序列化耗时为:" + (endTime - startTime));
        return object;
    }


    //
    public static void setOtherCacheData(String key, String val) {
        SharedPreferences.Editor editor = mOtherNeedToCacheSharePre.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static String getOtherCacheData(String key) {
        return mOtherNeedToCacheSharePre.getString(key, null);
    }

    public static void clearOtherData(String name) {
        SharedPreferences.Editor editor = mOtherNeedToCacheSharePre.edit();
        editor.remove(name);
        editor.commit();
    }

    public static String getRongUserId() {
        return mUserSharedPre.getString("rongUserId", null);
    }
}
