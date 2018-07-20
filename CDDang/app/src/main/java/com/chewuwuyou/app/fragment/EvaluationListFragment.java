package com.chewuwuyou.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.EvaluateListAdapter;
import com.chewuwuyou.app.bean.Comment;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.Mode;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EvaluationListFragment extends BaseFragment
        implements OnRefreshListener2<ListView>, FragmentCallBackBuilder {
    View mContentView;
    PullToRefreshListView listView;
    private TextView nullText;

    String type = null;
    // TODO没有使用到
    // private boolean mIsRefreshing = false;
    private EvaluateListAdapter adapter;

    private String mBusinessId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_evaluate_list, null);
        initView();
        initData();
        initEvent();
        return mContentView;
    }

    @Override
    protected void initView() {
        listView = (PullToRefreshListView) mContentView.findViewById(R.id.listView);
        nullText = (TextView) mContentView.findViewById(R.id.nullText);
        listView.setMode(Mode.BOTH);
        listView.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        adapter = new EvaluateListAdapter(getActivity());
        listView.setAdapter(adapter);
        Bundle bundle = getArguments();
        type = bundle.getString("type");

        if (getActivity().getIntent().getIntExtra("bId", 0) == 1) {
            mBusinessId = getActivity().getIntent().getStringExtra("businessId");
            getDataiy(true);
        } else {
            getData();
        }
    }

    private void getData() {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            mBusinessId = jo.getString("id");
                            getDataiy(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(getActivity(),
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, null, NetworkUtil.QUERY_ME_BUSINESS, false, 0);

    }

    @Override
    protected void initEvent() {
        listView.setOnRefreshListener(this);
    }

    private void getDataiy(final boolean isRefresh) {
        AjaxParams params = new AjaxParams();
        params.put("type", type);
        params.put("start", String.valueOf(isRefresh ? 0 : adapter.getCount()));
        params.put("businessId", mBusinessId);
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                listView.onRefreshComplete();
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {
                            JSONObject obj = new JSONObject(msg.obj.toString());
                            List<Comment> mComments = Comment.parseList(obj.getJSONArray("comments").toString());
                            Bundle bundel = new Bundle();
                            bundel.putInt("hao", obj.getInt("hao"));
                            bundel.putInt("zhong", obj.getInt("zhong"));
                            bundel.putInt("cha", obj.getInt("cha"));
                            if (callback != null)
                                callback.callback(0, bundel);
                            if (isRefresh)
                                adapter.setData(mComments);
                            else
                                adapter.addData(mComments);
                            if (adapter.getCount() <= 0) {
                                nullText.setVisibility(View.VISIBLE);
                            } else {
                                nullText.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // if (mComments != null) {
                        // // 按时间顺序降序排列
                        // Collections.sort(mComments, new Comparator<Comment>() {
                        // @Override
                        // public int compare(Comment lhs, Comment rhs) {
                        // Date date1 =
                        // DateTimeUtil.stringToDate(lhs.getPublishTime());
                        // Date date2 =
                        // DateTimeUtil.stringToDate(rhs.getPublishTime());
                        // // 对日期字段进行升序，如果欲降序可采用after方法
                        // if (date1.before(date2)) {
                        // return 1;
                        // }
                        // return -1;
                        // }
                        // });
                        // }
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(getActivity(), ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.GET_ALL_COMMENT, false, 1);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        getDataiy(true);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        getDataiy(false);
    }

    private FragmentCallBack callback;

    @Override
    public void setFragmentCallBack(FragmentCallBack callback) {
        this.callback = callback;

    }

}
