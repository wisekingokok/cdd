
package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.DBHelper;
import com.chewuwuyou.app.utils.DBHelper.DBFIELD;

/**
 * @describe：消费详情
 * @author ：caixuemei
 * @created ：2014-6-7下午4:37:34
 */
public class ConsumptionDetailsActivity extends CDDBaseActivity {

    private TextView mNaviBarTitle;// 标题、广告
    private ImageButton mNaviBarBack;// 返回
    private TextView mRMB;// 金额
    private TextView mDisplayCategory;// 类别
    private TextView mDisplayDate;// 日期
    private TextView mRemark;// 备注
    private Button mBtnDelete;// 删除此条记录
    private String id;// 删除显示数据的时候，本页面数据ID
    private RelativeLayout mTitleHeight;//标题布局高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.consumption_details_ac);
        init();
    }

    /**
     * 初始化布局
     */
    private void init() {
    	mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
        mNaviBarTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
        mNaviBarTitle.setText(R.string.consumption_details);
        mNaviBarBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mNaviBarBack.setVisibility(View.VISIBLE);
        mRMB = (TextView) findViewById(R.id.rmb);
        mDisplayDate = (TextView) findViewById(R.id.display_date);
        mDisplayCategory = (TextView) findViewById(R.id.display_category);
        mRemark = (TextView) findViewById(R.id.remark);
        mBtnDelete = (Button) findViewById(R.id.btnDelete);

        // 获取传递过来的数据
        id = getIntent().getStringExtra(DBFIELD.ID);
        String money = getIntent().getStringExtra(DBFIELD.MONEY);
        String category = getIntent().getStringExtra(DBFIELD.CATEGORY);
        String time = getIntent().getStringExtra(DBFIELD.TIME);
        String comment = getIntent().getStringExtra(DBFIELD.COMMENT);
        //mRMB.setText(money + "元");
        mRMB.setText(getString(R.string.price_contain_suffix, money));
        mDisplayCategory.setText(category);
        mDisplayDate.setText(time);
        mRemark.setHint("");
        mRemark.setText(comment);

        // 返回
        mNaviBarBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finishActivity();
            }
        });

        /**
         * 删除此条记录
         */
        mBtnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            	StatService.onEvent(ConsumptionDetailsActivity.this, "clickConsumptionDetailsDelBtn", "点击消费详情删除按钮");
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ConsumptionDetailsActivity.this);
                builder.setMessage("确定删除吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dh = new DBHelper(ConsumptionDetailsActivity.this);
                     	Intent intent = new Intent();
                        if (dh.deleteCarBookDataWithId(id)==true) {
                        	intent.setAction("com.chewuwuyou.app.ui.CarAccountDetailsActivity");
    						// 发送虚拟广播
                        	ConsumptionDetailsActivity.this.sendBroadcast(intent);
                        	  dh.close();
                             // 返回页面
                             finish();
                    }
                    }

                });
                
                // 取消
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                // 创建并显示
                builder.create().show();
            }
        });
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(ConsumptionDetailsActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(ConsumptionDetailsActivity.this);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}
}
