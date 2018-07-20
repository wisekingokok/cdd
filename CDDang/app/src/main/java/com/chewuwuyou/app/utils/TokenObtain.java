package com.chewuwuyou.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 车当当相关协议的工具类
 *
 * @author yuyong
 */
public class TokenObtain {

    /**
     * 获取七牛taken
     */
    public void Group(final Context context) {
        NetworkUtil.get(NetworkUtil.QI_NIU_TOKEN, null, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    CacheTools.setUserData("qiniutoken", jsonObject.getString("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.toastShow(context,"token获取失败");
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                ToastUtil.toastShow(context,strMsg);
            }
        });
    }


    /**
     * 保存本压缩图片
     * @param bitmap
     */
    public String qiniuImg(Context context, Bitmap bitmap){
        //创建缓存目录，系统一运行就得创建缓存目录的，
        File cdd = new File(Environment.getExternalStorageDirectory(), "chedangdang");
        if(!cdd.exists()){
            cdd.mkdirs();
        }
        File finalImageFile = new File(cdd,"qiniu.png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(finalImageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos);
        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return Environment.getExternalStorageDirectory() + "/chedangdang/qiniu.png";
    }


}
