package com.chewuwuyou.app.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

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
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.localImg;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 提交个人认证信息
 * @author yuyong
 *
 */
public class IDInfoActivity extends CDDBaseActivity implements OnClickListener,
        MenuItemClickListener {

    private EditText mRealNameET;// 真实姓名
    private EditText mIdCardNumET;// 身份证号码
    private EditText mAddressEt;// 详细地址

    private TextView mRemarkEt;// 请选择服务地区
    private String mProvinceName;
    private String mCityName;
    private String mDistrictName;
    private ImageView mIDCardBtn, mHeadBtn, mPortrait;// 身份证正面,身份证反面,头像
    private TextView mTitleTV;// 标题
    private ImageButton mBackIBtn;// 返回上一页
    private String mUpdaloadUrl;// 上传图片的地址
    private String mIdentityImageUrl;// 上传身份证的地址
    private String mHeadImageUrl;/* 头像图片 */
    private String mIdentityCard;// 身份证背面
    private String mHoldCertificates;// 身份证头像


    private Button mCommitVerBtn;
    private TextView mIdPortraitEnlarge, mIdIdentityEnlarge,
            mIdPersonalEnlarge;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private List<CityName> mCityNames;
    private String mProvinceId;
    private String mCityId;
    private String mDistrictId;
    private List<DistrictName> mDistrictNames;// 通过城市id查询地区的数据
    private ExamineBook response;
    private RelativeLayout mBusinessIdOther;
    private ImageView mEnterpriseHead;
    private String imageFilePath;
    private Uri imageFileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_info_ac);
        initView();// 初始化
        initData();// 逻辑处理
        initEvent();// 事件监听
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {

        mIdPortraitEnlarge = (TextView) findViewById(R.id.id_personal_img_enlarge);
        mIdIdentityEnlarge = (TextView) findViewById(R.id.id_identity_img_enlarge);
        mIdPersonalEnlarge = (TextView) findViewById(R.id.id_portrait_img_enlarge);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);


        mBusinessIdOther = (RelativeLayout) findViewById(R.id.business_id_other1);;
        mEnterpriseHead = (ImageView) findViewById(R.id.enterprise_head);;

        mRealNameET = (EditText) findViewById(R.id.real_name_et);
        mIdCardNumET = (EditText) findViewById(R.id.idcard_num_et);
        mAddressEt = (EditText) findViewById(R.id.address_et);

        mIDCardBtn = (ImageView) findViewById(R.id.id_card_btn);
        mHeadBtn = (ImageView) findViewById(R.id.head_btn);
        mPortrait = (ImageView) findViewById(R.id.portrait);
        mCommitVerBtn = (Button) findViewById(R.id.commit_ver_btn);

        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mTitleTV.setText("个人认证");
        mRemarkEt = (TextView) findViewById(R.id.service_et);// 请选择服务地区
        if(getIntent().getSerializableExtra("response")!=null){
            response = (ExamineBook) getIntent().getSerializableExtra("response");
            if(response.getIdentityType().equals("0")){

                mProvinceId = response.getProvinceId();
                mCityId = response.getCityId();
                mDistrictId = response.getDistrictId();
                mRealNameET.setText(response.getRealName());
                mIdCardNumET.setText(response.getIdentityNo());
                mAddressEt.setText(response.getAddress());
                mRemarkEt.setText(response.getProvinceName()+response.getCityName()+response.getDistrictName());

                ImageUtils.displayImage(response.getHeadImageUrl(),mEnterpriseHead, 10);
                ImageUtils.displayImage(response.getIdentityImageUrl(),mIDCardBtn, 10);
                ImageUtils.displayImage(response.getHandImageUrl(),mPortrait, 10);
                ImageUtils.displayImage(response.getIdentityBeiImageUrl(),mHeadBtn, 10);

                mIdentityImageUrl = response.getIdentityImageUrl();
                mHeadImageUrl =response.getHandImageUrl();
                mIdentityCard = response.getIdentityBeiImageUrl();
                mHoldCertificates =  response.getHeadImageUrl();
            }

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
        mRemarkEt.setOnClickListener(this);
        mCommitVerBtn.setOnClickListener(this);
        mBackIBtn.setOnClickListener(this);
        mIdPortraitEnlarge.setOnClickListener(this);
        mIdIdentityEnlarge.setOnClickListener(this);
        mIdPersonalEnlarge.setOnClickListener(this);
        mEnterpriseHead.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.business_id_other1://头像放大
                dialogId(0);
                break;
            case R.id.id_personal_img_enlarge:// 正面放大
                dialogId(1);
                break;
            case R.id.id_identity_img_enlarge:// 身份证反面放大
                dialogId(2);
                break;
            case R.id.id_portrait_img_enlarge:// 头像放大
                dialogId(3);
                break;
            case R.id.sub_header_bar_left_ibtn:// 返回上一页
                finishActivity();
                break;
            case R.id.id_card_btn:// 身份证正面
                mUpdaloadUrl = NetworkUtil.ID_CARD_IMG;
                openUploadMenu();
                break;
            case R.id.head_btn:// 身份证反面
                mUpdaloadUrl = NetworkUtil.ID_CARD_BACK;
                openUploadMenu();
                break;
            case R.id.portrait:// 身份证手持证件
                mUpdaloadUrl = NetworkUtil.HEAD_IMG;
                openUploadMenu();
                break;
            case R.id.enterprise_head:// 头像
                mUpdaloadUrl = NetworkUtil.HOLD_IMG;
                openUploadMenu();
                break;
            case R.id.service_et:// 详细地址
                Intent intent = new Intent(IDInfoActivity.this,
                        AreaSelectActivity.class);
                startActivityForResult(intent, 20);
                break;

            case R.id.commit_ver_btn:// 提交
                AjaxParams params = new AjaxParams();
                String name = mRealNameET.getText().toString().trim();// 真实姓名
                String CardNum = mIdCardNumET.getText().toString().trim();// 身份证
                String dress = mAddressEt.getText().toString().trim();// 详细地址
                try {
                    boolean  sfz = IDCardValidate.IDCardValidate(CardNum);//验证身份证是否正确
                    if(!sfz){
                        ToastUtil.toastShow(IDInfoActivity.this, "请输入正确的身份证号码");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.toastShow(IDInfoActivity.this, "请输入您的真实姓名");
                } else if (TextUtils.isEmpty(CardNum)||CardNum.length()<15) {
                    ToastUtil.toastShow(IDInfoActivity.this, "请输入正确的身份证号码");
                } else if (TextUtils.isEmpty(dress)) {
                    ToastUtil.toastShow(IDInfoActivity.this, "地址不能为空");
                } else if (TextUtils.isEmpty(mIdentityImageUrl)) {
                    ToastUtil.toastShow(IDInfoActivity.this, "身份证证件不能为空");
                } else if (TextUtils.isEmpty(mHeadImageUrl)) {
                    ToastUtil.toastShow(IDInfoActivity.this, "请上传你的头像");

                } else if (TextUtils.isEmpty(mIdentityCard)) {
                    ToastUtil.toastShow(IDInfoActivity.this, "请上传背面身份证");
                } else if (TextUtils.isEmpty(mRemarkEt.getText().toString())) {
                    ToastUtil.toastShow(IDInfoActivity.this, "请选择服务地区");
                }else if(TextUtils.isEmpty(mAddressEt.getText().toString())){
                    ToastUtil.toastShow(IDInfoActivity.this, "请输入详细地址");
                }else if (TextUtils.isEmpty(mProvinceId)
                        && TextUtils.isEmpty(mCityId)
                        && TextUtils.isEmpty(mDistrictId)) {
                    ToastUtil.toastShow(IDInfoActivity.this, "请选择服务地区");
                }else if(TextUtils.isEmpty(mHoldCertificates)){
                    ToastUtil.toastShow(IDInfoActivity.this, "请上传手持证件照");
                } else {
                    params.put("identityType", "0");// 个人
                    params.put("catetory", "5");// B类商家
                    params.put("realName", name);// 名称
                    params.put("identityNo", CardNum);// 身份证号
                    params.put("identityImageUrl", mIdentityImageUrl);// 身份证正面
                    params.put("headImageUrl", mHeadImageUrl);// 头像
                    params.put("handImageUrl", mHoldCertificates);//头像
                    params.put("identityBeiImageUrl", mIdentityCard);// 身份证背面
                    params.put("address", dress);// 地区
                    params.put("provinceId", mProvinceId);
                    params.put("cityId", mCityId);
                    params.put("districtId", mDistrictId);

                    if(response!=null){
                        params.put("updateId", response.getUserId());
                    }
                    commitInfo(params);
                }
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:// 拍照

                String url = Environment.getExternalStorageDirectory()+"/image.jpg";

                File dirFile = new File(url);
                if(!dirFile.exists()){//判断目录下的图片是否存在，不存在给出提示
                    ToastUtil.toastShow(IDInfoActivity.this,"请拍照");
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
                    uploadFileBase64(FileUtils.bitmapToString(url), mUpdaloadUrl);
                    deleteFile(url);//删除根目录下的图片
                }
                break;
            case 2:// 相册
                if(data!=null) {
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    uploadFileBase64(FileUtils.bitmapToString(paths.get(0)), mUpdaloadUrl);
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
                break;
            case 20:
                if (resultCode == RESULT_OK) {// 地区

                    mDistrictId = data.getStringExtra("districtId");
                    mCityId = data.getStringExtra("cityId");
                    mProvinceId = data.getStringExtra("provinceId");

                    mCityName = data.getStringExtra("city");
                    mProvinceName = data.getStringExtra("province");
                    mDistrictName = data.getStringExtra("district");

                    if (data.getStringExtra("cityId") == null) {
                        getCityIdAndDisId();
                    }

                    if (TextUtils.isEmpty(mDistrictName)) {
                        if (mProvinceName.equals(mCityName)) {
                            mRemarkEt.setText(mCityName);
                        } else {
                            mRemarkEt.setText(mProvinceName + mCityName);
                        }
                    } else {
                        if (mProvinceName.equals(mCityName)
                                && mCityName.equals(mDistrictName)) {
                            mRemarkEt.setText(mDistrictName);
                        } else if (mCityName.equals(mProvinceName)
                                && !mCityName.equals(mDistrictName)) {
                            mRemarkEt.setText(mCityName + mDistrictName);
                        } else if (mCityName.equals(mDistrictName)
                                && !mProvinceName.equals(mDistrictName)) {
                            mRemarkEt.setText(mDistrictName);
                        } else {
                            mRemarkEt.setText(mProvinceName + mCityName
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
    private void uploadFileBase64(String file, String uploadUrl) {
        AjaxParams params = new AjaxParams();
        params.put("imgStr", file);
        params.put("file", "");
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        ToastUtil.toastShow(IDInfoActivity.this, "上传成功!");
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            if (mUpdaloadUrl == NetworkUtil.ID_CARD_IMG) {
                                mIdentityImageUrl = jo.getString("url");

                            } else if (mUpdaloadUrl == NetworkUtil.HEAD_IMG) {
                                mHeadImageUrl = jo.getString("url");

                            } else if (mUpdaloadUrl == NetworkUtil.ID_CARD_BACK) {
                                mIdentityCard = jo.getString("url");

                            }else if(mUpdaloadUrl == NetworkUtil.HOLD_IMG){
                                mHoldCertificates = jo.getString("url");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        MyLog.i("YUY", "上传图片失败 = " + msg.obj.toString());
                        ToastUtil.toastShow(IDInfoActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        ToastUtil.toastShow(IDInfoActivity.this,"网络开小差了,请重新上传");
                        break;
                }
            }
        }, params, uploadUrl, false, 0);
    }

    private void openUploadMenu() {
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(IDInfoActivity.this);
        menuView.setCancelButtonTitle("取 消");// before add items
        menuView.addItems("拍 照", "从相册选取");
        menuView.setItemClickListener(IDInfoActivity.this);
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
                intent=new Intent(IDInfoActivity.this, MultiImageSelectorActivity.class);
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
    private void commitInfo(AjaxParams params) {

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        ToastUtil.toastShow(IDInfoActivity.this, "提交成功,等待审核");
                        IDVerificationActivity.idVerificationActivity.finishActivity();
                        finishActivity();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(IDInfoActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
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
