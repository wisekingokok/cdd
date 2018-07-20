package com.chewuwuyou.app.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.BanAdapter;
import com.chewuwuyou.app.bean.BanKuaiItem;
import com.chewuwuyou.app.callback.FilterEnterActionListener;
import com.chewuwuyou.app.utils.Bimp;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.EditTextLengthFilter;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.ImageItem;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.MyGridView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class AddHotTieActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    private ImageButton mHeaderBackIBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderBarTV;
    @ViewInject(id = R.id.sub_header_bar_right_tv)
    private TextView mHeaderBarRightTV;
    @ViewInject(id = R.id.tie_content_et, click = "onAction")
    private EditText mTieContentET;

    @ViewInject(id = R.id.tie_tu_gv, click = "onAction")
    private MyGridView mTieTuGV;
    @ViewInject(id = R.id.yue_type_btn)
    private TextView yue_type_btn;
    @ViewInject(id = R.id.selectLL)
    LinearLayout selectLL;
    @ViewInject(id = R.id.address)
    private TextView address;

    public static Bitmap mAddPicBmp;
    private View mParentView;
    private WindowManager mWindowManager;
    private GridAdapter mAdapter;
    private int mColumWidth;
    private int mBanId;
    private int mTuPosition = 0;
    private String mAddrStr;
    private static final int REQUEST_IMAGE = 22;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private static final int REQUEST_ADDRESS = 24;
    public MyLocationListenner myListener = new MyLocationListenner();

    private PopupWindow pop = null;
    private View popView;
    private GridView popGrid;
    private List<BanKuaiItem> mData;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    if (msg.obj != null) {
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            int tieId = jo.getInt("id");
                            if (tieId > 0) {
                                upLodeImgByBase64(tieId, Bimp.tempSelectBitmap, 0);//修改为图片上传
                            } else {
                                dismissWaitingDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dismissWaitingDialog();
                        }
                    } else {
                        dismissWaitingDialog();
                    }
                    //finishActivity();
                    break;
                case Constant.NET_DATA_FAIL:
                    ToastUtil.toastShow(AddHotTieActivity.this, "发布失败");
                    dismissWaitingDialog();
                    break;
                default:
                    ToastUtil.toastShow(AddHotTieActivity.this, "网络请求超时，换一换网络吧！");
                    dismissWaitingDialog();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAddPicBmp = ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.btn_addphoto));
        mParentView = getLayoutInflater().inflate(R.layout.add_tie_layout, null);
        setContentView(mParentView);
        mWindowManager = this.getWindowManager();
        initView();
        initData();
        initEvent();
    }

    // private List<PopItemBean> getPopItem() {
    // List<PopItemBean> list = new ArrayList<PopItemBean>();
    //
    // list.add(new PopItemBean(R.drawable.yue_type_sports_frame, "车展", 0));
    // list.add(new PopItemBean(R.drawable.yue_type_majiang_frame, "自驾", 1));
    // list.add(new PopItemBean(R.drawable.yue_type_movie_frame, "改装", 2));
    // list.add(new PopItemBean(R.drawable.yue_type_travell_frame, "风景", 3));
    // list.add(new PopItemBean(R.drawable.yue_type_dinner_frame, "城市", 4));
    // list.add(new PopItemBean(R.drawable.yue_type_bar_frame, "摩托", 5));
    // return list;
    // }

    private void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mBanId = getIntent().getIntExtra("banId", 0);
        mTieContentET.requestFocus();
        InputFilter[] filters = {new EditTextLengthFilter(AddHotTieActivity.this, 40)};
        mTieContentET.setFilters(filters);
        mTieContentET.setHint("不能超过40个字符噢");
        getMyLocation();
    }

    private void initView() {
        mHeaderBarTV.setText("图文分享");
        mHeaderBarRightTV.setText(R.string.tie_publish);
        mHeaderBarRightTV.setVisibility(View.VISIBLE);

        mTieTuGV.setSelector(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        int totalWidth = dm.widthPixels;
        mColumWidth = (totalWidth - getResources().getDimensionPixelSize(R.dimen.yue_photo_ll_padding) * 2
                - (10 + getResources().getDimensionPixelSize(R.dimen.yue_photo_ll_padding)) * 3 - 10) / 4;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mTieTuGV.setLayoutParams(params);
        mTieTuGV.setNumColumns(4);
        mTieTuGV.setColumnWidth(mColumWidth + 10);

        mTieTuGV.setStretchMode(GridView.NO_STRETCH);

        mAdapter = new GridAdapter(this);
        mTieTuGV.setAdapter(mAdapter);

        mTieTuGV.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                Acp.getInstance(AddHotTieActivity.this).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        , Manifest.permission.CAMERA)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(AddHotTieActivity.this, MultiImageSelectorActivity.class);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 5 - arg2);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                                startActivityForResult(intent, REQUEST_IMAGE);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtil.toastShow(AddHotTieActivity.this, permissions.toString() + "权限拒绝");
                            }
                        });
                mTuPosition = arg2;
            }
        });
        if (getIntent().getIntExtra("type", 0) == 1) {
            selectLL.setVisibility(View.VISIBLE);
            yue_type_btn.setText("车展");
        } else {
            selectLL.setVisibility(View.GONE);
        }
        address.setVisibility(View.VISIBLE);

        AjaxParams params1 = new AjaxParams();
        params1.put("reuse", String.valueOf(1));
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mData = BanKuaiItem.parseList(msg.obj.toString());
                        break;
                    case Constant.NET_DATA_FAIL:
                        break;
                    default:
                        break;
                }

            }
        }, params1, NetworkUtil.GET_ALL_BANKUAI, false, 0);
    }

    private void initEvent() {
        mHeaderBackIBtn.setOnClickListener(this);
        mHeaderBarRightTV.setOnClickListener(this);
        selectLL.setOnClickListener(this);
        address.setOnClickListener(this);
        mTieContentET.setOnEditorActionListener(new FilterEnterActionListener());
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
            if (Bimp.tempSelectBitmap.size() == 5) {
                return 5;
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

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mColumWidth, mColumWidth);
                params.setMargins(0, 10, 10, 0);
                holder.image.setLayoutParams(params);
                holder.deleteIV = (ImageView) convertView.findViewById(R.id.item_grid_delete_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                Bitmap bm = ImageUtils.createScaledBitmap(ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.btn_addphoto)), mColumWidth, mColumWidth,
                        ScalingLogic.CROP);
                holder.image.setImageBitmap(bm);
                // motify start by yuyong 2016/04/09 防止图片回收出现oom
                // bm.recycle();
                // motify start by yuyong 2016/04/09 防止图片回收出现oom
                if (position == 5) {
                    holder.image.setVisibility(View.GONE);
                }
                holder.deleteIV.setVisibility(View.GONE);
            } else {
                Bitmap bm = ImageUtils.createScaledBitmap(Bimp.tempSelectBitmap.get(position).getBitmap(), mColumWidth,
                        mColumWidth, ScalingLogic.CROP);
                holder.image.setImageBitmap(bm);
                holder.deleteIV.setVisibility(View.VISIBLE);
                holder.deleteIV.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Bimp.tempSelectBitmap.remove(position);
                        notifyDataSetChanged();
                    }
                });
                // motify start by yuyong 2016/04/09 防止图片回收出现oom
                // bm.recycle();
                // motify start by yuyong 2016/04/09 防止图片回收出现oom
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

    class AdapterDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            if (Bimp.max == Bimp.tempSelectBitmap.size()) {
            } else {
                Bimp.max += 1;
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            // Not yet implemented!
        }

    }

    private boolean isSelAddress = false;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data==null){
            return;
        }
        Bitmap bm = null;
        switch (requestCode) {
            case REQUEST_ADDRESS:
                if (resultCode == RESULT_OK) {
                    address.setText(data.getStringExtra("yue_address"));
                    mAddrStr = data.getStringExtra("yue_address");
                    isSelAddress = true;
                }
                break;
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    // Get the result list of select image paths
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    for (String url : paths) {
                        Log.e("YUY","--图片返回--"+url);
                        String fileName = String.valueOf(System.currentTimeMillis());
                        bm = ImageUtils.decodeFile(url, mBgTuWidth, mBgTuHeight, ScalingLogic.CROP);
                        FileUtils.saveBitmap(bm, fileName);
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setImagePath(url);
                        takePhoto.setBitmap(bm);
                        if (mTuPosition == Bimp.tempSelectBitmap.size()) {
                            Bimp.tempSelectBitmap.add(takePhoto);
                        } else {
                            Bimp.tempSelectBitmap.set(mTuPosition, takePhoto);
                        }
                        mTuPosition++;
                    }
                    mAdapter.notifyDataSetChanged();// 非常关键，不然图片显示不出来
                }
                break;
            default:
                break;
        }
    }

//	public void upLodeImg(final int id, final ArrayList<ImageItem> photos, final int position) {
//		try {
//			if (photos.size() == 0) {
//				dismissWaitingDialog();
//				ToastUtil.toastShow(AddHotTieActivity.this, "发布成功");
//				finishActivity();
//			} else {
//				File file;
//				AjaxParams params = new AjaxParams();
//
//				// file = getfileFromURI(photos.get(position).getImageUri());
//				file = new File(photos.get(position).getImagePath());
//				params.put("id", String.valueOf(id));
//				params.put("file", file);
//
//				requestNet(new Handler() {
//					@Override
//					public void handleMessage(Message msg) {
//						super.handleMessage(msg);
//						switch (msg.what) {
//						case Constant.NET_DATA_SUCCESS:
//							int nextPosition = position + 1;
//							if (nextPosition < photos.size()) {
//								upLodeImg(id, photos, nextPosition);
//							} else {
//								dismissWaitingDialog();
//								Intent intent = new Intent();
//						        intent.setAction("com.chewuwuyou.chequan.tizi");
//								// 发送 一个无序广播
//						        AddHotTieActivity.this.sendBroadcast(intent);
//								ToastUtil.toastShow(AddHotTieActivity.this, "发布成功");
//								finishActivity();
//							}
//							break;
//						case Constant.NET_DATA_FAIL:
//							upLodeImg(id, photos, position);
//							break;
//						default:
//							dismissWaitingDialog();
//							break;
//						}
//					}
//				}, params, NetworkUtil.UPLOAD_TIE_IMG, false, 1);
//			}
//		} catch (FileNotFoundException e) {
//			dismissWaitingDialog();
//			e.printStackTrace();
//		}
//	}

    /**
     * 上传图片通过base64上传
     *
     * @param tieId
     * @param photos
     * @param position
     */
    public void upLodeImgByBase64(final int tieId, final ArrayList<ImageItem> photos,
                                  final int position) {
        AjaxParams params = new AjaxParams();
        params.put("id", String.valueOf(tieId));
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
                            upLodeImgByBase64(tieId, photos, nextPosition);
                        } else {
                            dismissWaitingDialog();
                            Intent intent = new Intent();
                            intent.setAction("com.chewuwuyou.chequan.tizi");  // 发送 一个无序广播
                            AddHotTieActivity.this.sendBroadcast(intent);
                            ToastUtil.toastShow(AddHotTieActivity.this, "发布成功");
                            finishActivity();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        upLodeImgByBase64(tieId, photos, position);
                        break;
                    default:
                        dismissWaitingDialog();
                        break;
                }
            }
        }, params, NetworkUtil.UPLOAD_TIE_IMG, false, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();

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
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
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
                finish();
                break;
            case R.id.sub_header_bar_right_tv:
                if (TextUtils.isEmpty(mTieContentET.getText().toString().trim())) {
                    ToastUtil.toastShow(AddHotTieActivity.this, "写点什么吧");
                } else if (Bimp.tempSelectBitmap.isEmpty()) {
                    ToastUtil.toastShow(AddHotTieActivity.this, "必须得上传一张照片噢");
                } else {
                    if (mBanId == 0) {// 当贴ID为空时默认为banId
                        mBanId = mData.get(0).getId();
                    }
                    AjaxParams params = new AjaxParams();
                    params.put("banId", String.valueOf(mBanId));
                    params.put("content", mTieContentET.getText().toString());
                    if (!TextUtils.isEmpty(address.getText().toString())) {
                        params.put("location", mAddrStr);
                    } else
                        params.put("location", "不告诉你");
                    showWaitingDialog();
                    requestNet(mHandler, params, NetworkUtil.PUBLISH_TIE, false, 1);
                }

                break;
            case R.id.selectLL:
                if (mData == null) {
                    ToastUtil.toastShow(this, "未获取到类型");
                    return;
                }
                popView = getLayoutInflater().inflate(R.layout.pop_gridview, null);
                this.pop = new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                this.pop.setFocusable(true);
                this.pop.setOutsideTouchable(true);
                pop.setBackgroundDrawable(new ColorDrawable(0));

                popGrid = (GridView) popView.findViewById(R.id.gridView);
                final BanAdapter popAdapter = new BanAdapter(this, mData);
                popGrid.setAdapter(popAdapter);
                popGrid.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BanKuaiItem info = (BanKuaiItem) popAdapter.getItem(position);
                        yue_type_btn.setText(info.getTitle());
                        mBanId = mData.get(position).getId();
                        pop.dismiss();
                    }
                });
                pop.getContentView().setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        pop.dismiss();
                        return false;
                    }
                });
                pop.showAtLocation(mParentView, Gravity.CENTER, 0, 0);
                break;
            case R.id.address:
                Intent intent = new Intent(this, YueAddressLocActivity.class);
                startActivityForResult(intent, REQUEST_ADDRESS);
                break;
            default:
                break;
        }
    }

    /**
     * 定位自己的位置
     */
    public void getMyLocation() {
        mLocationClient = new LocationClient(AddHotTieActivity.this);
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();// 设定定位参数
        mLocationClient.start();// 开始定位
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

    /**
     * 监听函数，更新位置
     */
    private class MyLocationListenner implements BDLocationListener {

        @Override
        // 接收位置信息
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                CacheTools.setUserData("province", location.getProvince());
                CacheTools.setUserData("city", location.getCity());
                CacheTools.setUserData("district", location.getDistrict());
                CacheTools.setUserData("Lat", location.getLatitude() + "");
                CacheTools.setUserData("Lng", location.getLongitude() + "");
                CacheTools.setUserData("addrStr", location.getAddrStr());
                StringBuffer sbf = new StringBuffer().append(location.getCity()).append(location.getDistrict())
                        .append(location.getStreet());
                if (!isSelAddress) {
                    mAddrStr = sbf.toString();
                    address.setText(mAddrStr);
                }
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
