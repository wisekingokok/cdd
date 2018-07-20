package com.chewuwuyou.app.ui;

import java.io.File;
import java.text.ParseException;
import java.util.List;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CityName;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.DistrictName;
import com.chewuwuyou.app.bean.ExamineBook;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.IDCardValidate;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.localImg;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * @describe:商家入驻
 * @author:liuchun
 */
public class EnterpriseCheckActivity extends CDDBaseActivity implements
        OnClickListener, MenuItemClickListener {

    private EditText mShopNameET;// 企业名称
    private EditText mContactNameET;// 联系人姓名
    private EditText mContactNumberEt;// 联系人身份证
    private EditText mLicenseNumET;// 营业执照号码
    private EditText mAddressEt;// 详细地址
    private TextView mRemarkET;// 服务地区
    private RelativeLayout mBusinessIdOther;

    private String mProvinceName;
    private String mCityName;
    private String mDistrictName;
    private ImageView mIDCardBtn, mHeadBtn, mPortrait,mEnterpriseHead;// 身份证正面,身份证反面,头像
    private String mUpdaloadUrl;// 上传头像图片的地址

    private String mHandImageUrl;//上传头像
    private String mIdentityImageUrl;// 上传身份证的地址
    private String mHeadImageUrl;/* 上传手持图片 */
    private String mBusinessIdentity;// 商家身份证反面

    private Button mCommitVerBtn;// 下一步
    private TextView mTitleTV;// 标题
    private ImageButton mBackIBtn;// 返回上一页
    private RelativeLayout mTitleHeight;// 标题布局高度
    public static EnterpriseCheckActivity enterpriseCheckActivity;
    private String mProvinceId;
    private String mCityId;
    private String mDistrictId;
    private TextView mBusinessPositive, mBusinessBack, mBusinessOther;
    private List<DistrictName> mDistrictNames;// 通过城市id查询地区的数据
    private List<CityName> mCityNames;
    private ExamineBook mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_id_info_ac);
        enterpriseCheckActivity = this;
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        mBusinessIdOther = (RelativeLayout) findViewById(R.id.business_id_other1);
        mShopNameET = (EditText) findViewById(R.id.shop_name_et);
        mContactNameET = (EditText) findViewById(R.id.contact_name_et);
        mContactNumberEt = (EditText) findViewById(R.id.contact_number_et);// 联系人身份证号码
        mAddressEt = (EditText) findViewById(R.id.address_et);
        mLicenseNumET = (EditText) findViewById(R.id.business_license_et);
        mRemarkET = (TextView) findViewById(R.id.remark_et); // 服务地区

        mCommitVerBtn = (Button) findViewById(R.id.commit_ver_btn);

        mIDCardBtn = (ImageView) findViewById(R.id.id_enterprise_card_btn);
        mHeadBtn = (ImageView) findViewById(R.id.head_enterprise_btn);
        mPortrait = (ImageView) findViewById(R.id.enterprise_portrait);

        mBusinessPositive = (TextView) findViewById(R.id.business_id_positive); // 身份证正面
        mBusinessBack = (TextView) findViewById(R.id.business_id_back); // 身份证背面
        mBusinessOther = (TextView) findViewById(R.id.business_id_other); // 头像

        mEnterpriseHead = (ImageView) findViewById(R.id.enterprise_head);

        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mTitleTV.setText("企业认证");
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);

        if (getIntent().getSerializableExtra("response") != null) {
            mResponse = (ExamineBook) getIntent().getSerializableExtra(
                    "response");
            if (mResponse.getIdentityType().equals("1")) {

                mShopNameET.setText(mResponse.getCompanyName());
                mContactNameET.setText(mResponse.getCompanyContact());
                mContactNumberEt.setText(mResponse.getIdentityNo());
                mLicenseNumET.setText(mResponse.getLicenseNo());
                mAddressEt.setText(mResponse.getAddress());
                mRemarkET
                        .setText(mResponse.getProvinceName()
                                + mResponse.getCityName()
                                + mResponse.getDistrictName());
                mProvinceId = mResponse.getProvinceId();
                mCityId = mResponse.getCityId();
                mDistrictId = mResponse.getDistrictId();

                ImageUtils.displayImage(mResponse.getHeadImageUrl(),mEnterpriseHead, 10);
                ImageUtils.displayImage(mResponse.getIdentityImageUrl(), mIDCardBtn, 10);
                ImageUtils.displayImage(mResponse.getHandImageUrl(), mPortrait, 10);
                ImageUtils.displayImage(mResponse.getIdentityBeiImageUrl(), mHeadBtn, 10);

                mIdentityImageUrl = mResponse.getIdentityImageUrl();
                mBusinessIdentity = mResponse.getIdentityBeiImageUrl();
                mHandImageUrl = mResponse.getHeadImageUrl();
                mHeadImageUrl = mResponse.getHandImageUrl();
            }
        }
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.business_id_other1:
                dialogId(0);
                break;
            case R.id.business_id_positive:
                dialogId(1);
                break;
            case R.id.business_id_back:
                dialogId(2);
                break;
            case R.id.business_id_other:
                dialogId(3);
                break;
            case R.id.commit_ver_btn:
                try {
                    boolean  sfz = IDCardValidate.IDCardValidate(mContactNumberEt.getText().toString());//验证身份证是否正确
                    if(!sfz){
                        ToastUtil.toastShow(EnterpriseCheckActivity.this, "请输入正确的身份证号码");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(mShopNameET.getText().toString().trim())) {
                    ToastUtil.toastShow(EnterpriseCheckActivity.this, "请输入企业名称");
                } else if (TextUtils.isEmpty(mContactNumberEt.getText().toString()) || mContactNumberEt.getText().toString().trim().length()<15) {
                    ToastUtil
                            .toastShow(EnterpriseCheckActivity.this, "请输入正确的身份证号码");
                } else if (TextUtils.isEmpty(mContactNameET.getText().toString()
                        .trim())) {
                    ToastUtil.toastShow(EnterpriseCheckActivity.this, "请输入联系人姓名");
                } else if (TextUtils.isEmpty(mLicenseNumET.getText().toString()
                        .trim())
                        || mLicenseNumET.getText().toString().trim().length() < 10) {
                    ToastUtil.toastShow(EnterpriseCheckActivity.this, "请正确填写营业执照号码");
                } else if (TextUtils.isEmpty(mAddressEt.getText().toString())) {
                    ToastUtil.toastShow(EnterpriseCheckActivity.this, "请输入详细地址");
                } else if (TextUtils.isEmpty(mRemarkET.getText().toString())) {
                    ToastUtil.toastShow(EnterpriseCheckActivity.this, "请选择服务地区");
                } else if (TextUtils.isEmpty(mIdentityImageUrl)) {
                    ToastUtil.toastShow(EnterpriseCheckActivity.this, "请上传身份证正面");
                } else if (TextUtils.isEmpty(mBusinessIdentity)) {
                    ToastUtil.toastShow(EnterpriseCheckActivity.this, "请上传身份证背面");
                } else if (TextUtils.isEmpty(mHeadImageUrl)) {
                    ToastUtil.toastShow(EnterpriseCheckActivity.this, "请上传手持证件照");
                }else if(TextUtils.isEmpty(mHandImageUrl)){
                    ToastUtil.toastShow(EnterpriseCheckActivity.this, "请上传您的头像");
                }else if (TextUtils.isEmpty(mProvinceId)
                        && TextUtils.isEmpty(mCityId)
                        && TextUtils.isEmpty(mDistrictId)) {
                    ToastUtil.toastShow(EnterpriseCheckActivity.this, "请选择服务地区");
                } else {
                    intent = new Intent();
                    intent.setClass(EnterpriseCheckActivity.this,EnterpriseSubmitActivity.class);
                    intent.putExtra("companyName", mShopNameET.getText().toString().trim());// 企业名称
                    intent.putExtra("companyContact", mContactNameET.getText()
                            .toString().trim());// 联系人
                    intent.putExtra("identityNo", mContactNumberEt.getText()
                            .toString().trim());// 身份证号码
                    intent.putExtra("licenseNo", mLicenseNumET.getText().toString()
                            .trim());// 营业执照
                    intent.putExtra("address", mAddressEt.getText().toString()
                            .trim());// 详细地址
                    intent.putExtra("provinceId", mProvinceId);// 省Id
                    intent.putExtra("cityId", mCityId);// 市ID
                    intent.putExtra("districtId", mDistrictId);// 区id
                    intent.putExtra("identityImageUrl", mIdentityImageUrl);
                    intent.putExtra("identityBeiImageUrl", mBusinessIdentity);
                    intent.putExtra("headImageUrl", mHeadImageUrl);//手持
                    intent.putExtra("handImageUrl", mHandImageUrl);//头像

                    if (mResponse != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("response", mResponse);
                        intent.putExtras(bundle);
                    }
                    startActivity(intent);
                }
                break;
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.id_enterprise_card_btn:// 身份证正面
                mUpdaloadUrl = NetworkUtil.ID_CARD_IMG;
                openUploadMenu();
                break;
            case R.id.head_enterprise_btn:// 身份证反面
                mUpdaloadUrl = NetworkUtil.ID_CARD_BACK;
                openUploadMenu();

                break;
            case R.id.enterprise_portrait:// 手持
                mUpdaloadUrl = NetworkUtil.HEAD_IMG;
                openUploadMenu();
                break;
            case R.id.enterprise_head:// 头像
                mUpdaloadUrl = NetworkUtil.HOLD_IMG;
                openUploadMenu();
                break;

            case R.id.remark_et:// 选择城市
                intent = new Intent(EnterpriseCheckActivity.this,
                        AreaSelectActivity.class);
                startActivityForResult(intent, 20);
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
        mBusinessIdOther.setOnClickListener(this);
        mIDCardBtn.setOnClickListener(this);
        mHeadBtn.setOnClickListener(this);
        mPortrait.setOnClickListener(this);
        mPortrait.setOnClickListener(this);
        mBackIBtn.setOnClickListener(this);
        mRemarkET.setOnClickListener(this);
        mCommitVerBtn.setOnClickListener(this);
        mBusinessPositive.setOnClickListener(this);
        mBusinessBack.setOnClickListener(this);
        mBusinessOther.setOnClickListener(this);
        mEnterpriseHead.setOnClickListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case 1:// 拍照
                        String url = Environment.getExternalStorageDirectory()+"/image.jpg";
                        File dirFile = new File(url);
                        if(!dirFile.exists()){//判断目录下的图片是否存在，不存在给出提示
                            ToastUtil.toastShow(this,"请拍照");
                        }else{
                            if (mUpdaloadUrl == NetworkUtil.ID_CARD_IMG) {
                                mIDCardBtn.setImageBitmap(FileUtils.getSmallBitmap(url));
                            } else if (mUpdaloadUrl == NetworkUtil.HEAD_IMG) {
                                mPortrait.setImageBitmap(FileUtils.getSmallBitmap(url));
                            } else if (mUpdaloadUrl == NetworkUtil.ID_CARD_BACK) {
                                mHeadBtn.setImageBitmap(FileUtils.getSmallBitmap(url));
                            }else if(mUpdaloadUrl == NetworkUtil.HOLD_IMG){
                                mEnterpriseHead.setImageBitmap(FileUtils.getSmallBitmap(url));
                            }
                            uploadFile(FileUtils.bitmapToString(url), mUpdaloadUrl);
                            deleteFile(url);
                        }
                    break;
                case 2:// 相册


                    if(data!=null) {
                        List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        uploadFile(FileUtils.bitmapToString(paths.get(0)), mUpdaloadUrl);
                        if (mUpdaloadUrl == NetworkUtil.ID_CARD_IMG) {
                            mIDCardBtn.setImageBitmap(FileUtils.getSmallBitmap(paths.get(0)));
                        } else if (mUpdaloadUrl == NetworkUtil.HEAD_IMG) {
                            mPortrait.setImageBitmap(FileUtils.getSmallBitmap(paths.get(0)));
                        } else if (mUpdaloadUrl == NetworkUtil.ID_CARD_BACK) {
                            mHeadBtn.setImageBitmap(FileUtils.getSmallBitmap(paths.get(0)));
                        }else if(mUpdaloadUrl == NetworkUtil.HOLD_IMG){
                            mEnterpriseHead.setImageBitmap(FileUtils.getSmallBitmap(paths.get(0)));
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
//                        if (mUpdaloadUrl == NetworkUtil.ID_CARD_IMG) {
//                            mIDCardBtn.setImageBitmap(FileUtils.getSmallBitmap(path));
//                        } else if (mUpdaloadUrl == NetworkUtil.HEAD_IMG) {
//                            mPortrait.setImageBitmap(FileUtils.getSmallBitmap(path));
//                        } else if (mUpdaloadUrl == NetworkUtil.ID_CARD_BACK) {
//                            mHeadBtn.setImageBitmap(FileUtils.getSmallBitmap(path));
//                        }else if(mUpdaloadUrl == NetworkUtil.HOLD_IMG){
//                            mEnterpriseHead.setImageBitmap(FileUtils.getSmallBitmap(path));
//                        }
//                    }
                    break;
                case 20:// 地区
                    if(data!=null){
                        if(!TextUtils.isEmpty(data.getStringExtra("districtId"))){
                            mDistrictId = data.getStringExtra("districtId");
                        }

                        if(!TextUtils.isEmpty(data.getStringExtra("cityId"))){
                            mCityId = data.getStringExtra("cityId");
                        }

                        if(!TextUtils.isEmpty(data.getStringExtra("provinceId"))){
                            mProvinceId = data.getStringExtra("provinceId");
                        }

                        mCityName = data.getStringExtra("city");
                        mProvinceName = data.getStringExtra("province");
                        mDistrictName = data.getStringExtra("district");

                        if (data.getStringExtra("cityId") == null) {
                            getCityIdAndDisId();
                        }

                        if (TextUtils.isEmpty(mDistrictName)) {
                            if (mProvinceName.equals(mCityName)) {
                                mRemarkET.setText(mCityName);
                            } else {
                                mRemarkET.setText(mProvinceName + mCityName);
                            }
                        } else {
                            if (mProvinceName.equals(mCityName)
                                    && mCityName.equals(mDistrictName)) {
                                mRemarkET.setText(mDistrictName);
                            } else if (mCityName.equals(mProvinceName)
                                    && !mCityName.equals(mDistrictName)) {
                                mRemarkET.setText(mCityName + mDistrictName);
                            } else if (mCityName.equals(mDistrictName)
                                    && !mProvinceName.equals(mDistrictName)) {
                                mRemarkET.setText(mDistrictName);
                            } else {
                                mRemarkET.setText(mProvinceName + mCityName
                                        + mDistrictName);
                            }
                        }
                    }
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
     *
     * @param file
     * @param uploadUrl
     */
    private void uploadFile(String file, String uploadUrl) {

        AjaxParams params = new AjaxParams();
        params.put("imgStr", file);
        params.put("file", "");
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:

                        ToastUtil.toastShow(EnterpriseCheckActivity.this, "上传成功");
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            if (mUpdaloadUrl == NetworkUtil.ID_CARD_IMG) {
                                mIdentityImageUrl = jo.getString("url");
                            } else if (mUpdaloadUrl == NetworkUtil.HEAD_IMG) {
                                mHeadImageUrl = jo.getString("url");
                            } else if (mUpdaloadUrl == NetworkUtil.ID_CARD_BACK) {
                                mBusinessIdentity = jo.getString("url");
                            }else if(mUpdaloadUrl == NetworkUtil.HOLD_IMG){
                                mHandImageUrl = jo.getString("url");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        MyLog.i("YUY", "上传图片失败 = " + msg.obj.toString());
                        ToastUtil.toastShow(EnterpriseCheckActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        ToastUtil.toastShow(EnterpriseCheckActivity.this,"网络开小差了");
                        break;
                }
            }
        }, params, uploadUrl, false, 0);
    }

    private void openUploadMenu() {
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(EnterpriseCheckActivity.this);
        menuView.setCancelButtonTitle("取 消");// before add items
        menuView.addItems("拍 照", "从相册选取");
        menuView.setItemClickListener(EnterpriseCheckActivity.this);
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
     * 提交注册商家资料
     */
    // private void commitInfo(AjaxParams params) {
    //
    // requestNet(new Handler() {
    // @Override
    // public void handleMessage(Message msg) {
    // super.handleMessage(msg);
    // switch (msg.what) {
    // case Constant.NET_DATA_SUCCESS:
    // ToastUtil.toastShow(EnterpriseCheckActivity.this, "提交成功");
    // finishActivity();
    // break;
    //
    // default:
    // break;
    // }
    // }
    // }, params, NetworkUtil.BUSINESS_ENTER, false, 0);
    // }

    /**
     * 获取当前定位地区的省份及城市id
     */
    private void getCityIdAndDisId() {
        if (CacheTools.getUserData("citysData") != null) {
            mCityNames = CityName.parses(CacheTools.getUserData("citysData")
                    .toString());
            // 通过比较查询城市id 必须要求服务器的本地数据与定位的数据的一致
            for (CityName city : mCityNames) {
                if (mCityName.equals(city.getCityName())) {
                    mCityId = String.valueOf(city.getId());
                    mProvinceId = String.valueOf(city.getProvinceId());
                }
            }
            getDistrictId(mCityId);
        }
    }

    /**
     * 通过城市id查询地区数据从而获得地区id
     *
     * @param cityId
     */
    private void getDistrictId(String cityId) {

        AjaxParams params = new AjaxParams();
        params.put("cityId", cityId);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            mDistrictNames = DistrictName.parses(jo.getJSONArray(
                                    "districts").toString());
                            for (DistrictName district : mDistrictNames) {
                                if (mDistrictName.equals(district.getDistrictName())) {
                                    mDistrictId = String.valueOf(district.getId());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.GET_DISTRICT_BY_CITY, false, 1);

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
       if(id == 0){
           image.setImageResource(R.drawable.business_portrait);
       }else if (id == 1) {
            image.setImageResource(R.drawable.merchantphotoer);
        } else if (id == 2) {
            image.setImageResource(R.drawable.merchantphotobacker);
        } else if (id == 3) {
            image.setImageResource(R.drawable.reality_portraiter);
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
