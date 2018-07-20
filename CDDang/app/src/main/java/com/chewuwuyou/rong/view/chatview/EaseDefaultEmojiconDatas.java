package com.chewuwuyou.rong.view.chatview;

import android.content.Context;

import com.chewuwuyou.app.R;
import com.chewuwuyou.rong.utils.ExpressionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EaseDefaultEmojiconDatas {

    private static String[] emojis = new String[]{
            EaseSmileUtils.ee_1,
            EaseSmileUtils.ee_2,
            EaseSmileUtils.ee_3,
            EaseSmileUtils.ee_4,
            EaseSmileUtils.ee_5,
            EaseSmileUtils.ee_6,
            EaseSmileUtils.ee_7,
            EaseSmileUtils.ee_8,
            EaseSmileUtils.ee_9,
            EaseSmileUtils.ee_10,
            EaseSmileUtils.ee_11,
            EaseSmileUtils.ee_12,
            EaseSmileUtils.ee_13,
            EaseSmileUtils.ee_14,
            EaseSmileUtils.ee_15,
            EaseSmileUtils.ee_16,
            EaseSmileUtils.ee_17,
            EaseSmileUtils.ee_18,
            EaseSmileUtils.ee_19,
            EaseSmileUtils.ee_20,
            EaseSmileUtils.ee_21,
            EaseSmileUtils.ee_22,
            EaseSmileUtils.ee_23,
            EaseSmileUtils.ee_24,
            EaseSmileUtils.ee_25,
            EaseSmileUtils.ee_26,
            EaseSmileUtils.ee_27,
            EaseSmileUtils.ee_28,
            EaseSmileUtils.ee_29,
            EaseSmileUtils.ee_30,
            EaseSmileUtils.ee_31,
            EaseSmileUtils.ee_32,
            EaseSmileUtils.ee_33,
            EaseSmileUtils.ee_34,
            EaseSmileUtils.ee_35,

    };

    private static int[] icons = {R.drawable.d_hehe};


//    private static final EaseEmojicon[] DATA = createData();
//
//    private static EaseEmojicon[] createData() {
//        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
//        for (int i = 0; i < icons.length; i++) {
//            datas[i] = new EaseEmojicon(icons[i], emojis[i]);
//        }
//        return datas;
//    }

//    public static EaseEmojicon[] getData() {
//        return DATA;
//    }

    /**
     * 获取颜文字数据
     *
     * @param context
     * @return
     */
    public static EaseEmojicon[] createData(Context context) {
        Map<String, String> ywzMap = new HashMap<>();
        List<String> icons = ExpressionUtil.initStaticFaces(context);//获取颜文字的描述
        ywzMap = ExpressionUtil.getFaceStrMap(context);
        EaseEmojicon[] datas = new EaseEmojicon[ywzMap.size()];
        for (int i = 0; i < icons.size(); i++) {
            datas[i] = new EaseEmojicon(icons.get(i), ywzMap.get(icons.get(i).substring(0, icons.get(i).length() - 4)));
        }
        return datas;
    }

    /**
     * 获取gif数据
     *
     * @param context
     * @return
     */
    public static EaseEmojicon[] createGifData(Context context) {
        List<String> icons = ExpressionUtil.initStaticGifs(context);
        EaseEmojicon[] datas = new EaseEmojicon[icons.size()];
        for (int i = 0; i < icons.size(); i++) {
            datas[i] = new EaseEmojicon(icons.get(i), icons.get(i), EaseEmojicon.Type.BIG_EXPRESSION);
        }
        return datas;
    }


}
