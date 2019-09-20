package com.example.yfsl.clearcache_demo;

public class SQLTable {
    //表名
    public static final String TABLE_NAME = "demo";
    //姓名
    public static final String NAME = "name";
    //年龄
    public static final String OLD = "old";
    //是否结婚
    public static final String MARRIED = "married";
    //创建巡检录入信息表的sql
    public static final String CREATE_IET_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " text, "
            + OLD + " int, "
            + MARRIED + " text"
            + ");";
}
