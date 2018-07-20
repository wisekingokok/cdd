/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chewuwuyou.rong.view.chatview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.chewuwuyou.app.utils.MyLog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EaseSmileUtils {
    public static final String DELETE_KEY = "em_delete_delete_expression";

    public static final String ee_1 = "[爱你]";
    public static final String ee_2 = "[奥特曼]";
    public static final String ee_3 = "[友尽]";
    public static final String ee_4 = "[悲伤]";
    public static final String ee_5 = "[鄙视]";
    public static final String ee_6 = "[闭嘴]";
    public static final String ee_7 = "[馋嘴]";
    public static final String ee_8 = "[吃惊]";
    public static final String ee_9 = "[打哈欠]";
    public static final String ee_10 = "[打脸]";
    public static final String ee_11 = "[顶]";
    public static final String ee_12 = "[神烦狗]";
    public static final String ee_13 = "[肥皂]";
    public static final String ee_14 = "[感冒]";
    public static final String ee_15 = "[鼓掌]";
    public static final String ee_16 = "[哈哈]";
    public static final String ee_17 = "[害羞]";
    public static final String ee_18 = "[汗]";
    public static final String ee_19 = "[呵呵]";
    public static final String ee_20 = "[黑线]";
    public static final String ee_21 = "[哼]";
    public static final String ee_22 = "[花心]";
    public static final String ee_23 = "[调皮]";
    public static final String ee_24 = "[可爱]";
    public static final String ee_25 = "[可怜]";
    public static final String ee_26 = "[酷]";
    public static final String ee_27 = "[困]";
    public static final String ee_28 = "[懒得理你]";
    public static final String ee_29 = "[大哭]";
    public static final String ee_30 = "[马到成功]";
    public static final String ee_31 = "[包哥]";
    public static final String ee_32 = "[男孩儿]";
    public static final String ee_33 = "[愤怒]";
    public static final String ee_34 = "[怒骂]";
    public static final String ee_35 = "[女孩儿]";
    public static final String ee_36 = "[贪财]";
    public static final String ee_37 = "[么么哒]";
    public static final String ee_38 = "[傻眼]";
    public static final String ee_39 = "[生病]";
    public static final String ee_40 = "[草泥马]";

    private static final Factory spannableFactory = Factory
            .getInstance();

    private static final Map<Pattern, Object> emoticons = new HashMap<Pattern, Object>();


    static {
//        EaseEmojicon[] emojicons = EaseDefaultEmojiconDatas.getData();
//        for (int i = 0; i < emojicons.length; i++) {
//            addPattern(emojicons[i].getEmojiText(), emojicons[i].getIcon());
//        }
//        EaseEmojiconInfoProvider emojiconInfoProvider = EaseUI.getInstance().getEmojiconInfoProvider();
//        if (emojiconInfoProvider != null && emojiconInfoProvider.getTextEmojiconMapping() != null) {
//            for (Entry<String, Object> entry : emojiconInfoProvider.getTextEmojiconMapping().entrySet()) {
//                addPattern(entry.getKey(), entry.getValue());
//            }
//        }

    }

    /**
     * 添加文字表情mapping
     *
     * @param emojiText emoji文本内容
     * @param icon      图片资源id或者本地路径
     */
    public static void addPattern(String emojiText, Object icon) {
        emoticons.put(Pattern.compile(Pattern.quote(emojiText)), icon);
    }


    /**
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Object value = entry.getValue();
                    if (value instanceof String && !((String) value).startsWith("http")) {
                        File file = new File((String) value);
                        if (!file.exists() || file.isDirectory()) {
                            return false;
                        }
                        spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable.setSpan(new ImageSpan(context, (Integer) value),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }


    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static int getSmilesSize() {
        return emoticons.size();
    }


}
