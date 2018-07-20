package com.chewuwuyou.app.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ProhibitEdiuttextExpression;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

public class AddMailingAddress extends CDDBaseActivity implements View.OnClickListener {


    @ViewInject(id=R.id.sub_header_bar_left_ibtn) //返回上一页
    private ImageButton mBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//标题
    private TextView mTitleTV;
    @ViewInject(id = R.id.sub_header_bar_right_tv)//保存
    private TextView mBarRightTV;
    @ViewInject(id = R.id.mail_consignee)//收货人
    private ProhibitEdiuttextExpression mMailConsignee;
    @ViewInject(id = R.id.mail_phone)//联系方式
    private EditText mMailPhone;
    @ViewInject(id = R.id.mail_region)//选择地区
    private TextView mMailRegion;
    @ViewInject(id = R.id.mail_address)//详细地址
    private ProhibitEdiuttextExpression mMailAddress;
    @ViewInject(id = R.id.mail_postcode)//邮政编码
    private EditText mMailPostcode;
    @ViewInject(id = R.id.mail_default)//默认地址
    private ImageView mMailDefault;
    @ViewInject(id = R.id.mail_contacts)//选择联系人
    private LinearLayout mMailContacts;
    @ViewInject(id = R.id.mail_choice_address)//选择地区
    private LinearLayout mMailChoiceAddress;
    @ViewInject(id = R.id.default_address)//选择地区
    private LinearLayout mDefaultAddress;

    private String mSelected = "0";//是否是默认地址
    private final int REQUESTCODE = 10;//代表选择通讯录
    private final int REGION = 20;//选择地区

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mailing_address);
        initView();
        initData();
        initEvent();
    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.mail_choice_address://选择地区
                intent = new Intent(AddMailingAddress.this,
                        AreaSelectActivity.class);
                startActivityForResult(intent, REGION);
                break;
            case R.id.mail_contacts://选择联系人
                intent = new Intent(Intent.ACTION_PICK, android.provider.ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUESTCODE);
                break;
            case R.id.mail_default://设置成默认地址
                if (mSelected.equals("0")) {
                    mSelected = "1";
                    mMailDefault.setImageDrawable(this.getResources().getDrawable((R.drawable.btn_switch_1)));//选中
                } else {
                    mSelected = "0";
                    mMailDefault.setImageDrawable(this.getResources().getDrawable((R.drawable.btn_switch_0)));//选中
                }
                break;
            case R.id.sub_header_bar_right_tv://保存
                if (TextUtils.isEmpty(mMailConsignee.getText().toString())) {
                    ToastUtil.toastShow(AddMailingAddress.this, "收货人不能为空");
                } else if (TextUtils.isEmpty(mMailPhone.getText().toString())) {
                    ToastUtil.toastShow(AddMailingAddress.this, "联系方式不能为空");
                }else if(mMailPhone.getText().toString().trim().length()>14||mMailPhone.getText().toString().trim().length()<7){
                    ToastUtil.toastShow(AddMailingAddress.this, "联系方式格式不正确");
                } else if (TextUtils.isEmpty(mMailRegion.getText().toString())) {
                    ToastUtil.toastShow(AddMailingAddress.this, "地区不能为空");
                } else if (TextUtils.isEmpty(mMailAddress.getText().toString())) {
                    ToastUtil.toastShow(AddMailingAddress.this, "详细地址不能为空");
                }else if(mMailAddress.getText().toString().length()<5 || mMailAddress.getText().toString().length()>30){
                    ToastUtil.toastShow(AddMailingAddress.this, "详细地址为5-30个字符");
                } else if (TextUtils.isEmpty(mMailPostcode.getText().toString())) {
                    ToastUtil.toastShow(AddMailingAddress.this, "邮政编码不能为空");
                }else if(mMailPostcode.getText().toString().length()<6){
                    ToastUtil.toastShow(AddMailingAddress.this, "邮政编码不能小于6位");
                } else {
                    mailSubmit();
                }
                break;
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;
        }
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        mTitleTV.setText("新增地址");
        mBarRightTV.setText("保存");
        mBarRightTV.setVisibility(View.VISIBLE);
        mMailConsignee.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        mMailAddress.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        //判断是否是第一条数据
        if(Integer.parseInt(getIntent().getStringExtra("id"))>0){
            mDefaultAddress.setVisibility(View.VISIBLE);
        }else{
            mSelected = "1";
            mDefaultAddress.setVisibility(View.GONE);
        }
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mBarLeftIbtn.setOnClickListener(this);
        mBarRightTV.setOnClickListener(this);
        mMailChoiceAddress.setOnClickListener(this);//选择地区
        mMailContacts.setOnClickListener(this);//选择联系人
        mMailDefault.setOnClickListener(this);//默认地址
    }

    /**
     * 添加地址，提交信息
     */
    public void mailSubmit() {
        AjaxParams params = new AjaxParams();
        params.put("receiver", mMailConsignee.getText().toString());//收货人
        params.put("phone", mMailPhone.getText().toString());//联系方式
        params.put("region", mMailRegion.getText().toString());//所在地区
        params.put("address", mMailAddress.getText().toString());//详细地址
        params.put("zipCode", mMailPostcode.getText().toString());//邮政编码
        params.put("defaultAddress", mSelected);//默认地址

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.obj!=null){
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            finishActivity();//关闭当前页面
                            break;
                        case Constant.NET_DATA_FAIL:
                            ToastUtil.toastShow(AddMailingAddress.this,
                                    ((DataError) msg.obj).getErrorMessage());
                            break;
                        default:
                            break;
                    }
                }else{
                    ToastUtil.toastShow(AddMailingAddress.this,"数据解析错误");
                }
            }
        }, params, NetworkUtil.ADD_MAIL_ADDRESS, false, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return false;
    }
    /**
     * 回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE://选择联系人
                    Uri uri = data.getData();
                    ContentResolver cr = getContentResolver();
                    Cursor cursor = cr.query(uri, null, null, null, null);
                    try {

                        while (cursor.moveToNext()) {
                            // 取得联系人名字
                            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                            String contact = cursor.getString(nameFieldColumnIndex);
                            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId,null, null);
                            String num = "";
                            while (phone.moveToNext()) {
                                num= phone.getString(phone.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                break;//只取第一个
                            }
                            mMailConsignee.setText("");
                            mMailPhone.setText("");//联系人电话
                            mMailConsignee.setText(contact);//联系人名称
                            mMailPhone.setText(num.replace(" ",""));//联系人电话

                            phone.close();
                        }

                    } finally {

                        cursor.close();
                    }
                    break;
                case REGION://选择地区
                    String mCityName = data.getStringExtra("city");
                    String mProvinceName = data.getStringExtra("province");
                    String mAreaName = data.getStringExtra("district");
                    if (mAreaName.equals("全部")) {
                        mAreaName = "";
                    }
                    mMailRegion.setText(mProvinceName + mCityName + mAreaName);
                    break;
            }
        }
    }
}
