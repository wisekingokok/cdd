package com.chewuwuyou.app.adapter;

import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.uk.co.senab.photoview.PhotoView;
import com.uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @describe:帖子Adapter
 * @author:XH
 * @version
 * @created:
 */
public class SNSAdapter extends BaseAdapter implements OnClickListener {

	protected Activity mContext;
	private Animator mCurrentAnimator;
	private int mShortAnimationDuration = 300;
	protected LayoutInflater mInflater;
	protected ImageLoader mImageLoader;
	protected  HackyViewPager mViewPager;
	protected View mContainer;
	protected static final DisplayMetrics mOutMetrics = new DisplayMetrics();
	protected int mTuWidth = 0;
	protected int mTuHeight = 0;
	
	protected int mBgTuWidth = 0;
	protected int mBgTuHeight = 0;
	private AnimatorSet mAs;
	

	public SNSAdapter(Activity context) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		this.mImageLoader = ImageLoader.getInstance();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(mOutMetrics);
		mTuWidth = (mOutMetrics.widthPixels - 6 * mContext.getResources().getDimensionPixelSize(R.dimen.quan_padding) - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.quan_tu_interval))/3;
		mTuHeight = mTuWidth;
		
		mBgTuWidth = mOutMetrics.widthPixels;
		mBgTuHeight = (mOutMetrics.widthPixels * 2)/3;
	}
	public SNSAdapter(Activity context, HackyViewPager viewPager, View container) {

		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		this.mImageLoader = ImageLoader.getInstance();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(mOutMetrics);
		mTuWidth = (mOutMetrics.widthPixels - 3 * mContext.getResources().getDimensionPixelSize(R.dimen.quan_padding) - mContext.getResources().getDimensionPixelSize(R.dimen.quan_avatar_width) - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.quan_tu_interval))/3;
		mTuHeight = mTuWidth;
		
		mBgTuWidth = mOutMetrics.widthPixels;
		mBgTuHeight = (mOutMetrics.widthPixels * 2)/3;
		
		this.mViewPager = viewPager;
		this.mContainer = container;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	float startScale;
	Rect startBounds;
	float startScaleFinal;

	protected void zoomImageFromThumb(View thumbView, List<String> tuUrls, ViewGroup tusViewGroup, int position) {
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		mViewPager.setAdapter(new SamplePagerAdapter(tuUrls, tusViewGroup));
		mViewPager.setCurrentItem(position);
		startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();
		thumbView.getGlobalVisibleRect(startBounds);

		mContainer.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}
		mViewPager.setVisibility(View.VISIBLE);
		AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(1);
		animSet.play(ObjectAnimator.ofFloat(mViewPager, "pivotX", 0f)).with(ObjectAnimator.ofFloat(mViewPager, "pivotY", 0f))
				.with(ObjectAnimator.ofFloat(mViewPager, "alpha", 1.0f));
		animSet.start();
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(tusViewGroup, "alpha", 1.0f, 0.f);
		ObjectAnimator animatorX = ObjectAnimator.ofFloat(mViewPager, "x", startBounds.left, finalBounds.left);
		ObjectAnimator animatorY = ObjectAnimator.ofFloat(mViewPager, "y", startBounds.top, finalBounds.top);
		ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(mViewPager, "scaleX", startScale, 1f);
		ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(mViewPager, "scaleY", startScale, 1f);

		set.play(alphaAnimator).with(animatorX).with(animatorY).with(animatorScaleX).with(animatorScaleY);
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {

			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;
		startScaleFinal = startScale;
	}

	public boolean getScaleFinalBounds(ViewGroup tusViewGroup, int position) {
		View childView = tusViewGroup.getChildAt(position);

		startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		try {
			childView.getGlobalVisibleRect(startBounds);
		} catch (Exception e) {
			return false;
		}
		mContainer.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}
		startScaleFinal = startScale;
		return true;
	}

	class SamplePagerAdapter extends PagerAdapter {

		private List<String> mTuUrls;
		private ViewGroup mTusViewGroup;

		public SamplePagerAdapter(List<String> data, ViewGroup tusViewGroup) {
			this.mTuUrls = data;
			this.mTusViewGroup = tusViewGroup;
		}

		@Override
		public int getCount() {
			return mTuUrls.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			final PhotoView photoView = new PhotoView(container.getContext());
			String url = mTuUrls.get(position);
			mImageLoader.displayImage(ServerUtils.getServerIP(url), photoView, new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
					.showImageOnFail(R.drawable.image_load_fail).showImageForEmptyUri(R.drawable.image_load_fail).build());
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
				public void onPhotoTap(View paramAnonymousView, float paramAnonymousFloat1, float paramAnonymousFloat2) {
					if (mCurrentAnimator != null) {
						mCurrentAnimator.cancel();
					}
					photoView.clearZoom();

					boolean scaleResult = getScaleFinalBounds(mTusViewGroup, position);
				
					mAs = new AnimatorSet();
					ObjectAnimator containAlphaAnimator = ObjectAnimator.ofFloat(mTusViewGroup, "alpha", 0.f, 1.0f);
					if (scaleResult) {
						ObjectAnimator animatorX = ObjectAnimator.ofFloat(mViewPager, "x", startBounds.left);
						ObjectAnimator animatorY = ObjectAnimator.ofFloat(mViewPager, "y", startBounds.top);
						ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(mViewPager, "scaleX", startScaleFinal);
						ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(mViewPager, "scaleY", startScaleFinal);

						mAs.play(containAlphaAnimator).with(animatorX).with(animatorY).with(animatorScaleX).with(animatorScaleY);
					} else {
						ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mViewPager, "alpha", 0.1f);
						mAs.play(alphaAnimator).with(containAlphaAnimator);
					}
					mAs.setDuration(mShortAnimationDuration);
					
					mAs.setInterpolator(new DecelerateInterpolator());
					mAs.addListener(new AnimatorListenerAdapter() {

						@Override
						public void onAnimationEnd(Animator animation) {
							
							mViewPager.clearAnimation();
							mViewPager.setVisibility(View.GONE);
							mCurrentAnimator = null;
						}

						@Override
						public void onAnimationCancel(Animator animation) {
							mViewPager.clearAnimation();
							mViewPager.setVisibility(View.GONE);
							mCurrentAnimator = null;
						}
					});
					mAs.start();
					mCurrentAnimator = mAs;
				}
			});

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
}
