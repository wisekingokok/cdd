package com.chewuwuyou.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.GroupAggregateAdapter;
import com.chewuwuyou.app.bean.AllGroup;
import com.chewuwuyou.app.bean.SearchGroup;
import com.chewuwuyou.app.bean.SearchGroupData;
import com.chewuwuyou.app.bean.Userfriend;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.SelectFirentDialog;
import com.chewuwuyou.rong.adapter.SearchGroupAdapter;
import com.chewuwuyou.rong.bean.WholeGroup;
import com.chewuwuyou.app.transition_view.activity.RongChatActivity;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class GroupSearchFragment extends BaseFragment implements View.OnClickListener{

    private ListView mGroupSearcheList;//群列表
    private TextView mCancel;//取消搜索
    private ImageButton mClear;//清空输入内容
    private EditText mSearchET;//搜索群
    private TextView mGroupContext;//没有结果
    private SearchGroupAdapter mGroupListAdapt;
    private List<SearchGroupData> mwholeGroup;//我创建
    private List<Userfriend> mAllGroups;
    private GroupAggregateAdapter adapter;
    private LinearLayout mGroupMember;

    private String groupId;
    private String griupName;
    private View view;

    private String mSearchType;//接收传递的类型
    private String mGroupName;
    private String mGroupId;
    private String mGroup;
    private List<AllGroup> mAllGroup;
    private String mId;
    private Message message;

    public GroupSearchFragment(String mSearchType,String mGroupName,String mGroupId,String mGroup,List<AllGroup> mAllGroup,String id,Message message){
        this.mSearchType = mSearchType;
        this.mGroupName = mGroupName;
        this.mGroupId = mGroupId;
        this.mGroup = mGroup;
        this.mAllGroup = mAllGroup;
        this.mId = id;
        this.message = message;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group_search, container, false);
        initView();
        initData();
        initEvent();
        return view;
    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel://取消搜索
                getActivity().finish();
                break;
            case R.id.clear://清空输入内容
                if(!TextUtils.isEmpty(mSearchET.getText().toString())){
                    mSearchET.setText("");
                    mwholeGroup.clear();
                    if(mGroupListAdapt!=null){
                        mGroupListAdapt.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mGroupSearcheList = (ListView) view.findViewById(R.id.group_searche_list);
        mCancel = (TextView) view.findViewById(R.id.cancel);
        mClear = (ImageButton) view.findViewById(R.id.clear);
        mSearchET = (EditText) view.findViewById(R.id.searchET);
        mGroupContext = (TextView) view.findViewById(R.id.group_context);
        mGroupMember = (LinearLayout) view.findViewById(R.id.group_member);

        mwholeGroup = new ArrayList<SearchGroupData>();
        mAllGroups = new ArrayList<Userfriend>();
        if (!TextUtils.isEmpty(mSearchType) && mSearchType.equals(Constant.GROUP_FRIENDS)) {
            adapter = new GroupAggregateAdapter(getActivity(), mAllGroups);
            mGroupSearcheList.setAdapter(adapter);
        } else if (mSearchType.equals(Constant.GROUP_SETUP_SEARCH) || mSearchType.equals(Constant.GROUP_MANGMENT_SEARCH) || mSearchType.equals(Constant.DELETE_GROUP)) {
            mGroupListAdapt = new SearchGroupAdapter(mwholeGroup, getActivity());
            mGroupSearcheList.setAdapter(mGroupListAdapt);
        } else if (mSearchType.equals(Constant.GROUP_LIST_SEARCH)) {
            mGroupMember.setVisibility(View.GONE);
            mGroupListAdapt = new SearchGroupAdapter(mwholeGroup, getActivity());
            mGroupSearcheList.setAdapter(mGroupListAdapt);
        }
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
    }

    /**
     * 监听事件
     */
    @Override
    protected void initEvent() {
        mClear.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        mGroupSearcheList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if(!TextUtils.isEmpty(mGroup)&&mGroup.equals(Constant.GROUP_ZHUANFA)){
                    WholeGroup wholeGroup = new WholeGroup();
                    wholeGroup.setId(Integer.parseInt(mwholeGroup.get(position).getGroup_id()));
                    wholeGroup.setGroupName(mwholeGroup.get(position).getGroup_name());
                    if(!TextUtils.isEmpty(mwholeGroup.get(position).getGroup_img_url())){
                        wholeGroup.setGroupImgUrl(mwholeGroup.get(position).getGroup_img_url());
                    }else{
                        wholeGroup.setGroupImgUrl(mwholeGroup.get(position).getHead_image_url());
                    }
                    wholeGroup.setCreatedAt(mwholeGroup.get(position).getCreated_at());
                    wholeGroup.setUpdatedAt(mwholeGroup.get(position).getUpdated_at());
                    wholeGroup.setRemarks(mwholeGroup.get(position).getRemarks());
                    SelectFirentDialog selectFirentDialog = SelectFirentDialog.getIntense(message,wholeGroup);
                    selectFirentDialog.show(getFragmentManager(), "SelectFirentDialog");
                    selectFirentDialog.setFinishCallback(new SelectFirentDialog.FinishCallback() {
                        @Override
                        public void finishActivity(boolean isFinishActivity) {
                            if(isFinishActivity){
                                getActivity().finish();
                            }
                        }
                    });
                }else if (mSearchType.equals(Constant.GROUP_LIST_SEARCH)) {
                    intent = new Intent(getActivity(), RongChatActivity.class);
                    intent.putExtra(RongChatMsgFragment.KEY_TARGET, String.valueOf(mwholeGroup.get(position).getGroup_id()));
                    intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.GROUP);
                    startActivity(intent);
                }else if (mSearchType.equals(Constant.GROUP_SETUP_SEARCH)) {//搜索群成员
                    intent = new Intent(getActivity(), PersonalHomeActivity2.class);
                    intent.putExtra("new_user_id", mwholeGroup.get(position).getAccid());
                    startActivity(intent);
                } else if (mSearchType.equals(Constant.GROUP_FRIENDS)) {
                    intent = new Intent();
                    intent.putExtra("userId", mAllGroups.get(position).getUserId());
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                } else if (mSearchType.equals(Constant.DELETE_GROUP)) {
                    intent = new Intent();
                    intent.putExtra("userId",mwholeGroup.get(position).getAccid());
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }else{//移交管理权
                    if (mId.equals(mwholeGroup.get(position).getAccid())) {
                        ToastUtil.toastShow(getActivity(), "管理权不能移交群主");
                    } else {
                        groupId = mwholeGroup.get(position).getAccid();
                        dialog("", "确认选择" + "”" + mwholeGroup.get(position).getUser_group_name() + "“" + "为新群主，你将自动放弃群主身份", "");
                    }
                }
            }
        });


        /**
         * 监听输入的内容
         */
        mSearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSearchType.equals(Constant.GROUP_LIST_SEARCH)) {
                    if (!TextUtils.isEmpty(s.toString().trim())) {
                        searchGroup(s.toString().trim());
                    }
                } else if (mSearchType.equals(Constant.GROUP_SETUP_SEARCH) || mSearchType.equals(Constant.GROUP_MANGMENT_SEARCH)||mSearchType.equals(Constant.DELETE_GROUP)) {//移交管理权
                    if(TextUtils.isEmpty(mSearchET.getText().toString())){
                        mGroupMember.setVisibility(View.VISIBLE);
                        mGroupContext.setVisibility(View.GONE);
                        return;
                    }else{
                        mGroupMember.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(s.toString().trim())) {
                        searchGroupMember(s.toString().trim());
                    }
                } else if (mSearchType.equals(Constant.GROUP_FRIENDS)) {
                    if(TextUtils.isEmpty(mSearchET.getText().toString())){
                        mGroupMember.setVisibility(View.VISIBLE);
                        mGroupContext.setVisibility(View.GONE);
                        return;
                    }else{
                        mGroupMember.setVisibility(View.GONE);
                    }
                    if(mAllGroups!=null){
                        mAllGroups.clear();
                        String nam;
                        for (int i = 0; i < mAllGroup.size(); i++)
                            for (int j = 0; j < mAllGroup.get(i).getFriends().size(); j++) {
                                if (!TextUtils.isEmpty(mAllGroup.get(i).getFriends().get(j).getNickname())) {
                                    nam = mAllGroup.get(i).getFriends().get(j).getNickname();
                                } else {
                                    nam = mAllGroup.get(i).getFriends().get(j).getName();
                                }
                                if (nam.contains(s.toString())) {
                                    Userfriend userfriend = new Userfriend();
                                    userfriend.setNickname(mAllGroup.get(i).getFriends().get(j).getNickname());
                                    userfriend.setName(mAllGroup.get(i).getFriends().get(j).getName());
                                    userfriend.setPortraitUri(mAllGroup.get(i).getFriends().get(j).getPortraitUri());
                                    userfriend.setUserId(mAllGroup.get(i).getFriends().get(j).getUserId());
                                    mAllGroups.add(userfriend);
                                }
                            }
                        if(mAllGroups.size()==0){
                            mGroupContext.setVisibility(View.VISIBLE);
                        }else{
                            mGroupContext.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }

    /**
     * 搜索群
     */
    private void searchGroup(String groupName) {

        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("groupName", groupName);
        NetworkUtil.get(NetworkUtil.GROUP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                mwholeGroup.clear();
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(getActivity(),jsonObject.optInt("code"),jsonObject.optString("message"));
                    if(jsonObject.getString("code").equals("0")){
                        if (!TextUtils.isEmpty(jsonObject.getString("data").toString())) {
                            SearchGroup searchGroup = SearchGroup.parse(o.toString());
                            if(searchGroup.getData().size()==0){
                                mGroupContext.setVisibility(View.VISIBLE);
                            }else{
                                mGroupContext.setVisibility(View.GONE);
                            }
                            mwholeGroup.addAll(searchGroup.getData());
                            mGroupListAdapt.notifyDataSetChanged();
                        }
                    }else{
                        ToastUtil.toastShow(getActivity(),jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(getActivity(), "未搜索结果");
            }
        });
    }

    /**
     * 搜索群成员
     */
    private void searchGroupMember(String groupName) {
        AjaxParams params = new AjaxParams();
        params.put("groupId", mGroupId);
        params.put("groupName", mGroupName);
        params.put("remarkName", groupName);
        NetworkUtil.get(NetworkUtil.GROUP_MEMBER, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                mwholeGroup.clear();
                try {

                    System.out.println("+++++++++++++++++"+o);

                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(getActivity(),jsonObject.optInt("code"),jsonObject.optString("message"));
                    if(jsonObject.getString("code").equals("0")){
                        if (!TextUtils.isEmpty(jsonObject.getString("data").toString())) {
                            SearchGroup searchGroup = SearchGroup.parse(o.toString());
                            if(searchGroup.getData().size()==0){
                                mGroupContext.setVisibility(View.VISIBLE);
                            }else{
                                mGroupContext.setVisibility(View.GONE);
                            }
                            mwholeGroup.addAll(searchGroup.getData());
                            mGroupListAdapt.notifyDataSetChanged();
                        }else{
                            mGroupContext.setVisibility(View.VISIBLE);
                        }
                    }else{
                        ToastUtil.toastShow(getActivity(),jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(getActivity(), "未搜索结果");
            }
        });
    }

    /**
     * 提示用户是否进行操作
     */
    private void dialog(String title, String context, final String txet) {
        new com.chewuwuyou.app.utils.AlertDialog(getActivity()).builder().setTitle(title)
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

    /**
     * 更换群主
     */
    private void replaceGroup(String mainId){
        AjaxParams params = new AjaxParams();
        params.put("id", mGroupId);
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("groupMain", mainId);
        NetworkUtil.get(NetworkUtil.GROUP_HEAD, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(getActivity(),jsonObject.optInt("code"),jsonObject.optString("message"));
                    if(jsonObject.getString("code").equals("0")){
                        Intent intent = new Intent();
                        getActivity().setResult(getActivity().RESULT_OK, intent);
                        getActivity().finish();
                    }else{
                        ToastUtil.toastShow(getActivity(),jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(getActivity(), "移交失败");
            }
        });
    }
}
