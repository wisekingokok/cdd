/**
 *
 */

package com.chewuwuyou.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.MyWorkAdapter;
import com.chewuwuyou.app.adapter.OrderQueryAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.ValueTask;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单查询
 * liuchun
 */
public class TaskSearchActivity extends CDDBaseActivity implements OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    private ImageButton mBarLeftIbtn;//返回上一页
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mBarTV;//标题
    @ViewInject(id = R.id.search_content_et)
    private EditText mContentET;//搜索
    @ViewInject(id = R.id.search_iv)
    private ImageView mSearchIv;//点击搜索按钮
    @ViewInject(id = R.id.task_list)
    private ListView mTaskList;//列表
    @ViewInject(id = R.id.secretary_order_nothing)
    private TextView mSecretaryOrderNothing;//没有订单


    private OrderQueryAdapter mOrderQuiery;
    private List<ValueTask> mTasks;// 任务集合
    private String POST_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_search_layout);
        initView();
        initData();
        initEvent();

    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mBarTV.setText("订单查询");
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        if (CacheTools.getUserData("role").contains("2") || CacheTools.getUserData("role").contains("3")) {//商家角色
            POST_URL=NetworkUtil.ORDER_NUMBER;
        } else {
            POST_URL=NetworkUtil.TASK_MANAGER_URL;
        }
        mTasks = new ArrayList<ValueTask>();
        mOrderQuiery = new OrderQueryAdapter(TaskSearchActivity.this, mTasks);
        mTaskList.setAdapter(mOrderQuiery);
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mBarLeftIbtn.setOnClickListener(this);
        mSearchIv.setOnClickListener(this);
        /**
         * 订单点击事件监听
         */
        mTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mOrderQuiery.getOperating()) {
                    ValueTask task = (ValueTask) mOrderQuiery.getItem(position - 1);
                    task.isCheck = !task.isCheck;
                    mOrderQuiery.notifyDataSetChanged();
                    return;
                }
                ValueTask task = mTasks.get(position);
                String flag = task.getFlag();
                String status = task.getStatus();
                Intent intent = null;
                if (status.equals("2") && flag.equals("3")
                        && task.getFacilitatorId().equals(CacheTools.getUserData("userId"))) {
                    intent = new Intent(TaskSearchActivity.this, NewRobOrderDetailsActivity.class);
                    intent.putExtra("isBCancel", 1);// 区分已与其他服务商成交

                } else if (status.equals("28") && (flag.equals("1") || flag.equals("2"))) {
                    intent = new Intent(TaskSearchActivity.this, NewRobOrderDetailsActivity.class);
                } else if (flag.equals("3") && (status.equals("27") || status.equals("28"))) {
                    intent = new Intent(TaskSearchActivity.this, ToquoteAcitivity.class);
                } else {
                    intent = new Intent(TaskSearchActivity.this, OrderActivity.class);
                }
                intent.putExtra("taskId", mTasks.get(position).getId());
                startActivity(intent);

            }
        });
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;
            case R.id.search_iv://搜索
                if (TextUtils.isEmpty(mContentET.getText().toString())) {
                    ToastUtil.toastShow(TaskSearchActivity.this, "请输入订单号");
                } else {
                    queryorder();//查询订单信息
                }
                break;
        }
    }

    /**
     * 查询订单
     */
    public void queryorder() {
        mTasks.clear();//将数据清空
        AjaxParams params = new AjaxParams();
        params.put("vin", mContentET.getText().toString());
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        List<ValueTask> mTask = ValueTask.parseList(msg.obj.toString());
                        mTasks.addAll(mTask);
                        mOrderQuiery.notifyDataSetChanged();

                        if (mTasks.size() == 0) {
                            mTaskList.setVisibility(View.GONE);
                            mSecretaryOrderNothing.setVisibility(View.VISIBLE);
                        } else {
                            mTaskList.setVisibility(View.VISIBLE);
                            mSecretaryOrderNothing.setVisibility(View.GONE);
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(TaskSearchActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        }, params, POST_URL, false, 0);
    }
}
