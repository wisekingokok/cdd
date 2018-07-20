package com.chewuwuyou.rong.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.fragment.GroupListFragment;
import com.chewuwuyou.app.ui.BaseFragmentActivity;

import io.rong.imlib.model.Message;

/**
 * 群列表
 * liuchun
 */
public class GroupListActivtiy extends BaseFragmentActivity {


    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String mGroupId,mType,mGroupName;

    private String group;
    private Message mMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member_refund);
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化
     */
    protected void initView() {
        mFragments = new Fragment[1];
        fragmentManager = getSupportFragmentManager();
        if(!TextUtils.isEmpty(getIntent().getStringExtra("group"))){
            group = getIntent().getStringExtra("group");
            mMessage = getIntent().getParcelableExtra("message");
        }
        if (mFragments[0] == null) {
            //群id ,那个界面传递，群名称
            mFragments[0] = new GroupListFragment(group,mMessage);
            fragmentManager.beginTransaction().add(R.id.establish_first_step, mFragments[0]).commit();
        }
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]);
        fragmentTransaction.show(mFragments[0]).commit();// 设置第几页显示



    }
    /**
     * 逻辑处理
     */
    protected void initData() {
    }

    /**
     * 事件监听
     */
    protected void initEvent() {

    }


}
