package com.chewuwuyou.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.barcode.view.SweepListView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.BlackListAdapter;
import com.chewuwuyou.app.bean.BlackList;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.CharacterParser;

import com.chewuwuyou.app.widget.SideBar;
import com.chewuwuyou.eim.manager.XmppConnectionManager;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.privacy.PrivacyList;
import org.jivesoftware.smackx.privacy.PrivacyListManager;
import org.jivesoftware.smackx.privacy.packet.PrivacyItem;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BlacklistActivity extends CDDBaseActivity implements View.OnClickListener {


    @ViewInject(id = R.id.sub_header_bar_left_ibtn) //返回上一页
    private ImageButton mBarLeftIbtn;
    @ViewInject(id = R.id.dialog)//选择字母提示
    private TextView mDialog;
    @ViewInject(id = R.id.sub_header_bar_tv)//标题
    private TextView mTitleTV;
    @ViewInject(id = R.id.lv_contacts)//物流公司名称
    private SweepListView mListView;
    @ViewInject(id = R.id.sidrbar)//a-z字母排序
    private SideBar mSideBar;
    @ViewInject(id = R.id.et_search)
    private EditText mEditText;
    @ViewInject(id = R.id.textview_empty)
    private TextView mTextView_empty;
    private CharacterParser characterParser; //实例化汉字转拼音类

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private BlackListAdapter mAdapter;
    private List<BlackList> mLists = new ArrayList<>();
    private List<BlackList> mListsCopy = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);

        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();

    }

    @Override
    protected void initView() {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSideBar.setTextView(mDialog);
        mTitleTV.setText("黑名单管理");
        mAdapter = new BlackListAdapter(mLists, this);

    }

    @Override
    protected void initData() {
        mListView.setAdapter(mAdapter);

    }

    private void getData() {

        AjaxParams params = new AjaxParams();
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        //获取数据
                        List<BlackList> list = BlackList.parseList(String.valueOf(msg.obj));
                        mLists.clear();
                        mListsCopy.clear();
                        if (list.size() > 0) {
                            mLists.addAll(list);
                            mListsCopy.addAll(list);
                        }
                        Collections.sort(mLists, pinyinComparator);
                        mAdapter.notifyDataSetChanged();
                        break;

                    default:
                        break;
                }
            }
        }, null, NetworkUtil.GET_ALL_BLACKFRIEND, false, 0);


    }

    @Override
    protected void initEvent() {
        mBarLeftIbtn.setOnClickListener(this);
        //设置右侧触摸监听
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String content = mEditText.getText().toString();
                filterData(content);
                mListView.setSelection(0);

            }
        });


        mAdapter.setOnDeleteListener(new BlackListAdapter.onDeleteListener() {
            @Override
            public void onDelete(final int posion) {
                //   xmppRemove(mLists.get(posion).getOther());

                //移除黑名单
                final ProgressDialog mProgressDialog = ProgressDialog.show(BlacklistActivity.this, null, "请稍后……", false, true);

                AjaxParams params = new AjaxParams();
                params.put("userId", CacheTools.getUserData("rongUserId"));
                params.put("friendId", mLists.get(posion).getOther());
                params.put("groupId", "1");

                NetworkUtil.get(NetworkUtil.REMOVE_FIREND_TO_ZU, params, new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        super.onSuccess(s);


                        try {

                            JSONObject j = new JSONObject(s);
                            ErrorCodeUtil.doErrorCode(BlacklistActivity.this, j.getInt("code"), j.getString("message"));

                            mProgressDialog.dismiss();
                            MyLog.i("移除黑名单");
                            mLists.remove(posion);
                            mListsCopy.remove(posion);
                            mAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        mProgressDialog.dismiss();

                        ToastUtil.toastShow(BlacklistActivity.this, "移除失败");
                    }
                });


            }


        });
        //监听List的数据变化
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mLists.size() == 0)
                    mTextView_empty.setVisibility(View.VISIBLE);
                else
                    mTextView_empty.setVisibility(View.GONE);
//                if (mListsCopy.size() == 0)
//            //        mEditText.setVisibility(View.GONE);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent mIntent = new Intent(BlacklistActivity.this, PersonalHomeActivity2.class);
                mIntent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, Integer.valueOf(mLists.get(position - 1).getOther())+"");
                startActivity(mIntent);

            }
        });

    }

    private boolean xmppRemove(String name) {
        // 删除黑名单

        try {
            PrivacyListManager privacyManager = PrivacyListManager
                    .getInstanceFor(XmppConnectionManager.getInstance().getConnection());
            if (privacyManager == null) {
                return false;
            }
            PrivacyList plist = null;
            plist = privacyManager.getPrivacyList(Constant.Black_List);

            if (plist != null) {
                String ser = "@" + XmppConnectionManager.getInstance().getConnection().getServiceName();

                List<PrivacyItem> items = plist.getItems();
                MyLog.i("", "items size=" + items.size());
                for (PrivacyItem item : items) {
//                    String from = item.getValue().substring(0,
//                            item.getValue().indexOf(ser));
//                    MyLog.i("",
//                            "deleteFromPrivacyList item.getValue="
//                                    + item.getValue());
                    if (item.getValue().equalsIgnoreCase(name)) {
                        MyLog.i("", "deleteFromPrivacyList find object");
                        items.remove(item);
                        break;
                    }
                }
                MyLog.i("", "deleteFromPrivacyList items size=" + items.size());
                privacyManager.updatePrivacyList(Constant.Black_List, items);
            }
        } catch (XMPPException ex) {
        } catch (SmackException.NotConnectedException mE) {
            mE.printStackTrace();
        } catch (SmackException.NoResponseException mE) {
            mE.printStackTrace();
        }
        return true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
        }

    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */

    private void filterData(String filterStr) {
        MyLog.i("过滤数据");
        mLists.clear();
        if (TextUtils.isEmpty(filterStr)) {
            MyLog.i("还原数据--->" + mLists.size());
            mLists.addAll(mListsCopy);
        } else {

            for (BlackList mBlackList : mListsCopy) {
                String name = mBlackList.getNickName();
                String ddang = mBlackList.getDangDangHao();
                if (ddang.contains(filterStr) || name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    mLists.add(mBlackList);
                }
            }
        }

        MyLog.i("现数据--->" + mLists.size());
        // 根据a-z进行排序
        Collections.sort(mLists, pinyinComparator);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * @author xiaanming
     */
    public class PinyinComparator implements Comparator<BlackList> {

        @Override
        public int compare(BlackList o1, BlackList o2) {
            if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#")) {
                return -1;
            } else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@")) {
                return 1;
            } else {
                return o1.sortLetters.compareTo(o2.sortLetters);
            }
        }
    }


    public static void launch(Context mContext, Bundle mBundle) {

        Intent i = new Intent(mContext, BlacklistActivity.class);
        if (mBundle != null) {
            i.putExtras(mBundle);
        }
        mContext.startActivity(i);

    }


}
