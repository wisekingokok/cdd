package com.chewuwuyou.app.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.chewuwuyou.app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

/**
 * Created by xxy on 2016/8/19 0019.
 */
public class ImageLoaderBuilder {
    public final static String HEAD_FILE = "file://";
    public final static String HEAD_CONTENT = "content://";
    public final static String HEAD_ASSETS = "assets://";
    public final static String HEAD_DRAWABLE = "drawable://";
    private String url = "";
    private int errorImage = R.color.color_d6d6d6;
    private int loadingImage = R.color.color_d6d6d6;
    private int nullImage = R.color.color_d6d6d6;
    private BitmapDisplayer displayer;
    private int cornetRadiusPixels = -1;
    private DisplayImageOptions options;
    private boolean usingDefaultImage = true;

    public static ImageLoaderBuilder Builder() {
        return new ImageLoaderBuilder();
    }

    public ImageLoaderBuilder() {
    }

    /**
     * 加载网络图片
     *
     * @param url
     * @return
     */
    public ImageLoaderBuilder loadFromHttp(String url) {
        this.url = url;
        return this;
    }

    /**
     * 加载SD卡图片
     *
     * @param path
     * @return
     */
    public ImageLoaderBuilder loadFromSDCard(String path) {
        this.url = HEAD_FILE + path;
        return this;
    }

    /**
     * Content图片
     *
     * @param path
     * @return
     */
    public ImageLoaderBuilder loadFromContent(String path) {
        this.url = HEAD_CONTENT + path;
        return this;
    }

    /**
     * assets图片
     *
     * @param path
     * @return
     */
    public ImageLoaderBuilder loadFromAssets(String path) {
        this.url = HEAD_ASSETS + path;
        return this;
    }

    /**
     * Drawable图片
     *
     * @param resId
     * @return
     */
    public ImageLoaderBuilder loadFromDrawable(int resId) {
        this.url = HEAD_DRAWABLE + resId;
        return this;
    }

    /**
     * 设置Options
     *
     * @param options
     * @return
     */
    public ImageLoaderBuilder setOptions(DisplayImageOptions options) {
        this.options = options;
        return this;
    }

    /**
     * 设置加载中图片
     *
     * @param resId
     * @return
     */
    public ImageLoaderBuilder showImageOnLoading(int resId) {
        loadingImage = resId;
        return this;
    }

    /**
     * 加载失败图片
     *
     * @param resId
     * @return
     */
    public ImageLoaderBuilder showImageOnFail(int resId) {
        errorImage = resId;
        return this;
    }

    /**
     * Uri为Empty图片
     *
     * @param resId
     * @return
     */
    public ImageLoaderBuilder showImageForEmptyUri(int resId) {
        nullImage = resId;
        return this;
    }

    /**
     * 设置加载器
     *
     * @param displayer
     * @return
     */
    public ImageLoaderBuilder setDisplayer(BitmapDisplayer displayer) {
        this.displayer = displayer;
        return this;
    }

    /**
     * 设置是否启用默认的加载中、加载失败、uri空图片
     *
     * @param usingDefaultImage
     * @return
     */
    public ImageLoaderBuilder usingDefaultImage(boolean usingDefaultImage) {
        this.usingDefaultImage = usingDefaultImage;
        return this;
    }

    /**
     * 设置圆角
     *
     * @param cornetRadiusPixels
     * @return
     */
    public ImageLoaderBuilder roundedImage(int cornetRadiusPixels) {
        this.cornetRadiusPixels = cornetRadiusPixels;
        return this;
    }

    private DisplayImageOptions buildOptions() {
        if (options != null)
            return options;
        DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
        if (usingDefaultImage)
            optionsBuilder.showImageOnFail(errorImage)
                    .showImageForEmptyUri(nullImage)
                    .showStubImage(loadingImage);
        optionsBuilder.cacheInMemory(true)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565);
        if (displayer != null) {
            optionsBuilder.displayer(displayer);
            return optionsBuilder.build();
        }
        if (cornetRadiusPixels != -1) {
            optionsBuilder.displayer(new FilletBitmapDisplayer(cornetRadiusPixels));
            return optionsBuilder.build();
        }
        return optionsBuilder.build();
    }

    /**
     * 加载图片
     *
     * @param imageView
     */
    public void displayImage(ImageView imageView) {
        ImageLoader.getInstance().displayImage(url, imageView, buildOptions());
    }
}
