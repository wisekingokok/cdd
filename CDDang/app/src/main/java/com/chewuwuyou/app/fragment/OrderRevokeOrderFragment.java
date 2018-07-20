package com.chewuwuyou.app.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.OrderDoorAdapter;
import com.chewuwuyou.app.bean.DoorOrderAdapter;
import com.chewuwuyou.app.ui.OrderDetailsElementActivity;


/**
 * 撤销订单
 * @author Administrator
 *
 */

public class OrderRevokeOrderFragment extends BaseFragment {

	private View view;
    private ListView mRevokeOrder;
    private OrderDoorAdapter mOrederAdapter;//适配器
    private List<DoorOrderAdapter> mList;//集合
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.revoke_order_ft, null);
		initView();//初始化
		initData();//逻辑处理
		initEvent();//事件监听
		return view;
	}

	/**
	 * 初始化
	 */
	@Override
	protected void initView() {
		mRevokeOrder = (ListView) view.findViewById(R.id.revoke_order_list);

	}

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {
		mList = new ArrayList<DoorOrderAdapter>();
		for (int i = 0; i < 10; i++) {
			mList.add(new DoorOrderAdapter(0, "违章服务", "换休安装", "200.00", "14:02", "2015-6-18"));
		}
		mOrederAdapter = new OrderDoorAdapter(getActivity(), mList);
		mRevokeOrder.setAdapter(mOrederAdapter);
	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mRevokeOrder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), OrderDetailsElementActivity.class);
				intent.putExtra("ElementType", "5");
				startActivity(intent);
			}
		});
	}

}
