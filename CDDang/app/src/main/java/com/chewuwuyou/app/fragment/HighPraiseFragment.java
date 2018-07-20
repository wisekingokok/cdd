package com.chewuwuyou.app.fragment;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.BusinessServicePro;
import com.chewuwuyou.app.bean.ServicePro;
import com.chewuwuyou.app.bean.TrafficBusinessListBook;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.ui.BusinessDetailActivity;
import com.chewuwuyou.app.ui.BusinessPersonalCenterActivity;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.widget.MyGridView;

import de.greenrobot.event.EventBus;

/**
 * 商家 好评
 * 
 * @author Administrator
 * 
 */
public class HighPraiseFragment extends BaseFragment implements OnClickListener {

	private View view;
	private CommonAdapter<BusinessServicePro> mCommonAdapter;// 服务项目适配器
	private List<BusinessServicePro> mList;// 服务项目集合
	private MyGridView mBusinessList;
    private TrafficBusinessListBook  mBusinessListBook;
	private TextView mServcie;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.high_praise_ft, null);

		initView();
		initData();
		initEvent();
		return view;
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		// 注册EventBus
		EventBus.getDefault().register(this);
		mBusinessList = (MyGridView) view.findViewById(R.id.business_my_list);
		mServcie = (TextView) view.findViewById(R.id.servcie);
		mBusinessList.setFocusable(false);// 让listview失去焦点
		mBusinessList.smoothScrollToPosition(0, 20);// 设置显示的位置
	}

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {

	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
			mBusinessList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

					mBusinessListBook.setFees(mList.get(position).getFees());
					mBusinessListBook.setServicePrice(mList.get(position).getServicePrice());
					mBusinessListBook.setLocation(mList.get(position).getLocation());
					ServicePro servicePro = new ServicePro(mList.get(position).getId(), mList.get(position).getFees(), mList.get(position).getType(), mList.get(position).getProjectName(), mList.get(position).getProjectNum(), mList.get(position).getProjectImg(), mList.get(position).getServiceDesc(), mList.get(position).getServiceFolder());

					Intent intent = new Intent(getActivity(),BusinessDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Constant.TRAFFIC_SER,mBusinessListBook);
					bundle.putSerializable("servicedata",servicePro);
					intent.putExtra("address", BusinessPersonalCenterActivity.address);
					intent.putExtras(bundle);
					getActivity().startActivity(intent);
				}
			});
	}

	/**
	 * EventBus接收传递的数据
	 *
	 * @param busAdapter
	 */
	public void onEventMainThread(EventBusAdapter busAdapter) {
		final DecimalFormat df = new DecimalFormat("0.00");
		if (busAdapter.getIndex() == 1) {
			mList = busAdapter.getmListComment();
			if(mList.size()>0){
				mBusinessListBook = busAdapter.getmBusinessListBook();
				mCommonAdapter = new CommonAdapter<BusinessServicePro>(getActivity(),
						mList, R.layout.business_type_item) {
					@Override
					public void convert(ViewHolder holder, BusinessServicePro t,int p) {
						holder.setText(R.id.type_name, t.getProjectName());
						holder.setText(R.id.type_amount, df.format(t.getServicePrice()) + "");

						ImageUtils.commDisplayImage(t.getProjectImg(),
								((ImageView) holder.getView(R.id.type_img)), 0,R.drawable.user_fang_icon);// 评论人头像

					}
				};
				mBusinessList.setAdapter(mCommonAdapter);
			}else{
				mBusinessList.setVisibility(View.GONE);
				mServcie.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View v) {

	}
}
