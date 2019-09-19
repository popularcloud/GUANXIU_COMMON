package com.lwc.common.interf;

import android.database.sqlite.SQLiteDatabase;

/**
 * 操作数据库接口.
 * 
 * @Description 提供数据库操作接口.
 * @date 2013-5-31
 */
public interface OnOperationDataBase {

	/**
	 * 创建表接口.
	 * 
	 * @param db
	 *        数据库操作对象.
	 * 
	 * @version 1.0
	 * @createTime 2013-3-1,下午4:01:19
	 * @updateTime 2013-3-1,下午4:01:19
	 * @updateInfo
	 */
	void createTable(SQLiteDatabase db);

	/**
	 * 更新数据库接口.
	 * 
	 * @param db
	 *        数据库操作对象.
	 * @param oldVersion
	 *        老版本号.
	 * @param newVersion
	 *        新版本号.
	 * 
	 * @version 1.0
	 * @createTime 2013-3-1,下午4:02:10
	 * @updateTime 2013-3-1,下午4:02:10
	 * @updateInfo
	 */
	void updateDataBase(SQLiteDatabase db, int oldVersion, int newVersion);

}
