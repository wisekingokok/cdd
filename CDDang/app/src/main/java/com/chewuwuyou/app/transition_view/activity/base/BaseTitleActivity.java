package com.chewuwuyou.app.transition_view.activity.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.widget.XSwipeRefreshLayout;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by xxy on 2016/7/21.
 */
public abstract class BaseTitleActivity extends BaseActivity {
    /**
     * 左边图片按钮
     **/
    protected static final int TITLE_TAG_LEFT_BTN = 0x12;
    /**
     * 左边文字按钮
     **/
    protected static final int TITLE_TAG_LEFT_TV = 0x13;
    /**
     * 右边图片按钮
     **/
    protected static final int TITLE_TAG_RIGHT_BTN = 0x14;
    /**
     * 右边文字按钮
     **/
    protected static final int TITLE_TAG_RIGHT_TV = 0x15;

    private ImageButton leftBarBtn;
    private TextView leftBarTV;
    private TextView barTitle;
    private TextView rightBarTV;
    private ImageButton rightBarBtn;
    private View toolbar;
    private XSwipeRefreshLayout rootView;
    private TextView errorText;

    private static final Long DEFAULT_TIP_TIME = 4000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        setBarColor(R.color.new_blue);
        initBar();
    }

    private void initBar() {
        leftBarBtn = (ImageButton) findViewById(R.id.leftBarBtn);
        leftBarTV = (TextView) findViewById(R.id.leftBarTV);
        rightBarTV = (TextView) findViewById(R.id.rightBarTV);
        rightBarBtn = (ImageButton) findViewById(R.id.rightBarBtn);

        toolbar = findViewById(R.id.toolbar);
        barTitle = (TextView) findViewById(R.id.barTitle);
        errorText = (TextView) findViewById(R.id.errorText);

        leftBarBtn.setOnClickListener(barClick);
        leftBarTV.setOnClickListener(barClick);
        rightBarTV.setOnClickListener(barClick);
        rightBarBtn.setOnClickListener(barClick);
    }

    private View.OnClickListener barClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.leftBarBtn:
                    onTitleBarClick(view, TITLE_TAG_LEFT_BTN);
                    break;
                case R.id.leftBarTV:
                    onTitleBarClick(view, TITLE_TAG_LEFT_TV);
                    break;
                case R.id.rightBarTV:
                    onTitleBarClick(view, TITLE_TAG_RIGHT_TV);
                    break;
                case R.id.rightBarBtn:
                    onTitleBarClick(view, TITLE_TAG_RIGHT_BTN);
                    break;
            }
        }
    };

    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        if (rootView == null)
            rootView = (XSwipeRefreshLayout) findViewById(R.id.root_layout);
        rootView.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setRefreshingEnabled(false);
    }

    /**
     * 为刷新控件设置刷新监听
     *
     * @param onRefreshListener
     */
    protected void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        if (rootView == null)
            throw new RuntimeException("rootView is null");
        rootView.setOnRefreshListener(onRefreshListener);
    }

    /**
     * 手动调用刷新
     *
     * @param showView     是否显示View
     * @param isRefreshing 是否调用onRefresh()方法,当showView=true生效,反之无意义
     */
    protected void setRefreshing(boolean showView, boolean isRefreshing) {
        rootView.setRefreshing(showView, isRefreshing);
    }

    /**
     * 设置是否启用刷新(默认是设置不启用刷新的)
     *
     * @param enabled
     */
    protected void setRefreshingEnabled(boolean enabled) {
        if (rootView == null)
            throw new RuntimeException("rootView is null");
        rootView.setEnabled(enabled);
    }

    public ImageButton getLeftBarBtn() {
        return leftBarBtn;
    }

    public TextView getLeftBarTV() {
        return leftBarTV;
    }

    public ImageButton getRightBarBtn() {
        return rightBarBtn;
    }

    public TextView getRightBarTV() {
        return rightBarTV;
    }

    /**
     * 设置Title
     *
     * @param resId
     */
    protected void setBarTitle(int resId) {
        setBarTitle(getString(resId));
    }

    /**
     * 设置Title
     *
     * @param str
     */
    protected void setBarTitle(String str) {
        barTitle.setText(str);
    }

    /**
     * 设置左边按钮图片
     *
     * @param resId
     */
    protected void setLeftBarBtnImage(int resId) {
        leftBarBtn.setVisibility(View.VISIBLE);
        leftBarBtn.setImageResource(resId);
    }

    /**
     * 设置右边按钮图片
     *
     * @param resId
     */
    protected void setRightBarBtnImage(int resId) {
        rightBarBtn.setVisibility(View.VISIBLE);
        rightBarBtn.setImageResource(resId);
    }

    /**
     * 设置左边文字按钮文字
     *
     * @param resId
     */
    protected void setLeftBarTV(int resId) {
        setLeftBarTV(getString(resId));
    }

    /**
     * 设置左边文字按钮文字
     *
     * @param str
     */
    protected void setLeftBarTV(String str) {
        leftBarTV.setVisibility(View.VISIBLE);
        leftBarTV.setText(str);
    }

    /**
     * 设置右边文字按钮文字
     *
     * @param resId
     */
    protected void setRightBarTV(int resId) {
        setRightBarTV(getString(resId));
    }

    /**
     * 设置右边文字按钮文字
     *
     * @param str
     */
    protected void setRightBarTV(String str) {
        rightBarTV.setVisibility(View.VISIBLE);
        rightBarTV.setText(str);
    }

    /**
     * 显示提示信息（默认4秒关闭）
     *
     * @param tip
     */
    public void showTip(String tip) {
        showTip(tip, DEFAULT_TIP_TIME);
    }

    /**
     * 显示提示信息（time=0，不关闭）
     *
     * @param tip
     * @param time 单位=毫秒
     */
    public void showTip(String tip, Long time) {
        errorText.setText(tip);
        errorText.setVisibility(View.VISIBLE);
        if (time == 0) return;
        Observable.timer(time, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        cancelTip();
                    }
                });
    }


    /**
     * 关闭提示信息
     */
    protected void cancelTip() {
        errorText.setVisibility(View.GONE);
    }


    /**
     * title按钮点击事件
     *
     * @param view
     * @param title_tag TITLE_TAG_LEFT_BTN 左边按钮,
     *                  TITLE_TAG_LEFT_TV 左边文字按钮,
     *                  TITLE_TAG_RIGHT_BTN ;//右边按钮,
     *                  TITLE_TAG_RIGHT_TV ;//右边文字按钮
     */
    protected void onTitleBarClick(View view, int title_tag) {

    }
}
