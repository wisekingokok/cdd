package com.chewuwuyou.app.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.TieDetailHeaderEntity;
import com.chewuwuyou.app.bean.TiePingItem;
import com.chewuwuyou.app.bean.TiePingReplyItem;
import com.chewuwuyou.app.bean.TieTuItem;
import com.chewuwuyou.app.ui.HotTieDetailActivity;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.ui.VehicleQuanVewPager;
import com.chewuwuyou.app.ui.ZanOrInvolverActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ChatInputUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.viewcache.HotTieHeaderEntityViewCache;
import com.chewuwuyou.app.viewcache.TiePingItemViewCache;
import com.chewuwuyou.app.widget.HackyViewPager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @describe:论坛动态评论Adapter
 * @author:XH
 * @created:
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class HotTieDetailAdapter extends SNSAdapter implements OnClickListener {

    private List<Object> mData;
    private Handler mHandler;
    private Map<String, String> mFaceCharacterMap;

    public HotTieDetailAdapter(Activity context, Handler handler, List<Object> data, HackyViewPager viewPager,
                               View container) {
        super(context, viewPager, container);
        this.mData = data;
        this.mHandler = handler;
        this.mFaceCharacterMap = JsonUtil.getFaceStrMap(context);

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return (TieDetailHeaderEntity) mData.get(position);
        } else {
            return (TiePingItem) mData.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Integer position_integer = Integer.valueOf(position);
        if (position == 0) {
            HotTieHeaderEntityViewCache viewCache = null;
            convertView = mInflater.inflate(R.layout.hot_tie_detail_headerview, null);
            viewCache = new HotTieHeaderEntityViewCache(convertView);
            convertView.setTag(viewCache);

            TieDetailHeaderEntity item = (TieDetailHeaderEntity) mData.get(position);
            ImageUtils.displayImage(item.getUrl(), viewCache.getmDetailAvatarIV(), 360, R.drawable.user_fang_icon,
                    R.drawable.user_fang_icon);
            viewCache.getmDetailAvatarIV().setOnClickListener(this);
            viewCache.getmDetailAvatarIV().setTag(position_integer);
            viewCache.getmDetailNameTV().setText(CarFriendQuanUtils.showCarFriendName(item));
            viewCache.getmDetailContentTV()
                    .setText(ChatInputUtils.displayFacePic(mContext, item.getContent(), mFaceCharacterMap));

            if (item.getTus().size() > 0) {
                viewCache.getmDetailTuLL().setVisibility(View.VISIBLE);
            } else {
                viewCache.getmDetailTuLL().setVisibility(View.GONE);
            }
            List<TieTuItem> tus = item.getTus();
            final ArrayList<String> tuUrls = new ArrayList<String>();

            for (TieTuItem tu : tus) {
                tuUrls.add(tu.getUrl());
            }
            final LinearLayout tusLL = viewCache.getmDetailTuLL();
            tusLL.removeAllViews();
            for (int i = 0; i < tus.size(); i++) {
                final ImageView tuIV = new ImageView(mContext);
                tuIV.setAdjustViewBounds(true);
                tuIV.setScaleType(ScaleType.FIT_CENTER);
                LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tuIV.setLayoutParams(iv_params);
                tuIV.setImageResource(R.drawable.image_default);
                tuIV.setPadding(0, 3, 0, 3);
                String url = tus.get(i).getUrl();
                ImageUtils.displayImage(mContext, url, tuIV, 0, mBgTuWidth, mBgTuHeight, ScalingLogic.CROP,
                        R.drawable.image_default, R.drawable.image_default);
                final int viewPagerPosition = i;
                tuIV.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        //zoomImageFromThumb(arg0, tuUrls, tusLL, viewPagerPosition);
                        Intent intent = new Intent(mContext, VehicleQuanVewPager.class);
                        intent.putStringArrayListExtra("url",tuUrls);
                        intent.putExtra("viewPagerPosition",viewPagerPosition+"");
                        mContext.startActivity(intent);

                    }
                });
                tusLL.addView(tuIV);
            }

            viewCache.getmDetailTuLL().setOnClickListener(this);
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyyMMddHHmmss").parse(item.getPublishTime());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            viewCache.getmDetailDateTV().setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date));

            viewCache.getmDetailPingTV().setOnClickListener(this);
            viewCache.getmDetailPingTV().setTag(position_integer);

            viewCache.getmDetailZanTV().setOnClickListener(this);
            viewCache.getmDetailZanTV().setTag(position_integer);

            if (!TextUtils.isEmpty(item.getSex())) {
                if (item.getSex().equals("0")) {
                    viewCache.getmDetailSexIV().setImageResource(R.drawable.man);
                } else if (item.getSex().equals("1")) {
                    viewCache.getmDetailSexIV().setImageResource(R.drawable.woman);
                } else {
                    viewCache.getmDetailSexIV().setImageResource(R.drawable.icon_nosex);
                }
            }
            // viewCache.getmDetailSexIV().setImageResource(item.getSex().equals("1")
            // ? R.drawable.woman : R.drawable.man);
            viewCache.getmDetailZanTV()
                    .setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, item.getTiezancnt()));
            viewCache.getmDetailPingTV()
                    .setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, item.getTiepingcnt()));

            // 后面要改的

            if (Integer.parseInt(item.getZaned()) == 1) {
                viewCache.getmDetailZanTV().setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconheart, 0, 0, 0);
            } else {
                viewCache.getmDetailZanTV().setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconhear, 0, 0, 0);
            }
            // viewCache.getmDetailPingIV().setImageResource((item.getPinged()).equals("1")
            // ? R.drawable.reply : R.drawable.reply);
            // 显示赞列表
            if (item.getTiezancnt() > 0) {
                viewCache.getmDetailZanerLL().setVisibility(View.VISIBLE);
                int columWidth = mContext.getResources().getDimensionPixelSize(R.dimen.quan_zan_avatar_width);
                int numColumns = item.getTiezancnt();
                int totalWidth = numColumns * (columWidth
                        + 2 * mContext.getResources().getDimensionPixelSize(R.dimen.quan_zan_avatar_padding));
                LayoutParams params = new LayoutParams(totalWidth, LayoutParams.WRAP_CONTENT);
                viewCache.getmDetailZanerGV().setLayoutParams(params);
                viewCache.getmDetailZanerGV().setNumColumns(totalWidth);
                viewCache.getmDetailZanerGV().setColumnWidth(columWidth);
                viewCache.getmDetailZanerGV().setStretchMode(GridView.NO_STRETCH);
                TieZanAdapter tieZanAdapter = null;
                if (item.getTiezancnt() <= 6) {
                    tieZanAdapter = new TieZanAdapter(mContext, item.getTiezan());
                    // viewCache.getmDetailZanerMoreTV().setVisibility(View.GONE);
                } else {
                    tieZanAdapter = new TieZanAdapter(mContext, item.getTiezan().subList(0, 6));
                    // viewCache.getmDetailZanerMoreTV().setVisibility(View.VISIBLE);
                }
                viewCache.getmDetailZanerGV().setAdapter(tieZanAdapter);
                viewCache.getmDetailZanerGV().setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        Toast.makeText(mContext, "没做处理", Toast.LENGTH_SHORT).show();
                    }

                });
            } else {
                viewCache.getmDetailZanerLL().setVisibility(View.GONE);
            }

            if (item.getTiepingcnt() > 0) {
                viewCache.getmDetailPingTitleTV().setVisibility(View.VISIBLE);
                // viewCache.getmDetailPingTitleTV().setText(mContext.getString(R.string.tie_ping_title,
                // item.getTiepingcnt()));
                viewCache.getmDetailPingTitleTV().setText(mContext.getString(R.string.tie_ping_title));
            } else {
                viewCache.getmDetailPingTitleTV().setVisibility(View.GONE);
            }

            viewCache.getmDetailZanerLL().setOnClickListener(this);
            viewCache.getmDetailEditIV().setOnClickListener(this);
            viewCache.getmDetailEditIV().setTag(position_integer);

        } else {

            TiePingItemViewCache viewCache = null;
            if (convertView == null || !(convertView.getTag() instanceof TiePingItemViewCache)) {
                convertView = mInflater.inflate(R.layout.tie_ping_item, null);
                viewCache = new TiePingItemViewCache(convertView);
                convertView.setTag(viewCache);
            } else {
                viewCache = (TiePingItemViewCache) convertView.getTag();
            }

            TiePingItem item = (TiePingItem) mData.get(position);
            ImageUtils.displayImage(item.getUrl(), viewCache.getmPingAvatarIV(), 10, R.drawable.user_fang_icon,
                    R.drawable.user_fang_icon);

            viewCache.getmPingAvatarIV().setOnClickListener(this);
            viewCache.getmPingAvatarIV().setTag(position_integer);

            if (item.getFriendId().equals(CacheTools.getUserData("userId"))) {
                viewCache.getmPingReplyTV().setText("删除");
            } else {
                viewCache.getmPingReplyTV().setText("回复");
            }
            viewCache.getmPingReplyTV().setOnClickListener(this);
            viewCache.getmPingReplyTV().setTag(position_integer);

            viewCache.getmPingNameTV().setText(displayPingName(item));
            //显示表情
            viewCache.getmPingContentTV()
                    .setText(ChatInputUtils.displayBigFacePic(mContext, item.getContent(), mFaceCharacterMap));
            try {
                Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(item.getPublishTime());
                viewCache.getmPingDateTV().setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Message msg;
        Object tag = v.getTag();
        Integer position_integer = null;
        if (tag instanceof Integer) {
            position_integer = (Integer) tag;
        }
        switch (v.getId()) {
            case R.id.hot_tie_detail_avatar_iv:
                // 进入到个人详情中
                TieDetailHeaderEntity header = (TieDetailHeaderEntity) mData.get(position_integer.intValue());
                Intent header_intent = new Intent(mContext, PersonalHomeActivity2.class);
                header_intent.putExtra("userId", header.getUserId());
                mContext.startActivity(header_intent);
                break;
            case R.id.hot_tie_detail_ping_tv:
                // 发消息给Activity去弹出评价框和键盘
                msg = new Message();
                msg.what = HotTieDetailActivity.PING_TIE_ZI;
                mHandler.sendMessage(msg);
                break;
            case R.id.hot_tie_detail_zan_tv:
                // 切换赞状态
                msg = new Message();
                msg.what = HotTieDetailActivity.TOGGLE_ZAN;
                mHandler.sendMessage(msg);
                break;
            case R.id.hot_tie_detail_edit_iv:
                // 切换赞状态
                msg = new Message();
                msg.what = HotTieDetailActivity.EDIT_TIE;
                mHandler.sendMessage(msg);
                break;
            case R.id.ping_avatar_iv:
                // 进入到个人详情中
                TiePingItem ping = (TiePingItem) mData.get(position_integer.intValue());
                Intent ping_intent = new Intent(mContext, PersonalHomeActivity2.class);
                ping_intent.putExtra("userId", Integer.parseInt(ping.getFriendId()));
                mContext.startActivity(ping_intent);
                break;

            case R.id.ping_reply_tv:
                msg = new Message();
                TiePingItem item = (TiePingItem) mData.get(position_integer.intValue());
                if (item.getFriendId().equals(CacheTools.getUserData("userId"))) {
                    msg.what = HotTieDetailActivity.DELETE_PING;
                } else {
                    msg.what = HotTieDetailActivity.REPLY_PING;
                }
                msg.obj = item;
                mHandler.sendMessage(msg);
                break;

            case R.id.hot_tie_detail_zaner_ll:
                Intent intent = new Intent(mContext, ZanOrInvolverActivity.class);
                intent.setAction("com.chewuwuyou.app.show_hot_tie_zaner");
                Bundle bundle = new Bundle();
                bundle.putSerializable("tie_detail_header", (TieDetailHeaderEntity) mData.get(0));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                break;
            default:
                break;
        }
    }

    private SpannableString displayPingName(TiePingItem ping) {
        StringBuilder sb = new StringBuilder();
        SpannableString ssb = null;
        String name = CarFriendQuanUtils.showCarFriendName(ping);
        TiePingReplyItem toWho = ping.getToWho();

        try {
            if (toWho != null && toWho.getName() != null && toWho.getId() > 0) {
                String toname = toWho.getName();
                int length = mContext.getResources().getString(R.string.reply_to_who).length();
                sb.append(name).append(mContext.getResources().getString(R.string.reply_to_who)).append(toname);
                ssb = new SpannableString(sb.toString());
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), 0, name.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#434343")), name.length(), name.length() + length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), name.length() + length,
                        name.length() + length + toname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else {
                sb.append(name);
                ssb = new SpannableString(sb.toString());
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), 0, name.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new ClickSpan(Integer.parseInt(ping.getFriendId())), 0, name.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ssb;
    }

    public class ClickSpan extends ClickableSpan implements OnClickListener {
        private int uid;

        public ClickSpan(int uid) {
            this.uid = uid;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, PersonalHomeActivity2.class);
            intent.putExtra("userId", uid);
            mContext.startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

    }
}
