package com.chewuwuyou.rong.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.LogisticsCompanyAdapter;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformation;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.bean.LogisticsCompany;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.ui.LogisticsCompanyActivtiy;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.CharacterParser;
import com.chewuwuyou.app.widget.PinyinComparator;
import com.chewuwuyou.app.widget.PinyinGroupComparator;
import com.chewuwuyou.app.widget.SideBar;
import com.chewuwuyou.rong.adapter.TransferGroupAdapter;
import com.chewuwuyou.rong.bean.WholeGroup;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransferGroupManagementActivtiy extends CDDBaseActivity implements View.OnClickListener {


    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//标题
    private TextView mTitel;
    @ViewInject(id = R.id.network_request)//网络动画
    private LinearLayout mNetworkRequest;
    @ViewInject(id = R.id.network_abnormal_layout)//网络访问
    private LinearLayout mNetworkAbnormalLayout;
    @ViewInject(id = R.id.country_lvcountry)//群列表
    private ListView mCountryLvcountry;
    @ViewInject(id = R.id.dialog)//选中的字母
    private TextView mDialog;
    @ViewInject(id = R.id.sidrbar)//字母点击事件
    private SideBar mSidrbar;
    @ViewInject(id = R.id.group_search)//字母点击事件
    private LinearLayout mGroupSearch;

    private CharacterParser characterParser; //实例化汉字转拼音类

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinGroupComparator pinyinComparator;

    private TransferGroupAdapter mTransferGroupAdapter;//群适配器

    private List<GroupSetUpMemberInformationEr> mwholeGroups;//群列表

    private String  groupId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_group_management);
        initView();
        initData();
        initEvent();
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()){
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;
            case R.id.group_search://搜索
                intent = new Intent(TransferGroupManagementActivtiy.this,GroupSearchActivtiy.class);
                intent.putExtra("search_type", Constant.GROUP_MANGMENT_SEARCH);
                intent.putExtra("groupName",getIntent().getStringExtra("groupName"));
                intent.putExtra("groupId",getIntent().getStringExtra("groupId"));
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivityForResult(intent, 1);
                break;
        }
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mTitel.setText("移交管理权");
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinGroupComparator();
        mSidrbar.setTextView(mDialog);
        mwholeGroups = new ArrayList<GroupSetUpMemberInformationEr>();

    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        GroupMember();//访问数据得到群成员
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mGroupSearch.setOnClickListener(this);
        //设置右侧触摸监听
        mSidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mTransferGroupAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mCountryLvcountry.setSelection(position);
                }
            }
        });
        mCountryLvcountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(getIntent().getStringExtra("id").equals(mwholeGroups.get(position).getAccid())){
                    ToastUtil.toastShow(TransferGroupManagementActivtiy.this,"管理权不能移交给群主");
                }else{
                    dialog("","确认选择"+"”"+mwholeGroups.get(position).getUser_friend_name()+"“"+"为新群主，你将自动放弃群主身份","");
                    groupId = mwholeGroups.get(position).getAccid();
                }

            }
        });
    }


    /**
     * 提示用户是否进行操作
     */
    public void dialog(String title, String context, final String txet) {
        new com.chewuwuyou.app.utils.AlertDialog(TransferGroupManagementActivtiy
                .this).builder().setTitle(title)
                .setMsg(context)
                .setPositiveButton("取消", new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                    }
                }).setNegativeButton("确定", new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                replaceGroup(groupId);
            }
        }).show();
    }

    private void GroupMember(){
        AjaxParams params = new AjaxParams();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("userId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.GROUP_SET_UP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {super.onSuccess(o);

                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(TransferGroupManagementActivtiy.this,jsonObject.optInt("code"),jsonObject.optString("message"));
                    if(jsonObject.getString("code").equals("0")){
                        GroupSetUpMemberInformation mGroupSetUpMemberInformation = GroupSetUpMemberInformation.parse(o);
                        // 根据a-z进行排序源数据
                        mwholeGroups.addAll(filledData(mGroupSetUpMemberInformation.getData()));
                        Collections.sort(mwholeGroups, pinyinComparator);
                        mTransferGroupAdapter = new TransferGroupAdapter(mwholeGroups,TransferGroupManagementActivtiy.this);
                        mCountryLvcountry.setAdapter(mTransferGroupAdapter);
                    }else{
                        ToastUtil.toastShow(TransferGroupManagementActivtiy.this,jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(TransferGroupManagementActivtiy.this,"请求失败");
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 更换群主
     */
    private void replaceGroup(String mainId){
            AjaxParams params = new AjaxParams();
            params.put("id", getIntent().getStringExtra("groupId"));
            params.put("userId", CacheTools.getUserData("rongUserId"));
            params.put("groupMain", mainId);
            NetworkUtil.get(NetworkUtil.GROUP_NOTICE, params, new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String o) {
                    super.onSuccess(o);
                    try {
                        JSONObject jsonObject = new JSONObject(o);
                        if(jsonObject.getString("code").equals("0")){
                            ToastUtil.toastShow(TransferGroupManagementActivtiy.this, "移交成功");
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finishActivity();
                        }else{
                            ToastUtil.toastShow(TransferGroupManagementActivtiy.this,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    ToastUtil.toastShow(TransferGroupManagementActivtiy.this,"移交失败");
                }
            });
    }
    /**
     * 为ListView填充数据
     *
     * @param
     * @return
     */
    private List<GroupSetUpMemberInformationEr> filledData(List<GroupSetUpMemberInformationEr> mAccountList) {
        List<GroupSetUpMemberInformationEr> mSortList = new ArrayList<GroupSetUpMemberInformationEr>();

        String Name = "";
        for (int i = 0; i < mAccountList.size(); i++) {
            GroupSetUpMemberInformationEr sortModel = new GroupSetUpMemberInformationEr();
            if(!TextUtils.isEmpty(mAccountList.get(i).getUser_friend_name())){
                Name = mAccountList.get(i).getUser_friend_name();
            }else if(!TextUtils.isEmpty(mAccountList.get(i).getUser_group_name())){
                Name = mAccountList.get(i).getUser_group_name();
            }else if(!TextUtils.isEmpty(mAccountList.get(i).getUser_group_name())){
                Name = mAccountList.get(i).getUser_own_name();
            }else{
                Name = "1";
            }
            sortModel.setUser_friend_name(Name);
            sortModel.setAccid(mAccountList.get(i).getAccid());
            sortModel.setHead_image_url(mAccountList.get(i).getHead_image_url());
            sortModel.setSortLetters(mAccountList.get(i).getSortLetters());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(Name);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            switch (requestCode){
                case 1:
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finishActivity();
                    break;
            }
        }
    }
}
