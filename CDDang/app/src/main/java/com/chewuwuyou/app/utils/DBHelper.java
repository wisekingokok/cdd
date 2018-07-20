
package com.chewuwuyou.app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 数据库操作工具类
 * 
 * @author sxk
 */
public class DBHelper {

    public static class DBFIELD {
        public static final String ID = "id";
        public static final String MONEY = "money";
        public static final String CATEGORY = "category";
        public static final String TIME = "time";
        public static final String COMMENT = "comment";
    }

    private static final String TAG = "DBHelper";// 调试标签

    private static final String DATABASE_NAME = "db.sql";// 数据库名
    SQLiteDatabase db;
    Context context;// 应用环境上下文 Activity 是其子类

    public DBHelper(Context _context) {
        context = _context;
        // 开启数据库

        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        CreateCarBookTable();
        Log.v(TAG, "db path=" + db.getPath());
    }

    /**
     * 建表 列名 区分大小写？ 都有什么数据类型？ SQLite 3 TEXT 文本 NUMERIC 数值 INTEGER 整型 REAL 小数
     * NONE 无类型 查询可否发送select ?
     */
    public void CreateCarBookTable() {
        try {
            db.execSQL("create table if not exists carbook ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "money text, category text, time text, comment text)");
            Log.v(TAG, "Create Table carbook ok");
        } catch (Exception e) {
            Log.v(TAG, "Create Table carbook err,table exists.");
        }
    }

    /**
     * 增加数据
     * 
     * @param money
     * @param category
     * @param time
     * @param comment
     * @return
     */
    public boolean insertCarBookTableWith(String money, String category, String time, String comment) {
        String sql = "";
        try {
            sql = "insert into carbook (money, category, time, comment) values ("
                    + "'" + money + "',"
                    + "'" + category + "',"
                    + "'" + time + "',"
                    + "'" + comment + "')";
            db.execSQL(sql);
            Log.v(TAG, "insert Table carbook ok");
            return true;

        } catch (Exception e) {
            Log.v(TAG, "insert Table carbook err ,sql: " + sql);
            return false;
        }
    }

    /**
     * 查询所有记录
     * 
     * @return Cursor 指向结果记录的指针，类似于JDBC 的 ResultSet
     */
    public List<Map<String, String>> loadAll() {

        // Cursor cur=db.query("carbook",
        // new String[]{"id","money","category","time","comment"},
        // null,null, null, null, null);

        Cursor cur = db.rawQuery("select id,money,category,time,comment from carbook", null);

        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        while (cur.moveToNext()) {
            Map<String, String> subMap = new HashMap<String, String>();
            subMap.put(DBFIELD.ID, cur.getString(0));
            subMap.put(DBFIELD.MONEY, cur.getString(1));
            subMap.put(DBFIELD.CATEGORY, cur.getString(2));
            subMap.put(DBFIELD.TIME, cur.getString(3));
            subMap.put(DBFIELD.COMMENT, cur.getString(4));

            returnList.add(subMap);
        }
        cur.close();

        return returnList;
    }

    /**
     * 查询类别所有记录
     * 
     * @return Cursor 指向结果记录的指针，类似于JDBC 的 ResultSet
     */
    public List<Map<String, String>> queryCarBookWithCategory(String category) {

        // Cursor cur=db.query("carbook",
        // new String[]{"id","money","category","time","comment"},
        // null,null, null, null, null);

        Cursor cur = db.rawQuery(
                "select id,money,category,time,comment from carbook where category = '"
                        + category + "'",
                null);

        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        while (cur.moveToNext()) {
            Map<String, String> subMap = new HashMap<String, String>();
            subMap.put(DBFIELD.ID, cur.getString(0));
            subMap.put(DBFIELD.MONEY, cur.getString(1));
            subMap.put(DBFIELD.CATEGORY, cur.getString(2));
            subMap.put(DBFIELD.TIME, cur.getString(3));
            subMap.put(DBFIELD.COMMENT, cur.getString(4));

            returnList.add(subMap);
        }
        cur.close();

        return returnList;
    }

    /**
     * 查询时间相关所有记录
     * 
     * @return Cursor 指向结果记录的指针，类似于JDBC 的 ResultSet
     */
    public List<Map<String, String>> queryCarBookWithMonth(String month) {

        // Cursor cur=db.query("carbook",
        // new String[]{"id","money","category","time","comment"},
        // null,null, null, null, null);

        Cursor cur = db.rawQuery(
                "select id,money,category,time,comment from carbook where time like '%"
                        + month + "%'",
                null);

        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        while (cur.moveToNext()) {
            Map<String, String> subMap = new HashMap<String, String>();
            subMap.put(DBFIELD.ID, cur.getString(0));
            subMap.put(DBFIELD.MONEY, cur.getString(1));
            subMap.put(DBFIELD.CATEGORY, cur.getString(2));
            subMap.put(DBFIELD.TIME, cur.getString(3));
            subMap.put(DBFIELD.COMMENT, cur.getString(4));

            returnList.add(subMap);
        }
        cur.close();

        return returnList;
    }

    /**
     * 查询类别和月份消费详情,类型为全部时查询月份相关，时间为空时查询类型相关
     * 
     * @return Cursor 指向结果记录的指针，类似于JDBC 的 ResultSet
     */
    public List<Map<String, String>> queryCarBookWithCategoryAndMonth(String category, String month) {

        // Cursor cur=db.query("carbook",
        // new String[]{"id","money","category","time","comment"},
        // null,null, null, null, null);

        Cursor cur;
        if (category.equals("所有") && month.equals("所有")) {
            cur = db.rawQuery("select id,money,category,time,comment from carbook", null);
        } else if (category.equals("所有") && month.isEmpty() == false) {
            cur = db.rawQuery(
                    "select id,money,category,time,comment from carbook where time like '%"
                            + month + "%'",
                    null);
        } else if (category.isEmpty() == false && month.equals("所有")) {
            cur = db.rawQuery(
                    "select id,money,category,time,comment from carbook where category = '"
                            + category + "'",
                    null);
        } else {
            cur = db.rawQuery(
                    "select id,money,category,time,comment from carbook where category = '"
                            + category + "'" + " and time like '%"
                            + month + "%'",
                    null);
        }

        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        while (cur.moveToNext()) {
            Map<String, String> subMap = new HashMap<String, String>();
            subMap.put(DBFIELD.ID, cur.getString(0));
            subMap.put(DBFIELD.MONEY, cur.getString(1));
            subMap.put(DBFIELD.CATEGORY, cur.getString(2));
            subMap.put(DBFIELD.TIME, cur.getString(3));
            subMap.put(DBFIELD.COMMENT, cur.getString(4));

            returnList.add(subMap);
        }
        cur.close();

        return returnList;
    }

    /**
     * 查询类别和月份消费详情,类型为全部时查询月份相关，时间为空时查询类型相关
     * 
     * @return Cursor 指向结果记录的指针，类似于JDBC 的 ResultSet
     */
    public List<Map<String, String>> queryCarBookWithCategoryAndBetweenMonth(String category,
            String startDate, String endDate) {

        // Cursor cur=db.query("carbook",
        // new String[]{"id","money","category","time","comment"},
        // null,null, null, null, null);

        Cursor cur = null;
        if (category.equals("所有") && startDate.equals("") && endDate.equals("")) {
            cur = db.rawQuery("select id,money,category,time,comment from carbook", null);
        } else if (category.equals("所有") && !startDate.equals("") && endDate.equals("")) {
            cur = db.rawQuery(
                    "select id,money,category,time,comment from carbook where time>='"
                            + startDate + "'",
                    null);
        } else if (category.equals("所有") && startDate.equals("") && !endDate.equals("")) {
            cur = db.rawQuery(
                    "select id,money,category,time,comment from carbook where time<='"
                            + endDate + "'",
                    null);
        } else if (category.equals("所有") && !startDate.equals("") && !endDate.equals("")) {
            cur = db.rawQuery(
                    "select id,money,category,time,comment from carbook where time between'"
                            + startDate + "' and '" + endDate + "'",
                    null);
        } else if (!category.equals("所有") && !startDate.equals("") && !endDate.equals("")) {
            cur = db.rawQuery(
                    "select id,money,category,time,comment from carbook where category = '"
                            + category + "' and time between'"
                            + startDate + "' and '" + endDate + "'",
                    null);
        } else if (!category.equals("所有") && startDate.equals("") && endDate.equals("")) {
            cur = db.rawQuery(
                    "select id,money,category,time,comment from carbook where category = '"
                            + category + "'",
                    null);
        } else if (!category.equals("所有") && !startDate.equals("") && endDate.equals("")) {
            cur = db.rawQuery(
                    "select id,money,category,time,comment from carbook where category = '"
                            + category + "'" + " and time >='"
                            + startDate + "'",
                    null);
        } else if (!category.equals("所有") && startDate.equals("") && !endDate.equals("")) {
            cur = db.rawQuery(
                    "select id,money,category,time,comment from carbook where category = '"
                            + category + "'" + " and time <='"
                            + endDate + "'",
                    null);
        }
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        while (cur.moveToNext()) {
            Map<String, String> subMap = new HashMap<String, String>();
            subMap.put(DBFIELD.ID, cur.getString(0));
            subMap.put(DBFIELD.MONEY, cur.getString(1));
            subMap.put(DBFIELD.CATEGORY, cur.getString(2));
            subMap.put(DBFIELD.TIME, cur.getString(3));
            subMap.put(DBFIELD.COMMENT, cur.getString(4));

            returnList.add(subMap);
        }
        cur.close();

        return returnList;
    }

    /**
     * 查询某月份相关的某种类别的所有记录 （类别为空时，取时间相关的所有记录） YYYY-MM （时间为空时，全部时间）
     * 
     * @return Cursor 指向结果记录的指针，类似于JDBC 的 ResultSet
     */
    public double queryMoneyWithCategoryAndMonth(String category, String month) {

        // Cursor cur=db.query("carbook",
        // new String[]{"id","money","category","time","comment"},
        // null,null, null, null, null);

        Cursor cur;
        if (month.isEmpty()) {
            if (category.isEmpty()) {
                cur = db.rawQuery(
                        "select sum(money) from carbook", null);
            } else {
                cur = db.rawQuery(
                        "select sum(money) from carbook where category = '" + category + "'",
                        null);
            }

        } else if (category.isEmpty()) {
            cur = db.rawQuery(
                    "select sum(money) from carbook where  time like '%" + month + "%'",
                    null);
        } else {
            cur = db.rawQuery(
                    "select sum(money) from carbook where category = '"
                            + category + "' and time like '%" + month + "%'", null);
        }

        float sumMoney = 0;
        if (cur.moveToNext()) {
            sumMoney = cur.getFloat(0);
        }
        cur.close();

        return sumMoney;
    }

    /**
     * 查询有哪些月份
     * 
     * @param category 种类
     * @return
     */
    public List<String> queryMonths(String category) {

        // Cursor cur=db.query("carbook",
        // new String[]{"id","money","category","time","comment"},
        // null,null, null, null, null);

        Cursor cur = db.rawQuery(
                "select distinct substr(time,1,7) from carbook"
                        + (
                        (null == category || category.isEmpty())
                                ? "" : (" where category = '" + category + "'")
                        ) + " group by time",
                null);

        List<String> returnList = new ArrayList<String>();
        while (cur.moveToNext()) {
            returnList.add(cur.getString(0));
        }
        cur.close();
        return returnList;
    }

    /**
     * 查询有哪些月份
     * 
     * @param category 种类
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<String>[] queryMoneyByMonth(String category) {

        // Cursor cur=db.query("carbook",
        // new String[]{"id","money","category","time","comment"},
        // null,null, null, null, null);

        List<String> months = queryMonths(category);
        List<String> moneys = new ArrayList<String>();
        for (String month : months) {
            double money = queryMoneyWithCategoryAndMonth(category, month);
            moneys.add("" + money);
        }

        return new List[] {
                months, moneys
        };
    }

    /**
     * 删除消费记录
     * 
     * @return Cursor 指向结果记录的指针，类似于JDBC 的 ResultSet
     */
    public boolean deleteCarBookDataWithId(String id) {

        try {

            db.execSQL("delete from carbook where id = '" + id + "'");
        } catch (Exception e) {
            Log.v(TAG, "insert Table carbook err ,sql: "
                    + "delete from carbook where id = '" + id + "'");
            return false;
        }

        return true;
    }

    public void close() {
        db.close();
    }

}
