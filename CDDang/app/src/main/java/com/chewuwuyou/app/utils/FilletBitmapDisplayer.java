package com.chewuwuyou.app.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

/**
 * Created by IORI Hua on 2015/7/30.
 */
public class FilletBitmapDisplayer implements BitmapDisplayer {

	protected final int margin;

	public FilletBitmapDisplayer() {
		this(0);
	}

	public FilletBitmapDisplayer(int margin) {
		this.margin = margin;
	}

	@Override
	public Bitmap display(Bitmap arg0, ImageView arg1, LoadedFrom arg2) {
		if (!(arg1 instanceof ImageView)) {
			throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
		}

		arg1.setImageDrawable(new FilletDrawable(arg0, margin));
		return arg0;
	}

//	@Override
//	public Bitmap display(Bitmap bitmap, ImageView imageAware, LoadedFrom loadedFrom) {
//		if (!(imageAware instanceof ImageView)) {
//			throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
//		}
//
//		imageAware.setImageDrawable(new FilletDrawable(bitmap, margin));
//	}

}
