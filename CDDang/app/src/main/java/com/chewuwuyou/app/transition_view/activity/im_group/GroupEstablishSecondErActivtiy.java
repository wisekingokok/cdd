package com.chewuwuyou.app.transition_view.activity.im_group;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_presenter.im_group.GroupEstablishSecondErPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.rong.view.GroupEstablishSecondActivtiy;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 群创建第二步
 * liuchun
 */

public class GroupEstablishSecondErActivtiy extends BaseTitleActivity {


    @BindView(R.id.group_choice_portrait)//选择头像
    public ImageView groupChoicePortrait;
    @BindView(R.id.add_group_name)//群名称
    EditText addGroupName;
    @BindView(R.id.group_submit)//提交
    Button groupSubmit;

    private GroupEstablishSecondErPresenter mGroupEstablishSecondErPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_establish_second);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        setBarTitle("群创建");
        setRightBarTV("上一步");
        mGroupEstablishSecondErPresenter = new GroupEstablishSecondErPresenter(GroupEstablishSecondErActivtiy.this);
        mGroupEstablishSecondErPresenter.initView();
    }


    @OnClick({R.id.group_choice_portrait, R.id.group_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_choice_portrait://选择头像
                mGroupEstablishSecondErPresenter.uploadPortrait();

                break;
            case R.id.group_submit://提交信息
                if (TextUtils.isEmpty(addGroupName.getText().toString())) {
                    ToastUtil.toastShow(GroupEstablishSecondErActivtiy.this, "请输入群名称");
                } else if (addGroupName.getText().toString().length() < 2 || addGroupName.getText().toString().length() > 10) {
                    ToastUtil.toastShow(GroupEstablishSecondErActivtiy.this, "群名称为2-10个字");
                } else {
                    groupSubmit.setClickable(false);
                    mGroupEstablishSecondErPresenter.sbminGroup(addGroupName.getText().toString());
                }
                break;
        }
    }

    public void isClick(boolean isClick){
        if(isClick == false){
            groupSubmit.setClickable(false);
        }else{
            groupSubmit.setClickable(true);
        }
    }

    @Override
    protected void onTitleBarClick(View view, int title_tag) {
        switch (title_tag){
            case TITLE_TAG_RIGHT_TV:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGroupEstablishSecondErPresenter.onGroupPortraitResult(requestCode,data);
    }
}
