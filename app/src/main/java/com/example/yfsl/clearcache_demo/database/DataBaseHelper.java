package com.example.yfsl.clearcache_demo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.example.yfsl.clearcache_demo.SQLTable;


/**
 * 数据库
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "demo.db";
    //数据库版本创建是设置为1
    private static final int DB_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    /**
     * 创建表
     * @param db
     */
    private void createTable(SQLiteDatabase db) {
        //巡检录入表 存储器
        db.execSQL(SQLTable.CREATE_IET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createTable(db);
    }

    /**
     * 判断表格是否存在
     * @param tabName
     * @return
     */
    public boolean tabIsExist(String tabName){
        boolean result = false;
        if (TextUtils.isEmpty(tabName)){
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String sql = "select count(*) from sqlite_master where type ='table' and name ='" + tabName.trim() + "' ";
            cursor = db.rawQuery(sql,null);
            if (cursor.moveToNext()){
                int count = cursor.getInt(0);
                if (count>0){
                    result = true;
                }
            }
        }catch (Exception e){

        }
        return result;
    }
}
