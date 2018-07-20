package com.barcode.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.chewuwuyou.app.R;

public class SweepLayout extends LinearLayout {
	
	private Scroller mScroller;
	private int mHolderWidth = 100;
	
	public SweepLayout(Context context) {
		super(context);
	
		init();
	}

	public SweepLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.sweeplayout);
		mHolderWidth = (int)a.getDimension(R.styleable.sweeplayout_holderwidth, 100);
		a.recycle();
		init();
	}
	
	public void abortAnimation(){
		mScroller.abortAnimation();
	}
	
	public boolean getScrollState(){
		return this.getScrollX() > 0 ? true : false;
	}

	private void init(){
		mScroller = new Scroller(getContext());
	}
	
	public int getHolderWidth(){
		return this.mHolderWidth;
	}
	
	public void shrik(int duration){
		if(this.getScrollX() != 0){
			mScroller.startScroll(this.getScrollX(), 0, 0 - this.getScrollX(), 0, duration);
			invalidate();
		}
	}
	
	public void showSlide(int duration){
		
		mScroller.startScroll(this.getScrollX(), 0, mHolderWidth - this.getScrollX(), 0, duration);
		invalidate();
	}
	
	@Override
	public void computeScroll(){
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), 0);
			postInvalidate();
		}
	}
}
