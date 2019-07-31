package com.lib.common.hxp.db;

/**
 * Created by xpHuang on 2016/9/29.
 */
public class TimeLogSQL {
    public static final String TABLE_NAME = "t_time_log";
    public static final String ID = "id";//主键
    public static final String SIGNID = "signid";//机器的ID
//    public static final String NAME = "name";//产品的名称

    public static final String LOGTIME = "logtime";//记录时间
    public static final String LOGTPYE = "logtype";//记录类型 1:开机时间 、2：在线时间

    //创建表
    public static final String CREATE_TABLE = "CREATE TABLE if not exists "
            + TABLE_NAME + " ("
            + ID + " INTEGER PRIMARY KEY, "
            + SIGNID + " TEXT, "
//            + NAME + " TEXT, "
            + LOGTIME + " TEXT, "
            + LOGTPYE + " TEXT);";

    private static final String UNIQUE_INDEX_NAME = TABLE_NAME + "_unique_index_pid";//唯一索引名称

    //创建SIGNID字段的唯一索引
    public static final String CREATE_UNIQUE_INDEX = "CREATE UNIQUE INDEX "
            + UNIQUE_INDEX_NAME + " ON "
            + TABLE_NAME + " ("
            + SIGNID + ");";

    public static final java.lang.String DELETE_UNIQUE_INDEX = "drop index "+UNIQUE_INDEX_NAME;
}
