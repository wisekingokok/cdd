package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ChoiceProblemAdapter;

/**
 * @describe:选择问题
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-29下午6:26:04
 */
public class ChoiceProblemActivity extends CDDBaseActivity implements OnClickListener{

	private ListView choiceList;// 列表
	private List<String> mList;
	private ChoiceProblemAdapter mChoiceAdapter;
	private TextView choiceTitle;// 问题标题
	private ImageButton mSubHeaderBarIbtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_problem);
		initView();// 初始化控件
		initData();
		initEvent();
	}


	/**
	 * 初始化控件
	 */
	protected void initView() {
		choiceList = (ListView) findViewById(R.id.choice_list);// 显示问题列表
		choiceTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		choiceTitle.setText("选择问题");
		mSubHeaderBarIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mList = new ArrayList<String>();
	}

	@Override
	protected void initData() {
		String[] problemArr=getResources().getStringArray(R.array.problem_arr);
		for (int i = 0; i < problemArr.length; i++) {
			mList.add(problemArr[i]);
		}
		Intent intent = getIntent();// 接手传递过来的值

		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).equals(intent.getStringExtra("Promptyi"))) {
				mList.remove(i);
			}
			if (mList.get(i).equals(intent.getStringExtra("Prompter"))) {
				mList.remove(i);
			}
			if (mList.get(i).equals(intent.getStringExtra("Promptsan"))) {
				mList.remove(i);
			}
		}
		mChoiceAdapter = new ChoiceProblemAdapter(ChoiceProblemActivity.this,
				mList);
		choiceList.setAdapter(mChoiceAdapter);
		
		choiceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("problem", mList.get(position));
				setResult(RESULT_OK, intent);
				finish();
			}

		});
	}

	@Override
	protected void initEvent() {
		mSubHeaderBarIbtn.setOnClickListener(this);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;

		default:
			break;
		}
	}

}
