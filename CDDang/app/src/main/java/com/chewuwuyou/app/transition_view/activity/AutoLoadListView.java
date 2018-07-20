package com.chewuwuyou.app.transition_view.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


public class AutoLoadListView extends ListView implements
        AbsListView.OnScrollListener {
    private boolean isOpenLoad = true;

    private LoadingFooter mLoadingFooter;

    private OnLoadMoreListener mLoadNextListener;

    private boolean isFirstLoad = true;


    public AutoLoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AutoLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoLoadListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mLoadingFooter = new LoadingFooter(getContext());

        setOnScrollListener(this);
    }

    public void setOnLoadNextListener(OnLoadMoreListener listener) {
        mLoadNextListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if(!isOpenLoad)
            return;

        Log.e("------", "状态改变");

        if (mLoadingFooter.getState() == LoadingFooter.State.Loading || mLoadingFooter.getState() == LoadingFooter.State.TheEnd) {
            return;
        }
        boolean toBottom = view.getLastVisiblePosition() == view.getCount() - 1;
        if (toBottom && scrollState == OnScrollListener.SCROLL_STATE_IDLE && mLoadNextListener != null) {
            mLoadingFooter.setState(LoadingFooter.State.Loading);
            Log.e("------", "加载更多数据");
            if (isFirstLoad) {
                addFooterView(mLoadingFooter.getView());
                isFirstLoad = false;
            }
            mLoadNextListener.onLoadMore();
        }


    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        Log.e("---->", "滑动");

    }

    public void setState(LoadingFooter.State status) {
        mLoadingFooter.setState(status);
        mLoadingFooter.setState(status);
    }

    public void setState(LoadingFooter.State status, long delay) {
        mLoadingFooter.setState(status, delay);
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public boolean isOpenLoad() {
        return isOpenLoad;
    }

    public void setOpenLoad(boolean mIsCanLoad) {
        isOpenLoad = mIsCanLoad;
        removeFooterView(mLoadingFooter.getView());
    }

    public void addCustomFooter(View mView) {

        this.removeFooterView(mLoadingFooter.getView());
        this.addFooterView(mView);

    }

//	public void resetOriginFooterView() {
//
//		this.removeAllViews();
//		this.addFooterView(mLoadingFooter.getView());
//
//	}

}
