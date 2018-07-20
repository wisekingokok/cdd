package com.chewuwuyou.eim.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.eim.db.DBManager;
import com.chewuwuyou.eim.db.SQLiteTemplate;
import com.chewuwuyou.eim.db.SQLiteTemplate.RowMapper;
import com.chewuwuyou.eim.model.ChartHisBean;
import com.chewuwuyou.eim.model.IMMessage;
import com.chewuwuyou.eim.model.IMRoomMessage;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.util.StringUtil;

/**
 * 消息历史记录，
 *
 * @author sxk
 */
public class MessageManager {
    private static MessageManager messageManager = null;
    private static DBManager manager = null;

    private MessageManager(Context context) {
        // SharedPreferences sharedPre = context.getSharedPreferences(
        // Constant.LOGIN_SET, Context.MODE_PRIVATE);
        // String databaseName = sharedPre.getString(Constant.USERNAME, null);
        // 为每个登陆用户构建其聊天数据库
        String databaseName = CacheTools.getUserData("userId");
        manager = DBManager.getInstance(context, databaseName);
    }

    public static MessageManager getInstance(Context context) {

        if (messageManager == null) {
            messageManager = new MessageManager(context.getApplicationContext());
        }

        return messageManager;
    }

    /**
     * 保存消息.
     *
     * @param msg
     * @author sxk
     * @update 2012-5-16 下午3:23:15
     */
    public long saveIMMessage(IMMessage msg) {
        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        ContentValues contentValues = new ContentValues();
        if (StringUtil.notEmpty(msg.getContent())) {
            contentValues.put("content", StringUtil.doEmpty(msg.getContent()));
        }
        if (StringUtil.notEmpty(msg.getFromSubJid())) {
            contentValues.put("msg_from",
                    StringUtil.doEmpty(msg.getFromSubJid()));
        }
        if (StringUtil.notEmpty(msg.getFromRoom())) {
            contentValues
                    .put("msg_room", StringUtil.doEmpty(msg.getFromRoom()));
        }
        contentValues.put("msg_type", msg.getMsgType());
        contentValues.put("msg_time", msg.getTime());
        contentValues.put("type", msg.getType());
        contentValues.put("fileName", msg.getFileName());
        contentValues.put("msg_to", msg.getMsgTo());
        return st.insert("im_msg_his", contentValues);
    }

    /**
     * 保存消息.
     *
     * @param msg
     * @author sxk
     * @update 2012-5-16 下午3:23:15
     */
    public long saveRoomIMMessage(IMRoomMessage msg) {
        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        Boolean isExist = st
                .isExistsBySQL(
                        "select count(msg_time) num from im_msg_room where msg_room=? and msg_from=? and content=? ",
                        new String[]{"" + msg.getFromRoom(),
                                "" + msg.getFromSubJid(), "" + msg.getContent()});

        // 存在就更新时间算了
        if (isExist) {
            ContentValues contentValues = new ContentValues();
            if (StringUtil.notEmpty(msg.getTime())) {
                contentValues
                        .put("msg_time", StringUtil.doEmpty(msg.getTime()));
            }
            return st.update("im_msg_room", contentValues,
                    "msg_room=? and msg_from=? and content=?", new String[]{
                            "" + msg.getFromRoom(), "" + msg.getFromSubJid(),
                            "" + msg.getContent()});
        } else {
            ContentValues contentValues = new ContentValues();
            if (StringUtil.notEmpty(msg.getContent())) {
                contentValues.put("content",
                        StringUtil.doEmpty(msg.getContent()));
            }
            if (StringUtil.notEmpty(msg.getFromSubJid())) {
                contentValues.put("msg_from",
                        StringUtil.doEmpty(msg.getFromSubJid()));
            }
            if (StringUtil.notEmpty(msg.getFromRoom())) {
                contentValues.put("msg_room",
                        StringUtil.doEmpty(msg.getFromRoom()));
            }
            contentValues.put("msg_type", msg.getMsgType());
            contentValues.put("msg_time", msg.getTime());
            contentValues.put("type", msg.getType());
            return st.insert("im_msg_room", contentValues);
        }
    }

    /**
     * 根据消息内容更新消息
     *
     * @param content
     * @param fileName
     * @return
     */
    public int updateIMMessage(String content, String fileName) {

        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        ContentValues contentValues = new ContentValues();
        if (StringUtil.notEmpty(content)) {
            contentValues.put("fileName", StringUtil.doEmpty(fileName));
        }
        return st.updatefileNameByContent("im_msg_his", content, contentValues);
    }

    /**
     * 更新状态.
     *
     * @param status
     * @author sxk
     * @update 2012-5-16 下午3:22:44
     */
    public void updateStatus(String id, Integer status) {
        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        st.updateById("im_msg_his", id, contentValues);
    }

    /**
     * 查找与某人的聊天记录聊天记录
     *
     * @param pageNum  第几页
     * @param pageSize 要查的记录条数
     * @return
     * @author sxk
     * @update 2012-7-2 上午9:31:04
     */
    public List<IMMessage> getMessageListByFrom(String fromUser, int pageNum,
                                                int pageSize) {
        if (StringUtil.empty(fromUser)) {
            return null;
        }
        int fromIndex = (pageNum - 1) * pageSize;
        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        List<IMMessage> list = st.queryForList(
                new RowMapper<IMMessage>() {
                    @Override
                    public IMMessage mapRow(Cursor cursor, int index) {
                        IMMessage msg = new IMMessage();
                        msg.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                        msg.setContent(cursor.getString(cursor
                                .getColumnIndex("content")));
                        msg.setFromSubJid(cursor.getString(cursor
                                .getColumnIndex("msg_from")));
                        msg.setMsgType(cursor.getInt(cursor
                                .getColumnIndex("msg_type")));
                        msg.setTime(cursor.getString(cursor
                                .getColumnIndex("msg_time")));
                        msg.setType(cursor.getInt(cursor.getColumnIndex("type")));
                        msg.setFileName(cursor.getString(cursor
                                .getColumnIndex("fileName")));
                        return msg;
                    }
                },
                "select _id,content,msg_from, msg_type,msg_time,type,fileName from im_msg_his where msg_from=? and msg_to= '" + CacheTools.getUserData("userId") + "' order by msg_time desc limit ? , ? ",
                new String[]{"" + fromUser, "" + fromIndex, "" + pageSize});
        return list;

    }

    /**
     * 查找与某人的聊天记录聊天记录
     *
     * @param pageNum  第几页
     * @param pageSize 要查的记录条数
     * @return
     * @author sxk
     * @update 2012-7-2 上午9:31:04
     */
    public List<IMMessage> getMessageListByRoom(String fromUser, int pageNum,
                                                int pageSize) {
        if (StringUtil.empty(fromUser)) {
            return null;
        }
        int fromIndex = (pageNum - 1) * pageSize;
        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        List<IMMessage> list = st.queryForList(
                new RowMapper<IMMessage>() {
                    @Override
                    public IMMessage mapRow(Cursor cursor, int index) {
                        IMMessage msg = new IMMessage();
                        msg.setContent(cursor.getString(cursor
                                .getColumnIndex("content")));
                        msg.setFromRoom(cursor.getString(cursor
                                .getColumnIndex("msg_room")));
                        msg.setFromSubJid(cursor.getString(cursor
                                .getColumnIndex("msg_from")));
                        msg.setMsgType(cursor.getInt(cursor
                                .getColumnIndex("msg_type")));
                        msg.setTime(cursor.getString(cursor
                                .getColumnIndex("msg_time")));
                        msg.setType(cursor.getInt(cursor.getColumnIndex("type")));
                        msg.setFileName(cursor.getString(cursor
                                .getColumnIndex("fileName")));
                        return msg;
                    }
                },
                "select content,msg_room,msg_from, msg_type,msg_time,type,fileName from im_msg_his where msg_room=? order by msg_time desc limit ? , ? ",
                new String[]{"" + fromUser, "" + fromIndex, "" + pageSize});
        return list;

    }

    /**
     * 查找与某人的聊天记录总数
     *
     * @return
     * @author sxk
     * @update 2012-7-2 上午9:31:04
     */
    public int getChatCountWithSb(String fromUser) {
        if (StringUtil.empty(fromUser)) {
            return 0;
        }
        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        return st
                .getCount(
                        "select _id,content,msg_from msg_type  from im_msg_his where msg_from=? and  msg_to =  '" + CacheTools.getUserData("userId") + "'",
                        new String[]{"" + fromUser});

    }

    /**
     * 删除与某人的聊天记录 author shimiso
     *
     * @param fromUser
     */
    public int delChatHisWithSb(String fromUser) {
        if (StringUtil.empty(fromUser)) {
            return 0;
        }
        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        return st.deleteByCondition("im_msg_his", "msg_from=?",
                new String[]{"" + fromUser});
    }

    /**
     * 删除群组聊天信息
     */
    public int delChatRoom(String room) {
        if (StringUtil.empty(room)) {
            return 0;
        }
        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        return st.deleteByCondition("im_msg_his", "msg_room=?",
                new String[]{"" + room});
    }

    /**
     * 获取最近聊天人聊天最后一条消息和未读消息总数
     *
     * @return
     * @author sxk
     * @update 2012-5-16 下午3:22:53
     */
    public List<ChartHisBean> getRecentContactsWithLastMsg() {
        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        String sql = "select m.[_id],m.[content],m.[msg_time],m.[msg_from],m.[msg_room],m.[msg_to] from im_msg_his  m join (select msg_from,max(msg_time) as time from im_msg_his group by msg_from) as tem  on  tem.time=m.msg_time and tem.msg_from=m.msg_from and m.msg_to = "
                + CacheTools.getUserData("userId");
        List<ChartHisBean> list = st.queryForList(
                new RowMapper<ChartHisBean>() {

                    @Override
                    public ChartHisBean mapRow(Cursor cursor, int index) {
                        ChartHisBean notice = new ChartHisBean();
                        notice.setId(cursor.getString(cursor
                                .getColumnIndex("_id")));
                        notice.setContent(cursor.getString(cursor
                                .getColumnIndex("content")));
                        notice.setFrom(cursor.getString(cursor
                                .getColumnIndex("msg_from")));
                        notice.setNoticeTime(cursor.getString(cursor
                                .getColumnIndex("msg_time")));
                        notice.setRoom(cursor.getString(cursor
                                .getColumnIndex("msg_room")));
                        notice.setTo(cursor.getString(cursor
                                .getColumnIndex("msg_to")));
                        return notice;
                    }
                }, sql, null);

        List<ChartHisBean> totalRemovingList = new ArrayList<ChartHisBean>(); // 储存用来删掉的
        for (int i = 0; i < list.size(); i++) {
            // 现在的
            ChartHisBean nowHis = list.get(i);
            List<ChartHisBean> removingList = new ArrayList<ChartHisBean>(); // 储存用来删掉的
            for (int j = i + 1; j < list.size(); j++) {

                // 下一个开始的
                ChartHisBean next = list.get(j);
                // 是群聊信息，并且是同一个房间
                if (!StringUtil.isNullOrEmpty(nowHis.getRoom())
                        && !StringUtil.isNullOrEmpty(next.getRoom())
                        && nowHis.getRoom().equals(next.getRoom())) {
                    // 冒泡去掉小的，留下大的时间的
                    if (nowHis.getNoticeTime().compareTo(next.getNoticeTime()) > 0) {
                        // 去掉小的
                        removingList.add(next);
                    } else {
                        // 去掉小的
                        removingList.add(nowHis);
                    }
                }
            }

            totalRemovingList.addAll(removingList);
        }
        // 去掉重复的
        list.removeAll(totalRemovingList);

        for (ChartHisBean b : list) {

            int count = st
                    .getCount(
                            "select _id from im_notice where status=? and type=? and notice_from=?",
                            new String[]{"" + Notice.UNREAD,
                                    "" + Notice.CHAT_MSG, b.getFrom()});
            b.setNoticeSum(count);
        }
        return list;
    }


    /**
     * 删除单条聊天记录
     *
     * @param fromUser
     */
    public int delItemChatHisWithSb(String fromUser, int id) {
        if (StringUtil.empty(fromUser)) {
            return 0;
        }
        SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
        return st.deleteByCondition("im_msg_his", "_id=?",
                new String[]{id+""});
    }
}
