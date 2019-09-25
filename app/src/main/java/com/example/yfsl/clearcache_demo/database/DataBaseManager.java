package com.example.yfsl.clearcache_demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yfsl.clearcache_demo.SQLTable;


public class DataBaseManager {
    private static DataBaseManager instance;
    private SQLiteDatabase readDatabase;
    private SQLiteDatabase writeDatabase;
    private DataBaseHelper dataBaseHelper;

    public DataBaseManager(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        readDatabase = dataBaseHelper.getReadableDatabase();
        writeDatabase = dataBaseHelper.getWritableDatabase();
    }

    public static DataBaseManager getInstance(Context context){
        if (instance == null){
            synchronized (DataBaseManager.class){
                if (instance == null){
                    instance = new DataBaseManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 存数据
     */
    public void saveData(ContentValues values){
        String name = (String) values.get(SQLTable.NAME);
        int old = (int)values.get(SQLTable.OLD);
        String married = (String) values.get(SQLTable.MARRIED);
        int update = writeDatabase.update(SQLTable.TABLE_NAME,values,
                SQLTable.NAME + "=? and "+SQLTable.OLD + "=? and "+SQLTable.MARRIED + "=?",
                new String[]{name, String.valueOf(old),married});
        if (update<=0){//数据库中没有这样的数据时
            writeDatabase.insert(SQLTable.TABLE_NAME,null,values);
        }
    }

    /**
     * 删除数据
     */
    public void deleteData(String name,int old,String married){
        writeDatabase.delete(SQLTable.TABLE_NAME,SQLTable.NAME + "=? and "+SQLTable.OLD + "=? and "+SQLTable.MARRIED + "=?",
                new String[]{name, String.valueOf(old),married});
    }

}
