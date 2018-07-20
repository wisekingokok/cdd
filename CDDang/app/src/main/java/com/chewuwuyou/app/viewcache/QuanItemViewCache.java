package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @describe:圈动态的viewcache
 * @author:XH
 * @created:
 */
public class QuanItemViewCache {
    private View mView;

    private RelativeLayout mQuanBackgroundRL;// 图像
    private ImageView mQuanBackgroundIV;// 图像
    private ImageView mQuanBackgroundMyAvatarIV;// 图像

    private ImageView mQuanItemAvatarIV;// 图像
    private TextView mQuanItemNameTV;// 昵称
    private TextView mQuanItemContentTV;// 内容
    private LinearLayout mQuanItemTuLL;
    private FrameLayout mQuanItemTuFL;
    private TextView mQuanItemDateTV;
    private TextView mQuanItemDeleteTV;
    private View nullTV;

    // private LinearLayout mQuanItemPingLL;
    // private LinearLayout mQuanItemZanLL;
    private ImageView mQuanItemPingIV;
    private TextView mQuanItemPingTV;
    private ImageView mQuanItemZanIV;
    // private TextView mQuanItemPingTV;
    // private TextView mQuanItemZanTV;
    private LinearLayout mPingDetailLL;
    // private JustifyTextView mPingTextView;
    private LinearLayout mZanDetailLL;
    private TextView mZanDetailTV;
    private TextView quanItemJubao;
    private View ll_bottom;

    private View mZanDetailDividerView;// zan_detail_divider_view

    private LinearLayout mZanAndPingDetailLL;// android:id="@+id/zan_and_ping_detail_ll"

    public QuanItemViewCache(View view) {
        this.mView = view;
    }

    public View getmView() {
        return mView;
    }

    public View getNullTV() {
        if (nullTV == null)
            nullTV = mView.findViewById(R.id.nullTV);
        return nullTV;
    }

    public View getLl_bottom() {
        if (ll_bottom == null)
            ll_bottom = mView.findViewById(R.id.ll_bottom);
        return ll_bottom;
    }

    public TextView getQuanItemJubao() {
        if (quanItemJubao == null) {
            quanItemJubao = (TextView) mView.findViewById(R.id.quan_item_jubao);
        }
        return quanItemJubao;
    }

    public RelativeLayout getmQuanBackgroundRL() {
        if (mQuanBackgroundRL == null) {
            mQuanBackgroundRL = (RelativeLayout) mView.findViewById(R.id.quan_bg_rl);
        }
        return mQuanBackgroundRL;
    }

    public ImageView getmQuanBackgroundIV() {
        if (mQuanBackgroundIV == null) {
            mQuanBackgroundIV = (ImageView) mView.findViewById(R.id.quan_bg_iv);
        }
        return mQuanBackgroundIV;
    }

    public ImageView getmQuanBackgroundMyAvatarIV() {
        if (mQuanBackgroundMyAvatarIV == null) {
            mQuanBackgroundMyAvatarIV = (ImageView) mView.findViewById(R.id.quan_bg_my_avatar_iv);
        }
        return mQuanBackgroundMyAvatarIV;
    }

    // 圈图像
    public ImageView getmQuanItemAvatarIV() {
        if (mQuanItemAvatarIV == null) {
            mQuanItemAvatarIV = (ImageView) mView.findViewById(R.id.quan_item_avatar_iv);
        }
        return mQuanItemAvatarIV;
    }

    // 圈名字
    public TextView getmQuanItemNameTV() {
        if (mQuanItemNameTV == null) {
            mQuanItemNameTV = (TextView) mView.findViewById(R.id.quan_item_name_tv);
        }
        return mQuanItemNameTV;
    }

    // 圈文本
    public TextView getmQuanItemContentTV() {
        if (mQuanItemContentTV == null) {
            mQuanItemContentTV = (TextView) mView.findViewById(R.id.quan_item_content_tv);
        }
        return mQuanItemContentTV;
    }

    // // 圈文本更多
    // public TextView getmQuanItemContentMoreTV() {
    // if (mQuanItemContentMoreTV == null) {
    // mQuanItemContentMoreTV = (TextView)
    // mView.findViewById(R.id.quan_item_content_more_tv);
    // }
    // return mQuanItemContentMoreTV;
    // }

    public LinearLayout getmQuanItemTuLL() {
        if (mQuanItemTuLL == null) {
            mQuanItemTuLL = (LinearLayout) mView.findViewById(R.id.quan_item_tus_ll);
        }
        return mQuanItemTuLL;
    }

    public FrameLayout getmQuanItemTuFL() {
        if (mQuanItemTuFL == null) {
            mQuanItemTuFL = (FrameLayout) mView.findViewById(R.id.quan_item_tus_fl);
        }
        return mQuanItemTuFL;
    }

    // 圈发布时间
    public TextView getmQuanItemDateTV() {
        if (mQuanItemDateTV == null) {
            mQuanItemDateTV = (TextView) mView.findViewById(R.id.quan_item_date_tv);
        }
        return mQuanItemDateTV;
    }

    public TextView getmQuanItemDeleteTV() {
        if (mQuanItemDeleteTV == null) {
            mQuanItemDeleteTV = (TextView) mView.findViewById(R.id.quan_item_delete_tv);
        }
        return mQuanItemDeleteTV;
    }

    // // 评价LinearLayout
    // public LinearLayout getmQuanItemPingLL() {
    // if (mQuanItemPingLL == null) {
    // mQuanItemPingLL = (LinearLayout)
    // mView.findViewById(R.id.quan_item_ping_ll);
    // }
    // return mQuanItemPingLL;
    // }
    //
    // // 赞LinearLayout
    // public LinearLayout getmQuanItemZanLL() {
    // if (mQuanItemZanLL == null) {
    // mQuanItemZanLL = (LinearLayout)
    // mView.findViewById(R.id.quan_item_zan_ll);
    // }
    // return mQuanItemZanLL;
    // }

    // 评价图标
    public ImageView getmQuanItemPingIV() {
        if (mQuanItemPingIV == null) {
            mQuanItemPingIV = (ImageView) mView.findViewById(R.id.quan_item_ping_iv);
        }
        return mQuanItemPingIV;
    }

    public TextView getmQuanItemPingTV() {
        if (mQuanItemPingTV == null) {
            mQuanItemPingTV = (TextView) mView.findViewById(R.id.quan_item_ping_tv);
        }
        return mQuanItemPingTV;
    }

    // 点赞图标
    public ImageView getmQuanItemZanIV() {
        if (mQuanItemZanIV == null) {
            mQuanItemZanIV = (ImageView) mView.findViewById(R.id.quan_item_zan_iv);
        }
        return mQuanItemZanIV;
    }

    //
    // // 评价数
    // public TextView getmQuanItemPingTV() {
    // if (mQuanItemPingTV == null) {
    // mQuanItemPingTV = (TextView) mView.findViewById(R.id.quan_item_ping_tv);
    // }
    // return mQuanItemPingTV;
    // }
    //
    // // 点赞数
    // public TextView getmQuanItemZanTV() {
    // if (mQuanItemZanTV == null) {
    // mQuanItemZanTV = (TextView) mView.findViewById(R.id.quan_item_zan_tv);
    // }
    // return mQuanItemZanTV;
    // }
    // 评论细节
    public LinearLayout getmPingDetailLL() {
        if (mPingDetailLL == null) {
            mPingDetailLL = (LinearLayout) mView.findViewById(R.id.ping_detail_ll);
        }
        return mPingDetailLL;
    }

    // public JustifyTextView getmPingTextView() {
    // if (mPingTextView == null) {
    // mPingTextView = (JustifyTextView)
    // mView.findViewById(R.id.ping_detail_tv);
    // }
    // return mPingTextView;
    // }

    public TextView getmZanDetailTV() {
        if (mZanDetailTV == null) {
            mZanDetailTV = (TextView) mView.findViewById(R.id.zan_detail_tv);
        }
        return mZanDetailTV;
    }

    public LinearLayout getmZanDetailLL() {
        if (mZanDetailLL == null) {
            mZanDetailLL = (LinearLayout) mView.findViewById(R.id.zan_detail_ll);
        }
        return mZanDetailLL;
    }

    public LinearLayout getmZanAndPingDetailLL() {
        if (mZanAndPingDetailLL == null) {
            mZanAndPingDetailLL = (LinearLayout) mView.findViewById(R.id.zan_and_ping_detail_ll);
        }
        return mZanAndPingDetailLL;
    }

    public View getmZanDetailDividerView() {
        if (mZanDetailDividerView == null) {
            mZanDetailDividerView = (View) mView.findViewById(R.id.zan_detail_divider_view);
        }
        return mZanDetailDividerView;
    }

}
