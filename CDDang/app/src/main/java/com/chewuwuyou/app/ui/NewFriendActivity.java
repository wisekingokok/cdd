package com.chewuwuyou.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.barcode.view.SweepListView;
import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Contacter;
import com.chewuwuyou.app.bean.NewFriend;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.ImageLoaderBuilder;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.KeywordUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.CharacterParser;
import com.chewuwuyou.app.widget.SwipeLayout;


import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

public class NewFriendActivity extends CDDBaseActivity {


    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackIBtn;
    @ViewInject(id = R.id.sub_header_bar_right_tv, click = "onAction")
    private TextView mTextView_right;
    /**
     * 标题
     */
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mTitleTV;

    @ViewInject(id = R.id.lv_contacts)
    private ListView mListView;
    @ViewInject(id = R.id.textview_empty)
    private TextView mTextview_empty;

    @ViewInject(id = R.id.search_friend_phone_et)
    EditText mEditText;
    String editext;//editext的值

    // private ChatUser chatUser;// 通讯用户实体
    private List<NewFriend> mLists = new ArrayList<NewFriend>();
    private List<NewFriend> mListsCopy = new ArrayList<>();

    private NewFriendAdapter mAdapter = null;
//    private List<ChatUserInfo> chatUsers = new ArrayList<ChatUserInfo>();

    private CharacterParser characterParser; //实例化汉字转拼音类


    FinalDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        initView();
        initData();
        initEvent();
        getData();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

//
//    //当收到消息 显示小圆点
//    public void onEventMainThread(ContactNotificationMessage sendMsgBean) {
//
//        getData();
//
//    }


    @Override
    protected void initView() {

        db = FinalDb.create(this);
        List<NewFriend> list = db.findAll(NewFriend.class);

        //筛选数据，当多账户登录的时候
        Iterator<NewFriend> iterator = list.iterator();
        while (iterator.hasNext()) {
            NewFriend s = iterator.next();

            if (!TextUtils.isEmpty(s.getUserTag())) {

                if (!(CacheTools.getUserData("userId")).equals(s.getUserTag())) {
                    iterator.remove();
                }

            }
        }


        mLists.addAll(list);
        mListsCopy.addAll(list);

        mTitleTV.setText("新朋友");
        mTextView_right.setText("添加好友");
        mTextView_right.setVisibility(View.VISIBLE);
        mAdapter = new NewFriendAdapter(this, mLists);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mTextview_empty);
        characterParser = CharacterParser.getInstance();

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String content = mEditText.getText().toString();
                filterData(content);
                editext = s.toString();
                mListView.setSelection(0);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    ProgressDialog mProgressDialog;

    private void getData() {

        if (mLists.size() == 0)
            mProgressDialog = DialogUtil.showProgressDialog(this);
        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.postMulti(NetworkUtil.FRIEND_APPLY_LIST, params, new AjaxCallBack<String>() {
            @Override
            public boolean isProgress() {
                return super.isProgress();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    ErrorCodeUtil.doErrorCode(NewFriendActivity.this, jsonObject.getInt("code"), jsonObject.getString("message"));
                    if (jsonObject.getInt("code") == 0) {
                        String json = jsonObject.getString("data");
                        List<NewFriend> list = NewFriend.parseList(json);
                        if (list != null || list.size() != 0) {
                            //      mLists.clear();
                            mLists.addAll(0, list);
                            mListsCopy.addAll(list);

                        }
                        mAdapter.notifyDataSetChanged();

                    } else {
                        ToastUtil.toastShow(AppContext.getInstance(), jsonObject.getString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();

                ToastUtil.toastShow(NewFriendActivity.this,"网络错误，请稍后再试");
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {


    }

    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void onAction(View v) {

        switch (v.getId()) {
            case R.id.search_friend_ibtn:

                break;
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;

            case R.id.layoutContainer:

                Intent mIntent = new Intent(this, SearchNewFriendActivity.class);
                this.startActivity(mIntent);

                break;

            case R.id.sub_header_bar_right_tv:


                Intent m = new Intent(this, AddfriendActivity.class);

                this.startActivity(m);


                break;
        }
    }

    private void filterData(String filterStr) {
        MyLog.i("过滤数据");
        mLists.clear();
        if (TextUtils.isEmpty(filterStr)) {
            MyLog.i("还原数据--->" + mLists.size());
            mLists.addAll(mListsCopy);
        } else {
            for (NewFriend m : mListsCopy) {
                String name = m.getName();
                String id = m.getUserId();
                String tel = m.getPhone();
                if (id.contains(filterStr) || name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString()) || tel.contains(filterStr)) {
                    mLists.add(m);
                }
            }
        }
        MyLog.i("现数据--->" + mLists.size());
        mAdapter.notifyDataSetChanged();
    }


    class NewFriendAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<NewFriend> mNewFriends;
        private Context context;
        private SwipeLayout lastOpenedSwipeLayout;
        //  private List<ChatUserInfo> mChatUsers = new ArrayList<ChatUserInfo>();

        public NewFriendAdapter(Context context, List<NewFriend> inviteUsers) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
            this.mNewFriends = inviteUsers;
        }

        @Override
        public int getCount() {
            return mNewFriends == null ? 0 : mNewFriends.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewFriends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Integer position_integer = Integer.valueOf(position);
            final NewFriend notice = mNewFriends.get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.add_friend_item, null);
                holder = new ViewHolder();
                holder.headIV = (ImageView) convertView.findViewById(R.id.user_head_iv);
                holder.userNickNameTV = (TextView) convertView.findViewById(R.id.user_nick_name_tv);
                holder.addFriendDescriptionTV = (TextView) convertView
                        .findViewById(R.id.user_add_friend_descrption_tv);
                holder.addFriendBtn = (Button) convertView.findViewById(R.id.add_friend_agree_btn);
                //     holder.refuseFriendBtn = (Button) convertView.findViewById(R.id.add_friend_refuse_btn);
                holder.resultTV = (TextView) convertView.findViewById(R.id.friend_get_result_tv);
                holder.mTextView_delete = (TextView) convertView.findViewById(R.id.delete);

                holder.mSwipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // if (notice.getNoticeType() == Notice.ADD_FRIEND) {// 添加好友未处理
            // 加气泡，处理的就消失了整体

            holder.userNickNameTV.setText(KeywordUtil.matcherSearchTitle(getResources().getColor(R.color.new_blue), mNewFriends.get(position).getName(), editext));


            //    描述：后面描述加好友请求说明
            if (!TextUtils.isEmpty(notice.getRemarks()))
                holder.addFriendDescriptionTV.setText("验证信息：" + notice.getRemarks());
            else
                holder.addFriendDescriptionTV.setText("请求加你为好友");
            if (notice.getAddState() == NewFriend.AGREE || notice.getAddState() == NewFriend.REFUSE) {

                holder.addFriendBtn.setVisibility(View.GONE);
                holder.resultTV.setVisibility(View.VISIBLE);
                if (notice.getAddState() == NewFriend.AGREE) {
                    holder.resultTV.setText("已添加");
                } else if (notice.getAddState() == NewFriend.REFUSE) {
                    holder.resultTV.setText("已拒绝");
                }

            } else {
                holder.addFriendBtn.setVisibility(View.VISIBLE);
                holder.resultTV.setVisibility(View.GONE);
            }
            final Button addFriendBtn = holder.addFriendBtn;

            final TextView friendGetResuleTV = holder.resultTV;

            holder.mSwipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
                @Override
                public void onOpen(SwipeLayout swipeLayout) {
                    //当前 item 被打开时，记录下此 item
                    lastOpenedSwipeLayout = swipeLayout;
                }

                @Override
                public void onClose(SwipeLayout swipeLayout) {

                }

                @Override
                public void onStartOpen(SwipeLayout swipeLayout) {
                    //当前 item 将要打开时关闭上一次打开的 item
                    if (lastOpenedSwipeLayout != null) {
                        lastOpenedSwipeLayout.close();
                    }
                }

                @Override
                public void onStartClose(SwipeLayout swipeLayout) {

                }
            });

            holder.addFriendBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // 接受请求

                    final ProgressDialog m = DialogUtil.showProgressDialog(NewFriendActivity.this);

                    AjaxParams params = new AjaxParams();
                    params.put("userId", CacheTools.getUserData("rongUserId"));
                    params.put("friendId", notice.getUserId());
                    NetworkUtil.postMulti(NetworkUtil.AGREE_FRIEND, params, new AjaxCallBack<String>() {
                        @Override
                        public void onSuccess(String s) {
                            super.onSuccess(s);
                            m.dismiss();

                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(s);
                                ErrorCodeUtil.doErrorCode(NewFriendActivity.this, jsonObject.getInt("code"), jsonObject.getString("message"));
                                notice.setAddState(NewFriend.AGREE);
                                notice.setUserTag(CacheTools.getUserData("userId"));
                                db.save(notice);
                                notifyDataSetChanged();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                            m.dismiss();
                            ToastUtil.toastShow(NewFriendActivity.this, strMsg);

                        }
                    });


                }
            });

            holder.headIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonalHomeActivity2.class);
                    intent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, mNewFriends.get(position).getUserId());
                    //     intent.putExtra(PersonalHomeActivity2.IS_YANZHENG, 1);
                    context.startActivity(intent);
                }
            });
            ImageUtils.displayImage(mNewFriends.get(position).getPortraitUri(), (ImageView) holder.headIV, 8, R.drawable.user_fang_icon,
                    R.drawable.user_fang_icon);


            holder.mTextView_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (mNewFriends.get(position).getAddState() == 1) {//当同意了才删除本地数据库
                        db.delete(mNewFriends.get(position));
                        mNewFriends.remove(position);
                        notifyDataSetChanged();
                    } else
                        deleteShenqing(mNewFriends.get(position), position);
                }


            });

            return convertView;
        }

        private void deleteShenqing(final NewFriend mNewFriend, final int position) {
            //删除请求
            final ProgressDialog m = DialogUtil.showProgressDialog(NewFriendActivity.this);

            AjaxParams params = new AjaxParams();
            params.put("userId", CacheTools.getUserData("rongUserId"));
            params.put("friendId", mNewFriend.getUserId());
            NetworkUtil.postMulti(NetworkUtil.DELETE_FRIEND_ASK, params, new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    m.dismiss();

                    mNewFriends.remove(position);
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    m.dismiss();
                    ToastUtil.toastShow(NewFriendActivity.this, strMsg);

                }
            });


        }


        private class ViewHolder {
            public SwipeLayout mSwipeLayout;
            public ImageView headIV;
            public TextView userNickNameTV;
            public TextView addFriendDescriptionTV;
            public Button addFriendBtn;
            public TextView resultTV;
            TextView mTextView_delete;
        }


    }


//    /**
//     * @author xiaanming
//     */
//    public static class PinyinComparator implements Comparator<Notice> {
//
//        @Override
//        public int compare(Notice o1, Notice o2) {
//            if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#")) {
//                return -1;
//            } else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@")) {
//                return 1;
//            } else {
//                return o1.sortLetters.compareTo(o2.sortLetters);
//            }
//        }
//    }
}
