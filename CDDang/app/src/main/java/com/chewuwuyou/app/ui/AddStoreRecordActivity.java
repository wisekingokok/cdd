package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.StoreRecordAdapter;
import com.chewuwuyou.app.bean.InputOrderType;

/**
 * @describe:门店录单
 * @author:liuchun
 */
public class AddStoreRecordActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitel;// 标题
	private ImageButton mSubHeaderBarLeftIbtn;// 返回上一页
	private GridView mTypeStore;
	private List<InputOrderType> mInputList;
	private StoreRecordAdapter storeRecord;
	private EditText mStoreRecordItem;
	// private List<String> mlist;
	// private TextView mCurrent;
	private RelativeLayout mTitleHeight;// 标题布局高度

	/**
	 * 更新数据
	 */
	// private Handler mHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// switch (msg.what) {
	// case Constant.SEND_ADAPTER:
	// mCurrent.setText(msg.obj.toString());
	// break;
	// default:
	// break;
	// }
	// }
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_store_record);

		initView();// 初始化控件
		initData();// 逻辑处理
		initEvent();// 事件监听
	}

	/**
	 * 初始化
	 */
	@Override
	protected void initView() {
		mTitel = (TextView) findViewById(R.id.sub_header_bar_tv);
		mTitel.setText("门店录单");
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);// 返回上一页
		mTypeStore = (GridView) findViewById(R.id.type_store);
		mStoreRecordItem = (EditText) findViewById(R.id.store_record_item);
		// mCurrent = (TextView) findViewById(R.id.current);
		// mlist = new ArrayList<String>();
		mInputList = new ArrayList<InputOrderType>();
		mInputList.add(new InputOrderType(0, "违章处理", false));
		mInputList.add(new InputOrderType(0, "车辆处理", false));
		mInputList.add(new InputOrderType(0, "驾证服务", false));
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一頁
			finishActivity();
			break;

		default:
			break;
		}
	}

	/**
	 * 业务逻辑
	 */
	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		storeRecord = new StoreRecordAdapter(this, mInputList);
		mTypeStore.setAdapter(storeRecord);
	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
		mTypeStore.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < mInputList.size(); i++) {
					if (i == position) {
						mInputList.get(position).setSelected(true);
					} else {
						mInputList.get(i).setSelected(false);
					}
				}

				storeRecord.notifyDataSetChanged();
			}
		});

		/**
		 * 监听输入的字数
		 */
		mStoreRecordItem.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				/*
				 * if(){
				 * 
				 * }
				 * 
				 * mlist.add(s.toString().trim()); Message message = new
				 * Message(); message.what = Constant.SEND_ADAPTER; message.obj
				 * = mlist.size()+1; mHandler.sendMessage(message);
				 */
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

}
