package com.chewuwuyou.rong.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.ui.CDDBaseActivity;

import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 添加群成员
 */
public class AddGroupMembersActivity extends CDDBaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//订单标题
    private TextView mTitel;
    @ViewInject(id = R.id.sub_header_bar_right_tv)//添加
    private TextView mSubBarRightTV;
    @ViewInject(id = R.id.group_search)//搜索
    private TextView mGroupSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_members);
        initView();
        initData();
        initEvent();

    }

    /**
     * 事件点击
     * @param v
     */
    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()){
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.group_search://搜索
                break;
            case R.id.sub_header_bar_right_tv://添加
                break;

        }
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mTitel.setText("群成员");
        mSubBarRightTV.setText("添加");
    }
    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {

    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mGroupSearch.setOnClickListener(this);
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mSubBarRightTV.setOnClickListener(this);
    }


}
