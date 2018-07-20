package com.barcode.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable {

	public PullableScrollView(Context context) {
		super(context);
	}

	public PullableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 用于判断滑动时上滑动还是下滑动
	@Override
	public boolean canPullDown() {
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
			return true;
		else
			return false;
	}

	private boolean mDisableEdgeEffects = true;

	// 用于接口回调
	public interface OnScrollChangedListener {
		void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
	}

	private OnScrollChangedListener mOnScrollChangedListener;

	// 我们定义的滑动接口，在布局中调用这个接口就可以得到滑动的位置，然后在这个接口里面开始写你的逻辑。
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollChangedListener != null) {
			mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		mOnScrollChangedListener = listener;
	}

	@Override
	protected float getTopFadingEdgeStrength() {
		if (mDisableEdgeEffects
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return 0.0f;
		}
		return super.getTopFadingEdgeStrength();
	}

	@Override
	protected float getBottomFadingEdgeStrength() {
		if (mDisableEdgeEffects
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return 0.0f;
		}
		return super.getBottomFadingEdgeStrength();
	}
}
