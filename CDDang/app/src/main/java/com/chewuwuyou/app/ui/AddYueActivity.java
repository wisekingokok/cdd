package com.chewuwuyou.app.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.callback.FilterEnterActionListener;
import com.chewuwuyou.app.utils.Bimp;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.ImageItem;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * @version 1.1.0
 * @describe:车友邦新活动发布
 * @author:yuyong
 * @created:2015-3-9上午10:28:13
 */
public class AddYueActivity extends BaseActivity implements
        View.OnClickListener {
    public static Bitmap bimap;
    private GridAdapter mAdapter;
    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    private ImageButton mHeaderBackIBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderBarTV;
    @ViewInject(id = R.id.sub_header_bar_right_tv)
    private TextView mHeaderBarRightTV;

    @ViewInject(id = R.id.yue_type_btn)
    private Button mYueTypeBtn;

    @ViewInject(id = R.id.yue_photo_tv)
    private TextView mYuePhotoTV;// 提醒还有几张图片

    @ViewInject(id = R.id.yue_limiting_rl)
    private RelativeLayout mYueLimitingRL;

    @ViewInject(id = R.id.yue_limiting_tv)
    private TextView mYueLimitingTV;

    @ViewInject(id = R.id.yue_participation_way_rl)
    private RelativeLayout mYueParticpationWayRL;

    @ViewInject(id = R.id.yue_participation_way_tv)
    private TextView mYueParticpationWayTV;

    @ViewInject(id = R.id.yue_available_date_rl)
    private RelativeLayout mYueAvailableDateRL;

    @ViewInject(id = R.id.yue_available_date_tv)
    private TextView mYueAvailableDateTV;

    @ViewInject(id = R.id.yue_address_rl)
    private RelativeLayout mYueAddressRL;

    @ViewInject(id = R.id.yue_address_tv)
    private TextView mYueAddressTV;

    @ViewInject(id = R.id.yue_content_et)
    private EditText mYueContentET;
    @ViewInject(id = R.id.yue_title_et)
    private EditText mYueTitleET;
    private PopupWindow mYueTypePopWindow = null;
    private View mYueTypePopView;
    private GridView mYuePhotoGV;
    private Button mPetTypeBtn;
    private Button mTravellTypeBtn;
    private Button mDinnerTypeBtn;
    private Button mFilmTypeBtn;
    private Button mShoppingTypeBtn;
    private Button mBarTypeBtn;
    private Button mMahjongTypeBtn;
    private Button mSportsTypeBtn;

    private PopupWindow mYueLimitPopWindow = null;
    private View mYueLimitPopView;
    private Button mOnlyMaleBtn;// 仅限男生
    private Button mOnlyFemaleBtn;// 仅限女生
    private Button mNoLimitBtn;// 男女不限

    private PopupWindow mYueParticipationWayPopWindow = null;
    private View mYueParticipationWayView;
    private Button mFreeloadBtn;// 求请客
    private Button mAABtn;// AA制度
    private Button mMyTreatBtn;// 我请客
    private String mChargeType;
    private String mType;
    private String mSex;
    // private String mRegion;
    private int mTuPosition = 0;
    private View mParentView;
    WindowManager mWindowManager;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private int mColumWidth;
    private static final int REQUEST_IMAGE = 22;
    private static final int REQUEST_ADDRESS = 23;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    if (msg.obj != null) {
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());

                            int yueId = jo.getInt("id");
                            if (yueId > 0) {
                                upLodeImgByBase64(yueId, Bimp.tempSelectBitmap, 0);//修改为Base64上传图片
                            } else {
                                dismissWaitingDialog();
                                mHeaderBarRightTV.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            dismissWaitingDialog();
                            mHeaderBarRightTV.setEnabled(true);
                            e.printStackTrace();
                        }
                    } else {
                        dismissWaitingDialog();
                        mHeaderBarRightTV.setEnabled(true);
                    }
                    //finishActivity();
                    break;
                case Constant.NET_DATA_FAIL:
                    dismissWaitingDialog();
                    ToastUtil.toastShow(AddYueActivity.this,
                            ((DataError) msg.obj).getErrorMessage());
                    mHeaderBarRightTV.setEnabled(true);
                    break;
                default:
                    ToastUtil.toastShow(AddYueActivity.this, "网络请求超时，换一换网络吧！");
                    dismissWaitingDialog();
                    mHeaderBarRightTV.setEnabled(true);
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bimap = ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.btn_addphoto));

        mParentView = getLayoutInflater().inflate(R.layout.add_yue_ac, null);
        setContentView(mParentView);
        // 参数没有用到 注释
        // mRegion = getIntent().getStringExtra("add_yue_region");
        mWindowManager = this.getWindowManager();
        initView();

        initEvent();
        city = getIntent().getStringExtra("city");
        city = CacheTools.getUserData("city");
    }

    private void initView() {
        mHeaderBarTV.setText(R.string.new_yue_title);
        mHeaderBarRightTV.setText(R.string.yue_publish);
        mHeaderBarRightTV.setVisibility(View.VISIBLE);

        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断

        mYueTypePopView = getLayoutInflater().inflate(
                R.layout.choose_yue_type_pop_window, null);
        this.mYueTypePopWindow = new PopupWindow(mYueTypePopView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.mYueTypePopWindow.setFocusable(true);
        this.mYueTypePopWindow.setOutsideTouchable(true);
        mYueTypePopWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.mPetTypeBtn = (Button) mYueTypePopView
                .findViewById(R.id.pet_type_btn);
        this.mPetTypeBtn.setOnClickListener(this);
        this.mTravellTypeBtn = (Button) mYueTypePopView
                .findViewById(R.id.travell_type_btn);
        this.mTravellTypeBtn.setOnClickListener(this);
        this.mDinnerTypeBtn = (Button) mYueTypePopView
                .findViewById(R.id.dinner_type_btn);
        this.mDinnerTypeBtn.setOnClickListener(this);
        this.mFilmTypeBtn = (Button) mYueTypePopView
                .findViewById(R.id.movie_type_btn);
        this.mFilmTypeBtn.setOnClickListener(this);
        this.mShoppingTypeBtn = (Button) mYueTypePopView
                .findViewById(R.id.shopping_type_btn);
        this.mShoppingTypeBtn.setOnClickListener(this);
        this.mBarTypeBtn = (Button) mYueTypePopView
                .findViewById(R.id.bar_type_btn);
        this.mBarTypeBtn.setOnClickListener(this);
        this.mMahjongTypeBtn = (Button) mYueTypePopView
                .findViewById(R.id.mahjong_type_btn);
        this.mMahjongTypeBtn.setOnClickListener(this);
        this.mSportsTypeBtn = (Button) mYueTypePopView
                .findViewById(R.id.sports_type_btn);
        this.mSportsTypeBtn.setOnClickListener(this);
        mYueTypeBtn
                .setText(getResources().getString(R.string.yue_travell_type));
        mType = Constant.YueYueType.TRAVEL.toString();

        mYueLimitPopView = getLayoutInflater().inflate(
                R.layout.yue_limiting_pop_window, null);
        mYueLimitPopWindow = new PopupWindow(mYueLimitPopView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mYueLimitPopWindow.setFocusable(true);
        mYueLimitPopWindow.setOutsideTouchable(true);
        mYueLimitPopWindow.setBackgroundDrawable(new ColorDrawable(0));
        mOnlyMaleBtn = (Button) mYueLimitPopView
                .findViewById(R.id.yue_limiting_only_male_btn);
        mOnlyMaleBtn.setOnClickListener(this);
        mOnlyFemaleBtn = (Button) mYueLimitPopView
                .findViewById(R.id.yue_limiting_only_female_btn);
        mOnlyFemaleBtn.setOnClickListener(this);
        mNoLimitBtn = (Button) mYueLimitPopView
                .findViewById(R.id.yue_limiting_no_limit_btn);
        mNoLimitBtn.setOnClickListener(this);
        mSex = "9";// 默认为不限
        mYueLimitingTV.setText(R.string.yue_limiting_no_limit);

        mYueParticipationWayView = getLayoutInflater().inflate(
                R.layout.yue_participation_way_pop_window, null);
        mYueParticipationWayPopWindow = new PopupWindow(
                mYueParticipationWayView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mYueParticipationWayPopWindow.setFocusable(true);
        mYueParticipationWayPopWindow.setOutsideTouchable(true);
        mYueParticipationWayPopWindow
                .setBackgroundDrawable(new ColorDrawable(0));
        mFreeloadBtn = (Button) mYueParticipationWayView
                .findViewById(R.id.yue_participation_way_freeload_btn);
        mFreeloadBtn.setOnClickListener(this);
        mAABtn = (Button) mYueParticipationWayView
                .findViewById(R.id.yue_participation_way_AA_btn);
        mAABtn.setOnClickListener(this);
        mMyTreatBtn = (Button) mYueParticipationWayView
                .findViewById(R.id.yue_participation_way_mytreat_btn);
        mMyTreatBtn.setOnClickListener(this);
        mYueParticpationWayTV.setText(R.string.yue_participation_way_AA);
        mChargeType = "1";// 默认值
        Date curDate = new Date(System.currentTimeMillis());
        mYueAvailableDateTV.setText(formatter.format(curDate));

        mYueAddressTV.setText(CacheTools.getUserData("addrStr"));
        mYuePhotoGV = (GridView) findViewById(R.id.yue_photo_gv);
        mYuePhotoGV.setSelector(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        int totalWidth = dm.widthPixels;
        mColumWidth = (totalWidth
                - getResources().getDimensionPixelSize(
                R.dimen.yue_photo_ll_padding)
                * 2
                - getResources().getDimensionPixelSize(
                R.dimen.yue_photo_padding) * 3 - 10 * 4) / 4;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        mYuePhotoGV.setLayoutParams(params);
        mYuePhotoGV.setNumColumns(4);
        mYuePhotoGV.setColumnWidth(mColumWidth + 10);

        mYuePhotoGV.setStretchMode(GridView.NO_STRETCH);

        mAdapter = new GridAdapter(this);
        mYuePhotoGV.setAdapter(mAdapter);

        mYuePhotoGV.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                Acp.getInstance(AddYueActivity.this).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        , Manifest.permission.CAMERA)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(AddYueActivity.this,
                                        MultiImageSelectorActivity.class);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,
                                        true);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
                                        4 - arg2);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                                        MultiImageSelectorActivity.MODE_MULTI);
                                startActivityForResult(intent, REQUEST_IMAGE);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtil.toastShow(AddYueActivity.this, permissions.toString() + "权限拒绝");
                            }
                        });


                mTuPosition = arg2;
            }
        });

        getMyLocation();
    }

    private void initEvent() {
        mHeaderBackIBtn.setOnClickListener(this);
        mHeaderBarRightTV.setOnClickListener(this);
        mYueTypeBtn.setOnClickListener(this);
        mYueLimitingRL.setOnClickListener(this);
        mYueParticpationWayRL.setOnClickListener(this);
        mYueAvailableDateRL.setOnClickListener(this);
        mYueAddressRL.setOnClickListener(this);
        mYueContentET.setOnEditorActionListener(new FilterEnterActionListener());
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
            if (Bimp.tempSelectBitmap.size() == 4) {
                return 4;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        mColumWidth, mColumWidth);
                params.setMargins(0, 10, 10, 0);
                holder.image.setLayoutParams(params);
                holder.deleteIV = (ImageView) convertView
                        .findViewById(R.id.item_grid_delete_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                Bitmap bm = ImageUtils.createScaledBitmap(
                        ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.btn_addphoto)), mColumWidth,
                        mColumWidth, ScalingLogic.CROP);
                holder.image.setImageBitmap(bm);
                if (position == 4) {
                    holder.image.setVisibility(View.GONE);
                }
                holder.deleteIV.setVisibility(View.GONE);
            } else {
                Bitmap bm = ImageUtils.createScaledBitmap(Bimp.tempSelectBitmap
                                .get(position).getBitmap(), mColumWidth, mColumWidth,
                        ScalingLogic.CROP);
                holder.image.setImageBitmap(bm);

                holder.deleteIV.setVisibility(View.VISIBLE);
                holder.deleteIV.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Bimp.tempSelectBitmap.remove(position);
                        notifyDataSetChanged();
                    }
                });

            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
            public ImageView deleteIV;// item_grid_delete_iv
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

//    class AdapterDataSetObserver extends DataSetObserver {
//
//        @Override
//        public void onChanged() {
//            if (Bimp.max == Bimp.tempSelectBitmap.size()) {
//            } else {
//                Bimp.max += 1;
//            }
//            mAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onInvalidated() {
//            // Not yet implemented!
//        }
//
//    }

    private String city = null;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm = null;

        switch (requestCode) {

            case REQUEST_ADDRESS:
                if (data != null) {
                    mYueAddressTV.setText(data.getStringExtra("yue_address"));
                    city = data.getStringExtra("yue_city");
                }
                break;
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    List<String> paths = data
                            .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    for (String url : paths) {
                        MyLog.e("YUY","选择图片地址 = "+url);
                        String fileName = String
                                .valueOf(System.currentTimeMillis());
                        bm = ImageUtils.decodeFile(url, mColumWidth, mColumWidth,
                                ScalingLogic.CROP);

                        FileUtils.saveBitmap(bm, fileName);

                        ImageItem takePhoto = new ImageItem();
                        // takePhoto.setImageUri(Uri.parse(url));
                        takePhoto.setImagePath(url);
                        takePhoto.setBitmap(bm);
                        if (mTuPosition == Bimp.tempSelectBitmap.size()) {
                            Bimp.tempSelectBitmap.add(takePhoto);
                        } else {
                            Bimp.tempSelectBitmap.set(mTuPosition, takePhoto);
                        }
                        mTuPosition++;
                        mYuePhotoTV.setText(getResources().getString(
                                R.string.yue_photo_size, mTuPosition, 4));
                    }
                    mAdapter.notifyDataSetChanged();// 非常关键，不然图片显示不出来
                }
                break;
            default:
                break;
        }
    }

//    public void upLodeImg(final int yueId, final ArrayList<ImageItem> photos,
//                          final int position) {
//        try {
//            File file;
//            AjaxParams params = new AjaxParams();
//            file = new File(photos.get(position).getImagePath());
//            params.put("id", String.valueOf(yueId));
//            params.put("file", file);
//
//            requestNet(new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    switch (msg.what) {
//                        case Constant.NET_DATA_SUCCESS:
//                            int nextPosition = position + 1;
//                            if (nextPosition < photos.size()) {
//                                upLodeImg(yueId, photos, nextPosition);
//                            } else {
//                                dismissWaitingDialog();
//                                Intent intent = new Intent();
//                                intent.setAction("com.chewuwuyou.chequan.tizi");
//                                // 发送 一个无序广播
//                                AddYueActivity.this.sendBroadcast(intent);
//                                ToastUtil.toastShow(AddYueActivity.this, "发布成功");
//                                finishActivity();
//                            }
//                            break;
//                        case Constant.NET_DATA_FAIL:
//                            upLodeImg(yueId, photos, position);
//                            break;
//                        default:
//                            dismissWaitingDialog();
//                            break;
//                    }
//                }
//            }, params, NetworkUtil.UPLOAD_YUE_IMG, false, 1);
//        } catch (FileNotFoundException e) {
//            dismissWaitingDialog();
//            e.printStackTrace();
//        }
//    }

    /**
     * 上传图片通过base64上传
     *
     * @param yueId
     * @param photos
     * @param position
     */
    public void upLodeImgByBase64(final int yueId, final ArrayList<ImageItem> photos,
                                  final int position) {
        AjaxParams params = new AjaxParams();
        params.put("id", String.valueOf(yueId));
        params.put("imgStr", FileUtils.Bitmap2StrByBase64(photos.get(position).getBitmap()));
        params.put("file", "");
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        int nextPosition = position + 1;
                        if (nextPosition < photos.size()) {
                            upLodeImgByBase64(yueId, photos, nextPosition);
                        } else {
                            dismissWaitingDialog();
                            Intent intent = new Intent();
                            intent.setAction("com.chewuwuyou.chequan.tizi");
                            // 发送 一个无序广播
                            AddYueActivity.this.sendBroadcast(intent);
                            ToastUtil.toastShow(AddYueActivity.this, "发布成功");
                            finishActivity();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        upLodeImgByBase64(yueId, photos, position);
                        break;
                    default:
                        dismissWaitingDialog();
                        break;
                }
                mHeaderBarRightTV.setEnabled(true);
            }
        }, params, NetworkUtil.UPLOAD_YUE_IMG, false, 0);
    }

    private boolean validateVariables() {
        if (TextUtils.isEmpty(mYueTitleET.getText().toString().trim())) {
            ToastUtil.showToast(AddYueActivity.this,
                    R.string.yue_title_must_not_empty);
            return false;
        }
        if (mYueContentET.getText().toString().length() < 10) {
            ToastUtil.showToast(AddYueActivity.this,
                    R.string.yue_content_too_short);
            return false;
        }
        if (TextUtils.isEmpty(mSex) || TextUtils.isEmpty(mChargeType)
                || TextUtils.isEmpty(mType)
                // || TextUtils.isEmpty(mRegion)
                || TextUtils.isEmpty(mYueAvailableDateTV.getText().toString().trim())) {
            return false;
        }
        Long thisTime = new Date().getTime();
        String time = mYueAvailableDateTV.getText().toString().trim();
        try {
            Long fTime = formatter.parse(time).getTime();
            if (fTime < thisTime) {
                Toast.makeText(AddYueActivity.this, "活动时间不能小于当前时间哟...", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Bimp.tempSelectBitmap.size() == 0) {
            ToastUtil.toastShow(AddYueActivity.this, "至少要上传一张图");
            return false;
        }
        if (TextUtils.isEmpty(mYueAddressTV.getText().toString())) {
            ToastUtil.toastShow(AddYueActivity.this, "请选择活动地点");
            return false;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv:
                if (validateVariables()) {
                    mHeaderBarRightTV.setEnabled(false);
                    AjaxParams params = new AjaxParams();
                    params.put("title", mYueTitleET.getText().toString());
                    params.put("content", mYueContentET.getText().toString());
                    params.put("location", mYueAddressTV.getText().toString());
                    params.put("chargeType", mChargeType);
                    params.put("type", mType);
                    params.put("sex", mSex);
                    params.put("avaliableDate", mYueAvailableDateTV.getText()
                            .toString());
                    params.put("region", city);//"成都市"
                    showWaitingDialog();
                    requestNet(mHandler, params, NetworkUtil.PUBLISH_YUE, false, 0);
                }
                break;
            case R.id.yue_type_btn:
                mYueTypePopWindow.showAtLocation(mParentView, Gravity.CENTER, 0, 0);
                break;

            case R.id.pet_type_btn:
                mType = Constant.YueYueType.PET.toString();
                mYueTypeBtn.setText(R.string.yue_pet_type);
                mYueTypeBtn.setBackgroundResource(R.drawable.yue_type_pet_frame);
                mYueTypePopWindow.dismiss();
                break;
            case R.id.travell_type_btn:
                mType = Constant.YueYueType.TRAVEL.toString();
                mYueTypeBtn.setText(R.string.yue_travell_type);
                mYueTypeBtn
                        .setBackgroundResource(R.drawable.yue_type_travell_frame);
                mYueTypePopWindow.dismiss();
                break;
            case R.id.dinner_type_btn:
                mType = Constant.YueYueType.DINNER.toString();
                mYueTypeBtn.setText(R.string.yue_dinner_type);
                mYueTypeBtn.setBackgroundResource(R.drawable.yue_type_dinner_frame);
                mYueTypePopWindow.dismiss();
                break;
            case R.id.movie_type_btn:
                mType = Constant.YueYueType.FILM.toString();
                mYueTypeBtn.setText(R.string.yue_movie_type);
                mYueTypeBtn.setBackgroundResource(R.drawable.yue_type_movie_frame);
                mYueTypePopWindow.dismiss();
                break;
            case R.id.bar_type_btn:
                mType = Constant.YueYueType.BAR.toString();
                mYueTypeBtn.setText(R.string.yue_bar_type);
                mYueTypeBtn.setBackgroundResource(R.drawable.yue_type_bar_frame);
                mYueTypePopWindow.dismiss();
                break;
            case R.id.shopping_type_btn:
                mType = Constant.YueYueType.SHOPPING.toString();
                mYueTypeBtn.setText(R.string.yue_shopping_type);
                mYueTypeBtn
                        .setBackgroundResource(R.drawable.yue_type_shopping_frame);
                mYueTypePopWindow.dismiss();
                break;
            case R.id.mahjong_type_btn:
                mType = Constant.YueYueType.MAHJONG.toString();
                mYueTypeBtn.setText(R.string.yue_mahjong_type);
                mYueTypeBtn
                        .setBackgroundResource(R.drawable.yue_type_majiang_frame);
                mYueTypePopWindow.dismiss();
                break;
            case R.id.sports_type_btn:
                mType = Constant.YueYueType.SPORTS.toString();
                mYueTypeBtn.setText(R.string.yue_sports_type);
                mYueTypeBtn.setBackgroundResource(R.drawable.yue_type_sports_frame);
                mYueTypePopWindow.dismiss();
                break;

            // private int sex; // 限制参与性别类型 -- 0表示男，1表示女，9表示不限
            case R.id.yue_limiting_rl:
                mYueLimitPopWindow
                        .showAtLocation(mParentView, Gravity.CENTER, 0, 0);
                break;
            case R.id.yue_limiting_only_male_btn:
                mSex = "0";
                mYueLimitingTV.setText(R.string.yue_limiting_only_male);
                mYueLimitPopWindow.dismiss();
                break;

            case R.id.yue_limiting_only_female_btn:
                mSex = "1";
                mYueLimitingTV.setText(R.string.yue_limiting_only_female);
                mYueLimitPopWindow.dismiss();
                break;

            case R.id.yue_limiting_no_limit_btn:
                mSex = "9";
                mYueLimitingTV.setText(R.string.yue_limiting_no_limit);
                mYueLimitPopWindow.dismiss();
                break;

            // private int chargeType;// 付费方式：0：我请客，1：AA, 2:求请客
            case R.id.yue_participation_way_rl:
                mYueParticipationWayPopWindow.showAtLocation(mParentView,
                        Gravity.CENTER, 0, 0);
                break;

            case R.id.yue_participation_way_mytreat_btn:
                mChargeType = "0";
                mYueParticpationWayTV
                        .setText(R.string.yue_participation_way_mytreat);
                mYueParticipationWayPopWindow.dismiss();
                break;

            case R.id.yue_participation_way_AA_btn:
                mChargeType = "1";
                mYueParticpationWayTV.setText(R.string.yue_participation_way_AA);
                mYueParticipationWayPopWindow.dismiss();
                break;

            case R.id.yue_participation_way_freeload_btn:
                mChargeType = "2";
                mYueParticpationWayTV
                        .setText(R.string.yue_participation_way_freeload);
                mYueParticipationWayPopWindow.dismiss();
                break;
            case R.id.yue_available_date_rl:
                showDateDialog(mYueAvailableDateTV);
                break;
            case R.id.yue_address_rl:
                Intent intent = new Intent(AddYueActivity.this,
                        YueAddressLocActivity.class);
                startActivityForResult(intent, REQUEST_ADDRESS);
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();

    }

    private void showDateDialog(final TextView dateTimerTV) {
        LayoutInflater inflater = LayoutInflater.from(AddYueActivity.this);
        View dialoglayout = inflater.inflate(R.layout.datetime_dialog, null);
        final DatePicker datePicker = (DatePicker) dialoglayout.findViewById(R.id.date);
        final TimePicker timePicker = (TimePicker) dialoglayout.findViewById(R.id.time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.setCalendarViewShown(Integer.valueOf(android.os.Build.VERSION.SDK) >= 23);
        datePicker.setSpinnersShown(true);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        AlertDialog.Builder avaliableDialog = new AlertDialog.Builder(AddYueActivity.this);
        avaliableDialog.setTitle(R.string.dialog_time_picker_dialog_title);
        avaliableDialog.setView(dialoglayout);
        avaliableDialog.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String strDamTime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                        strDamTime += " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                        dateTimerTV.setText(strDamTime);
                    }
                });
        avaliableDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        avaliableDialog.create().show();
    }

    /**
     * 定位自己的位置
     */
    public void getMyLocation() {
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();// 设定定位参数
        mLocationClient.start();// 开始定位

        showWaitingDialog();
    }

    // 定位
    // 设置相关参数
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.disableCache(false);// 禁止启用缓存定位
        option.setPoiNumber(5); // 最多返回POI个数
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.setPoiDistance(1000); // poi查询距离
        option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
        mLocationClient.setLocOption(option);

    }


    public MyLocationListenner myListener = new MyLocationListenner();

    /**
     * 监听函数，更新位置
     */
    public class MyLocationListenner implements BDLocationListener {

        @SuppressWarnings("static-access")
        @Override
        // 接收位置信息
        public void onReceiveLocation(final BDLocation location) {
            dismissWaitingDialog();
            if (location == null)
                return;
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                mYueAddressTV.setText(location.getAddrStr());
                city = location.getCity();
                // 退出时销毁定位
                mLocationClient.stop();
            }
        }

        // 接收POI信息函数
        @Override
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }
}
