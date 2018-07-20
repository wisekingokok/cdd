package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;

/**
 * @describe:查看评价
 * @author:liuchun
 */
public class OrderDetailsSee extends CDDBaseActivity implements OnClickListener{


	private TextView mTitleTV;//标题
	private ImageButton mBackIBtn;//返回上一页
	private RelativeLayout mTitleHeight;//标题布局高度
	private TextView mEvaluateTimeTV,mEvaluateContextTV;
	private RatingBar mOrderRating;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_see_ac);
		
		initView();
		initData();
		initEvent();
	}
	

	@Override
	protected void initView() {
		mEvaluateContextTV = (TextView) findViewById(R.id.order_context_valuate);
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mEvaluateTimeTV =(TextView) findViewById(R.id.evaluate_time);
		mOrderRating = (RatingBar) findViewById(R.id.order_rating);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			OrderDetailsSee.this.finishActivity();
			break;

		default:
			break;
		}
		
	}
	

	@Override
	protected void initData() {
		mTitleTV.setText("查看评价");
		String comment = getIntent().getStringExtra("id");
		if(!TextUtils.isEmpty(comment)){
			AjaxParams params = new AjaxParams();
			params.put("taskId", comment);
			seeComment(params);
		}
	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
	}

	/**
	 * 查看评价
	 */
	public void seeComment(AjaxParams params){
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					try {
						JSONArray jsonArray = new JSONArray(msg.obj.toString());
						if(jsonArray!=null){
							mEvaluateTimeTV.setText(""+jsonArray.getJSONObject(0).getString("time").toString().substring(0,19));
							mOrderRating.setRating((Integer.parseInt(jsonArray.getJSONObject(0).getString("star"))));
						    mEvaluateContextTV.setText(jsonArray.getJSONObject(0).getString("content").toString());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case Constant.NET_DATA_FAIL:
		
					break;
				default:
					break;
				}
			}
		}, params, NetworkUtil.SEE_COMMENT, false, 0);
	}


}
