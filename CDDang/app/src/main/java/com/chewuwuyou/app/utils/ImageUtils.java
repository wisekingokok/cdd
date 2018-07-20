package com.chewuwuyou.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.AsyncBitmapLoader.ImageCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 图片操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */

public class ImageUtils {

    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";

    /**
     * 请求相册
     */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 3000;
    /**
     * 请求相机
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 3001;
    /**
     * 请求裁剪
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 3002;
    // private static final int STROKE_WIDTH = 4;
    public static Uri tempFile;
    public static Uri cropImageUri;
    public static String cropImagePath;
    public static ImageLoader mImageLoader;
    public static String tempImageFilePath = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/"
            + "chedangdang" + "/temp.jpg";

    public static void displayDefaultImage(String url, ImageView mIV, int Rounde) {
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(
                    NetworkUtil.IMAGE_BASE_URL + url,
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.user_fang_icon)
                            .showImageForEmptyUri(R.drawable.user_fang_icon)
                            .showImageOnFail(R.drawable.user_fang_icon)
                            .displayer(new RoundedBitmapDisplayer(Rounde))
                            .build());
        } else {
            mImageLoader.displayImage(
                    NetworkUtil.IMAGE_BASE_URL + url,
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.user_fang_icon)
                            .showImageForEmptyUri(R.drawable.user_fang_icon)
                            .showImageOnFail(R.drawable.user_fang_icon).build());
        }

    }

    public static void displayImage(String url, ImageView mIV, int Rounde,
                                    int imageDefaultID, int imageLoadFailID) {
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(
                    ServerUtils.getServerIP(url),
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).showStubImage(imageDefaultID)
                            .showImageForEmptyUri(imageDefaultID)
                            .showImageOnFail(imageLoadFailID)
                            .displayer(new FilletBitmapDisplayer(Rounde))
                            .build());
        } else {
            mImageLoader.displayImage(ServerUtils.getServerIP(url), mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).showStubImage(imageDefaultID)
                            .showImageForEmptyUri(imageDefaultID)
                            .showImageOnFail(imageLoadFailID).build());
        }

    }

    public static void displayImage(final Context context, String url,
                                    final ImageView imageView, int Rounde, final int dstWidth,
                                    final int dstHeight, final ScalingLogic scalingLogic,
                                    final int imageDefaultID, final int imageLoadFailID) {
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (url == null) {
            return;
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(
                    ServerUtils.getServerIP(url),
                    imageView,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).showStubImage(imageDefaultID)
                            .showImageForEmptyUri(imageDefaultID)
                            .showImageOnFail(imageLoadFailID)
                            .displayer(new RoundedBitmapDisplayer(Rounde))
                            .build()
//							, 
//							new ImageLoadingListener() {
//
//						@Override
//						public void onLoadingCancelled(String arg0, View arg1) {
//
//						}
//
//						@Override
//						public void onLoadingComplete(String arg0, View arg1,
//								Bitmap arg2) {
//							Bitmap bm = null;
//							if (arg2 != null) {
//								bm = ImageUtils.createScaledBitmap(arg2,
//										dstWidth, dstHeight, scalingLogic);
//								imageView.setImageBitmap(bm);
//							} else {
//								InputStream is = context.getResources()
//										.openRawResource(imageLoadFailID);
//								bm = ImageUtils.createScaledBitmap(
//										BitmapFactory.decodeStream(is),
//										dstWidth, dstHeight, scalingLogic);
//								imageView.setImageBitmap(bm);
//							}
//							// motify start by yuyong 2016/04/09 防止图片回收出现oom
//							// bm.recycle();
//							// motify start by yuyong 2016/04/09 防止图片回收出现oom
//						}
//
//						@Override
//						public void onLoadingFailed(String arg0, View arg1,
//								FailReason arg2) {
//
//						}
//
//						@Override
//						public void onLoadingStarted(String arg0, View arg1) {
//						}
//
//					}
            );
        } else {
            mImageLoader.displayImage(ServerUtils.getServerIP(url), imageView,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).showStubImage(imageDefaultID)
                            .showImageForEmptyUri(imageDefaultID)
                            .showImageOnFail(imageLoadFailID).build()
//							,new ImageLoadingListener() {
//
//						@Override
//						public void onLoadingCancelled(String arg0, View arg1) {
//
//						}
//
//						@Override
//						public void onLoadingComplete(String arg0, View arg1,
//								Bitmap arg2) {
//							if (arg2 != null) {
//								Bitmap bm = ImageUtils.createScaledBitmap(arg2,
//										dstWidth, dstHeight, scalingLogic);
//								imageView.setImageBitmap(bm);
//							} else {
//								InputStream is = context.getResources()
//										.openRawResource(imageLoadFailID);
//								Bitmap bm = ImageUtils.createScaledBitmap(
//										BitmapFactory.decodeStream(is),
//										dstWidth, dstHeight, scalingLogic);
//								imageView.setImageBitmap(bm);
//							}
//						}
//
//						@Override
//						public void onLoadingFailed(String arg0, View arg1,
//								FailReason arg2) {
//
//						}
//
//						@Override
//						public void onLoadingStarted(String arg0, View arg1) {
//
//						}
//
//					}
            );
        }

    }

    /**
     * 从本地文件加载图片
     *
     * @param path
     * @param mIV
     * @param Rounde
     * @param imageDefaultID
     * @param imageLoadFailID
     */
    public static void displayImageFromFile(String path, ImageView mIV,
                                            int Rounde, int imageDefaultID, int imageLoadFailID) {
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(
                    "file://" + path,
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).showStubImage(imageDefaultID)
                            .showImageForEmptyUri(imageDefaultID)
                            .showImageOnFail(imageLoadFailID)
                            .displayer(new FilletBitmapDisplayer(Rounde))
                            .build());
        } else {
            mImageLoader.displayImage("file://" + path, mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).showStubImage(imageDefaultID)
                            .showImageForEmptyUri(imageDefaultID)
                            .showImageOnFail(imageLoadFailID).build());
        }

    }

    public static void displayImage(final Context context, String url,
                                    final ImageView imageView, int Rounde, final int dstWidth,
                                    final int dstHeight, final ScalingLogic scalingLogic,
                                    final int imageDefaultID, final int imageLoadFailID,
                                    ImageLoadingListener listener) {
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (url == null) {
            return;
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(
                    ServerUtils.getServerIP(url),
                    imageView,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).showStubImage(imageDefaultID)
                            .showImageForEmptyUri(imageDefaultID)
                            .showImageOnFail(imageLoadFailID)
                            .displayer(new RoundedBitmapDisplayer(Rounde))
                            .build(), listener);
        } else {
            mImageLoader
                    .displayImage(
                            ServerUtils.getServerIP(url),
                            imageView,
                            new DisplayImageOptions.Builder()
                                    .cacheInMemory(true).cacheOnDisc(true)
                                    .showStubImage(imageDefaultID)
                                    .showImageForEmptyUri(imageDefaultID)
                                    .showImageOnFail(imageLoadFailID).build(),
                            listener);
        }
    }

    /**
     * 显示图片
     *
     * @param url
     * @param mIV
     * @param Rounde
     */
    public static void displayImage(String url, ImageView mIV, int Rounde) {

        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(
                    ServerUtils.getServerIP(url),
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.user_fang_icon)
                            .showImageForEmptyUri(R.drawable.user_fang_icon)
                            .showImageOnFail(R.drawable.user_fang_icon)
                            .displayer(new RoundedBitmapDisplayer(Rounde))
                            .build());
        } else {
            mImageLoader.displayImage(
                    ServerUtils.getServerIP(url),
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.user_fang_icon)
                            .showImageForEmptyUri(R.drawable.user_fang_icon)
                            .showImageOnFail(R.drawable.user_fang_icon).build());
        }

    }

    /**
     * 显示图片
     *
     * @param url
     * @param mIV
     * @param Rounde
     */
    public static void displayUserHeadImage(String url, ImageView mIV,
                                            int Rounde) {

        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(
                    ServerUtils.getServerIP(url),
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .displayer(new RoundedBitmapDisplayer(Rounde))
                            .build());
        } else {
            mImageLoader.displayImage(ServerUtils.getServerIP(url), mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).build());
        }

    }

    /**
     * 显示图片
     *
     * @param url
     * @param mIV
     * @param Rounde
     * @param listener
     */
    public static void displayImage(String url, ImageView mIV, int Rounde,
                                    ImageLoadingListener listener) {

        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(
                    ServerUtils.getServerIP(url),
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.user_fang_icon)
                            .showImageForEmptyUri(R.drawable.user_fang_icon)
                            .showImageOnFail(R.drawable.user_fang_icon)
                            .displayer(new RoundedBitmapDisplayer(Rounde))
                            .build(), listener);
        } else {
            mImageLoader.displayImage(
                    ServerUtils.getServerIP(url),
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.user_fang_icon)
                            .showImageForEmptyUri(R.drawable.user_fang_icon)
                            .showImageOnFail(R.drawable.user_fang_icon).build(),
                    listener);
        }

    }

    public static void displayImage(String url, final ImageView imageView,
                                    int Rounde, final int dstWidth, final int dstHeight,
                                    final ScalingLogic scalingLogic) {
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (url == null) {
            return;
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(ServerUtils.getServerIP(url),
                    imageView,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.image_default)
                            .showImageForEmptyUri(R.drawable.image_default)
                            .showImageOnFail(R.drawable.image_load_fail)
                            .displayer(new RoundedBitmapDisplayer(Rounde))
                            .build());
//			mImageLoader.displayImage(
//					ServerUtils.getServerIP(url),
//					imageView,
//					new DisplayImageOptions.Builder().cacheInMemory(true)
//							.cacheOnDisc(true)
//							.showStubImage(R.drawable.image_default)
//							.showImageForEmptyUri(R.drawable.image_default)
//							.showImageOnFail(R.drawable.image_load_fail)
//							.displayer(new RoundedBitmapDisplayer(Rounde))
//							.build(), new ImageLoadingListener() {
//
//						@Override
//						public void onLoadingCancelled(String arg0, View arg1) {
//
//						}
//
//						@Override
//						public void onLoadingComplete(String arg0, View arg1,
//								Bitmap arg2) {
//							if (arg2 != null) {
//								Bitmap bm = ImageUtils.createScaledBitmap(arg2,
//										dstWidth, dstHeight, scalingLogic);
//								imageView.setImageBitmap(bm);
//								// motify start by yuyong 2016/04/09 防止图片回收出现oom
//								// bm.recycle();
//								// motify start by yuyong 2016/04/09 防止图片回收出现oom
//							}
//
//						}
//
//						@Override
//						public void onLoadingFailed(String arg0, View arg1,
//								FailReason arg2) {
//
//						}
//
//						@Override
//						public void onLoadingStarted(String arg0, View arg1) {
//
//						}
//
//					});
        } else {
            mImageLoader.displayImage(
                    ServerUtils.getServerIP(url),
                    imageView,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.image_default)
                            .showImageForEmptyUri(R.drawable.image_default)
                            .showImageOnFail(R.drawable.image_load_fail)
                            .build()
//                    , new ImageLoadingListener() {
//
//                        @Override
//                        public void onLoadingCancelled(String arg0, View arg1) {
//
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String arg0, View arg1,
//                                                      Bitmap arg2) {
//                            if (arg2 != null) {
//                                Bitmap bm = ImageUtils.createScaledBitmap(arg2,
//                                        dstWidth, dstHeight, scalingLogic);
//                                imageView.setImageBitmap(bm);
//                            }
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String arg0, View arg1,
//                                                    FailReason arg2) {
//
//                        }
//
//                        @Override
//                        public void onLoadingStarted(String arg0, View arg1) {
//
//                        }
//
//                    }
            );
        }

    }

    /**
     * 显示即时通讯图片 必须遵守即时通讯发送图片的协议 #IMGISD:/chatImg/20150514132742351.pngGMI#
     *
     * @param url
     * @param mIV
     * @param Rounde
     */
    public static void displayChatImage(String url, ImageView mIV, int Rounde) {
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(
                    ServerUtils.getChatServerIP(url),
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.user_fang_icon)
                            .showImageForEmptyUri(R.drawable.user_fang_icon)
                            .showImageOnFail(R.drawable.user_fang_icon)
                            .displayer(new RoundedBitmapDisplayer(Rounde))
                            .build());
        } else {
            mImageLoader.displayImage(
                    ServerUtils.getChatServerIP(url),
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.user_fang_icon)
                            .showImageForEmptyUri(R.drawable.user_fang_icon)
                            .showImageOnFail(R.drawable.user_fang_icon)
                            .build());
        }

    }

    /**
     * 写图片文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @throws IOException
     */
    public static void saveImage(Context context, String fileName, Bitmap bitmap)
            throws IOException {
        saveImage(context, fileName, bitmap, 100);
    }

    public static void saveImage(Context context, String fileName,
                                 Bitmap bitmap, int quality) throws IOException {
        if (bitmap == null || fileName == null || context == null)
            return;
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/车当当/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        FileOutputStream fos = context.openFileOutput(fileName,
                Context.MODE_PRIVATE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, quality, stream);
        byte[] bytes = stream.toByteArray();
        fos.write(bytes);
        fos.close();
    }

    public static void showImg(ImageView view, String url, int defaultimg) {
        if (url != null && !"".equals(url)) {
            Bitmap bitmap = new AsyncBitmapLoader().loadBitmap(view,
                    ServerUtils.getServerIP(url), new ImageCallBack() {
                        @Override
                        public void imageLoad(View imageView, Bitmap bitmap) {
                            ((ImageView) imageView).setImageBitmap(bitmap);
                        }
                    });

            if (bitmap != null) {
                view.setImageBitmap(bitmap);
            } else {
                view.setImageResource(defaultimg);
            }
        } else {
            view.setImageResource(defaultimg);
        }

    }

    /**
     * 图片裁剪的方法 1:1
     *
     * @param uri
     * @param activity
     */
    public static void cropImageUri(Uri uri, Activity activity) {
        ImageUtils.cropImageUri = ImageUtils.createImagePathUri(activity);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCROP);

    }

    /**
     * 图片裁剪的方法3:2
     *
     * @param uri
     * @param activity
     */
    public static void cropImageUri1(Uri uri, Activity activity) {
        ImageUtils.cropImageUri = ImageUtils.createImagePathUri(activity);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 3);
        intent.putExtra("aspectY", 2);
        intent.putExtra("outputX", 480);
        intent.putExtra("outputY", 320);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCROP);

    }

    /**
     * 图片裁剪的方法3:2
     *
     * @param uri
     */
    public static void cropImageUri3(Uri uri, Activity activity, int outX, int outY) {
        String basePath = Environment.getExternalStorageDirectory() + "/"
                + "chedangdang";
        File file = new File(basePath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
        String path = basePath + "/" + "tmp_pic_" + System.currentTimeMillis()
                + ".jpg";
        File photo = new File(path);
        if (!new File(getRealPathFromURI(uri, activity)).exists()) return;
        ImageUtils.cropImageUri = Uri.fromFile(photo);
        ImageUtils.cropImagePath = photo.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outX);
        intent.putExtra("outputY", outY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCROP);

    }

    public static String getRealPathFromURI(Uri contentUri, Context context) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /***
     * 从相册中取图片
     */
    public static void pickPhoto(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        activity.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD);

    }

    public static void takePhoto(final Activity activity) {
        ImageUtils.tempFile = ImageUtils.createImagePathUri(activity);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
        // 返回图片在onActivityResult中通过以下代码获取
        // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.tempFile);
        activity.startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    // 将进行剪裁后的图片显示到UI界面上
    @SuppressWarnings("deprecation")
    public static void setPicToView(Intent picdata, ImageView view) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            view.setBackgroundDrawable(drawable);
        }
    }

    // 提示对话框方法
    public static void showDialog(final Activity activity, boolean ifCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("图片设置");
        builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                takePhoto(activity);
            }
        });
        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                pickPhoto(activity);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(ifCancel);
        dialog.show();
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws IOException
     */
    public static void saveImageToSD(Context ctx, String filePath,
                                     Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0,
                    filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(filePath));
            bitmap.compress(CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            if (ctx != null) {
                scanPhoto(ctx, filePath);
            }
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    private static void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取bitmap
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = context.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return getBitmapByPath(filePath, null);
    }

    public static Bitmap getBitmapByPath(String filePath,
                                         BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 获取bitmap
     *
     * @param file
     * @return
     */
    public static Bitmap getBitmapByFile(File file) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 使用当前时间戳拼接一个唯一的文件名
     *
     * @param format
     * @return
     */
    // public static String getTempFileName() {
    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
    // String fileName = format.format(new Timestamp(System
    // .currentTimeMillis()));
    // return fileName;
    // }

    /**
     * 使用系统当前日期加以调整作为照片的名称
     *
     * @return
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     * 获取照相机使用的目录
     *
     * @return
     */
    public static String getCamerPath() {
        return Environment.getExternalStorageDirectory() + File.separator
                + "FounderNews" + File.separator;
    }

    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     *
     * @param mUri
     * @return
     */
    public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + SDCARD + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }

    /**
     * 通过uri获取文件的绝对路径
     *
     * @param uri
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getAbsoluteImagePath(Activity context, Uri uri) {
        String imagePath = "";
        String[] proj = {MediaColumns.DATA};
        Cursor cursor = context.managedQuery(uri, proj, // Which columns to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }

        return imagePath;
    }

    public static Bitmap loadImgThumbnail(String filePath, int w, int h) {
        Bitmap bitmap = getBitmapByPath(filePath);
        return zoomBitmap(bitmap, w, h);
    }

    /**
     * 获取SD卡中最新图片路径
     *
     * @return
     */
    @SuppressWarnings({"deprecation", "unused"})
    public static String getLatestImage(Activity context) {
        String latestImage = null;
        String[] items = {BaseColumns._ID, MediaColumns.DATA};
        Cursor cursor = context.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, items, null,
                null, BaseColumns._ID + " desc");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                latestImage = cursor.getString(1);
                break;
            }
        }

        return latestImage;
    }

    /**
     * 计算缩放图片的宽高
     *
     * @param img_size
     * @param square_size
     * @return
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size)
            return img_size;
        double ratio = square_size
                / (double) Math.max(img_size[0], img_size[1]);
        return new int[]{(int) (img_size[0] * ratio),
                (int) (img_size[1] * ratio)};
    }

    /**
     * 创建缩略图
     *
     * @param context
     * @param largeImagePath 原始大图路径
     * @param thumbfilePath  输出缩略图路径
     * @param square_size    输出图片宽度
     * @param quality        输出图片质量
     * @throws IOException
     */
    public static void createImageThumbnail(Context context,
                                            String largeImagePath, String thumbfilePath, int square_size,
                                            int quality) throws IOException {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        // 原始图片bitmap
        Bitmap cur_bitmap = getBitmapByPath(largeImagePath, opts);

        if (cur_bitmap == null)
            return;

        // 原始图片的高宽
        int[] cur_img_size = new int[]{cur_bitmap.getWidth(),
                cur_bitmap.getHeight()};
        // 计算原始图片缩放后的宽高
        int[] new_img_size = scaleImageSize(cur_img_size, square_size);
        // 生成缩放后的bitmap
        Bitmap thb_bitmap = zoomBitmap(cur_bitmap, new_img_size[0],
                new_img_size[1]);
        // 生成缩放后的图片文件
        saveImageToSD(null, thumbfilePath, thb_bitmap, quality);
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap) {
        // 获取这个图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = 200;
        int newHeight = 200;
        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        // 旋转图片 动作
        // matrix.postRotate(45);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return resizedBitmap;
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (null == drawable) {
            return null;
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 获得圆角图片的方法
     *
     * @param bitmap
     * @param roundPx 一般设成14
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获得带倒影的图片方法
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 将bitmap转化为drawable
     *
     * @param bitmap
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * 获取图片类型
     *
     * @param file
     * @return
     */
    public static String getImageType(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            String type = getImageType(in);
            return type;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取图片的类型信息
     *
     * @param in
     * @return
     * @see #getImageType(byte[])
     */
    public static String getImageType(InputStream in) {
        if (in == null) {
            return null;
        }
        try {
            byte[] bytes = new byte[8];
            in.read(bytes);
            return getImageType(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取图片的类型信息
     *
     * @param bytes 2~8 byte at beginning of the image file
     * @return image mimetype or null if the file is not image
     */
    public static String getImageType(byte[] bytes) {
        if (isJPEG(bytes)) {
            return "image/jpeg";
        }
        if (isGIF(bytes)) {
            return "image/gif";
        }
        if (isPNG(bytes)) {
            return "image/png";
        }
        if (isBMP(bytes)) {
            return "application/x-bmp";
        }
        return null;
    }

    private static boolean isJPEG(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
    }

    private static boolean isGIF(byte[] b) {
        if (b.length < 6) {
            return false;
        }
        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    private static boolean isPNG(byte[] b) {
        if (b.length < 8) {
            return false;
        }
        return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
                && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10);
    }

    private static boolean isBMP(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == 0x42) && (b[1] == 0x4d);
    }

    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @param context
     * @return 图片的uri
     */
    private static Uri createImagePathUri(Context context) {
        Uri imageFilePath = null;
        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaColumns.DISPLAY_NAME, imageName);
        values.put(ImageColumns.DATE_TAKEN, time);
        values.put(MediaColumns.MIME_TYPE, "image/jpeg");
        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            imageFilePath = context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            imageFilePath = context.getContentResolver().insert(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        }
        Log.i("YUY", "生成的照片输出路径：" + imageFilePath.toString());
        return imageFilePath;
    }

    public static void saveMyBitmap(String bitName, Bitmap mBitmap) {
        File f = new File("/sdcard/" + bitName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
     * <p/>
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
     * <p/>
     * B.本地路径:url="file://mnt/sdcard/photo/image.png";
     * <p/>
     * C.支持的图片格式 ,png, jpg,bmp,gif等等
     *
     * @param url
     * @return
     */
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(),
                    Constant.IO_BUFFER_SIZE);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, Constant.IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[Constant.IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    public static Bitmap getBitmapByUrl(ImageView view, String imgUrl) {
        Bitmap bitmap = new AsyncBitmapLoader().loadBitmap(view,
                ServerUtils.getServerIP(imgUrl), null);
        return bitmap;
    }

    // 加密为MD5

    public static String getMD5(String content) {

        try {

            MessageDigest digest = MessageDigest.getInstance("MD5");

            digest.update(content.getBytes());

            return getHashString(digest);

        } catch (Exception e) {

        }

        return null;

    }

    private static String getHashString(MessageDigest digest) {

        StringBuilder builder = new StringBuilder();

        for (byte b : digest.digest()) {

            builder.append(Integer.toHexString((b >> 4) & 0xf));

            builder.append(Integer.toHexString(b & 0xf));

        }

        return builder.toString().toLowerCase();

    }

    /**
     * 缩放
     *
     * @param unscaledBitmap 原图
     * @param dstWidth       目标宽度
     * @param dstHeight      目标高度
     * @param scalingLogic   目标高度
     */
    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap,
                                            int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (unscaledBitmap == null) {
            return null;
        }
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(),
                unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(),
                unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Config.ARGB_4444);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(
                Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }

    /**
     * 创建源矩形
     *
     * @param srcWidth  原宽
     * @param srcHeight 原高
     * @param dstWidth  目宽
     * @param dstHeight 目高
     */
    public static Rect calculateSrcRect(int srcWidth, int srcHeight,
                                        int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int) (srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth,
                        srcHeight);
            } else {
                final int srcRectHeight = (int) (srcWidth / dstAspect);
                final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop
                        + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    /**
     * 创建目标矩形
     *
     * @param srcWidth  原宽
     * @param srcHeight 原高
     * @param dstWidth  目宽
     * @param dstHeight 目高
     */
    public static Rect calculateDstRect(int srcWidth, int srcHeight,
                                        int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    /**
     * 也就是说先将Options的属性inJustDecodeBounds设为true，先获取图片的基本大小信息数据(信息没有保存在bitmap里面，
     * 而是保存在options里面)，通过options.outHeight和 options.
     * outWidth获取的大小信息以及自己想要到得图片大小计算出来缩放比例inSampleSize
     * ，然后紧接着将inJustDecodeBounds设为false，就可以根据已经得到的缩放比例得到自己想要的图片缩放图了。
     *
     * @param dstWidth  目宽
     * @param dstHeight 目高
     * @return 缩放之后的图
     */
    public static Bitmap decodeFile(String pathName, int dstWidth,
                                    int dstHeight, ScalingLogic scalingLogic) {
        Options options = new Options();
        options.inJustDecodeBounds = true;// 设置为true的时候不分配内存,bitmap=null，但是可以获取一些参数,例如获取到outHeight(图片原始高度)和
        // outWidth(图片的原始宽度)，存放到options中
        BitmapFactory.decodeFile(pathName, options);
        options.inJustDecodeBounds = false;
        // 容量变为以前的多少分之一
        // 然后计算一个inSampleSize(缩放值)，就可以取图片了
        options.inSampleSize = calculateSampleSize(options.outWidth,
                options.outHeight, dstWidth, dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeFile(pathName, options);
        return unscaledBitmap;
    }

    /**
     * 计算例子的大小
     *
     * @param srcWidth  原宽
     * @param srcHeight 原高
     * @param dstWidth  目宽
     * @param dstHeight 目高
     */
    public static int calculateSampleSize(int srcWidth, int srcHeight,
                                          int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        // 如果是适配的话
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;// 原来的比例
            final float dstAspect = (float) dstWidth / (float) dstHeight;// 现在的比例
            if (srcAspect > dstAspect) {// 如果原来的比例大于现在的比例
                return srcWidth / dstWidth;//
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }

    // 裁剪或者适配
    public static enum ScalingLogic {
        CROP, FIT
    }

    /**
     * 对图片进行压缩
     *
     * @param image
     * @return
     */
    public static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;
        // return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 显示图片
     *
     * @param url
     * @param mIV
     * @param Rounde
     * @param resId
     */
    public static void commDisplayImage(String url, ImageView mIV, int Rounde,
                                        int resId) {
        MyLog.i("", ServerUtils.getServerIP(url));
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        if (Rounde > 0) {
            mImageLoader.displayImage(ServerUtils.getServerIP(url), mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).showStubImage(resId)
                            .showImageForEmptyUri(resId).showImageOnFail(resId)
                            .displayer(new RoundedBitmapDisplayer(Rounde))
                            .build());
        } else {
            mImageLoader.displayImage(
                    ServerUtils.getServerIP(url),
                    mIV,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.image_default)
                            .showImageForEmptyUri(R.drawable.image_default)
                            .showImageOnFail(R.drawable.image_load_fail)
                            .build());
        }

    }

}
