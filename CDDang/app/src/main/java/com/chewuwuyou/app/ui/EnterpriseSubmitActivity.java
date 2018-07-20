package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.ExamineBook;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.localImg;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;

import java.io.File;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * @describe:提交商家信息
 * @author:liuchun
 */
public class EnterpriseSubmitActivity extends CDDBaseActivity implements
        OnClickListener, MenuItemClickListener {

    private TextView mTitleTV;// 标题
    private ImageButton mBackIBtn;// 返回上一页
    private Button mCommitVerSubmitBtn;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private ImageView mEnterpriseBusinessPortrait, mEnterpriseStorePortrait;// 营业执照,门店照片
    private String mUpdaloadUrl;// 上传图片的地址
    private String orderBusinessLicense, orderStore;// 营业执照，门店
    private TextView mBusinessStore, mBusinessLicense;
    private ExamineBook mResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterprise_sbbmit_ac);

        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        mEnterpriseBusinessPortrait = (ImageView) findViewById(R.id.enterprise_business_portrait);
        mEnterpriseStorePortrait = (ImageView) findViewById(R.id.enterprise_store_portrait);
        mCommitVerSubmitBtn = (Button) findViewById(R.id.commit_ver_btn);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mTitleTV.setText("企业认证");
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);

        mBusinessStore = (TextView) findViewById(R.id.business_license);
        mBusinessLicense = (TextView) findViewById(R.id.business_store);



        if(getIntent().getSerializableExtra("response")!=null){
            mResponse = (ExamineBook) getIntent().getSerializableExtra("response");
            if(mResponse.getIdentityType().equals("1")){
                ImageUtils.displayImage(mResponse.getLicenseImageUrl(),mEnterpriseBusinessPortrait, 10);
                ImageUtils.displayImage(mResponse.getStoreFrontImageUrl(),mEnterpriseStorePortrait, 10);
                orderBusinessLicense = mResponse.getLicenseImageUrl();
                orderStore = mResponse.getStoreFrontImageUrl();
            }
        }
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_license:
                dialogId(1);
                break;
            case R.id.business_store:
                dialogId(2);
                break;
            case R.id.commit_ver_btn:
                if (TextUtils.isEmpty(orderBusinessLicense)) {
                    ToastUtil.toastShow(EnterpriseSubmitActivity.this, "请上传营业执照");
                } else if (TextUtils.isEmpty(orderStore)) {
                    ToastUtil.toastShow(EnterpriseSubmitActivity.this, "请上传门店照片");
                } else {
                    AjaxParams params = new AjaxParams();
                    params.put("identityType", "1");// 企业
                    params.put("catetory", "5");// B类商家
                    params.put("companyName",getIntent().getStringExtra("companyName"));// 名称
                    params.put("companyContact",getIntent().getStringExtra("companyContact"));// 联系人
                    params.put("identityNo",getIntent().getStringExtra("identityNo"));// 身份证号码
                    params.put("licenseNo", getIntent().getStringExtra("licenseNo"));// 营业执照
                    params.put("address", getIntent().getStringExtra("address"));// 地区
                    params.put("provinceId",getIntent().getStringExtra("provinceId"));// 省Id
                    params.put("cityId", getIntent().getStringExtra("cityId"));// 是Id
                    params.put("districtId",getIntent().getStringExtra("districtId"));// 区Id
                    params.put("identityImageUrl",getIntent().getStringExtra("identityImageUrl"));// 上传身份证的地址
                    params.put("identityBeiImageUrl",getIntent().getStringExtra("identityBeiImageUrl"));// 身份证反面地址
                    params.put("headImageUrl",getIntent().getStringExtra("headImageUrl"));
                    params.put("handImageUrl",getIntent().getStringExtra("handImageUrl"));
                    params.put("licenseImageUrl", orderBusinessLicense);// 营业执照
                    params.put("storeFrontImageUrl", orderStore);// 门店照片
                    if(mResponse!=null){
                        params.put("updateId", mResponse.getUserId());
                    }
                    networkBusiness(params);
                }
                break;
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.enterprise_business_portrait:// 营业执照
                mUpdaloadUrl = NetworkUtil.LICENSE_IMG;
                openUploadMenu();
                break;
            case R.id.enterprise_store_portrait:// 门店
                mUpdaloadUrl = NetworkUtil.STORE_IMG;
                openUploadMenu();
                break;
            default:
                break;
        }

    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断

    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mEnterpriseBusinessPortrait.setOnClickListener(this);
        mEnterpriseStorePortrait.setOnClickListener(this);
        mBackIBtn.setOnClickListener(this);
        mCommitVerSubmitBtn.setOnClickListener(this);
        mBusinessStore.setOnClickListener(this);
        mBusinessLicense.setOnClickListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case 1:// 拍照
                    String url = Environment.getExternalStorageDirectory()+"/image.jpg";
                    File dirFile = new File(url);
                    if(!dirFile.exists()){//判断目录下的图片是否存在，不存在给出提示
                        ToastUtil.toastShow(this,"请拍照");
                    }else{

                        if (mUpdaloadUrl == NetworkUtil.LICENSE_IMG) {
                            mEnterpriseBusinessPortrait.setImageBitmap(FileUtils.getSmallBitmap(url));
                        } else if (mUpdaloadUrl == NetworkUtil.STORE_IMG) {
                            mEnterpriseStorePortrait.setImageBitmap(FileUtils.getSmallBitmap(url));
                        }
                        uploadFile(FileUtils.bitmapToString(url), mUpdaloadUrl);
                        deleteFile(url);
                    }
                    break;
                case 2:// 相册

                    if(data!=null) {
                        List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        uploadFile(FileUtils.bitmapToString(paths.get(0)), mUpdaloadUrl);
                        if (mUpdaloadUrl == NetworkUtil.LICENSE_IMG) {
                            mEnterpriseBusinessPortrait.setImageBitmap(FileUtils.getSmallBitmap(paths.get(0)));
                        } else if (mUpdaloadUrl == NetworkUtil.STORE_IMG) {
                            mEnterpriseStorePortrait.setImageBitmap(FileUtils.getSmallBitmap(paths.get(0)));
                        }
                    }

//                    if(data!=null){
//                        ContentResolver resolver = getContentResolver();
//                        //照片的原始资源地址
//                        Uri originalUri = data.getData();
//                        String[] proj = {MediaStore.Images.Media.DATA};
//                        //好像是android多媒体数据库的封装接口，具体的看Android文档
//                        Cursor cursor = managedQuery(originalUri, proj, null, null, null);
//                        //按我个人理解 这个是获得用户选择的图片的索引值
//                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
//                        cursor.moveToFirst();
//                        //最后根据索引值获取图片路径
//                        String path = cursor.getString(column_index);
//                        if(path!=null){
//                            uploadFile(FileUtils.bitmapToString(path), mUpdaloadUrl);
//                        }else{
//                            ToastUtil.toastShow(this,"请在图库选择图片");
//                        }
//
//                        if (mUpdaloadUrl == NetworkUtil.LICENSE_IMG) {
//                            mEnterpriseBusinessPortrait.setImageBitmap(FileUtils.getSmallBitmap(path));
//                        } else if (mUpdaloadUrl == NetworkUtil.STORE_IMG) {
//                            mEnterpriseStorePortrait.setImageBitmap(FileUtils.getSmallBitmap(path));
//                        }
//                    }
                    break;
                default:
                    break;
            }
        }

    /**
     * 删除SD卡或者手机的缓存图片和目录
     */
    public boolean deleteFile(String img) {
        File dirFile = new File(img);
        if(! dirFile.exists()){
            return false;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }

        dirFile.delete();
        return true;
    }
    /**
     * 上传图片到服务器
     * @param uploadUrl
     */
    private void uploadFile(String imgStr, String uploadUrl) {

        AjaxParams params = new AjaxParams();
        params.put("imgStr", imgStr);
        params.put("file", "");
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        ToastUtil.toastShow(EnterpriseSubmitActivity.this, "上传成功");
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            if (mUpdaloadUrl == NetworkUtil.LICENSE_IMG) {
                                orderBusinessLicense = jo.getString("url");
                            } else if (mUpdaloadUrl == NetworkUtil.STORE_IMG) {
                                orderStore = jo.getString("url");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        MyLog.i("YUY", "上传图片失败 = " + msg.obj.toString());
                        ToastUtil.toastShow(EnterpriseSubmitActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        ToastUtil.toastShow(EnterpriseSubmitActivity.this,"网络开小差了");
                        break;
                }
            }
        }, params, uploadUrl, false, 0);
    }

    private void openUploadMenu() {
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(EnterpriseSubmitActivity.this);
        menuView.setCancelButtonTitle("取 消");// before add items
        menuView.addItems("拍 照", "从相册选取");
        menuView.setItemClickListener(EnterpriseSubmitActivity.this);
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
    }

    @Override
    public void onItemClick(int itemPosition) {
        Intent intent;
        switch (itemPosition) {
            case 0:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //直接使用，没有缩小
                startActivityForResult(intent,1);  //用户点击了从相机获取
                break;
            case 1://相册
                intent=new Intent(this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,1);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                startActivityForResult(intent, 2);

                break;
            default:
                break;
        }
    }

    /**
     * 提交商家信息
     */
    public void networkBusiness(AjaxParams params) {

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        ToastUtil.toastShow(EnterpriseSubmitActivity.this, "提交资料成功,等待审核");
                        EnterpriseCheckActivity.enterpriseCheckActivity
                                .finishActivity();
                        IDVerificationActivity.idVerificationActivity
                                .finishActivity();
                        finishActivity();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(EnterpriseSubmitActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                }
            }
        }, params, NetworkUtil.BUSINESS_ENTER, false, 0);
    }

    /**
     * 传递id放大图片
     *
     * @param id
     */
    @SuppressWarnings("deprecation")
    public void dialogId(int id) {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.portrait_settled_dialog, null);
        ImageView image = (ImageView) layout
                .findViewById(R.id.order_dialog_img);
        ImageView imagems = (ImageView) layout
                .findViewById(R.id.order_img_delete);
        if (id == 1) {
            image.setImageResource(R.drawable.businesslicenseer);
        } else if (id == 2) {
            image.setImageResource(R.drawable.storeer);
        }
        imagems.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		/*
		 * window.setWindowAnimations(R.style.myrightstyle); // 添加动画
		 */
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }
}
