package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.eim.util.QRUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.qiniu.android.http.CancellationHandler;

/**
 * @version 1.1.0
 * @describe:生成二维码
 * @author:yuyong
 * @created:2014-11-10下午5:35:04
 */
public class PersonMyBarCodeActivity extends CDDBaseActivity implements OnClickListener {

    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private Button mScanQRCodeBtn;// 扫描二维码
    private ImageView mBarCodeIV;
    private TextView mNickNameTV;// 昵称
    private RelativeLayout mTitleHeight;// 标题布局高度
    private TextView mAreaTV;// 地区
    private ImageView mHeadIV;// 头像
    private PersonHome mPersonHome;
    private TextView rightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data_my_qrcode_ac);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        rightBtn = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        rightBtn.setText("扫一扫");
        rightBtn.setVisibility(View.VISIBLE);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mScanQRCodeBtn = (Button) findViewById(R.id.scan_qr_code_btn);
        mBarCodeIV = (ImageView) findViewById(R.id.qr_code_img);
        mNickNameTV = (TextView) findViewById(R.id.usernameText);
        mAreaTV = (TextView) findViewById(R.id.area);
        mHeadIV = (ImageView) findViewById(R.id.headimage);
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mTitleTV.setText("我的二维码");
        mPersonHome = (PersonHome) getIntent().getSerializableExtra(Constant.PERSONINFO_SER);
        if (mPersonHome != null) {

            if (mPersonHome.getUrls() != null && !mPersonHome.getUrls().isEmpty())
                ImageUtils.displayImage(mPersonHome.getUrls().get(0).getUrl(), mHeadIV, 10, listener);

        } else if (!TextUtils.isEmpty(CacheTools.getUserData("url"))) {

            ImageUtils.displayImage(CacheTools.getUserData("url"), mHeadIV, 10, listener);

        } else {

            mHeadIV.setImageResource(R.drawable.user_fang_icon);
            createQR(BitmapFactory.decodeResource(getResources(), R.drawable.ic_luncher));
        }


        mAreaTV.setText(CacheTools.getUserData("provinceId") + " " + CacheTools.getUserData("cityId"));
        mNickNameTV.setText(CacheTools.getUserData("nickName"));

        // mBarCodeIV
        // .setImageBitmap(create2DCode("MD:"+mPersonHome.getUserId()+";"));
    }

    ImageLoadingListener listener = new ImageLoadingListener() {

        @Override
        public void onLoadingStarted(String arg0, View arg1) {

        }

        @Override
        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            createQR(BitmapFactory.decodeResource(getResources(), R.drawable.ic_luncher));
        }

        @Override
        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
            createQR(drawable2Bitmap(mHeadIV.getDrawable()));

        }

        @Override
        public void onLoadingCancelled(String arg0, View arg1) {

        }
    };

    private void createQR(Bitmap bitmap) {
        try {

            if (mPersonHome != null)

                mBarCodeIV.setImageBitmap(
                        QRUtils.createCode("MD:" + mPersonHome.getUserId() + ";", bitmap, BarcodeFormat.QR_CODE));
            else
                mBarCodeIV.setImageBitmap(
                        QRUtils.createCode("MD:" + CacheTools.getUserData("userId") + ";", bitmap, BarcodeFormat.QR_CODE));


        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        mScanQRCodeBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv:
                Intent intent = new Intent(PersonMyBarCodeActivity.this, CaptureActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 用字符串生成二维码
     *
     * @param str
     * @return
     * @throws WriterException
     * @author zhouzhe@lenovo-cw.com
     */
    public Bitmap create2DCode(String str) throws WriterException {
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 600, 600);
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

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
