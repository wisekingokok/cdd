package com.chewuwuyou.app.transition_view.activity.im_group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.fragment.EstablishGroupFirstStepFragment;
import com.chewuwuyou.app.transition_presenter.im_group.EstabGroupFirstStepPresenter;
import com.chewuwuyou.app.transition_presenter.im_group.GroupListErPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.rong.view.EstablishGroupFirstStepActivtiy;
import com.chewuwuyou.rong.view.GroupEstablishSecondActivtiy;

import butterknife.ButterKnife;
import io.rong.imlib.model.Message;

/**
 * 群列表
 * liuchun
 */

public class EstablishGroupFirstStepErActivtiy extends BaseActivity {

    private EstabGroupFirstStepPresenter mEstabGroupFirstStepPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establish_group_first_step);
        initView();
    }

    private void initView(){
        mEstabGroupFirstStepPresenter = new EstabGroupFirstStepPresenter(EstablishGroupFirstStepErActivtiy.this);
        mEstabGroupFirstStepPresenter.initDate();//接收传递的值并显示fragment
    }
}
