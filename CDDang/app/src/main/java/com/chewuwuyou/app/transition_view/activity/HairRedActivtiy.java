package com.chewuwuyou.app.transition_view.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_presenter.HairRedPewsenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HairRedActivtiy extends BaseActivity {


    public static final String ENTITY_KEY = "entity_key";

    @BindView(R.id.hair_red_img)
    public ImageView mHairRedImg;//领取人头像
    @BindView(R.id.hair_red_name)
    public TextView mHairRedName;//领取人名称
    @BindView(R.id.hair_red_title)
    TextView mHairRedTitle;//随机红包
    @BindView(R.id.hair_red_language)
    public TextView mHairRedLanguage;//祝福语
    @BindView(R.id.hair_red_luck)
    public LinearLayout mHairRedLuck;//查看大家手气
    @BindView(R.id.hair_red_kai)
    ImageView mHairRedKai;//点击开
    @BindView(R.id.hair_red_bu_delete)
    ImageButton mHairRedBuDelete;//关闭提示框


    public AnimationDrawable animationDrawable;
    private HairRedPewsenter mHairRedPewsenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);//点击空白处不消失
        setContentView(R.layout.hair_red_activtiy);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        ButterKnife.bind(this);
        mHairRedPewsenter = new HairRedPewsenter(HairRedActivtiy.this);
        mHairRedPewsenter.initView();
    }


    /**
     * 红包已过期
     */
    public void groupRed() {
        mHairRedKai.setVisibility(View.GONE);//因此开按钮
        mHairRedLanguage.setVisibility(View.GONE);//因此祝福语
        mHairRedTitle.setText(getString(R.string.red_overdue));
        mHairRedLuck.setVisibility(View.GONE);

    }

    /**
     * 红包抢完了
     */
    public void GrabRed() {
        mHairRedTitle.setText(getString(R.string.red_grab));
        mHairRedKai.setVisibility(View.GONE);//隐藏开按钮
        mHairRedLanguage.setVisibility(View.GONE);//因此祝福语
    }


    @OnClick({R.id.hair_red_kai, R.id.hair_red_luck, R.id.hair_red_bu_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hair_red_kai://获取红包
                mHairRedKai.setBackgroundDrawable(getResources().getDrawable(R.drawable.hair_red_img_kai));
                //ivOpen指的是需要播放动画的ImageView控件
                animationDrawable = (AnimationDrawable) mHairRedKai.getBackground();
                animationDrawable.start();//启动动画
                mHairRedPewsenter.robRed();
                break;
            case R.id.hair_red_luck://大家手气
                mHairRedPewsenter.everybodyLuck();
                break;
            case R.id.hair_red_bu_delete://关闭页面
                finish();
                break;
        }
    }
}
