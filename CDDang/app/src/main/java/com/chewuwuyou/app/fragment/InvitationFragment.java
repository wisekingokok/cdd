package com.chewuwuyou.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.InvitationAdapter;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.bean.TieItem;
import com.chewuwuyou.app.bean.ZanItem;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.callback.PullFragmentCallBack;
import com.chewuwuyou.app.tools.MyListView;
import com.chewuwuyou.app.ui.HotTieDetailActivity;
import com.chewuwuyou.app.ui.TieDetailActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;

import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文
 *
 * @author 向
 */
public class InvitationFragment extends BaseFragment implements
        FragmentCallBackBuilder, PullFragmentCallBack {
    private AlertDialog mDeleteTieDialog;
    private View mContentView;
    private MyListView listView;
    private ImageView nullCon;
    private FragmentCallBack callback;
    private PersonHome mPersonHome;
    private TextView nullConTV;
    private int mCurcor = 0;// 当前有几条
    private List<TieItem> mData = new ArrayList<TieItem>();// 数据

    private InvitationAdapter adapter;
    // private int mTodayNum;// 今日话题

    // 处理消息
    public static final int TOGGLE_ZAN = 111;// 赞或者取消赞一个帖
    // public static final int PING_TIE = 112;// 评价一个帖

    // 在我的帖子中要用
    public static final int EDIT_TIE = 114;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            final TieItem tie;
            switch (msg.what) {
                case TOGGLE_ZAN:// 切换赞一个帖子
                    tie = (TieItem) msg.obj;
                    toggleZan(tie);
                    break;
                case EDIT_TIE:
                    tie = (TieItem) msg.obj;
                    // setTheme(R.style.ActionSheetStyleIOS7);
                    ActionSheet menuView = new ActionSheet(getActivity());
                    menuView.setCancelButtonTitle("取 消");// before add items
                    if (tie.getZiji().equals("1")) {// 自己发的帖子
                        menuView.addItems("删除");
                    } else {
                        menuView.addItems("举报");
                    }

                    menuView.setItemClickListener(new MenuItemClickListener() {

                        @Override
                        public void onItemClick(int itemPosition) {
                            switch (itemPosition) {
                                case 0:
                                    if (tie.getZiji().equals("1")) {// 自己发的帖子
                                        showDeleteTieDialog(tie);
                                    } else {
                                        final EditText editText = new EditText(
                                                getActivity());
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("请输入举报原因")
                                                .setView(editText)
                                                .setPositiveButton("确定",
                                                        new OnClickListener() {

                                                            @Override
                                                            public void onClick(
                                                                    DialogInterface arg0,
                                                                    int arg1) {
                                                                AjaxParams params = new AjaxParams();
                                                                // 传入帖子id和uid
                                                                params.put("type", "2");
                                                                params.put(
                                                                        "relateId",
                                                                        String.valueOf(tie
                                                                                .getId()));
                                                                params.put(
                                                                        "reason",
                                                                        editText.getText()
                                                                                .toString());

                                                                requestNet(
                                                                        new Handler() {

                                                                            @Override
                                                                            public void handleMessage(
                                                                                    Message msg) {
                                                                                // stub
                                                                                super.handleMessage(msg);
                                                                                switch (msg.what) {
                                                                                    case Constant.NET_DATA_SUCCESS:
                                                                                        Toast.makeText(
                                                                                                getActivity(),
                                                                                                "已提交",
                                                                                                Toast.LENGTH_SHORT)
                                                                                                .show();

                                                                                        break;
                                                                                    case Constant.NET_DATA_FAIL:
                                                                                        Toast.makeText(
                                                                                                getActivity(),
                                                                                                "举报失败，再试试",
                                                                                                Toast.LENGTH_SHORT)
                                                                                                .show();
                                                                                        break;
                                                                                    default:
                                                                                        break;
                                                                                }

                                                                            }
                                                                        },
                                                                        params,
                                                                        NetworkUtil.MAKE_ACCU,
                                                                        false, 0);
                                                            }
                                                        })
                                                .setNegativeButton("取消", null).show();
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    menuView.setCancelableOnTouchMenuOutside(true);
                    menuView.showMenu();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_invitation, null);
        initView();
        initData();
        initEvent();

        return mContentView;
    }

    @Override
    protected void initView() {
        listView = (MyListView) mContentView.findViewById(R.id.listView);
        nullCon = (ImageView) mContentView.findViewById(R.id.nullCon);
        nullConTV = (TextView) mContentView.findViewById(R.id.nullConTV);
    }

    @Override
    protected void initData() {
        if (adapter == null) {
            adapter = new InvitationAdapter(getActivity(), mHandler, mData,
                    "com.chewuwuyou.app.other_tie");
        }
        listView.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = null;
                if (mData.get(arg2).getReuse() != null
                        && mData.get(arg2).getReuse().equals("1")) {
                    intent = new Intent(getActivity(),
                            HotTieDetailActivity.class);
                } else {
                    intent = new Intent(getActivity(), TieDetailActivity.class);
                }

                intent.putExtra("id", mData.get(arg2).getId());
                intent.putExtra("ziji", mData.get(arg2).getZiji());
                startActivity(intent);
            }

        });
    }

    public void setPersonHome(PersonHome personHome) {
        this.mPersonHome = personHome;
        pullDown();
    }

    @Override
    public void setFragmentCallBack(FragmentCallBack callback) {
        this.callback = callback;
    }

    // 获得所有帖子
    private void getAllTie(final boolean refresh) {
        if (mPersonHome == null) {
            if (callback != null)
                callback.callback(0, 1);
            return;
        }
        if (refresh)
            mCurcor = 0;
        AjaxParams params = new AjaxParams();
        params.put("start", String.valueOf(mCurcor));
        params.put("otherId", mPersonHome.getUserId() + "");
        if (!String.valueOf(mPersonHome.getUserId()).equals(
                CacheTools.getUserData("userId"))) {// 判断不是本人,则加载Id
            nullConTV.setVisibility(View.GONE);
            nullCon.setVisibility(View.VISIBLE);
        } else {
            nullConTV.setVisibility(View.VISIBLE);
            nullCon.setVisibility(View.GONE);
        }
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (callback != null)
                    callback.callback(0, 1);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        List<TieItem> response = TieItem.parseList(msg.obj
                                .toString());
                        if (response != null) {
                            if (refresh)
                                mData.clear();
                            mData.addAll(response);
                            adapter.setData(mData);
                            mCurcor = mData.size();
                            if (mData.size() <= 0) {
                                if (!String.valueOf(mPersonHome.getUserId())
                                        .equals(CacheTools.getUserData("userId"))) {
                                    nullConTV.setVisibility(View.GONE);
                                    nullCon.setVisibility(View.VISIBLE);
                                } else {
                                    nullConTV.setVisibility(View.VISIBLE);
                                    nullCon.setVisibility(View.GONE);
                                    nullConTV.setText("O(^_^)O空空如也，请请勤劳发帖~");
                                }
                            } else {
                                nullCon.setVisibility(View.GONE);
                                nullConTV.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        nullCon.setVisibility(View.GONE);
                        nullConTV.setVisibility(View.VISIBLE);
                        nullConTV.setText("O(T_T)O网络请求失败~");
                        break;
                }
            }
        }, params, NetworkUtil.GET_MY_TIE, false, 1);
    }

    @Override
    public void pullUp() {
        getAllTie(false);
    }

    @Override
    public void pullDown() {
        getAllTie(true);
    }

    // 切换赞状态
    private void toggleZan(final TieItem tie) {
        AjaxParams params = new AjaxParams();
        // 传入帖子id和uid
        params.put("tieZiId", String.valueOf(tie.getId()));
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:

                        ZanItem tieZan = ZanItem.parse(msg.obj.toString());
                        if (tieZan != null) {
                            // 如果以前赞过，那现在取消赞
                            if (tie.getZaned().equals("0")) {
                                tie.setZaned(1 + "");
                                tie.setTiezancnt(tie.getTiezancnt() + 1);
                                Toast.makeText(getActivity(), "点赞成功",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                tie.setZaned(0 + "");
                                tie.setTiezancnt(tie.getTiezancnt() - 1);
                                Toast.makeText(getActivity(), "取消赞",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        break;

                    case Constant.NET_DATA_FAIL:
                        // 提示切换赞失败
                        Toast.makeText(getActivity(), "失败，请重新尝试",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.TIE_TOGGLE_ZAN, false, 0);
    }

    private void showDeleteTieDialog(final TieItem tie) {
        mDeleteTieDialog = new AlertDialog.Builder(getActivity())
                .setTitle("确定删除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delTie(tie);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        mDeleteTieDialog.setCanceledOnTouchOutside(true);
        mDeleteTieDialog.show();
    }

    private void delTie(final TieItem tie) {
        AjaxParams params = new AjaxParams();
        params.put("tieZiId", String.valueOf(tie.getId()));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT)
                                .show();
                        // mData.remove(position);
                        mData.remove(tie);
                        adapter.notifyDataSetChanged();
                        break;
                    case Constant.NET_DATA_FAIL:
                        Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    default:
                        break;
                }
            }

        }, params, NetworkUtil.DEL_TIE, false, 0);

    }
}
