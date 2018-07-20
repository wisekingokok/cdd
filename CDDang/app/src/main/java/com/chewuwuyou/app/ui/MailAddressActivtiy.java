package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.MailAddressAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.IllegalScore;
import com.chewuwuyou.app.bean.MailAddress;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮寄地址
 * liuchun
 */
public class MailAddressActivtiy extends CDDBaseActivity implements View.OnClickListener {

    //返回上一页
    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//标题
    private ImageButton mBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//标题
    private TextView mTitleTV;
    @ViewInject(id = R.id.maillist)//地址列表
    private ListView mMailList;
    @ViewInject(id = R.id.frame_Layout)//列表布局
    private FrameLayout mFrameLayout;
    @ViewInject(id = R.id.add_maill_address)
    private Button mAddSubmit;//添加地址
    @ViewInject(id = R.id.layout_address)//添加新地址
    private LinearLayout mLayoutAddress;//地址
    @ViewInject(id = R.id.newly_maill_address)//新建地址
    private Button mNewlyMaillAddress;
    private MailAddressAdapter mAddressAdapter;
    private List<MailAddress> mailAddressList;

    Handler mHanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    deleteMail(Integer.parseInt(msg.obj.toString()));//传递id删除地址
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_address);
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
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;

            case R.id.add_maill_address://添加新地址
                if(mailAddressList.size()>19){
                     ToastUtil.toastShow(MailAddressActivtiy.this,"最多可添加20条地址");
                }else{
                    intent = new Intent(this, AddMailingAddress.class);
                    intent.putExtra("id", mailAddressList.size() + "");
                    startActivity(intent);
                }

                break;
            case R.id.newly_maill_address://新建地址

                if (mailAddressList.size()>=20)
                {
                    showToastMessage("地址最多20个", Toast.LENGTH_SHORT);
                }
                else {
                    intent = new Intent(this, AddMailingAddress.class);
                    intent.putExtra("id", mailAddressList.size() + "");
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mailAddressList = new ArrayList<MailAddress>();
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        mTitleTV.setText("邮寄地址管理");
    }

    @Override
    protected void onResume() {
        super.onResume();
        milAdministrationList();//邮寄地址列表查询
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mBarLeftIbtn.setOnClickListener(this);
        mAddSubmit.setOnClickListener(this);
        mNewlyMaillAddress.setOnClickListener(this);
    }

    /**
     * 邮寄地址管理
     */
    private void milAdministrationList() {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        List<MailAddress> mailAddress = MailAddress.parseList(msg.obj.toString());//获取数据
                        if (mailAddress.size() > 0) {
                            mFrameLayout.setVisibility(View.VISIBLE);
                            mLayoutAddress.setVisibility(View.GONE);

                            mailAddressList.clear();
                            mailAddressList.addAll(mailAddress);
                            mAddressAdapter = new MailAddressAdapter(MailAddressActivtiy.this, mailAddressList, mHanler);//创建适配器
                            mMailList.setAdapter(mAddressAdapter);

                        } else {
                            mFrameLayout.setVisibility(View.GONE);
                            mLayoutAddress.setVisibility(View.VISIBLE);
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(MailAddressActivtiy.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, null, NetworkUtil.MAIL_QUERY, false, 0);
    }

    /**
     * 删除邮寄地址
     */
    public void deleteMail(final int id) {
        AjaxParams params = new AjaxParams();
        params.put("id", String.valueOf(mailAddressList.get(id).getId()));//删除id
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mailAddressList.remove(id);//删除

                        if (mailAddressList.size() > 0) {
                            mailAddressList.get(0).setDefaultAddress(1);//设置第一条为默认地址
                            mFrameLayout.setVisibility(View.VISIBLE);
                            mLayoutAddress.setVisibility(View.GONE);
                        } else {
                            mFrameLayout.setVisibility(View.GONE);
                            mLayoutAddress.setVisibility(View.VISIBLE);
                        }
                        mAddressAdapter.notifyDataSetChanged();//刷新适配器
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(MailAddressActivtiy.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.DELETE_MAIL_ADDRESS, false, 0);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return false;
    }
}
