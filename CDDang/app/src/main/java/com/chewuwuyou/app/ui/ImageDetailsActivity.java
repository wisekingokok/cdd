package com.chewuwuyou.app.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.widget.ZoomImageView;

/**
 * 查看大图的Activity界面。
 * 
 */
public class ImageDetailsActivity extends BaseActivity {

	/**
	 * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动
	 */
	private ZoomImageView zoomImageView;

	/**
	 * 待展示的图片
	 */
	private Bitmap bitmap;

	// private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_details);
		// mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		// isTitle(mTitleHeight);//根据不同手机判断
		zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
		// 取出图片路径，并解析成Bitmap对象，然后在ZoomImageView中显示
		String imagePath = getIntent().getStringExtra("image_path");
		bitmap = BitmapFactory.decodeFile(imagePath);
		if (bitmap != null) {
			zoomImageView.setImageBitmap(bitmap);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 记得将Bitmap对象回收掉
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

}