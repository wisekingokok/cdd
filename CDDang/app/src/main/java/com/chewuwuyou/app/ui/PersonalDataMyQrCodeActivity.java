package com.chewuwuyou.app.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.PersonalInfo;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * @describe：我的二维码
 * @author ：caixuemei
 * @created ：2014-7-10下午2:45:52
 */
public class PersonalDataMyQrCodeActivity extends BaseActivity {

	//private Button mScanQRCode;
	private ImageView mImageView;
	private TextView mNickName;// 昵称
	private TextView mArea;// 地区
	private ImageView mHeadImage;// 头像
	private RelativeLayout mTitleHeight;//标题布局高度
	private PersonalInfo mPersonalInfo;// 个人信息实体类
	private Button mScanQrCodeBtn;
	// 查询并显示个人资料
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.NET_DATA_SUCCESS:
				try {
					mPersonalInfo = PersonalInfo.parse(msg.obj.toString());
				} catch (Exception e) {
				}
				if (mPersonalInfo != null) {
					// ImageLoader.getInstance().displayImage(
					// NetworkUtil.IMAGE_BASE_URL + mPersonalInfo.getUrl(),
					// mHeadImage,
					// new DisplayImageOptions.Builder()
					// .cacheInMemory(true)
					// .cacheOnDisc(true)
					// .displayer(new RoundedBitmapDisplayer(10))
					// .build());
					ImageUtils.displayImage(mPersonalInfo.getUrl(), mHeadImage, 10);
					String nickName = mPersonalInfo.getNickName();
					String location = mPersonalInfo.getLocation();
					if (nickName.equals("")) {
						mNickName.setText("未设置");
					} else {
						mNickName.setText(nickName);
					}
					if (location.equals("")) {
						mArea.setText("未设置");
					} else {
						mArea.setText(location);
					}
				}
				break;
			case Constant.NET_DATA_FAIL:
				Toast.makeText(PersonalDataMyQrCodeActivity.this, "请求数据失败", 500)
						.show();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.personal_data_my_qrcode_ac);

		init();
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		mScanQrCodeBtn = (Button) findViewById(R.id.scan_qr_code_btn);
		isTitle(mTitleHeight);//根据不同手机判断
		((TextView) findViewById(R.id.sub_header_bar_tv)).setText("二维码");
		findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finishActivity();
					}
				});
		// mScanQRCode = (Button) findViewById(R.id.scan_qr_code);
		mImageView = (ImageView) findViewById(R.id.qr_code_img);
		mNickName = (TextView) findViewById(R.id.usernameText);
		mArea = (TextView) findViewById(R.id.area);
		mHeadImage = (ImageView) findViewById(R.id.headimage);

		try {
			mImageView
					.setImageBitmap(create2DCode("www.chewuwuyou.com/spng/upload/apk/chewuwuyou.apk\n\n"
							+ "MD:" + CacheTools.getUserData("userId") + ";"));
		} catch (WriterException e) {
			e.printStackTrace();
		}
		// 扫描二维码
		// mScanQRCode.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// Intent intent = new Intent(PersonalDataMyQrCodeActivity.this,
		// CaptureActivity.class);
		// startActivity(intent);
		// }
		// });

		if(getIntent().getIntExtra("scanning", 0)==1){
			mScanQrCodeBtn.setVisibility(View.GONE);
		}
		/*scanning*/
		// 向服务器请求数据
		requestNet(mHandler, null, NetworkUtil.SELECT_PERSONAL_DATA, false, 0);
		// 更新UI
		mHandler.sendEmptyMessage(Constant.UPDATE_UI);
	}

	/**
	 * 用字符串生成二维码
	 * 
	 * @param str
	 * @author zhouzhe@lenovo-cw.com
	 * @return
	 * @throws WriterException
	 */
	public Bitmap create2DCode(String str) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, 600, 600);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}

			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}
