package com.chewuwuyou.app.transition_view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_presenter.RedPacketPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedPacketDetailActivity extends BaseActivity implements View.OnClickListener {


    private static final String REDPACKET_ID = "redpacket_id";
    private static final String REDPACKET = "redpacket";
    public static final String REDPACKET_TYPE = "red_packet_type";
    //单聊 我的视角
    /**
     * 红包我的视角 单聊 未领取
     */
    public static int MY_SINGLE_NOGET = 1;
    /**
     * 红包我的视角 单聊 已过期  (已退款)
     */
    public static int MY_SINGLE_GUOQI = 2;
    /**
     * 红包我的视角 单聊 已领取
     */
    public static int MY_SINGLE_GET = 3;

    //对方视角
    /**
     * 红包对方视角  单聊  已领取  （只有一个情况，过期不会弹到此界面）
     */
    public static int TA_SINGLE_GET = 4;

    //群聊

    /**
     * 红包我的视角 群聊 过期 （ 没有领完或者没有领,有退款）
     */
    public static int MY_MULTI_GUOQI;
    /**
     * 红包我的视角 群聊 未过期（包含所有情况，领完，没有领完）
     */
    public static int MY_MULTI_GET;

    /**
     * 对方视角 群聊  领取（过期不调到此页面，点击查看手气能够查看领取信息）
     */
    public static int TA_MULTI_GET;

    RedAdapter mAdapter;
    List<RedPacketDetailEntity.RedPerson> mList = new ArrayList<>();

    int state;
    @BindView(R.id.sub_header_bar_left_ibtn)
    ImageButton mSubHeaderBarLeftIbtn;
    @BindView(R.id.sub_header_bar_left_tv)
    TextView mSubHeaderBarLeftTv;
    @BindView(R.id.sub_header_bar_tv)
    TextView mSubHeaderBarTv;
    @BindView(R.id.sub_header_bar_right_tv)
    TextView mSubHeaderBarRightTv;
    @BindView(R.id.sub_header_bar_right_ibtn)
    ImageButton mSubHeaderBarRightIbtn;
    @BindView(R.id.title_height)
    RelativeLayout mTitleHeight;
    @BindView(R.id.lv_info)
    AutoLoadListView mListView;

    ImageView mHead;

    TextView mName;

    TextView mMoney;

    TextView mInfo;

    TextView mState;

    TextView mMark;

    LinearLayout mLinearLayout;

    RedPacketPresenter mPresenter;
    int page;
    RedPacketDetailEntity mRedPacketDetailEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpackets_detail);
        ButterKnife.bind(this);
        setBarColor(R.color.color_fb4d55);
        initView();

    }

    private void initView() {
        mPresenter = new RedPacketPresenter(this);

        state = getIntent().getIntExtra(REDPACKET_TYPE, 1);

        mRedPacketDetailEntity = (RedPacketDetailEntity) getIntent().getSerializableExtra(REDPACKET);

        mSubHeaderBarTv.setText(R.string.cddhb);
        View mView = getLayoutInflater().inflate(R.layout.item_hb_detail_header, null);
        mHead = (ImageView) mView.findViewById(R.id.ibtn_head);
        mName = (TextView) mView.findViewById(R.id.tv_name);
        mMoney = (TextView) mView.findViewById(R.id.tv_money);
        mInfo = (TextView) mView.findViewById(R.id.tv_info);//红包个数详情
        mState = (TextView) mView.findViewById(R.id.tv_state);
        mMark = (TextView) mView.findViewById(R.id.tv_mark);
        mLinearLayout = (LinearLayout) mView.findViewById(R.id.linear_info);

        setInfo();
        mListView.addHeaderView(mView);
        mAdapter = new RedAdapter(this, mList, R.layout.item_redpacket_detail);
        mListView.setAdapter(mAdapter);
        setEvent();

    }

    private void setInfo() {
        Glide.with(this).load(mRedPacketDetailEntity.getHeadImg()).crossFade().into(mHead);
        mName.setText(mRedPacketDetailEntity.getNickName() + getResources().getString(R.string.hb));
        mMark.setText(mRedPacketDetailEntity.getLeaveMessage());
        mMoney.setText(mRedPacketDetailEntity.getMoney() + "");
        if (mRedPacketDetailEntity.getType() == 0) {
            //单个红包，不显示下面的红包状态信息  且不开启加载更多
            mLinearLayout.setVisibility(View.GONE);
            mListView.setOpenLoad(false);

        } else {
            //多个红包
            mLinearLayout.setVisibility(View.VISIBLE);
            if ("2".equals(mRedPacketDetailEntity.getStatus()))
                mState.setVisibility(View.VISIBLE);//退款
            else
                mState.setVisibility(View.GONE);
            if (Integer.valueOf(TextUtils.isEmpty(mRedPacketDetailEntity.getRemainSize()) ? "0" : mRedPacketDetailEntity.getRemainSize()) < mRedPacketDetailEntity.getSize()) {
                int has_get_count = Integer.valueOf(TextUtils.isEmpty(mRedPacketDetailEntity.getRemainSize()) ? "0" : mRedPacketDetailEntity.getRemainSize());///已经领取个数
                double has_get_money = (Double.valueOf(TextUtils.isEmpty(mRedPacketDetailEntity.getRemainMoney()) ? "0" : mRedPacketDetailEntity.getRemainMoney()));//已经领取金额
                mInfo.setText("已领取" + has_get_count + "/" + mRedPacketDetailEntity.getSize() + "个," + "共" + has_get_money + "/" + mRedPacketDetailEntity.getMoney() + "元");

            } else//红包被抢完
            {
                mInfo.setText(mRedPacketDetailEntity.getSize() + "个红包共" + mRedPacketDetailEntity.getMoney() + "元，" + mRedPacketDetailEntity.getLootAllTime() + "秒被抢光");
            }
        }

        //mState.setText();
        mList.addAll(mRedPacketDetailEntity.finRedPacketsInDtos);

    }

    private void setEvent() {

        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mListView.setOnLoadNextListener(new AutoLoadListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getData(CacheTools.getUserData("userId"), mRedPacketDetailEntity.getId() + "", 10, page);
            }
        });

    }


    public void setData(RedPacketDetailEntity list) {

        if (list == null) {
            mListView.setState(LoadingFooter.State.TheEnd);
            return;
        }
        this.mList.addAll(list.finRedPacketsInDtos);
        mAdapter.notifyDataSetChanged();
        mListView.setState(LoadingFooter.State.Idle);
        page++;
    }

    public void setError() {

        mListView.setState(LoadingFooter.State.Idle);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finish();
                break;
            default:
                break;

        }
    }

    //adpater

    public class RedAdapter extends CommonAdapter<RedPacketDetailEntity.RedPerson> {

        Context mContext;

        public RedAdapter(Context context, List<RedPacketDetailEntity.RedPerson> datas, int layoutId) {
            super(context, datas, layoutId);
            mContext = context;
        }

        @Override
        public void convert(ViewHolder holder, RedPacketDetailEntity.RedPerson mRedPerson, int position) {

            if (mRedPerson.getTheBest() == 1)
                holder.getView(R.id.most).setVisibility(View.VISIBLE);
            else
                holder.getView(R.id.most).setVisibility(View.GONE);

            holder.setText(R.id.name, mRedPerson.getNickName());
            holder.setText(R.id.time, mRedPerson.getCreateAt());
            holder.setText(R.id.jine, mRedPerson.getMoney() + "");
            Glide.with(mContext).load(mRedPerson.getHeadImg()).centerCrop().transform(new GlideRoundTransform(mContext, 2)).crossFade().into((ImageView) holder.getView(R.id.head));

        }

    }

    /**
     * @param mContext
     * @param mRedPacketDetailEntity 红包实体
     */

    public static void launch(@NonNull Context mContext, @NonNull RedPacketDetailEntity mRedPacketDetailEntity) {

        Intent mIntent = new Intent(mContext, RedPacketDetailActivity.class);
        mIntent.putExtra(REDPACKET, mRedPacketDetailEntity);
        //   mIntent.putExtra(REDPACKET_TYPE, type);
        mContext.startActivity(mIntent);

    }

}
