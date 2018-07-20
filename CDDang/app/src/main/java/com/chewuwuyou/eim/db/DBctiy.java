package com.chewuwuyou.eim.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chewuwuyou.app.bean.LatelyCity;

import java.util.List;

/**
 * 最近访问城市
 *
 * @author xiehy
 */
public class DBctiy extends SQLiteOpenHelper {

    /**
     * 数据库名称
     */
    private final static String DATABASE_NAME = "ctiy.db";

    private final static int DATABASE_VERSION = 1;
    /**
     * 创建表
     */
    private final static String TABLE_NAME = "city";


    /**
     * 创建数据库
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public DBctiy(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 创建表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "
                + TABLE_NAME
                + "(id integer primary key,cityid text,provinceid text,countyid text,province text,city text, county text)";
        db.execSQL(sql);
    }

    /**
     * 添加数据
     *
     * @param cityid     城市ID
     * @param provinceid 省Id
     * @param countyid   区ID
     * @param province   省名称
     * @param city       城市名称
     * @param county     区名称
     */
    public void addCtiy(String cityid, String provinceid, String countyid, String province, String city, String county) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "insert into city(cityid,provinceid,countyid,province,city,county) values(?,?,?,?,?,?)";
        Object[] bindArgs = {cityid, provinceid, countyid, province, city, county};
        db.execSQL(sql, bindArgs);
        db.close();//关闭
    }

    /**
     * 查询数据
     *
     * @param city
     * @param time
     */
    public List<LatelyCity> seltelCtiy() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<LatelyCity> list = new java.util.ArrayList<LatelyCity>();
        String sql = "select * from city  order by id desc";
        String[] selectionArgs = {};
        android.database.Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            LatelyCity city = new LatelyCity();
            city.setId(cursor.getInt(0));
            city.setCityId(cursor.getInt(1));
            city.setProvinceId(cursor.getInt(2));
            city.setDistrictId(cursor.getInt(3));
            city.setProvince(cursor.getString(4));
            city.setCity(cursor.getString(5));
            city.setDistrict(cursor.getString(6));
            list.add(city);
        }
        db.close();//关闭
        cursor.close();//关闭
        return list;
    }

    /**
     * 删除数据
     *
     * @param city
     * @param time
     */
    public int deltelDistrict(String county) {
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = "county=?";//删除的条件
        String[] selectionArgs = {county};
        int is = db.delete("city", whereClause, selectionArgs);//执行删除
        db.close();//关闭
        return is;
    }

    public int deltelArea(String provinceName, String cityName, String district) {
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = "province=? and city=? and county=?";//删除的条件
        String[] selectionArgs = {provinceName, cityName, district};
        int is = db.delete("city", whereClause, selectionArgs);//执行删除
        db.close();//关闭
        return is;
    }

    /**
     * 删除数据
     *
     * @param city
     * @param time
     */
    public int deltelCtiy(String county) {
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = "city=?";//删除的条件
        String[] selectionArgs = {county};
        int is = db.delete("city", whereClause, selectionArgs);//执行删除
        db.close();//关闭
        return is;
    }

    /**
     * 版本升级时调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}
