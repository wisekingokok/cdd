package com.chewuwuyou.rong.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.fragment.EstablishGroupFirstStepFragment;
import com.chewuwuyou.app.ui.BaseFragmentActivity;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ToastUtil;

import io.rong.imlib.model.Message;

/**
 * 创建群 第一步
 * liuchun
 */
public class EstablishGroupFirstStepActivtiy extends BaseFragmentActivity implements View.OnClickListener {


    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String mGroupId,mType,mGroupName;
    private Message mMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establish_group_first_step);
        initView();
        initData();
        initEvent();
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv://下一步
                intent = new Intent(EstablishGroupFirstStepActivtiy.this,GroupEstablishSecondActivtiy.class);
                startActivity(intent);
                break;
        }
    }

    /**
     *初始化
     */
    private void initView() {
        mFragments = new Fragment[1];
        fragmentManager = getSupportFragmentManager();
        mType = getIntent().getStringExtra("type");
        if(mType.equals("1")||mType.equals("2")){
            mGroupId = getIntent().getStringExtra("groupId");
            mGroupName = getIntent().getStringExtra("groupName");
        }else if(mType.equals(Constant.FORWARD_GROUP)){
            mMsg =  getIntent().getParcelableExtra("msg");
        }
        if (mFragments[0] == null) {
            //群id ,那个界面传递，群名称
            mFragments[0] = new EstablishGroupFirstStepFragment(mGroupId,mType,mGroupName,mMsg);
            fragmentManager.beginTransaction().add(R.id.establish_first_step, mFragments[0]).commit();
        }
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]);
        fragmentTransaction.show(mFragments[0]).commit();// 设置第几页显示

    }

    /**
     * 逻辑处理
     */
    private void initData() {

    }

    /**
     * 事件监听
     */
    private void initEvent() {


    }
}
