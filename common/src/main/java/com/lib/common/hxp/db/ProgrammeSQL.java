package com.lib.common.hxp.db;

/**
 * Created by xpHuang on 2016/9/29.
 */
public class ProgrammeSQL {
    public static final String TABLE_NAME = "t_programme";
    public static final String ID = "id";//主键
    public static final String PID = "pid";//方案的ID
    public static final String GOODSID = "goods_id";//方案的产品ID
    public static final String TITLE = "title";//方案的标题
    public static final String SHAREID = "shareid";//方案分享id
    public static final String STYLE = "style";//方案的类型
    public static final String SPACE = "space";//方案的控件
    public static final String CONTENT = "content";//方案的备注
    public static final String PIMAGE = "pimage";//方案的备注
    public static final String SCEENPATH= "sceenpath";//方案的背景地址

    //创建表
    public static final String CREATE_TABLE = "CREATE TABLE if not exists "
            + TABLE_NAME + " ("
            + ID + " INTEGER PRIMARY KEY, "
            + PID + " INTEGER, "
            + GOODSID + " TEXT, "
            + TITLE + " TEXT, "
            + SHAREID + " TEXT, "
            + STYLE + " TEXT, "
            + SPACE + " TEXT, "
            + SCEENPATH + " TEXT, "
            + PIMAGE + " BLOB, "
            + CONTENT + " TEXT);";

    private static final String UNIQUE_INDEX_NAME = TABLE_NAME + "_unique_index_pid";//唯一索引名称

    //创建PID字段的唯一索引
    public static final String CREATE_UNIQUE_INDEX = "CREATE UNIQUE INDEX "
            + UNIQUE_INDEX_NAME + " ON "
            + TABLE_NAME + " ("
            + PID + ");";

}
