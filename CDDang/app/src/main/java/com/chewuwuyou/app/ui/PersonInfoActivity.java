package com.chewuwuyou.app.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarModel;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.NewFriend;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.bean.TuItem;
import com.chewuwuyou.app.utils.Bimp;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.ImageItem;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.TokenObtain;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;
import com.chewuwuyou.app.widget.MyGridView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * @version 1.1.0
 * @describe:我的信息详情
 * @author:yuyong
 * @created:2015-1-13上午11:42:14
 */
public class PersonInfoActivity extends CDDBaseActivity implements OnClickListener, MenuItemClickListener {

    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private MyGridView mUserPicPullGV;
    private TextView mUserMoodET;// 车友心情
    private TextView mNickNameTV;// 昵称
    private TextView mUserSexTV;// 性别
    private TextView mUserWorkTV;// 职业
    private TextView mUserAddressTV;// 家乡
    private RelativeLayout mTitleHeight;// 标题布局高度
    private TextView mUserLoveCarTV;// 爱车
    private ImageView mUserLoveCarIV;// 爱车图片
    private TextView mUserHobbiesTV;// 兴趣爱好
    private TextView mUserAgeTV;// 年龄
    private TextView mUserStarSeatTV;// 星座

    private LinearLayout mUserNickLL;// 修改昵称
    private LinearLayout mUpdateUserSexLL;// 修改性别
    private LinearLayout mUserWorkLL;// 选择从事职业
    private LinearLayout mUserAddressLL;// 选择家乡所在地址
    private RelativeLayout mUserLoveCarBrandLL;// 选择爱车
    private LinearLayout mUserAgeLL;// 选择年龄
    private LinearLayout mUserStarSeatLL;// 选择星座
    private LinearLayout hubbiesLL;
    private ImageView my_portrait;// 头像
    private LinearLayout moodLL;

    // 为显示车型图片
    private List<CarModel> mCarModels;
    private String mCarBrand;
    private String mCarModel;
    /**
     * 车友信息实体
     */
    private PersonHome mPersonHome;
    private GridAdapter mGridAdapter;
    private RelativeLayout topRl;// 头部
    private ImageView mPersonalPicBgIV;// 个性墙北京
    private TextView mUserPicBgIV;// 修改个性墙背景图
    private List<TuItem> mPicList;// 存储照片墙的所有图片,使用bitmap来进行默认存储新增

    private ImageLoader mImageLoader;
    private ArrayList<ImageItem> mNeedToQueryNetTus = new ArrayList<ImageItem>();// 提交的Img
    private ImageItem imageItem;//当前操作的实体
    private static boolean isMoveMode = false;
    private ImageItem icon;
    private Animation shakeAnim;
    private String portriat_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_edit_ac);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        my_portrait = (ImageView) findViewById(R.id.my_portrait);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mUserMoodET = (TextView) findViewById(R.id.car_friend_mood_et);
        mNickNameTV = (TextView) findViewById(R.id.user_nickname_tv);
        mUserSexTV = (TextView) findViewById(R.id.user_sex_tv);
        mUserWorkTV = (TextView) findViewById(R.id.user_work_tv);
        mUserAddressTV = (TextView) findViewById(R.id.user_address_tv);
        mUserLoveCarTV = (TextView) findViewById(R.id.user_love_car_tv);
        mUserHobbiesTV = (TextView) findViewById(R.id.user_hobbies_tv);
        mUserNickLL = (LinearLayout) findViewById(R.id.user_nick_ll);
        topRl = (RelativeLayout) findViewById(R.id.rl);
        mUpdateUserSexLL = (LinearLayout) findViewById(R.id.user_sex_ll);
        mUserWorkLL = (LinearLayout) findViewById(R.id.user_work_ll);
        mUserAddressLL = (LinearLayout) findViewById(R.id.user_address_ll);
        mUserLoveCarBrandLL = (RelativeLayout) findViewById(R.id.user_love_car_brand_rl);
        mUserLoveCarIV = (ImageView) findViewById(R.id.user_love_car_iv);
        mPersonalPicBgIV = (ImageView) findViewById(R.id.personal_pic_bg_iv);
        mUserPicBgIV = (TextView) findViewById(R.id.user_pic_bg_iv);
        mUserAgeTV = (TextView) findViewById(R.id.user_age_tv);
        mUserStarSeatTV = (TextView) findViewById(R.id.user_star_seat_tv);
        mUserAgeLL = (LinearLayout) findViewById(R.id.user_age_ll);
        mUserStarSeatLL = (LinearLayout) findViewById(R.id.user_star_seat_ll);
        mUserPicPullGV = (MyGridView) findViewById(R.id.person_pic_pull);
        hubbiesLL = (LinearLayout) findViewById(R.id.hubbiesLL);
        moodLL = (LinearLayout) findViewById(R.id.moodLL);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        int height = (50 * screenW) / 75;
        LayoutParams params = (LayoutParams) topRl.getLayoutParams();
        params.height = height;
        topRl.setLayoutParams(params);

        mTitleTV.setFocusable(true);
        mTitleTV.setFocusableInTouchMode(true);
        mTitleTV.requestFocus();

        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.cycle_anim);
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        mTitleTV.setText("编辑我的信息");
        mTuWidth = (mOutMetrics.widthPixels - 2 * getResources().getDimensionPixelSize(R.dimen.quan_padding)
                - 3 * (10 + getResources().getDimensionPixelSize(R.dimen.quan_tu_interval)) - 10) / 4;
        mTuWidth = (mOutMetrics.widthPixels - getResources().getDimensionPixelSize(R.dimen.yue_photo_ll_padding) * 2
                - (10 + getResources().getDimensionPixelSize(R.dimen.yue_photo_padding)) * 3 - 10) / 4;
        mCarModels = CarModel.parseBrands(getFromAssets("mobile_models"));
        mPersonHome = (PersonHome) getIntent().getSerializableExtra(Constant.PERSONINFO_SER);
        updateUI();
    }

    /**
     * 刷新UI
     */
    private void updateUI() {
        if (!TextUtils.isEmpty(mPersonHome.getCarBrand()) && !mPersonHome.getCarBrand().equals("未设置")) {
            mCarBrand = mPersonHome.getCarBrand().split("/")[0];

            if (mPersonHome.getCarBrand().contains("/")) {
                mCarModel = mPersonHome.getCarBrand().split("/")[1];
            } else {
                mCarModel = mPersonHome.getCarBrand();
            }
            // 设置车型图片
            setImage(mUserLoveCarIV, getVehicleImage(mCarBrand, mCarModel, mCarModels));
        }
        if (!TextUtils.isEmpty(mPersonHome.getImageBack())) {
            ImageUtils.displayImage(mPersonHome.getImageBack(), mPersonalPicBgIV, 0, mOutMetrics.widthPixels,
                    mOutMetrics.widthPixels, ScalingLogic.CROP);
        } else {
            mPersonalPicBgIV.setImageResource(R.drawable.cddbg1);
        }
        if (mPersonHome.getUrls().size() > 9) {
            mPicList = mPersonHome.getUrls().subList(0, 9);
        } else {
            mPicList = mPersonHome.getUrls();
        }
        if (mPicList == null)
            mPicList = new ArrayList<TuItem>();
        mNeedToQueryNetTus.clear();
        for (int i = 0; i < mPicList.size(); i++) {
            if (i != 0)
                mNeedToQueryNetTus.add(new ImageItem(mPicList.get(i).getUrl(), "" + mPicList.get(i).getId()));
        }
        if (mPicList != null && mPicList.size() > 0) {
            icon = new ImageItem(mPicList.get(0).getUrl(), "" + mPicList.get(0).getId());
            CacheTools.setUserData("url", ServerUtils.getServerIP(icon.getImagePath()));
            ImageUtils.displayUserHeadImage(icon.getImagePath(), my_portrait, 8);
        }
        if (mGridAdapter == null)
            mGridAdapter = new GridAdapter(PersonInfoActivity.this);
        mUserPicPullGV.setAdapter(mGridAdapter);
        if (!TextUtils.isEmpty(mPersonHome.getSign())) {
            mUserMoodET.setText(mPersonHome.getSign());
        } else {
            mUserMoodET.setHint("...");
        }
        mNickNameTV.setText(mPersonHome.getNick());
        if (mPersonHome.getSex().equals("0")) {
            mUserSexTV.setText("男");
        } else if (mPersonHome.getSex().equals("1")) {
            mUserSexTV.setText("女");
        } else {
            mUserSexTV.setText("不详");
        }
        mUserWorkTV.setText(mPersonHome.getPro());
        mUserAddressTV.setText(mPersonHome.getLocation());
        mUserLoveCarTV.setText(mPersonHome.getCarBrand());
        mUserHobbiesTV.setText(mPersonHome.getHobby());
        mUserAgeTV.setText(mPersonHome.getAge());
        mUserStarSeatTV.setText(mPersonHome.getStarsit());
        mUserLoveCarIV.setVisibility(View.GONE);
    }

    /**
     * 获取车友信息数据
     */
    private void getDate() {
        showWaitingDialog();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 3000);
    }

    private void getDateTimer() {
        AjaxParams params = new AjaxParams();
        params.put("ids", String.valueOf(mPersonHome.getUserId()));
        requestNet(new Handler(Looper.getMainLooper()) {
                       @Override
                       public void handleMessage(Message msg) {
                           super.handleMessage(msg);
                           dismissWaitingDialog();
                           switch (msg.what) {
                               case Constant.NET_DATA_SUCCESS:
                                   try {
                                       JSONArray jArray = new JSONArray(msg.obj.toString());
//                                       if (TextUtils.isEmpty(jArray.getJSONObject(0).getString("DDNumber")))
//                                           return;
                                       mPersonHome = PersonHome.parse(jArray.get(0).toString());

                                       //保存到本地头像，聊天的时候 要用。
                                       CacheTools.setUserData("rongPortraitUri",ServerUtils.getServerIP(mPersonHome.getUrls().get(0).getUrl()));

                                       uploadImage(mPersonHome.getUrls().get(0).getUrl());


                                       updateUI();
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                                   break;
                           }
                       }
                   }
                , params, NetworkUtil.GET_USER_INFO, false, 1);
        Intent intent = new Intent();
        intent.setAction("com.chewuwuyou.app.ui.personinfo");
        sendBroadcast(intent);// 发送 一个无序广播
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                getDateTimer();
            }
            super.handleMessage(msg);
        }
    };
    Timer timer = new Timer();

    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        mUserNickLL.setOnClickListener(this);
        mUpdateUserSexLL.setOnClickListener(this);
        mUserWorkLL.setOnClickListener(this);
        mUserAddressLL.setOnClickListener(this);
        mUserLoveCarBrandLL.setOnClickListener(this);
        mUserPicBgIV.setOnClickListener(this);
        mUserAgeLL.setOnClickListener(this);
        mUserStarSeatLL.setOnClickListener(this);
        hubbiesLL.setOnClickListener(this);
        moodLL.setOnClickListener(this);
        my_portrait.setOnClickListener(this);

        mGridAdapter.setOnTouchListener(new TouchListener() {
            @Override
            public boolean touch(final View v, final MotionEvent event, int position) {
                // 获取当前点的xy位置
                final int currentX = (int) event.getRawX();
                final int currentY = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) event.getRawX();
                        downY = (int) event.getRawY();
                        my_portrait_w = my_portrait.getWidth();
                        my_portrait_h = my_portrait.getHeight();
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        if (windowManager == null)
                            windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                        timerTouch = new Timer();
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!isMoveMode) {
                                            isMoveMode = true;
                                            if (imageView == null)
                                                imageView = new ImageView(PersonInfoActivity.this);
                                            imageView.setImageDrawable(((ImageView) v).getDrawable());
                                            imageView.setBackgroundDrawable(v.getBackground());
                                            setWindowParams(currentX - my_portrait_w / 2, currentY - my_portrait_h / 2, my_portrait_w, my_portrait_h);
                                            imageView.startAnimation(shakeAnim);
                                            my_portrait.startAnimation(shakeAnim);
                                            windowManager.addView(imageView, windowParams);
                                            v.getParent().requestDisallowInterceptTouchEvent(true);
                                        } else {
                                            v.getParent().requestDisallowInterceptTouchEvent(false);
                                        }
                                    }
                                });
                            }
                        };
                        timerTouch.schedule(task, 500);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isMoveMode) {
                            windowParams.x = currentX - my_portrait_w / 2;
                            windowParams.y = currentY - my_portrait_h / 2;
                            windowManager.updateViewLayout(imageView, windowParams);
                        } else if (Math.abs(downX - event.getRawX()) > TOUCH_SLOP
                                || Math.abs(downY - event.getRawY()) > TOUCH_SLOP) {
                            timerTouch.cancel();
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isMoveMode) {
                            isMoveMode = false;
                            windowManager.removeView(imageView);
                            String imgId = ((ImageItem) mGridAdapter.getItem(position)).getImageId();
                            if (icon != null && icon.getImageId() != null && inRangeOfView(my_portrait, event) && !icon.getImageId().equals(imgId)) {
                                changeImageSort(icon.getImageId(), imgId, ((ImageView) v).getDrawable());
                            }
                        } else
                            timerTouch.cancel();
                        break;
                }
                return false;
            }
        });
    }

    private Timer timerTouch;
    private TimerTask task;
    private int my_portrait_w;
    private int my_portrait_h;
    private int downX;
    private int downY;
    //移动的阈值
    private static final int TOUCH_SLOP = 20;

    private void changeImageSort(String firstId, String changeId, final Drawable drawable) {
        AjaxParams params = new AjaxParams();
        params.put("firstId", firstId);
        params.put("changeId", changeId);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        my_portrait.setImageDrawable(drawable);
                        getDate();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(PersonInfoActivity.this, "操作失败");
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.CHANGE_IMAGE_SORT, false, 0);


    }

    private void showSelectDialog(int positon) {
        String[] strs;
        if (mNeedToQueryNetTus.size() < 8 && mGridAdapter.getCount() - 1 == positon) {// 添加
            PIC_STATE = ADD_PIC;
            strs = new String[]{"拍 照", "从相册选取"};
        } else {// 替换
            PIC_STATE = UPDATE_PIC;
            imageItem = (ImageItem) mGridAdapter.getItem(positon);
            strs = new String[]{"拍 照", "从相册选取", "删 除"};
        }
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(PersonInfoActivity.this);
        menuView.setCancelButtonTitle("取 消");// before add items
        menuView.addItems(strs);
        menuView.setItemClickListener(this);
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
    }


    private ImageView imageView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;

    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y || ev.getRawY() > (y + view.getHeight()))
            return false;
        return true;
    }

    private void setWindowParams(int x, int y, int w, int h) {
        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP | Gravity.LEFT;
        windowParams.x = x;
        windowParams.y = y;
        windowParams.width = w;
        windowParams.height = h;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = R.style.anim_view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.user_sex_ll:
                Intent sIntent = new Intent(PersonInfoActivity.this, PersonalDataSexActivity.class);
                startActivityForResult(sIntent, 15);
                break;
            case R.id.user_work_ll:
                Intent useWorkIntent = new Intent(PersonInfoActivity.this, ChooseProfessionActivity.class);
                startActivityForResult(useWorkIntent, 20);
                break;
            case R.id.user_love_car_brand_rl:
                StatService.onEvent(PersonInfoActivity.this, "clickChooseVhicelBrand", "修改个人信息点击选择车品牌");
                Intent intent = new Intent(PersonInfoActivity.this, ChooseCarBrandActivity.class);
                startActivityForResult(intent, 20);
                break;
            case R.id.user_address_ll:
                Intent chooseCityIntent = new Intent(PersonInfoActivity.this, AreaSelectActivity.class);
                startActivityForResult(chooseCityIntent, 2);
                break;
            case R.id.user_nick_ll:
                DialogUtil.updateNameDialog(PersonInfoActivity.this, mNickNameTV.getText().toString(), mNickNameTV, 1);
                break;
            case R.id.user_pic_bg_iv:
                PIC_STATE = BG_PIC;
                setTheme(R.style.ActionSheetStyleIOS7);
                ActionSheet menuView = new ActionSheet(PersonInfoActivity.this);
                menuView.setCancelButtonTitle("取 消");// before add items
                menuView.addItems("拍 照", "从相册选取");
                menuView.setItemClickListener(PersonInfoActivity.this);
                menuView.setCancelableOnTouchMenuOutside(true);
                menuView.showMenu();
                break;
            case R.id.user_age_ll:
                chooseTimeDialog();
                break;
            case R.id.user_star_seat_ll:

                break;
            case R.id.hubbiesLL:// 兴趣爱好user_hobbies_tv
                DialogUtil.updateHappiesDialog(this, mUserHobbiesTV);
                break;
            case R.id.moodLL:// 心情car_friend_mood_et
                DialogUtil.updateMoodDialog(this, mUserMoodET);
                break;
            case R.id.my_portrait:
                PIC_STATE = UPDATE_ICON;
                setTheme(R.style.ActionSheetStyleIOS7);
                ActionSheet menuView1 = new ActionSheet(PersonInfoActivity.this);
                menuView1.setCancelButtonTitle("取 消");// before add items
                menuView1.addItems("拍 照", "从相册选取");
                menuView1.setItemClickListener(PersonInfoActivity.this);
                menuView1.setCancelableOnTouchMenuOutside(true);
                menuView1.showMenu();
                break;
        }

    }

    private void chooseTimeDialog() {
        final Calendar c;
        Dialog dialog = null;
        c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        final int nowYear = c.get(Calendar.YEAR);
        dialog = new DatePickerDialog(PersonInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                if (year > nowYear) {
                    mUserAgeTV.setText("0");
                } else {
                    mUserAgeTV.setText(String.valueOf(nowYear - year));
                }
                mUserStarSeatTV.setText(Tools.getStarSeat(month + 1, dayOfMonth));
                AjaxParams params = new AjaxParams();
                params.put("age", mUserAgeTV.getText().toString());
                params.put("starsit", mUserStarSeatTV.getText().toString());
                updateInfo(params);//修改星座和年龄
            }
        }, c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH)); // 传入天数
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                if (ImageUtils.tempFile != null) {
                    // 对图片进行裁剪
                    ImageUtils.cropImageUri3(ImageUtils.tempFile, PersonInfoActivity.this, 540, 540);
                }
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                if (data != null && data.getData() != null) {
                    // 对图片进行裁剪
                    ImageUtils.cropImageUri3(data.getData(), PersonInfoActivity.this, 540, 540);
                }
                break;

            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:// 返回的结
                if (data == null) return;
                switch (PIC_STATE) {
                    case ADD_PIC:
                        imageItem = new ImageItem();
                        imageItem.setImagePath(ImageUtils.cropImagePath);
                        upLodeImg(imageItem);
                        break;
                    case UPDATE_PIC:
                        imageItem.setImagePath(ImageUtils.cropImagePath);
                        upLodeImg(imageItem);
                        break;
                    case BG_PIC:
                        uploadBgImg(new File(ImageUtils.cropImagePath));
                        break;
                    case UPDATE_ICON:
                        icon.setImagePath(ImageUtils.cropImagePath);
                        upLodeImg(icon);
                        break;
                }
                break;
            case 20:
                if (resultCode == RESULT_OK) {
                    mCarBrand = data.getStringExtra("brandName");
                    mCarModel = data.getStringExtra("name");
                    AjaxParams params = new AjaxParams();
                    params.put("carBrand", mCarBrand + "/" + mCarModel);
                    updateInfo(params);
                    mUserLoveCarTV.setText(mCarBrand + "/" + mCarModel);
                    setImage(mUserLoveCarIV, getVehicleImage(mCarBrand, mCarModel, mCarModels));
                }
                break;
        }
        if (Constant.Vehicle.CHOICE_MODEL == resultCode) {
            mCarBrand = data.getStringExtra("brand");
            mCarModel = data.getStringExtra("model");
            AjaxParams params = new AjaxParams();
            params.put("carBrand", mCarBrand + "/" + mCarModel);
            updateInfo(params);
            mUserLoveCarTV.setText(mCarBrand + "/" + mCarModel);
            setImage(mUserLoveCarIV, getVehicleImage(mCarBrand, mCarModel, mCarModels));

        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data.getStringExtra("province").toString().equals(data.getStringExtra("city").toString())) {
                mUserAddressTV.setText(data.getStringExtra("province"));
            } else {
                mUserAddressTV.setText(data.getStringExtra("province") + data.getStringExtra("city"));
            }
            AjaxParams params = new AjaxParams();
            params.put("location", mUserAddressTV.getText().toString().trim());
            updateInfo(params);
        }
        if (requestCode == 15 && 30 == resultCode) {
            int choicesex = data.getExtras().getInt("choicesex");
            mUserSexTV.setText(choicesex == 0 ? "男" : "女");
            AjaxParams params = new AjaxParams();
            params.put("sex", choicesex + "");
            updateInfo(params);
        }
        if (55 == resultCode) {
            mUserWorkTV.setText(data.getStringExtra("profession"));
            AjaxParams params = new AjaxParams();
            params.put("pro", mUserWorkTV.getText().toString());
            updateInfo(params);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();
    }

    @Override
    public void onItemClick(int itemPosition) {
        switch (itemPosition) {
            case 0:
                Acp.getInstance(PersonInfoActivity.this).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.CAMERA)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                ImageUtils.takePhoto(PersonInfoActivity.this);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtil.toastShow(PersonInfoActivity.this, permissions.toString() + "权限拒绝");
                            }
                        });

                break;
            case 1:
                Acp.getInstance(PersonInfoActivity.this).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
                                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtil.toastShow(PersonInfoActivity.this, permissions.toString() + "权限拒绝");
                            }
                        });

                break;
            case 2:
                PIC_STATE = DELETE_PIC;
                mGridAdapter.deleteImg(imageItem);
                break;
            default:
                break;
        }
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            if (mNeedToQueryNetTus.size() >= 8) {
                return 8;
            }
            return (mNeedToQueryNetTus.size() + 1);
        }

        public Object getItem(int arg0) {
            return mNeedToQueryNetTus.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.capture_head_item, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.capture_item_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (mNeedToQueryNetTus.size() == position) {
                holder.image.setImageResource(R.drawable.btn_add_pic);
            } else {
                final ImageItem imgItem = mNeedToQueryNetTus.get(position);
                ImageUtils.displayImage(imgItem.imagePath, holder.image, 8, R.drawable.image_default,
                        R.drawable.image_default);
                if (touch != null && mNeedToQueryNetTus.size() != position)
                    holder.image.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return touch.touch(v, event, position);
                        }
                    });
            }
            holder.image.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    showSelectDialog(position);
                }
            });
            return convertView;
        }

        public void deleteImg(ImageItem imgItem) {
            if (getCount() <= 2) {
                ToastUtil.toastShow(PersonInfoActivity.this, "至少应有一张头像");
                return;
            }
            upLodeImg(imgItem);
        }


        public class ViewHolder {
            public ImageView image;
        }

        private TouchListener touch;

        public void setOnTouchListener(TouchListener touch) {
            this.touch = touch;
        }
    }

    interface TouchListener {
        boolean touch(View v, MotionEvent event, int position);
    }

    private static final int ADD_PIC = 1;//新增
    private static final int UPDATE_PIC = 2;//更新
    private static final int DELETE_PIC = 3;//删除
    private static final int BG_PIC = 4;//背景图
    private static final int UPDATE_ICON = 5;//修改ICON

    private static int PIC_STATE = 0;//图片相关操作状态

    /**
     * 提交 头像
     *
     * @param imageItem
     */
    public void upLodeImg(ImageItem imageItem) {
        String requestNetUrl = null;
//        File file = null;
        AjaxParams params = new AjaxParams();
        switch (PIC_STATE) {
            case ADD_PIC:
                requestNetUrl = NetworkUtil.UPLOAD_PICTURE;// 新增
                //motify start by yuyong 修改为base64上传
//                    file = new File(imageItem.getImagePath());
//                    params.put("file", file);
                params.put("file", "");
                params.put("imgStr", FileUtils.Bitmap2StrByBase64(imageItem.getBitmap()));
                break;
            case UPDATE_PIC:
                params.put("updateId", imageItem.getImageId());
                requestNetUrl = NetworkUtil.UPLOAD_PICTURE;// 修改
//                    file = new File(imageItem.getImagePath());
//                    params.put("file", file);
                params.put("file", "");
                params.put("imgStr", FileUtils.Bitmap2StrByBase64(imageItem.getBitmap()));
                break;
            case DELETE_PIC:
                params.put("imageId", imageItem.getImageId());
                requestNetUrl = NetworkUtil.REMOVE_USER_PIC;// 刪除
                break;
            case UPDATE_ICON:
                params.put("updateId", imageItem.getImageId());
                requestNetUrl = NetworkUtil.UPLOAD_PICTURE;// 修改
//                    file = new File(imageItem.getImagePath());
//                    params.put("file", file);
                params.put("file", "");
                params.put("imgStr", FileUtils.Bitmap2StrByBase64(imageItem.getBitmap()));

                break;
            default:
                return;
        }
        if (requestNetUrl == null) {
            ToastUtil.toastShow(PersonInfoActivity.this, "图片上传失败");
            return;
        }
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        getDate();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(PersonInfoActivity.this, "图片上传失败");
                        break;
                    default:
                        break;
                }
            }
        }, params, requestNetUrl, false, 0);


    }


    /**
     * 上传图片至新的接口
     *
     * @param url
     */

    private void uploadImage(String url) {

        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("portraitUri", url);
        params.put("name", mNickNameTV.getText().toString());

        NetworkUtil.get(NetworkUtil.REFRESH_USER, params, new AjaxCallBack<String>() {
            @Override
            public boolean isProgress() {
                return super.isProgress();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                try {
                    JSONObject j = new JSONObject(s);
                    ErrorCodeUtil.doErrorCode(PersonInfoActivity.this, j.getInt("code"), j.getString("message"));

                    MyLog.i("已上传图片至新接口");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

            }
        });


    }

    /**
     * 提交背景图片
     *
     * @param file
     */
    private void uploadBgImg(File file) {
        AjaxParams params = new AjaxParams();
        try {
            params.put("file", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        getDate();
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(PersonInfoActivity.this, "上传背景墙失败！");
                        break;
                }
            }
        }, params, NetworkUtil.UPLOADE_PIC_BG, false, 0);
    }

    /**
     * 修改个人信息
     *
     * @param params
     */
    private void updateInfo(final AjaxParams params) {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Intent intent = new Intent();
                        intent.setAction("com.chewuwuyou.app.ui.personinfo");
                        sendBroadcast(intent);// 发送 一个无序广播
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(PersonInfoActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        ToastUtil.toastShow(PersonInfoActivity.this, "修改信息失败");
                        break;
                }
            }
        }, params, NetworkUtil.UPDATE_INFO_URL, false, 0);


    }


}
