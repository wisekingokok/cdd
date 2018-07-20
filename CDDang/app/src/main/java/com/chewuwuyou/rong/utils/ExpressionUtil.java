package com.chewuwuyou.rong.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.FaceGVAdapter;
import com.chewuwuyou.app.adapter.GifGVAdapter;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.widget.AnimatedGifDrawable;
import com.chewuwuyou.app.widget.AnimatedImageSpan;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionUtil {

    public static Map<String, String> mFaceMap = new HashMap<String, String>();
    public static Map<String, String> mFaceCharaterMap;

    /**
     * 获取表情Map数据
     *
     * @param context
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> getFaceStrMap(Context context) {
        String key;
        try {
            JSONObject jo = new JSONObject(getFromAssets("face.json", context));
            Iterator iter = jo.keys();
            while (iter.hasNext()) {
                key = (String) iter.next();
                mFaceMap.put(jo.getString(key), key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mFaceMap;
    }

    public static SpannableStringBuilder prase(Context mContext,
                                               final TextView gifTextView, String content) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[[^\\]]+\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            try {
                String num = tempText.substring("[p/_".length(),
                        tempText.length() - ".png]".length());
                String gif = "g/" + num + ".gif";
                /**
                 * 如果open这里不抛异常说明存在gif，则显示对应的gif 否则说明gif找不到，则显示png\\[[^\\]]+\\]
                 * */
                InputStream is = mContext.getAssets().open(gif);
                sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(is,
                                new AnimatedGifDrawable.UpdateListener() {
                                    @Override
                                    public void update() {
                                        gifTextView.postInvalidate();
                                    }
                                })), m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                is.close();
            } catch (Exception e) {
                String png = tempText.substring("[".length(), tempText.length()
                        - "]".length());
                try {
                    sb.setSpan(
                            new ImageSpan(mContext, BitmapFactory
                                    .decodeStream(mContext.getAssets()
                                            .open(png))), m.start(), m.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return sb;
    }

    public static SpannableStringBuilder praseText(Context mContext,
                                                   final TextView textView, String content) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[[^\\]]+\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();

            try {
                String num = tempText.substring("[p/_".length(),
                        tempText.length() - ".png]".length());
                String gif = "g/" + num + ".gif";
                /**
                 * 如果open这里不抛异常说明存在gif，则显示对应的gif 否则说明gif找不到，则显示png\\[[^\\]]+\\]
                 * */
                InputStream is = mContext.getAssets().open(gif);
                sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(is,
                                new AnimatedGifDrawable.UpdateListener() {
                                    @Override
                                    public void update() {
                                        textView.postInvalidate();
                                    }
                                })), m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                is.close();
            } catch (Exception e) {
                String png = tempText.substring("[".length(), tempText.length()
                        - "]".length());
                try {
                    sb.setSpan(
                            new ImageSpan(mContext, BitmapFactory
                                    .decodeStream(mContext.getAssets()
                                            .open(png))), m.start(), m.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return sb;
    }

    public static SpannableStringBuilder getFace(Context mContext, String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
             *
             * */
            String tempText = mFaceMap.get(png.substring(0, png.length() - 4));
            MyLog.i("YUY", "tempText = " + tempText);
            sb.append(tempText);
            sb.setSpan(
                    new ImageSpan(mContext, BitmapFactory.decodeStream(mContext
                            .getAssets().open("face/" + png))),
                    sb.length() - tempText.length(), sb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb;
    }

    /**
     * 向输入框里添加表情
     */
    public static void insert(EditText input, CharSequence text) {
        MyLog.i("YUY", "------------------输入的表情文字 = " + text);
        int iCursorStart = Selection.getSelectionStart((input.getText()));
        int iCursorEnd = Selection.getSelectionEnd((input.getText()));
        if (iCursorStart != iCursorEnd) {
            ((Editable) input.getText()).replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((input.getText()));
        ((Editable) input.getText()).insert(iCursor, text);
    }

    /**
     * 删除图标执行事件
     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
     */
    public static void delete(EditText input) {

        int edittextCursor = input.getSelectionStart();
        Editable editable = input.getText();
        String tempString = input.getText().toString()
                .substring(0, edittextCursor);
        int end = tempString.lastIndexOf("]");
        if (end == edittextCursor - 1) {
            int start = tempString.lastIndexOf("[");
            if (start != -1) {
                editable.delete(start, edittextCursor);
            } else if (edittextCursor != 0) {
                editable.delete(edittextCursor - 1, edittextCursor);
            }
        } else if (edittextCursor != 0) {
            editable.delete(edittextCursor - 1, edittextCursor);
        }

    }

    /**
     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
     **/
    public static boolean isDeletePng(EditText input, int cursor) {
        String st = "[p/_000.png]";
        String content = input.getText().toString().substring(0, cursor);
        if (content.length() >= st.length()) {
            String checkStr = content.substring(content.length() - st.length(),
                    content.length());
            String regex = "\\[[^\\]]+\\]";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    /**
     * 初始化表情图片的显示
     *
     * @param context
     * @param position
     * @param staticFacesList
     * @param columns         行
     * @param rows            列
     * @param editText
     * @return
     */
    public static View viewPagerItem(final Context context, int position,
                                     List<String> staticFacesList, int columns, int rows,
                                     final EditText editText) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.face_gridview, null);// 表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
        /**
         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
         * */
        List<String> subList = new ArrayList<String>();
        subList.addAll(staticFacesList
                .subList(position * (columns * rows - 1),
                        (columns * rows - 1) * (position + 1) > staticFacesList
                                .size() ? staticFacesList.size() : (columns
                                * rows - 1)
                                * (position + 1)));
        /**
         * 末尾添加删除图标
         * */
        subList.add("_del.png");
        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, context);
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        // 单击表情执行的操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    String png = ((TextView) ((LinearLayout) view)
                            .getChildAt(1)).getText().toString();
                    MyLog.i("YUY", "png  =  " + png);
                    if (!png.contains("_del")) {// 如果不是删除图标
                        ExpressionUtil.insert(
                                editText,
                                ExpressionUtil.getFace(context,
                                        png.substring(5, png.length() - 4)));
                    } else {
                        ExpressionUtil.delete(editText);// 点击删除按钮
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return gridview;
    }

    /**
     * 初始化gif图
     *
     * @param context
     * @param position
     * @param staticGifsList
     * @param columns
     * @param rows
     * @param editText
     * @param mHandler
     * @return
     */
    public static View viewPagerGifItem(final Context context, int position,
                                        List<String> staticGifsList, int columns, int rows,
                                        final EditText editText, final Handler mHandler) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.face_gridview, null);// 表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
        /**
         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
         * */
        List<String> subList = new ArrayList<String>();
        subList.addAll(staticGifsList
                .subList(position * (columns * rows),
                        (columns * rows - 1) * (position + 1) > staticGifsList
                                .size() ? staticGifsList.size() : (columns
                                * rows - 1)
                                * (position + 1)));
        /**
         * 末尾添加删除图标
         * */
        // subList.add("_del.png");
        GifGVAdapter mGvAdapter = new GifGVAdapter(subList, context);
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        // 单击表情执行的操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    String png = ((TextView) ((LinearLayout) view)
                            .getChildAt(1)).getText().toString();

                    // gif png = static_emoticons/emotion10.png
                    // gif messageContent = #EMOemotion10.gifOME#
                    MyLog.i("YUY", "gif png = " + png);
                    if (!png.contains("_del")) {// 如果不是删除图标
                        String messageCon = png.split("/")[1];
                        String messageContent = "#EMO"
                                + messageCon.substring(0,
                                messageCon.length() - 4) + ".gif"
                                + "OME#";
                        Message msg = new Message();
                        msg.what = 12;
                        msg.obj = messageContent;
                        mHandler.sendMessage(msg);
                        // AChatActivity.sendMessage(messageContent);
                        MyLog.i("YUY", "gif messageContent = " + messageContent);
                        // ExpressionUtil.insert(editText,
                        // ExpressionUtil.getFace(context, png));
                    } else {
                        ExpressionUtil.delete(editText);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return gridview;
    }

    /**
     * 根据表情数量以及GridView设置的行数和列数计算Pager数量
     *
     * @return
     */
    public static int getPagerCount(int listsize, int columns, int rows) {
        return listsize % (columns * rows - 1) == 0 ? listsize
                / (columns * rows - 1) : listsize / (columns * rows - 1) + 1;
    }

    /**
     * 初始化表情列表staticFacesList
     */
    public static List<String> initStaticFaces(Context context) {
        List<String> facesList = null;
        try {
            facesList = new ArrayList<String>();
            String[] faces = context.getAssets().list("face");
            // 将Assets中的表情名称转为字符串一一添加进staticFacesList
            Log.e("YUY","---颜文字的长度 = "+faces.length);
            for (int i = 0; i < faces.length; i++) {
                facesList.add(faces[i]);
            }
            // 去掉删除图片
            facesList.remove("_del.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return facesList;
    }

    /**
     * 初始化表情列表staticFacesList
     */
    public static List<String> initStaticGifs(Context context) {
        List<String> facesList = null;
        try {
            facesList = new ArrayList<String>();
            String[] faces = context.getAssets().list("static_emoticons");
            // 将Assets中的表情名称转为字符串一一添加进staticFacesList
            for (int i = 0; i < faces.length; i++) {
                facesList.add(faces[i]);
            }
            // 去掉删除图片
            facesList.remove("_del.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return facesList;
    }

    /**
     * 从assets 文件夹中获取文件并读取数据
     *
     * @param fileName assets文件夹下的路径+文件名
     * @return
     */
    public static String getFromAssets(String fileName, Context context) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String[] getFaces(Context context) throws IOException {
        return context.getAssets().list("face");
    }
}
