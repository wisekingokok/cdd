package com.chewuwuyou.eim.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * SQLite数据库的帮助类 该类属于扩展类,主要承担数据库初始化和版本升级使用,其他核心全由核心父类完成
 * 
 * @author sxk
 */
public class DataBaseHelper extends SDCardSQLiteOpenHelper {

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	// 多了一个好友添加状态friend_add_state 整形 0是指默认值,没被任何操作，1是同意，2是拒绝
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE if not exists [im_msg_his] ([_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, [content] NVARCHAR, [fileName] NVARCHAR,[msg_from] NVARCHAR,[msg_room] NVARCHAR, [msg_to] NVARCHAR, [msg_time] TEXT, [msg_type] INTEGER,[type] INTEGER);");
		db.execSQL("CREATE TABLE if not exists [im_notice]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [type] INTEGER, [title] NVARCHAR, [content] NVARCHAR, [notice_from] NVARCHAR, [notice_to] NVARCHAR,[notice_time] TEXT, [status] INTEGER, [friend_add_state] INTEGER);");
		// 创建群聊数据表
		// db.execSQL("CREATE TABLE if not exists [im_msg_room] ([_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, [content] NVARCHAR,[msg_from] NVARCHAR,[msg_room] NVARCHAR, [msg_to] NVARCHAR, [msg_time] TEXT, [msg_type] INTEGER,[type] INTEGER);");

		// add start by yuyong 创建聊天数据表 对聊天数据表进行升级 2016/05/10
		// db.execSQL("CREATE TABLE if not exists [im_msg_his] ([_id] INTEGER PRIMARY KEY AUTOINCREMENT, [content] text, [msg_from] text, [msg_to] text, [msg_time] text, [msg_type] text, [msg_myId] text, [msg_resource_type] text, [file_path] text, [read] integer);");
		// add end by yuyong 创建聊天数据表 对聊天数据表进行升级 2016/05/10
		// add start by yuyong 创建主页消息页面的聊天数据表
		// db.execSQL("create table if not exists meaasgeList (id INTEGER PRIMARY KEY AUTOINCREMENT, myUserId text, fromUserId text, type text, content text, time text, nickName text)");
		// add end by yuyong 创建主页消息页面的聊天数据表 2016/05/10
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
