package com.chewuwuyou.app.utils;

import android.Manifest;
import android.app.*;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.bean.CarModel;
import com.chewuwuyou.app.bean.Contacter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Serie;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.ui.LunchPageActivity;
import com.chewuwuyou.app.ui.MainActivityEr;
import com.chewuwuyou.eim.manager.ContacterManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import org.apache.http.util.EncodingUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.util.Cache;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class Tools {
    private static final double EARTH_RADIUS = 6378137;
    public static List<String> mContactsNumbers = new ArrayList<String>();
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 0;
    private static final int PHONES_DISPLAY_NAME_INDEX = 1;
    /**
     * 获取库Phone表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{Phone.NUMBER, Phone.DISPLAY_NAME};
//    public static int VERSION_UPDATE_LOG = 0;

    /**
     * 得到手机通讯录联系人信息
     **/
    public static void getPhoneContacts(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();

        // 获取手机联系人
        try {
            Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);


            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    // 得到手机号码
                    String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;
                    mContactsNumbers.add(phoneNumber.replace(" ", ""));
                }

                phoneCursor.close();
            }


        } catch (Exception e) {

            ToastUtil.toastShow(mContext, "读取联系人权限失败，请到打开手机权限");

        }

    }


    /**
     * 得到手机通讯录联系人信息 包含姓名
     **/
    public static List<Contacter> getPhoneContactsWithName(Context mContext) {

        List<Contacter> mlist = new ArrayList<>();

        ContentResolver resolver = mContext.getContentResolver();

        // 获取手机联系人
        try {
            Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);


            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    // 得到手机号码
                    String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                    String phoneName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;
                    String num = phoneNumber.replaceAll(" ", "");
                    mlist.add(new Contacter(phoneName, num));
                }

                phoneCursor.close();
            }


        } catch (Exception e) {

            ToastUtil.toastShow(mContext, "读取联系人权限失败，请到打开手机权限");

        }

        return mlist;

    }

    /**
     * 得到手机SIM卡联系人人信息
     **/
    public static void getSIMContacts(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                mContactsNumbers.add(phoneNumber);
            }

            phoneCursor.close();
        }
    }

    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    public static boolean hasSdcard() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 讲bitmap转换为inputStream
     *
     * @param bm
     * @return
     */
    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * 判断是否是模拟器
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(final Context context) {
        final boolean[] isEmu = {false};
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.READ_PHONE_STATE).build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        TelephonyManager tm = (TelephonyManager)
                                context.getSystemService(Context.TELEPHONY_SERVICE);
                        String imei = tm.getDeviceId();
                        if (imei != null && imei.equals("000000000000000")) {
                            isEmu[0] = true;
                        }

                        isEmu[0] = (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastUtil.toastShow(context, permissions.toString() + "权限拒绝");
                    }
                });
        return isEmu[0];
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setListViewHeightBasedOnChildren(ExpandableListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static String getVehicleImage(Context context, String brand, String model) {
        // 车型对象集合
        List<Serie> series;
        // 品牌集合
        List<CarModel> carModels;
        // 品牌对象
        CarModel carModel;
        // 车型对象
        Serie serie;
        // 获取车辆品牌车型对象
        carModels = CarModel.parseBrands(getFromAssets(context, "mobile_models"));
        // 定义图片url
        String carImageUrl;
        for (int i = 0, size = carModels.size(); i < size; i++) {
            if (carModels.get(i).getBrand().equals(brand)) {
                carModel = carModels.get(i);
                series = carModel.getSeries();
                for (int j = 0, len = series.size(); j < len; j++) {
                    if (series.get(j).getName().equals(model)) {
                        // 得到车型的图片地址
                        serie = series.get(j);
                        carImageUrl = NetworkUtil.IMAGE_BASE_URL + serie.getIcon();
                        return carImageUrl;
                    }
                }
            }
        }
        return null;
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

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * 获取字符串
     *
     * @param flag
     * @param targetStr
     * @return
     */
    public static String getDigit(String flag, String targetStr) {
        // targetStr:shangjia:12345 flag:BD:
        int index = targetStr.indexOf(flag) + flag.length();
        if (index < 0) {
            return null;
        }
        int index2 = 0;
        for (int i = index; i < targetStr.length(); i++) {
            MyLog.i("YUY", "=====sub==" + targetStr.substring(index, index + 1));
            if (targetStr.substring(i, i + 1).equals(";")) {
                index2 = i;
                break;
            }
        }
        MyLog.i("YUY", "====index1==" + index + "===index2=" + index2);
        return targetStr.substring(index, index2);
    }

    /**
     * * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /***
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /*
     * 根据两点间经纬度坐标（double值），计算两点间距离，
     *
     * @param lat1
     *
     * @param lng1
     *
     * @param lat2
     *
     * @param lng2
     *
     * @return 距离：单位为米
     */
    public static double DistanceOfTwoPoints(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s / 1000;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * list去掉重复数据
     *
     * @param list
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<String> removeDuplicate(List<String> list)

    {
        Set set = new LinkedHashSet<String>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static String getStarSeat(int mouth, int day) {
        String starSeat = null;

        if ((mouth == 3 && day >= 21) || (mouth == 4 && day <= 19)) {
            starSeat = "白羊座";
        } else if ((mouth == 4 && day >= 20) || (mouth == 5 && day <= 20)) {
            starSeat = "金牛座";
        } else if ((mouth == 5 && day >= 21) || (mouth == 6 && day <= 21)) {
            starSeat = "双子座";
        } else if ((mouth == 6 && day >= 22) || (mouth == 7 && day <= 22)) {
            starSeat = "巨蟹座";
        } else if ((mouth == 7 && day >= 23) || (mouth == 8 && day <= 22)) {
            starSeat = "狮子座";
        } else if ((mouth == 8 && day >= 23) || (mouth == 9 && day <= 22)) {
            starSeat = "处女座";
        } else if ((mouth == 9 && day >= 23) || (mouth == 10 && day <= 23)) {
            starSeat = "天秤座";
        } else if ((mouth == 10 && day >= 24) || (mouth == 11 && day <= 22)) {
            starSeat = "天蝎座";
        } else if ((mouth == 11 && day >= 23) || (mouth == 12 && day <= 21)) {
            starSeat = "射手座";
        } else if ((mouth == 12 && day >= 22) || (mouth == 1 && day <= 19)) {
            starSeat = "摩羯座";
        } else if ((mouth == 1 && day >= 20) || (mouth == 2 && day <= 18)) {
            starSeat = "水瓶座";
        } else {
            starSeat = "双鱼座";
        }
        return starSeat;
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    @SuppressWarnings("deprecation")
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    public static void inputPrice(final EditText et) {
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                MyLog.i("YUY", "onTextChanged  =  " + arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                MyLog.i("YUY", "beforeTextChanged  =  " + arg0);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                MyLog.i("YUY", "afterTextChanged  =  " + arg0.toString());
                if (arg0.toString().contains(".") && arg0.toString().length() < 4) {
                    et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(arg0.length() + 2)});
                } else {
                    et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                }

            }
        });
    }

    /**
     * 清除缓存相关所有信息
     *
     * @param context
     */
    public static void clearInfo(Context context) {
        CacheTools.clearAllUserData();
        CacheTools.clearAllObject();
        CacheTools.clearObject(CacheTools.getUserData("telephone"));
        NetworkUtil.removeCookie(context);
        NetworkUtil.clearCookie();
        JPushInterface.setAlias(context.getApplicationContext(), null, null);//设置别名为空
        JPushInterface.setTags(context.getApplicationContext(), null, null);//设置标签为空
        JPushInterface.setAliasAndTags(context.getApplicationContext(), null, null);
        JPushInterface.stopPush(context.getApplicationContext());// 注销推送
        MyLog.e("YUY", "推送是否停止 = " + JPushInterface.isPushStopped(context));
        JPushInterface.clearAllNotifications(context.getApplicationContext());
        JPushInterface.clearLocalNotifications(context.getApplicationContext());
        AppManager.getAppManager().finishAllActivity();
        CacheTools.setUserData("isNotification", Constant.JPUSH_STATUS.JPUSH_CLOSE);
        Constant.IS_SET_PAYPASS = 0;// 设置密码标识为0
    }

    /**
     * 获取当前类名
     *
     * @param context
     * @return
     */
    public static String getNowClass(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo cinfo = runningTasks.get(0);
        ComponentName component = cinfo.topActivity;
        return component.getClassName();
    }

    /**
     * 获取Activity配置的lable
     *
     * @param context
     * @return
     */
//    public static String getLable(Context context) {
//        /**
//         * 测试取lable标签
//         */
//        PackageManager pm = context.getPackageManager();
//        try {
//            ActivityInfo activityInfo = pm.getActivityInfo(getComponentName(), 0);
//            Log.d("ActivityLabel", "acitivyt layble ----------" + activityInfo.loadLabel(pm).toString());
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    // 音频获取源
    public static int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public static int sampleRateInHz = 44100;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    public static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    public static int bufferSizeInBytes = 0;

    /**
     * 判断是是否有录音权限
     */
    public static boolean isHasPermission(final Context context) {
        bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);
        //开始录制音频
        try {
            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        /**
         * 根据开始录音判断是否有录音权限
         */
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            new AlertDialog(context).builder().setTitle("温馨提示").setMsg("权限未开启，请前去设置").setNegativeButton("去设置", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
                }
            }).setPositiveButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return false;
        }
        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;

        return true;
    }

    /**
     * 获取手机唯一标识
     *
     * @return
     */
    public static String getPhoneIM(final Context context) {
        final String[] deviceId = {null};
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.READ_PHONE_STATE).build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
                        deviceId[0] = TelephonyMgr.getDeviceId();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastUtil.toastShow(context, permissions.toString() + "权限拒绝");
                    }
                });
        return deviceId[0];

    }

    /**
     * 获取userAgent
     * @param context
     * @return
     */
    public static String getPhoneUA(Context context) {
        if (CacheTools.getUserData("UA") != null) {
            return CacheTools.getUserData("UA");
        }
        WebView webview = new WebView(context);
        WebSettings settings = webview.getSettings();
        String ua = settings.getUserAgentString();
        CacheTools.setUserData("UA", ua);
        return ua;
    }
}
