package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.fragment.EvaluationListFragment;

/**
 * 评价列表界面
 *
 * @author Administrator
 */
public class EvaluateListActivity extends BaseFragmentActivity implements
        OnClickListener, FragmentCallBack {
    public EvaluationListFragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    /**
     * 评论group
     */
    private RadioGroup mPlGroup;
    private ImageButton mBackIBtn;
    /**
     * 好评、中评、差评
     */
    private RadioButton mHpButton, mZpButton, mCpButton;
    private TextView mTitleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_list);
        initView();
        initData();
        initEvent();

    }

    protected void initView() {
        mPlGroup = (RadioGroup) findViewById(R.id.pl_group);
        mHpButton = (RadioButton) findViewById(R.id.hp_btn);
        mZpButton = (RadioButton) findViewById(R.id.zp_btn);
        mCpButton = (RadioButton) findViewById(R.id.cp_btn);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);

        mFragments = new EvaluationListFragment[3];
        fragmentManager = getSupportFragmentManager();

        mFragments[0] = new EvaluationListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "1");

        mFragments[0].setArguments(bundle);

        mFragments[1] = new EvaluationListFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("type", "2");
        mFragments[1].setArguments(bundle1);

        // mFragments[2] = (EvaluationListFragment)
        // fragmentManager.findFragmentById(R.id.fragment_poor);
        mFragments[2] = new EvaluationListFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("type", "3");
        mFragments[2].setArguments(bundle2);

        fragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[0])
                .commit();

        fragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[1])
                .commit();

        fragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[2])
                .commit();
        mFragments[0].setFragmentCallBack(this);
        mFragments[1].setFragmentCallBack(this);
        mFragments[2].setFragmentCallBack(this);
        onclickSwitch(0);// 设置初始显示第一个页面
        // tab事件，切换页面
        mPlGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.hp_btn:
                        onclickSwitch(0);
                        break;
                    case R.id.zp_btn:
                        onclickSwitch(1);
                        break;
                    case R.id.cp_btn:
                        onclickSwitch(2);
                        break;
                }
            }
        });
    }

    protected void initData() {
        mTitleTV.setText("客户评价");
    }

    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
    }

    /**
     * 切换
     *
     * @param id
     */
    private void onclickSwitch(int id) {
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
        mPlGroup.setFocusable(true);
        mPlGroup.setFocusableInTouchMode(true);
        mPlGroup.requestFocus();
        switch (id) {
            case 0:
                fragmentTransaction.show(mFragments[0]).commit();// 服务
                break;
            case 1:
                fragmentTransaction.show(mFragments[1]).commit();// 消息
                break;
            case 2:
                fragmentTransaction.show(mFragments[2]).commit();// 车圈
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
        }
    }

    @Override
    public void callback(int pager, Object obj) {
        if (obj == null)
            return;
        Bundle bundle = (Bundle) obj;
        mHpButton
                .setText(getString(R.string.good_comment, bundle.getInt("hao")));
        mZpButton.setText(getString(R.string.average_comment,
                bundle.getInt("zhong")));
        mCpButton.setText(getString(R.string.negative_comment,
                bundle.getInt("cha")));
    }

}
