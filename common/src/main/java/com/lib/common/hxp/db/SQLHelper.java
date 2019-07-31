package com.lib.common.hxp.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

public class SQLHelper extends SQLiteOpenHelper {

//    private final String DATABASE_PATH = android.os.Environment
//            .getExternalStorageDirectory().getAbsolutePath() + "/vote";
    private String DATABASE_FILENAME = "data.sqlite";
    private Context mContext;

    private static String DATABASE_PATH =  getSDPath() + "/Download/" + File.separator ;

    private static String DB_NAME = "myDBName";

    public SQLHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, 3);
        mContext=context;
    }


    //获取跟目录
    public static String getSDPath() {
        File sdDir = Environment.getExternalStorageDirectory();
        return sdDir.toString();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//        //版本1
        db.execSQL(CartSQL.CREATE_TABLE);//创建购物车表
        db.execSQL(CartSQL.CREATE_UNIQUE_INDEX);//创建购物车表唯一索引
        db.execSQL(CollectSQL.CREATE_TABLE);//创建收藏表
        db.execSQL(CollectSQL.CREATE_UNIQUE_INDEX);//创建收藏表唯一索引
        db.execSQL(LogisticSQL.CREATE_TABLE);//创建物流表
        db.execSQL(LogisticSQL.CREATE_UNIQUE_INDEX);//创建物流表唯一索引
        db.execSQL(ProgrammeSQL.CREATE_TABLE);//创建方案表
        db.execSQL(ProgrammeSQL.CREATE_UNIQUE_INDEX);//创建方案表唯一索引
        db.execSQL(SetPriceSQL.CREATE_TABLE);//创建方案表
        db.execSQL(SetPriceSQL.CREATE_UNIQUE_INDEX);//创建方案表唯一索引
        db.execSQL(UploadImageSQL.CREATE_TABLE);//创建方案表
        db.execSQL(UploadImageSQL.CREATE_UNIQUE_INDEX);//创建方案表
        db.execSQL(TimeLogSQL.CREATE_TABLE);//创建时间记录表唯一索引
//        db.execSQL(TimeLogSQL.CREATE_UNIQUE_INDEX);//创建时间记录表唯一索引

        openDatabase(db);
    }


    private SQLiteDatabase openDatabase(SQLiteDatabase db) {
        try {
            String databaseFilename = DATABASE_PATH  + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists())
                dir.mkdir();
            db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
            return db;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 当系统在构造SQLiteOpenHelper类的对象时，如果发现版本号不一样，就会自动调用onUpgrade函数，让你在这里对数据库进行升级。
     * 升级完成后，数据库会自动存储最新的版本号为当前数据库版本号。
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                //版本2
                db.execSQL(TimeLogSQL.CREATE_TABLE);//创建时间记录表
                db.execSQL(TimeLogSQL.CREATE_UNIQUE_INDEX);//创建时间记录表唯一索引
            case 2:
                db.execSQL(TimeLogSQL.CREATE_TABLE);//创建时间记录表
                db.execSQL(TimeLogSQL.DELETE_UNIQUE_INDEX);
        }
    }
}
