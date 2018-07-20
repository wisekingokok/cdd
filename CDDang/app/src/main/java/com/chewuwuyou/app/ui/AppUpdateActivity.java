
package com.chewuwuyou.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;

import android.os.Bundle;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @version 1.1.0
 * @describe:显示app上次更新的版本信息
 * @author:yuyong
 * @created:2015-1-19下午3:27:44
 */
public class AppUpdateActivity extends BaseActivity {
    @ViewInject(id = R.id.update_version_tv)
    private TextView mAppUpdateVersionTV;// 版本信息
    @ViewInject(id = R.id.update_info_tv)
    private TextView mAppUpdateInfoTV;// 更新信息
    private RelativeLayout mTitleHeight;//标题布局高度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_update_ac);

        initView();
    }

    private void initView() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);//根据不同手机判断
        ((TextView) findViewById(R.id.sub_header_bar_tv)).setText("版本信息");
        findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finishActivity();
            }
        });
        mAppUpdateVersionTV.setText(getIntent().getStringExtra("appVersion"));
        String updateInfo = getIntent().getStringExtra("appUpdateInfo");
        StringBuilder sb = new StringBuilder("");
        if (updateInfo.contains("@")) {
            for (int i = 0; i < updateInfo.split("@").length; i++) {
                sb.append(updateInfo.split("@")[i]).append("\n\n");
            }
        } else {
            sb.append(updateInfo);
        }
        mAppUpdateInfoTV.setText(sb.toString());
    }

}
