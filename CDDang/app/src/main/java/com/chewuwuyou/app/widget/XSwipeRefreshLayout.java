package com.chewuwuyou.app.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.chewuwuyou.app.R;


/**
 * Created by xxy .
 */
public class XSwipeRefreshLayout extends SwipeRefreshLayout {
    private float startY;
    private float startX;
    // 记录viewPager是否拖拽的标记
    private boolean mIsVpDragger;
    private OnRefreshListener listener;

    public XSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //改变加载显示的颜色
        setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        //设置初始时的大小
//        setSize(SwipeRefreshLayout.LARGE);
        //设置向下拉多少出现刷新
        setDistanceToTriggerSync(100);
        //设置刷新出现的位置
        setProgressViewEndTarget(false, 150);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
        super.setOnRefreshListener(listener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();// 记录手指按下的位置
                startX = ev.getX();
                mIsVpDragger = false; // 初始化标记
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsVpDragger) return false;
                if (Math.abs(ev.getX() - startX) - Math.abs(ev.getY() - startY) > 10) {// 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                    mIsVpDragger = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsVpDragger = false;// 初始化标记
                break;
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 手动调用刷新
     *
     * @param showView     是否显示View
     * @param isRefreshing 是否调用onRefresh()方法,当showView=true生效,反之无意义
     */
    public void setRefreshing(final boolean showView, boolean isRefreshing) {
        post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(showView);
            }
        });
        if (showView && isRefreshing)
            callOnRefreshing();
    }

    /**
     * 直接调用刷新方法，而不显示Bar
     */
    public void callOnRefreshing() {
        listener.onRefresh();
    }
}
