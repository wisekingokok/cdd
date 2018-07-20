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
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.callback.FilterEnterActionListener;
import com.chewuwuyou.app.utils.Bimp;
import com.chewuwuyou.app.utils.Constant;
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

public class AddQuanActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    private ImageButton mHeaderBackIBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderBarTV;
    @ViewInject(id = R.id.sub_header_bar_right_tv)
    private TextView mHeaderBarRightTV;
    @ViewInject(id = R.id.quan_wen_content_et)
    private EditText mDynamicContentET;

    private MyGridView mDynamicTuGV;

    public static Bitmap mAddPicBmp;
    private View mParentView;
    private WindowManager mWindowManager;
    private GridAdapter mAdapter;
    private int mColumWidth;
    private int mTuPosition = 0;
    private static final int REQUEST_IMAGE = 22;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stubE
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    if (msg.obj != null) {
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            int quanId = jo.getInt("id");
                            if (quanId > 0) {
                                upLodeImgByBase64(quanId, Bimp.tempSelectBitmap, 0);
                            } else {
                                dismissWaitingDialog();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            dismissWaitingDialog();
                            e.printStackTrace();
                        }
                    } else {
                        dismissWaitingDialog();
                    }
                    //finishActivity();
                    break;
                case Constant.NET_DATA_FAIL:
                    dismissWaitingDialog();
                    ToastUtil.toastShow(AddQuanActivity.this, "发布失败");
                    break;
                default:
                    ToastUtil.toastShow(AddQuanActivity.this, "网络请求超时，换一换网络吧！");
                    dismissWaitingDialog();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mAddPicBmp = ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.btn_addphoto));
        mParentView = getLayoutInflater().inflate(R.layout.add_dynamic_layout, null);
        setContentView(mParentView);
        mWindowManager = this.getWindowManager();
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mDynamicContentET.requestFocus();
    }

    private void initView() {
        mHeaderBarTV.setText("动态发布");
        mHeaderBarRightTV.setText(R.string.yue_publish);
        mHeaderBarRightTV.setVisibility(View.VISIBLE);

        mDynamicTuGV = (MyGridView) findViewById(R.id.quan_tu_gv);

        mDynamicTuGV.setSelector(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        int totalWidth = dm.widthPixels;
        mColumWidth = (totalWidth - getResources().getDimensionPixelSize(R.dimen.yue_photo_ll_padding) * 2
                - (10 + getResources().getDimensionPixelSize(R.dimen.yue_photo_padding)) * 3 - 10) / 4;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mDynamicTuGV.setLayoutParams(params);
        mDynamicTuGV.setNumColumns(4);
        mDynamicTuGV.setColumnWidth(mColumWidth + 10);

        mDynamicTuGV.setStretchMode(GridView.NO_STRETCH);

        mAdapter = new GridAdapter(this);
        mDynamicTuGV.setAdapter(mAdapter);

        mDynamicTuGV.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                Acp.getInstance(AddQuanActivity.this).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        , Manifest.permission.CAMERA)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(AddQuanActivity.this,
                                        MultiImageSelectorActivity.class);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,
                                        true);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
                                        9 - arg2);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                                        MultiImageSelectorActivity.MODE_MULTI);
                                startActivityForResult(intent, REQUEST_IMAGE);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtil.toastShow(AddQuanActivity.this, permissions.toString() + "权限拒绝");
                            }
                        });
                mTuPosition = arg2;
            }
        });
    }

    private void initEvent() {
        mHeaderBackIBtn.setOnClickListener(this);
        mHeaderBarRightTV.setOnClickListener(this);
        mDynamicContentET.setOnEditorActionListener(new FilterEnterActionListener());
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
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
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
                if (position == 9) {
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
                        // TODO Auto-generated method stub
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm = null;
        switch (requestCode) {
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    // Get the result list of select image paths
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    for (String url : paths) {
                        String fileName = String.valueOf(System.currentTimeMillis());
                        bm = ImageUtils.decodeFile(url, mColumWidth, mColumWidth, ScalingLogic.CROP);

                        if (bm != null) {
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
                    }
                    mAdapter.notifyDataSetChanged();// 非常关键，不然图片显示不出来
                }
                break;
            default:
                break;
        }
    }

//    public void upLodeImg(final int id, final ArrayList<ImageItem> photos, final int position) {
//        try {
//            if (photos.size() == 0) {
//                dismissWaitingDialog();
//                ToastUtil.toastShow(AddQuanActivity.this, "发布成功");
//                finishActivity();
//            } else {
//                File file;
//                AjaxParams params = new AjaxParams();
//                file = new File(photos.get(position).getImagePath());
//                params.put("id", String.valueOf(id));
//                params.put("file", file);
//
//                requestNet(new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        // TODO Auto-generated method stub
//                        super.handleMessage(msg);
//                        switch (msg.what) {
//                            case Constant.NET_DATA_SUCCESS:
//                                int nextPosition = position + 1;
//                                if (nextPosition < photos.size()) {
//                                    upLodeImg(id, photos, nextPosition);
//                                } else {
//                                    dismissWaitingDialog();
//
//                                    Intent intent = new Intent();
//                                    intent.setAction("com.chewuwuyou.chequan.tizi");
//                                    // 发送 一个无序广播
//                                    AddQuanActivity.this.sendBroadcast(intent);
//
//                                    ToastUtil.toastShow(AddQuanActivity.this, "发布成功");
//                                    finishActivity();
//                                }
//                                break;
//                            case Constant.NET_DATA_FAIL:
//                                upLodeImg(id, photos, position);
//                                break;
//                            default:
//                                dismissWaitingDialog();
//                                break;
//                        }
//                    }
//                }, params, NetworkUtil.UPLOAD_QUAN_IMG, false, 1);
//            }
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
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
        if (photos.size() == 0) {
            dismissWaitingDialog();
            ToastUtil.toastShow(AddQuanActivity.this, "发布成功");
            finishActivity();
        } else {
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
                                AddQuanActivity.this.sendBroadcast(intent);
                                ToastUtil.toastShow(AddQuanActivity.this, "发布成功");
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
                }
            }, params, NetworkUtil.UPLOAD_QUAN_IMG, false, 1);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
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
                if (TextUtils.isEmpty(mDynamicContentET.getText().toString().trim())) {
                    ToastUtil.toastShow(AddQuanActivity.this, "写点什么吧");
                } else {
                    AjaxParams params = new AjaxParams();
                    params.put("content", mDynamicContentET.getText().toString());
                    showWaitingDialog();
                    requestNet(mHandler, params, NetworkUtil.PUBLISH_QUAN, false, 1);
                }
                break;

            default:
                break;
        }
    }

}
