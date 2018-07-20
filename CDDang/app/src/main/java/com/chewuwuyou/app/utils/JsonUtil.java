package com.chewuwuyou.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class JsonUtil {
    public static Map<String, String> faceCharaterMap;
    public static Map<String, String> mFaceMap = new HashMap<String, String>();

    public static Map<String, String> getFaceCharacterjson(Context context) {
        // Map<String, String> faceCharacterMap = null;// 颜文字map
        if (faceCharaterMap == null) {
            try {
                JSONArray jaArray = new JSONArray(getFromAssets(context,
                        "facecharacter.json"));
                faceCharaterMap = new HashMap<String, String>();
                for (int i = 0; i < jaArray.length(); i++) {
                    faceCharaterMap.put(
                            jaArray.getJSONObject(i).getString("content"),
                            jaArray.getJSONObject(i).getString("src"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return faceCharaterMap;
    }

    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * json转map
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, String> getMapForJson(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            String value;
            Map<String, String> valueMap = new HashMap<String, String>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.getString(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * map key=[呵呵] value =d_hehe
     *
     * @param context
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> getFaceStrMap(Context context) {
        String key;
        try {
            JSONObject jo = new JSONObject(getFromAssets(context, "face.json"));
            Iterator iter = jo.keys();
            while (iter.hasNext()) {
                key = (String) iter.next();
                MyLog.i("YUY",
                        "faceMap key = " + key + " value = "
                                + jo.getString(key));
                mFaceMap.put(key, jo.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mFaceMap;
    }

    /**
     * 兼容API<19版本兼容问题
     *
     * @param index
     * @param jsonArray
     * @throws Exception
     */
    public static void JSONArrayRemove(int index, JSONArray jsonArray) throws Exception {
        if (index < 0)
            return;
        Field valuesField = JSONArray.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        List<Object> values = (List<Object>) valuesField.get(jsonArray);
        if (index >= values.size())
            return;
        values.remove(index);
    }
}
