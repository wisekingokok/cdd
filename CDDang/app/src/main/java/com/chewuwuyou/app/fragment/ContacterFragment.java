package com.chewuwuyou.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ChatUserInfo;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.ui.SearchFriendActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.EditTextLengthFilter;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshExpandableListView;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.ContacterManager;
import com.chewuwuyou.eim.manager.ContacterManager.MRosterGroup;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.model.User;
import com.chewuwuyou.eim.util.MsgUtil;
import com.chewuwuyou.eim.util.StringUtil;
import com.chewuwuyou.eim.view.ContacterExpandAdapter;

import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通讯录
 */
public class ContacterFragment extends ContacterBaseFragment implements
        OnClickListener, FragmentCallBackBuilder {

    private View mContentView;
    private PullToRefreshExpandableListView mContacterList = null;
    private ContacterExpandAdapter mExpandAdapter = null;
    private List<String> groupNames;
    private List<String> newNames = new ArrayList<String>();
    private List<MRosterGroup> rGroups;
    private User clickUser;
    private TextView nullText;
    private WakeLock mWakeLock;
    private Map<String, ChatUserInfo> userMap = new HashMap<String, ChatUserInfo>();
    // private String userIdjoint = "";// 用户id拼接用于查询用户头像及昵称
    // private List<ChatUserInfo> mUserInfos;// 用户的头像和昵称的集合
    // private RelativeLayout mServiceNumberRL;// 服务号
    private LinearLayout searchLL;
    private boolean isInit = true;
    private String mZfMsg;//转发的消息

    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.contacter_ac, null);
        initView();
        initData();
        initEvent();
        return mContentView;
    }

    @Override
    protected void initView() {
        mContacterList = (PullToRefreshExpandableListView) mContentView.findViewById(R.id.main_expand_list);
        searchLL = (LinearLayout) mContentView.findViewById(R.id.searchLL);
        nullText = (TextView) mContentView.findViewById(R.id.nullText);
    }

    @Override
    protected void initData() {
        rGroups = new ArrayList<>();
        // 现在AppgetActivity()接管了生命周期，这个Activity可能启动太快，而导致refresh发生在登陆完成之前。
        if (mExpandAdapter == null) {
            mExpandAdapter = new ContacterExpandAdapter(getActivity(), rGroups, userMap);// 联系人
            mContacterList.getRefreshableView().setAdapter(mExpandAdapter);
        }

    }

    @Override
    protected void initEvent() {
        searchLL.setOnClickListener(this);
        mContacterList.getRefreshableView().setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int groupPosition = (int) arg1.getTag(ContacterExpandAdapter.GROUP_TAG);
                int childPosition = (int) arg1.getTag(ContacterExpandAdapter.CHILD_TAG);
                getActivity().setTheme(R.style.ActionSheetStyleIOS7);
                if (childPosition == ContacterExpandAdapter.CHILD_TAG_NULL) {//长按Group
                    final String groupName = rGroups.get(groupPosition).getName();
                    if (StringUtil.notEmpty(groupName) && !Constant.ALL_FRIEND.equals(groupName) && !Constant.NO_GROUP_FRIEND.equals(groupName)) {
                        ActionSheet menuView = new ActionSheet(getActivity());
                        menuView.setCancelButtonTitle("取 消");
                        menuView.addItems("添加分组", "更改组名");
                        menuView.setItemClickListener(new MenuItemClickListener() {

                            @Override
                            public void onItemClick(int itemPosition) {
                                switch (itemPosition) {
                                    case 0:// 添加分组
                                        addNewGroup();
                                        break;
                                    case 1:// 更改组名
                                        updateGroupNameA(groupName);
                                        break;
                                }
                            }
                        });
                        menuView.setCancelableOnTouchMenuOutside(true);
                        menuView.showMenu();
                        return true;
                    } else {
                        ActionSheet menuView = new ActionSheet(getActivity());
                        menuView.setCancelButtonTitle("取 消");
                        menuView.addItems("添加分组");
                        menuView.setItemClickListener(new MenuItemClickListener() {

                            @Override
                            public void onItemClick(int itemPosition) {
                                switch (itemPosition) {
                                    case 0:// 添加分组
                                        addNewGroup();
                                        break;
                                }
                            }
                        });
                        menuView.setCancelableOnTouchMenuOutside(true);
                        menuView.showMenu();
                        return true;
                    }
                } else {//长按Child
                    clickUser = mExpandAdapter.getChild(groupPosition, childPosition);
                    if (clickUser.getJID().equals("xiaodang@iz232jtyxeoz") || clickUser.getJID().equals("xiaoding@iz232jtyxeoz")) {
                        ActionSheet menuView = new ActionSheet(getActivity());
                        menuView.setCancelButtonTitle("取 消");
                        menuView.addItems("添加好友");
                        menuView.setItemClickListener(new MenuItemClickListener() {

                            @Override
                            public void onItemClick(int itemPosition) {
                                switch (itemPosition) {
                                    case 0:// 添加好友
                                        addSubscriber();
                                        break;
                                }
                            }
                        });
                        menuView.setCancelableOnTouchMenuOutside(true);
                        menuView.showMenu();
                        return true;
                    } else if (StringUtil.notEmpty(clickUser.getGroupName())
                            && !Constant.ALL_FRIEND.equals(clickUser
                            .getGroupName())
                            && !Constant.NO_GROUP_FRIEND
                            .equals(clickUser.getGroupName())) {
                        ActionSheet menuView = new ActionSheet(
                                getActivity());
                        menuView.setCancelButtonTitle("取 消");
                        menuView.addItems("设置备注", "添加好友", "删除好友",
                                "移动到分组", "退出该组");
                        menuView.setItemClickListener(new MenuItemClickListener() {

                            @Override
                            public void onItemClick(int itemPosition) {
                                switch (itemPosition) {
                                    case 0:// 设置昵称
                                        setNickname(clickUser);
                                        break;
                                    case 1:// 添加好友
                                        addSubscriber();
                                        break;
                                    case 2:// 删除好友
                                        showDeleteDialog(clickUser);
                                        break;
                                    case 3:// 移动到分组 （1.先移除本组，2移入某组）
                                        //ui移除old组
                                        removeUserFromGroupUI(clickUser);
                                        removeUserFromGroup(clickUser, clickUser.getGroupName());
                                        addToGroup(clickUser);
                                        break;
                                    case 4:// 移出组
                                        //ui移除old组
                                        removeUserFromGroupUI(clickUser);
                                        //api级出某组
                                        removeUserFromGroup(clickUser, clickUser.getGroupName());
                                        break;
                                }
                            }
                        });
                        menuView.setCancelableOnTouchMenuOutside(true);
                        menuView.showMenu();
                        return true;
                    } else {
                        ActionSheet menuView = new ActionSheet(getActivity());
                        menuView.setCancelButtonTitle("取 消");
                        menuView.addItems("添加好友", "删除好友", "移动到分组", "退出该组");
                        menuView.setItemClickListener(new MenuItemClickListener() {

                            @Override
                            public void onItemClick(int itemPosition) {
                                switch (itemPosition) {
                                    case 0:// 添加好友
                                        addSubscriber();
                                        break;
                                    case 1:// 删除好友
                                        showDeleteDialog(clickUser);
                                        break;
                                    case 2:// 移动到分组 （1.先移除本组，2移入某组）
                                        // ui移除old组
                                        removeUserFromGroupUI(clickUser);
                                        removeUserFromGroup(clickUser, clickUser.getGroupName());
                                        addToGroup(clickUser);
                                        break;
                                    case 3:// 移出组
                                        //ui移除old组
                                        removeUserFromGroupUI(clickUser);
                                        // api级出某组
                                        removeUserFromGroup(clickUser, clickUser.getGroupName());
                                        break;
                                }
                            }
                        });
                        menuView.setCancelableOnTouchMenuOutside(true);
                        menuView.showMenu();
                        return true;
                    }
                }
            }
        });
      
        mContacterList.getRefreshableView().setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                User u = rGroups.get(groupPosition).getUsers().get(childPosition);
                if (!TextUtils.isEmpty(mZfMsg)) {
                    MsgUtil.showChatMsgZfDialog(getActivity(), mZfMsg, u.getName(), u.getJID());
                } else {
                    if (u != null) {
                        if (u.getJID().contains("@") && !u.getJID().contains("xiaoding") && !u.getJID().contains("xiaodang")) {
                            Intent intent = new Intent(getActivity(), PersonalHomeActivity2.class);
                            intent.putExtra("userId", Integer.valueOf(u.getJID().split("@")[0]));
                            startActivity(intent);
                        } else {
                            createChat(u);
                        }
                    }
                }
                return false;
            }
        });
        mContacterList.getRefreshableView().setGroupIndicator(null);
//        mContacterList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {//list停止滚动时加载图片
//                    mExpandAdapter.start();
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (totalItemCount == 0 || visibleItemCount == 0) return;
//                for (int i = 0; i < visibleItemCount; i++) {
//                    View v = mContacterList.getChildAt(i);
//                    if (v == null) continue;
//                    int childPosition = (int) v.getTag(ContacterExpandAdapter.CHILD_TAG);
//                    if (childPosition == ContacterExpandAdapter.CHILD_TAG_NULL) continue;
//                    int groupPosition = (int) v.getTag(ContacterExpandAdapter.GROUP_TAG);
//                    mExpandAdapter.getDetails(groupPosition, childPosition, i == 0);
//                }
//                if (isInit) {
//                    isInit = false;
//                    mExpandAdapter.start();
//                }
//            }
//        });
        mContacterList.getRefreshableView().setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                isInit = true;
            }
        });

        mContacterList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ExpandableListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                whenAppResume();
            }
        });
    }


    /**
     * 有新消息进来
     */
    @Override
    protected void msgReceive(Notice notice) {
        MyLog.i("YUY", "-----------------" + notice);
        // for (ChartHisBean ch : inviteNotices) {
        // if (ch.getFrom().equals(notice.getFrom())) {
        // ch.setContent(notice.getContent());
        // ch.setNoticeTime(notice.getNoticeTime());
        // Integer x = ch.getNoticeSum() == null ? 0 : ch.getNoticeSum();
        // ch.setNoticeSum(x + 1);
        // }
        // }
        // noticeAdapter.setNoticeList(inviteNotices);
        // noticeAdapter.notifyDataSetChanged();
        // setPaoPao();

    }

    /**
     * 拼装请求参数，用于请求详细信息
     *
     * @author sxk
     * @update 2012-5-16 下午7:15:21
     */
    public void whenAppResume() {
        // 本函数在login成功后被激发
        if (CacheTools.getUserData("role") == null) {
            mContacterList.onRefreshComplete();
            return;
        }
//        delayRefresh();//延时刷新
        refreshList();
        if (rGroups == null || rGroups.size() == 0) {
            nullText.setVisibility(View.VISIBLE);
        } else {
            nullText.setVisibility(View.GONE);
        }
        List<String> userJIDList = new ArrayList<String>();
        for (int i = 0; i < rGroups.size(); i++) {
//            rGroupsSort(rGroups.get(i).getUsers());
            for (int j = 0; j < rGroups.get(i).getUsers().size(); j++) {
                if (rGroups.get(i).getUsers() == null) continue;
                try{
                    String userJID = rGroups.get(i).getUsers().get(j).getJID();
                    if (!userJID.equals(Constant.XIAODANG) && !userJID.equals(Constant.XIAODING)) {
                        userJIDList.add(userJID.substring(0, userJID.indexOf("@")));
                    }
                }catch(Exception e){
                    MyLog.e("YUY","请求ID异常");
                }

            }
        }
        if (userJIDList == null || userJIDList.size() == 0) {
            mContacterList.onRefreshComplete();
            return;
        }
        Tools.removeDuplicate(userJIDList);// 移除 相同userJid，避免使用更多的数据流量
        List<List<String>> listList = new ArrayList<>();
        for (int i = 0; i < userJIDList.size(); i++) {
            if (listList.size() == 0 || listList.get(listList.size() - 1).size() == 50)
                listList.add(new ArrayList<String>());
            listList.get(listList.size() - 1).add(userJIDList.get(i));
        }
        for (int i = 0; i < listList.size(); i++) {
            List<String> list = listList.get(i);
            String userIdjoint = "";
            for (int j = 0; j < list.size(); j++) {
                userIdjoint += list.get(j) + "-";
            }
            getDataByIds(userIdjoint.substring(0, userIdjoint.length() - 1));
        }
    }

    /**
     * 请求详细信息
     *
     * @param ids
     */
    private void getDataByIds(String ids) {
        // 请求用户头像和昵称
        AjaxParams params = new AjaxParams();
        params.put("ids", ids);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mContacterList.onRefreshComplete();
                switch (msg.what) {
                    case com.chewuwuyou.app.utils.Constant.NET_DATA_SUCCESS:
                        List<ChatUserInfo> mUserInfos = ChatUserInfo.parseList(msg.obj.toString());
                        for (int i = 0; i < mUserInfos.size(); i++) {
                            userMap.put(mUserInfos.get(i).getId(), mUserInfos.get(i));
                        }
                        mExpandAdapter.setContacter(rGroups, userMap);
                        mExpandAdapter.notifyDataSetChanged();
                        break;
                    case com.chewuwuyou.app.utils.Constant.NET_DATA_FAIL:
                        MyLog.e("YUY", "系统异常");
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.GET_CHAT_USER_INFO, true, 0);
    }

    /**
     * 对联系人根据在线进行排序
     *
     * @param
     */
    private void rGroupsSort(List<User> mUsers) {
        if (mUsers != null) {
//            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(mUsers, new Comparator<User>() {

                @Override
                public int compare(User user1, User user2) {
                    if (user1.getJID().equals(Constant.XIAODANG))
                        return -1;
                    if (user2.getJID().equals(Constant.XIAODANG))
                        return 1;
                    if (user1.getJID().equals(Constant.XIAODING))
                        return -1;
                    if (user2.getJID().equals(Constant.XIAODING))
                        return 1;
                    return user1.isAvailable() && !user2.isAvailable() ? -1 : 1;
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWakeLock.release();
    }

    @SuppressWarnings({"deprecation", "static-access"})
    @Override
    public void onResume() {
        PowerManager pm = (PowerManager) getActivity().getSystemService(getActivity().POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "DPA");
        mWakeLock.acquire();
//        if (ContacterManager.contacters == null || ContacterManager.contacters.size() == 0)
        whenAppResume();
        super.onResume();
    }

    /**
     * 设置昵称
     *
     * @param user
     */
    private void setNickname(final User user) {
        final EditText name_input = new EditText(getActivity());
        name_input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        name_input.setHint("输入备注");
        new AlertDialog.Builder(getActivity()).setTitle("设置备注")
                .setView(name_input)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = name_input.getText().toString();
                        if (!"".equals(name)) {
                            setNickname(user.getJID(), name);
                            userMap.get(user.getJID().split("@")[0])
                                    .setNoteName(name);
                            mExpandAdapter.notifyDataSetChanged();
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     * 添加好友
     */
    private void addSubscriber() {
        Intent intent = new Intent(getActivity(), SearchFriendActivity.class);
        startActivity(intent);
    }

    /**
     * 添加一个联系人
     *
     * @param userJid  联系人JID
     * @param nickname 联系人昵称
     * @param groups   联系人添加到哪些组
     * @throws XMPPException
     */
    protected void createSubscriber(String userJid, String nickname,
                                    String[] groups) throws XMPPException {
        try {
            XmppConnectionManager.getInstance().getConnection().getRoster()
                    .createEntry(userJid, nickname, groups);
        } catch (NotLoggedInException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入组
     *
     * @param user
     */
    private void addToGroup(final User user) {
        if (groupNames.isEmpty()) {
            Toast.makeText(getActivity(), "没有分组喔", Toast.LENGTH_SHORT).show();
            return;
        }
        LayoutInflater inflaterx = LayoutInflater.from(getActivity());
        View dialogView = inflaterx.inflate(R.layout.yd_group_dialog, null);
        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.list);
        ArrayAdapter<String> ada = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, groupNames);
        spinner.setAdapter(ada);

        new AlertDialog.Builder(getActivity())
                .setTitle(
                        "移动[" + userMap.get(user.getJID().split("@")[0])
                                .getNick() + "]至分组")
                .setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String groupName = (spinner.getSelectedItem())
                                .toString();
                        if (StringUtil.notEmpty(groupName)) {
                            groupName = StringUtil.doEmpty(groupName);
                            if (newNames.contains(groupName)) {
                                newNames.remove(groupName);
                            }
                            // UI级把用户移到某组
                            addUserGroupUI(user, groupName);
                            // api移入组
                            addUserToGroup(user, groupName);
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }


    /**
     * 修改组名
     *
     * @param groupName
     */
    private void updateGroupNameA(final String groupName) {
        final EditText name_input = new EditText(getActivity());
        name_input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        name_input.setPadding(5, 0, 5, 0);
        InputFilter[] filters = {new EditTextLengthFilter(getActivity(), 16)};
        name_input.setFilters(filters);
        name_input.setHint("输入组名");
        new AlertDialog.Builder(getActivity()).setTitle("修改组名")
                .setView(name_input)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String gNewName = name_input.getText().toString().trim();
                        if (TextUtils.isEmpty(gNewName)) {
                            ToastUtil.toastShow(getActivity(), "组名不能为空");
                            return;
                        }
                        if (newNames.contains(gNewName)
                                || groupNames.contains(gNewName)) {
                            ToastUtil.toastShow(getActivity(), "组名已存在");
                            return;
                        }
                        // UI级修改操作
                        updateGroupNameUI(groupName, gNewName);
                        // UIAPI
                        updateGroupName(groupName, gNewName);
                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     * 刷新当前的列表
     */
    private void refreshList() {
        if (CacheTools.getUserData("role") == null) {
            MyLog.e("YUY", "未登录");
            mContacterList.onRefreshComplete();
            return;
        }
        XMPPConnection conn = XmppConnectionManager.getInstance().getConnection();
        if (conn == null || !conn.isConnected()) {
            MyLog.e("YUY", "xmpp连接无效");
            return;
        }
        if (conn.getRoster() != null) {
            groupNames = ContacterManager.getGroupNames(conn.getRoster());
            rGroups = ContacterManager.getGroups(conn.getRoster());
            mExpandAdapter.setContacter(rGroups, userMap);
        } else {
            mContacterList.onRefreshComplete();
            MyLog.e("YUY", "getRoster为空");
        }

    }

    @Override
    protected void addUserReceive(User user) {
        MyLog.i("YUY", "添加好友");
        if (CacheTools.getUserData("role") != null) {
            whenAppResume();
        }
    }

    @Override
    protected void deleteUserReceive(User user) {
        MyLog.i("YUY", "删除好友");
        if (user == null)
            return;
        MyLog.i("YUY", user.getName() == null ? user.getJID() : user.getName() + "被删除了");
        refreshList();
    }

    @Override
    protected void changePresenceReceive(User user) {
        MyLog.i("YUY", "用户状态改变");
        if (user == null)
            return;
        if (ContacterManager.contacters == null)
            return;
        if (ContacterManager.contacters.get(user.getJID()) == null)
            return;
        // 下线
        if (!user.isAvailable())
            if (ContacterManager.contacters.get(user.getJID()).isAvailable())
                ;
        // 上线
        if (user.isAvailable())
            if (!ContacterManager.contacters.get(user.getJID()).isAvailable())
                ;
        if (CacheTools.getUserData("role") != null) {
            refreshList();
        }

    }

    /**
     * Contact Service启动完毕时刷新
     */
    @Override
    protected void doContactStarted() {
        // statDoLogin();
        // contactStarted = true;
        // doAllStarted();
    }

    /**
     * Chat Service 启动时
     */
    protected void doChatStarted() {
        // chatStarted = true;
        // doAllStarted();
    }

    /**
     * System Msg Service 启动时
     */
    protected void doSystemMsgStarted() {
        // systemMsgStarted = true;
        // doAllStarted();
    }

    /**
     * Reconnect Service 启动时
     */
    protected void doReConnectStarted() {
        // reconnectStarted = true;
        // doAllStarted();
    }

    @Override
    protected void updateUserReceive(User user) {
        refreshList();
    }

    @Override
    protected void subscripUserReceive(final String subFrom) {
        Notice notice = new Notice();
        notice.setFrom(subFrom);
        notice.setNoticeType(Notice.CHAT_MSG);
    }

    /**
     * 加入组
     */
    private void addNewGroup() {
        final EditText name_input = new EditText(getActivity());
        name_input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        InputFilter[] filters = {new EditTextLengthFilter(getActivity(), 16)};
        name_input.setFilters(filters);
        name_input.setHint("输入组名");
        new AlertDialog.Builder(getActivity()).setTitle("加入组")
                .setView(name_input)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String groupName = name_input.getText().toString();
                        if (StringUtil.empty(groupName)) {
                            // showToast("组名不能为空");
                            return;
                        }
                        // ui上增加数据
                        if (groupNames.contains(groupName)) {
                            // showToast("组名已经存在");
                            return;
                        }
                        addGroupNamesUi(groupName);

                    }
                }).setNegativeButton("取消", null).show();
    }

    public void delayRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * UI级添加分组 这里用一句话描述这个方法的作用.
     *
     * @author sxk
     * @update 2012-7-2 下午1:04:09
     */
    public void addGroupNamesUi(String newGroupName) {
        groupNames.add(newGroupName);
        newNames.add(newGroupName);
        MRosterGroup mg = new MRosterGroup(newGroupName, new ArrayList<User>());
        rGroups.add(rGroups.size(), mg);
        // 刷新用户信息
        mExpandAdapter.setContacter(rGroups, userMap);
        mExpandAdapter.notifyDataSetChanged();
    }

    /**
     * UI级删除用户
     */
    private void deleteUserUI(User user) {
        for (MRosterGroup g : rGroups) {
            List<User> us = g.getUsers();
            if (us != null && us.size() > 0) {
                if (us.contains(user)) {
                    us.remove(user);
                    g.setUsers(us);
                }
            }
        }
        mExpandAdapter.setContacter(rGroups, userMap);
        mExpandAdapter.notifyDataSetChanged();
    }

    /**
     * UI级移动用户，把用户移除某组
     */

    private void removeUserFromGroupUI(User user) {

        for (MRosterGroup g : rGroups) {
            if (g.getUsers().contains(user)) {
                if (StringUtil.notEmpty(g.getName())
                        && !Constant.ALL_FRIEND.equals(g.getName())) {
                    List<User> users = g.getUsers();
                    users.remove(user);
                    g.setUsers(users);

                }
            }
        }
        mExpandAdapter.setContacter(rGroups, userMap);
        mExpandAdapter.notifyDataSetChanged();
    }

    /**
     * UI级移动用户，把用户加入某组
     */

    private void addUserGroupUI(User user, String groupName) {
        for (MRosterGroup g : rGroups) {
            if (groupName.equals(g.getName())) {
                List<User> users = g.getUsers();
                users.add(user);
                g.setUsers(users);
            }
        }
        mExpandAdapter.setContacter(rGroups, userMap);
        mExpandAdapter.notifyDataSetChanged();

    }

    /**
     * UI更改组名
     */

    private void updateGroupNameUI(String old, String newGroupName) {

        if (StringUtil.empty(old) || Constant.ALL_FRIEND.equals(old)
                || Constant.NO_GROUP_FRIEND.equals(old)) {
            return;
        }
        // 虽然没必要，但是如果输入忘记限制
        if (StringUtil.empty(newGroupName)
                || Constant.ALL_FRIEND.equals(newGroupName)
                || Constant.NO_GROUP_FRIEND.equals(newGroupName)) {
            ToastUtil.toastShow(getActivity(), "已有该组名");
            return;
        }

        // 要修改的组名是新添加的但是没有添加到服务器端的，只是ui级添加的，如下操作
        if (newNames.contains(old)) {
            newNames.remove(old);
            newNames.add(newGroupName);
            return;
        }
        // 列表修改;
        for (MRosterGroup g : rGroups) {
            if (g.getName().equals(old)) {
                g.setName(newGroupName);
            }
        }
        mExpandAdapter.notifyDataSetChanged();

    }

    /**
     * 删除用户
     *
     * @param clickUser
     */
    private void showDeleteDialog(final User clickUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(
                getResources().getString(R.string.delete_user_confim))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // ui删除
                                deleteUserUI(clickUser);
                                // api删除
                                try {
                                    removeSubscriber(clickUser.getJID());
                                } catch (XMPPException e) {
                                }
                                // 删除数据库
                                NoticeManager.getInstance(getActivity())
                                        .delNoticeHisWithSb(clickUser.getJID());
                                MessageManager.getInstance(getActivity())
                                        .delChatHisWithSb(clickUser.getJID());
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void handReConnect(boolean isSuccess) {
        MyLog.e("YUY", "xmpp重新连接了");
        whenAppResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchLL:
//                if (userMap == null || userMap.size() <= 0) {
//                    ToastUtil.toastShow(getActivity(), "正在获取好友...");
//                    return;
//                }
                if (callback != null) {
                    callback.callback(1, userMap);
                }
                break;
            default:
                break;
        }
    }

    private FragmentCallBack callback;

    @Override
    public void setFragmentCallBack(FragmentCallBack callback) {
        this.callback = callback;
    }

    public void setMessage(String msg) {
        mZfMsg = msg;
    }

}
