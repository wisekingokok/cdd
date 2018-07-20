package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.SysNotificationAdapter;
import com.chewuwuyou.app.bean.SystemNotification;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;

/**
 * @version 1.1.0
 * @describe:系统提醒
 * @author:yuyong
 * @created:2015-1-14下午1:36:34
 */
public class SystemRemindActivity extends BaseActivity {

    // private List<Map<String, String>> returnList = new ArrayList<Map<String,
    // String>>();
    private SystemNotification mSNotification;
    private List<SystemNotification> mSNotifications;// 系统通知集合
    @ViewInject(id = R.id.system_remind_list)
    private ListView mSysListView;
    private RelativeLayout mTitleHeight;// 标题布局高度
    @ViewInject(id = R.id.remind_null_rl)
    private RelativeLayout mRemindNullRL;//为空显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_remind_ac);
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        ((TextView) findViewById(R.id.sub_header_bar_tv)).setText("系统提醒");
        findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finishActivity();
                    }
                });
        MyLog.i("YUY", "======系统提醒详情==="
                + getIntent().getStringExtra("content"));
        try {
            initView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() throws JSONException {
        mSNotifications = new ArrayList<SystemNotification>();
        if(TextUtils.isEmpty(getIntent().getStringExtra("content"))){
            mRemindNullRL.setVisibility(View.VISIBLE);
            return;
        }
        if (getIntent().getStringExtra("content") != null
                && !"".equals(getIntent().getStringExtra("content"))) {
            JSONObject theJsonObject = new JSONObject(getIntent()
                    .getStringExtra("content"));
            Iterator<String> keyIter = theJsonObject.keys();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                if (key.equals("cars")) {
                    cars(theJsonObject.getJSONObject("cars"));
                }
                if (key.equals("drivers")) {
                    drivers(theJsonObject.getJSONObject("drivers"));
                }
            }
            SysNotificationAdapter adapter = new SysNotificationAdapter(
                    SystemRemindActivity.this, mSNotifications);
            mSysListView.setAdapter(adapter);
            mSysListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    if (mSNotifications.get(arg2).getServiceType()
                            .equals("车辆违章提醒")) {
                        skipToService(Constant.SERVICE_TYPE.ILLEGAL_SERVICE);
                    } else if (mSNotifications.get(arg2).getServiceType()
                            .equals("车辆年审提醒")) {
                        skipToService(Constant.SERVICE_TYPE.CAR_SERVICE);
                    } else if (mSNotifications.get(arg2).getServiceType()
                            .equals("驾证年审提醒")) {
                        skipToService(Constant.SERVICE_TYPE.LICENCE_SERVICE);
                    } else {
                        // TODO 有可能三种类型都不是
                        skipToService(Constant.SERVICE_TYPE.ILLEGAL_SERVICE);
                    }
                }

            });
        }

    }

    private void cars(JSONObject jo) throws JSONException {
        Iterator<String> keyIter = jo.keys();
        while (keyIter.hasNext()) {
            // 车牌号
            String key = keyIter.next();
            car(key, jo.getJSONObject(key));

        }
    }

    private void car(String chepai, JSONObject jsonObject) throws JSONException {
        Iterator<String> keyIter = jsonObject.keys();
        while (keyIter.hasNext()) {
            // 类型
            String key = keyIter.next();
            if (key.equals("illegal")) {
                illegal(chepai, jsonObject.getJSONArray("illegal"));

            }
            if (key.equals("carCheck")) {
                carCheck(chepai, jsonObject.getString("carCheck"));
            }
        }
    }

    private void carCheck(String jsonObject, String string) {
        // 车辆年审
        // resultMap.put("che", "...")
        if (!TextUtils.isEmpty(string)) {
            mSNotification = new SystemNotification();
            mSNotification.setServiceType("车辆年审提醒");
            mSNotification.setTitle(jsonObject);
            mSNotification.setContent("年审时间:" + string.substring(0, 9));
            mSNotifications.add(mSNotification);
        }

    }

    private void illegal(String chepai, JSONArray jsonObject) {
        // 违章处理
        // resultMap.put("che","wei", "2")
        if (jsonObject.length() > 0) {
            mSNotification = new SystemNotification();
            mSNotification.setServiceType("车辆违章提醒");
            mSNotification.setTitle(chepai);
            mSNotification.setContent("您有" + jsonObject.length() + "违章记录");
            mSNotifications.add(mSNotification);
        }
    }

    private void drivers(JSONObject jo) throws JSONException {
        Iterator<String> keyIter = jo.keys();
        while (keyIter.hasNext()) {
            // 类型
            String key = keyIter.next();
            drivers(key, jo.getJSONObject(key));

        }
    }

    private void drivers(String key, JSONObject object) throws JSONException {

        if (!TextUtils.isEmpty(object.getString("driverCheck"))) {
            // 驾证年审
            mSNotification = new SystemNotification();
            mSNotification.setTitle(key);
            mSNotification.setServiceType("驾证年审提醒");
            mSNotification.setContent("年审时间:"
                    + object.getString("driverCheck").substring(0, 9));
            mSNotifications.add(mSNotification);
        }

        if (!TextUtils.isEmpty(object.getString("driverCore"))) {
            mSNotification = new SystemNotification();
            mSNotification.setTitle(key);
            mSNotification.setServiceType("驾证扣分提醒");
            mSNotification.setContent("您的驾证有扣" + object.getString("driverCore")
                    + "分的记录");
            mSNotifications.add(mSNotification);

        }

    }

    /**
     * 通过提醒类型进行判断办理服务类型
     *
     * @param serviceType
     */
    private void skipToService(int serviceType) {
        Intent intent;
        if (CacheTools.getUserData("role") != null) {
            intent = new Intent(SystemRemindActivity.this,
                    ServiceTypeActivity.class);
            intent.putExtra("serviceType", serviceType);
            startActivity(intent);
        } else {
            intent = new Intent(SystemRemindActivity.this, LoginActivity.class);
            startActivity(intent);
            finishActivity();
        }

    }

}
