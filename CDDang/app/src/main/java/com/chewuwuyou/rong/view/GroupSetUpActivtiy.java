package com.chewuwuyou.rong.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.GroupSetUpMemberAdapter;
import com.chewuwuyou.app.bean.GroupSetUpEssential;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformation;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.TokenObtain;
import com.chewuwuyou.app.widget.MyGridView;
import com.chewuwuyou.rong.bean.ChangeGroupBean;
import com.chewuwuyou.rong.bean.ClearMessagesBean;
import com.chewuwuyou.rong.bean.RefreshBean;
import com.chewuwuyou.rong.utils.RongApi;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.ielse.view.SwitchView;
import de.greenrobot.event.EventBus;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 群设置
 * liuchun
 */
public class GroupSetUpActivtiy extends CDDBaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//订单标题
    private TextView mTitel;
    @ViewInject(id = R.id.group_member)
    private MyGridView mGroupMember;//群成员
    @ViewInject(id = R.id.all_group_lay)
    private LinearLayout mAllGroup;//所有群组员
    @ViewInject(id = R.id.group_name)
    private TextView mGroupName;//群昵称
    @ViewInject(id = R.id.group_notice)
    private TextView mGroupNotice;//群公告
    @ViewInject(id = R.id.switchView)
    private SwitchView mSwitchView;//屏蔽群消息
    @ViewInject(id = R.id.group_nickname)
    private TextView mGroupNickName;//群昵称
    //    @ViewInject(id = R.id.group_file)
//    private TextView mGroupFile;//群文件
//    @ViewInject(id = R.id.group_chat_record)
//    private TextView mGroupChatRecord;//聊天记录
    @ViewInject(id = R.id.delete_chat_record)
    private LinearLayout mDeleteChatRecord;//删除聊天记录
    @ViewInject(id = R.id.exit_group)
    private Button mExitGroup;//退出群
    @ViewInject(id = R.id.group_portrait_lay)
    private LinearLayout mGroupPortraitLay;
    @ViewInject(id = R.id.group_portrait)
    private ImageView mGroupPortrait;//显示群头像
    @ViewInject(id = R.id.group_name_lay)
    private LinearLayout mGroupNameLay;//群名称
    @ViewInject(id = R.id.group_notice_lay)
    private LinearLayout mGroupNoticeLay;//群公告
    @ViewInject(id = R.id.group_nickname_lay)
    private LinearLayout mGroupNicknameLay;//群昵称
    @ViewInject(id = R.id.network_request)//网络动画
    private LinearLayout mNetworkRequest;
    @ViewInject(id = R.id.network_abnormal_layout)//网络访问
    private LinearLayout mNetworkAbnormalLayout;
    @ViewInject(id = R.id.group_grew_tv)
    private TextView mGroupGrewTV;//群成员
    @ViewInject(id = R.id.group_notice_message)
    private TextView mGroupNoticeMessageTV;//群信息
    @ViewInject(id = R.id.group_notice_message_lay)
    private LinearLayout mGroupNoticeMessageLAY;//群信息
    @ViewInject(id = R.id.administration_group_lay)
    private LinearLayout mAdministrationGroupLAY;//移交管理权
    @ViewInject(id = R.id.network_again)//重新加载
    private TextView mNetworkAgain;
    @ViewInject(id = R.id.group_right)//重新加载
    private ImageView mGroupRight;


    private GroupSetUpMemberAdapter memberAdapter;
    private List<GroupSetUpMemberInformationEr> mAccountList;
    private GroupSetUpEssential mGroupSetUpEssential;
    private int maskMessage;//判断是否屏蔽消息

    private final int GROUP_NOTICE = 10;//群公告
    private final int GROUP_NAME = 20;//群名称
    private final int GROUP_NICKNAME = 30;//群昵称
    private final int TRANSFER_GROUP = 40;//移交管理权
    private final int ADD_GROUP = 50;//添加成员
    private final int DELETE_GROUP = 60;//删除成员
    private Dialog dialog;

    private String dataCityUrl = "";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_setup_ac);
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
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();//返回上一页
                break;
            case R.id.group_portrait_lay://查看群头像
                if (TextUtils.isEmpty(CacheTools.getUserData("qiniutoken"))) {
                    TokenObtain tokenObtain = new TokenObtain();
                    tokenObtain.Group(GroupSetUpActivtiy.this);
                }
                if (mGroupSetUpEssential.getGroup_main().equals("0")) {
                    intent = new Intent(GroupSetUpActivtiy.this, MultiImageSelectorActivity.class);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                    startActivityForResult(intent, 2);
                } else {
                    ToastUtil.toastShow(GroupSetUpActivtiy.this, "您当前没有权限修改群图片");
                }
                break;
            case R.id.group_name_lay://查看群名称
                if (mGroupSetUpEssential.getGroup_main().equals("0")) {
                    intent = new Intent(this, SetUpGroupNickNameaActivtiy.class);
                    intent.putExtra("group_name", "0");
                    intent.putExtra("mingc", mGroupName.getText().toString().trim());
                    intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                    startActivityForResult(intent, GROUP_NAME);
                } else {
                    ToastUtil.toastShow(GroupSetUpActivtiy.this, "您当前没有权限修改群名称");
                }
                break;
            case R.id.group_notice_lay://查看群公告

                if (mGroupSetUpEssential.getGroup_main().equals("0")) {
                    intent = new Intent(this, GroupNoticeActivtiy.class);
                    if (!TextUtils.isEmpty(mGroupSetUpEssential.getGroup_announcement())) {
                        intent.putExtra("gonggao", mGroupNoticeMessageTV.getText().toString());
                    }
                    intent.putExtra("groupmain", mGroupSetUpEssential.getGroup_main());
                    intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                    startActivityForResult(intent, GROUP_NOTICE);
                } else {
                    if (TextUtils.isEmpty(mGroupSetUpEssential.getGroup_announcement())) {
                        ToastUtil.toastShow(GroupSetUpActivtiy.this, "暂无公告");
                    } else {
                        intent = new Intent(this, GroupNoticeActivtiy.class);
                        if (!TextUtils.isEmpty(mGroupSetUpEssential.getGroup_announcement())) {
                            intent.putExtra("gonggao", mGroupNoticeMessageTV.getText().toString());
                        }
                        intent.putExtra("groupSize", mGroupSetUpEssential.getMemberCount());
                        intent.putExtra("groupNamae", mGroupSetUpEssential.getGroup_name());
                        intent.putExtra("groupmain", mGroupSetUpEssential.getGroup_main());
                        intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                        startActivityForResult(intent, GROUP_NOTICE);
                    }
                }

                break;
            case R.id.all_group_lay://查看所有群成员
                intent = new Intent(this, GroupMemberListActivtiy.class);
                intent.putExtra("groupName", mGroupName.getText().toString());
                intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                startActivity(intent);
                break;
            case R.id.group_nickname_lay://设置在群里面显得昵称
                intent = new Intent(this, SetUpGroupNickNameaActivtiy.class);
                intent.putExtra("group_name", "1");
                intent.putExtra("nic", mGroupNickName.getText().toString().trim());
                intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                startActivityForResult(intent, GROUP_NICKNAME);
                break;
//            case R.id.group_file://群文件
//                intent = new Intent(this, GroupFileAtivtiy.class);
//                startActivity(intent);
//                break;
//            case R.id.group_chat_record://聊天记录
//
//                break;
            case R.id.administration_group_lay://移交管理权
                if (mGroupSetUpEssential.getGroup_main().equals("0")) {
                    intent = new Intent(this, TransferGroupManagementActivtiy.class);
                    intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                    intent.putExtra("id", String.valueOf(mAccountList.get(0).getAccid()));
                    intent.putExtra("groupName", mGroupName.getText().toString());
                    startActivityForResult(intent, TRANSFER_GROUP);
                }
                break;
            case R.id.network_again://重新加载
                mNetworkRequest.setVisibility(View.VISIBLE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                GroupBasic();
                break;
            case R.id.delete_chat_record://删除聊天记录
//                intent = new Intent(this, TransferGroupManagementActivtiy.class);
//                startActivity(intent);
                GroupOperation(0);
                break;
            case R.id.exit_group://退出群
                GroupOperation(1);
                break;
        }
    }

    /**
     * 初始化界面
     */
    @Override
    protected void initView() {
        mTitel.setText("群设置");
        mAccountList = new ArrayList<GroupSetUpMemberInformationEr>();
        // 注册EventBus
        EventBus.getDefault().register(this);

        MyLog.e("YUY", "群组ID = " + getIntent().getStringExtra("groupId"));
        RongApi.getConversationNotificationStatus(Conversation.ConversationType.GROUP, getIntent().getStringExtra("groupId"), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                switch (conversationNotificationStatus) {
                    case DO_NOT_DISTURB:
                        mSwitchView.setOpened(true);
                        break;
                    case NOTIFY:
                        mSwitchView.setOpened(false);
                        break;
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });

        mSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RongApi.setConversationNotificationStatus(Conversation.ConversationType.GROUP, getIntent().getStringExtra("groupId"), !mSwitchView.isOpened() ? Conversation.ConversationNotificationStatus.NOTIFY : Conversation.ConversationNotificationStatus.DO_NOT_DISTURB, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        switch (conversationNotificationStatus) {
                            case DO_NOT_DISTURB:
                                groupShield("1");
                                break;
                            case NOTIFY:
                                groupShield("0");
                                break;
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toast.makeText(GroupSetUpActivtiy.this, "设置失败,请重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {


    }


    /**
     * 屏蔽
     */
    private void groupShield(final String isShield) {
        AjaxParams params = new AjaxParams();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("maskMessage", isShield);
        NetworkUtil.get(NetworkUtil.SHIELD_GROUP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                if (isShield.equals("0")) {
                    mSwitchView.setOpened(false);
                } else {
                    mSwitchView.setOpened(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GroupBasic();//访问数据
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mAllGroup.setOnClickListener(this);
        mAdministrationGroupLAY.setOnClickListener(this);
        mNetworkAgain.setOnClickListener(this);

//        mGroupFile.setOnClickListener(this);
//        mGroupChatRecord.setOnClickListener(this);
        mDeleteChatRecord.setOnClickListener(this);
        mExitGroup.setOnClickListener(this);
        mGroupPortraitLay.setOnClickListener(this);
        mGroupNameLay.setOnClickListener(this);
        mGroupNoticeLay.setOnClickListener(this);
        mGroupNicknameLay.setOnClickListener(this);


        mGroupMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;
                if (mAccountList.get(position).getAccid().equals("gengduo")) {
                    intent = new Intent(GroupSetUpActivtiy.this, GroupMemberListActivtiy.class);
                    intent.putExtra("groupName", mGroupName.getText().toString());
                    intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                    startActivity(intent);
                } else if (mAccountList.get(position).getAccid().equals("jia")) {
                    intent = new Intent(GroupSetUpActivtiy.this, EstablishGroupFirstStepActivtiy.class);
                    intent.putExtra("type", Constant.GROUP_SET_UP);
                    intent.putExtra("groupName", mGroupName.getText().toString());
                    intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                    startActivityForResult(intent, ADD_GROUP);
                } else if (mAccountList.get(position).getAccid().equals("jian")) {
                    intent = new Intent(GroupSetUpActivtiy.this, DeleteGroupMember.class);
                    intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                    intent.putExtra("groupName", mGroupName.getText().toString());
                    intent.putExtra("id", String.valueOf(mAccountList.get(0).getAccid()));
                    startActivityForResult(intent, DELETE_GROUP);
                } else {
                    intent = new Intent(GroupSetUpActivtiy.this, PersonalHomeActivity2.class);
                    intent.putExtra("new_user_id", mAccountList.get(position).getAccid());
                    startActivity(intent);
                }

            }
        });
    }


    private void GroupMember() {
        AjaxParams params = new AjaxParams();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("userId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.GROUP_SET_UP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);

                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                mAccountList.clear();

                try {
                    JSONObject object = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(GroupSetUpActivtiy.this, object.optInt("code"), object.optString("message"));
                    if (object.getString("code").equals("0")) {
                        List<GroupSetUpMemberInformationEr> data = new ArrayList<GroupSetUpMemberInformationEr>();
                        GroupSetUpMemberInformation mGroupSetUpMemberInformation = GroupSetUpMemberInformation.parse(o);
                        mAccountList.addAll(mGroupSetUpMemberInformation.getData());
                        if (mAccountList.size() > 30) {
                            data.add(new GroupSetUpMemberInformationEr("", "", "", "", "gengduo", ""));
                        }

                        if (mGroupSetUpEssential.getGroup_main().equals("0")) {
                            if (mGroupSetUpMemberInformation.getData().size() > 30) {
                                mAccountList = mGroupSetUpMemberInformation.getData().subList(0, 29);
                            }
                            data.add(new GroupSetUpMemberInformationEr("", "", "", "", "jia", ""));
                            if (mGroupSetUpMemberInformation.getData().size() > 1) {
                                data.add(new GroupSetUpMemberInformationEr("", "", "", "", "jian", ""));
                            }
                            mAccountList.addAll(data);
                        } else {
                            if (mGroupSetUpMemberInformation.getData().size() > 30) {
                                mAccountList = mGroupSetUpMemberInformation.getData().subList(0, 26);
                            }
                            data.add(new GroupSetUpMemberInformationEr("", "", "", "", "jia", ""));
                            mAccountList.addAll(data);
                        }
                        if (!mGroupSetUpEssential.getGroup_main().equals("0")) {
                            mGroupRight.setVisibility(View.GONE);
                        }
                        memberAdapter = new GroupSetUpMemberAdapter(GroupSetUpActivtiy.this, mAccountList);
                        mGroupMember.setAdapter(memberAdapter);
                    } else {
                        ToastUtil.toastShow(GroupSetUpActivtiy.this, object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(GroupSetUpActivtiy.this, "网络异常");
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void GroupBasic() {
        AjaxParams params = new AjaxParams();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("userId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.GROUP_BASIC, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(GroupSetUpActivtiy.this, jsonObject.optInt("code"), jsonObject.optString("message"));
                    if (jsonObject.getString("code").equals("0")) {

                        mGroupSetUpEssential = GroupSetUpEssential.parse(jsonObject.getJSONObject("data").toString());
                        ImageUtils.displayImage(mGroupSetUpEssential.getGroup_img_url(), mGroupPortrait, 10, R.drawable.user_fang_icon,
                                R.drawable.user_fang_icon);
                        mGroupName.setText(mGroupSetUpEssential.getGroup_name() + "  ");
                        mGroupNotice.setText(mGroupSetUpEssential.getMask_message() + "  ");
                        mGroupNickName.setText(mGroupSetUpEssential.getRemark_name() + "  ");
                        mGroupGrewTV.setText("全部群组员（" + mGroupSetUpEssential.getMemberCount() + "）");
                        maskMessage = Integer.parseInt(mGroupSetUpEssential.getMask_message());

                        if (TextUtils.isEmpty(mGroupSetUpEssential.getGroup_announcement())) {
                            mGroupNotice.setText("暂无公告  ");
                            mGroupNoticeMessageLAY.setVisibility(View.GONE);
                        } else {
                            mGroupNotice.setText("");
                            mGroupNoticeMessageLAY.setVisibility(View.VISIBLE);
                            mGroupNoticeMessageTV.setText(mGroupSetUpEssential.getGroup_announcement());
                        }
                        if (mGroupSetUpEssential.getGroup_main().equals("0")) {
                            mAdministrationGroupLAY.setVisibility(View.VISIBLE);
                        } else {
                            mAdministrationGroupLAY.setVisibility(View.GONE);
                        }
                        GroupMember();//访问网络接收群用户成员
                    } else {
                        ToastUtil.toastShow(GroupSetUpActivtiy.this, jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(GroupSetUpActivtiy.this, "网络异常");
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 群公告
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case GROUP_NOTICE://群公告
                    mGroupNotice.setText("");
                    mGroupNoticeMessageLAY.setVisibility(View.VISIBLE);
                    mGroupNoticeMessageTV.setText(data.getStringExtra("group_announcement"));
                    break;
                case GROUP_NAME://群名称
                    mGroupName.setText(data.getStringExtra("group_name") + "  ");
                    break;
                case GROUP_NICKNAME://群昵称
                    mGroupNickName.setText(data.getStringExtra("remark_name") + "  ");
                    break;
                case TRANSFER_GROUP://移交管理权
                    mAccountList.remove(mAccountList.size() - 1);
                    memberAdapter.notifyDataSetChanged();
                    mGroupSetUpEssential.setGroup_main("1");//改变管理员状态
                    mAdministrationGroupLAY.setVisibility(View.GONE);
                    break;
                case DELETE_GROUP://删除
                    mNetworkRequest.setVisibility(View.VISIBLE);//关闭网络动画
                    mNetworkAbnormalLayout.setVisibility(View.GONE);

                    GroupBasic();
                    break;
                case 2://选择图片
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (data != null) {
                        mGroupPortrait.setImageBitmap(FileUtils.getSmallBitmap(paths.get(0)));
                    }
                    if (!TextUtils.isEmpty(paths.get(0).toString().trim())) {
                        String sd = new TokenObtain().qiniuImg(GroupSetUpActivtiy.this, FileUtils.getSmallBitmap(paths.get(0).toString().trim()));//压缩图片
                        if (!TextUtils.isEmpty(sd)) {
                            EstablishGroup(GroupSetUpActivtiy.this, sd);//获取压缩后的图片并上传
                        }
                    }
                    break;
                case ADD_GROUP://添加群成员
                    mNetworkRequest.setVisibility(View.VISIBLE);//关闭网络动画
                    mNetworkAbnormalLayout.setVisibility(View.GONE);
                    GroupBasic();
                    break;

            }
        }
    }

    /**
     * 是否退群、是否清空聊天记录
     */
    public void GroupOperation(final int typeId) {
        dialog = new Dialog(this, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.dialog_group_chat_record, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle); // 添加动画

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);

        TextView mGroupEmptyTuichu = (TextView) layout.findViewById(R.id.group_empty_tuichu);
        TextView mGroupEmptyMessageTV = (TextView) layout.findViewById(R.id.group_empty_message);
        TextView mGroupEmptyCancelTV = (TextView) layout.findViewById(R.id.group_empty_cancel);

        if (typeId == 1) {
            mGroupEmptyTuichu.setVisibility(View.VISIBLE);
            mGroupEmptyMessageTV.setText("确认退出");
        }
        mGroupEmptyMessageTV.setOnClickListener(new View.OnClickListener() {//清空
            @Override
            public void onClick(View v) {

                if (typeId == 0) {
                    RongApi.clearMessages(Conversation.ConversationType.GROUP, getIntent().getStringExtra("groupId"), new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            EventBus.getDefault().post(new RefreshBean());

                            dialog.dismiss();
                            Toast.makeText(GroupSetUpActivtiy.this, "清除成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Toast.makeText(GroupSetUpActivtiy.this, "操作失败", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    if (mGroupSetUpEssential.getGroup_main().equals("0")) {
                        dissolution_Group();//解散群
                    } else {
                        SignOutGroup();//退出群
                    }
                }
            }
        });
        mGroupEmptyCancelTV.setOnClickListener(new View.OnClickListener() {//消息
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 退出群
     */
    private void SignOutGroup() {
        AjaxParams params = new AjaxParams();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("userIds", CacheTools.getUserData("rongUserId"));
        params.put("loginUserId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.DELETE_GROUP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(o);
                    if (object.getString("code").equals("0")) {
                        ToastUtil.toastShow(GroupSetUpActivtiy.this, "退群成功");
                        EventBus.getDefault().post(new GroupSetUpEssential());
                        EventBus.getDefault().post(new RefreshBean());
                        EventBus.getDefault().post(new ClearMessagesBean());
                        finishActivity();
                    } else {
                        ToastUtil.toastShow(GroupSetUpActivtiy.this, object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(GroupSetUpActivtiy.this, "退群失败");
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 群主解散群
     */
    private void dissolution_Group() {
        AjaxParams params = new AjaxParams();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("userId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.DISSOLUTION_GROUP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(o);
                    if (object.getString("code").equals("0")) {
                        ToastUtil.toastShow(GroupSetUpActivtiy.this, "退群成功");
                        EventBus.getDefault().post(new GroupSetUpEssential());
                        EventBus.getDefault().post(new RefreshBean());
                        EventBus.getDefault().post(new ClearMessagesBean());
                        ChangeGroupBean groupBean = new ChangeGroupBean();
                        groupBean.targetId = String.valueOf(mGroupSetUpEssential.getId());
                        EventBus.getDefault().post(groupBean);
                        finishActivity();
                    } else {
                        ToastUtil.toastShow(GroupSetUpActivtiy.this, object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(GroupSetUpActivtiy.this, "解散群失败");
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 修改群头像
     */
    private void mDataCityUrl() {
        AjaxParams params = new AjaxParams();
        params.put("id", getIntent().getStringExtra("groupId"));
        params.put("userId", String.valueOf(CacheTools.getUserData("rongUserId")));
        params.put("groupImgUrl", dataCityUrl);
        NetworkUtil.get(NetworkUtil.GROUP_HEAD, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                ToastUtil.toastShow(GroupSetUpActivtiy.this, "上传成功");
                mProgressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(o);
                    if (!object.getString("code").equals("0")) {
                        ToastUtil.toastShow(GroupSetUpActivtiy.this, object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(GroupSetUpActivtiy.this, "头像修改失败");
            }
        });
    }

    public void onEventMainThread(EventBusAdapter busAdapter) {
        if (!TextUtils.isEmpty(busAdapter.getGroupIndex())) {
            if (busAdapter.getGroupIndex().equals("1")) {
                mNetworkRequest.setVisibility(View.VISIBLE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                GroupBasic();
            }
        }

    }

    /**
     * 上传图片
     */
    public void EstablishGroup(final Context context, String dataCity) {
        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("正在上传");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(dataCity, String.valueOf(System.currentTimeMillis()), CacheTools.getUserData("qiniutoken"), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                MyLog.e("YUY", "七牛上传图片c = " + info.statusCode);
                MyLog.e("YUY", "七牛上传图片qiniutoken = " + CacheTools.getUserData("qiniutoken"));
                if (info.statusCode == 200) {
                    ToastUtil.toastShow(context, "图片上传成功");
                    //res包含hash、key等信息，具体字段取决于上传策略的设置。
                    dataCityUrl = NetworkUtil.QI_NIU_BASE_URL + key;
                    mProgressDialog.dismiss();
                } else if (info.statusCode == 401) {
                    TokenObtain tokenObtain = new TokenObtain();
                    tokenObtain.Group(context);
                    mProgressDialog.dismiss();
                    ToastUtil.toastShow(context, "七牛token过期");
                } else {
                    mProgressDialog.dismiss();
                    ToastUtil.toastShow(context, "图片上传失败");
                    if (TextUtils.isEmpty(CacheTools.getUserData("qiniutoken"))) {
                        TokenObtain tokenObtain = new TokenObtain();
                        tokenObtain.Group(context);
                    }
                }
            }
        }, null);
    }
}
