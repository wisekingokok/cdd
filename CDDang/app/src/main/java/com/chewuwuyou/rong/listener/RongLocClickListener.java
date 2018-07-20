package com.chewuwuyou.rong.listener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chewuwuyou.eim.activity.im.BaiduMapActivity;
import com.chewuwuyou.rong.bean.CDDLBSMsg;
import com.chewuwuyou.rong.utils.Constant;
import com.chewuwuyou.rong.view.ShowLocationMapActivity;

import java.io.Serializable;

/**
 * Created by xxy on 2016/9/9 0009.
 */
public class RongLocClickListener implements View.OnClickListener {
    private Context context;
    //    private double lat;
//    private double lon;
    private CDDLBSMsg mLocMessage;//位置信息

    public RongLocClickListener(Context context, CDDLBSMsg locMsg) {
        this.context = context.getApplicationContext();
        this.mLocMessage = locMsg;
//        this.lat = lat;
//        this.lon = lon;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ShowLocationMapActivity.class);
//        intent.putExtra("latitude", lat);
//        intent.putExtra("longitude", lon);
        Bundle bundle=new Bundle();
        bundle.putParcelable(Constant.LOC_MSG,mLocMessage);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
