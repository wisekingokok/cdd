package com.chewuwuyou.app.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ContactsFriendAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.Contacter;
import com.chewuwuyou.app.bean.SearchFriendBean;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.ListViewForScrollView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:添加通讯录好友
 * @author:yuyong
 * @date:2015-5-29下午12:00:00
 * @version:1.2.1
 */
public class AddContactsFriendActivity extends CDDBaseActivity implements
        OnClickListener {

    private TextView mTitleTV;
    private TextView nullText;
    private ImageButton mBackIbtn;

    private TextView mTextView_canAdd;
    private ListViewForScrollView mListView_canadd;//可加为好友列表
    private ContactsFriendAdapter mCanAddAdapter;

    private TextView mTextView_caaInvite;
    private ListViewForScrollView mListView_invite;//可以邀请联系人的好友
    private InviteAdapter mInviteAdapter;
    private List<Contacter> mInviteContacter;

    List<Contacter> allContacters = new ArrayList<>();//所有联系人


    private List<SearchFriendBean> mSearchFriendBeans;// 通过通讯录查找出来的可添加为好友的集合


    private StringBuilder phoneNums;// 拼接联系人电话号码用于查询加好友

    private RelativeLayout mTitleHeight;//标题布局高度

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    // friend:1已是朋友，0不是朋友
                    try {
                        JSONObject jo = new JSONObject(msg.obj.toString());
                        mSearchFriendBeans.addAll(SearchFriendBean.parseList(jo
                                .getJSONArray("info").toString()));

                        //过滤 已经是friend 的用户

                        Iterator<SearchFriendBean> iter = mSearchFriendBeans.iterator();
                        while (iter.hasNext()) {
                            SearchFriendBean s = iter.next();
                            if ("1".equals(s.getFriend())) {
                                iter.remove();
                            }
                        }

                        //与本地匹配数据
                        for (int i = 0; i < mSearchFriendBeans.size(); i++) {

                            Iterator<Contacter> iter2 = allContacters.iterator();
                            while (iter2.hasNext()) {
                                Contacter s = iter2.next();
                                MyLog.i("contact--->" + s.getTel());
                                MyLog.i("invite--->" + mSearchFriendBeans.get(i).getTel());
                                if (s.getTel().equals(mSearchFriendBeans.get(i).getTel())) {
                                    mSearchFriendBeans.get(i).setContactName(s.getName());
                                    iter2.remove();
                                }

                            }
                        }

                        if (mSearchFriendBeans != null && mSearchFriendBeans.size() != 0) {
                            mTextView_canAdd.setText(mSearchFriendBeans.size() + "位好友可添加");
                            mTextView_canAdd.setVisibility(View.VISIBLE);

                        } else {
                            mTextView_canAdd.setVisibility(View.GONE);
                            mListView_canadd.setVisibility(View.GONE);
                        }
                        if (allContacters.size() > 0) {
                            mTextView_caaInvite.setText(allContacters.size() + "位好友可邀请");
                            mTextView_caaInvite.setVisibility(View.VISIBLE);
                        } else {
                            mTextView_caaInvite.setVisibility(View.GONE);
                            mListView_invite.setVisibility(View.GONE);
                        }


                        MyLog.i("now-->" + allContacters.size());
                        mCanAddAdapter.notifyDataSetChanged();
                        mInviteContacter.addAll(allContacters);
                        mInviteAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;

                case Constant.NET_REQUEST_FAIL:

                    Toast.makeText(AddContactsFriendActivity.this, "网络不可用，请检查您的网络是否连接", Toast.LENGTH_SHORT).show();

                    break;

                default:
                    break;
            }
//            if (mCanAddAdapter.getCount() <= 0)
//                nullText.setVisibility(View.VISIBLE);
//            else
//                nullText.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contacts_friend_ac);
        initView();
        initData();
        initEvent();

    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        mTextView_caaInvite = (TextView) findViewById(R.id.invite_tag);
        mTextView_canAdd = (TextView) findViewById(R.id.added_tag);


        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mListView_canadd = (ListViewForScrollView) findViewById(R.id.listview_added);
        mListView_invite = (ListViewForScrollView) findViewById(R.id.listview_can_add);
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        //    nullText = (TextView) findViewById(R.id.nullText);
        isTitle(mTitleHeight);//根据不同手机判断
        mTitleTV.setText(R.string.add_contacts_friend_title);
        mBackIbtn.setOnClickListener(this);
        Acp.getInstance(AddContactsFriendActivity.this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.READ_CONTACTS)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        Tools.getPhoneContacts(AddContactsFriendActivity.this);
                        Tools.getSIMContacts(AddContactsFriendActivity.this);
                        Tools.removeDuplicate(Tools.mContactsNumbers);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastUtil.toastShow(AddContactsFriendActivity.this, permissions.toString() + "权限拒绝");
                    }
                });

        phoneNums = new StringBuilder();

        for (int i = 0; i < Tools.mContactsNumbers.size(); i++) {
            if (Tools.mContactsNumbers.get(i).replace(" ", "").length() == 11) {
                phoneNums.append(Tools.mContactsNumbers.get(i).replace(" ", "") + "-");
            }
        }

        allContacters.addAll(Tools.getPhoneContactsWithName(this));

        if (!TextUtils.isEmpty(phoneNums)) {
            AjaxParams params = new AjaxParams();
            params.put("tels",
                    phoneNums.toString().substring(0, phoneNums.length() - 1));
            MyLog.i("获取到的电话号码-------------" + phoneNums);
            requestNet(mHandler, params, NetworkUtil.GET_CONTACTS_FRIEND,
                    false, 0);
        } else {
            MyLog.i("YUY", "通讯录为空或权限已被禁用");
        }


        mSearchFriendBeans = new ArrayList<SearchFriendBean>();
        mCanAddAdapter = new ContactsFriendAdapter(AddContactsFriendActivity.this, mSearchFriendBeans);
        mListView_canadd.setAdapter(mCanAddAdapter);

        mInviteContacter = new ArrayList<>();
        mInviteAdapter = new InviteAdapter(this, mInviteContacter, R.layout.item_friend_invite);
        mListView_invite.setAdapter(mInviteAdapter);


    }

    @Override
    protected void initEvent() {
        // TODO Auto-generated method stub


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;

            default:
                break;
        }
    }

    class InviteAdapter extends CommonAdapter<Contacter> {


        public InviteAdapter(Context context, List<Contacter> datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, final Contacter m, int position) {

            final ProgressDialog mProgressDialog = DialogUtil.showProgressDialog(AddContactsFriendActivity.this);
            holder.setText(R.id.name, m.getName());
            holder.setText(R.id.invite, "短信邀请");

            holder.getConvertView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name = CacheTools.getUserData("realName");
                    String portiUrl = CacheTools.getUserData("url");
                    String id = CacheTools.getUserData("userId");
                    String age = CacheTools.getUserData("age");
                    String sex;

                    final String content = "一起来车当当分享有车新生活,我在车当当的昵称是" + CacheTools.getUserData("nickName") + " ";
                    if (CacheTools.getUserData("sex", 0) == 0) {

                        sex = "man";
                    } else
                        sex = "women";


                    try {
                        final String url0 = "http://www.cddang.com/mhwcw/share/share.html?name=" + name + "&headImage=" + portiUrl + "&id=" + id + "&sex=" + sex + "&age=" + age;
                        String url = "http://www.cddang.com/mhwcw/share/share.html?name=" + URLEncoder.encode(name, "utf-8") + "&headImage=" + portiUrl + "&id=" + id + "&sex=" + sex+ "&age=" + age;
                        String phoneNumber = m.getTel();
                        final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));

                        AjaxParams params = new AjaxParams();
                        params.put("url", url);

                        NetworkUtil.getFhMulti().post(NetworkUtil.GET_SHORT_URL, params, new AjaxCallBack<String>() {

                            @Override
                            public void onSuccess(String s) {
                                super.onSuccess(s);
                                if (mProgressDialog != null)
                                    mProgressDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    ErrorCodeUtil.doErrorCode(AddContactsFriendActivity.this, jsonObject.getInt("code"), jsonObject.getString("message"));
                                    //断地址链接转换成功
                                    if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                                        String url2 = jsonObject.getString("data");
                                        intent.putExtra("sms_body", content + url2);                    //断地址链接转换失败

                                    } else {
                                        //    ToastUtil.toastShow(AppContext.getInstance(), jsonObject.getString("message"));
                                        intent.putExtra("sms_body", content + url0);//断地址链接转换失败
                                    }

                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    intent.putExtra("sms_body", content + url0);//断地址链接转换失败
                                    startActivity(intent);

                                }
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                //       ToastUtil.toastShow(AddfriendActivity.this,"网络错误，请稍后再试");
                                if (mProgressDialog != null)
                                    mProgressDialog.dismiss();
                                intent.putExtra("sms_body", content + url0);//断地址链接转换失败
                                startActivity(intent);

                            }
                        });


                    } catch (UnsupportedEncodingException mE) {
                        mE.printStackTrace();
                    }


                }
            });

        }


    }


    class PersonInvite {

        String name; //姓名
        String number;//电话号码

    }


}
